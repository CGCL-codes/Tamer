package org.fudaa.fudaa.tr.post;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import com.memoire.bu.BuDesktop;
import com.memoire.bu.BuMenu;
import com.memoire.bu.BuMenuItem;
import com.memoire.bu.BuSeparator;
import org.fudaa.ctulu.CtuluCommandManager;
import org.fudaa.ctulu.gui.CtuluSelectorPopupButton;
import org.fudaa.ebli.calque.BCalqueLegende;
import org.fudaa.ebli.calque.ZEbliCalquesPanel;
import org.fudaa.ebli.commun.EbliActionAbstract;
import org.fudaa.ebli.commun.EbliActionInterface;
import org.fudaa.ebli.commun.EbliActionPaletteAbstract;
import org.fudaa.ebli.commun.EbliActionSimple;
import org.fudaa.ebli.commun.EbliComponentFactory;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.visuallibrary.EbliNode;
import org.fudaa.ebli.visuallibrary.EbliNodeDefault;
import org.fudaa.ebli.visuallibrary.EbliScene;
import org.fudaa.ebli.visuallibrary.EbliWidget;
import org.fudaa.ebli.visuallibrary.EbliWidgetBordureSingle;
import org.fudaa.ebli.visuallibrary.actions.*;
import org.fudaa.ebli.visuallibrary.animation.EbliWidgetAnimAdapter;
import org.fudaa.ebli.visuallibrary.calque.CalqueLegendeWidgetAdapter;
import org.fudaa.ebli.visuallibrary.calque.EbliWidgetControllerCalque;
import org.fudaa.ebli.visuallibrary.calque.EbliWidgetCreatorLegende;
import org.fudaa.ebli.visuallibrary.calque.EbliWidgetCreatorVueCalque;
import org.fudaa.ebli.visuallibrary.creator.*;
import org.fudaa.fudaa.tr.common.TrResource;
import org.fudaa.fudaa.tr.post.actions.TrPostActionChangeSceneForWidget;
import org.fudaa.fudaa.tr.post.actions.TrPostActionChooseAndCreateCalque;
import org.fudaa.fudaa.tr.post.actions.TrPostActionDuplicate;
import org.fudaa.fudaa.tr.post.actions.TrPostActionDuplicateLayout;
import org.fudaa.fudaa.tr.post.dialogSpec.TrPostWizardCreateScope;
import org.netbeans.api.visual.widget.Widget;

/**
 * Controller des actions pour les ebliWidget. Gere les multi layout.
 * 
 * @author Adrien Hadoux
 */
public class TrPostLayoutPanelController {

    List<EbliActionAbstract> actions_;

    /**
   * Calque principal associ� a la vue layout.
   */
    private ZEbliCalquesPanel calquePrincipal;

    JPanel conteneur_;

    JScrollPane conteneurSceneView_;

    ArrayList<BuMenuItem> listeMenusScenes_ = new ArrayList<BuMenuItem>();

    BuMenu menuScenes_ = new BuMenu(TrResource.getS("Liste des Layouts"), "LISTELAYOUT");

    JTabbedPane pn_;

    TrPostProjet projet_;

    /**
   * scene seelctionnee par l utilisateur
   */
    TrPostScene sceneCourante_;

    TrPostLayoutPanelController(final TrPostScene _scene, final TrPostProjet _projet) {
        sceneCourante_ = _scene;
        _scene.setCmdMng(new CtuluCommandManager());
        projet_ = _projet;
    }

    /**
   * Methode specialisee dans l ajout d un ndoe de type calque a la scene.
   * 
   * @param title de la widget
   * @param preferredLocation de la widget
   * @param preferedDimension de la widget
   * @param calque contenu de la widget
   * @return le node cree
   */
    public EbliNode addCalque(final String title, final Point preferredLocation, final Dimension preferedDimension, final TrPostVisuPanel calque, final CalqueLegendeWidgetAdapter _legende) {
        if (calquePrincipal == null) calquePrincipal = calque;
        final EbliNode nodeCalque = new EbliNodeDefault();
        nodeCalque.setTitle(title);
        final EbliWidgetCreatorVueCalque creator = new EbliWidgetCreatorVueCalque(calque);
        creator.setPreferredSize(preferedDimension);
        creator.setPreferredLocation(preferredLocation);
        nodeCalque.setCreator(creator);
        addNode(nodeCalque);
        nodeCalque.setDescription(projet_.formatInfoSource(calque.getSource()));
        calque.getController().buildActions();
        ((EbliWidgetControllerCalque) nodeCalque.getController()).ajoutLegende();
        return nodeCalque;
    }

