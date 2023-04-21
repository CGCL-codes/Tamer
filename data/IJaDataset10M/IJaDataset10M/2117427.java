package org.vesuf.util;

import java.awt.*;
import java.applet.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *  An applet displaying a load progress indicator.
 *  pa_code The starting class.
 *  pa_archive The archives to load.
 *  pa_text The text to be diplayed while loading.
 */
public class ProgressApplet extends Applet implements AppletStub {

    /** Display text. */
    protected String text;

    /** Progress percentage. */
    protected int linepct;

    /**
	 *  Init the progress applet.
	 */
    public void init() {
        this.linepct = 0;
        this.text = getParameter("pa_text");
        this.setBackground(Color.white);
        new Thread(new Runnable() {

            public void run() {
                try {
                    loadArchives();
                    String code = getParameter("pa_code");
                    Class clazz = Class.forName(code);
                    Applet applet = (Applet) clazz.newInstance();
                    applet.setStub(ProgressApplet.this);
                    ProgressApplet.this.setLayout(new BorderLayout());
                    ProgressApplet.this.add("Center", applet);
                    applet.init();
                    validate();
                    applet.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressApplet.this.setLayout(new BorderLayout());
                    ProgressApplet.this.add("Center", new Label(e.toString()));
                }
            }
        }).start();
    }

    /**
	 *  Request to resize the contained applet.
	 */
    public void appletResize(int width, int height) {
        Insets insets = getInsets();
        width += insets.left + insets.right;
        height += insets.top + insets.bottom;
        resize(width, height);
    }

    /**
	 *  paint the status line component.
	 */
    public void paint(java.awt.Graphics g) {
        Rectangle r = this.getBounds();
        FontMetrics fm = g.getFontMetrics();
        int y0 = (r.height - (fm.getHeight() + 6)) / 2;
        int y1 = (r.height + (fm.getHeight() + 6)) / 2 - 1;
        g.setColor(Color.gray);
        g.drawLine(0, y0, r.width - 2, y0);
        g.drawLine(0, y0, 0, y1 - 1);
        g.setColor(Color.white);
        g.drawLine(r.width - 1, y0, r.width - 1, y1);
        g.drawLine(0, y1, r.width - 1, y1);
        paintLine(g, linepct);
    }

    public void update(Graphics g) {
        paint(g);
    }

    /**
	 *  Display progress.
	 */
    protected void setLine(int percent) {
        if (linepct != percent) {
            linepct = percent;
            if (isVisible()) {
                java.awt.Graphics g = getGraphics();
                if (g != null) {
                    paintLine(g, percent);
                    g.dispose();
                }
            }
        }
    }

    /**
	 *  Draw the status line contents.
	 */
    protected void paintLine(java.awt.Graphics g, int linepct) {
        Rectangle r = this.getBounds();
        FontMetrics fm = g.getFontMetrics();
        r.width -= 2;
        Image buffer = createImage(r.width, fm.getHeight() + 4);
        Graphics g2 = buffer.getGraphics();
        g2.setColor(Color.lightGray);
        g2.fillRect(0, 0, r.width, r.height);
        if (linepct >= 0) {
            int len = Math.min((r.width * linepct) / 100, r.width);
            String line = text + " " + linepct + "%";
            int textx = 3;
            int texty = fm.getAscent() + fm.getLeading() / 2 + 2;
            g2.setClip(0, 0, len, r.height);
            g2.setColor(new Color(0, 0, 125));
            g2.fillRect(0, 0, len, r.height);
            g2.setColor(Color.white);
            g2.drawString(line, textx, texty);
            g2.setClip(len, 0, r.width - len, r.height);
            g2.setColor(Color.black);
            g2.drawString(line, textx, texty);
        }
        g2.dispose();
        g.drawImage(buffer, 1, (r.height - (fm.getHeight() + 6)) / 2 + 1, this);
    }

    /**
	  *  Load archives.
	  */
    protected void loadArchives() throws IOException, MalformedURLException {
        System.out.println("Loading... ");
        int size = 0;
        StringTokenizer stok = new StringTokenizer(getParameter("pa_archive"), ",");
        String[] jars = new String[stok.countTokens()];
        URL[] urls = new URL[stok.countTokens()];
        for (int i = 0; stok.hasMoreTokens(); i++) {
            jars[i] = stok.nextToken().trim();
            urls[i] = new URL(getCodeBase(), jars[i]);
            size += urls[i].openConnection().getContentLength();
            System.gc();
        }
        System.out.println("Overall size: " + size);
        int cnt = 0;
        int read;
        for (int i = 0; i < urls.length; i++) {
            InputStream is = urls[i].openConnection().getInputStream();
            byte buf[] = new byte[4096];
            while ((read = is.read(buf)) != -1) {
                cnt += read;
                setLine(cnt * 100 / size);
            }
            System.out.println("Loaded: " + jars[i]);
            is.close();
        }
        setLine(-1);
    }
}
