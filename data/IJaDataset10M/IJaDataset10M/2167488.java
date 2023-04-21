package visugraph.gview.animate;

import java.awt.geom.Point2D;
import visugraph.gview.GraphComponent;

/**
 * Permet de créer des animations sur la vue d'un composant.
 * Sont notamment supportés le zoom et le déplacement de la vue. Toutes les coordonnées sont comptées
 * dans la métrique du graphe. Cette animation peut détecter les modifications concourrantes et s'arrêter
 * si la vue est déplacée par l'utilisateur par exemple.
 */
public class ViewAnimator<N, E> extends AbstractAnimator {

    private GraphComponent<N, E> component;

    private double targetZoom, initZoom, lastZoom;

    private Point2D targetCorner, initCorner, lastCorner;

    private boolean detectModif;

    /**
	 * Créer une nouvelle animation. 
	 * @param component le composant dont la vue doit être modifiée.
	 * @param corner coordonnées des origines de la vue à atteindre
	 * @param zoom le facteur de zoom final demandé.
	 * @param duration durée totale de l'animation
	 */
    public ViewAnimator(GraphComponent<N, E> component, Point2D corner, double zoom, int duration) {
        super(duration);
        this.setInterpolator(AccelDecelInterpolator.INSTANCE);
        this.component = component;
        this.targetCorner = new Point2D.Double();
        this.targetCorner.setLocation(corner);
        this.targetZoom = zoom;
        this.detectModif = true;
    }

    public ViewAnimator(GraphComponent<N, E> component, Point2D corner, double zoom) {
        this(component, corner, zoom, 500);
    }

    public boolean isDetectConcurrentAccess() {
        return this.detectModif;
    }

    public void setDetectConcurrentAccess(boolean modif) {
        this.detectModif = modif;
    }

    public double getZoom() {
        return this.targetZoom;
    }

    public Point2D getCorner() {
        return this.targetCorner;
    }

    public void setZoom(double zoom) {
        this.targetZoom = zoom;
    }

    public void setCorner(Point2D corner) {
        this.targetCorner.setLocation(corner);
    }

    protected void processStep(double timePos, double animPos) {
        double curZoom = this.component.getMetric().getZoom();
        Point2D curCorner = this.component.getMetric().getCorner();
        if (this.detectModif && (curZoom != this.lastZoom || !curCorner.equals(this.lastCorner))) {
            this.stop();
        } else {
            double newZoom = this.initZoom + (this.targetZoom - this.initZoom) * animPos;
            double newCornerX = initCorner.getX() + (targetCorner.getX() - initCorner.getX()) * animPos;
            double newCornerY = initCorner.getY() + (targetCorner.getY() - initCorner.getY()) * animPos;
            this.component.getMetric().setZoom(newZoom);
            this.component.getMetric().setCorner(newCornerX, newCornerY);
            this.lastCorner.setLocation(newCornerX, newCornerY);
            this.lastZoom = newZoom;
        }
    }

    protected void animationStarted() {
        this.initCorner = new Point2D.Double();
        this.initCorner.setLocation(this.component.getMetric().getCorner());
        this.initZoom = this.component.getMetric().getZoom();
        this.lastZoom = this.initZoom;
        this.lastCorner = new Point2D.Double();
        this.lastCorner.setLocation(this.initCorner);
        this.component.disableQuality();
    }

    protected void animationEnded() {
        if (!this.isStopAsked()) {
            this.component.getMetric().setZoom(this.targetZoom);
            this.component.getMetric().setCorner(this.targetCorner);
        }
        this.component.restoreQuality();
    }
}
