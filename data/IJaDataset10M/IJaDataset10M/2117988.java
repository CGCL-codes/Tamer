package fr.fg.client.data;

import com.google.gwt.core.client.JavaScriptObject;

public class StructureSkillData extends JavaScriptObject {

    public static final int TYPE_STASIS = 1;

    public static final int STASIS_RELOAD = 24 * 3600;

    public static final int STASIS_RANGE = 25;

    public static final String FIELD_TYPE = "a", FIELD_LAST_USE = "b", FIELD_RELOAD = "c";

    protected StructureSkillData() {
    }

    public final native int getType();

    public final native double getLastUseTime();

    public final native double getReloadRemainingTime();
}
