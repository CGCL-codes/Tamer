package com.abso.mp3tunes.locker.ui.properties;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import com.abso.mp3tunes.locker.core.data.Album;
import com.abso.mp3tunes.locker.core.data.LockerDataUtils;

/**
 * A specialized section showing the properties of an album.
 */
public class AlbumPropertySection extends AbstractLockerPropertySection {

    /** The text control used to show the album's title. */
    private Text titleText;

    /** The text control used to show the artist's name. */
    private Text artistNameText;

    /** The text control used to show the number of tracks. */
    private Text trackCountText;

    /** The text control used to show the total size. */
    private Text sizeText;

    /** The text control used to show the release date. */
    private Text releaseDateText;

    /** The text control used to show the purchase date. */
    private Text purchaseDateText;

    /** The text control used to show the art status code. */
    private Text hasArtText;

    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite mainComposite = createMainControl(parent);
        createCLabel(mainComposite, "Title:");
        titleText = createText(mainComposite, false);
        createCLabel(mainComposite, "Artist:");
        artistNameText = createText(mainComposite, false);
        createCLabel(mainComposite, "Nr. of Tracks:");
        trackCountText = createText(mainComposite, false);
        createCLabel(mainComposite, "Size:");
        sizeText = createText(mainComposite, false);
        createCLabel(mainComposite, "Relase Date:");
        releaseDateText = createText(mainComposite, false);
        createCLabel(mainComposite, "Purchase Date:");
        purchaseDateText = createText(mainComposite, false);
        createCLabel(mainComposite, "Art:");
        hasArtText = createText(mainComposite, false);
    }

    public void refresh() {
        List elems = getSelectedElements();
        if (elems.size() == 1) {
            Album album = (Album) elems.get(0);
            titleText.setText(album.getTitle());
            artistNameText.setText(album.getArtistName());
            trackCountText.setText(Integer.toString(album.getTrackCount()));
            sizeText.setText(LockerDataUtils.getPrettyPrintedSize(album.getSize()));
            releaseDateText.setText(ObjectUtils.toString(album.getReleaseDate(), "-"));
            purchaseDateText.setText(ObjectUtils.toString(album.getPurchaseDate(), "-"));
            hasArtText.setText(Integer.toString(album.getHasArt()));
        } else {
            Set artistNames = new HashSet();
            int trackCount = 0;
            long size = 0L;
            for (Iterator i = elems.iterator(); i.hasNext(); ) {
                Album album = (Album) i.next();
                artistNames.add(album.getArtistName());
                trackCount += album.getTrackCount();
                size += album.getSize();
            }
            titleText.setText(elems.size() + " albums");
            if (artistNames.size() == 1) {
                artistNameText.setText((String) artistNames.iterator().next());
            } else {
                artistNameText.setText(artistNames.size() + " artists");
            }
            trackCountText.setText(Integer.toString(trackCount));
            sizeText.setText(LockerDataUtils.getPrettyPrintedSize(size));
            releaseDateText.setText("-");
            purchaseDateText.setText("-");
            hasArtText.setText("-");
        }
    }
}
