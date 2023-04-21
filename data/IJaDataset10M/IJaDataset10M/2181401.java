package si.fri.pvis;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import si.fri.DotNetStore.DotNetStoreB2BSoapProxy;

/**
 * A class for administrating the JavaStore.
 * 
 * @author Nejc
 * 
 */
public class AdminJavaStore {

    /**
     * Resets the JavaStore. The stock and the wishList are emptied.
     * 
     */
    public synchronized void resetStore() {
        JavaStore.m_stock = new Stock();
        JavaStore.m_stock.clear();
        JavaStore.m_wishList = new HashMap<String, List<Article>>();
        JavaStore.m_wishList.clear();
        JavaStore.m_buyerLog = new HashMap<String, List<Article>>();
        JavaStore.m_buyerLog.clear();
    }

    /**
     * Adds an item with <code>name</code>, <code>price</code> and
     * <code>stock</code> to the JavaStore stock. If an article already exists
     * only its price and quantity in stock are updated.
     * 
     * @param name
     *            Name of the article to be added to the stock.
     * @param price
     *            Price of the article to be added to the stock.
     * @param quantity
     *            Quantity of the article to be added to the stock.
     */
    public synchronized void addItemToStock(String name, int price, int quantity) {
        JavaStore.m_stock.addArticle(new Article(name, price, quantity));
    }

    /**
     * Returns the buyer log for <code>user</code>. Articles that were bought
     * are sorted descending by the time of purchase.
     * 
     * @param user User whose buyerLog to return.
     *            
     * @return A string of the format 'ItemName, ItemPrice, ItemQuantity' for
     *            each time the item was bought. Returns <code>null</code> if
     *            no items were bought yet.
     */
    public String[] getBuyerLog(String user) {
        List<Article> buyerLog = JavaStore.m_buyerLog.get(user);
        if (buyerLog.size() == 0) {
            return null;
        }
        String[] userLog = new String[buyerLog.size()];
        for (int i = 0; i < userLog.length; i++) {
            userLog[i] = buyerLog.get(i).toString();
        }
        return userLog;
    }

    /**
     * Returns a buyer log summary for all users. Summary is sorted in ascending
     * alphabetical order by the user name.
     * 
     * @return A string of the format 'UserName, NumberOfBoughtArticles,
     *         PriceOfBoughtArticles' for every user. Returns null if no
     *         articles were bought yet.
     */
    public String[] getAllBuyersSummary() {
        Map<String, List<Article>> buyerLogs = JavaStore.m_buyerLog;
        int size = buyerLogs.size();
        if (size == 0) {
            return null;
        }
        String[] buyersSummary = new String[size];
        int pos = 0;
        for (String user : buyerLogs.keySet()) {
            List<Article> articles = buyerLogs.get(user);
            int noOfArticles = 0;
            int priceOfArticles = 0;
            for (Article article : articles) {
                int articleQuantity = article.getQuantity();
                noOfArticles += articleQuantity;
                priceOfArticles += article.getPrice() * articleQuantity;
            }
            buyersSummary[pos] = user + ", " + noOfArticles + ", " + priceOfArticles;
            pos++;
        }
        java.util.Arrays.sort(buyersSummary);
        return buyersSummary;
    }

    /**
     * Returns all items that have a stock of zero.
     * 
     * @return A string of the format 'ItemName, ItemPrice, ItemQuantity' for
     *         each item with stock zero. Returns <code>null</code> if there
     *         are no such items.
     */
    public String[] getItemsWithStockZero() {
        List<Article> articles = new ArrayList<Article>();
        for (Article article : JavaStore.m_stock.getArticles()) {
            if (article.getQuantity() == 0) {
                articles.add(article);
            }
        }
        DotNetStoreB2BSoapProxy dotNetProxy = new DotNetStoreB2BSoapProxy();
        String[] dotNetItems = new String[0];
        try {
            dotNetItems = dotNetProxy.getItemsWithStockZero();
        } catch (RemoteException e) {
        }
        List<String> articlesWithStockZero = new ArrayList<String>();
        for (Article article : articles) {
            if (article == null) {
                continue;
            }
            for (int i = 0; i < dotNetItems.length; i++) {
                if (dotNetItems[i] != null && dotNetItems[i].contains(article.getName())) {
                    articlesWithStockZero.add(article.toString());
                    article = null;
                    dotNetItems[i] = null;
                    break;
                }
            }
        }
        for (Article article : articles) {
            if (article == null) {
                continue;
            }
            boolean articleExistsInDotNet = false;
            try {
                if (dotNetProxy.getItemsWithName(article.getName()) != null) {
                    articleExistsInDotNet = true;
                }
            } catch (RemoteException e) {
            }
            if (!articleExistsInDotNet) {
                articlesWithStockZero.add(article.toString());
                article = null;
            }
        }
        for (String articleInDotNet : dotNetItems) {
            if (articleInDotNet == null) {
                continue;
            }
            String dotNetArticleName = articleInDotNet.substring(0, articleInDotNet.indexOf(","));
            if (JavaStore.m_stock.getArticle(dotNetArticleName) == null) {
                articlesWithStockZero.add(articleInDotNet);
            }
        }
        String[] stockZeroArticles = new String[articlesWithStockZero.size()];
        for (int i = 0; i < articlesWithStockZero.size(); i++) {
            stockZeroArticles[i] = articlesWithStockZero.get(i);
        }
        if (stockZeroArticles.length > 0) {
            return stockZeroArticles;
        }
        return null;
    }

    /**
     * Empty method. Used to destroy all sessions that timed out.
     * 
     */
    public void ping() {
    }
}
