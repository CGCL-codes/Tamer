package mycompiler.yufa;

import java.util.StringTokenizer;

class TreeNode {

    TreeNode child[] = new TreeNode[3];

    TreeNode sibling = null;

    int lineno;

    String nodekind;

    String kind;

    int idnum;

    String name[] = new String[10];

    Attr attr = new Attr();
}

class Attr {

    ArrayAttr arrayAttr = null;

    ProcAttr procAttr = null;

    ExpAttr expAttr = null;

    String type_name;
}

class ArrayAttr {

    int low;

    int up;

    String childtype;
}

class ProcAttr {

    String paramt;
}

class ExpAttr {

    String op;

    int val;

    String varkind;

    String type;
}

class TokenType {

    int lineshow;

    String Lex;

    String Sem;
}

public class Recursion {

    TokenType token = new TokenType();

    int MAXTOKENLEN = 10;

    int lineno = 0;

    String temp_name;

    StringTokenizer fenxi;

    public boolean Error = false;

    public String stree;

    public String serror;

    public TreeNode yufaTree;

    public Recursion(String s) {
        yufaTree = Program(s);
        printTree(yufaTree, 0);
    }

    TreeNode Program(String ss) {
        fenxi = new StringTokenizer(ss, "\n");
        ReadNextToken();
        TreeNode root = newNode("ProcK");
        TreeNode t = ProgramHead();
        TreeNode q = DeclarePart();
        TreeNode s = ProgramBody();
        if (t != null) root.child[0] = t; else syntaxError("a program head is expected!");
        if (q != null) root.child[1] = q;
        if (s != null) root.child[2] = s; else syntaxError("a program body is expected!");
        match("DOT");
        if (!(token.Lex.equals("ENDFILE"))) syntaxError("Code ends before file\n");
        if (Error == true) return null;
        return root;
    }

    /**************************����ͷ����********************************/
    TreeNode ProgramHead() {
        TreeNode t = newNode("PheadK");
        match("PROGRAM");
        if (token.Lex.equals("ID")) t.name[0] = token.Sem;
        match("ID");
        return t;
    }

    /**************************��������**********************************/
    TreeNode DeclarePart() {
        TreeNode typeP = newNode("TypeK");
        TreeNode tp1 = TypeDec();
        if (tp1 != null) typeP.child[0] = tp1; else typeP = null;
        TreeNode varP = newNode("VarK");
        TreeNode tp2 = VarDec();
        if (tp2 != null) varP.child[0] = tp2; else varP = null;
        TreeNode procP = ProcDec();
        if (procP == null) {
        }
        if (varP == null) {
            varP = procP;
        }
        if (typeP == null) {
            typeP = varP;
        }
        if (typeP != varP) typeP.sibling = varP;
        if (varP != procP) varP.sibling = procP;
        return typeP;
    }

    /**************************������������******************************/
    TreeNode TypeDec() {
        TreeNode t = null;
        if (token.Lex.equals("TYPE")) t = TypeDeclaration(); else if ((token.Lex.equals("VAR")) || (token.Lex.equals("PROCEDURE")) || (token.Lex.equals("BEGIN"))) {
        } else ReadNextToken();
        return t;
    }

    TreeNode TypeDeclaration() {
        match("TYPE");
        TreeNode t = TypeDecList();
        if (t == null) syntaxError("a type declaration is expected!");
        return t;
    }

    TreeNode TypeDecList() {
        TreeNode t = newNode("DecK");
        if (t != null) {
            TypeId(t);
            match("EQ");
            TypeName(t);
            match("SEMI");
            TreeNode p = TypeDecMore();
            if (p != null) t.sibling = p;
        }
        return t;
    }

    TreeNode TypeDecMore() {
        TreeNode t = null;
        if (token.Lex.equals("ID")) t = TypeDecList(); else if ((token.Lex.equals("VAR")) || (token.Lex.equals("PROCEDURE")) || (token.Lex.equals("BEGIN"))) {
        } else ReadNextToken();
        return t;
    }

