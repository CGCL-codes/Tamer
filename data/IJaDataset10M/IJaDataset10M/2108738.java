package org.fudaa.ebli.visuallibrary.creator;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import org.fudaa.ebli.visuallibrary.EbliNode;
import org.fudaa.ebli.visuallibrary.EbliNodeDefault;
import org.fudaa.ebli.visuallibrary.EbliScene;
import org.fudaa.ebli.visuallibrary.EbliWidget;

/**
 * Classe responsable du cycle de vie de la widget.
 */
public abstract class EbliWidgetCreator {

    EbliWidget widget;

    protected EbliWidgetCreator owner;

    private Point preferedLocation;

    private Dimension preferredSize;

    private Map propGraphique;

    public EbliWidgetCreator() {
    }

    /**
   * A appeler pour mettre a jour les listeners.<br>
   * Appele lorsque la widget est ajoute a la scene.
   */
    public void widgetAttached() {
        if (owner != null) {
            owner.getWidget().addSatellite(getWidget());
        }
    }

    /**
   * A appeler pour mettre a jour les listeners.<br>
   * Appele lorsque la widget est enleve de la scene.
   */
    public void widgetDetached() {
        if (owner != null) {
            owner.getWidget().removeSatellite(getWidget());
        }
    }

    /**
   * @param copy utilise pour reprendre la location et la preferredSize
   */
    public EbliWidgetCreator(EbliWidgetCreator copy) {
        if (copy != null) {
            preferedLocation = clonePoint(copy.getCurrentPreferredLocation());
            preferredSize = cloneDimension(copy.getCurrentPreferredSize());
            if (copy.isWidgetCreated()) {
                propGraphique = copy.getWidget().duplicateGraphicalProperties();
            }
        }
    }

    public static Point clonePoint(Point init) {
        if (init == null) return null;
        return new Point(init);
    }

    public static Dimension cloneDimension(Dimension init) {
        if (init == null) return null;
        return new Dimension(init);
    }

    /**
   * @return the widget
   */
    public final EbliWidget getWidget() {
        return widget;
    }

    /**
   * @return true si widget cr��e.
   */
    public final boolean isWidgetCreated() {
        return widget != null;
    }

    public final void setPreferredLocation(final Point p) {
        this.preferedLocation = p;
        if (isWidgetCreated()) getWidget().setPreferredLocation(p);
    }

    public final void setPreferredSize(final Dimension d) {
        this.preferredSize = d;
        if (isWidgetCreated()) getWidget().setPreferredSize(d);
    }

    public final Point getInitPreferredLocation() {
        return preferedLocation;
    }

    public final Point getCurrentPreferredLocation() {
        if (isWidgetCreated()) {
            Point p = getWidget().getLocation();
            if (p != null) {
                return p;
            }
            if (getWidget().isPreferredBoundsSet()) {
                Rectangle preferredBounds = getWidget().getPreferredBounds();
                if (preferredBounds != null) {
                    getWidget().convertLocalToScene(preferredBounds.getLocation());
                }
            }
        }
        return getInitPreferredLocation();
    }

    public final Dimension getCurrentPreferredSize() {
        if (isWidgetCreated()) {
            if (getWidget().isPreferredBoundsSet()) {
                Rectangle preferredBounds = getWidget().getPreferredBounds();
                if (preferredBounds != null) {
                    return new Dimension(preferredBounds.width, preferredBounds.height);
                }
            }
        }
        return getInitPreferredSize();
    }

    private final Dimension getInitPreferredSize() {
        return preferredSize;
    }

    public EbliWidget create(EbliScene _scene) {
        if (getWidget() != null) return getWidget();
        widget = createWidget(_scene);
        if (widget == null) return null;
        widget.setPreferredLocation(getInitPreferredLocation());
        if (getInitPreferredSize() != null) widget.setPreferredSize(getInitPreferredSize());
        if (propGraphique != null) {
            widget.setPropGraphique(propGraphique);
        }
        if (owner != null) owner.create(_scene).addSatellite(widget);
        postCreateWidget();
        return widget;
    }

