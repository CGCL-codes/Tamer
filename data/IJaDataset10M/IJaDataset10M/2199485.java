package com.javaeedev.j2megame.puzzle;

import java.io.IOException;
import javax.microedition.lcdui.*;

/**
 * MainCanvas.
 * 
 * @author Xuefeng
 */
public class MainCanvas extends Canvas implements CommandListener, Updatable {

    private final int IMAGE_WIDTH = 30;

    private Document document;

    private Command exit = new Command("Exit", Command.EXIT, 1);

    public void update() {
        repaint();
    }

    public void commandAction(Command c, Displayable displayable) {
        if (c == exit) PuzzleMIDlet.quitApp();
    }

    public MainCanvas(String imageName) {
        setCommandListener(this);
        addCommand(exit);
        Image[] images = new Image[9];
        for (int i = 0; i < 9; i++) {
            try {
                images[i] = Image.createImage("/image/" + imageName + "/" + i + ".png");
            } catch (IOException ioe) {
                images[i] = null;
            }
        }
        document = new Document(this, images, 2, 2);
    }

    protected void paint(Graphics g) {
        g.fillRect(0, 0, getWidth(), getHeight());
        int state = document.getState();
        if (state == Document.PUZZLE_STATE) {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    Image image = document.getImage(x, y);
                    int m = x * IMAGE_WIDTH;
                    int n = y * IMAGE_WIDTH;
                    if (image != null) {
                        g.drawImage(image, y * IMAGE_WIDTH, x * IMAGE_WIDTH, Graphics.LEFT | Graphics.TOP);
                    } else {
                        g.setColor(0x000000);
                        g.fillRect(y * IMAGE_WIDTH, x * IMAGE_WIDTH, IMAGE_WIDTH, IMAGE_WIDTH);
                    }
                }
            }
            g.setColor(0xffffff);
            for (int i = 0; i <= 3; i++) {
                g.drawLine(0, i * IMAGE_WIDTH, 3 * IMAGE_WIDTH, i * IMAGE_WIDTH);
                g.drawLine(i * IMAGE_WIDTH, 0, i * IMAGE_WIDTH, 3 * IMAGE_WIDTH);
            }
        } else {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    Image image = document.getImage(x, y);
                    g.drawImage(image, y * IMAGE_WIDTH, x * IMAGE_WIDTH, Graphics.LEFT | Graphics.TOP);
                }
            }
        }
    }

    protected void keyReleased(int keyCode) {
        if (keyCode == KEY_NUM0) {
            document.setPuzzleState();
        }
    }

    protected void keyPressed(int keyCode) {
        switch(keyCode) {
            case KEY_NUM1:
                document.move(0, 0);
                break;
            case KEY_NUM2:
                document.move(0, 1);
                break;
            case KEY_NUM3:
                document.move(0, 2);
                break;
            case KEY_NUM4:
                document.move(1, 0);
                break;
            case KEY_NUM5:
                document.move(1, 1);
                break;
            case KEY_NUM6:
                document.move(1, 2);
                break;
            case KEY_NUM7:
                document.move(2, 0);
                break;
            case KEY_NUM8:
                document.move(2, 1);
                break;
            case KEY_NUM9:
                document.move(2, 2);
                break;
            case KEY_NUM0:
                document.setImageState();
                break;
        }
    }
}
