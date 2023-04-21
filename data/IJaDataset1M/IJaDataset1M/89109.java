package org.retro.gis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class RelatePanel extends JPanel implements Runnable {

    private static final int maxTimer = 10;

    private static final int state_Load = 0;

    private static final int state_RunSOM = 1;

    private final Font _font;

    private final Font _dataFont;

    private int lastScrWidth = -1;

    private int lastScrHeight = -1;

    private int midScrWidth = -1;

    private int midScrHeight = -1;

    private Thread _thread = null;

    private int _timer = 0;

    private int _state = state_Load;

    private RenderDataPoints _network = null;

    {
        _font = new Font("Arial", Font.BOLD, 12);
        _dataFont = new Font("Arial", Font.PLAIN, 9);
    }

    public RelatePanel() {
        super();
        this.setBackground(Color.white);
        _thread = null;
    }

    public void refreshScreenStats() {
        lastScrWidth = getWidth();
        lastScrHeight = getHeight();
        midScrWidth = lastScrWidth / 2;
        midScrHeight = lastScrHeight / 2;
        repaint();
    }

    public void start() {
        if (_thread == null) {
            _timer = 0;
            _state = state_Load;
            _thread = new Thread(this);
            _thread.start();
            _network = new RenderDataPoints(lastScrWidth, lastScrHeight);
        }
    }

    public void setList(java.util.List _l) {
        if (_network != null) {
            _network.setList(_l);
        }
    }

    public void stop() {
        _thread = null;
        _network = null;
    }

    public void run() {
        while (_thread != null) {
            try {
                Thread.sleep(200);
                changeState();
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void changeState() {
        _timer++;
        if (_timer >= maxTimer) {
            _timer = 0;
            switch(_state) {
                case 0:
                    _state = state_RunSOM;
                    if (_network != null) {
                        _network.renderWeights();
                        repaint();
                    }
                    break;
                case 1:
                    _state = state_RunSOM;
                    if (_network != null) {
                        _network.renderWeights();
                        repaint();
                    }
                    break;
            }
            ;
        }
    }

    private void renderLineGrid(Graphics g) {
        g.drawLine(10, (midScrHeight + 4), (midScrWidth - 10), (midScrHeight + 4));
        g.drawLine((midScrWidth + 16), (midScrHeight + 4), (lastScrWidth - 10), (midScrHeight + 4));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(_font);
        g.setColor(new Color(10, 10, 210));
        g.drawString("Scheme Relate Builder [ heartbeat:" + _timer + " ]", 4, 20);
        if (lastScrHeight > 0) {
            g.setColor(new Color(10, 210, 5));
            g.drawOval(midScrWidth, midScrHeight, 9, 9);
            renderLineGrid(g);
            g.setFont(_dataFont);
            g.setColor(new Color(10, 10, 180));
            g.drawString("data[ loaded ]", midScrWidth, midScrHeight);
            if (_network != null) {
                _network.renderSystem(g);
            }
        }
    }
}
