package net.cmp4oaw.ea_com.visitor;

import net.cmp4oaw.ea_com.common.EA_PrimitiveType;
import net.cmp4oaw.ea_com.common.EA_TaggedValue;
import net.cmp4oaw.ea_com.element.EA_Actor;
import net.cmp4oaw.ea_com.element.EA_ChangeElement;
import net.cmp4oaw.ea_com.element.EA_DataType;
import net.cmp4oaw.ea_com.element.EA_Enumeration;
import net.cmp4oaw.ea_com.element.EA_FeatureElement;
import net.cmp4oaw.ea_com.element.EA_File;
import net.cmp4oaw.ea_com.element.EA_Interface;
import net.cmp4oaw.ea_com.element.EA_Note;
import net.cmp4oaw.ea_com.element.EA_NoteConstraint;
import net.cmp4oaw.ea_com.element.EA_Object;
import net.cmp4oaw.ea_com.element.EA_RequirementElement;
import net.cmp4oaw.ea_com.element.EA_Screen;
import net.cmp4oaw.ea_com.element.EA_UseCase;

public interface EA_ElementVisitor {

    public void visit(EA_Actor o);

    public void visit(EA_ChangeElement o);

    public void visit(EA_Enumeration o);

    public void visit(EA_FeatureElement o);

    public void visit(EA_File o);

    public void visit(EA_Interface o);

    public void visit(EA_Note o);

    public void visit(EA_NoteConstraint o);

    public void visit(EA_Object o);

    public void visit(EA_PrimitiveType o);

    public void visit(EA_DataType o);

    public void visit(EA_RequirementElement o);

    public void visit(EA_TaggedValue o);

    public void visit(EA_UseCase o);

    public void visit(EA_Screen o);
}