    void TypeId(TreeNode t) {
        if ((token.Lex.equals("ID")) && (t != null)) {
            t.name[(t.idnum)] = token.Sem;
            t.idnum = t.idnum + 1;
        }
        match("ID");
    }

    void TypeName(TreeNode t) {
        if (t != null) {
            if ((token.Lex.equals("INTEGER")) || (token.Lex.equals("CHAR"))) BaseType(t); else if ((token.Lex.equals("ARRAY")) || (token.Lex.equals("RECORD"))) StructureType(t); else if (token.Lex.equals("ID")) {
                t.kind = "IdK";
                t.attr.type_name = token.Sem;
                match("ID");
            } else ReadNextToken();
        }
    }

    void BaseType(TreeNode t) {
        if (token.Lex.equals("INTEGER")) {
            match("INTEGER");
            t.kind = "IntegerK";
        } else if (token.Lex.equals("CHAR")) {
            match("CHAR");
            t.kind = "CharK";
        } else ReadNextToken();
    }

    void StructureType(TreeNode t) {
        if (token.Lex.equals("ARRAY")) {
            ArrayType(t);
        } else if (token.Lex.equals("RECORD")) {
            t.kind = "RecordK";
            RecType(t);
        } else ReadNextToken();
    }

    void ArrayType(TreeNode t) {
        t.attr.arrayAttr = new ArrayAttr();
        match("ARRAY");
        match("LMIDPAREN");
        if (token.Lex.equals("INTC")) t.attr.arrayAttr.low = Integer.parseInt(token.Sem);
        match("INTC");
        match("UNDERANGE");
        if (token.Lex.equals("INTC")) t.attr.arrayAttr.up = Integer.parseInt(token.Sem);
        match("INTC");
        match("RMIDPAREN");
        match("OF");
        BaseType(t);
        t.attr.arrayAttr.childtype = t.kind;
        t.kind = "ArrayK";
    }

    void RecType(TreeNode t) {
        TreeNode p = null;
        match("RECORD");
        p = FieldDecList();
        if (p != null) t.child[0] = p; else syntaxError("a record body is requested!");
        match("END");
    }

    TreeNode FieldDecList() {
        TreeNode t = newNode("DecK");
        TreeNode p = null;
        if (t != null) {
            if ((token.Lex.equals("INTEGER")) || (token.Lex.equals("CHAR"))) {
                BaseType(t);
                IdList(t);
                match("SEMI");
                p = FieldDecMore();
            } else if (token.Lex.equals("ARRAY")) {
                ArrayType(t);
                IdList(t);
                match("SEMI");
                p = FieldDecMore();
            } else {
                ReadNextToken();
                syntaxError("type name is expected");
            }
            t.sibling = p;
        }
        return t;
    }

    TreeNode FieldDecMore() {
        TreeNode t = null;
        if (token.Lex.equals("INTEGER") || token.Lex.equals("CHAR") || token.Lex.equals("ARRAY")) t = FieldDecList(); else if (token.Lex.equals("END")) {
        } else ReadNextToken();
        return t;
    }

    void IdList(TreeNode t) {
        if (token.Lex.equals("ID")) {
            t.name[(t.idnum)] = token.Sem;
            t.idnum = t.idnum + 1;
            match("ID");
        }
        IdMore(t);
    }

    void IdMore(TreeNode t) {
        if (token.Lex.equals("COMMA")) {
            match("COMMA");
            IdList(t);
        } else if (token.Lex.equals("SEMI")) {
        } else ReadNextToken();
    }

    /**************************������������******************************/
    TreeNode VarDec() {
        TreeNode t = null;
        if (token.Lex.equals("VAR")) t = VarDeclaration(); else if ((token.Lex.equals("PROCEDURE")) || (token.Lex.equals("BEGIN"))) {
        } else ReadNextToken();
        return t;
    }

    TreeNode VarDeclaration() {
        match("VAR");
        TreeNode t = VarDecList();
        if (t == null) syntaxError("a var declaration is expected!");
        return t;
    }

