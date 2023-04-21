package query.results;

import message.MessageId;
import model.stock.StockDropOut;
import query.framework.results.LazySearchResultsSpecification;

public class StockDropOutSearchResultsSpecification extends LazySearchResultsSpecification {

    public StockDropOutSearchResultsSpecification() {
        add(MessageId.date);
        add(MessageId.article);
        add(MessageId.count);
        add(MessageId.unitCost);
    }

    public Object value(Object object, int columnIndex) {
        StockDropOut dropOut = (StockDropOut) object;
        switch(columnIndex) {
            case 0:
                return dropOut.getDate();
            case 1:
                return dropOut.getArticle();
            case 2:
                return dropOut.getCount();
            case 3:
                return dropOut.getUnitCost();
        }
        return null;
    }
}
