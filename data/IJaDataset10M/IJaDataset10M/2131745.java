package alefpp.parser.visitor;

import alefpp.parser.syntaxtree.*;
import java.util.*;

/**
 * All GJ visitors with no argument must implement this interface.
 */
@SuppressWarnings("all")
public interface GJNoArguVisitor<R> {

    public R visit(NodeList n);

    public R visit(NodeListOptional n);

    public R visit(NodeOptional n);

    public R visit(NodeSequence n);

    public R visit(NodeToken n);

    /**
    * f0 -> ( UseStatement() )*
    * f1 -> ( EmptyStatement() | VariableDeclaration() ";" | SubroutineDeclaration() | Module() )*
    * f2 -> ( Main() )?
    * f3 -> <EOF>
    */
    public R visit(Alefpp n);

    /**
    * f0 -> "use"
    * f1 -> <IDENTIFIER>
    * f2 -> ( "." ( <IDENTIFIER> | "*" ) )*
    * f3 -> [ ":" <STRING_LITERAL> ]
    * f4 -> ";"
    */
    public R visit(UseStatement n);

    /**
    * f0 -> ";"
    */
    public R visit(EmptyStatement n);

    /**
    * f0 -> <SPCIAL_SCALAR>
    *       | <SCALAR>
    *       | "$"
    */
    public R visit(Scalar n);

    /**
    * f0 -> OperatorAssign()
    * f1 -> Expression()
    */
    public R visit(ScalarAssign n);

    /**
    * f0 -> "my"
    * f1 -> [ "`" ]
    * f2 -> <SCALAR>
    * f3 -> [ FullIdentifier() ]
    * f4 -> ( "@" | "%" )*
    * f5 -> [ ScalarAssign() ]
    */
    public R visit(VariableDeclaration n);

    /**
    * f0 -> "["
    * f1 -> [ ArgumentList() ]
    * f2 -> "]"
    */
    public R visit(Arguments n);

    /**
    * f0 -> Expression()
    * f1 -> ( "," Expression() )*
    */
    public R visit(ArgumentList n);

    /**
    * f0 -> "#"
    * f1 -> ( ModuleSubroutineCalling() | <INTEGER_LITERAL> | Expression() )
    */
    public R visit(ArrayIndex n);

    /**
    * f0 -> "\\"
    * f1 -> ( <IDENTIFIER> | Expression() )
    */
    public R visit(HashKey n);

    /**
    * f0 -> "["
    * f1 -> ( Expression() ( "," Expression() )* )?
    * f2 -> "]"
    */
    public R visit(Array n);

    /**
    * f0 -> "{"
    * f1 -> ( ( <IDENTIFIER> | Expression() ) "=>" Expression() ( "," ( <IDENTIFIER> | Expression() ) "=>" Expression() )* )?
    * f2 -> "}"
    */
    public R visit(Hash n);

