package query.results;

import message.MessageId;
import model.expense.ExpenseArticle;
import query.framework.results.LazySearchResultsSpecification;

public class ExpenseArticleSearchResultsSpecification extends LazySearchResultsSpecification {

    public ExpenseArticleSearchResultsSpecification() {
        add(MessageId.name);
    }

    public Object value(Object object, int columnIndex) {
        ExpenseArticle article = (ExpenseArticle) object;
        switch(columnIndex) {
            case 0:
                return article.getName();
        }
        return null;
    }
}
