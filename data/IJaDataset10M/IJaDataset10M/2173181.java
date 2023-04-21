package cn.myapps.core.macro.repository.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import cn.myapps.base.action.BaseHelper;
import cn.myapps.core.macro.repository.ejb.RepositoryProcess;
import cn.myapps.util.ProcessFactory;

public class RepositoryActionHelper extends BaseHelper {

    Collection Repotype = new ArrayList();

    int i = 100;

    public RepositoryActionHelper() throws ClassNotFoundException {
        super(ProcessFactory.createProcess(RepositoryProcess.class));
        Repotype.add("{*[File]*}");
        Repotype.add("{*[Date]*}");
        Repotype.add("{*[String]*}");
        Repotype.add("{*[Number]*}");
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Collection getRepotype() {
        return Repotype;
    }

    public void setRepotype(List repotype) {
        Repotype = repotype;
    }
}
