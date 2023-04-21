package org.fudaa.fudaa.modeleur.modeleur1d.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import org.fudaa.ctulu.CtuluCommandComposite;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluListSelectionEvent;
import org.fudaa.ctulu.CtuluListSelectionInterface;
import org.fudaa.ctulu.CtuluListSelectionListener;
import org.fudaa.ctulu.CtuluNumberFormatDefault;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ebli.commun.EbliActionInterface;
import org.fudaa.ebli.commun.EbliComponentFactory;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.palette.BPaletteInfo.InfoData;
import org.fudaa.ebli.trace.TraceIcon;
import org.fudaa.ebli.trace.TraceIconModel;
import org.fudaa.ebli.trace.TraceLigne;
import org.fudaa.ebli.trace.TraceLigneModel;
import org.fudaa.fudaa.modeleur.modeleur1d.model.ProfilContainerI;
import org.fudaa.fudaa.modeleur.modeleur1d.model.ProfilContainerException;
import org.fudaa.fudaa.modeleur.modeleur1d.model.ProfilContainerListener;
import org.fudaa.ebli.courbe.*;
import com.memoire.bu.BuBorderLayout;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuComboBox;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuSpecificBar;
import com.memoire.bu.BuToggleButton;
import java.util.Map;
import org.fudaa.ebli.commun.EbliActionPaletteAbstract;
import org.fudaa.fudaa.modeleur.modeleur1d.model.ProfileSetI;
import org.fudaa.fudaa.modeleur.modeleur1d.model.ProfileSetListener;
import org.fudaa.fudaa.modeleur.resource.MdlResource;

/**
 * Cette vue permet la modification et le visionnage d'un profil (pr�alablement
 * selectionn� dans la vue du bief).
 * 
 * Element important a propos de l'implementation de l'undo/redo :
 *  une gestion de l'undo/redo est d�j� pr�sente dans le framework de courbe, 
 *  mais il n'est pas utilis� pour g�rer l'undo/redo. La raison est qu'il permet 
 *  d'annuler trop d'action (notamment l'ajout de courbe) ce qui peut provoquer 
 *  des comportements incoh�rent dans l'application. C'est pour cette raison que 
 *  tous les undo/redo passent � la place par le gestionnaire undo/redo du
 *  controller1d.
 * 
 * @author Emmanuel MARTIN
 * @version $Id: VueCourbe.java 6860 2011-12-18 20:48:39Z bmarchan $
 */
public class VueCourbe extends BuPanel implements ProfileSetListener {

    /**
   * Petite classe utilie pour le combo box de choix de ligne directrice.
   * Il permet de concerver l'index de la ligne directrice en plus de son nom.
   * @author Emmanuel MARTIN
   * @version $Id: VueCourbe.java 6860 2011-12-18 20:48:39Z bmarchan $
   */
    protected class PaireLd {

        public int idx;

        public String name;

        public PaireLd(int _idx, String _name) {
            idx = _idx;
            name = _name;
        }

        public String toString() {
            return name;
        }
    }

    /**
   * Ce nouveau model de courbe permet d'utiliser un model d�j� existant sous la
   * forme d'une DataGeometry.
   */
    protected class CourbeGeomModel implements EGModel, ProfilContainerListener {

        private ProfilContainerI data_;

        /** Un commande manager temporaire. */
        private CtuluCommandContainer cmd_;

        /** L'offset de d�calage pour l'alignement en X*/
        double xoffSet_;

        /** L'offset de d�calage pour l'alignement en Z*/
        double zoffSet_;

        public CourbeGeomModel() {
        }

        /**
     * @return Retourne le profil associ�.
     */
        public ProfilContainerI getProfil() {
            return data_;
        }

        public void setProfil(ProfilContainerI _prf) {
            if (data_ == _prf) return;
            if (data_ != null) data_.removeProfilContainerListener(this);
            data_ = _prf;
            if (data_ != null) data_.addProfilContainerListener(this);
        }

        public void setOffsets(double _xoffSet, double _zoffSet) {
            xoffSet_ = _xoffSet;
            zoffSet_ = _zoffSet;
        }

