package magicstudio.util;

import java.util.List;
import java.util.LinkedList;

public class CombinedException extends Exception {

    private final List _exceptions;

    public CombinedException() {
        super("This is a combined exception which contains several exceptions.");
        _exceptions = new LinkedList();
    }

    public void add(Exception e) {
        assert e != null;
        _exceptions.add(e);
    }

    public void add(CombinedException e) {
        assert e != null;
        _exceptions.addAll(e._exceptions);
    }

    public String[] getContainedMessages() {
        String[] s = new String[_exceptions.size()];
        for (int i = 0; i < s.length; i++) {
            s[i] = ((Exception) (_exceptions.toArray()[i])).getMessage();
        }
        return s;
    }

    public Exception[] getContainedExceptions() {
        Exception[] e = new Exception[_exceptions.size()];
        for (int i = 0; i < e.length; i++) {
            e[i] = (Exception) (_exceptions.toArray()[i]);
        }
        return e;
    }

    public boolean isEmpty() {
        return _exceptions.isEmpty();
    }
}
