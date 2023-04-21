package edu.gsbme.MMLParser2.MathML.MEE.MathAST;

public class VectorExpr extends Expr {

    public List dataList;

    public VectorExpr() {
        dataList = new EmptyDataList();
    }

    public boolean isMatrix() {
        if (dataList instanceof EmptyDataList) return false;
        if (((DataList) dataList).D instanceof VectorExpr) return true; else return false;
    }

    public void insertData(Expr expr) {
        if (dataList instanceof EmptyDataList) {
            DataList newArg = new DataList(expr, dataList);
            dataList = newArg;
            newArg.parent = this;
        } else {
            DataList temp = (DataList) dataList;
            while (!(temp.DL instanceof EmptyDataList)) {
                temp = (DataList) temp.DL;
            }
            DataList newArg = new DataList(expr, temp.DL);
            temp.DL = newArg;
            newArg.parent = temp;
        }
    }

    public int length() {
        int length = 0;
        if (dataList instanceof EmptyDataList) {
            return 0;
        } else {
            DataList temp = (DataList) dataList;
            length = 1;
            while (!(temp.DL instanceof EmptyDataList)) {
                temp = (DataList) temp.DL;
                length++;
            }
        }
        return length;
    }

    @Override
    public Object visit(Visitor v, Object o) {
        return v.visitVectorExpr(this, o);
    }
}
