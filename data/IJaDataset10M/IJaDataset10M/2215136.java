package joodin.impl.widgets.internal;

import joodin.impl.util.BorderConvert;
import joodin.impl.util.SplitOrientationConvert;
import joodin.impl.widgets.VaadinContainer;
import joodin.impl.widgets.VaadinControl;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.ISplitCompositeSpi;
import org.jowidgets.spi.widgets.setup.ISplitCompositeSetupSpi;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;

public class SplitCompositeImpl extends VaadinControl implements ISplitCompositeSpi {

    private final IContainerSpi first;

    private final IContainerSpi second;

    private double weight;

    private Component parent;

    private IGenericWidgetFactory factory;

    public SplitCompositeImpl(final Object parentUiReference, final IGenericWidgetFactory factory, final ISplitCompositeSetupSpi setup) {
        super(SplitOrientationConvert.convert(setup.getOrientation()));
        this.factory = factory;
        getUiReference().setMargin(false);
        this.weight = setup.getWeight();
        this.parent = (Component) parentUiReference;
        ((Panel) parentUiReference).addComponent(getUiReference());
        getUiReference().setMargin(false);
        getUiReference().setSplitPosition((int) (setup.getWeight() * (float) 100), Sizeable.UNITS_PERCENTAGE);
        final Panel content1 = new Panel();
        content1.setContent(new AbsoluteLayout());
        content1.setSizeFull();
        final Panel content2 = new Panel();
        content2.setSizeFull();
        content2.setContent(new AbsoluteLayout());
        content1.addStyleName(BorderConvert.convert(setup.getFirstBorder()));
        content2.addStyleName(BorderConvert.convert(setup.getSecondBorder()));
        first = new VaadinContainer(factory, content1);
        second = new VaadinContainer(factory, content2);
        first.setLayout(setup.getFirstLayout());
        second.setLayout(setup.getSecondLayout());
        getUiReference().setFirstComponent(content1);
        getUiReference().setSecondComponent(content2);
    }

    @Override
    public IContainerSpi getFirst() {
        return first;
    }

    @Override
    public IContainerSpi getSecond() {
        return second;
    }

    @Override
    public AbstractSplitPanel getUiReference() {
        return (AbstractSplitPanel) super.getUiReference();
    }
}
