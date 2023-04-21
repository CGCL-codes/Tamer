package query.framework.query;

import java.util.HashMap;
import java.util.Map;
import model.money.MoneyAmount;
import model.receipt.Sell;
import model.receipt.SellItem;
import model.stock.Article;
import model.stock.StockDropOut;
import query.QueryFactory;
import query.criteria.IntervalSearchCriteria;
import query.criteria.StockArticleSearchCriteria;
import query.framework.criteria.Criteria;
import query.framework.results.DefaultLazySearchResults;
import query.framework.results.SearchResults;
import query.results.StockAnalysisResultsSpecification;

public class StockAnalysisSearchQuery implements SearchQuery {

    public static class ArticleCalculation {

        private MoneyAmount sellValueTotal = MoneyAmount.zero();

        private MoneyAmount costValueTotal = MoneyAmount.zero();

        private MoneyAmount dropOutCostTotal = MoneyAmount.zero();

        private int sellCount = 0;

        public void addSellValue(MoneyAmount sellValue) {
            sellValueTotal = sellValueTotal.plus(sellValue);
        }

        public void addCostValue(MoneyAmount costValue) {
            costValueTotal = costValueTotal.plus(costValue);
        }

        public void addDropOut(MoneyAmount cost) {
            dropOutCostTotal = dropOutCostTotal.plus(cost);
        }

        public void addSellCount(double count) {
            sellCount += count;
        }

        public MoneyAmount sellValueTotal() {
            return sellValueTotal;
        }

        public MoneyAmount costValueTotal() {
            return costValueTotal;
        }

        public MoneyAmount dropOutCostTotal() {
            return dropOutCostTotal;
        }

        public int sellCount() {
            return sellCount;
        }
    }

    private IntervalSearchCriteria intervalCriteria;

    public SearchResults results() {
        Iterable<Article> stockArticles = articles();
        Iterable<Sell> sells = sells();
        Iterable<StockDropOut> dropOuts = dropOuts();
        Map<Article, ArticleCalculation> calculations = new HashMap();
        DefaultLazySearchResults results = new DefaultLazySearchResults(new StockAnalysisResultsSpecification(calculations));
        for (Article article : stockArticles) {
            calculations.put(article, new ArticleCalculation());
            results.add(article);
        }
        for (Sell sell : sells) {
            for (SellItem sellItem : sell.items()) {
                Article article = sellItem.getArticle();
                ArticleCalculation calculation = calculations.get(article);
                if (calculation != null) {
                    calculation.addSellValue(sellItem.getSellValue().by(sellItem.getCount()));
                    calculation.addCostValue(sellItem.getCostValue().by(sellItem.getCount()));
                    calculation.addSellCount(sellItem.getCount());
                }
            }
        }
        for (StockDropOut stockDropOut : dropOuts) {
            ArticleCalculation calculation = calculations.get(stockDropOut.getArticle());
            if (calculation != null) {
                calculation.addDropOut(stockDropOut.getTotalCost());
            }
        }
        return results;
    }

    private Iterable<Article> articles() {
        SearchQuery stockArticleSearchQuery = QueryFactory.instance().stockArticleSearchQuery();
        stockArticleSearchQuery.setCriteria(new StockArticleSearchCriteria() {

            public String getArticleName() {
                return "";
            }
        });
        return stockArticleSearchQuery.results();
    }

    private Iterable<Sell> sells() {
        SearchQuery sellSearchQuery = QueryFactory.instance().sellSearchQuery();
        sellSearchQuery.setCriteria(intervalCriteria);
        return sellSearchQuery.results();
    }

    private Iterable<StockDropOut> dropOuts() {
        SearchQuery dropOutsQuery = QueryFactory.instance().stockDropOutSearchQuery();
        dropOutsQuery.setCriteria(intervalCriteria);
        return dropOutsQuery.results();
    }

    public void setCriteria(Criteria criteria) {
        this.intervalCriteria = (IntervalSearchCriteria) criteria;
    }
}