    /**
    * f0 -> OperatorArrow()
    * f1 -> ( <IDENTIFIER> [ Arguments() ] | Scalar() [ ArrayIndex() | HashKey() ] )
    * f2 -> ( MemberAccess() )*
    */
    public R visit(MemberAccess n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> ( "." <IDENTIFIER> )*
    */
    public R visit(FullIdentifier n);

    /**
    * f0 -> FullIdentifier()
    * f1 -> ( ( "::" )? Arguments() )?
    * f2 -> [ MemberAccess() ]
    */
    public R visit(ModuleSubroutineCalling n);

    /**
    * f0 -> CastStatement()
    *       | ReferenceStatement()
    *       | XiSubroutine()
    *       | ConditionalExpression()
    *       | ConditionalOrExpression()
    *       | AdditiveExpression()
    *       | MultiplicativeExpression()
    *       | BitwiseExpression()
    *       | ShiftExpression()
    *       | Array()
    *       | Hash()
    *       | ValuesExpression()
    */
    public R visit(Expression n);

    /**
    * f0 -> "to"
    * f1 -> Expression()
    * f2 -> FullIdentifier()
    */
    public R visit(CastStatement n);

    /**
    * f0 -> "ref"
    * f1 -> Expression()
    * f2 -> FullIdentifier()
    */
    public R visit(ReferenceStatement n);

    /**
    * f0 -> ( "+" | "-" )
    * f1 -> ( Expression() )+
    */
    public R visit(AdditiveExpression n);

    /**
    * f0 -> ( "*" | "/" | "%" | "//" | "**" )
    * f1 -> ( Expression() )+
    */
    public R visit(MultiplicativeExpression n);

    /**
    * f0 -> ( "&" | "|" )
    * f1 -> ( Expression() )+
    */
    public R visit(BitwiseExpression n);

    /**
    * f0 -> ( "<<" | ">>" )
    * f1 -> ( Expression() )+
    */
    public R visit(ShiftExpression n);

    /**
    * f0 -> ConditionalAndExpression()
    * f1 -> ( LogicOrExpression() ConditionalAndExpression() )*
    */
    public R visit(ConditionalOrExpression n);

    /**
    * f0 -> EqualityExpression()
    * f1 -> ( LogicAndExpression() EqualityExpression() )*
    */
    public R visit(ConditionalAndExpression n);

    /**
    * f0 -> RelationalExpression()
    * f1 -> ( ( "==" | RelationalNotEqual() ) RelationalExpression() )*
    */
    public R visit(EqualityExpression n);

    /**
    * f0 -> ExclusiveOrExpression()
    * f1 -> ( ( "<" | ">" | RelationalLessOrEqual() | RelationalGreaterOrEqual() ) ExclusiveOrExpression() )*
    */
    public R visit(RelationalExpression n);

    /**
    * f0 -> ValuesExpression()
    * f1 -> ( LogicXOrExpression() ValuesExpression() )*
    */
    public R visit(ExclusiveOrExpression n);

    /**
    * f0 -> "walo"
    */
    public R visit(WaloExpression n);

    /**
    * f0 -> WaloExpression()
    *       | "true"
    *       | "false"
    *       | <STRING_LITERAL>
    *       | <INTEGER_LITERAL>
    *       | <FLOATING_POINT_LITERAL>
    *       | PreInDeCrementExpression()
    *       | InDeCrementExpression()
    *       | ScalarValue() [ MemberAccess() ] [ ScalarAssign() ]
    *       | ModuleSubroutineCalling() [ ScalarAssign() ]
    *       | "(" Expression() ")" [ MemberAccess() ]
    *       | LogicNotExprssion() Expression()
    */
    public R visit(ValuesExpression n);

    /**
    * f0 -> ( "++" | "--" )
    * f1 -> ( ScalarValue() [ MemberAccess() ] | ModuleSubroutineCalling() )
    */
    public R visit(PreInDeCrementExpression n);

    /**
    * f0 -> ( ScalarValue() [ MemberAccess() ] | ModuleSubroutineCalling() )
    * f1 -> ( "++" | "--" )
    */
    public R visit(InDeCrementExpression n);

    /**
    * f0 -> "="
    * f1 -> Expression()
    * f2 -> ","
    * f3 -> ValuesExpression()
    * f4 -> [ "," ValuesExpression() ]
    */
    public R visit(ConditionalExpression n);

    /**
    * f0 -> Scalar()
    * f1 -> ( ArrayIndex() | HashKey() )*
    */
    public R visit(ScalarValue n);

    /**
    * f0 -> "eval"
    * f1 -> BlockStatement()
    * f2 -> ( "error" "(" ( <SCALAR> | "my" <SCALAR> [ FullIdentifier() ] ) ")" BlockStatement() )+
    * f3 -> [ "end" BlockStatement() ]
    */
    public R visit(EvalStatement n);

    /**
    * f0 -> "goto"
    * f1 -> Expression()
    * f2 -> ":"
    * f3 -> BlockStatement()
    */
    public R visit(GotoStatement n);

    /**
    * f0 -> "miss"
    * f1 -> ":"
    * f2 -> BlockStatement()
    */
    public R visit(MissStatement n);

    /**
    * f0 -> "jump"
    * f1 -> Expression()
    * f2 -> ( GotoStatement() )+
    * f3 -> [ MissStatement() ]
    */
    public R visit(JumpStatement n);

    /**
    * f0 -> Expression()
    * f1 -> "!loop"
    * f2 -> BlockStatement()
    */
    public R visit(DoWhileStatement n);

    /**
    * f0 -> Expression()
    * f1 -> "loop"
    * f2 -> BlockStatement()
    */
    public R visit(WhileStatement n);

    /**
    * f0 -> EachInit()
    * f1 -> ","
    * f2 -> Expression()
    * f3 -> "each"
    * f4 -> BlockStatement()
    */
    public R visit(EachStatement n);

    /**
    * f0 -> "my" <SCALAR> [ FullIdentifier() ]
    *       | ScalarValue() [ MemberAccess() ]
    *       | ModuleSubroutineCalling()
    */
    public R visit(EachInit n);

    /**
    * f0 -> GoInit()
    * f1 -> ","
    * f2 -> Expression()
    * f3 -> "go"
    * f4 -> [ "(" Expression() ")" ]
    * f5 -> BlockStatement()
    */
    public R visit(GoStatement n);

    /**
    * f0 -> Expression()
    * f1 -> "=>"
    * f2 -> BlockStatement()
    * f3 -> [ "," BlockStatement() ]
    */
    public R visit(IfStatement n);

    /**
    * f0 -> "my" <SCALAR> [ FullIdentifier() ] [ ScalarAssign() ]
    *       | ValuesExpression()
    */
    public R visit(GoInit n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> ":"
    */
    public R visit(Label n);

    /**
    * f0 -> "next"
    * f1 -> [ <IDENTIFIER> ]
    */
    public R visit(Next n);

    /**
    * f0 -> "last"
    * f1 -> [ <IDENTIFIER> ]
    */
    public R visit(Last n);

    /**
    * f0 -> FullIdentifier()
    * f1 -> [ ( "[" )+ | ( "@" | "%" )+ ]
    */
    public R visit(ParameterType n);

    /**
    * f0 -> ":"
    * f1 -> FullIdentifier()
    * f2 -> ( ( "[" )+ | ( "@" | "%" )+ )?
    */
    public R visit(SubroutineType n);

    /**
    * f0 -> "sub"
    * f1 -> <IDENTIFIER>
    * f2 -> [ SubroutineType() ]
    * f3 -> ( "(" [ ParameterType() ( "," ParameterType() )* ] ")" )?
    * f4 -> [ DiesStatement() ]
    * f5 -> Block()
    */
    public R visit(SubroutineDeclaration n);

    /**
    * f0 -> Block()
    * f1 -> Arguments()
    */
    public R visit(XiSubroutine n);

    /**
    * f0 -> "ret"
    * f1 -> Expression()
    * f2 -> ";"
    */
    public R visit(ReturnStatement n);

    /**
    * f0 -> "die"
    * f1 -> Expression()
    */
    public R visit(DieStatement n);

    /**
    * f0 -> ( "=" FullIdentifier() )+
    */
    public R visit(DiesStatement n);

    /**
    * f0 -> "synchro"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Block()
    */
    public R visit(SynchonizedStatement n);

    /**
    * f0 -> "main"
    * f1 -> [ DiesStatement() ]
    * f2 -> Block()
    */
    public R visit(Main n);

    /**
    * f0 -> "{"
    * f1 -> ( BlockStatement() )*
    * f2 -> "}"
    */
    public R visit(Block n);

    /**
    * f0 -> EmptyStatement()
    *       | Block()
    *       | Statement()
    *       | ReturnStatement()
    */
    public R visit(BlockStatement n);

    /**
    * f0 -> DoWhileStatement()
    *       | WhileStatement()
    *       | EachStatement()
    *       | GoStatement()
    *       | IfStatement()
    *       | Label()
    *       | Last() ";"
    *       | Next() ";"
    *       | VariableDeclaration() ";"
    *       | PreInDeCrementExpression() ";"
    *       | InDeCrementExpression() ";"
    *       | ScalarValue() [ MemberAccess() ] [ ScalarAssign() ] ";"
    *       | ModuleSubroutineCalling() [ ScalarAssign() ] ";"
    *       | DieStatement() ";"
    *       | JumpStatement() ";"
    *       | EvalStatement()
    *       | SynchonizedStatement()
    */
    public R visit(Statement n);

    /**
    * f0 -> "~"
    *       | "+"
    *       | "-"
    *       | "#"
    *       | "must"
    */
    public R visit(Modifier n);

    /**
    * f0 -> ( Modifier() )*
    * f1 -> "module"
    * f2 -> FullIdentifier()
    * f3 -> ModuleBodyBlock()
    */
    public R visit(Module n);

    /**
    * f0 -> ( Modifier() )*
    * f1 -> VariableDeclaration()
    */
    public R visit(ModuleVariableDeclaration n);

    /**
    * f0 -> "autoload"
    * f1 -> Block()
    */
    public R visit(ModuleAutoload n);

    /**
    * f0 -> "init"
    * f1 -> [ "(" [ ParameterType() ( "," ParameterType() )* ] ")" ]
    * f2 -> [ DiesStatement() ]
    * f3 -> Block()
    */
    public R visit(ModuleInit n);

    /**
    * f0 -> "{"
    * f1 -> ( DollarISA() )?
    * f2 -> ( AtISA() )?
    * f3 -> ( ModuleVariableDeclaration() ";" | SubroutineDeclaration() | ModuleAutoload() | EmptyStatement() | ModuleInit() )*
    * f4 -> "}"
    */
    public R visit(ModuleBodyBlock n);

    /**
    * f0 -> <DOLLARISA>
    * f1 -> "<"
    * f2 -> FullIdentifier()
    * f3 -> ">"
    */
    public R visit(DollarISA n);

    /**
    * f0 -> <ATISA>
    * f1 -> "<"
    * f2 -> FullIdentifier()
    * f3 -> ( "," FullIdentifier() )*
    * f4 -> ">"
    */
    public R visit(AtISA n);

    /**
    * f0 -> ( "!" | "Â¬" | "¬" )
    */
    public R visit(LogicNotExprssion n);

    /**
    * f0 -> ( "&&" | "âˆ§" | "∧" )
    */
    public R visit(LogicAndExpression n);

    /**
    * f0 -> ( "||" | "âˆ¨" | "∨" )
    */
    public R visit(LogicOrExpression n);

    /**
    * f0 -> ( "^" | "âŠ•" | "⊕" )
    */
    public R visit(LogicXOrExpression n);

    /**
    * f0 -> ( "<=" | "â‰¤" | "≤" )
    */
    public R visit(RelationalLessOrEqual n);

    /**
    * f0 -> ( ">=" | "â‰¥" | "≥" )
    */
    public R visit(RelationalGreaterOrEqual n);

    /**
    * f0 -> ( "!=" | "â‰ " | "≠" )
    */
    public R visit(RelationalNotEqual n);

    /**
    * f0 -> ( "<-" | "â†�" | "←" )
    */
    public R visit(OperatorAssign n);

    /**
    * f0 -> ( "->" | "â†’" | "→" )
    */
    public R visit(OperatorArrow n);
}
