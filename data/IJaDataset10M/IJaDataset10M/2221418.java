package javax.swing;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.Timer_MultithreadedTest.ConcreteActionListener;
import junit.framework.TestCase;

public class AbstractButton_MultithreadedTest extends TestCase {

    protected AbstractButton button = null;

    public AbstractButton_MultithreadedTest(final String str) {
        super(str);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        button = new JButton();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    class MyAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public Object performed = null;

        public int eventsCounter = 0;

        public MyAction(final String text, final Icon icon) {
            super(text, icon);
        }

        public void actionPerformed(final ActionEvent e) {
            synchronized (this) {
                eventsCounter++;
                performed = e;
                notifyAll();
            }
        }
    }

    ;

    public void testDoClickint1() throws InterruptedException {
        MyAction action = new MyAction(null, null);
        MyAction listener = new MyAction(null, null);
        button = new JButton(action);
        button.addActionListener(listener);
        synchronized (action) {
            button.doClick(50);
            action.wait(1000);
        }
        assertTrue("action performed", listener.performed != null);
        assertTrue("action performed", action.performed != null);
    }

    public void testDoClickint2() throws InterruptedException {
        ConcreteActionListener listener = new ConcreteActionListener();
        button = new JButton();
        button.addActionListener(listener);
        synchronized (listener) {
            button.doClick(1);
            button.doClick(1);
            button.doClick(1);
            button.doClick(1);
            button.doClick(1);
            listener.waitNumActions(1500, 5);
        }
        assertEquals(5, listener.getCounter());
    }

    public void testDoClick() {
        Icon icon = new ImageIcon(new BufferedImage(20, 20, BufferedImage.TYPE_BYTE_GRAY));
        String text = "texttext";
        MyAction action = new MyAction(text, icon);
        MyAction listener = new MyAction(text, icon);
        button = new JButton(action);
        button.addActionListener(listener);
        try {
            synchronized (action) {
                button.doClick();
                action.wait(1000);
            }
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
        assertTrue("action performed", listener.performed != null);
        assertTrue("action performed", action.performed != null);
    }
}
