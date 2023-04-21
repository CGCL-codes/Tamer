package game;

import game.objects.*;
import game.objects.Mario;
import editor.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.image.VolatileImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class gameMain extends JPanel implements Runnable {

    public static boolean showSpritePos = false;

    public static boolean showSpriteNum = false;

    public static boolean showCamera = false;

    private static boolean levelLoaded = false;

    private boolean openLevelFile = false;

    public static Dimension resolution = new Dimension(400, 300);

    public static boolean antialiasing = true;

    public static boolean[] keyPressed = new boolean[99999];

    public static boolean[] keyReleased = new boolean[99999];

    MouseMotion mouse = new MouseMotion();

    MouseKlick button = new MouseKlick();

    public Thread main;

    public static Image marioSpriteSheet;

    public static Image boxSpriteSheet;

    public static Image coinSpriteSheet;

    public static Image tileSheet;

    public static Image[] backgroundImage;

    private VolatileImage tileLayer;

    private VolatileImage[] backgroundLayer;

    public static VolatileImage renderImage;

    public static int numberOfSprites;

    public static Sprite[] sprite = new Sprite[99999];

    public static Image[] spriteImage = new Image[99999];

    public static int numberOfTiles;

    public static Sprite[] tile = new Sprite[99999];

    public static Image[] tileImage = new Image[99999];

    public static Actor[] actor = new Actor[99999];

    public static Mario mario;

    public static PopupCoin pCoin;

    public static WorldTile[] tileObject = new WorldTile[99999];

    public static ItemContainer[] box = new ItemContainer[99999];

    public static Coin[] coin = new Coin[99999];

    public static int numberOfBoxes;

    public static int numberOfCoins;

    public static int collectedCoins = 0;

    StringBuffer strBuffer = new StringBuffer();

    public static int width;

    ;

    public static int height;

    private File initLevel = null;

    public static Level loadedLevel = new Level("");

    public static Camera camera;

    public static boolean fullscreen = false;

    public static boolean newFrame = false;

    public gameMain(String level) {
        this.setDoubleBuffered(true);
        this.addKeyListener(new KeyPressListener());
        this.setFocusable(true);
        main = new Thread(this);
        main.start();
        initLevel = new File(level);
        openLevelFile = false;
    }

    public gameMain(boolean open) {
        this.setDoubleBuffered(true);
        this.addKeyListener(new KeyPressListener());
        this.setFocusable(true);
        main = new Thread(this);
        main.start();
        openLevelFile = open;
    }

    public gameMain() {
        this.setDoubleBuffered(true);
        this.addKeyListener(new KeyPressListener());
        this.setFocusable(true);
        main = new Thread(this);
        main.start();
        openLevelFile = true;
    }

    public static int fps;

    public void initialize() {
        try {
            boxSpriteSheet = ImageIO.read(new File("ItemContainer.png"));
            coinSpriteSheet = ImageIO.read(new File("Coin.png"));
        } catch (Exception e) {
        }
        if (openLevelFile == false) {
            try {
                loadLevel(initLevel);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            loadLevel(FileOpenDialog("Open ..."));
        }
        renderTileLayer();
        backgroundLayer = new VolatileImage[backgroundImage.length];
        for (int i = 0; i < backgroundImage.length; i++) {
            renderBackgroundLayer(0);
            renderBackgroundLayer(1);
        }
        renderImage = this.getGraphicsConfiguration().createCompatibleVolatileImage(loadedLevel.getWidth() * 16, loadedLevel.getHeight() * 16, Transparency.TRANSLUCENT);
        Graphics2D g2d = renderImage.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, renderImage.getWidth(), renderImage.getHeight());
        g2d.setBackground(new Color(0, 0, 0, 0));
    }

    public void run() {
        initialize();
        Timer t = new Timer();
        while (true) {
            if (levelLoaded == true) {
                camera.follow(mario.sprite);
                int a = 0;
                while (actor[a] != null && actor[a] instanceof Actable) {
                    Actable actable = (Actable) actor[a];
                    actable.act();
                    a++;
                }
                pCoin.fly();
                for (int i = 0; i < numberOfBoxes; i++) {
                    try {
                        box[i].open();
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e);
                    }
                }
                for (int i = 0; i < numberOfCoins; i++) {
                    try {
                        coin[i].collect();
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e);
                    }
                }
                if (mario.sprite.posy > loadedLevel.getHeight() * 16) {
                    camera.position = new Point(width / 2, camera.prefHeight + camera.tolerance);
                    mario.sprite.setPosition(new Point(gameMain.mario.spawn.x, gameMain.mario.spawn.y));
                }
            }
            try {
                if (fullscreen == false) {
                    t.start();
                    render();
                    repaint();
                    System.out.println("FPS: " + (int) (((100 / (double) t.stop())) * 2));
                } else {
                    t.start();
                    long sleeptime = 5 - t.stop();
                    if (sleeptime < 0) {
                        sleeptime = 0;
                    }
                    main.sleep(1L + sleeptime);
                    fps = (int) (((100 / (double) t.stop())) * 2);
                    System.out.println("FPS: " + fps);
                }
            } catch (Exception e) {
            }
        }
    }

    private class Timer {

        long startTime;

        public Timer() {
            start();
        }

        public void start() {
            startTime = System.currentTimeMillis();
        }

        public long stop() {
            return System.currentTimeMillis() - startTime;
        }
    }

    private class KeyPressListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (keyPressed[key] == false) {
                keyPressed[key] = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            keyReleased[key] = true;
            keyPressed[key] = false;
        }
    }

    public VolatileImage renderTileLayer() {
        tileLayer = this.getGraphicsConfiguration().createCompatibleVolatileImage(loadedLevel.getWidth() * 16, loadedLevel.getHeight() * 16, Transparency.TRANSLUCENT);
        Graphics2D g2d = tileLayer.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, tileLayer.getWidth(), tileLayer.getHeight());
        g2d.setBackground(new Color(0, 0, 0, 0));
        g2d.setColor(new Color(1f, 1f, 1f, 1f));
        for (int i = 0; i < numberOfTiles; i++) {
            tile[i].draw(g2d, this);
        }
        return tileLayer;
    }

    public VolatileImage renderBackgroundLayer(int LayerNumber) {
        backgroundLayer[LayerNumber] = this.getGraphicsConfiguration().createCompatibleVolatileImage(loadedLevel.getWidth() * 16, loadedLevel.getHeight() * 16, Transparency.TRANSLUCENT);
        Graphics2D g2d = backgroundLayer[LayerNumber].createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(new Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, backgroundLayer[LayerNumber].getWidth(), backgroundLayer[LayerNumber].getHeight());
        g2d.setBackground(new Color(0, 0, 0, 0));
        g2d.setColor(new Color(1f, 1f, 1f, 1f));
        for (int i = 0; i < backgroundLayer[LayerNumber].getWidth(this) / backgroundImage[LayerNumber].getWidth(this); i++) {
            g2d.drawImage(backgroundImage[LayerNumber], i * backgroundImage[LayerNumber].getWidth(this), 0, this);
        }
        return backgroundLayer[LayerNumber];
    }

    public VolatileImage render() {
        Graphics2D g2d = renderImage.createGraphics();
        try {
            for (int i = 0; i < backgroundLayer.length; i++) {
                g2d.drawImage(backgroundLayer[i], -(int) (camera.position.x * Math.pow(0.5, backgroundLayer.length - i)), -(int) (camera.position.y * Math.pow(0.5, backgroundLayer.length - i)) + backgroundLayer[i].getHeight(this) - backgroundLayer[i].getHeight(this) / (i + 1), this);
            }
        } catch (Exception e) {
        }
        try {
            g2d.drawImage(tileLayer, camera.center.x - camera.position.x, camera.center.y - camera.position.y, this);
        } catch (Exception e) {
        }
        try {
            int a = 0;
            while (sprite[a] != null) {
                if (sprite[a].animation.plays == true) {
                    sprite[a].getAnimation().nextFrame();
                }
                g2d.drawImage(sprite[a].img, sprite[a].posx + ((sprite[a].flipH - 1) / (-2)) * sprite[a].size.width - camera.position.x + camera.center.x, sprite[a].posy + ((sprite[a].flipV - 1) / (-2)) * sprite[a].size.height - camera.position.y + camera.center.y, sprite[a].posx + sprite[a].size.width * sprite[a].flipH + ((sprite[a].flipH - 1) / (-2)) * sprite[a].size.width - camera.position.x + camera.center.x, sprite[a].posy + sprite[a].size.height * sprite[a].flipV + ((sprite[a].flipV - 1) / (-2)) * sprite[a].size.height - camera.position.y + camera.center.y, sprite[a].getAnimation().col * sprite[a].size.width, sprite[a].getAnimation().row * sprite[a].size.height, (sprite[a].getAnimation().col + 1) * sprite[a].size.width, (sprite[a].getAnimation().row + 1) * sprite[a].size.height, this);
                a++;
            }
        } catch (Exception e) {
            g2d.drawString("Error drawing a Sprite", 20, 20);
        }
        g2d.drawImage(coinSpriteSheet, 16, 16, 32, 32, 0, 0, 16, 16, this);
        g2d.setColor(Color.BLACK);
        g2d.drawString("x " + collectedCoins, 32, 30);
        g2d.setColor(Color.WHITE);
        g2d.drawString("x " + collectedCoins, 32, 29);
        if (showSpritePos == true) {
            for (int i = 0; i < numberOfSprites; i++) {
                g2d.setColor(Color.red);
                g2d.drawRect(sprite[i].posx - camera.position.x + camera.center.x, sprite[i].posy - camera.position.y + camera.center.y, 1, 1);
                g2d.setColor(Color.black);
            }
        }
        if (showSpriteNum == true) {
            for (int i = 0; i < numberOfSprites; i++) {
                g2d.setColor(Color.black);
                g2d.drawString("" + i, sprite[i].posx - camera.position.x + camera.center.x, sprite[i].posy - camera.position.y + camera.center.y);
                g2d.setColor(Color.white);
                g2d.drawString("" + i, sprite[i].posx - camera.position.x + camera.center.x, sprite[i].posy - camera.position.y + camera.center.y - 1);
            }
        }
        if (showSpritePos == true) {
            for (int i = 0; i < numberOfTiles; i++) {
                g2d.setColor(Color.red);
                g2d.drawRect(tile[i].posx + ((tile[i].flipH - 1) / (-2)) * tile[i].size.width - camera.position.x + camera.center.x, tile[i].posy + ((tile[i].flipV - 1) / (-2)) * tile[i].size.height - camera.position.y + camera.center.y, 1, 1);
                g2d.setColor(Color.black);
            }
        }
        if (showCamera == true) {
            g2d.setColor(Color.red);
            g2d.drawLine(0, camera.prefHeight - camera.position.y + camera.center.y, loadedLevel.getWidth() * 16, camera.prefHeight - camera.position.y + camera.center.y);
            g2d.setColor(new Color(1, 0, 0, 0.33f));
            g2d.fillRect(0, camera.prefHeight - camera.position.y + camera.tolerance, loadedLevel.getWidth() * 16, camera.tolerance);
            g2d.setColor(new Color(0, 1, 0, 0.33f));
            g2d.fillRect(camera.center.x - camera.position.x + camera.center.x, camera.center.y - camera.position.y + camera.center.y, camera.bounds.width - 2 * camera.center.x, camera.bounds.height);
            g2d.setColor(Color.green);
            g2d.drawLine(camera.center.x - camera.position.x + camera.center.x, 0, camera.center.x - camera.position.x + camera.center.x, 999);
            g2d.drawLine(camera.bounds.width - camera.center.x - camera.position.x + camera.center.x, 0, camera.bounds.width - camera.center.x - camera.position.x + camera.center.x, 999);
        }
        return renderImage;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (antialiasing == true) {
            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHints(rh);
        }
        g2d.drawImage(renderImage, 0, 0, width, height, 0, 0, resolution.width, resolution.height, this);
        g2d.setColor(Color.yellow);
        g2d.drawString("FPS: " + fps, 20, 20);
    }

    public static File FileOpenDialog(String title) {
        FileDialog filedialog = new FileDialog((Frame) null, title, FileDialog.LOAD);
        filedialog.setVisible(true);
        File fileSelected = null;
        try {
            fileSelected = new File(filedialog.getDirectory(), filedialog.getFile());
        } catch (Exception e) {
        }
        return fileSelected;
    }

    public static File FileSaveDialog(String title) {
        FileDialog filedialog = new FileDialog((Frame) null, title, FileDialog.SAVE);
        filedialog.setVisible(true);
        File fileSelected = null;
        try {
            fileSelected = new File(filedialog.getDirectory(), filedialog.getFile());
        } catch (Exception e) {
        }
        return fileSelected;
    }

    public static void loadLevel(File levelFile) {
        loadedLevel.clean();
        if (levelFile != null) {
            GameObject[] go = new GameObject[0];
            try {
                loadedLevel = new Level(levelFile.getPath());
                camera = loadedLevel.getCamera();
                go = loadedLevel.getGameObjects();
            } catch (Exception e) {
                System.out.println(e);
            }
            numberOfBoxes = 0;
            numberOfSprites = 0;
            numberOfTiles = 0;
            for (int i = 0; i < actor.length; i++) {
                actor[i] = null;
            }
            backgroundImage = new Image[2];
            try {
                tileSheet = ImageIO.read(new File(loadedLevel.levelName + "/tilesheet.png"));
                backgroundImage[0] = ImageIO.read(new File(loadedLevel.levelName + "/bg0.png"));
                backgroundImage[1] = ImageIO.read(new File(loadedLevel.levelName + "/bg1.png"));
            } catch (Exception e) {
                System.out.println("ERROR loading images: " + e);
            }
            int MapWidth = loadedLevel.getWidth();
            int MapHeight = loadedLevel.getHeight();
            for (int y = 0; y < MapHeight; y++) {
                for (int x = 0; x < MapWidth; x++) {
                    char CharAtXY = loadedLevel.level.substring(MapWidth * y, loadedLevel.level.length()).charAt(x);
                    for (int i = 0; i < go.length; i++) {
                        if (CharAtXY == go[i].objectChar) {
                            try {
                                invoke("game.objects." + go[i].name, "new" + go[i].name, new Class[] { Point.class }, new Object[] { new Point(x * 16, y * 16) });
                            } catch (Exception e) {
                                System.out.println("ERROR trying to invoke method: " + e);
                            }
                        }
                    }
                    if ((int) CharAtXY >= 48 && (int) CharAtXY <= 57) {
                        tileObject[gameMain.numberOfTiles] = new WorldTile(Integer.parseInt(CharAtXY + ""));
                        tileObject[gameMain.numberOfTiles - 1].sprite.setPosition(x * 16, y * 16);
                    }
                }
            }
            loadedLevel.clean();
            camera.forceSetPosition(new Point(mario.spawn.x, camera.prefHeight));
            pCoin = new PopupCoin(new Point(-80, -80));
            levelLoaded = true;
        } else {
            System.out.println("Loading cancelled...");
        }
    }

    public static void invoke(String aClass, String aMethod, Class[] params, Object[] args) throws Exception {
        Class c = Class.forName(aClass);
        Constructor constructor = c.getConstructor(params);
        Method m = c.getDeclaredMethod(aMethod, params);
        Object i = constructor.newInstance(args);
        Object r = m.invoke(i, args);
    }
}
