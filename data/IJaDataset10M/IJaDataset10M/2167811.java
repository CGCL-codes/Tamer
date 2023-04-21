package com.tcmj.common.tools.net;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author tcmj
 */
public class DownloadTest {

    @Test
    public final void shouldDownloadAFileFromAnUrlToASpecificLocation() throws MalformedURLException, IOException {
        System.out.println("shouldDownloadAFileFromAnUrlToASpecificLocation");
        URL url = new URL("http://tcmj.googlecode.com/files/oisafetool.jar");
        File myfile = new File(System.getProperty("user.dir"), "myjarfile.jar");
        Download.aFile(url, myfile);
        checkFile(myfile);
        try {
            myfile.delete();
        } catch (Exception e) {
        }
    }

    @Test
    public final void shouldDownloadAFileFromAnUrlAsTempFile() throws MalformedURLException, IOException {
        System.out.println("shouldDownloadAFileFromAnUrlAsTempFile");
        URL url = new URL("http://tcmj.googlecode.com/files/oisafetool.jar");
        File downloadedFile = Download.aFile(url);
        checkFile(downloadedFile);
    }

    @Test
    public final void shouldDownloadAFileFromAnUrlWithParameters() throws MalformedURLException, IOException {
        System.out.println("shouldDownloadAFileFromAnUrlWithParameters");
        URL url = new URL("http://tcmj.googlecode.com/files/oisafetool.jar?user=max&name=mutzke");
        File downloadedFile = Download.aFile(url);
        checkFile(downloadedFile);
    }

    @Test
    public final void shouldDownloadAHtmlFileFromAnUrlWithParameters() throws MalformedURLException, IOException {
        System.out.println("shouldDownloadAFileFromAnUrlWithParameters");
        URL url = new URL("http://www.theserverside.com/discussions/thread.tss?thread_id=32379");
        File downloadedFile = Download.aFile(url);
        checkFile(downloadedFile);
    }

    /**
     * internal assertion method used by the tests
     */
    private static final void checkFile(File file) {
        assertThat("filehandle is null", file, notNullValue());
        assertThat("file does not exist", file.exists(), is(true));
        assertThat("filehandle doesn't point to a file", file.isFile(), is(true));
        assertThat("file size zero", file.length(), not(is(0L)));
        System.out.println("Downloaded File: " + file);
        System.out.println("Size: " + file.length() + " bytes");
    }
}
