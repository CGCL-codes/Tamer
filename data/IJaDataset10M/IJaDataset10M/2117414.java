package MyButton;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import wiiusej.WiiUseApiManager;
import wiiusej.Wiimote;

public class StageHandler implements ActionListener {

    private static int hitSpell;

    private static int hitSpellP1;

    private static int morphMeter;

    private static int morphMeterP1 = -200;

    private static int suicide;

    private static int suicideP1;

    private Wiimote[] wiimotes;

    public InputAdapter ia = new InputAdapter(), iaP1 = new InputAdapter();

    private static ArrayList<Kogel> kogels = new ArrayList<Kogel>();

    private ArrayList<String> input = new ArrayList<String>();

    private ArrayList<Hill> hill = new ArrayList<Hill>();

    private ArrayList<Shape> solidobjects = new ArrayList<Shape>();

    private ArrayList<Spell> spell = new ArrayList<Spell>();

    private boolean rumbleActive = false, rumbleActiveP1 = false, onground = false;

    private boolean objectdetect = false, dubblejumppossible = false;

    private boolean BGanirepeat = false;

    private Random rdm = new Random();

    static int PlayerSizeYP2 = 100, PlayerSizeXP2 = 100, PlayerSizeXP1 = 160, PlayerSizeYP1 = 160;

    static int xcorrectionHill = 0;

    static int jumpcount, count = 0, spellcount = -20, spellSeparate = 10, rumblestart, rumblestartP1, countlimit = 100000;

    static int hillamount = 15, hillXmove = -1, ahillXmove = 5, hillspread = 150;

    static int BGnr = 8, BGAnimcnt = 1300, playerswitch = 0, spellnr = 1, spellchangecnt = 0;

    public int deletedHills = 0;

    public static double x = 0, y = 0, xP1 = 1280 / 2, yP1 = 90, speedx = 0, speedy = 0, speedxP1 = 0, speedyP1 = 0, frictiony = 0.5, frictionxP1, frictionx = 0.5, speedlimit = 4, speedlimitP1X = 3, speedlimitP1Y = 2;

    public static double acceleration = 1.0, accelerationP1 = 0.6, rumbletime, rumbletimeP1, gravity, gravityG = 1.04, slowdownNr = 1.0;

    double ManaMax = 100, ManaPwr = ManaMax, ManaGen = 0.40;

    double MorfLimit = 500, MorfPwr = MorfLimit, MorfGen = -0.5;

    private TexturePaint blocksTxtr;

    public static float jsAngle, jsMagnitude;

    public float ncGX, ncGY, ncGZ;

    private static int cx, cy, firelinex = 0, fireliney = 0, nunX, nunY;

    public static float jsAngleP1, jsMagnitudeP1;

    public float ncGXP1, ncGYP1, ncGZP1;

    private static int cxP1, cyP1, firelinexP1 = 0, firelineyP1 = 0, nunXP1, nunYP1;

    SoundPlayer soundjump = new SoundPlayer();

    SoundPlayer soundhurt = new SoundPlayer();

    SoundPlayer soundswitch = new SoundPlayer();

    SoundPlayer soundsquish = new SoundPlayer();

    MusicPlayer BGMusic = new MusicPlayer();

    private Image background, BGanimation, Spellicon;

    BufferedImage BGTxt1, BGTxt2, BGTxt3, BGTxt4, BGTxt5, BGTxt6, BGTxt7, BGTxt8;

    BufferedImage magicspell1;

    BufferedImage blockTxtrImage;

    BufferedImage BG1, BG2, BG3, BG4, BG5, BG6, BG7, BG8;

    BufferedImage BGani1, BGani2, BGani3, BGani4, BGani5, BGani6, BGani7, BGani8;

    BufferedImage WandWitch, WandWizzard;

    BufferedImage Spellicon1, Spellicon2, Spellicon3, Spellicon4, Spellicon5, Spellicon6, Spellicon7, Spellicon8;

    BufferedImage magicshield, magicshield1;

    Timer timer = new Timer(1000 / 90, this);

    public Bunny bunny = new Bunny((int) x, (int) y, "");

    public Frog frog = new Frog((int) x, (int) y, "");

    public Witch witch = new Witch((int) xP1, (int) yP1, "");

    ;

    public Wizard wizard = new Wizard((int) xP1, (int) yP1, "");

    private String direction = "RIGHT";

    private int gameCount;

    public ArrayList<Fireball> fireballsArray = new ArrayList<Fireball>();

    public static int stageChecker = 0;

    public int getPlayer1XCoordinaat() {
        return (int) xP1;
    }

    public int getPlayer1YCoordinaat() {
        return (int) yP1;
    }

    public PowerUP power = new PowerUP(rdm.nextInt(1200), -1000, "SE");

    ArrayList<PowerUP> powers = new ArrayList<PowerUP>();

    public StageHandler() {
        BGAniBalls();
    }

    public void startTimer() {
        timer.start();
        bunny.readFace();
        wizard.readFace();
        frog.readFace();
        witch.readFace();
        BGMusic.playMusic("src\\BackgroundMusic.wav");
    }

