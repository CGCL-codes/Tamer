package org.magiclabs.magickml.ui.converters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.magiclabs.magickml.domain.KMLDomain;
import org.magiclabs.magickml.domain.converters.IKMLConverter;

public class ConvertersView extends ViewPart {

    public static final String ID = "magickml.converters.view";

    @Override
    public void createPartControl(Composite parent) {
        for (IKMLConverter converter : KMLDomain.instance.listConverters()) {
            createButton(parent, converter);
        }
    }

    private void createButton(Composite parent, final IKMLConverter converter) {
        Button button = new Button(parent, SWT.NORMAL);
        {
            button.addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent arg0) {
                    produce(converter);
                }

                public void widgetDefaultSelected(SelectionEvent arg0) {
                    produce(converter);
                }
            });
            button.setText(converter.getName());
        }
    }

    private void produce(IKMLConverter converter) {
        converter.convert(KMLDomain.instance.provider().provideKML());
    }

    @Override
    public void setFocus() {
    }
}
