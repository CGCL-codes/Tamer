package com.umc.beans.media;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author DonGyros
 *
 */
public class PhotoAlbum extends MediaBase {

    /**Der berechnete/bereinigte Titel aus dem original Dateinamen*/
    private String computedTitle = null;

    /**List of possible childs*/
    private Collection<Photo> childs = new ArrayList<Photo>();

    private String title = null;

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getComputedTitel() {
        return this.computedTitle;
    }

    public void setComputedTitel(String computedTitle) {
        this.computedTitle = computedTitle;
    }

    public Collection<Photo> getChilds() {
        return childs;
    }

    public void addChild(Photo m) {
        this.childs.add(m);
    }
}
