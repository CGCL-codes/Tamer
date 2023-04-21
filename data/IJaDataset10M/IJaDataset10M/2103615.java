package cz.muni.fi.pclis.web.controllers;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;

/**
 * Preparer of main navigator resource bundle key
 * User: Ľuboš Pecho
 * Date: 2.2.2010
 * Time: 22:52:55
 */
public class MainNavigatorActualKeyPreparer implements ViewPreparer {

    private String key;

    public void execute(TilesRequestContext tilesRequestContext, AttributeContext attributeContext) throws PreparerException {
        tilesRequestContext.getRequestScope().put("selectedKey", key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
