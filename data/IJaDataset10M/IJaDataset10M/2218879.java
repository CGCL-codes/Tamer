package net.sourceforge.processdash.data.applet.js;

import net.sourceforge.processdash.data.SaveableData;
import net.sourceforge.processdash.data.applet.DataInterpreter;
import net.sourceforge.processdash.data.repository.DataListener;
import net.sourceforge.processdash.data.repository.DataRepository;
import net.sourceforge.processdash.data.repository.Repository;

public class JSRepository implements Repository {

    DataRepository r;

    public JSRepository(DataRepository data) {
        this.r = data;
    }

    public void addDataListener(String name, DataListener dl) {
        r.addDataListener(name, dl);
    }

    public void maybeCreateValue(String name, String value, String prefix) {
        r.maybeCreateValue(name, value, prefix);
    }

    public void putValue(String name, SaveableData value) {
        if (DataInterpreter.RESTORE_DEFAULT_TOKEN.equals(value)) r.restoreDefaultValue(name); else r.userPutValue(name, value);
    }

    public void removeDataListener(String name, DataListener dl) {
        r.removeDataListener(name, dl);
    }

    public void removeValue(String name) {
        r.removeValue(name);
    }
}