    TreeNode VarDecList() {
        TreeNode t = newNode("DecK");
        TreeNode p = null;
        if (t != null) {
            TypeName(t);
            VarIdList(t);
            match("SEMI");
            p = VarDecMore();
            t.sibling = p;
        }
        return t;
    }

    TreeNode VarDecMore() {
        TreeNode t = null;
        if ((token.Lex.equals("INTEGER")) || (token.Lex.equals("CHAR")) || (token.Lex.equals("ARRAY")) || (token.Lex.equals("RECORD")) || (token.Lex.equals("ID"))) t = VarDecList(); else if ((token.Lex.equals("PROCEDURE")) || (token.Lex.equals("BEGIN"))) {
        } else ReadNextToken();
        return t;
    }

    void VarIdList(TreeNode t) {
        if (token.Lex.equals("ID")) {
            t.name[(t.idnum)] = token.Sem;
            t.idnum = t.idnum + 1;
            match("ID");
        } else {
            syntaxError("a varid is expected here!");
            ReadNextToken();
        }
        VarIdMore(t);
    }

    void VarIdMore(TreeNode t) {
        if (token.Lex.equals("COMMA")) {
            match("COMMA");
            VarIdList(t);
        } else if (token.Lex.equals("SEMI")) {
        } else ReadNextToken();
    }

    /****************************�����������****************************/
    TreeNode ProcDec() {
        TreeNode t = null;
        if (token.Lex.equals("PROCEDURE")) t = ProcDeclaration(); else if (token.Lex.equals("BEGIN")) {
        } else ReadNextToken();
        return t;
    }

    TreeNode ProcDeclaration() {
        TreeNode t = newNode("ProcDecK");
        match("PROCEDURE");
        if (token.Lex.equals("ID")) {
            t.name[0] = token.Sem;
            t.idnum = t.idnum + 1;
            match("ID");
        }
        match("LPAREN");
        ParamList(t);
        match("RPAREN");
        match("SEMI");
        t.child[1] = ProcDecPart();
        t.child[2] = ProcBody();
        t.sibling = ProcDecMore();
        return t;
    }

    TreeNode ProcDecMore() {
        TreeNode t = null;
        if (token.Lex.equals("PROCEDURE")) t = ProcDeclaration(); else if (token.Lex.equals("BEGIN")) {
        } else ReadNextToken();
        return t;
    }

    void ParamList(TreeNode t) {
        TreeNode p = null;
        if ((token.Lex.equals("INTEGER")) || (token.Lex.equals("CHAR")) || (token.Lex.equals("ARRAY")) || (token.Lex.equals("RECORD")) || (token.Lex.equals("ID")) || (token.Lex.equals("VAR"))) {
            p = ParamDecList();
            t.child[0] = p;
        } else if (token.Lex.equals("RPAREN")) {
        } else ReadNextToken();
    }

    TreeNode ParamDecList() {
        TreeNode t = Param();
        TreeNode p = ParamMore();
        if (p != null) t.sibling = p;
        return t;
    }

    TreeNode ParamMore() {
        TreeNode t = null;
        if (token.Lex.equals("SEMI")) {
            match("SEMI");
            t = ParamDecList();
            if (t == null) syntaxError("a param declaration is request!");
        } else if (token.Lex.equals("RPAREN")) {
        } else ReadNextToken();
        return t;
    }

    TreeNode Param() {
        TreeNode t = newNode("DecK");
        if ((token.Lex.equals("INTEGER")) || (token.Lex.equals("CHAR")) || (token.Lex.equals("ARRAY")) || (token.Lex.equals("RECORD")) || (token.Lex.equals("ID"))) {
            t.attr.procAttr = new ProcAttr();
            t.attr.procAttr.paramt = "valparamType";
            TypeName(t);
            FormList(t);
        } else if (token.Lex.equals("VAR")) {
            match("VAR");
            t.attr.procAttr = new ProcAttr();
            t.attr.procAttr.paramt = "varparamType";
            TypeName(t);
            FormList(t);
        } else ReadNextToken();
        return t;
    }

