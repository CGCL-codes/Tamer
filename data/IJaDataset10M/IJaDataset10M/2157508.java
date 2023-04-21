package jepe.part;

import java.awt.Graphics;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.util.*;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import jepe.Jepe;
import jepe.image.*;
import jepe.map.Map;
import jepe.util.*;

public class IntroTwo extends jepe.GamePart {

    private Element xml;

    private Config config;

    private boolean requpdate;

    private int option, prevOption;

    private int updates = 0;

    private int framerate;

    private int frameno = 0;

    private int lastframeno;

    private boolean stopped = false;

    private int[] stopframes;

    private int keycycles = 0;

    private boolean keyPressed = false;

    private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

    private int[] imagesx;

    private int[] imagesy;

    private int[] imagestart;

    private int[] imageend;

    private boolean[] display;

    private int imageslength;

    private BufferedImage[][] animations;

    private int[] anisx;

    private int[] anisy;

    private int[] anisplitx;

    private int[] anisplity;

    private int[] anistart;

    private int[] aniend;

    private boolean[] anidisplay;

    private boolean[][] framedisplay;

    private int anislength;

    private int[] frameslength;

    private int[] aniloop;

    private int[] choiceframes;

    private int[] chstart;

    private int[][] selectionid;

    private int[][] selectionL;

    private int[][] selectionR;

    private int[][] selectionD;

    private int[][] selectionU;

    private int choiceslength;

    private int[] selectionslength;

    private ArrayList<BufferedImage> simages = new ArrayList<BufferedImage>();

    private int[] simagesx;

    private int[] simagesy;

    private boolean[][] sidisplay;

    private int simageslength;

    private static IntroTwo instance = null;

    public static IntroTwo getInstance() {
        if (IntroTwo.instance == null) {
            IntroTwo.instance = new IntroTwo();
        }
        return IntroTwo.instance;
    }

    private IntroTwo() {
        super();
        config = engine.getConfig();
        try {
            SAXBuilder builder = new SAXBuilder();
            String xmlFile = this.config.getDataDir() + "/introtwodata/introtwo.cutscene";
            this.xml = builder.build(xmlFile).getRootElement();
            Debug.info("Parsing:" + xmlFile);
            framerate = Integer.parseInt(xml.getAttributeValue("framerate"));
            lastframeno = Integer.parseInt(xml.getChild("end").getAttributeValue("frame"));
            int stops = 0;
            for (Object elo : xml.getChildren("stop")) {
                stops++;
            }
            stopframes = new int[stops];
            stops = 0;
            for (Object elo : xml.getChildren("stop")) {
                Element st = (Element) elo;
                stopframes[stops] = Integer.parseInt(st.getAttributeValue("frame"));
                stops++;
            }
            this.setupImages(xml);
            this.setupAnimations(xml);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load the Intro, forwarding to game...");
            engine.setGamePart(Jepe.GAME);
        }
    }

    private void setupImages(Element e) {
        int i = 0;
        for (Object elo : e.getChildren("image")) {
            System.out.println("Image setup for loop: " + i);
            i++;
        }
        int length = i;
        imagesx = new int[length];
        imagesy = new int[length];
        imagestart = new int[length];
        imageend = new int[length];
        display = new boolean[length];
        System.out.println("Image setup going! : " + i);
        loadImages(e, length);
    }

    private void loadImages(Element e, int length) {
        imageslength = length;
        int i = 0;
        for (Object elo : e.getChildren("image")) {
            Element el = (Element) elo;
            try {
                System.out.println("Length in loadImages: " + imageslength);
                System.out.println("i in loadImages: " + i);
                BufferedImage src = getImage(config.getDataDir() + "/introtwodata/" + el.getAttributeValue("src"));
                int x = Integer.parseInt(el.getAttributeValue("x")), y = Integer.parseInt(el.getAttributeValue("y"));
                String[] durations = el.getAttributeValue("duration").split(":");
                images.add(src);
                imagesx[i] = x;
                imagesy[i] = y;
                imagestart[i] = Integer.parseInt(durations[0]);
                imageend[i] = Integer.parseInt(durations[1]);
            } catch (Exception x) {
                x.printStackTrace();
                System.err.println("Error loading images in the Intro, forwarding to game...");
                engine.setGamePart(Jepe.GAME);
            }
            i++;
        }
    }

    private void setupAnimations(Element e) {
        int a = 0;
        for (Object elo : e.getChildren("animation")) {
            System.out.println("Animation setup for loop: " + a);
            a++;
        }
        int length = a;
        anisx = new int[length];
        anisy = new int[length];
        anisplitx = new int[length];
        anisplity = new int[length];
        anistart = new int[length];
        aniend = new int[length];
        anidisplay = new boolean[length];
        framedisplay = new boolean[length][];
        frameslength = new int[length];
        animations = new BufferedImage[length][];
        aniloop = new int[length];
        System.out.println("Ani setup going! : " + a);
        loadAnimations(e, length);
    }

