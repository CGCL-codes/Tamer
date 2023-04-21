package org.fudaa.dodico.dico;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Classe utilisee pour contenir les resultats d'une lecture.
 * @author deniger
 * @version $Id: DicoCasResult.java,v 1.5 2006-09-19 14:42:27 deniger Exp $
 */
public class DicoCasResult implements DicoCasInterface {

    private final Map list_;

    private Map keyCommentaires_;

    private boolean printKeys_;

    private boolean printLongKeys_;

    private boolean printKeysValues_;

    private boolean commandStop_;

    /**
   * Initialise la liste des mot-cles ( sans la remplir).
   */
    public DicoCasResult() {
        list_ = new Hashtable(100);
    }

    /**
   * Recopie les pointeurs.
   * @param _t la table des mot-cles a utiliser DicoEntite->String
   * @param _comment la table des commentaires DicoEntite->String
   */
    public DicoCasResult(final Map _t, final Map _comment) {
        list_ = _t;
        keyCommentaires_ = _comment;
    }

    /**
   * @param _key le mot-cle
   * @param _com le nouveau commentaire pour ce mot-cle
   */
    public void addCommentaire(final DicoEntite _key, final String _com) {
        if (keyCommentaires_ == null) {
            keyCommentaires_ = new HashMap();
        }
        keyCommentaires_.put(_key, _com);
    }

    public String getCommentaire(final DicoEntite _e) {
        if (keyCommentaires_ == null) {
            return null;
        }
        return (String) keyCommentaires_.get(_e);
    }

    /**
   * Ajoute.
   * @param _c le mot-cle a modifier
   * @param _value le nouvelle valeur pour ce mot-cle
   */
    public void addKeyValue(final DicoEntite _c, final String _value) {
        if (_c == null) {
            throw new IllegalArgumentException("keyword null");
        }
        if (_value == null) {
            throw new IllegalArgumentException("value null");
        }
        list_.put(_c, _value);
    }

    public synchronized boolean containsEntite(final DicoEntite _e) {
        if (_e == null) {
            throw new IllegalArgumentException("keyword null");
        }
        return list_.containsKey(_e);
    }

    public synchronized int getNbInput() {
        return list_.size();
    }

    public String getValue(final DicoEntite _ent) {
        return (String) list_.get(_ent);
    }

    public Set getEntiteSet() {
        return new HashSet(list_.keySet());
    }

    /**
   * @param _b true si la commande ecrire les mot-cles a ete trouvee
   */
    public void setPrintKeys(final boolean _b) {
        printKeys_ = _b;
    }

    /**
   * @return true si la commande ecrire les mot-cles a ete trouvee
   */
    public boolean printKeys() {
        return printKeys_;
    }

    public boolean printLongKeys() {
        return printLongKeys_;
    }

    public boolean printKeysAndValues() {
        return printKeysValues_;
    }

    /**
   * @param _b
   */
    public void setPrintKeysValues(final boolean _b) {
        printKeysValues_ = _b;
    }

    /**
   * @param _b
   */
    public void setPrintLongKeys(final boolean _b) {
        printLongKeys_ = _b;
    }

    /**
   * @return true si commande stop
   */
    public boolean commandStop() {
        return commandStop_;
    }

    /**
   * @param _b
   */
    public void setCommandStop(final boolean _b) {
        commandStop_ = _b;
    }

    /**
   * @return true si stop programme
   */
    public boolean commandStopProgram() {
        return commandStop_;
    }

    /**
   * @param _b
   */
    public void setCommandStopProgram(final boolean _b) {
        commandStop_ = _b;
    }

    public Map getInputs() {
        return list_;
    }

    /**
   *
   */
    public Map getComments() {
        return keyCommentaires_;
    }
}
