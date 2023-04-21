package java.org.lindenb.graffiti;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Layer implements Iterable<Figure> {

    private PropertyChangeSupport propertyChanges;

    private float alpha = 1f;

    private boolean visible = true;

    private Model model;

    private List<Figure> figures_ = new ArrayList<Figure>();

    public Layer(Model model) {
        this.propertyChanges = new PropertyChangeSupport(this);
        this.model = model;
    }

    public List<Figure> figures() {
        return this.figures_;
    }

    @Override
    public Iterator<Figure> iterator() {
        return figures().iterator();
    }

    public Model getModel() {
        return model;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean is_visible) {
        boolean oldValue = this.visible;
        this.visible = is_visible;
        this.propertyChanges.firePropertyChange("visible", oldValue, is_visible);
    }

    public float getOpacity() {
        return alpha;
    }

    public Composite getComposite() {
        return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getOpacity());
    }

    public void setOpacity(float alpha) {
        float oldValue = this.alpha;
        this.alpha = alpha;
        this.propertyChanges.firePropertyChange("alpha", oldValue, this.alpha);
    }
}
