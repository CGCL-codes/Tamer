package net.sf.jlue.context.initializer;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.sf.jlue.aop.support.ObjectFactory;
import net.sf.jlue.context.config.InitializerParser;
import net.sf.jlue.exception.ConfigureException;

/**
 * @author Sun Yat-ton (Mail:PubTlk@Hotmail.com)
 * @version 1.00 2008-1-29
 * 
 */
public class InitializerManagerFromConfig implements InitializerManager {

    protected List initDescs;

    protected Map inits = new HashMap();

    /**
	 * 
	 */
    public InitializerManagerFromConfig(InputStream in) {
        try {
            loadConfig(in);
        } catch (Exception e) {
            throw new ConfigureException(e);
        }
    }

    private void loadConfig(InputStream in) throws Exception {
        InitializerParser initParser = new InitializerParser();
        initDescs = initParser.parse(in);
        for (Iterator iterator = initDescs.iterator(); iterator.hasNext(); ) {
            InitializerDescriptor desc = (InitializerDescriptor) iterator.next();
            Initializer i = (Initializer) ObjectFactory.getInstance(desc.getClazz());
            i.setName(desc.getName());
            i.setDescriptor(desc);
            inits.put(desc.getName(), i);
        }
    }

    public InitializerDescriptor getDescriptor(String name) {
        int i = initDescs.indexOf(new InitializerDescriptor(name));
        if (i < 0) return null;
        return (InitializerDescriptor) initDescs.get(i);
    }

    public List getDescriptors() {
        Collections.sort(initDescs);
        return initDescs;
    }

    public Initializer getInitializer(String name) {
        return (Initializer) inits.get(name);
    }

    public Set getInitializers() {
        final InitializerManagerFromConfig those = this;
        Comparator cpt = new Comparator() {

            public int compare(Object o1, Object o2) {
                Initializer orgi = (Initializer) o1;
                Initializer targ = (Initializer) o2;
                int vo = Integer.parseInt(those.getDescriptor(orgi.getName()).getOnStartup());
                int vt = Integer.parseInt(those.getDescriptor(targ.getName()).getOnStartup());
                if (vo == vt) vt++;
                return vo - vt;
            }

            public boolean equals(Object obj) {
                if (null == obj || !(obj instanceof Comparator)) return false;
                Comparator target = (Comparator) obj;
                if (target.hashCode() == this.hashCode()) return true;
                return false;
            }
        };
        TreeSet sd = new TreeSet(cpt);
        sd.addAll(inits.values());
        return sd;
    }

    public void registerInitializer(String name, Initializer init) {
        inits.put(name, init);
    }
}
