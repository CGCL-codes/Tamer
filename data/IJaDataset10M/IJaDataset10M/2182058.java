package de.linwave.gtm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GTMRemove {

    private static GTM gtm = GTM.getGTMInstance();

    private Map<String, Object> objToResolve = new HashMap<String, Object>();

    private int stack;

    /**
	 * Remove a object instance from the DB but do NOT follow any reference
	 * 
	 * @param obj
	 * @throws Exception
	 */
    public void delete(Object obj) {
        deleteFlat(obj);
    }

    /**
	 * Remove a object instance from the DB but do NOT follow any reference
	 * 
	 * @param <E>
	 * @param e
	 * @throws Exception
	 */
    private <E> void deleteFlat(E e) {
        long oid = OID.getOID(ObjectTraverser.getFieldInfo(e, GTM.OID), e, false);
        if (oid == 0) {
            throw new RuntimeException("Object " + e + " not bound to the DB");
        }
        String orderKey = GlobalName.buildGlobalName(e.getClass(), oid, "");
        while (orderKey != null && (orderKey = gtm.ORDER(orderKey)) != null) {
            String gln = GlobalName.buildGlobalName(e.getClass(), oid, orderKey);
            try {
                gtm.KILL(gln);
            } catch (Exception ex) {
                throw new RuntimeException("Could not remove entry " + gln, ex);
            }
            orderKey = GlobalName.buildGlobalName(e.getClass(), oid, orderKey);
        }
    }

    /**
	 * 
	 * @param <E>
	 * @param e
	 * @param depth
	 * @throws Exception
	 */
    public <E> void deleteCascade(E e, int depth) {
        List<String> nodesToDelete = new ArrayList<String>();
        int stack = _deleteCascade(e, depth, nodesToDelete);
        if (stack == 0) {
            for (String gln : nodesToDelete) {
                gtm.KILL(gln);
            }
        } else {
            throw new RuntimeException("Could not remove object " + e + ". Reason: stack != 0. Recursion problem?");
        }
    }

    /**
	 * Internal method to delete a object cascaded Remove referenced objects only if reference count == 0
	 * 
	 * @param <E>
	 * @param e
	 * @param depth
	 * @param nodesToDelete
	 * @return
	 * @throws Exception
	 */
    private <E> int _deleteCascade(E e, int depth, List<String> nodesToDelete) {
        stack++;
        if (stack > depth) {
            decrementStack();
            return stack;
        }
        String value = null;
        Class<?> clasz = e.getClass();
        long oid = OID.getOID(ObjectTraverser.getFieldInfo(e, GTM.OID), e, false);
        if (oid == 0) {
            throw new RuntimeException("Object " + e + " not bound to the DB");
        }
        String refName = GlobalName.buildGlobalName(clasz, oid);
        if (objToResolve.containsKey(refName)) {
            decrementStack();
            return stack;
        } else {
            objToResolve.put(refName, e);
            GlobalName.decrementReferenceCount(refName);
        }
        String orderKey = GlobalName.buildGlobalName(clasz, oid, "");
        while (orderKey != null && (orderKey = gtm.ORDER(orderKey)) != null) {
            FieldInfo fi = ObjectTraverser.getFieldInfo(e, orderKey);
            Field field = fi.field;
            String gln = GlobalName.buildGlobalName(clasz, oid, orderKey);
            value = gtm.GET(gln);
            if (fi.isArray && fi.isPrimitive) {
                nodesToDelete.add(gln);
            } else if (fi.isCollection && fi.isPrimitive) {
                List<String> arrList = getCollectionFromGTM(clasz, oid, field.getName(), orderKey);
                for (int i = 0; i < arrList.size(); i++) {
                    nodesToDelete.add(GlobalName.buildGlobalName(clasz, oid, orderKey, i));
                }
            } else if (fi.isCollection) {
                List<String> arrList = getCollectionFromGTM(clasz, oid, field.getName(), orderKey);
                for (int i = 0; i < arrList.size(); i++) {
                    nodesToDelete.add(GlobalName.buildGlobalName(clasz, oid, orderKey, i));
                    String s = arrList.get(i);
                    if (GlobalName.decrementReferenceCount(s) == 0) {
                        String rmGln = GlobalName.buildGlobalName(clasz, oid, field.getName(), i);
                        nodesToDelete.add(rmGln);
                    }
                    MemberInfo mi = getMemberFromReference(s);
                    _deleteCascade(mi.instance, depth, nodesToDelete);
                }
            } else if (value != null && value.length() > 0) {
                if (fi.typeHandler != null) {
                    nodesToDelete.add(gln);
                } else {
                    nodesToDelete.add(gln);
                    if (GlobalName.decrementReferenceCount(value) == 0) {
                        MemberInfo mi = getMemberFromReference(value);
                        _deleteCascade(mi.instance, depth, nodesToDelete);
                    }
                }
            }
            orderKey = GlobalName.buildGlobalName(clasz, oid, orderKey);
        }
        decrementStack();
        return stack;
    }

    private void decrementStack() {
        stack--;
        if (stack == 0) {
            objToResolve.clear();
        }
    }

    /**
	 * 
	 * @param clasz
	 * @param OID
	 * @param fieldName
	 * @param orderKey
	 * @return
	 * @throws Exception
	 */
    private List<String> getCollectionFromGTM(Class<?> clasz, long OID, String fieldName, String orderKey) {
        List<String> arrList = new ArrayList<String>();
        String arrayKey = GlobalName.buildGlobalName(clasz, OID, orderKey, "");
        while ((arrayKey = gtm.ORDER(arrayKey)) != null) {
            arrList.add(gtm.GET(GlobalName.buildGlobalName(clasz, OID, fieldName, arrayKey)));
            arrayKey = GlobalName.buildGlobalName(clasz, OID, fieldName, arrayKey);
        }
        return arrList;
    }

    /**
	 * Pass in a 'foreign' object reference 'Adress123455(2)' and get the instance back.
	 * 
	 * @param value
	 * @return
	 */
    private MemberInfo getMemberFromReference(String value) {
        MemberInfo mi = new MemberInfo();
        int openBrace = value.lastIndexOf('(');
        if (openBrace > -1) {
            String globalName = value.substring(0, openBrace);
            String oid = value.substring(openBrace);
            oid = oid.substring(1, oid.length() - 1);
            String className = GlobalName.GlobalName2ClassName(globalName);
            try {
                mi.clasz = Class.forName(className);
                mi.clasz.getConstructor().newInstance(new Object[0]);
                mi.instance = mi.clasz.newInstance();
                mi.oid = OID.toLong(oid);
                Field field = ObjectTraverser.getFieldInfo(mi.instance, GTM.OID).field;
                setField(field, mi.instance, mi.oid);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return mi;
    }

    /**
	 * 
	 * @param field
	 * @param target
	 * @param value
	 * @return
	 * @throws Exception
	 */
    private Object setField(Field field, Object target, Object value) throws Exception {
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (Exception ex) {
            System.out.println("Error while setting field '" + field.getName() + "' with value (" + value + ")");
            ex.printStackTrace();
        }
        return value;
    }
}
