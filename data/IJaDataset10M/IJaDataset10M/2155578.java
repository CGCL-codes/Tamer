package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.set.hash.TIntHashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewMessageData;
import org.xblackcat.rojac.gui.*;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.dialog.ignoreunread.IgnoreTopicsDialog;
import org.xblackcat.rojac.gui.dialog.ignoreunread.TopicIgnoringSelection;
import org.xblackcat.rojac.gui.popup.TopicIgnoreSetter;
import org.xblackcat.rojac.gui.popup.UserIgnoreFlagSetter;
import org.xblackcat.rojac.gui.theme.HintIcon;
import org.xblackcat.rojac.gui.view.AnItemView;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ThreadState;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.message.MessageDataHolder;
import org.xblackcat.rojac.gui.view.message.MessagePane;
import org.xblackcat.rojac.gui.view.model.*;
import org.xblackcat.rojac.gui.view.model.Thread;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IgnoreUpdatedPacket;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xBlackCat
 */
public class TreeTableThreadView extends AnItemView {

    private static final Log log = LogFactory.getLog(TreeTableThreadView.class);

    protected final SortedThreadsModel model = new SortedThreadsModel();

    private final IModelControl modelControl;

    private JToolBar toolbar;

    private final JXTreeTable threads;

    private final MessagePane messagePane;

    protected int rootItemId;

    protected ThreadState state;

    protected MessageData selectedItem;

    protected boolean resortingModel = false;

    private TableThreadViewLayout layout;

    private final JSplitPane splitPane;

