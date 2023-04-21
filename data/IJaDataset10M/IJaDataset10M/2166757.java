package de.creepsmash.client.creep;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import de.creepsmash.client.game.GameContext;
import de.creepsmash.client.util.Cache;
import de.creepsmash.common.IConstants;

/**
 * implementation of a Creep1.
 * @author Robert
 *
 */
public class Creep1 extends AbstractCreep {

    /**
	 * constructor to set gamecontext and type of creep.
	 * @param context gamecontext
	 * @param t type of creep
	 */
    public Creep1(GameContext context, IConstants.Creeps t) {
        super(context, t);
        setImage(new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB));
        Graphics2D g2 = (Graphics2D) getImage().getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(10, 6, 14, 10);
        g2.drawLine(6, 6, 10, 10);
        g2.drawLine(6, 14, 10, 10);
        g2.drawLine(10, 14, 14, 10);
        g2.dispose();
    }

    /**
	 * {@inheritDoc}
	 */
    public String getDescription() {
        return null;
    }

    /**
	 * 
	 */
    public void loadImage() {
        if (Cache.getInstance().hasCreepImg(IConstants.Creeps.creep1)) {
            setImage(Cache.getInstance().getCreepImg(IConstants.Creeps.creep1));
        }
    }
}
