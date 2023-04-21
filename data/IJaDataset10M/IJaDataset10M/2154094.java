package org.glossitope.container.wm.buffered.animations;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.glossitope.container.util.AnimRepainter;
import org.glossitope.container.wm.buffered.Buffered2DPeer;
import org.glossitope.container.wm.buffered.BufferedDeskletContainer;
import org.glossitope.container.wm.buffered.BufferedWM;
import org.glossitope.container.wm.buffered.DeskletRenderPanel;
import org.glossitope.container.wm.buffered.manage.ManagePanelAnimations;

/**
 *
 * @author joshy
 */
public class StdCreationAnimation extends TransitionAnimation {

    private boolean oldAnim = false;

    /** Creates a new instance of StdCreationAnimation */
    public StdCreationAnimation() {
    }

    /**
     *
     * @param bdc
     * @return
     */
    public Animator createAnimation(TransitionEvent evt) {
        final BufferedWM wm = evt.getWindowManager();
        final JComponent panel = (JComponent) wm.getRenderPanel();
        final BufferedDeskletContainer dc = evt.getContainer();
        final Buffered2DPeer peer = (Buffered2DPeer) dc.getPeer();
        final Point2D initialLocation = dc.getLocation();
        if (oldAnim) {
            Animator anim = new Animator(750);
            anim.addTarget(new PropertySetter(dc, "location", dc.getLocation(), dc.getLocation()));
            anim.addTarget(new PropertySetter(dc, "alpha", 0f, 1f));
            anim.addTarget(new PropertySetter(dc, "rotation", Math.PI, Math.PI * 2.0));
            anim.addTarget(new PropertySetter(dc, "scale", 0.1, 1.0));
            return anim;
        } else {
            Animator anim = new Animator(1500);
            double startScale = 3.0;
            double endScale = 1.0;
            Dimension2D size = peer.getSize();
            size = new Dimension(250, 150);
            final Point2D center = new Point2D.Double(500, 300);
            Point2D start = new Point2D.Double(center.getX() - startScale * size.getWidth() / 2.0, center.getY() - startScale * size.getHeight() / 2.0);
            start = center;
            Point2D end = new Point2D.Double(center.getX() - endScale * size.getWidth() / 2.0, center.getY() - endScale * size.getHeight() / 2.0);
            end = center;
            final Equation bouncer = new BouncerEquation(1.0, 0.3, 0.058, 12.0, 0.0);
            calc(0.11f, peer, bouncer, center);
            anim.addTarget(new PropertySetter(peer, "alpha", 0f, 1f));
            anim.addTarget(new TimingTarget() {

                public void begin() {
                    if (panel instanceof DeskletRenderPanel) {
                        ((DeskletRenderPanel) panel).setAnimating(true);
                    }
                }

                public void end() {
                    Animator a2 = new Animator(300);
                    double targetScale = ManagePanelAnimations.calculateScale(peer);
                    int count = wm.getProxies().size();
                    Point2D pt = ManagePanelAnimations.calculateLocation(panel, count - 1);
                    if (!wm.isManageMode()) {
                        targetScale = 1.0;
                        pt = initialLocation;
                    }
                    a2.addTarget(new PropertySetter(peer, "scale", peer.getScale(), targetScale));
                    a2.addTarget(new PropertySetter(peer, "location", peer.getLocation(), pt));
                    a2.addTarget(new AnimRepainter(panel));
                    a2.addTarget(new TimingTargetAdapter() {

                        public void end() {
                            if (panel instanceof DeskletRenderPanel) {
                                ((DeskletRenderPanel) panel).setAnimating(false);
                            }
                        }
                    });
                    a2.start();
                }

                public void repeat() {
                }

                public void timingEvent(float f) {
                    calc(f, peer, bouncer, center);
                    wm.getRenderPanel().repaint();
                }
            });
            return anim;
        }
    }

    private void calc(float f, Buffered2DPeer peer, Equation bouncer, Point2D center) {
        peer.setScale(1.0 + 2.0 * bouncer.compute(f));
        peer.setLocation(new Point2D.Double(center.getX() - peer.getScale() * peer.getSize().getWidth() / 2, center.getY() - peer.getScale() * peer.getSize().getHeight() / 2));
    }
}
