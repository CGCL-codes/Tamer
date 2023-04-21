package maelstrom;

import java.awt.Rectangle;

public class Obstacle extends GameSprite implements java.io.Serializable {

    GameData gameData;

    int locX, locY;

    int length;

    public Obstacle(GameData data) {
        gameData = data;
        locX = 10;
        locY = 40;
        length = 50;
    }

    public Obstacle(GameData data, int initialX, int initialY, int initialLength) {
        gameData = data;
        locX = initialX;
        locY = initialY;
        length = initialLength;
    }

    public boolean containsPoint(int x, int y) {
        return ((locX <= x) && (x < locX + 1) && (locY <= y) && (y < locY + length));
    }

    public Rectangle getExtents() {
        return new Rectangle(locX, locY, 1, length);
    }

    public void move(int mouseX, int mouseY) {
    }

    public void paint(java.awt.Graphics g) {
        g.setColor(java.awt.Color.black);
        g.fillRect(locX, locY, 1, length);
    }
}
