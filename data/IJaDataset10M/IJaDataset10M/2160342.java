package xslt;

import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JToolBar;
import javax.swing.JPopupMenu;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Container;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/** * GUI panel for list of XSL files * * @author Robert McKinnon - robmckinnon@users.sourceforge.net * @author Eric Le Lay - kerik-sf@users.sourceforge.net */
public class StylesheetPanel extends BufferOrFileSelector implements ListSelectionListener {

    private static final String LAST_STYLESHEETS = "xslt.last-stylesheet";

    private View view;

    private XSLTProcessor processor;

    private DefaultListModel stylesheetsListModel;

    private JList stylesheetsList;

    private XsltAction addStylesheetAction = new AddStylesheetAction();

    private XsltAction removeStylesheetAction = new RemoveStylesheetAction();

    private XsltAction upAction = new MoveStylesheetUpAction();

    private XsltAction downAction = new MoveStylesheetDownAction();

    private XsltAction openFileAction = new OpenFileAction();

    /**   * Constructor for the StylesheetPanel object.   */
    public StylesheetPanel(View view, XSLTProcessor processor) {
        super("xslt.stylesheets");
        this.view = view;
        this.processor = processor;
        this.stylesheetsListModel = initStylesheetListModel();
        this.stylesheetsList = initStylesheetList();
        JScrollPane editorComponent = new JScrollPane(stylesheetsList);
        JToolBar toolBar = initToolBar();
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(editorComponent, BorderLayout.CENTER);
        container.add(toolBar, BorderLayout.EAST);
        setSourceField(container);
    }

    /**   * Returns the selected stylesheet file name, or if none are selected returns null.   */
    public String getSelectedStylesheet() {
        int selectedIndex = stylesheetsList.getSelectedIndex();
        if (selectedIndex == -1) {
            return null;
        } else {
            return getStylesheet(selectedIndex);
        }
    }

    public String getStylesheet(int index) {
        return (String) stylesheetsListModel.get(index);
    }

    public Object[] getStylesheets() {
        return stylesheetsListModel.toArray();
    }

    public void setSelected(Point location) {
        int index = this.stylesheetsList.locationToIndex(location);
        if (index != -1) {
            stylesheetsList.setSelectedIndex(index);
        }
    }

    public void setStylesheets(String[] stylesheets) {
        stylesheetsListModel.clear();
        for (int i = 0; i < stylesheets.length; i++) {
            stylesheetsListModel.add(i, stylesheets[i]);
        }
        storeStylesheets();
    }

    public boolean stylesheetsExist() {
        return !isFileSelected() || stylesheetsListModel.size() > 0;
    }

    public int getStylesheetCount() {
        return stylesheetsListModel.size();
    }

    protected void setFileSelectionEnabled(boolean enabled) {
        if (!enabled) {
            addStylesheetAction.setEnabled(false);
            removeStylesheetAction.setEnabled(false);
            stylesheetsList.clearSelection();
            stylesheetsList.setEnabled(false);
        } else {
            addStylesheetAction.setEnabled(true);
            stylesheetsList.setEnabled(true);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        boolean selectionExists = stylesheetsList.getSelectedIndex() != -1;
        removeStylesheetAction.setEnabled(selectionExists);
        upAction.setEnabled(selectionExists && (stylesheetsListModel.getSize() > 1) && (stylesheetsList.getSelectedIndex() != 0));
        downAction.setEnabled(selectionExists && (stylesheetsListModel.getSize() > 1) && (stylesheetsList.getSelectedIndex() < stylesheetsListModel.getSize() - 1));
    }

    private DefaultListModel initStylesheetListModel() {
        DefaultListModel stylesheetsListModel = new DefaultListModel();
        List values = PropertyUtil.getEnumeratedProperty(LAST_STYLESHEETS);
        Iterator it = values.iterator();
        while (it.hasNext()) {
            stylesheetsListModel.addElement(it.next());
        }
        return stylesheetsListModel;
    }

    private JList initStylesheetList() {
        JList list = new JList(stylesheetsListModel);
        list.setName("stylesheets");
        list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.getSelectionModel().addListSelectionListener(this);
        XsltAction[] actions = new XsltAction[] { openFileAction, null, upAction, downAction, null, addStylesheetAction, removeStylesheetAction };
        list.setComponentPopupMenu(XsltAction.initMenu(actions));
        list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ENTER"), "open-file");
        list.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "open-file");
        list.getActionMap().put("open-file", openFileAction);
        return list;
    }

