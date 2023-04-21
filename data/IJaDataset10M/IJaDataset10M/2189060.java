package net.sf.groofy.match.ui.providers;

import net.sf.groofy.datamodel.Correspondence;

public class GroovesharkArtistViewerColumn extends GroovesharkTracksTableViewerColumn {

    @Override
    public String getText(Object element) {
        return ((Correspondence) element).getArtistName();
    }
}
