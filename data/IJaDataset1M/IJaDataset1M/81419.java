package org.jmad.adapters;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import org.jmad.User;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Strategy;
import org.simpleframework.xml.graph.CycleStrategy;

public abstract class GenericXMLAdapter<T extends Serializable> implements IFileAdapter<T> {

    private Class<T> persistentClass;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(GenericXMLAdapter.class);

    private Serializer serializer;

    @SuppressWarnings("unchecked")
    public GenericXMLAdapter() {
        Class<T> class1 = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.persistentClass = class1;
        Strategy strategy = new CycleStrategy("refid", "ref");
        serializer = new Persister(strategy);
    }

    @Override
    public T read(String filename) throws AdapterException {
        File source = new File(filename);
        T example;
        try {
            example = serializer.read(persistentClass, source);
        } catch (Exception e) {
            String msg = "%s: %s, while reading %s object";
            msg = String.format(msg, e.getClass().getName(), e.getMessage(), persistentClass.getName());
            logger.error(msg);
            throw new AdapterException(msg, e);
        }
        return example;
    }

    @Override
    public void write(String filename, T adaptedSource) throws AdapterException {
        File result = new File(filename);
        try {
            serializer.write(adaptedSource, result);
        } catch (Exception e) {
            String msg = "%s: %s, while writing %s object";
            msg = String.format(msg, e.getClass().getName(), e.getMessage(), persistentClass.getName());
            logger.error(msg);
            throw new AdapterException(msg, e);
        }
    }
}
