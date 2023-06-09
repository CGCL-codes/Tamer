package fitnesse.wiki;

import java.util.*;

public abstract class InheritedItemBuilder {

    protected List getInheritedItems(WikiPage page, Set visitedPages) throws Exception {
        List items = new ArrayList();
        addItemsFromPage(page, items);
        List ancestors = WikiPageUtil.getAncestorsOf(page);
        for (Iterator iterator = ancestors.iterator(); iterator.hasNext(); ) {
            WikiPage ancestor = (WikiPage) iterator.next();
            if (!visitedPages.contains(ancestor)) {
                visitedPages.add(ancestor);
                addItemsFromPage(ancestor, items);
            }
        }
        return items;
    }

    private void addItemsFromPage(WikiPage itemPage, List items) throws Exception {
        List itemsOnThisPage = getItemsFromPage(itemPage);
        items.addAll(itemsOnThisPage);
    }

    protected abstract List getItemsFromPage(WikiPage page) throws Exception;
}
