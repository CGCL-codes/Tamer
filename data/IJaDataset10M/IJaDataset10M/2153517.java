package org.fudaa.dodico.dico;

import java.io.File;
import org.fudaa.ctulu.fileformat.FileFormatVersionInterface;

/**
 * @author deniger
 * @version $Id: DicoCasFileFormat.java,v 1.18 2007-06-29 15:10:27 deniger Exp $
 */
public abstract class DicoCasFileFormat extends DicoCasFileFormatAbstract {

    /**
   * @param _nom le nom du fichier dico lu ( telemac2d, ...�
   */
    public DicoCasFileFormat(final String _nom) {
        super(_nom);
    }

    /**
   * @return le manager des dictionnaires
   */
    public abstract DicoManager getDicoManager();

    public String getLastVersion() {
        return getDicoManager().getLastVersion(this);
    }

    /**
   * @param _language le langage voulu
   * @return la version dans le langage demande
   * @see DicoLanguage
   */
    public DicoCasFileFormatVersion getLastVersionImpl(final int _language) {
        return getDicoManager().createLastVersionImpl(this, _language);
    }

    public FileFormatVersionInterface getLastVersionInstance(File _f) {
        return getLastVersionImpl();
    }

    public FileFormatVersionInterface getLastVersionInstance() {
        return getLastVersionImpl();
    }

    /**
   * @return version
   */
    public DicoCasFileFormatVersion getLastVersionImpl() {
        return getDicoManager().createLastVersionImpl(this, DicoLanguage.getCurrentID());
    }

    /**
   *
   */
    public int getVersionNb() {
        return getDicoManager().getVersionsNb(this);
    }

    /**
   *
   */
    public String[] getVersions() {
        return getDicoManager().getVersions(this);
    }

    /**
   * @param _version
   * @return la version
   */
    public DicoCasFileFormatVersion getVersionImpl(final String _version) {
        return getDicoManager().createVersionImpl(this, _version);
    }

    /**
   * @param _version
   * @param _language
   * @return la version
   */
    public DicoCasFileFormatVersion getVersionImpl(final String _version, final int _language) {
        return getDicoManager().createVersionImpl(this, _version, _language);
    }
}
