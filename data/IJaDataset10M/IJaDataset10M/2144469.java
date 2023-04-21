package edu.umich.marketplace.woc;

import org.apache.log4j.Logger;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSMutableArray;
import edu.umich.marketplace.EndCategoryMessages;
import edu.umich.marketplace.eof.Advert;
import edu.umich.marketplace.eof.ApplicationModel;
import edu.umich.marketplace.eof.Category;
import edu.umich.marketplace.eof.UserSessionModel;

public class CategoryMessages extends MPComponent {

    private static final Logger logger = Logger.getLogger(CategoryMessages.class);

    public Category thisCategory;

    public NSMutableArray<Category> categoriesInAdverts = new NSMutableArray<Category>();

    private final ApplicationModel _aModel = app.getApplicationModel();

    private final UserSessionModel _sModel = sess.getUserSessionModel();

    private final EndCategoryMessages _endCat = _sModel.getEndCategoryMessages();

    public CategoryMessages(WOContext context) {
        super(context);
        logger.trace("+++ constructor");
    }

    public boolean anyMessagesForActiveAdverts() {
        for (final Advert advert : _aModel.getActiveAdverts()) {
            if (_endCat.isMessageForCategory(advert.category())) {
                if (!categoriesInAdverts.containsObject(advert.category())) {
                    categoriesInAdverts.add(advert.category());
                }
            }
        }
        return categoriesInAdverts.count() > 0;
    }

    public String getMessageTitleForCategory() {
        return _endCat.getMessageTitleForCategory(thisCategory);
    }

    public String getMessageTextForCategory() {
        return _endCat.getMessageTextForCategory(thisCategory);
    }

    public String getMessageStyleForCategory() {
        return _endCat.getMessageStyleForCategory(thisCategory);
    }
}
