package com.bitgate.util.services.engine.tags.u;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;
import org.w3c.dom.Node;
import com.bitgate.util.db.QueryContainer;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.persistence.GlobalPersistence;
import com.bitgate.util.persistence.GlobalPersistentCache;
import com.bitgate.util.persistence.Persistence;
import com.bitgate.util.persistence.PersistentCache;
import com.bitgate.util.services.engine.DocumentTag;
import com.bitgate.util.services.engine.RenderEngine;
import com.bitgate.util.services.engine.TagInspector;
import com.bitgate.util.services.engine.tags.ElementDescriber;
import com.bitgate.util.sort.KeySizeSortAscending;
import com.bitgate.util.sort.KeySizeSortDescending;
import com.bitgate.util.sort.KeySortAscending;
import com.bitgate.util.sort.KeySortDescending;

/**
 * This element allows for functionality to step through lists of variables ranging from <code>Vector</code> elements, to
 * <code>HashMap</code> elements, to <code>SQL ResultSet</code> objects.
 * 
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/services/engine/tags/u/Foreach.java#58 $
 */
public class Foreach extends DocumentTag implements ElementDescriber {

    private String varName, varType, destVariable, iteratorVar, scope, trimVar, sort;

    public Foreach() {
    }

    public ArrayList getSubElements() {
        return new ArrayList();
    }

    public void prepareTag(Node n) {
        super.prepareTag(n);
        varName = TagInspector.get(n, "var");
        varType = TagInspector.get(n, "type");
        destVariable = TagInspector.get(n, "dest");
        iteratorVar = TagInspector.get(n, "iterator");
        scope = TagInspector.get(n, "scope");
        trimVar = TagInspector.get(n, "trim");
        sort = TagInspector.get(n, "sort");
    }