    void FormList(TreeNode t) {
        if (token.Lex.equals("ID")) {
            t.name[(t.idnum)] = token.Sem;
            t.idnum = t.idnum + 1;
            match("ID");
        }
        FidMore(t);
    }

    void FidMore(TreeNode t) {
        if (token.Lex.equals("COMMA")) {
            match("COMMA");
            FormList(t);
        } else if ((token.Lex.equals("SEMI")) || (token.Lex.equals("RPAREN"))) {
        } else ReadNextToken();
    }

    TreeNode ProcDecPart() {
        TreeNode t = DeclarePart();
        return t;
    }

    TreeNode ProcBody() {
        TreeNode t = ProgramBody();
        if (t == null) syntaxError("a program body is requested!");
        return t;
    }

    /****************************�����岿��******************************/
    TreeNode ProgramBody() {
        TreeNode t = newNode("StmLK");
        match("BEGIN");
        t.child[0] = StmList();
        match("END");
        return t;
    }

    TreeNode StmList() {
        TreeNode t = Stm();
        TreeNode p = StmMore();
        if (t != null) {
            if (p != null) t.sibling = p;
        }
        return t;
    }

    TreeNode StmMore() {
        TreeNode t = null;
        if ((token.Lex.equals("ELSE")) || (token.Lex.equals("FI")) || (token.Lex.equals("END")) || (token.Lex.equals("ENDWH"))) {
        } else if (token.Lex.equals("SEMI")) {
            match("SEMI");
            t = StmList();
        } else ReadNextToken();
        return t;
    }

    TreeNode Stm() {
        TreeNode t = null;
        if (token.Lex.equals("IF")) t = ConditionalStm(); else if (token.Lex.equals("WHILE")) t = LoopStm(); else if (token.Lex.equals("READ")) t = InputStm(); else if (token.Lex.equals("WRITE")) t = OutputStm(); else if (token.Lex.equals("RETURN")) t = ReturnStm(); else if (token.Lex.equals("ID")) {
            temp_name = token.Sem;
            match("ID");
            t = AssCall();
        } else ReadNextToken();
        return t;
    }

    TreeNode AssCall() {
        TreeNode t = null;
        if ((token.Lex.equals("ASSIGN")) || (token.Lex.equals("LMIDPAREN")) || (token.Lex.equals("DOT"))) t = AssignmentRest(); else if (token.Lex.equals("LPAREN")) t = CallStmRest(); else ReadNextToken();
        return t;
    }

    TreeNode AssignmentRest() {
        TreeNode t = newStmtNode("AssignK");
        TreeNode c = newExpNode("VariK");
        c.name[0] = temp_name;
        c.idnum = c.idnum + 1;
        VariMore(c);
        t.child[0] = c;
        match("ASSIGN");
        t.child[1] = Exp();
        return t;
    }

    TreeNode ConditionalStm() {
        TreeNode t = newStmtNode("IfK");
        match("IF");
        t.child[0] = Exp();
        match("THEN");
        if (t != null) t.child[1] = StmList();
        if (token.Lex.equals("ELSE")) {
            match("ELSE");
            t.child[2] = StmList();
        }
        match("FI");
        return t;
    }

    TreeNode LoopStm() {
        TreeNode t = newStmtNode("WhileK");
        match("WHILE");
        t.child[0] = Exp();
        match("DO");
        t.child[1] = StmList();
        match("ENDWH");
        return t;
    }

    TreeNode InputStm() {
        TreeNode t = newStmtNode("ReadK");
        match("READ");
        match("LPAREN");
        if (token.Lex.equals("ID")) {
            t.name[0] = token.Sem;
            t.idnum = t.idnum + 1;
        }
        match("ID");
        match("RPAREN");
        return t;
    }

    TreeNode OutputStm() {
        TreeNode t = newStmtNode("WriteK");
        match("WRITE");
        match("LPAREN");
        t.child[0] = Exp();
        match("RPAREN");
        return t;
    }

    TreeNode ReturnStm() {
        TreeNode t = newStmtNode("ReturnK");
        match("RETURN");
        return t;
    }

