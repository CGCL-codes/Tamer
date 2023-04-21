package org.sinaxe.components;

import org.sinaxe.*;
import org.sinaxe.util.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.Component;

public class YMLMappingEditor implements SinaxeComponent, SinaxeDataTypes {

    private SinaxeVariable contextVariable;

    private SinaxeVariable mappingcontextVariable;

    private SinaxeVariable sourcemappingcontextVariable;

    private SinaxeVariable destmappingcontextVariable;

    private SinaxeVariable pathidVariable;

    private SinaxeVariable filterVariable;

    private SinaxeVariable newnameVariable;

    private SinaxeQuery sourcemappingsQuery;

    private SinaxeQuery destmappingsQuery;

    private SinaxeQuery contextpathQuery;

    private SinaxeQuery sourcenodenameQuery;

    private SinaxeQuery destnodenameQuery;

    private SinaxeSubscription sourcenodenameSubscription;

    private SinaxeSubscription destnodenameSubscription;

    private SinaxeOutPort addsourcemappingOutPort;

    private SinaxeOutPort adddestmappingOutPort;

    private SinaxeOutPort updatesourcemappingnamesOutPort;

    private SinaxeOutPort updatedestmappingnamesOutPort;

    private SinaxeInPort loadInPort;

    private SinaxeInPort setsourceInPort;

    private SinaxeInPort setdestInPort;

    private SinaxeInPort createcurrentcontextInPort;

    private ArrayList sourceMappings = new ArrayList();

    private ArrayList destMappings = new ArrayList();

    private ArrayList sourcePath = new ArrayList();

    private ArrayList destPath = new ArrayList();

    public YMLMappingEditor() {
    }

    protected Object findClosestMappingContext(Object context, List sourcePath, List destPath) {
        boolean changed = true;
        Object currentContext = context;
        List mappings;
        while (changed) {
            changed = false;
            if (!sourcePath.isEmpty()) {
                mappings = querySourceMappings(currentContext, sourcePath.get(0));
                if (!mappings.isEmpty()) {
                    sourcePath.remove(0);
                    currentContext = mappings;
                    changed = true;
                }
            }
            if (!destPath.isEmpty()) {
                mappings = queryDestMappings(currentContext, destPath.get(0));
                if (!mappings.isEmpty()) {
                    destPath.remove(0);
                    currentContext = mappings;
                    changed = true;
                }
            }
        }
        if (currentContext instanceof List) return ((List) currentContext).get(0);
        return currentContext;
    }

    protected Object createMappings(Object context, List sourcePath, List destPath) {
        Object currentContext = context;
        while (!sourcePath.isEmpty() || !destPath.isEmpty()) {
            if (!sourcePath.isEmpty()) {
                Object pathId = sourcePath.remove(0);
                pathidVariable.setValue(pathId);
                addsourcemappingOutPort.send(currentContext);
                List mappings = querySourceMappings(currentContext, pathId);
                if (mappings.isEmpty()) {
                    SinaxeErrorHandler.error("Event to 'addsourcemapping' failed to " + "create the mapping!!!");
                    return null;
                }
                currentContext = mappings;
            }
            if (!destPath.isEmpty()) {
                Object pathId = destPath.remove(0);
                pathidVariable.setValue(pathId);
                adddestmappingOutPort.send(currentContext);
                List mappings = queryDestMappings(currentContext, pathId);
                if (mappings.isEmpty()) {
                    SinaxeErrorHandler.error("Event to 'adddestmapping' failed to " + "create the mapping!!!");
                    return null;
                }
                currentContext = mappings;
            }
        }
        if (currentContext instanceof List) return ((List) currentContext).get(0);
        return currentContext;
    }

    protected void createCurrentContext() {
        if (mappingcontextVariable.getValue() == null && contextVariable.getValue() != null) {
            ArrayList sourcePath = new ArrayList(this.sourcePath);
            ArrayList destPath = new ArrayList(this.destPath);
            Object context = findClosestMappingContext(contextVariable.getValue(), sourcePath, destPath);
            context = createMappings(context, sourcePath, destPath);
            sourceMappings.add(context);
            destMappings.add(context);
            mappingcontextVariable.setValue(context);
        }
    }

    protected void load(Object context) {
        contextVariable.setValue(context);
        sourceMappings.clear();
        sourceMappings.add(context);
        destMappings.clear();
        destMappings.add(context);
    }

    protected ArrayList getCommonMappings() {
        ArrayList commonMappings = new ArrayList();
        for (int i = 0; i < sourceMappings.size(); i++) {
            for (int j = 0; j < destMappings.size(); j++) {
                if (sourceMappings.get(i) == destMappings.get(j)) commonMappings.add(sourceMappings.get(i));
            }
        }
        if (commonMappings.isEmpty()) return null;
        return commonMappings;
    }

    protected List findDestMappings(Object context) {
        ArrayList newMappings = new ArrayList();
        List mappings = destmappingsQuery.getNodes(context);
        if (mappings != null && !mappings.isEmpty()) {
            newMappings.addAll(mappings);
            newMappings.addAll(findDestMappings(mappings));
        }
        return newMappings;
    }

    protected List findSourceMappings(Object context) {
        ArrayList newMappings = new ArrayList();
        List mappings = sourcemappingsQuery.getNodes(context);
        if (mappings != null && !mappings.isEmpty()) {
            newMappings.addAll(mappings);
            newMappings.addAll(findSourceMappings(mappings));
        }
        return newMappings;
    }

