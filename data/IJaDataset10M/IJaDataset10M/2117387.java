package EDU.Washington.grad.gjb.cassowary_demos;

import java.awt.*;
import java.applet.*;
import java.util.Vector;
import EDU.Washington.grad.gjb.cassowary.*;

public class QuadDemo extends Applet {

    DraggableBox db[];

    DraggableBox mp[];

    int dbDragging;

    ClSimplexSolver solver;

    public void init() {
        solver = new ClSimplexSolver();
        dbDragging = -1;
        db = new DraggableBox[8];
        mp = new DraggableBox[4];
        int a;
        for (a = 0; a < 8; a++) db[a] = new DraggableBox(a);
        for (a = 0; a < 4; a++) mp[a] = db[a + 4];
        db[0].SetCenter(5, 5);
        db[1].SetCenter(5, 200);
        db[2].SetCenter(200, 200);
        db[3].SetCenter(200, 5);
        try {
            solver.addPointStay(db[0].CenterPt(), 1.0);
            solver.addPointStay(db[1].CenterPt(), 2.0);
            solver.addPointStay(db[2].CenterPt(), 4.0);
            solver.addPointStay(db[3].CenterPt(), 8.0);
            ClLinearExpression cle;
            ClLinearEquation cleq;
            cle = new ClLinearExpression(db[0].X());
            cle = (cle.plus(db[1].X())).divide(2);
            cleq = new ClLinearEquation(mp[0].X(), cle);
            solver.addConstraint(cleq);
            cle = new ClLinearExpression(db[0].Y());
            cle = (cle.plus(db[1].Y())).divide(2);
            cleq = new ClLinearEquation(mp[0].Y(), cle);
            solver.addConstraint(cleq);
            cle = new ClLinearExpression(db[1].X());
            cle = (cle.plus(db[2].X())).divide(2);
            cleq = new ClLinearEquation(mp[1].X(), cle);
            solver.addConstraint(cleq);
            cle = new ClLinearExpression(db[1].Y());
            cle = (cle.plus(db[2].Y())).divide(2);
            cleq = new ClLinearEquation(mp[1].Y(), cle);
            solver.addConstraint(cleq);
            cle = new ClLinearExpression(db[2].X());
            cle = (cle.plus(db[3].X())).divide(2);
            cleq = new ClLinearEquation(mp[2].X(), cle);
            solver.addConstraint(cleq);
            cle = new ClLinearExpression(db[2].Y());
            cle = (cle.plus(db[3].Y())).divide(2);
            cleq = new ClLinearEquation(mp[2].Y(), cle);
            solver.addConstraint(cleq);
            cle = new ClLinearExpression(db[3].X());
            cle = (cle.plus(db[0].X())).divide(2);
            cleq = new ClLinearEquation(mp[3].X(), cle);
            solver.addConstraint(cleq);
            cle = new ClLinearExpression(db[3].Y());
            cle = (cle.plus(db[0].Y())).divide(2);
            cleq = new ClLinearEquation(mp[3].Y(), cle);
            solver.addConstraint(cleq);
            cle = CL.Plus(db[0].X(), 10);
            solver.addConstraint(new ClLinearInequality(cle, CL.LEQ, db[2].X())).addConstraint(new ClLinearInequality(cle, CL.LEQ, db[3].X()));
            cle = CL.Plus(db[1].X(), 10);
            solver.addConstraint(new ClLinearInequality(cle, CL.LEQ, db[2].X())).addConstraint(new ClLinearInequality(cle, CL.LEQ, db[3].X()));
            cle = CL.Plus(db[0].Y(), 10);
            solver.addConstraint(new ClLinearInequality(cle, CL.LEQ, db[1].Y())).addConstraint(new ClLinearInequality(cle, CL.LEQ, db[2].Y()));
            cle = CL.Plus(db[3].Y(), 10);
            solver.addConstraint(new ClLinearInequality(cle, CL.LEQ, db[1].Y())).addConstraint(new ClLinearInequality(cle, CL.LEQ, db[2].Y()));
            int width = getSize().width;
            int height = getSize().height;
            solver.addConstraint(new ClLinearInequality(db[0].X(), CL.GEQ, 0.0));
            solver.addConstraint(new ClLinearInequality(db[0].Y(), CL.GEQ, 0.0));
            solver.addConstraint(new ClLinearInequality(db[1].X(), CL.GEQ, 0.0));
            solver.addConstraint(new ClLinearInequality(db[1].Y(), CL.GEQ, 0.0));
            solver.addConstraint(new ClLinearInequality(db[2].X(), CL.GEQ, 0.0));
            solver.addConstraint(new ClLinearInequality(db[2].Y(), CL.GEQ, 0.0));
            solver.addConstraint(new ClLinearInequality(db[3].X(), CL.GEQ, 0.0));
            solver.addConstraint(new ClLinearInequality(db[3].Y(), CL.GEQ, 0.0));
            solver.addConstraint(new ClLinearInequality(db[0].X(), CL.LEQ, width));
            solver.addConstraint(new ClLinearInequality(db[0].Y(), CL.LEQ, height));
            solver.addConstraint(new ClLinearInequality(db[1].X(), CL.LEQ, width));
            solver.addConstraint(new ClLinearInequality(db[1].Y(), CL.LEQ, height));
            solver.addConstraint(new ClLinearInequality(db[2].X(), CL.LEQ, width));
            solver.addConstraint(new ClLinearInequality(db[2].Y(), CL.LEQ, height));
            solver.addConstraint(new ClLinearInequality(db[3].X(), CL.LEQ, width));
            solver.addConstraint(new ClLinearInequality(db[3].Y(), CL.LEQ, height));
        } catch (ExCLInternalError e) {
            System.out.println("constructor: CLInternalError!");
        } catch (ExCLRequiredFailure e) {
            System.out.println("constructor: CLRequiredFailure!");
        } catch (ExCLNonlinearExpression e) {
            System.out.println("constructor: CLNonlinearExpression!");
        }
    }

