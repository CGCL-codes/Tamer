package gnu.java.beans.encoder;

import java.util.HashMap;
import gnu.java.beans.encoder.elements.ArrayInstantiation;
import gnu.java.beans.encoder.elements.Array_Get;
import gnu.java.beans.encoder.elements.Array_Set;
import gnu.java.beans.encoder.elements.ClassResolution;
import gnu.java.beans.encoder.elements.Element;
import gnu.java.beans.encoder.elements.List_Get;
import gnu.java.beans.encoder.elements.List_Set;
import gnu.java.beans.encoder.elements.MethodInvocation;
import gnu.java.beans.encoder.elements.NullObject;
import gnu.java.beans.encoder.elements.ObjectInstantiation;
import gnu.java.beans.encoder.elements.ObjectReference;
import gnu.java.beans.encoder.elements.PrimitiveInstantiation;
import gnu.java.beans.encoder.elements.StaticFieldAccess;
import gnu.java.beans.encoder.elements.StaticMethodInvocation;
import gnu.java.beans.encoder.elements.StringReference;

/**
 * This class is a {@link ScannerState} implementation that creates
 * suitable {@link gnu.java.beans.encoder.elements.Element} instances
 * for each transition variant.
 * 
 * <p>Furthermore it can optionally skip a certain number of child
 * elements. The algorithm can cope with the fact that one 
 * <code>GenericScannerState</code> instance may be called at
 * different levels of recursions.</p>
 * 
 * @author Robert Schuster (robertschuster@fsfe.org)
 */
class GenericScannerState extends ScannerState {

    private int skipElements, initialSkipElements;

    final Root root;

    HashMap skipValues;

    GenericScannerState(Root newRoot) {
        root = newRoot;
    }

    GenericScannerState(Root root, int skipElements) {
        this(root);
        this.skipElements = initialSkipElements = skipElements;
        if (skipElements > 0) skipValues = new HashMap();
    }

    protected void enterImpl(Context ctx) {
        if (skipValues != null) {
            Integer skip = (Integer) skipValues.get(ctx);
            if (skip == null) {
                skip = Integer.valueOf(initialSkipElements);
                skipValues.put(ctx, skip);
            }
            skipElements = skip.intValue();
        }
    }

    void methodInvocation(String methodName) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new MethodInvocation(methodName));
    }

    void staticMethodInvocation(String className, String methodName) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new StaticMethodInvocation(className, methodName));
    }

    void staticFieldAccess(String className, String fieldName) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new StaticFieldAccess(className, fieldName));
    }

    void classResolution(String className) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new ClassResolution(className));
    }

    void objectInstantiation(String className, ObjectId objectId) {
        if (skipValues != null && skipElements > 0) return;
        Element elem = new ObjectInstantiation(className);
        elem.initId(objectId);
        root.addChild(elem);
    }

    void primitiveInstantiation(String primitiveName, String valueAsString) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new PrimitiveInstantiation(primitiveName, valueAsString));
    }

    void objectArrayInstantiation(String arrayClassName, String lengthAsString, ObjectId objectId) {
        if (skipValues != null && skipElements > 0) return;
        Element elem = new ArrayInstantiation(arrayClassName, lengthAsString);
        elem.initId(objectId);
        root.addChild(elem);
    }

    void primitiveArrayInstantiation(String arrayClassName, String lengthAsString, ObjectId objectId) {
        objectArrayInstantiation(arrayClassName, lengthAsString, objectId);
    }

    void arraySet(String indexAsString) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new Array_Set(indexAsString));
    }

    void arrayGet(String indexAsString) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new Array_Get(indexAsString));
    }

    void listGet() {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new List_Get());
    }

    void listSet() {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new List_Set());
    }

    void nullObject() {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new NullObject());
    }

    void stringReference(String string) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new StringReference(string));
    }

    void objectReference(ObjectId id) {
        if (skipValues != null && skipElements > 0) return;
        root.addChild(new ObjectReference(id));
    }

    void end() {
        if (skipValues != null) {
            if (skipElements > 0) skipElements--; else {
                root.end();
            }
            skipValues.put(context(), Integer.valueOf(skipElements));
        } else root.end();
    }

    void enter() {
    }
}
