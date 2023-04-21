package edu.umich.soar.debugger.doc;

import org.eclipse.swt.SWT;
import sml.Kernel;
import edu.umich.soar.debugger.MainFrame;
import edu.umich.soar.debugger.dialogs.NewWindowDialog;
import edu.umich.soar.debugger.general.JavaElementXML;
import edu.umich.soar.debugger.helpers.Logger;
import edu.umich.soar.debugger.manager.MainWindow;
import edu.umich.soar.debugger.manager.Pane;
import edu.umich.soar.debugger.modules.AbstractView;

/************************************************************************
 * 
 * A set of commands that produce actions within the debugger (e.g. closing a
 * window or operating a menu item).
 * 
 * This allows us to script the debugger from within modules and do pretty
 * arbitrary code actions using a simple sort of scripting.
 * 
 ************************************************************************/
public class ScriptCommands {

    protected MainFrame m_Frame;

    protected Document m_Document;

    public ScriptCommands(MainFrame frame, Document doc) {
        m_Frame = frame;
        m_Document = doc;
    }

    public Object executeCommand(String command, boolean echoCommand) {
        if (command == null) return null;
        String[] tokens = command.split(" ");
        if (tokens.length == 0) return null;
        String first = tokens[0];
        if (first.equals("loadsource")) {
            m_Frame.loadSource();
            return null;
        }
        if (first.equals("askcd")) {
            m_Frame.getFileMenu().changeDirectory();
            return null;
        }
        if (first.equals("removeview")) {
            return executeRemoveView(tokens);
        }
        if (first.equals("replaceview")) {
            return executeReplaceView(tokens);
        }
        if (first.equals("addview")) {
            return executeAddView(tokens);
        }
        if (first.equals("addtab")) {
            return executeAddTab(tokens);
        }
        if (first.equals("renameview")) {
            return executeRenameView(tokens);
        }
        if (first.equals("clear")) {
            return executeClearView(tokens);
        }
        if (first.equals("movetabs")) {
            return executeMoveTabs(tokens);
        }
        if (first.equals("remote")) {
            return executeRemoteConnect(tokens);
        }
        if (first.equals("copy") || first.equals("paste")) {
            return executeCopyPaste(tokens);
        }
        if (first.equals("log")) {
            return executeLog(tokens);
        }
        if (first.equals("properties")) {
            String frameName = tokens[1];
            String viewName = tokens[2];
            MainFrame frame = m_Document.getFrameByName(frameName);
            AbstractView view = frame.getView(viewName);
            view.showProperties();
        }
        return null;
    }

    protected Object executeClearView(String[] tokens) {
        String frameName = tokens[1];
        String viewName = tokens[2];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        view.clearDisplay();
        return null;
    }

    protected Object executeRemoteConnect(String[] tokens) {
        String ip = tokens.length >= 2 ? tokens[1] : null;
        String portStr = tokens.length >= 3 ? tokens[2] : Integer.toString(Kernel.GetDefaultPort());
        String agentName = tokens.length >= 4 ? tokens[3] : null;
        int port = Integer.parseInt(portStr);
        if (m_Document.isRemote() || m_Document.isStopping()) return Boolean.FALSE;
        try {
            m_Document.remoteConnect(ip, port, agentName);
            return Boolean.TRUE;
        } catch (Exception ex) {
            m_Frame.ShowMessageBox("Error making remote connection: " + ex.getMessage());
            return Boolean.FALSE;
        }
    }

    protected Object executeCopyPaste(String[] tokens) {
        String command = tokens[0];
        String frameName = tokens[1];
        String viewName = tokens[2];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        if (command.equalsIgnoreCase("copy")) view.copy(); else view.paste();
        return Boolean.TRUE;
    }

    protected Object executeLog(String[] tokens) {
        String action = tokens[1];
        String frameName = tokens[2];
        String viewName = tokens[3];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        Logger logger = view.getLogger();
        if (action.equalsIgnoreCase("start")) logger.startLogging(false); else if (action.equalsIgnoreCase("append")) logger.startLogging(true); else if (action.equalsIgnoreCase("dialog")) logger.showDialog(); else if (action.equalsIgnoreCase("stop")) logger.stopLogging();
        return Boolean.TRUE;
    }

