package org.systemsbiology.apps.corragui.domain;

import java.io.Serializable;

public class Module implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9204104789681254449L;

    private String name;

    private Resources resources;

    public Module() {
        this.name = new String();
        this.resources = new Resources();
    }

    public Module(String name) {
        this.name = name;
        this.resources = new Resources();
    }

    public String getName() {
        return name;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Resources getResources() {
        return this.resources;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Module: ");
        buf.append(getName());
        buf.append(" ");
        buf.append(resources.toString());
        return buf.toString();
    }

    public static class Resources implements Serializable {

        private long mem = 268435456;

        public Resources() {
        }

        public void setMemory(long mem) {
            this.mem = mem;
        }

        public long getMemory() {
            return this.mem;
        }

        public String toString() {
            return "Memory: " + mem;
        }
    }
}
