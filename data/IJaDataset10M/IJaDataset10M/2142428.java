package net.sourceforge.x360mediaserve.plugins.wicketUI.impl.pages;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.x360mediaserve.api.database.QueryResult;
import net.sourceforge.x360mediaserve.api.database.items.container.ArtistItem;
import net.sourceforge.x360mediaserve.api.database.items.container.PlaylistItem;
import net.sourceforge.x360mediaserve.api.services.DataManager;
import net.sourceforge.x360mediaserve.plugins.wicketUI.impl.wicket.X360MSWicketUIWebSession;
import org.apache.wicket.IClusterable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BannerPanel extends X360MSPanel {

    private static final Logger logger = LoggerFactory.getLogger(BannerPanel.class);

    private static final long serialVersionUID = 1L;

    PlaylistsPage mediaPage;

    public BannerPanel(String id, PlaylistsPage mediaPage, String playlistId) {
        super(id);
        setOutputMarkupId(true);
        this.mediaPage = mediaPage;
        add(newDropDown(playlistId));
        add(newPlaylistForm());
    }

    private static class FormBean implements IClusterable {

        private String playlistName;

        public String getPlaylistName() {
            return playlistName;
        }

        public void setPlaylistName(String playlistName) {
            this.playlistName = playlistName;
        }
    }

    private Form newPlaylistForm() {
        final FormBean bean = new FormBean();
        final Form playlistForm = new Form("playlist_add_form", new CompoundPropertyModel<FormBean>(bean));
        playlistForm.setOutputMarkupId(true);
        final TextField<String> textField = new TextField<String>("playlistName");
        playlistForm.add(textField);
        AjaxButton button = new AjaxButton("playlist_add_button") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                String name = textField.getValue();
                if (name != null && name.length() > 0) {
                    QueryResult newPlaylistResult = getDataManager().addPlaylist(name);
                    String newId = newPlaylistResult.getPrefix() + "/" + ((PlaylistItem) newPlaylistResult.getResult().get(0)).getId();
                    Form newForm = newDropDown(newId);
                    BannerPanel.this.replace(newForm);
                    target.addComponent(newForm);
                    Form newPlaylistForm = newPlaylistForm();
                    playlistForm.replaceWith(newPlaylistForm);
                    target.addComponent(newPlaylistForm);
                    mediaPage.setPlaylist(newId, target);
                }
            }
        };
        playlistForm.add(button);
        return playlistForm;
    }

    private Form newDropDown(String selectedItemId) {
        Form form = new Form("playlist_select_form");
        form.setOutputMarkupId(true);
        DataManager db = getDataManager();
        QueryResult<PlaylistItem> playlists = db != null ? db.getPlaylists(0, 1000, null) : null;
        final List<String> lst = new ArrayList<String>();
        final List<String> ids = new ArrayList<String>();
        String defaultModel = null;
        if (playlists != null) for (PlaylistItem playlist : playlists.getResult()) {
            String id = playlists.getPrefix() + "/" + playlist.getId();
            if (id.equals(selectedItemId)) defaultModel = playlist.getName();
            lst.add(playlist.getName());
            ids.add(id);
        }
        final DropDownChoice playlistSelect = new DropDownChoice("playlist_select", new Model(), lst);
        playlistSelect.setOutputMarkupId(true);
        playlistSelect.setNullValid((lst.size() == 0));
        if (lst.size() > 0) {
            playlistSelect.setDefaultModelObject(defaultModel);
        }
        playlistSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                logger.info("Got Object:{}", playlistSelect.getModelObject());
                int index = Integer.parseInt(playlistSelect.getModelValue());
                if (index != -1) {
                    logger.info("Using playlist:{}", lst.get(index));
                    mediaPage.setPlaylist(ids.get(index), target);
                }
            }
        });
        form.add(playlistSelect);
        return form;
    }
}
