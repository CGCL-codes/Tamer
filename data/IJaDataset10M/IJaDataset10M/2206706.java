package com.rapidminer.gui.r;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import com.rapidminer.gui.r.actions.ClearVariablesAction;
import com.rapidminer.gui.r.actions.LoadVariablesAction;
import com.rapidminer.gui.r.actions.SaveVariablesAction;
import com.rapidminer.gui.tools.ExtendedJList;
import com.rapidminer.gui.tools.ExtendedJScrollPane;
import com.rapidminer.gui.tools.ExtendedListModel;
import com.rapidminer.gui.tools.ViewToolBar;

/**
 * This is the viewer component of the variable list of the
 * current R Console
 * 
 * @author Sebastian Land
 */
public class RVariableListPanel extends JPanel {

    private static final long serialVersionUID = 3855827858319288341L;

    private ExtendedListModel variableListModel = new ExtendedListModel();

    private ExtendedJList variableList = new ExtendedJList(variableListModel);

    private ViewToolBar toolBar = new ViewToolBar();

    private RVariableList controller;

    public RVariableListPanel(RVariableList controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        variableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        variableList.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!variableList.isSelectionEmpty()) {
                    String insertedVariable = (String) variableListModel.getElementAt(variableList.locationToIndex(e.getPoint()));
                    if (e.getClickCount() == 2) {
                        RVariableListPanel.this.controller.insertVariableName(insertedVariable);
                    } else if (e.getClickCount() == 3) {
                        RVariableListPanel.this.controller.showVariable(insertedVariable);
                    }
                }
            }
        });
        ExtendedJScrollPane scrollPane = new ExtendedJScrollPane(variableList);
        add(scrollPane, BorderLayout.CENTER);
        toolBar.add(new SaveVariablesAction(controller));
        toolBar.add(new LoadVariablesAction(controller));
        toolBar.addSeparator();
        toolBar.add(new ClearVariablesAction(controller.getSession()));
        add(toolBar, BorderLayout.NORTH);
    }

    public List<String> getSelectedVariableNames() {
        List<String> selected = new LinkedList<String>();
        for (Object selectedValue : variableList.getSelectedValues()) {
            selected.add((String) selectedValue);
        }
        return selected;
    }

    public void setDefinedVariables(String[] variableNames) {
        Arrays.sort(variableNames);
        variableListModel.clear();
        boolean enabled = variableList.isEnabled();
        for (String variable : variableNames) {
            variableListModel.addElement(variable);
            variableListModel.setEnabled(variable, enabled);
        }
    }

    public void enableVariableList(boolean enable) {
        variableList.setEnabled(enable);
    }
}
