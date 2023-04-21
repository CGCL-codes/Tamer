package mrIndustry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import mrIndustry.SpielObjekte.Umgebung;
import mrIndustry.SpielObjekte.ZentralLager;
import mrIndustry.SpielObjekte.LeistungsSteigerung.Autofabrik;
import mrIndustry.SpielObjekte.LeistungsSteigerung.Werkzeugfabrik;
import mrIndustry.SpielObjekte.PrivateObjekte.WohnhausMittelKlasse;
import mrIndustry.SpielObjekte.PrivateObjekte.WohnhausOberKlasse;
import mrIndustry.SpielObjekte.PrivateObjekte.WohnhausUnterKlasse;
import mrIndustry.SpielObjekte.Rohstoffgebaeude.Bergwerk;
import mrIndustry.SpielObjekte.Rohstoffgebaeude.Eisenmine;
import mrIndustry.SpielObjekte.Rohstoffgebaeude.Farm;
import mrIndustry.SpielObjekte.Rohstoffgebaeude.Foersterei;
import mrIndustry.SpielObjekte.Verarbeitungsgebaude.Baeckerei;

public class Spielkarte extends JPanel {

    public SpielObjekt newObject;

    public double resourceFactor = 1;

    public SpielObjekt getNewObject() {
        return newObject;
    }

    public void setNewObject(SpielObjekt newObject) {
        this.newObject = newObject;
    }

    public SpielObjekt spielObjekte[][];

    public SpielObjekt[][] getSpielObjekte() {
        return spielObjekte;
    }

    public ZentralLager getZentralLager() {
        return ((ZentralLager) this.spielObjekte[this.zentralLagerX][this.zentralLagerY]);
    }

    int mapsizeX;

    int mapsizeY;

    int horizontal;

    int map_array[][];

    public int getMapsizeX() {
        return mapsizeX;
    }

    public void setMapsizeX(int mapsizeX) {
        this.mapsizeX = mapsizeX;
    }

    public int getMapsizeY() {
        return mapsizeY;
    }

