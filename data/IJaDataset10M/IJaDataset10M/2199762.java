package sightmusic.work.actions;

import sightmusic.hotbase.HotBaseDetailInterface;
import sightmusic.hotbase.HotBaseManagerTwo;

public class ActionSelectionGenreTwoFirst extends ActionSelectionGenre {

    /**
		 * 
		 */
    private static final long serialVersionUID = 1L;

    public ActionSelectionGenreTwoFirst(HotBaseDetailInterface hotBaseDetail) {
        super(hotBaseDetail);
    }

    public void actionPerformed(String genre) {
        HotBaseManagerTwo.getInstance().setGenreFirst(genre);
    }
}
