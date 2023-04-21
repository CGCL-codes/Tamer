package humandiagramgef;

import humanbodymodel.Human;
import humanbodymodel.HumanLink;
import java.util.List;
import java.util.Random;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class HumanDiagramEditPart extends AbstractGraphicalEditPart {

    public HumanDiagramEditPart() {
        super();
    }

    @Override
    protected IFigure createFigure() {
        return new HumanPolylineConnectionAnchorFigure();
    }

    @Override
    protected void createEditPolicies() {
    }

    @Override
    protected List<HumanLink> getModelSourceConnections() {
        Human model = (Human) getModel();
        return model.getOutgoingLinks();
    }

    @Override
    protected List<HumanLink> getModelTargetConnections() {
        Human model = (Human) getModel();
        return model.getIncomingLinks();
    }

    @Override
    protected void refreshVisuals() {
        final HumanPolylineConnectionAnchorFigure figure = (HumanPolylineConnectionAnchorFigure) getFigure();
        Human model = (Human) getModel();
        final HumanContainerEditPart parent = (HumanContainerEditPart) this.getParent();
        figure.getLable().setText(model.getName());
        figure.setForegroundColor(new Color(Display.getCurrent(), model.getColor_r(), model.getColor_g(), model.getColor_b()));
        Rectangle layout = new Rectangle((int) model.getX(), (int) model.getY(), 40, 40);
        parent.setLayoutConstraint(this, figure, layout);
    }

    @Override
    public void setModel(Object model) {
        super.setModel(model);
        ((Human) model).eAdapters().add(new Adapter() {

            @Override
            public void notifyChanged(Notification notification) {
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        refreshVisuals();
                        refreshSourceConnections();
                        refreshTargetConnections();
                    }
                });
            }

            @Override
            public Notifier getTarget() {
                return (Human) getModel();
            }

            @Override
            public void setTarget(Notifier newTarget) {
            }

            @Override
            public boolean isAdapterForType(Object type) {
                return false;
            }
        });
    }

    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return ((HumanPolylineConnectionAnchorFigure) getFigure()).getConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return ((HumanPolylineConnectionAnchorFigure) getFigure()).getConnectionAnchor();
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return ((HumanPolylineConnectionAnchorFigure) getFigure()).getConnectionAnchor();
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return ((HumanPolylineConnectionAnchorFigure) getFigure()).getConnectionAnchor();
    }
}
