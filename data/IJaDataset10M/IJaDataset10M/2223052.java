package org.fudaa.fudaa.sipor;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import org.fudaa.ctulu.CtuluLibString;
import com.memoire.bu.BuCharValidator;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuStringValidator;
import com.memoire.bu.BuTextField;
import com.memoire.bu.BuValueValidator;

/**
 * Composant permettant de saisir des dur�es sous diff�rents formats. Le format des donn�es est controle par des
 * validateur. Principe de convertion automatique (chaine vers nombre de minutes et vise versa) : 60 minutes -> 1 heure ;
 * 24 heures -> 1 jour ; 31 jours -> 1 mois ( = nombre de jours de janvier ) ; 31+28 jours -> 2 mois ( = nombre de jours
 * de janv. + fevrier ).
 * 
 * @version $Revision: 1.8 $ $Date: 2007-07-23 12:46:35 $ by $Author: hadouxad $
 * @author Bertrand AUDINET et Nicolas Chevalier
 */
public class DureeField extends JComponent implements FocusListener {

    public static String getSep() {
        return ":";
    }

    boolean presenceMois_;

    boolean presenceJours_;

    boolean presenceMinutes_;

    boolean presenceHeures_;

    BuTextField zoneTexte_ = new BuTextField();

    private final BuLabel label_ = new BuLabel();

    static int[] calendrier_ = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    int nbEltsMax_;

    private final BuCharValidator charDuree_ = new BuCharValidator() {

        public boolean isCharValid(char _char) {
            if (_char == ':') {
                String chaine = zoneTexte_.getText();
                StringTokenizer token = new StringTokenizer(chaine, getSep());
                int nbElts = token.countTokens();
                if (nbElts < nbEltsMax_) {
                    return true;
                }
            } else if (Character.isDigit(_char)) {
                return true;
            }
            return false;
        }
    };

    final BuValueValidator valueDuree_ = new BuValueValidator() {

        public boolean isValueValid(Object _value) {
            if (!(_value instanceof Integer)) {
                return false;
            }
            int val = ((Integer) _value).intValue();
            if (val < 0) {
                return false;
            }
            if (presenceHeures_ && !presenceMinutes_ && (val % 60) != 0) {
                return false;
            }
            val = val / 60;
            if (presenceJours_ && !presenceHeures_ && (val % 24) != 0) {
                return false;
            }
            val = val / 24;
            if (presenceMois_ && !presenceJours_) {
                int m = 0;
                while (val >= (calendrier_[m % 12]) * 1440) {
                    val = val - (calendrier_[m % 12]) * 1440;
                    m++;
                }
                if (val != 0) {
                    return false;
                }
            }
            return true;
        }
    };

    final BuStringValidator stringDuree_ = new BuStringValidator() {

        public boolean isStringValid(String _string) {
            StringTokenizer token = new StringTokenizer(_string, getSep());
            if (token.countTokens() > nbEltsMax_) {
                return false;
            }
            return true;
        }

        public String valueToString(Object _value) {
            int minutes = ((Integer) _value).intValue();
            String nbMois = CtuluLibString.EMPTY_STRING;
            String nbJours = CtuluLibString.EMPTY_STRING;
            String nbHeures = CtuluLibString.EMPTY_STRING;
            String nbMinutes = CtuluLibString.EMPTY_STRING;
            if (presenceMois_) {
                int m = 0;
                while (minutes >= (calendrier_[m % 12]) * 1440) {
                    minutes -= (calendrier_[m % 12]) * 1440;
                    m++;
                }
                nbMois = (m < 10 ? CtuluLibString.ZERO + String.valueOf(m) : String.valueOf(m));
            }
            if (presenceJours_) {
                int jours = minutes / 1440;
                minutes -= jours * 1440;
                if (presenceMois_) {
                    nbJours = getSep();
                }
                nbJours += (jours < 10 ? CtuluLibString.ZERO + String.valueOf(jours) : String.valueOf(jours));
            }
            if (presenceHeures_) {
                int heures = minutes / 60;
                minutes -= heures * 60;
                if (presenceJours_) {
                    nbHeures = getSep();
                }
                nbHeures += (heures < 10 ? CtuluLibString.ZERO + String.valueOf(heures) : String.valueOf(heures));
            }
            if (presenceMinutes_) {
                if (presenceHeures_) {
                    nbMinutes = getSep();
                }
                nbMinutes += (minutes < 10 ? CtuluLibString.ZERO + String.valueOf(minutes) : String.valueOf(minutes));
            }
            return nbMois + nbJours + nbHeures + nbMinutes;
        }

        public Object stringToValue(String _chaine) {
            StringTokenizer token = new StringTokenizer(_chaine, getSep());
            int nbEltDonnes = token.countTokens();
            int nbEltsAttendus = nbEltsMax_;
            int min = 0;
            if (presenceMois_ && nbEltDonnes == nbEltsAttendus--) {
                int tempo = Integer.parseInt(token.nextToken());
                for (int i = 0; i < tempo; i++) {
                    min += +1440 * calendrier_[i % 12];
                }
            }
            if (presenceJours_ && nbEltDonnes >= nbEltsAttendus--) {
                min += 1440 * Integer.parseInt(token.nextToken());
            }
            if (presenceHeures_ && nbEltDonnes >= nbEltsAttendus--) {
                min += 60 * Integer.parseInt(token.nextToken());
            }
            if (presenceMinutes_ && nbEltDonnes >= nbEltsAttendus--) {
                min += Integer.parseInt(token.nextToken());
            }
            return new Integer(min);
        }
    };

