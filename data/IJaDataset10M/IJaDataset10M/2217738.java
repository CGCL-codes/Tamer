package org.kommando.web.firefox.objectsource;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import org.kommando.application.core.Application;
import org.kommando.core.catalog.AbstractObjectSource;
import org.kommando.core.catalog.CatalogObject;
import org.kommando.core.catalog.ObjectSource;
import org.kommando.core.catalog.SimpleCatalogObject;
import org.kommando.core.util.Assert;
import org.kommando.web.WebPage;
import org.kommando.web.firefox.Firefox;

/**
 * {@link ObjectSource} for the firefox plugin.
 * 
 * @author Peter De Bruycker
 */
public class FirefoxObjectSource extends AbstractObjectSource {

    private static final String ID = "web:firefox";

    private static final String BOOKMARK_ID_PREFIX = "web:firefox:bookmark:";

    private Firefox firefox;

    public FirefoxObjectSource(Firefox firefox) {
        Assert.argumentNotNull("firefox", firefox);
        this.firefox = firefox;
    }

    @Override
    public Class<?>[] getExtensionTypes(CatalogObject object) {
        if (object.hasExtension(Application.class) && object.getExtension(Application.class).getName().equals("Firefox")) {
            return new Class<?>[] { Firefox.class };
        }
        return null;
    }

    @Override
    public <T> T getExtension(CatalogObject object, Class<T> extensionType) {
        if (extensionType.equals(Firefox.class)) {
            return extensionType.cast(firefox);
        }
        return null;
    }

    @Override
    public List<CatalogObject> getObjects() {
        List<CatalogObject> objects = new ArrayList<CatalogObject>();
        return objects;
    }

    @Override
    public CatalogObject getObject(String id) {
        if (id.startsWith(BOOKMARK_ID_PREFIX)) {
            String url = id.substring(BOOKMARK_ID_PREFIX.length());
            for (WebPage bookmark : firefox.getBookmarks()) {
                if (url.equals(bookmark.getUrl())) {
                    return createWebPageCatalogObject(bookmark);
                }
            }
        }
        return null;
    }

    @Override
    public List<CatalogObject> getChildren(CatalogObject object) {
        if (object.hasExtension(Firefox.class)) {
            List<CatalogObject> bookmarkObjects = new ArrayList<CatalogObject>();
            List<WebPage> bookmarks = object.getExtension(Firefox.class).getBookmarks();
            for (WebPage bookmark : bookmarks) {
                bookmarkObjects.add(createWebPageCatalogObject(bookmark));
            }
            return bookmarkObjects;
        }
        return null;
    }

    private CatalogObject createWebPageCatalogObject(WebPage webPage) {
        SimpleCatalogObject webPageObject = new SimpleCatalogObject(BOOKMARK_ID_PREFIX + webPage.getUrl(), webPage.getTitle(), webPage.getUrl(), webPage.getIcon());
        webPageObject.setExtension(WebPage.class, webPage);
        return webPageObject;
    }

    @Override
    public boolean hasChildren(CatalogObject object) {
        return object.hasExtension(Firefox.class);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDescription() {
        return "TODO";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getName() {
        return "Firefox";
    }
}
