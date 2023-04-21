package fr.fg.client.data;

import com.google.gwt.core.client.JavaScriptObject;

public class DialogData extends JavaScriptObject {

    public static final String FIELD_TALKER = "a", FIELD_CONTENT = "b", FIELD_OPTIONS = "c", FIELD_AVATAR = "d", FIELD_VALID_OPTIONS = "e";

    protected DialogData() {
    }

    public final native boolean isEndOfDialog();

    public final native String getAvatar();

    public final native String getTalker();

    public final native String getContent();

    public final native int getOptionsCount();

    public final native String getOptionAt(int index);

    public final native int getValidOptionsCount();

    public final native boolean isValidOptionAt(int index);

    public final String[] getOptions() {
        String[] options = new String[getOptionsCount()];
        for (int i = 0; i < options.length; i++) options[i] = getOptionAt(i);
        return options;
    }

    public final boolean[] getValidOptions() {
        boolean[] options = new boolean[getValidOptionsCount()];
        for (int i = 0; i < options.length; i++) options[i] = isValidOptionAt(i);
        return options;
    }
}
