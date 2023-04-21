package zildo.platform.filter;

import org.lwjgl.opengl.GL11;
import zildo.client.ClientEngineZildo;
import zildo.fwk.gfx.GraphicStuff;
import zildo.fwk.gfx.filter.FilterEffect;
import zildo.fwk.gfx.filter.ZoomFilter;
import zildo.monde.sprites.persos.PersoZildo;
import zildo.monde.util.Point;
import zildo.server.EngineZildo;

public class LwjglZoomFilter extends ZoomFilter {

    public LwjglZoomFilter(GraphicStuff graphicStuff) {
        super(graphicStuff);
    }

    @Override
    public boolean renderFilter() {
        focusOnZildo();
        GL11.glDisable(GL11.GL_BLEND);
        return true;
    }

    @Override
    protected void focusOnZildo() {
        PersoZildo zildo = EngineZildo.persoManagement.getZildo();
        Point zildoPos = zildo.getCenteredScreenPosition();
        ClientEngineZildo.openGLGestion.setZoomPosition(zildoPos);
        float z = 2.0f * (float) Math.sin(getFadeLevel() * (0.25f * Math.PI / 256.0f));
        ClientEngineZildo.openGLGestion.setZ(z);
    }

    /**
	 * Re-initialize z coordinate
	 */
    @Override
    public void doOnInactive(FilterEffect effect) {
        ClientEngineZildo.openGLGestion.setZ(0);
    }
}
