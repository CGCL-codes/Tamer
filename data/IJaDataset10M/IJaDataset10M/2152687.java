package JaCoP.fz;

public class ASTVarDeclItem extends SimpleNode {

    public ASTVarDeclItem(int id) {
        super(id);
    }

    public ASTVarDeclItem(Parser p, int id) {
        super(p, id);
    }

    String id;

    int kind;

    int index1, index2;

    void setId(String ID) {
        id = ID;
    }

    String getIdent() {
        return id;
    }

    void setIndexes(int i1, int i2) {
        index1 = i1;
        index2 = i2;
    }

    int getLowIndex() {
        return index1;
    }

    int getHighIndex() {
        return index2;
    }

    void setKind(int t) {
        kind = t;
    }

    int getKind() {
        return kind;
    }

    public String toString() {
        String limits = "";
        if (kind > 1) limits = "[" + index1 + ".." + index2 + "]";
        String kindS = null;
        switch(kind) {
            case 0:
                kindS = "(var): ";
                break;
            case 1:
                kindS = "(non-var): ";
                break;
            case 2:
                kindS = "(array-var): ";
                break;
            case 3:
                kindS = "(array-non-var): ";
                break;
        }
        return super.toString() + kindS + id + limits;
    }
}
