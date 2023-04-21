package com.konarkdev.elibrary_manager.server.dataobjects;

import java.util.BitSet;
import java.util.Date;
import javax.jdo.JDODetachedFieldAccessException;
import javax.jdo.PersistenceManager;
import javax.jdo.spi.*;

public class Booked implements PersistenceCapable, Detachable {

    private Book book;

    private User user;

    private Date bookedDate;

    protected transient StateManager jdoStateManager;

    protected transient byte jdoFlags;

    protected Object jdoDetachedState[];

    private static final byte jdoFieldFlags[];

    private static final Class jdoPersistenceCapableSuperclass;

    private static final Class jdoFieldTypes[];

    private static final String jdoFieldNames[];

    private static final int jdoInheritedFieldCount = __jdoGetInheritedFieldCount();

    public Book getBook() {
        return jdoGetbook(this);
    }

    public void setBook(Book arg0) {
        jdoSetbook(this, arg0);
    }

    public User getUser() {
        return jdoGetuser(this);
    }

    public void setUser(User arg0) {
        jdoSetuser(this, arg0);
    }

    public Date getBookedDate() {
        return jdoGetbookedDate(this);
    }

    public void setBookedDate(Date arg0) {
        jdoSetbookedDate(this, arg0);
    }

    public String toString() {
        return (new StringBuilder()).append(jdoGetbook(this)).append("::").append(jdoGetuser(this)).toString();
    }

    public void jdoCopyKeyFieldsFromObjectId(javax.jdo.spi.PersistenceCapable.ObjectIdFieldConsumer objectidfieldconsumer, Object obj) {
    }

    protected void jdoCopyKeyFieldsFromObjectId(Object obj) {
    }

    public final void jdoCopyKeyFieldsToObjectId(Object obj) {
    }

    public final void jdoCopyKeyFieldsToObjectId(javax.jdo.spi.PersistenceCapable.ObjectIdFieldSupplier objectidfieldsupplier, Object obj) {
    }

    public final Object jdoGetObjectId() {
        if (jdoStateManager != null) {
            return jdoStateManager.getObjectId(this);
        }
        if (!jdoIsDetached()) {
            return null;
        } else {
            return jdoDetachedState[0];
        }
    }

    public final Object jdoGetVersion() {
        if (jdoStateManager != null) {
            return jdoStateManager.getVersion(this);
        }
        if (!jdoIsDetached()) {
            return null;
        } else {
            return jdoDetachedState[1];
        }
    }

    protected final void jdoPreSerialize() {
        if (jdoStateManager != null) {
            jdoStateManager.preSerialize(this);
        }
    }

    public final PersistenceManager jdoGetPersistenceManager() {
        return jdoStateManager == null ? null : jdoStateManager.getPersistenceManager(this);
    }

    public final Object jdoGetTransactionalObjectId() {
        return jdoStateManager == null ? null : jdoStateManager.getTransactionalObjectId(this);
    }

    public final boolean jdoIsDeleted() {
        return jdoStateManager == null ? false : jdoStateManager.isDeleted(this);
    }

    public final boolean jdoIsDirty() {
        if (jdoStateManager != null) {
            return jdoStateManager.isDirty(this);
        }
        if (!jdoIsDetached()) {
            return false;
        }
        return ((BitSet) jdoDetachedState[3]).length() > 0;
    }

    public final boolean jdoIsNew() {
        return jdoStateManager == null ? false : jdoStateManager.isNew(this);
    }

    public final boolean jdoIsPersistent() {
        return jdoStateManager == null ? false : jdoStateManager.isPersistent(this);
    }

    public final boolean jdoIsTransactional() {
        return jdoStateManager == null ? false : jdoStateManager.isTransactional(this);
    }

    public final boolean jdoIsDetached() {
        if (jdoStateManager == null) {
            return jdoDetachedState != null;
        } else {
            return false;
        }
    }

    public final void jdoMakeDirty(String fieldName) {
        if (jdoStateManager != null) {
            jdoStateManager.makeDirty(this, fieldName);
        }
    }

    public final Object jdoNewObjectIdInstance() {
        return null;
    }

    public final Object jdoNewObjectIdInstance(Object key) {
        return null;
    }

    public final void jdoProvideFields(int fieldId[]) {
        if (fieldId == null) {
            throw new IllegalArgumentException("argment is null");
        }
        int i = fieldId.length - 1;
        if (i >= 0) {
            do {
                jdoProvideField(fieldId[i]);
            } while (--i >= 0);
        }
    }