    public TreeTableThreadView(ViewId id, IAppControl appControl, ModelControl modelControl) {
        super(id, appControl);
        this.modelControl = modelControl.get();
        model.addTreeModelListener(new DataIntegrityMonitor());
        Runnable onScrollEnd = new Runnable() {

            @Override
            public void run() {
                if (Property.VIEW_THREAD_SET_READ_ON_SCROLL.get()) {
                    if (!selectedItem.isRead()) {
                        MessageUtils.markMessageRead(getId(), selectedItem, 0);
                        selectedItem = selectedItem.setRead(true);
                    }
                }
                if (selectedItem != null) {
                    selectNextPost(selectedItem.getMessageId());
                }
            }
        };
        messagePane = new MessagePane(appControl, onScrollEnd);
        threads = getThreadsContainer();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(threads), messagePane);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(.1);
        messagePane.setupTitleAsDivider(splitPane);
        add(splitPane, BorderLayout.CENTER);
        ShortCutUtils.mergeInputMaps(this, messagePane);
        InputMap map = threads.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ShortCutUtils.removeShortCuts(map.getParent());
        InputMap inputMap = ShortCutUtils.mergeInputMaps(getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT), map);
        threads.setInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);
        toolbar = createToolBar();
        if (toolbar != null) {
            add(toolbar, BorderLayout.NORTH);
        } else {
            IInfoChangeListener toolBarTracker = new IInfoChangeListener() {

                @Override
                public void infoChanged() {
                    toolbar = createToolBar();
                    if (toolbar == null) {
                        return;
                    }
                    removeInfoChangeListener(this);
                    if (layout != null) {
                        toolbar.setOrientation(layout.getToolbarOrientation());
                        add(toolbar, layout.getToolbarPosition());
                    } else {
                        add(toolbar, BorderLayout.WEST);
                    }
                }
            };
            addInfoChangeListener(toolBarTracker);
        }
    }

    protected JXTreeTable getThreadsContainer() {
        final JXTreeTable threads = new JXTreeTable();
        threads.setAutoCreateColumnsFromModel(false);
        threads.setTreeTableModel(model);
        threads.setEditable(false);
        threads.setShowsRootHandles(true);
        threads.setSortable(false);
        threads.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        threads.setRowSelectionAllowed(true);
        threads.setColumnSelectionAllowed(false);
        threads.setScrollsOnExpand(true);
        threads.setRootVisible(modelControl.isRootVisible());
        threads.setToggleClickCount(2);
        threads.setDefaultRenderer(APostProxy.class, new PostTableCellRenderer());
        threads.setTreeCellRenderer(new PostTreeCellRenderer(threads));
        threads.addTreeSelectionListener(new PostSelector());
        threads.addTreeExpansionListener(new ThreadExpander());
        threads.addMouseListener(new ItemListener());
        threads.getModel().addTableModelListener(new SelectionHolder());
        threads.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (Header h : Header.values()) {
            TableColumnExt column = new TableColumnExt(h.ordinal());
            if (h.getWidth() > 0) {
                column.setPreferredWidth(h.getWidth());
                column.setMaxWidth(h.getWidth() << 2);
            }
            column.setMinWidth(10);
            column.setIdentifier(h);
            column.setToolTipText(h.getTitle());
            threads.addColumn(column);
        }
        threads.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "parentOrCollapse");
        threads.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "nextOrExpand");
        threads.getActionMap().put("parentOrCollapse", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = threads.getSelectedRow();
                if (row == -1) {
                    return;
                }
                if (threads.isExpanded(row)) {
                    threads.collapseRow(row);
                    threads.scrollRowToVisible(row);
                } else if (row > 0) {
                    TreePath pathForRow = threads.getPathForRow(row);
                    if (pathForRow != null) {
                        TreePath parent = pathForRow.getParentPath();
                        if (parent != null) {
                            row = threads.getRowForPath(parent);
                            if (row >= 0) {
                                threads.setRowSelectionInterval(row, row);
                                threads.scrollRowToVisible(row);
                            }
                        }
                    }
                }
            }
        });
        threads.getActionMap().put("nextOrExpand", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = threads.getSelectedRow();
                if (row == -1) {
                    return;
                }
                if (!threads.isExpanded(row) && !model.isLeaf(threads.getPathForRow(row).getLastPathComponent())) {
                    threads.expandRow(row);
                    threads.scrollRowToVisible(row);
                } else if (row < threads.getRowCount() - 1) {
                    threads.scrollRowToVisible(row + 1);
                    threads.setRowSelectionInterval(row + 1, row + 1);
                }
            }
        });
        threads.setDragEnabled(true);
        threads.setTransferHandler(new TransferHandler() {

            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                if (c instanceof JXTreeTable) {
                    JXTreeTable threads = (JXTreeTable) c;
                    int[] rows = threads.getSelectedRows();
                    if (rows == null || rows.length == 0) {
                        return null;
                    }
                    Post[] posts = new Post[rows.length];
                    for (int i = 0; i < rows.length; i++) {
                        posts[i] = (Post) threads.getPathForRow(rows[i]).getLastPathComponent();
                    }
                    return new PostTransferable(posts);
                }
                return null;
            }
        });
        return threads;
    }

    protected void selectPost(Post post, boolean collapseChildren) {
        if (post != null) {
            TreePath path = model.getPathToRoot(post);
            if (model.isPathValid(path)) {
                TreePath parentPath = path.getParentPath();
                if (parentPath != null && threads.isCollapsed(parentPath)) {
                    threads.expandPath(parentPath);
                }
                scrollPathToVisible(path);
                if (collapseChildren) {
                    threads.collapsePath(path);
                }
                selectedItem = post.getMessageData();
                threads.getTreeSelectionModel().setSelectionPath(path);
                return;
            }
        }
        selectedItem = null;
        threads.clearSelection();
    }

    protected void scrollPathToVisible(TreePath path) {
        int row = threads.getRowForPath(path);
        if (row >= 0) {
            threads.setRowSelectionInterval(row, row);
            Rectangle bounds = threads.getCellRect(row, threads.convertColumnIndexToView(threads.getHierarchicalColumn()), true);
            threads.scrollRectToVisible(bounds);
        }
        threads.scrollPathToVisible(path);
    }

    protected Post getSelectedPost() {
        int selectedRow = threads.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        TreePath path = threads.getPathForRow(selectedRow);
        return path == null ? null : (Post) path.getLastPathComponent();
    }

    private JToolBar createToolBar() {
        ThreadToolbarActions[] actions = modelControl.getToolbar();
        if (actions == null) {
            return null;
        }
        JToolBar toolBar = new JToolBar();
        for (ThreadToolbarActions c : actions) {
            if (c != null) {
                toolBar.add(c.makeButton(this));
            } else {
                toolBar.addSeparator();
            }
        }
        return toolBar;
    }

    @Override
    public void loadItem(int itemId) {
        this.rootItemId = itemId;
        modelControl.fillModelByItemId(model, itemId);
    }

    @Override
    public ThreadState getObjectState() {
        assert RojacUtils.checkThread(true);
        int messageId = selectedItem == null ? 0 : selectedItem.getMessageId();
        return new ThreadState(messageId);
    }

    @Override
    public void setObjectState(IState state) {
        assert RojacUtils.checkThread(true);
        if (state == null) {
            return;
        }
        if (!(state instanceof ThreadState)) {
            RojacUtils.fireDebugException("Invalid state object " + state.toString() + " [" + state.getClass() + "]");
            return;
        }
        this.state = (ThreadState) state;
        applyState();
    }

    @Override
    public void makeVisible(int messageId) {
        setObjectState(new ThreadState(messageId));
    }

    @Override
    public boolean containsItem(int messageId) {
        return modelControl.allowSearch() && model.getRoot().getMessageById(messageId) != null;
    }

    @Override
    public String getTabTitle() {
        return modelControl.getTitle(model);
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return modelControl.getTitlePopup(model, appControl);
    }

    @Override
    public Icon getTabTitleIcon() {
        return modelControl.getTitleIcon(model);
    }

    protected Object getToolbarPlacement() {
        return toolbar == null ? 0 : ((BorderLayout) getLayout()).getConstraints(toolbar);
    }

    @Override
    public TableThreadViewLayout storeLayout() {
        TableColumnModel cm = threads.getColumnModel();
        TableThreadViewLayout.Column[] columns = new TableThreadViewLayout.Column[Header.values().length];
        int i = 0;
        for (Header h : Header.values()) {
            TableColumn c = cm.getColumn(h.ordinal());
            int width = c.getWidth();
            int columnIndex = cm.getColumnIndex(h);
            columns[i++] = new TableThreadViewLayout.Column(h, columnIndex, width);
        }
        return new TableThreadViewLayout(toolbar == null ? 0 : toolbar.getOrientation(), splitPane.getDividerLocation(), getToolbarPlacement(), columns);
    }

    @Override
    public void setupLayout(IViewLayout o) {
        if (o instanceof TableThreadViewLayout) {
            layout = (TableThreadViewLayout) o;
            if (toolbar != null) {
                remove(toolbar);
                toolbar.setOrientation(layout.getToolbarOrientation());
                add(toolbar, layout.getToolbarPosition());
            }
            splitPane.setDividerLocation(layout.getDividerLocation());
            TableColumnModel cm = threads.getColumnModel();
            for (TableThreadViewLayout.Column c : layout.getColumns()) {
                int idx = cm.getColumnIndex(c.getAnchor());
                cm.getColumn(idx).setWidth(c.getWidth());
                cm.getColumn(idx).setPreferredWidth(c.getWidth());
                cm.moveColumn(idx, c.getIndex());
            }
        }
    }

    protected final void selectItem(Post post, boolean showTopicHint) {
        selectPost(post, false);
        if (post != null && showTopicHint) {
            String hintStr = Message.Hint_TopicChangedTo.get(post.getMessageData().getSubject());
            JLabel hint = new JLabel(hintStr);
            hint.setToolTipText(hintStr);
            messagePane.getHintContainer().addHint(HintIcon.ThreadChanged, hint);
        }
    }

    public void selectNextPost(int postId) {
        Post post = model.getRoot().getMessageById(postId);
        if (post != null) {
            selectNextPost(post, true);
        }
    }

    private void applyState() {
        assert RojacUtils.checkThread(true);
        if (state == null) {
            return;
        }
        final int messageId = state.openedMessageId();
        if (!model.isInitialized()) {
            model.addTreeModelListener(new ForumLoadWaiter(messageId));
        } else {
            expandThread(messageId);
        }
    }

    private void selectNextPost(Post currentPost, boolean unread) {
        selectNextPost(currentPost, unread, new LinkedList<Post>(), false);
    }

    private void selectNextPost(Post currentPost, boolean unread, Collection<Post> toCollapse, boolean showHint) {
        Post next = currentPost;
        boolean skip;
        do {
            next = findNextPost(next, 0, unread, toCollapse);
            skip = PostUtils.isPostIgnoredByUser(next);
        } while (skip);
        if (next != null) {
            selectItem(next, showHint || next.getMessageData().getThreadRootId() != currentPost.getMessageData().getThreadRootId());
            if (!toCollapse.isEmpty()) {
                for (Post post : toCollapse) {
                    threads.collapsePath(model.getPathToRoot(post));
                }
                scrollPathToVisible(model.getPathToRoot(next));
            }
        }
    }

    private void selectPrevPost(Post currentPost, boolean unread) {
        Post prev = currentPost;
        boolean skip;
        do {
            prev = findPrevPost(prev, unread);
            skip = PostUtils.isPostIgnoredByUser(prev);
        } while (skip);
        if (prev != null) {
            selectItem(prev, prev.getTopicId() != currentPost.getTopicId());
        }
    }

    private Post findNextPost(Post post, int idx, boolean unread, Collection<Post> toCollapse) {
        if (post == null) {
            post = model.getRoot();
            if (post.getSize() == 0) {
                return null;
            }
        }
        boolean ignoreSubUserThread = Property.SKIP_IGNORED_USER_THREAD.get();
        if (post.isIgnored()) {
            return jumpNextParent(post, unread, toCollapse);
        }
        if (post.getLoadingState() == LoadingState.NotLoaded) {
            if (!unread || post.isRead() != ReadStatus.Read) {
                modelControl.loadThread(model, post, new LoadNextPost(post, unread, toCollapse, false));
                return null;
            }
        }
        if (post.getLoadingState() == LoadingState.Loaded && idx >= post.getSize() || (unread && post.isRead() == ReadStatus.Read)) {
            return jumpNextParent(post, unread, toCollapse);
        }
        int i = idx;
        while (i < post.getSize()) {
            Post p = post.getChild(i);
            if (p.isIgnored()) {
                i++;
                continue;
            }
            if (!unread && !(ignoreSubUserThread && p.isIgnoredUser())) {
                return p;
            }
            switch(p.isRead()) {
                case Read:
                    i++;
                    break;
                case ReadPartially:
                    switch(p.getLoadingState()) {
                        case Loaded:
                            i = 0;
                            post = p;
                            break;
                        case NotLoaded:
                            modelControl.loadThread(model, p, new LoadNextPost(p, unread, toCollapse, true));
                        case Loading:
                            return null;
                    }
                    break;
                case Unread:
                    if (ignoreSubUserThread && p.isIgnoredUser()) {
                        i++;
                    } else {
                        return p;
                    }
                    break;
            }
        }
        Post parent = post.getParent();
        if (unread && parent != null) {
            int nextIdx = parent.getIndex(post) + 1;
            return findNextPost(parent, nextIdx, unread, toCollapse);
        } else {
            return null;
        }
    }

    private Post jumpNextParent(Post post, boolean unread, Collection<Post> toCollapse) {
        Post parent = post.getParent();
        if (parent != null) {
            int nextIdx = parent.getIndex(post) + 1;
            if (Property.VIEW_THREAD_COLLAPSE_THREADS_AFTER_GO2NEXT.get()) {
                toCollapse.add(post);
            }
            return findNextPost(parent, nextIdx, unread, toCollapse);
        } else {
            return null;
        }
    }

    private Post findPrevPost(Post post, boolean unread) {
        try {
            if (post == null) {
                return findLastPost(model.getRoot(), unread);
            }
            Post parent = post.getParent();
            if (parent == null) {
                return null;
            }
            int idx = parent.getIndex(post) - 1;
            if (post.isIgnored()) {
                if (idx >= 0) {
                    return findPrevPost(parent.getChild(idx), unread);
                } else {
                    return findPrevPost(parent, unread);
                }
            }
            if (unread) {
                while (idx >= 0) {
                    Post p = parent.getChild(idx);
                    if (p.isIgnored()) {
                        idx--;
                        continue;
                    }
                    switch(p.isRead()) {
                        case Read:
                            idx--;
                            break;
                        case ReadPartially:
                        case Unread:
                            return findLastPost(p, unread);
                    }
                }
                switch(parent.isRead()) {
                    case Read:
                    case ReadPartially:
                        return findPrevPost(parent, true);
                    case Unread:
                        return parent;
                }
            } else if (idx >= 0) {
                return findLastPost(parent.getChild(idx), unread);
            } else {
                return parent;
            }
        } catch (RuntimeException e) {
        }
        return null;
    }

    /**
     * Searches for the last unread post in the tree thread.
     *
     * @param post   root of sub-tree.
     * @param unread if set to true - unread post will be searched.
     * @return last unread post in sub-tree or <code>null</code> if no unread post is exist in sub-tree.
     * @throws RuntimeException will be thrown in case when data loading is needed to make correct search.
     */
    private Post findLastPost(Post post, boolean unread) throws RuntimeException {
        boolean ignoreSubUserThread = Property.SKIP_IGNORED_USER_THREAD.get();
        if (unread && post.isRead() == ReadStatus.Read || post.isIgnored()) {
            return null;
        }
        switch(post.getLoadingState()) {
            case NotLoaded:
                modelControl.loadThread(model, post, new LoadPreviousPost(post, unread));
            case Loading:
                throw new RojacDebugException("Restart search later");
        }
        int idx = post.getSize() - 1;
        while (idx >= 0) {
            Post p = findLastPost(post.getChild(idx), unread);
            if (p != null) {
                return p;
            }
            idx--;
        }
        if (unread && post.isRead() != ReadStatus.Unread || post.isIgnored() || (ignoreSubUserThread && post.isIgnoredUser())) {
            return null;
        }
        return post;
    }

    private void expandThread(final int messageId) {
        Post post = model.getRoot().getMessageById(messageId);
        if (post != null) {
            selectItem(post, false);
        } else {
            new ThreadChecker(messageId).execute();
        }
    }

    private void resortAndReloadModel() {
        boolean skipSaveState = Property.VIEW_THREAD_COLLAPSE_THREADS_AFTER_SYNC.get();
        @SuppressWarnings("unchecked") Enumeration<TreePath> expanded = skipSaveState ? null : (Enumeration<TreePath>) threads.getExpandedDescendants(model.getPathToRoot(model.getRoot()));
        resortingModel = true;
        modelControl.resortModel(model);
        resortingModel = false;
        if (expanded != null) {
            while (expanded.hasMoreElements()) {
                threads.expandPath(expanded.nextElement());
            }
        }
        if (selectedItem != null) {
            Post selected = model.getRoot().getMessageById(selectedItem.getMessageId());
            selectItem(selected, false);
        } else {
            selectItem(null, false);
        }
    }

    @Override
    public final void processPacket(IPacket packet) {
        if (packet instanceof ReloadDataPacket) {
            messagePane.getHintContainer().clearHints();
            loadItem(getId().getId());
            return;
        }
        final Post curSelection = getSelectedPost();
        Runnable postProcessor = new Runnable() {

            public void run() {
                if (curSelection != null && !curSelection.equals(getSelectedPost())) {
                    selectItem(curSelection, false);
                } else {
                    selectItem(null, false);
                }
            }
        };
        if (model.isInitialized()) {
            modelControl.processPacket(model, packet, null);
        }
    }

    private class SelectionHolder implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE && !(e.getColumn() == TableModelEvent.ALL_COLUMNS && e.getFirstRow() == TableModelEvent.HEADER_ROW && e.getLastRow() == TableModelEvent.HEADER_ROW)) {
                return;
            }
            ThreadState s = getObjectState();
            if (s.openedMessageId() == 0) {
                return;
            }
            Post post = model.getRoot().getMessageById(s.openedMessageId());
            if (post == null) {
                return;
            }
            final TreePath pathToRoot = model.getPathToRoot(post);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    scrollPathToVisible(pathToRoot);
                }
            });
        }
    }

    class NewThreadAction extends AButtonAction {

        public NewThreadAction() {
            super(ShortCut.NewThread);
        }

        public void actionPerformed(ActionEvent e) {
            appControl.editMessage(rootItemId, null);
        }
    }

    class PreviousUnreadAction extends AButtonAction {

        public PreviousUnreadAction() {
            super(ShortCut.PrevUnreadMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedPost();
            selectPrevPost(currentPost, true);
        }
    }

    class NextUnreadAction extends AButtonAction {

        public NextUnreadAction() {
            super(ShortCut.NextUnreadMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedPost();
            selectNextPost(currentPost, true);
        }
    }

    class PreviousAction extends AButtonAction {

        public PreviousAction() {
            super(ShortCut.PrevMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedPost();
            selectPrevPost(currentPost, false);
        }
    }

    class NextAction extends AButtonAction {

        public NextAction() {
            super(ShortCut.NextMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedPost();
            selectNextPost(currentPost, false);
        }
    }

    class MarkSubTreeReadAction extends AButtonAction {

        public MarkSubTreeReadAction() {
            super(ShortCut.MarkSubTreeRead);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Post post = getSelectedPost();
            if (post == null) {
                return;
            }
            if (post instanceof org.xblackcat.rojac.gui.view.model.Thread) {
                new ThreadReadFlagSetter(true, post.getMessageData()).execute();
            } else {
                new SubTreeReadFlagSetter(true, post).execute();
            }
        }
    }

    class MarkWholeThreadReadAction extends AButtonAction {

        public MarkWholeThreadReadAction() {
            super(ShortCut.MarkWholeThreadRead);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Post post = getSelectedPost();
            if (post == null) {
                return;
            }
            new ThreadReadFlagSetter(true, post.getThreadRoot().getMessageData()).execute();
        }
    }

    class ToThreadRootAction extends AButtonAction {

        public ToThreadRootAction() {
            super(ShortCut.ToThreadRoot);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedPost();
            if (currentPost != null) {
                selectPost(modelControl.getTreeRoot(currentPost), true);
            }
        }
    }

    class IgnoreUnreadAction extends AButtonAction {

        IgnoreUnreadAction() {
            super(ShortCut.IgnoreUnread);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Post root = model.getRoot();
            assert root instanceof ForumRoot : "Only forum view can use the action";
            if (Property.IGNORE_TOPICS_DIALOG_SHOW.get() || !Property.IGNORE_TOPICS_SELECT_METHOD.isSet()) {
                IgnoreTopicsDialog dlg = new IgnoreTopicsDialog(appControl.getMainFrame());
                WindowsUtils.center(dlg);
                dlg.setVisible(true);
                TopicIgnoringSelection selectionType = dlg.getAcceptedSelectionType();
                if (selectionType == null) {
                    return;
                }
                Property.IGNORE_TOPICS_SELECT_METHOD.set(selectionType);
            }
            TIntHashSet threadsToIgnore = new TIntHashSet();
            for (int i = 0; i < root.getSize(); i++) {
                Thread thread = (Thread) root.getChild(i);
                if (thread.isIgnored()) {
                    continue;
                }
                switch(Property.IGNORE_TOPICS_SELECT_METHOD.get()) {
                    case TotallyUnread:
                        if (thread.getPostAmount() - 1 <= thread.getUnreadPosts()) {
                            threadsToIgnore.add(thread.getMessageId());
                        }
                        break;
                    case HaveUnread:
                        if (thread.isRead() != ReadStatus.Read) {
                            threadsToIgnore.add(thread.getMessageId());
                        }
                        break;
                }
            }
            final int[] threadIds = threadsToIgnore.toArray();
            final int forumId = root.getForumId();
            new RojacWorker<Void, Integer>() {

                @Override
                protected Void perform() throws Exception {
                    IMiscAH miscAH = Storage.get(IMiscAH.class);
                    for (int threadId : threadIds) {
                        miscAH.addToIgnoredTopicList(threadId);
                        publish(threadId);
                    }
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    for (Integer threadId : chunks) {
                        new IgnoreUpdatedPacket(forumId, threadId, true).dispatch();
                    }
                }
            }.execute();
        }
    }

    class ToggleIgnoreTopicAction extends AButtonAction {

        private final boolean ignore;

        ToggleIgnoreTopicAction(boolean ignore) {
            super(ignore ? ShortCut.IgnoreTopic : ShortCut.FollowTopic);
            this.ignore = ignore;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Post post = getSelectedPost();
            if (post == null) {
                return;
            }
            new TopicIgnoreSetter(!ignore, post.getThreadRoot().getMessageId(), post.getForumId()).execute();
        }
    }

    class ToggleIgnoreUserAction extends AButtonAction {

        private final boolean ignore;

        ToggleIgnoreUserAction(boolean ignore) {
            super(ignore ? ShortCut.IgnoreUser : ShortCut.FollowUser);
            this.ignore = ignore;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Post root = getSelectedPost();
            if (root == null) {
                return;
            }
            final int userId = root.getMessageData().getUserId();
            if (userId <= 0) {
                return;
            }
            new UserIgnoreFlagSetter(!ignore, userId).execute();
        }
    }

    private class LoadNextPost implements Runnable {

        private final Post item;

        private final boolean unread;

        private Collection<Post> toCollapse;

        private final boolean showHint;

        public LoadNextPost(Post item, boolean unread, Collection<Post> toCollapse, boolean showHint) {
            this.item = item;
            this.unread = unread;
            this.toCollapse = toCollapse;
            this.showHint = showHint;
        }

        @Override
        public void run() {
            selectNextPost(item, unread, toCollapse, showHint);
        }
    }

    private class LoadPreviousPost implements Runnable {

        private final Post item;

        private final boolean unread;

        public LoadPreviousPost(Post item, boolean unread) {
            this.item = item;
            this.unread = unread;
        }

        @Override
        public void run() {
            Post prevUnread = findLastPost(item, unread);
            if (prevUnread != null) {
                selectItem(prevUnread, true);
            }
        }
    }

    private class ThreadChecker extends MessageChecker {

        public ThreadChecker(int messageId) {
            super(messageId);
        }

        @Override
        protected void done() {
            if (data != null) {
                final Post root = model.getRoot();
                Post rootMessage = root.getMessageById(data.getThreadRootId());
                if (rootMessage != null) {
                    modelControl.loadThread(model, rootMessage, new Runnable() {

                        @Override
                        public void run() {
                            selectItem(root.getMessageById(messageId), false);
                        }
                    });
                }
            } else {
                if (sourceStackTrace != null) {
                    log.error("Can't load message #" + messageId, sourceStackTrace);
                }
                appControl.closeTab(getId());
            }
        }
    }

    private class DataIntegrityMonitor implements TreeModelListener {

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
            Post root = model.getRoot();
            if (e.getTreePath().getLastPathComponent() == root) {
                fireInfoChanged();
            }
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            Post root = model.getRoot();
            if (root == null) {
                TreeTableThreadView.this.appControl.closeTab(getId());
            } else {
                threads.setRootVisible(modelControl.isRootVisible());
                if (e.getTreePath() == null) {
                    resortAndReloadModel();
                }
                fireInfoChanged();
            }
        }
    }

    protected class PostSelector implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            if (!resortingModel) {
                TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
                if (newLeadSelectionPath != null) {
                    if (newLeadSelectionPath.getLastPathComponent() != null) {
                        selectedItem = ((Post) newLeadSelectionPath.getLastPathComponent()).getMessageData();
                        new PostLoader(selectedItem).execute();
                    }
                }
            }
            fireViewStateChanged();
        }
    }

    protected class ThreadExpander implements TreeExpansionListener {

        public void treeExpanded(TreeExpansionEvent event) {
            final TreePath path = event.getPath();
            Post item = (Post) path.getLastPathComponent();
            if (item.getLoadingState() == LoadingState.NotLoaded) {
                modelControl.loadThread(model, item, new Runnable() {

                    @Override
                    public void run() {
                        threads.expandPath(path);
                    }
                });
            }
            if (item.getLoadingState() == LoadingState.Loaded) {
                if (item.getSize() == 1) {
                    Post child = item.getChild(0);
                    threads.expandPath(path.pathByAddingChild(child));
                }
            }
        }

        public void treeCollapsed(TreeExpansionEvent event) {
            final TreePath path = event.getPath();
            Post item = (Post) path.getLastPathComponent();
            if (item.getLoadingState() == LoadingState.Loaded) {
                if (item.getThreadRoot() == item) {
                    modelControl.unloadThread(model, item);
                }
            }
        }
    }

    protected class ItemListener extends PopupMouseAdapter {

        @Override
        protected void triggerDoubleClick(MouseEvent e) {
            Point p = e.getPoint();
            TreePath path = threads.getPathForLocation(p.x, p.y);
            if (path != null) {
                Post mi = (Post) path.getLastPathComponent();
                modelControl.onDoubleClick(mi, appControl);
            }
        }

        @Override
        protected void triggerPopup(MouseEvent e) {
            Point p = e.getPoint();
            TreePath path = threads.getPathForLocation(p.x, p.y);
            if (path != null) {
                Post mi = (Post) path.getLastPathComponent();
                JPopupMenu m = modelControl.getItemMenu(mi, appControl);
                if (m != null) {
                    m.show(e.getComponent(), p.x, p.y);
                }
            }
        }
    }

    /**
     * Util class: waits until forum will be loaded and then select specified message in thread view
     */
    private class ForumLoadWaiter implements TreeModelListener {

        private final int messageId;

        public ForumLoadWaiter(int messageId) {
            this.messageId = messageId;
        }

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            if (e.getPath() == null && model.isInitialized() && model.getRoot() != null) {
                model.removeTreeModelListener(this);
                if (messageId != 0) {
                    expandThread(messageId);
                } else {
                    selectItem(null, false);
                }
            }
        }
    }

    private class PostLoader extends RojacWorker<Void, MessageDataHolder> {

        private final int messageId;

        private final MessageData messageData;

        public PostLoader(int messageId) {
            this.messageId = messageId;
            messageData = null;
        }

        public PostLoader(MessageData messageData) {
            this.messageId = messageData.getMessageId();
            this.messageData = messageData;
        }

        @Override
        protected Void perform() throws Exception {
            if (messageId != 0) {
                String messageBody;
                MessageData messageData = this.messageData;
                try {
                    if (messageId > 0) {
                        IMessageAH messageAH = Storage.get(IMessageAH.class);
                        if (messageData == null) {
                            messageData = messageAH.getMessageData(messageId);
                            if (messageData == null) {
                                return null;
                            }
                        }
                        messageBody = messageAH.getMessageBodyById(messageId);
                    } else {
                        NewMessage newMessage = Storage.get(INewMessageAH.class).getNewMessageById(-messageId);
                        messageData = new NewMessageData(newMessage);
                        messageBody = newMessage.getMessage();
                    }
                } catch (StorageException e) {
                    throw new RuntimeException("Can't load message #" + messageId, e);
                }
                try {
                    publish(new MessageDataHolder(messageData, messageBody));
                } catch (Exception e) {
                    throw new RuntimeException("Can't parse message #" + messageId + ". Body: " + messageBody, e);
                }
            } else {
                publish(new MessageDataHolder(null, null));
            }
            return null;
        }

        @Override
        protected void process(java.util.List<MessageDataHolder> chunks) {
            for (MessageDataHolder md : chunks) {
                selectedItem = md.getMessage();
                messagePane.fillFrame(selectedItem, md.getMessageBody());
                threads.requestFocus();
                if (selectedItem == null) {
                    continue;
                }
                if (!messageData.isRead()) {
                    Long delay = Property.VIEW_THREAD_AUTOSET_READ.get();
                    if (delay != null && delay >= 0) {
                        MessageUtils.markMessageRead(getId(), messageData, delay);
                    }
                }
            }
        }
    }
}
