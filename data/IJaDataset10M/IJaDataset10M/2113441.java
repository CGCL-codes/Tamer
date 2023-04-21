package org.gap.jseed.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.gap.jseed.Validator;

public class BeanValidator implements Validator {

    @Override
    public void validate(Class<?> validatee) {
        StringBuffer result = new StringBuffer();
        Map<String, SetGetPair> mutators = new HashMap<String, SetGetPair>();
        for (Method each : validatee.getDeclaredMethods()) {
            if (isGetMethod(each)) {
                String property = BeanUtil.getPropertyName(each.getName());
                ensureMutatorFor(mutators, property);
                mutators.get(property).addGetMethod(each);
            } else if (isSetMethod(each)) {
                String property = BeanUtil.getPropertyName(each.getName());
                ensureMutatorFor(mutators, property);
                mutators.get(property).addSetMethod(each);
            } else if (isAddAndRemoveMethod(each)) {
                String property = BeanUtil.getPropertyName(each.getName()) + "s";
                ensureMutatorFor(mutators, property);
                mutators.get(property).addAddAndRemoveMethod(each);
            }
        }
        for (String each : mutators.keySet()) {
            if (mutators.get(each).isPair() || mutators.get(each).isCollection()) {
                continue;
            }
            result.append(mutators.get(each).createMessage());
        }
        if (result.length() > 0) {
            throw new RuntimeException(validatee.getName() + ": " + result.toString());
        }
    }

    /**
	 * Ensures the mutator has the {@link SetGetPair} added.
	 */
    private void ensureMutatorFor(Map<String, SetGetPair> mutators, String property) {
        if (!mutators.containsKey(property)) {
            mutators.put(property, new SetGetPair());
        }
    }

    /**
	 * determines if this method is a set method
	 * @param each
	 * @return
	 */
    private boolean isSetMethod(Method each) {
        return each.getName().startsWith("set");
    }

    private boolean isAddAndRemoveMethod(Method each) {
        String name = each.getName();
        return (name.startsWith("add") || name.startsWith("remove")) && !name.endsWith("Listener");
    }

    /**
	 * determines if this method is a get method
	 * @param each
	 * @return
	 */
    private boolean isGetMethod(Method each) {
        return each.getName().startsWith("get") || each.getName().startsWith("is");
    }

    /**
	 * Validator for set/get pairing.  if a pair does not exist then something 
	 * is terribly wrong :P
	 * @author gpelcha
	 *
	 */
    private class SetGetPair {

        private String propertyName;

        private static final int SETTERS = -1;

        private static final int GETTERS = 1;

        private LinkedList<Method> getters;

        private LinkedList<Method> setters;

        private LinkedList<Method> addAndRemove;

        public SetGetPair() {
            getters = new LinkedList<Method>();
            setters = new LinkedList<Method>();
            addAndRemove = new LinkedList<Method>();
        }

        public Object createMessage() {
            int value = setters.size() > getters.size() ? -1 : setters.size() == getters.size() ? 0 : 1;
            switch(value) {
                case 0:
                    return "Too many getters [" + getters.size() + "] and/or too many setters [" + setters.size() + "] for [" + propertyName + "], should be exactly 1 each";
                case SETTERS:
                    return "There is a setter method but no getter method for [" + propertyName + "]";
                case GETTERS:
                    return "There is a getter method but no setter method for [" + propertyName + "]";
            }
            return "uncertain of error";
        }

        public boolean isCollection() {
            if (getters.size() != 1) {
                return false;
            }
            if (addAndRemove.size() != 2) {
                return false;
            }
            return true;
        }

        public boolean isPair() {
            if (setters.size() != 1) {
                return false;
            }
            if (getters.size() != 1) {
                return false;
            }
            return true;
        }

        public void addSetMethod(Method setter) {
            propertyName = BeanUtil.getPropertyName(setter.getName());
            setters.add(setter);
        }

        public void addGetMethod(Method getter) {
            propertyName = BeanUtil.getPropertyName(getter.getName());
            getters.add(getter);
        }

        public void addAddAndRemoveMethod(Method addRemove) {
            propertyName = BeanUtil.getPropertyName(addRemove.getName() + "s");
            addAndRemove.add(addRemove);
        }
    }
}
