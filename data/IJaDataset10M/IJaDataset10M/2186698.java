package org.openstock.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by IntelliJ IDEA.
 * User: dm
 * Date: Sep 16, 2007
 * Time: 1:22:33 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FileSymbolIndicator extends SymbolIndicator {

    protected final File file;

    protected transient RandomAccessFile randomAccessFile;

    protected FileSymbolIndicator(File file) throws FileNotFoundException {
        if (file == null) throw new IllegalArgumentException("file");
        this.file = file;
        this.randomAccessFile = new RandomAccessFile(file, "r");
    }
}
