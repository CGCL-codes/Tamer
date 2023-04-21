package cookbook.view.management;

import cookbook.RepresentationConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ScrollPaneConstants;
import cookbook.model.CookbookManager;
import cookbook.model.RecipeList;
import cookbook.model.RecipeType;
import cookbook.LanguageContent;
import java.util.Observer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import static javax.swing.LayoutStyle.ComponentPlacement.*;

/**
 * Recipe list representation
 * 
 * @author Dominik Schaufelberger
 */
public class RecipeListRepr extends JPanel implements Observer {

    private JScrollPane scrollPane;

    private JList recipeList;

    private JComboBox filterBox;

    /**
     * Creates a list representation.
     */
    public RecipeListRepr() {
        this.recipeList = new JList(new DefaultListModel());
        this.recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.recipeList.setSelectedIndex(this.recipeList.getModel().getSize());
        this.scrollPane = new JScrollPane(this.recipeList);
        this.scrollPane.setPreferredSize(RepresentationConstants.LIST_SIZE);
        this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.filterBox = new JComboBox(LanguageContent.list_combobox_labels);
        this.filterBox.setPreferredSize(RepresentationConstants.LIST_COMBOBOX_SIZE);
        this.filterBox.setSelectedIndex(0);
        this.setLayout(createGroupLayout());
        this.filterBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JComboBox cb = (JComboBox) e.getSource();
                        CookbookManager.getCookbookManager().getRList().setSelectedFilter(cb.getSelectedItem().toString());
                    }
                });
            }
        });
        this.recipeList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                String selectedRecipe = (String) list.getSelectedValue();
                CookbookManager.getCookbookManager().getRList().setSelectedRecipe(selectedRecipe);
            }
        });
        this.recipeList.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    CookbookManager.getCookbookManager().loadFile();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
    }

    private GroupLayout createGroupLayout() {
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.filterBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(this.scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(this.filterBox, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(RELATED).addComponent(this.scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
        return layout;
    }

    @Override
    public void update(Observable o, Object arg) {
        DefaultListModel listModel = (DefaultListModel) this.recipeList.getModel();
        RecipeList list = (RecipeList) o;
        RecipeType selectedType;
        String selectedFilter = list.getSelectedFilter();
        if (selectedFilter.equals(LanguageContent.list_combobox_labels[1])) {
            selectedType = RecipeType.COOKING;
        } else if (selectedFilter.equals(LanguageContent.list_combobox_labels[2])) {
            selectedType = RecipeType.BAKING;
        } else if (selectedFilter.equals(LanguageContent.list_combobox_labels[3])) {
            selectedType = RecipeType.SALAD;
        } else if (selectedFilter.equals(LanguageContent.list_combobox_labels[4])) {
            selectedType = RecipeType.DESSERT;
        } else {
            selectedType = RecipeType.ALL;
        }
        String[] filteredRecipes = list.getTypespecificRecipeNames(selectedType);
        listModel.clear();
        for (int i = 0; i <= filteredRecipes.length - 1; i++) {
            listModel.addElement(filteredRecipes[i]);
        }
    }
}