    protected Object executeAddView(String[] tokens) {
        String frameName = tokens[1];
        String viewName = tokens[2];
        String direction = tokens[3];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        Pane pane = view.getPane();
        Module module = NewWindowDialog.showDialog(frame, "Select new window", m_Document.getModuleList());
        if (module == null) return null;
        AbstractView newView = module.createInstance();
        if (newView == null) return null;
        boolean addSash = !newView.isFixedSizeView();
        boolean storeContent = true;
        JavaElementXML xml = frame.getMainWindow().convertToXML(storeContent);
        JavaElementXML existingPane = pane.getElementXML();
        JavaElementXML parent = existingPane.getParent();
        if (!(newView.isFixedSizeView() && view.isFixedSizeView())) {
            while (parent.getTagName().equals("composite")) {
                existingPane = parent;
                parent = parent.getParent();
            }
        }
        boolean horiz = ((direction.equals(MainWindow.kAttachTopValue) || direction.equals(MainWindow.kAttachBottomValue)));
        Pane newPane = new Pane("no parent");
        newPane.setHorizontalOrientation(horiz);
        newView.generateName(frame);
        newPane.addView(newView);
        JavaElementXML newChild1 = existingPane;
        JavaElementXML newChild2 = newPane.convertToXML(Pane.kTagName, false);
        JavaElementXML newParent = null;
        if (addSash) {
            int orientation = (direction.equals(MainWindow.kAttachLeftValue) || direction.equals(MainWindow.kAttachRightValue)) ? SWT.HORIZONTAL : SWT.VERTICAL;
            int[] weights = new int[] { 50, 50 };
            newParent = frame.getMainWindow().buildXMLForSashForm(orientation, weights);
            if (direction.equals(MainWindow.kAttachRightValue) || direction.equals(MainWindow.kAttachBottomValue)) {
                newParent.addChildElement(newChild1);
                newParent.addChildElement(newChild2);
            } else {
                newParent.addChildElement(newChild2);
                newParent.addChildElement(newChild1);
            }
        } else {
            newParent = frame.getMainWindow().buildXMLforComposite(direction);
            newParent.addChildElement(newChild1);
            newParent.addChildElement(newChild2);
        }
        boolean success = parent.replaceChild(existingPane, newParent);
        if (!success) throw new IllegalStateException("Error in replacing panes");
        try {
            frame.getMainWindow().loadFromXML(xml);
            frame.saveCurrentLayoutFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object executeAddTab(String[] tokens) {
        String frameName = tokens[1];
        String viewName = tokens[2];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        Pane pane = view.getPane();
        Module module = NewWindowDialog.showDialog(frame, "Select new window", m_Document.getModuleList());
        if (module == null) return null;
        AbstractView newView = module.createInstance();
        newView.generateName(frame);
        if (newView == null) return null;
        if (!view.isFixedSizeView() && newView.isFixedSizeView()) {
            frame.ShowMessageBox("I'm sorry.  You can't add a fixed size window (like a button bar) to a window containing a variable sized one (like a text window)");
            return null;
        }
        if (view.isFixedSizeView() && !newView.isFixedSizeView()) {
            frame.ShowMessageBox("I'm sorry.  You can't add a variable sized window (like a text window) to a window containing a fixed sized one (like a button bar)");
            return null;
        }
        boolean storeContent = true;
        JavaElementXML xml = frame.getMainWindow().convertToXML(storeContent);
        JavaElementXML existingPane = pane.getElementXML();
        existingPane.addAttribute(Pane.kAttributeSingleView, "false");
        existingPane.addAttribute(Pane.kAttributeView, newView.getName());
        JavaElementXML newViewXML = newView.convertToXML(AbstractView.kTagView, false);
        existingPane.addChildElement(newViewXML);
        try {
            frame.getMainWindow().loadFromXML(xml);
            frame.saveCurrentLayoutFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object executeReplaceView(String[] tokens) {
        String frameName = tokens[1];
        String viewName = tokens[2];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        Pane pane = view.getPane();
        Module module = NewWindowDialog.showDialog(frame, "Select new window", m_Document.getModuleList());
        if (module == null) return null;
        AbstractView newView = module.createInstance();
        if (newView == null) return null;
        if (view.isFixedSizeView() && !newView.isFixedSizeView()) {
            frame.ShowMessageBox("I'm sorry.  You can't replace a fixed size window (like a button bar) with a variable sized one (like a text window)");
            return null;
        }
        if (!view.isFixedSizeView() && newView.isFixedSizeView()) {
            frame.ShowMessageBox("I'm sorry.  You can't replace a variable sized window (like a text window) with a fixed sized one (like a button bar)");
            return null;
        }
        boolean storeContent = true;
        JavaElementXML xml = frame.getMainWindow().convertToXML(storeContent);
        JavaElementXML existingPane = pane.getElementXML();
        JavaElementXML parentXML = existingPane.getParent();
        Pane newPane = new Pane("no parent");
        newPane.setHorizontalOrientation(pane.isHorizontalOrientation());
        newView.generateName(frame);
        newPane.addView(newView);
        JavaElementXML newPaneXML = newPane.convertToXML(Pane.kTagName, false);
        boolean success = parentXML.replaceChild(existingPane, newPaneXML);
        if (!success) throw new IllegalStateException("Error in replacing panes");
        try {
            frame.getMainWindow().loadFromXML(xml);
            frame.saveCurrentLayoutFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object executeMoveTabs(String[] tokens) {
        String frameName = tokens[1];
        String viewName = tokens[2];
        String direction = tokens[3];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        Pane pane = view.getPane();
        boolean storeContent = true;
        JavaElementXML xml = frame.getMainWindow().convertToXML(storeContent);
        JavaElementXML existingPane = pane.getElementXML();
        boolean top = (direction.equalsIgnoreCase("top"));
        existingPane.addAttribute(Pane.kAttributeTabAtTop, Boolean.toString(top));
        try {
            frame.getMainWindow().loadFromXML(xml);
            frame.saveCurrentLayoutFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object executeRemoveView(String[] tokens) {
        String frameName = tokens[1];
        String viewName = tokens[2];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        Pane pane = view.getPane();
        boolean storeContent = true;
        JavaElementXML xml = frame.getMainWindow().convertToXML(storeContent);
        JavaElementXML removePane = pane.getElementXML();
        if (!pane.isSingleView()) {
            removePane.removeChild(view.getElementXML());
            if (removePane.getNumberChildren() == 1) {
                removePane.addAttribute(Pane.kAttributeSingleView, "true");
            }
        } else {
            boolean removingFixedSize = view.isFixedSizeView();
            if (removingFixedSize) {
                JavaElementXML parent = removePane.getParent();
                if (parent.getNumberChildren() != 2) throw new IllegalStateException("The parent of a fixed size window should be a composite pair");
                JavaElementXML otherPane = parent.getChild(0);
                if (otherPane == removePane) otherPane = parent.getChild(1);
                JavaElementXML superParent = parent.getParent();
                superParent.replaceChild(parent, otherPane);
            } else {
                boolean done = false;
                while (!done) {
                    JavaElementXML parent = removePane.getParent();
                    if (parent == null) {
                        frame.ShowMessageBox("Can't remove the last window");
                        return null;
                    }
                    parent.removeChild(removePane);
                    if (parent.getTagName().equals("composite")) {
                        removePane = parent;
                    } else if (parent.getNumberChildren() > 0) {
                        done = true;
                    }
                    removePane = parent;
                }
            }
        }
        try {
            frame.getMainWindow().loadFromXML(xml);
            frame.saveCurrentLayoutFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object executeRenameView(String[] tokens) {
        String frameName = tokens[1];
        String viewName = tokens[2];
        MainFrame frame = m_Document.getFrameByName(frameName);
        AbstractView view = frame.getView(viewName);
        Pane pane = view.getPane();
        boolean done = false;
        while (!done) {
            String name = frame.ShowInputDialog("Rename window", "Enter the new name:", view.getName());
            if (name == null || name.length() == 0 || name.equals(view.getName())) return null;
            boolean valid = true;
            for (int i = 0; i < name.length(); i++) {
                char ch = name.charAt(i);
                if (!((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || (ch == '_' || ch == '-' || ch == '<' || ch == '>'))) valid = false;
            }
            if (!valid) {
                frame.ShowMessageBox("Invalid characters in name", "Window names are limited to letters, numbers and '_', '-', '<', '>'");
                continue;
            }
            if (frame.isViewNameInUse(name)) {
                frame.ShowMessageBox("Name in use", "The window name has to be unique and " + name + " is already in use.");
                continue;
            }
            view.changeName(name);
            pane.updateTabs();
            done = true;
        }
        frame.saveCurrentLayoutFile();
        return null;
    }

    /**
     * We allow the variables [thisframe] and [thisview] in the command. Here we
     * bind those variables
     */
    public String replaceVariables(MainFrame frame, AbstractView view, String command) {
        StringBuffer newCommand = new StringBuffer();
        String[] tokens = command.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == "[thisframe]") tokens[i] = frame.getName(); else if (tokens[i] == "[thisview]") tokens[i] = view.getName();
            newCommand.append(tokens[i]);
            if (i != tokens.length - 1) newCommand.append(" ");
        }
        return newCommand.toString();
    }
}
