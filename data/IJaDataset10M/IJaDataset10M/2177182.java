package blue.plaf;

import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.netbeans.swing.tabcontrol.plaf.*;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    Logger logger = Logger.getLogger("blue.plaf.Installer");

    @Override
    public void restored() {
        try {
            LookAndFeel plaf = new blue.plaf.BlueLookAndFeel();
            UIManager.setLookAndFeel(plaf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UIManager.put(DefaultTabbedContainerUI.KEY_EDITOR_CONTENT_BORDER, BorderFactory.createEmptyBorder());
        UIManager.put("EditorTabDisplayerUI", "blue.plaf.BlueEditorTabDisplayerUI");
        UIManager.getDefaults().put("ViewTabDisplayerUI", "blue.plaf.BlueViewTabDisplayerUI");
        UIManager.put("TabbedContainer.view.contentBorder", new BlueViewBorder(UIManager.getColor("SplitPane.highlight"), UIManager.getColor("SplitPane.darkShadow")));
        logger.info("Finished blue PLAF installation");
    }
}
