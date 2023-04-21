package com.l2jserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Noctarius
 */
public final class L2Properties extends Properties {

    private static final long serialVersionUID = 1L;

    private static Logger _log = Logger.getLogger(L2Properties.class.getName());

    public L2Properties() {
    }

    public L2Properties(String name) throws IOException {
        load(new FileInputStream(name));
    }

    public L2Properties(File file) throws IOException {
        load(new FileInputStream(file));
    }

    public L2Properties(InputStream inStream) throws IOException {
        load(inStream);
    }

    public L2Properties(Reader reader) throws IOException {
        load(reader);
    }

    public void load(String name) throws IOException {
        load(new FileInputStream(name));
    }

    public void load(File file) throws IOException {
        load(new FileInputStream(file));
    }

    @Override
    public void load(InputStream inStream) throws IOException {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(inStream, Charset.defaultCharset());
            super.load(reader);
        } finally {
            inStream.close();
            if (reader != null) reader.close();
        }
    }

    @Override
    public void load(Reader reader) throws IOException {
        try {
            super.load(reader);
        } finally {
            reader.close();
        }
    }

    @Override
    public String getProperty(String key) {
        String property = super.getProperty(key);
        if (property == null) {
            _log.info("L2Properties: Missing property for key - " + key);
            return null;
        }
        return property.trim();
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String property = super.getProperty(key, defaultValue);
        if (property == null) {
            _log.warning("L2Properties: Missing defaultValue for key - " + key);
            return null;
        }
        return property.trim();
    }
}
