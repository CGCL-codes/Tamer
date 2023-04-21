package org.fudaa.fudaa.sig.layer;

import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.fudaa.ctulu.CtuluCommandManager;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluUndoRedoInterface;
import org.fudaa.ebli.calque.BArbreCalque;
import org.fudaa.ebli.calque.BGroupeCalque;
import org.fudaa.ebli.calque.ZCalqueGrille;
import org.fudaa.ebli.calque.ZEbliCalquePanelController;
import org.fudaa.ebli.calque.ZEbliCalquesPanel;
import org.fudaa.ebli.calque.action.CalqueGISEditionAction;
import org.fudaa.ebli.calque.action.EditVisibilityAction;
import org.fudaa.ebli.calque.action.SceneDeplacementAction;
import org.fudaa.ebli.calque.action.SceneRotationAction;
import org.fudaa.ebli.calque.action.SceneSelectNextAction;
import org.fudaa.ebli.calque.action.SceneSelectPreviousAction;
import org.fudaa.ebli.calque.action.SceneShowDistanceAction;
import org.fudaa.ebli.calque.edition.ZCalqueDeplacementInteraction;
import org.fudaa.ebli.calque.edition.ZCalqueEditionGroup;
import org.fudaa.ebli.calque.edition.ZEditorDefault;
import org.fudaa.ebli.commun.EbliActionInterface;
import org.fudaa.ebli.commun.EbliComponentFactory;
import org.fudaa.fudaa.commun.impl.FudaaCommonImplementation;
import org.fudaa.fudaa.sig.FSigAttibuteTypeManager;
import com.memoire.bu.BuMenu;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ctulu.gui.CtuluLibSwing;

/**
 * Le panneau de visualisation 2D g�rant les calques. Cette classe ajoute la gestion
 * les calques SIG et des services associ�s.
 * 
 * @author Fred Deniger
 * @version $Id: FSigVisuPanel.java 6288 2011-06-14 13:42:14Z bmarchan $
 */
public abstract class FSigVisuPanel extends ZEbliCalquesPanel implements CtuluUndoRedoInterface {

    protected CtuluCommandManager mng_;

    FSigAttibuteTypeManager attMng_;

    ZCalqueGrille grille_;

    ThemeMenu theme_;

    /**
   * @param _impl l'implementation parente
   */
    public FSigVisuPanel(final FudaaCommonImplementation _impl) {
        this(null, _impl);
    }

    public FSigVisuPanel(final BGroupeCalque _gcMain, final CtuluUI _impl) {
        this(_gcMain, new FSigVisuPanelController(_impl));
    }

    public FSigVisuPanel(final BGroupeCalque _gcMain, FSigVisuPanelController _controller) {
        super(_gcMain, _controller);
        setModeVisible(true);
        gisEditor_.setUi(_controller.getUI());
        addCqInfos();
    }

    protected BGroupeCalque addCqInfos() {
        if (getCqInfos() != null) {
            return getCqInfos();
        }
        final BGroupeCalque cq = super.addCqInfos();
        grille_ = new ZCalqueGrille(getVueCalque());
        grille_.setVisible(false);
        grille_.setDestructible(false);
        cq.setCouleur(Color.LIGHT_GRAY);
        cq.add(grille_);
        return cq;
    }

    protected ZCalqueDeplacementInteraction getCalqueDeplacement() {
        ZCalqueDeplacementInteraction cqDep = null;
        if ((cqDep = (ZCalqueDeplacementInteraction) getVueCalque().getCalque().getCalqueParNom("cqDeplacement")) == null) {
            cqDep = new ZCalqueDeplacementInteraction(getCqSelectionI());
            this.addCalqueInteraction(cqDep);
            cqDep.setGele(true);
            cqDep.setTarget(getEditor());
        }
        return cqDep;
    }

    protected void cancelEdition() {
        if (gisEditor_ != null && gisEditor_.getSupport().getCalqueActif() != null) {
            gisEditor_.cancelEdition();
        }
    }

    protected void configureGISGroup(final ZCalqueEditionGroup _layer) {
    }

