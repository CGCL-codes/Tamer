package com.googlecode.wicket.soundmanager.muxtape;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import com.googlecode.wicket.soundmanager.SoundManagerPanel;

/**
 * @author ildella
 */
public abstract class PlaylistContainer extends SoundManagerPanel {

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference(new PackageResourceReference(MuxTapeDemoPage.class, "css/demo.css"));
        response.renderCSSReference(new PackageResourceReference(MuxTapeDemoPage.class, "css/optional-annotations.css"));
        response.renderCSSReference(new PackageResourceReference(MuxTapeDemoPage.class, "css/optional-themes.css"));
        response.renderCSSReference(new PackageResourceReference(MuxTapeDemoPage.class, "css/page-player.css"));
        response.renderJavascriptReference(new PackageResourceReference(MuxTapeDemoPage.class, "script/page-player.js"));
        response.renderJavascriptReference(new PackageResourceReference(MuxTapeDemoPage.class, "script/optional-page-player-metadata.js"));
    }

    public PlaylistContainer(final String id) {
        super(id);
        add(createPlaylistPanel("playlist"));
    }

    protected abstract Component createPlaylistPanel(String componentId);
}
