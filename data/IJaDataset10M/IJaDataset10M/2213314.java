package org.fudaa.ctulu.fileformat;

import java.io.File;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.ProgressionInterface;

/**
 * @author deniger
 * @version $Id: FileReadOperationAbstract.java,v 1.7 2006-09-19 14:36:56 deniger Exp $
 */
public abstract class FileReadOperationAbstract extends FileOperationAbstract {

    protected String getOperationDescription(final File _f) {
        return CtuluLib.getS("Lecture") + CtuluLibString.ESPACE + _f.getName();
    }

    protected abstract Object internalRead();

    /**
   * lit le fichier et ferme le flux. Pour recuperer les donnees,
   * il suffit d'utiliser getSource.
   * @return la synthese de la lecture
   */
    public final CtuluIOOperationSynthese read() {
        return closeOperation(internalRead());
    }

    /**
   * @param _f le fichier a lire
   * @param _inter la barre de progression
   * @return la synthese de la lecture
   */
    public final CtuluIOOperationSynthese read(final File _f, final ProgressionInterface _inter) {
        setFile(_f);
        final String s = getOperationDescription(_f);
        analyze_.setDesc(s);
        if (_inter != null) {
            _inter.setDesc(s);
        }
        setProgressReceiver(_inter);
        return read();
    }
}
