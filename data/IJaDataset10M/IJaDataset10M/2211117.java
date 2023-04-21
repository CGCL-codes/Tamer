package de.abg.jreichert.serviceqos.ui.labeling;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;
import com.google.inject.Inject;

/**
 * Provides labels for a EObjects.
 * 
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#labelProvider
 */
public class MeasurementLabelProvider extends DefaultEObjectLabelProvider {

    @Inject
    public MeasurementLabelProvider(AdapterFactoryLabelProvider delegate) {
        super(delegate);
    }
}
