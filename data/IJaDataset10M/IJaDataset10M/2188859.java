package org.goniolab.module;

import org.almondframework.l10n.ALexicon;
import org.almondframework.ui.AApplication;
import org.goniolab.GonioLab;

/**
 *
 * @author Patrik Karlsson
 */
public class GonioLabModule {

    public AApplication getAApplication() {
        return GonioLab.getApplication();
    }

    protected ALexicon lexicon = GonioLab.getLexicon();
}
