package sourceforge.shinigami.pseudogame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import sourceforge.shinigami.graphics.Texture;
import sourceforge.shinigami.gui.SWindow;

public class Level extends SWindow {

    private final int TILESX, TILESY;

    private final int WIDTH, HEIGHT;

    public static final int CORRECTION = 38;

    public static final int EASY_MODE = 1;

    public static final int MEDIUM_MODE = 2;

    public static final int HARD_MODE = 3;

    private int[][][] waves;

    private int initialCash;

    private int initialLife;

    private Texture mapTexture;

    private TowerMap map;

    protected final Tower[] towers;

    protected final Foe[] foes;

    private int updateCounter = 1;

    private int waveDuration = 700;

    private int waveCounter = 1;

    private Texture selection = new Texture("Database/PseudoGame/Selection.png");

    private Texture redX = new Texture("Database/PseudoGame/RedX.png");

    private Texture noCash = new Texture("Database/PseudoGame/Buttons/NoCash.png");

    PGButton buttons[] = { new PGButton(new Texture("Database/PseudoGame/Buttons/NotCreateTower01.png"), new Texture("Database/PseudoGame/Buttons/NotCreateTowerClicked01.png"), null), new PGButton(new Texture("Database/PseudoGame/Buttons/CreateTower01.png"), new Texture("Database/PseudoGame/Buttons/CreateTowerClicked01.png"), null), new PGButton(new Texture("Database/PseudoGame/Buttons/CreateTower02.png"), new Texture("Database/PseudoGame/Buttons/CreateTowerClicked02.png"), null), new PGButton(new Texture("Database/PseudoGame/Buttons/CreateTower03.png"), new Texture("Database/PseudoGame/Buttons/CreateTowerClicked03.png"), null) };

    private boolean removingTower = false;

    int mapStartX = 0;

    int mapStartY = 0;

    private int markx;

    private int marky;

    private boolean almostWon = false;

    private static Level currentLevel;

    private static int mode = MEDIUM_MODE;

    protected boolean won = false;

    private boolean goingToNextLevel = false;

    protected boolean lost = false;

    private boolean isQuitting = false;

    private int sendingCounter = 0;

    private Foe[] waveFoes;

    private int[] numberOfFoes;

