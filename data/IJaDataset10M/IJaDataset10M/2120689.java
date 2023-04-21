package Floors;

/**
 *
 * @author vf
 */
public class GradientDownRight extends Slope {

    public GradientDownRight() {
        friction = 1;
        xDeviation = gradient;
        yDeviation = gradient;
        floorImage = EnigmaUtils.ImageLoader.loadImage("./images/fl-gradient2.png").getSubimage(2 * 32, 0, 32, 32);
    }
}
