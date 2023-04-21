package com.umc.beans.media;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import com.umc.beans.BackdropImage;
import com.umc.beans.BannerImage;
import com.umc.beans.CoverImage;
import com.umc.beans.PosterImage;
import com.umc.beans.Warning;
import com.umc.gui.GuiController;

/**
 * 
 * @author DonGyros
 *
 */
public class MovieGroup extends MediaBase {

    /**Der berechnete/bereinigte Titel aus dem original Dateinamen*/
    private String computedTitle = null;

    /**Cover object*/
    private CoverImage cover = null;

    /**Poster Object*/
    private PosterImage poster = null;

    /**Backdrop Object*/
    private BackdropImage backdrop = null;

    /**Banner object*/
    private BannerImage banner = null;

    /**List of possible childs*/
    private Collection<Movie> childs = new ArrayList<Movie>();

    /**Flag which is set to true if the movie has been set to 'watched' by the user*/
    private boolean isWatched = false;

    /**Warnings*/
    private Collection<Warning> warnings = new ArrayList<Warning>();

    public String getComputedTitel() {
        return this.computedTitle;
    }

    public void setComputedTitel(String computedTitle) {
        this.computedTitle = computedTitle;
    }

    public Collection<Movie> getChilds() {
        return childs;
    }

    public void addChild(Movie im) {
        this.childs.add(im);
    }

    public CoverImage getCover() {
        return cover;
    }

    public void setCover(CoverImage cover) {
        this.cover = cover;
    }

    public BackdropImage getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(BackdropImage backdrop) {
        this.backdrop = backdrop;
    }

    public PosterImage getPoster() {
        return poster;
    }

    public void setPoster(PosterImage poster) {
        this.poster = poster;
    }

    /**
	 * Checks if this moviegroup is watched.
	 * 
	 * @return the isWatched
	 */
    public boolean isWatched() {
        return isWatched;
    }

    /**
	 * If set to true the moviegroup will be set to 'watched'.
	 * 
	 * @param isWatched the isWatched to set
	 */
    public void setWatched(boolean isWatched) {
        this.isWatched = isWatched;
    }

    @Override
    public String toString() {
        String lng = GuiController.getInstance().getSelectedScanLanguage();
        if (StringUtils.isNotEmpty(getLanguageData().getTitle(lng))) return getLanguageData().getTitle(lng);
        return "[No group title found]";
    }
}