    protected void postCreateWidget() {
    }

    /**
   * Cette action ne doit etre appelee que par la EbliScene pour attacher le widget a la scene
   * 
   * @param _scene la scene contenant
   * @return la widget creee
   */
    protected abstract EbliWidget createWidget(EbliScene _scene);

    /**
   * Duplication du creator
   * 
   * @param satteliteOwnerDuplicated si le creator est un satellite, le creator de son owner est pass� en param�tre.
   * @return le creator
   */
    public abstract EbliWidgetCreator duplicate(EbliWidgetCreator satteliteOwnerDuplicated);

    public final List<EbliNode> duplicate(EbliNode nodeAdupliquer, EbliWidgetCreator satteliteOwnerDuplicated) {
        final EbliNode duplique = new EbliNodeDefault();
        EbliWidgetCreator creatorDuplicated = duplicate(satteliteOwnerDuplicated);
        duplique.setCreator(creatorDuplicated);
        duplique.setTitle(nodeAdupliquer.getTitle());
        duplique.setDescription(nodeAdupliquer.getDescription());
        creatorDuplicated.owner = satteliteOwnerDuplicated;
        EbliWidget w = getWidget();
        List<EbliNode> res = new ArrayList<EbliNode>();
        res.add(duplique);
        if (w.hasSattelite()) {
            Set<EbliWidget> satellites = w.getSatellites();
            EbliScene sc = widget.getEbliScene();
            for (EbliWidget ebliWidget : satellites) {
                EbliNode node = (EbliNode) sc.findObject(ebliWidget);
                res.addAll(node.getCreator().duplicate(node, creatorDuplicated));
            }
        }
        return res;
    }

    /**
   * @param nodeAdupliquer
   * @param satteliteOwnerDuplicated
   * @return liste les noeuds dupliques
   */
    public final List<EbliNode> duplicateInSameScene(EbliNode nodeAdupliquer, EbliWidgetCreator satteliteOwnerDuplicated) {
        List<EbliNode> res = duplicate(nodeAdupliquer, satteliteOwnerDuplicated);
        Point oldLocation = getWidget().getLocation();
        int deltaY = getWidget().getClientArea().height;
        final Point nouvellePosition = new Point(oldLocation.x, (oldLocation.y + deltaY));
        res.get(0).getCreator().setPreferredLocation(nouvellePosition);
        int size = res.size();
        for (int i = 1; i < size; i++) {
            EbliWidgetCreator creator = res.get(i).getCreator();
            Point satellitePreferredLocation = creator.getInitPreferredLocation();
            satellitePreferredLocation.y += deltaY;
            creator.setPreferredLocation(satellitePreferredLocation);
        }
        JComponent view = getWidget().getScene().getView();
        for (int i = 0; i < size; i++) {
            EbliWidgetCreator creator = res.get(i).getCreator();
            int idx = 1;
            Integer clientProperty = (Integer) view.getClientProperty(creator.getClass().toString() + ".idx");
            if (clientProperty != null) {
                idx = clientProperty.intValue();
            }
            res.get(i).setTitle(res.get(i).getTitle() + " " + idx);
            view.putClientProperty(creator.getClass().toString() + ".idx", Integer.valueOf(idx + 1));
        }
        return res;
    }

    /**
   * Methode qui permet d'injecter les datas dans le creator pour la persistence
   * 
   * @return true si les donnees de persistences ont pu �tre charg�es.
   */
    public abstract boolean loadPersistData(Object data, Map parameters);

    /**
   * Methode qui permet de recuperer les datas specifiques du creator
   */
    public abstract Object savePersistData(Map parameters);

    /**
   * method called after the widget has been added to the view
   */
    public void widgetAddedInView() {
    }
}