    TreeNode CallStmRest() {
        TreeNode t = newStmtNode("CallK");
        match("LPAREN");
        TreeNode c = newExpNode("VariK");
        c.name[0] = temp_name;
        c.idnum = c.idnum + 1;
        t.child[0] = c;
        t.child[1] = ActParamList();
        match("RPAREN");
        return t;
    }

    TreeNode ActParamList() {
        TreeNode t = null;
        if (token.Lex.equals("RPAREN")) {
        } else if ((token.Lex.equals("ID")) || (token.Lex.equals("INTC"))) {
            t = Exp();
            if (t != null) t.sibling = ActParamMore();
        } else ReadNextToken();
        return t;
    }

    TreeNode ActParamMore() {
        TreeNode t = null;
        if (token.Lex.equals("RPAREN")) {
        } else if (token.Lex.equals("COMMA")) {
            match("COMMA");
            t = ActParamList();
        } else ReadNextToken();
        return t;
    }

    /*************************���ʽ����********************************/
    TreeNode Exp() {
        TreeNode t = simple_exp();
        if ((token.Lex.equals("LT")) || (token.Lex.equals("EQ"))) {
            TreeNode p = newExpNode("OpK");
            p.child[0] = t;
            p.attr.expAttr.op = token.Lex;
            t = p;
            match(token.Lex);
            if (t != null) t.child[1] = simple_exp();
        }
        return t;
    }

    TreeNode simple_exp() {
        TreeNode t = term();
        while ((token.Lex.equals("PLUS")) || (token.Lex.equals("MINUS"))) {
            TreeNode p = newExpNode("OpK");
            p.child[0] = t;
            p.attr.expAttr.op = token.Lex;
            t = p;
            match(token.Lex);
            t.child[1] = term();
        }
        return t;
    }

    TreeNode term() {
        TreeNode t = factor();
        while ((token.Lex.equals("TIMES")) || (token.Lex.equals("OVER"))) {
            TreeNode p = newExpNode("OpK");
            p.child[0] = t;
            p.attr.expAttr.op = token.Lex;
            t = p;
            match(token.Lex);
            p.child[1] = factor();
        }
        return t;
    }

    TreeNode factor() {
        TreeNode t = null;
        if (token.Lex.equals("INTC")) {
            t = newExpNode("ConstK");
            t.attr.expAttr.val = Integer.parseInt(token.Sem);
            match("INTC");
        } else if (token.Lex.equals("ID")) t = Variable(); else if (token.Lex.equals("LPAREN")) {
            match("LPAREN");
            t = Exp();
            match("RPAREN");
        } else ReadNextToken();
        return t;
    }

    TreeNode Variable() {
        TreeNode t = newExpNode("VariK");
        if (token.Lex.equals("ID")) {
            t.name[0] = token.Sem;
            t.idnum = t.idnum + 1;
        }
        match("ID");
        VariMore(t);
        return t;
    }

    void VariMore(TreeNode t) {
        if ((token.Lex.equals("EQ")) || (token.Lex.equals("LT")) || (token.Lex.equals("PLUS")) || (token.Lex.equals("MINUS")) || (token.Lex.equals("RPAREN")) || (token.Lex.equals("RMIDPAREN")) || (token.Lex.equals("SEMI")) || (token.Lex.equals("COMMA")) || (token.Lex.equals("THEN")) || (token.Lex.equals("ELSE")) || (token.Lex.equals("FI")) || (token.Lex.equals("DO")) || (token.Lex.equals("ENDWH")) || (token.Lex.equals("END")) || (token.Lex.equals("ASSIGN")) || (token.Lex.equals("TIMES")) || (token.Lex.equals("OVER"))) {
        } else if (token.Lex.equals("LMIDPAREN")) {
            match("LMIDPAREN");
            t.child[0] = Exp();
            t.attr.expAttr.varkind = "ArrayMembV";
            match("RMIDPAREN");
        } else if (token.Lex.equals("DOT")) {
            match("DOT");
            t.child[0] = FieldVar();
            t.attr.expAttr.varkind = "FieldMembV";
        } else ReadNextToken();
    }

