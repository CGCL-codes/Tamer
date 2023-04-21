package com.ununbium.LoadGen;

import java.io.*;
import java.util.jar.*;
import java.util.*;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;

public abstract class ScriptParameter {

    public String script;

    private static final String paramFileName = "parameter.xml";

    public int fileID = 0;

    public File jarFile;

    private InputStream is = null;

    public Hashtable<String, TestParameter> testParams = null;

    private static final Logger logger = Logger.getLogger(ScriptParameter.class);

    public ScriptParameter() {
    }

    public boolean hasParameterFile() {
        if (is != null) return true;
        JarFile temp = null;
        try {
            temp = new JarFile(jarFile);
        } catch (Exception e) {
            logger.error("Error: opening jar file failed: " + e);
            return false;
        }
        JarEntry file = temp.getJarEntry(paramFileName);
        if (file == null) {
            logger.debug("Could not find parameter file in jarfile");
            return false;
        }
        try {
            is = temp.getInputStream(file);
            if (is == null) {
                logger.warn("InputStream is null?");
            } else {
                logger.info("Parameter File Found and InputStream Set");
            }
        } catch (Exception e) {
            logger.error("Error: processing param file: " + file.getName() + " exception: " + e);
            return false;
        }
        return true;
    }

    public void processParamFile() {
        Digester d = new Digester();
        d.push(this);
        d.addCallMethod("ScriptParameters/Parameter", "processDataFile", 4);
        d.addCallParam("ScriptParameters/Parameter/Name", 0);
        d.addCallParam("ScriptParameters/Parameter/File", 1);
        d.addCallParam("ScriptParameters/Parameter/Delimiter", 2);
        d.addCallParam("ScriptParameters/Parameter/NextMethod", 3);
        try {
            d.parse(is);
        } catch (Exception e) {
            logger.error("Error: parsing parameter file failed! file: " + script + ".parameter exception: " + e);
            return;
        }
        logger.info("Parsed Parameter file for script: " + script);
    }

    public void processDataFile(String paramName, String fileName, String delim, String nextMethod) {
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
        } catch (Exception e) {
            logger.error("Error: Opening jar file for script: " + script + " exception: " + e);
            return;
        }
        Enumeration en = jar.entries();
        BufferedReader is = null;
        while (en.hasMoreElements()) {
            JarEntry file = (JarEntry) en.nextElement();
            if (file.getName().endsWith(fileName)) {
                try {
                    if (file.getSize() == 0) {
                        logger.warn("Is the datafile empty? file: " + file.getName());
                        return;
                    }
                    is = new BufferedReader(new InputStreamReader(jar.getInputStream(file)));
                } catch (IOException e) {
                    logger.error("Error: could not open data file for reading!  file: " + file.getName() + " exception: " + e);
                    return;
                }
                break;
            }
        }
        if (is == null) {
            logger.warn("Warning: Parameter data file not found! filename: " + fileName);
            return;
        }
        String header = null;
        try {
            header = is.readLine();
            if (header == null || header.length() == 0) {
                logger.error("Header is empty, this datafile is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("Error: could not read header line of datafile exception: " + e);
            return;
        }
        StringTokenizer headTok = new StringTokenizer(header, delim, true);
        String buff = null;
        int column = -1;
        while (headTok.hasMoreTokens()) {
            try {
                column++;
                buff = headTok.nextToken();
                if (buff.equals(paramName)) {
                    break;
                }
            } catch (Exception e) {
                logger.warn("Warning: Error parsing data file: " + fileName + " for parameter: " + paramName);
                return;
            }
        }
        Vector data = new Vector();
        try {
            while (is.ready()) {
                buff = is.readLine();
                StringTokenizer tok = new StringTokenizer(buff, delim, true);
                for (int i = 0; i < column; i++) {
                    tok.nextToken();
                }
                data.addElement(tok.nextToken());
            }
        } catch (NoSuchElementException ignored) {
        } catch (Exception e) {
            logger.warn("Exception while reading data file: " + e, e);
        }
        addParameter(paramName, data, nextMethod);
    }

    public abstract void addParameter(String name, Vector data, String method);
}
