package pspdash.data;

import java.util.Vector;
import netscape.javascript.JSObject;

class NSSelectField extends NSField {

    Vector optionList = null;

    JSObject formOptions = null;

    private static boolean USE_DOUBLE = true;

    public NSSelectField(JSObject element, Repository data, String dataPath) {
        super(element, data, dataPath);
        optionList = new Vector();
        formOptions = (JSObject) element.getMember("options");
        JSObject option;
        int numOptions = NSFieldManager.intValue(formOptions.getMember("length"));
        for (int optIdx = 0; optIdx < numOptions; optIdx++) optionList.addElement(getOptionValue(formOptions, optIdx));
        if (variantValue != null) paint();
    }

    public void fetch() {
        variantValue = i.getString();
    }

    public void paint() {
        setSelection((String) variantValue);
    }

    public void parse() {
        variantValue = getSelection();
    }

    public void setSelection(String text) {
        if (element != null && optionList != null) {
            for (int idx = optionList.size(); idx-- > 0; ) if (stringEquals(text, (String) optionList.elementAt(idx))) {
                element.setMember("selectedIndex", makeInt(idx));
                int resultingValue = NSFieldManager.intValue(element.getMember("selectedIndex"));
                if (idx != resultingValue) {
                    USE_DOUBLE = !USE_DOUBLE;
                    element.setMember("selectedIndex", makeInt(idx));
                }
                return;
            }
        }
    }

    private boolean stringEquals(String a, String b) {
        if ((a == null || a.length() == 0) && (b == null || b.length() == 0)) return true;
        if (a == null) return false;
        return a.equals(b);
    }

    private Object makeInt(int i) {
        if (USE_DOUBLE) return new Double(i); else return new Integer(i);
    }

    public String getSelection() {
        if (element == null) return "";
        int idx = NSFieldManager.intValue(element.getMember("selectedIndex"));
        if (idx == -1 || idx >= optionList.size()) return "";
        return (String) optionList.elementAt(idx);
    }

    private static String getOptionValue(JSObject formOptions, int idx) {
        JSObject option = (JSObject) formOptions.getSlot(idx);
        String result = (String) option.getMember("value");
        if (result == null || result.trim().length() == 0) {
            result = (String) option.getMember("text");
            if (result != null) result = result.trim();
        }
        return result;
    }
}
