package tudresden.ocl.check.bootstrap;

import tudresden.ocl.check.types.*;
import tudresden.ocl.lib.NameAdapter;

public class SableReflectionFacade extends ReflectionFacade {

    public SableReflectionFacade(String[] packageNames, ReflectionAdapter reflAdapter, NameAdapter nameAdapter) {
        super(packageNames, reflAdapter, nameAdapter);
    }

    protected ClassAny getClassAny(Class c) {
        ClassAny ret = (ClassAny) classAnyInstances.get(c);
        if (ret == null) {
            ret = new ReflectionClassAny(c);
            classAnyInstances.put(c, ret);
        }
        return ret;
    }

    class ReflectionClassAny extends ClassAny {

        protected ReflectionClassAny(Class c) {
            super(c, SableReflectionFacade.this);
        }

        public Type navigateQualified(String name, Type[] qualifiers) {
            if (qualifiers == null || qualifiers.length == 0) {
                if (name.equals("boundNames")) {
                    return new Collection(Collection.SET, Basic.STRING);
                }
                if (name.equals("subnodes") || name.equals("supernodes")) {
                    return new Collection(Collection.SET, getClassAny(tudresden.ocl.parser.node.Node.class));
                }
            }
            return super.navigateQualified(name, qualifiers);
        }

        public boolean hasState(String stateName) {
            return false;
        }
    }
}
