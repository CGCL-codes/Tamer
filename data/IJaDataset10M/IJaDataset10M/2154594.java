package blockworld.lib;

import java.util.Observable;
import java.util.Observer;

public class IntegerAttrAdapter implements Observer {

    protected IntegerAttrListener _listener;

    public IntegerAttrAdapter(IntegerAttrListener listener) {
        _listener = listener;
    }

    public void update(Observable o, Object arg) {
        _listener.onValueChange((Integer) arg);
    }

    public boolean equals(Object o) {
        return o instanceof IntegerAttrAdapter && ((IntegerAttrAdapter) o)._listener.equals(_listener);
    }
}
