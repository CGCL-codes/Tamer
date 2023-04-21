package net.sourceforge.freejava.valtype.util;

public class TypeDistance {

    /**
     * @return -1 If actualClass isn't subclass of declClass.
     * @throws NullPointerException
     *             If any argument is <code>null</code>.
     * @throws IllegalArgumentException
     *             If <code>declClass</code> is interface.
     */
    public static int getClassExtendsCount(Class<?> declClass, Class<?> actualClass) {
        if (declClass.isInterface()) throw new IllegalArgumentException("declClass is interface: " + declClass);
        int dist = -1;
        while (declClass.isAssignableFrom(actualClass)) {
            dist++;
            actualClass = actualClass.getSuperclass();
            if (actualClass == null) break;
        }
        return dist;
    }

    /**
     * @return -1 If actualClass doesn't implements declInterface.
     * @throws NullPointerException
     *             If any argument is <code>null</code>.
     * @throws IllegalArgumentException
     *             If <code>declInterface</code> isn't interface.
     */
    public static int getMinInterfaceExtendsCount(Class<?> declInterface, Class<?> actualClass) {
        if (actualClass == null) throw new NullPointerException("actualClass");
        if (!declInterface.isInterface()) throw new IllegalArgumentException("declInterface isn't interface: " + declInterface);
        int min = Integer.MAX_VALUE;
        int classExtends = -1;
        while (actualClass != null) {
            Class<?>[] actualInterfaces = actualClass.getInterfaces();
            actualClass = actualClass.getSuperclass();
            classExtends++;
            if (actualInterfaces.length == 0) continue;
            int _dist = getMinInterfaceExtendsCount(declInterface, actualInterfaces);
            if (_dist == -1) continue;
            int dist = classExtends + _dist + 1;
            min = Math.min(min, dist);
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    /**
     * @return -1 If none of <code>actualInterfaces</code> implements <code>declInterface</code>.
     * @throws NullPointerException
     *             If any argument is <code>null</code>.
     * @throws IllegalArgumentException
     *             If <code>declInterface</code> isn't interface.
     */
    public static int getMinInterfaceExtendsCount(Class<?> declInterface, Class<?>[] actualInterfaces) {
        if (declInterface == null) throw new NullPointerException("declInterface");
        if (!declInterface.isInterface()) throw new IllegalArgumentException("declInterface isn't interface: " + declInterface);
        if (actualInterfaces.length == 0) return -1;
        int min = Integer.MAX_VALUE;
        for (Class<?> actualInterface : actualInterfaces) {
            if (declInterface.isAssignableFrom(actualInterface)) {
                int dist = 0;
                Class<?>[] superInterfaces = actualInterface.getInterfaces();
                if (superInterfaces.length != 0) {
                    int superDist = getMinInterfaceExtendsCount(declInterface, superInterfaces);
                    if (superDist != -1) dist += superDist + 1;
                }
                min = Math.min(min, dist);
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    /**
     * @return -1 If declType isn't assignable from actualType.
     * @throws
     */
    public static int dist(Class<?> declType, Class<?> actualType) {
        if (declType.isInterface()) return getMinInterfaceExtendsCount(declType, actualType); else return getClassExtendsCount(declType, actualType);
    }

    /**
     * @return -1 If size of the two array is different, or any of the actual type not is-a
     *         corresponding declared type.
     * @throws NullPointerException
     *             If any argument is <code>null</code>.
     */
    public static int dist(Class<?>[] declTypes, Class<?>[] actualTypes) {
        if (declTypes == null) throw new NullPointerException("declTypes");
        if (actualTypes == null) throw new NullPointerException("actualTypes");
        if (declTypes.length != actualTypes.length) return -1;
        int distsum = 0;
        for (int i = 0; i < declTypes.length; i++) {
            if (actualTypes[i] == null) continue;
            int dist = dist(declTypes[i], actualTypes[i]);
            if (dist == -1) return -1;
            distsum += dist;
        }
        return distsum;
    }
}
