package eulergui.tools;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultTreeModel;
import eulergui.gui.interfaces.ToolTipProvider;

/** Combo Tree View, a tree view with a text filter, similar to the preferences panel in Eclipse;
 * used in {@link OwlClassHierarchyBuilder} */
public class ComboTreeView extends JPanel {

    private static final long serialVersionUID = 1L;

    JTree jTree;

    JTextField textField;

    private FilteredTreeNode tree;

    private ToolTipProvider toolTipProvider;

    public ComboTreeView(FilteredTreeNode tree) {
        this.tree = tree;
        jTree = new JTree() {

            @Override
            public String getToolTipText(MouseEvent evt) {
                return toolTipProvider.getToolTipText(evt);
            }
        };
        ToolTipManager.sharedInstance().registerComponent(jTree);
        textField = new JTextField(50);
        setLayout(new BorderLayout());
        add(textField, BorderLayout.NORTH);
        add(jTree, BorderLayout.CENTER);
        jTree.setModel(new DefaultTreeModel(tree));
        textField.getDocument().addDocumentListener(new RedoFilterListener());
    }

    class RedoFilterListener implements DocumentListener {

        void redoFilter() {
            final StringTokenizer st = new StringTokenizer(textField.getText());
            final List<String> list = new ArrayList<String>();
            while (st.hasMoreTokens()) {
                list.add(st.nextToken());
            }
            tree.filter(list);
            ((DefaultTreeModel) jTree.getModel()).nodeStructureChanged(tree);
            for (int r = 0; r < jTree.getRowCount(); r++) {
                jTree.expandRow(r);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            redoFilter();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            redoFilter();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            redoFilter();
        }
    }

    public JTree getJTree() {
        return jTree;
    }

    /**
	 * @param toolTipProvider
	 *            the toolTipProvider to set
	 */
    public void setToolTipProvider(ToolTipProvider toolTipProvider) {
        this.toolTipProvider = toolTipProvider;
    }
}
