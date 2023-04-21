package amiba.system;

import java.lang.reflect.*;

public interface DModel {

    public int getCost(Method meth, Object[] args, Object[] dargs);

    public Object[] getDefaults(Method meth);
}
