package org.fudaa.fudaa.crue.builder;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.tree.DefaultMutableTreeNode;
import com.memoire.bu.BuLabel;
import org.apache.commons.collections.CollectionUtils;
import org.fudaa.dodico.crue.metier.etude.EMHProjet;
import org.fudaa.dodico.crue.metier.etude.EMHRun;
import org.fudaa.dodico.crue.metier.etude.FichierCrue;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHModeleBase;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHScenario;
import org.fudaa.dodico.crue.metier.etude.ManagerEMHSousModele;
import org.fudaa.fudaa.crue.builder.CrueTreeProject.TreeNodeModele;
import org.fudaa.fudaa.crue.builder.CrueTreeProject.TreeNodeRunDefault;
import org.fudaa.fudaa.crue.builder.CrueTreeProject.TreeNodeScenario;
import org.fudaa.fudaa.crue.builder.CrueTreeProject.TreeNodeSousModele;
import org.fudaa.fudaa.crue.builder.CrueTreeProject.TreeScenarioModel;
import org.fudaa.fudaa.crue.common.FCrueResource;
import org.fudaa.fudaa.crue.common.GrapheCellRenderer;
import org.fudaa.fudaa.crue.common.UiContext;
import org.jdesktop.swingx.JXTree;

/**
 * Gere les interfaces relative a un scenario Crue. Gere le lien entre un EMHScenario metier et l'interface propose par
 * les actions scenarios.
 * 
 * @author Adrien Hadoux
 */
public class CrueScenarioBuilder {

    /**
   * Cree la liste avec ou non bordure.
   * 
   * @param baseScenarios
   * @param setBorder
   * @return
   */
    public static JList buildListScenario(final List<ManagerEMHScenario> baseScenarios, boolean setBorder, UiContext factory) {
        final DefaultListModel model = new DefaultListModel();
        if (baseScenarios != null) {
            for (final ManagerEMHScenario scenario : baseScenarios) {
                final BuLabel label = new BuLabel(CrueTreeProject.iconeScenario);
                label.setText(scenario.getNom());
                model.addElement(label);
            }
        }
        final JList liste = factory.createList(model);
        liste.setCellRenderer(new GrapheCellRenderer());
        if (setBorder) {
            liste.setBorder(BorderFactory.createTitledBorder(FCrueResource.getS("ihm.scenarios.dispos")));
        }
        return liste;
    }

