package org.maximodeveloper.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.Remote;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jdt.core.JavaModelException;
import org.maximodeveloper.filedb.MaxAttributeSaver;
import org.maximodeveloper.filedb.MaxObjectSaver;
import org.maximodeveloper.tool.MyMethod;

public class ProxyClass {

    private Class klazz = null;

    private URLClassLoader loader = null;

    private String klazzName;

    Set importSet;

    private static SourceWriter sWriter;

    private IScriptWriter scriptWriter;

    ProxyClass baseClass;

    ProxyClass baseMboProxyClass;

    private String[] methodsToOverride;

    public ProxyClass() {
    }

    public ProxyClass forName(String className) {
        klazzName = className;
        return this;
    }

    public ProxyClass getSuperclass() {
        loadIt();
        ProxyClass superKlazz = buildProxyClassForRemoteInterface();
        if (klazz == null) {
            return superKlazz;
        }
        superKlazz.setKlazz(klazz.getSuperclass());
        return superKlazz;
    }

    private void loadIt() {
        try {
            if (loader == null) {
                loader = getURLClassLoader();
            }
            if (klazz == null && klazzName != null) {
                klazz = loader.loadClass(klazzName);
                initWriters();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public URLClassLoader getURLClassLoader() throws JavaModelException {
        return new EClassLoader().getURLClassLoader();
    }

    private void initWriters() {
        importSet = new HashSet();
        scriptWriter = SourceWriterBuilder.getDBScriptWriter();
    }

    public ProxyClass getMyClass() {
        loadIt();
        return this;
    }

    public String getName() {
        loadIt();
        return klazz.getName();
    }

    public Package getPackage() {
        loadIt();
        return klazz.getPackage();
    }

    public Method[] getDeclaredMethods() {
        loadIt();
        return klazz.getDeclaredMethods();
    }

    public Method[] getAllMethods() {
        loadIt();
        Method[] methods = klazz.getMethods();
        Set<Method> retSet = new HashSet<Method>();
        retSet.addAll(Arrays.asList(methods));
        retSet.addAll(getProtectedMethods());
        return retSet.toArray(new Method[0]);
    }

    private Set<Method> getProtectedMethods() {
        Set<Method> ret = new HashSet<Method>();
        return getProtectedMethods(klazz.getSuperclass());
    }

    private Set<Method> getProtectedMethods(Class kl) {
        Set<Method> ret = new HashSet<Method>();
        Method[] meth = kl.getDeclaredMethods();
        for (int i = 0; i < meth.length; i++) {
            if (Modifier.isProtected(meth[i].getModifiers())) {
                ret.add(meth[i]);
            }
        }
        Class superclass = kl.getSuperclass();
        if (superclass != null) {
            ret.addAll(getProtectedMethods(superclass));
        }
        return ret;
    }

    public Constructor[] getDeclaredConstructors() {
        loadIt();
        return klazz.getDeclaredConstructors();
    }

    public void setKlazz(Class klazz) {
        this.klazz = klazz;
        if (klazz == null) {
            return;
        }
        klazzName = klazz.getName();
    }

    public boolean isPrimitive() {
        loadIt();
        if (klazz == null) {
            return false;
        }
        return klazz.isPrimitive();
    }

    public boolean isArray() {
        loadIt();
        return klazz.isArray();
    }

    public String toString() {
        loadIt();
        return klazz.toString();
    }

    public ProxyClass getRemoteInterface() {
        loadIt();
        Class[] interfaces = klazz.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (hasRemote(interfaces[i])) {
                ProxyClass p = buildProxyClassForRemoteInterface();
                p.setKlazz(interfaces[i]);
                return p;
            }
        }
        return null;
    }

    protected ProxyClass buildProxyClassForRemoteInterface() {
        return new ProxyClass();
    }

    private boolean hasRemote(Class clz) {
        Class h = clz;
        while (h != null) {
            if (h == Remote.class) {
                return true;
            }
            Class[] interfaces = h.getInterfaces();
            for (int j = 0; j < interfaces.length; j++) {
                if (hasRemote(interfaces[j])) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean isDataBean() {
        loadIt();
        return isDataBean(klazz);
    }

    private boolean isDataBean(Class clz) {
        if (clz == null) {
            return false;
        }
        if (MaxProperties.getDataBeanClass().equals(clz.getName())) {
            return true;
        } else {
            return isDataBean(clz.getSuperclass());
        }
    }

    public boolean isMboSet() {
        loadIt();
        return isMboSet(klazz);
    }

    private boolean isMboSet(Class clz) {
        if (clz == null) {
            return false;
        }
        String[] mboSetNames = MboNamesBuilder.build().getMboSetNames();
        for (int i = 0; i < mboSetNames.length; i++) {
            if (mboSetNames[i].equals(clz.getName())) {
                return true;
            }
        }
        return isMboSet(clz.getSuperclass());
    }

    public boolean isMbo() {
        loadIt();
        return isMbo(klazz);
    }

    private boolean isMbo(Class clz) {
        if (clz == null) {
            return false;
        }
        String[] mboNames = MboNamesBuilder.build().getMboNames();
        for (int i = 0; i < mboNames.length; i++) {
            if (mboNames[i].equals(clz.getName())) {
                return true;
            }
        }
        return isMbo(clz.getSuperclass());
    }

    public boolean isFieldClass() {
        loadIt();
        return isFieldClass(klazz);
    }

    private boolean isFieldClass(Class clz) {
        if (clz == null) {
            return false;
        }
        String[] fieldClassNames = MboNamesBuilder.build().getFieldClasses();
        for (int i = 0; i < fieldClassNames.length; i++) {
            if (fieldClassNames[i].equals(clz.getName())) {
                return true;
            }
        }
        return isFieldClass(clz.getSuperclass());
    }

    public boolean isRemoteInterface() {
        loadIt();
        if (!isInterface()) {
            return false;
        }
        return isRemoteInterface(klazz);
    }

    private boolean isRemoteInterface(Class clz) {
        if (clz == null) {
            return false;
        }
        if ("java.rmi.Remote".equals(clz.getName())) {
            return true;
        } else {
            Class[] superInterfaces = clz.getInterfaces();
            boolean ret = false;
            for (int i = 0; i < superInterfaces.length; i++) {
                ret |= isRemoteInterface(superInterfaces[i]);
            }
            return ret;
        }
    }

    public boolean equals(Object arg0) {
        loadIt();
        return klazz.getName().equals(((ProxyClass) arg0).getName());
    }

    public int hashCode() {
        loadIt();
        return klazz.getName().hashCode();
    }

    public boolean isInterface() {
        loadIt();
        if (klazz == null) {
            return false;
        }
        return klazz.isInterface();
    }

    public String translateClass() {
        loadIt();
        if (this == null) {
            return "";
        }
        String name = getName();
        if (name.indexOf("$") != -1) {
            name = name.replace('$', '.');
        }
        String baseClassName = name.substring(name.lastIndexOf(".") + 1).replaceFirst(";", "");
        if (name.startsWith("[")) {
            int lastParenthesis = name.lastIndexOf("[");
            String classType = name.substring(lastParenthesis + 1, lastParenthesis + 2);
            String pars = "";
            for (int i = 0; i < lastParenthesis + 1; i++) {
                pars += "[]";
            }
            if (classType.equals("I")) {
                baseClassName = "int";
            }
            if (classType.equals("B")) {
                baseClassName = "byte";
            }
            if (classType.equals("D")) {
                baseClassName = "double";
            }
            if (classType.equals("F")) {
                baseClassName = "float";
            }
            if (classType.equals("C")) {
                baseClassName = "char";
            }
            if (classType.equals("S")) {
                baseClassName = "short";
            }
            if (classType.equals("Z")) {
                baseClassName = "boolean";
            }
            if (classType.equals("L")) {
                int lPosition = getName().indexOf('L');
                String cName = name.substring(lPosition + 1, name.length() - 1);
                try {
                    if (sWriter != null) {
                        sWriter.addToImportSet(buildProxyClassForRemoteInterface().forName(cName));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            baseClassName += pars;
            return baseClassName;
        }
        if (sWriter != null) {
            sWriter.addToImportSet(this);
        }
        return name.substring(name.lastIndexOf(".") + 1).replaceFirst(";", "[]");
    }

    public String getNewClassName() {
        String baseClassName = "";
        if (baseClass != null) {
            baseClassName = baseClass.translateClass() + "Remote";
        } else {
            baseClassName = translateClass();
        }
        Pattern p1 = Pattern.compile("(.+)(SetRemote)");
        Matcher m1 = p1.matcher(baseClassName);
        if (m1.matches()) {
            return m1.group(1) + getDefaultNameExtension() + m1.group(2);
        }
        Pattern p = Pattern.compile("(.+)(Set|Remote)");
        Matcher m = p.matcher(baseClassName);
        if (m.matches()) {
            return m.group(1) + getDefaultNameExtension() + m.group(2);
        }
        return baseClassName + getDefaultNameExtension();
    }

    private String getDefaultNameExtension() {
        return MaxProperties.getClassSuffix();
    }

    String getPackageName() {
        loadIt();
        return getPackage().getName();
    }

    public String getNewPackageName() {
        if (baseClass != null) {
            return baseClass.getPackageName().replaceFirst("psdi", MaxProperties.getPackagePrefix());
        }
        return getPackageName().replaceFirst("psdi", MaxProperties.getPackagePrefix());
    }

    public String getNewFullClassName() {
        return getNewPackageName() + "." + getNewClassName();
    }

    public void writeClassToFile() {
        loadIt();
        this.baseClass = null;
        sWriter = SourceWriterBuilder.getSourceWriter(this);
        if (isInterface()) {
            sWriter.addToImportSet(this);
        }
        sWriter.writeClassToFile();
        SourceWriterBuilder.deleteWriter();
        MaxProperties.addClassReadyForFormating(this);
    }

    public void writeInterfaceToFile(ProxyClass baseClass) {
        loadIt();
        this.baseClass = baseClass;
        sWriter = SourceWriterBuilder.getInterfaceSourceWriter(this, baseClass);
        sWriter.writeClassToFile();
        SourceWriterBuilder.deleteWriter();
    }

    public void writeInterfaceToFile(ProxyClass baseClass, Set additionalMethods) {
        loadIt();
        sWriter = SourceWriterBuilder.getInterfaceSourceWriter(this, null);
        InterfaceSourceWriter isw = (InterfaceSourceWriter) sWriter;
        isw.writeClassToFile(additionalMethods);
        SourceWriterBuilder.deleteWriter();
    }

    public void updateMaxAttribute(String objectName, String attributeName) {
        new MaxAttributeSaver().updateChange(objectName, attributeName, getNewFullClassName());
    }

    public void updateMaxObject(String objectName) {
        new MaxObjectSaver().updateChange(objectName, getNewFullClassName());
    }

    public boolean hasRemoteInterface() {
        loadIt();
        return hasRemote(klazz);
    }

    public void writeRemoteInterfaceToFile() {
        getRemoteInterface().writeInterfaceToFile(this);
    }

    public void writeRemoteInterfaceToFile(Set additionalMethods) {
        getRemoteInterface().writeInterfaceToFile(this, additionalMethods);
    }

    public Method[] getMethods() {
        loadIt();
        if (klazz == null) {
            return null;
        }
        return klazz.getDeclaredMethods();
    }

    public Set getPublicMethods() {
        Method[] allMethods = getMethods();
        Set publicMethods = new HashSet();
        if (allMethods == null) {
            return publicMethods;
        }
        for (int i = 0; i < allMethods.length; i++) {
            if (Modifier.isPublic(allMethods[i].getModifiers())) {
                publicMethods.add(allMethods[i]);
            }
        }
        return publicMethods;
    }

    public ProxyClass getSuperRemoteInterface() {
        ProxyClass sri = buildProxyClassForRemoteInterface();
        ProxyClass remoteInterface = getRemoteInterface();
        if (remoteInterface == null) {
            return null;
        }
        Class[] interfaces = remoteInterface.klazz.getInterfaces();
        if (interfaces.length == 0) {
            return null;
        }
        sri.setKlazz(interfaces[0]);
        return sri;
    }

    public Set getAllMethodsForAllRemoteInterfaces() {
        ProxyClass remote = getRemoteInterface();
        if (remote != null) {
            Set thisMethods = remote.getPublicMethods();
            ProxyClass superRemoteInterface = remote.getRemoteInterface();
            Set superMethods = new HashSet();
            if (superRemoteInterface != null) {
                superMethods = superRemoteInterface.getAllMethodsForAllRemoteInterfaces();
            }
            Set resultSet = new HashSet();
            resultSet.addAll(thisMethods);
            resultSet.addAll(superMethods);
            return resultSet;
        }
        return new HashSet();
    }

    public void discardClassLoader() {
        loader = null;
    }

    public Set getAllMyMethodsForAllRemoteInterfaces() {
        return MyMethod.convert(getAllMethodsForAllRemoteInterfaces());
    }

    public Set getPublicMyMethods() {
        return MyMethod.convert(getPublicMethods());
    }

    public boolean isLoaded() {
        loadIt();
        return klazz != null;
    }

    public void replaceLoader(URL[] classPath) {
        loader = new URLClassLoader(classPath);
    }

    public String getBaseMbo() {
        String mboSetClassName = getName();
        Pattern regex = Pattern.compile("(.*)Set(.*)");
        Matcher matcher = regex.matcher(mboSetClassName);
        if (!matcher.matches()) {
            Logger.debug("MboSet class " + mboSetClassName + " doesn't have Set!");
            return "";
        }
        return matcher.group(1) + matcher.group(2);
    }

    public ProxyClass getBaseMboProxyClass() {
        if (baseMboProxyClass == null) {
            ProxyClass proxyCl = buildProxyClassForRemoteInterface().forName(getBaseMbo());
            proxyCl.loadIt();
            baseMboProxyClass = proxyCl;
            return proxyCl;
        }
        return baseMboProxyClass;
    }

    public boolean overrideMethods() {
        return MaxProperties.overrideAllMethods();
    }

    public boolean overrideSuperMethods() {
        return MaxProperties.overrideSuperMethods();
    }

    public String[] getMethodsToOverride() {
        if (isFieldClass()) {
            return new String[] { "init", "action", "validate" };
        }
        if (isMboSet()) {
            return new String[] { "getMboInstance" };
        }
        if (isMbo()) {
            return new String[] { "init", "add" };
        }
        return null;
    }

    public Method[] getMyMethods() {
        MyMethod[] allMethods = getAllMaximoMethods();
        MyMethod[] declaredMethods = MyMethod.convert(getDeclaredMethods());
        MyMethod[] full = new MyMethod[allMethods.length + declaredMethods.length];
        System.arraycopy(declaredMethods, 0, full, 0, declaredMethods.length);
        System.arraycopy(allMethods, 0, full, declaredMethods.length, allMethods.length);
        Set<MyMethod> choosenMethods = new HashSet<MyMethod>();
        if (overrideMethods()) {
            for (int i = 0; i < allMethods.length; i++) {
                choosenMethods.add(allMethods[i]);
            }
        } else if (overrideSuperMethods()) {
            MyMethod[] superClMethods = MyMethod.convert(getDeclaredMethods());
            for (int i = 0; i < superClMethods.length; i++) {
                int modifiers = superClMethods[i].getMethod().getModifiers();
                if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                    choosenMethods.add(superClMethods[i]);
                }
            }
        }
        for (int i = 0; i < full.length; i++) {
            if (Modifier.isAbstract(full[i].getMethod().getModifiers())) {
                choosenMethods.add(full[i]);
            }
            String[] methodsToOverride2 = getMethodsToOverride();
            if (methodsToOverride2 != null && methodsToOverride2.length != 0) {
                for (int j = 0; j < methodsToOverride2.length; j++) {
                    if (methodsToOverride2[j].equals(full[i].getName())) {
                        choosenMethods.add(full[i]);
                    }
                }
            }
        }
        return (Method[]) MyMethod.convertBack(choosenMethods).toArray(new Method[0]);
    }

    private MyMethod[] getAllMaximoMethods() {
        return (MyMethod[]) getAllMaximoMethods(this).toArray(new MyMethod[0]);
    }

    public Set<MyMethod> getAllMaximoMethodsSet() {
        return getAllMaximoMethods(this);
    }

    private Set<MyMethod> getAllMaximoMethods(ProxyClass klZZ) {
        Set<MyMethod> ret = new HashSet<MyMethod>();
        if (!(klZZ.isFieldClass() || klZZ.isMbo() || klZZ.isMboSet())) {
            return ret;
        }
        MyMethod[] mm = MyMethod.convert(klZZ.getDeclaredMethods());
        MyMethod[] sm = klZZ.getSuperclass().getAllMaximoMethods();
        for (MyMethod m : mm) {
            if (!m.getReturnTypeP().isVisible()) continue;
            ret.add(m);
        }
        for (MyMethod m : sm) {
            if (!m.getReturnTypeP().isVisible()) continue;
            ret.add(m);
        }
        return ret;
    }

    public boolean isVisible() {
        loadIt();
        if (Modifier.isPublic(klazz.getModifiers())) {
            return true;
        }
        return false;
    }
}
