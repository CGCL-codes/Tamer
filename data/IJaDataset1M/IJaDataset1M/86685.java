package org.webstrips.gui;

import org.webstrips.core.ComicDriver;

public interface ComicListListener {

    public void comicAdded(ComicList source, ComicDriver newComic);

    public void comicRemoved(ComicList source, ComicDriver removedComic);

    public void comicReloaded(ComicList source, ComicDriver removedComic, ComicDriver newComic);

    public void comicSelected(ComicList source, ComicDriver comic);
}