    public StringBuffer render(RenderEngine c) {
        if (c.isCancelled()) {
            return new StringBuffer();
        }
        String logTime = null;
        if (c.getWorkerContext() != null) {
            logTime = c.getWorkerContext().getWorkerStart();
        }
        if (c.isBreakState() || !c.canRender("u")) {
            return new StringBuffer();
        }
        StringBuffer buffer = new StringBuffer();
        varName = TagInspector.processElement(varName, c);
        varType = TagInspector.processElement(varType, c);
        destVariable = TagInspector.processElement(destVariable, c);
        iteratorVar = TagInspector.processElement(iteratorVar, c);
        scope = TagInspector.processElement(scope, c);
        trimVar = TagInspector.processElement(trimVar, c);
        sort = TagInspector.processElement(sort, c);
        int iterations = 1;
        boolean trim = true;
        if (varName == null || varType == null || destVariable == null) {
            return new StringBuffer("<!-- Variable name, type, and/or destination is missing from requested Foreach element. -->");
        }
        if (trimVar != null && trimVar.equalsIgnoreCase("false")) {
            trim = false;
        }
        c.getDocumentEngine().newThis();
        String currentHost = null;
        if (c.getVendContext() != null && c.getVendContext().getVend() != null) {
            currentHost = c.getVendContext().getVend().getVendName();
        }
        String currentVHost = null;
        if (c.getWorkerContext() != null) {
            currentVHost = c.getWorkerContext().getLabel();
        }
        if (varType.equalsIgnoreCase("vector")) {
            Vector listVariable = c.getVariableContainer().getVector(varName);
            if (listVariable == null) {
                c.getDocumentEngine().deleteThis();
                return new StringBuffer();
            }
            Debug.user(logTime, "Rendering foreach with vector/list type, size of vector=" + listVariable.size());
            for (Enumeration e = listVariable.elements(); e.hasMoreElements(); ) {
                if (c.isCancelled()) {
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                if (c.isStopState()) {
                    c.clearStopState();
                    break;
                }
                if (iteratorVar != null && !iteratorVar.equals("")) {
                    if (c.isProtectedVariable(iteratorVar)) {
                        c.setExceptionState(true, "Attempted to modify a read-only variable '" + iteratorVar + "'");
                        c.getDocumentEngine().deleteThis();
                        return new StringBuffer();
                    }
                    c.getVariableContainer().setVariable(iteratorVar, Integer.toString(iterations++));
                }
                String elem = (String) e.nextElement();
                StringBuffer result = null;
                if (trim) {
                    elem = elem.trim();
                }
                if (c.isProtectedVariable(destVariable)) {
                    c.setExceptionState(true, "Attempted to modify a read-only variable '" + destVariable + "'");
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                c.getVariableContainer().setVariable(destVariable, elem);
                boolean prevEchoMode = c.getEchoMode();
                c.setEchoMode(false);
                if ((result = TagInspector.processBody(new DocumentTag(thisNode), c)) == null) {
                    c.setEchoMode(prevEchoMode);
                    c.getDocumentEngine().deleteThis();
                    return buffer;
                }
                c.setEchoMode(prevEchoMode);
                buffer.append(result);
            }
        } else if (varType.equalsIgnoreCase("stack")) {
            Stack listObject = null;
            if (scope != null && scope.equalsIgnoreCase("session")) {
                PersistentCache.getDefault().newPersistence(Session.getCurrentStaticSessionID(c), Session.getStaticSessionPool(c));
                Persistence ps = PersistentCache.getDefault().get(Session.getCurrentStaticSessionID(c));
                listObject = ps.getStack(varName);
            } else if (scope != null && scope.equalsIgnoreCase("global")) {
                GlobalPersistentCache.getDefault().newPersistence(null, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(null);
                listObject = ps.getStack(varName);
            } else if (scope != null && scope.equalsIgnoreCase("server")) {
                GlobalPersistentCache.getDefault().newPersistence(currentHost, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(currentHost);
                listObject = ps.getStack(varName);
            } else if (scope != null && scope.equalsIgnoreCase("local")) {
                GlobalPersistentCache.getDefault().newPersistence(currentVHost, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(currentVHost);
                listObject = ps.getStack(varName);
            }
            if (listObject == null) {
                listObject = c.getVariableContainer().getStack(varName);
            }
            if (listObject == null) {
                c.getDocumentEngine().deleteThis();
                return new StringBuffer("<!-- Stack object is null or does not exist. -->");
            }
            Debug.user(logTime, "Rendering foreach with stack size of vector=" + listObject.size());
            Object obj = null;
            try {
                if (c.isCancelled()) {
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                while ((obj = listObject.pop()) != null) {
                    if (c.isStopState()) {
                        c.clearStopState();
                        break;
                    }
                    if (iteratorVar != null && !iteratorVar.equals("")) {
                        if (c.isProtectedVariable(iteratorVar)) {
                            c.setExceptionState(true, "Attempted to modify a read-only variable '" + iteratorVar + "'");
                            c.getDocumentEngine().deleteThis();
                            return new StringBuffer();
                        }
                        c.getVariableContainer().setVariable(iteratorVar, Integer.toString(iterations++));
                    }
                    if (!(obj instanceof String)) {
                        continue;
                    }
                    String elem = (String) obj;
                    StringBuffer result = null;
                    if (trim) {
                        elem = elem.trim();
                    }
                    if (c.isProtectedVariable(destVariable)) {
                        c.setExceptionState(true, "Attempted to modify a read-only variable '" + destVariable + "'");
                        c.getDocumentEngine().deleteThis();
                        return new StringBuffer();
                    }
                    c.getVariableContainer().setVariable(destVariable, elem);
                    boolean prevEchoMode = c.getEchoMode();
                    c.setEchoMode(false);
                    if ((result = TagInspector.processBody(new DocumentTag(thisNode), c)) == null) {
                        c.setEchoMode(prevEchoMode);
                        c.getDocumentEngine().deleteThis();
                        return buffer;
                    }
                    c.setEchoMode(prevEchoMode);
                    buffer.append(result);
                }
            } catch (EmptyStackException e) {
            }
        } else if (varType.equalsIgnoreCase("array")) {
            ArrayList listObject = null;
            if (scope != null && scope.equalsIgnoreCase("session")) {
                PersistentCache.getDefault().newPersistence(Session.getCurrentStaticSessionID(c), Session.getStaticSessionPool(c));
                Persistence ps = PersistentCache.getDefault().get(Session.getCurrentStaticSessionID(c));
                listObject = ps.getArray(varName);
            } else if (scope != null && scope.equalsIgnoreCase("global")) {
                GlobalPersistentCache.getDefault().newPersistence(null, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(null);
                listObject = ps.getArray(varName);
            } else if (scope != null && scope.equalsIgnoreCase("server")) {
                GlobalPersistentCache.getDefault().newPersistence(currentHost, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(currentHost);
                listObject = ps.getArray(varName);
            } else if (scope != null && scope.equalsIgnoreCase("local")) {
                GlobalPersistentCache.getDefault().newPersistence(currentVHost, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(currentVHost);
                listObject = ps.getArray(varName);
            }
            if (listObject == null) {
                listObject = c.getVariableContainer().getArray(varName);
            }
            if (listObject == null) {
                c.getDocumentEngine().deleteThis();
                return new StringBuffer("<!-- Stack object is null or does not exist. -->");
            }
            int listObjectSize = listObject.size();
            Debug.user(logTime, "Rendering foreach with array size of list=" + listObjectSize);
            if (sort != null && !sort.equals("") && (sort.equalsIgnoreCase("asc") || sort.equalsIgnoreCase("desc") || sort.equalsIgnoreCase("ascending") || sort.equalsIgnoreCase("descending") || sort.equalsIgnoreCase("keyasc") || sort.equalsIgnoreCase("keydesc") || sort.equalsIgnoreCase("keyascending") || sort.equalsIgnoreCase("keydescending"))) {
                String[] arrayList = new String[listObject.size()];
                for (int i = 0; i < listObject.size(); i++) {
                    arrayList[i] = (String) listObject.get(i);
                }
                if (sort.equalsIgnoreCase("asc") || sort.equalsIgnoreCase("ascending")) {
                    Arrays.sort(arrayList, new KeySortAscending());
                } else if (sort.equalsIgnoreCase("desc") || sort.equalsIgnoreCase("descending")) {
                    Arrays.sort(arrayList, new KeySortDescending());
                } else if (sort.equalsIgnoreCase("keyasc") || sort.equalsIgnoreCase("keyascending")) {
                    Arrays.sort(arrayList, new KeySizeSortAscending());
                } else if (sort.equalsIgnoreCase("keydesc") || sort.equalsIgnoreCase("keydescending")) {
                    Arrays.sort(arrayList, new KeySizeSortDescending());
                }
                listObject = new ArrayList(arrayList.length);
                for (int i = 0; i < arrayList.length; i++) {
                    listObject.add(arrayList[i]);
                }
            }
            for (int i = 0; i < listObjectSize; i++) {
                if (c.isCancelled()) {
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                if (c.isStopState()) {
                    c.clearStopState();
                    break;
                }
                if (iteratorVar != null && !iteratorVar.equals("")) {
                    if (c.isProtectedVariable(iteratorVar)) {
                        c.setExceptionState(true, "Attempted to modify a read-only variable '" + iteratorVar + "'");
                        c.getDocumentEngine().deleteThis();
                        return new StringBuffer();
                    }
                    c.getVariableContainer().setVariable(iteratorVar, Integer.toString(iterations++));
                }
                String elem = (String) listObject.get(i);
                StringBuffer result = null;
                if (trim) {
                    elem = elem.trim();
                }
                if (c.isProtectedVariable(destVariable)) {
                    c.setExceptionState(true, "Attempted to modify a read-only variable '" + destVariable + "'");
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                c.getVariableContainer().setVariable(destVariable, elem);
                boolean prevEchoMode = c.getEchoMode();
                if ((result = TagInspector.processBody(new DocumentTag(thisNode), c)) == null) {
                    c.getDocumentEngine().deleteThis();
                    return buffer;
                }
                buffer.append(result);
            }
        } else if (varType.equalsIgnoreCase("list")) {
            LinkedList listObject = null;
            if (scope != null && scope.equalsIgnoreCase("session")) {
                PersistentCache.getDefault().newPersistence(Session.getCurrentStaticSessionID(c), Session.getStaticSessionPool(c));
                Persistence ps = PersistentCache.getDefault().get(Session.getCurrentStaticSessionID(c));
                listObject = ps.getList(varName);
            } else if (scope != null && scope.equalsIgnoreCase("global")) {
                GlobalPersistentCache.getDefault().newPersistence(null, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(null);
                listObject = ps.getList(varName);
            } else if (scope != null && scope.equalsIgnoreCase("server")) {
                GlobalPersistentCache.getDefault().newPersistence(currentHost, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(currentHost);
                listObject = ps.getList(varName);
            } else if (scope != null && scope.equalsIgnoreCase("local")) {
                GlobalPersistentCache.getDefault().newPersistence(currentVHost, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(currentVHost);
                listObject = ps.getList(varName);
            }
            if (listObject == null) {
                listObject = c.getVariableContainer().getList(varName);
            }
            if (listObject == null) {
                c.getDocumentEngine().deleteThis();
                return new StringBuffer("<!-- Stack object is null or does not exist. -->");
            }
            Debug.user(logTime, "Rendering foreach with stack size of list=" + listObject.size());
            Object obj = null;
            int pos = 0;
            do {
                if (c.isCancelled()) {
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                try {
                    obj = listObject.get(pos++);
                } catch (Exception e) {
                    break;
                }
                if (obj == null) {
                    break;
                }
                if (c.isStopState()) {
                    c.clearStopState();
                    break;
                }
                if (iteratorVar != null && !iteratorVar.equals("")) {
                    if (c.isProtectedVariable(iteratorVar)) {
                        c.setExceptionState(true, "Attempted to modify a read-only variable '" + iteratorVar + "'");
                        c.getDocumentEngine().deleteThis();
                        return new StringBuffer();
                    }
                    c.getVariableContainer().setVariable(iteratorVar, Integer.toString(iterations++));
                }
                String elem = (String) obj;
                StringBuffer result = null;
                if (trim) {
                    elem = elem.trim();
                }
                if (c.isProtectedVariable(destVariable)) {
                    c.setExceptionState(true, "Attempted to modify a read-only variable '" + destVariable + "'");
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                c.getVariableContainer().setVariable(destVariable, elem);
                boolean prevEchoMode = c.getEchoMode();
                c.setEchoMode(false);
                if ((result = TagInspector.processBody(new DocumentTag(thisNode), c)) == null) {
                    c.setEchoMode(prevEchoMode);
                    c.getDocumentEngine().deleteThis();
                    return buffer;
                }
                c.setEchoMode(prevEchoMode);
                buffer.append(result);
            } while (obj != null);
        } else if ((varType.equalsIgnoreCase("resultset") || varType.equalsIgnoreCase("results"))) {
            QueryContainer listSet = c.getVariableContainer().getContainer(varName);
            if (listSet == null) {
                c.getDocumentEngine().deleteThis();
                return new StringBuffer("<!-- ResultSet list object does not exist. -->");
            }
            if (destVariable == null || destVariable.equals("")) {
                destVariable = "currentvalue";
            }
            while (listSet.next()) {
                if (c.isCancelled()) {
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                if (c.isStopState()) {
                    c.clearStopState();
                    break;
                }
                StringBuffer result = new StringBuffer();
                if (listSet == null) {
                    c.getVariableContainer().removeContainer(destVariable);
                } else {
                    c.getVariableContainer().setContainer(destVariable, listSet);
                }
                c.getVariableContainer().setVariable(destVariable + ".type", "resultset");
                if (iteratorVar != null && !iteratorVar.equals("")) {
                    if (c.isProtectedVariable(iteratorVar)) {
                        c.setExceptionState(true, "Attempted to modify a read-only variable '" + iteratorVar + "'");
                        c.getDocumentEngine().deleteThis();
                        return new StringBuffer();
                    }
                    c.getVariableContainer().setVariable(iteratorVar, Integer.toString(iterations++));
                }
                boolean prevEchoMode = c.getEchoMode();
                c.setEchoMode(false);
                if ((result = TagInspector.processBody(new DocumentTag(thisNode), c)) == null) {
                    c.setEchoMode(prevEchoMode);
                    c.getDocumentEngine().deleteThis();
                    return buffer;
                }
                c.setEchoMode(prevEchoMode);
                buffer.append(result);
            }
            listSet.close();
        } else if ((varType.equalsIgnoreCase("hashmap") || varType.equalsIgnoreCase("keylist"))) {
            HashMap listVariables = null;
            if (scope != null && scope.equalsIgnoreCase("session")) {
                PersistentCache.getDefault().newPersistence(Session.getCurrentStaticSessionID(c), Session.getStaticSessionPool(c));
                Persistence ps = PersistentCache.getDefault().get(Session.getCurrentStaticSessionID(c));
                listVariables = ps.getHashMap(varName);
            } else if (scope != null && scope.equalsIgnoreCase("global")) {
                GlobalPersistentCache.getDefault().newPersistence(null, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(null);
                listVariables = ps.getHashMap(varName);
            } else if (scope != null && scope.equalsIgnoreCase("server")) {
                GlobalPersistentCache.getDefault().newPersistence(currentHost, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(currentHost);
                listVariables = ps.getHashMap(varName);
            } else if (scope != null && scope.equalsIgnoreCase("local")) {
                GlobalPersistentCache.getDefault().newPersistence(currentVHost, Session.getStaticSessionPool(c));
                GlobalPersistence ps = GlobalPersistentCache.getDefault().get(currentVHost);
                listVariables = ps.getHashMap(varName);
            }
            if (listVariables == null) {
                listVariables = c.getVariableContainer().getHashMap(varName);
                if (listVariables != null) {
                    Debug.user(logTime, "Retrieval of variable '" + varName + "': Size=" + listVariables.size());
                } else {
                    Debug.user(logTime, "Retrieval of variable '" + varName + "' yields no hashmap.");
                }
            }
            Debug.debug("ListVariables = '" + listVariables + "'");
            if (listVariables == null) {
                c.getDocumentEngine().deleteThis();
                return new StringBuffer();
            }
            String[] listKeys = new String[listVariables.size()];
            Iterator elements = listVariables.keySet().iterator();
            int listCounter = 0;
            while (elements.hasNext()) {
                String key = (String) elements.next();
                listKeys[listCounter++] = key;
            }
            if (sort != null && (sort.equalsIgnoreCase("asc") || sort.equalsIgnoreCase("ascending"))) {
                Arrays.sort(listKeys, new KeySortAscending());
            } else if (sort != null && (sort.equalsIgnoreCase("desc") || sort.equalsIgnoreCase("descending"))) {
                Arrays.sort(listKeys, new KeySortDescending());
            }
            for (int i = 0; i < listKeys.length; i++) {
                if (c.isCancelled()) {
                    c.getDocumentEngine().deleteThis();
                    return new StringBuffer();
                }
                if (c.isStopState()) {
                    c.clearStopState();
                    break;
                }
                String key = listKeys[i];
                Object val = listVariables.get(key);
                StringBuffer result = null;
                c.getVariableContainer().setVariable("currentkey", key);
                if (iteratorVar != null && !iteratorVar.equals("")) {
                    if (c.isProtectedVariable(iteratorVar)) {
                        c.setExceptionState(true, "Attempted to modify a read-only variable '" + iteratorVar + "'");
                        c.getDocumentEngine().deleteThis();
                        return new StringBuffer();
                    }
                    c.getVariableContainer().setVariable(iteratorVar, Integer.toString(iterations++));
                }
                if (val instanceof Vector) {
                    c.getVariableContainer().setVector("currentvalue", (Vector) val);
                    c.getVariableContainer().setVariable("currentvaluetype", "vector");
                } else if (val instanceof HashMap) {
                    c.getVariableContainer().setHashMap("currentvalue", (HashMap) val);
                    c.getVariableContainer().setVariable("currentvaluetype", "hashmap");
                } else {
                    if (val != null) {
                        if (trim) {
                            c.getVariableContainer().setVariable("currentvalue", ((String) val).trim());
                        } else {
                            c.getVariableContainer().setVariable("currentvalue", (String) val);
                        }
                    } else {
                        c.getVariableContainer().setVariable("currentvalue", "");
                        continue;
                    }
                }
                boolean prevEchoMode = c.getEchoMode();
                c.setEchoMode(false);
                if ((result = TagInspector.processBody(new DocumentTag(thisNode), c)) == null) {
                    c.setEchoMode(prevEchoMode);
                    c.getDocumentEngine().deleteThis();
                    return buffer;
                }
                c.setEchoMode(prevEchoMode);
                buffer.append(result);
            }
        } else {
            c.getDocumentEngine().deleteThis();
            return new StringBuffer("<!-- Variable '" + varName + "' is not of type '" + varType + "' -->");
        }
        c.getDocumentEngine().deleteThis();
        return buffer;
    }
}