    protected ZEditorDefault createGisEditor() {
        ZEditorDefault editor = new FSigEditor(this);
        mng_ = editor.getMng();
        return editor;
    }

    /**
   * @return un tableau contenant les actions sp�cifiques � l'application qui
   *         seront dans le menu Vue2D (et pas dans la barre d'outils).
   */
    protected EbliActionInterface[] getActionsInterface() {
        return new EbliActionInterface[] { null, new SceneSelectPreviousAction(gisEditor_.getSceneEditor()), new SceneSelectNextAction(gisEditor_.getSceneEditor()) };
    }

    protected BuMenu[] createSpecificMenus(final String _title) {
        final BuMenu[] res = new BuMenu[2];
        res[0] = new BuMenu(_title, "LAYER");
        res[0].setIcon(null);
        res[0].add(BArbreCalque.buildZNormalMenu(modelArbre_));
        EbliComponentFactory.INSTANCE.addActionsToMenu(getActionsInterface(), res[0]);
        fillMenuWithToolsActions(res[0]);
        res[1] = getThemeMenu();
        res[1].setMnemonic(res[1].getText().charAt(0));
        res[1].setIcon(null);
        return res;
    }

    protected ThemeMenu getThemeMenu() {
        if (theme_ == null) {
            theme_ = new ThemeMenu(getArbreCalqueModel(), getDonneesCalque(), CtuluLib.getS("Th�mes[GIS]"));
        }
        return theme_;
    }

    protected void initButtonGroupSpecific(final List<EbliActionInterface> _l, final ZEbliCalquePanelController _res) {
    }

    /**
   * Pour etre accessible depuis inner classes.
   */
    protected void updateKeyMap(final JComponent _c) {
        super.updateKeyMap(_c);
    }

    public void clearCmd(final CtuluCommandManager _source) {
        if (_source != getCmdMng()) {
            getCmdMng().clean();
        }
    }

    public EbliActionInterface[] getApplicationActions() {
        final List<EbliActionInterface> arrayList = new ArrayList<EbliActionInterface>(4);
        initButtonGroupSpecific(arrayList, getController());
        arrayList.add(null);
        arrayList.add(new CalqueGISEditionAction(getArbreCalqueModel().getTreeSelectionModel(), gisEditor_, getScene()));
        arrayList.add(new SceneRotationAction(getArbreCalqueModel().getTreeSelectionModel(), gisEditor_, gisEditor_.getSceneEditor(), getEbliFormatter()));
        arrayList.add(new SceneDeplacementAction(getArbreCalqueModel().getTreeSelectionModel(), gisEditor_, getEbliFormatter(), getCalqueDeplacement()));
        arrayList.add(new EditVisibilityAction(getArbreCalqueModel(), getScene(), mng_));
        arrayList.add(new SceneShowDistanceAction(gisEditor_, getEbliFormatter()));
        return (EbliActionInterface[]) arrayList.toArray(new EbliActionInterface[arrayList.size()]);
    }

    public FSigAttibuteTypeManager getAttributeMng() {
        if (attMng_ == null) {
            attMng_ = new FSigAttibuteTypeManager();
        }
        return attMng_;
    }

    /**
   * @return le manager des commandes pour le undo/redo
   */
    public CtuluCommandManager getCmdMng() {
        return mng_;
    }

    /**
   * @return la fenetre de l'implementation parente.
   */
    public Frame getFrame() {
        return CtuluLibSwing.getFrameAncestorHelper(getCtuluUI().getParentComponent());
    }

    public FSigEditor getEditor() {
        return (FSigEditor) gisEditor_;
    }

    /**
   * L'action refaire.
   */
    public void redo() {
        mng_.redo();
    }

    public void restaurer() {
        super.restaurer();
    }

    /**
   * L'action defaire.
   */
    public void undo() {
        mng_.undo();
    }

    public void duplicate() {
        gisEditor_.getSceneEditor().copySelectedObjects();
    }

    /**
   * @return le groupe contenant les donn�es geographiques.
   */
    public abstract FSigLayerGroup getGroupGIS();

    /**
   * Met a jour le composant d'information.
   */
    public final void updateInfoComponent() {
        getController().updateInfoComponent();
    }
}
