package org.dbmaintain.script;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 * @since 26-dec-2008
 */
public class ScriptUpdate implements Comparable<ScriptUpdate> {

    private ScriptUpdateType type;

    private Script script;

    private Script renamedToScript;

    public ScriptUpdate(ScriptUpdateType type, Script script) {
        this(type, script, null);
    }

    public ScriptUpdate(ScriptUpdateType type, Script script, Script renamedToScript) {
        this.type = type;
        this.script = script;
        this.renamedToScript = renamedToScript;
    }

    public ScriptUpdateType getType() {
        return type;
    }

    public Script getScript() {
        return script;
    }

    public Script getRenamedToScript() {
        return renamedToScript;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScriptUpdate)) return false;
        ScriptUpdate that = (ScriptUpdate) o;
        if (type != that.type) return false;
        if (!script.equals(that.script)) return false;
        if (renamedToScript != null ? !renamedToScript.equals(that.renamedToScript) : that.renamedToScript != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + script.hashCode();
        result = 31 * result + (renamedToScript != null ? renamedToScript.hashCode() : 0);
        return result;
    }

    public int compareTo(ScriptUpdate other) {
        return script.compareTo(other.getScript());
    }

    @Override
    public String toString() {
        return "ScriptUpdate{" + "type=" + type + ", script=" + script + (renamedToScript != null ? ", renamedToScript=" + renamedToScript : "") + '}';
    }
}
