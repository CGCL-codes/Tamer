package com.jme.util.export.xml;

import com.jme.util.export.JMEExporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Part of the jME XML IO system as introduced in the google code jmexml project.
 * 
 * @author Kai Rabien (hevee) - original author of the code.google.com jmexml project
 * @author Doug Daniels (dougnukem) - adjustments for jME 2.0 and Java 1.5
 */
public class XMLExporter implements JMEExporter {

    public static final String ELEMENT_MAPENTRY = "MapEntry";

    public static final String ELEMENT_KEY = "Key";

    public static final String ELEMENT_VALUE = "Value";

    public static final String ELEMENT_FLOATBUFFER = "FloatBuffer";

    public static final String ATTRIBUTE_SIZE = "size";

    private DOMOutputCapsule domOut;

    public XMLExporter() {
    }

    public boolean save(Savable object, OutputStream f) throws IOException {
        try {
            this.domOut = new DOMOutputCapsule(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument(), this);
            domOut.write(object, object.getClass().getName(), null);
            DOMSerializer serializer = new DOMSerializer();
            serializer.serialize(domOut.getDoc(), f);
            f.flush();
            return true;
        } catch (Exception ex) {
            IOException e = new IOException();
            e.initCause(ex);
            throw e;
        }
    }

    public boolean save(Savable object, File f) throws IOException {
        return save(object, new FileOutputStream(f));
    }

    public OutputCapsule getCapsule(Savable object) {
        return domOut;
    }

    public static XMLExporter getInstance() {
        return new XMLExporter();
    }
}
