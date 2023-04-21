package core;

public class HP12CConfiguration {

    private double size;

    private int xpos, ypos;

    private String skin;

    private String lang;

    private int stksize;

    private int memsize;

    private int prgsize;

    private int c;

    private int dmy;

    private int com;

    private int alg;

    private int beg;

    private int fix;

    private int keymap[][];

    protected static final int[][] KEYMAP_DEF = { { 0, 40 }, { 1, 49 }, { 2, 50 }, { 3, 51 }, { 4, 52 }, { 5, 53 }, { 6, 54 }, { 7, 55 }, { 8, 56 }, { 9, 57 }, { 10, 47 }, { 11, 110 }, { 12, 105 }, { 13, 112 }, { 14, 109 }, { 15, 118 }, { 16, 99 }, { 20, 42 }, { 21, 121 }, { 22, 120 }, { 23, 116 }, { 24, 100 }, { 25, 37 }, { 26, 101 }, { 30, 45 }, { 31, 82 }, { 32, 83 }, { 33, 68 }, { 34, 122 }, { 35, 8 }, { 36, 10 }, { 40, 43 }, { 41, 113 }, { 42, 102 }, { 43, 103 }, { 44, 115 }, { 45, 114 }, { 48, 46 }, { 49, 119 } };

    public HP12CConfiguration() {
        this.setDefaults();
    }

    public void setDefaults() {
        this.setSize(1);
        this.setFix(9);
        this.setSkin("default");
        this.setLanguage("en");
        this.setStackSize(4);
        this.setMemorySize(20);
        this.setProgramSize(100);
        this.keymap = KEYMAP_DEF;
    }

    public int getKeyCode(int ch) {
        for (int i = 0; i < keymap.length; i++) {
            if (keymap[i][1] == ch) {
                return keymap[i][0];
            }
        }
        return -1;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setXPos(int xpos) {
        this.xpos = xpos;
    }

    public void setYPos(int ypos) {
        this.ypos = ypos;
    }

    public void setSkin(String skinName) {
        this.skin = skinName;
    }

    public void setLanguage(String langCode) {
        this.lang = langCode;
    }

    public void setStackSize(int size) {
        this.stksize = size;
    }

    public void setMemorySize(int size) {
        this.memsize = size;
    }

    public void setProgramSize(int size) {
        this.prgsize = size;
    }

    public void setC(int bool) {
        this.c = bool;
    }

    public void setDmy(int bool) {
        this.dmy = bool;
    }

    public void setCom(int bool) {
        this.com = bool;
    }

    public void setAlg(int bool) {
        this.alg = bool;
    }

    public void setBeg(int bool) {
        this.beg = bool;
    }

    public void setFix(int fix) {
        this.fix = fix;
    }

    public void setKeyMap(int[][] keyMap) {
        this.keymap = keyMap;
    }

    public double getSize() {
        return this.size;
    }

    public int getXPos() {
        return this.xpos;
    }

    public int getYPos() {
        return this.ypos;
    }

    public String getSkin() {
        return this.skin;
    }

    public String getLanguage() {
        return this.lang;
    }

    public int getStackSize() {
        return this.stksize;
    }

    public int getMemorySize() {
        return this.memsize;
    }

    public int getProgramSize() {
        return this.prgsize;
    }

    public int getC() {
        return this.c;
    }

    public int getDmy() {
        return this.dmy;
    }

    public int getCom() {
        return this.com;
    }

    public int getAlg() {
        return this.alg;
    }

    public int getBeg() {
        return this.beg;
    }

    public int getFix() {
        return this.fix;
    }

    public int[][] getKeyMap() {
        return this.keymap;
    }
}
