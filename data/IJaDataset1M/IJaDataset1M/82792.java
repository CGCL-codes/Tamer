package net.sf.bbarena.view;

import java.awt.*;
import java.awt.image.*;
import net.sf.bbarena.model.*;
import net.sf.bbarena.model.pitch.Square;
import net.sf.bbarena.model.team.Player;
import net.sf.bbarena.view.util.*;

public class RangeRulerUi extends PluggableComponent {

    private PitchView pitchView;

    private RangeRuler rangeRuler;

    private Square start;

    private Square end;

    private BufferedImage rangeRulerImage;

    public RangeRulerUi(PitchView owner, RangeRuler rangeRuler) {
        super(owner);
        pitchView = owner;
        this.rangeRuler = rangeRuler;
        rangeRulerImage = BitmapManager.loadIconPreserveAlpha("data/gfx/field/rangeruler.png").getImage();
    }

    public void setStart(Square start) {
        this.start = start;
        owner.repaint();
    }

    public Square getStart() {
        return start;
    }

    public void setEnd(Square end) {
        this.end = end;
        owner.repaint();
    }

    public Square getEnd() {
        return end;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            start = end = null;
        }
    }

    public void paint(Graphics g) {
        if (start != null) {
            Coordinate sc = pitchView.squareCenterInViewCoord(start);
            Coordinate ec = sc;
            if (end != null) {
                ec = pitchView.squareCenterInViewCoord(end);
            }
            int length = (int) length(sc, ec);
            if (length > 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(sc.getX(), sc.getY());
                double dx = ec.getX() - sc.getX();
                double dy = ec.getY() - sc.getY();
                double angle = 0;
                if (dx == 0) {
                    if (dy < 0) {
                        angle = -Math.PI / 2;
                    } else {
                        angle = Math.PI / 2;
                    }
                } else if (dy == 0) {
                    if (dx < 0) {
                        angle += Math.PI;
                    }
                } else {
                    angle = Math.atan(dy / dx);
                    if (dx < 0) {
                        angle += Math.PI;
                    }
                }
                g2.rotate(angle);
                g2.drawImage(rangeRulerImage, -9, -rangeRulerImage.getHeight() / 2, null);
                if (pitchView.getUseHelpers() && start.getPlayer() != null) {
                    for (Player player : rangeRuler.getInterceptionOpponents(start.getCoords(), end.getCoords(), start.getPlayer().getTeam())) {
                        pitchView.paintHighlightedSquare(g, player.getSquare(), Color.red);
                    }
                }
                g2.dispose();
            }
        }
    }

    private double length(Coordinate f, Coordinate t) {
        int x = f.getX() - t.getX();
        int y = f.getY() - t.getY();
        return Math.sqrt(x * x + y * y);
    }
}
