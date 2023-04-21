package antirashka.ui;

import antirashka.engine.*;
import antirashka.icons.IconManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.HashSet;
import java.util.Set;

final class MapPanel extends JPanel implements IImageListener, Scrollable {

    private final JRadioButtonMenuItem menuItemAlways = new JRadioButtonMenuItem("Always center", true);

    private final JRadioButtonMenuItem menuItemMove = new JRadioButtonMenuItem("Center on move", false);

    private final JRadioButtonMenuItem menuItemNever = new JRadioButtonMenuItem("Never center", false);

    private final JCheckBoxMenuItem menuItemMap = new JCheckBoxMenuItem("Map");

    private final JFrame owner;

    private final IController controller;

    private final Dimension size;

    private final Dimension tile;

    private final BufferedImage imageCopy;

    private final Set<ISpecialEffect> effects = new HashSet<ISpecialEffect>();

    private final MapPreviewPanel preview;

    private JDialog previewDialog = null;

    private Point drag = null;

    MapPanel(final JFrame owner, final IController controller) {
        this.owner = owner;
        this.controller = controller;
        this.size = controller.getSize();
        this.tile = controller.getTile();
        this.imageCopy = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        this.preview = new MapPreviewPanel(this, tile, size);
        ButtonGroup group = new ButtonGroup();
        group.add(menuItemAlways);
        group.add(menuItemMove);
        group.add(menuItemNever);
        menuItemMap.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showPreviewDialog(owner);
            }
        });
        menuItemMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
        controller.addImageListener(this);
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                controller.mapMouseClick(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (drag != null) {
                    drag = null;
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                controller.mapMouseMove(e);
            }

            public void mouseDragged(MouseEvent e) {
                Point p = e.getPoint();
                if (drag == null) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    drag = p;
                } else {
                    Rectangle rect = getVisibleRect();
                    rect.x -= p.x - drag.x;
                    rect.y -= p.y - drag.y;
                    scrollRectToVisible(rect);
                }
            }
        });
        initKeys(this, this);
        setOpaque(true);
    }

    static void initKeys(final MapPanel panel, JComponent comp) {
        final IController controller = panel.controller;
        InputMap im = comp.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD8, 0), "up");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "down");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "left");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0), "right");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0), "upleft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD9, 0), "upright");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "downleft");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), "downright");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "move");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "fire");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "skip");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), "save");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK), "map");
        ActionMap am = comp.getActionMap();
        am.put("up", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.move(0, -1);
            }
        });
        am.put("down", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.move(0, 1);
            }
        });
        am.put("left", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.move(-1, 0);
            }
        });
        am.put("right", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.move(1, 0);
            }
        });
        am.put("upleft", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.move(-1, -1);
            }
        });
        am.put("upright", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.move(1, -1);
            }
        });
        am.put("downleft", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.move(-1, 1);
            }
        });
        am.put("downright", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.move(1, 1);
            }
        });
        am.put("cancel", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.cancel();
            }
        });
        am.put("move", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.moveCurrentPlayer(false, panel.getMousePosition());
            }
        });
        am.put("fire", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.fireCurrentPlayer(false, panel.getMousePosition());
            }
        });
        am.put("skip", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.skipCurrentPlayer();
            }
        });
        am.put("save", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                controller.save(MainFrame.SAVE_FILE);
            }
        });
        am.put("map", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                panel.menuItemMap.setSelected(!panel.menuItemMap.isSelected());
                panel.showPreviewDialog(panel.owner);
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        synchronized (imageCopy) {
            g.drawImage(imageCopy, 0, 0, this);
        }
        synchronized (effects) {
            for (ISpecialEffect effect : effects) {
                effect.paint(g);
            }
        }
    }

    void paintPreview(Graphics g, AffineTransform transform) {
        synchronized (imageCopy) {
            ((Graphics2D) g).drawImage(imageCopy, transform, preview);
        }
    }

    public Dimension getPreferredSize() {
        return size;
    }

    public void callRepaint(RenderedImage image, final int x, final int y, final int width, final int height) {
        synchronized (imageCopy) {
            image.copyData(imageCopy.getRaster());
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                repaint(x, y, width, height);
                preview.doRepaint(x, y, width, height);
            }
        });
    }

    public void callScrollTo(int centerX, int centerY, CenterMode mode, boolean force) {
        if (mode == CenterMode.MOVE) {
            if (menuItemNever.isSelected()) return;
        } else if (mode == CenterMode.ROUND_START) {
            if (menuItemNever.isSelected() || menuItemMove.isSelected()) return;
        }
        Rectangle rect = getVisibleRect();
        if (!force && rect.contains(centerX, centerY)) {
            return;
        }
        rect.x += centerX - (rect.x + rect.width / 2);
        rect.y += centerY - (rect.y + rect.height / 2);
        scrollRectToVisible(rect);
    }

    public void setCursor(CursorType type) {
        IconManager icons = IconManager.getInstance();
        switch(type) {
            case TARGET:
                setCursor(icons.getCursor("target"));
                break;
            case LEFT:
                setCursor(icons.getCursor("left"));
                break;
            case LEFT_UP:
                setCursor(icons.getCursor("left_up"));
                break;
            case LEFT_DOWN:
                setCursor(icons.getCursor("left_down"));
                break;
            case RIGHT:
                setCursor(icons.getCursor("right"));
                break;
            case RIGHT_UP:
                setCursor(icons.getCursor("right_up"));
                break;
            case RIGHT_DOWN:
                setCursor(icons.getCursor("right_down"));
                break;
            case UP:
                setCursor(icons.getCursor("up"));
                break;
            case DOWN:
                setCursor(icons.getCursor("down"));
                break;
            default:
                setCursor(Cursor.getDefaultCursor());
                break;
        }
    }

    public void addSpecialEffect(final ISpecialEffect effect) {
        synchronized (effects) {
            effects.add(effect);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                repaint(effect.getBounds());
            }
        });
    }

    public void removeSpecialEffect(final ISpecialEffect effect) {
        synchronized (effects) {
            effects.remove(effect);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                repaint(effect.getBounds());
            }
        });
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            return tile.height;
        } else {
            return tile.width;
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return getScrollableUnitIncrement(visibleRect, orientation, direction);
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    void addCenterMenu(JComponent menu) {
        menu.add(menuItemAlways);
        menu.add(menuItemMove);
        menu.add(menuItemNever);
    }

    void addMapMenu(JComponent menu) {
        menu.add(menuItemMap);
    }

    void showPreviewDialog(JFrame owner) {
        if (menuItemMap.isSelected()) {
            if (previewDialog == null) {
                previewDialog = new JDialog(owner, "Map", false);
                previewDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
                previewDialog.add(new JScrollPane(preview), BorderLayout.CENTER);
                previewDialog.setPreferredSize(new Dimension(400, 400));
                previewDialog.pack();
                previewDialog.setLocationRelativeTo(owner);
                previewDialog.addWindowListener(new WindowAdapter() {

                    public void windowClosing(WindowEvent e) {
                        menuItemMap.setSelected(false);
                    }
                });
            }
            previewDialog.setVisible(true);
        } else {
            if (previewDialog != null) {
                previewDialog.setVisible(false);
            }
        }
    }

    void dispose() {
        previewDialog.dispose();
    }
}
