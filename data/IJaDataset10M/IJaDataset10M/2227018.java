package net.sf.l2j.gameserver.scripting;

/**
 * @author KenM
 */
public abstract class ScriptManager<S extends ManagedScript> {

    public abstract Iterable<S> getAllManagedScripts();

    public boolean reload(S ms) {
        return ms.reload();
    }

    public boolean unload(S ms) {
        return ms.unload();
    }

    public void setActive(S ms, boolean status) {
        ms.setActive(status);
    }

    public abstract String getScriptManagerName();
}
