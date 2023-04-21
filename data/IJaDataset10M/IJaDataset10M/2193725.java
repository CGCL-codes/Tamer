package com.cidero.control;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import com.cidero.upnp.*;

/**
 *  Media browser panel class. Split window with browsing heirarchy
 *  panel on left (tree structure) and tabbed media item on the right.
 */
public class MediaBrowserPanel extends JPanel {

    private static final Logger logger = Logger.getLogger("com.cidero.control");

    MediaController mediaController;

    MediaServerDevice mediaDevice;

    MediaTreePanel treePanel;

    MediaItemPanel itemPanel;

    static final int MEDIA_TREE_PANEL = 1;

    static final int MEDIA_ITEM_PANEL = 2;

    int lastMouseClickPanel = MEDIA_TREE_PANEL;

    public MediaBrowserPanel(MediaController mediaController) {
        super(new GridLayout(1, 0));
        this.mediaController = mediaController;
        treePanel = new MediaTreePanel(this);
        treePanel.setPreferredSize(new Dimension(280, 400));
        itemPanel = new MediaItemPanel(this);
        itemPanel.setPreferredSize(new Dimension(540, 400));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, itemPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    public void setLastMouseClickPanel(int panel) {
        lastMouseClickPanel = panel;
    }

    public int getLastMouseClickPanel() {
        return lastMouseClickPanel;
    }

    public MediaTreePanel getMediaTreePanel() {
        return treePanel;
    }

    public MediaItemPanel getMediaItemPanel() {
        return itemPanel;
    }

    public void setMediaServer(MediaServerDevice mediaDevice) {
        logger.fine("setMediaDevice");
        if (this.mediaDevice == mediaDevice) {
            logger.fine("MediaDevice unchanged");
            return;
        }
        this.mediaDevice = mediaDevice;
        treePanel.setTreeModel(mediaDevice.getTreeModel());
        itemPanel.setAudioItemModel(mediaDevice.getAudioItemModel());
        itemPanel.setImageItemModel(mediaDevice.getImageItemModel());
        itemPanel.setVideoItemModel(mediaDevice.getVideoItemModel());
    }

    public MediaServerDevice getMediaServer() {
        return mediaDevice;
    }

    public void unsetMediaServer(MediaServerDevice mediaDevice) {
        logger.fine("unsetMediaDevice");
        if (this.mediaDevice != mediaDevice) {
            logger.fine("MediaDevice unchanged");
            return;
        }
        this.mediaDevice = null;
        treePanel.setTreeModelToDefault();
        itemPanel.setAudioItemModel(null);
    }

    /**
   *  Get selected object.  If object in tree panel was last thing
   *  selected, return it, otherwise return selected object in item
   *  panel. This allows for playback of an album object (contains
   *  playlist resource) by selecting it in the tree window 
   *  (emulates Intel Control Point behavior)
   */
    public CDSObject getSelectedObject() {
        CDSObject obj;
        if (lastMouseClickPanel == MEDIA_ITEM_PANEL) {
            logger.fine("getSelectedObject: ActivePanel = MEDIA_ITEM_PANEL");
            obj = itemPanel.getSelectedObject();
        } else {
            logger.fine("getSelectedObject: ActivePanel = MEDIA_TREE_PANEL");
            obj = treePanel.getSelectedObject();
        }
        if (obj != null) logger.fine("getSelectionObj: objString = " + obj.toString());
        return obj;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame window = new JFrame("Media Browser Panel Test");
        Container cp = window.getContentPane();
        window.setBounds(0, 0, 800, 200);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        MediaBrowserPanel mediaBrowserPanel = new MediaBrowserPanel(null);
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("MediaMine");
        DefaultMutableTreeNode musicNode = new DefaultMutableTreeNode("Music");
        musicNode.setAllowsChildren(true);
        DefaultMutableTreeNode artistsNode = new DefaultMutableTreeNode("Artists");
        artistsNode.setAllowsChildren(true);
        DefaultMutableTreeNode moviesNode = new DefaultMutableTreeNode("Movies");
        DefaultMutableTreeNode photosNode = new DefaultMutableTreeNode("Photos");
        rootNode.add(musicNode);
        musicNode.add(artistsNode);
        rootNode.add(moviesNode);
        rootNode.add(photosNode);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        treeModel.setAsksAllowsChildren(true);
        MediaTreePanel treePanel = mediaBrowserPanel.getMediaTreePanel();
        treePanel.setTreeModel(treeModel);
        AudioItemModel audioModel = new AudioItemModel();
        CDSMusicTrack track1 = new CDSMusicTrack();
        track1.setCreator("U2");
        track1.setArtist("U2");
        track1.setAlbum("Achtung Baby");
        track1.setTitle("The Real Thing");
        track1.setGenre("Rock");
        audioModel.add(track1);
        CDSMusicTrack track2 = new CDSMusicTrack();
        track2.setCreator("U2");
        track2.setArtist("U2");
        track2.setAlbum("Achtung Baby");
        track2.setTitle("Zoo Station");
        track2.setGenre("Rock");
        audioModel.add(track2);
        ImageQueue imageModel = new ImageQueue();
        CDSImageItem image1 = new CDSImageItem();
        image1.setCreator("PicTaker");
        image1.setTitle("Pic Title");
        CDSResource resource = new CDSResource();
        resource.setName("/ll/olivern/images/tvon.jpg");
        resource.setProtocolInfo("http-get:*:image/jpeg:*");
        resource.setResolution("513x778");
        image1.addResource(resource);
        imageModel.add(image1);
        CDSImageItem image2 = new CDSImageItem();
        image2.setCreator("PicTaker");
        image2.setTitle("Pic Title 2");
        resource = new CDSResource();
        resource.setName("/ll/olivern/images/musicMatch2.jpg");
        resource.setProtocolInfo("http-get:*:image/jpeg:*");
        resource.setResolution("480x720");
        image2.addResource(resource);
        imageModel.add(image2);
        CDSImageItem image3 = new CDSImageItem();
        image3.setCreator("PicTaker");
        image3.setTitle("Pic Title 3");
        resource = new CDSResource();
        resource.setName("/ll/olivern/images/PlanetEarth.jpg");
        resource.setProtocolInfo("http-get:*:image/jpeg:*");
        resource.setResolution("510x778");
        image3.addResource(resource);
        imageModel.add(image3);
        CDSImageItem image4 = new CDSImageItem();
        image4.setCreator("PicTaker");
        image4.setTitle("Pic Title 4");
        resource = new CDSResource();
        resource.setName("/ll/olivern/junk/LondonBridge.jpg");
        resource.setProtocolInfo("http-get:*:image/jpeg:*");
        resource.setResolution("500x375");
        image4.addResource(resource);
        imageModel.add(image4);
        MediaItemPanel itemPanel = mediaBrowserPanel.getMediaItemPanel();
        itemPanel.setAudioItemModel(audioModel);
        itemPanel.setImageItemModel(imageModel);
        cp.add(mediaBrowserPanel, BorderLayout.CENTER);
        window.setVisible(true);
    }
}
