package net.mp3spider.search.event.search;

import java.util.EventListener;

/**
 *
 * @author Esteban Fuentealba
 */
public interface Mp3SpiderSearchCompleteListener extends EventListener {

    void onSearchComplete(Mp3SpiderSearchCompleteEvent evt);
}
