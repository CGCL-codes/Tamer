package org.dynalang.mop.beans;

import java.lang.reflect.Member;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.dynalang.classtoken.ClassToken;
import org.dynalang.mop.CallProtocol;

/**
 * @author Attila Szegedi
 * @version $Id: $
 * @param <T>
 */
abstract class OverloadedMethod<T extends Member> {

    private Class<?>[][] marshalTypes;

    private final Map<ClassTokenString, Object> selectorCache = new ConcurrentHashMap<ClassTokenString, Object>();

    private final List<T> members = new LinkedList<T>();

    void addMember(T member) {
        members.add(member);
        Class<?>[] argTypes = DynamicMethod.getParameterTypes(member);
        int l = argTypes.length;
        onAddSignature(member, argTypes);
        if (marshalTypes == null) {
            marshalTypes = new Class[l + 1][];
            marshalTypes[l] = argTypes;
            updateSignature(l);
        } else if (marshalTypes.length <= l) {
            Class<?>[][] newMarshalTypes = new Class[l + 1][];
            System.arraycopy(marshalTypes, 0, newMarshalTypes, 0, marshalTypes.length);
            marshalTypes = newMarshalTypes;
            marshalTypes[l] = argTypes;
            updateSignature(l);
        } else {
            Class<?>[] oldTypes = marshalTypes[l];
            if (oldTypes == null) {
                marshalTypes[l] = argTypes;
            } else {
                for (int i = 0; i < oldTypes.length; ++i) {
                    oldTypes[i] = OverloadedMethodUtilities.getMostSpecificCommonType(oldTypes[i], argTypes[i]);
                }
            }
            updateSignature(l);
        }
        afterSignatureAdded(l);
    }

    void onClassTokensInvalidated(ClassToken[] tokens) {
        for (Iterator<ClassTokenString> i = selectorCache.keySet().iterator(); i.hasNext(); ) {
            if (i.next().containsAny(tokens)) {
                i.remove();
            }
        }
    }

    Class<?>[][] getMarshalTypes() {
        return marshalTypes;
    }

    Object getMemberForArgs(Object[] args, boolean varArg) {
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < argTypes.length; ++i) {
            Object arg = args[i];
            argTypes[i] = arg == null ? OverloadedMethodUtilities.OBJECT_CLASS : arg.getClass();
        }
        ClassTokenString argTypeTokens = new ClassTokenString(argTypes);
        Object objMember = selectorCache.get(argTypeTokens);
        if (objMember == null) {
            objMember = new ClassString<T>(argTypes).getMostSpecific(members, varArg);
            selectorCache.put(argTypeTokens, objMember);
        }
        return objMember;
    }

    abstract void onAddSignature(T member, Class<?>[] argTypes);

    abstract void updateSignature(int l);

    abstract void afterSignatureAdded(int l);

    abstract Object createInvocation(Object[] args, CallProtocol callProtocol);
}
