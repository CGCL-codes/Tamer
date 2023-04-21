package todopad.ui;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import todopad.model.*;

public class Pane extends JLayeredPane {

    private JPanel _contentPane;

    private JSplitPane _splitPane;

    private JPanel _listPane;

    private FindBar _findBar;

    private MessageBar _messageBar;

    private ExplorerPane _explorerPane;

    private TodoPad _todoPad;

    private TodoPadActionService _todoPadActionService;

    private ExplorerPaneActionService _explorerPaneActionService;

    private final todopad.ui.Frame _frame;

    public Pane(todopad.ui.Frame aFrame) {
        super();
        _frame = aFrame;
        initContentPane();
        this._findBar = new FindBar(this);
        this._messageBar = new MessageBar(this);
        this.add(_findBar, JLayeredPane.PALETTE_LAYER);
        this.add(_messageBar, JLayeredPane.PALETTE_LAYER);
    }

    private void initContentPane() {
        _contentPane = new JPanel(new BorderLayout());
        this._listPane = new JPanel(new BorderLayout());
        this._explorerPane = new ExplorerPane(Root.instant(), new ExplorerPaneActionService() {

            public void focusPad() {
                if (_todoPad != null) {
                    _todoPad.requestFocusForTodoPad();
                    _todoPad.selectLast();
                }
            }
        });
        _todoPadActionService = new TodoPadActionServiceImpl(_explorerPane.getTree(), _frame);
        this._todoPad = new TodoPad(_todoPadActionService);
        this._splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this._listPane.add(_explorerPane);
        _splitPane.setLeftComponent(_listPane);
        _splitPane.setRightComponent(_todoPad);
        _contentPane.add(_splitPane, BorderLayout.CENTER);
        _explorerPane.getTree().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent aE) {
                TreePath lNewLeadSelectionPath = aE.getNewLeadSelectionPath();
                if (lNewLeadSelectionPath != null) {
                    Object lObject = lNewLeadSelectionPath.getLastPathComponent();
                    if (lObject instanceof TodoListNode) {
                        TodoListNode lListNode = (TodoListNode) lObject;
                        _todoPad.open(lListNode.getTodoList());
                    }
                }
            }
        });
        this.add(_contentPane);
    }

    public void setBounds(int aX, int aY, int aWidth, int aHeight) {
        super.setBounds(aX, aY, aWidth, aHeight);
        _contentPane.setSize(this.getSize());
        _findBar.paneResized();
        _messageBar.paneResized();
    }

    public TodoPad getTodoPad() {
        return _todoPad;
    }

    public ExplorerPane getExplorerPane() {
        return _explorerPane;
    }

    public JSplitPane getSplitPane() {
        return _splitPane;
    }

    public void setSplitPane(JSplitPane aSplitPane) {
        _splitPane = aSplitPane;
    }

    public void gotoToday() {
        _explorerPane.selectToday();
    }

    public void gotoPerviousItem() {
        if (_todoPadActionService != null) {
            _todoPadActionService.gotoPerviousNode();
        }
    }

    public void gotoNextItem() {
        if (_todoPadActionService != null) {
            _todoPadActionService.gotoNextNode();
        }
    }

    public void showFindBar() {
        this._findBar.showFindBar();
    }

    public void showMessageBar(String message) {
        this._messageBar.showMessageBar(message);
    }

    public void openItem() {
        OpenPadDialog lOpenItemDialog = new OpenPadDialog((Frame) SwingUtilities.getRoot(this), _explorerPane);
        lOpenItemDialog.setVisible(true);
        _todoPad.requestFocusForTodoPad();
    }

    public void newTask() {
        NewTaskDialog lNewTaskDialog = new NewTaskDialog((Frame) SwingUtilities.getRoot(this), _todoPad);
        lNewTaskDialog.setVisible(true);
        _todoPad.requestFocusForTodoPad();
    }
}

class TodoPadActionServiceImpl implements TodoPadActionService {

    private final JTree _tree;

    private final todopad.ui.Frame _frame;

    public TodoPadActionServiceImpl(JTree aTree, todopad.ui.Frame aFrame) {
        _tree = aTree;
        _frame = aFrame;
    }

    public void gotoPerviousNode() {
        gotoNode(false);
    }

    public void gotoNextNode() {
        gotoNode(true);
    }

    private void gotoNode(boolean isNext) {
        TreePath lSelectionPath = _tree.getSelectionPath();
        if (lSelectionPath != null) {
            Object lLastPathComponent = lSelectionPath.getLastPathComponent();
            if (lLastPathComponent instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) lLastPathComponent;
                DefaultMutableTreeNode lNewSelectedNode = (isNext ? currentNode.getNextNode() : currentNode.getPreviousNode());
                while (!(lNewSelectedNode instanceof TodoListNode) && lNewSelectedNode != null) {
                    lNewSelectedNode = (isNext ? lNewSelectedNode.getNextNode() : lNewSelectedNode.getPreviousNode());
                }
                if (lNewSelectedNode != null) {
                    ArrayList<TreeNode> newPath = new ArrayList<TreeNode>();
                    TreeNode node = lNewSelectedNode;
                    while (node != null) {
                        newPath.add(0, node);
                        node = node.getParent();
                    }
                    Object[] lPath = newPath.toArray();
                    TreePath newTreePath = new TreePath(lPath);
                    _tree.setSelectionPath(newTreePath);
                }
            }
        }
    }

    public void titleChanged(File aFile) {
        _frame.titleChanged(aFile);
    }
}
