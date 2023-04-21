package sangria.ui;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Vector;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import sangria.GameSession;
import sangria.board.Region;
import sangria.board.Court;
import sangria.board.Province;
import sangria.play.Caballero;
import sangria.play.CaballeroWithoutPositionException;
import sangria.play.Player;
import sangria.cards.ActionCard;
import sangria.cards.CardStack;

/**
 * @author Wouter Lievens
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserInterface extends Frame implements ImageObserver {

    private Button btnQuit_;

    private GameSession session_;

    private Image spain_;

    private Image[] pointMarkers_;

    public UserInterface(String title, GameSession session) {
        super(title + ": " + session.getTitle());
        session_ = session;
        setSize(1024, 768);
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(null);
        btnQuit_ = new Button("Quit");
        btnQuit_.setSize(60, 25);
        btnQuit_.setLocation(10, 30);
        btnQuit_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnQuitAction(e);
            }
        });
        add(btnQuit_);
        loadImages();
        show();
    }

    private void loadImages() {
        try {
            spain_ = ImageIO.read(new File("img/spain.gif"));
            pointMarkers_ = new Image[9];
            for (int n = 0; n < 9; ++n) {
                pointMarkers_[n] = ImageIO.read(new File("img/points_" + n + ".gif"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void btnQuitAction(ActionEvent e) {
        System.exit(0);
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }

    private void drawShape(Graphics gfx, Region region) {
        if (region.getShape() != null) {
            gfx.setColor(Color.lightGray);
            Player dominating = region.getDominatingPlayer();
            if (dominating != null) {
                gfx.setColor(dominating.getColor().brighter());
            }
            gfx.fillPolygon(region.getShape());
            gfx.setColor(Color.black);
            gfx.drawPolygon(region.getShape());
        }
    }

    private void drawLabel(Graphics gfx, Region region) {
        gfx.setColor(Color.black);
        gfx.drawString(region.getName(), (int) region.getLabelPoint().getX(), (int) region.getLabelPoint().getY());
    }

    private void drawScores(Graphics gfx, Region region) {
        int size = 20;
        int diff = 2;
        int cx = (int) region.getScorePoint().getX();
        int cy = (int) region.getScorePoint().getY();
        gfx.drawImage(pointMarkers_[region.getHighScore()], cx, cy, size, size, this);
        cx += size + diff;
        gfx.drawImage(pointMarkers_[region.getMidScore()], cx, cy, size, size, this);
        cx += size + diff;
        gfx.drawImage(pointMarkers_[region.getLowScore()], cx, cy, size, size, this);
    }

    private void drawGrandes(Graphics gfx, Region region) {
        final int size = Caballero.getShapeSize() * 2;
        for (int n = 0; n < session_.getPlayers().size(); ++n) {
            Player player = (Player) session_.getPlayers().get(n);
            if (player.getHome() == region) {
                gfx.setColor(player.getColor());
                Point pos = player.getGrandePosition();
                gfx.fillRect((int) pos.getX() - size / 2, (int) pos.getY() - size / 2, size, size);
                gfx.setColor(Color.black);
                gfx.drawRect((int) pos.getX() - size / 2, (int) pos.getY() - size / 2, size, size);
            }
        }
    }

    private void drawCaballero(Graphics gfx, Caballero caballero) throws CaballeroWithoutPositionException {
        final int size = Caballero.getShapeSize();
        Point pos = caballero.getPosition();
        if (pos == null) {
            throw new CaballeroWithoutPositionException();
        }
        gfx.setColor(caballero.getOwner().getColor());
        gfx.fillRect((int) pos.getX() - size / 2, (int) pos.getY() - size / 2, size, size);
        gfx.setColor(Color.black);
        gfx.drawRect((int) pos.getX() - size / 2, (int) pos.getY() - size / 2, size, size);
    }

    private void drawCaballeros(Graphics gfx, Region region) throws CaballeroWithoutPositionException {
        Vector caballeros = region.getCaballeros();
        for (int n = 0; n < caballeros.size(); ++n) {
            drawCaballero(gfx, (Caballero) caballeros.get(n));
        }
    }

    private void drawCourt(Graphics gfx, Player player) throws CaballeroWithoutPositionException {
        Court court = player.getCourt();
        Polygon polygon = court.getShape();
        gfx.setColor(player.getColor().brighter());
        gfx.fillPolygon(polygon);
        gfx.setColor(Color.black);
        gfx.drawPolygon(polygon);
        for (int n = 0; n < court.getCaballeros().size(); ++n) {
            drawCaballero(gfx, (Caballero) court.getCaballeros().get(n));
        }
    }

    private void drawProvince(Graphics gfx, Player player) throws CaballeroWithoutPositionException {
        Province province = player.getProvince();
        Polygon polygon = province.getShape();
        gfx.setColor(player.getColor().brighter());
        gfx.fillPolygon(polygon);
        gfx.setColor(Color.black);
        gfx.drawPolygon(polygon);
        for (int n = 0; n < province.getCaballeros().size(); ++n) {
            drawCaballero(gfx, (Caballero) province.getCaballeros().get(n));
        }
    }

    private void drawActionCards(Graphics gfx) {
        int width = 100;
        int height = 140;
        int x;
        int y = 560;
        final Color back = new Color(200, 220, 150);
        for (int n = 0; n < session_.getCardStacks().size(); ++n) {
            CardStack cardStack = (CardStack) session_.getCardStacks().get(n);
            ActionCard card = cardStack.getTop();
            x = 20 + n * (width + 20);
            gfx.setColor(back);
            gfx.fillRect(x, y, width, height);
            gfx.setColor(Color.black);
            gfx.drawRect(x, y, width, height);
            gfx.drawString((n + 1) + " Caballeros", x + 10, y + 20);
            gfx.drawString(card.getTitle(), x + 10, y + 40);
            gfx.drawString(card.getDescription(), 20, getHeight() - 20);
        }
    }

    private void drawCards(Graphics gfx) {
        drawActionCards(gfx);
    }

    public void paint(Graphics gfx) {
        try {
            gfx.drawString("Courts", 650, 50);
            gfx.drawString("Provinces", 850, 50);
            Vector regions = session_.getBoard().getRegions();
            for (int n = 0; n < regions.size(); ++n) {
                Region region = (Region) (regions.get(n));
                drawShape(gfx, region);
                drawScores(gfx, region);
                drawGrandes(gfx, region);
                drawCaballeros(gfx, region);
                drawLabel(gfx, region);
            }
            Vector players = session_.getPlayers();
            for (int n = 0; n < players.size(); ++n) {
                Player player = (Player) players.get(n);
                drawCourt(gfx, player);
                drawProvince(gfx, player);
            }
            drawCards(gfx);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
