package org.nakedobjects.plugins.dnd.form;

import org.nakedobjects.plugins.dnd.view.Axes;
import org.nakedobjects.plugins.dnd.view.View;
import org.nakedobjects.plugins.dnd.view.ViewFactory;
import org.nakedobjects.plugins.dnd.view.ViewRequirement;
import org.nakedobjects.plugins.dnd.view.border.BackgroundBorder;
import org.nakedobjects.plugins.dnd.view.border.EmptyBorder;
import org.nakedobjects.plugins.dnd.view.border.IconBorder;
import org.nakedobjects.plugins.dnd.view.border.LineBorder;
import org.nakedobjects.plugins.dnd.view.composite.CompositeViewDecorator;

public class SummaryFormSpecification extends AbstractFormSpecification {

    public boolean canDisplay(ViewRequirement requirement) {
        return requirement.isObject() && !requirement.isTextParseable() && requirement.hasReference() && requirement.isOpen() && requirement.isSubview() && requirement.isFixed();
    }

    @Override
    protected void init() {
        addViewDecorator(new IconBorder.Factory());
        addViewDecorator(new CompositeViewDecorator() {

            public View decorate(View view, Axes axes) {
                return new EmptyBorder(3, new BackgroundBorder(new LineBorder(1, 8, new EmptyBorder(3, view))));
            }
        });
    }

    @Override
    protected ViewFactory createFieldFactory() {
        return new SummaryFields();
    }

    public String getName() {
        return "Summary";
    }
}
