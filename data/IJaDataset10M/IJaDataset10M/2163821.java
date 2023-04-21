package sk.yw.azetclient.preferences;

/**
 *
 * @author error216
 */
public abstract class AbstractHandler implements Handler {

    public void beginApply() {
    }

    public abstract void preferenceChanged(String name, String oldValue, String newValue);

    public void endApply() {
    }
}