    TreeNode FieldVar() {
        TreeNode t = newExpNode("VariK");
        if (token.Lex.equals("ID")) {
            t.name[0] = token.Sem;
            t.idnum = t.idnum + 1;
        }
        match("ID");
        FieldVarMore(t);
        return t;
    }

    void FieldVarMore(TreeNode t) {
        if ((token.Lex.equals("ASSIGN")) || (token.Lex.equals("TIMES")) || (token.Lex.equals("EQ")) || (token.Lex.equals("LT")) || (token.Lex.equals("PLUS")) || (token.Lex.equals("MINUS")) || (token.Lex.equals("OVER")) || (token.Lex.equals("RPAREN")) || (token.Lex.equals("SEMI")) || (token.Lex.equals("COMMA")) || (token.Lex.equals("THEN")) || (token.Lex.equals("ELSE")) || (token.Lex.equals("FI")) || (token.Lex.equals("DO")) || (token.Lex.equals("ENDWH")) || (token.Lex.equals("END"))) {
        } else if (token.Lex.equals("LMIDPAREN")) {
            match("LMIDPAREN");
            t.child[0] = Exp();
            t.child[0].attr.expAttr.varkind = "ArrayMembV";
            match("RMIDPAREN");
        } else ReadNextToken();
    }

    void match(String expected) {
        if (token.Lex.equals(expected)) ReadNextToken(); else {
            syntaxError("not match error ");
            ReadNextToken();
        }
    }

    void syntaxError(String s) {
        serror = serror + "\n>>> ERROR :" + "Syntax error at " + String.valueOf(token.lineshow) + ": " + s;
        Error = true;
    }

    void ReadNextToken() {
        if (fenxi.hasMoreTokens()) {
            int i = 1;
            String stok = fenxi.nextToken();
            StringTokenizer fenxi1 = new StringTokenizer(stok, ":,");
            while (fenxi1.hasMoreTokens()) {
                String fstok = fenxi1.nextToken();
                if (i == 1) {
                    token.lineshow = Integer.parseInt(fstok);
                    lineno = token.lineshow;
                }
                if (i == 2) token.Lex = fstok;
                if (i == 3) token.Sem = fstok;
                i++;
            }
        }
    }

    TreeNode newNode(String s) {
        TreeNode t = new TreeNode();
        t.nodekind = s;
        t.lineno = lineno;
        return t;
    }

    TreeNode newStmtNode(String s) {
        TreeNode t = new TreeNode();
        t.nodekind = "StmtK";
        t.lineno = lineno;
        t.kind = s;
        return t;
    }

    TreeNode newExpNode(String s) {
        TreeNode t = new TreeNode();
        t.nodekind = "ExpK";
        t.kind = s;
        t.lineno = lineno;
        t.attr.expAttr = new ExpAttr();
        t.attr.expAttr.varkind = "IdV";
        t.attr.expAttr.type = "Void";
        return t;
    }

    void writeStr(String s) {
        stree = stree + s;
    }

    void writeSpace() {
        stree = stree + "  ";
    }

    void writeTab(int x) {
        stree = stree + "\n";
        while (x != 0) {
            stree = stree + "\t";
            x--;
        }
    }