    public Level(int life, int cash, int[][][] waves, Texture mapTexture) {
        super();
        this.waves = waves;
        initialLife = life;
        initialCash = cash;
        TILESX = 9;
        TILESY = 10;
        WIDTH = TILESX * Tower.WIDTH;
        HEIGHT = TILESY * Tower.HEIGHT;
        map = new TowerMap(TILESX, TILESY, new Player(initialLife, initialCash));
        this.hasBar(false);
        this.hasClose(false);
        this.mapTexture = mapTexture;
        this.mapTexture = new Texture(this.mapTexture.getImage().getScaledInstance(WIDTH + 150, HEIGHT, Image.SCALE_DEFAULT));
        new PGProperties();
        towers = PGProperties.getTowers();
        foes = PGProperties.getEnemies();
        buttons[0].setPosition(700, 150);
        buttons[0].setSize(48, 48);
        buttons[1].setPosition(700, 240);
        buttons[1].setSize(48, 48);
        buttons[2].setPosition(700, 330);
        buttons[2].setSize(48, 48);
        buttons[3].setPosition(700, 420);
        buttons[3].setSize(48, 48);
        for (PGButton b : buttons) this.addMouseListener(b);
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    @Override
    public void render(Graphics2D g, int adjustX, int adjustY) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        markx = (mouseX + mapStartX - CORRECTION) / Tower.WIDTH;
        marky = (mouseY + mapStartY) / Tower.HEIGHT;
        mapTexture.render(g, 0 - mapStartX, 0 - mapStartY);
        map.render(g, mapStartX - CORRECTION, mapStartY);
        if (map.getBeingPlaced() != null || removingTower) {
            if (markx < TILESX) {
                if (removingTower) if (!map.hasTower(markx, marky)) {
                    redX.render(g, (markx * Tower.WIDTH) + CORRECTION, marky * Tower.HEIGHT);
                } else {
                    selection.render(g, (markx * Tower.WIDTH) + CORRECTION, marky * Tower.HEIGHT);
                } else {
                    if (map.hasCashToBuild()) {
                        if (map.hasTower(markx, marky)) {
                            redX.render(g, (markx * Tower.WIDTH) + CORRECTION, marky * Tower.HEIGHT);
                        } else {
                            map.getBeingPlaced().render(g, (markx * Tower.WIDTH) + CORRECTION, marky * Tower.HEIGHT);
                            selection.render(g, (markx * Tower.WIDTH) + CORRECTION, marky * Tower.HEIGHT);
                        }
                    } else {
                        noCash.render(g, (markx * Tower.WIDTH) + CORRECTION, marky * Tower.HEIGHT);
                    }
                }
            }
        }
        for (PGButton b : buttons) b.render(g, 0, 0);
        g.setColor(Color.BLACK);
        g.drawString("HotKey: V", 700, 130);
        g.drawString("Vender calouro", 700, 140);
        g.drawString("(metade do pre�o)", 700, 151);
        g.drawString("HotKey: 1", 700, 220);
        g.drawString("Criar calouro", 700, 230);
        g.drawString("(" + towers[1].getPrice() + " cash)", 700, 241);
        g.drawString("HotKey: 2", 700, 310);
        g.drawString("Criar calouro forte", 700, 320);
        g.drawString("(" + towers[2].getPrice() + " cash)", 700, 331);
        g.drawString("HotKey: 3", 700, 400);
        g.drawString("Criar calouro especial", 700, 410);
        g.drawString("(" + towers[3].getPrice() + " cash)", 700, 421);
        g.setColor(Color.RED);
        g.fillRect(700, 500, 50, 10);
        g.setColor(Color.GREEN);
        g.fillRect(700, 500, (int) ((float) 50 / ((float) waveDuration / (float) (waveCounter % waveDuration))), 10);
        if (markx < TILESX) if (map.getTile(markx, marky) != null) {
            g.setColor(Color.RED);
            g.drawOval(((markx * Tower.WIDTH) - (mapStartX - CORRECTION)) - map.getTile(markx, marky).getRange() + (Tower.WIDTH / 2), ((marky * Tower.HEIGHT) - mapStartY) - map.getTile(markx, marky).getRange() + (Tower.HEIGHT / 2), map.getTile(markx, marky).getRange() * 2, map.getTile(markx, marky).getRange() * 2);
        } else if (map.getBeingPlaced() != null) {
            g.setColor(Color.RED);
            g.drawOval(((markx * Tower.WIDTH) - (mapStartX - CORRECTION)) - map.getBeingPlaced().getRange() + (Tower.WIDTH / 2), ((marky * Tower.HEIGHT) - mapStartY) - map.getBeingPlaced().getRange() + (Tower.HEIGHT / 2), map.getBeingPlaced().getRange() * 2, map.getBeingPlaced().getRange() * 2);
        }
        g.setColor(Color.BLACK);
    }

    @Override
    public void update() {
        super.update();
        if (numberOfFoes != null && sendingCounter % 10 == 0) {
            for (int x = 0; x <= numberOfFoes.length; x++) {
                if (x == numberOfFoes.length) {
                    numberOfFoes = null;
                    break;
                }
                if (numberOfFoes[x] > 0) {
                    sendFoe(waveFoes[x]);
                    numberOfFoes[x]--;
                    break;
                }
            }
        }
        sendingCounter++;
        map.update();
        if (!won && !lost) {
            for (int x = 0; x < buttons.length; x++) {
                buttons[x].update();
            }
            if (buttons[1].mouseDown()) {
                removingTower = false;
                map.setBeingPlaced(towers[1]);
                buttons[1].processedMouseDown();
            } else if (buttons[2].mouseDown()) {
                removingTower = false;
                map.setBeingPlaced(towers[2]);
                buttons[2].processedMouseDown();
            } else if (buttons[3].mouseDown()) {
                removingTower = false;
                map.setBeingPlaced(towers[3]);
                buttons[3].processedMouseDown();
            } else if (buttons[0].mouseDown()) {
                map.towerPlaced();
                removingTower = true;
                buttons[0].mouseDown();
            }
            if (almostWon && map.hasNoFoes() && !lost) {
                almostWon = false;
                won = true;
            } else for (int x = 0; x < waves[mode - 1].length + 1; x++) {
                if (waveCounter / waveDuration == x + 1 && waveCounter % waveDuration == 0) {
                    if (x == waves[mode - 1].length) {
                        almostWon = true;
                        break;
                    }
                    this.sendWave(waves[mode - 1][x].clone(), foes);
                    waveCounter++;
                }
            }
            if (waveCounter / waveDuration == 0 || !isSendingWave()) waveCounter++;
            updateCounter++;
            if (map.getPlayer().getlifes() == 0 && !won) {
                lost = true;
            }
        }
    }

