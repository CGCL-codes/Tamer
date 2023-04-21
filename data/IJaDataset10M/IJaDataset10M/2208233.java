package org.sodbeans.tutorial;

import java.io.File;
import org.openide.modules.InstalledFileLocator;

/**
 * 
 *
 * @author Andreas Stefik, Kim Slattery
 */
public class TutorialLoader {

    public static final String TUTORIAL_ROOT = "tutorials";

    public static final String INDEX_LOCATION = File.separator + "tutorials.index";

    public static final String CODE_NAME_BASE = "org.sodbeans.tutorial";

    private File tutorialRoot = null;

    public TutorialLoader() {
        File file = InstalledFileLocator.getDefault().locate(TUTORIAL_ROOT, CODE_NAME_BASE, false);
        tutorialRoot = file;
    }

    public File loadIndex() {
        File index = new File(tutorialRoot.getAbsolutePath() + File.separator + "tutorials.index");
        return index;
    }

    public File loadTutorial(String local) {
        File tutorial = new File(tutorialRoot.getAbsolutePath() + File.separator + local);
        return tutorial;
    }
}
