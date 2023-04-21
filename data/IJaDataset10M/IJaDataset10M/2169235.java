package neon.systems.scripting;

import java.util.*;
import neon.util.MultiMap;

/**
 * This class loads neon scripts, written in JavaScript, from an external script file. 
 * 
 * @author mdriesen
 */
public class ScriptFactory {

    private static HashMap<String, String> scripts = new HashMap<String, String>();

    private static MultiMap<Integer, String> tasks = new MultiMap<Integer, String>();

    /**
	 * @param id	the id of the wanted script
	 * @return		the script with the given id
	 */
    public static String getScript(String id) {
        return scripts.get(id);
    }

    public static void addScript(String name, String content) {
        scripts.put(name, content);
    }

    public static void addScripts(HashMap<String, String> newScripts) {
        scripts.putAll(newScripts);
    }

    public static void addTask(String s, Integer time) {
        tasks.put(time, s);
    }

    public static void deleteTask(String s, Integer time) {
        tasks.get(time).remove(s);
    }

    public static MultiMap<Integer, String> getTasks() {
        return tasks;
    }
}
