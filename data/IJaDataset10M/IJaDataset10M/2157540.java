package net.sourceforge.classreader.methods;

import net.sourceforge.classreader.ClassReader;

public class Methods {

    private final MethodInfo[] methodInfos;

    private Methods(MethodInfo[] methodInfos) {
        this.methodInfos = methodInfos;
    }

    public MethodInfo getMethodInfo(int index) {
        return methodInfos[index];
    }

    public MethodInfo[] getMethodInfos() {
        return methodInfos;
    }

    public static Methods getMethods(ClassReader classStream, int methodsCount) {
        MethodInfo[] methodInfos = new MethodInfo[methodsCount];
        for (int i = 0; i < methodsCount; i++) {
            methodInfos[i] = MethodInfo.getMethodInfo(classStream);
        }
        return new Methods(methodInfos);
    }
}
