package sun.dyn.util;

import java.dyn.LinkagePermission;
import java.dyn.MethodHandles.Lookup;
import java.dyn.NoAccessException;
import java.lang.reflect.Modifier;
import sun.dyn.MemberName;
import sun.dyn.MethodHandleImpl;
import sun.dyn.empty.Empty;

/**
 * This class centralizes information about the JVM's linkage access control.
 * @author jrose
 */
public class VerifyAccess {

    private VerifyAccess() {
    }

    /**
     * Evaluate the JVM linkage rules for access to the given method on behalf of caller.
     * Return non-null if and only if the given accessing class has at least partial
     * privileges to invoke the given method.  The return value {@code Object.class}
     * denotes unlimited privileges.
     * <p>
     * Some circumstances require an additional check on the
     * leading parameter (the receiver) of the method, if it is non-static.
     * In the case of {@code invokespecial} ({@code isSpecialInvoke} is true),
     * the leading parameter must be the accessing class or a subclass.
     * In the case of a call to a {@code protected} method outside the same
     * package, the same constraint applies.
     * @param m the proposed callee
     * @param isSpecialInvoke if true, a non-static method m is checked as if for {@code invokespecial}
     * @param lookupClass the class for which the access check is being made
     * @return null if the method is not accessible, else a receiver type constraint, else {@code Object.class}
     */
    public static Class<?> isAccessible(Class<?> defc, int mods, Class<?> lookupClass, boolean isSpecialInvoke) {
        if (!isAccessible(defc, lookupClass)) return null;
        Class<?> constraint = Object.class;
        if (isSpecialInvoke && !Modifier.isStatic(mods)) {
            constraint = lookupClass;
        }
        if (Modifier.isPublic(mods)) return constraint;
        if (Modifier.isPrivate(mods)) return isSamePackageMember(defc, lookupClass) ? constraint : null;
        if (isSamePackage(defc, lookupClass)) return constraint;
        if (Modifier.isProtected(mods) && defc.isAssignableFrom(lookupClass)) return constraint;
        return null;
    }

    /**
     * Evaluate the JVM linkage rules for access to the given class on behalf of caller.
     */
    public static boolean isAccessible(Class<?> refc, Class<?> lookupClass) {
        int mods = refc.getModifiers();
        if (Modifier.isPublic(mods)) return true;
        if (isSamePackage(lookupClass, refc)) return true;
        return false;
    }

    /**
     * Test if two classes have the same class loader and package qualifier.
     * @param class1
     * @param class2
     * @return whether they are in the same package
     */
    public static boolean isSamePackage(Class<?> class1, Class<?> class2) {
        if (class1 == class2) return true;
        if (!loadersAreRelated(class1.getClassLoader(), class2.getClassLoader())) return false;
        String name1 = class1.getName(), name2 = class2.getName();
        int dot = name1.lastIndexOf('.');
        if (dot != name2.lastIndexOf('.')) return false;
        for (int i = 0; i < dot; i++) {
            if (name1.charAt(i) != name2.charAt(i)) return false;
        }
        return true;
    }

    /**
     * Test if two classes are defined as part of the same package member (top-level class).
     * If this is true, they can share private access with each other.
     * @param class1
     * @param class2
     * @return whether they are identical or nested together
     */
    public static boolean isSamePackageMember(Class<?> class1, Class<?> class2) {
        if (class1 == class2) return true;
        if (!isSamePackage(class1, class2)) return false;
        if (getOutermostEnclosingClass(class1) != getOutermostEnclosingClass(class2)) return false;
        return true;
    }

    private static Class<?> getOutermostEnclosingClass(Class<?> c) {
        Class<?> pkgmem = c;
        for (Class<?> enc = c; (enc = enc.getEnclosingClass()) != null; ) pkgmem = enc;
        return pkgmem;
    }

    private static boolean loadersAreRelated(ClassLoader loader1, ClassLoader loader2) {
        if (loader1 == loader2 || loader1 == null || loader2 == null) {
            return true;
        }
        for (ClassLoader scan1 = loader1; scan1 != null; scan1 = scan1.getParent()) {
            if (scan1 == loader2) return true;
        }
        for (ClassLoader scan2 = loader2; scan2 != null; scan2 = scan2.getParent()) {
            if (scan2 == loader1) return true;
        }
        return false;
    }

    /**
     * Ensure the requesting class have privileges to perform invokedynamic
     * linkage operations on subjectClass.  True if requestingClass is
     * Access.class (meaning the request originates from the JVM) or if the
     * classes are in the same package and have consistent class loaders.
     * (The subject class loader must be identical with or be a child of
     * the requesting class loader.)
     * @param requestingClass
     * @param subjectClass
     */
    public static void checkBootstrapPrivilege(Class requestingClass, Class subjectClass, String permissionName) {
        if (requestingClass == null) return;
        if (requestingClass == subjectClass) return;
        SecurityManager security = System.getSecurityManager();
        if (security == null) return;
        if (isSamePackage(requestingClass, subjectClass)) return;
        security.checkPermission(new LinkagePermission(permissionName, requestingClass));
    }

    private static RuntimeException checkNameFailed(MemberName self, Lookup lookup, String comment) {
        return new NoAccessException("cannot access from " + lookup + ": " + self.toString() + ": " + comment);
    }

    public static void checkName(MemberName self, Lookup lookup) {
        Class<?> lc = lookup.lookupClass();
        if (lc == null) return;
        Class<?> dc = self.getDeclaringClass();
        int samepkg = 0;
        if (!Modifier.isPublic(dc.getModifiers())) {
            if (lc != Empty.class) samepkg = (isSamePackage(dc, lc) ? 1 : -1);
            if (samepkg <= 0) throw checkNameFailed(self, lookup, "class is not public");
        }
        if (self.isPublic()) {
            return;
        } else if (lc == Empty.class) {
            throw checkNameFailed(self, lookup, "member is not public");
        } else if (self.isProtected()) {
            if (dc.isAssignableFrom(lc)) return;
        } else if (self.isPrivate()) {
            if (isSamePackageMember(dc, lc)) return;
            throw checkNameFailed(self, lookup, "member is private");
        }
        if (samepkg == 0) samepkg = (isSamePackage(dc, lc) ? 1 : -1);
        if (samepkg > 0) return;
        throw checkNameFailed(self, lookup, self.isProtected() ? "member is protected" : "member is private to package");
    }
}
