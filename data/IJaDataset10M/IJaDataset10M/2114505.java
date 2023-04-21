package org.fudaa.dodico.dico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.StringTokenizer;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.fileformat.FileFormatVersionInterface;
import org.fudaa.ctulu.fileformat.FileReadOperationAbstract;
import org.fudaa.ctulu.fileformat.FortranInterface;

/**
 * @author Fred Deniger
 * @version $Id: DicoCasReader.java,v 1.26 2007-11-20 11:42:56 bmarchan Exp $
 */
public class DicoCasReader extends FileReadOperationAbstract {

    DicoModelAbstract dico_;

    DicoCasFileFormatVersionAbstract ft_;

    LineNumberReader in_;

    boolean quoteEnCours_;

    StringBuffer valueBuffer_;

    DicoEntite currentkey_;

    StringBuffer commentairesEnCours_;

    /**
   * @param _t la version a utiliser pour lire le fichier cas.
   */
    public DicoCasReader(final DicoCasFileFormatVersionAbstract _t) {
        ft_ = _t;
        dico_ = ft_.getDico();
        if (dico_ == null) {
            throw new IllegalArgumentException();
        }
    }

    public void setFile(final File _f) {
        analyze_ = new CtuluAnalyze();
        analyze_.setResource(_f.getAbsolutePath());
        try {
            in_ = new LineNumberReader(new FileReader(_f));
        } catch (final FileNotFoundException e) {
            in_ = null;
        }
    }

