package org.fudaa.ctulu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import com.memoire.fu.FuLib;

/**
 * Permet d'ecrire un fichier csv. Ne prend pas en compte le fait que les string peuvent contenir des caract�res
 * sp�ciaux. Derniere version: la version nio a ete remplace par un Streamwriter plus simple. Avec les nio des
 * caract�res bizarres etaient ajout�s en fin du fichier.
 * 
 * @author Fred Deniger
 * @version $Id: CsvWriter.java,v 1.8 2006-10-19 14:12:03 deniger Exp $
 */
public class CsvWriter {

    Writer out_;

    boolean firstCol_ = true;

    String lineSep_ = CtuluLibString.LINE_SEP;

    char sepChar_ = ' ';

    /**
   * @param _f le fichier de destination
   * @throws IOException
   */
    public CsvWriter(final File _f) throws IOException {
        out_ = new OutputStreamWriter(new FileOutputStream(_f, false), Charset.forName(FuLib.DEFAULT_ENCODING).newEncoder());
    }

    public CsvWriter(final OutputStream _f) throws IOException {
        out_ = new OutputStreamWriter(_f, Charset.forName(FuLib.DEFAULT_ENCODING).newEncoder());
    }

    public void flush() throws IOException {
        out_.flush();
    }

    /**
   * @param _s la chaine a ajouter
   * @param _addSepChar true si doit ajouter le sep de car
   * @throws IOException
   */
    private void appendString(final String _s, final boolean _addSepChar) throws IOException {
        if (_addSepChar && !firstCol_) {
            out_.write(sepChar_);
        }
        out_.write(_s);
        firstCol_ = false;
    }

    public void writeLine(final String _comment) throws IOException {
        out_.write(_comment);
        out_.write(lineSep_);
    }

    /**
   * @param _d le double a ajouter dans le champs.
   * @throws IOException
   */
    public void appendDouble(final double _d) throws IOException {
        appendString(Double.toString(_d), true);
    }

    /**
   * @param _s la chaine a ajoute dans le champs
   * @throws IOException
   */
    public void appendString(final String _s) throws IOException {
        appendString(_s, true);
    }

    /**
   * Ferme le flux.
   * 
   * @throws IOException
   */
    public void close() throws IOException {
        out_.close();
    }

    /**
   * @return le separateur de ligne
   */
    public final String getLineSep() {
        return lineSep_;
    }

    /**
   * @return le separateur de champs
   */
    public final char getSepChar() {
        return sepChar_;
    }

    /**
   * Retour a la ligne.
   * 
   * @throws IOException
   */
    public void newLine() throws IOException {
        appendString(lineSep_, false);
        firstCol_ = true;
    }

    /**
   * @param _lineSep le separateur de ligne
   */
    public final void setLineSep(final String _lineSep) {
        lineSep_ = _lineSep;
    }

    /**
   * @param _sepChar
   */
    public final void setSepChar(final char _sepChar) {
        sepChar_ = _sepChar;
    }
}
