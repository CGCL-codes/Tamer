package zaphod.toy.gef.japanexample.editparts;

import java.util.List;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import zaphod.toy.gef.japanexample.editpolicies.MyXYLayoutEditPolicy;
import zaphod.toy.gef.japanexample.model.ContentsModel;

public class ContentsEditPart extends AbstractGraphicalEditPart {

    @Override
    protected IFigure createFigure() {
        Layer figure = new Layer();
        figure.setLayoutManager(new XYLayout());
        return figure;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new MyXYLayoutEditPolicy());
    }

    @Override
    protected List getModelChildren() {
        ContentsModel model = getCastedModel();
        return model.getChildren();
    }

    private ContentsModel getCastedModel() {
        Object model = getModel();
        if (model instanceof ContentsModel) {
            return ((ContentsModel) model);
        }
        throw new RuntimeException("No coressponding Model : " + model.getClass().getName());
    }
}
