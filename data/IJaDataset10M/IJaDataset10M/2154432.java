package com.open_squad.openplan.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import com.open_squad.openplan.editpolicies.AppEditLayoutPolicy;
import com.open_squad.openplan.editpolicies.LotComponentEditPolicy;
import com.open_squad.openplan.figure.LotFigure;
import com.open_squad.openplan.model.DetailPart;
import com.open_squad.openplan.model.Lot;
import com.open_squad.openplan.model.Node;

public class LotEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

    @Override
    protected IFigure createFigure() {
        IFigure figure = new LotFigure();
        return figure;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new LotComponentEditPolicy());
    }

    protected static final Integer LOTEDGE = 50;

    protected void refreshVisuals() {
        LotFigure figure = (LotFigure) getFigure();
        int x, y, width, height;
        Date toDay = new Date();
        Calendar min = new GregorianCalendar();
        min.setTime(toDay);
        int day = min.get(Calendar.DAY_OF_MONTH);
        Calendar cal = new GregorianCalendar();
        cal.setTime(getCastedModel().getEstStartDate());
        int start = cal.get(Calendar.HOUR_OF_DAY);
        cal.setTime(getCastedModel().getEstEndDate());
        int end = cal.get(Calendar.HOUR_OF_DAY);
        x = LOTEDGE * start;
        y = 0;
        width = LOTEDGE * (end - start);
        height = LOTEDGE;
        figure.setLayout(new Rectangle(x, y, width, height));
        Iterator iterator = getCastedModel().getDetailParts().iterator();
        String tooltipText = "";
        DetailPart part = null;
        while (iterator.hasNext()) {
            part = (DetailPart) iterator.next();
            tooltipText += "Commande : " + part.getOrderDetail().getOrder().getIdOrder() + "\n";
            tooltipText += "Client : " + part.getOrderDetail().getOrder().getClient().getName() + "\n";
            tooltipText += "Quantit� : " + part.getOrderDetail().getQuantity() + "\n";
            if (iterator.hasNext()) tooltipText += "--------------------\n";
        }
        tooltipText = "Couleur : " + part.getOrderDetail().getColor().getName() + "\n\n" + tooltipText;
        tooltipText = "Tissu : " + part.getOrderDetail().getMaterial().getCode() + "\n" + tooltipText;
        figure.setToolTip(new Label(tooltipText));
    }

    Lot getCastedModel() {
        return (Lot) getModel();
    }

    public void activate() {
        super.activate();
        ((Node) getModel()).addPropertyChangeListener(this);
    }

    public void deactivate() {
        super.deactivate();
        ((Node) getModel()).removePropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Lot.PROPERTY_SCHEDULE_CHANGED)) refreshVisuals();
    }
}