    /**
   * Cr�ation d'une zone de texte. Le format des donn�es saisies devra respecter les champs demand�s. ATTENTION : restez
   * coherant si vous creez les mois et les heures, creez aussi les jours.
   * 
   * @param _m presence des mois.
   * @param _j presence des jours.
   * @param _h presence des heures.
   * @param _min presence des minutes.
   */
    public DureeField(final boolean _m, boolean _j, boolean _h, final boolean _min) {
        String textLabel = CtuluLibString.EMPTY_STRING;
        if ((_m) && (!_j) && (_h)) {
            throw new IllegalArgumentException("ERREUR de DureeField : manque les jours");
        }
        if ((_j) && (!_h) && (_min)) {
            throw new IllegalArgumentException("ERREUR de DureeField : manque les heures");
        }
        if ((_m) && (!_j) && (!_h) && (_min)) {
            throw new IllegalArgumentException("ERREUR de DureeField : manque les jours et les heures");
        }
        presenceMois_ = _m;
        presenceJours_ = _j;
        presenceHeures_ = _h;
        presenceMinutes_ = _min;
        zoneTexte_.setCharValidator(charDuree_);
        zoneTexte_.setValueValidator(valueDuree_);
        zoneTexte_.setStringValidator(stringDuree_);
        final BuGridLayout lodate = new BuGridLayout(2, 5, 5, false, false);
        setLayout(lodate);
        add(zoneTexte_);
        add(label_);
        zoneTexte_.setColumns(11);
        zoneTexte_.addFocusListener(this);
        if (presenceMois_) {
            textLabel += "m:";
            nbEltsMax_++;
        }
        if (presenceJours_) {
            textLabel += "j:";
            nbEltsMax_++;
        }
        if (presenceHeures_) {
            textLabel += "h:";
            nbEltsMax_++;
        }
        if (presenceMinutes_) {
            textLabel += "min:";
            nbEltsMax_++;
        }
        label_.setText(textLabel.substring(0, textLabel.length() - 1));
        setValue(0);
    }

    /**
   * Permet de lire la valeur de la zone de saisie (en nombre de minutes).
   */
    private int getDureeField() {
        final int val = ((Integer) zoneTexte_.getValue()).intValue();
        return val;
    }

    public long getDureeFieldLong() {
        return getDureeField();
    }

    /**
   * Permet de modifier la valeur de la zone de texte.
   * 
   * @param _m nombre de mois.
   * @param _j nombre de jours.
   * @param _h nombre d' heures.
   * @param min nombre de minutes.
   */
    public void setDureeField(final int _m, final int _j, final int _h, int _min) {
        int min = _min;
        if ((min >= 0) && (_h >= 0) && (_j >= 0) && (_m >= 0)) {
            min = min + _h * 60 + _j * 24 * 60;
            for (int i = 0; i < _m; i++) {
                min = min + 60 * 24 * calendrier_[i % 12];
            }
            setDureeField(min);
        } else {
            setDureeField(-1);
        }
    }

