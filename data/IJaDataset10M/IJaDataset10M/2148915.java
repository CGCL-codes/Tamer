package org.blueoxygen.komodo.artpublisher.actions;

import java.util.ArrayList;
import java.util.List;
import org.blueoxygen.cimande.persistence.PersistenceAware;
import org.blueoxygen.cimande.persistence.PersistenceManager;
import org.blueoxygen.komodo.ArtPublisher;
import org.blueoxygen.komodo.Article;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author harry
 * email :  harry@intercitra.com
 */
public class ViewArtPublisher extends ActionSupport implements PersistenceAware {

    protected PersistenceManager pm;

    protected ArtPublisher artPublisher;

    private String id = "";

    private List articles = new ArrayList();

    public String execute() {
        if (!id.equalsIgnoreCase("")) {
            String query = " FROM " + Article.class.getName() + " as warintek_content WHERE warintek_content.artPublisher.id ='" + getId() + "'";
            artPublisher = (ArtPublisher) pm.getById(ArtPublisher.class, getId());
            articles = pm.getList(query, null, null);
            return SUCCESS;
        } else if (artPublisher != null) {
            return SUCCESS;
        } else {
            addActionError("Tidak ditemukan penerbit.");
            return ERROR;
        }
    }

    public List getArticles() {
        return articles;
    }

    public void setArticles(List articles) {
        this.articles = articles;
    }

    public ArtPublisher getArtPublisher() {
        return artPublisher;
    }

    public void setArtPublisher(ArtPublisher artPublisher) {
        this.artPublisher = artPublisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.pm = persistenceManager;
    }
}
