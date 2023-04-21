package org.jbjf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.jbjf.core.ZipItem;

/**
 * The <code>ZipArchive</code> class provides ...
 * <pre>
 * -------------- 
 * <b>History </b>: Begin 
 * -------------- 
 * &lt;history&gt;
 * &nbsp;&nbsp;&lt;change&gt; 
 *     1.0.0; ASL; Jul 24, 2006
 *     Initial version created and customized for the ...
 *     Naming Conventions 
 *     ------------------
 *     Scope Conventions
 *       >> g - global
 *       >> m - module/class
 *       >> l - local/method/function
 *     Variable Conventions
 *       >> str - string, text, character
 *       >> lng - integer, long, numeric
 *       >> flt - real, floating point
 *       >> the - object, class, module
 *     Examples
 *       >> lstrName - local string to contain name
 *       >> glngVerbose - global integer indicator for verbose mode
 *       >> mtheScanner - class/module for a document scanner
 * &nbsp;&nbsp;&lt;/change&gt; 
 * &lt;/history&gt; 
 * -------------- 
 * <b>History </b>: End 
 * -------------- 
 * </pre>
 * @author  Adym S. Lincoln<br>
 * Copyright (C) 2007. Adym S. Lincoln All rights reserved.
 * @version 1.0.0
 * @since   1.0.0
 */
public class ZipArchive {

    /** 
     * Stores a fully qualified class name.  Used for debugging and 
     * auditing.
     * @since 1.0.0
     */
    public static final String ID = ZipArchive.class.getName();

    /** 
     * Stores the class name, primarily used for debugging and so 
     * forth.  Used for debugging and auditing.
     * @since 1.0.0
     */
    private String SHORT_NAME = "ZipArchive()";

    /** 
     * Stores a <code>SYSTEM IDENTITY HASHCODE</code>.  Used for
     * debugging and auditing.
     * @since 1.0.0
     */
    private String SYSTEM_IDENTITY = String.valueOf(System.identityHashCode(this));

    /**
     * Class property that keeps the directory location and the 
     * name of the file.
     */
    private String zipFile = null;

    /**
     * Class property that tracks whether the zip archive has been
     * written to the file or not.  Works with the autoWrite 
     * indicator as not all archiving should be automatic.
     */
    private boolean isWritten = false;

    /**
     * Class property that automates the writing out of the zip
     * archive.  This is turned on by default.
     */
    private boolean autoWrite = true;

    /**
     * Class property that stores the list of items for the zip
     * archive.  Typically contains a list of <code>String</code>
     * objects.
     */
    private HashMap zipItems;

    /** 
     * Class property that is used to transfer bytes between a
     * file and the zip archive.
     */
    private byte[] byteBuffer;

    /** 
     * Class property that stores the zip archive object.  
     */
    private ZipOutputStream zipArchive;

    /**
     * Default constructor.
     */
    public ZipArchive() {
        this.zipFile = "zip-archive.zip";
        this.autoWrite = true;
        this.isWritten = false;
        this.zipItems = new HashMap();
        this.byteBuffer = new byte[1024];
    }

    /**
     * Custom constructor that accepts the zip archive directory
     * path and filename.
     */
    public ZipArchive(String zipPath) {
        this.zipFile = zipPath;
        this.autoWrite = true;
        this.isWritten = false;
        this.zipItems = new HashMap();
        this.byteBuffer = new byte[1024];
    }

    /**
     * Custom constructor that accepts the zip archive directory
     * path and filename and the autoWrite indicator.
     */
    public ZipArchive(String zipPath, boolean autoWrite) {
        this.zipFile = zipPath;
        this.autoWrite = autoWrite;
        this.isWritten = false;
        this.zipItems = new HashMap();
        this.byteBuffer = new byte[1024];
    }

    /**
     * Adds an item to the zip archive.  A file item is passed in
     * as a directory path and filename as a <code>String</code>.
     * <p>
     * @param fileItem  A <code>String</code> that has the directory
     * path and file name for the item to add.
     * @return  A True/False indicator on whether the add succeeds.
     */
    public boolean addItem(String fileItem) throws Exception {
        boolean lblnResults = false;
        if (fileItem != null) {
            if (fileItem.length() > 1) {
                this.zipItems.put(fileItem, fileItem);
                lblnResults = true;
                if (isAutoWrite()) {
                    writeArchive();
                }
            }
        }
        return lblnResults;
    }

