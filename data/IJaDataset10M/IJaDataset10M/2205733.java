package nl.huub.van.amelsvoort.game;

/**
 * cvar_t implements the struct cvar_t of the C version
 */
public final class cvar_t {

    public String name;

    public String string;

    public String latched_string;

    public int flags = 0;

    public boolean modified = false;

    public float value = 0.0f;

    public float waarde = 0.0f;

    public cvar_t next = null;
}
