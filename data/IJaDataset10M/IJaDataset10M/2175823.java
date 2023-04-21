package com.googlecode.boringengine;

import java.util.Scanner;
import com.googlecode.boringengine.internal.Renderer;
import java.io.InputStream;

public class Graphics {

    private static Renderer r;

    private static GameLoader loader;

    private static double ups, fps;

    public static void init(Renderer render) {
        r = render;
    }

    public static void gameChange(GameLoader l) {
        loader = l;
    }

    public static Sprite loadSpriteFromFile(String fileName) {
        InputStream in = loader.getFile(fileName);
        if (in == null) {
            Log.error("File %s not found", fileName);
            return r.createSprite("0", 1, 1);
        }
        Scanner input = new Scanner(in);
        return loadSpriteFromScanner(input);
    }

    public static Sprite loadSpriteFromScanner(Scanner in) {
        String line, spriteStr = "";
        int w = 0, h = 0;
        while (in.hasNextLine()) {
            line = in.nextLine();
            if (line.equals("")) return r.createSprite(spriteStr, w, h);
            spriteStr += line;
            w = line.length();
            h++;
        }
        return r.createSprite(spriteStr, w, h);
    }

    public static void drawRect(int x, int y, int w, int h, int color) {
        r.moveOverWithoutScale(x, y);
        r.drawRect(w, h, color);
        r.moveOverWithoutScale(-x, -y);
    }

    public static void drawSprite(Sprite s, int x, int y) {
        r.moveOverWithoutScale(x, y);
        r.drawSprite(s);
        r.moveOverWithoutScale(-x, -y);
    }

    public static void drawSprite(Sprite s, int x, int y, double scale) {
        double oldScale = r.getScale();
        r.addScale(scale);
        r.moveOverWithoutScale(x, y);
        r.drawSprite(s);
        r.moveOverWithoutScale(-x, -y);
        r.setScale(oldScale);
    }

    public static String getFPSString() {
        return String.format("%.2f UPS, %.2f FPS", ups, fps);
    }

    public static void setUPS(double u) {
        ups = u;
    }

    public static void setFPS(double f) {
        fps = f;
    }
}
