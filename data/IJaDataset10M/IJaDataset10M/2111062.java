package org.jtorrent.torrent;

import junit.framework.TestCase;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Ryan Thomas
 * r.n.thomas@gmail.com
 *
 * @author ryan
 * @version $Revision$ $Author$ $Date$
 * @since Jun 20, 2009
 */
public class TestTorrentFileReader extends TestCase {

    private static final List<String> TEST_FILES = new ArrayList<String>();

    private File tmpFile;

    @Override
    protected void setUp() throws Exception {
        TEST_FILES.add("testdata/multi.torrent");
        TEST_FILES.add("testdata/single.torrent");
        TEST_FILES.add("testdata/infotest.torrent");
        tmpFile = File.createTempFile("tmp", "torrent");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (tmpFile != null) {
            tmpFile.delete();
        }
    }

    public void testRead() throws InvalidTorrentFileException, IOException {
        for (String file : TEST_FILES) {
            new TorrentFileReader(new File(file)).read();
        }
    }

    private void writeTestData(String data) throws IOException {
        FileOutputStream fos = new FileOutputStream(tmpFile);
        fos.write(data.getBytes());
        fos.flush();
        fos.close();
    }

    public void testInvalidFile() throws IOException {
        String invalidFile = "a8:announce12:ANNOUNCE_URLe";
        writeTestData(invalidFile);
        TorrentFileReader reader = new TorrentFileReader(tmpFile);
        try {
            reader.read();
        } catch (InvalidTorrentFileException e) {
            return;
        }
        fail("Failed to catch InvalidTorrentFileException");
    }

    public void testSingleFileNoMd5() throws InvalidTorrentFileException, IOException {
        String singleFile = "d8:announce12:ANNOUNCE_URL7:comment17:This is a comment13:creation datei1234567890e4:infod6:lengthi678172672e4:name24:debian-501-i386-CD-1.iso12:piece lengthi524288e6:pieces40:1234567890123456789012345678901234567890e7:numbersli12345eee";
        writeTestData(singleFile);
        TorrentFileReader reader = new TorrentFileReader(tmpFile);
        TorrentData data = reader.read();
        assertTrue("isMultiFile", !data.isMultiFile());
        assertEquals("Announce URL", "ANNOUNCE_URL", data.getAnnounceUrl());
        assertEquals("Comment", "This is a comment", data.getComment());
        assertEquals("Creation Date", 1234567890000l, data.getCreationDate());
        assertEquals("File Length", 678172672, data.getLength());
        assertEquals("File Length", 678172672, data.getFiles().iterator().next().getLength());
        assertEquals("File name", "debian-501-i386-CD-1.iso", data.getName());
        assertEquals("File name", "debian-501-i386-CD-1.iso", data.getFiles().iterator().next().getPath());
        assertEquals("Piece Length", 524288, data.getPieceLength());
        assertEquals("Pieces", 2, data.getPieces().length);
        assertEquals("MD5Sum", null, data.getMd5sum());
        assertEquals("MD5Sum", null, data.getFiles().iterator().next().getMd5sum());
        assertEquals("Info Packet", "d6:lengthi678172672e4:name24:debian-501-i386-CD-1.iso12:piece lengthi524288e6:pieces40:1234567890123456789012345678901234567890e", new String(data.getInfoPacket()));
        data.toString();
    }