    /**
     * Adds many items to the zip archive.  Files are passed as a 
     * <code>List</code> of <code>String</code> objects.
     * <p>
     * @param fileItems  A <code>List</code> of <code>String</code>
     * objects that points to one or more files.  Each item should
     * provide any necessary directory path information to locate
     * the file.
     * @return  A True/False indicator on whether the add succeeds.
     */
    public boolean addItems(List fileItems) throws Exception {
        boolean lblnResults = false;
        if (fileItems != null) {
            if (fileItems.size() > 0) {
                Iterator lfileItems = fileItems.iterator();
                while (lfileItems.hasNext()) {
                    Entry lfileItem = (Entry) lfileItems.next();
                    String lfilePath = (String) lfileItem.getValue();
                    if (lfilePath != null) {
                        if (lfilePath.length() > 1) {
                            this.zipItems.put(lfilePath, lfilePath);
                            lblnResults = true;
                        }
                    }
                }
                if (isAutoWrite()) {
                    writeArchive();
                }
            }
        }
        return lblnResults;
    }

    /**
     * Writes out the current items to a zip archive.  If the zip
     * archive exists, then it will be replaced with the items in
     * this class.
     * <p>
     * @throws Exception    Any issues are escalated upward.
     */
    public void writeArchive() throws Exception {
        if (getZipItems().size() > 0) {
            if (getZipArchive() != null) {
                getZipArchive().close();
                setZipArchive(null);
            }
            if (new File(getZipFile()).exists()) {
                ZipInputStream lzipInput = new ZipInputStream(new FileInputStream(getZipFile()));
                ZipEntry zipEntry = lzipInput.getNextEntry();
                while (zipEntry != null) {
                    getZipItems().put(zipEntry.getName(), zipEntry.getName());
                    zipEntry = lzipInput.getNextEntry();
                }
                lzipInput.close();
            }
            Iterator literItems = getZipItems().entrySet().iterator();
            while (literItems.hasNext()) {
                Entry lnextItem = (Entry) literItems.next();
                String lfileItem = (String) lnextItem.getValue();
                FileInputStream lfileInput = new FileInputStream(lfileItem);
                if (lfileItem != null) {
                    addStream(new ZipEntry(lfileItem), lfileInput);
                }
                try {
                    lfileInput.close();
                } catch (IOException lioXcp) {
                    lioXcp.printStackTrace();
                }
            }
            literItems = null;
            getZipArchive().close();
        }
    }

    /**
     * Specialized add function that takes an <code>InputStream</code>
     * as a parameter instead of the tradition <code>String</code> filename.
     * <p>
     * @param pzipEntry  A standard zipfile entry.
     * @param pfileInput        An <code>InputStream</code> object that is
     *                  allocated to the file to add.
     * @throws Exception    Any problems, escalate to the parent.
     */
    private void addStream(ZipEntry pzipEntry, InputStream pfileInput) throws Exception {
        if (getZipArchive() == null) {
            this.zipArchive = new ZipOutputStream(new FileOutputStream(getZipFile()));
        }
        getZipArchive().putNextEntry(pzipEntry);
        int llngLength = pfileInput.read(getByteBuffer());
        while (llngLength > 0) {
            getZipArchive().write(getByteBuffer(), 0, llngLength);
            llngLength = pfileInput.read(getByteBuffer());
        }
        getZipArchive().closeEntry();
    }

    /**
     * Traditional getter method that 
     * @return the zipItems
     */
    public HashMap getZipItems() {
        return zipItems;
    }

    /**
     * Traditional setter method that sets 
     * @param zipItems the zipItems to set
     */
    public void setZipItems(HashMap zipItems) {
        this.zipItems = zipItems;
    }

    /**
     * Traditional getter method that 
     * @return the zipArchive
     */
    public ZipOutputStream getZipArchive() {
        return zipArchive;
    }

    /**
     * Traditional setter method that sets 
     * @param zipArchive the zipArchive to set
     */
    public void setZipArchive(ZipOutputStream zipArchive) {
        this.zipArchive = zipArchive;
    }

    /**
     * Traditional getter method that 
     * @return the byteBuffer
     */
    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    /**
     * Traditional setter method that sets 
     * @param byteBuffer the byteBuffer to set
     */
    public void setByteBuffer(byte[] byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * Traditional getter method that 
     * @return the zipFile
     */
    public String getZipFile() {
        return zipFile;
    }

    /**
     * Traditional setter method that sets 
     * @param zipFile the zipFile to set
     */
    public void setZipFile(String zipFile) {
        this.zipFile = zipFile;
    }

    /**
     * Traditional getter method that 
     * @return the isWritten
     */
    public boolean isWritten() {
        return isWritten;
    }

    /**
     * Traditional setter method that sets 
     * @param isWritten the isWritten to set
     */
    public void setWritten(boolean isWritten) {
        this.isWritten = isWritten;
    }

    /**
     * Traditional getter method that 
     * @return the autoWrite
     */
    public boolean isAutoWrite() {
        return autoWrite;
    }

    /**
     * Traditional setter method that sets 
     * @param autoWrite the autoWrite to set
     */
    public void setAutoWrite(boolean autoWrite) {
        this.autoWrite = autoWrite;
    }
}
