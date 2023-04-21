package net.jadoth.codegen.java.codeobjects;

import java.lang.reflect.Member;

/**
 * @author Thomas Muenz
 *
 */
public interface JavaTypeMemberDescription extends JavaModifierableCompilationObjectDescription, Member {

    public JavaTypeDescription getOwnerType();

    public int getNestingLevel();
}
