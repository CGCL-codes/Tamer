package de.fu_berlin.inf.dpp.videosharing.source;

import java.awt.image.BufferedImage;
import org.picocontainer.Disposable;
import de.fu_berlin.inf.dpp.videosharing.activities.VideoActivity;

/**
 * Everything which can be shared by video.
 * 
 * @author s-lau
 * 
 */
public interface ImageSource extends Disposable {

    public BufferedImage toImage();

    public void processActivity(VideoActivity activity);

    public void switchMode();
}