    void printTree(TreeNode t, int l) {
        TreeNode tree = t;
        while (tree != null) {
            if (tree.nodekind.equals("ProcK")) {
                stree = "ProcK";
            } else if (tree.nodekind.equals("PheadK")) {
                writeTab(1);
                writeStr("PheadK");
                writeSpace();
                writeStr(tree.name[0]);
            } else if (tree.nodekind.equals("DecK")) {
                writeTab(l);
                writeStr("DecK");
                writeSpace();
                if (tree.attr.procAttr != null) {
                    if (tree.attr.procAttr.paramt.equals("varparamType")) writeStr("Var param:"); else if (tree.attr.procAttr.paramt.equals("valparamType")) writeStr("Value param:");
                }
                if (tree.kind.equals("ArrayK")) {
                    writeStr("ArrayK");
                    writeSpace();
                    writeStr(String.valueOf(tree.attr.arrayAttr.low));
                    writeSpace();
                    writeStr(String.valueOf(tree.attr.arrayAttr.up));
                    writeSpace();
                    if (tree.attr.arrayAttr.childtype.equals("CharK")) writeStr("CharK"); else if (tree.attr.arrayAttr.childtype.equals("IntegerK")) writeStr("IntegerK");
                } else if (tree.kind.equals("CharK")) writeStr("CharK"); else if (tree.kind.equals("IntegerK")) writeStr("IntegerK"); else if (tree.kind.equals("RecordK")) writeStr("RecordK"); else if (tree.kind.equals("IdK")) {
                    writeStr("IdK");
                    writeStr(tree.attr.type_name);
                } else syntaxError("error1!");
                if (tree.idnum != 0) for (int i = 0; i < (tree.idnum); i++) {
                    writeSpace();
                    writeStr(tree.name[i]);
                } else syntaxError("wrong!no var!");
            } else if (tree.nodekind.equals("TypeK")) {
                writeTab(l);
                writeStr("TypeK");
            } else if (tree.nodekind.equals("VarK")) {
                writeTab(l);
                writeStr("VarK");
            } else if (tree.nodekind.equals("ProcDecK")) {
                writeTab(l);
                writeStr("ProcDecK");
                writeSpace();
                writeStr(tree.name[0]);
            } else if (tree.nodekind.equals("StmLK")) {
                writeTab(l);
                writeStr("StmLK");
            } else if (tree.nodekind.equals("StmtK")) {
                writeTab(l);
                writeStr("StmtK");
                writeSpace();
                if (tree.kind.equals("IfK")) writeStr("If"); else if (tree.kind.equals("WhileK")) writeStr("While"); else if (tree.kind.equals("AssignK")) writeStr("Assign"); else if (tree.kind.equals("ReadK")) {
                    writeStr("Read");
                    writeSpace();
                    writeStr(tree.name[0]);
                } else if (tree.kind.equals("WriteK")) writeStr("Write"); else if (tree.kind.equals("CallK")) writeStr("Call"); else if (tree.kind.equals("ReturnK")) writeStr("Return"); else syntaxError("error2!");
            } else if (tree.nodekind.equals("ExpK")) {
                writeTab(l);
                writeStr("ExpK");
                if (tree.kind.equals("OpK")) {
                    writeSpace();
                    writeStr("Op");
                    writeSpace();
                    if (tree.attr.expAttr.op.equals("EQ")) writeStr("="); else if (tree.attr.expAttr.op.equals("LT")) writeStr("<"); else if (tree.attr.expAttr.op.equals("PLUS")) writeStr("+"); else if (tree.attr.expAttr.op.equals("MINUS")) writeStr("-"); else if (tree.attr.expAttr.op.equals("TIMES")) writeStr("*"); else if (tree.attr.expAttr.op.equals("OVER")) writeStr("/"); else syntaxError("error3!");
                } else if (tree.kind.equals("ConstK")) {
                    writeSpace();
                    writeStr("Const");
                    writeSpace();
                    writeStr(String.valueOf(tree.attr.expAttr.val));
                } else if (tree.kind.equals("VariK")) {
                    writeSpace();
                    writeStr("Vari");
                    writeSpace();
                    if (tree.attr.expAttr.varkind.equals("IdV")) {
                        writeStr("Id");
                        writeSpace();
                        writeStr(tree.name[0]);
                    } else if (tree.attr.expAttr.varkind.equals("FieldMembV")) {
                        writeStr("FieldMember");
                        writeSpace();
                        writeStr(tree.name[0]);
                    } else if (tree.attr.expAttr.varkind.equals("ArrayMembV")) {
                        writeStr("ArrayMember");
                        writeSpace();
                        writeStr(tree.name[0]);
                    } else syntaxError("var type error!");
                } else syntaxError("error4!");
            } else syntaxError("error5!");
            for (int i = 0; i < 3; i++) printTree(tree.child[i], l + 1);
            tree = tree.sibling;
        }
    }
}
