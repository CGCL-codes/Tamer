package com.nibble.scriptengine;

import com.nibble.managers.Logger;
import com.nibble.scriptengine.Parameter.Visibility;
import com.nibble.scriptengine.StatementVariableDeclaration.DeclarationInfo;
import com.nibble.tools.Pair;
import com.nibble.tools.reflect.AbstractDynamic;
import com.nibble.tools.reflect.Dynamic;

/**
 * Nibble's security class. This class has been created to do all the availability checking when accessing instance
 * members of Nibble's prototype based class instances. Specifically, this class provides security checks for instance
 * member visibility.
 * 
 * @author Benny Bottema
 * @see ClassDefinition
 * @see Parser#parseClassDeclaration
 */
final class Security {

    private final Logger logger;

    Security(final Logger logger) {
        this.logger = logger;
    }

    /**
	 * Performs security checks on a static property or method. Checks if the requested member is indeed static and
	 * further delagtes security checks to {@link #doSecurityCheck(AbstractDynamic, StackStatement, String)}.
	 * 
	 * @param defPair The {@link ClassDefinition} and its accompanied prototype.
	 * @param memberName The name of the member to perform security checks for.
	 * @param librarian a {@link Librarian} reference to used to fetch methods on the {@link ClassDefinition}
	 * @throws SecurityException Thrown when member isn't <code>static</code> while it should be, when member is not
	 *             visible from outside or member didn't exist at all.
	 * @see #doSecurityCheck(AbstractDynamic, StackStatement, String)
	 */
    void doSecurityCheckOnStaticMember(final Pair<ClassDefinition, Dynamic> defPair, final String memberName, final Librarian librarian) throws SecurityException, MemberNotStaticSecurityException {
        Object member = defPair.getObjectA().getMethod(memberName, librarian, true);
        member = member != null ? member : defPair.getObjectA().getVariable(memberName, librarian, true);
        if (member != null) {
            final StackStatement scope = new DynamicStackStatement(defPair.getObjectB());
            new Security(logger).doSecurityCheck(defPair.getObjectB(), scope, memberName);
        } else {
            throw new MemberNotStaticSecurityException(defPair.getObjectA().getClassName(), memberName);
        }
    }

    /**
	 * Collects all data needed to perform a security check. The security check itself is mostly delegated to
	 * {@link #isAvailable(Pair, StackStatement, String, Librarian)}. Only if there is no prototype based class
	 * definition from the specified subject (property's owner), this method will return early without problem.
	 * 
	 * @param subject The property's owner.
	 * @param scope The scope/context in which the variable we're checking is being reference.
	 * @param id The property's name.
	 * @throws SecurityException Thrown when the property is not accessible from the localscope scope.
	 * @see #isAvailable(Pair, StackStatement, String, Librarian)
	 */
    void doSecurityCheck(final AbstractDynamic subject, final StackStatement scope, final String id) throws SecurityException {
        final Pair<ClassDefinition, Dynamic> defPairSub = getClassDefinition(subject);
        if (defPairSub != null) {
            final Librarian librarian = ScriptManager.getLibrarian();
            final boolean isConstructor = defPairSub.getObjectA().getClassName().equals(id);
            final boolean isSuperConstructor = defPairSub.getObjectA().subClassOf(id, librarian);
            try {
                isAvailable(defPairSub, scope, id, librarian);
            } catch (final MemberNotFoundSecurityException e) {
                if (!isConstructor && !isSuperConstructor) {
                    throw e;
                }
            }
        }
    }

    /**
	 * Returns a prototype based {@link ClassDefinition} if available, or <code>null</code> otherwise.
	 * 
	 * @param subject The subject to inspect and find the class definition for.
	 * @return The <code>ClassDefinition</code> if applicable.
	 */
    Pair<ClassDefinition, Dynamic> getClassDefinition(final AbstractDynamic subject) {
        final Librarian librarian = ScriptManager.getLibrarian();
        final String className = (String) subject.getScopedProperty(ClassDefinition.CLASSNAME);
        return librarian.fetchClassDefinition(className);
    }

