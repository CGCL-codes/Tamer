package com.abra.j2xb;

import com.abra.j2xb.beans.xmlModel.MoXmlBindingModel;
import com.abra.j2xb.beans.xmlBinding.XmlPersister;
import com.abra.j2xb.beans.xmlBinding.XmlPersistException;
import java.io.*;

/**
 * @author Yoav Abrahami
 * @version 1.0, May 1, 2008
 * @since   JDK1.5
 */
public class MixedScenario {

    MoXmlBindingModel xmlBindingModel;

    XmlPersister xmlPersister1;

    XmlPersister xmlPersister2;

    Class persistedClass;

    String description;

    private String persisterDescription1;

    private String persisterDescription2;

    MixedScenario(MoXmlBindingModel xmlBindingModel, XmlPersister persister1, XmlPersister persister2, Class persistedClass, String description, String persisterDescription1, String persisterDescription2) {
        this.xmlBindingModel = xmlBindingModel;
        this.xmlPersister1 = persister1;
        this.xmlPersister2 = persister2;
        this.persistedClass = persistedClass;
        this.description = description;
        this.persisterDescription1 = persisterDescription1;
        this.persisterDescription2 = persisterDescription2;
    }

    public void execute(Object instance) throws IOException, XmlPersistException {
        System.out.println();
        System.out.println();
        System.out.println(String.format("persist %s using %s", description, persisterDescription1));
        FileOutputStream out = new FileOutputStream(String.format(".tmp/%s.%s.10.xml", description, persisterDescription1));
        try {
            xmlPersister1.save(xmlBindingModel, instance, out);
            xmlPersister1.save(xmlBindingModel, instance, System.out);
        } finally {
            out.flush();
            out.close();
        }
        System.out.println();
        System.out.println();
        System.out.println(String.format("read %s using %s", description, persisterDescription2));
        Object readInstance;
        FileInputStream in = new FileInputStream(String.format(".tmp/%s.%s.10.xml", description, persisterDescription1));
        try {
            readInstance = xmlPersister2.load(xmlBindingModel, persistedClass, in);
        } finally {
            in.close();
        }
        System.out.println();
        System.out.println();
        System.out.println(String.format("persist %s using %s", description, persisterDescription2));
        out = new FileOutputStream(String.format(".tmp/%s.%s.11.xml", description, persisterDescription2));
        try {
            xmlPersister2.save(xmlBindingModel, readInstance, out);
            xmlPersister2.save(xmlBindingModel, readInstance, System.out);
        } finally {
            out.flush();
            out.close();
        }
        System.out.println();
        System.out.println();
        System.out.println(String.format("read %s using %s", description, persisterDescription1));
        in = new FileInputStream(String.format(".tmp/%s.%s.11.xml", description, persisterDescription2));
        try {
            readInstance = xmlPersister1.load(xmlBindingModel, persistedClass, in);
        } finally {
            in.close();
        }
        System.out.println();
        System.out.println();
        System.out.println(String.format("persist %s using %s", description, persisterDescription1));
        out = new FileOutputStream(String.format(".tmp/%s.%s.12.xml", description, persisterDescription1));
        try {
            xmlPersister1.save(xmlBindingModel, instance, out);
            xmlPersister1.save(xmlBindingModel, instance, System.out);
        } finally {
            out.flush();
            out.close();
        }
    }
}
