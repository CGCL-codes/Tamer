package conga.io.format;

import conga.io.Source;
import conga.param.ParameterImpl;
import conga.param.tree.ParamListImpl;
import conga.param.tree.Tree;
import java.io.Reader;
import java.util.Properties;

/** @author Justin Caballero */
public abstract class AbstractPropertiesFormat extends AbstractFormat {

    protected void doRead(Reader reader, Tree tree, Source source) {
        Properties props = loadProps(reader);
        ParamListImpl params = new ParamListImpl();
        int count = 0;
        for (Object o : props.keySet()) {
            String key = (String) o;
            params.getBackingList().add(new ParameterImpl(key, props.getProperty(key), source, null, count++));
        }
        tree.add(params);
    }

    protected abstract Properties loadProps(Reader reader);
}
