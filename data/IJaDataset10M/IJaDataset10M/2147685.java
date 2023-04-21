package org.fudaa.fudaa.piv.io;

import org.fudaa.ctulu.fileformat.FileFormatUnique;
import org.fudaa.ctulu.fileformat.FileReadOperationAbstract;
import org.fudaa.ctulu.fileformat.FileWriteOperationAbstract;
import org.fudaa.fudaa.piv.PivResource;

/**
 * La classe d�crivant le format d'un fichier grid_param.dat contenant le
 * contour de grille.
 *
 * @author Bertrand Marchand (marchand@deltacad.fr)
 * @version $Id: PivGridParamFileFormat.java 6609 2011-11-03 10:39:52Z bmarchan $
 */
public class PivGridParamFileFormat extends FileFormatUnique {

    private PivGridParamFileFormat() {
        super(1);
        nom_ = PivResource.getS("PIV shape grid");
        description_ = PivResource.getS("Fichier contenant le contour de grille");
        extensions_ = new String[] { "dat" };
    }

    static final PivGridParamFileFormat INSTANCE = new PivGridParamFileFormat();

    /**
   * Retourne le singleton pour instancier un reader ou un writer.
   * @return Le singleton
   */
    public static PivGridParamFileFormat getInstance() {
        return INSTANCE;
    }

    /**
   * Cr�ation d'un reader.
   * @return Le reader
   */
    public FileReadOperationAbstract createReader() {
        return new PivGridParamReader();
    }

    /**
   * Cr�ation d'un writer.
   * @return Le writer
   */
    public FileWriteOperationAbstract createWriter() {
        return new PivGridParamWriter();
    }
}
