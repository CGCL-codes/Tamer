package com.ynhenc.kml;

import java.util.Iterator;
import com.ynhenc.comm.ArrayListEx;

public class KmlFolder extends KmlObject {

    public KmlPlacemarkIterator getPlacemarkIterator() {
        return placemarkList;
    }

    @Override
    public String getTag() {
        return "Folder";
    }

    public KmlFolder(String name, String desc, ArrayListEx<KmlPlacemark> placemarkList) {
        this(name, desc, createKmlPlacemarkIterator(placemarkList));
    }

    public KmlFolder(String name, String desc, KmlPlacemarkIterator placemarkList) {
        super(name, desc);
        this.placemarkList = placemarkList;
    }

    private static KmlPlacemarkIterator createKmlPlacemarkIterator(ArrayListEx<KmlPlacemark> pmList) {
        final Iterator<KmlPlacemark> pmListIt = pmList.iterator();
        KmlPlacemarkIterator placemarkIterator = new KmlPlacemarkIterator() {

            @Override
            public boolean hasNext() {
                return pmListIt.hasNext();
            }

            @Override
            public KmlPlacemark next() {
                return pmListIt.next();
            }

            @Override
            public void remove() {
                pmListIt.remove();
            }
        };
        return placemarkIterator;
    }

    private KmlPlacemarkIterator placemarkList;
}
