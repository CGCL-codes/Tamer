package com.mtp.pounder;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.StringTokenizer;

/**

Finds Component's using a generated key.

@author Matthew Pekar

**/
public class KeyIdentifier implements ComponentIdentifier {

    public static KeyIdentifier instantiate(String s) {
        int index = s.indexOf(':') + 1;
        String data = s.substring(index, s.length());
        return new KeyIdentifier(data);
    }

    protected int[] key;

    public KeyIdentifier() {
        key = null;
    }

    public KeyIdentifier(Component c) {
        this.key = generateKey(c);
    }

    public KeyIdentifier(String data) {
        this.key = getKeyForString(data);
    }

    public Component getComponent(Window w) {
        Container c = w;
        for (int i = 0; i < (key.length - 1); i++) {
            c = (Container) c.getComponent(key[i]);
        }
        return c.getComponent(key[key.length - 1]);
    }

    public String asString() {
        return "com.mtp.pounder.KeyIdentifier:" + getStringForKey(key);
    }

    public String toString() {
        return "Key: " + getStringForKey(key);
    }

    public boolean equals(Object o) {
        KeyIdentifier ki = (KeyIdentifier) o;
        if (key.length != ki.key.length) return false;
        for (int i = 0; i < key.length; i++) {
            if (key[i] != ki.key[i]) return false;
        }
        return true;
    }

    protected Component getRoot(Component c) {
        Container parent = c.getParent();
        if (parent == null) return c; else return getRoot(parent);
    }

    protected int getHeight(Component c) {
        return getHeight(c, 0);
    }

    protected int getHeight(Component c, int curHeight) {
        if (c == null) return 0;
        Container parent = c.getParent();
        if ((c instanceof Window) && (parent instanceof Window)) return curHeight + 1;
        return getHeight(parent) + curHeight + 1;
    }

    protected int getIndexInParent(Component c) {
        Container parent = c.getParent();
        Component[] children = parent.getComponents();
        for (int i = 0; i < children.length; i++) {
            if (children[i] == c) return i;
        }
        throw new RuntimeException("This should never occur, component: " + c + ", parent: " + c.getParent());
    }

    protected int[] generateKey(Component c) {
        Component root = getRoot(c);
        int height = getHeight(c);
        int[] ret = new int[height - 1];
        int index = ret.length - 1;
        while (index >= 0) {
            ret[index] = getIndexInParent(c);
            c = c.getParent();
            index -= 1;
        }
        return ret;
    }

    protected String getStringForKey(int[] key) {
        StringBuffer buf = new StringBuffer(key.length * 2);
        for (int i = 0; i < key.length; i++) {
            buf.append(String.valueOf(key[i]));
            if (i != (key.length - 1)) buf.append(',');
        }
        return buf.toString();
    }

    protected int[] getKeyForString(String s) {
        StringTokenizer t = new StringTokenizer(s, ",");
        int[] ret = new int[t.countTokens()];
        int index = 0;
        while (t.hasMoreTokens()) {
            ret[index] = Integer.valueOf(t.nextToken()).intValue();
            index += 1;
        }
        return ret;
    }
}
