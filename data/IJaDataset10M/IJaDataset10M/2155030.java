package de.wadndadn.midiclipse.ui.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import de.wadndadn.midiclipse.ui.MidiclipseUiPlugin;

/**
 * TODO Document.
 * 
 * @author SchubertCh
 */
public final class MidiclipseConsoleFactory implements IConsoleFactory {

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.console.IConsoleFactory#openConsole()
     */
    public void openConsole() {
        showConsole();
    }

    /**
     * TODO Document.
     */
    public static void showConsole() {
        MidiclipseConsole console = MidiclipseUiPlugin.getPlugin().getConsole();
        if (console != null) {
            IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
            IConsole[] existing = manager.getConsoles();
            boolean exists = false;
            for (int i = 0; i < existing.length; ++i) {
                if (console == existing[i]) {
                    exists = true;
                }
            }
            if (!exists) {
                manager.addConsoles(new IConsole[] { console });
            }
            manager.showConsoleView(console);
        }
    }

    /**
     * TODO Document.
     */
    public static void closeConsole() {
        IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
        MidiclipseConsole console = MidiclipseUiPlugin.getPlugin().getConsole();
        if (console != null) {
            manager.removeConsoles(new IConsole[] { console });
            ConsolePlugin.getDefault().getConsoleManager().addConsoleListener(console.new MyLifecycle());
        }
    }
}
