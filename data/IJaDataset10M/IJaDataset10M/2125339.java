package de.shandschuh.jaolt.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import de.shandschuh.jaolt.core.AuctionPlatformAccount;
import de.shandschuh.jaolt.core.AuctionPlatformSite;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.core.auction.Category;
import de.shandschuh.jaolt.core.exception.CommonException;
import de.shandschuh.jaolt.gui.FormManager;
import de.shandschuh.jaolt.gui.core.ButtonPanelFactory;
import de.shandschuh.jaolt.gui.core.CategoryJTree;
import de.shandschuh.jaolt.gui.core.Saveable;
import de.shandschuh.jaolt.gui.core.buttons.CancelJButton;
import de.shandschuh.jaolt.gui.core.buttons.OkJButton;
import de.shandschuh.jaolt.gui.dialogs.selectcategoryjdialog.CategoryJTreeNode;
import de.shandschuh.jaolt.gui.listener.dialogs.selectcategory.DefaultTreeSelectionListener;

public class CategorySelectionJDialog extends JDialog implements Saveable {

    /** Default serial version uid */
    private static final long serialVersionUID = 1L;

    private JTextField categoryPathJTextField;

    private Category category;

    private Category clonedCategory;

    private boolean success;

    private CategoryJTree categoryJTree;

    private JScrollPane treeJScrollPane;

    private JPanel panel;

    private static CategorySelectionJDialog currentInstance;

    public static final CategorySelectionJDialog create(Category category, Vector<Category> categories, AuctionPlatformAccount auctionPlatformAccount, AuctionPlatformSite auctionPlatformSite, JDialog parent) {
        if (currentInstance == null) {
            currentInstance = new CategorySelectionJDialog(category, categories, auctionPlatformAccount, auctionPlatformSite, parent);
        } else {
            currentInstance.setup(category, categories, auctionPlatformAccount, auctionPlatformSite);
        }
        return currentInstance;
    }

    public static final void clear() {
        if (currentInstance != null) {
            currentInstance.setForceRebuild(true);
        }
    }

    private CategorySelectionJDialog(Category category, Vector<Category> categories, AuctionPlatformAccount auctionPlatformAccount, AuctionPlatformSite auctionPlatformSite, JDialog parent) {
        super(parent, Language.translateStatic("CHOOSE_CATEGORY_TITLE"), true);
        categoryPathJTextField = new JTextField(13);
        categoryPathJTextField.setEditable(false);
        categoryPathJTextField.setEnabled(false);
        categoryPathJTextField.setDisabledTextColor(Color.BLACK);
        JPanel topJPanel = new JPanel(new BorderLayout());
        topJPanel.add(categoryPathJTextField);
        setSize(400, 550);
        categoryJTree = new CategoryJTree();
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topJPanel, BorderLayout.NORTH);
        categoryJTree.addTreeSelectionListener(new DefaultTreeSelectionListener(this));
        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        treeJScrollPane = new JScrollPane(categoryJTree);
        panel.add(treeJScrollPane);
        contentPane.add(panel);
        if (!FormManager.isLeftToRight()) {
            contentPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        contentPane.add(ButtonPanelFactory.create(new OkJButton(this), new CancelJButton(this)), BorderLayout.SOUTH);
        setLocationRelativeTo(parent);
        setup(category, categories, auctionPlatformAccount, auctionPlatformSite);
    }

    public void setup(Category category, Vector<Category> categories, AuctionPlatformAccount auctionPlatformAccount, AuctionPlatformSite auctionPlatformSite) {
        success = false;
        this.category = category;
        clonedCategory = category.clone();
        if (categories != null) {
            categoryJTree.setup(categories);
        } else {
            categoryJTree.setup(auctionPlatformAccount, auctionPlatformSite);
        }
        categoryJTree.selectCategory(clonedCategory);
        setVisible(true);
    }

    public void setSelectedCategory(CategoryJTreeNode node) {
        clonedCategory.update(node.getCategory());
        categoryPathJTextField.setText(clonedCategory.toString());
    }

    public void save() throws CommonException {
        if (!clonedCategory.isLeaf() && clonedCategory.getName() != null && clonedCategory.getName().length() > 0) {
            throw new CommonException(Language.translateStatic("ERROR_PLEASECHOOSESUBCATEGORY"));
        } else {
            dispose(true);
        }
    }

    public void dispose() {
        dispose(false);
    }

    public void dispose(boolean valid) {
        success = valid;
        if (valid) {
            category.update(clonedCategory);
        }
        super.dispose();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setForceRebuild(boolean forceRebuild) {
        categoryJTree.setForceRebuild(forceRebuild);
    }
}
