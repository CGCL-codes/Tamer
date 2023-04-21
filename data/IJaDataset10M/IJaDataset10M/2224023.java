package de.fzi.wikipipes.impl.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class TestData {

    public static Reader getExampleWif() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream in = cl.getResourceAsStream("Example.wif.xml");
        assert in != null;
        return new InputStreamReader(in);
    }
}
