package net.sf.rottz.tv.client.rottzclient.screens.components.infopanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import net.sf.rottz.tv.client.RottzTVClientException;
import net.sf.rottz.tv.client.rottzclient.screens.GameScreen;
import net.sf.rottz.tv.common.media.MovieInfo;
import net.sf.rottz.tv.common.world.ProgramType;

public class MovieProgramPanel extends InfoPanel {

    public static int SLOT_WIDTH = 350;

    public static int SLOT_HEIGHT = 40;

    private static final Dimension SLOT_DIMENSION = new Dimension(SLOT_WIDTH, SLOT_HEIGHT);

    private GameScreen screen = null;

    private MovieInfo movieInfo;

    private boolean hasStarted;

    @Override
    public void render(Graphics g, Point clientPos) {
        if (screen == null) throw new RottzTVClientException("Required helpers were not set. Aborting render of panel.");
        int numBlocks = 1;
        if (movieInfo != null) numBlocks = movieInfo.getLengthBlocks();
        Rectangle movieRect = buildProgramRect(numBlocks, clientPos);
        String name = "(NO PROGRAM)";
        if (movieInfo != null) {
            name = movieInfo.getName();
            if (hasStarted) name = "[" + name + "]";
        }
        screen.drawRectangle(g, movieRect, Color.BLACK, Color.GRAY);
        screen.drawCenteredString(g, name, movieRect, Color.BLACK);
    }

    private Rectangle buildProgramRect(int numBlocks, Point pos) {
        Rectangle programRect = new Rectangle(pos, SLOT_DIMENSION);
        programRect.height = numBlocks * (SLOT_DIMENSION.height);
        return programRect;
    }

    public void setHelpers(GameScreen screen, MovieInfo movieInfo, boolean hasStarted) {
        this.screen = screen;
        this.movieInfo = movieInfo;
        this.hasStarted = hasStarted;
    }
}