    /**
   * Test rapide pour savoir si le fichier _f contient des donnees valides.
   * @param _f le fichier a tester
   * @return true si fichier valide
   */
    public boolean testFile(final File _f) {
        LineNumberReader l = null;
        String line;
        try {
            l = new LineNumberReader(new FileReader(_f));
            String trimLine = null;
            while ((line = l.readLine()) != null) {
                trimLine = line.trim();
                if ((trimLine.length() == 0) || (trimLine.charAt(0) == '/')) {
                    continue;
                }
                final int indexEq = getEqualsIndex(trimLine);
                if (indexEq < 0) {
                    return false;
                }
                final String s = trimLine.substring(0, indexEq).trim();
                return dico_.isKey(s);
            }
        } catch (final IOException e) {
        } finally {
            if (l != null) {
                try {
                    l.close();
                } catch (final IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    private DicoCasInterface readCas() {
        DicoCasResult inter = null;
        if (in_ == null) {
            analyze_.addFatalError(DicoResource.getS("Flux d'entr�e non trouv�"));
            return null;
        }
        try {
            String line;
            quoteEnCours_ = false;
            valueBuffer_ = new StringBuffer(100);
            inter = new DicoCasResult();
            currentkey_ = null;
            commentairesEnCours_ = new StringBuffer();
            while ((line = in_.readLine()) != null) {
                if (quoteEnCours_) {
                    splitLine(line, inter);
                } else if (line.trim().length() == 0) {
                    commentairesEnCours_ = new StringBuffer(30);
                } else {
                    if (line.charAt(0) == '/') {
                        if (commentairesEnCours_.length() > 0) {
                            commentairesEnCours_.append(CtuluLibString.LINE_SEP);
                        }
                        commentairesEnCours_.append(line.substring(1));
                    } else if (line.startsWith(ft_.getCommandStop())) {
                        inter.setCommandStop(true);
                    } else if (line.startsWith(ft_.getCommandStopProgram())) {
                        inter.setCommandStopProgram(true);
                    } else if (line.startsWith(ft_.getCommandPrintKeys())) {
                        inter.setPrintKeys(true);
                    } else if (line.startsWith(ft_.getCommandPrintLongKeys())) {
                        inter.setPrintLongKeys(true);
                    } else if (line.startsWith(ft_.getCommandPrintKeysValues())) {
                        inter.setPrintKeysValues(true);
                    } else {
                        splitLine(line, inter);
                    }
                }
            }
        } catch (final IOException _ioe) {
            analyze_.manageException(_ioe);
        }
        if (currentkey_ != null) {
            endValue(inter);
        }
        return inter;
    }

    /**
   * Supprime les commentaires et les quotes ( et transforme les double en simple).
   */
    private void splitLine(final String _s, final DicoCasResult _m) {
        final int l = _s.length();
        final int index = _s.indexOf('\'');
        if (quoteEnCours_) {
            if (index < 0) {
                appendValue(_s);
            } else if (index == l - 1) {
                appendValue(_s.substring(0, l - 1));
                endValue(_m);
                quoteEnCours_ = false;
            } else {
                final char c = _s.charAt(index + 1);
                if (c == '\'') {
                    appendValue(_s.substring(0, index + 1));
                    splitLine(_s.substring(index + 2), _m);
                } else {
                    appendValue(_s.substring(0, index));
                    quoteEnCours_ = false;
                    splitLine(_s.substring(index + 1), _m);
                }
            }
        } else {
            final int indexCom = _s.indexOf('/');
            if (indexCom < 0) {
                if (index >= 0) {
                    final int equalIdx = getEqualsIndex(_s);
                    if (equalIdx > index) {
                        addKeyOrValue(_s.substring(0, equalIdx), _m);
                        splitLine(_s.substring(equalIdx + 1), _m);
                        return;
                    }
                    putStringInArray(_s.substring(0, index), _m);
                    quoteEnCours_ = true;
                    splitLine(_s.substring(index + 1), _m);
                } else {
                    putStringInArray(_s, _m);
                }
            } else if ((index >= 0) && (index < indexCom)) {
                putStringInArray(_s.substring(0, index), _m);
                quoteEnCours_ = true;
                splitLine(_s.substring(index + 1), _m);
            } else {
                putStringInArray(_s.substring(0, indexCom), _m);
                final int com2 = _s.indexOf('/', indexCom + 1);
                if ((com2 < 0) || (com2 == l - 1)) {
                    return;
                }
                splitLine(_s.substring(com2 + 1), _m);
            }
        }
    }

    /**
   * @param _s la ligne lue
   * @return l'index pour le caractere = ou :. -1 si non trouve
   */
    public int getEqualsIndex(final String _s) {
        final int i = _s.indexOf('=');
        if (i < 0) {
            return _s.indexOf(':');
        }
        final int i2Pt = _s.indexOf(':');
        if (i2Pt < 0) {
            return i;
        }
        if (i2Pt < i) {
            return i2Pt;
        }
        return i;
    }

    private void putStringInArray(final String _s, final DicoCasResult _m) {
        final StringTokenizer tk = new StringTokenizer(_s, ":=");
        String t;
        while (tk.hasMoreTokens()) {
            t = tk.nextToken().trim();
            if (t.length() > 0) {
                if (dico_.isKey(t)) {
                    addKeyOrValue(t, _m);
                } else {
                    final int i = t.indexOf(' ');
                    if (i > 0) {
                        addKeyOrValue(t.substring(0, i).trim(), _m);
                        addKeyOrValue(t.substring(i + 1).trim(), _m);
                    } else {
                        addKeyOrValue(t, _m);
                    }
                }
            }
        }
    }

    private void endValue(final DicoCasResult _m) {
        final String s = valueBuffer_.toString().trim();
        if (currentkey_ == null) {
            analyze_.addError(DicoResource.getS("La valeur {0} est ignor�e", s), in_);
        } else {
            _m.addKeyValue(currentkey_, s);
            valueBuffer_ = new StringBuffer(30);
            if (!currentkey_.isValide(s)) {
                analyze_.addError(DicoResource.getS("La valeur {0} est invalide pour le mot-cl� {1}", s, currentkey_.getNom()), in_.getLineNumber());
            }
            if (commentaireFinal_ != null && commentaireFinal_.length() > 0) {
                _m.addCommentaire(currentkey_, commentaireFinal_);
            }
            currentkey_ = null;
        }
    }

    private void appendValue(final String _t) {
        valueBuffer_.append(_t);
    }

    String commentaireFinal_;

    private void addKeyOrValue(final String _s, final DicoCasResult _m) {
        final String s = _s.trim();
        if (dico_.isKey(s)) {
            if (currentkey_ != null) {
                endValue(_m);
            }
            valueBuffer_ = new StringBuffer(30);
            currentkey_ = dico_.getEntite(s);
            if (currentkey_ != null) {
                commentaireFinal_ = commentairesEnCours_.toString();
                commentairesEnCours_ = new StringBuffer(30);
            }
        } else {
            appendValue(_s);
        }
    }

    /**
   * @throws IOException fermeture
   */
    public void close() throws IOException {
        in_.close();
    }

    /**
   * @param _f le fichier a lire (0).
   */
    public void setFile(final File[] _f) {
        setFile(_f[0]);
    }

    /**
   * @return la version utilisee
   */
    public DicoCasFileFormatVersionAbstract getDicoCasFileFormatVersion() {
        return ft_;
    }

    /**
   * @return la version utilisee
   */
    public FileFormatVersionInterface getFileFormatVersion() {
        return getDicoCasFileFormatVersion();
    }

    /**
   *
   */
    protected Object internalRead() {
        return readCas();
    }

    /**
   *
   */
    protected FortranInterface getFortranInterface() {
        return new FortranInterface() {

            public void close() throws IOException {
                if (in_ != null) {
                    in_.close();
                }
            }
        };
    }

    /**
   * @return la version utilisee
   */
    public FileFormatVersionInterface getVersion() {
        return ft_;
    }
}
