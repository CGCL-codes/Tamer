package ui.size_structure;

import javax.swing.JPanel;
import javax.swing.*;
import math.Func;
import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;
import java.util.Random;
import dynamics.size_structure.SizeFrequency;
import dynamics.size_structure.SizeStructureObserver;

/**
 * @author umezawa
 *
 * ���̐������ꂽ�R�����g�̑}����e���v���[�g��ύX���邽��
 * �E�B���h�E > �ݒ� > Java > �R�[�h���� > �R�[�h�ƃR�����g
 */
public class GraphPanel extends JPanel implements SizeStructureObserver {

    double[] f;

    Func[] funca;

    LinkedList funcList;

    public GraphPanel() {
    }

    public GraphPanel(math.Func func) {
        this.funca = new Func[1];
        this.funca[0] = func;
    }

    public GraphPanel(Func[] funca) {
        this.funca = funca;
    }

    public void paintComponent(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(getWidth() / 10, 9 * getHeight() / 10);
        AffineTransform trns = new AffineTransform(1, 0, 0, -1, 0, 0);
        g2.transform(trns);
        Line2D line = new Line2D.Double(0.0, 0.0, 0.0, 9 * getHeight() / 10);
        g2.draw(line);
        Point2D start = new Point2D.Double(0, 0);
        Point2D end = new Point2D.Double(9 * getWidth() / 10, 0.0);
        line.setLine(start, end);
        g2.draw(line);
        if (funca != null) for (int i = 0; i < funca.length; ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                end.setLocation(5 * j, funca[i].calc(j) / 10);
                line.setLine(start, end);
                start.setLocation(end);
                g2.draw(line);
            }
            start.setLocation(0, 0);
        }
        if (funcList != null) {
            java.util.Iterator ite = funcList.iterator();
            Func func;
            while (ite.hasNext()) {
                func = (Func) ite.next();
                for (int j = 0; j < getWidth(); ++j) {
                    end.setLocation(5 * j, func.calc(j) / 10);
                    line.setLine(start, end);
                    start.setLocation(end);
                    g2.draw(line);
                }
                start.setLocation(0, 0);
            }
        }
    }

    /**
	 * @return
	 */
    public Func[] getFunca() {
        return funca;
    }

    /**
	 * @param funcs
	 */
    public void setFunca(Func[] funcs) {
        funca = funcs;
    }

    /**
	 * this function add to display list
	 * @param sizeFrequency
	 */
    public void addFunc(Func f) {
        if (funcList == null) {
            funcList = new LinkedList();
        }
        funcList.add(f);
        return;
    }

    /**
	 * @return
	 */
    public LinkedList getFuncList() {
        return funcList;
    }

    /**
	 * @param list
	 */
    public void setFuncList(LinkedList list) {
        funcList = list;
    }
}
