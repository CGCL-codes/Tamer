package ar.com.kyol.jet.client.wrappers;

import com.google.gwt.user.client.ui.Label;

public class NullWrapper extends Wrapper {

    /**
	 * Instantiates a new null wrapper.
	 *
	 * @param useValueAsString the use value as string
	 */
    public NullWrapper(boolean useValueAsString) {
        super(useValueAsString);
        initWidget(new Label(""));
    }

    @Override
    protected String getValueAsString() {
        return "";
    }
}
