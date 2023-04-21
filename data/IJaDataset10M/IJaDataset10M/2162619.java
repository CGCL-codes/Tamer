package com.rugl.console.commands;

import java.util.LinkedList;
import java.util.List;
import com.rugl.GameBox;
import com.rugl.console.Command;
import com.rugl.console.Console;
import com.ryanm.config.Configurator;
import com.ryanm.config.imp.ConfGet;
import com.ryanm.util.text.TextUtils;

/**
 * Alters a numerical variable
 * 
 * @author ryanm
 */
public class Add extends Command {

    /**
	 */
    public Add() {
        super("add");
    }

    @Override
    public void execute(String command) {
        String[] c = TextUtils.split(command, '(', ')', ' ');
        if (c.length >= 3) {
            if (c[0].equals("add")) {
                String path = c[1];
                if (path.startsWith("(") && path.endsWith(")")) {
                    path = path.substring(1, path.length() - 1);
                    try {
                        double addition = Double.valueOf(c[2]).floatValue();
                        Configurator conf = ConfGet.forPath(path, GameBox.configurators);
                        if (conf != null) {
                            String cp = conf.getPath();
                            String var = path.substring(cp.length() + 1);
                            boolean found = false;
                            for (int i = 0; i < conf.getNames().length && !found; i++) {
                                if (conf.getNames()[i] instanceof String) {
                                    String v = (String) conf.getNames()[i];
                                    if ((conf.getType(v) == int.class || conf.getType(v) == float.class) && v.compareToIgnoreCase(var) == 0) {
                                        var = v;
                                        found = true;
                                    }
                                }
                            }
                            if (found) {
                                double value = ((Number) conf.getValue(var)).doubleValue();
                                value += addition;
                                conf.setValue(var, new Double(value));
                                Console.log(path + "/" + var + " set to " + conf.getValue(var));
                            } else {
                                Console.error("Could not find numerical variable \"" + var + "\" in configurator \"" + conf.getPath() + "\"");
                            }
                        } else {
                            if (path.startsWith("/")) {
                                Console.error("Configurator \"" + path + "\" not found");
                            } else {
                                Console.error("Configurator paths must start with \"/\"");
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        Console.error("Could not parse number \"" + c[2] + "\"");
                    }
                } else {
                    Console.error("Parenthesise the path : " + getUsage());
                }
            }
        } else {
            Console.error(getUsage());
        }
    }

    @Override
    public String getUsage() {
        return "add (<variable path>) <amount>\n\tAdds some specified amount to numerical variables";
    }

    @Override
    public void suggest(String current, List<Suggestion> suggestions) {
        if (current.length() < 6 && "add (/".startsWith(current)) {
            suggestions.add(new Suggestion("add (/", 0));
        } else if (current.startsWith("add (/")) {
            String path = current.substring(5);
            LinkedList<Suggestion> cs = new LinkedList<Suggestion>();
            suggestConfiguratorName(path, cs);
            while (!cs.isEmpty()) {
                Suggestion s = cs.removeFirst();
                s.offset += 5;
                suggestions.add(s);
            }
        }
    }
}
