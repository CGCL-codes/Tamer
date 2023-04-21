package hermes.gui.tab;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import hermes.*;
import hermes.gui.panel.HTMLTitledPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

/**
 *
 * @author Jethro Borsje
 */
public class HomeTab extends JPanel {

    /** Creates a new instance of HomeTab */
    public HomeTab() {
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
        constraints.gridheight = 4;
        constraints.anchor = constraints.FIRST_LINE_START;
        constraints.fill = constraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        this.add(getHomeContent(), constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.fill = constraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(getPopularConcepts(), constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        this.add(getPopularNews(), constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        this.add(getRecentNews(), constraints);
        constraints.gridx = 1;
        constraints.gridy = 3;
        this.add(getOntologyStats(), constraints);
        this.setBackground(Color.WHITE);
    }

    private HTMLTitledPanel getHomeContent() {
        String content = "<html><font face=\"Tahoma\" size=\"3\">";
        content += "<p>Welcome to the Hermes News Portal! Hermes is a news portal, which enables you to search for news items all over the internet, thereby taking your personal preferences into account. Hermes uses various Semantic Web technologies (including OWL and SPARQL) to classify the news items to their proper meaning, while keeping track of the relevance of the news items. By using this approach, Hermes provides an easy and structured way to extract news items from various sources according to your interests.</p>";
        content += "<p>The screen you see now, is the 'Home' tab. On the right you can immediately jump to the most popular news times, the most recent news items, and the most popular concepts (i.e., keywords that trigger a news item to be classified). To start a new search, select the 'Original graph' tab. The left side of the screen displays a graph with all possible concepts. You can select the areas you're interested in by clicking the nodes. In the upper right corner, where the 'Control Panel' resides, you can specify which nodes should be taken into account. The lower right corner, being the 'Node Info Panel', shows you relevant information about the selected node. You can view the selected concepts by selecting the 'Search graph' tab. There is the option to define a time constraint using the 'Time Constraint Panel', or you can directly proceed by clicking the 'Get news' button. The results of your search will be displayed on the 'Results' tab. From here, you can analyze your results or start a new search.</p>";
        content += "</font></html>";
        HTMLTitledPanel homePanel = new HTMLTitledPanel("Welcome", content);
        homePanel.setPanelSize(400, 300);
        return homePanel;
    }

    private HTMLTitledPanel getPopularConcepts() {
        String content = "<html><font face=\"Tahoma\" size=\"3\">";
        content += "Google - " + Library.createRelNewsLink("uri", 4) + " - " + Library.createRelConceptsLink(Constants.KB_NS + "GOOG", 7) + "<br>";
        content += "Microsoft - " + Library.createRelNewsLink("uri", 5) + " - " + Library.createRelConceptsLink(Constants.KB_NS + "MSFT", 11) + "<br>";
        content += "Linux - " + Library.createRelNewsLink("uri", 3) + " - " + Library.createRelConceptsLink(Constants.KB_NS + "Linux", 4) + "<br>";
        content += "Steve Ballmer - " + Library.createRelNewsLink("uri", 2) + " - " + Library.createRelConceptsLink(Constants.KB_NS + "Steve_Ballmer", 3) + "<br>";
        content += "</font></html>";
        return new HTMLTitledPanel("Popular concepts", content);
    }

    private HTMLTitledPanel getRecentNews() {
        String content = "";
        for (int i = 0; i < Main.newsItems.size(); i++) {
            content += Main.newsItems.get(i).getAsRecent();
        }
        return new HTMLTitledPanel("Recent news", content);
    }

    private HTMLTitledPanel getPopularNews() {
        String content = "";
        for (int i = 0; i < Main.popularItems.size(); i++) {
            content += Main.popularItems.get(i).getAsPopular();
        }
        return new HTMLTitledPanel("Popular news", content);
    }

    private HTMLTitledPanel getOntologyStats() {
        String content = "";
        OntModel model = Main.getModel();
        OntClass concepts = model.getOntClass(Constants.KB_NS + "Concept");
        OntClass news = model.getOntClass(Constants.NEWS_NS + "News");
        OntClass relation = model.getOntClass(Constants.NEWS_NS + "Relation");
        int numberOfNews = news.listInstances(false).toList().size();
        int numberOfRelation = relation.listInstances(false).toList().size();
        content += Constants.HTML_FONT_OPEN;
        content += "Number of concepts: " + 473 + "<br>";
        content += "Number of news items: " + numberOfNews + "<br>";
        content += "Number of relations: " + numberOfRelation + "<br>";
        content += Constants.HTML_FONT_CLOSE;
        return new HTMLTitledPanel("Ontology statistics", content);
    }
}
