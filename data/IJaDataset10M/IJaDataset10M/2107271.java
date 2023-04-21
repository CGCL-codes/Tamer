package Javalette.Absyn;

public class ItemArgDecl extends ArgDecl implements java.io.Serializable {

    public Type type_;

    public ListArrDec listarrdec_;

    public String cppident_;

    public ItemArgDecl(Type p1, ListArrDec p2, String p3) {
        type_ = p1;
        listarrdec_ = p2;
        cppident_ = p3;
    }

    public <R, A> R accept(Javalette.Absyn.ArgDecl.Visitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }
}
