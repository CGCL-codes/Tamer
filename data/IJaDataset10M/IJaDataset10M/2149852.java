package bb7.applet;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;

public class Futuregrapher7 extends Applet {

    private static final long serialVersionUID = -1203054989077417722L;

    int counterX = 0;

    int[] previousPrice = new int[1000];

    long[] previousTime = new long[1000];

    int[] x = new int[1000];

    int[] y = new int[1000];

    StringBuffer buffer;

    int yaxis1;

    int yaxis2;

    int yaxis3;

    int yaxis4;

    int yaxis5;

    String yaxis1label = "";

    String yaxis2label = "";

    String yaxis3label = "";

    String yaxis4label = "";

    String yaxis5label = "";

    public void init() {
        buffer = new StringBuffer();
    }

    public void start() {
        loadData();
        processData();
    }

    public void stop() {
    }

    public void destroy() {
    }

    void addItem(String newWord) {
        buffer.append(newWord);
        repaint();
    }

    public void loadData() {
        try {
            URL myURL = new URL("http://131.130.85.73:3886/transactions.db");
            DataInputStream in = new DataInputStream(myURL.openStream());
            do {
                previousPrice[counterX] = in.readInt();
                previousTime[counterX] = in.readLong();
                counterX = counterX + 1;
            } while (!(previousPrice[counterX - 1] == -1) && (counterX < 1000));
        } catch (IOException e) {
            addItem("IOException while loading data.");
        }
    }

    public void processData() {
        int minimumPrice = 99999;
        int maximumPrice = 0;
        int minimumDot = getSize().height - 25;
        int maximumDot = 15;
        float converter = 0.0f;
        for (int i = 0; i < counterX - 1; i++) {
            if (previousPrice[i] < minimumPrice) {
                minimumPrice = previousPrice[i];
            }
            if (previousPrice[i] > maximumPrice) {
                maximumPrice = previousPrice[i];
            }
        }
        converter = (float) (((double) (maximumDot - minimumDot)) / ((double) ((maximumPrice - minimumPrice))));
        yaxis1 = maximumPrice;
        yaxis2 = yaxis1 - (maximumPrice - minimumPrice) / 4;
        yaxis3 = yaxis2 - (maximumPrice - minimumPrice) / 4;
        yaxis4 = yaxis3 - (maximumPrice - minimumPrice) / 4;
        yaxis5 = minimumPrice;
        yaxis1label = (new Integer(yaxis1)).toString();
        yaxis2label = (new Integer(yaxis2)).toString();
        yaxis3label = (new Integer(yaxis3)).toString();
        yaxis4label = (new Integer(yaxis4)).toString();
        yaxis5label = (new Integer(yaxis5)).toString();
        yaxis1 = minimumDot + (int) (converter * (yaxis1 - minimumPrice));
        yaxis2 = minimumDot + (int) (converter * (yaxis2 - minimumPrice));
        yaxis3 = minimumDot + (int) (converter * (yaxis3 - minimumPrice));
        yaxis4 = minimumDot + (int) (converter * (yaxis4 - minimumPrice));
        yaxis5 = minimumDot + (int) (converter * (yaxis5 - minimumPrice));
        System.out.println("maximumPrice: " + maximumPrice + ", minimumPrice: " + minimumPrice + ", converter: " + converter);
        x[0] = 80;
        for (int i = 0; i < counterX - 1; i++) {
            y[i] = minimumDot + (int) (converter * (previousPrice[i] - minimumPrice));
            if (i > 0) {
                x[i] = x[i - 1] + (int) ((getSize().width - 100) / (counterX - 2));
            }
        }
    }

    public void paint(Graphics g) {
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        g.drawString("Price", 60, 12);
        g.drawString("Time", (getSize().width) / 2, getSize().height - 5);
        g.drawString(yaxis1label, 10, yaxis1);
        g.drawString(yaxis2label, 10, yaxis2);
        g.drawString(yaxis3label, 10, yaxis3);
        g.drawString(yaxis4label, 10, yaxis4);
        g.drawString(yaxis5label, 10, yaxis5);
        g.drawLine(80, 10, 80, getSize().height - 20);
        g.drawLine(80, getSize().height - 20, getSize().width - 20, getSize().height - 20);
        for (int i = 0; i < counterX - 2; i++) {
            g.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
        }
    }
}
