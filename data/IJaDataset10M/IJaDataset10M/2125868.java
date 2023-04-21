package tests.projectLocationTests;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sprutframework.UserProjectLocation;
import tests.annotations.Description;
import java.io.File;

public class UserProjectLocationTest {

    private File jar;

    private File war;

    private File zip;

    private File dir;

    @Before
    public void init() {
        jar = new File("src/tests/tests/projectLocationTests/testJar.jar");
        war = new File("src/tests/tests/projectLocationTests/testWar.war");
        zip = new File("src/tests/tests/projectLocationTests/testZip.zip");
        dir = new File("src/tests/tests/projectLocationTests");
    }

    @Test
    public void jarFileExist() {
        Assert.assertTrue(jar.exists());
    }

    @Test
    public void warFileExist() {
        Assert.assertTrue(war.exists());
    }

    @Test
    public void zipFileExist() {
        Assert.assertTrue(zip.exists());
    }

    @Test
    public void directoryExist() {
        Assert.assertTrue(dir.exists());
    }

    @Test
    @Description("Testing object with archive file testJar.jar")
    public void test1_isArchive() {
        UserProjectLocation projLoc = new UserProjectLocation(jar);
        Assert.assertTrue(projLoc.isArchive());
    }

    @Test
    @Description("Testing object with archive file testWar.war")
    public void test2_isArchive() {
        UserProjectLocation projLoc = new UserProjectLocation(war);
        Assert.assertTrue(projLoc.isArchive());
    }

    @Test
    @Description("Testing object with archive file testJar.jar; Project location is not a directory")
    public void test_isNotDirectory() {
        UserProjectLocation projLoc = new UserProjectLocation(jar);
        Assert.assertFalse(projLoc.isDirectory());
    }

    @Test
    @Description("Testing object with directory; Project location is a directory")
    public void test1_isDirectory() {
        UserProjectLocation projLoc = new UserProjectLocation(dir);
        Assert.assertTrue(projLoc.isDirectory());
    }

    @Test
    @Description("Testing object with archive zip file testZip; Project location is not an archive")
    public void test_isNotArchive() {
        UserProjectLocation projLoc = new UserProjectLocation(zip);
        Assert.assertFalse(projLoc.isArchive());
    }

    @Test
    @Description("Testing object with archive zip file testZip; Project location is not a directory")
    public void test2_isNotDirectory() {
        UserProjectLocation projLoc = new UserProjectLocation(zip);
        Assert.assertFalse(projLoc.isDirectory());
    }
}
