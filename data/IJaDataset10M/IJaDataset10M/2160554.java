package astcentric.editor.swing.plain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import astcentric.editor.common.control.tree.ASTView;
import astcentric.editor.common.view.tree.TreeFacade;
import astcentric.editor.plain.StandardEditor;
import astcentric.editor.swing.MenuCreator;
import astcentric.structure.basic.AST;
import astcentric.structure.basic.ASTWriter;
import astcentric.structure.basic.Node;

public class TabbedEditorPane extends JTabbedPane {

    private static final class TabMapValue {

        private final StandardEditor _editor;

        private final JScrollPane _pane;

        TabMapValue(StandardEditor editor, JScrollPane pane) {
            _editor = editor;
            _pane = pane;
        }

        StandardEditor getEditor() {
            return _editor;
        }

        JScrollPane getPane() {
            return _pane;
        }
    }

    private static final MenuCreator MENU_CREATOR = new MenuCreator();

    private final List<JMenu> _editMenus = new ArrayList<JMenu>();

    private final List<StandardEditor> _views = new ArrayList<StandardEditor>();

    private final Map<AST, TabMapValue> _tabMap = new LinkedHashMap<AST, TabMapValue>();

    private final JMenuBar _menuBar;

    private final int _indexOfEditMenu;

    public TabbedEditorPane(JMenuBar menuBar, int indexOfEditMenu) {
        _menuBar = menuBar;
        _indexOfEditMenu = indexOfEditMenu;
        addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int selectedIndex = getSelectedIndex();
                if (selectedIndex >= 0) {
                    replaceEditMenu(_editMenus.get(selectedIndex));
                } else {
                    JMenu menu = new JMenu("Edit");
                    menu.setEnabled(false);
                    replaceEditMenu(menu);
                }
            }
        });
    }

    public void openTabFor(StandardEditor editor, Node nodeToBeFocused) {
        ASTView view = editor.getView();
        AST ast = view.getAST();
        TabMapValue tabMapValue = _tabMap.get(ast);
        if (tabMapValue == null) {
            _views.add(editor);
            JMenu editMenu = MENU_CREATOR.create(editor.getMenu());
            _editMenus.add(editMenu);
            replaceEditMenu(editMenu);
            JComponent treePanel = new TreePanel(view.getView());
            JScrollPane scrollPane = new JScrollPane(treePanel);
            scrollPane.setActionMap(new ActionMap());
            String title = ast.getInfo().getName();
            if (ast.isSealed()) {
                title = title + " (read only)";
                editMenu.setEnabled(false);
                editMenu.removeAll();
            }
            add(scrollPane, title);
            TreeFacade tree = view.getView().getTree();
            ast.addASTListener(tree.getASTListener());
            tabMapValue = new TabMapValue(editor, scrollPane);
            _tabMap.put(ast, tabMapValue);
        }
        JScrollPane scrollPane = tabMapValue.getPane();
        scrollPane.getViewport().getView().requestFocus();
        setSelectedComponent(scrollPane);
        if (nodeToBeFocused == null) {
            nodeToBeFocused = ast.getRoot();
        }
        TreeFacade tree = tabMapValue.getEditor().getView().getView().getTree();
        tree.setFocusOnCorrespondingNode(nodeToBeFocused, false);
    }

    private void replaceEditMenu(JMenu editMenu) {
        _menuBar.remove(_indexOfEditMenu);
        _menuBar.add(editMenu, _indexOfEditMenu);
        editMenu.invalidate();
        _menuBar.getParent().validate();
        _menuBar.repaint();
    }

    public void saveAll(ASTWriter writer) {
        for (StandardEditor view : _views) {
            view.saveAST(writer);
        }
    }

    public void saveAST(ASTWriter writer) {
        _views.get(getSelectedIndex()).saveAST(writer);
    }

    public boolean close(ASTWriter writer) {
        int selectedIndex = getSelectedIndex();
        StandardEditor editor = _views.get(selectedIndex);
        if (editor.close(writer)) {
            ASTView view = editor.getView();
            view.getAST().removeASTListener(view.getView().getTree().getASTListener());
            _views.remove(selectedIndex);
            _editMenus.remove(selectedIndex);
            remove(selectedIndex);
            fireStateChanged();
            return true;
        }
        return false;
    }

    public boolean closeAll(ASTWriter writer) {
        int i = 0;
        while (i < getTabCount()) {
            setSelectedIndex(i);
            if (close(writer) == false) {
                ++i;
            }
        }
        return i == 0;
    }
}