    public static void setMode(int difficulty) {
        mode = difficulty;
    }

    public static int getMode() {
        return mode;
    }

    public static void setCurrentLevel(Level level) {
        currentLevel = level;
    }

    public static void initCurrentLevel() {
        currentLevel.off();
        currentLevel.setPosition(0, 0);
        currentLevel.setSize(800, 600);
        currentLevel.initGraphics();
    }

    public static Level getCurrentLevel() {
        return currentLevel;
    }

    public boolean isSendingWave() {
        return numberOfFoes != null;
    }

    public boolean hasWon() {
        return won;
    }

    public boolean hasLost() {
        return lost;
    }

    public boolean goingToNextLevel() {
        return goingToNextLevel;
    }

    public void processedNextLevel() {
        goingToNextLevel = false;
    }

    public boolean isQuitting() {
        return isQuitting;
    }

    public void processedQuit() {
        isQuitting = false;
    }

    public void sendWave(int[] numberOfFoes, Foe[] foes) {
        this.waveFoes = foes;
        this.numberOfFoes = numberOfFoes;
    }

    public void sendFoe(Foe f) {
        map.createFoe(9, (int) (10 * Math.random()), f);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) isQuitting = true; else if (e.getKeyCode() == KeyEvent.VK_1) {
            removingTower = false;
            map.setBeingPlaced(towers[1]);
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            removingTower = false;
            map.setBeingPlaced(towers[2]);
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            removingTower = false;
            map.setBeingPlaced(towers[3]);
        } else if (e.getKeyCode() == KeyEvent.VK_V) {
            map.setBeingPlaced(null);
            removingTower = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            map.setBeingPlaced(null);
            removingTower = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    int mouseX = 0;

    int mouseY = 0;

    boolean mouseDown = false;

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        mouseDown = true;
        if (lost) isQuitting = true;
        if (won) goingToNextLevel = true;
        if (markx < TILESX && map.getBeingPlaced() != null && !map.hasTower(markx, marky)) {
            map.placeTower(markx, marky);
        }
        if (removingTower && markx < TILESX && map.hasTower(markx, marky)) {
            map.removeTower(markx, marky);
            removingTower = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        mouseDown = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        super.keyTyped(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
    }

    @Override
    public void setListeners() {
        for (PGButton b : buttons) {
            addMouseListener(b);
        }
    }

    public void restartLevel() {
        updateCounter = waveCounter = 1;
        sendingCounter = 0;
        map = new TowerMap(TILESX, TILESY, new Player(initialLife, initialCash));
        almostWon = false;
        won = false;
        lost = false;
    }

    public static class Builder {

        private int[][][] waves;

        private Texture mapTexture;

        private int life;

        private int cash;

        public Builder(Texture mapTexture) {
            this.mapTexture = mapTexture;
        }

        public Builder setWaves(int[][][] waves) {
            this.waves = waves;
            return this;
        }

        public Builder setLife(int life) {
            this.life = life;
            return this;
        }

        public Builder setCash(int cash) {
            this.cash = cash;
            return this;
        }

        public Level build() {
            return new Level(life, cash, waves, mapTexture);
        }
    }
}
