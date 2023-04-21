package com.monad.homerun.view;

import java.util.ArrayList;
import java.util.List;
import com.monad.homerun.base.DataObject;

/**
 * Album is an assemblage of scenes - intended to be viewed in a time-sequence
 * as in a slide-show, or manually traversed with user-controls.
 */
public class Album extends DataObject {

    private static final long serialVersionUID = 6454087733799455446L;

    private ArrayList<Selector> sceneList = null;

    private int spaceSecs = 0;

    public Album() {
        sceneList = new ArrayList<Selector>();
        spaceSecs = 5;
    }

    public Album(String name) {
        super(name);
        sceneList = new ArrayList<Selector>();
        spaceSecs = 5;
    }

    public Album(String name, Album album) {
        this(name);
        for (Selector sel : album.sceneList) {
            sceneList.add(sel);
        }
    }

    /**
	 * Returns the number of seconds to display a scene if in
	 * 'slide-show' mode
	 * 
	 * @return time
	 *         the number of seconds to show each scene
	 */
    public int getInterval() {
        return spaceSecs;
    }

    public void setInterval(int secs) {
        spaceSecs = secs;
    }

    public int getNumScenes() {
        return getNumSelectors();
    }

    public int getNumSelectors() {
        return sceneList.size();
    }

    public String[] getSceneNames(String category) {
        List<String> matches = new ArrayList<String>();
        for (Selector sel : sceneList) {
            if (sel.getCategory().equals(category)) {
                matches.add(sel.getSceneName());
            }
        }
        return matches.toArray(new String[0]);
    }

    public String[] getSceneNames() {
        List<String> matches = new ArrayList<String>();
        for (Selector sel : sceneList) {
            if (sel.getCategory().equals(getName()) || sel.getCategory().equals(Scene.FREE)) {
                matches.add(sel.getSceneName());
            }
        }
        return matches.toArray(new String[0]);
    }

    public void addSelector(Selector selector) {
        sceneList.add(selector);
    }

    public void removeSelector(String category, String sceneName) {
        for (Selector sel : sceneList) {
            if (sel.getCategory().equals(category) && sel.getSceneName().equals(sceneName)) {
                sceneList.remove(sel);
                break;
            }
        }
    }
}
