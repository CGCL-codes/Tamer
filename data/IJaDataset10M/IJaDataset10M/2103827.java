package net.sf.brico.cmd.impl;

import java.util.Map;
import net.sf.brico.cmd.Command;
import net.sf.brico.cmd.CommandFactory;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class MapBackedCommandFactory implements CommandFactory {

    private Map commandMap;

    public MapBackedCommandFactory(Map commandMap) {
        this.commandMap = commandMap;
    }

    public Command createCommand(String name) {
        return (Command) commandMap.get(name);
    }
}
