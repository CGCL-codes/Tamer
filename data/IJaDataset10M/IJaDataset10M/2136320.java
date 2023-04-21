package lx.ghm.xelerator;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.TreePath;
import lx.ghm.xelerator.gui.BatchRunGui;
import lx.ghm.xelerator.gui.ColorChooserPopup;
import lx.ghm.xelerator.gui.MainGui;
import lx.ghm.xelerator.gui.tree.XBeanNode;
import lx.ghm.xelerator.gui.tree.XTree;
import lx.ghm.xelerator.processor.XRunner;
import lx.ghm.xelerator.xml.XMLDocument;
import cz.dhl.term.VTMapper;

public class XController implements ActionListener {

    private MainGui gui;

    private MouseListener treeListener = null;

    private XTerminal recorder;

    public void setGui(MainGui g) {
        gui = g;
    }

    public void exit() {
    }

    public void setMainProject() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        XProjectsRoot projectsRoot = gui.getTree().getRoot();
        XProject oldMainProject = projectsRoot.getCurrentXProject();
        if (oldMainProject != null) {
            TreePath oldProjectPath = gui.getTree().selectNodeByBean(oldMainProject);
            gui.getTree().clearNodeBolder(oldProjectPath);
        }
        projectsRoot.setCurrentXProject(xProject);
        TreePath projectPath = gui.getTree().selectNodeByBean(xProject);
        gui.getTree().setNodeBolder(projectPath);
    }

    public void newProject() {
        XProjectsRoot projectsRoot = gui.getTree().getRoot();
        XProject xProject = new XProject();
        projectsRoot.addXProject(xProject);
        TreePath rootPath = gui.getTree().selectNodeByBean(projectsRoot);
        TreePath projectPath = gui.getTree().addNode(rootPath, xProject, xProject.getName());
        if (projectsRoot.size() == 1) {
            projectsRoot.setCurrentXProject(xProject);
            gui.getTree().setNodeBolder(projectPath);
        }
        gui.getTree().addNode(projectPath, xProject.getXParameter(), xProject.getXParameter().getName());
        gui.getTree().addNode(projectPath, xProject.getXTerminalGroup(), xProject.getXTerminalGroup().getName());
        gui.getTree().addNode(projectPath, xProject.getXWork(), xProject.getXWork().getName());
        gui.getTree().expandAll(projectPath, true);
    }

    public void openProject(String projectFileName) {
        XProject xProject = XMLDocument.readProjectFromXML(projectFileName);
        XParameter xParameter = xProject.getXParameter();
        XTerminalGroup xTerminalGroup = xProject.getXTerminalGroup();
        XWork xWork = xProject.getXWork();
        XProjectsRoot projectsRoot = gui.getTree().getRoot();
        TreePath rootPath = gui.getTree().selectNodeByBean(projectsRoot);
        TreePath projectPath = gui.getTree().addNode(rootPath, xProject, xProject.getName());
        gui.getTree().addNode(projectPath, xParameter, xParameter.getName());
        TreePath xTerminalGroupPath = gui.getTree().addNode(projectPath, xTerminalGroup, xTerminalGroup.getName());
        TreePath xWorkPath = gui.getTree().addNode(projectPath, xWork, xWork.getName());
        for (int i = 0; i < xTerminalGroup.size(); i++) {
            XTerminal xTerminal = xTerminalGroup.get(i);
            TreePath xTerminalPath = gui.getTree().addNode(xTerminalGroupPath, xTerminal, xTerminal.getName());
            if (xTerminal.getDefinedColor().length() > 0) {
                Color color = new Color(Integer.parseInt(xTerminal.getDefinedColor()));
                gui.getTree().setNodeColor(xTerminalPath, color);
            }
        }
        for (int j = 0; j < xWork.size(); j++) {
            XNode tmpNode = xWork.get(j);
            if (tmpNode instanceof XNotification) {
                XNotification xNotification = (XNotification) tmpNode;
                gui.getTree().addNode(xWorkPath, xNotification, xNotification.getName());
            } else if (tmpNode instanceof XBreakPoint) {
                XBreakPoint xBreakPoint = (XBreakPoint) tmpNode;
                gui.getTree().addNode(xWorkPath, xBreakPoint, xBreakPoint.getName());
            } else if (tmpNode instanceof XTask) {
                XTask xTask = (XTask) tmpNode;
                TreePath xTaskPath = gui.getTree().addNode(xWorkPath, xTask, xTask.getName());
                XTerminal tmpXTerminal = xTask.resetTerminal(xTerminalGroup);
                if (tmpXTerminal.getDefinedColor().length() > 0) {
                    Color color = new Color(Integer.parseInt(tmpXTerminal.getDefinedColor()));
                    gui.getTree().setNodeColor(xTaskPath, color);
                }
                for (int k = 0; k < xTask.size(); k++) {
                    gui.getTree().addNode(xTaskPath, xTask.get(k), xTask.get(k).getName());
                }
            } else if (tmpNode instanceof XTimer) {
                XTimer xTimer = (XTimer) tmpNode;
                TreePath xTimerPath = gui.getTree().addNode(xWorkPath, xTimer, xTimer.getName());
                XTerminal tmpXTerminal = xTimer.resetTerminal(xTerminalGroup);
                if (tmpXTerminal.getDefinedColor().length() > 0) {
                    Color color = new Color(Integer.parseInt(tmpXTerminal.getDefinedColor()));
                    gui.getTree().setNodeColor(xTimerPath, color);
                }
            }
        }
        projectsRoot.addXProject(xProject);
        if (projectsRoot.size() == 1) {
            projectsRoot.setCurrentXProject(xProject);
            gui.getTree().setNodeBolder(projectPath);
            gui.getTree().expandAll(projectPath, true);
        }
    }

    public void saveProject(String projectFileName) {
        XProject xProject = this.getParentProject(gui.getTree().getSelectionPath());
        if (xProject == null) {
            gui.showError("Please choose a XProject to save.");
            return;
        }
        XMLDocument.writeXML(xProject, projectFileName);
    }

    public void closeProject() {
        XProject xProject = this.getParentProject(gui.getTree().getSelectionPath());
        if (xProject == null) {
            gui.showError("Please choose a XProject to close.");
            return;
        }
        if (xProject.getXTerminalGroup() != null) {
            for (int i = 0; i < xProject.getXTerminalGroup().size(); i++) {
                xProject.getXTerminalGroup().get(i).close();
            }
        }
        gui.getTree().removeNode(gui.getTree().selectNodeByBean(xProject));
        XProjectsRoot projectsRoot = gui.getTree().getRoot();
        if (xProject == projectsRoot.getCurrentXProject()) {
            projectsRoot.setCurrentXProject(null);
        }
        projectsRoot.removeXProject(xProject);
    }

    public void setXTerminalColor(int x, int y) {
        final ColorChooserPopup chooser = new ColorChooserPopup();
        chooser.addColorChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                TreePath nodePath = gui.getTree().getSelectionPath();
                if (nodePath == null) {
                    gui.showError("Only XTerminal Node could be set color!");
                    return;
                }
                Object bean = gui.getTree().getSelectedBean(nodePath);
                if (bean instanceof XTerminal) {
                    Color color = chooser.getColor();
                    gui.getTree().setNodeColor(nodePath, color);
                    chooser.setVisible(false);
                    XTerminal xTerm = (XTerminal) bean;
                    xTerm.setDefinedColor(new Integer(color.getRGB()).toString());
                    XWork xWork = getParentProject(nodePath).getXWork();
                    for (int i = 0; i < xWork.size(); i++) {
                        Object tmpObj = xWork.get(i);
                        if (tmpObj instanceof XTask || tmpObj instanceof XTimer) {
                            IXTask iTask = (IXTask) tmpObj;
                            if (xTerm.hashCode() == iTask.getXTerminal().hashCode()) {
                                TreePath xTaskPath = gui.getTree().selectNodeForObject(iTask);
                                gui.getTree().setNodeColor(xTaskPath, color);
                            }
                        }
                    }
                    gui.getTree().setSelectionPath(nodePath);
                }
            }
        });
        chooser.show(gui, x, y);
    }

    public void createXParametersNode() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        if (xProject.getXParameter() != null) {
            gui.showError("Only one XParameter Node could be added!");
            return;
        }
        TreePath projectPath = gui.getTree().selectNodeByBean(xProject);
        if (projectPath != null) {
            XParameter xParameter = new XParameter();
            gui.getTree().addNode(projectPath, xParameter, "xParameter");
            xProject.setXParameter(xParameter);
            return;
        }
        gui.showError("Please select/add a XProject first to add assertions.");
    }

    public void createXTerminalGroup() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        if (xProject.getXTerminalGroup() != null) {
            gui.showError("Only one XTerminalGroup Node could be added!");
            return;
        }
        TreePath projectPath = gui.getTree().selectNodeByBean(xProject);
        if (projectPath != null) {
            XTerminalGroup xTerminalGroup = new XTerminalGroup();
            gui.getTree().addNode(projectPath, xTerminalGroup, "xTerminalGroup");
            xProject.setXTerminalGroup(xTerminalGroup);
            return;
        }
        gui.showError("Please select/add a XProject first to add assertions.");
    }

    public void createXWrok() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        if (xProject.getXWork() != null) {
            gui.showError("Only one XWrok Node could be added!");
            return;
        }
        TreePath projectPath = gui.getTree().selectNodeByBean(xProject);
        if (projectPath != null) {
            XWork xWork = new XWork();
            xWork.setName("XWork");
            gui.getTree().addNode(projectPath, xWork, xWork.getName());
            xProject.setXWork(xWork);
            return;
        }
        gui.showError("Please select/add a XProject first to add XWork.");
    }

    public void addXTerminal(String hostIP, String hostPort, String loginName, String password) {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        TreePath parentPath = gui.getTree().selectNodeByBean(xProject.getXTerminalGroup());
        if (parentPath != null) {
            XTerminal xTerm = new XTerminal(hostIP, hostPort, loginName, password);
            xTerm.setId(new Integer(xTerm.hashCode()).toString());
            xProject.getXTerminalGroup().xTerminalHT.put(xTerm.getId(), xTerm);
            gui.getTree().addNode(parentPath, xTerm, xTerm.getName());
            xProject.getXTerminalGroup().add(xTerm);
            return;
        }
        gui.showError("Please select/add a XTerminalGroup first to add XTerminal.");
    }

    public void addXTask(XTerminal xTerm, String taskName, String currentPrompt, String command) {
        TreePath currentPath = gui.getTree().getSelectionPath();
        if (currentPath != null) {
            XProject xProject = getParentProject(currentPath);
            Object bean = gui.getTree().getSelectedBean(currentPath);
            if (bean instanceof XTask || bean instanceof XTimer || bean instanceof XBreakPoint || bean instanceof XNotification) {
                XTask xTask = new XTask(xTerm, currentPrompt, command);
                xTask.setName(taskName);
                xTask.setXTerminal(recorder);
                xProject.getXWork().insertAfter((XNode) bean, xTask);
                gui.getTree().insertNode(currentPath, xTask, taskName);
                gui.getTree().expandAll(currentPath.getParentPath(), true);
                gui.getTree().setSelectionPath(currentPath);
            } else {
                TreePath xWorkPath = gui.getTree().selectNodeByBean(xProject.getXWork());
                XTask xTask = new XTask(xTerm, currentPrompt, command);
                xTask.setName(taskName);
                xTask.setXTerminal(recorder);
                xProject.getXWork().add(xTask);
                gui.getTree().addNode(xWorkPath, xTask, taskName);
                gui.getTree().expandAll(xWorkPath, true);
                gui.getTree().setSelectionPath(currentPath);
            }
            return;
        }
        gui.showError("Please add a XTask under XWork node.");
    }

    public void addXAssertion(String assertionContent, boolean isRegExp, boolean hasGlobalVar, boolean isContain) {
        TreePath parentPath = gui.getTree().getSelectionPath();
        if (parentPath != null) {
            Object bean = gui.getTree().getSelectedBean(parentPath);
            if (bean instanceof XTask) {
                XTask xTask = (XTask) bean;
                XAssertion xAssertion = new XAssertion(assertionContent, isRegExp, hasGlobalVar, isContain);
                xTask.addAssertion(xAssertion);
                gui.getTree().addNode(parentPath, xAssertion, xAssertion.getName());
                gui.getTree().expandPath(parentPath);
                return;
            }
        }
        gui.showError("Please add a XAssertion under XTask node.");
    }

    public void addXTimer(XTerminal xTerm, String seconds) {
        TreePath currentPath = gui.getTree().getSelectionPath();
        if (currentPath != null) {
            XProject xProject = getParentProject(gui.getTree().getSelectionPath());
            Object bean = gui.getTree().getSelectedBean(currentPath);
            if (bean instanceof XTask || bean instanceof XTimer || bean instanceof XBreakPoint || bean instanceof XNotification) {
                XTimer xTimer = new XTimer(xTerm, "0");
                xTimer.setName(xTimer.getName());
                xTimer.setXTerminal(recorder);
                gui.getTree().insertNode(currentPath, xTimer, xTimer.getName());
                xProject.getXWork().insertAfter((XNode) bean, xTimer);
                gui.getTree().expandAll(currentPath.getParentPath(), true);
            } else {
                TreePath xWorkPath = gui.getTree().selectNodeByBean(xProject.getXWork());
                XTimer xTimer = new XTimer(xTerm, "0");
                xTimer.setName(xTimer.getName());
                xTimer.setXTerminal(recorder);
                xProject.getXWork().add(xTimer);
                gui.getTree().addNode(xWorkPath, xTimer, xTimer.getName());
                gui.getTree().expandAll(xWorkPath, true);
            }
            return;
        }
        gui.showError("Please add a XTimer under XWork node.");
    }

    public void addXBreakPoint() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        TreePath currentPath = gui.getTree().getSelectionPath();
        if (currentPath != null) {
            Object bean = gui.getTree().getSelectedBean(currentPath);
            if (bean instanceof XTask || bean instanceof XTimer || bean instanceof XBreakPoint || bean instanceof XNotification) {
                XBreakPoint xBreakPoint = new XBreakPoint();
                gui.getTree().insertNode(currentPath, xBreakPoint, "XBreakPoint");
                xProject.getXWork().insertAfter((XNode) bean, xBreakPoint);
                gui.getTree().expandAll(currentPath.getParentPath(), true);
            }
            return;
        }
        gui.showError("Please add a XBreakPoint under XWork node.");
    }

    public void addXNotification() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        TreePath currentPath = gui.getTree().getSelectionPath();
        if (currentPath != null) {
            Object bean = gui.getTree().getSelectedBean(currentPath);
            if (bean instanceof XWork) {
                XNotification xNotification = new XNotification();
                gui.getTree().addNode(currentPath, xNotification, "XNotification");
                xProject.getXWork().add(xNotification);
                gui.getTree().expandAll(currentPath, true);
            } else if (bean instanceof XTask || bean instanceof XTimer || bean instanceof XBreakPoint || bean instanceof XNotification) {
                XNotification xNotification = new XNotification();
                gui.getTree().insertNode(currentPath, xNotification, "XNotification");
                xProject.getXWork().insertAfter((XNode) bean, xNotification);
                gui.getTree().expandAll(currentPath.getParentPath(), true);
            }
            return;
        }
        gui.showError("Please add a XNotification under XWork node.");
    }

    public void removeNode() {
        TreePath path = gui.getTree().getSelectionPath();
        if (path != null) {
            XProject xProject = getParentProject(gui.getTree().getSelectionPath());
            Object bean = gui.getTree().getSelectedBean(path);
            if (bean instanceof XTerminal) {
                XTerminal xTerminal = (XTerminal) bean;
                if (!xTerminal.isClose) xTerminal.close();
                xProject.getXTerminalGroup().remove(xTerminal);
                gui.getTree().removeNode(path);
            } else if (bean instanceof XTask) {
                XTask xTask = (XTask) bean;
                xProject.getXWork().remove(xTask);
                gui.getTree().removeNode(path);
            } else if (bean instanceof XTimer) {
                XTimer xTimer = (XTimer) bean;
                xProject.getXWork().remove(xTimer);
                gui.getTree().removeNode(path);
            } else if (bean instanceof XAssertion) {
                TreePath parentPath = path.getParentPath();
                XTask xTask = (XTask) gui.getTree().getSelectedBean(parentPath);
                xTask.removeAssertion((XAssertion) bean);
                gui.getTree().removeNode(path);
            } else if (bean instanceof XBreakPoint) {
                XBreakPoint xBreakPoint = (XBreakPoint) bean;
                xProject.getXWork().remove(xBreakPoint);
                gui.getTree().removeNode(path);
            } else if (bean instanceof XNotification) {
                XNotification xNotification = (XNotification) bean;
                xProject.getXWork().remove(xNotification);
                gui.getTree().removeNode(path);
            } else {
                gui.showError("You can only delete XTerminal, XTask or XAssertion.");
            }
            gui.getTree().expandAll(gui.getTree().selectNodeByBean(xProject), true);
            return;
        }
        gui.showError("Please select a XNode to delete.");
    }

    public void batchRun() {
        XProjectsRoot projectsRoot = gui.getTree().getRoot();
        if (projectsRoot.size() <= 0) {
            gui.showError("Please open XProjects first.");
            return;
        }
        gui.getTree().expandAll();
        BatchRunGui batchRunGUI = new BatchRunGui();
        batchRunGUI.setXProjects(projectsRoot.getAllXProjects());
        batchRunGUI.setXGui(gui);
        JDialog dialog = new JDialog(gui, "Batch Run XProjects", true);
        dialog.setSize(800, 600);
        dialog.setLocation(180, 70);
        dialog.getContentPane().add(batchRunGUI);
        dialog.setModal(false);
        dialog.show();
    }

    private XRunner runner = new XRunner();

    public void StartRun() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        if (xProject == null || xProject.getXTerminalGroup() == null || xProject.getXWork() == null || xProject.getXTerminalGroup().size() <= 0 || xProject.getXWork().size() <= 0) {
            showProjectNotSelectedError();
            return;
        }
        gui.runBtn.setEnabled(false);
        gui.pauseBtn.setEnabled(true);
        gui.getMenuStartRun().setEnabled(false);
        gui.getMenuPauseRun().setEnabled(true);
        if (runner.isStop) {
            runner = new XRunner(gui, xProject);
        }
        runner = new XRunner(gui, xProject);
        runner.start();
    }

    public void ContinueRun() {
        gui.continueBtn.setEnabled(false);
        gui.pauseBtn.setEnabled(true);
        gui.getMenuContinueRun().setEnabled(true);
        gui.getMenuPauseRun().setEnabled(true);
        runner.continueRun();
    }

    public void PauseRun() {
        gui.continueBtn.setEnabled(true);
        gui.pauseBtn.setEnabled(false);
        gui.getMenuContinueRun().setEnabled(true);
        gui.getMenuPauseRun().setEnabled(false);
        runner.pauseRun();
    }

    public void StopRun() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        if (xProject == null) return;
        if (xProject.getXTerminalGroup() == null || xProject.getXWork() == null) return;
        gui.runBtn.setEnabled(true);
        gui.continueBtn.setEnabled(false);
        gui.pauseBtn.setEnabled(false);
        gui.getMenuContinueRun().setEnabled(true);
        gui.getMenuContinueRun().setEnabled(false);
        gui.getMenuPauseRun().setEnabled(false);
        runner.stopRun();
        gui.getTree().clearTreeColor();
    }

    private void showProjectNotSelectedError() {
        gui.showError("Please selecte a XProject.");
    }

    public void startRecord() {
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        if (xProject == null) showProjectNotSelectedError();
        XTerminalGroup terminalGroup = xProject.getXTerminalGroup();
        if (terminalGroup == null || terminalGroup.size() == 0) {
            gui.showError("Please add a XTerminal at least.");
            return;
        }
        JDialog dialog = new JDialog(gui, "Select a XTerminal to record", true);
        JComboBox comboBox = new JComboBox();
        dialog.setSize(300, 60);
        dialog.setLocation(300, 300);
        for (int i = 0; i < terminalGroup.size(); i++) {
            comboBox.addItem(terminalGroup.get(i));
        }
        dialog.getContentPane().add(comboBox);
        comboBox.setSize(60, 10);
        dialog.show();
        System.out.println(comboBox.getSelectedIndex());
        recorder = (XTerminal) comboBox.getSelectedItem();
        if (recorder.isClose) {
            recorder.login(xProject);
            recorder.show(false);
            new Thread(recorder).start();
        }
        VTMapper mapper = recorder.getEmulator().getMapper();
        mapper.setJTerminal(recorder.getTerminal());
        mapper.setXController(this);
        mapper.RECORD_MODE = true;
        mapper.START_RECORDING = true;
        gui.getMenuStartRecord().setEnabled(false);
        gui.getMenuStopRecord().setEnabled(true);
        gui.recordBtn.setEnabled(false);
        gui.stopRecordBtn.setEnabled(true);
    }

    public void stopRecord() {
        VTMapper mapper = recorder.getEmulator().getMapper();
        if (mapper == null) return;
        mapper.RECORD_MODE = false;
        mapper.START_RECORDING = false;
        gui.getMenuStartRecord().setEnabled(true);
        gui.getMenuStopRecord().setEnabled(false);
        gui.recordBtn.setEnabled(true);
        gui.stopRecordBtn.setEnabled(false);
    }

    public void record(String prompt, String command) {
        String taskName = recorder.getName() + ":" + command;
        if (command.length() == 0) taskName = recorder.getName() + ": [ENTER]";
        addXTask(recorder, taskName, prompt, command);
    }

    private void insert(JTextPane textPane, String str, AttributeSet attrSet) {
        Document doc = textPane.getDocument();
        try {
            doc.insertString(doc.getLength(), str, attrSet);
        } catch (BadLocationException e) {
            System.out.println("BadLocationException: " + e);
        }
    }

    private void setDocs(JTextPane textPane, String str, Color col, boolean bold, int fontSize) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attrSet, col);
        if (bold == true) {
            StyleConstants.setBold(attrSet, true);
        }
        StyleConstants.setFontSize(attrSet, fontSize);
        insert(textPane, str, attrSet);
    }

    public void checkXTaskScript() {
        String scriptContent = "";
        XProject xProject = getParentProject(gui.getTree().getSelectionPath());
        XWork xWork = xProject.getXWork();
        JTextPane textPane = new JTextPane();
        JScrollPane contentPane = new JScrollPane(textPane);
        for (int i = 0; i < xWork.size(); i++) {
            Object workNode = xWork.get(i);
            if (workNode instanceof XTask) {
                XTask xTask = (XTask) workNode;
                String term = xTask.getXTerminal().getName();
                String command = xTask.getCommand();
                String prompt = xTask.getPrompt();
                if (xTask.isHasGlobalVar()) prompt = xProject.getXParameter().doTranslate(prompt);
                if (xTask.isHasGlobalVar()) command = xProject.getXParameter().doTranslate(command);
                scriptContent = scriptContent + "[" + term + "][Prompt]:" + prompt + "\n";
                scriptContent = scriptContent + "[" + term + "][Command]:" + command + "\n";
                for (int j = 0; j < xTask.size(); j++) {
                    XAssertion xAssertion = xTask.get(j);
                    String assertionPrompt = xAssertion.getPrompt();
                    String assertionContent = xAssertion.getContent();
                    if (xAssertion.isHasGlobalVar()) {
                        assertionPrompt = xProject.getXParameter().doTranslate(assertionPrompt);
                        assertionContent = xProject.getXParameter().doTranslate(assertionContent);
                    }
                    scriptContent = scriptContent + "\t [Check Assertion][Prompt]:" + assertionPrompt + "\n";
                    scriptContent = scriptContent + "\t [Check Assertion][Content]:" + assertionContent + "\n";
                }
            } else if (workNode instanceof XNotification) {
                scriptContent = scriptContent + "Send XNotification\n";
            } else if (workNode instanceof XBreakPoint) {
                scriptContent = scriptContent + "Set XBreakPoint\n";
            } else if (workNode instanceof XTimer) {
                XTimer xTimer = (XTimer) workNode;
                String term = xTimer.getXTerminal().getName();
                scriptContent = scriptContent + "[" + term + "]:" + "wait for " + xTimer.getSeconds() + "seconds.\n";
            }
        }
        if (scriptContent.equals("")) return;
        textPane.setText(scriptContent);
        JDialog dialog = new JDialog(gui, "Check XTask Scripts:", true);
        dialog.setSize(800, 600);
        dialog.setLocation(180, 70);
        dialog.setContentPane(contentPane);
        dialog.setModal(false);
        dialog.show();
    }

    private void clickRunNode(int selRow, TreePath path) {
        XProject xProject = getParentProject(path);
        Object curSel = gui.getTree().getSelectedBean(path);
        if (curSel == null) {
            return;
        }
        if (curSel instanceof XProject) {
            gui.getTree().expandAll(gui.getTree().selectNodeByBean(xProject), true);
        } else if (curSel instanceof XTerminal) {
            XTerminal xTerminal = (XTerminal) curSel;
            if (xTerminal.isClose) {
                xTerminal.login(xProject);
                xTerminal.show(false);
                new Thread(xTerminal).start();
            } else {
                xTerminal.getFrame().toFront();
            }
        } else if (curSel instanceof IXTask) {
            IXTask task = (IXTask) curSel;
            XTerminal xTerminal = task.getXTerminal();
            if (!xTerminal.isClose) {
                xTerminal.getFrame().toFront();
            }
        }
    }

    private Object preSelectedNode = null;

    private XProject getParentProject(TreePath path) {
        if (path == null) return null;
        Object[] objs = path.getPath();
        if (objs.length >= 1) {
            Object tmpNode = ((XBeanNode) objs[1]).getBean();
            if (tmpNode instanceof XProject) {
                return (XProject) tmpNode;
            }
        }
        return null;
    }

    private void showNodeGui(int selRow, TreePath path) {
        Object curSel = gui.getTree().getSelectedBean(path);
        if (curSel == null) return;
        XProject xProject = getParentProject(path);
        if (xProject == null) return;
        if (preSelectedNode != null) {
            if (preSelectedNode instanceof XProject) {
                gui.projectGUI.save();
            } else if (preSelectedNode instanceof XTerminalGroup) {
                gui.terminalGroupGUI.save();
            } else if (preSelectedNode instanceof XTerminal) {
                gui.terminalGUI.save();
            } else if (preSelectedNode instanceof XParameter) {
                gui.parameterGUI.save();
            } else if (preSelectedNode instanceof XWork) {
                gui.workGUI.save();
            } else if (preSelectedNode instanceof XTask) {
                gui.taskGUI.save();
            } else if (preSelectedNode instanceof XTimer) {
                gui.timerGUI.save();
            } else if (preSelectedNode instanceof XAssertion) {
                gui.assertionGUI.save();
            } else if (preSelectedNode instanceof XNotification) {
                gui.notificationGUI.save();
            }
        }
        if (curSel instanceof XProject) {
            gui.projectGUI.setObject(gui.getTree().getSelectedNode(path));
            gui.getContentPanel().setViewportView(gui.projectGUI);
        } else if (curSel instanceof XTerminalGroup) {
            gui.terminalGroupGUI.setObject(gui.getTree().getSelectedNode(path));
            gui.getContentPanel().setViewportView(gui.terminalGroupGUI);
        } else if (curSel instanceof XTerminal) {
            gui.terminalGUI.setObject(gui.getTree().getSelectedNode(path));
            gui.getContentPanel().setViewportView(gui.terminalGUI);
        } else if (curSel instanceof XParameter) {
            gui.parameterGUI.setObject(gui.getTree().getSelectedNode(path));
            gui.getContentPanel().setViewportView(gui.parameterGUI);
        } else if (curSel instanceof XWork) {
            gui.workGUI.setObject(gui.getTree().getSelectedNode(path));
            gui.getContentPanel().setViewportView(gui.workGUI);
        } else if (curSel instanceof XTask) {
            gui.taskGUI.setObject(gui.getTree().getSelectedNode(path), xProject.getXTerminalGroup());
            gui.getContentPanel().setViewportView(gui.taskGUI);
        } else if (curSel instanceof XTimer) {
            gui.timerGUI.setObject(gui.getTree().getSelectedNode(path), xProject.getXTerminalGroup());
            gui.getContentPanel().setViewportView(gui.timerGUI);
        } else if (curSel instanceof XAssertion) {
            gui.assertionGUI.setObject(gui.getTree().getSelectedNode(path));
            gui.getContentPanel().setViewportView(gui.assertionGUI);
        } else if (curSel instanceof XNotification) {
            gui.notificationGUI.setObject(gui.getTree().getSelectedNode(path));
            gui.getContentPanel().setViewportView(gui.notificationGUI);
        } else {
            return;
        }
        preSelectedNode = curSel;
    }

    private void showPopupMenu(Component invoker, int x, int y, int selRow, TreePath path) {
        Object curSel = gui.getTree().getSelectedBean(path);
        if (curSel != null) {
            if (curSel instanceof XTerminal) {
                setXTerminalColor(x, y);
                return;
            }
            gui.getPopupMenu().show(invoker, x, y);
        }
    }

    public MouseListener getTreeListener() {
        if (treeListener == null) {
            treeListener = new MouseAdapter() {

                public void mouseClicked(MouseEvent evt) {
                    XTree tree = (XTree) evt.getSource();
                    TreePath path = tree.getPathForLocation(evt.getX(), evt.getY());
                    if (path != null) {
                        int selRow = tree.getRowForLocation(evt.getX(), evt.getY());
                        if (selRow != -1) {
                            if (evt.getModifiers() == evt.BUTTON3_MASK) {
                                gui.getTree().setSelectionPath(path);
                                showPopupMenu(evt.getComponent(), evt.getX(), evt.getY(), selRow, path);
                            } else if (evt.getClickCount() == 1) {
                                showNodeGui(selRow, path);
                            } else if (evt.getClickCount() == 2) {
                                clickRunNode(selRow, path);
                            }
                        }
                    }
                }
            };
        }
        return treeListener;
    }

    public void actionPerformed(ActionEvent e) {
    }

    public Action NewProjectAction = new AbstractAction("New") {

        public void actionPerformed(ActionEvent evt) {
            try {
                newProject();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };

    public Action OpenProjectAction = new AbstractAction("Open") {

        public void actionPerformed(ActionEvent evt) {
            try {
                JFileChooser fDialog = new JFileChooser(".");
                fDialog.setMultiSelectionEnabled(true);
                fDialog.setDialogType(JFileChooser.OPEN_DIALOG);
                XFileFilter filter = new XFileFilter("xml");
                filter.setDescription("Xelerator File(xml)");
                fDialog.addChoosableFileFilter(filter);
                int result = fDialog.showOpenDialog(gui);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] files = fDialog.getSelectedFiles();
                    for (int i = 0; i < files.length; i++) {
                        String fname = files[i].getAbsolutePath();
                        openProject(fname);
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };

    public Action SaveProjectAction = new AbstractAction("Save") {

        public void actionPerformed(ActionEvent evt) {
            try {
                JFileChooser fDialog = new JFileChooser(".");
                fDialog.setDialogTitle("Save Xelerator Project");
                fDialog.setDialogType(JFileChooser.SAVE_DIALOG);
                int result = fDialog.showSaveDialog(gui);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String fname = fDialog.getSelectedFile().getAbsolutePath();
                    saveProject(fname);
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };

    public Action StartRunAction = new AbstractAction("Open") {

        public void actionPerformed(ActionEvent evt) {
            try {
                StartRun();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };

    public Action ContinueRunAction = new AbstractAction("Continue") {

        public void actionPerformed(ActionEvent evt) {
            try {
                ContinueRun();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };

    public Action PauseRunAction = new AbstractAction("Pause") {

        public void actionPerformed(ActionEvent evt) {
            try {
                PauseRun();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };

    public Action StopRunAction = new AbstractAction("Stop") {

        public void actionPerformed(ActionEvent evt) {
            try {
                StopRun();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };

    public Action RecordAction = new AbstractAction("Record") {

        public void actionPerformed(ActionEvent evt) {
            try {
                startRecord();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };

    public Action StopRecordAction = new AbstractAction("StopRecord") {

        public void actionPerformed(ActionEvent evt) {
            try {
                stopRecord();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    };
}
