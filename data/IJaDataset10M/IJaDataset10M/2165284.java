package EDU.Washington.grad.noth.cda;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Vector;
import EDU.Washington.grad.gjb.cassowary.*;

public class AlignmentConstraint extends Constraint {

    protected Vector relConstrs;

    public static final int TOP_ALIGN = 0;

    public static final int BOTTOM_ALIGN = 1;

    public static final int LEFT_ALIGN = 2;

    public static final int RIGHT_ALIGN = 3;

    protected int align;

    protected SelPoint targetSP;

    public AlignmentConstraint(ClSimplexSolver solver, Vector ccVector, int alignment) {
        super(solver);
        int a;
        SelPoint sp;
        ConstrComponent cc;
        for (a = 0; a < ccVector.size(); a++) {
            cc = (ConstrComponent) ccVector.elementAt(a);
            addCC(cc);
            cc.addInterestedConstr(this);
        }
        align = alignment;
        targetSP = null;
        relConstrs = new Vector(1);
        addConstraints();
    }

    public void removeConstraints() {
        int a;
        ClLinearEquation cle;
        for (a = 0; a < relConstrs.size(); a++) {
            cle = (ClLinearEquation) relConstrs.elementAt(a);
            try {
                if (cle != null) solver.removeConstraint(cle);
            } catch (ExCLInternalError e) {
                System.out.println("AlignConstr.remConstr: ExCLInternalError " + "removing #" + a + " = " + cle);
            } catch (ExCLConstraintNotFound e) {
                System.out.println("AlignConstr.remConstr: ExCLConstraintNotFound " + "removing #" + a + " = " + cle);
            }
        }
        relConstrs.removeAllElements();
        targetSP = null;
    }

    public void replaceSelPoint(SelPoint oldsp, SelPoint newsp) {
    }

    public void notifyCCBBoxChange(ConstrComponent c) {
        ConstrComponent srcCC, targetCC;
        removeConstraints();
        addConstraints();
    }

    public boolean canDiscard() {
        if (ccList.size() < 2) return true;
        return false;
    }

    public void notifyCCRemoval(ConstrComponent c) {
        if (ccList.contains(c)) ccList.removeElement(c);
        if (c.selPoints.contains(targetSP)) {
            removeConstraints();
            addConstraints();
        }
    }

    public void draw(Graphics g) {
        ConstrComponent cc;
        int lowx, lowy, highx, highy, a, px, py;
        Rectangle bb;
        lowx = 5000;
        lowy = 5000;
        highx = -5000;
        highy = -5000;
        for (a = 0; a < ccList.size(); a++) {
            cc = (ConstrComponent) ccList.elementAt(a);
            bb = cc.bbox;
            switch(align) {
                case TOP_ALIGN:
                    px = bb.x;
                    py = bb.y;
                    break;
                case BOTTOM_ALIGN:
                    px = bb.x;
                    py = bb.y + bb.height;
                    break;
                case LEFT_ALIGN:
                    px = bb.x;
                    py = bb.y;
                    break;
                case RIGHT_ALIGN:
                    px = bb.x + bb.width;
                    py = bb.y;
                    break;
                default:
                    System.out.println("AlignConstr.draw:  Unknown type " + align);
                    px = 0;
                    py = 0;
            }
            if (lowx > px) lowx = px;
            if (lowy > py) lowy = py;
            if (highx < px) highx = px;
            if (highy < py) highy = py;
            switch(align) {
                case TOP_ALIGN:
                    px = bb.x + bb.width;
                    py = bb.y;
                    break;
                case BOTTOM_ALIGN:
                    px = bb.x + bb.width;
                    py = bb.y + bb.height;
                    break;
                case LEFT_ALIGN:
                    px = bb.x;
                    py = bb.y + bb.height;
                    break;
                case RIGHT_ALIGN:
                    px = bb.x + bb.width;
                    py = bb.y + bb.height;
                    break;
                default:
                    System.out.println("AlignConstr.draw:  Unknown type " + align);
                    px = 0;
                    py = 0;
            }
            if (lowx > px) lowx = px;
            if (lowy > py) lowy = py;
            if (highx < px) highx = px;
            if (highy < py) highy = py;
        }
        g.setColor(Color.gray);
        g.drawLine(lowx, lowy, highx, highy);
        bbox.x = lowx - 2;
        bbox.y = lowy - 2;
        bbox.width = (highx - lowx) + 4;
        bbox.height = (highy - lowy) + 4;
        if (isSelected) {
            g.setColor(CDA_G.DARK_GREEN);
            g.drawRect(bbox.x, bbox.y, bbox.width, bbox.height);
        } else if (isHighlighted) {
            g.setColor(CDA_G.DARK_BLUE);
            g.drawRect(bbox.x, bbox.y, bbox.width, bbox.height);
            g.drawRect(bbox.x - 1, bbox.y - 1, bbox.width + 2, bbox.height + 2);
        }
    }