    public EbliNode addLigne() {
        final EbliNode nodeLigne = new EbliNodeDefault();
        nodeLigne.setTitle("Ligne");
        nodeLigne.setCreator(new EbliWidgetCreatorShape(new ShapeCreatorLine()));
        nodeLigne.getCreator().setPreferredSize(new Dimension(200, 100));
        nodeLigne.getCreator().setPreferredLocation(new Point(350, 125));
        addNode(nodeLigne);
        return nodeLigne;
    }

    public EbliNode addCercle() {
        final EbliNode nodeCercle = new EbliNodeDefault();
        nodeCercle.setTitle("Cercle");
        nodeCercle.setCreator(new EbliWidgetCreatorShape(new ShapeCreatorCircle()));
        nodeCercle.getCreator().setPreferredSize(new Dimension(200, 100));
        nodeCercle.getCreator().setPreferredLocation(new Point(350, 125));
        addNode(nodeCercle);
        return nodeCercle;
    }

    public EbliNode addEllipse() {
        final EbliNode nodeEllipse = new EbliNodeDefault();
        nodeEllipse.setTitle("Ellipse");
        nodeEllipse.setCreator(new EbliWidgetCreatorShape(new ShapeCreatorEllipse()));
        nodeEllipse.getCreator().setPreferredSize(new Dimension(200, 100));
        nodeEllipse.getCreator().setPreferredLocation(new Point(350, 125));
        addNode(nodeEllipse);
        return nodeEllipse;
    }

    /**
   * Methode d ajout de composant de base graphique.
   * 
   * @return
   */
    public EbliNode addFleche() {
        final EbliNode nodeFleche = new EbliNodeDefault();
        nodeFleche.setTitle("Fleche");
        nodeFleche.setCreator(new EbliWidgetCreatorShape(new ShapeCreatorFleche()));
        nodeFleche.getCreator().setPreferredSize(new Dimension(100, 50));
        nodeFleche.getCreator().setPreferredLocation(new Point(350, 125));
        addNode(nodeFleche);
        return nodeFleche;
    }

    public EbliNode addFlecheSimple() {
        final EbliNode nodeFleche = new EbliNodeDefault();
        nodeFleche.setTitle("Fleche simple");
        nodeFleche.setCreator(new EbliWidgetCreatorConnectionWidget());
        addNode(nodeFleche);
        return nodeFleche;
    }

    public EbliNode addDblFleche() {
        final EbliNode nodeFleche = new EbliNodeDefault();
        nodeFleche.setTitle("Double Fleche");
        nodeFleche.setCreator(new EbliWidgetCreatorShape(new ShapeCreatorDblFleche()));
        nodeFleche.getCreator().setPreferredSize(new Dimension(100, 50));
        nodeFleche.getCreator().setPreferredLocation(new Point(350, 125));
        addNode(nodeFleche);
        return nodeFleche;
    }

    /**
   * Methode generique d ajout d un node widget a la scene.
   * 
   * @param node
   */
    public Widget addNode(final EbliNode node) {
        final Widget addNode = getSceneCourante().addNode(node);
        getSceneCourante().refresh();
        getSceneCourante().getCmdMng().addCmd(new CommandUndoRedoCreation(node, getSceneCourante()));
        return addNode;
    }

    public EbliNode addRectangle() {
        final EbliNode nodeCercle = new EbliNodeDefault();
        nodeCercle.setTitle("rectangle");
        nodeCercle.setCreator(new EbliWidgetCreatorShape(new ShapeCreatorRectangle()));
        nodeCercle.getCreator().setPreferredSize(new Dimension(200, 100));
        nodeCercle.getCreator().setPreferredLocation(new Point(350, 125));
        addNode(nodeCercle);
        return nodeCercle;
    }

