package net.stevechaloner.intellijad.console;

import com.intellij.openapi.project.Project;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Manages consoles on a per-project basis.
 */
public class ConsoleManager {

    /**
     * The consoles.
     */
    private final Map<Project, IntelliJadConsole> consoles = new HashMap<Project, IntelliJadConsole>();

    /**
     * Creates and binds a console to the given project.
     *
     * @param project the project to bind the console to
     * @return the new console
     */
    @NotNull
    private IntelliJadConsole addConsole(@NotNull Project project) {
        IntelliJadConsole console = new IntelliJadConsole(project);
        consoles.put(project, console);
        return console;
    }

    /**
     * Gets the console for the project.  If one doesn't exist, it is created
     * and bound.
     *
     * @param project the project whose console is required
     * @return the console
     */
    @NotNull
    public IntelliJadConsole getConsole(@NotNull Project project) {
        IntelliJadConsole console = consoles.get(project);
        if (console == null) {
            console = addConsole(project);
        }
        return console;
    }

    /**
     * Removes the console from the manager, and disposes of it.
     *
     * @param project the project whose console should be removed
     */
    public void disposeConsole(@NotNull Project project) {
        synchronized (consoles) {
            IntelliJadConsole console = consoles.get(project);
            if (console != null) {
                console.disposeConsole();
            }
            consoles.remove(project);
        }
    }
}
