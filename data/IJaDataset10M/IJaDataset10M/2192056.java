package audictiv.client.ui.website.panels.search;

import java.util.ArrayList;
import audictiv.client.services.ServicesManager;
import audictiv.shared.Album;
import audictiv.shared.Artist;
import audictiv.shared.Band;
import audictiv.shared.Song;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Hyperlink;

public class SearchModule extends DecoratedTabPanel {

    HTMLTable r1, r2, r3, r4;

    public SearchModule(String searchText) {
        initializeComponents();
        setProperties();
        setStyles();
        search(searchText);
        buildPanel();
        this.selectTab(0);
    }

    private void initializeComponents() {
        r1 = new FlexTable();
        r2 = new FlexTable();
        r3 = new FlexTable();
        r4 = new FlexTable();
        r1.setText(0, 0, "SONG TITLE");
        r1.setText(0, 1, "ARTIST/BAND NAME");
        r1.setText(0, 2, "DATE OF UPLOAD");
        r2.setText(0, 0, "ALBUM TITLE");
        r2.setText(0, 1, "ARTIST/BAND NAME");
        r2.setText(0, 2, "NUMBER OF TRACKS");
        r2.setText(0, 3, "DATE OF UPLOAD");
        r3.setText(0, 0, "ARTIST NAME");
        r3.setText(0, 1, "DATE OF REGISTRATION");
        r4.setText(0, 0, "BAND NAME");
        r4.setText(0, 1, "NUMBER OF ARTISTS");
        r4.setText(0, 2, "DATE OF REGiSTRATION");
    }

    private void setProperties() {
        this.setAnimationEnabled(true);
    }

    private void setStyles() {
        this.setStyleName("searchModule");
        r1.setStyleName("table");
        r2.setStyleName("table");
        r3.setStyleName("table");
        r4.setStyleName("table");
    }

    private void buildPanel() {
        this.add(r1, "Songs");
        this.add(r2, "Albums");
        this.add(r3, "Artists");
        this.add(r4, "Bands");
    }

    private void search(String searchText) {
        ServicesManager.getInstance().searchService().getSearchResultSong(searchText, new AsyncCallback<ArrayList<Song>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error!");
            }

            @Override
            public void onSuccess(ArrayList<Song> result) {
                if (result.size() == 0) {
                    r1.clear(true);
                    r1.setText(0, 0, "No records associated to songs matched! Plese try with other keywords!");
                }
                for (int i = 0; i < result.size(); i++) {
                    Hyperlink link0 = new Hyperlink(result.get(i).getTitle(), "viewDetails?S=" + String.valueOf(result.get(i).getAcid()));
                    r1.setWidget(i + 1, 0, link0);
                    Hyperlink link1 = new Hyperlink(result.get(i).getComposerName(), "viewProfile?C=" + String.valueOf(result.get(i).getCid()));
                    r1.setWidget(i + 1, 1, link1);
                    r1.setText(i + 1, 2, result.get(i).getUploadDate().toString());
                }
            }
        });
        ServicesManager.getInstance().searchService().getSearchResultAlbum(searchText, new AsyncCallback<ArrayList<Album>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error!");
            }

            @Override
            public void onSuccess(ArrayList<Album> result) {
                if (result.size() == 0) {
                    r2.clear(true);
                    r2.setText(0, 0, "No records associated to albums matched! Plese try with other keywords!");
                }
                for (int i = 0; i < result.size(); i++) {
                    Hyperlink link0 = new Hyperlink(result.get(i).getTitle(), "viewDetails?AL=" + String.valueOf(result.get(i).getAcid()));
                    r2.setWidget(i + 1, 0, link0);
                    Hyperlink link1 = new Hyperlink(result.get(i).getComposerName(), "viewProfile?C=" + String.valueOf(result.get(i).getCid()));
                    r2.setWidget(i + 1, 1, link1);
                    r2.setText(i + 1, 2, String.valueOf(result.get(i).getNumberOfTracks()));
                    r2.setText(i + 1, 3, result.get(i).getUploadDate().toString());
                }
            }
        });
        ServicesManager.getInstance().searchService().getSearchResultArtist(searchText, new AsyncCallback<ArrayList<Artist>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error!");
            }

            @Override
            public void onSuccess(ArrayList<Artist> result) {
                if (result.size() == 0) {
                    r3.clear(true);
                    r3.setText(0, 0, "No records associated to artists matched! Plese try with other keywords!");
                }
                for (int i = 0; i < result.size(); i++) {
                    Hyperlink link0 = new Hyperlink(result.get(i).getfName() + " " + result.get(i).getsName(), "viewProfile?C=" + String.valueOf(result.get(i).getID()));
                    r3.setWidget(i + 1, 0, link0);
                    r3.setText(i + 1, 1, result.get(i).getSubscriptionDate().toString());
                }
            }
        });
        ServicesManager.getInstance().searchService().getSearchResultBand(searchText, new AsyncCallback<ArrayList<Band>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error!");
            }

            @Override
            public void onSuccess(ArrayList<Band> result) {
                if (result.size() == 0) {
                    r4.clear(true);
                    r4.setText(0, 0, "No records associated to bands matched! Plese try with other keywords!");
                }
                for (int i = 0; i < result.size(); i++) {
                    Hyperlink link0 = new Hyperlink(result.get(i).getName(), "viewProfile?C=" + String.valueOf(result.get(i).getID()));
                    r4.setWidget(i + 1, 0, link0);
                    r4.setText(i + 1, 1, String.valueOf(result.get(i).getNumOfPeople()));
                    r4.setText(i + 1, 2, result.get(i).getSubscriptionDate().toString());
                }
            }
        });
    }
}
