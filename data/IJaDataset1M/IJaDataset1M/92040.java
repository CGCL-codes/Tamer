package BitmapFont;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.media.opengl.GL;

public class BitmapFonts {

    int nofchs;

    int pointSize;

    int maxWidth;

    int height;

    int leading;

    int ascent;

    int descent;

    BitmapFont[] fonts;

    int[] indexByChar;

    static final char minChar = 32;

    static final char maxChar = 127;

    static final int maxAllowedWidth = 100;

    static final int maxAllowedHeight = 100;

    public BitmapFonts() {
        indexByChar = new int[maxChar + 1];
        for (int i = 0; i <= maxChar; i++) {
            indexByChar[i] = 0;
        }
    }

    public void generate(String fname, int fsize) {
        nofchs = maxChar - minChar + 1;
        BufferedImage bim = new BufferedImage(maxAllowedWidth, maxAllowedHeight, BufferedImage.TYPE_INT_RGB);
        int[] B = new int[maxAllowedWidth * maxAllowedHeight];
        Graphics g = bim.getGraphics();
        g.setFont(new Font(fname, Font.BOLD, fsize));
        FontMetrics fm = g.getFontMetrics();
        pointSize = fsize;
        maxWidth = 0;
        ascent = fm.getAscent();
        descent = fm.getDescent();
        leading = fm.getLeading();
        height = pointSize;
        fonts = new BitmapFont[nofchs];
        String ascii;
        int i = 0;
        for (char ch = minChar; ch <= maxChar; ch++) {
            ascii = "" + ch;
            g.clearRect(0, 0, maxAllowedWidth, maxAllowedHeight);
            g.setColor(Color.green);
            g.drawString(ascii, 0, fm.getLeading() + fm.getAscent());
            BitmapFont f = new BitmapFont(ch);
            f.generate(bim, B, fm);
            if (maxWidth < fm.charWidth(ch)) maxWidth = fm.charWidth(ch);
            fonts[i++] = f;
            indexByChar[ch] = i;
        }
    }

    public void write(DataOutputStream fout) throws IOException {
        fout.writeInt(nofchs);
        fout.writeInt(pointSize);
        fout.writeInt(maxWidth);
        fout.writeInt(height);
        fout.writeInt(leading);
        fout.writeInt(ascent);
        fout.writeInt(descent);
        for (int i = 0; i < nofchs; i++) {
            BitmapFont f = fonts[i];
            f.write(fout);
        }
    }

    public void read(DataInputStream fin) throws IOException {
        nofchs = fin.readInt();
        pointSize = fin.readInt();
        maxWidth = fin.readInt();
        height = fin.readInt();
        leading = fin.readInt();
        ascent = fin.readInt();
        descent = fin.readInt();
        fonts = new BitmapFont[nofchs];
        for (int i = 0; i < nofchs; i++) {
            BitmapFont f = new BitmapFont();
            f.read(fin);
            fonts[i] = f;
            indexByChar[f.ch] = i;
        }
    }

    public void print(boolean verbose) {
        System.out.println("number of characters: " + nofchs);
        System.out.println("point size: " + pointSize);
        System.out.println("maximum width: " + maxWidth);
        System.out.println("height: " + height);
        System.out.println("leading: " + leading);
        System.out.println("ascent: " + ascent);
        System.out.println("descent: " + descent);
        for (int i = 0; i < nofchs; i++) {
            BitmapFont f = fonts[i];
            f.print(verbose);
        }
    }

    public void setupGL(GL gl, int offset) {
        for (int i = 0; i < nofchs; i++) {
            BitmapFont f = fonts[i];
            gl.glNewList(f.ch + offset, GL.GL_COMPILE);
            gl.glBitmap(f.width, f.height, 0, descent, f.width, 0, f.getData());
            gl.glEndList();
        }
    }

    public int charWidth(char ch) {
        return fonts[indexByChar[ch]].width;
    }

    public int getDescent() {
        return descent;
    }
}
