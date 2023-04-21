package main.newViz;

import framework.Game;
import framework.GameRenderer;
import framework.Person;
import impl.game.grid.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

/**
 * Date: 14.05.2008
 * Time: 19:48:48
 *
 * @author Denis DIR Rozhnev
 */
class DrawPanel extends JPanel {

    private static final Object ANTIALIASING = RenderingHints.VALUE_ANTIALIAS_ON;

    private static final Object RENDERING = RenderingHints.VALUE_RENDER_SPEED;

    private static final boolean VERBOSE = false;

    private GameRenderer renderer;

    protected int i = 0;

    protected float coord = 0;

    protected final int border = 2;

    public BufferedImage bimg;

    protected Player dragPlayer;

    private DropTarget dropTarget;

    private DropTargetListener dtListener;

    private int acceptableActions = DnDConstants.ACTION_COPY;

    public DrawPanel(Config config) {
        dtListener = new MyTargetListener();
        dropTarget = new DropTarget(this, this.acceptableActions, this.dtListener, true);
        dragPlayer = new Player(-10, -10, "");
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                Dimension d = getSize();
                bimg = createBinaryImage(d.width, d.height);
            }
        });
        renderer = config.getRenderer();
        config.registerObserver(new Config.Observer() {

            public void configUpdate(Config config) {
                renderer = config.getRenderer();
                config.getGame().addListener(new Game.Listener() {

                    public void tuned(int turnCount) {
                        repaint();
                    }
                });
            }
        });
    }

    private BufferedImage createBinaryImage(int w, int h) {
        return (BufferedImage) createImage(w, h);
    }

    public Graphics2D createGraphics2D(int width, int height, BufferedImage bi, Graphics g) {
        Graphics2D g2;
        if (bi != null) {
            g2 = bi.createGraphics();
        } else {
            g2 = (Graphics2D) g;
        }
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RENDERING);
        g2.clearRect(0, 0, width, height);
        return g2;
    }

    private int cnt = 0;

    private long t0 = System.currentTimeMillis();

    public void paint(Graphics g) {
        if (renderer == null) return;
        Dimension d = getSize();
        d.height -= 6;
        d.width -= 6;
        if (bimg == null) {
            bimg = createBinaryImage(d.width, d.height);
        }
        Graphics2D g2 = createGraphics2D(d.width, d.height, bimg, g);
        renderer.render(g2);
        dragPlayer.draw(g2);
        g.drawImage(bimg, 3, 3, null);
        g2.dispose();
        if (VERBOSE) {
            cnt++;
            if (cnt > 500) {
                long t = System.currentTimeMillis();
                System.out.println("FPS:" + (cnt * 1000 / (t - t0)));
                t0 = t;
                cnt = 0;
            }
        }
    }

    protected class MyTargetListener implements DropTargetListener {

        private boolean isDragOk(DropTargetDragEvent e) {
            return e.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        public void dragEnter(DropTargetDragEvent dtde) {
            if (!isDragOk(dtde)) {
                dtde.rejectDrag();
                return;
            }
            dtde.acceptDrag(dtde.getDropAction());
        }

        public void dragOver(DropTargetDragEvent dtde) {
            if (!isDragOk(dtde)) {
                dtde.rejectDrag();
                return;
            }
            dtde.acceptDrag(dtde.getDropAction());
            if (!dragPlayer.isInit()) {
                Object data = null;
                try {
                    data = dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    if (data == null) throw new NullPointerException();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                if (data instanceof String) {
                    try {
                        Class klass = Class.forName((String) data);
                        Person person = (Person) klass.newInstance();
                        dragPlayer.setName(person.getShortName());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
            dragPlayer.setLocation(dtde.getLocation());
            repaint();
        }

        public void dropActionChanged(DropTargetDragEvent dtde) {
            if (!isDragOk(dtde)) {
                dtde.rejectDrag();
                return;
            }
            dtde.acceptDrag(dtde.getDropAction());
        }

        public void dragExit(DropTargetEvent dte) {
            dragPlayer.setLocation(-10, -10);
            dragPlayer.setName("");
            repaint();
        }

        public void drop(DropTargetDropEvent dtde) {
            if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) try {
                dtde.acceptDrop(dtde.getDropAction());
                Object data = dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);
                if (data instanceof String) {
                    renderer.setPlayer(dtde.getLocation(), (String) data);
                    dtde.dropComplete(true);
                } else {
                    dtde.dropComplete(false);
                }
            } catch (Throwable t) {
                t.printStackTrace();
                dtde.dropComplete(false);
            } finally {
                dragExit(dtde);
            }
        }
    }
}
