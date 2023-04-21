package com.hadeslee.audiotag.audio.ogg;

import com.hadeslee.audiotag.audio.exceptions.CannotReadException;
import com.hadeslee.audiotag.audio.exceptions.CannotWriteException;
import com.hadeslee.audiotag.tag.vorbiscomment.VorbisCommentTag;
import com.hadeslee.audiotag.audio.ogg.OggVorbisTagReader;
import com.hadeslee.audiotag.audio.ogg.util.OggCRCFactory;
import com.hadeslee.audiotag.audio.ogg.util.OggPageHeader;
import com.hadeslee.audiotag.tag.Tag;
import java.io.*;
import java.nio.*;
import java.util.logging.Logger;

/**
 * Write Vorbis Tag within an ogg
 * <p/>
 * VorbisComment holds the tag information within an ogg file
 */
public class OggVorbisTagWriter {

    public static Logger logger = Logger.getLogger("com.hadeslee.jaudiotagger.audio.ogg");

    private OggVorbisCommentTagCreator tc = new OggVorbisCommentTagCreator();

    private OggVorbisTagReader reader = new OggVorbisTagReader();

    public void delete(RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException, CannotReadException, CannotWriteException {
        VorbisCommentTag tag = null;
        try {
            tag = (VorbisCommentTag) reader.read(raf);
        } catch (CannotReadException e) {
            write(new VorbisCommentTag(), raf, tempRaf);
            return;
        }
        VorbisCommentTag emptyTag = new VorbisCommentTag();
        emptyTag.setVendor(tag.getVendor());
        write(emptyTag, raf, tempRaf);
    }

    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException, CannotWriteException, IOException {
        logger.info("Starting to write file:");
        logger.fine("Read identificationHeader:");
        OggPageHeader pageHeader = OggPageHeader.read(raf);
        raf.seek(0);
        rafTemp.getChannel().transferFrom(raf.getChannel(), 0, pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        rafTemp.skipBytes(pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        logger.fine("Written identificationHeader:");
        logger.fine("Read 2ndpage:");
        OggPageHeader secondPageHeader = OggPageHeader.read(raf);
        long secondPageHeaderEndPos = raf.getFilePointer();
        raf.seek(0);
        OggVorbisTagReader.OggVorbisHeaderSizes vorbisHeaderSizes = reader.readOggVorbisHeaderSizes(raf);
        ByteBuffer newComment = tc.convert(tag);
        int newCommentLength = newComment.capacity();
        int setupHeaderLength = vorbisHeaderSizes.getSetupHeaderSize();
        int oldCommentLength = vorbisHeaderSizes.getCommentHeaderSize();
        int newSecondPageLength = setupHeaderLength + newCommentLength;
        logger.fine("Old Page size: " + secondPageHeader.getPageLength());
        logger.fine("Setup Header Size: " + setupHeaderLength);
        logger.fine("Old comment: " + oldCommentLength);
        logger.fine("New comment: " + newCommentLength);
        logger.fine("New Page Size: " + newSecondPageLength);
        if (isCommentAndSetupHeaderFitsOnASinglePage(newCommentLength, setupHeaderLength)) {
            if ((secondPageHeader.getPageLength() < OggPageHeader.MAXIMUM_PAGE_DATA_SIZE) && (secondPageHeader.getPacketList().size() == 2) && (!secondPageHeader.isLastPacketIncomplete())) {
                logger.info("Header and Setup remain on single page:");
                replaceSecondPageOnly(oldCommentLength, setupHeaderLength, newCommentLength, newSecondPageLength, secondPageHeader, newComment, secondPageHeaderEndPos, raf, rafTemp);
            } else {
                logger.info("Header and Setup now on single page:");
                replaceSecondPageAndRenumberPageSeqs(vorbisHeaderSizes, newCommentLength, newSecondPageLength, secondPageHeader, newComment, raf, rafTemp);
            }
        } else {
            logger.info("Header and Setup remain on multiple page:");
            replacePagesAndRenumberPageSeqs(vorbisHeaderSizes, newCommentLength, secondPageHeader, newComment, raf, rafTemp);
        }
    }

    /**
     * Usually can use this method, previously comment and setup header all fit on page 2
     * and they still do, so just replace this page. And copy further pages as is.
     *
     * @param setupHeaderLength
     * @param oldCommentLength
     * @param newCommentLength
     * @param newSecondPageLength
     * @param secondPageHeader
     * @param newComment
     * @param secondPageHeaderEndPos
     * @param raf
     * @param rafTemp
     * @throws IOException
     */
    private void replaceSecondPageOnly(int oldCommentLength, int setupHeaderLength, int newCommentLength, int newSecondPageLength, OggPageHeader secondPageHeader, ByteBuffer newComment, long secondPageHeaderEndPos, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException {
        byte[] segmentTable = createSegmentTable(newCommentLength, setupHeaderLength);
        int newSecondPageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);
        secondPageBuffer.order(ByteOrder.LITTLE_ENDIAN);
        secondPageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
        secondPageBuffer.put((byte) segmentTable.length);
        for (int i = 0; i < segmentTable.length; i++) {
            secondPageBuffer.put(segmentTable[i]);
        }
        secondPageBuffer.put(newComment);
        raf.seek(secondPageHeaderEndPos);
        raf.skipBytes(oldCommentLength);
        raf.getChannel().read(secondPageBuffer);
        secondPageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++) {
            secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
        }
        secondPageBuffer.rewind();
        rafTemp.getChannel().write(secondPageBuffer);
        rafTemp.getChannel().transferFrom(raf.getChannel(), rafTemp.getFilePointer(), raf.length() - raf.getFilePointer());
    }

    /**
     * Previously comment and/or setup header on a number of pages
     * now can just replace this page fitting al on 2nd page, and renumber subsequent sequence pages
     *
     * @param originalHeaderSizes
     * @param newCommentLength
     * @param newSecondPageLength
     * @param secondPageHeader
     * @param newComment
     * @param raf
     * @param rafTemp
     * @throws IOException
     */
    private void replaceSecondPageAndRenumberPageSeqs(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes, int newCommentLength, int newSecondPageLength, OggPageHeader secondPageHeader, ByteBuffer newComment, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException, CannotReadException, CannotWriteException {
        byte[] segmentTable = createSegmentTable(newCommentLength, originalHeaderSizes.getSetupHeaderSize());
        int newSecondPageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);
        secondPageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
        secondPageBuffer.order(ByteOrder.LITTLE_ENDIAN);
        secondPageBuffer.put((byte) segmentTable.length);
        for (int i = 0; i < segmentTable.length; i++) {
            secondPageBuffer.put(segmentTable[i]);
        }
        secondPageBuffer.put(newComment);
        int pageSequence = secondPageHeader.getPageSequence();
        byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
        logger.finest(setupHeaderData.length + ":" + secondPageBuffer.position() + ":" + secondPageBuffer.capacity());
        secondPageBuffer.put(setupHeaderData);
        secondPageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++) {
            secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
        }
        secondPageBuffer.rewind();
        rafTemp.getChannel().write(secondPageBuffer);
        long startAudio = raf.getFilePointer();
        logger.finest("About to read Audio starting from:" + startAudio);
        long startAudioWritten = rafTemp.getFilePointer();
        while (raf.getFilePointer() < raf.length()) {
            OggPageHeader nextPage = OggPageHeader.read(raf);
            ByteBuffer nextPageHeaderBuffer = ByteBuffer.allocate(nextPage.getRawHeaderData().length + nextPage.getPageLength());
            nextPageHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            nextPageHeaderBuffer.put(nextPage.getRawHeaderData());
            raf.getChannel().read(nextPageHeaderBuffer);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, ++pageSequence);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            crc = OggCRCFactory.computeCRC(nextPageHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                nextPageHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            nextPageHeaderBuffer.rewind();
            rafTemp.getChannel().write(nextPageHeaderBuffer);
        }
        if ((raf.length() - startAudio) != (rafTemp.length() - startAudioWritten)) {
            throw new CannotWriteException("File written counts dont match, file not written");
        }
    }

    /**
     * CommentHeader extends over multiple pages
     *
     * @param originalHeaderSizes
     * @param newCommentLength
     * @param secondPageHeader
     * @param newComment
     * @param raf
     * @param rafTemp
     *
     * @throws IOException
     * @throws CannotReadException
     * @throws CannotWriteException
     */
    private void replacePagesAndRenumberPageSeqs(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes, int newCommentLength, OggPageHeader secondPageHeader, ByteBuffer newComment, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException, CannotReadException, CannotWriteException {
        int pageSequence = secondPageHeader.getPageSequence();
        int noOfPagesNeededForComment = newCommentLength / OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        logger.info("Comment requires:" + noOfPagesNeededForComment + " complete pages");
        int newCommentOffset = 0;
        for (int i = 0; i < noOfPagesNeededForComment; i++) {
            byte[] segmentTable = this.createSegments(OggPageHeader.MAXIMUM_PAGE_DATA_SIZE, false);
            int pageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer pageBuffer = ByteBuffer.allocate(pageHeaderLength + OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
            pageBuffer.order(ByteOrder.LITTLE_ENDIAN);
            pageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            pageBuffer.put((byte) segmentTable.length);
            for (int j = 0; j < segmentTable.length; j++) {
                pageBuffer.put(segmentTable[j]);
            }
            pageBuffer.put(newComment.array(), newCommentOffset, OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
            pageBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            pageSequence++;
            if (i != 0) {
                pageBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            }
            pageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            byte[] crc = OggCRCFactory.computeCRC(pageBuffer.array());
            for (int j = 0; j < crc.length; j++) {
                pageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + j, crc[j]);
            }
            pageBuffer.rewind();
            rafTemp.getChannel().write(pageBuffer);
            newCommentOffset += OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        }
        int lastPageCommentPacketSize = newCommentLength % OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        logger.fine("Last comment packet size:" + lastPageCommentPacketSize);
        if (!isCommentAndSetupHeaderFitsOnASinglePage(lastPageCommentPacketSize, originalHeaderSizes.getSetupHeaderSize())) {
            logger.fine("Spread over two pages");
            byte[] commentSegmentTable = createSegments(lastPageCommentPacketSize, true);
            int remainingSegmentSlots = OggPageHeader.MAXIMUM_NO_OF_SEGMENT_SIZE - commentSegmentTable.length;
            int firstHalfOfHeaderSize = remainingSegmentSlots * OggPageHeader.MAXIMUM_SEGMENT_SIZE;
            byte[] firstHalfofSegmentHeaderTable = createSegments(firstHalfOfHeaderSize, false);
            byte[] segmentTable = createSegmentTable(lastPageCommentPacketSize, firstHalfOfHeaderSize);
            int lastCommentHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + commentSegmentTable.length + firstHalfofSegmentHeaderTable.length;
            ByteBuffer lastCommentHeaderBuffer = ByteBuffer.allocate(lastCommentHeaderLength + lastPageCommentPacketSize + firstHalfOfHeaderSize);
            lastCommentHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastCommentHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            lastCommentHeaderBuffer.put((byte) segmentTable.length);
            for (int i = 0; i < segmentTable.length; i++) {
                lastCommentHeaderBuffer.put(segmentTable[i]);
            }
            lastCommentHeaderBuffer.put(newComment.array(), newCommentOffset, lastPageCommentPacketSize);
            byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
            logger.finest(setupHeaderData.length + ":" + lastCommentHeaderBuffer.position() + ":" + lastCommentHeaderBuffer.capacity());
            int copyAmount = setupHeaderData.length;
            if (setupHeaderData.length > lastCommentHeaderBuffer.remaining()) {
                copyAmount = lastCommentHeaderBuffer.remaining();
                logger.finest("Copying :" + copyAmount);
            }
            lastCommentHeaderBuffer.put(setupHeaderData, 0, copyAmount);
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            lastCommentHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            pageSequence++;
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            byte[] crc = OggCRCFactory.computeCRC(lastCommentHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                lastCommentHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            lastCommentHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastCommentHeaderBuffer);
            int secondHalfOfHeaderSize = originalHeaderSizes.getSetupHeaderSize() - firstHalfOfHeaderSize;
            segmentTable = createSegmentTable(secondHalfOfHeaderSize, 0);
            int lastSetupHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer lastSetupHeaderBuffer = ByteBuffer.allocate(lastSetupHeaderLength + secondHalfOfHeaderSize);
            lastSetupHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastSetupHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            lastSetupHeaderBuffer.put((byte) segmentTable.length);
            for (int i = 0; i < segmentTable.length; i++) {
                lastSetupHeaderBuffer.put(segmentTable[i]);
            }
            logger.finest(setupHeaderData.length - copyAmount + ":" + lastSetupHeaderBuffer.position() + ":" + lastSetupHeaderBuffer.capacity());
            lastSetupHeaderBuffer.put(setupHeaderData, copyAmount, setupHeaderData.length - copyAmount);
            lastSetupHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            lastSetupHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            lastSetupHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            crc = OggCRCFactory.computeCRC(lastSetupHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                lastSetupHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            lastSetupHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastSetupHeaderBuffer);
        } else {
            logger.fine("Setupheader fits on comment page");
            byte[] segmentTable = createSegmentTable(lastPageCommentPacketSize, originalHeaderSizes.getSetupHeaderSize());
            int lastHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer lastCommentHeaderBuffer = ByteBuffer.allocate(lastHeaderLength + lastPageCommentPacketSize + originalHeaderSizes.getSetupHeaderSize());
            lastCommentHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastCommentHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            lastCommentHeaderBuffer.put((byte) segmentTable.length);
            for (int i = 0; i < segmentTable.length; i++) {
                lastCommentHeaderBuffer.put(segmentTable[i]);
            }
            lastCommentHeaderBuffer.put(newComment.array(), newCommentOffset, lastPageCommentPacketSize);
            raf.seek(originalHeaderSizes.getSetupHeaderStartPosition());
            OggPageHeader setupPageHeader;
            byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(), raf);
            lastCommentHeaderBuffer.put(setupHeaderData);
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, pageSequence);
            lastCommentHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS, OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            byte[] crc = OggCRCFactory.computeCRC(lastCommentHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                lastCommentHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            lastCommentHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastCommentHeaderBuffer);
        }
        long startAudio = raf.getFilePointer();
        long startAudioWritten = rafTemp.getFilePointer();
        logger.fine("Writing audio, audio starts in original file at :" + startAudio + ":Written to:" + startAudioWritten);
        while (raf.getFilePointer() < raf.length()) {
            logger.fine("Reading Ogg Page");
            OggPageHeader nextPage = OggPageHeader.read(raf);
            ByteBuffer nextPageHeaderBuffer = ByteBuffer.allocate(nextPage.getRawHeaderData().length + nextPage.getPageLength());
            nextPageHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);
            nextPageHeaderBuffer.put(nextPage.getRawHeaderData());
            raf.getChannel().read(nextPageHeaderBuffer);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS, ++pageSequence);
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);
            byte[] crc = OggCRCFactory.computeCRC(nextPageHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++) {
                nextPageHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }
            nextPageHeaderBuffer.rewind();
            rafTemp.getChannel().write(nextPageHeaderBuffer);
        }
        if ((raf.length() - startAudio) != (rafTemp.length() - startAudioWritten)) {
            throw new CannotWriteException("File written counts dont macth, file not written");
        }
    }

    /**
     * This method creates a new segment table for the second page (header).
     *
     * @param newCommentLength Of this value the start of the segment table
     *                         will be created.
     * @param setupHeaderLength The length of setup table, zero if comment String extends
     * over multiple pages and this is not the last page.
     * @return new segment table.
     */
    private byte[] createSegmentTable(int newCommentLength, int setupHeaderLength) {
        byte[] newStart;
        if (setupHeaderLength == 0) {
            newStart = createSegments(newCommentLength, false);
            return newStart;
        } else {
            newStart = createSegments(newCommentLength, true);
        }
        byte[] restShouldBe = createSegments(setupHeaderLength, false);
        byte[] result = new byte[newStart.length + restShouldBe.length];
        System.arraycopy(newStart, 0, result, 0, newStart.length);
        System.arraycopy(restShouldBe, 0, result, newStart.length, restShouldBe.length);
        return result;
    }

    /**
     * This method creates a byte array of values whose sum should
     * be the value of <code>length</code>.<br>
     *
     * @param length     Size of the page which should be
     *                   represented as 255 byte packets.
     * @param quitStream If true and a length is a multiple of 255 we need another
     *                   segment table entry with the value of 0. Else it's the last stream of the
     *                   table which is already ended.
     * @return Array of packet sizes. However only the last packet will
     *         differ from 255.
     *
     * TODO if pass is data of max length (65025 bytes) and have quitStream==true
     * this will return 256 segments which is illegal, should be checked somewhere
     */
    private byte[] createSegments(int length, boolean quitStream) {
        byte[] result = new byte[length / OggPageHeader.MAXIMUM_SEGMENT_SIZE + ((length % OggPageHeader.MAXIMUM_SEGMENT_SIZE == 0 && !quitStream) ? 0 : 1)];
        int i = 0;
        for (; i < result.length - 1; i++) {
            result[i] = (byte) 0xFF;
        }
        result[result.length - 1] = (byte) (length - (i * OggPageHeader.MAXIMUM_SEGMENT_SIZE));
        return result;
    }

    /**
     *
     * @param commentLength
     * @param setupHeaderLength
     * @return true if there is enough room to fit the comment and the setup headers on one page taking into
     * account the maximum no of segment s allowed per page and zero lacing values.
     */
    private boolean isCommentAndSetupHeaderFitsOnASinglePage(int commentLength, int setupHeaderLength) {
        int additionalZeroLacingRequiredForComment = 0;
        int additionalZeroLacingRequiredForHeader = 0;
        if ((commentLength % OggPageHeader.MAXIMUM_SEGMENT_SIZE == 0)) {
            additionalZeroLacingRequiredForComment = 1;
        }
        if (setupHeaderLength % OggPageHeader.MAXIMUM_SEGMENT_SIZE == 0) {
            additionalZeroLacingRequiredForHeader = 1;
        }
        if (((commentLength / OggPageHeader.MAXIMUM_SEGMENT_SIZE) + (setupHeaderLength / OggPageHeader.MAXIMUM_SEGMENT_SIZE) + additionalZeroLacingRequiredForComment + additionalZeroLacingRequiredForHeader) <= OggPageHeader.MAXIMUM_NO_OF_SEGMENT_SIZE) {
            return true;
        }
        return false;
    }
}
