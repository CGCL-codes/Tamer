package org.jaffa.util;

import java.util.*;

/** This class determines how an event posted by a browser is to be handled.
 */
public class EventHandler {

    /**
     * This method will generate an array of EventHandler.Method objects.
     * Each object will have a Name, the signature & the parameters
     * For eg: if eventId="grid1=4;partLookup;clicked"
     * The following methods will be generated:
     * do_grid1_4_partLookup_clicked
     * do_grid1_partLookup_clicked(String v1), v1="4"
     * do_grid1_clicked(String v1, String v2), v1="4" & v2="partLookup"
     * @param eventId The event.
     * @return an array of EventHandler.Method objects.
     */
    public static EventHandler.Method[] getEventMethods(String eventId) {
        List methods = new ArrayList();
        if (eventId != null) {
            String fieldName = null;
            List arguments = new ArrayList();
            String eventName = null;
            StringTokenizer stz = new StringTokenizer(eventId, " =;");
            while (stz.hasMoreTokens()) arguments.add(stz.nextToken());
            if (arguments.size() > 0) fieldName = (String) arguments.remove(0);
            if (arguments.size() > 0) eventName = (String) arguments.remove(arguments.size() - 1);
            if (arguments.size() == 0) {
                Method method = new Method(fieldName);
                if (eventName != null) method.appendName(eventName);
                methods.add(method);
            } else {
                for (int i = 0; i <= arguments.size(); i++) {
                    Method method = new Method(fieldName);
                    int j = 0;
                    for (; j < i; j++) method.appendArgument(String.class, (String) arguments.get(j));
                    int k = j;
                    for (; k < arguments.size(); k++) method.appendName((String) arguments.get(k));
                    method.appendName(eventName);
                    methods.add(method);
                }
            }
        }
        return (EventHandler.Method[]) methods.toArray(new EventHandler.Method[0]);
    }

    /** This class respresents an EventHandler method. It has the method name,
     * the argument types and the argument values.
     */
    public static class Method {

        private StringBuffer m_name = new StringBuffer("do");

        private List m_argumentTypes = new ArrayList();

        private List m_argumentValues = new ArrayList();

        /** Constructor.
         * @param name The name of the event handler method.
         */
        public Method(String name) {
            appendName(name);
        }

        /** Getter for property name.
         * @return Value of property name.
         */
        public String getName() {
            return m_name.toString();
        }

        /** Getter for property argumentTypes.
         * @return Value of property argumentTypes.
         */
        public Class[] getArgumentTypes() {
            return (Class[]) m_argumentTypes.toArray(new Class[0]);
        }

        /** Getter for property argumentValues.
         * @return Value of property argumentValues.
         */
        public Object[] getArgumentValues() {
            return m_argumentValues.toArray();
        }

        /** Returns diagnostic information.
         * @return diagnostic information.
         */
        public String toString() {
            StringBuffer buf = new StringBuffer(getName());
            buf.append('(');
            for (int i = 0; i < m_argumentTypes.size(); i++) {
                if (i > 0) {
                    buf.append(',');
                    buf.append(' ');
                }
                buf.append(((Class) m_argumentTypes.get(i)).getName());
                buf.append('=');
                buf.append(m_argumentValues.get(i));
            }
            buf.append(')');
            return buf.toString();
        }

        private void appendName(String name) {
            if (name != null) {
                m_name.append('_');
                m_name.append(name);
            }
        }

        private void appendArgument(Class argumentType, Object argumentValue) {
            if (argumentType != null && argumentValue != null) {
                m_argumentTypes.add(argumentType);
                m_argumentValues.add(argumentValue);
            }
        }
    }
}
