package net.tralfamadore.cmf;

/**
 * User: billreh
 * Date: 1/18/11
 * Time: 11:45 PM
 */
public class Script extends BaseContent {

    private String script;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Script)) return false;
        if (!super.equals(o)) return false;
        Script script1 = (Script) o;
        return !(script != null ? !script.equals(script1.script) : script1.script != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (script != null ? script.hashCode() : 0);
        return result;
    }
}
