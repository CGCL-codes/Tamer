package ru.ifmo.fcdesigner.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Observer;
import java.util.Observable;
import taiga.config.ConfigurationException;
import taiga.config.impl.xml.XMLConfigurationException;
import ru.ifmo.fcdesigner.flowchart.view.FlowchartLoader;
import ru.ifmo.fcdesigner.flowchart.view.FlowchartElementView;
import ru.ifmo.fcdesigner.flowchart.view.layout.Layout;
import ru.ifmo.fcdesigner.gui.tools.FlowchartToolManager;

/**
 * Description.
 *
 * @author Dmitry Paraschenko
 */
public class MainFrame extends JFrame {

    private FlowchartToolManager flowchartToolManager;

    private JToggleButton defaultToolButton;

    private JToggleButton createStartElementToolButton;

    private JToggleButton createFinishElementToolButton;

    private JToggleButton createConditionElementToolButton;

    private JToggleButton createScriptElementToolButton;

    private JToggleButton createCallElementToolButton;

    private JToggleButton createInputElementToolButton;

    private JToggleButton createOutputElementToolButton;

    private JToggleButton createConnectorButton;

    private FlowchartComponent flowchartComponent;

    private ToolPanel toolPanel;

    public MainFrame() {
        super("Flowchart designer");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(600, 400);
        this.setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - 30 - getHeight()) / 2);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contentPane = this.getContentPane();
        this.setJMenuBar(new MenuPanel());
        toolPanel = new ToolPanel();
        contentPane.add(toolPanel, BorderLayout.PAGE_START);
        flowchartComponent = new FlowchartComponent();
        setFlowchart(null);
        contentPane.add(flowchartComponent, BorderLayout.CENTER);
    }

    private class ToolPanel extends JToolBar implements Observer {

        ButtonGroup buttonGroup;

        private ToolPanel() {
            super(JToolBar.HORIZONTAL);
            buttonGroup = new ButtonGroup();
            this.setFloatable(false);
            addButton(defaultToolButton = new JToggleButton(new DefaultToolAction()));
            defaultToolButton.setSelected(true);
            this.addSeparator();
            addButton(createStartElementToolButton = new JToggleButton(new CreateStartElementToolAction()));
            addButton(createFinishElementToolButton = new JToggleButton(new CreateFinishElementToolAction()));
            addButton(createConditionElementToolButton = new JToggleButton(new CreateConditionElementToolAction()));
            addButton(createScriptElementToolButton = new JToggleButton(new CreateScriptElementToolAction()));
            addButton(createCallElementToolButton = new JToggleButton(new CreateCallElementToolAction()));
            addButton(createInputElementToolButton = new JToggleButton(new CreateInputElementToolAction()));
            addButton(createOutputElementToolButton = new JToggleButton(new CreateOutputElementToolAction()));
            this.addSeparator();
            addButton(createConnectorButton = new JToggleButton(new CreateConnectorToolAction()));
        }

        private void addButton(JToggleButton button) {
            this.add(button);
            buttonGroup.add(button);
        }

        public void update(Observable o, Object arg) {
            defaultToolButton.setSelected(true);
        }
    }

    private class MenuPanel extends JMenuBar {

        private MenuPanel() {
            JMenu menu;
            menu = new JMenu(new FileAction());
            this.add(menu);
            menu.add(new CreateFlowchartAction());
            menu.addSeparator();
            menu.add(new SaveFlowchartAction());
            menu.add(new LoadFlowchartAction());
            menu.addSeparator();
            menu.add(new ExitAction());
            menu = new JMenu(new ActionsAction());
            this.add(menu);
            JMenu submenu = new JMenu(new AddElementAction());
            submenu.add(new CreateStartElementToolAction());
            submenu.add(new CreateFinishElementToolAction());
            submenu.add(new CreateConditionElementToolAction());
            submenu.add(new CreateScriptElementToolAction());
            submenu.add(new CreateCallElementToolAction());
            submenu.add(new CreateInputElementToolAction());
            submenu.add(new CreateOutputElementToolAction());
            submenu.addSeparator();
            submenu.add(new CreateConnectorToolAction());
            menu.add(submenu);
            menu.add(new RemoveElementAction());
            menu = new JMenu(new LayoutAction());
            this.add(menu);
            menu.add(new LayoutElementsAction());
            menu.add(new LayoutConnectorsAction());
            menu = new JMenu(new SettingsAction());
            this.add(menu);
        }
    }

    private void setFlowchart(FlowchartElementView flowchart) {
        flowchartToolManager = new FlowchartToolManager(flowchart);
        flowchartComponent.setFlowchart(flowchart);
        flowchartComponent.setFlowchartToolManager(flowchartToolManager);
        flowchartToolManager.addObserver(toolPanel);
        MainFrame.this.repaint();
    }

    private class FileAction extends AbstractAction {

        private FileAction() {
            putValue(Action.NAME, "File");
            putValue(Action.MNEMONIC_KEY, (int) 'F');
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class CreateFlowchartAction extends AbstractAction {

        private CreateFlowchartAction() {
            putValue(Action.NAME, "Create flowchart");
            putValue(Action.MNEMONIC_KEY, (int) 'C');
        }

        public void actionPerformed(ActionEvent e) {
            setFlowchart(new FlowchartElementView(new Rectangle(5, 5, 100, 100)));
        }
    }

    private class SaveFlowchartAction extends AbstractAction {

        private SaveFlowchartAction() {
            putValue(Action.NAME, "Save");
            putValue(Action.SHORT_DESCRIPTION, "Save flowchart");
            putValue(Action.MNEMONIC_KEY, (int) 'S');
        }

        public void actionPerformed(ActionEvent e) {
            FileDialog fileDialog = new FileDialog(MainFrame.this, "Save flowchart", FileDialog.SAVE);
            fileDialog.setVisible(true);
            if (fileDialog.getFile() == null) {
                return;
            }
            File file = new File(fileDialog.getDirectory(), fileDialog.getFile());
            System.out.println("Save to: '" + file.getAbsolutePath() + "'");
            FlowchartLoader.saveFlowchartToXML(file, flowchartToolManager.getFlowchart());
        }
    }

    private class LoadFlowchartAction extends AbstractAction {

        private LoadFlowchartAction() {
            putValue(Action.NAME, "Load");
            putValue(Action.SHORT_DESCRIPTION, "Load flowchart");
            putValue(Action.MNEMONIC_KEY, (int) 'L');
        }

        public void actionPerformed(ActionEvent e) {
            FileDialog fileDialog = new FileDialog(MainFrame.this, "Load flowchart", FileDialog.LOAD);
            fileDialog.setVisible(true);
            if (fileDialog.getFile() == null) {
                return;
            }
            File file = new File(fileDialog.getDirectory(), fileDialog.getFile());
            System.out.println("Loading from: '" + file.getAbsolutePath() + "'");
            try {
                setFlowchart(FlowchartLoader.loadFlowchartFromXML(file));
            } catch (ConfigurationException e1) {
                e1.printStackTrace();
            } catch (XMLConfigurationException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class ExitAction extends AbstractAction {

        private ExitAction() {
            putValue(Action.NAME, "Exit");
            putValue(Action.SHORT_DESCRIPTION, "Exit application");
            putValue(Action.MNEMONIC_KEY, (int) 'x');
        }

        public void actionPerformed(ActionEvent e) {
            MainFrame.this.dispose();
        }
    }

    private class LayoutAction extends AbstractAction {

        private LayoutAction() {
            putValue(Action.NAME, "Layout");
            putValue(Action.MNEMONIC_KEY, (int) 'L');
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class LayoutElementsAction extends AbstractAction {

        private LayoutElementsAction() {
            putValue(Action.NAME, "Elements");
            putValue(Action.MNEMONIC_KEY, (int) 'E');
        }

        public void actionPerformed(ActionEvent e) {
            Layout layout = new Layout(flowchartToolManager.getFlowchart());
            if (!layout.layoutElements()) {
                System.out.println("Error!");
            }
        }
    }

    private class LayoutConnectorsAction extends AbstractAction {

        private LayoutConnectorsAction() {
            putValue(Action.NAME, "Connectors");
            putValue(Action.MNEMONIC_KEY, (int) 'C');
        }

        public void actionPerformed(ActionEvent e) {
            Layout layout = new Layout(flowchartToolManager.getFlowchart());
            if (!layout.layoutConnectors()) {
                System.out.println("Error!");
            }
        }
    }

    private class SettingsAction extends AbstractAction {

        private SettingsAction() {
            putValue(Action.NAME, "Settings");
            putValue(Action.MNEMONIC_KEY, (int) 'S');
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class ActionsAction extends AbstractAction {

        private ActionsAction() {
            putValue(Action.NAME, "Actions");
            putValue(Action.MNEMONIC_KEY, (int) 'A');
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class AddElementAction extends AbstractAction {

        private AddElementAction() {
            putValue(Action.NAME, "Add");
            putValue(Action.MNEMONIC_KEY, (int) 'A');
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    private class RemoveElementAction extends AbstractAction {

        private RemoveElementAction() {
            putValue(Action.NAME, "Remove");
            putValue(Action.MNEMONIC_KEY, (int) 'R');
        }

        public void actionPerformed(ActionEvent e) {
            flowchartToolManager.removeSelectedElement();
        }
    }

    private class DefaultToolAction extends AbstractAction {

        private DefaultToolAction() {
            putValue(Action.NAME, "Select");
            putValue(Action.MNEMONIC_KEY, (int) 'e');
        }

        public void actionPerformed(ActionEvent e) {
            defaultToolButton.setSelected(true);
            flowchartToolManager.selectDefaultTool();
        }
    }

    private class CreateStartElementToolAction extends AbstractAction {

        private CreateStartElementToolAction() {
            putValue(Action.NAME, "Start");
            putValue(Action.MNEMONIC_KEY, (int) 'S');
        }

        public void actionPerformed(ActionEvent e) {
            createStartElementToolButton.setSelected(true);
            flowchartToolManager.selectCreateStartElementTool();
        }
    }

    private class CreateFinishElementToolAction extends AbstractAction {

        private CreateFinishElementToolAction() {
            putValue(Action.NAME, "Finish");
            putValue(Action.MNEMONIC_KEY, (int) 'F');
        }

        public void actionPerformed(ActionEvent e) {
            createFinishElementToolButton.setSelected(true);
            flowchartToolManager.selectCreateFinishElementTool();
        }
    }

    private class CreateConditionElementToolAction extends AbstractAction {

        private CreateConditionElementToolAction() {
            putValue(Action.NAME, "Condition");
            putValue(Action.MNEMONIC_KEY, (int) 'C');
        }

        public void actionPerformed(ActionEvent e) {
            createConditionElementToolButton.setSelected(true);
            flowchartToolManager.selectCreateConditionElementTool();
        }
    }

    private class CreateScriptElementToolAction extends AbstractAction {

        private CreateScriptElementToolAction() {
            putValue(Action.NAME, "Script");
            putValue(Action.MNEMONIC_KEY, (int) 'r');
        }

        public void actionPerformed(ActionEvent e) {
            createScriptElementToolButton.setSelected(true);
            flowchartToolManager.selectCreateScriptElementTool();
        }
    }

    private class CreateCallElementToolAction extends AbstractAction {

        private CreateCallElementToolAction() {
            putValue(Action.NAME, "Call");
            putValue(Action.MNEMONIC_KEY, (int) 'l');
        }

        public void actionPerformed(ActionEvent e) {
            createCallElementToolButton.setSelected(true);
            flowchartToolManager.selectCreateCallElementTool();
        }
    }

    private class CreateInputElementToolAction extends AbstractAction {

        private CreateInputElementToolAction() {
            putValue(Action.NAME, "Input");
            putValue(Action.MNEMONIC_KEY, (int) 'I');
        }

        public void actionPerformed(ActionEvent e) {
            createInputElementToolButton.setSelected(true);
            flowchartToolManager.selectCreateInputElementTool();
        }
    }

    private class CreateOutputElementToolAction extends AbstractAction {

        private CreateOutputElementToolAction() {
            putValue(Action.NAME, "Output");
            putValue(Action.MNEMONIC_KEY, (int) 'O');
        }

        public void actionPerformed(ActionEvent e) {
            createOutputElementToolButton.setSelected(true);
            flowchartToolManager.selectCreateOutputElementTool();
        }
    }

    private class CreateConnectorToolAction extends AbstractAction {

        private CreateConnectorToolAction() {
            putValue(Action.NAME, "ConnectorElement");
            putValue(Action.MNEMONIC_KEY, (int) 'n');
        }

        public void actionPerformed(ActionEvent e) {
            createConnectorButton.setSelected(true);
            flowchartToolManager.selectCreateConnectorTool();
        }
    }
}
