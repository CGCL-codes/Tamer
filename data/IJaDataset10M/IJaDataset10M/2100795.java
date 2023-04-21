package net.rptools.tokentool.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;
import net.rptools.lib.swing.SwingUtil;
import net.rptools.lib.transferable.FileListTransferable;
import net.rptools.lib.transferable.ImageTransferable;
import net.rptools.tokentool.TokenTool;

public class TokenPreviewPanel extends JComponent implements Observer, DragGestureListener, DragSourceListener {

    private static final Dimension PREFERRED_SIZE = new Dimension(100, 100);

    private static final int GRID_SIZE = 10;

    private FutureTask composeTask;

    private static ExecutorService composeThreadPool = Executors.newFixedThreadPool(1);

    boolean repaintRequested;

    private BufferedImage iconImage;

    public TokenPreviewPanel() {
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);
        setPreferredSize(PREFERRED_SIZE);
        setMinimumSize(PREFERRED_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension size = getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, size.width - 1, size.height - 1);
        g.setColor(Color.lightGray);
        for (int y = 0; y < size.height; y += GRID_SIZE) {
            for (int x = y % (GRID_SIZE * 2) == 0 ? 0 : GRID_SIZE; x < size.width; x += GRID_SIZE * 2) {
                g.fillRect(x, y, GRID_SIZE, GRID_SIZE);
            }
        }
        BufferedImage tokenImage = getIconImage();
        if (tokenImage == null) {
            return;
        }
        Dimension imgSize = new Dimension(tokenImage.getWidth(), tokenImage.getHeight());
        SwingUtil.constrainTo(imgSize, size.width, size.height);
        if (tokenImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            Object preRenderHint = g2d.getRenderingHint(RenderingHints.KEY_RENDERING);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(tokenImage, (size.width - imgSize.width) / 2, (size.height - imgSize.height) / 2, imgSize.width, imgSize.height, this);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, preRenderHint);
        }
    }

    private BufferedImage getIconImage() {
        if (iconImage == null) {
            update(null, null);
        }
        return iconImage;
    }

    public synchronized void update(Observable o, Object arg) {
        repaintRequested = true;
        if (composeTask != null && !composeTask.isDone()) {
            return;
        }
        composeTask = new FutureTask<BufferedImage>(new Runnable() {

            public void run() {
                while (repaintRequested) {
                    repaintRequested = false;
                    iconImage = TokenTool.getFrame().getComposedToken();
                    repaint();
                }
            }
        }, null);
        composeThreadPool.submit(composeTask);
    }

    private File tempTokenFile = new File(".token.drag.png");

    public void dragGestureRecognized(DragGestureEvent dge) {
        BufferedImage tokenImage = TokenTool.getFrame().getComposedToken();
        if (tokenImage == null) {
            return;
        }
        Transferable transferable = null;
        try {
            ImageIO.write(tokenImage, "png", tempTokenFile);
            transferable = new FileListTransferable(tempTokenFile);
        } catch (Exception e) {
            transferable = new ImageTransferable(tokenImage);
        }
        dge.startDrag(Toolkit.getDefaultToolkit().createCustomCursor(tokenImage, new Point(0, 0), "Thumbnail"), transferable, this);
    }

    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    public void dragEnter(DragSourceDragEvent dsde) {
    }

    public void dragExit(DragSourceEvent dse) {
    }

    public void dragOver(DragSourceDragEvent dsde) {
    }

    public void dropActionChanged(DragSourceDragEvent dsde) {
    }
}
