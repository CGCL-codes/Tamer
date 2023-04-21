package org.fudaa.fudaa.tr.post.actions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import com.memoire.bu.BuDialog;
import com.memoire.bu.BuDialogConfirmation;
import com.memoire.bu.BuScrollPane;
import org.fudaa.ctulu.CtuluCommand;
import org.fudaa.ctulu.CtuluLibImage;
import org.fudaa.ctulu.CtuluResource;
import org.fudaa.ctulu.ProgressionBuAdapter;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.gui.CtuluTaskOperationGUI;
import org.fudaa.ebli.courbe.EGGraphe;
import org.fudaa.ebli.courbe.EGGrapheDuplicator;
import org.fudaa.ebli.courbe.EGGrapheTreeModel;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.visuallibrary.EbliNode;
import org.fudaa.ebli.visuallibrary.EbliWidgetBordureSingle;
import org.fudaa.ebli.visuallibrary.actions.EbliWidgetActionAbstract;
import org.fudaa.ebli.visuallibrary.graphe.EbliWidgetControllerGraphe;
import org.fudaa.ebli.visuallibrary.graphe.EbliWidgetCreatorGraphe;
import org.fudaa.ebli.visuallibrary.graphe.EbliWidgetGraphe;
import org.fudaa.ebli.visuallibrary.graphe.GrapheCellRenderer;
import org.fudaa.fudaa.tr.common.TrResource;
import org.fudaa.fudaa.tr.post.ScopCourbeTreeModel;
import org.fudaa.fudaa.tr.post.TrPostCourbeTreeModel;
import org.fudaa.fudaa.tr.post.TrPostProjet;
import org.fudaa.fudaa.tr.post.profile.MvProfileTreeModel;

/**
 * Classe Fusion Widgets des graphes entre eux
 * 
 * @author Adrien Hadoux
 */
public class TrPostActionFusionGraphes extends EbliWidgetActionAbstract {

    TrPostProjet projet_;

    EbliWidgetGraphe widgetGraphe_;

    EbliNode nodeGraphe_;

    JList jListeGraphes_;

    ArrayList<JLabel> listeObjetsCalques;

    ArrayList<EbliNode> listeGraphesPossibles;

    JComponent content_;

    BuDialog dialog_;

    DefaultListModel modelGraphesPossibles_;

    public TrPostActionFusionGraphes(final EbliWidgetGraphe widget, final EbliNode nodeGraphe, final TrPostProjet projet) {
        super(widget.getEbliScene(), TrResource.getS("Fusion avec un autre graphe"), CtuluResource.CTULU.getIcon("cible"), "ADDPOINTWIDGET");
        widgetGraphe_ = widget;
        nodeGraphe_ = nodeGraphe;
        projet_ = projet;
        if (!((EbliWidgetControllerGraphe) widgetGraphe_.getController()).hasAlreadyFusion) {
            widgetGraphe_.getController().getPopup().add(this);
            ((EbliWidgetControllerGraphe) widgetGraphe_.getController()).hasAlreadyFusion = true;
        }
    }

    /**
   * remplissage de la combo avec les graphes disponibles et compatibles
   * 
   * @param scene
   */
    private void remplirCombo() {
        final Map params = new HashMap();
        CtuluLibImage.setCompatibleImageAsked(params);
        listeGraphesPossibles = new ArrayList<EbliNode>();
        listeObjetsCalques = new ArrayList<JLabel>();
        final Set<EbliNode> listeNode = (Set<EbliNode>) scene_.getObjects();
        for (final Iterator<EbliNode> it = listeNode.iterator(); it.hasNext(); ) {
            final EbliNode currentNode = it.next();
            if (currentNode != nodeGraphe_ && currentNode.getCreator() instanceof EbliWidgetCreatorGraphe) {
                final EbliWidgetCreatorGraphe new_name = (EbliWidgetCreatorGraphe) currentNode.getCreator();
                if (isCompatibleWithGraphe(new_name.getGraphe())) {
                    final JLabel label = new JLabel();
                    final BufferedImage image = new_name.getGraphe().produceImage(70, 30, params);
                    final Icon icone = new ImageIcon(image);
                    label.setIcon(icone);
                    label.setText("Fusionner avec " + currentNode.getTitle());
                    listeObjetsCalques.add(label);
                    listeGraphesPossibles.add(currentNode);
                }
            }
        }
        if (jListeGraphes_ == null) jListeGraphes_ = new JList();
        modelGraphesPossibles_ = new DefaultListModel();
        jListeGraphes_.setModel(modelGraphesPossibles_);
        for (final Iterator<JLabel> it = listeObjetsCalques.iterator(); it.hasNext(); ) {
            modelGraphesPossibles_.addElement(it.next());
        }
        jListeGraphes_.setSize(250, 350);
        jListeGraphes_.setBorder(BorderFactory.createTitledBorder(TrResource.TR.getString("Graphes possibles")));
        jListeGraphes_.setCellRenderer(new GrapheCellRenderer());
    }

