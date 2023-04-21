package com.meschbach.cise.jam;

import com.meschbach.cise.util.JarEntryIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A <code>ZipManipulator</code> encapsulates a series of transformations on
 * a ZIP archive include: adding entries, removing entries, and replacing
 * entries.
 * 
 * Copyright 2010-2011 by Mark Eschbach, under Apache License, Vesrion 2.0<p>
 *
 * @author "Mark Eschbach" meschbach@gmail.com;
 * @version 1.0.4
 * @since 1.1.0
 */
public class ZipManipulator {

    InputStream source;

    OutputStream destination;

    List<StreamProcessor> postProcessors;

    Map<String, EntryVisitor> filters;

    EntryVisitor defaultVisitor;

    public ZipManipulator() {
        filters = new HashMap<String, EntryVisitor>();
        postProcessors = new LinkedList<StreamProcessor>();
        defaultVisitor = CopyVisitor.getSharedInstance();
    }

    public OutputStream getDestination() {
        return destination;
    }

    public void setDestination(OutputStream destination) {
        this.destination = destination;
    }

    public InputStream getSource() {
        return source;
    }

    public void setSource(InputStream source) {
        this.source = source;
    }

    public void createEmptySource() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(buffer);
        ZipEntry defaultEntry = new ZipEntry("META-INF/created-by");
        defaultEntry.setTime(System.currentTimeMillis());
        zos.putNextEntry(defaultEntry);
        zos.write(getClass().getCanonicalName().getBytes());
        zos.close();
        byte data[] = buffer.toByteArray();
        source = new ByteArrayInputStream(data);
    }

    public void performOperations() throws IOException {
        if (source == null) {
            throw new IllegalStateException("JAR source may not be null");
        }
        if (destination == null) {
            throw new IllegalStateException("JAR destination may not be null");
        }
        JarInputStream jis = new JarInputStream(source);
        JarOutputStream jos = new JarOutputStream(destination);
        jos.setLevel(9);
        jos.setMethod(JarOutputStream.DEFLATED);
        JarEntryIterator jei = new JarEntryIterator(jis);
        while (jei.hasNext()) {
            JarEntry sourceEntry = jei.next();
            final String entryName = sourceEntry.getName();
            if (filters.containsKey(entryName)) {
                EntryVisitor ev = filters.get(entryName);
                ev.visitEntry(entryName, sourceEntry, jis, jos);
            } else {
                defaultVisitor.visitEntry(entryName, sourceEntry, jis, jos);
            }
        }
        Iterator<StreamProcessor> isp = new LinkedList<StreamProcessor>(postProcessors).iterator();
        while (isp.hasNext()) {
            StreamProcessor sp = isp.next();
            sp.affectStream(jos);
        }
        jos.finish();
        jos.flush();
    }

    public void addEntryVisitor(String name, EntryVisitor ev) {
        if (ev == null) {
            throw new NullPointerException("Entry visitor may not be null");
        }
        if (name == null) {
            throw new NullPointerException("Entry name may not be null");
        }
        filters.put(name, ev);
    }

    public void addPostProcessor(StreamProcessor processor) {
        if (processor == null) {
            throw new NullPointerException();
        }
        postProcessors.add(processor);
    }
}
