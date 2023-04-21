package com.mainatom.utils;

import junit.framework.*;
import java.io.*;
import java.net.*;

public class ZipUtilsTest extends TestCase {

    public String fnload(String resourcename) {
        URL u = getClass().getResource(resourcename);
        if (u == null) {
            throw new RuntimeException(String.format("Resource [%s] not found for class [%s]", resourcename, getClass().getName()));
        }
        return u.getFile();
    }

    public void testExtractTo() throws Exception {
        FileInputStream f = new FileInputStream(fnload("data/zip1.zip"));
        File out = new File("temp/zip1");
        JpFileUtils.deleteDirectory(out);
        out.mkdirs();
        ZipUtils.extractTo(f, out);
    }
}
