package com.anecdote.ideaplugins.changesbar;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vcs.changes.*;
import com.intellij.openapi.vcs.changes.ui.ChangesBrowserChangeListNode;
import com.intellij.openapi.vcs.changes.ui.ChangesListView;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.BalloonImpl;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.popup.BalloonPopupBuilderImpl;
import com.intellij.util.ui.UIUtil;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.StringWriter;
import java.util.*;
import java.util.List;

class ChangesBar extends JPanel implements EventListener {

    public static final String VERSION = "1.7.8";

    private static final Icon ARROW_ICON = IconLoader.getIcon("/general/comboArrow.png");

    private final ChangeListSelectionButton _defaultListSelectionButton = new ChangeListSelectionButton();

    private ChangeListManager _changeListManager;

    private ChangeList _defaultChangeList;

    private static final Icon CHANGES_ICON = IconLoader.getIcon("/general/toolWindowChanges.png");

    static final Icon WARNING_ICON = IconLoader.getIcon("/com/anecdote/ideaplugins/changesbar/warning.gif");

    private final AnAction _newChangelistAction = new NewChangeListAction(ChangesBar.this);

    private final JButton _warningButton = new JButton(WARNING_ICON);

    private boolean _showWarning;

    private Runnable _queuedWarning;

    private Balloon _popup;

    private ChangeList _createdChangeList;

    private final ChangesBarProjectComponent _projectComponent;

    private Runnable _warningHider = new Runnable() {

        public void run() {
            _warningButton.setVisible(false);
            if (_popup != null) {
                _popup.dispose();
                _popup = null;
            }
        }
    };

    private ChangeListAdapter _changeListListener;

