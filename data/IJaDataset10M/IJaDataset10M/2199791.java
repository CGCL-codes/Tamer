package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;
import at.rc.tacos.platform.model.Disease;

public class DiseaseLabelProvider extends LabelProvider {

    /**
	 * Returns the name of the disease to render
	 */
    @Override
    public String getText(Object object) {
        Disease disease = (Disease) object;
        return disease.getDiseaseName();
    }
}