    public void setMapsizeY(int mapsizeY) {
        this.mapsizeY = mapsizeY;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    int vertical;

    public boolean zentralLager;

    public int zentralLagerX;

    public int zentralLagerY;

    MrIndustry wnd;

    public int getZentralLagerX() {
        return zentralLagerX;
    }

    public void setZentralLagerX(int zentralLagerX) {
        this.zentralLagerX = zentralLagerX;
    }

    public int getZentralLagerY() {
        return zentralLagerY;
    }

    public void setZentralLagerY(int zentralLagerY) {
        this.zentralLagerY = zentralLagerY;
    }

    public Spielkarte(int sizeX, int sizeY, final MrIndustry wnd) {
        this.wnd = wnd;
        this.mapsizeX = sizeX;
        this.mapsizeY = sizeY;
        this.horizontal = 0;
        this.vertical = 0;
        setLayout(new GridLayout(sizeX, sizeY));
        this.spielObjekte = new SpielObjekt[this.mapsizeX][this.mapsizeX];
        this.map_array = generateLand(45);
        this.map_array = generateForrest(50);
        this.map_array = generateRessources(5);
        zentralLager = false;
    }

    public int[][] getMapArray() {
        return map_array;
    }

    public void setMapArray(int map_array[][]) {
        this.map_array = map_array;
    }

    public int[][] pathFind(int currX, int tarX, int currY, int tarY) {
        double[] path_arr = new double[4];
        int[][] weg_array = new int[getMapsizeX()][getMapsizeY()];
        boolean zielerreicht = false;
        while (zielerreicht == false) {
            path_arr[0] = Math.abs(currX - tarX) + Math.abs((currY - 1) - tarY);
            path_arr[1] = Math.abs((currX + 1) - tarX) + Math.abs(currY - tarY);
            path_arr[2] = Math.abs(currX - tarX) + Math.abs((currY + 1) - tarY);
            path_arr[3] = Math.abs((currX - 1) - tarX) + Math.abs(currY - tarY);
            double erg = path_arr[0];
            int a = 0;
            for (int i = 0; i < path_arr.length; i++) {
                if (path_arr[i] < erg) {
                    erg = path_arr[i];
                    a = i;
                }
            }
            switch(a) {
                case 0:
                    currY -= 1;
                    break;
                case 1:
                    currX += 1;
                    break;
                case 2:
                    currY += 1;
                    break;
                case 3:
                    currX -= 1;
                    break;
            }
            weg_array[currX][currY] = 1;
            if (currX == tarX && currY == tarY) {
                zielerreicht = true;
            }
        }
        for (int z = 0; z < getMapsizeX(); z++) {
            for (int g = 0; g < getMapsizeY(); g++) {
                if (weg_array[z][g] == 1) {
                    map_array[z][g] = 2;
                }
            }
        }
        setMapArray(map_array);
        return map_array;
    }

    public int[][] generateLand(int freq) {
        int w = getMapsizeX();
        int h = getMapsizeY();
        int map_array[][] = new int[w][h];
        for (int x = 1; x < w - 3; x++) {
            for (int y = 2; y < h - 3; y++) {
                int rndMr = MrIndustry.rndZahl(100);
                if (rndMr <= freq && x != 1 && y != 1 && x % 4 == 0 && y % 4 == 0) {
                    map_array[x][y] = 1;
                    map_array[x + 1][y] = 1;
                    map_array[x][y + 1] = 1;
                    map_array[x + 1][y + 1] = 1;
                }
            }
        }
        for (int x = 4; x < w - 4; x++) {
            for (int y = 4; y < h - 4; y++) {
                if (x > 1 && y > 1 && x < w - 1 && y < h - 1) {
                    if (map_array[x][y] == 1) {
                        if (MrIndustry.rndZahl(100) <= freq) {
                            map_array[x][y] = 1;
                            map_array[x + 1][y] = 1;
                            map_array[x][y + 1] = 1;
                            map_array[x + 1][y + 1] = 1;
                        }
                    }
                }
            }
        }
        for (int x = 1; x < w; x++) {
            for (int y = 2; y < h; y++) {
                if (map_array[x][y] >= 1 && map_array[x + 1][y] < 1 && map_array[x + 2][y] >= 1) {
                    map_array[x + 1][y] = 1;
                }
                if (map_array[x][y] >= 1 && map_array[x + 1][y] < 1 && map_array[x + 2][y + 2] >= 1) {
                    map_array[x][y + 1] = 1;
                }
                if (map_array[x][y] >= 1 && map_array[x][y + 1] < 1 && map_array[x][y + 2] >= 1) {
                    map_array[x][y + 1] = 1;
                }
                if (x > 3 && y > 3 && x < w - 3 && y < h - 3) {
                    if (map_array[x][y] >= 1 && map_array[x + 1][y] < 1 && map_array[x + 2][y] < 1 && map_array[x + 3][y] >= 1) {
                        map_array[x + 1][y] = 1;
                        map_array[x + 2][y] = 1;
                    }
                    if (map_array[x][y] >= 1 && map_array[x][y + 1] < 1 && map_array[x][y + 2] < 1 && map_array[x][y + 3] >= 1) {
                        map_array[x][y + 1] = 1;
                        map_array[x][y + 2] = 1;
                    }
                }
            }
        }
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (x > 0 && y > 0 && x < w - 1 && y < h - 1) {
                    if (map_array[x][y] == 0) {
                        if (map_array[x + 1][y] == 1) {
                            map_array[x][y] = 11;
                        } else if (map_array[x][y + 1] == 1) {
                            map_array[x][y] = 12;
                        } else if (map_array[x - 1][y] == 1) {
                            map_array[x][y] = 13;
                        } else if (map_array[x][y - 1] == 1) {
                            map_array[x][y] = 14;
                        } else if (map_array[x + 1][y] == 0 && map_array[x + 1][y + 1] == 1 && map_array[x][y + 1] == 0) {
                            map_array[x][y] = 15;
                        } else if (map_array[x - 1][y] != 1 && map_array[x][y + 1] != 1 && map_array[x - 1][y + 1] == 1) {
                            map_array[x][y] = 16;
                        } else if (map_array[x - 1][y] != 1 && map_array[x][y + 1] != 1 && map_array[x - 1][y - 1] == 1) {
                            map_array[x][y] = 17;
                        } else if (map_array[x + 1][y] != 1 && map_array[x + 1][y - 1] >= 1 && map_array[x][y - 1] != 1) {
                            map_array[x][y] = 18;
                        }
                        if (map_array[x - 1][y] == 1 && map_array[x][y - 1] == 1) {
                            map_array[x][y] = 19;
                        }
                        if (map_array[x + 1][y] == 1 && map_array[x][y - 1] == 1 && map_array[x + 1][y - 1] == 1) {
                            map_array[x][y] = 20;
                        }
                        if (map_array[x - 1][y] == 1 && map_array[x - 1][y + 1] == 1 && map_array[x][y + 1] == 1) {
                            map_array[x][y] = 21;
                        }
                        if (map_array[x + 1][y] == 1 && map_array[x][y + 1] == 1) {
                            map_array[x][y] = 22;
                        }
                    }
                }
            }
        }
        return map_array;
    }

    public boolean checkFor(int x, int y) {
        boolean verification = false;
        int map_array[][] = getMapArray();
        if (map_array[x][y] >= 1 && map_array[x - 1][y] >= 1 && map_array[x - 2][y] >= 1 && map_array[x + 1][y] >= 1 && map_array[x + 2][y] >= 1 && map_array[x][y - 1] >= 1 && map_array[x][y - 2] >= 1 && map_array[x][y + 1] >= 1 && map_array[x + 2][y - 1] >= 1 && map_array[x + 2][y + 1] >= 1 && map_array[x - 2][y - 1] >= 1 && map_array[x - 2][y + 1] >= 1 && map_array[x + 1][y - 2] >= 1 && map_array[x + 1][y + 2] >= 1 && map_array[x - 1][y - 1] >= 1 && map_array[x - 1][y + 2] >= 1 && map_array[x][y + 2] >= 1 && map_array[x + 1][y + 1] >= 1 && map_array[x + 2][y + 2] >= 1 && map_array[x - 1][y - 1] >= 1 && map_array[x - 2][y - 2] >= 1 && map_array[x + 1][y - 1] >= 1 && map_array[x + 2][y - 2] >= 1 && map_array[x - 1][y + 1] >= 1 && map_array[x - 2][y + 2] >= 1 && map_array[x][y] <= 2 && map_array[x - 1][y] <= 2 && map_array[x - 2][y] <= 2 && map_array[x + 1][y] <= 2 && map_array[x + 2][y] <= 2 && map_array[x][y - 1] <= 2 && map_array[x][y - 2] <= 2 && map_array[x][y + 1] <= 2 && map_array[x + 2][y - 1] <= 2 && map_array[x + 2][y + 1] <= 2 && map_array[x - 2][y - 1] <= 2 && map_array[x - 2][y + 1] <= 2 && map_array[x + 1][y - 2] <= 2 && map_array[x + 1][y + 2] <= 2 && map_array[x - 1][y - 1] <= 2 && map_array[x - 1][y + 2] <= 2 && map_array[x][y + 2] <= 2 && map_array[x + 1][y + 1] <= 2 && map_array[x + 2][y + 2] <= 2 && map_array[x - 1][y - 1] <= 2 && map_array[x - 2][y - 2] <= 2 && map_array[x + 1][y - 1] <= 2 && map_array[x + 2][y - 2] <= 2 && map_array[x - 1][y + 1] <= 2 && map_array[x - 2][y + 2] <= 2) {
            verification = true;
        } else {
            verification = false;
        }
        return verification;
    }

    public int[][] generateForrest(int freq) {
        int w = getMapsizeX();
        int h = getMapsizeY();
        int map_array[][] = getMapArray();
        for (int x = 1; x < w; x++) {
            for (int y = 2; y < h; y++) {
                int rndMr = MrIndustry.rndZahl(100);
                if (rndMr <= freq) {
                    if (checkFor(x, y) == true) {
                        map_array[x][y] = 2;
                    }
                }
            }
        }
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (x > 1 && y > 1 && x < w - 1 && y < h - 1) {
                    if (map_array[x][y] == 2) {
                        if (MrIndustry.rndZahl(100) <= freq) {
                            if (checkFor(x, y) == true) {
                                map_array[x][y] = 2;
                            }
                            if (checkFor(x + 1, y) == true) {
                                map_array[x + 1][y] = 2;
                            }
                            if (checkFor(x + 1, y + 1) == true) {
                                map_array[x + 1][y + 1] = 2;
                            }
                            if (checkFor(x, y + 1) == true) {
                                map_array[x][y + 1] = 2;
                            }
                        }
                    }
                }
            }
        }
        for (int l = 1; l < 2; l++) {
            for (int x = 1; x < w; x++) {
                for (int y = 2; y < h; y++) {
                    if (map_array[x][y] == 2 && map_array[x + 1][y] == 1 && map_array[x + 2][y] == 2) {
                        map_array[x + 1][y] = 2;
                    }
                    if (map_array[x][y] == 2 && map_array[x + 1][y] == 1 && map_array[x + 2][y + 2] == 2) {
                        map_array[x][y + 1] = 2;
                    }
                    if (map_array[x][y] == 2 && map_array[x][y + 1] == 1 && map_array[x][y + 2] == 2) {
                        map_array[x][y + 1] = 2;
                    }
                    if (map_array[x][y] == 2 && map_array[x + 1][y] == 1 && map_array[x + 2][y] == 1 && map_array[x + 3][y] == 2) {
                        map_array[x + 1][y] = 1;
                        map_array[x + 2][y] = 1;
                    }
                    if (map_array[x][y] == 2 && map_array[x][y + 1] == 1 && map_array[x][y + 2] == 1 && map_array[x][y + 3] == 2) {
                        map_array[x][y + 1] = 1;
                        map_array[x][y + 2] = 1;
                    }
                }
            }
        }
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (x > 0 && y > 0 && x < w - 1 && y < h - 1) {
                    if (map_array[x][y] == 1) {
                        if (map_array[x + 1][y] == 2) {
                            map_array[x][y] = 3;
                        } else if (map_array[x][y + 1] == 2) {
                            map_array[x][y] = 4;
                        } else if (map_array[x - 1][y] == 2) {
                            map_array[x][y] = 5;
                        } else if (map_array[x][y - 1] == 2) {
                            map_array[x][y] = 6;
                        } else if (map_array[x + 1][y] == 1 && map_array[x + 1][y + 1] == 2 && map_array[x][y + 1] == 1) {
                            map_array[x][y] = 9;
                        } else if (map_array[x - 1][y - 1] == 2) {
                            map_array[x][y] = 7;
                        } else if (map_array[x - 1][y + 1] == 2) {
                            map_array[x][y] = 10;
                        } else if (map_array[x + 1][y - 1] == 2) {
                            map_array[x][y] = 8;
                        }
                        if (map_array[x - 1][y] == 2 && map_array[x][y - 1] == 2) {
                            map_array[x][y] = 23;
                        }
                        if (map_array[x + 1][y] == 2 && map_array[x][y - 1] == 2 && map_array[x + 1][y - 1] == 2) {
                            map_array[x][y] = 24;
                        }
                        if (map_array[x - 1][y] == 2 && map_array[x][y + 1] == 2) {
                            map_array[x][y] = 25;
                        }
                        if (map_array[x + 1][y] == 2 && map_array[x][y + 1] == 2) {
                            map_array[x][y] = 26;
                        }
                    }
                }
            }
        }
        return map_array;
    }

    public int[][] wegfindung(int startX, int startY, int destinationX, int destinationY) {
        int[][] map_array = getMapArray();
        int[][] path_array = map_array;
        double[] pos = new double[4];
        while (startX != destinationX && startY != destinationY) {
            pos[0] = Math.abs((startX) - destinationX) + Math.abs((startY - 1) - destinationY);
            pos[1] = Math.abs((startX + 1) - destinationX) + Math.abs(startY - destinationY);
            pos[2] = Math.abs(startX - destinationX) + Math.abs((startY + 1) - destinationY);
            pos[3] = Math.abs((startX - 1) - destinationX) + Math.abs(startY - destinationY);
            java.util.Arrays.sort(pos);
            startX = destinationX;
            startY = destinationY;
        }
        return path_array;
    }

    public int[][] generateRessources(int freq) {
        int[][] map_array = getMapArray();
        int w = getMapsizeX();
        int h = getMapsizeY();
        for (int x = 4; x < w; x++) {
            for (int y = 4; y < h; y++) {
                if (x > 1 && y > 1 && x < w - 1 && y < h - 1) {
                    if (map_array[x][y] == 1) {
                        if (MrIndustry.rndZahl(100) <= freq) {
                            switch(MrIndustry.rndZahl(2)) {
                                case 1:
                                    map_array[x][y] = 28;
                                    break;
                                case 2:
                                    map_array[x][y] = 27;
                                    break;
                                default:
                                    map_array[x][y] = 27;
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return map_array;
    }

    public void add(SpielObjekt Obj) {
        this.spielObjekte[Obj.locX][Obj.locY] = Obj;
    }

    public void newGame() {
        int[][] map_array = getMapArray();
        for (int x = 0; x < this.spielObjekte.length; x++) {
            for (int y = 0; y < this.spielObjekte[x].length; y++) {
                this.spielObjekte[x][y] = new Umgebung(map_array[x][y], x, y, this);
            }
        }
        this.newObject = new ZentralLager(0, 0, true, this);
    }

    public void zeichneAlle(Graphics g) {
        for (int x = 0; x < this.spielObjekte.length; x++) {
            for (int y = 0; y < this.spielObjekte[x].length; y++) {
                if (this.spielObjekte[x][y].getDragDrop() == false) this.spielObjekte[x][y].zeichne(g, this.horizontal, this.vertical);
            }
        }
        for (int x = 0; x < this.spielObjekte.length; x++) {
            for (int y = 0; y < this.spielObjekte[x].length; y++) {
                if (this.spielObjekte[x][y].getHighlight() == true) {
                    if (this.spielObjekte[x][y].getTileColor() == true) {
                        g.setColor(Color.YELLOW);
                    } else {
                        g.setColor(Color.RED);
                    }
                    g.drawRect(this.spielObjekte[x][y].locX * wnd.textureSize - this.getHorizontal() - 1, this.spielObjekte[x][y].locY * wnd.textureSize - this.getVertical() - 1, (wnd.textureSize + 1), (wnd.textureSize + 1));
                    g.setColor(Color.BLACK);
                }
                if (this.spielObjekte[x][y].getDragDrop() == true) this.spielObjekte[x][y].zeichne(g, this.horizontal, this.vertical);
            }
        }
        if (this.newObject != null) {
            this.newObject.zeichne(g, this.horizontal, this.vertical);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.zeichneAlle(g);
    }

    public void shutDownOne() {
        int x;
        int y;
        boolean succsessful = false;
        x = 0;
        y = 0;
        while (!succsessful) {
            if (this.spielObjekte[x][y].bevoelkerung > 0 && this.spielObjekte[x][y].isActive()) {
                succsessful = true;
                this.spielObjekte[x][y].activate(false);
            } else {
                x++;
                if (x > this.spielObjekte.length - 1) {
                    x = 0;
                    y++;
                }
                if (y > this.spielObjekte[x].length - 1) {
                    y = 0;
                }
            }
        }
    }
}
