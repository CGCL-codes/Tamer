package com.memoire.bu;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import com.memoire.fu.FuLog;

public class BuFileViewer extends BuPanel {

    BuFileRenderer renderer_ = null;

    BuProgressBar progress_ = null;

    public BuFileRenderer getFileRenderer() {
        return renderer_;
    }

    public void setFileRenderer(BuFileRenderer _renderer) {
        renderer_ = _renderer;
    }

    Runnable runnable_ = null;

    public void updateContent(final Object[] _dirs, final Object[] _files) {
        if (renderer_ == null) return;
        final Runnable r = new Runnable() {

            public void run() {
                int ld = _dirs.length;
                int lf = _files.length;
                int lt = ld + lf;
                int pp = 0;
                final Component[] v = new Component[lt];
                int n = 0;
                for (int i = 0; i < ld; i++) {
                    if (this != runnable_) return;
                    Component c = buildView(_dirs[i], n);
                    if (c != null) {
                        v[n] = c;
                        n++;
                        if ((progress_ != null) && (progress_.getParent() == BuFileViewer.this)) {
                            final int cp = 100 * n / lt;
                            if (cp != pp) {
                                pp = cp;
                                SwingUtilities.invokeLater(new Runnable() {

                                    public void run() {
                                        progress_.setValue(cp);
                                    }
                                });
                            }
                        }
                    }
                }
                for (int i = 0; i < lf; i++) {
                    if (this != runnable_) return;
                    Component c = buildView(_files[i], n);
                    if (c != null) {
                        v[n] = c;
                        n++;
                        if ((progress_ != null) && (progress_.getParent() == BuFileViewer.this)) {
                            final int cp = 100 * n / lt;
                            if (cp != pp) {
                                pp = cp;
                                SwingUtilities.invokeLater(new Runnable() {

                                    public void run() {
                                        progress_.setValue(cp);
                                    }
                                });
                            }
                        }
                    }
                }
                final int count = n;
                final Runnable ref = this;
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        setVisible(false);
                        if ((progress_ != null) && (progress_.getParent() == BuFileViewer.this)) remove(progress_);
                        for (int i = 0; i < count; i++) {
                            if (ref != runnable_) break;
                            add(v[i]);
                        }
                        Container p = getParent();
                        if (p instanceof JViewport) ((JViewport) p).setViewPosition(new Point(0, 0));
                        revalidate();
                        setVisible(true);
                        repaint();
                        if (ref == runnable_) runnable_ = null;
                    }
                });
            }
        };
        runnable_ = r;
        removeAll();
        if (_dirs.length + _files.length > 3) {
            if (progress_ == null) {
                progress_ = new BuProgressBar();
                Dimension ps = progress_.getPreferredSize();
                ps.width = 200;
                progress_.setPreferredSize(ps);
                progress_.setSize(ps);
            }
            progress_.setValue(0);
            add(progress_);
            revalidate();
            repaint();
        }
        if (SwingUtilities.isEventDispatchThread()) {
            Thread t = new Thread(r, "Explorer Content Updater");
            t.setPriority(Thread.NORM_PRIORITY - 1);
            t.start();
        } else {
            r.run();
        }
        new Thread(new Runnable() {

            public void run() {
                if (runnable_ != r) return;
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException ex) {
                }
                if (runnable_ != r) return;
                repaint();
            }
        }, "Explorer Micro Wait").start();
    }

    protected Component buildView(Object _o, int _n) {
        Component r = null;
        try {
            r = renderer_.getPanelCellRendererComponent(null, _o, _n, false, false);
        } catch (Throwable th) {
            FuLog.error(th);
        }
        return r;
    }
}
