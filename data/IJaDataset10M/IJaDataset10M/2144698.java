package com.naildrivin5.inlaw.util;

import java.util.*;
import java.io.*;
import java.text.*;
import com.naildrivin5.inlaw.*;

/** Helper class for debugging that can output an entire database */
public class DatabaseDumper {

    private Database itsDatabase;

    public DatabaseDumper(Database database) {
        itsDatabase = database;
    }

    public void dump(PrintWriter writer) {
        dump(writer, true);
    }

    public void dump(PrintWriter writer, boolean pretty) {
        SortedSet<String> types = new TreeSet<String>(itsDatabase.getAllTypes());
        Set<String> relationshipNames = new HashSet<String>();
        int numItems = 0;
        for (String s : types) {
            if (pretty) {
                writer.print("[");
                writer.print(s);
                writer.println("]");
            }
            SortedSet<Item> items = new TreeSet<Item>(new ItemNameComparator());
            items.addAll(itsDatabase.findByType(s));
            for (Item i : items) {
                numItems++;
                if (pretty) {
                    writer.print("\t");
                    writer.println(i.getName());
                    for (Relationship r : i.getRelationships()) {
                        relationshipNames.add(r.getName());
                        writer.print("\t\t");
                        writer.print(r.getName());
                        writer.print(" => ");
                        writer.println(getItemString(r.getDestination(), false, true));
                    }
                } else {
                    writer.print(getItemString(i, true, false));
                }
            }
        }
        if (pretty) {
            writer.println();
            writer.print(numItems);
            writer.println(" total items");
            writer.print(types.size());
            writer.println(" total types");
            writer.print(relationshipNames.size());
            writer.println(" total relationship types");
        }
        writer.flush();
    }

    /** Returns the string representation of an item, optionally showing relationships.
     * @param i the item to get as a string
     * @param full if false, the item's type and name are returned.  If true, a multi-line string is returned
     * that has the type and name, followed by a relationship name, the destination item's type and name, all on one line,
     * one line per relationship.  For example, suppose an item "foo" of type "blahType" has a relation "parent" to
     * and item "bar" of type "barType" and a relation "child" to an item "baz" of type "blahType".  The output would look
     * like so:
     * <pre>
     * blahType,foo,parent,bar,barType
     * blahType,foo,child,baz,fooType
     * </pre>
     * @param pretty if false, each field is comma-separated. If true, a nicer, but non-machine parsable format is used
     */
    public static String getItemString(Item i, boolean full, boolean pretty) {
        if (!full) {
            if (pretty) if (i instanceof Literal) return MessageFormat.format("\"{0}\"", i.getName()); else return MessageFormat.format("[{0}]:{1}", i.getType(), i.getName()); else return MessageFormat.format("{0},{1}", i.getType(), i.getName());
        } else {
            StringBuilder b = new StringBuilder();
            if (i.getRelationships().size() > 0) {
                for (Relationship r : i.getRelationships()) {
                    b.append(getItemString(i, false, pretty));
                    if (pretty) b.append(" == "); else b.append(",");
                    b.append(r.getName());
                    if (pretty) b.append(" => "); else b.append(",");
                    b.append(getItemString(r.getDestination(), false, pretty));
                    b.append("\n");
                }
            } else {
                b.append(getItemString(i, false, pretty));
            }
            return b.toString();
        }
    }
}