    /**
   * Methode d ajout de composant de base graphique.
   * 
   * @return
   */
    public EbliNode addRectangleTexte() {
        final EbliNode nodeRect = new EbliNodeDefault();
        nodeRect.setTitle("Rectangle texte");
        nodeRect.setCreator(new EbliWidgetCreatorTextLabel("Tapez votre texte ici"));
        nodeRect.getCreator().setPreferredSize(new Dimension(200, 100));
        nodeRect.getCreator().setPreferredLocation(new Point(350, 125));
        addNode(nodeRect);
        return nodeRect;
    }

    @SuppressWarnings("serial")
    protected void addShapeActions(final List<EbliActionAbstract> _l) {
        _l.add(new EbliWidgetActiontextEditor(getSceneCourante()));
        _l.add(new EbliActionSimple(EbliResource.EBLI.getString("Ligne"), EbliResource.EBLI.getToolIcon("trait"), "WIDGETLINE") {

            @Override
            public void actionPerformed(final ActionEvent _evt) {
                addLigne();
            }
        });
        _l.add(new EbliActionSimple(EbliResource.EBLI.getString("Editeur Fleche"), EbliResource.EBLI.getToolIcon("crystal_bu_link"), "WIDGETFLECHESIMPLE") {

            @Override
            public void actionPerformed(final ActionEvent _evt) {
                addFlecheSimple();
            }
        });
        _l.add(new EbliActionSimple(EbliResource.EBLI.getString("Fleche"), EbliResource.EBLI.getToolIcon("crystal_bu_link"), "WIDGETFLECHE") {

            @Override
            public void actionPerformed(final ActionEvent _evt) {
                addFleche();
            }
        });
        _l.add(new EbliActionSimple(EbliResource.EBLI.getString("Double Fleche"), EbliResource.EBLI.getToolIcon("crystal_bu_scrollpane_corner"), "WIDGETDBLFLECHE") {

            @Override
            public void actionPerformed(final ActionEvent _evt) {
                addDblFleche();
            }
        });
        _l.add(new EbliActionSimple(EbliResource.EBLI.getString("Ellipse"), EbliResource.EBLI.getToolIcon("ellip"), "WIDGETELLIPSE") {

            @Override
            public void actionPerformed(final ActionEvent _evt) {
                addEllipse();
            }
        });
        _l.add(new EbliActionSimple(EbliResource.EBLI.getString("Cercle"), EbliResource.EBLI.getToolIcon("cerc"), "WIDGETCERCLE") {

            @Override
            public void actionPerformed(final ActionEvent _evt) {
                addCercle();
            }
        });
        _l.add(new EbliActionSimple(EbliResource.EBLI.getString("Rectangle"), EbliResource.EBLI.getToolIcon("rect"), "WIDGETRECT") {

            @Override
            public void actionPerformed(final ActionEvent _evt) {
                addRectangle();
            }
        });
        _l.add(new EbliWidgetActionImageChooser(getSceneCourante()));
    }

    protected JMenu createMenu() {
        final BuMenu m = new BuMenu();
        final List<EbliActionAbstract> acts = getActions();
        if (acts == null) {
            return null;
        }
        for (final EbliActionAbstract a : acts) {
            if (a == null) {
                m.add(new BuSeparator());
            } else {
                m.add(a.buildMenuItem(EbliComponentFactory.INSTANCE));
            }
        }
        return m;
    }

    protected JComponent[] createSpecificComponent(final JDesktopPane j, final JComponent parent) {
        final List<EbliActionAbstract> acts = getActions();
        if (acts == null) {
            return null;
        }
        EbliLib.updateMapKeyStroke(parent, acts.toArray(new EbliActionInterface[acts.size()]));
        BuDesktop buJ = null;
        if (j instanceof BuDesktop) {
            buJ = (BuDesktop) j;
        }
        final List<JComponent> res = new ArrayList<JComponent>(acts.size());
        for (final EbliActionAbstract a : acts) {
            if (a == null) {
                res.add(null);
            } else {
                if ((buJ != null) && (a instanceof EbliActionPaletteAbstract)) {
                    ((EbliActionPaletteAbstract) a).setDesktop(buJ);
                }
                res.add(a.buildToolButton(EbliComponentFactory.INSTANCE));
            }
        }
        return res.toArray(new JComponent[res.size()]);
    }