    ChangesBar(ChangesBarProjectComponent projectComponent) {
        super(new BorderLayout());
        _projectComponent = projectComponent;
        add(_warningButton, BorderLayout.WEST);
        _warningButton.setVisible(false);
        _warningButton.setFocusable(false);
        _warningButton.setMargin(new Insets(1, 1, 0, 1));
        _warningButton.putClientProperty("Quaqua.Button.style", "placard");
        _warningButton.setToolTipText("A Changelist Collision has been detected.\nClick to Clear Collision Warning");
        _warningButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showChangesToolwindow(_projectComponent.getProject());
            }
        });
        add(_defaultListSelectionButton, BorderLayout.CENTER);
        updateButtonText();
    }

    private void showChangesToolwindow(Project project) {
        if (!project.isDisposed()) {
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ChangesBarProjectComponent.CHANGES_TOOLWINDOW_NAME);
            ContentManager contentManager = toolWindow.getContentManager();
            contentManager.setSelectedContent(contentManager.getContent(0));
            ChangesListView view = _projectComponent.getChangesListView();
            if (view != null) {
                DefaultTreeModel treeModel = view.getModel();
                Object root = treeModel.getRoot();
                ChangesBrowserChangeListNode nodeToSelect = null;
                if (_createdChangeList != null) {
                    int childCount = treeModel.getChildCount(root);
                    for (int i = 0; i < childCount; i++) {
                        Object child = treeModel.getChild(root, i);
                        if (child instanceof ChangesBrowserChangeListNode) {
                            ChangesBrowserChangeListNode node = (ChangesBrowserChangeListNode) child;
                            if (node.getUserObject().equals(_createdChangeList)) {
                                nodeToSelect = node;
                            }
                        }
                    }
                }
                if (nodeToSelect != null) {
                    TreePath path = new TreePath(new Object[] { root, nodeToSelect });
                    view.setSelectionPath(path);
                    view.expandPath(path);
                    view.scrollPathToVisible(path);
                    view.requestFocusInWindow();
                }
            }
            toolWindow.show(null);
            setWarningVisible(null);
            SwingUtilities.getWindowAncestor(ChangesBar.this).validate();
            SwingUtilities.getWindowAncestor(ChangesBar.this).invalidate();
            SwingUtilities.getWindowAncestor(ChangesBar.this).repaint();
        }
    }

    void setChangeListManager(ChangeListManager changeListManager) {
        if (_changeListManager == changeListManager) {
            return;
        }
        if (_changeListManager != null) {
            _changeListManager.removeChangeListListener(getChangeListListener());
        }
        LocalChangeList oldChangeList = getDefaultChangeList();
        _changeListManager = changeListManager;
        getChangeListListener().defaultListChanged(oldChangeList, getDefaultChangeList());
        if (_changeListManager != null) {
            _changeListManager.addChangeListListener(getChangeListListener());
        }
    }

    private LocalChangeList getDefaultChangeList() {
        return _changeListManager != null ? _changeListManager.getDefaultChangeList() : null;
    }

    private void updateButtonText(final ChangeList list) {
        Runnable runnable = new Runnable() {

            public void run() {
                if (list != null) {
                    _defaultListSelectionButton.setText(list.getName() + " (" + list.getChanges().size() + ')');
                } else {
                    _defaultListSelectionButton.setText("[No List]");
                }
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    private void updateButtonText() {
        updateButtonText(getDefaultChangeList());
    }

    private DefaultActionGroup createSelectionActionGroup() {
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        if (_changeListManager != null) {
            final List<LocalChangeList> changeLists = _changeListManager.getChangeLists();
            for (final LocalChangeList changeList : changeLists) {
                if (!ChangesBarProjectComponent.isConflictChangeList(changeList)) {
                    actionGroup.add(new ChangeListSelectionAction(changeList));
                }
            }
        }
        actionGroup.add(_newChangelistAction);
        return actionGroup;
    }

    void setWarningVisible(final ChangeList createdChangeList) {
        _showWarning = createdChangeList != null;
        _createdChangeList = createdChangeList;
        if (_showWarning) {
            if (_queuedWarning == null) {
                _queuedWarning = new Runnable() {

                    public void run() {
                        if (_showWarning) {
                            _warningButton.setVisible(true);
                            validate();
                            if (ChangesBarApplicationComponent.isShowPopup() && _popup == null) {
                                ConflictPopupPanel popupPanel = new ConflictPopupPanel("Changelist Collision Detected!", WARNING_ICON, "Click here to clear this alert and open the Changes toolwindow.", new AbstractAction() {

                                    public void actionPerformed(ActionEvent e) {
                                        showChangesToolwindow(_projectComponent.getProject());
                                    }
                                }) {

                                    protected void hideContainer() {
                                        if (_popup != null) {
                                            _popup.dispose();
                                            _popup = null;
                                        }
                                    }
                                };
                                BalloonBuilder balloonBuilder = new BalloonPopupBuilderImpl(popupPanel).setFillColor(MessageType.WARNING.getPopupBackground()).setHideOnClickOutside(false).setHideOnKeyOutside(false).setPreferredPosition(Balloon.Position.above);
                                _popup = balloonBuilder.createBalloon();
                                _popup.show(RelativePoint.getCenterOf(_warningButton), Balloon.Position.above);
                            }
                        }
                        _queuedWarning = null;
                    }
                };
            }
            SwingUtilities.invokeLater(_queuedWarning);
        } else {
            if (SwingUtilities.isEventDispatchThread()) {
                _warningHider.run();
            } else {
                SwingUtilities.invokeLater(_warningHider);
            }
        }
    }

    Project getProject() {
        return _projectComponent.getProject();
    }

    public ChangeListListener getChangeListListener() {
        if (_changeListListener == null) {
            _changeListListener = new ChangeListAdapter();
        }
        return _changeListListener;
    }

    private class ChangeListSelectionButton extends JButton {

        private boolean _myForcePressed = false;

        private static final int MAX_ROW_COUNT = 30;

        private static final int PREF_BUTTON_HEIGHT = 21;

        private ChangeListSelectionButton() {
            super(CHANGES_ICON);
            setModel(new MyButtonModel());
            setHorizontalAlignment(SwingConstants.LEFT);
            setFocusable(false);
            final Insets margins = getMargin();
            setMargin(new Insets(margins.top, 2, margins.bottom, 2));
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (!_myForcePressed) {
                        showPopup();
                    }
                }
            });
            putClientProperty("Quaqua.Button.style", "placard");
        }

        private void showPopup() {
            final DefaultActionGroup group = createSelectionActionGroup();
            _myForcePressed = true;
            repaint();
            final Runnable onDispose = new Runnable() {

                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            _myForcePressed = false;
                        }
                    });
                    repaint();
                }
            };
            final ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup("Changelists", group, DataManager.getInstance().getDataContext(), JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false, onDispose, MAX_ROW_COUNT);
            final Dimension preferredSize = popup.getContent().getPreferredSize();
            preferredSize.width = Math.max(preferredSize.width, getSize().width);
            popup.getContent().setPreferredSize(preferredSize);
            final Point point = getBounds().getLocation();
            point.y -= preferredSize.height;
            popup.show(new RelativePoint(ChangesBar.this, point));
        }

        @Override
        public void setText(String text) {
            super.setText(text);
            updateButtonSize();
        }

        @Override
        public void updateUI() {
            super.updateUI();
            if (UIUtil.isMotifLookAndFeel()) {
                setBorder(BorderFactory.createEtchedBorder());
            } else {
                setBorder(UIUtil.getButtonBorder());
            }
        }

        private class MyButtonModel extends DefaultButtonModel {

            @Override
            public boolean isPressed() {
                return _myForcePressed || super.isPressed();
            }

            @Override
            public boolean isArmed() {
                return _myForcePressed || super.isArmed();
            }
        }

        @Override
        public final void paint(Graphics g) {
            super.paint(g);
            final Dimension size = getSize();
            final String text = getText();
            final boolean isEmpty = getIcon() == null && (text == null || text.trim().length() == 0);
            final int x = isEmpty ? (size.width - ARROW_ICON.getIconWidth()) >> 1 : size.width - ARROW_ICON.getIconWidth() - 2;
            ARROW_ICON.paintIcon(null, g, x, (size.height - ARROW_ICON.getIconHeight()) >> 1);
        }

        private void updateButtonSize() {
            int width;
            final String text = getText();
            if ((text == null || text.trim().length() == 0) && getIcon() == null) {
                width = ARROW_ICON.getIconWidth() + 10;
            } else {
                width = getUI().getPreferredSize(ChangeListSelectionButton.this).width + ARROW_ICON.getIconWidth() + 2;
            }
            setPreferredSize(new Dimension(width, PREF_BUTTON_HEIGHT));
        }
    }

    private class ChangeListSelectionAction extends ToggleAction {

        private final LocalChangeList _changeList;

        private ChangeListSelectionAction(LocalChangeList changeList) {
            super(changeList.getName() + " (" + changeList.getChanges().size() + ')', "", CHANGES_ICON);
            _changeList = changeList;
        }

        @Override
        public boolean isSelected(AnActionEvent e) {
            return _changeList.equals(_defaultChangeList);
        }

        @Override
        public void setSelected(AnActionEvent e, boolean state) {
            if (state) {
                _changeListManager.setDefaultChangeList(_changeList);
            }
        }
    }

    @SuppressWarnings({ "SSBasedInspection" })
    private static void dumpComponentHierarchy(int level, Component component) {
        final StringWriter writer = new StringWriter();
        for (int i = 0; i < level - 1; i++) {
            writer.append("| ");
        }
        if (level > 0) {
            writer.append("+-");
        }
        writer.append(component.toString());
        System.out.println(writer.toString());
        if (component instanceof Container) {
            for (final Component child : ((Container) component).getComponents()) {
                dumpComponentHierarchy(level + 1, child);
            }
        }
    }

    public static void main(String args[]) {
        JFrame jframe = new JFrame();
        jframe.getContentPane().setLayout(new BorderLayout());
        JPanel jpanel = new JPanel(new BorderLayout());
        jframe.getContentPane().add(jpanel, "Center");
        JTree jtree = new JTree();
        jpanel.add(jtree);
        final Ref ref = new Ref();
        jtree.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent mouseevent) {
                if (ref.get() != null && ((BalloonImpl) ref.get()).isVisible()) {
                    ((BalloonImpl) ref.get()).dispose();
                } else {
                    JEditorPane jeditorpane = new JEditorPane();
                    jeditorpane.setEditorKit(new HTMLEditorKit());
                    jeditorpane.setText(UIUtil.toHtml("<html><body><center>Really cool balloon<br>Really fucking <a hre" + "f=\\\"http://jetbrains.com\\\">big</a></center></body></html"));
                    Dimension dimension = (new JLabel(jeditorpane.getText())).getPreferredSize();
                    jeditorpane.setEditable(false);
                    jeditorpane.setOpaque(false);
                    jeditorpane.setBorder(null);
                    jeditorpane.setPreferredSize(dimension);
                    ref.set(new BalloonImpl(jeditorpane, Color.black, MessageType.INFO.getPopupBackground(), false, false, false));
                    ((BalloonImpl) ref.get()).setShowPointer(true);
                    if (mouseevent.isControlDown()) {
                        ((BalloonImpl) ref.get()).show(new RelativePoint(mouseevent), Balloon.Position.above);
                    } else if (mouseevent.isAltDown()) {
                        ((BalloonImpl) ref.get()).show(new RelativePoint(mouseevent), Balloon.Position.below);
                    } else if (mouseevent.isMetaDown()) {
                        ((BalloonImpl) ref.get()).show(new RelativePoint(mouseevent), Balloon.Position.atLeft);
                    } else {
                        ((BalloonImpl) ref.get()).show(new RelativePoint(mouseevent), Balloon.Position.atRight);
                    }
                }
            }
        });
        jframe.setBounds(300, 300, 300, 300);
        jframe.setVisible(true);
    }

    private class ChangeListAdapter implements ChangeListListener {

        public void changeListAdded(ChangeList list) {
        }

        public void changesRemoved(Collection<Change> changes, ChangeList fromList) {
        }

        public void changeListRemoved(ChangeList list) {
            if (_defaultChangeList.equals(list)) {
                getChangeListListener().defaultListChanged(_defaultChangeList, null);
            }
        }

        public void changeListChanged(ChangeList list) {
            if (list.equals(_defaultChangeList)) {
                updateButtonText(list);
            }
        }

        public void changeListRenamed(ChangeList list, String oldName) {
            if (list.equals(_defaultChangeList)) {
                updateButtonText(list);
            }
        }

        public void changeListCommentChanged(ChangeList list, String oldComment) {
        }

        public void changesMoved(Collection<Change> changes, ChangeList fromList, ChangeList toList) {
            updateButtonText();
        }

        public void defaultListChanged(ChangeList oldDefaultList, ChangeList newDefaultList) {
            _defaultChangeList = newDefaultList;
            updateButtonText();
        }

        public void unchangedFileStatusChanged() {
            updateButtonText();
        }

        public void changeListUpdateDone() {
            updateButtonText();
        }
    }
}
