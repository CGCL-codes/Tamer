package zildo.fwk.gfx.filter;

import zildo.fwk.gfx.GraphicStuff;
import zildo.monde.util.Pointf;

/**
 * @author Tchegito
 *
 */
public abstract class CloudFilter extends ScreenFilter {

    /**
	 * @param graphicStuff
	 */
    public CloudFilter(GraphicStuff graphicStuff) {
        super(graphicStuff);
    }

    protected float u = 0;

    protected float v = 0;

    protected Pointf wind = new Pointf(0.01f, 0);

    protected Pointf move = new Pointf(0, 0);

    public void setPosition(int x, int y) {
        u = x;
        v = -y;
        move.add(wind);
        u += move.x;
        v += move.y;
    }
}
