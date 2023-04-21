package com.calipso.reportgenerator.userinterface;

import com.calipso.reportgenerator.common.ReportGeneratorConfiguration;
import javax.swing.tree.*;
import javax.swing.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;

public class ColorConditionUI extends JDialog {

    private DefaultMutableTreeNode root;

    private DefaultMutableTreeNode current;

    private DefaultTreeModel treeModel;

    private ColorConditionManager conditionManager;

    private Map metricStates;

    private JTree tree;

    private JButton addButton;

    private JButton removeButton;

    private JButton modifyButton;

    private JButton closeButton;

    /**
   * Genera un frame tipo modal , donde se genera un tree con los valores de las metricas
   *
   * @param owner
   * @param modal
   * @param conditionManager
   * @param metricStates
   */
    public ColorConditionUI(Frame owner, boolean modal, ColorConditionManager conditionManager, Map metricStates, ReportGeneratorConfiguration conf) {
        super(owner, modal);
        Image icon = conf.getImage("icon");
        if (icon != null) {
            owner.setIconImage(icon);
        }
        this.conditionManager = conditionManager;
        this.metricStates = metricStates;
        getContentPane().add(crateCenterJScrollPane(), BorderLayout.CENTER);
        getContentPane().add(createEastPanel(), BorderLayout.EAST);
        modifyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifySelection();
            }
        });
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeSelection();
            }
        });
        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeValueChanged(evt);
            }
        });
        removeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeColorCondition(current);
            }
        });
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new java.awt.Dimension(587, 226));
        setLocation((screenSize.width - 587) / 2, (screenSize.height - 226) / 2);
        setResizable(false);
        checkBtnState();
    }

    /**
   * Cierra el Dialog ColorConditionUI
   */
    private void close() {
        this.setVisible(false);
        this.dispose();
    }

    /**
   * Borra un item del tree
   * @param current
   */
    private void removeColorCondition(DefaultMutableTreeNode current) {
        if (current.getLevel() > 1) {
            ColorCondition colorCondition = (ColorCondition) current.getUserObject();
            conditionManager.remove(colorCondition);
            treeModel.removeNodeFromParent(current);
            treeModel.reload();
        }
    }

    /**
   * Este evento mantiene actualizado el nodo que se esta seleccionando actualmente
   * @param evt
   */
    private void treeValueChanged(TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (evt.getPath().getLastPathComponent());
        current = node;
        checkBtnState();
    }

    /**
   * Setea el estado de los botones para agregar o borrar
   */
    private void checkBtnState() {
        if (current == null || current.getLevel() == 0) {
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            modifyButton.setEnabled(false);
        } else {
            if (current.getLevel() == 1) {
                addButton.setEnabled(true);
                removeButton.setEnabled(false);
                modifyButton.setEnabled(false);
            } else {
                addButton.setEnabled(true);
                modifyButton.setEnabled(true);
                removeButton.setEnabled(true);
            }
        }
    }

    /**
   * Retorna el nodo del tree que ha sido seleccionado
   * @param node
   * @return
   */
    private DefaultMutableTreeNode getCurrent(DefaultMutableTreeNode node, boolean modify) {
        int level = node.getLevel();
        if (level > 0) {
            if (level == 1) {
                return node;
            } else if (level == 2 && modify) {
                return node;
            } else {
                return (DefaultMutableTreeNode) node.getParent();
            }
        }
        return null;
    }

    private void modifySelection() {
        DefaultMutableTreeNode node = getCurrent(current, true);
        DefaultMutableTreeNode previous = node;
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        Collection collection = conditionManager.getConditionsFor(((MetricState) parent.getUserObject()).getName());
        Iterator iterator = collection.iterator();
        ColorCondition currentCondition = null;
        while (iterator.hasNext()) {
            currentCondition = (ColorCondition) iterator.next();
            if (currentCondition.toString().equals(previous.getUserObject().toString())) {
                break;
            }
        }
        if (node != null) {
            ColorCondition colorCondition = ChoosSemaphoreSelection.newColorCondition(null, (MetricState) parent.getUserObject(), currentCondition);
            if (colorCondition != null) {
                conditionManager.put(colorCondition);
                conditionManager.remove(currentCondition);
                treeModel.removeNodeFromParent(node);
                treeModel.nodesWereRemoved(node, null, null);
                treeModel.insertNodeInto(new DefaultMutableTreeNode(colorCondition), parent, 0);
                treeModel.nodeStructureChanged(parent);
                treeModel.reload();
            }
        }
    }

    private ColorCondition getConditionManagerFrom(DefaultMutableTreeNode previous, DefaultMutableTreeNode parent) {
        ColorCondition returnValue = null;
        Collection collection = conditionManager.getConditionsFor(parent.getUserObject().toString().toUpperCase());
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            ColorCondition colorCondition = (ColorCondition) iterator.next();
            if (colorCondition.toString().equals(previous.getUserObject().toString())) {
                returnValue = colorCondition;
                break;
            }
        }
        return returnValue;
    }

    /**
   *  Mustra un color chooser , para seleccionar el color con que se desea pintar los valores de la tabla
   */
    private void executeSelection() {
        DefaultMutableTreeNode node = getCurrent(current, false);
        if (node != null) {
            ColorCondition colorCondition = ChoosSemaphoreSelection.newColorCondition(null, (MetricState) node.getUserObject(), null);
            if (colorCondition != null) {
                conditionManager.put(colorCondition);
                node.add(new DefaultMutableTreeNode(colorCondition));
                treeModel.reload();
            }
        }
    }

    /**
   *  crea el panel ubicado al este del frame , donde se incorpora al norte del mismo el botonn de agregar
   * opciones.
   * @return
   */
    private Component createEastPanel() {
        JPanel eastPanel = new JPanel(new GridLayout(7, 1));
        addButton = new JButton(com.calipso.reportgenerator.common.LanguageTraslator.traslate("130"));
        addButton.setMnemonic('a');
        modifyButton = new JButton(com.calipso.reportgenerator.common.LanguageTraslator.traslate("333"));
        modifyButton.setMnemonic('m');
        removeButton = new JButton(com.calipso.reportgenerator.common.LanguageTraslator.traslate("131"));
        removeButton.setMnemonic('b');
        closeButton = new JButton(com.calipso.reportgenerator.common.LanguageTraslator.traslate("132"));
        closeButton.setMnemonic('c');
        eastPanel.add(addButton);
        eastPanel.add(modifyButton);
        eastPanel.add(removeButton);
        eastPanel.add(closeButton);
        return eastPanel;
    }

    /**
   * Crea un JScrollPane en el centro
   * @return
   */
    private Component crateCenterJScrollPane() {
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(createCenterPanel());
        return jScrollPane;
    }

    /**
   * Crea el JTree , adentro del JScrollPane
   * @return
   */
    private Component createCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());
        center.add(createTree(), BorderLayout.CENTER);
        return center;
    }

    /**
   * Retorna un map , com los estados de las metricas
   * @return
   */
    public Map getMetricStates() {
        return metricStates;
    }

    /**
   * Crea el JTree y carga sus valores
   * @return
   */
    private Component createTree() {
        Iterator iterator = getMetricStates().values().iterator();
        root = new DefaultMutableTreeNode(com.calipso.reportgenerator.common.LanguageTraslator.traslate("170"));
        while (iterator.hasNext()) {
            MetricState value = (MetricState) iterator.next();
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(value);
            loadAllValuesNode(node);
            root.add(node);
        }
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        return tree;
    }

    /**
   * Carga los valores para cada metrica , si es que los hay
   * @param padre
   */
    private void loadAllValuesNode(DefaultMutableTreeNode padre) {
        String name = ((MetricState) padre.getUserObject()).getName();
        Collection conditions = conditionManager.getConditionsFor(name);
        if (padre.isLeaf()) {
            if (conditions != null) {
                Iterator iterator = conditions.iterator();
                while (iterator.hasNext()) {
                    ColorCondition colorCondition = (ColorCondition) iterator.next();
                    if (colorCondition.getExpression() != null) {
                        DefaultMutableTreeNode aux = new DefaultMutableTreeNode(colorCondition);
                        padre.add(aux);
                    }
                }
            }
        }
    }

    /**
   * Retotrna el ColorConditionManager
   * @return
   */
    public ColorConditionManager getConditionManager() {
        return conditionManager;
    }
}