    /** Idem setDureeField(int m,int j,int h, int min). */
    public void setValue(final int _m, final int _j, final int _h, final int _min) {
        setDureeField(_m, _j, _h, _min);
    }

    /**
   * Permet de modifier la valeur de la zone de texte.
   * 
   * @param _val : nouvelle valeur, en minutes.
   */
    private void setDureeField(final int _val) {
        zoneTexte_.setValue(new Integer(_val));
    }

    public void setDureeField(final long _val) {
        zoneTexte_.setValue(new Integer((int) _val));
    }

    /** Idem setDureeField(int val). */
    public void setValue(final int _val) {
        setDureeField(_val);
    }

    public void setValue(final long _val) {
        setDureeField(_val);
    }

    /** Pour activer ou desactiver le composant. */
    public void setEnabled(final boolean _flag) {
        zoneTexte_.setEnabled(_flag);
        label_.setEnabled(_flag);
    }

    /** Quand on entre dans la zone de texte c'est que l'on entre dans le composant. */
    public void focusGained(final FocusEvent _e) {
        zoneTexte_.selectAll();
        processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_GAINED));
    }

    /** Quand on sort de la zone de texte c'est que l'on sort du composant. */
    public void focusLost(final FocusEvent _e) {
        processFocusEvent(new FocusEvent(this, FocusEvent.FOCUS_LOST));
    }

    /** Pour savoir si le composant est actif. Renvoie true si zoneTexte et label sont actifs */
    public boolean isEnabled() {
        final boolean flag1 = zoneTexte_.isEnabled();
        final boolean flag2 = label_.isEnabled();
        return (flag1 & flag2);
    }

    /**
   * M�thode permettant de formatter un nombre de minutes. S'il reste des minutes et qu'elles ne sont pas demand�es,
   * elles sont ajout�es automatiquement.
   * 
   * @param _pMois pr�sence des mois.
   * @param _pJours pr�sence des mois.
   * @param _pHeures pr�sence des mois.
   * @param _pMinutes pr�sence des mois.
   */
    public static String formatter(final boolean _pMois, final boolean _pJours, final boolean _pHeures, boolean _pMinutes, final int _minutes) {
        String retour = CtuluLibString.EMPTY_STRING;
        int minutes = _minutes;
        if (_pMois) {
            int m = 0;
            while (minutes >= (calendrier_[m % 12]) * 1440) {
                minutes -= (calendrier_[m % 12]) * 1440;
                m++;
            }
            retour += (m < 10 ? CtuluLibString.ZERO : CtuluLibString.EMPTY_STRING) + String.valueOf(m) + getSep();
        }
        if (_pJours) {
            final int jours = minutes / 1440;
            minutes -= jours * 1440;
            retour += (jours < 10 ? CtuluLibString.ZERO : CtuluLibString.EMPTY_STRING) + String.valueOf(jours) + getSep();
        }
        if (_pHeures) {
            final int heures = minutes / 60;
            minutes -= heures * 60;
            retour += (heures < 10 ? CtuluLibString.ZERO : CtuluLibString.EMPTY_STRING) + String.valueOf(heures) + getSep();
        }
        if (!_pMinutes && minutes > 0) {
            retour += (minutes < 10 ? CtuluLibString.ZERO : CtuluLibString.EMPTY_STRING) + String.valueOf(minutes) + "min:";
        } else if (_pMinutes) {
            retour += (minutes < 10 ? CtuluLibString.ZERO : CtuluLibString.EMPTY_STRING) + String.valueOf(minutes) + getSep();
        }
        return retour.substring(0, retour.length() - 1);
    }

    public static String formatter(final boolean _pMois, final boolean _pJours, final boolean _pHeures, final boolean _pMinutes, final long _minutes) {
        return formatter(_pMois, _pJours, _pHeures, _pMinutes, (int) _minutes);
    }

    public static String formatter(final boolean _pMois, final boolean _pJours, final boolean _pHeures, final boolean _pMinutes, final double _minutes) {
        return formatter(_pMois, _pJours, _pHeures, _pMinutes, (int) _minutes);
    }
}
