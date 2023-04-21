package net.sourceforge.mandalajar;

import javax.swing.event.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * @author  Lukas C. Faulstich
 */
public class MandalaComponent extends ModelElement implements Serializable {

    protected transient ChangeEvent thisChanged = new ChangeEvent(this);

    protected transient MandalaModel model;

    /**
	 * Radius of the inner rim as fraction of the maximum radius allowed by the
	 * canvas.
	 */
    protected transient float innerRadius = 0.0f;

    protected transient MandalaComponent next;

    protected transient MandalaComponent previous;

    protected transient int rank = -1;

    /**
	 * Radius of the outer rim as fraction of the maximum radius allowed by the
	 * canvas
	 */
    protected float radius = 0.0f;

    /**
	 * Order of rotation symmetry
	 */
    protected int symmetry = 6;

    /**
         * The component is turned by the angle of phase *  2pi/symmetry.
	 * Allowed values are in the interval [-1,1].
         */
    protected float phase = 0.0f;

    /**
         * The component can be rotationally distorted by turning the inner
         * rim with respect to the outer rim at the angle of twist * 2pi/symmetry
         *  
         */
    protected float twist = 0.0f;

    protected boolean circle = true;

    protected boolean wheel = true;

    protected boolean star = false;

    protected boolean wave = false;

    protected boolean rings = false;

    protected boolean orbits = false;

    protected boolean image = false;

    protected String imageFile;

    protected transient boolean focused = false;

    protected String title;

    public MandalaComponent() {
    }

    public MandalaComponent(MandalaModel model) {
        this.model = model;
        model.addComponent(this);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float newRadius) {
        if (newRadius < 0.0f) {
            newRadius = 0.0f;
        } else if (newRadius > 1.0f) {
            newRadius = 1.0f;
        }
        if (this.radius != newRadius) {
            this.radius = newRadius;
            setChanged();
            if (next != null) {
                next.setInnerRadius(newRadius);
            }
            if (this.innerRadius > newRadius) {
                setInnerRadius(newRadius);
            }
        }
    }

    protected float getInnerRadius() {
        return innerRadius;
    }

    protected void setInnerRadius(float newRadius) {
        if (newRadius < 0.0f) {
            newRadius = 0.0f;
        } else if (newRadius > 1.0f) {
            newRadius = 1.0f;
        }
        if (newRadius != this.innerRadius) {
            this.innerRadius = newRadius;
            setChanged();
            if (previous != null) {
                previous.setRadius(newRadius);
            }
            if (newRadius > this.radius) {
                setRadius(newRadius);
            }
        }
    }

    public int getSymmetry() {
        return symmetry;
    }

    public void setSymmetry(int symmetry) {
        this.symmetry = symmetry;
        setChanged();
    }

    public void setModel(MandalaModel model) {
        if (this.model != null) {
            removeChangeListener(model);
        }
        this.model = model;
        if (model != null) {
            addChangeListener(model);
        }
    }

    public float getPhase() {
        return phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
        setChanged();
    }

    public float getTwist() {
        return twist;
    }

    public void setTwist(float twist) {
        this.twist = twist;
        setChanged();
    }

    public int getRank() {
        return rank;
    }

    public String getTitle() {
        return title;
    }

    public boolean isWheel() {
        return wheel;
    }

    public void setWheel(boolean rays) {
        this.wheel = rays;
        setChanged();
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
        setChanged();
    }

    public boolean isWave() {
        return wave;
    }

    public void setWave(boolean wave) {
        this.wave = wave;
        setChanged();
    }

    public boolean isCircle() {
        return circle;
    }

    public void setCircle(boolean circle) {
        this.circle = circle;
        setChanged();
    }

    public boolean isRings() {
        return rings;
    }

    public void setRings(boolean rings) {
        this.rings = rings;
        setChanged();
    }

    public boolean isOrbits() {
        return orbits;
    }

    public void setOrbits(boolean orbits) {
        this.orbits = orbits;
        setChanged();
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{" + "\n\t\ttitle = " + getTitle() + ",\n\t\trank = " + getRank() + ",\n\t\tenabled = " + isEnabled() + ",\n\t\tradius = " + getRadius() + ",\n\t\tinnerRadius = " + getInnerRadius() + ",\n\t\tsymmetry = " + getSymmetry() + ",\n\t\tphase = " + getPhase() + ",\n\t\ttwist = " + getTwist() + ",\n\t\tcircle = " + isCircle() + ",\n\t\twheel = " + isWheel() + ",\n\t\tstar = " + isStar() + ",\n\t\twave = " + isWave() + ",\n\t\trings = " + isRings() + ",\n\t\torbits = " + isOrbits() + ",\n\t\timage = " + isImage() + ",\n\t\timageFile = " + getImageFile() + "\n\t}";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isEnabled() {
        return radius > innerRadius;
    }

    public MandalaComponent getNext() {
        return next;
    }

    public void setNext(MandalaComponent next) {
        this.next = next;
    }

    public MandalaComponent getPrevious() {
        return previous;
    }

    public void setPrevious(MandalaComponent previous) {
        this.previous = previous;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
        setChanged();
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
        setChanged();
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
