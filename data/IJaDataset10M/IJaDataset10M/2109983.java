package de.beas.explicanto.types;

import java.util.List;

/**
 * @author AlexanderS
 *
 * WSPage: 		real datastructure with regions as children. Transmitted via webservice.
 * WSPageBlob: 	datastructure with xml clob instead of region as children. Stored in DB.
 * WSPageHead:	common header (title, height, width, ...) of both.
 * 
 */
public class WSPage extends WSPageHead {

    private int _PageWidth = 0;

    private int _PageHeight = 0;

    private boolean loaded = true;

    public WSPage() {
    }

    public WSPage(WSPageHead head) {
        this();
        setTitle(head.getTitle());
        setNextuid(head.getNextuid());
        setUid(head.getUid());
        noWSsetParentUID(head.noWSgetParentUID());
        setPageElementID(head.getPageElementID());
    }

    public WSPage(long parentUID, int position, String layout, String title, int width, int height, List regions, int nextuid, long pageElementID) {
        super(parentUID, position, title, nextuid, pageElementID);
        setPageLayoutType(layout);
        setTitle(title);
        setPageWidth(width);
        setPageHeight(height);
        setChildren(regions);
    }

    public int getPageHeight() {
        return _PageHeight;
    }

    public int getPageWidth() {
        return _PageWidth;
    }

    public void setPageHeight(int i) {
        _PageHeight = i;
    }

    public void setPageWidth(int i) {
        _PageWidth = i;
    }

    /**
	 * @return
	 */
    public boolean isLoaded() {
        return loaded;
    }

    /**
	 * @param b
	 */
    public void setLoaded(boolean b) {
        loaded = b;
    }

    public WSPage copyTree() {
        WSPage pg = NewJAXBConverter.convertPage(NewJAXBConverter.convertPage(this));
        pg.setPageLayoutType(getPageLayoutType());
        pg.resetIDs();
        return pg;
    }
}
