package net.sourceforge.jdefprog.mcl.interpret;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.sourceforge.jdefprog.logging.DescUtils;
import net.sourceforge.jdefprog.mcl.interpret.context.builders.AbstractTypeResolutor;
import net.sourceforge.jdefprog.reflection.Type;

public class ClassResolutorBuffer extends AbstractTypeResolutor {

    private Map<String, Type> classes = new HashMap<String, Type>();

    public void addClass(Type c) {
        classes.put(c.getName(), c);
    }

    @Override
    protected boolean concreteExistClass(String canonicalName) {
        boolean result = classes.containsKey(canonicalName);
        if (ParserTest.logger.isLoggable(Level.FINEST) && !result) {
            ParserTest.logger.finest("Class " + canonicalName + " not found, elements are " + DescUtils.arrayToString(classes.keySet().toArray(new String[] {})));
        }
        return result;
    }

    @Override
    protected Type concreteGet(String canonicalName) {
        return classes.get(canonicalName);
    }

    public void clear() {
        classes.clear();
    }
}
