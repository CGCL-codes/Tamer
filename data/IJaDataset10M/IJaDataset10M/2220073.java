package com.usoog.hextd.creep;

import com.usoog.commons.gamecore.gamegrid.ScaledCanvas;
import com.usoog.hextd.Constants.COLOR;
import com.usoog.hextd.util.Cache;
import com.usoog.hextd.util.Geometry;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import com.usoog.hextd.core.HexTDPlayer;
import com.usoog.hextd.creep.CreepBaseDirectional.DirectionalData;

public class CreepHardGrey extends CreepBase {

    public static String type = "hg";

    private static Color bodyColor = new Color(150, 150, 150);

    private static float reductionFactor = 0.005f;

    private DirectionalData dd = new DirectionalData();

    /** Creates a new instance of CreepHardGrey */
    public CreepHardGrey() {
        super("Hard Grey");
        this.damageColor = COLOR.OTHER;
        this.damageColorAnti = COLOR.OTHER;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getInfoString() {
        return "<h1>Hard Grey Box</h1>" + "<p>" + "It's a box, it's grey, it's boring.<br>" + "But it's harder to kill then normal.</p>" + "<p><b>Damage Reduction:</b> " + CreepHardGrey.form.format(Math.round(reductionFactor * this.healthMax)) + "<br>" + "" + super.getInfoString() + "</p>";
    }

    @Override
    public void damage(HexTDPlayer shooter, long damage) {
        long realDamage = Math.round(damage - reductionFactor * this.healthMax);
        realDamage = Math.max(realDamage, 1);
        if (this.isValidTarget()) {
            health -= realDamage;
        }
        if (health <= 0) {
            validTarget = false;
            dead = true;
            shooter.getResources().doReceive(reward);
            getOwner().minionDied(this);
        }
        fireStatusChangedEvent();
    }

    @Override
    public void resetScale(ScaledCanvas sc) {
        super.resetScale(sc);
        dd.size = size;
        Cache cache = Cache.getInstance();
        resetScale(sc, dd, cache);
    }

    public static void resetScale(ScaledCanvas sc, DirectionalData dd, Cache cache) {
        double paintScale = sc.getPaintScale();
        float strokeWidth = sc.getDefaultStrokeWidth();
        if (cache.hasShape("ESHGShape" + dd.size.letter)) {
            dd.bodyShape = cache.getShape("ESHGShape" + dd.size.letter);
        } else {
            double scale = paintScale * dd.size.scaleMod;
            double r = scale / 6;
            double[][] points = { { +r, +r }, { +r, -r }, { -r, -r }, { -r, +r } };
            dd.bodyShape = Geometry.coordsToGeneralPath(points, true);
            cache.putShape("ESHGShape" + dd.size.letter, dd.bodyShape);
        }
        dd.bodyShapeStroke = new BasicStroke(3.5f * strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        dd.bodyShapeColor = bodyColor;
    }

    @Override
    public void paint(Graphics2D g2, int gameTime) {
        if (this.inactive || this.dead) {
        } else {
            Stroke defaultStroke = g2.getStroke();
            AffineTransform saveXform = g2.getTransform();
            g2.transform(atTranslate);
            if (this.shielded) {
                this.paintShield_bottom(g2);
            }
            this.paintHealthLine(g2);
            g2.setColor(dd.bodyShapeColor);
            g2.setStroke(dd.bodyShapeStroke);
            g2.draw(dd.bodyShape);
            g2.setTransform(saveXform);
            if (this.shielded) {
                g2.transform(atTranslate);
                this.paintShield_top(g2);
                g2.setTransform(saveXform);
            }
            super.paintSelected(g2);
            g2.setStroke(defaultStroke);
        }
    }

    /**
	 * Paint the image of this creep type on the given graphics device, using
	 * the given canvas for scaling info, fetching data using the given cache
	 * name, using the given time for animation.
	 *
	 * @param sc the canvas to use for scaling information.
	 * @param g2 the graphics device to paint on.
	 * @param cacheName the name to use for fetching a cache instance.
	 * @param paintTime the time to use for animation.
	 */
    public static void paint(ScaledCanvas sc, Graphics2D g2, String cacheName, int paintTime, Size size) {
        DirectionalData dd = new DirectionalData();
        dd.size = size;
        Cache cache = Cache.getInstance(cacheName);
        resetScale(sc, dd, cache);
        Stroke defaultStroke = g2.getStroke();
        g2.setColor(dd.bodyShapeColor);
        g2.setStroke(dd.bodyShapeStroke);
        g2.draw(dd.bodyShape);
        g2.setStroke(defaultStroke);
    }
}
