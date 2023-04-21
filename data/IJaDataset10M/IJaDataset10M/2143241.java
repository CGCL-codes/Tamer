package ca.llsutherland.squash.domain.helper;

import ca.llsutherland.squash.domain.Administrator;
import ca.llsutherland.squash.domain.Clock;
import ca.llsutherland.squash.domain.NewsItem;
import ca.llsutherland.squash.exceptions.ValidationException;
import ca.llsutherland.squash.utils.ErrorConstants;

public class NewsItemsHelper {

    public static NewsItem createUnpersistedNewsItem(String news, Clock dateCreated, Clock dateUpdated, Clock displayDate, Clock removeDate, Administrator createdBy) {
        NewsItem newsItem = createUnpersistedNewsItem(news, dateCreated, dateUpdated, displayDate, removeDate);
        newsItem.setCreatedBy(createdBy);
        return newsItem;
    }

    private static NewsItem createUnpersistedNewsItem(String news, Clock dateCreated, Clock dateUpdated, Clock displayDate, Clock removeDate) {
        NewsItem item = new NewsItem();
        item.setNews(news);
        item.setDateCreated(dateCreated);
        item.setDateUpdated(dateUpdated);
        item.setDisplayDate(displayDate);
        item.setRemoveDate(removeDate);
        return item;
    }

    public static NewsItem createNewNewsItem(String news, Clock dateCreated, Clock dateUpdated, Clock displayDate, Clock removeDate, Administrator createdBy, Long id) {
        NewsItem newsItem = createUnpersistedNewsItem(news, dateCreated, dateUpdated, displayDate, removeDate, createdBy);
        newsItem.setId(id);
        return newsItem;
    }

    public static void assertNewValid(NewsItem newsItem) {
        DomainObjectHelper.assertNotNullWithErrorMessage(newsItem, ErrorConstants.NULL_NEWS_ITEM_ERROR);
        DomainObjectHelper.assertNonBlankStringWithErrorMessage(newsItem.getNews(), ErrorConstants.BLANK_STRING_ERROR);
        assertNewsLength(newsItem);
        assertAdministratorNotNull(newsItem);
        assertDisplayRemoveDatesValid(newsItem);
    }

    private static void assertDisplayRemoveDatesValid(NewsItem newsItem) {
        if (newsItem.getDisplayDate() == null || newsItem.getRemoveDate() == null) {
            throw new ValidationException(ErrorConstants.NULL_DATE_ERROR);
        }
        if (!newsItem.getDisplayDate().isBefore(newsItem.getRemoveDate())) {
            throw new ValidationException(ErrorConstants.INVALID_NEWS_DATES);
        }
    }

    private static void assertAdministratorNotNull(NewsItem newsItem) {
        if (newsItem.getCreatedBy() == null) {
            throw new ValidationException(ErrorConstants.NULL_ADMINISTRATOR_ERROR);
        }
    }

    private static void assertNewsLength(NewsItem newsItem) {
        if (newsItem.getNews().length() > 99) {
            throw new ValidationException(ErrorConstants.NEWS_TOO_LONG_ERROR);
        }
    }
}
