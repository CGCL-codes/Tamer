package ch.nostromo.jchessclock.gui.clock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import ch.nostromo.jchessclock.controller.JChessClockController;

public class ChessClockPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel panelAnalogClocks;

    private ClockPanel clockRight;

    private ClockPanel clockLeft;

    private JPanel panelDigitalClocks;

    private boolean demoMode = true;

    public ChessClockPanel() {
        super();
        initGUI();
        nosInitialize();
        demoMode();
    }

    public void nosInitialize() {
        getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "pressed");
        this.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    escapePressed();
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    pausePressed();
                } else {
                    switchPressed();
                }
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        this.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                panelDigitalClocks.setPreferredSize(new java.awt.Dimension(630, (int) (getSize().height * 0.1)));
            }

            public void componentHidden(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }
        });
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            this.setLayout(thisLayout);
            this.setPreferredSize(new java.awt.Dimension(630, 300));
            this.setBackground(Color.black);
            {
                panelAnalogClocks = new JPanel();
                GridLayout panelAnalogClocksLayout = new GridLayout(1, 1);
                panelAnalogClocks.setBackground(Color.black);
                panelAnalogClocksLayout.setColumns(1);
                panelAnalogClocksLayout.setHgap(5);
                panelAnalogClocksLayout.setVgap(5);
                panelAnalogClocks.setLayout(panelAnalogClocksLayout);
                {
                    clockLeft = new ClockPanel();
                    clockLeft.addClockPanelListener(new ClockPanelListener() {

                        public void countdownFinished() {
                            countdownReached();
                        }
                    });
                    panelAnalogClocks.add(clockLeft);
                }
                this.add(panelAnalogClocks, BorderLayout.CENTER);
                {
                    clockRight = new ClockPanel();
                    clockRight.addClockPanelListener(new ClockPanelListener() {

                        public void countdownFinished() {
                            countdownReached();
                        }
                    });
                    panelAnalogClocks.add(clockRight);
                }
            }
            {
                panelDigitalClocks = new JPanel();
                panelDigitalClocks.setBackground(Color.black);
                GridLayout panelDigitalClocksLayout = new GridLayout(1, 1);
                panelDigitalClocksLayout.setColumns(1);
                panelDigitalClocksLayout.setHgap(5);
                panelDigitalClocksLayout.setVgap(5);
                panelDigitalClocks.setLayout(panelDigitalClocksLayout);
                this.add(panelDigitalClocks, BorderLayout.SOUTH);
                panelDigitalClocks.setPreferredSize(new java.awt.Dimension(630, 42));
            }
            this.panelDigitalClocks.add(clockLeft.getDigitalLabel());
            this.panelDigitalClocks.add(clockRight.getDigitalLabel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void demoMode() {
        this.clockRight.resetClock(ClockPanel.ClockType.WATCH, 0, false);
        this.clockLeft.resetClock(ClockPanel.ClockType.WATCH, 0, false);
        this.clockLeft.start();
        this.clockRight.start();
        this.demoMode = true;
    }

    public void resetClock(double minLeft, double minRight, boolean leftStart) {
        this.clockRight.resetClock(ClockPanel.ClockType.COUNTDOWN, (long) (minRight * 60 * 1000), !leftStart);
        this.clockLeft.resetClock(ClockPanel.ClockType.COUNTDOWN, (long) (minLeft * 60 * 1000), leftStart);
        this.demoMode = false;
        this.repaint();
    }

    public void switchPressed() {
        if (!demoMode) {
            clockRight.switchButtonPressed();
            clockLeft.switchButtonPressed();
        }
    }

    public void pausePressed() {
        if (!demoMode) {
            clockRight.pauseButtonPressed();
            clockLeft.pauseButtonPressed();
        }
    }

    private void countdownReached() {
        JChessClockController.getInstance().countdownReached();
    }

    private void escapePressed() {
        JChessClockController.getInstance().escapePressed();
    }
}
