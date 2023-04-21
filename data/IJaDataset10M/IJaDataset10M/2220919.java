package visitor;

import syntaxtree.And;
import syntaxtree.ArrayAssign;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.Assign;
import syntaxtree.Block;
import syntaxtree.BooleanType;
import syntaxtree.Call;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.False;
import syntaxtree.Formal;
import syntaxtree.Identifier;
import syntaxtree.IdentifierExp;
import syntaxtree.IdentifierType;
import syntaxtree.If;
import syntaxtree.IntArrayType;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.LessThan;
import syntaxtree.MainClass;
import syntaxtree.MethodDecl;
import syntaxtree.Minus;
import syntaxtree.NewArray;
import syntaxtree.NewObject;
import syntaxtree.Not;
import syntaxtree.Plus;
import syntaxtree.Print;
import syntaxtree.Program;
import syntaxtree.This;
import syntaxtree.Times;
import syntaxtree.True;
import syntaxtree.VarDecl;
import syntaxtree.While;
import activationRegister.util.Exp;

/**
 * 
 * */
public interface TranslateVisitor {

    /**
	 * 
	 * */
    public Exp visit(Program n);

    /**
	 * 
	 * */
    public Exp visit(MainClass n);

    /**
	 * 
	 * */
    public Exp visit(ClassDeclSimple n);

    /**
	 * 
	 * */
    public Exp visit(ClassDeclExtends n);

    /**
	 * 
	 * */
    public Exp visit(VarDecl n);

    /**
	 * 
	 * */
    public Exp visit(MethodDecl n);

    /**
	 * 
	 * */
    public Exp visit(Formal n);

    /**
	 * 
	 * */
    public Exp visit(IntArrayType n);

    /**
	 * 
	 * */
    public Exp visit(BooleanType n);

    /**
	 * 
	 * */
    public Exp visit(IntegerType n);

    /**
	 * 
	 * */
    public Exp visit(IdentifierType n);

    /**
	 * 
	 * */
    public Exp visit(Block n);

    /**
	 * 
	 * */
    public Exp visit(If n);

    /**
	 * 
	 * */
    public Exp visit(While n);

    /**
	 * 
	 * */
    public Exp visit(Print n);

    /**
	 * 
	 * */
    public Exp visit(Assign n);

    /**
	 * 
	 * */
    public Exp visit(ArrayAssign n);

    /**
	 * 
	 * */
    public Exp visit(And n);

    /**
	 * 
	 * */
    public Exp visit(LessThan n);

    /**
	 * 
	 * */
    public Exp visit(Plus n);

    /**
	 * 
	 * */
    public Exp visit(Minus n);

    /**
	 * 
	 * */
    public Exp visit(Times n);

    /**
	 * 
	 * */
    public Exp visit(ArrayLookup n);

    /**
	 * 
	 * */
    public Exp visit(ArrayLength n);

    /**
	 * 
	 * */
    public Exp visit(Call n);

    /**
	 * 
	 * */
    public Exp visit(IntegerLiteral n);

    /**
	 * 
	 * */
    public Exp visit(True n);

    /**
	 * 
	 * */
    public Exp visit(False n);

    /**
	 * 
	 * */
    public Exp visit(IdentifierExp n);

    /**
	 * 
	 * */
    public Exp visit(This n);

    /**
	 * 
	 * */
    public Exp visit(NewArray n);

    /**
	 * 
	 * */
    public Exp visit(NewObject n);

    /**
	 * 
	 * */
    public Exp visit(Not n);

    /**
	 * 
	 * */
    public Exp visit(Identifier n);
}
