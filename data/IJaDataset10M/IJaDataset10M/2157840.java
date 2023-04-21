package edu.luc.cs.trull.demo.counter;

import java.awt.GridLayout;
import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A bounded counter component driven by a manual clock
 * (<a href="http://webpages.cs.luc.edu/~laufer/trull/latest/doc/javaws/demo1.jnlp">run this demo</a>).
 *
 * <PRE>
 * Demo1:
 *
 *     Counter("down", 10)
 * ||  ButtonComponent("tick")
 * ||  loop
 *         await TICK -> emit UP
 * ||  loop
 *         await OUCH -> emit RESET
 * ||  loop
 *         await PRESSED -> emit DOWN
 * </PRE>
 * <P>
 * <A HREF="../../../../demo/triveni/demo/counter/Demo1.java">view source</A>
 * <P>
 * <A HREF="doc-files/counter1.html">run applet</A>
 *
 * @see edu.luc.cs.trull.demo.counter.Counter
 * @see edu.luc.cs.trull.demo.counter.ButtonComponent
 */
public class Demo1 extends Applet implements EventLabels {

    public void init() {
        super.init();
        getContentPane().setLayout(new GridLayout(1, 0));
        VisualComponent counter;
        VisualComponent button;
        setComponent(new Composite(new Component[] { counter = new Counter(DOWN, 10, 100, 40), button = new ButtonComponent(TICK), new Loop(new AwaitOne(TICK, new Emit(UP))), new Loop(new AwaitOne(OUCH, new Emit(RESET))), new Loop(new AwaitOne(PRESSED, new Emit(DOWN))) }));
        getContentPane().add(counter.getView());
        getContentPane().add(button.getView());
    }

    public static void main(String[] args) {
        runInFrame("Demo1", new Demo1());
    }
}
