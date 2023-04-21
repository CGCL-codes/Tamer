package org.callbackparams.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Visitor;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.callbackparams.AdaptiveRule;
import org.callbackparams.internal.template.AdaptiveRulesPackager;
import org.callbackparams.internal.template.AdaptiveRulesPackager.VisitorParentGenerator;
import org.callbackparams.internal.template.TestrunCallbacks;
import org.callbackparams.support.ClassBytecodeBuilder;
import org.callbackparams.support.MethodHashKey;
import org.callbackparams.wrap.legacy.Wrapper;
import org.callbackparams.wrap.legacy.WrapperLegacySupport;

/**
 * This is a thread-safe Rebyter implementation.
 *
 * @author Henrik Kaipe
 */
class CallbackMethodProxyingRebyter implements CallbackTestClassReloader.Rebyter {

    private static final String testrunCallbacksClassName = TestrunCallbacks.class.getName();

    private static final String testrunCallbacksArgumentArrayFieldName = lookupArgumentArrayFieldName();

    private static final Type testrunCallbacksArgumentArrayType = Type.getType(new Object[0].getClass());

    private final ClassLoader templateClassLoader;

    private final Class adaptiveRuleSuperClass;

    /**
     * This is the class that will have its super-class changed to the
     * rebyted TestrunCallbacks class.
     * It will be set to a proper value during construction. The initial value
     * is just a convenient dummy-value that release the code from null-checks.
     */
    private Class topClassInRebytedPackage = getClass();

    /**
     * Keeps track of and performs all necessary constructor modifications.
     */
    private final TestrunCallbacksConstructors constructors2Modify;

    /**
     * The keys in this map are names of all the classes that need to be rebyted
     * in order to have their methods-with-callback-arguments proxied by a
     * methodName$callbackProxy() method.
     */
    private final NoargMethodProxyMap proxyMap;

    private final Class testClass;

    private boolean adaptiveRulesAreUsed = false;

    private final Map testMethodHash = new HashMap();

    private final Map syntheticVisitorParents = new HashMap();

    private final List callbackInjectionFields = new ArrayList();

    public CallbackMethodProxyingRebyter(Class testClass, CallbackRebyteInfo rebyteInfo) {
        this.testClass = testClass;
        this.templateClassLoader = testClass.getClassLoader();
        this.proxyMap = new NoargMethodProxyMap(testClass);
        setupClassesToRebyte(testClass, rebyteInfo);
        if (false == adaptiveRulesAreUsed) {
            testMethodHash.clear();
            syntheticVisitorParents.clear();
        }
        this.constructors2Modify = new TestrunCallbacksConstructors(topClassInRebytedPackage);
        this.adaptiveRuleSuperClass = rebyteInfo.adaptiveRuleSuperClass();
    }

