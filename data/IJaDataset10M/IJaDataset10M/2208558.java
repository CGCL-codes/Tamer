package org.gems.designer.figures;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.gems.designer.GraphicsProvider;
import org.gems.designer.ModelRepository;
import org.gems.designer.edit.GemsFigure;
import org.gems.designer.edit.GraphicalPartLookup;
import org.gems.designer.model.Atom;
import org.gems.designer.model.Container;
import org.gems.designer.model.Model;
import org.gems.designer.model.ModelObject;
import org.gems.designer.model.Wire;
import org.gems.designer.model.meta.BaseTypes;
import org.gems.designer.model.meta.MetaType;
import org.gems.designer.model.meta.Relationship;

public class ModelExpandablePanel extends FFExpandablePanel {

    private List<ConnectionAnchor> originalSourceAnchors_ = new ArrayList<ConnectionAnchor>(20);

    private List<ConnectionAnchor> originalTargetAnchors_ = new ArrayList<ConnectionAnchor>(20);

    private List<Connection> hiddenConnections_ = new ArrayList<Connection>(20);

    private List<Connection> reSourcedConnections_ = new ArrayList<Connection>(20);

    private List<Connection> reTargetedConnections_ = new ArrayList<Connection>(20);

    private Label imageLabel_;

    public ModelExpandablePanel(Model model) {
        super(model);
        GraphicsProvider p = ModelRepository.getInstance().getModelProvider(model.getModelID()).getGraphicsProvider();
        Hashtable hints = (Hashtable) p.getDrawingHint(model);
        Image image = null;
        if (hints != null) image = (Image) StyleUtilities.getImage(hints, CSSFigure.CONTENT);
        if (image == null) image = p.getLargeIcon(model);
        if (image != null) {
            Label l = new Label(image);
            imageLabel_ = l;
            l.setFont(Display.getDefault().getSystemFont());
            setContentSwapPanel(l);
        }
    }

    @Override
    public void init() {
        expandable_ = containsOthers(getModel().getMetaType());
        expanded_ = ((Model) getModel()).isExpanded() && expandable_;
        super.init();
    }

    @Override
    public void postUpdateExpansion() {
        super.postUpdateExpansion();
        if (((Model) getModel()).isExpanded() != expanded_) ((Model) getModel()).setExpanded(expanded_);
    }

    @Override
    public void preUpdateExpansion() {
        super.preUpdateExpansion();
        GemsFigure fig = (GemsFigure) GraphicalPartLookup.getFigure(getModel());
        if (fig != null) {
            if (!expanded_) {
                List<Connection> allcons = collectDescendantConnections((Container) getModel(), new ArrayList<Connection>(30));
                ConnectionAnchor anchor = fig.connectionAnchorAt(new Point(0, 0));
                for (Connection c : allcons) updateConnectionForClosure(anchor, c);
            } else {
                for (Connection c : hiddenConnections_) c.setVisible(true);
                for (int i = 0; i < reSourcedConnections_.size(); i++) {
                    Connection con = reSourcedConnections_.get(i);
                    con.setSourceAnchor(originalSourceAnchors_.get(i));
                }
                for (int i = 0; i < reTargetedConnections_.size(); i++) {
                    Connection con = reTargetedConnections_.get(i);
                    con.setTargetAnchor(originalTargetAnchors_.get(i));
                }
            }
            refreshPart(getModel());
        }
    }

    @Override
    public void updateContents() {
        super.updateContents();
    }

    public static boolean containsOthers(MetaType t) {
        if (t == null) return false;
        List<Relationship> rels = t.getRelationships();
        for (Relationship r : rels) {
            if (BaseTypes.CONT_RELATION_TYPE.isAssignableFrom(r.getRelationshipType()) && r.getSource() == t) return true;
        }
        return containsOthers(t.getParent());
    }

    public List<Connection> collectDescendantConnections(Container c, List<Connection> cons) {
        for (Object child : c.getChildren()) {
            ModelObject mo = (ModelObject) child;
            if (child instanceof Container) {
                collectDescendantConnections((Container) child, cons);
            }
            for (Object w : mo.getConnections()) {
                Wire wire = (Wire) w;
                Connection con = (Connection) GraphicalPartLookup.getFigure(wire);
                if (con != null) cons.add(con);
            }
        }
        return cons;
    }

    public void updateConnectionForClosure(ConnectionAnchor anchor, Connection con) {
        IFigure src = con.getSourceAnchor().getOwner();
        IFigure trg = con.getTargetAnchor().getOwner();
        if (FigureUtilities.isAncestor(this, src)) {
            if (FigureUtilities.isAncestor(this, trg)) {
                if (con.isVisible()) {
                    hiddenConnections_.add(con);
                    con.setVisible(false);
                }
            } else {
                reSourcedConnections_.add(con);
                originalSourceAnchors_.add(con.getSourceAnchor());
                con.setSourceAnchor(anchor);
            }
        } else {
            reTargetedConnections_.add(con);
            originalTargetAnchors_.add(con.getTargetAnchor());
            con.setTargetAnchor(anchor);
            refreshPart(con.getSourceAnchor().getOwner());
        }
    }

    public void setClosedImage(Image img) {
        if (imageLabel_ == null) {
            imageLabel_ = new Label(img);
            setContentSwapPanel(imageLabel_);
        } else {
            imageLabel_.setIcon(img);
        }
    }

    public Image getClosedImage() {
        if (imageLabel_ != null) return imageLabel_.getIcon(); else return null;
    }
}
