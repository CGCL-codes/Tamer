package be.lassi.ui.cuesteps;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import be.lassi.base.Holder;
import be.lassi.base.Listener;
import be.lassi.cues.Cue;
import be.lassi.cues.CueDetail;
import be.lassi.util.UiTools;

public class CueDetailFrame extends JFrame {

    private final CueDetailModel model = new CueDetailModel();

    private final CardLayout layout = new CardLayout();

    private final JPanel cardPanel = new JPanel(layout);

    private final Map<String, FormInfo> forms = new HashMap<String, FormInfo>();

    private CueDetailForm currentForm;

    private final Holder<Cue> cueHolder;

    private JTree tree;

    public CueDetailFrame(final Holder<Cue> cueHolder) {
        super("Cue Detail");
        this.cueHolder = cueHolder;
        setContentPane(createPanel());
        pack();
        init();
    }

    private void init() {
        cueHolder.add(new Listener() {

            public void changed() {
                Cue cue = cueHolder.getValue();
                System.out.println("Cue: " + cue.getDescription());
                FormInfo found = null;
                for (FormInfo info : forms.values()) {
                    if (info.getForm().supports(cue)) {
                        found = info;
                        break;
                    }
                }
                if (found == null) {
                    throw new AssertionError("Could not find form for cue step:" + cue);
                }
                showForm(found.getForm().getKey());
                model.setCueDetail(cue.getDetail());
                tree.setSelectionPath(found.getTreePath());
            }
        });
    }

    protected JComponent createPanel() {
        return new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTree(), cardPanel);
    }

    private JComponent createTree() {
        DefaultTreeModel defaultree = new DefaultTreeModel(createTreeNodes());
        tree = new JTree(defaultree);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setIcon(null);
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(renderer);
        UiTools.fixExpandedBounds(tree);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(final TreeSelectionEvent arg0) {
                TreePath path = arg0.getPath();
                Object object = path.getLastPathComponent();
                System.out.println("selection=" + object);
                String key = (String) ((DefaultMutableTreeNode) object).getUserObject();
                showForm(key);
            }
        });
        return tree;
    }

    private void showForm(final String key) {
        layout.show(cardPanel, key);
        currentForm = forms.get(key).getForm();
    }

    private DefaultMutableTreeNode add(final CueDetailForm form) {
        cardPanel.add(form.getPanel(), form.getKey());
        FormInfo info = new FormInfo(form);
        forms.put(form.getKey(), info);
        return info.getNode();
    }

    private TreeNode createTreeNodes() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        CueDetailForm form = new LightForm(model);
        root.add(add(form));
        DefaultMutableTreeNode node = add(new ShowSceneForm(model));
        node.add(add(new ShowPrevSceneForm(model)));
        node.add(add(new ShowRefSceneForm(model)));
        root.add(node);
        node = add(new GoForm(model));
        node.add(add(new GoNextForm(model)));
        node.add(add(new GoPrevForm(model)));
        node.add(add(new LoopForm(model)));
        root.add(node);
        root.add(add(new HoldForm(model)));
        node = add(new LayerControlForm(model));
        node.add(add(new SetAttributesForm(model)));
        node.add(add(new SetMixerForm(model)));
        node.add(add(new SetChaserForm(model)));
        root.add(node);
        root.add(add(new CommentForm(model)));
        root.add(add(new AudioForm(model)));
        return root;
    }

    public CueDetail getCueDetail() {
        return currentForm.getCueDetail();
    }
}