    public void addConstraints() {
        SelPoint sp;
        ClLinearExpression cle;
        ClLinearEquation cleq;
        ConstrComponent cc;
        int minx, miny, maxx, maxy, a;
        minx = 5000;
        miny = 5000;
        maxx = -5000;
        maxy = -5000;
        if (relConstrs.size() != ccList.size()) {
            if (relConstrs.size() != 0) {
                System.out.println("AlignConstr.addConstr: relConstrs = " + relConstrs + ", should be empty!");
                relConstrs.removeAllElements();
            }
            relConstrs.setSize(ccList.size());
            for (a = 0; a < ccList.size(); a++) {
                cc = (ConstrComponent) ccList.elementAt(a);
                switch(align) {
                    case LEFT_ALIGN:
                        sp = cc.leftSP;
                        if (sp.x < minx) {
                            minx = sp.x;
                            targetSP = sp;
                        }
                        break;
                    case RIGHT_ALIGN:
                        sp = cc.rightSP;
                        if (sp.x > maxx) {
                            maxx = sp.x;
                            targetSP = sp;
                        }
                        break;
                    case TOP_ALIGN:
                        sp = cc.topSP;
                        if (sp.y < miny) {
                            miny = sp.y;
                            targetSP = sp;
                        }
                        break;
                    case BOTTOM_ALIGN:
                        sp = cc.bottomSP;
                        if (sp.y > maxy) {
                            maxy = sp.y;
                            targetSP = sp;
                        }
                        break;
                    default:
                        System.out.println("AlignConstr.addConstr: Unknown type " + align);
                        return;
                }
            }
            for (a = 0; a < ccList.size(); a++) {
                cc = (ConstrComponent) ccList.elementAt(a);
                switch(align) {
                    case LEFT_ALIGN:
                        sp = cc.leftSP;
                        try {
                            cle = new ClLinearExpression(sp.X());
                            cleq = new ClLinearEquation(targetSP.X(), cle);
                            relConstrs.setElementAt(cleq, a);
                            solver.addConstraint(cleq);
                        } catch (ExCLRequiredFailure e) {
                            System.out.println("AlignConstr.addCon: ExCLRequiredFailure!");
                        } catch (ExCLInternalError e) {
                            System.out.println("AlignConstr.addCon: ExCLInternalError!");
                        }
                        break;
                    case RIGHT_ALIGN:
                        sp = cc.rightSP;
                        try {
                            cle = new ClLinearExpression(sp.X());
                            cleq = new ClLinearEquation(targetSP.X(), cle);
                            relConstrs.setElementAt(cleq, a);
                            solver.addConstraint(cleq);
                        } catch (ExCLRequiredFailure e) {
                            System.out.println("AlignConstr.addCon: ExCLRequiredFailure!");
                        } catch (ExCLInternalError e) {
                            System.out.println("AlignConstr.addCon: ExCLInternalError!");
                        }
                        break;
                    case TOP_ALIGN:
                        sp = cc.topSP;
                        try {
                            cle = new ClLinearExpression(sp.Y());
                            cleq = new ClLinearEquation(targetSP.Y(), cle);
                            relConstrs.setElementAt(cleq, a);
                            solver.addConstraint(cleq);
                        } catch (ExCLRequiredFailure e) {
                            System.out.println("AlignConstr.addCon: ExCLRequiredFailure!");
                        } catch (ExCLInternalError e) {
                            System.out.println("AlignConstr.addCon: ExCLInternalError!");
                        }
                        break;
                    case BOTTOM_ALIGN:
                        sp = cc.bottomSP;
                        try {
                            cle = new ClLinearExpression(sp.Y());
                            cleq = new ClLinearEquation(targetSP.Y(), cle);
                            relConstrs.setElementAt(cleq, a);
                            solver.addConstraint(cleq);
                        } catch (ExCLRequiredFailure e) {
                            System.out.println("AlignConstr.addCon: ExCLRequiredFailure!");
                        } catch (ExCLInternalError e) {
                            System.out.println("AlignConstr.addCon: ExCLInternalError!");
                        }
                        break;
                }
            }
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("AlignConstr: targetSP = " + targetSP);
        sb.append("\nccList = ");
        for (int a = 0; a < ccList.size(); a++) {
            sb.append(ccList.elementAt(a) + "\n");
        }
        return sb.toString();
    }
}
