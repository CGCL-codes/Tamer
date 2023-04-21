package org.fudaa.fudaa.commun.exec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.StringTokenizer;
import com.memoire.bu.BuMenu;
import com.memoire.bu.BuMenuItem;
import com.memoire.bu.BuPreferences;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.fudaa.commun.FudaaUI;
import org.fudaa.fudaa.commun.exec.FudaaExec.LaunchAction;

/**
 * Classe permettant de gerer les executables exterieures pour une application pr�cise.Les ex�cutables sont enregistres
 * (ou supprimer) dans les preferences de bu. La liste des preferences est enregistree dans les preferences de
 * l'application.
 * 
 * @author deniger
 * @version $Id: FudaaAppliManagerImpl.java,v 1.14 2007-02-02 11:22:15 deniger Exp $
 */
public class FudaaAppliManagerImpl extends Observable {

    BuPreferences prefAppli_;

    List actions_;

    /**
   * La cle preference pour la liste des executables.
   */
    private static final String LIST_EXE_PREF = "executable.list";

    private String prefForList_;

    private FudaaGlobalExecManager execMng_;

    /**
   * @param _prefAppli les preferences contenant la liste.
   */
    public FudaaAppliManagerImpl(final BuPreferences _prefAppli) {
        this(_prefAppli, null);
    }

    /**
   * @param _prefAppli les preferences stockant les exe
   * @param _prefForList
   */
    public FudaaAppliManagerImpl(final BuPreferences _prefAppli, final String _prefForList) {
        prefAppli_ = _prefAppli;
        prefForList_ = _prefForList;
        if (prefForList_ == null) {
            prefForList_ = CtuluLibString.EMPTY_STRING;
        }
        reload();
    }

    private String getKeyForList() {
        return prefForList_.length() == 0 ? LIST_EXE_PREF : prefForList_ + CtuluLibString.DOT + LIST_EXE_PREF;
    }

    /**
   * @return la liste des FudaaExec
   */
    public List getFudaaExecList() {
        final int nb = getActionsNb();
        final List r = new ArrayList(nb > 0 ? nb : 10);
        for (int i = 0; i < nb; i++) {
            r.add(actions_.get(i));
        }
        return r;
    }

    private void reload() {
        final String exeList = prefAppli_.getStringProperty(getKeyForList(), null);
        if (exeList == null) {
            actions_ = null;
        } else {
            final StringTokenizer tk = new StringTokenizer(exeList, CtuluLibString.VIR);
            final int n = tk.countTokens();
            actions_ = new ArrayList(n);
            if (execMng_ == null) {
                execMng_ = new FudaaGlobalExecManager();
            }
            for (int i = 0; i < n; i++) {
                actions_.add(execMng_.getFudaaExec(tk.nextToken()));
            }
        }
    }

    /**
   * @param _i l'indice de l'exe
   * @return L'exe demand�
   */
    public FudaaExec getExec(final int _i) {
        return (FudaaExec) actions_.get(_i);
    }

    /**
   * @return le nb d'action
   */
    public int getActionsNb() {
        return actions_ == null ? 0 : actions_.size();
    }

    /**
   * @param _e l'exe
   * @return l'item permettant de lancer l'exe _e.
   */
    public static BuMenuItem createMenuItem(final FudaaExec _e) {
        if (_e != null) {
            final BuMenuItem r = new BuMenuItem();
            r.setAction(_e.getAction());
            return r;
        }
        return null;
    }

    /**
   * Ajoute au menu _m toutes les actions concernant les applications.
   * 
   * @param _m le menu qui recoit les actions
   */
    public void createMenuItems(final BuMenu _m, final FudaaUI _parent, final File _dir) {
        final int n = getActionsNb();
        BuMenuItem it;
        for (int i = 0; i < n; i++) {
            it = new BuMenuItem();
            final FudaaExec e = FudaaAppliManagerImpl.this.getExec(i);
            if (e != null) {
                final LaunchAction action = e.getAction();
                action.setDir(_dir);
                action.setUi(_parent);
                it.setAction(action);
            }
            _m.add(it);
        }
    }

    /**
   * @return un panneau donnant tous les exe g�r�s
   */
    public CtuluDialogPanel createPanel() {
        return new FudaaExecManagerPanel(this);
    }

    /**
   * @param _news
   */
    public void setNewValues(final FudaaExec[] _news) {
        if (_news == null) {
            prefAppli_.removeProperty(getKeyForList());
            actions_ = null;
            setChanged();
        } else {
            final int n = _news.length;
            final StringBuffer b = new StringBuffer(50);
            if (n > 0) {
                b.append(_news[0].getIDName());
                for (int i = 1; i < n; i++) {
                    b.append(CtuluLibString.VIR);
                    b.append(_news[i].getIDName());
                }
            }
            if (b.length() == 0) {
                prefAppli_.removeProperty(getKeyForList());
            } else {
                prefAppli_.putStringProperty(getKeyForList(), b.toString());
            }
            reload();
            setChanged();
        }
        notifyObservers();
    }

    /**
   * @return Returns the execMng.
   */
    public FudaaGlobalExecManager getExecMng() {
        if (execMng_ == null) {
            execMng_ = new FudaaGlobalExecManager();
        }
        return execMng_;
    }
}