    public final void jdoReplaceFields(int fieldId[]) {
        if (fieldId == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int i = fieldId.length;
        if (i > 0) {
            int j = 0;
            do {
                jdoReplaceField(fieldId[j]);
            } while (++j < i);
        }
    }

    public final void jdoReplaceFlags() {
        if (jdoStateManager != null) {
            jdoFlags = jdoStateManager.replacingFlags(this);
        }
    }

    public final synchronized void jdoReplaceStateManager(StateManager stateManager) {
        if (jdoStateManager != null) {
            jdoStateManager = jdoStateManager.replacingStateManager(this, stateManager);
            return;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new JDOPermission("setStateManager"));
        }
        jdoStateManager = stateManager;
        jdoFlags = 1;
    }

    public final synchronized void jdoReplaceDetachedState() {
        if (jdoStateManager == null) {
            throw new IllegalStateException("state manager is null");
        } else {
            jdoDetachedState = jdoStateManager.replacingDetachedState(this, jdoDetachedState);
            return;
        }
    }

    public PersistenceCapable jdoNewInstance(StateManager sm) {
        Booked result = new Booked();
        result.jdoFlags = 1;
        result.jdoStateManager = sm;
        return result;
    }

    public PersistenceCapable jdoNewInstance(StateManager sm, Object o) {
        Booked result = new Booked();
        result.jdoFlags = 1;
        result.jdoStateManager = sm;
        result.jdoCopyKeyFieldsFromObjectId(o);
        return result;
    }

    public void jdoReplaceField(int fieldIndex) {
        if (jdoStateManager == null) {
            throw new IllegalStateException("state manager is null");
        }
        switch(fieldIndex) {
            case 0:
                book = (Book) jdoStateManager.replacingObjectField(this, fieldIndex);
                return;
            case 1:
                bookedDate = (Date) jdoStateManager.replacingObjectField(this, fieldIndex);
                return;
            case 2:
                user = (User) jdoStateManager.replacingObjectField(this, fieldIndex);
                return;
        }
        throw new IllegalArgumentException("out of field index :" + fieldIndex);
    }

    public void jdoProvideField(int fieldIndex) {
        if (jdoStateManager == null) {
            throw new IllegalStateException("state manager is null");
        }
        switch(fieldIndex) {
            case 0:
                jdoStateManager.providedObjectField(this, fieldIndex, book);
                return;
            case 1:
                jdoStateManager.providedObjectField(this, fieldIndex, bookedDate);
                return;
            case 2:
                jdoStateManager.providedObjectField(this, fieldIndex, user);
                return;
        }
        throw new IllegalArgumentException("out of field index :" + fieldIndex);
    }

    protected final void jdoCopyField(Booked obj, int index) {
        switch(index) {
            case 0:
                book = obj.book;
                break;
            case 1:
                bookedDate = obj.bookedDate;
                break;
            case 2:
                user = obj.user;
                break;
            default:
                throw new IllegalArgumentException("out of field index :" + index);
        }
    }

    public void jdoCopyFields(Object obj, int fieldNumbers[]) {
        if (jdoStateManager == null) {
            throw new IllegalStateException("state manager is null");
        }
        if (fieldNumbers == null) {
            throw new IllegalStateException("fieldNumbers is null");
        }
        if (!(obj instanceof Booked)) {
            throw new IllegalArgumentException("object is notcom.konarkdev.elibrary_manager.server.dataobjects.Booked");
        }
        Booked me = (Booked) obj;
        if (jdoStateManager != me.jdoStateManager) {
            throw new IllegalArgumentException("state manager unmatch");
        }
        int i = fieldNumbers.length - 1;
        if (i >= 0) {
            do {
                jdoCopyField(me, fieldNumbers[i]);
            } while (--i >= 0);
        }
    }

    private static final String[] __jdoFieldNamesInit() {
        return (new String[] { "book", "bookedDate", "user" });
    }

    private static final Class[] __jdoFieldTypesInit() {
        return (new Class[] { ___jdo$loadClass("com.konarkdev.elibrary_manager.server.dataobjects.Book"), ___jdo$loadClass("java.util.Date"), ___jdo$loadClass("com.konarkdev.elibrary_manager.server.dataobjects.User") });
    }

    private static final byte[] __jdoFieldFlagsInit() {
        return (new byte[] { 10, 21, 10 });
    }

    protected static int __jdoGetInheritedFieldCount() {
        return 0;
    }