    protected void setSource(Object context) {
        sourceMappings.clear();
        sourceMappings.add(contextVariable.getValue());
        sourceMappings.addAll(findDestMappings(contextVariable.getValue()));
        sourcePath.clear();
        List contextPath = contextpathQuery.getNodes(context);
        for (int i = 0; i < contextPath.size(); i++) {
            Object pathId = contextPath.get(i);
            if (!sourceMappings.isEmpty()) downSource(pathId);
            sourcePath.add(pathId);
        }
        sourcemappingcontextVariable.setValue(sourceMappings);
        mappingcontextVariable.setValue(getCommonMappings());
        sourcenodenameSubscription.getString(context);
        sourcenodenameSubscription.addSinaxeSubscriptionListener(new DefaultSinaxeSubscriptionListener() {

            public void valueChanged(SinaxeSubscriptionEvent e) {
                String newName = e.getNewStringValue();
                newnameVariable.setValue(newName);
                updatesourcemappingnamesOutPort.send(sourcemappingcontextVariable.getValue());
            }
        });
    }

    protected List querySourceMappings(Object context, Object pathId) {
        if (pathId != null) {
            pathidVariable.setValue(pathId);
            filterVariable.setValue(Boolean.TRUE);
        }
        List mappings = sourcemappingsQuery.getNodes(context);
        if (pathId != null) filterVariable.setValue(Boolean.FALSE);
        return mappings;
    }

    protected void downSource(Object pathId) {
        List mappings = querySourceMappings(sourceMappings, pathId);
        sourceMappings.clear();
        if (mappings != null && !mappings.isEmpty()) {
            sourceMappings.addAll(mappings);
            sourceMappings.addAll(findDestMappings(mappings));
        }
    }

    protected void setDest(Object context) {
        destMappings.clear();
        destMappings.add(contextVariable.getValue());
        destMappings.addAll(findSourceMappings(contextVariable.getValue()));
        destPath.clear();
        List contextPath = contextpathQuery.getNodes(context);
        for (int i = 0; i < contextPath.size(); i++) {
            Object pathId = contextPath.get(i);
            if (!destMappings.isEmpty()) downDest(pathId);
            destPath.add(pathId);
        }
        destmappingcontextVariable.setValue(destMappings);
        mappingcontextVariable.setValue(getCommonMappings());
        destnodenameSubscription.getString(context);
        destnodenameSubscription.addSinaxeSubscriptionListener(new DefaultSinaxeSubscriptionListener() {

            public void valueChanged(SinaxeSubscriptionEvent e) {
                String newName = e.getNewStringValue();
                newnameVariable.setValue(newName);
                updatedestmappingnamesOutPort.send(destmappingcontextVariable.getValue());
            }
        });
    }

    protected List queryDestMappings(Object context, Object pathId) {
        if (pathId != null) {
            pathidVariable.setValue(pathId);
            filterVariable.setValue(Boolean.TRUE);
        }
        List mappings = destmappingsQuery.getNodes(context);
        if (pathId != null) filterVariable.setValue(Boolean.FALSE);
        return mappings;
    }

    protected void downDest(Object pathId) {
        List mappings = queryDestMappings(destMappings, pathId);
        destMappings.clear();
        if (mappings != null && !mappings.isEmpty()) {
            destMappings.addAll(mappings);
            destMappings.addAll(findSourceMappings(mappings));
        }
    }

    public void sinaxeInit(SinaxeRuntime runtime, SinaxeProperty properties) throws SinaxeException {
        contextVariable = runtime.registerVariable("context");
        mappingcontextVariable = runtime.registerVariable("mappingcontext");
        sourcemappingcontextVariable = runtime.registerVariable("sourcemappingcontext");
        destmappingcontextVariable = runtime.registerVariable("destmappingcontext");
        pathidVariable = runtime.registerVariable("pathid");
        filterVariable = runtime.registerVariable("filter");
        filterVariable.setValue(Boolean.FALSE);
        newnameVariable = runtime.registerVariable("newname");
        sourcemappingsQuery = runtime.getQuery("sourcemappings", true);
        destmappingsQuery = runtime.getQuery("destmappings", true);
        contextpathQuery = runtime.getQuery("contextpath", true);
        sourcenodenameQuery = runtime.getQuery("sourcenodename", true);
        destnodenameQuery = runtime.getQuery("destnodename", true);
        sourcenodenameSubscription = sourcenodenameQuery.newSubscription();
        destnodenameSubscription = destnodenameQuery.newSubscription();
        addsourcemappingOutPort = runtime.registerOutPort("addsourcemapping", DT_ANY);
        adddestmappingOutPort = runtime.registerOutPort("adddestmapping", DT_ANY);
        updatesourcemappingnamesOutPort = runtime.registerOutPort("updatesourcemappingnames", DT_ANY);
        updatedestmappingnamesOutPort = runtime.registerOutPort("updatedestmappingnames", DT_ANY);
        loadInPort = runtime.registerInPort("load", DT_CONTEXT | DT_NULL);
        loadInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                load(data);
            }
        });
        setsourceInPort = runtime.registerInPort("setsource", DT_CONTEXT | DT_NULL);
        setsourceInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                setSource(data);
            }
        });
        setdestInPort = runtime.registerInPort("setdest", DT_CONTEXT | DT_NULL);
        setdestInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                setDest(data);
            }
        });
        createcurrentcontextInPort = runtime.registerInPort("createcurrentcontext", DT_ANY);
        createcurrentcontextInPort.addListener(new SinaxePortListener() {

            public void sinaxePortEvent(SinaxeInPort port, Object data) {
                createCurrentContext();
            }
        });
    }

    public void sinaxeExit() throws SinaxeException {
    }

    public Component sinaxeGetComponent() {
        return null;
    }

    public String sinaxeGetDocumentation() {
        return null;
    }

    public Object sinaxePointToContext(java.awt.Point source) {
        return null;
    }
}