    private void loadAnimations(Element e, int length) {
        anislength = length;
        int a = 0;
        for (Object elo : e.getChildren("animation")) {
            Element el = (Element) elo;
            try {
                System.out.println("Length in loadAnimations: " + anislength);
                System.out.println("a in loadAnimations: " + a);
                int x = Integer.parseInt(el.getAttributeValue("x")), y = Integer.parseInt(el.getAttributeValue("y"));
                String[] durations = el.getAttributeValue("duration").split(":");
                String[] imagewithsplit = el.getAttributeValue("image").split(":");
                String[] imagesplit = imagewithsplit[1].split(",");
                anisx[a] = x;
                anisy[a] = y;
                anisplitx[a] = Integer.parseInt(imagesplit[0]);
                anisplity[a] = Integer.parseInt(imagesplit[1]);
                anistart[a] = Integer.parseInt(durations[0]);
                aniend[a] = Integer.parseInt(durations[1]);
                BufferedImage[] frames = new BufferedImage[(anisplitx[a] * anisplity[a])];
                boolean[] noframes = new boolean[frames.length];
                for (int p = 0; p < frames.length; p++) {
                    frames[p] = jepe.Jepe.graphicsManager.getImages(config.getDataDir() + "/introtwodata/" + imagewithsplit[0], anisplitx[a], anisplity[a])[p];
                    System.out.println("Adding frame " + p);
                }
                System.out.println();
                System.out.println("LOAD VALUES");
                System.out.println("##    x   y   s   e   f");
                System.out.println("#" + a + ":  " + anisx[a] + "  " + anisy[a] + "  " + anistart[a] + "  " + aniend[a] + "   " + frames.length);
                System.out.println();
                framedisplay[a] = noframes;
                frameslength[a] = framedisplay.length;
                aniloop[a] = 0;
                System.out.println(noframes.length + "///" + framedisplay.length + "///" + framedisplay[a].length);
                animations[a] = frames;
            } catch (Exception x) {
                x.printStackTrace();
                System.err.println("Error loading animations in the Intro, forwarding to game...");
                engine.setGamePart(Jepe.GAME);
            }
            a++;
        }
    }

    private void setupChoices(Element e) {
        int c = 0;
        for (Object elo : e.getChildren("choice")) {
            System.out.println("Choices setup for loop: " + c);
            c++;
        }
        int length = c;
        choiceframes = new int[length];
        chstart = new int[length];
        int s = 0;
        for (Object elo : e.getChild("choice").getChildren("selection")) {
            System.out.println("Selections setup for loop: " + s);
            s++;
        }
        int slength = s;
        selectionid = new int[length][slength];
        selectionL = new int[length][slength];
        selectionR = new int[length][slength];
        selectionD = new int[length][slength];
        selectionU = new int[length][slength];
        selectionslength = new int[length];
        int i = 0;
        for (Object elo : e.getChildren("image")) {
            System.out.println("Image setup for loop: " + i);
            i++;
        }
        int imlength = i;
        simagesx = new int[imlength];
        simagesy = new int[imlength];
        sidisplay = new boolean[imlength][];
        System.out.println("Choice/Image setup going! : " + i);
        loadImages(e, length);
        System.out.println("Choice setup going! : " + c);
        loadChoices(e, length);
    }

    private void loadChoices(Element e, int length) {
        choiceslength = length;
        int c = 0;
        for (Object elo : e.getChildren("choice")) {
            Element el = (Element) elo;
            try {
                System.out.println("Length in loadChoices: " + choiceslength);
                System.out.println("c in loadChoices: " + c);
                int frame = Integer.parseInt(el.getAttributeValue("frame")), start = Integer.parseInt(el.getAttributeValue("start"));
                choiceframes[c] = frame;
                chstart[c] = start;
                int s = 0;
                for (Object elp : el.getChildren("selection")) {
                    Element em = (Element) elp;
                    System.out.println("s in loadChoices/selections: " + s);
                    int si = 0;
                    for (Object elq : em.getChildren("image")) {
                        Element eln = (Element) elq;
                        try {
                            System.out.println("Length in loadImages: " + simageslength);
                            System.out.println("si in loadImages: " + si);
                            BufferedImage src = getImage(config.getDataDir() + "/introtwodata/" + eln.getAttributeValue("src"));
                            int x = Integer.parseInt(eln.getAttributeValue("x")), y = Integer.parseInt(eln.getAttributeValue("y"));
                            simages.add(src);
                            simagesx[si] = x;
                            simagesy[si] = y;
                            sidisplay[c][si] = false;
                            selectionslength[c] = si;
                        } catch (Exception x) {
                            x.printStackTrace();
                            System.err.println("Error loading choice images in the Intro, forwarding to game...");
                            engine.setGamePart(Jepe.GAME);
                        }
                        si++;
                    }
                    int id, left, right, up, down;
                    id = Integer.parseInt(em.getAttributeValue("id"));
                    if (em.getAttributeValue("left") != null) {
                        left = Integer.parseInt(em.getAttributeValue("left"));
                    } else {
                        left = 0;
                    }
                    if (em.getAttributeValue("right") != null) {
                        right = Integer.parseInt(em.getAttributeValue("right"));
                    } else {
                        right = 0;
                    }
                    if (em.getAttributeValue("up") != null) {
                        up = Integer.parseInt(em.getAttributeValue("up"));
                    } else {
                        up = 0;
                    }
                    if (em.getAttributeValue("down") != null) {
                        down = Integer.parseInt(em.getAttributeValue("down"));
                    } else {
                        down = 0;
                    }
                    selectionid[c][s] = id;
                    selectionL[c][s] = left;
                    selectionR[c][s] = right;
                    selectionU[c][s] = up;
                    selectionD[c][s] = down;
                    s++;
                }
            } catch (Exception x) {
                x.printStackTrace();
                System.err.println("Error loading choices or selections in the Intro, forwarding to game...");
                engine.setGamePart(Jepe.GAME);
            }
            c++;
        }
    }

