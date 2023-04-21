package foxtrot.examples;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import foxtrot.Job;
import foxtrot.Task;
import foxtrot.Worker;

/**
 * @version $Revision: 260 $
 */
public class MultipleDialogExample extends JFrame {

    private JButton button;

    public static void main(String[] args) {
        MultipleDialogExample example = new MultipleDialogExample();
        example.setVisible(true);
    }

    public MultipleDialogExample() {
        super("Multiple Dialogs Example");
        button = new JButton("START");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

    private void start() {
        button.setText("RUNNING");
        Worker.post(new Job() {

            public Object run() {
                startThreads();
                return null;
            }
        });
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException x) {
            Thread.currentThread().interrupt();
        }
    }

    private void startThreads() {
        Thread t1 = new Thread() {

            public void run() {
                int result = foxtrotReportError("Thread 1");
                System.out.println("result 1 = " + result);
            }
        };
        Thread t2 = new Thread() {

            public void run() {
                int result = foxtrotReportError("Thread 2");
                System.out.println("result 2 = " + result);
            }
        };
        t1.start();
        t2.start();
    }

    private int reportError(final String message) {
        if (SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Cannot call this method from Event Dispatch Thread");
        try {
            System.out.println("entered report error");
            final IntegerHolder holder = new IntegerHolder();
            holder.setValue(-2);
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    System.out.println("entered invoke and wait");
                    sleep(500);
                    System.out.println("slept 500");
                    Component root = JOptionPane.getRootFrame();
                    int result = JOptionPane.showConfirmDialog(root, message);
                    holder.setValue(result);
                    System.out.println("exiting invoke and wait");
                }
            });
            System.out.println("exiting report error");
            return holder.getValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return -5;
    }

    private int foxtrotReportError(final String message) {
        if (SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Cannot call this method from Event Dispatch Thread");
        try {
            System.out.println("entered foxtrot report error");
            final IntegerHolder holder = new IntegerHolder();
            holder.setValue(-2);
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        System.out.println("entered invoke and wait 1");
                        Worker.post(new Task() {

                            public Object run() throws Exception {
                                System.out.println("entered task run " + this);
                                SwingUtilities.invokeAndWait(new Runnable() {

                                    public void run() {
                                        System.out.println("entered invoke and wait 2");
                                        sleep(500);
                                        System.out.println("slept 500");
                                        Component root = JOptionPane.getRootFrame();
                                        int result = JOptionPane.showConfirmDialog(root, message);
                                        holder.setValue(result);
                                        System.out.println("exiting invoke and wait 2");
                                    }
                                });
                                System.out.println("exiting task run " + this);
                                return null;
                            }
                        });
                        System.out.println("exiting invoke and wait 1");
                    } catch (Exception x) {
                        x.printStackTrace();
                    }
                }
            });
            System.out.println("exiting foxtrot report error");
            return holder.getValue();
        } catch (InterruptedException x) {
            x.printStackTrace();
        } catch (InvocationTargetException x) {
            x.printStackTrace();
        }
        return -5;
    }

    private int syncReportError(final String message) throws InterruptedException {
        if (SwingUtilities.isEventDispatchThread()) throw new IllegalStateException("Cannot call this method from Event Dispatch Thread");
        synchronized (Notifier.class) {
            while (Notifier.isBusy()) Notifier.class.wait();
        }
        try {
            Notifier.setBusy(true);
            final IntegerHolder holder = new IntegerHolder();
            holder.setValue(-2);
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    sleep(500);
                    System.out.println("slept 500");
                    Component root = JOptionPane.getRootFrame();
                    int result = JOptionPane.showConfirmDialog(root, message);
                    holder.setValue(result);
                }
            });
            return holder.getValue();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            Notifier.setBusy(false);
        }
        return -5;
    }

    public static class Notifier {

        private static boolean busy;

        public static synchronized boolean isBusy() {
            return busy;
        }

        public static synchronized void setBusy(boolean value) {
            busy = value;
            Notifier.class.notifyAll();
        }
    }

    public static class IntegerHolder {

        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
