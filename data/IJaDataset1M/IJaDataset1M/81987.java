package mrIndustry.SpielObjekte;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import mrIndustry.MrIndustry;
import mrIndustry.SpielObjekt;
import mrIndustry.Spielkarte;

public class Umgebung extends SpielObjekt {

    int aniLength;

    public int getAniLength() {
        return aniLength;
    }

    public void setAniLength(int aniLength) {
        this.aniLength = aniLength;
    }

    public Umgebung(int gelaende, int locX, int locY, final Spielkarte map) {
        super();
        this.map = map;
        this.setAniLength(33);
        this.setGelaende(gelaende);
        this.loadTextures();
        this.setActive(true);
        this.setLocX(locX);
        this.setLocY(locY);
        this.setHighlight(false);
        this.setName("Umgebung");
    }

    public void tick() {
        super.tick();
    }

    private void addToWater(String filename) {
        for (int i = 0; i <= this.getAniLength(); i++) {
            this.loadTexture(filename, i);
        }
    }

    private void addWater() {
        double rnd = Math.random();
        double rnd1 = Math.random();
        int waterRnd = (int) (Math.random() * 6);
        int sharkRnd = 0;
        int incrementWater = 1;
        for (int i = 0; i <= this.getAniLength(); i++) {
            this.loadTexture("waterGroundColor.png", i);
            if (rnd1 > 0.8 && MrIndustry.textureScale > 30) {
                this.loadTexture("fish/fish" + (i + 1) + ".png", i);
            }
            if (rnd > 0.98 && MrIndustry.textureScale > 30) {
                this.loadTexture("fish/hai" + sharkRnd + ".png", i);
                if (Math.random() > 0.5) {
                    sharkRnd += 1;
                } else {
                    sharkRnd -= 1;
                }
                if (sharkRnd > 9) {
                    sharkRnd -= 2;
                }
                if (sharkRnd < 0) {
                    sharkRnd += 2;
                }
            }
            if (rnd > 0.99 && MrIndustry.textureScale > 30) {
                this.loadTexture("boatwreck.png", i);
            }
            this.loadTexture("water" + waterRnd + ".png", i);
            waterRnd = waterRnd + incrementWater;
            if (Math.random() > 0.7) {
                incrementWater = incrementWater / (-1);
            }
            if (waterRnd == 7) {
                waterRnd = 5;
                incrementWater = -1;
            }
            if (waterRnd == -1) {
                waterRnd = 1;
                incrementWater = 1;
            }
        }
    }

    public void loadTextures() {
        super.loadTextures();
        if (MrIndustry.textureScale < 50) {
            this.setAniLength(10);
        }
        if (MrIndustry.textureScale < 5) {
            this.setAniLength(2);
        }
        switch(this.gelaende) {
            case 0:
                this.addWater();
                break;
            case 1:
                double rnd = Math.random();
                double rnd1 = Math.random();
                double rnd2 = Math.random();
                double rnd3 = Math.random();
                double rnd4 = Math.random();
                double rnd5 = Math.random();
                double rnd6 = Math.random();
                int count = (int) (Math.random() * 100);
                for (int i = 0; i <= 1; i++) {
                    this.loadTexture("grass.png", i);
                    if (rnd1 > 0.9) {
                        this.loadTexture("bush0.png", i);
                    }
                    if (rnd2 > 0.95) {
                        this.loadTexture("flowerYellow0.png", i);
                    }
                    if (rnd3 > 0.95) {
                        this.loadTexture("flowerYellow1.png", i);
                    }
                    if (rnd4 > 0.95) {
                        this.loadTexture("flowerYellow2.png", i);
                    }
                    if (rnd5 > 0.95) {
                        this.loadTexture("stone0.png", i);
                    }
                    if (rnd6 > 0.95) {
                        this.loadTexture("stone1.png", i);
                    }
                }
                break;
            case 2:
                this.loadTexture("forrest.png", 0);
                break;
            case 3:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_left.png", 0);
                break;
            case 4:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_top.png", 0);
                break;
            case 5:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_right.png", 0);
                break;
            case 6:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_bot.png", 0);
                break;
            case 7:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_bot_right.png", 0);
                break;
            case 8:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_bot_left.png", 0);
                break;
            case 9:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_top_left.png", 0);
                break;
            case 10:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_top_right.png", 0);
                break;
            case 11:
                this.addWater();
                this.addToWater("water_left.png");
                break;
            case 12:
                this.addWater();
                this.addToWater("water_top.png");
                break;
            case 13:
                this.addWater();
                this.addToWater("water_right.png");
                break;
            case 14:
                this.addWater();
                this.addToWater("water_bot.png");
                break;
            case 15:
                this.addWater();
                this.addToWater("water_bot_right.png");
                break;
            case 16:
                this.addWater();
                this.addToWater("water_bot_left.png");
                break;
            case 17:
                this.addWater();
                this.addToWater("water_top_left.png");
                break;
            case 18:
                this.addWater();
                this.addToWater("water_top_right.png");
                break;
            case 19:
                this.addWater();
                this.addToWater("water_ecke_top_left.png");
                break;
            case 20:
                this.addWater();
                this.addToWater("water_ecke_top_right.png");
                break;
            case 21:
                this.addWater();
                this.addToWater("water_ecke_bot_left.png");
                break;
            case 22:
                this.addWater();
                this.addToWater("water_ecke_bot_right.png");
                break;
            case 23:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_ecke_top_left.png", 0);
                break;
            case 24:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_ecke_top_right.png", 0);
                break;
            case 25:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_ecke_bot_left.png", 0);
                break;
            case 26:
                this.loadTexture("grass.png", 0);
                this.loadTexture("forrest_ecke_bot_right.png", 0);
                break;
            case 27:
                this.loadTexture("grass.png", 0);
                this.loadTexture("eisen.png", 0);
                break;
            case 28:
                this.loadTexture("grass.png", 0);
                this.loadTexture("kohle.png", 0);
                break;
        }
    }
}
