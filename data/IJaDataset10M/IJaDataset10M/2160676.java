package weiqi;

import java.util.Vector;
import common.Point;

/**
 * setFather/Son/Younger/Older ���������ڵ�֮�佨��˫�����ӣ������null���� ���ǲ���
 * ���ԭ�����е�˫�����ӡ�e.g.�Ѿ���n1.son=n2.���ڵ���n1.setSon(n3),��õ�n1.son=n3,
 * n3.father=n1, n2.father=n1
 * 
 */
public class SgfNode {

    Point point;

    SgfNode father, son, older, younger;

    String comment;

    Vector deadStones;

    int playerColor;

    public SgfNode() {
        this.reset();
    }

    public void reset() {
        playerColor = 0;
        point = null;
        father = null;
        son = null;
        older = null;
        younger = null;
        comment = null;
        deadStones = null;
    }

    public int getX() {
        return point.getX();
    }

    public int getY() {
        return point.getY();
    }

    public void setPoint(int x, int y) {
        setPoint(new Point(x, y));
    }

    public void setPoint(Point newPoint) {
        point = newPoint;
    }

    public SgfNode getOlder() {
        return older;
    }

    public SgfNode getFather() {
        return father;
    }

    public SgfNode searchFather() {
        if (father != null) return father;
        SgfNode bro = this.getOlder();
        while (bro != null) {
            if (bro.father != null) return bro.father;
            bro = bro.getOlder();
        }
        return null;
    }

    public void setFatherOnly(SgfNode newFather) {
        this.father = newFather;
    }

    public SgfNode getSon() {
        return son;
    }

    public void setSon(SgfNode newSon) {
        this.son = newSon;
        if (newSon != null) newSon.father = this;
    }

    public SgfNode getYounger() {
        return younger;
    }

    public void setYounger(SgfNode newYounger) {
        this.younger = newYounger;
        if (newYounger != null) newYounger.older = this;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String newComment) {
        comment = newComment;
    }

    public Point getPoint() {
        return point;
    }

    public SgfNode searchChildren(Point pt) {
        SgfNode son = this.getSon();
        while (son != null) {
            if (son.getPoint() != null) {
                if (son.getPoint().equals(pt)) return son;
            }
            son = son.getYounger();
        }
        return null;
    }

    public Vector getChildren() {
        Vector vec = new Vector();
        SgfNode son = this.getSon();
        while (son != null) {
            vec.addElement(son);
            son = son.getYounger();
        }
        return vec;
    }

    public Vector getDeadStones() {
        return deadStones;
    }

    public int getDeadColor() {
        return (int) (playerColor * -1);
    }

    public void setDeadStones(Vector deadStones) {
        this.deadStones = deadStones;
    }

    public SgfNode addBranch(Point pt) {
        SgfNode newSon = new SgfNode();
        newSon.setPoint(pt);
        if (this.son == null) {
            this.setSon(newSon);
        } else {
            SgfNode bro = this.getSon();
            while (bro.getYounger() != null) {
                bro = bro.getYounger();
            }
            bro.setYounger(newSon);
        }
        return newSon;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public void output(StringBuffer buf) {
        buf.append(';');
        if (this.point != null) {
            if (this.playerColor == 1) buf.append("B["); else buf.append("W[");
            buf.append(SgfModel.pointToString(this.getPoint()));
            buf.append(']');
        }
        if (this.comment != null) {
            buf.append("C[");
            buf.append(this.comment);
            buf.append(']');
        }
    }
}
