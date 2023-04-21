package com.slychief.clubmixer.client.commons.services;

import com.slychief.clubmixer.client.commons.handler.albumart.AlbumartChangeEvent;
import com.slychief.clubmixer.client.commons.handler.albumart.AlbumartChangeListener;
import com.slychief.clubmixer.commons.data.entities.ImageSize;
import com.slychief.clubmixer.logging.ClubmixerLogger;
import com.slychief.clubmixer.server.library.entities.Song;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Alexander Schindler
 */
public class AlbumartStorageHandler implements AlbumartChangeListener {

    private static final String APP_DATA_DIR = "/Application Data";

    private static final String SLYCHIEF_MUTLIMEDIA_DIR = "/Slychief Multimedia";

    private static final String USER_HOME = "user.home";

    private static final String CLUBMIXER_CLIENT_DIR = APP_DATA_DIR + SLYCHIEF_MUTLIMEDIA_DIR + "/Clubmixer Client";

    private static LastfmAlbumartDownloader lastfm;

    /**
     * Constructs ...
     *
     */
    public AlbumartStorageHandler() {
        lastfm = new LastfmAlbumartDownloader();
        File appDir = new File(System.getProperty(USER_HOME) + APP_DATA_DIR);
        if (!appDir.exists()) {
            (new File(System.getProperty(USER_HOME) + APP_DATA_DIR)).mkdir();
        }
        File homeDir = new File(System.getProperty(USER_HOME) + APP_DATA_DIR + SLYCHIEF_MUTLIMEDIA_DIR);
        if (!homeDir.exists()) {
            homeDir.mkdir();
        }
        File clientDir = new File(System.getProperty(USER_HOME) + CLUBMIXER_CLIENT_DIR);
        if (!clientDir.exists()) {
            clientDir.mkdir();
            (new File(System.getProperty(USER_HOME) + CLUBMIXER_CLIENT_DIR + "/albumart")).mkdir();
        }
    }

    /**
     * Method description
     *
     *
     * @param image
     * @param song
     * @param _size
     */
    public static void storeImage(Image image, Song song, ImageSize _size) {
        try {
            String sizePostfix = "";
            if (_size.equals(ImageSize.SMALL)) {
                sizePostfix = "small";
            } else if (_size.equals(ImageSize.MEDIUM)) {
                sizePostfix = "medium";
            } else if (_size.equals(ImageSize.BIG)) {
                sizePostfix = "big";
            }
            File imageDir = new File(System.getProperty(USER_HOME) + CLUBMIXER_CLIENT_DIR + "/albumart");
            String[] tmp = song.getAlbumArt().split("/");
            String imageFileName = tmp[(tmp.length - 1)];
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.drawImage(image, null, null);
            File imageFile = new File(imageDir, imageFileName);
            ImageIO.write(bufferedImage, "jpg", imageFile);
        } catch (IOException ex) {
            ClubmixerLogger.debugUnexpectedException(AlbumartStorageHandler.class, ex);
        }
    }

    /**
     * Method description
     *
     *
     * @param song
     * @param _size
     *
     * @return
     */
    public static FadeImageIcon loadImage(Song song, ImageSize _size) {
        if (song.getAlbumArt() == null) {
            return null;
        }
        FadeImageIcon icon = null;
        String sizePostfix = "";
        if (_size.equals(ImageSize.SMALL)) {
            sizePostfix = "small";
        } else if (_size.equals(ImageSize.MEDIUM)) {
            sizePostfix = "medium";
        } else if (_size.equals(ImageSize.BIG)) {
            sizePostfix = "big";
        }
        String[] tmp = song.getAlbumArt().split("/");
        String imageFileName = tmp[(tmp.length - 1)];
        File imageDir = new File(System.getProperty(USER_HOME) + CLUBMIXER_CLIENT_DIR + "/albumart");
        File imageFile = new File(imageDir, imageFileName);
        try {
            BufferedImage image = ImageIO.read(imageFile);
            icon = new FadeImageIcon(image);
        } catch (IOException ex) {
        }
        return icon;
    }

    /**
     * Method description
     *
     *
     * @param se
     */
    @Override
    public void onAlbumartChanged(AlbumartChangeEvent se) {
    }

    /**
     * Method description
     *
     *
     * @param song
     *
     * @return
     */
    public static String getAlbumartURL(Song song) {
        return lastfm.getAlbumartUrl(song);
    }
}