    private JToolBar initToolBar() {
        this.upAction.setEnabled(false);
        this.downAction.setEnabled(false);
        this.removeStylesheetAction.setEnabled(stylesheetsExist());
        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.setFloatable(false);
        toolBar.add(addStylesheetAction.getButton());
        toolBar.add(removeStylesheetAction.getButton());
        toolBar.addSeparator();
        toolBar.add(upAction.getButton());
        toolBar.add(downAction.getButton());
        return toolBar;
    }

    private void storeStylesheets() {
        List stylesheetsList = Arrays.asList(stylesheetsListModel.toArray());
        PropertyUtil.setEnumeratedProperty(LAST_STYLESHEETS, stylesheetsList);
    }

    private class AddStylesheetAction extends XsltAction {

        AddStylesheetAction() {
            super("xslt.stylesheets.add");
        }

        public void actionPerformed(ActionEvent e) {
            String path = null;
            if (stylesheetsListModel.size() > 0) {
                path = MiscUtilities.getParentOfPath(getStylesheet(0));
            }
            String[] selections;
            if (getTopLevelAncestor() != view && getTopLevelAncestor() instanceof JFrame) {
                selections = GUIUtilities.showVFSFileDialog((JFrame) getTopLevelAncestor(), view, path, JFileChooser.OPEN_DIALOG, false);
            } else {
                selections = GUIUtilities.showVFSFileDialog(view, path, JFileChooser.OPEN_DIALOG, false);
            }
            if (selections != null) {
                stylesheetsListModel.addElement(selections[0]);
                if ((stylesheetsList.getSelectedIndex() != -1) && (stylesheetsListModel.getSize() > 1)) {
                    downAction.setEnabled(true);
                }
                storeStylesheets();
            }
            Container topLevelAncestor = StylesheetPanel.this.getTopLevelAncestor();
            if (topLevelAncestor instanceof JFrame) {
                ((JFrame) topLevelAncestor).toFront();
            }
        }
    }

    private class RemoveStylesheetAction extends XsltAction {

        RemoveStylesheetAction() {
            super("xslt.stylesheets.remove");
        }

        public void actionPerformed(ActionEvent e) {
            int index = stylesheetsList.getSelectedIndex();
            if (index < 0) return;
            stylesheetsListModel.remove(index);
            if (stylesheetsListModel.getSize() > 0) {
                stylesheetsList.setSelectedIndex(0);
            } else {
                removeStylesheetAction.setEnabled(false);
            }
            storeStylesheets();
        }
    }

    private abstract class MoveStylesheetAction extends XsltAction {

        private int increment;

        MoveStylesheetAction(String actionName, int increment) {
            super(actionName);
            this.increment = increment;
        }

        public void actionPerformed(ActionEvent e) {
            int selectedIndex = stylesheetsList.getSelectedIndex();
            Object selected = stylesheetsListModel.get(selectedIndex);
            stylesheetsListModel.remove(selectedIndex);
            stylesheetsListModel.insertElementAt(selected, selectedIndex + increment);
            stylesheetsList.setSelectedIndex(selectedIndex + increment);
            storeStylesheets();
        }
    }

    private void openFile() {
        String file = getSelectedStylesheet();
        if (file != null) {
            jEdit.openFile(view, file);
        }
    }

    private class MoveStylesheetUpAction extends MoveStylesheetAction {

        MoveStylesheetUpAction() {
            super("xslt.stylesheets.up", -1);
        }
    }

    private class MoveStylesheetDownAction extends MoveStylesheetAction {

        MoveStylesheetDownAction() {
            super("xslt.stylesheets.down", 1);
        }
    }

    private class OpenFileAction extends XsltAction {

        OpenFileAction() {
            super("xslt.file.open");
        }

        public void actionPerformed(ActionEvent e) {
            openFile();
        }
    }
}