    public List<EbliActionAbstract> getActions() {
        if (actions_ == null) {
            final List<EbliActionAbstract> init = new ArrayList<EbliActionAbstract>(20);
            final TrPostScene sceneCourante = getSceneCourante();
            init.add(new EbliWidgetActionAlign.Left(sceneCourante));
            init.add(new EbliWidgetActionAlign.Right(sceneCourante));
            init.add(new EbliWidgetActionAlign.Middle(sceneCourante));
            init.add(new EbliWidgetActionAlign.Center(sceneCourante));
            init.add(new EbliWidgetActionAlign.Top(sceneCourante));
            init.add(new EbliWidgetActionAlign.Bottom(sceneCourante));
            init.add(null);
            init.add(new EbliWidgetActionMoveToFirst(sceneCourante));
            init.add(new EbliWidgetActionMoveToBack(sceneCourante));
            init.add(null);
            init.add(new EbliWidgetActionRetaillageHorizontal(sceneCourante, EbliWidgetActionRetaillageHorizontal.RETAIILLAGE_MAX));
            init.add(new EbliWidgetActionRetaillageVertical(sceneCourante, EbliWidgetActionRetaillageVertical.RETAIILLAGE_MAX));
            init.add(null);
            init.add(new EbliWidgetActionGroup(sceneCourante));
            init.add(null);
            init.add(new EbliWidgetActionDelete(sceneCourante));
            init.add(null);
            init.add(new TrPostActionDuplicate(sceneCourante, projet_));
            init.add(new TrPostActionDuplicateLayout(sceneCourante, projet_));
            init.add(new TrPostActionChangeSceneForWidget(sceneCourante, projet_));
            init.add(null);
            init.add(new EbliWidgetActionColorBackground.ForScene(sceneCourante));
            init.add(new EbliWidgetActionConfigure(sceneCourante));
            init.add(null);
            addShapeActions(init);
            init.add(null);
            init.add(new EbliActionSimple(EbliResource.EBLI.getString("Gestion Multi-Sources"), EbliResource.EBLI.getToolIcon("tableau"), "MULTI SOURCES") {

                @Override
                public void actionPerformed(final ActionEvent _evt) {
                    if (!projet_.filleProjetctManager_.isVisible()) {
                        projet_.impl_.addInternalFrame(projet_.filleProjetctManager_);
                    } else projet_.filleProjetctManager_.moveToFront();
                }
            });
            init.add(new TrPostActionChooseAndCreateCalque(projet_, this));
            init.add(null);
            init.add(new EbliWidgetAnimAdapter(sceneCourante).createAction());
            init.add(null);
            init.add(new TrPostWizardCreateScope.ImportAction(projet_));
            actions_ = Collections.unmodifiableList(init);
        }
        return actions_;
    }

    /**
   * @return the calquePrincipal
   */
    public ZEbliCalquesPanel getCalquePrincipal() {
        return calquePrincipal;
    }

    /**
   * affichage de la scene.
   * 
   * @return
   */
    public JComponent getPanel() {
        if (conteneur_ == null) {
            pn_ = new JTabbedPane();
            pn_.setTabPlacement(SwingConstants.BOTTOM);
            conteneurSceneView_ = new JScrollPane(getSceneCourante().createView());
            new CtuluSelectorPopupButton() {

                @Override
                public JComponent createComponent() {
                    return getSceneCourante().createSatelliteView();
                }
            }.installButton(conteneurSceneView_);
            pn_.addTab("Layout 1", conteneurSceneView_);
            conteneur_ = new JPanel(new BorderLayout());
            conteneur_.add(conteneurSceneView_, BorderLayout.CENTER);
        }
        return conteneur_;
    }

    public TrPostScene getSceneCourante() {
        return sceneCourante_;
    }
}
