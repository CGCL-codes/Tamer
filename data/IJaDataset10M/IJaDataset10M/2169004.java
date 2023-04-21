package net.sourceforge.processdash.data.applet;

import net.sourceforge.processdash.util.FormatUtil;

public class InputName {

    public String prefix, relName, name, flags, value;

    public InputName(String text, String prefix) {
        int tabpos;
        if (text.startsWith("[")) {
            text = text.substring(1);
            tabpos = text.lastIndexOf(']');
        } else tabpos = text.indexOf('\t');
        if (tabpos == -1) {
            name = text;
            flags = value = "";
        } else {
            name = text.substring(0, tabpos);
            int equalpos = text.indexOf('=', tabpos + 1);
            if (equalpos == -1) {
                flags = text.substring(tabpos + 1);
                value = "";
            } else {
                flags = text.substring(tabpos + 1, equalpos);
                value = text.substring(equalpos + 1);
            }
        }
        this.prefix = prefix;
        this.relName = name;
        if (prefix == null) prefix = "";
        if (name == null || name.length() == 0) name = prefix; else if (!name.startsWith("/")) name = prefix + "/" + name;
    }

    public boolean hasFlag(char flag) {
        return (flags.indexOf(flag) != -1);
    }

    public int digitFlag() {
        for (int pos = flags.length(); pos != 0; ) {
            char c = flags.charAt(--pos);
            if ((c <= '9') && (c >= '0')) return (c - '0');
        }
        return FormatUtil.AUTO_DECIMAL;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        if (flags != null && flags.length() != 0) result.append(flags);
        if (value != null && value.length() != 0) {
            result.append('=');
            result.append(value);
        }
        if (result.length() == 0) return name; else {
            result.insert(0, '\t');
            result.insert(0, name);
            return result.toString();
        }
    }
}