    public void testSingleFileMd5() throws InvalidTorrentFileException, IOException {
        String singleFile = "d8:announce12:ANNOUNCE_URL7:comment17:This is a comment13:creation datei1234567890e4:infod6:lengthi678172672e4:name24:debian-501-i386-CD-1.iso6:md5sum20:ABCDEFABCDEFABCDEFAB12:piece lengthi524288e6:pieces40:1234567890123456789012345678901234567890ee";
        writeTestData(singleFile);
        TorrentFileReader reader = new TorrentFileReader(tmpFile);
        TorrentData data = reader.read();
        assertTrue("isMultiFile", !data.isMultiFile());
        assertEquals("Announce URL", "ANNOUNCE_URL", data.getAnnounceUrl());
        assertEquals("Comment", "This is a comment", data.getComment());
        assertEquals("Creation Date", 1234567890000l, data.getCreationDate());
        assertEquals("File Length", 678172672, data.getLength());
        assertEquals("File Length", 678172672, data.getFiles().iterator().next().getLength());
        assertEquals("File name", "debian-501-i386-CD-1.iso", data.getName());
        assertEquals("File name", "debian-501-i386-CD-1.iso", data.getFiles().iterator().next().getPath());
        assertEquals("Piece Length", 524288, data.getPieceLength());
        assertEquals("Pieces", 2, data.getPieces().length);
        assertEquals("MD5Sum", "ABCDEFABCDEFABCDEFAB", data.getMd5sum());
        assertEquals("MD5Sum", "ABCDEFABCDEFABCDEFAB", data.getFiles().iterator().next().getMd5sum());
        assertEquals("Info Packet", "d6:lengthi678172672e4:name24:debian-501-i386-CD-1.iso6:md5sum20:ABCDEFABCDEFABCDEFAB12:piece lengthi524288e6:pieces40:1234567890123456789012345678901234567890e", new String(data.getInfoPacket()));
        data.toString();
    }

    public void testMultiFile() throws InvalidTorrentFileException, IOException {
        String multiFile = "d" + "8:announce12:ANNOUNCE_URL" + "7:comment17:This is a comment" + "13:creation datei1234567890e" + "4:info" + "d4:name10:SOMEFOLDER12:piece lengthi12345678e6:pieces40:1234567890123456789012345678901234567890" + "5:files" + "l" + "d" + "6:lengthi123456e" + "4:path" + "l4:dir14:dir24:dir38:file.ext" + "e" + "e" + "d" + "6:lengthi234567e" + "4:pathl4:pir14:pir24:pir38:pile.exte" + "6:md5sum20:ABCDEFABCDEFABCDEFAB" + "e" + "e" + "e" + "e";
        writeTestData(multiFile);
        TorrentFileReader reader = new TorrentFileReader(tmpFile);
        TorrentData data = reader.read();
        assertTrue("isMultiFile", data.isMultiFile());
        assertEquals("Nil Length", -1, data.getLength());
        assertEquals("Announce URL", "ANNOUNCE_URL", data.getAnnounceUrl());
        assertEquals("Comment", "This is a comment", data.getComment());
        assertEquals("Creation Date", 1234567890000l, data.getCreationDate());
        assertEquals("Folder Name", "SOMEFOLDER", data.getName());
        assertEquals("Piece Length", 12345678, data.getPieceLength());
        assertEquals("Piece Count", 2, data.getPieces().length);
        assertEquals("File Count", 2, data.getFiles().size());
        assertEquals("File 1 length", 123456, data.getFiles().get(0).getLength());
        assertEquals("File 1 path", ":dir1:dir2:dir3:file.ext".replaceAll(":", File.separator), data.getFiles().get(0).getPath());
        assertEquals("File 1 md5", null, data.getFiles().get(0).getMd5sum());
        assertEquals("File 2 length", 234567, data.getFiles().get(1).getLength());
        assertEquals("File 1 path", ":pir1:pir2:pir3:pile.ext".replaceAll(":", File.separator), data.getFiles().get(1).getPath());
        assertEquals("File 1 md5", "ABCDEFABCDEFABCDEFAB", data.getFiles().get(1).getMd5sum());
        assertEquals("Info Packet", "d4:name10:SOMEFOLDER12:piece lengthi12345678e6:pieces40:12345678901234567890123456789012345678905:filesld6:lengthi123456e4:pathl4:dir14:dir24:dir38:file.exteed6:lengthi234567e4:pathl4:pir14:pir24:pir38:pile.exte6:md5sum20:ABCDEFABCDEFABCDEFABeee", new String(data.getInfoPacket()));
        data.toString();
    }
}
