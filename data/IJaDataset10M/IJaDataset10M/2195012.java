package org.fudaa.dodico.reflux.io;

import org.fudaa.ctulu.fileformat.FileFormatUnique;
import org.fudaa.ctulu.fileformat.FileReadOperationAbstract;
import org.fudaa.ctulu.fileformat.FileWriteOperationAbstract;

/**
 * @author Fred Deniger
 * @version $Id: RefluxSolutionInitFileFormat.java,v 1.10 2006-11-15 09:22:53 deniger Exp $
 */
public final class RefluxSolutionInitFileFormat extends FileFormatUnique {

    private static final RefluxSolutionInitFileFormat INSTANCE = new RefluxSolutionInitFileFormat();

    /**
   * @return singleton
   */
    public static RefluxSolutionInitFileFormat getInstance() {
        return INSTANCE;
    }

    /**
   * Initialise les donnees.
   */
    public RefluxSolutionInitFileFormat() {
        super(1);
        extensions_ = new String[] { "siv" };
    }

    /**
   * @see org.fudaa.ctulu.fileformat.FileFormatVersion#createReader()
   */
    public FileReadOperationAbstract createReader() {
        return new RefluxSolutionInitReader();
    }

    /**
   * @see org.fudaa.ctulu.fileformat.FileFormatVersion#createWriter()
   */
    public FileWriteOperationAbstract createWriter() {
        return new RefluxSolutionInitWriter();
    }
}
