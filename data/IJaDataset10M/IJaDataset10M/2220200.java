package org.glossitope.container.wm.buffered.manage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXInsets;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.TextPainter;
import org.glossitope.container.security.DeskletConfig;
import org.glossitope.container.security.Registry;
import org.glossitope.container.util.AnimRepainter;
import org.glossitope.container.wm.buffered.Buffered2DPeer;
import org.glossitope.container.wm.buffered.BufferedDeskletContainer;
import org.glossitope.container.wm.buffered.BufferedWM;
import org.glossitope.container.wm.buffered.DeskletProxy;
import org.glossitope.container.wm.buffered.DeskletRenderPanel;
import org.glossitope.container.wm.buffered.animations.StartAnimAfter;

/**
 *
 * @author joshy
 */
public class ManagePanelAnimations {

    /** Creates a new instance of ManagePanelAnimations */
    public ManagePanelAnimations(BufferedWM wm) {
        this.wm = wm;
        rootPanel = (DeskletRenderPanel) wm.getRenderPanel();
        try {
            closeIcon = new ImageIcon(ImageIO.read(getClass().getResource("/images/close2.png")));
            closeOverIcon = new ImageIcon(ImageIO.read(getClass().getResource("/images/close2_over.png")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private List<JPanel> manageButtons;

    private BufferedWM wm;

    private final int transitionLength = 500;

    private final int manageButtonGap = 60;

    private final int manageButtonSpacing = 10;

    private final int manageButtonWidth = 270;

    private final int manageButtonHeight = 55;

    private static final int firstColumnX = 100;

    static final int scaledWidth = 200;

    static final int scaledHeight = 100;

    static final int rowGap = 20;

    static final int columnGap = 70;

    private Icon closeIcon;

    private Icon closeOverIcon;

    private DeskletRenderPanel rootPanel;

    private static final MultipleGradientPaint buttonGradient = new LinearGradientPaint(new Point(0, 20), new Point(0, 10), new float[] { 0f, 0.49f, 0.5f, 1f }, new Color[] { new Color(0x2C7FA8), new Color(0x308AB7), new Color(0x359BCD), new Color(0x4BA5D2) });

    public void showManagePanel() {
        manageButtons = new ArrayList<JPanel>();
        List<DeskletConfig> configs = Registry.getInstance().getDeskletConfigs();
        Animator firstAnim = new Animator(1);
        Animator prevAnim = firstAnim;
        Animator lastAnim = firstAnim;
        int y = 0;
        final int x = wm.getRenderPanel().getWidth() - manageButtonWidth - 20;
        int animlen = 20;
        if (configs.size() > 0) {
            animlen = transitionLength / configs.size();
        }
        for (DeskletConfig cfg : configs) {
            final DeskletConfig config = cfg;
            final ManageDeskletPanel panel = new ManageDeskletPanel();
            panel.setDeskletName(cfg.getName());
            panel.setDeskletDescription(cfg.getDescription());
            panel.addButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    wm.start(config);
                }
            });
            panel.setOpaque(false);
            panel.setPadding(new JXInsets(3));
            RectanglePainter rect = new RectanglePainter(2, 2, 2, 2, 10, 10, true, buttonGradient, 2, Color.WHITE);
            rect.setPaintStretched(true);
            panel.setBackgroundPainter(rect);
            manageButtons.add(panel);
            final int startY = y;
            final int endY = y + manageButtonGap;
            final Animator propAnim = PropertySetter.createAnimator(animlen, panel, "location", new Point(x, startY), new Point(x, endY));
            TimingTarget creator = new TimingTarget() {

                public void begin() {
                    rootPanel.add(panel);
                    panel.setLocation(x, startY);
                    Dimension dim = new Dimension(manageButtonWidth, manageButtonHeight);
                    panel.setPreferredSize(dim);
                    panel.setSize(dim);
                    panel.validate();
                }

                public void end() {
                }

                public void repeat() {
                }

                public void timingEvent(float f) {
                }
            };
            propAnim.addTarget(creator);
            prevAnim.addTarget(new StartAnimAfter(propAnim));
            prevAnim = propAnim;
            lastAnim = propAnim;
            y += 40 + 20;
        }
        firstAnim.start();
    }

    public void hideManagePanel() {
        Animator startAnim = new Animator(1);
        Animator prevAnim = startAnim;
        int animlen = 20;
        if (manageButtons.size() > 0) {
            animlen = transitionLength / manageButtons.size();
        }
        for (int i = manageButtons.size() - 1; i >= 0; i--) {
            final JPanel panel = manageButtons.get(i);
            final Animator propAnim = PropertySetter.createAnimator(animlen, panel, "location", panel.getLocation(), new Point(panel.getLocation().x, panel.getLocation().y - manageButtonGap));
            propAnim.addTarget(new TimingTarget() {

                public void begin() {
                }

                public void end() {
                    ((JComponent) wm.getRenderPanel()).remove(panel);
                }

                public void repeat() {
                }

                public void timingEvent(float f) {
                }
            });
            prevAnim.addTarget(new StartAnimAfter(propAnim));
            prevAnim = propAnim;
        }
        startAnim.start();
        manageButtons = null;
    }

    Map<Buffered2DPeer, Point2D> originalLocations = new HashMap<Buffered2DPeer, Point2D>();

    Map<BufferedDeskletContainer, JButton> stopButtons = new HashMap<BufferedDeskletContainer, JButton>();

    Map<BufferedDeskletContainer, JXPanel> rolloverPanels = new HashMap<BufferedDeskletContainer, JXPanel>();

    public void moveDeskletsToColumns(final AbstractButton button) {
        Animator anim = new Animator(500);
        anim.addTarget(new AnimButtonDisabler(button));
        int i = 0;
        for (DeskletProxy proxy : wm.getProxies()) {
            if (proxy.contentContainer.getPeer() instanceof Buffered2DPeer) {
                final Buffered2DPeer peer = (Buffered2DPeer) proxy.contentContainer.getPeer();
                final BufferedDeskletContainer bdc = proxy.contentContainer;
                originalLocations.put(peer, peer.getLocation());
                double targetScale = calculateScale(peer);
                anim.addTarget(new PropertySetter(peer, "scale", 1.0, targetScale));
                Point2D pt = calculateLocation((JComponent) wm.getRenderPanel(), i);
                anim.addTarget(new PropertySetter(peer, "location", originalLocations.get(peer), pt));
                anim.addTarget(new TimingTarget() {

                    public void begin() {
                    }

                    public void end() {
                        final JButton close = new JButton();
                        close.setText("");
                        close.setIcon(closeIcon);
                        close.setRolloverIcon(closeOverIcon);
                        close.setBorderPainted(false);
                        close.setOpaque(false);
                        close.setContentAreaFilled(false);
                        close.setLocation((int) peer.getLocation().getX() - 80, (int) peer.getLocation().getY());
                        ((JComponent) wm.getRenderPanel()).add(close);
                        stopButtons.put(bdc, close);
                        final JXPanel panel = new JXPanel();
                        Dimension dim = new Dimension(scaledWidth + 10 * 2, scaledHeight + 10 * 2);
                        panel.setPreferredSize(dim);
                        panel.setSize(dim);
                        panel.setLocation((int) peer.getLocation().getX() - 10, (int) bdc.getLocation().getY() - 10);
                        panel.setOpaque(false);
                        rootPanel.add(panel);
                        rolloverPanels.put(bdc, panel);
                        String text = bdc.getContext().getConfig().getName();
                        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
                        AlphaPainter alpha = new AlphaPainter();
                        alpha.setAlpha(0f);
                        alpha.setCacheable(false);
                        alpha.setPainters(new RectanglePainter(5, 5, 5, 5, 20, 20, true, new Color(0, 0, 0, 160), 3.0f, Color.WHITE), new TextPainter(text, font, Color.WHITE));
                        panel.setBackgroundPainter(alpha);
                        final Animator anim = new Animator(500);
                        anim.addTarget(new PropertySetter(alpha, "alpha", 0f, 1f));
                        anim.addTarget(new AnimRepainter(panel));
                        panel.addMouseListener(new MouseListener() {

                            public void mouseClicked(MouseEvent e) {
                            }

                            public void mouseEntered(MouseEvent e) {
                                float fract = 0f;
                                if (anim.isRunning()) {
                                    fract = anim.getTimingFraction();
                                    anim.stop();
                                }
                                anim.setDirection(Animator.Direction.FORWARD);
                                anim.setInitialFraction(fract);
                                anim.start();
                            }

                            public void mouseExited(MouseEvent e) {
                                float fract = 1f;
                                if (anim.isRunning()) {
                                    fract = anim.getTimingFraction();
                                    anim.stop();
                                }
                                anim.setDirection(Animator.Direction.BACKWARD);
                                anim.setInitialFraction(fract);
                                anim.start();
                            }

                            public void mousePressed(MouseEvent e) {
                            }

                            public void mouseReleased(MouseEvent e) {
                            }
                        });
                        close.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                wm.stop(bdc);
                                rootPanel.remove(close);
                                rootPanel.remove(panel);
                            }
                        });
                    }

                    public void repeat() {
                    }

                    public void timingEvent(float f) {
                    }
                });
            }
            i++;
        }
        anim.addTarget(new AnimRepainter(rootPanel));
        anim.addTarget(new ToggleAnimatingProperty());
        anim.start();
    }

    public static Point2D calculateLocation(JComponent panel, int i) {
        int x = firstColumnX;
        int y = i * (scaledHeight + rowGap) + 50;
        while (y > panel.getHeight() - scaledHeight - rowGap) {
            x += scaledWidth + columnGap;
            y -= (panel.getHeight() - scaledHeight - rowGap);
        }
        return new Point(x, y);
    }

    public static double calculateScale(Buffered2DPeer bdc) {
        double targetScale = scaledWidth / bdc.getSize().getWidth();
        if (bdc.getSize().getWidth() > scaledWidth || bdc.getSize().getHeight() > scaledHeight) {
            if (targetScale * bdc.getSize().getHeight() > scaledHeight) {
                targetScale = scaledHeight / bdc.getSize().getHeight();
            }
        }
        return targetScale;
    }

    public void moveDeskletsToOriginalPositions(final AbstractButton button) {
        Animator anim = new Animator(500);
        anim.addTarget(new AnimButtonDisabler(button));
        for (DeskletProxy dc : wm.getProxies()) {
            if (dc.contentContainer.getPeer() instanceof Buffered2DPeer) {
                Buffered2DPeer peer = (Buffered2DPeer) dc.contentContainer.getPeer();
                anim.addTarget(new PropertySetter(peer, "location", peer.getLocation(), originalLocations.get(peer)));
                if (peer.getScale() != 1.0) {
                    anim.addTarget(new PropertySetter(peer, "scale", peer.getScale(), 1.0));
                }
            }
        }
        anim.addTarget(new AnimRepainter(rootPanel));
        anim.addTarget(new TimingTarget() {

            public void begin() {
                for (JButton b : stopButtons.values()) {
                    rootPanel.remove(b);
                }
                stopButtons.clear();
                for (JXPanel p : rolloverPanels.values()) {
                    rootPanel.remove(p);
                }
                rolloverPanels.clear();
            }

            public void end() {
            }

            public void repeat() {
            }

            public void timingEvent(float f) {
            }
        });
        anim.addTarget(new ToggleAnimatingProperty());
        anim.start();
        originalLocations.clear();
    }

    private class ToggleAnimatingProperty implements TimingTarget {

        public void begin() {
            rootPanel.setAnimating(true);
        }

        public void end() {
            rootPanel.setAnimating(false);
        }

        public void repeat() {
        }

        public void timingEvent(float f) {
        }
    }

    private class AnimButtonDisabler implements TimingTarget {

        private AbstractButton button;

        public AnimButtonDisabler(AbstractButton button) {
            super();
            this.button = button;
        }

        public void begin() {
            button.setEnabled(false);
        }

        public void end() {
            button.setEnabled(true);
        }

        public void repeat() {
        }

        public void timingEvent(float f) {
        }
    }

    private class AnimPanelRollover implements MouseListener {

        private JXPanel panel;

        private Painter normal;

        private Painter rollover;

        public AnimPanelRollover(JXPanel panel, Painter normal, Painter rollover) {
            super();
            this.panel = panel;
            this.normal = normal;
            this.rollover = rollover;
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
            panel.setBackgroundPainter(rollover);
        }

        public void mouseExited(MouseEvent e) {
            panel.setBackgroundPainter(normal);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
