package metafrastes;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.resultset.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Literal;
import hermes.NewsPlugin;
import hermes.Relation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JEditorPane;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import metafrastes.ontology.Library;

/**
 *
 * @author Jethro Borsje/Hanno Embregts
 */
public class NewsItem {

    private String m_id;

    private String m_title;

    private String m_date;

    private String m_source;

    private String m_content;

    private String m_URI;

    private String m_logo;

    private ArrayList<Relation> m_relationList;

    private ArrayList<NewsItem> m_relatedNewsItems;

    private Hashtable<String, NewsPlugin> m_newsPlugins;

    /** Creates a new instance of NewsItem */
    public NewsItem(String p_id, String p_title, String p_date, String p_source, String p_content, String p_URI, ArrayList<Relation> p_relationList) {
        m_id = p_id;
        m_title = p_title;
        m_date = p_date;
        m_source = p_source;
        m_content = p_content;
        m_URI = p_URI;
        m_relationList = p_relationList;
        m_relatedNewsItems = new ArrayList();
        retreiveRelatedNews();
        getImage();
    }

    public NewsItem(String p_id) {
        m_id = p_id;
        OntModel model = Main.getModel();
        String queryString = "PREFIX hermes: <" + Constants.NEWS_NS + ">\n" + "SELECT ?title ?date ?content ?link ?publisher\n" + "WHERE {\n" + "<_:" + m_id + "> hermes:title ?title .\n" + "<_:" + m_id + "> hermes:time ?date .\n" + "<_:" + m_id + "> hermes:textItem ?content .\n" + "<_:" + m_id + "> hermes:link ?link .\n" + "<_:" + m_id + "> hermes:publisher ?publisher .\n" + "}";
        QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution result = rs.nextSolution();
                m_title = ((Literal) result.get("title")).getString();
                m_date = ((Literal) result.get("date")).getString();
                m_content = ((Literal) result.get("content")).getString();
                m_URI = ((Literal) result.get("link")).getString();
                m_source = ((Literal) result.get("publisher")).getString();
                try {
                    DatatypeFactory xmlDateFac = DatatypeFactory.newInstance();
                    XMLGregorianCalendar xmlCalendar = xmlDateFac.newXMLGregorianCalendar(m_date);
                    GregorianCalendar gregorianCalendar = xmlCalendar.toGregorianCalendar();
                    SimpleDateFormat dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    m_date = dateAndTime.format(gregorianCalendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            qexec.close();
        }
        m_relatedNewsItems = new ArrayList();
        getImage();
    }

    private void getImage() {
        OntModel model = Main.getModel();
        String queryString = "PREFIX hermes: <" + Constants.NEWS_NS + ">\n" + "PREFIX kb: <" + Constants.KB_NS + ">\n" + "SELECT ?image\n" + "WHERE {\n" + "<_:" + m_id + "> hermes:relation ?relation .\n" + "?relation hermes:timesFound ?timesFound .\n" + "?relation hermes:relatedTo ?concept .\n" + "?concept kb:image ?image .\n" + "}\n" + "ORDER BY DESC(?timesFound)\n" + "LIMIT 1";
        QueryExecution qexec = QueryExecutionFactory.create(queryString, model);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {
                QuerySolution result = rs.nextSolution();
                String image = ((Literal) result.get("image")).getString();
                m_logo = Constants.LOGO_DIR + image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            qexec.close();
        }
    }

    public ArrayList<Relation> getRelationList() {
        return m_relationList;
    }

    static int i = 0;

    public void retreiveRelatedNews() {
    }

    public String getEntireStory() {
        String content = "<p>" + Constants.HTML_FONT_OPEN;
        content += "<h2>" + m_title + "</h2>";
        content += "<b>" + m_source + "</b> - " + m_date + "<br>";
        content += m_content;
        content += Library.createRelNewsLink(m_id, m_relatedNewsItems.size());
        content += Constants.HTML_FONT_CLOSE + "</p>";
        JEditorPane newsItemPane = new JEditorPane("text/html", content);
        newsItemPane.setEditable(false);
        return content;
    }

    public String getAsPopular() {
        String content = Constants.HTML_FONT_OPEN;
        content += m_title + " - ";
        content += Library.createEntireStoryLink(m_URI) + " - ";
        content += Library.createRelNewsLink(m_id, m_relatedNewsItems.size());
        content += Constants.HTML_FONT_CLOSE + "<br>";
        JEditorPane newsItemPane = new JEditorPane("text/html", content);
        newsItemPane.setEditable(false);
        return content;
    }

    public String getAsRecent() {
        String content = Constants.HTML_FONT_OPEN;
        content += m_title + " - ";
        content += Library.createEntireStoryLink(m_URI) + " - ";
        content += Library.createRelNewsLink(m_id, m_relatedNewsItems.size());
        content += Constants.HTML_FONT_CLOSE + "<br>";
        JEditorPane newsItemPane = new JEditorPane("text/html", content);
        newsItemPane.setEditable(false);
        return content;
    }

    public String getAsShort() {
        String newsContent = m_content;
        newsContent = newsContent.replaceAll("<p>", "");
        newsContent = newsContent.replaceAll("</p>", "");
        String content = "<table border=\"0\"><tr>";
        if (m_logo != null) content += "<td><img src=\"" + m_logo + "\" width=\"80\" height=\"80\" border=\"1\"></td>";
        content += "<td>";
        content += Constants.HTML_FONT_LARGE_OPEN + "<b>" + addRelationColors(m_title) + Constants.HTML_FONT_LARGE_CLOSE + "</b><br>";
        content += Constants.HTML_FONT_OPEN;
        content += "<b>" + m_source + "</b> - " + m_date + "<br>";
        content += addRelationColors(newsContent.substring(0, (newsContent.length() < 200 ? newsContent.length() : 200))) + " ...<br>";
        content += Library.createEntireStoryLink(m_URI) + "<br>";
        content += Library.createRelNewsLink(m_id, m_relatedNewsItems.size());
        content += Library.createNewsPluginLinks(m_newsPlugins, m_id);
        content += Constants.HTML_FONT_CLOSE;
        content += "</td></tr></table>";
        content += "<hr>";
        return content;
    }

    public void setNewsPlugins(Hashtable<String, NewsPlugin> p_newsPlugins) {
        m_newsPlugins = p_newsPlugins;
    }

    public String getRelatedNews() {
        String content = "";
        for (int i = 0; i < m_relatedNewsItems.size(); i++) {
            content += m_relatedNewsItems.get(i).getAsShort();
        }
        return content;
    }

    private String addRelationColors(String p_text) {
        String retval = p_text;
        for (int i = 0; i < m_relationList.size(); i++) {
            Relation relation = m_relationList.get(i);
            ArrayList<String> hits = relation.getHits();
            if (relation.getColor() != null) {
                for (int j = 0; j < hits.size(); j++) {
                    String currentHit = hits.get(j);
                    Pattern p = Pattern.compile("(?i)([\\W&&[^#]]|^)(" + currentHit + ")(\\W)");
                    Matcher m = p.matcher(retval);
                    retval = m.replaceAll("$1<font color=\"" + relation.getColor() + "\"><strong>$2</strong></font>$3");
                }
            }
        }
        return retval;
    }
}
