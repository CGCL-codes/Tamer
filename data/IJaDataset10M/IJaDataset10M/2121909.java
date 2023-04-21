package de.iph.arbeitsgruppenassistent.client.taskmanagement;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import de.iph.arbeitsgruppenassistent.client.core.CorePlugin;
import de.iph.arbeitsgruppenassistent.client.core.Tool;

/**
 * This class will be called from the {@link CorePlugin} and initiates 
 * {@link TaskManagement}.
 *   
 * @author Andreas Bruns
 */
public final class TaskTool implements Tool {

    public void init(final JComponent rootContainer) {
        rootContainer.setLayout(new BorderLayout());
        rootContainer.add(new TaskManagement(), BorderLayout.CENTER);
    }
}
