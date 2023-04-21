package net.sf.fkk.gui.avatar;

import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.net.URL;
import net.sf.fkk.gui.MediaManager;
import net.sf.fkk.global.Programmverwaltung;

class Avatar {

    Avatar(MediaManager media, URL baseurl) {
        mediaManager = media;
        BaseAvatar = new String[2];
        WinAvatar = new String[2];
        LoseAvatar = new String[2];
        ImageArray = new BufferedImage[3][2];
        BaseURL = baseurl;
    }

    void ladeAvatare() {
        for (int i = 0; i < 2; ++i) {
            if (BaseAvatar[i] != null) {
                ImageArray[0][i] = mediaManager.getAvatarPicture(BaseAvatar[i], BaseURL);
            }
        }
        for (int i = 0; i < 2; ++i) {
            if (WinAvatar[i] != null) {
                ImageArray[1][i] = mediaManager.getAvatarPicture(WinAvatar[i], BaseURL);
            }
        }
        for (int i = 0; i < 2; ++i) {
            if (LoseAvatar[i] != null) {
                ImageArray[2][i] = mediaManager.getAvatarPicture(LoseAvatar[i], BaseURL);
            }
        }
        for (int i = 0; i < 3; ++i) {
            if ((ImageArray[i][0] == null) && (ImageArray[i][1] != null)) {
                ImageArray[i][0] = flipAvatar(ImageArray[i][1]);
            }
            if ((ImageArray[i][1] == null) && (ImageArray[i][0] != null)) {
                ImageArray[i][1] = flipAvatar(ImageArray[i][0]);
            }
        }
        for (int i = 1; i < 3; ++i) {
            if ((ImageArray[i][0] == null) && (ImageArray[i][1] == null)) {
                ImageArray[i][0] = ImageArray[0][0];
                ImageArray[i][1] = ImageArray[0][1];
            }
        }
    }

    private BufferedImage flipAvatar(BufferedImage source) {
        AffineTransform at = new AffineTransform(-1, 0, 0, 1, source.getWidth(), 0);
        AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage dimg = ato.filter(source, null);
        return (dimg);
    }

    public BufferedImage getAvatar(boolean win, boolean lose, boolean left) {
        int ori = left ? 0 : 1;
        int idx = 0;
        if (win == true) idx = 1;
        if (lose == true) idx = 2;
        return (ImageArray[idx][ori]);
    }

    boolean KI;

    String Name;

    String BaseAvatar[];

    String WinAvatar[];

    String LoseAvatar[];

    private BufferedImage ImageArray[][];

    private URL BaseURL;

    private MediaManager mediaManager;
}
