package net.sf.jauvm.vm.ref;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.sf.jauvm.vm.AccessControl;
import net.sf.jauvm.vm.Types;

public final class FieldRef extends SymbolicRef<Field> {

    private static final Reference<Field> nil = new WeakReference<Field>(null);

    private volatile Reference<Field> field = nil;

    private final String owner;

    private final String name;

    private final String descriptor;

    private final boolean expectsStatic;

    private final boolean expectsPuttable;

    private final Reference<Class<?>> referrer;

    public FieldRef(String owner, String name, String descriptor, Class<?> referrer, boolean expectsStatic, boolean expectsPuttable) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
        this.expectsStatic = expectsStatic;
        this.expectsPuttable = expectsPuttable;
        this.referrer = new WeakReference<Class<?>>(referrer);
    }

    public Field get() {
        if (field.get() == null) resolve();
        return field.get();
    }

    private synchronized void resolve() {
        if (field.get() != null) return;
        Class<?> cls = ClassRef.get(owner, referrer.get());
        Field f = findField(cls, name, descriptor);
        if (expectsStatic != Modifier.isStatic(f.getModifiers())) throw new IncompatibleClassChangeError(Types.getInternalName(cls));
        if (expectsPuttable && Modifier.isFinal(f.getModifiers())) throw new IllegalAccessError(Types.getInternalName(f));
        AccessControl.checkPermission(f, referrer.get());
        AccessControl.makeAccessible(f);
        field = new SoftReference<Field>(f);
    }

    private static Field findField(Class<?> cls, String name, String descriptor) {
        assert cls != null;
        for (; cls != null; cls = cls.getSuperclass()) {
            for (Field f : cls.getDeclaredFields()) {
                if (fieldMatches(f, name, descriptor)) return f;
            }
            for (Class<?> c : cls.getInterfaces()) {
                Field f = findFieldHelper(c, name, descriptor);
                if (f != null) return f;
            }
        }
        throw new NoSuchFieldError(fieldInternalName(cls, name, descriptor));
    }

    private static Field findFieldHelper(Class<?> cls, String name, String descriptor) {
        for (Field f : cls.getDeclaredFields()) {
            if (fieldMatches(f, name, descriptor)) return f;
        }
        for (Class<?> c : cls.getInterfaces()) {
            Field f = findFieldHelper(c, name, descriptor);
            if (f != null) return f;
        }
        return null;
    }

    private static boolean fieldMatches(Field f, String name, String descriptor) {
        return name.equals(f.getName()) && descriptor.equals(Types.getDescriptor(f));
    }

    private static String fieldInternalName(Class<?> cls, String name, String descriptor) {
        return Types.getInternalName(cls) + '/' + name + ' ' + descriptor;
    }
}
