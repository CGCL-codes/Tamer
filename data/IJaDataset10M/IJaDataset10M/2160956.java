package org.fudaa.dodico.ef.io.dunes;

import java.io.File;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.fileformat.FileFormatUnique;
import org.fudaa.ctulu.fileformat.FileReadOperationAbstract;
import org.fudaa.ctulu.fileformat.FileWriteOperationAbstract;
import org.fudaa.dodico.commun.DodicoLib;
import org.fudaa.dodico.fichiers.FileFormatSoftware;

/**
 * Un format pour les fichiers de modelisation geometriques Dunes.
 * @author Bertrand Marchand
 * @version $Id: SinusxFileFormat.java,v 1.15 2007/05/04 13:47:27 deniger Exp $
 */
public final class DunesGEOFileFormat extends FileFormatUnique {

    static final DunesGEOFileFormat INSTANCE = new DunesGEOFileFormat();

    /**
   * @return singleton
   */
    public static DunesGEOFileFormat getInstance() {
        return INSTANCE;
    }

    private DunesGEOFileFormat() {
        super(1);
        extensions_ = new String[] { "geo" };
        id_ = "DUNESGEO";
        nom_ = "Dunes g�ometrie";
        description_ = DodicoLib.getS("Comporte les d�finitions de points, polylignes et polygones");
        software_ = FileFormatSoftware.REFLUX_IS;
    }

    public FileReadOperationAbstract createReader() {
        return null;
    }

    public CtuluIOOperationSynthese write(final File _f, final Object _source, final ProgressionInterface _prog) {
        return super.write(_f, _source, _prog);
    }

    public FileWriteOperationAbstract createWriter() {
        return new DunesGEOWriter(this);
    }
}