    public void actionPerformed(ActionEvent arg0) {
        if ((count % 400) == 0) {
            RandomEnvoirment(rdm.nextInt(BGnr) + 1);
        }
        if (count % (90 * 150) == 0 && count != 0) {
            timer.stop();
            new EndGame();
        } else {
            power.update();
            resetPowerUP(false);
            if (count < countlimit) {
                count++;
            } else {
                count = 0;
            }
            hill();
            checkFallP2();
            checkInput();
            checkInputP1();
            updateParticls();
            morfSecCounter();
            gravity();
            spellsCheck();
            ManaGen();
            MorfGen();
            roundupP1();
            roundupP2();
            if (gameCount % 25 == 0) {
                witch.update();
                wizard.update();
            }
            gameCount++;
            if (gameCount >= 100000) {
                gameCount = 0;
            }
        }
    }

    public void draw(Graphics g) {
        if (BGAnimcnt > -1400) {
            BGAnimcnt -= ahillXmove / 5;
        } else {
            BGAnimcnt = 1400;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, 1280, 720, null);
        g2.drawImage(BGanimation, BGAnimcnt, 0, 1280, 720, null);
        if (BGanirepeat) {
            for (int BGAniCnt = -5; BGAniCnt < 5; BGAniCnt++) {
                g2.drawImage(BGanimation, BGAnimcnt + (BGAniCnt * 500), 0, 1280, 720, null);
            }
        }
        blocksTxtr = new TexturePaint(blockTxtrImage, new Rectangle2D.Double(hillXmove, 0, blockTxtrImage.getWidth(), blockTxtrImage.getHeight()));
        g2.setPaint(blocksTxtr);
        for (Shape s : solidobjects) {
            g2.draw(s);
            g2.fill(s);
        }
        g2.setColor(new Color((25), (137), (224), 65));
        Rectangle.Double ManaBarBorder = new Rectangle.Double(8, 8, ((1280 / 2) - 158) * (ManaMax / ManaMax) + 4, 30 + 4);
        g2.fill(ManaBarBorder);
        g2.setColor(new Color((25), (137), (224), 255));
        Rectangle.Double ManaBar = new Rectangle.Double(10, 10, ((1280 / 2) - 160) * (ManaPwr / ManaMax), 30);
        g2.fill(ManaBar);
        g2.setColor(new Color((38), (9), (71), 65));
        Rectangle.Double MorfBarBorder = new Rectangle.Double((1280 - 27) - (MorfLimit / MorfLimit) * MorfLimit, 8, MorfLimit + 4, 30 + 4);
        g2.fill(MorfBarBorder);
        g2.setColor(new Color((38), (9), (71), 255));
        Rectangle.Double MorfBar = new Rectangle.Double((1280 - 25) - (MorfPwr / MorfLimit) * MorfLimit, 10, (MorfPwr / MorfLimit) * MorfLimit, 30);
        g2.fill(MorfBar);
        g2.drawImage(Spellicon, 10, 50, 50, 50, null);
        g2.setFont(new Font("Calibri", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("Mana", 10, 38);
        g2.drawString("MorphO-Meter", 1124, 38);
        power.draw(g);
        for (Kogel k : kogels) {
            k.draw(g);
        }
        for (Spell k : spell) {
            g2.drawImage(magicspell1, (int) k.xCor, (int) k.yCor, (int) k.xSize, (int) k.ySize, null);
        }
        nunXP1 = (int) (x + firelinexP1);
        nunYP1 = (int) (y + firelineyP1);
        if (!((firelinexP1 < 2 && firelinexP1 > -2) && (firelineyP1 < 2 && firelineyP1 > -2))) {
            for (int cnt = -25; cnt < 25; cnt += 5) {
            }
        }
        nunX = (int) (xP1 + firelinex);
        nunY = (int) (yP1 + fireliney);
        if (!((firelinex < 2 && firelinex > -2) && (fireliney < 2 && fireliney > -2))) {
            addprtcls(nunX, nunY, 1, false, 46, 39, 170, -3);
            addprtcls(nunX + (126 / 2), nunY + (117 / 2), 1, false, 46, 39, 170, -3);
            if (playerswitch % 2 == 1) {
                g2.drawImage(WandWizzard, nunX - (63 / 2), nunY - (53 / 2), 63, 53, null);
            } else {
                g2.drawImage(WandWitch, nunX - (63 / 2), nunY - (53 / 2), 63, 53, null);
            }
        }
        if (playerswitch % 2 == 0) {
            frog.x = (int) x;
            frog.y = (int) y;
            witch.x = (int) xP1 - 80;
            witch.y = (int) yP1 - 80;
            frog.draw(g);
            witch.draw(g);
        } else {
            bunny.x = (int) x;
            bunny.y = (int) y;
            wizard.x = (int) xP1 - 80;
            wizard.y = (int) yP1 - 80;
            bunny.draw(g);
            wizard.draw(g);
        }
        for (Fireball fireball : fireballsArray) {
            fireball.draw(g);
            Rectangle2D frog1 = new Rectangle2D.Double(x, y, frog.w, frog.h);
            Rectangle2D randomObject = new Rectangle2D.Double(fireball.x, fireball.y, 70, 70);
            if (frog1.intersects(randomObject)) {
                MorfPwrAdd(-10);
                fireball.x = 1000;
                fireball.y = 1000;
            } else {
            }
        }
    }

    public void BGAniBalls() {
        try {
            URL BGurl1 = getClass().getClassLoader().getResource("snowmoutain.jpg");
            URL BGurl2 = getClass().getClassLoader().getResource("landscapegrass.jpg");
            URL BGurl3 = getClass().getClassLoader().getResource("desert.jpg");
            URL BGurl4 = getClass().getClassLoader().getResource("vulcano1.jpg");
            URL BGurl5 = getClass().getClassLoader().getResource("lightning1.jpg");
            URL BGurl6 = getClass().getClassLoader().getResource("space.jpg");
            URL BGurl7 = getClass().getClassLoader().getResource("water.jpg");
            URL BGurl8 = getClass().getClassLoader().getResource("FunkyTime.jpg");
            URL BGAniurl1 = getClass().getClassLoader().getResource("snowAnimation.png");
            URL BGAniurl2 = getClass().getClassLoader().getResource("grasslandAnimation.png");
            URL BGAniurl3 = getClass().getClassLoader().getResource("desertAnimation.png");
            URL BGAniurl4 = getClass().getClassLoader().getResource("volcanoAnimation.png");
            URL BGAniurl5 = getClass().getClassLoader().getResource("lightningAnimation.png");
            URL BGAniurl6 = getClass().getClassLoader().getResource("spaceAnimation.png");
            URL BGAniurl7 = getClass().getClassLoader().getResource("waterAnimation.png");
            URL BGAniurl8 = getClass().getClassLoader().getResource("FunkyTimeAnimation.png");
            URL BlokTxt1 = getClass().getClassLoader().getResource("Ice.jpg");
            URL BlokTxt2 = getClass().getClassLoader().getResource("Sand.jpg");
            URL BlokTxt3 = getClass().getClassLoader().getResource("desertSand.jpg");
            URL BlokTxt4 = getClass().getClassLoader().getResource("LavaRock.jpg");
            URL BlokTxt5 = getClass().getClassLoader().getResource("RockSmooth1.jpg");
            URL BlokTxt6 = getClass().getClassLoader().getResource("SpaceRock.jpg");
            URL BlokTxt7 = getClass().getClassLoader().getResource("Waterplants.jpg");
            URL BlokTxt8 = getClass().getClassLoader().getResource("Tiles.jpg");
            URL magicurl1 = getClass().getClassLoader().getResource("magicball.png");
            URL WandWitchurl = getClass().getClassLoader().getResource("magicwitch.png");
            URL WandWizzardurl = getClass().getClassLoader().getResource("magicwizzard.png");
            URL spellicon1url = getClass().getClassLoader().getResource("spellicon1.png");
            URL spellicon2url = getClass().getClassLoader().getResource("spellicon2.png");
            URL spellicon3url = getClass().getClassLoader().getResource("spellicon3.png");
            URL spellicon4url = getClass().getClassLoader().getResource("spellicon4.png");
            URL spellicon5url = getClass().getClassLoader().getResource("spellicon5.png");
            URL spellicon6url = getClass().getClassLoader().getResource("spellicon6.png");
            URL spellicon7url = getClass().getClassLoader().getResource("spellicon7.png");
            URL spellicon8url = getClass().getClassLoader().getResource("spellicon8.png");
            URL shieldurl = getClass().getClassLoader().getResource("magicshield.png");
            URL shield1url = getClass().getClassLoader().getResource("magicshield1.png");
            BG1 = ImageIO.read(BGurl1);
            BG2 = ImageIO.read(BGurl2);
            BG3 = ImageIO.read(BGurl3);
            BG4 = ImageIO.read(BGurl4);
            BG5 = ImageIO.read(BGurl5);
            BG6 = ImageIO.read(BGurl6);
            BG7 = ImageIO.read(BGurl7);
            BG8 = ImageIO.read(BGurl8);
            BGani1 = ImageIO.read(BGAniurl1);
            BGani2 = ImageIO.read(BGAniurl2);
            BGani3 = ImageIO.read(BGAniurl3);
            BGani4 = ImageIO.read(BGAniurl4);
            BGani5 = ImageIO.read(BGAniurl5);
            BGani6 = ImageIO.read(BGAniurl6);
            BGani7 = ImageIO.read(BGAniurl7);
            BGani8 = ImageIO.read(BGAniurl8);
            BGTxt1 = ImageIO.read(BlokTxt1);
            BGTxt2 = ImageIO.read(BlokTxt2);
            BGTxt3 = ImageIO.read(BlokTxt3);
            BGTxt4 = ImageIO.read(BlokTxt4);
            BGTxt5 = ImageIO.read(BlokTxt5);
            BGTxt6 = ImageIO.read(BlokTxt6);
            BGTxt7 = ImageIO.read(BlokTxt7);
            BGTxt8 = ImageIO.read(BlokTxt8);
            magicspell1 = ImageIO.read(magicurl1);
            WandWitch = ImageIO.read(WandWitchurl);
            WandWizzard = ImageIO.read(WandWizzardurl);
            Spellicon1 = ImageIO.read(spellicon1url);
            Spellicon = Spellicon1;
            Spellicon2 = ImageIO.read(spellicon2url);
            Spellicon3 = ImageIO.read(spellicon3url);
            Spellicon4 = ImageIO.read(spellicon4url);
            Spellicon5 = ImageIO.read(spellicon5url);
            Spellicon6 = ImageIO.read(spellicon6url);
            Spellicon7 = ImageIO.read(spellicon7url);
            Spellicon8 = ImageIO.read(spellicon8url);
            magicshield = ImageIO.read(shieldurl);
            magicshield1 = ImageIO.read(shield1url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void resetPowerUP(boolean hit) {
        if (power.y >= 700 || hit) {
            power.x = rdm.nextInt(1200);
            power.y = -1000;
        }
    }

    private void spellsCheck() {
        for (Iterator<Spell> itr = spell.iterator(); itr.hasNext(); ) {
            Spell k = itr.next();
            if (k.yCor > 720 || k.yCor < 0 || k.xCor < 0 || k.xCor > 1280) {
                itr.remove();
            } else if ((k.xCor + (k.xSize / (3 / 4)) > x && k.yCor + (k.ySize / (3 / 4)) > y && k.xCor + (k.xSize / (3 / 4)) < x + PlayerSizeXP2 && k.yCor + (k.ySize / (3 / 4)) < y + PlayerSizeYP2) || (k.xCor + (k.xSize / 4) > x && k.yCor + (k.ySize / 4) > y && k.xCor + (k.xSize / 4) < x + PlayerSizeXP2 && k.yCor + (k.ySize / 4) < y + PlayerSizeYP2) || (k.xCor + (k.xSize / 2) > x && k.yCor + (k.ySize / 2) > y && k.xCor + (k.xSize / 2) < x + PlayerSizeXP2 && k.yCor + (k.ySize / 2) < y + PlayerSizeYP2) || (k.xCor >= x && k.yCor >= y && k.xCor <= x + PlayerSizeXP2 && k.yCor <= y + PlayerSizeYP2) || (k.xCor + k.xSize >= x && k.yCor + k.ySize >= y && k.xCor + k.xSize <= x + PlayerSizeXP2 && k.yCor + k.ySize <= y + PlayerSizeYP2)) {
                spellHit(k.spellnr);
                itr.remove();
                if (playerswitch % 2 == 0) {
                    hitSpell++;
                } else hitSpellP1++;
            } else {
                k.update();
            }
        }
    }

    private void MorfGen() {
        if (MorfPwr + MorfGen >= 0) {
            MorfPwr += MorfGen;
        } else {
            playerswitch();
        }
    }

    public static int getNanX() {
        return nunX;
    }

    public static int getNanY() {
        return nunY;
    }

    public static int getStageChecker() {
        return stageChecker;
    }

    private void playerswitch() {
        playerswitch++;
        MorfPwr = MorfLimit;
        ManaPwr = ManaMax;
        spell.clear();
        reset(PlayerSizeXP2 + 10, PlayerSizeYP2 + 40, false);
        resetP1(1280 / 2, 90);
    }

    private void ManaGen() {
        if (ManaPwr + ManaGen < ManaMax) {
            ManaPwr += ManaGen;
        }
    }

    public void spell(int spellNr) {
        int cost;
        if (count > spellcount + spellSeparate) {
            switch(spellNr) {
                case 1:
                    cost = 10;
                    if ((ManaPwr - cost) > 0) {
                        ManaPwr -= cost;
                        spell.add(new Spell(1));
                        spellSeparate = 3;
                    }
                    break;
                case 2:
                    cost = 20;
                    if ((ManaPwr - cost) > 0) {
                        ManaPwr -= cost;
                        spell.add(new Spell(2));
                        spellSeparate = 10;
                    }
                    break;
                case 3:
                    cost = 30;
                    if ((ManaPwr - cost) > 0) {
                        ManaPwr -= cost;
                        spell.add(new Spell(3));
                        spell.add(new Spell(9));
                        spell.add(new Spell(10));
                        spellSeparate = 20;
                    }
                    break;
                case 4:
                    cost = 30;
                    if ((ManaPwr - cost) > 0) {
                        ManaPwr -= cost;
                        spell.add(new Spell(4));
                        spell.add(new Spell(11));
                        spell.add(new Spell(12));
                        spellSeparate = 20;
                    }
                    break;
                case 5:
                    cost = 55;
                    if ((ManaPwr - cost) > 0) {
                        ManaPwr -= cost;
                        spell.add(new Spell(5));
                        spellSeparate = 30;
                    }
                    break;
                case 6:
                    cost = 40;
                    if ((ManaPwr - cost) > 0) {
                        ManaPwr -= cost;
                        spell.add(new Spell(6));
                        spellSeparate = 20;
                    }
                    break;
                case 7:
                    cost = 40;
                    if ((ManaPwr - cost) > 0) {
                        ManaPwr -= cost;
                        spell.add(new Spell(7));
                        spellSeparate = 20;
                    }
                    break;
                case 8:
                    cost = 55;
                    if ((ManaPwr - cost) > 0) {
                        ManaPwr -= cost;
                        spell.add(new Spell(8));
                        spellSeparate = 30;
                    }
                    break;
            }
            spellcount = count;
        }
    }

    public void spellHit(int spellNr) {
        soundhurt.playSound(2);
        switch(spellNr) {
            case 1:
                MorfPwrAdd(10);
                break;
            case 2:
                MorfPwrAdd(40);
                break;
            case 3:
                MorfPwrAdd(70);
                break;
            case 4:
                MorfPwrAdd(70);
                break;
            case 5:
                MorfPwrAdd(70);
                break;
            case 6:
                MorfPwrAdd(100);
                break;
            case 7:
                MorfPwrAdd(100);
                break;
            case 8:
                MorfPwrAdd(200);
                break;
        }
    }

    private void MorfPwrAdd(int add) {
        if (MorfPwr + add <= MorfLimit) {
            MorfPwr += add;
        } else {
            MorfPwr = MorfLimit;
        }
    }

    public void RandomEnvoirment(int pick) {
        slowdownNr = 1.0;
        gravityG = 1.04;
        frictionx = 0.5;
        frictiony = 0.5;
        speedlimit = 4;
        acceleration = 1.0;
        switch(pick) {
            case 1:
                frictionx = 0.0001;
                Background(1);
                stageChecker = 1;
                break;
            case 2:
                speedlimit = 7;
                Background(2);
                stageChecker = 2;
                break;
            case 3:
                Background(3);
                stageChecker = 3;
                break;
            case 4:
                Background(4);
                stageChecker = 4;
                break;
            case 5:
                Background(5);
                stageChecker = 5;
                break;
            case 6:
                gravityG = 1.001;
                stageChecker = 6;
                Background(6);
                break;
            case 7:
                gravityG = 1.01;
                frictionx = 1.3;
                frictiony = 1.3;
                slowdownNr = 0.5;
                Background(7);
                stageChecker = 7;
                break;
            case 8:
                acceleration = -1.0;
                Background(8);
                stageChecker = 8;
                break;
        }
    }

    public void Background(int BG) {
        switch(BG) {
            case 1:
                blockTxtrImage = BGTxt1;
                BGanirepeat = false;
                BGanimation = BGani1;
                background = BG1;
                break;
            case 2:
                blockTxtrImage = BGTxt2;
                BGanirepeat = true;
                BGanimation = BGani2;
                background = BG2;
                break;
            case 3:
                blockTxtrImage = BGTxt3;
                BGanirepeat = true;
                BGanimation = BGani3;
                background = BG3;
                break;
            case 4:
                blockTxtrImage = BGTxt4;
                BGanirepeat = false;
                BGanimation = BGani4;
                background = BG4;
                break;
            case 5:
                blockTxtrImage = BGTxt5;
                BGanirepeat = true;
                BGanimation = BGani5;
                background = BG5;
                break;
            case 6:
                blockTxtrImage = BGTxt6;
                BGanirepeat = false;
                BGanimation = BGani6;
                background = BG6;
                break;
            case 7:
                blockTxtrImage = BGTxt7;
                BGanirepeat = false;
                BGanimation = BGani7;
                background = BG7;
                break;
            case 8:
                blockTxtrImage = BGTxt8;
                BGanirepeat = false;
                BGanimation = BGani8;
                background = BG8;
                break;
        }
    }

    public void checkInputP1() {
        int wiinr;
        if (!(playerswitch % 2 == 0)) {
            input = iaP1.getInput();
            wiinr = 1;
        } else {
            input = ia.getInput();
            wiinr = 0;
        }
        if (input.contains("UP") && input.contains("LEFT")) {
            accelurateYcheckP1(-(accelerationP1 / 2));
            accelurateXcheckP1(-(accelerationP1 / 2));
            checkDirection(witch, "LEFT");
            checkDirection(wizard, "LEFT");
        } else if (input.contains("UP") && input.contains("RIGHT")) {
            accelurateYcheckP1(-(accelerationP1 / 2));
            accelurateXcheckP1(+(accelerationP1 / 2));
            checkDirection(witch, "RIGHT");
            checkDirection(wizard, "RIGHT");
        } else if (input.contains("UP")) {
            accelurateYcheckP1(-accelerationP1);
            addP1prtcls();
        } else if (input.contains("DOWN") && input.contains("LEFT")) {
            accelurateYcheckP1(+(accelerationP1 / 2));
            accelurateXcheckP1(-(accelerationP1 / 2));
            checkDirection(witch, "LEFT");
            checkDirection(wizard, "LEFT");
        } else if (input.contains("DOWN") && input.contains("RIGHT")) {
            accelurateYcheckP1(+(accelerationP1 / 2));
            accelurateXcheckP1(+(accelerationP1 / 2));
            checkDirection(witch, "RIGHT");
            checkDirection(wizard, "RIGHT");
        } else if (input.contains("DOWN")) {
            accelurateYcheckP1(+accelerationP1);
        } else if (input.contains("LEFT")) {
            accelurateXcheckP1(-accelerationP1);
            checkDirection(witch, "LEFT");
            checkDirection(wizard, "LEFT");
        } else if (input.contains("RIGHT")) {
            accelurateXcheckP1(+accelerationP1);
            checkDirection(witch, "RIGHT");
            checkDirection(wizard, "RIGHT");
        } else {
            if (speedxP1 >= 0.1) {
                speedxP1 += -0.1;
            } else if (speedxP1 <= 0.1) {
                speedxP1 += 0.1;
            }
            if (speedyP1 >= 0.1) {
                speedyP1 += -0.1;
            } else if (speedyP1 <= 0.1) {
                speedyP1 += 0.1;
            }
        }
        addP1prtcls();
        if (!(playerswitch % 2 == 0)) {
            jsAngleP1 = ia.getJsAngle();
            jsMagnitudeP1 = ia.getJsMagnitude();
            if (input.contains("C") || input.contains("Z")) {
            }
            ncGXP1 = ia.getNcGX();
            ncGYP1 = ia.getNcGY();
            ncGZP1 = ia.getNcGZ();
        } else {
            jsAngleP1 = iaP1.getJsAngle();
            jsMagnitudeP1 = iaP1.getJsMagnitude();
            if (input.contains("C") || input.contains("Z")) {
            }
        }
        if (!(playerswitch % 2 == 0)) {
            jsAngle = iaP1.getJsAngle();
            jsMagnitude = iaP1.getJsMagnitude();
            if (input.contains("C") || input.contains("Z")) {
            }
            ncGX = iaP1.getNcGX();
            ncGY = iaP1.getNcGY();
            ncGZ = iaP1.getNcGZ();
        } else {
            jsAngle = ia.getJsAngle();
            jsMagnitude = ia.getJsMagnitude();
            if (input.contains("C") || input.contains("Z")) {
            }
            ncGX = ia.getNcGX();
            ncGY = ia.getNcGY();
            ncGZ = ia.getNcGZ();
        }
        if (input.contains("B")) {
            spell(spellnr);
        }
        if (input.contains("A")) {
            if (count > spellchangecnt + 15) {
                spellchangecnt = count;
                if (spellnr <= 7) {
                    spellnr++;
                } else {
                    spellnr = 1;
                }
                switch(spellnr) {
                    case 1:
                        Spellicon = Spellicon1;
                        break;
                    case 2:
                        Spellicon = Spellicon2;
                        break;
                    case 3:
                        Spellicon = Spellicon3;
                        break;
                    case 4:
                        Spellicon = Spellicon4;
                        break;
                    case 5:
                        Spellicon = Spellicon5;
                        break;
                    case 6:
                        Spellicon = Spellicon6;
                        break;
                    case 7:
                        Spellicon = Spellicon7;
                        break;
                    case 8:
                        Spellicon = Spellicon8;
                        break;
                }
            }
        }
        if (yP1 + speedyP1 > 0 && yP1 + speedyP1 < (720 / 6)) {
            yP1 = yP1 + speedyP1;
        } else {
            speedyP1 = 0;
        }
        if (xP1 + speedxP1 > 0 && xP1 + speedxP1 < 1280) {
            xP1 = xP1 + speedxP1;
        } else {
            speedxP1 = 0;
        }
    }

    public void checkDirection(FloatingPlayer fp, String direction) {
        if (fp.status == "LEFT") {
            if (direction == "RIGHT") {
                fp.status = "RIGHT";
                fp.currentImage1 = 3;
                fp.update();
            }
        } else {
            if (direction == "LEFT") {
                fp.status = "LEFT";
                fp.currentImage1 = 0;
                fp.update();
            }
        }
    }

    public void roundupP1() {
        int r = 150;
        firelinexP1 = (int) (jsMagnitudeP1 * (cxP1 + r * -Math.cos((Math.PI / 180) * (jsAngleP1 + 90))));
        firelineyP1 = (int) (jsMagnitudeP1 * (cyP1 + r * -Math.sin((Math.PI / 180) * (jsAngleP1 + 90))));
    }

    public void roundupP2() {
        int r = 150;
        firelinex = (int) (jsMagnitude * (cx + r * -Math.cos((Math.PI / 180) * (jsAngle + 90))));
        fireliney = (int) (jsMagnitude * (cy + r * -Math.sin((Math.PI / 180) * (jsAngle + 90))));
    }

    public void checkInput() {
        int wiinr;
        if (!(playerswitch % 2 == 0)) {
            input = ia.getInput();
            wiinr = 0;
        } else {
            input = iaP1.getInput();
            wiinr = 1;
        }
        if (input.contains("LEFT")) {
            direction = "LEFT";
            accelurateXcheck(-acceleration);
            addP2prtcls();
            bunny.changeImage(direction);
            frog.changeImage(direction);
        } else if (input.contains("RIGHT")) {
            direction = "RIGHT";
            accelurateXcheck(+acceleration);
            addP2prtcls();
            bunny.changeImage(direction);
            frog.changeImage(direction);
        }
        slowdown(slowdownNr);
        y = y + speedy;
        if (onground && (x - ahillXmove > 0)) {
            x -= (1.7);
        }
        collisioncheck();
        if (!objectdetect && (x + speedx + PlayerSizeXP2 < 1280) && x + speedx > 0) {
            x = x + speedx;
        } else {
            speedx = 0;
        }
        if (x < 0) {
            soundsquish.playSound(5);
            reset(30, 260, true);
            addP2prtcls();
            if (playerswitch % 2 == 0) {
                suicideP1++;
            } else {
                suicide++;
            }
        }
        if (input.contains("B")) {
            jump();
        }
        if (input.contains("PLUS")) {
            try {
                timer.wait();
            } catch (InterruptedException e) {
            }
        }
        if (input.contains("MINUS")) {
            timer.notify();
        }
        if (rumblestart + rumbletime > countlimit) {
            rumbletime = (rumblestart + rumbletime) - countlimit + (countlimit - rumblestart);
            rumblestart = 0;
        }
        if (rumblestart + rumbletime < count) {
            deactivateRumble(wiinr);
        }
    }

    public void collisioncheck() {
        objectdetect = false;
        for (Iterator<Shape> itr = solidobjects.iterator(); itr.hasNext(); ) {
            Shape s = itr.next();
            if (s.contains((int) x + PlayerSizeXP2 - 1, (int) (y + 1)) || s.contains((int) x + PlayerSizeXP2 - 1, (int) (y + PlayerSizeYP2 - 1))) {
                x -= ahillXmove;
            }
            if (s.contains((int) x + speedx - 1, (int) (y - 1)) || s.contains((int) x + speedx + PlayerSizeXP2 + 1, (int) (y - 1)) || s.contains((int) x + speedx - 1, (int) (y + PlayerSizeYP2 + 1)) || s.contains((int) x + speedx + PlayerSizeXP2 + 1, (int) (y + PlayerSizeYP2 + 1))) {
                objectdetect = true;
            }
            Rectangle2D frogR2 = new Rectangle2D.Double(x, y, frog.w, frog.h);
            Rectangle2D pR2 = new Rectangle2D.Double(power.x, power.y, power.w, power.h);
            if (frogR2.intersects(pR2)) {
                resetPowerUP(true);
                ManaPwr = -50;
            }
        }
    }

    private void addP2prtcls() {
        if ((playerswitch % 2 == 0)) {
            addprtcls(x + (PlayerSizeXP2 / 2), y + (PlayerSizeYP2 / 2), 1, true, 31, 191, 0, -1);
        } else {
            addprtcls(x + (PlayerSizeXP2 / 2), y + (PlayerSizeYP2 / 2), 1, true, 156, 74, 24, -1);
        }
    }

    private void addP1prtcls() {
        if (!(playerswitch % 2 == 0)) {
            addprtcls(xP1, yP1 + (PlayerSizeYP1 / 4), 1, true, 31, 191, 0, -1);
        } else {
            addprtcls(xP1, yP1 + (PlayerSizeYP1 / 4), 1, true, 156, 74, 24, -1);
        }
    }

    private void fullreset() {
        spellnr = 1;
        playerswitch = 0;
        spell.clear();
        spellchangecnt = 0;
        deactivateRumble(0);
        deactivateRumble(1);
        MorfPwr = MorfLimit;
        ManaPwr = ManaMax;
        reset(PlayerSizeXP2 + 10, PlayerSizeYP2 + 50, false);
        resetP1(1280 / 2, 90);
        slowdownNr = 1.0;
        BGAnimcnt = 1400;
        count = 0;
        spellcount = -20;
        gravityG = 1.04;
        frictionx = 0.5;
        frictiony = 0.5;
        speedlimit = 4;
        acceleration = 1.0;
        ahillXmove = 5;
        hillXmove = 0;
        hill.clear();
    }

    public void hill() {
        xcorrectionHill = 1;
        hillXmove -= ahillXmove;
        while (hill.size() < hillamount) {
            hill.add(new Hill());
        }
        if (hillXmove % xcorrectionHill == 0) {
            hill.add(new Hill());
        }
        solidobjects.clear();
        for (Iterator<Hill> itr = hill.iterator(); itr.hasNext(); ) {
            Hill k = itr.next();
            if (solidobjects.size() < hillamount) {
                if (xcorrectionHill + hillXmove > -300 && xcorrectionHill + hillXmove < 1280) {
                    solidobjects.add(new Rectangle2D.Double(xcorrectionHill + hillXmove, k.GetrctgY(), k.GetrctgW(), k.GetrctgH()));
                }
            }
            xcorrectionHill += hillspread;
        }
    }

    public void reset(float newX, float newY, boolean P2died) {
        deactivateRumble(1);
        x = newX;
        y = newY;
        speedx = 0;
        speedy = 0;
        gravity = 1;
        if (P2died) {
            MorfPwrAdd(100);
        }
    }

    public void resetP1(float newX, float newY) {
        deactivateRumble(0);
        xP1 = newX;
        yP1 = newY;
        speedxP1 = 0;
        speedyP1 = 0;
    }

    public void jump() {
        if (onground) {
            soundjump.playSound(0);
            speedy = -10;
            jumpcount = count;
            bunny.status = "UP";
            bunny.changeImage(direction);
            frog.status = "UP";
            frog.changeImage(direction);
        } else if (!onground && dubblejumppossible && count > jumpcount + 20) {
            soundjump.playSound(0);
            gravity = 1.04;
            speedy = -14;
            dubblejumppossible = false;
        }
    }

    public void gravity() {
        checkground();
        if (!onground) {
            if (gravity < 1) {
                gravity = 1.04;
            }
            gravity = gravity * gravityG;
            y = y + gravity;
        } else if (onground) {
            gravity = 0;
            if (speedy > 0) {
                speedy = 0;
            }
        }
    }

    public void checkFallP2() {
        if (!onground && gravity > 2) {
            bunny.status = "DOWN";
            bunny.changeImage(direction);
            frog.status = "DOWN";
            frog.changeImage(direction);
        } else if (bunny.status == "DOWN") {
            bunny.status = "";
            bunny.changeImage(direction);
            frog.status = "";
            frog.changeImage(direction);
        }
    }

    public void checkground() {
        if (y + PlayerSizeYP2 > 720) {
            onground = false;
            reset(30, 60, true);
        } else {
            onground = false;
        }
        for (Iterator<Shape> itr = solidobjects.iterator(); itr.hasNext(); ) {
            Shape s = itr.next();
            if (s.contains((int) x, (int) (y + speedy + gravity + PlayerSizeYP2 + 2)) || s.contains((int) x + (PlayerSizeXP2 / 2), (int) (y + speedy + gravity + PlayerSizeYP2 + 2)) || s.contains((int) x + PlayerSizeXP2, (int) (y + speedy + gravity + PlayerSizeYP2 + 2))) {
                onground = true;
                dubblejumppossible = true;
            }
        }
    }

    public void accelurateXcheckP1(double X) {
        if (speedxP1 + X < speedlimitP1X && speedxP1 + X > -speedlimitP1X) {
            speedxP1 = speedxP1 + X;
        }
    }

    public void accelurateYcheckP1(double Y) {
        if (speedyP1 + Y < speedlimitP1Y && speedyP1 + Y > -speedlimitP1Y) {
            speedyP1 = speedyP1 + Y;
        }
    }

    public void accelurateXcheck(double X) {
        if (speedx + X < speedlimit && speedx + X > -speedlimit) {
            speedx = speedx + X;
        }
    }

    public void accelurateYcheck(double Y) {
        if (speedy + Y < speedlimit && speedy + Y > -speedlimit) {
            speedy = speedy + Y;
        }
    }

    public void slowdown(double frctionlvl) {
        if (speedy > 0.1) {
            speedy = speedy - (frictiony * frctionlvl);
        } else if (speedy < -0.1) {
            speedy = speedy + (frictiony * frctionlvl);
        } else {
            speedy = 0;
        }
        if (speedx - (frictionx * frctionlvl) > 0.0) {
            speedx = speedx - (frictionx * frctionlvl);
        } else if (speedx + (frictionx * frctionlvl) < -0.0) {
            speedx = speedx + (frictionx * frctionlvl);
        } else {
            speedx = 0;
        }
    }

    public void updateParticls() {
        for (Iterator<Kogel> itr = kogels.iterator(); itr.hasNext(); ) {
            Kogel k = itr.next();
            if (k.alpha < 0.01) {
                itr.remove();
            } else {
                k.update();
            }
        }
    }

    public static void addprtcls(double xCor, double yCor, int prtAmount, boolean alldirections, int r, int g, int b, double colorFade) {
        for (int idx = 0; idx < prtAmount; idx++) {
            kogels.add(new Kogel(xCor, yCor, alldirections, r, g, b, colorFade));
        }
    }

    public void rumble(int wiinr) {
        if (wiinr == 1) {
            rumblestart = count;
            rumbleActive = true;
        } else {
            rumblestartP1 = count;
            rumbleActiveP1 = true;
        }
        wiimotes[wiinr].activateRumble();
    }

    public void deactivateRumble(int wiinr) {
        if (wiinr == 1) {
            if (rumbleActive) {
                wiimotes[wiinr].deactivateRumble();
            }
            rumbleActive = false;
        } else {
            if (rumbleActiveP1) {
                wiimotes[wiinr].deactivateRumble();
            }
            rumbleActiveP1 = false;
        }
    }

    public static int getSpellHitP1() {
        return hitSpellP1;
    }

    public static int getSpellHit() {
        return hitSpell;
    }

    public static int getMorphmeterP1() {
        return morphMeterP1;
    }

    public static int getMorphmeter() {
        return morphMeter;
    }

    public static int getSuicide() {
        return suicide;
    }

    public static int getSuicideP1() {
        return suicideP1;
    }

    public void morfSecCounter() {
        if (playerswitch % 2 == 0) {
            morphMeterP1++;
        } else {
            morphMeter++;
        }
    }
}
