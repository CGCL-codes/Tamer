package com.dukesoftware.ongakumusou.data.element;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * <p></p>
 * <h5>update history</h5> 
 * <p>2007/06/02 The file was created.</p>
 * 
 * @author 
 * @since 2007/06/02
 * @version last update 2007/06/02
 */
public interface MusicList {

    public MusicElement getCurrent();

    public MusicElement getNext();

    public MusicElement getPrev();

    public boolean contains(MusicElement element);

    public void clear();

    public void addLast(MusicElement element);

    public boolean remove(MusicElement element);

    public Iterator<MusicElement> iterator();

    public int size();

    public void addAll(Collection<MusicElement> c);

    public void setCurrentMusic(MusicElement elem);
}