    public boolean update(long elapsedTime) {
        requpdate = false;
        if (keyPressed == true && keycycles < 5) {
            keycycles++;
            System.out.println("Cycles: " + keycycles);
            return true;
        } else if (keycycles >= 5) {
            keyPressed = false;
            keycycles = 0;
            return true;
        }
        updates++;
        if (updates >= framerate) {
            updates = 0;
            if (!stopped) frameno++;
            System.out.println("Frame: " + frameno);
            requpdate = true;
        }
        for (int st = 0; st < stopframes.length; st++) {
            if (frameno == stopframes[st]) {
                stopped = true;
            }
        }
        for (int i = 0; i < imageslength; i++) {
            if (imagestart[i] == frameno) {
                display[i] = true;
            }
            if (frameno >= imageend[i]) {
                if (display[i]) {
                    display[i] = false;
                }
            }
        }
        for (int a = 0; a < anislength; a++) {
            if (anistart[a] == frameno) {
                anidisplay[a] = true;
                System.out.println("Display animation#" + a);
                System.out.println(aniend[a] + " " + aniend[0]);
            }
            if (anidisplay[a] == true) {
                if (aniloop[a] < (framedisplay[a].length)) {
                    framedisplay[a][aniloop[a]] = true;
                }
                if (aniloop[a] > 0) framedisplay[a][aniloop[a] - 1] = false;
                if (aniloop[a] == 0) framedisplay[a][framedisplay[a].length - 1] = false;
                if (requpdate == true) {
                    aniloop[a]++;
                    if (aniloop[a] == framedisplay[a].length) {
                        aniloop[a] = 0;
                    }
                }
            }
            if (frameno >= aniend[a]) {
                if (anidisplay[a]) {
                    anidisplay[a] = false;
                    System.out.println("Stop displaying animation #" + a + ", frame#" + aniend[a]);
                }
            }
        }
        for (int c = 0; c < choiceslength; c++) {
            if (stopped && frameno == choiceframes[c]) {
                for (int s = 0; s < selectionslength[c]; s++) {
                    for (KeyEvent e : engine.getEventHandler().getPressedKeys()) {
                        if (e.getWhen() >= this.lastUpdate) {
                            switch(e.getKeyCode()) {
                                case KeyEvent.VK_LEFT:
                                    if (selectionL[c][s] != 0) {
                                        sidisplay[c][selectionL[c][s]] = true;
                                    }
                                    break;
                                case KeyEvent.VK_RIGHT:
                                    if (selectionR[c][s] != 0) {
                                        sidisplay[c][selectionR[c][s]] = true;
                                    }
                                    break;
                                case KeyEvent.VK_UP:
                                    if (selectionU[c][s] != 0) {
                                        sidisplay[c][selectionU[c][s]] = true;
                                    }
                                    break;
                                case KeyEvent.VK_DOWN:
                                    if (selectionD[c][s] != 0) {
                                        sidisplay[c][selectionD[c][s]] = true;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }
        if (frameno >= lastframeno) {
            engine.setGamePart(Jepe.GAME);
        }
        if (requpdate == true) {
            return true;
        }
        if (this.firstrun) {
            this.lastUpdate = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    public void render(Graphics g) {
        if (this.firstrun) {
            this.firstrun = false;
        } else {
            for (int i = 0; i < imageslength; i++) {
                if (display[i] == true) {
                    g.drawImage(images.get(i), imagesx[i], imagesy[i], null);
                }
            }
            for (int a = 0; a < anislength; a++) {
                if (anidisplay[a] == true) {
                    for (int f = 0; f < framedisplay[a].length; f++) {
                        if (framedisplay[a][f] == true) {
                            g.drawImage(animations[a][f], anisx[a], anisy[a], null);
                        }
                    }
                }
            }
            for (int c = 0; c < choiceslength; c++) {
                for (int si = 0; si < simageslength; si++) {
                    if (sidisplay[c][si] == true) {
                        g.drawImage(simages.get(si), simagesx[si], simagesy[si], null);
                    }
                }
            }
        }
    }
}
