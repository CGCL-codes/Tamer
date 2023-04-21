package com.example.possessed;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import tony.db.DBUtil;
import com.tony.fbqueries.FBPhoto;
import com.tony.fbqueries.FBPhotoAlbum;
import com.tony.fbqueries.FBQueries;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

public class FixPictures extends VerticalLayout implements ValueChangeListener, Button.ClickListener, Serializable {

    private GridLayout photospanel = null;

    private boolean built = false;

    private Map<String, Long> pictureids = new HashMap<String, Long>();

    private NativeSelect albumSelector = new NativeSelect("Album");

    private Button nextButton = new Button("Next >");

    private long picUid;

    private Layout returnLayout;

    public FixPictures(long uid, Layout returnlayout) {
        this.returnLayout = returnlayout;
        this.picUid = uid;
    }

    @Override
    public void attach() {
        photospanel = new GridLayout(4, 30);
        photospanel.setStyleName("picchooser");
        buildScreen();
    }

    private void buildScreen() {
        Connection conn = DBUtil.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("select uid,picture.fbid from picture " + "where userid=?");
            prep.setString(1, (String) getApplication().getUser());
            rs = prep.executeQuery();
            while (rs.next()) {
                pictureids.put(rs.getString(2), rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(prep);
            DBUtil.close(conn);
        }
        albumSelector.setNullSelectionAllowed(true);
        albumSelector.setImmediate(true);
        albumSelector.addListener(this);
        addComponent(albumSelector);
        Button refresh = new Button("Refresh");
        refresh.addListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                try {
                    buildAlbumSelector();
                } catch (IOException e) {
                    getWindow().showNotification("Unable to retrieve albums - Facebook took too long to respond.", Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        nextButton = new Button("Next");
        nextButton.addListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                buildPhotos(null, (String) nextButton.getData());
            }
        });
        try {
            buildAlbumSelector();
        } catch (IOException e) {
            getWindow().showNotification("Unable to retrieve albums - Facebook took too long to respond.", Notification.TYPE_ERROR_MESSAGE);
        }
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponent(refresh);
        buttons.addComponent(nextButton);
        addComponent(buttons);
        addComponent(photospanel);
    }

    private void buildAlbumSelector() throws IOException {
        albumSelector.removeAllItems();
        FBPhotoAlbum[] al = FBQueries.getAlbums(getApplication());
        for (FBPhotoAlbum alb : al) {
            albumSelector.addItem(alb);
        }
    }

    private void buildPhotos(FBPhotoAlbum album, String next) {
        photospanel.removeAllComponents();
        Map map = FBQueries.getPhotos(getApplication(), album, next);
        if (map == null) {
            nextButton.setEnabled(false);
            return;
        } else {
            nextButton.setEnabled(true);
        }
        FBPhoto[] photos = (FBPhoto[]) map.get("fbphoto");
        if (map.containsKey("next") && map.get("next") != null) {
            nextButton.setData(map.get("next"));
            nextButton.setEnabled(true);
        } else nextButton.setEnabled(false);
        ArrayList<String> fbids = new ArrayList<String>();
        for (FBPhoto photo : photos) {
            fbids.add(photo.getUid());
        }
        HashSet<String> bannedIDs = new HashSet<String>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("SELECT bannedPictures.fbid FROM TABLE(fbid varchar(20)=?) T INNER JOIN bannedPictures ON T.fbid=bannedpictures.fbid");
            prep.setObject(1, fbids.toArray());
            rs = prep.executeQuery();
            while (rs.next()) {
                bannedIDs.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(prep);
            DBUtil.close(conn);
        }
        for (FBPhoto photo : photos) {
            VerticalLayout photolayout = new VerticalLayout();
            photolayout.setStyleName("picchooser");
            Embedded ifrm = new Embedded(null, new ExternalResource(photo.getSmallurl()));
            ifrm.setType(Embedded.TYPE_IMAGE);
            photolayout.addComponent(ifrm);
            Button add = new Button("Set Picture");
            add.setData(photo);
            add.addListener((ClickListener) this);
            photolayout.addComponent(add);
            if (pictureids.containsKey(photo.getUid())) {
                add.setEnabled(false);
            }
            if (bannedIDs.contains(photo.getUid())) {
                add.setCaption("Banned");
                add.setEnabled(false);
            }
            photospanel.addComponent(photolayout);
        }
    }

    public void valueChange(ValueChangeEvent event) {
        FBPhotoAlbum album = (FBPhotoAlbum) event.getProperty().getValue();
        if (album != null) {
            buildPhotos(album, null);
        }
    }

    public void buttonClick(ClickEvent event) {
        FBPhoto f = (FBPhoto) event.getButton().getData();
        Connection conn = DBUtil.getConnection();
        PreparedStatement prep = null;
        try {
            prep = conn.prepareStatement("update picture set fbid=?,picurl=?,Thumnailurl=?,width=0,height=0 where uid=? ");
            prep.setString(1, f.getUid());
            prep.setString(2, f.getBigurl());
            prep.setString(3, f.getSmallurl());
            prep.setLong(4, picUid);
            prep.executeUpdate();
            TabSheet setupts = ((PossessedApplication) getApplication()).getSetupTabsheet();
            setupts.setSelectedTab(returnLayout);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(prep);
            DBUtil.close(conn);
        }
    }
}
