package de.umass.lastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import de.umass.xml.DomElement;

/**
 * This utility class can be used to generically generate Result objects (usually Lists or {@link PaginatedResult}s) from an XML response
 * using {@link ItemFactory ItemFactories}.
 *
 * @author Janni Kovacs
 */
final class ResponseBuilder {

    private ResponseBuilder() {
    }

    private static <T> ItemFactory<T> getItemFactory(Class<T> itemClass) {
        return ItemFactoryBuilder.getFactoryBuilder().getItemFactory(itemClass);
    }

    public static <T> Collection<T> buildCollection(Result result, Class<T> itemClass) {
        return buildCollection(result, getItemFactory(itemClass));
    }

    public static <T> Collection<T> buildCollection(Result result, ItemFactory<T> factory) {
        if (!result.isSuccessful()) return Collections.emptyList();
        return buildCollection(result.getContentElement(), factory);
    }

    public static <T> Collection<T> buildCollection(DomElement element, Class<T> itemClass) {
        return buildCollection(element, getItemFactory(itemClass));
    }

    public static <T> Collection<T> buildCollection(DomElement element, ItemFactory<T> factory) {
        if (element == null) return Collections.emptyList();
        Collection<DomElement> children = element.getChildren();
        Collection<T> items = new ArrayList<T>(children.size());
        for (DomElement child : children) {
            items.add(factory.createItemFromElement(child));
        }
        return items;
    }

    public static <T> PaginatedResult<T> buildPaginatedResult(Result result, Class<T> itemClass) {
        return buildPaginatedResult(result, getItemFactory(itemClass));
    }

    public static <T> PaginatedResult<T> buildPaginatedResult(Result result, ItemFactory<T> factory) {
        if (!result.isSuccessful()) {
            return new PaginatedResult<T>(0, 0, Collections.<T>emptyList());
        }
        DomElement contentElement = result.getContentElement();
        Collection<DomElement> children = contentElement.getChildren();
        Collection<T> items = new ArrayList<T>(children.size());
        for (DomElement e : children) {
            items.add(factory.createItemFromElement(e));
        }
        int page = Integer.valueOf(contentElement.getAttribute("page"));
        String totalPagesAttribute = contentElement.getAttribute("totalPages");
        if (totalPagesAttribute == null) totalPagesAttribute = contentElement.getAttribute("totalpages");
        int totalPages = Integer.valueOf(totalPagesAttribute);
        return new PaginatedResult<T>(page, totalPages, items);
    }

    public static <T> T buildItem(Result result, Class<T> itemClass) {
        return buildItem(result, getItemFactory(itemClass));
    }

    public static <T> T buildItem(Result result, ItemFactory<T> factory) {
        if (!result.isSuccessful()) return null;
        return buildItem(result.getContentElement(), factory);
    }

    public static <T> T buildItem(DomElement element, Class<T> itemClass) {
        return buildItem(element, getItemFactory(itemClass));
    }

    private static <T> T buildItem(DomElement element, ItemFactory<T> factory) {
        return factory.createItemFromElement(element);
    }
}
