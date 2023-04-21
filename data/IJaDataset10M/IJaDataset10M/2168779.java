package siocjavaapi;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.ArrayList;

/**
 *
 * @author fbustos
 */
public class Forum {

    private String id;

    private String url;

    private String blogTitle;

    private String description;

    private ArrayList<Forum> forums;

    private ArrayList<Post> topics;

    public Forum(String id, String url, String blogTitle, String description) {
        this.id = id;
        this.url = url;
        this.blogTitle = blogTitle;
        this.description = description;
        forums = new ArrayList<Forum>();
        topics = new ArrayList<Post>();
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Forum> getForums() {
        return forums;
    }

    public void setForums(ArrayList<Forum> forums) {
        this.forums = forums;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Post> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<Post> topics) {
        this.topics = topics;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addForum(Forum forum) {
        this.forums.add(forum);
    }

    public void addTopic(Post topic) {
        this.topics.add(topic);
    }

    public void generate(Model m, String lan, FilterInterface filter) {
        generate(m, lan, this, filter);
    }

    public static void generate(Model m, String lan, Forum f, FilterInterface filter) {
        String sioc = "http://rdfs.org/sioc/ns#";
        Resource rForum = m.createResource(sioc + "forum");
        Property pParentOf = m.createProperty(sioc + "parent_of");
        Property pContainerOf = m.createProperty(sioc + "container_of");
        Resource forum = m.createResource(f.getUrl()).addProperty(RDF.type, rForum);
        if (f.getDescription() != null) {
            forum.addProperty(DCTerms.description, m.createLiteral(f.getDescription(), lan));
        }
        if (f.getBlogTitle() != null) {
            forum.addProperty(DCTerms.title, m.createLiteral(f.getBlogTitle(), lan));
        }
        int forumSize = f.getForums().size();
        int topicSize = f.getTopics().size();
        if (forumSize > 0) {
            for (int i = 0; i < forumSize; ++i) {
                forum.addProperty(pParentOf, m.createResource(f.getForums().get(i).getUrl()));
            }
        }
        if (topicSize > 0) {
            for (int i = 0; i < topicSize; ++i) {
                forum.addProperty(pContainerOf, m.createResource(f.getTopics().get(i).getUrl()));
            }
        }
        if (forumSize > 0) {
            for (int i = 0; i < forumSize; ++i) {
                generate(m, lan, f.getForums().get(i), filter);
            }
        }
        if (topicSize > 0) {
            for (int i = 0; i < topicSize; ++i) {
                f.getTopics().get(i).generate(m, lan, filter);
            }
        }
    }
}
