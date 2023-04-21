package hermes.gui.tab;

import hermes.*;
import hermes.gui.panel.HTMLTitledPanel;
import hermes.gui.panel.TitledPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ResultTab.java
 *
 * Created on 20 oktober 2006, 21:16
 * 
 * @author Jethro Borsje
 */
public class ResultTab extends JPanel {

    private TitledPanel m_searchQueryPanel;

    private HTMLTitledPanel m_resultsPanel;

    private ArrayList<String> m_searchQuery;

    private ArrayList<NewsItem> m_results;

    private Hashtable<String, JCheckBox> m_searchQueryCheckbox;

    /** Creates a new instance of ResultTab */
    public ResultTab() {
        m_searchQuery = new ArrayList();
        m_searchQueryPanel = createSearchQueryPanel();
        m_resultsPanel = createResultsPanel();
        initComponents();
    }

    /**
     * Init the components of this tab.
     */
    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = constraints.FIRST_LINE_START;
        constraints.fill = constraints.BOTH;
        constraints.weightx = .1;
        constraints.weighty = .1;
        this.add(m_searchQueryPanel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = .9;
        constraints.weighty = .9;
        this.add(m_resultsPanel, constraints);
        this.setBackground(Color.WHITE);
    }

    /**
     * Create the panel which is going to contain a list of the concepts the user 
     * selected. About this concepts, the news is shown.
     */
    private TitledPanel createSearchQueryPanel() {
        String content = Constants.HTML_FONT_OPEN + "You have not conducted a search yet." + Constants.HTML_FONT_CLOSE;
        return new HTMLTitledPanel("Search query", content);
    }

    /**
     * Create the panel which is going to show the news items.
     */
    private HTMLTitledPanel createResultsPanel() {
        String content = Constants.HTML_FONT_OPEN + "There are no results." + Constants.HTML_FONT_CLOSE;
        return new HTMLTitledPanel("Resulting news items", content);
    }

    /**
     * Set the resulting news items in the result panel.
     */
    public void setResult(ArrayList<NewsItem> p_results) {
        m_results = p_results;
        String content = "";
        m_resultsPanel.setHTMLContent("");
        for (int i = 0; i < m_results.size(); i++) {
            NewsItem newsItem = m_results.get(i);
            content += newsItem.getAsShort();
        }
        m_resultsPanel.setHTMLContent(content);
        m_resultsPanel.setTitle("Resulting news items (" + m_results.size() + ")");
    }

    /**
     * Set the concepts the user searched for in the search query panel.
     */
    public void setSearchQuery(ArrayList<String> p_searchQuery, Hashtable<String, String> p_conceptColors) {
        m_searchQuery = p_searchQuery;
        m_searchQueryCheckbox = new Hashtable<String, JCheckBox>();
        JLabel title = new JLabel("These are the concepts of your search query:");
        title.setFont(Constants.LABEL_FONT);
        JPanel content = new JPanel(new GridLayout(0, 4));
        content.add(title);
        content.add(new JLabel(""));
        content.add(new JLabel(""));
        content.add(new JLabel(""));
        for (int i = 0; i < m_searchQuery.size(); i++) {
            String checkboxText = Constants.HTML_OPEN + Constants.HTML_FONT_OPEN;
            checkboxText += "<font color=\"" + p_conceptColors.get(Constants.KB_NS + m_searchQuery.get(i)) + "\">";
            checkboxText += "<strong>" + m_searchQuery.get(i) + "</strong>";
            checkboxText += Constants.HTML_FONT_CLOSE + Constants.HTML_FONT_CLOSE + Constants.HTML_CLOSE;
            JCheckBox checkbox = new JCheckBox(checkboxText);
            checkbox.setFont(Constants.LABEL_FONT);
            checkbox.setSelected(true);
            checkbox.setBackground(Constants.BACKGROUND);
            checkbox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    checkboxActionPerformed();
                }
            });
            m_searchQueryCheckbox.put(m_searchQuery.get(i), checkbox);
            content.add(checkbox);
        }
        content.setBackground(Constants.BACKGROUND);
        m_searchQueryPanel.setContent(content);
    }

    /**
     * This method is triggered when the checkbox for a concept in the search query 
     * is checked / unchecked.
     */
    private void checkboxActionPerformed() {
        for (int i = 0; i < m_searchQuery.size(); i++) {
            String concept = m_searchQuery.get(i);
            JCheckBox checkbox = m_searchQueryCheckbox.get(concept);
            for (int numResults = 0; numResults < m_results.size(); numResults++) {
                NewsItem newsItem = m_results.get(numResults);
                ArrayList<Relation> relationList = newsItem.getRelationList();
                for (int numRelations = 0; numRelations < relationList.size(); numRelations++) {
                    Relation relation = relationList.get(numRelations);
                    if (relation.getconceptURI().equals(Constants.KB_NS + concept)) {
                        relation.setEnabled(checkbox.isSelected());
                    }
                }
            }
        }
        updateVisibleResults();
    }

    /**
     * Update the result set visibility with respect to the checkboxes for the concepts 
     * in the search query.
     */
    private void updateVisibleResults() {
        int numVisibleResults = 0;
        String content = "";
        for (int numResults = 0; numResults < m_results.size(); numResults++) {
            boolean visible = false;
            NewsItem newsItem = m_results.get(numResults);
            ArrayList<Relation> relationList = newsItem.getRelationList();
            int numRelations = 0;
            while (numRelations < relationList.size() && !visible) {
                Relation relation = relationList.get(numRelations);
                int i = 0;
                while (i < m_searchQuery.size() && !visible) {
                    String concept = m_searchQuery.get(i);
                    if (relation.getconceptURI().equals(Constants.KB_NS + concept)) {
                        if (relation.getEnabled()) visible = true;
                    }
                    i++;
                }
                numRelations++;
            }
            if (visible) {
                content += newsItem.getAsShort();
                numVisibleResults++;
            }
        }
        m_resultsPanel.setTitle("Resulting news items (" + numVisibleResults + ")");
        m_resultsPanel.setHTMLContent(content);
        m_resultsPanel.validate();
    }
}