    /**
	 * Checks if a variable is available, meaning it should exist and it should be visible within the specified scope.
	 * 
	 * @param defPair De {@link ClassDefinition} and its prototype that has the requested property.
	 * @param scope The scope object from which access is requested to the specified propertry.
	 * @param id The requested property's name.
	 * @param librarian A {@link Librarian} reference used to fina visibility up the inheritance chain for a prototype
	 *            based class instance.
	 * @throws SecurityException
	 * @see #isVisible(com.nibble.scriptengine.Parameter.Visibility, Pair, StackStatement, String)
	 */
    private void isAvailable(final Pair<ClassDefinition, Dynamic> defPair, final StackStatement scope, final String id, final Librarian librarian) throws SecurityException {
        final DeclarationInfo infoProperty = defPair.getObjectA().getVariable(id, librarian, false);
        final StackStatementFunction func = defPair.getObjectA().getMethod(id, librarian, false);
        if (infoProperty != null) {
            isVisible(infoProperty.visibility, defPair, scope, id);
        } else if (func != null) {
            isVisible(func.getVisibility(), defPair, scope, id);
        } else {
            throw new MemberNotFoundSecurityException(defPair.getObjectA().getClassName(), id);
        }
    }

    /**
	 * Checks if a variable is allowed to be referenced from within the specified scope.<br />
	 * Returns:
	 * <ul>
	 * <li>false, if the variable is not public and the scope can't possibly be in the prototype chain (ie. because it
	 * isn't {@link Dynamic})</li>
	 * <li>true, if the variable is public anyway (see {@link Visibility}), else</li>
	 * <li>true, if the scope is the same <code>Dynamic</code> as the property owner, else</li>
	 * <li>false</li>
	 * </ul>
	 * 
	 * @param v A {@link Visibility} modifier that represents the declared visiblity of the instance member.
	 * @param defPair The {@link ClassDefinition} and coupled <code>Dynamic</code> prototype.
	 * @param scope The scope/context in which the variable we're checking is being reference.
	 * @param id The name of the variable we're checking.
	 * @throws MemberNotVisibleSecurityException
	 * @see #isAvailable(Pair, StackStatement, String, Librarian)
	 */
    void isVisible(final Visibility v, final Pair<ClassDefinition, Dynamic> defPair, final StackStatement scope, final String id) throws MemberNotVisibleSecurityException {
        if (v == Visibility.PUBLIC) {
            return;
        } else {
            final DynamicStackStatement dynamicScope = findDynamicScope(scope);
            if (dynamicScope != null) {
                final AbstractDynamic scopedDynamic = dynamicScope.stack;
                final Dynamic prototype = defPair.getObjectB();
                if (!(prototype == scopedDynamic || scopedDynamic.isInPrototypeChain(prototype))) {
                    throw new MemberNotVisibleSecurityException(defPair.getObjectA().getClassName(), id);
                }
            } else {
                throw new MemberNotVisibleSecurityException(defPair.getObjectA().getClassName(), id);
            }
        }
    }

    /**
	 * Tries to find the {@link DynamicStackStatement} up the chain if available. This way we can use "this.property" on
	 * private properties of a prototype based class.
	 * 
	 * @param scope The current scope we're in (ie. a if-statement or a for-loop)
	 * @return The parent <code>DynamicStackStatement</code> context if available, or <code>null</code> otherwise.
	 */
    DynamicStackStatement findDynamicScope(final StackStatement scope) {
        StackStatement localScope = scope;
        do {
            if (localScope instanceof DynamicStackStatement) {
                return (DynamicStackStatement) localScope;
            } else if (localScope == localScope.localscope) {
                return null;
            }
        } while ((localScope = localScope.localscope) != null);
        return null;
    }
}
