package org.starmx.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.starmx.Scope;
import org.starmx.StarMXContext;
import org.starmx.config.BeanInfo;
import org.starmx.config.MBeanInfo;
import org.starmx.config.ObjectMapping;
import org.starmx.config.ProcessInfo;
import org.starmx.core.ProcessConfig;
import org.starmx.naming.LookupException;
import org.starmx.naming.ObjectLocatorFactory;
import org.starmx.util.ResourceFinder;

public class ProcessConfigImpl implements ProcessConfig {

    private ProcessInfo processInfo;

    private Scope processScope;

    private ObjectLocatorFactory objectLocatorFactory;

    private Logger log;

    public ProcessConfigImpl(ProcessInfo processInfo, ProcessScope processScope) {
        this.processInfo = processInfo;
        this.processScope = processScope;
        objectLocatorFactory = StarMXContext.getDefault().getObjectLocatorFactory();
        log = Logger.getLogger("org.starmx.process." + processInfo.getId());
    }

    public ProcessInfo getProcessInfo() {
        return processInfo;
    }

    public Object getAnchorObject(String mappingName) throws LookupException {
        ObjectMapping mapping = processInfo.getObjectMapping(mappingName);
        if (mapping != null) {
            return objectLocatorFactory.getObjectLocator(mapping.getRefType()).lookup(mapping.getRef());
        }
        throw new LookupException("Object not found: " + mappingName);
    }

    public Map<String, Object> getAllAnchorObjects() throws LookupException {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        for (ObjectMapping mapping : processInfo.getObjectMappings()) {
            Object mappedObj = objectLocatorFactory.getObjectLocator(mapping.getRefType()).lookup(mapping.getRef());
            objectMap.put(mapping.getName(), mappedObj);
        }
        return objectMap;
    }

    private Class<?> getAnchorObjectInterface(String mappingName) {
        ObjectMapping mapping = processInfo.getObjectMapping(mappingName);
        if (mapping != null) {
            Object info = mapping.getRefObjectInfo();
            switch(mapping.getRefType()) {
                case MBean:
                    MBeanInfo mbeanInfo = (MBeanInfo) info;
                    if (mbeanInfo.getMbeanInterface() == null) {
                        try {
                            objectLocatorFactory.getObjectLocator(mapping.getRefType()).lookup(mapping.getRef()).getClass();
                        } catch (LookupException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return mbeanInfo.getMbeanInterface();
                case Bean:
                    return ((BeanInfo) info).getClazz();
                default:
                    throw new RuntimeException("Not supported for : " + mapping.getRefType());
            }
        }
        throw new RuntimeException("Object not found: " + mappingName);
    }

    public String getPolicyFileContent() {
        String content = readPolicyFile();
        for (ObjectMapping mapping : processInfo.getObjectMappings()) {
            if (mapping.getRefType() == ObjectMapping.Type.MBean) {
                String classNameRegEx = mapping.getName() + "@StarMX";
                Pattern ptr = Pattern.compile(classNameRegEx);
                Matcher m = ptr.matcher(content);
                if (m.find()) {
                    String mbeanClassName = getAnchorObjectInterface(mapping.getName()).getName();
                    if (mbeanClassName.charAt(0) == '$') mbeanClassName = '\\' + mbeanClassName;
                    content = m.replaceAll(mbeanClassName);
                }
            }
        }
        return content;
    }

    private String readPolicyFile() {
        String fileName = processInfo.getPolicyFile();
        if (fileName == null) throw new RuntimeException("No policy file specified for policy: " + processInfo.getId());
        InputStream ins = ResourceFinder.getResourceAsStream(fileName);
        if (ins == null) throw new RuntimeException("Can not find file <" + fileName + "> for policy: " + processInfo.getId());
        BufferedReader input = new BufferedReader(new InputStreamReader(ins));
        StringBuilder contents = new StringBuilder();
        String line = null;
        try {
            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            }
        }
        return contents.toString();
    }

    public Scope getProcessScope() {
        return processScope;
    }

    public Scope getStarMXScope() {
        return StarMXContext.getDefault().getStarmxScope();
    }

    public Logger getLogger() {
        return log;
    }
}
