package org.jabusuite.article.session;

import java.util.Calendar;
import java.util.List;
import javax.ejb.Remote;
import org.jabusuite.article.Article;
import org.jabusuite.article.ArticleGroup;
import org.jabusuite.core.companies.JbsCompany;
import org.jabusuite.core.users.JbsUser;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.core.utils.JbsManagementRemote;

/**
 * @author hilwers
 * @date 29.03.2007
 *
 */
@Remote
public interface ArticlesRemote extends JbsManagementRemote {

    /**
     * Creates a new Article
     * @param article The article to persist in the database
     * @param user The owner of te article
     * @param company The company the article belongs to
     */
    public void createDataset(Article article, JbsUser user, JbsCompany company);

    /**
     * Updates the specified article.
     * @param article
     */
    public void updateDataset(Article article, JbsUser changeUser) throws EJbsObject;

    /**
     * Deletes an existing article.
     * @param article
     * @param changeUser
     */
    public void deleteDataset(Article article, JbsUser changeUser) throws EJbsObject;

    /**
     * Finds the article with the specified id
     * @param id
     * @param withAdditionalData if true the prices and groups will also be retrieved, otherwise not
     * @return
     */
    public Article findDataset(long id, boolean withAdditionalData);

    /**
     * Retrieves a list of all articles
     * @param date The date where the prices should be fetched (may be null)
     * @param articleGroup The group the articles shall belong to (may be null)
     * @param filter An additional filter in sql-syntax (a stands for Article, p for ArticlePrice and g for ArticleGroup)
     * @param orderFields The fields to order by (comma-separated
     * @param user The user who fetches
     * @param company The company the articles shall belog to
     * @param firstResult The fist result (may be 0)
     * @param resultCount The amount of results (if 00 all articles will be fetched at once
     * @return A List with the articles matching the specified criteria.
     */
    public List getDatasets(Calendar date, ArticleGroup articleGroup, String filter, String orderFields, JbsUser user, JbsCompany company, int firstResult, int resultCount);

    /**
     * Counts the datasets
     * @param date The date (for the prices)
     * @param articleGroup The article group
     * @param filter An additional filter
     * @param user The user
     * @param company The company
     * @return
     */
    public long getDatasetCount(Calendar date, ArticleGroup articleGroup, String filter, JbsUser user, JbsCompany company);
}
