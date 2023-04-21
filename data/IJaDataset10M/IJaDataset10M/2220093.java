package org.nakedobjects.viewer.skylark.special;

import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.core.AbstractCompositeViewSpecification;

public class ScheduleSpecification extends AbstractCompositeViewSpecification {

    ScheduleSpecification() {
        builder = new ScheduleLayout(new CollectionElementBuilder(new ScheduleBlockSubviews(), true));
    }

    public String getName() {
        return "Schedule";
    }

    private static class ScheduleBlockSubviews implements SubviewSpec {

        private ScheduleBlockSpecification blockSpecification = new ScheduleBlockSpecification();

        public View createSubview(Content content, ViewAxis axis) {
            return blockSpecification.createView(content, axis);
        }

        public View decorateSubview(View view) {
            return view;
        }
    }
}
