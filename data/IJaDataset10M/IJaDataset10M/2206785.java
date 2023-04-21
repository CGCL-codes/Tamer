package hu.csq.dyneta.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tamás Cséri
 */
public class LongCalculationExecuterTest {

    class TestCalcultaion implements LongCalculation<String> {

        private volatile boolean toRun = true;

        public void interrupt() {
            toRun = false;
        }

        public String calc(LongCalculationCallback cb) {
            int time = 15000;
            cb.setString("Testing long calculation");
            cb.setMaxTick(100);
            for (int i = 0; i < 100; i++) {
                if (!toRun) return "Error";
                try {
                    Thread.sleep(time / 100);
                } catch (InterruptedException ex) {
                }
                cb.tick();
            }
            return "Result";
        }

        public String calcString() {
            return "Test calculation";
        }
    }

    /**
     * Test of execute method, of class LongCalculationExecuter.
     */
    @Test
    public void testExecute() {
        System.out.println("execute");
        final JDialog window = new JDialog((java.awt.Frame) null, "Test window, press the button", true);
        JButton button = new JButton("Press this button");
        window.add(button);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                LongCalculation<String> calc = new TestCalcultaion();
                LongCalculationExecuter instance = new LongCalculationExecuter();
                Object expResult = "Result";
                Object result = instance.execute(calc, true, true, null);
                assertEquals(expResult, result);
                window.dispose();
            }
        });
        window.pack();
        window.setVisible(true);
    }
}
