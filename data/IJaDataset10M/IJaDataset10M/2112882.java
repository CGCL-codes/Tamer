package es.seat131.viewerfree.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import es.seat131.viewerfree.dto.UserDto;

public class AlbumManagerTest {

    private static final String ALBUM2 = "album2";

    private static final String ALBUM = "album";

    private static final String USER = "admin";

    private static final String ALBUM_PATH = "./images";

    private static final String PICTURE = "antz.jpg";

    private UserDto _userDto;

    private static FilenameFilter _filenameFilter = new FileFilter(".jpg", ".gif,.svn", false);

    private static FilenameFilter _dirfilenameFilter = new FileFilter("", ".svn", true);

    @Before
    public void setUp() throws Exception {
        _userDto = new UserDto(USER, "pass");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void testGetAlbums() {
        FileMock fileMock = new FileMock();
        AlbumManagerMock.setFileMock(fileMock);
        String[] files = new String[] { ALBUM, ALBUM2 };
        fileMock._listReturn = files;
        assertArrayEquals(files, AlbumManagerMock.getInstance().getAlbums(_userDto));
        assertEquals(_dirfilenameFilter, fileMock._filenameFilterValue);
        assertTrue(fileMock._listCalled);
        assertEquals(ALBUM_PATH + "/" + USER, AlbumManagerMock._file);
    }

    @Test
    @Ignore
    public void testGetFotos() {
        FileMock fileMock = new FileMock();
        AlbumManagerMock.setFileMock(fileMock);
        String[] files = new String[] { PICTURE };
        fileMock._listReturn = files;
        assertArrayEquals(files, AlbumManagerMock.getInstance().getPictures(_userDto, ALBUM));
        assertEquals(_filenameFilter, fileMock._filenameFilterValue);
        assertTrue(fileMock._listCalled);
        assertEquals(ALBUM_PATH + "/" + USER + "/" + ALBUM, AlbumManagerMock._file);
    }

    @Test
    @Ignore
    public void testGetFotoStringString() throws IOException {
        FileMock fileMock = new FileMock();
        fileMock._lengthReturn = 10;
        AlbumManagerMock.setFileMock(fileMock);
        assertEquals(59045, AlbumManagerMock.getInstance().getPicture(_userDto, ALBUM, PICTURE).length);
    }

    @Test
    @Ignore
    public void testGetFotoStringStringOutputStream() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AlbumManagerMock.getInstance().getPicture(_userDto, ALBUM, PICTURE, outputStream);
        assertTrue(outputStream.size() >= 59045);
    }

    @Test
    @Ignore
    public void testGetPreviewFoto() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AlbumManagerMock.getInstance().getPicturePreview(_userDto, ALBUM, PICTURE, outputStream);
    }

    @Test
    @Ignore
    public void testGetPreviewFotoCacheada() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AlbumManagerMock.getInstance().getPreviewCachedPicture(_userDto, ALBUM, PICTURE, outputStream);
    }

    @Test
    public void testFilter() throws Exception {
        FileFilter fileFilter = new FileFilter("includes", "excludes", false);
        assertFalse(fileFilter.accept(null, "dsasdadsfasdfas_excludes"));
        assertTrue(fileFilter.accept(null, "dsasdadsfasdfas_includes"));
    }

    @Test
    public void testFilter2() throws Exception {
        System.err.println(URLEncoder.encode("/Main?ACTION=VER_ALBUM&ALBUM_NAME=album", "utf-8"));
    }

    @Test
    public void testPrueba() throws Exception {
        String pp = "ftp.casa";
        String tt = "lala.pepe";
        String ll = "lala.lala";
        String dd = "lalalala";
        Pattern pattern = Pattern.compile("^.*(\\.casa|\\.pepe)");
        Matcher matcher = pattern.matcher(pp);
        Matcher matcher2 = pattern.matcher(tt);
        Matcher matcher3 = pattern.matcher(ll);
        Matcher matcher4 = pattern.matcher(dd);
        assertTrue(matcher.matches());
        assertTrue(matcher2.matches());
        assertFalse(matcher3.matches());
        assertFalse(matcher4.matches());
    }

    private static class AlbumManagerMock extends AlbumManager {

        private static AlbumManager _albumManager = new AlbumManagerMock();

        private static FileMock _fileMock;

        public static String _file;

        public static File _foto;

        @Override
        File getFile(String file) {
            _file = file;
            return _fileMock;
        }

        public static IAlbumManager getInstance() {
            _albumManager.setAlbumPath(ALBUM_PATH);
            _albumManager.setPreviewPath("./preview");
            return _albumManager;
        }

        public static void setFileMock(FileMock fileMock) {
            _fileMock = fileMock;
        }
    }

    private static class FileMock extends File {

        public boolean _listCalled;

        public String[] _listReturn;

        public FilenameFilter _filenameFilterValue;

        public long _lengthReturn;

        public FileMock() {
            super("");
        }

        @Override
        public String[] list(FilenameFilter filter) {
            _listCalled = true;
            _filenameFilterValue = filter;
            return _listReturn;
        }

        @Override
        public long length() {
            return _lengthReturn;
        }
    }
}
