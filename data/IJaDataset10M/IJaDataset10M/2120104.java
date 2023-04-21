package foxtrot.examples;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JButton;
import foxtrot.Task;
import foxtrot.Worker;

/**
 * An example of how to create a Task that is interruptible. <p>
 * This is not provided by the Foxtrot API, because it is too much application dependent: what are
 * the correct actions to take when a Task is interrupted ? <br>
 * This implies that the Task must be collaborative, and check once in a while if it is interrupted
 * by another thread.
 *
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 126 $
 */
public class InterruptExample extends JFrame {

    private JButton button;

    private boolean running;

    private boolean taskInterrupted;

    public static void main(String[] args) {
        InterruptExample example = new InterruptExample();
        example.setVisible(true);
    }

    public InterruptExample() {
        super("Foxtrot Example");
        final String label = "Run Task !";
        button = new JButton(label);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onButtonClick(label);
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new GridBagLayout());
        c.add(button);
        setSize(300, 200);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = getSize();
        int x = (screen.width - size.width) >> 1;
        int y = (screen.height - size.height) >> 1;
        setLocation(x, y);
    }

    private void onButtonClick(final String label) {
        if (!running) {
            running = true;
            button.setText("Interrupt");
            try {
                ArrayList list = getData();
                if (list == null) {
                    return;
                } else {
                    javax.swing.DefaultComboBoxModel model = new javax.swing.DefaultComboBoxModel(list.toArray());
                }
            } catch (Exception x) {
                x.printStackTrace();
            } finally {
                button.setText(label);
                setTaskInterrupted(false);
                running = false;
            }
        } else {
            button.setText(label);
            setTaskInterrupted(true);
        }
    }

    private ArrayList getData() throws Exception {
        return (ArrayList) Worker.post(new Task() {

            public Object run() throws Exception {
                System.out.println("Task started...");
                ArrayList list = new ArrayList();
                for (int i = 0; i < 100; ++i) {
                    System.out.println("Heavy Operation number " + (i + 1));
                    Thread.sleep(250);
                    Object data = new Object();
                    list.add(data);
                    System.out.println("Checking if task is interrupted...");
                    if (isTaskInterrupted()) {
                        System.out.println("Task interrupted !");
                        break;
                    }
                    System.out.println("Task not interrupted, going on");
                }
                if (isTaskInterrupted()) {
                    list.clear();
                    return null;
                }
                return list;
            }
        });
    }

    private synchronized boolean isTaskInterrupted() {
        return taskInterrupted;
    }

    private synchronized void setTaskInterrupted(boolean value) {
        taskInterrupted = value;
    }
}
