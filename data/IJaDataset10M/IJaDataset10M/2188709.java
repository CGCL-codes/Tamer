package com.ingenta.clownbike;

import java.util.List;

public class DiscussionMessagesPageFactory {

    private DiscussionMessageItemizedFinder _finder;

    private PageFactory _idsPageFactory;

    public DiscussionMessagesPageFactory(DiscussionMessageItemizedFinder finder, int hitsPerPage) {
        _finder = finder;
        _idsPageFactory = new PageFactory(_finder.getIds(), hitsPerPage);
    }

    public Page create(DatabaseTransaction transaction, int pageNumber) throws DatabaseException {
        Page idsPage = _idsPageFactory.create(pageNumber);
        if (idsPage != null) {
            List messages = _finder.findByIds(transaction, idsPage.getContents());
            Page messagesPage = new Page(idsPage.getPageCount(), idsPage.getPageNumber(), messages);
            return messagesPage;
        }
        return null;
    }

    public Page createBefore(DatabaseTransaction transaction, Page page) throws DatabaseException {
        return create(transaction, page.getPageNumber() - 1);
    }

    public Page createAfter(DatabaseTransaction transaction, Page page) throws DatabaseException {
        return create(transaction, page.getPageNumber() + 1);
    }
}
