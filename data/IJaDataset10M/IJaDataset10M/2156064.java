package org.libreplan.web.orders;

import java.util.List;
import java.util.Set;
import org.libreplan.business.labels.entities.Label;
import org.libreplan.business.orders.entities.OrderElement;
import org.zkoss.ganttz.IPredicate;

/**
 * Checks if {@link Label} from {@link OrderElement} matches attribute label
 *
 * @author Diego Pino Garcia <dpino@igalia.com>
 *
 */
public class LabelOrderElementPredicate implements IPredicate {

    private Label label;

    public LabelOrderElementPredicate(Label label) {
        this.label = label;
    }

    @Override
    public boolean accepts(Object object) {
        final OrderElement orderElement = (OrderElement) object;
        return accepts(orderElement.getParent()) || accepts(orderElement) || accepts(orderElement.getChildren());
    }

    /**
     * Returns true if at least there's one {@link Label} in
     * {@link OrderElement} that holds predicate or orderElement is a new object
     *
     * @param orderElement
     * @return
     */
    private boolean accepts(OrderElement orderElement) {
        if (orderElement == null) {
            return false;
        }
        if (orderElement.isNewObject()) {
            return true;
        }
        final Set<Label> labels = orderElement.getLabels();
        if (label.getName().isEmpty() && labels.isEmpty()) {
            return true;
        }
        for (Label each : labels) {
            if (label != null && equalsLabel(each)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if there's at least on element in orderElements which any of
     * its labels holds predicate
     *
     * @param orderElements
     * @return
     */
    private boolean accepts(List<OrderElement> orderElements) {
        boolean result = false;
        for (OrderElement orderElement : orderElements) {
            result |= accepts(orderElement);
        }
        return result;
    }

    private boolean equalsLabel(Label label) {
        return (this.label.getName().equals(label.getName()) && this.label.getType().getName().equals(label.getType().getName()));
    }
}
