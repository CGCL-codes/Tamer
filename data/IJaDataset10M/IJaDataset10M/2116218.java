package tm.backtrack;

/** A class for objects that can be backed up
 */
public class BTVar<T> {

    private BTTimeManager timeMan;

    private BTValManager<T> valMan;

    public BTVar(BTTimeManager tm) {
        timeMan = tm;
        valMan = null;
    }

    public BTVar(BTTimeManager tm, T initialValue) {
        timeMan = tm;
        valMan = new BTValManager<T>();
        valMan.set(timeMan.getCurrentTime(), initialValue);
    }

    public void set(T o) {
        if (valMan == null) {
            valMan = new BTValManager<T>();
        }
        valMan.set(timeMan.getCurrentTime(), o);
    }

    public T get() {
        if (valMan == null) {
            return null;
        } else {
            return valMan.get(timeMan.getCurrentTime());
        }
    }
}
