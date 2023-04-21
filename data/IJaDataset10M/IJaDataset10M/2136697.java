package com.hadeslee.audiotag.audio.flac;

import com.hadeslee.audiotag.tag.vorbiscomment.VorbisCommentCreator;
import com.hadeslee.audiotag.tag.Tag;
import com.hadeslee.audiotag.tag.flac.FlacTag;
import com.hadeslee.audiotag.audio.flac.metadatablock.MetadataBlockHeader;
import com.hadeslee.audiotag.audio.flac.metadatablock.BlockType;
import com.hadeslee.audiotag.audio.flac.metadatablock.MetadataBlockDataPadding;
import com.hadeslee.audiotag.audio.flac.metadatablock.MetadataBlockDataPicture;
import com.hadeslee.audiotag.audio.generic.AbstractTagCreator;
import java.io.UnsupportedEncodingException;
import java.nio.*;
import java.util.logging.Logger;
import java.util.ListIterator;

/**
 * Create the tag data ready for writing to flac file
 */
public class FlacTagCreator extends AbstractTagCreator {

    public static Logger logger = Logger.getLogger("com.hadeslee.jaudiotagger.audio.flac");

    public static final int DEFAULT_PADDING = 4000;

    private static final VorbisCommentCreator creator = new VorbisCommentCreator();

    /**
     *
     * @param tag
     * @param paddingSize extra padding to be added
     * @return
     * @throws UnsupportedEncodingException
     */
    public ByteBuffer convert(Tag tag, int paddingSize) throws UnsupportedEncodingException {
        logger.info("Convert flac tag:padding:" + paddingSize);
        FlacTag flacTag = (FlacTag) tag;
        int tagLength = 0;
        ByteBuffer vorbiscomment = null;
        if (flacTag.getVorbisCommentTag() != null) {
            vorbiscomment = creator.convert(flacTag.getVorbisCommentTag());
            tagLength = vorbiscomment.capacity() + MetadataBlockHeader.HEADER_LENGTH;
        }
        for (MetadataBlockDataPicture image : flacTag.getImages()) {
            tagLength += image.getBytes().length + MetadataBlockHeader.HEADER_LENGTH;
        }
        logger.info("Convert flac tag:taglength:" + tagLength);
        ByteBuffer buf = ByteBuffer.allocate(tagLength + paddingSize);
        MetadataBlockHeader vorbisHeader;
        if (flacTag.getVorbisCommentTag() != null) {
            if ((paddingSize > 0) || (flacTag.getImages().size() > 0)) {
                vorbisHeader = new MetadataBlockHeader(false, BlockType.VORBIS_COMMENT, vorbiscomment.capacity());
            } else {
                vorbisHeader = new MetadataBlockHeader(true, BlockType.VORBIS_COMMENT, vorbiscomment.capacity());
            }
            buf.put(vorbisHeader.getBytes());
            buf.put(vorbiscomment);
        }
        ListIterator<MetadataBlockDataPicture> li = flacTag.getImages().listIterator();
        while (li.hasNext()) {
            MetadataBlockDataPicture imageField = li.next();
            MetadataBlockHeader imageHeader;
            if (paddingSize > 0 || li.hasNext()) {
                imageHeader = new MetadataBlockHeader(false, BlockType.PICTURE, imageField.getLength());
            } else {
                imageHeader = new MetadataBlockHeader(true, BlockType.PICTURE, imageField.getLength());
            }
            buf.put(imageHeader.getBytes());
            buf.put(imageField.getBytes());
        }
        logger.info("Convert flac tag at" + buf.position());
        if (paddingSize > 0) {
            int paddingDataSize = paddingSize - MetadataBlockHeader.HEADER_LENGTH;
            MetadataBlockHeader paddingHeader = new MetadataBlockHeader(true, BlockType.PADDING, paddingDataSize);
            MetadataBlockDataPadding padding = new MetadataBlockDataPadding(paddingDataSize);
            buf.put(paddingHeader.getBytes());
            buf.put(padding.getBytes());
        }
        buf.rewind();
        return buf;
    }
}
