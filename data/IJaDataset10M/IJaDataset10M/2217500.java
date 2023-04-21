package engine.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import engine.World;

/**
 * A collection of physics demonstrations in a tidy box
 * 
 * @author Kevin Glass
 */
public class DemoBox extends AbstractDemo {

    /** The list of demos */
    private ArrayList<AbstractDemo> demos = new ArrayList<AbstractDemo>();

    /** The demo currently being played */
    private AbstractDemo currentDemo;

    /** The index of the current demo */
    private int current = 0;

    /**
	 * Create a new collection of demos
	 */
    public DemoBox() {
        super("Phys2D Demo Box");
    }

    /**
	 * Add a demo to the box
	 * 
	 * @param demo The demo to add
	 */
    public void add(AbstractDemo demo) {
        demos.add(demo);
    }

    /**
	 * @see engine.test.AbstractDemo#update()
	 */
    protected void update() {
        currentDemo.update();
    }

    /**
	 * @see engine.test.AbstractDemo#keyHit(char)
	 */
    public void keyHit(char c) {
        super.keyHit(c);
        if (c == 'n') {
            current++;
            if (current >= demos.size()) {
                current = 0;
            }
            needsReset = true;
        }
        if (c == 'p') {
            current--;
            if (current < 0) {
                current = demos.size() - 1;
            }
            needsReset = true;
        }
        currentDemo.keyHit(c);
    }

    /**
	 * @see engine.test.AbstractDemo#init(engine.World)
	 */
    protected void init(World world) {
        if (demos.size() == 0) {
            frame.setVisible(false);
            JOptionPane.showMessageDialog(null, "No Demos specified!");
            System.exit(0);
        }
        currentDemo = demos.get(current);
        currentDemo.init(world);
    }

    /**
	 * @see engine.test.AbstractDemo#renderGUI(java.awt.Graphics2D)
	 */
    protected void renderGUI(Graphics2D g) {
        g.setColor(Color.black);
        g.drawString("N - Next Demo", 15, 450);
        g.drawString("P - Previous Demo", 15, 470);
        currentDemo.renderGUI(g);
        g.setFont(g.getFont().deriveFont(Font.BOLD));
        g.drawString(currentDemo.getTitle(), 300, 470);
    }

    /**
	 * Entry point for the demo box
	 * 
	 * @param argv The arguments to the demo box
	 */
    public static void main(String[] argv) {
        DemoBox box = new DemoBox();
        box.add(new Demo01());
        box.add(new Demo02());
        box.add(new Demo03());
        box.add(new Demo05());
        box.add(new Demo06());
        box.add(new Demo07());
        box.add(new Demo08());
        box.add(new Demo09());
        box.add(new Demo10());
        box.add(new Demo11());
        box.add(new Demo12());
        box.add(new Demo13());
        box.add(new Demo14());
        box.add(new Demo15());
        box.add(new Demo16());
        box.add(new Demo17());
        box.add(new Demo19());
        box.add(new Demo20());
        box.add(new AllShapesDemo());
        box.start();
    }
}