        /**
     * Place l'intersection du profil avec la limite de stockage gauche �
     * l'index _idx.
     */
        public void setLimiteGauche(int _idx) {
            if (data_ == null) return;
            controller_.clearError();
            try {
                data_.setLimiteGauche(_idx, controller_.getCommandContainer());
            } catch (ProfilContainerException _e) {
                controller_.showError(_e.getMessage());
            }
        }

        /**
     * Place l'intersection du profil avec la limite de stockage droite �
     * l'index _idx.
     */
        public void setLimiteDroite(int _idx) {
            if (data_ == null) return;
            controller_.clearError();
            try {
                data_.setLimiteDroite(_idx, controller_.getCommandContainer());
            } catch (ProfilContainerException _e) {
                controller_.showError(_e.getMessage());
            }
        }

        /**
     * Place l'intersection du profil avec la rive gauche � l'index _idx.
     */
        public void setRiveGauche(int _idx) {
            if (data_ == null) return;
            controller_.clearError();
            try {
                data_.setRiveGauche(_idx, controller_.getCommandContainer());
            } catch (ProfilContainerException _e) {
                controller_.showError(_e.getMessage());
            }
        }

        /**
     * Place l'intersection du profil avec la rive droite � l'index _idx.
     */
        public void setRiveDroite(int _idx) {
            if (data_ == null) return;
            controller_.clearError();
            try {
                data_.setRiveDroite(_idx, controller_.getCommandContainer());
            } catch (ProfilContainerException _e) {
                controller_.showError(_e.getMessage());
            }
        }

        /**
     * Place au point voulu le passage de la ligne directrice.
     */
        public void setLigneDirectriceAt(int _idxPoint, int _idxLd) {
            if (data_ == null) return;
            controller_.clearError();
            try {
                data_.setLigneDirectriceAt(_idxLd, _idxPoint, controller_.getCommandContainer());
            } catch (ProfilContainerException _e) {
                controller_.showError(_e.getMessage());
            }
        }

        /**
     * Retourne les noms des lignes directrices d�placable vers ce point.
     */
        protected PaireLd[] getAllowedLignesDirectricesAt(int _idxPoint) {
            if (data_ == null) return null;
            int[] idx = data_.getAllowedMoveOfLignesDirectricesTo(_idxPoint);
            PaireLd[] lds = new PaireLd[idx.length];
            for (int i = 0; i < lds.length; i++) lds[i] = new PaireLd(idx[i], data_.getLigneDirectriceName(idx[i]));
            return lds;
        }

        public void profilContainerDataModified() {
            updateCurves();
        }

