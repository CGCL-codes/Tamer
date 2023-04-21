package com.freelancer.imgdec;

import com.atolsystems.atolutilities.AFileUtilities;
import com.atolsystems.atolutilities.AListUtilities;
import com.atolsystems.atolutilities.AStringUtilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Paint extends java.awt.Frame {

    Image image;

    static byte[] img;

    static int xSize = 2;

    static int ySize = (17713 - 1) / xSize;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("crypted-hex-openusinghexeditor");
        img = AFileUtilities.file2Bytes(file);
        System.out.println(img.length);
        List<Integer> primes = primeFactors(img.length - 1);
        AListUtilities.println(System.out, primes);
        HashSet<Integer> used = new HashSet<Integer>();
        int colorCnt = 0;
        for (int i = 0; i < img.length; i++) {
            if (used.add(0xFF & img[i])) {
                System.out.println(AStringUtilities.byteToHex(img[i]));
                colorCnt++;
            }
        }
        System.out.println(colorCnt);
        new Paint();
    }

    long start = System.currentTimeMillis();

    public Paint() {
        setTitle("Paint an Icon example!");
        setSize(123 * 5, 144 * 5);
        setVisible(true);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        xSize = 123;
    }

    int cnt2 = 0;

    @Override
    public void paint(Graphics g) {
        Toolkit tool = Toolkit.getDefaultToolkit();
        cnt2++;
        cnt2 = cnt2 % 2;
        xSize += cnt2;
        xSize = 123;
        ySize = (17713 - 1) / xSize;
        System.out.println();
        System.out.println(xSize);
        System.out.println(ySize);
        paintRgb8(g, img, xSize, ySize);
    }

    public void paintRgb8(Graphics g, byte[] data, int xSize, int ySize) {
        int i = 0;
        for (int x = 0; x < xSize * 4; x += 4) {
            for (int y = 0; y < ySize * 4; y += 4) {
                Color color = rgb8ToColor(img[i]);
                i++;
                g.setColor(color);
                g.fillRect(100 + x, 100 + y, 4, 4);
            }
        }
    }

    Color rgb8ToColor(int rgb8) {
        Color color;
        int level = rgb8;
        if (level < 0) {
            level = level & 0xFF;
        }
        level ^= 0xFF & ~0xC6;
        {
            int r = 1 + (0x7 & (level >> 5));
            int g = 1 + (0x7 & (level >> 2));
            int b = 1 + (0x3 & (level >> 0));
            color = new Color((r * 256 / 8) - 1, (g * 256 / 8) - 1, (b * 256 / 4) - 1);
        }
        return color;
    }

    public static List<Integer> primeFactors(int numbers) {
        int n = numbers;
        List<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        if (n > 1) {
            factors.add(n);
        }
        return factors;
    }

    public class ScheduledInvalidate {

        Toolkit toolkit;

        Timer timer;

        public ScheduledInvalidate(int seconds) {
            toolkit = Toolkit.getDefaultToolkit();
            timer = new Timer();
            timer.schedule(new InvalidateTask(), seconds * 1000);
        }

        class InvalidateTask extends TimerTask {

            public void run() {
                Paint.this.repaint();
            }
        }
    }
}