    /**
   * Cree la liste avec ou non bordure.
   * 
   * @param baseScenarios
   * @param setBorder
   * @return
   */
    public static JXTree buildListScenarioAndRun(final List<ManagerEMHScenario> baseScenarios, ManagerEMHScenario scenarioCourant, UiContext factory) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        if (baseScenarios != null) {
            for (final ManagerEMHScenario managerScenario : baseScenarios) {
                boolean used = false;
                if (scenarioCourant != null && scenarioCourant == managerScenario && scenarioCourant.getRunCourant() == null) {
                    used = true;
                }
                DefaultMutableTreeNode scenarioNode = new DefaultMutableTreeNode(new ScenarioRunBean(managerScenario, null, used));
                root.add(scenarioNode);
                List<EMHRun> listeRuns = managerScenario.getListeRuns();
                if (CollectionUtils.isNotEmpty(listeRuns)) {
                    for (EMHRun emhRun : listeRuns) {
                        used = false;
                        if (scenarioCourant != null && scenarioCourant == managerScenario && scenarioCourant.getRunCourant() == emhRun) {
                            used = true;
                        }
                        scenarioNode.add(new DefaultMutableTreeNode(new ScenarioRunBean(managerScenario, emhRun, used)));
                    }
                }
            }
        }
        final JXTree tree = factory.createTree(root);
        tree.setRootVisible(false);
        tree.setCellRenderer(new ScenarioRunCellRenderer());
        return tree;
    }

    /**
   * Construit une bu list avec l'ensemble des scenarios du logiciel
   * 
   * @param baseScenarios
   * @return
   */
    public static JList buildListScenario(final List<ManagerEMHScenario> baseScenarios, UiContext factory) {
        return buildListScenario(baseScenarios, true, factory);
    }

    /**
   * Construit une bu list avec l'ensemble des scenarios du logiciel
   * 
   * @param baseScenarios
   * @return modele du tree.
   */
    public static TreeScenarioModel buildTreeScenario(EMHProjet projet) {
        final TreeNodeString root = new TreeNodeString("root");
        if (projet.getListeScenarios() != null) {
            for (final ManagerEMHScenario scenario : projet.getListeScenarios()) {
                TreeNodeScenario nodeScenar = new TreeNodeScenario(scenario, projet);
                root.add(nodeScenar);
                if (scenario.getListeRuns() != null) {
                    for (EMHRun run : scenario.getListeRuns()) {
                        TreeNodeRunDefault nodeRun = new TreeNodeRunDefault(projet, scenario, run);
                        nodeScenar.add(nodeRun);
                    }
                }
                if (scenario.getFils() != null) {
                    TreeNodeString conteneurModeles = new TreeNodeString("Modeles");
                    nodeScenar.add(conteneurModeles);
                    for (ManagerEMHModeleBase m : scenario.getFils()) {
                        TreeNodeModele nodeModel = new TreeNodeModele(m);
                        conteneurModeles.add(nodeModel);
                        if (CollectionUtils.isNotEmpty(m.getFils())) {
                            TreeNodeString conteneurSModeles = new TreeNodeString("Sous Modeles");
                            nodeModel.add(conteneurSModeles);
                            for (ManagerEMHSousModele sousmodele : m.getFils()) {
                                TreeNodeSousModele nodeSModel = new TreeNodeSousModele(sousmodele);
                                conteneurSModeles.add(nodeSModel);
                                if (sousmodele.getListeFichiers() != null) {
                                    TreeNodeString conteneurSModelesFichier = new TreeNodeString("Fichiers");
                                    nodeSModel.add(conteneurSModelesFichier);
                                    for (FichierCrue fc : sousmodele.getListeFichiers().getFichiers()) {
                                        conteneurSModelesFichier.add(createTreeNodeFile(projet, fc));
                                    }
                                }
                            }
                        }
                        if (m.getListeFichiers() != null) {
                            TreeNodeString conteneurModelesFichier = new TreeNodeString("Fichiers");
                            nodeModel.add(conteneurModelesFichier);
                            for (FichierCrue fc : m.getListeFichiers().getFichiers()) {
                                conteneurModelesFichier.add(createTreeNodeFile(projet, fc));
                            }
                        }
                    }
                }
                if (scenario.getListeFichiers() != null) {
                    TreeNodeString conteneurModelesFichier = new TreeNodeString("Fichiers");
                    nodeScenar.add(conteneurModelesFichier);
                    for (FichierCrue fc : scenario.getListeFichiers().getFichiers()) {
                        conteneurModelesFichier.add(createTreeNodeFile(projet, fc));
                    }
                }
            }
        }
        TreeScenarioModel modeleTree = new TreeScenarioModel(projet);
        modeleTree.setRoot(root);
        return modeleTree;
    }

    private static TreeNodeStringFile createTreeNodeFile(EMHProjet projet, FichierCrue fc) {
        return new TreeNodeStringFile(fc.getNom(), fc, projet);
    }

    /**
   * Genere l'encart du scenario courant
   * 
   * @param scenarCourant
   * @return
   */
    public static JList buildListScenarioCourant(final ManagerEMHScenario scenarCourant, UiContext componentFactory) {
        if (scenarCourant == null) {
            return null;
        }
        final DefaultListModel model = new DefaultListModel();
        final BuLabel label = new BuLabel(CrueTreeProject.iconeScenarioCourant);
        label.setText(scenarCourant.getNom());
        model.addElement(label);
        final JList liste = componentFactory.createList(model);
        liste.setCellRenderer(new GrapheCellRenderer());
        liste.setBorder(BorderFactory.createTitledBorder(FCrueResource.getS("ihm.scenario.courant")));
        return liste;
    }
}
