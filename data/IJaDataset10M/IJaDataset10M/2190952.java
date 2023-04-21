package it.xtypes.example.lambda.ui.labeling;

import it.xtypes.example.lambda.lambda.Abstraction;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;
import com.google.inject.Inject;

/**
 * Provides labels for a EObjects.
 * 
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#labelProvider
 */
public class LambdaLabelProvider extends DefaultEObjectLabelProvider {

    @Inject
    public LambdaLabelProvider(AdapterFactoryLabelProvider delegate) {
        super(delegate);
    }

    String text(Abstraction abstraction) {
        return "lambda ";
    }
}
