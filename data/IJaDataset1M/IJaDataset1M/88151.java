package polr.client.ui.speech;

import java.util.LinkedList;
import java.util.Queue;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.loading.LoadingList;
import polr.client.GlobalGame;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.TextArea;

public class SpeechPopup extends Frame {

    Queue<String> speechQueue;

    TextArea speechDisplay;

    protected String stringToPrint;

    Polygon triangle;

    Image bg;

    boolean isGoingDown = true;

    public SpeechPopup() {
        speechQueue = new LinkedList<String>();
        initGUI();
    }

    public SpeechPopup(String speech) {
        speechQueue = new LinkedList<String>();
        for (String line : speech.split("/n")) speechQueue.add(line);
        initGUI();
    }

    public void initGUI() {
        LoadingList.setDeferredLoading(true);
        try {
            bg = new Image("res/ui/hud/textbackground.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
        LoadingList.setDeferredLoading(false);
        Label bg = new Label(this.bg);
        bg.setSize(400, 100);
        bg.setLocation(0, -11);
        speechDisplay = new TextArea();
        speechDisplay.setFocusable(false);
        speechDisplay.setSize(384, 100);
        speechDisplay.setLocation(16, 5);
        speechDisplay.setBorderRendered(false);
        speechDisplay.setFont(GlobalGame.getDPFont());
        speechDisplay.setOpaque(false);
        this.getContentPane().add(bg);
        this.getContentPane().add(speechDisplay);
        this.setWidth(400);
        this.setHeight(100);
        this.setX((GlobalGame.width / 2) - getWidth() / 2);
        this.setY((GlobalGame.height / 2) + getWidth() / 2);
        this.getTitleBar().setVisible(false);
        this.setResizable(false);
        this.setFocusable(false);
        this.setAlwaysOnTop(true);
        try {
            advance();
        } catch (Exception e) {
        }
    }

    public void advance() throws Exception {
        triangle = null;
        stringToPrint = speechQueue.poll();
        if (stringToPrint != null) {
            speechDisplay.setText(stringToPrint);
        } else throw new Exception();
    }

    public void advancing(String toPrint) {
    }

    public void advancedPast(String printed) {
    }

    public void advanced(String done) {
    }

    public boolean canAdvance() {
        return true;
    }

    public void triangulate() {
        triangle = new Polygon();
        triangle.addPoint(getWidth() - 30 + getX(), 60 + getY());
        triangle.addPoint(getWidth() - 30 + getX() + 10, 60 + getY());
        triangle.addPoint(getWidth() - 30 + getX() + 5, 60 + getY() + 10);
    }

    @Override
    public void render(GUIContext container, Graphics g) {
        super.render(container, g);
        if (triangle != null) {
            g.setColor(Color.red);
            g.fill(triangle);
            if (Math.round(triangle.getCenterY()) > 584) triangle.setCenterY(584); else if (Math.round(triangle.getCenterY()) < 574) triangle.setCenterY(574);
            if (Math.round(triangle.getCenterY()) == 574) isGoingDown = true; else if (Math.round(triangle.getCenterY()) == 584) isGoingDown = false;
            if (isGoingDown) triangle.setCenterY(triangle.getCenterY() + .5f); else triangle.setCenterY(triangle.getCenterY() - .5f);
        }
    }

    public void addSpeech(String speech) {
        for (String line : speech.split("\n")) speechQueue.add(line);
    }
}