    public QuadDemo() {
        super();
    }

    public boolean mouseDown(Event e, int x, int y) {
        for (int a = 0; a < db.length; a++) {
            if (db[a].Contains(x, y)) {
                dbDragging = a;
                break;
            }
        }
        repaint();
        if (dbDragging != -1) {
            try {
                solver.addEditVar(db[dbDragging].X()).addEditVar(db[dbDragging].Y()).beginEdit();
            } catch (ExCLInternalError ex) {
                System.err.println("mouseDown: CLInternalError!");
                System.err.println(ex.description());
            }
        }
        return true;
    }

    public boolean mouseUp(Event e, int x, int y) {
        if (dbDragging != -1) {
            try {
                dbDragging = -1;
                solver.endEdit();
                repaint();
            } catch (ExCLInternalError ex) {
                System.err.println("mouseUp: CLInternalError!");
                System.err.println(ex.description());
            }
        }
        return true;
    }

    public boolean mouseDrag(Event e, int x, int y) {
        if (dbDragging != -1) {
            try {
                solver.suggestValue(db[dbDragging].X(), x).suggestValue(db[dbDragging].Y(), y).resolve();
            } catch (ExCLInternalError ex) {
                System.out.println("mouseDrag: CLInternalError!");
            } catch (ExCLError ex) {
                System.out.println("mouseDrag: CLError!");
            }
            repaint();
        }
        return true;
    }

    public void paint(Graphics g) {
        g.drawLine((int) db[0].CenterX(), (int) db[0].CenterY(), (int) db[1].CenterX(), (int) db[1].CenterY());
        g.drawLine((int) db[1].CenterX(), (int) db[1].CenterY(), (int) db[2].CenterX(), (int) db[2].CenterY());
        g.drawLine((int) db[2].CenterX(), (int) db[2].CenterY(), (int) db[3].CenterX(), (int) db[3].CenterY());
        g.drawLine((int) db[3].CenterX(), (int) db[3].CenterY(), (int) db[0].CenterX(), (int) db[0].CenterY());
        g.drawLine((int) mp[0].CenterX(), (int) mp[0].CenterY(), (int) mp[1].CenterX(), (int) mp[1].CenterY());
        g.drawLine((int) mp[1].CenterX(), (int) mp[1].CenterY(), (int) mp[2].CenterX(), (int) mp[2].CenterY());
        g.drawLine((int) mp[2].CenterX(), (int) mp[2].CenterY(), (int) mp[3].CenterX(), (int) mp[3].CenterY());
        g.drawLine((int) mp[3].CenterX(), (int) mp[3].CenterY(), (int) mp[0].CenterX(), (int) mp[0].CenterY());
        for (int a = 0; a < 8; a++) db[a].draw(g);
        if (dbDragging != -1) {
            g.setColor(Color.blue);
            db[dbDragging].draw(g);
            g.setColor(Color.black);
        }
        g.setColor(Color.black);
    }

    public static void main(String[] argv) {
        System.err.println("Run using `appletviewer quaddemo.htm'");
    }
}