    protected static int jdoGetManagedFieldCount() {
        return jdoFieldNames.length;
    }

    private static Class __jdoPersistenceCapableSuperclassInit() {
        return null;
    }

    public static Class ___jdo$loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private Object jdoSuperClone() throws CloneNotSupportedException {
        Booked o = (Booked) super.clone();
        o.jdoFlags = 0;
        o.jdoStateManager = null;
        return o;
    }

    private static void jdoSetbook(Booked objPC, Book book_m) {
        if (objPC.jdoStateManager == null) {
            objPC.book = book_m;
        } else {
            objPC.jdoStateManager.setObjectField(objPC, 0, objPC.book, book_m);
        }
        if (!objPC.jdoIsDetached()) {
            return;
        } else {
            ((BitSet) objPC.jdoDetachedState[3]).set(0);
            return;
        }
    }

    private static Book jdoGetbook(Booked objPC) {
        if (objPC.jdoStateManager != null && !objPC.jdoStateManager.isLoaded(objPC, 0)) {
            return (Book) objPC.jdoStateManager.getObjectField(objPC, 0, objPC.book);
        }
        if (objPC.jdoIsDetached() && !((BitSet) objPC.jdoDetachedState[2]).get(0) && !((BitSet) objPC.jdoDetachedState[3]).get(0)) {
            throw new JDODetachedFieldAccessException("You have just attempted to access field \"book\" yet this field was not detached" + " when you detached the object. Either dont access this field, or detach the fiel" + "d when detaching the object.");
        } else {
            return objPC.book;
        }
    }

    private static void jdoSetbookedDate(Booked objPC, Date bookedDate_c) {
        if (objPC.jdoFlags != 0 && objPC.jdoStateManager != null) {
            objPC.jdoStateManager.setObjectField(objPC, 1, objPC.bookedDate, bookedDate_c);
            return;
        }
        objPC.bookedDate = bookedDate_c;
        if (!objPC.jdoIsDetached()) {
            return;
        } else {
            ((BitSet) objPC.jdoDetachedState[3]).set(1);
            return;
        }
    }

    private static Date jdoGetbookedDate(Booked objPC) {
        if (objPC.jdoFlags > 0 && objPC.jdoStateManager != null && !objPC.jdoStateManager.isLoaded(objPC, 1)) {
            return (Date) objPC.jdoStateManager.getObjectField(objPC, 1, objPC.bookedDate);
        }
        if (objPC.jdoIsDetached() && !((BitSet) objPC.jdoDetachedState[2]).get(1)) {
            throw new JDODetachedFieldAccessException("You have just attempted to access field \"bookedDate\" yet this field was not de" + "tached when you detached the object. Either dont access this field, or detach th" + "e field when detaching the object.");
        } else {
            return objPC.bookedDate;
        }
    }

    private static void jdoSetuser(Booked objPC, User user_m) {
        if (objPC.jdoStateManager == null) {
            objPC.user = user_m;
        } else {
            objPC.jdoStateManager.setObjectField(objPC, 2, objPC.user, user_m);
        }
        if (!objPC.jdoIsDetached()) {
            return;
        } else {
            ((BitSet) objPC.jdoDetachedState[3]).set(2);
            return;
        }
    }

    private static User jdoGetuser(Booked objPC) {
        if (objPC.jdoStateManager != null && !objPC.jdoStateManager.isLoaded(objPC, 2)) {
            return (User) objPC.jdoStateManager.getObjectField(objPC, 2, objPC.user);
        }
        if (objPC.jdoIsDetached() && !((BitSet) objPC.jdoDetachedState[2]).get(2) && !((BitSet) objPC.jdoDetachedState[3]).get(2)) {
            throw new JDODetachedFieldAccessException("You have just attempted to access field \"user\" yet this field was not detached" + " when you detached the object. Either dont access this field, or detach the fiel" + "d when detaching the object.");
        } else {
            return objPC.user;
        }
    }

    public Booked() {
    }

    static {
        jdoFieldNames = __jdoFieldNamesInit();
        jdoFieldTypes = __jdoFieldTypesInit();
        jdoFieldFlags = __jdoFieldFlagsInit();
        jdoPersistenceCapableSuperclass = __jdoPersistenceCapableSuperclassInit();
        JDOImplHelper.registerClass(___jdo$loadClass("com.konarkdev.elibrary_manager.server.dataobjects.Booked"), jdoFieldNames, jdoFieldTypes, jdoFieldFlags, jdoPersistenceCapableSuperclass, new Booked());
    }
}
