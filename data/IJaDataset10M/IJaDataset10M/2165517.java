package com.ibm.awb.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarArchive extends Archive {

    /**
     * 
     */
    private static final long serialVersionUID = -6983723997728651259L;

    private static boolean verbose = false;

    Manifest manifest = null;

    byte[] contents = null;

    boolean allowPut = false;

    public JarArchive(InputStream in) throws java.io.IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] buff = new byte[512];
        int i;
        while ((i = in.read(buff, 0, 512)) >= 0) {
            bao.write(buff, 0, i);
        }
        this.contents = bao.toByteArray();
        this.update();
    }

    public JarArchive(String zipname) throws java.io.IOException {
        this(new FileInputStream(zipname));
    }

    private static void debug(String msg) {
        if (verbose) {
            System.out.println(msg);
        }
    }

    public Archive getArchiveFor(String classname) {
        return null;
    }

    public Manifest getManifest() {
        return this.manifest;
    }

    @Override
    public synchronized byte[] getResourceAsByteArray(String name) {
        byte[] b = this.getResourceInCache(name);
        if (b == null) {
            b = this.putResourceFromJarArchive(name);
        }
        return b;
    }

    @Override
    public synchronized InputStream getResourceAsStream(String name) {
        byte[] b = this.getResourceInCache(name);
        if (b == null) {
            b = this.putResourceFromJarArchive(name);
        }
        if (b != null) {
            return new ByteArrayInputStream(b);
        }
        return null;
    }

    public boolean isAglet(String classname) {
        return (this.manifest != null) && this.manifest.isAglet(classname);
    }

    synchronized void putResoruce(String name, byte[] res) {
        if (this.allowPut) {
            super.putResource(name, res);
        }
    }

    private byte[] putResourceFromJarArchive(String name) {
        return null;
    }

    private void readFully(InputStream in, byte[] b) throws java.io.IOException {
        int offset = 0;
        while (offset < b.length) {
            offset += in.read(b, offset, b.length - offset);
        }
    }

    public void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
    }

    private void update() throws java.io.IOException {
        this.allowPut = true;
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(this.contents));
        ZipEntry ze = null;
        java.io.ByteArrayOutputStream bao = null;
        byte[] buff = null;
        ;
        int i = 0;
        while ((ze = zis.getNextEntry()) != null) {
            String n = ze.getName();
            debug("[" + (i++) + "] " + n);
            if (sun.tools.jar.Manifest.isManifestName(n)) {
                this.manifest = new Manifest(zis);
            } else if (n.toUpperCase().startsWith("MANIFEST/") && n.toUpperCase().endsWith(".SF")) {
            } else {
                byte b[];
                long l = ze.getSize();
                if (l < 0) {
                    int read;
                    if (bao == null) {
                        buff = new byte[512];
                        bao = new java.io.ByteArrayOutputStream();
                    }
                    bao.reset();
                    while ((read = zis.read(buff, 0, 512)) > 0) {
                        bao.write(buff, 0, read);
                    }
                    b = bao.toByteArray();
                } else {
                    b = new byte[(int) l];
                    this.readFully(zis, b);
                }
                this.putResource(ze.getName(), b);
            }
        }
        this.allowPut = false;
    }
}
