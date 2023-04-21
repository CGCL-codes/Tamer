package largetic;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import largetic.Token.Identifier;

public class GenericTree {

    abstract static class Leaf implements Show {

        abstract void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException;
    }

    static class Type extends Leaf {

        public static Type getInstance(int d) {
            return null;
        }

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Debug.println(this + ".processInput()");
        }

        public void show(String ident) {
        }
    }

    static class PrimitiveType extends Type {

        private String name;

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Debug.println(this + ".processInput()");
            assert false;
            return;
        }

        PrimitiveType(String name) {
            this.name = name;
        }

        public void show(String ident) {
            System.err.println(ident + this + " " + name);
        }

        public String toString() {
            return "PrimitiveType";
        }
    }

    abstract static class AbstractStatement extends Statement {
    }

    static class AssignList extends AbstractStatement {

        List<Assign> list = new LinkedList<Assign>();

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Expression expr = (Expression) iTree;
            Expression exprL, exprR;
            Assign assign;
            Pair<Assign, Pair<Expression, Expression>> pair;
            LinkedList<Expression> todoExpr = new LinkedList<Expression>();
            todoExpr.add(expr);
            while (todoExpr.size() > 0) {
                expr = todoExpr.removeFirst();
                pair = Assign.getInstance(expr);
                assign = pair.first;
                exprL = pair.second.first;
                exprR = pair.second.second;
                if (exprL != null) {
                    todoExpr.add(exprL);
                    if (exprR != null) todoExpr.add(exprR);
                } else assert exprR == null;
                list.add(assign);
            }
        }

        public Value getLastValue() {
            return null;
        }

        public void show(String ident) {
        }
    }

    static class Member extends Leaf {

        private int type;

        private Identifier name;

        public int type() {
            return type;
        }

        public Identifier name() {
            return name;
        }

        Member(int typeId) {
            this.type = typeId;
        }

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Debug.println(this + ".processInput()");
            Declarator d = (Declarator) iTree;
            this.name = d.name();
            Debug.println(":: Create new Member; name = \"" + this.name.getString() + "\", typeId = \"" + type + "\" ::");
            Debug.println(":: --- ::");
        }

        public void show(String ident) {
            System.err.println(ident + this + " " + name.getString() + ":" + type);
        }

        public String toString() {
            return "Member";
        }
    }

    static class StructureType extends Type {

        private Identifier name;

        private List<Member> members = new LinkedList<Member>();

        public Identifier name() {
            return name;
        }

        public List<Member> members() {
            return members;
        }

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Debug.println(this + ".processInput()");
            InputTree.Structure its = (InputTree.Structure) iTree;
            InputTree.DeclarationBlock db = its.body();
            this.name = its.name();
            Debug.println(":: Create new Structure Type; name = \"" + this.name.getString() + "\", nameId = \"" + generizer.getTypeId(name.getString()) + "\" ::");
            for (InputTree.Declaration decl : db.getDeclarations()) {
                InputTree.Type itype = decl.type();
                int typeId = generizer.getTypeId(itype.name().getString());
                for (Declarator d : decl.declarators()) {
                    Member member = new Member(typeId);
                    member.processInput(d, generizer);
                    members.add(member);
                }
            }
            Debug.println(":: --- ::");
            if (db.getFunctionDecls().size() > 0) throw new GenerizerException("Function declaration is not allowed");
            if (db.getStructureDecls().size() > 0) throw new GenerizerException("Structre declaration is not allowed");
        }

        public void show(String ident) {
            System.err.println(ident + this + " " + name.getString());
            for (Member m : members) {
                m.show(ident + "  > ");
            }
        }

        public String toString() {
            return "StructureType";
        }
    }

    static class Parametr extends Leaf {

        private int typeId;

        private int varId;

        public int typeId() {
            return typeId;
        }

        public int varId() {
            return varId;
        }

        @Override
        void processInput(largetic.InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            InputTree.Declaration decl = (InputTree.Declaration) iTree;
            this.typeId = generizer.getTypeId(decl.type().name().getString());
            assert decl.declarators().size() == 1;
            String pName = decl.declarators().getFirst().name().getString();
            this.varId = generizer.registerName(pName, typeId);
        }

        public void show(String ident) {
            System.err.println(ident + this + " " + varId + ":" + typeId);
        }

        public String toString() {
            return "Parametr";
        }
    }

    static class DelegateType extends Type implements Comparable<DelegateType>, Show {

        private int retType;

        private Vector<Integer> params = new Vector<Integer>();

        public int retType() {
            return retType;
        }

        public Vector<Integer> params() {
            return params;
        }

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Debug.println(this + ".processInput()");
            InputTree.Function itf = (InputTree.Function) iTree;
            retType = generizer.getTypeId(itf.type().name().getString());
            for (InputTree.Declaration decl : itf.params().elements()) {
                Parametr p = new Parametr();
                p.processInput(decl, generizer);
            }
        }

        DelegateType() {
        }

        public void show(String ident) {
            System.err.println(ident + this + " delegate");
        }

        public String toString() {
            return "DelegateType";
        }

        public int compareTo(DelegateType o) {
            if (this.retType != o.retType()) return this.retType > o.retType() ? 1 : -1;
            if (this.params.size() != o.params().size()) return this.params.size() > o.params().size() ? 1 : -1;
            for (int i = 0; i < this.params.size(); i++) {
                if (this.params.get(i) != o.params().get(i)) return this.params.get(i) > o.params().get(i) ? 1 : -1;
            }
            return 0;
        }
    }

    abstract static class Value extends Leaf {

        private Type type;

        public Type type() {
            return type;
        }

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Debug.println(this + ".processInput()");
        }

        public void show(String ident) {
        }
    }

    static class ValueRhs extends Leaf {

        @Override
        void processInput(largetic.InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
        }

        public void show(String ident) {
        }
    }

    static class ValueLhs extends Leaf {

        @Override
        void processInput(largetic.InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
        }

        public void show(String ident) {
        }
    }

    static class Declaration extends Leaf {

        private int typeId;

        private ValueRhs initializer = null;

        private Identifier name;

        public int typeId() {
            return typeId;
        }

        public ValueRhs initializer() {
            return initializer;
        }

        public Identifier name() {
            return name;
        }

        Declaration(int typeId) {
            this.typeId = typeId;
        }

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Declarator d = (Declarator) iTree;
            this.name = d.name();
        }

        public void show(String ident) {
        }
    }

    abstract static class Statement extends Leaf {
    }

    static class Body extends Leaf {

        public Generizer.Names names = new Generizer.Names();

        public Generizer.Types types = new Generizer.Types();

        private List<Declaration> elements = new LinkedList<Declaration>();

        public List<Declaration> elements() {
            return elements;
        }

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            Debug.println(this + ".processInput()");
            assert iTree instanceof InputTree.DeclarationBlock;
            InputTree.DeclarationBlock db = (InputTree.DeclarationBlock) iTree;
            for (InputTree.Declaration itDecl : db.getDeclarations()) {
                InputTree.Type itType = itDecl.type();
                int typeId = generizer.getTypeId(itType.name().getString());
                for (Declarator d : itDecl.declarators()) {
                    Declaration decl = new Declaration(typeId);
                    decl.processInput(d, generizer);
                    generizer.registerName(d.name().getString(), typeId);
                    elements.add(decl);
                }
            }
        }

        public void show(String ident) {
            names.show(ident);
            types.show(ident);
        }

        public String toString() {
            return "Body";
        }
    }

    abstract static class GlobalStatement extends Statement {
    }

    static class GlobalBody extends GlobalStatement implements Show {

        private Body body = new Body();

        public Body body() {
            return body;
        }

        @Override
        void processInput(InputTree.Leaf iTree, Generizer generizer) throws GenerizerException {
            InputTree.DeclarationBlock gBlock = (InputTree.DeclarationBlock) iTree;
            List<Pair<InputTree.Structure, StructureType>> structsToProcess = new LinkedList<Pair<InputTree.Structure, StructureType>>();
            for (InputTree.Structure its : gBlock.getStructureDecls()) {
                Token.Identifier ident = its.name();
                StructureType st = new StructureType();
                generizer.registerTypeName(ident.getString(), st);
                structsToProcess.add(new Pair<InputTree.Structure, StructureType>(its, st));
            }
            for (Pair<InputTree.Structure, StructureType> pair : structsToProcess) {
                InputTree.Structure its = pair.first;
                StructureType st = pair.second;
                st.processInput(its, generizer);
            }
            for (InputTree.Function itf : gBlock.getFunctionDecls()) {
                Token.Identifier ident = itf.name();
                DelegateType dt = new DelegateType();
                int typeId = generizer.registerTypeName(null, dt);
                generizer.registerName(ident.getString(), typeId);
                dt.processInput(itf, generizer);
            }
        }

        public void show(String ident) {
            body.show(ident);
        }
    }

    public void processInput(InputTree inputTree, Generizer generizer) throws GenerizerException {
        Debug.println(this + ".processInput()");
        globalBody.processInput(inputTree.globalBlock(), generizer);
        current.processInput(inputTree.globalBlock(), generizer);
    }

    private GlobalBody globalBody;

    private Body currentBody;

    private Leaf current;

    public GlobalBody globalBody() {
        return globalBody;
    }

    public Body currentBody() {
        return currentBody;
    }

    GenericTree() {
        this.globalBody = new GlobalBody();
        this.currentBody = this.globalBody.body();
        this.current = this.currentBody;
    }

    public String toString() {
        return "GenericTree";
    }
}
