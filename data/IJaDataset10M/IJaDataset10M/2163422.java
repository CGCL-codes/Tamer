package com.umc.plugins;

import java.util.Hashtable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.apache.log4j.Logger;
import com.umc.beans.persons.*;

public class result_debug_output {

    public static void debug_output(Hashtable<String, Object> table, Logger log) {
        log = null;
        String act;
        Set<String> set = table.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            act = it.next();
            if (table.get(act) instanceof String) {
                if (log == null) System.out.println(act + ": " + table.get(act)); else log.info(act + ": " + table.get(act));
            } else if (table.get(act) instanceof LinkedList) {
                LinkedList llist = (LinkedList) table.get(act);
                if (llist.size() == 0) {
                    if (log == null) System.out.println(act + ":"); else log.info(act + ":");
                } else {
                    for (int i = 0; i < llist.size(); i++) {
                        if (llist.get(i) instanceof String) {
                            if (log == null) System.out.println(act + " - " + i + ": " + llist.get(i)); else log.info(act + " - " + i + ": " + llist.get(i));
                        } else if (llist.get(i) instanceof Person) {
                            Person person = (Person) llist.get(i);
                            String output;
                            String id = " (";
                            if (person.getIdIMDB() != null) id += "IMDB-ID: " + person.getIdIMDB();
                            if (person.getIdOFDB() != null) {
                                if (!id.equals(" (")) id += " - ";
                                id += "OFDB-ID: " + person.getIdOFDB();
                            }
                            if (person.getIdMovieDB() != null) {
                                if (!id.equals(" (")) id += " - ";
                                id += "MovieDB-ID: " + person.getIdMovieDB();
                            }
                            if (!id.equals(" (")) id += ")"; else id = null;
                            output = act + " - " + i + " - " + person.getPersonType() + ": " + person.getName() + id;
                            if (llist.get(i) instanceof Actor) {
                                if (!((Actor) llist.get(i)).getRole().equals("")) {
                                    output += " as " + ((Actor) person).getRole();
                                }
                            }
                            if (log == null) System.out.println(output); else log.info(output);
                        }
                    }
                }
            }
        }
    }

    public static void debug_output(Hashtable<String, Object> table) {
        debug_output(table, null);
    }
}
