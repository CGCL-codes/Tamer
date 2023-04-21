package org.nakedobjects.nos.client.dnd.special;

import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.ObjectContent;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewAxis;
import org.nakedobjects.nos.client.dnd.ViewSpecification;
import org.nakedobjects.nos.client.dnd.core.AbstractCompositeViewSpecification;
import org.nakedobjects.nos.client.dnd.value.PercentageBarField;

class BarSpecification extends AbstractCompositeViewSpecification {

    public BarSpecification() {
        builder = new StackLayout(new ObjectFieldBuilder(new DataFormSubviews()));
    }

    private static class DataFormSubviews implements SubviewSpec {

        public View createSubview(Content content, ViewAxis axis) {
            if (content instanceof ObjectContent && ((ObjectContent) content).getObject() instanceof Percentage) {
                ViewSpecification specification = new PercentageBarField.Specification();
                return specification.createView(content, axis);
            }
            return null;
        }

        public View decorateSubview(View view) {
            return view;
        }
    }

    public String getName() {
        return "Data Form";
    }
}