    private static String lookupArgumentArrayFieldName() {
        Class desiredFieldType = new Object[0].getClass();
        final Field[] fields = TestrunCallbacks.class.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            final Field f = fields[i];
            final int modifiers = f.getModifiers();
            if (false == Modifier.isStatic(modifiers) && false == Modifier.isFinal(modifiers) && Modifier.isProtected(modifiers) && desiredFieldType == f.getType() && TestrunCallbacks.class == f.getDeclaringClass()) {
                return f.getName();
            }
        }
        throw new Error("Cannot find field with callback method arguments");
    }

    private void setupClassesToRebyte(Class clazz, CallbackRebyteInfo rebyteInfo) {
        if (Object.class.getClassLoader() == clazz.getClassLoader()) {
            return;
        }
        if (clazz.getPackage() == topClassInRebytedPackage.getPackage()) {
            this.topClassInRebytedPackage = clazz;
        }
        List adaptiveRules = new ArrayList();
        List testMethods = new ArrayList();
        final Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            final Field f = fields[i];
            if (rebyteInfo.isCallbackInjectField(f)) {
                this.callbackInjectionFields.add(f);
                this.topClassInRebytedPackage = clazz;
            }
            if (rebyteInfo.isAdaptiveRuleField(f)) {
                adaptiveRules.add(f);
                adaptiveRulesAreUsed = true;
            }
        }
        final Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            final Method m = methods[i];
            if (rebyteInfo.isCallbackProxiedMethod(m)) {
                rebyteInfo.putNoargProxyMethod(proxyMap.addMethodToBeProxied(m), m);
                this.topClassInRebytedPackage = clazz;
            }
            if (rebyteInfo.isTestMethod(m)) {
                testMethods.add(m);
                testMethodHash.put(MethodHashKey.getHashKey(m), m);
                this.topClassInRebytedPackage = clazz;
            }
        }
        if (false == testMethods.isEmpty() || false == adaptiveRules.isEmpty()) {
            VisitorParentGenerator generator = new VisitorParentGenerator(clazz, adaptiveRules, testMethods);
            syntheticVisitorParents.put(generator.getInterfaceName(), generator);
        }
        if (rebyteInfo.isExplicitTopClass(clazz)) {
            this.topClassInRebytedPackage = clazz;
            return;
        }
        setupClassesToRebyte(clazz.getSuperclass(), rebyteInfo);
    }

    public ClassLoader getParentClassLoader() {
        String topClassName = topClassInRebytedPackage.getName();
        for (ClassLoader parent = topClassInRebytedPackage.getClassLoader().getParent(); null != parent; parent = parent.getParent()) {
            try {
                parent.loadClass(topClassName);
            } catch (ClassNotFoundException expectedForProperParentLoader) {
                return parent;
            }
        }
        return null;
    }

    public byte[] newSyntheticClass(String className) {
        if (syntheticVisitorParents.containsKey(className)) {
            VisitorParentGenerator generator = (VisitorParentGenerator) syntheticVisitorParents.remove(className);
            return generator.generateByteCode();
        } else {
            return null;
        }
    }

    public byte[] rebyte(CallbackContext callbackContext, JavaClass templateClassDef) {
        final String className = templateClassDef.getClassName();
        if (AdaptiveRule.class.getName().equals(className)) {
            return rebyteAdaptiveRule();
        }
        if (AdaptiveRulesPackager.Visitor.class.getName().equals(className)) {
            return AdaptiveRulesPackager.generateRebytedVisitor(testClass, syntheticVisitorParents.keySet());
        }
        for (Iterator iterFields = callbackInjectionFields.iterator(); iterFields.hasNext(); ) {
            final Field f = (Field) iterFields.next();
            if (className.equals(f.getDeclaringClass().getName())) {
                callbackContext.addFieldToInject(f);
            }
        }
        final Class templateClass;
        try {
            templateClass = templateClassLoader.loadClass(className);
        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        }
        Map methodsToProxy = proxyMap.getProxyMethodsForClass(className);
        if (false == constructors2Modify.needsConstructorModifications(className) && methodsToProxy.isEmpty() && testMethodHash.isEmpty() && false == isAbstractWrapper(templateClass)) {
            return templateClassDef.getBytes();
        }
        ClassBytecodeBuilder cbb = ClassBytecodeBuilder.newInstance(templateClass);
        cbb.setPublic();
        if (false == testMethodHash.isEmpty()) {
            final org.apache.bcel.classfile.Method[] methods = templateClassDef.getMethods();
            InstructionFactory factory = cbb.getInstructionFactory();
            for (int i = 0; i < methods.length && false == testMethodHash.isEmpty(); ++i) {
                org.apache.bcel.classfile.Method m = methods[i];
                MethodHashKey methodHashKey = new MethodHashKey(className, m.getName(), m.getSignature());
                Method testMethod = (Method) testMethodHash.remove(methodHashKey);
                if (null != testMethod) {
                    cbb.prependMethod(m, AdaptiveRulesPackager.defineTestMethodDetourCall(factory, testMethod));
                }
            }
        }
        for (Iterator iterEntr = methodsToProxy.entrySet().iterator(); iterEntr.hasNext(); ) {
            final Map.Entry proxyEntr = (Map.Entry) iterEntr.next();
            proxyMethod(cbb, proxyEntr, callbackContext.addMethodParams((Method) proxyEntr.getValue()));
        }
        constructors2Modify.modifyConstructors(className, cbb);
        return cbb.getByteCode();
    }

    private void proxyMethod(ClassBytecodeBuilder cbb, Map.Entry proxyDef, int callbackArgumentIndexStart) {
        org.apache.bcel.classfile.Method oldMethod = cbb.getMethod((Method) proxyDef.getValue());
        InstructionFactory factory = cbb.getInstructionFactory();
        InstructionList il = new InstructionList();
        final MethodGen mg = new MethodGen(oldMethod.getAccessFlags(), oldMethod.getReturnType(), Type.NO_ARGS, null, (String) proxyDef.getKey(), factory.getClassGen().getClassName(), il, factory.getConstantPool());
        if (false == mg.isStatic()) {
            il.append(InstructionFactory.ALOAD_0);
        }
        Instruction pushCallbackArgumentsArray = factory.createGetField(testrunCallbacksClassName, testrunCallbacksArgumentArrayFieldName, testrunCallbacksArgumentArrayType);
        for (int i = 0, length = oldMethod.getArgumentTypes().length; i < length; ++i) {
            il.append(InstructionFactory.ALOAD_0);
            il.append(pushCallbackArgumentsArray);
            il.append(factory.createConstant(new Integer(callbackArgumentIndexStart + i)));
            il.append(InstructionFactory.AALOAD);
        }
        il.append(factory.createInvoke(factory.getClassGen().getClassName(), oldMethod.getName(), oldMethod.getReturnType(), oldMethod.getArgumentTypes(), oldMethod.isStatic() ? Constants.INVOKESTATIC : oldMethod.isPrivate() ? Constants.INVOKESPECIAL : Constants.INVOKEVIRTUAL));
        il.append(InstructionFactory.createReturn(oldMethod.getReturnType()));
        final List attributesToKeep = new ArrayList();
        Visitor annotationTransferer = new EmptyVisitor() {

            public void visitUnknown(Unknown obj) {
                mg.addAttribute(obj);
                attributesToKeep.remove(attributesToKeep.size() - 1);
            }
        };
        final Attribute[] attributes = oldMethod.getAttributes();
        for (int i = 0; i < attributes.length; ++i) {
            final Attribute a = attributes[i];
            attributesToKeep.add(a);
            a.accept(annotationTransferer);
        }
        mg.setMaxStack();
        cbb.addMethod(mg.getMethod());
        oldMethod.setAttributes((Attribute[]) attributesToKeep.toArray(new Attribute[attributesToKeep.size()]));
    }

    private byte[] rebyteAdaptiveRule() {
        ClassBytecodeBuilder cbb = rebuilderForClass(AdaptiveRule.class);
        if (null != adaptiveRuleSuperClass) {
            cbb.setNewSuperClass(adaptiveRuleSuperClass.getName());
        }
        return cbb.getByteCode();
    }

    private ClassBytecodeBuilder rebuilderForClass(Class classToRebyte) {
        try {
            return ClassBytecodeBuilder.newInstance(templateClassLoader.loadClass(classToRebyte.getName()));
        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        }
    }

    /**
     * Returns true if the specified class is an abstract implementation
     * of {@link Wrapper} and does not extend {@link WrapperLegacySupport}.
     */
    private static boolean isAbstractWrapper(Class templateClass) {
        return false == templateClass.isInterface() && Wrapper.class.isAssignableFrom(templateClass) && Modifier.isAbstract(templateClass.getModifiers()) && false == WrapperLegacySupport.class.isAssignableFrom(templateClass);
    }
}