    /**
   * verifie si les graphes sont compatibles
   * 
   * @param graphe
   * @return
   */
    public boolean isCompatibleWithGraphe(final EGGraphe graphe) {
        if (graphe.getModel() instanceof ScopCourbeTreeModel || this.widgetGraphe_.getGraphe().getModel() instanceof ScopCourbeTreeModel) return true;
        return (this.widgetGraphe_.getGraphe().getModel() instanceof TrPostCourbeTreeModel && graphe.getModel() instanceof TrPostCourbeTreeModel) || (this.widgetGraphe_.getGraphe().getModel() instanceof MvProfileTreeModel && graphe.getModel() instanceof MvProfileTreeModel);
    }

    JComponent constructPanel() {
        final JPanel content = new JPanel(new BorderLayout());
        content.add(new JLabel(TrResource.getS("Fusion avec un autre graphe")), BorderLayout.NORTH);
        content.add(new BuScrollPane(jListeGraphes_), BorderLayout.CENTER);
        final JButton valide = new JButton(TrResource.getS("R�aliser la fusion"), EbliResource.EBLI.getIcon("crystal_valider"));
        valide.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                if (jListeGraphes_.getSelectedIndex() != -1) {
                    mergeGraph();
                    dialog_.dispose();
                }
            }
        });
        jListeGraphes_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JPanel operations = new JPanel(new FlowLayout(FlowLayout.CENTER));
        operations.add(valide);
        content.add(operations, BorderLayout.SOUTH);
        return content;
    }

    /**
   * Methode qui: merge le graphe choisi dans la combo avec le node actuel degage le node choisi de la scene remet a
   * jour al combo actuelle
   */
    private void mergeGraph() {
        final int selected = jListeGraphes_.getSelectedIndex();
        if (selected == -1) {
            return;
        }
        final EbliNode nodeToMerge = listeGraphesPossibles.get(selected);
        EbliWidgetGraphe grapheWidget = null;
        if (nodeToMerge.getWidget() instanceof EbliWidgetBordureSingle) grapheWidget = (EbliWidgetGraphe) ((EbliWidgetBordureSingle) nodeToMerge.getWidget()).getIntern(); else grapheWidget = (EbliWidgetGraphe) nodeToMerge.getWidget();
        if (widgetGraphe_.getGraphe().getModel() instanceof TrPostCourbeTreeModel && !(grapheWidget.getGraphe().getModel() instanceof MvProfileTreeModel)) {
            final TrPostCourbeTreeModel grapheToMerge = (TrPostCourbeTreeModel) grapheWidget.getGraphe().getModel();
            final TrPostCourbeTreeModel grapheModel = (TrPostCourbeTreeModel) widgetGraphe_.getGraphe().getModel();
            final EGGrapheTreeModel saveModel = (EGGrapheTreeModel) grapheModel.duplicate(new EGGrapheDuplicator());
            new CtuluTaskOperationGUI(projet_.impl_, TrResource.getS("Fusion avec un autre graphe")) {

                @Override
                public void act() {
                    final ProgressionInterface prog = new ProgressionBuAdapter(this);
                    if (grapheToMerge instanceof ScopCourbeTreeModel) grapheModel.mergeWithAScopeTreeModel((ScopCourbeTreeModel) grapheToMerge); else grapheModel.mergeWithAnotherTreeModel(grapheToMerge);
                }
            }.start();
            ;
            scene_.getCmdMng().addCmd(new CtuluCommand() {

                public void redo() {
                    grapheModel.mergeWithAnotherTreeModel(grapheToMerge);
                    scene_.removeNodeWithEdges(nodeToMerge);
                    scene_.refresh();
                }

                public void undo() {
                    widgetGraphe_.getGraphe().setModel(saveModel);
                    grapheModel.fireStructureChanged();
                    scene_.addNode(nodeToMerge);
                    scene_.refresh();
                }
            });
        } else if (widgetGraphe_.getGraphe().getModel() instanceof MvProfileTreeModel) {
            if (grapheWidget.getGraphe().getModel() instanceof MvProfileTreeModel) {
                final MvProfileTreeModel grapheToMerge = (MvProfileTreeModel) grapheWidget.getGraphe().getModel();
                final MvProfileTreeModel grapheModel = (MvProfileTreeModel) widgetGraphe_.getGraphe().getModel();
                final EGGrapheTreeModel saveModel = (EGGrapheTreeModel) grapheModel.duplicate(new EGGrapheDuplicator());
                new CtuluTaskOperationGUI(projet_.impl_, TrResource.getS("Fusion avec un autre graphe")) {

                    @Override
                    public void act() {
                        final ProgressionInterface prog = new ProgressionBuAdapter(this);
                        grapheModel.mergeWithAnotherTreeModel(grapheToMerge);
                    }
                }.start();
                scene_.getCmdMng().addCmd(new CtuluCommand() {

                    public void redo() {
                        grapheModel.mergeWithAnotherTreeModel(grapheToMerge);
                        scene_.removeNodeWithEdges(nodeToMerge);
                        scene_.refresh();
                    }

                    public void undo() {
                        widgetGraphe_.getGraphe().setModel(saveModel);
                        grapheModel.fireStructureChanged();
                        scene_.addNode(nodeToMerge);
                        scene_.refresh();
                    }
                });
            } else if (grapheWidget.getGraphe().getModel() instanceof ScopCourbeTreeModel) {
                final ScopCourbeTreeModel grapheToMerge = (ScopCourbeTreeModel) grapheWidget.getGraphe().getModel();
                final MvProfileTreeModel grapheModel = (MvProfileTreeModel) widgetGraphe_.getGraphe().getModel();
                final EGGrapheTreeModel saveModel = (EGGrapheTreeModel) grapheModel.duplicate(new EGGrapheDuplicator());
                new CtuluTaskOperationGUI(projet_.impl_, TrResource.getS("Fusion avec un autre graphe")) {

                    @Override
                    public void act() {
                        grapheModel.mergeWithAnotherScopeTreeModel(grapheToMerge);
                    }
                }.start();
                scene_.getCmdMng().addCmd(new CtuluCommand() {

                    public void redo() {
                        grapheModel.mergeWithAnotherScopeTreeModel(grapheToMerge);
                        scene_.removeNodeWithEdges(nodeToMerge);
                        scene_.refresh();
                    }

                    public void undo() {
                        widgetGraphe_.getGraphe().setModel(saveModel);
                        grapheModel.fireStructureChanged();
                        scene_.addNode(nodeToMerge);
                        scene_.refresh();
                    }
                });
            }
        } else {
            final ScopCourbeTreeModel grapheToMerge = (ScopCourbeTreeModel) widgetGraphe_.getGraphe().getModel();
            final MvProfileTreeModel grapheModel = (MvProfileTreeModel) grapheWidget.getGraphe().getModel();
            final EGGrapheTreeModel saveModel = (EGGrapheTreeModel) grapheModel.duplicate(new EGGrapheDuplicator());
            new CtuluTaskOperationGUI(projet_.impl_, TrResource.getS("Fusion avec un autre graphe")) {

                @Override
                public void act() {
                    final ProgressionInterface prog = new ProgressionBuAdapter(this);
                    grapheToMerge.mergeWithAnotherMvProfileTreeModel(grapheModel);
                }
            }.start();
            scene_.getCmdMng().addCmd(new CtuluCommand() {

                public void redo() {
                    grapheToMerge.mergeWithAnotherMvProfileTreeModel(grapheModel);
                    scene_.removeNodeWithEdges(nodeToMerge);
                    scene_.refresh();
                }

                public void undo() {
                    widgetGraphe_.getGraphe().setModel(saveModel);
                    grapheModel.fireStructureChanged();
                    scene_.addNode(nodeToMerge);
                    scene_.refresh();
                }
            });
        }
        if (((EbliWidgetControllerGraphe) grapheWidget.getController()).hasLegende()) scene_.removeNode(grapheWidget.getNodeLegende());
        scene_.removeNodeWithEdges(nodeToMerge);
        scene_.refresh();
        remplirCombo();
        if (((EbliWidgetControllerGraphe) widgetGraphe_.getController()).hasLegende()) {
            scene_.removeNode(widgetGraphe_.getNodeLegende());
            scene_.refresh();
            ((EbliWidgetControllerGraphe) widgetGraphe_.getController()).ajoutLegende();
            scene_.refresh();
        }
    }

    @Override
    public void actionPerformed(final ActionEvent _e) {
        remplirCombo();
        dialog_ = new BuDialogConfirmation(projet_.impl_.getApp(), projet_.impl_.getInformationsSoftware(), TrResource.getS("Fusion avec un autre graphe"));
        if (content_ == null) content_ = constructPanel();
        dialog_.setContentPane(content_);
        dialog_.setSize(400, 250);
        dialog_.setModal(true);
        dialog_.activate();
    }
}