        public boolean addValue(double _x, double _y, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean addValue(double[] _x, double[] _y, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean deplace(int[] idx, double _deltax, double _deltay, CtuluCommandContainer _cmd) {
            if (data_ == null) return false;
            Arrays.sort(idx);
            boolean createCmd = false;
            if (cmd_ == null) {
                createCmd = true;
                cmd_ = new CtuluCommandComposite(MdlResource.getS("D�placement d'un ou plusieurs points"));
            }
            if (_deltax < 0) for (int index : idx) setValue(index, getX(index) + _deltax, getY(index) + _deltay, cmd_); else for (int i = idx.length - 1; i >= 0; i--) setValue(idx[i], getX(idx[i]) + _deltax, getY(idx[i]) + _deltay, cmd_);
            if (createCmd) {
                if (controller_.getCommandContainer() != null) controller_.getCommandContainer().addCmd(((CtuluCommandComposite) cmd_).getSimplify());
                cmd_ = null;
            }
            return true;
        }

        public void fillWithInfo(InfoData _table, CtuluListSelectionInterface pt) {
        }

        public String getPointLabel(int _i) {
            if (data_ == null) return null;
            String[] names = data_.getNamesLignesDirectricesAt(_i);
            if (names.length == 0) return null;
            String label = "";
            for (int i = 0; i < names.length; i++) label += names[i];
            return label;
        }

        public int getNbValues() {
            if (data_ == null) return 0;
            return data_.getNbPoint();
        }

        public String getTitle() {
            return MdlResource.getS("Courbe");
        }

        public double getX(int _idx) {
            if (data_ == null) return 0;
            return data_.getCurv(_idx) - xoffSet_;
        }

        public double getXMax() {
            if (data_ == null) return 0;
            return data_.getCurvMax() - xoffSet_;
        }

        public double getXMin() {
            if (data_ == null) return 0;
            return data_.getCurvMin() - xoffSet_;
        }

        public double getY(int _idx) {
            if (data_ == null) return 0;
            return data_.getZ(_idx) - zoffSet_;
        }

        public double getYMax() {
            if (data_ == null) return 0;
            return data_.getZMax() - zoffSet_;
        }

        public double getYMin() {
            if (data_ == null) return 0;
            return data_.getZMin() - zoffSet_;
        }

        public boolean isDuplicatable() {
            return false;
        }

        public boolean isModifiable() {
            return true;
        }

        public boolean isPointDrawn(int _i) {
            return true;
        }

        public boolean isRemovable() {
            return true;
        }

        public boolean isSegmentDrawn(int _i) {
            return true;
        }

        public boolean isTitleModifiable() {
            return false;
        }

        public boolean isXModifiable() {
            return true;
        }

        public boolean removeValue(int _i, CtuluCommandContainer _cmd) {
            if (data_ == null) return false;
            try {
                boolean createCmd = false;
                if (cmd_ == null) {
                    createCmd = true;
                    cmd_ = new CtuluCommandComposite(MdlResource.getS("Suppression d'un point"));
                }
                data_.remove(_i, cmd_);
                if (createCmd) {
                    if (controller_.getCommandContainer() != null) controller_.getCommandContainer().addCmd(((CtuluCommandComposite) cmd_).getSimplify());
                    cmd_ = null;
                }
                return true;
            } catch (ProfilContainerException _exc) {
                controller_.showError(_exc.getMessage());
                return false;
            }
        }

        public boolean removeValue(int[] _i, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean setTitle(String name) {
            return false;
        }

        public boolean setValue(int _i, double _x, double _y, CtuluCommandContainer _cmd) {
            if (data_ == null) return false;
            controller_.clearError();
            try {
                boolean createCmd = false;
                if (cmd_ == null) {
                    createCmd = true;
                    cmd_ = new CtuluCommandComposite(MdlResource.getS("D�placement d'un point"));
                }
                data_.setValues(_i, _x, _y, cmd_);
                if (createCmd) {
                    if (controller_.getCommandContainer() != null) controller_.getCommandContainer().addCmd(((CtuluCommandComposite) cmd_).getSimplify());
                    cmd_ = null;
                }
                return true;
            } catch (ProfilContainerException _exc) {
                controller_.showError(_exc.getMessage());
                return false;
            }
        }

        public boolean setValues(int[] _idx, double[] _x, double[] _y, CtuluCommandContainer _cmd) {
            if (data_ == null) return false;
            boolean createCmd = false;
            if (cmd_ == null) {
                createCmd = true;
                cmd_ = new CtuluCommandComposite(MdlResource.getS("D�placement d'un ou plusieurs points"));
            }
            for (int index : _idx) setValue(index, _x[index], _y[index], cmd_);
            if (createCmd) {
                if (controller_.getCommandContainer() != null) controller_.getCommandContainer().addCmd(((CtuluCommandComposite) cmd_).getSimplify());
                cmd_ = null;
            }
            return true;
        }

        public void restoreFromSpecificDatas(Object _data, Map _infos) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public EGModel duplicate() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void viewGenerationSource(Map infos, CtuluUI impl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void replayData(EGGrapheTreeModel model, Map infos, CtuluUI impl) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Object savePersistSpecificDatas() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void replayData(EGGrapheModel model, Map infos, CtuluUI impl) {
        }
    }

    protected class EmptyCurveModel implements EGModel {

        public boolean isRemovable() {
            return true;
        }

        public boolean isDuplicatable() {
            return false;
        }

        public int getNbValues() {
            return 0;
        }

        public boolean isSegmentDrawn(int _i) {
            return false;
        }

        public boolean isPointDrawn(int _i) {
            return false;
        }

        public String getPointLabel(int _i) {
            return null;
        }

        public double getX(int _idx) {
            return 0;
        }

        public double getY(int _idx) {
            return 0;
        }

        public double getXMin() {
            return 0;
        }

        public double getXMax() {
            return 0;
        }

        public double getYMin() {
            return 0;
        }

        public double getYMax() {
            return 0;
        }

        public boolean isModifiable() {
            return false;
        }

        public boolean isXModifiable() {
            return false;
        }

        public boolean setValue(int _i, double _x, double _y, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean addValue(double _x, double _y, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean addValue(double[] _x, double[] _y, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean removeValue(int _i, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean removeValue(int[] _i, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean deplace(int[] _selectIdx, double _deltaX, double _deltaY, CtuluCommandContainer _cmd) {
            return false;
        }

        public boolean setValues(int[] _idx, double[] _x, double[] _y, CtuluCommandContainer _cmd) {
            return false;
        }

        public String getTitle() {
            return null;
        }

        public boolean isTitleModifiable() {
            return false;
        }

        public boolean setTitle(String _newName) {
            return false;
        }

        public void fillWithInfo(InfoData _table, CtuluListSelectionInterface _selectedPt) {
        }

        public EGModel duplicate() {
            return null;
        }

        public void viewGenerationSource(Map infos, CtuluUI impl) {
        }

        public void replayData(EGGrapheTreeModel model, Map infos, CtuluUI impl) {
        }

        public Object savePersistSpecificDatas() {
            return null;
        }

        public void restoreFromSpecificDatas(Object data, Map infos) {
        }

        @Override
        public void replayData(EGGrapheModel model, Map infos, CtuluUI impl) {
        }
    }

    /**
   * Enum�ration pour l'alignement du profil suivant X et Z.
   */
    public enum Alignment {

        /** Alignement X sur l'abscisse 0 */
        LEFT, /** Alignement X sur la zone de stockage gauche */
        LEFT_STORE, /** Alignement X sur la rive gauche */
        LEFT_BANK, /** Alignement X sur l'abscisse maxi */
        RIGHT, /** Alignement X sur la zone de stockage droit */
        RIGHT_STORE, /** Alignement X sur la rive droite */
        RIGHT_BANK, /** Alignement X sur l'axe hydraulique */
        AXIS, /** Alignement X sur une ligne directrice */
        DIR_LINE, /** Alignement X sur le point de Z min */
        X_ZMIN, /** Alignement Z par defaut (tous les profils dans le m�me systeme) */
        Z_IDENT, /** Alignement Z sur les Z min */
        Z_MIN
    }

    /** L'alignement des courbes suivant X */
    private Alignment curveXAlignment_ = Alignment.LEFT;

    /** L'alignement des courbes suivant Z */
    private Alignment curveZAlignment_ = Alignment.Z_IDENT;

    /** L'indice de la ligne directrice pour l'alignement en X */
    private int indXAlignment_;

    /** La vue ou est affich�e le panneau de courbe. */
    VueProfilI controller_;

    /** La courbe courante. */
    private EGCourbeSimple courbe_;

    /** Le container principal de la courbe. */
    private EGGraphe grapheVue_;

    /** Le container principal de la courbe. */
    private EGFillePanel containerCourbe_;

    /** Le model de la courbe courante */
    CourbeGeomModel courbeModel_;

    /** La courbe pr�c�dente */
    EGCourbeSimple prevCurve_;

    /** La courbe suivante */
    EGCourbeSimple nextCurve_;

    BuButton btLimiteGauche_;

    BuButton btRiveGauche_;

    BuButton btRiveDroite_;

    BuButton btLimiteDroite_;

    BuComboBox coLignesDirectrices_;

    BuToggleButton btShowPrevNext_;

    private EGGrapheSimpleModel grapheModel_;

    private EGAxeVertical axeY_;

    private ProfileSetI profSet_;

    private int selectedPrf_ = -1;

    private boolean isPrevNextVisible_ = false;

    public VueCourbe(VueProfilI _controller, ProfileSetI _profSet) {
        controller_ = _controller;
        profSet_ = _profSet;
        if (profSet_ != null) profSet_.addListener(this);
        grapheModel_ = new EGGrapheSimpleModel();
        grapheVue_ = new EGGraphe(grapheModel_);
        EGAxeHorizontal axeX = new EGAxeHorizontal(false);
        axeX.setTitre(MdlResource.getS("Abscisse en travers"));
        axeX.setUnite("m");
        axeX.setBounds(0, 500);
        axeX.setGraduations(true);
        grapheVue_.setXAxe(axeX);
        axeY_ = new EGAxeVertical();
        axeY_.setGraduations(true);
        axeY_.setTraceGraduations(new TraceLigneModel(TraceLigne.POINTILLE, 1, Color.LIGHT_GRAY));
        axeY_.setTitre("Z");
        axeY_.setUnite("m");
        DecimalFormat df = CtuluLib.getDecimalFormat();
        df.setMaximumFractionDigits(2);
        axeY_.setSpecificFormat(new CtuluNumberFormatDefault(df));
        axeY_.setBounds(0, 50);
        axeY_.setLineColor(Color.BLACK);
        containerCourbe_ = new EGFillePanel(grapheVue_);
        setLayout(new BuBorderLayout());
        List<JComponent> actions = new ArrayList<JComponent>();
        buildToolButtons(actions);
        updateToolBar();
        BuSpecificBar speBar = new BuSpecificBar();
        speBar.addTools(actions.toArray(new JComponent[0]));
        add(speBar, BuBorderLayout.NORTH);
        add(containerCourbe_, BuBorderLayout.CENTER);
        addSelectionListener(new CtuluListSelectionListener() {

            public void listeSelectionChanged(CtuluListSelectionEvent _e) {
                int[] idx = getSelection();
                coLignesDirectrices_.removeAllItems();
                coLignesDirectrices_.addItem(new PaireLd(-1, MdlResource.getS("LD")));
                if (idx != null && idx.length == 1) {
                    PaireLd[] paireLd = courbeModel_.getAllowedLignesDirectricesAt(idx[0]);
                    for (int i = 0; i < paireLd.length; i++) coLignesDirectrices_.addItem(paireLd[i]);
                }
            }
        });
        setPreferredSize(new Dimension(200, 200));
        buildCurves();
        updateCurves();
    }

    private void buildToolButtons(List<JComponent> _actions) {
        EbliActionInterface[] actionAbs = containerCourbe_.getSpecificActions();
        EbliLib.updateMapKeyStroke(this, actionAbs);
        HashSet<String> actionsKept = new HashSet<String>(Arrays.asList(new String[] { "SELECT", "RESTORE", "AUTO_REST", "CONFIGURE_REPERE", "ZOOM", "SUIVI", "MOVE_POINT", "CONFIGURE", "INFOS", "TABLE" }));
        for (int i = 0; i < actionAbs.length; i++) {
            if (actionAbs[i] != null && actionsKept.contains(actionAbs[i].getValue(Action.ACTION_COMMAND_KEY))) {
                AbstractButton button = actionAbs[i].buildToolButton(EbliComponentFactory.INSTANCE);
                button.setText("");
                _actions.add(button);
            }
        }
        btShowPrevNext_ = new BuToggleButton(MdlResource.MDL.getIcon("profils_pn"));
        btShowPrevNext_.setToolTipText(MdlResource.getS("Visualiser les profils pr�c�dent/suivant"));
        btShowPrevNext_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setPrevNextVisible(((BuToggleButton) e.getSource()).isSelected());
            }
        });
        _actions.add(btShowPrevNext_);
        EbliActionPaletteAbstract btAlign = new EbliActionPaletteAbstract(MdlResource.getS("Alignement des profils"), MdlResource.MDL.getIcon("aligner-profils"), "ALIGNMENT") {

            @Override
            protected JComponent buildContentPane() {
                return new CurveOptionPanel(VueCourbe.this);
            }
        };
        _actions.add(btAlign.buildToolButton(EbliComponentFactory.INSTANCE));
        btLimiteGauche_ = new BuButton(MdlResource.getS("SG"));
        btLimiteGauche_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int[] idx = getSelection();
                if (idx != null && idx.length == 1) courbeModel_.setLimiteGauche(idx[0]);
            }
        });
        _actions.add(btLimiteGauche_);
        btRiveGauche_ = new BuButton(MdlResource.getS("RG"));
        btRiveGauche_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int[] idx = getSelection();
                if (idx != null && idx.length == 1) courbeModel_.setRiveGauche(idx[0]);
            }
        });
        _actions.add(btRiveGauche_);
        btRiveDroite_ = new BuButton(MdlResource.getS("RD"));
        btRiveDroite_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int[] idx = getSelection();
                if (idx != null && idx.length == 1) courbeModel_.setRiveDroite(idx[0]);
            }
        });
        _actions.add(btRiveDroite_);
        btLimiteDroite_ = new BuButton(MdlResource.getS("SD"));
        btLimiteDroite_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int[] idx = getSelection();
                if (idx != null && idx.length == 1) courbeModel_.setLimiteDroite(idx[0]);
            }
        });
        _actions.add(btLimiteDroite_);
        coLignesDirectrices_ = new BuComboBox();
        coLignesDirectrices_.addItem(new PaireLd(-1, MdlResource.getS("LD")));
        coLignesDirectrices_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int[] idx = getSelection();
                if (idx != null && idx.length == 1) {
                    PaireLd paireLd = (PaireLd) coLignesDirectrices_.getSelectedItem();
                    if (paireLd != null && paireLd.idx != -1) courbeModel_.setLigneDirectriceAt(idx[0], paireLd.idx);
                }
            }
        });
        _actions.add(coLignesDirectrices_);
    }

    public void profilRemoved(int _idxProfil) {
        updateCurves();
    }

    public void profilAdded(int _idxProfil) {
        updateCurves();
    }

    /**
   * Change de profil set.
   * @param _profSet Le profilSet
   */
    public void setProfilSet(ProfileSetI _profSet) {
        if (_profSet == profSet_) return;
        if (profSet_ != null) {
            profSet_.removeListener(this);
        }
        profSet_ = _profSet;
        if (profSet_ != null) {
            profSet_.addListener(this);
        }
        selectedPrf_ = -1;
        updateCurves();
        updateToolBar();
    }

    public void setSelectedProfile(int _prf) {
        selectedPrf_ = _prf;
        updateCurves();
        updateToolBar();
    }

    public int getSelectedProfile() {
        return selectedPrf_;
    }

    /**
   * @return Retourne le profil set associ�.
   */
    public ProfileSetI getProfilSet() {
        return profSet_;
    }

    private void updateToolBar() {
        boolean isBief = profSet_ != null && profSet_.isBief();
        btRiveGauche_.setEnabled(isBief);
        btRiveDroite_.setEnabled(isBief);
        btLimiteGauche_.setEnabled(isBief);
        btLimiteDroite_.setEnabled(isBief);
        coLignesDirectrices_.setEnabled(isBief);
    }

    /**
   * Affiche ou non les profils pr�c�dents et suivant.
   */
    public void setPrevNextVisible(boolean _b) {
        isPrevNextVisible_ = _b;
        btShowPrevNext_.setSelected(isPrevNextVisible_);
        updateCurves();
    }

    /**
   * Aligne les courbes suivant X sur une ligne particuliere.
   * @param _align L'alignement
   * @param _ind L'indice (utilis� uniquement pour les lignes directrices).
   */
    public void setXAlignment(Alignment _align, int _ind) {
        curveXAlignment_ = _align;
        indXAlignment_ = _ind;
        updateCurves();
    }

    public void setZAlignment(Alignment _align) {
        curveZAlignment_ = _align;
        updateCurves();
    }

    /**
   * @param _pf Le profil � d�caler suivant Z.
   * @param _pfRef Le profil r�f�rence.
   * @return L'offset de d�calage sur Z pour un calage des courbes sur l'alignement
   * d�finit.
   */
    private double computeZOffset(ProfilContainerI _pf, ProfilContainerI _pfRef) {
        switch(curveZAlignment_) {
            case Z_IDENT:
            default:
                return 0;
            case Z_MIN:
                return _pf.getZMin() - _pfRef.getZMin();
        }
    }

    /**
   * @param _pf Le profil � d�caler suivant X.
   * @param _pfRef Le profil r�f�rence.
   * @return L'offset de d�calage sur X pour un calage des courbes sur l'alignement
   * d�finit.
   */
    private double computeXOffset(ProfilContainerI _pf, ProfilContainerI _pfRef) {
        switch(curveXAlignment_) {
            case LEFT:
            default:
                return 0;
            case LEFT_STORE:
                return _pf.getAbsCurvLimiteStockageGauche() - _pfRef.getAbsCurvLimiteStockageGauche();
            case LEFT_BANK:
                return _pf.getAbsCurvRiveGauche() - _pfRef.getAbsCurvRiveGauche();
            case RIGHT:
                return _pf.getCurvMax() - _pfRef.getCurvMax();
            case RIGHT_STORE:
                return _pf.getAbsCurvLimiteStockageDroite() - _pfRef.getAbsCurvLimiteStockageDroite();
            case RIGHT_BANK:
                return _pf.getAbsCurvRiveDroite() - _pfRef.getAbsCurvRiveDroite();
            case AXIS:
                return _pf.getAbsCurvAxeHydrauliqueOnProfil() - _pfRef.getAbsCurvAxeHydrauliqueOnProfil();
            case DIR_LINE:
                int iptPf = _pf.getPointForDirLine(indXAlignment_);
                int iptPfRef = _pfRef.getPointForDirLine(indXAlignment_);
                return (iptPf == -1 || iptPfRef == -1) ? 0 : _pf.getCurv(iptPf) - _pfRef.getCurv(iptPfRef);
            case X_ZMIN:
                iptPf = 0;
                iptPfRef = 0;
                double zmin;
                zmin = Double.POSITIVE_INFINITY;
                for (int i = 0; i < _pf.getNbPoint(); i++) {
                    double z = _pf.getZ(i);
                    if (zmin > z) {
                        zmin = z;
                        iptPf = i;
                    }
                }
                zmin = Double.POSITIVE_INFINITY;
                for (int i = 0; i < _pfRef.getNbPoint(); i++) {
                    double z = _pfRef.getZ(i);
                    if (zmin > z) {
                        zmin = z;
                        iptPfRef = i;
                    }
                }
                return _pf.getCurv(iptPf) - _pfRef.getCurv(iptPfRef);
        }
    }

    /**
   * Construction des courbes, une seule fois. Le modele de chaque courbe est ensuite
   * modifi�.
   */
    private void buildCurves() {
        Color c;
        c = Color.YELLOW.darker();
        prevCurve_ = new EGCourbeSimple(axeY_, new CourbeGeomModel());
        prevCurve_.setLigneModel(new TraceLigneModel(TraceLigne.TIRETE, 0.5f, c));
        prevCurve_.setAspectContour(c);
        prevCurve_.setIconeModel(new TraceIconModel(TraceIcon.CARRE_PLEIN, 1, c));
        grapheModel_.addCourbe(prevCurve_, null);
        c = Color.GREEN.darker();
        nextCurve_ = new EGCourbeSimple(axeY_, new CourbeGeomModel());
        nextCurve_.setLigneModel(new TraceLigneModel(TraceLigne.TIRETE, 0.5f, c));
        nextCurve_.setAspectContour(c);
        nextCurve_.setIconeModel(new TraceIconModel(TraceIcon.CARRE_PLEIN, 1, c));
        grapheModel_.addCourbe(nextCurve_, null);
        c = Color.RED;
        courbe_ = new EGCourbeSimple(axeY_, courbeModel_ = new CourbeGeomModel());
        courbe_.setLigneModel(new TraceLigneModel(TraceLigne.LISSE, 1, c));
        courbe_.setAspectContour(c);
        courbe_.setIconeModel(new TraceIconModel(TraceIcon.CARRE_PLEIN, 2, c));
        courbe_.setDisplayPointLabels(true);
        grapheModel_.addCourbe(courbe_, null);
    }

    /**
   * Mise a jour des courbes, par changement de mod�le.
   */
    private void updateCurves() {
        if (profSet_ == null) return;
        int idCurrent = selectedPrf_;
        CourbeGeomModel md = (CourbeGeomModel) prevCurve_.getModel();
        if (isPrevNextVisible_ && idCurrent > 0) {
            ProfilContainerI pf = profSet_.getProfil(idCurrent - 1);
            double xoff = computeXOffset(pf, profSet_.getProfil(idCurrent));
            double zoff = computeZOffset(pf, profSet_.getProfil(idCurrent));
            md.setProfil(pf);
            md.setOffsets(xoff, zoff);
            prevCurve_.setVisible(true);
        } else {
            md.setProfil(null);
            prevCurve_.setVisible(false);
        }
        md = (CourbeGeomModel) nextCurve_.getModel();
        if (isPrevNextVisible_ && idCurrent != -1 && idCurrent < profSet_.getNbProfil() - 1) {
            ProfilContainerI pf = profSet_.getProfil(idCurrent + 1);
            double xoff = computeXOffset(pf, profSet_.getProfil(idCurrent));
            double zoff = computeZOffset(pf, profSet_.getProfil(idCurrent));
            md.setProfil(pf);
            md.setOffsets(xoff, zoff);
            nextCurve_.setVisible(true);
        } else {
            md.setProfil(null);
            nextCurve_.setVisible(false);
        }
        md = (CourbeGeomModel) courbe_.getModel();
        if (idCurrent != -1) {
            md.setProfil(profSet_.getProfil(idCurrent));
            courbe_.setVisible(true);
        } else {
            md.setProfil(null);
            courbe_.setVisible(false);
        }
        updateLabels();
        grapheVue_.structureChanged();
    }

    /**
   * Mise a jour des lignes remarquables sur le graphe pour �tre en conformit�
   * avec le profil courant.
   */
    private void updateLabels() {
        if (profSet_ == null || !profSet_.isBief()) return;
        int idCurrent = selectedPrf_;
        if (idCurrent == -1) return;
        courbe_.setMarqueurs(null);
        double absCurv = courbeModel_.getProfil().getAbsCurvRiveGauche();
        if (absCurv != -1) {
            courbe_.addMarqueur(new EGCourbeMarqueur(absCurv, MdlResource.getS("RG"), true, new TraceLigneModel(TraceLigne.POINTILLE, 1, Color.blue), false));
        }
        absCurv = courbeModel_.getProfil().getAbsCurvRiveDroite();
        if (absCurv != -1) {
            courbe_.addMarqueur(new EGCourbeMarqueur(absCurv, MdlResource.getS("RD"), true, new TraceLigneModel(TraceLigne.POINTILLE, 1, Color.blue), false));
        }
        absCurv = courbeModel_.getProfil().getAbsCurvAxeHydrauliqueOnProfil();
        if (absCurv != -1) {
            courbe_.addMarqueur(new EGCourbeMarqueur(absCurv, MdlResource.getS("AH"), true, new TraceLigneModel(TraceLigne.POINTILLE, 1, Color.green.darker()), false));
        }
        absCurv = courbeModel_.getProfil().getAbsCurvLimiteStockageGauche();
        if (absCurv != -1) {
            courbe_.addMarqueur(new EGCourbeMarqueur(absCurv, MdlResource.getS("SG"), true, new TraceLigneModel(TraceLigne.POINTILLE, 1, Color.green), false));
        }
        absCurv = courbeModel_.getProfil().getAbsCurvLimiteStockageDroite();
        if (absCurv != -1) {
            courbe_.addMarqueur(new EGCourbeMarqueur(absCurv, MdlResource.getS("SD"), true, new TraceLigneModel(TraceLigne.POINTILLE, 1, Color.green), false));
        }
    }

    /** Ajout un listener � la selection dans le tableau. */
    public void addSelectionListener(CtuluListSelectionListener _listener) {
        containerCourbe_.getSelection().addListeSelectionListener(_listener);
    }

    /** Retourne un tableau contenant les index selectionn�s. */
    public int[] getSelection() {
        return containerCourbe_.getSelection().getSelectedIndex();
    }

    /** Selectionne les points dont les indices sont pass�s en param�tre. */
    public void setSelection(int[] _idxSelection) {
        containerCourbe_.getSelection().clear();
        if (_idxSelection != null) for (int i = 0; i < _idxSelection.length; i++) containerCourbe_.getSelection().add(_idxSelection[i]);
    }
}
