package blue.ui.core.soundObject.renderer;

import blue.soundObject.External;

/**
 *
 * @author syi
 */
public class ExternalRenderer extends LetterRenderer {

    public ExternalRenderer() {
        super("E");
    }

    @Override
    public Class getSoundObjectClass() {
        return External.class;
    }
}
