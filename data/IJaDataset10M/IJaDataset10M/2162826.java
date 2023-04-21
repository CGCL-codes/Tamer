package uk.gov.dti.og.fox.dom;

import uk.gov.dti.og.fox.track.Track;

public class TextXMLFragment extends Track {

    private final String mXMLFragment;

    public TextXMLFragment(String pXMLFragment) {
        mXMLFragment = pXMLFragment;
    }

    public String toString() {
        return mXMLFragment;
    }
}
