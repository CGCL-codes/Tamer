package org.kopsox.environment.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.kopsox.comm.listener.Observer;
import org.kopsox.data.ValueDescriptor;
import org.kopsox.environment.proxy.EnvironmentPropertiesEnum;
import org.kopsox.environment.proxy.KopsoxRegistry;
import org.kopsox.general.priority.impl.PriorityImpl;
import org.kopsox.util.comm.ObserverTreeSet;
import org.kopsox.util.validation.ValidatorSet;
import org.kopsox.util.xml.data.XMLValueDescriptor;
import org.kopsox.util.xml.data.impl.NameNamespaceKey;
import org.kopsox.util.xml.data.impl.NamePriorityDescriptor;
import org.kopsox.util.xml.parser.XMLParserFactory;
import org.kopsox.util.xml.parser.exception.KopsoxXMLParserException;
import org.kopsox.util.xml.parser.impl.ValueXMLParser;
import org.kopsox.validation.Validator;

/**
 * @author Konrad Renner - 15.10.2010
 *
 */
public class ValueEnvironmentImpl implements org.kopsox.environment.ValueEnvironment {

    private final Map<NameNamespaceKey, XMLValueDescriptor> values;

    private volatile boolean initialized;

    private ValueXMLParser parser;

    public ValueEnvironmentImpl() {
        this.values = new HashMap<NameNamespaceKey, XMLValueDescriptor>();
        this.initialized = false;
        this.parser = null;
    }

    @Override
    public synchronized boolean initialize(final Properties properties) {
        if (!this.initialized && properties != null) {
            String pathToXML = properties.getProperty(EnvironmentPropertiesEnum.PATHTO_VALUE_XML.toString());
            if (pathToXML == null) {
                return false;
            }
            try {
                this.parser = XMLParserFactory.getXMLParser(ValueXMLParser.class, pathToXML);
                this.parser.parseXML();
                List<XMLValueDescriptor> descriptors = this.parser.getValueDescriptors();
                addXMLValueDescriptors(descriptors);
                this.initialized = true;
            } catch (KopsoxXMLParserException e) {
                e.printStackTrace();
                return false;
            }
        }
        return this.initialized;
    }

    private final synchronized void addXMLValueDescriptors(List<XMLValueDescriptor> descriptors) {
        for (XMLValueDescriptor desc : descriptors) {
            NameNamespaceKey key = new NameNamespaceKey(desc.getDescriptor().getName(), desc.getDescriptor().getNamespace());
            if (!this.values.containsKey(key)) {
                values.put(key, desc);
            }
        }
    }

    @Override
    public ObserverTreeSet getObserversForValue(String name, String namespace) {
        XMLValueDescriptor desc = getXMLValueDescription(name, namespace);
        ObserverTreeSet ret = new ObserverTreeSet();
        if (desc == null) {
            return ret;
        }
        Collection<NamePriorityDescriptor> observers = desc.getObservers();
        for (NamePriorityDescriptor npdesc : observers) {
            try {
                Class<?> clazz = Class.forName(npdesc.getName());
                Observer obs = (Observer) clazz.newInstance();
                try {
                    obs.setPriority(new PriorityImpl(Integer.parseInt(npdesc.getPriority())));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                ret.add(obs);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public ValidatorSet getValidatorsForValue(String name, String namespace) {
        XMLValueDescriptor desc = getXMLValueDescription(name, namespace);
        ValidatorSet ret = new ValidatorSet();
        if (desc == null) {
            return ret;
        }
        Collection<NamePriorityDescriptor> validators = desc.getValidators();
        for (NamePriorityDescriptor npdesc : validators) {
            Validator val = KopsoxRegistry.INSTANCE.getValidatorEnvironment().getValidator(npdesc.getName());
            if (val != null) {
                try {
                    val.setPriority(new PriorityImpl(Integer.parseInt(npdesc.getPriority())));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                ret.add(val);
            }
        }
        return ret;
    }

    @Override
    public ValueDescriptor getValueDescription(String name, String namespace) {
        XMLValueDescriptor desc = getXMLValueDescription(name, namespace);
        if (desc == null) {
            return null;
        }
        return desc.getDescriptor();
    }

    @Override
    public String getTypeNameFromValue(String name, String namespace) {
        XMLValueDescriptor desc = getXMLValueDescription(name, namespace);
        if (desc == null) {
            return null;
        }
        return desc.getTypeFromValue();
    }

    @Override
    public String getInitialDataFromValue(String name, String namespace) {
        XMLValueDescriptor desc = getXMLValueDescription(name, namespace);
        if (desc == null) {
            return null;
        }
        return desc.getInitialValue();
    }

    private final XMLValueDescriptor getXMLValueDescription(String name, String namespace) {
        if (!this.initialized) {
            return null;
        }
        NameNamespaceKey key = new NameNamespaceKey(name, namespace);
        if (!this.values.containsKey(key)) {
            addXMLValueDescriptors(this.parser.getValueDescriptors(namespace));
        }
        if (this.values.containsKey(key)) {
            return this.values.get(key);
        }
        return null;
    }
}
