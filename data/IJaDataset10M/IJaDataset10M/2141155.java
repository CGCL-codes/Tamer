package org.eclipse.swt.custom;

class SashFormData {

    long weight;

    String getName() {
        String string = getClass().getName();
        int index = string.lastIndexOf('.');
        if (index == -1) return string;
        return string.substring(index + 1, string.length());
    }

    /**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
    public String toString() {
        return getName() + " {weight=" + weight + "}";
    }
}
