package cbr2.service;

import DE.FhG.IGD.semoa.service.*;
import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.util.*;
import DE.FhG.IGD.ui.*;
import java.security.*;
import java.util.*;
import java.io.*;
import DE.siemens.ct.pmap.authorization.service.TicketCheckInterface;
import DE.siemens.ct.pmap.authorization.TicketCheckException;
import DE.siemens.ct.pmap.authorization.asn1.*;
import codec.asn1.ASN1Exception;

/**
 * A class which implements the service that is
 * used to advertise the pictures.<p>
 *
 * This class provides an iterator for the pictures in the
 * configured picture directory. This iterator attempts to
 * open and read line by line a file with the name &quot;
 * ls.txt&quot; in the picture directory. This file must
 * contain the names of the known pictures. Such a file
 * can be created simply by issuing the command <code>ls
 * &gt;ls.txt</code> in the picture directory.<p>
 *
 * Iterating through the picture names and loading pictures
 * requires that this class has privileges to read said
 * files and that the reading instance is created by a class
 * that has said privileges also. Malicious code can not
 * create an instance of this class and exploit it to access
 * said directories. See {@link AbstractService#doPrivileged
 * AbstractService} for details.<p>
 *
 * <b>Be careful, this class contains a security hole that
 * might allow untrusted code to delete files!</b>
 *
 * @author Volker Roth
 * @version "$Id: PictureDB.java 1034 2003-04-25 17:54:08Z jpeters $"
 * @see PrivilegedOpen
 * @see Service
 */
public class PictureDB extends AbstractService implements GuardedPicsSource {

    /**
     * The directory from which pictures are loaded.
     */
    protected File pictures_;

    /**
     * The directory from which thumbnails are loaded.
     */
    protected File thumbnails_;

    /**
     * Creates an instance.
     */
    public PictureDB() {
    }

    /**
     * Creates an instance which loads images from the
     * given directories. For each image there should be a
     * thumbnail available. Thumbnails must have the same
     * name as the picture to which they refer.
     *
     * @param p The directory containing the available
     *   pictures.
     * @param t The directory containing the thumbnails
     *   for the available pictures.
     */
    public PictureDB(File p, File t) {
        if (p == null || t == null) {
            throw new NullPointerException("Either pics dir or thumbnail dir is NULL!");
        }
        pictures_ = p;
        thumbnails_ = t;
    }

    public String author() {
        return People.VROTH;
    }

    public String revision() {
        return "$Revision: 1034 $/$Date: 2003-04-25 13:54:08 -0400 (Fri, 25 Apr 2003) $";
    }

    public String info() {
        return "Provides access to pictures.";
    }

    public void setPictures(File file) {
        if (file == null) {
            throw new NullPointerException("File");
        }
        pictures_ = file;
    }

    public void setThumbnails(File file) {
        if (file == null) {
            throw new NullPointerException("File");
        }
        thumbnails_ = file;
    }

    /**
     * Returns an iterator that iterates the names of the
     * available pictures.
     *
     * @return The iterator that iterates the names of the
     *   available pictures..
     */
    public Iterator iterator() {
        PrivilegedAction priv;
        priv = new PrivilegedOpen(new File(pictures_, "ls.txt"), PrivilegedOpen.READ);
        return new LineIterator((InputStream) doPrivileged(priv));
    }

    /**
     * Retrieves a picture based on the name.
     *
     * @param name The name of the picture. This name
     *   must be locally unique.
     * @return The encoding of the picture with the
     *   given name.
     */
    public byte[] getPicture(String name) {
        if (name == null) {
            throw new NullPointerException("Need a file name!");
        }
        return loadPicture(new File(pictures_, name));
    }

    public byte[] getPicture(String name, byte[] token, OutputStream os) {
        TicketCheckInterface tcs;
        Environment env;
        InputStream is;
        if (name == null) {
            throw new NullPointerException("Need a file name!");
        }
        env = Environment.getEnvironment();
        tcs = (TicketCheckInterface) env.lookup(WhatIs.stringValue("TICKET_CHECK"));
        if (tcs == null) {
            System.out.println("Ticket check service not found!");
            return null;
        }
        try {
            Collection readFiles;
            AccessRight access;
            readFiles = new HashSet();
            readFiles.add(new KerberosString(name));
            access = new AccessRight(new KerberosStringSeq(readFiles), null, null);
            tcs.checkTicket(access, new ByteArrayInputStream(token), os);
            return loadPicture(new File(pictures_, name));
        } catch (ASN1Exception ex1) {
            System.out.println("ASN1Exception caught: " + ex1.getMessage());
            return null;
        } catch (TicketCheckException ex2) {
            System.out.println("Ticket not valid!");
            return null;
        }
    }

    /**
     * Retrieves a thumbnail based on the name.
     *
     * @param name The name of the picture. This name
     *   must be locally unique.
     * @return The encoding of the picture with the
     *   given name.
     */
    public byte[] getThumbnail(String name) {
        if (name == null) {
            throw new NullPointerException("Need a file name!");
        }
        return loadPicture(new File(thumbnails_, name));
    }

    /**
     * Loads a file into a byte array. This method is used
     * for loading pictures and thumbnails from folders to
     * which access is restricted.
     *
     * @param file The file to load.
     * @return The bytes of the file.
     */
    private byte[] loadPicture(File file) {
        PrivilegedAction priv;
        FileInputStream fis;
        byte[] im;
        int n;
        priv = new PrivilegedOpen(file, PrivilegedOpen.READ);
        fis = (FileInputStream) doPrivileged(priv);
        if (fis == null) {
            return null;
        }
        try {
            n = fis.available();
            im = new byte[n];
            fis.read(im);
            fis.close();
            return im;
        } catch (Exception e) {
            return null;
        }
    }
}
