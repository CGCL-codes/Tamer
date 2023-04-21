package polyglot.ext.jl5.ast;

import java.util.List;
import polyglot.ast.AmbQualifierNode;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assert;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Block;
import polyglot.ast.Call;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Case;
import polyglot.ast.Cast;
import polyglot.ast.Catch;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Conditional;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldDecl;
import polyglot.ast.FlagsNode;
import polyglot.ast.Formal;
import polyglot.ast.Id;
import polyglot.ast.If;
import polyglot.ast.Import;
import polyglot.ast.Instanceof;
import polyglot.ast.LocalDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.NodeFactory;
import polyglot.ast.PackageNode;
import polyglot.ast.Prefix;
import polyglot.ast.Receiver;
import polyglot.ast.Return;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.TypeNode;
import polyglot.ext.jl5.types.FlagAnnotations;
import polyglot.types.ClassDef.Kind;
import polyglot.types.Package;
import polyglot.types.QName;
import polyglot.types.Ref;
import polyglot.types.Type;
import polyglot.util.Position;

/**
 * NodeFactory for jl5 extension.
 */
public interface JL5NodeFactory extends NodeFactory {

    public AmbQualifierNode AmbQualifierNode(Position pos, Prefix qual, Id name, List args);

    public AmbTypeNode AmbTypeNode(Position pos, Prefix qual, Id name, List args);

    public AnnotationElemDecl AnnotationElemDecl(Position pos, FlagAnnotations flags, TypeNode type, Id name, Expr def);

    public polyglot.ast.ArrayTypeNode ArrayTypeNode(Position pos, TypeNode base, boolean varargs);

    public Assert Assert(Position pos, Expr cond, Expr errorMsg);

    public BoundedTypeNode BoundedTypeNode(Position pos, Kind kind, TypeNode bound);

    public Call Call(Position pos, Receiver target, Id name, List<Expr> args);

    public Call Call(Position pos, Receiver target, Id name, List<Expr> args, List<TypeNode> typeArgs);

    public CanonicalTypeNode CanonicalTypeNode(Position pos, Ref<? extends Type> type);

    public Case Case(Position pos, Expr expr);

    public Cast Cast(Position pos, TypeNode castType, Expr expr);

    public Catch Catch(Position pos, Formal formal, Block body);

    public ClassBody ClassBody(Position pos, List<ClassMember> members);

    public ClassDecl ClassDecl(Position pos, FlagAnnotations flags, Id name, TypeNode superType, List<TypeNode> interfaces, ClassBody body, List paramTypes);

    public Conditional Conditional(Position pos, Expr cond, Expr conseq, Expr altern);

    public ConstructorDecl ConstructorDecl(Position pos, FlagAnnotations flags, Id name, List<Formal> formals, List<TypeNode> throwTypes, Block body);

    public ConstructorDecl ConstructorDecl(Position pos, FlagAnnotations flags, Id name, List<Formal> formals, List<TypeNode> throwTypes, Block body, List typeParams);

    public ElementValuePair ElementValuePair(Position pos, Id name, Expr value);

    public EnumConstantDecl EnumConstantDecl(Position pos, FlagAnnotations flags, Id name, List args);

    public EnumConstantDecl EnumConstantDecl(Position pos, FlagAnnotations flags, Id name, List args, ClassBody body);

    public EnumDecl EnumDecl(Position pos, FlagAnnotations flags, Id name, TypeNode superType, List interfaces, ClassBody body);

    public EnumDecl EnumDecl(Position pos, FlagAnnotations flags, Id name, TypeNode superType, List interfaces, ClassBody body, List paramTypes);

    public ExtendedFor ExtendedFor(Position pos, List varDecls, Expr expr, Stmt stmt);

    public Field Field(Position pos, Receiver target, Id name);

    public FieldDecl FieldDecl(Position pos, FlagAnnotations flags, TypeNode type, Id name, Expr init);

    public FieldDecl FieldDecl(Position pos, FlagsNode flags, TypeNode type, Id name, Expr init);

    public Formal Formal(Position pos, FlagAnnotations flags, TypeNode type, Id name);

    public Binary.Operator getBinOpFromAssignOp(Assign.Operator op);

    public If If(Position pos, Expr cond, Stmt conseq, Stmt altern);

    public Import Import(Position pos, Import.Kind kind, QName Name);

    public Instanceof Instanceof(Position pos, Expr expr, TypeNode tn);

    public LocalDecl LocalDecl(Position pos, FlagAnnotations flags, TypeNode type, Id name, Expr init);

    public LocalDecl LocalDecl(Position pos, FlagsNode flags, TypeNode type, Id name, Expr init);

    public MarkerAnnotationElem MarkerAnnotationElem(Position pos, TypeNode name);

    public MethodDecl MethodDecl(Position pos, FlagAnnotations flags, TypeNode returnType, Id name, List<Formal> formals, List<TypeNode> throwTypes, Block body, List typeParams);

    public MethodDecl MethodDecl(Position pos, FlagsNode flags, TypeNode returnType, Id name, List<Formal> formals, List<TypeNode> throwTypes, Block body);

    public New New(Position pos, Expr qualifier, TypeNode tn, List arguments, ClassBody body, List<TypeNode> typeArgs);

    public New New(Position pos, TypeNode tn, List arguments, ClassBody body, List<TypeNode> typeArgs);

    public NewArray NewArray(Position pos, TypeNode baseType, List dims, int addDims, ArrayInit init);

    public NormalAnnotationElem NormalAnnotationElem(Position pos, TypeNode name, List elements);

    public PackageNode PackageNode(Position pos, FlagAnnotations fl, Ref<? extends Package> p);

    public PackageNode PackageNode(Position pos, Ref<? extends Package> p);

    public ParamTypeNode ParamTypeNode(Position pos, List bounds, String id);

    public Return Return(Position pos, Expr expr);

    public SingleElementAnnotationElem SingleElementAnnotationElem(Position pos, TypeNode name, Expr value);

    public ConstructorCall SuperCall(Position pos, Expr outer, List<Expr> args, List<TypeNode> typeArgs);

    public ConstructorCall SuperCall(Position pos, List<Expr> args, List<TypeNode> typeArgs);

    public Switch Switch(Position pos, Expr expr, List elements);

    public ConstructorCall ThisCall(Position pos, Expr outer, List<Expr> args, List<TypeNode> typeArgs);

    public ConstructorCall ThisCall(Position pos, List<Expr> args, List<TypeNode> typeArgs);
}
