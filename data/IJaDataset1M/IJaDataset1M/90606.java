package interfaces;

import javax.microedition.lcdui.Image;

/**
 *
 * @author Jan Oliver Zieger
 */
public interface IScaleImageService extends IService {

    Image scaleImage(Image im, int width, int height);
}
