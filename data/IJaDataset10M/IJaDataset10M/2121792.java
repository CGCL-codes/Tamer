package remote.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.xml.ws.soap.SOAPFaultException;
import junit.framework.JUnit4TestAdapter;
import org.junit.Assert;
import org.junit.Test;
import remote.RemoteFile;
import remote.RemoteFileInputStream;
import remote.RemoteFileOutputStream;

public class RemoteFileTest {

    private RemoteFileInputStream _iStream;

    private RemoteFileOutputStream _oStream;

    private String _testFilePath1 = "/hello.txt";

    private String _username = "Renegade";

    private String _testString1 = "Hello! Ez egy teszt f�jl...";

    @Test
    public void testRootDirectory() {
        RemoteFile[] roots = RemoteFile.listRoots();
        Assert.assertNotNull(roots[0]);
        Assert.assertTrue(new RemoteFile(RemoteFile.separator, _username).exists());
    }

    @Test
    public void testStreams() throws Exception {
        try {
            _oStream = new RemoteFileOutputStream(_testFilePath1, _username);
            PrintWriter writer = new PrintWriter(_oStream);
            writer.println(_testString1);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        _iStream = new RemoteFileInputStream(_testFilePath1, _username);
        BufferedReader reader = new BufferedReader(new InputStreamReader(_iStream));
        String feedBack = reader.readLine();
        reader.close();
        Assert.assertEquals("Nem egyeznek!", _testString1, feedBack);
    }

    @Test
    public void testFile() throws Exception {
        RemoteFile rootDir = new RemoteFile(RemoteFile.separator, _username);
        Assert.assertTrue("A gyökérnek könyvtárnak kellene lennie!", rootDir.isDirectory());
        Assert.assertFalse("A gyökér nem lehet fájl és könyvtár egyszerre!", rootDir.isFile());
    }

    @Test(expected = SOAPFaultException.class)
    public void testCreateNonCreatableDir() {
        RemoteFile dir1 = new RemoteFile("/nincs/ilyen/dir", _username);
        dir1.mkdir();
    }

    @Test(expected = SOAPFaultException.class)
    public void testCreateNonCreatableFile() throws Exception {
        RemoteFile file1 = new RemoteFile("/nincs/ilyen/file", _username);
        file1.createNewFile();
    }

    @Test
    public void testCreateAndDelete() throws Exception {
        RemoteFile dir = new RemoteFile("/dir1", _username);
        RemoteFile file = new RemoteFile("/dir1/index.html", _username);
        Assert.assertTrue(dir.mkdir());
        Assert.assertTrue(file.createNewFile());
        File[] files = dir.listFiles();
        Assert.assertTrue(files.length > 0);
        Assert.assertTrue(file.delete());
        Assert.assertTrue(dir.delete());
    }

    @Test
    public void testMkDirs() {
        RemoteFile dir = new RemoteFile("/test/a/full/path/to/create", _username);
        Assert.assertTrue("Nem lehetett létrehozni a könyvtárszerkezetet", dir.mkdirs());
    }

    @Test
    public void testDeleteDirHierarchy() {
        RemoteFile dir;
        dir = new RemoteFile("/test/a/full/path/to/create", _username);
        Assert.assertTrue(dir.delete());
        dir = new RemoteFile("/test/a/full/path/to", _username);
        Assert.assertTrue(dir.delete());
        dir = new RemoteFile("/test/a/full/path", _username);
        Assert.assertTrue(dir.delete());
        dir = new RemoteFile("/test/a/full", _username);
        Assert.assertTrue(dir.delete());
        dir = new RemoteFile("/test/a", _username);
        Assert.assertTrue(dir.delete());
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(RemoteFileTest.class);
    }

    public static void main(String[] args) throws Exception {
        new RemoteFileTest().testStreams();
    }
}
