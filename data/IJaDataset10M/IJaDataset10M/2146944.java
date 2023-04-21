package org.herakles.ml.selection.trainingData.queryGenerate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyExpression;

public class AxiomParameterGenerator {

    private static final int FACTOR = 10000;

    private static final int COLLECTION_SIZE = 10;

    private static final int MAX_STRING_LENGTH = 50;

    private static final char[] basicChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    private OWLOntoElementExtractor ontoExtractor;

    private OWLClassExpressionGenerator owlClassExpressionGenerator;

    private OWLPropertyExpressionGenerator owlPropertyExpressionGenerator;

    private OWLDataPropertyExpressionGenerator owlDataPropertyExpressionGenerator;

    private OWLObjectPropertyExpressionGenerator owlObjectPropertyExpressionGenerator;

    public AxiomParameterGenerator(OWLOntoElementExtractor ontoExtractor, OWLClassExpressionGenerator owlClassExpressionGenerator, OWLPropertyExpressionGenerator owlPropertyExpressionGenerator, OWLDataPropertyExpressionGenerator owlDataPropertyExpressionGenerator, OWLObjectPropertyExpressionGenerator owlObjectPropertyExpressionGenerator) {
        this.ontoExtractor = ontoExtractor;
        this.owlClassExpressionGenerator = owlClassExpressionGenerator;
        this.owlPropertyExpressionGenerator = owlPropertyExpressionGenerator;
        this.owlDataPropertyExpressionGenerator = owlDataPropertyExpressionGenerator;
        this.owlObjectPropertyExpressionGenerator = owlObjectPropertyExpressionGenerator;
    }

    public Integer getint() {
        return (int) (Math.random() * FACTOR);
    }

    public Boolean getboolean() {
        if (Math.random() < 0.5) {
            return true;
        } else {
            return false;
        }
    }

    public Float getfloat() {
        return (float) Math.random() * FACTOR;
    }

    public Double getdouble() {
        return Math.random() * FACTOR;
    }

    public String getstring() {
        int stringLength = (int) (Math.random() * MAX_STRING_LENGTH);
        char[] chars = new char[stringLength];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = basicChars[(int) (Math.random() * chars.length)];
        }
        return new String(chars);
    }

    public OWLDatatype getOWLDatatype() {
        return ontoExtractor.getOWLDatatype();
    }

    public OWLDataRange getOWLDataRange() {
        return getOWLDatatype();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(Class<T> c) {
        int length = (int) (Math.random() * COLLECTION_SIZE) + 1;
        List<T> list = new ArrayList<T>();
        Method method = getMethod(c.getSimpleName());
        for (int i = 0; i < length; i++) {
            list.add((T) invokeMethod(this, method));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> getSet(Class<T> c) {
        int length = (int) (Math.random() * COLLECTION_SIZE) + 1;
        Set<T> set = new HashSet<T>();
        Method method = getMethod(c.getSimpleName());
        for (int i = 0; i < length; i++) {
            set.add((T) invokeMethod(this, method));
        }
        return set;
    }

    public OWLClassExpression getOWLClassExpression() {
        return owlClassExpressionGenerator.getOWLClassExpression();
    }

    public OWLPropertyExpression<?, ?> getOWLPropertyExpression() {
        return owlPropertyExpressionGenerator.getOWLPropertyExpression();
    }

    public OWLDataPropertyExpression getOWLDataPropertyExpression() {
        return owlDataPropertyExpressionGenerator.getOWLDataPropertyExpression();
    }

    public OWLObjectPropertyExpression getOWLObjectPropertyExpression() {
        return owlObjectPropertyExpressionGenerator.getOWLObjectPropertyExpression();
    }

    public OWLIndividual getOWLIndividual() {
        return ontoExtractor.getOWLIndividual();
    }

    public OWLClass getOWLClass() {
        return ontoExtractor.getOWLClass();
    }

    public OWLEntity getOWLEntity() {
        return ontoExtractor.getOWLEntity();
    }

    public Method getMethod(String paraName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = this.getClass().getMethod("get" + paraName, parameterTypes);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public Object invokeMethod(Object calledObject, Method method, Object... objects) {
        Object value = null;
        try {
            value = method.invoke(calledObject, objects);
        } catch (IllegalArgumentException e) {
            System.out.println("\n" + method.getDeclaringClass() + ": " + method.getName());
            Type[] types = method.getParameterTypes();
            if (types.length == objects.length) {
                for (int i = 0; i < types.length; i++) {
                    System.out.println(types[i].toString() + " <- " + objects[i].toString() + ": " + (objects[i].getClass().equals(getParamClass(types[i]))));
                }
            } else {
                System.out.println("types: " + types.length + " but objects: " + objects.length);
            }
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    public Class<?> getParamClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        return null;
    }
}
