package jconsole.plugins.ui;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import org.junit.Test;

public class ConnectionPoolMonitorPanelTest {

    public static void main(final String[] args) {
        new ConnectionPoolMonitorPanelTest().test();
    }

    @SuppressWarnings("nls")
    final ConnectionPoolMonitorPanel panel = new ConnectionPoolMonitorPanel("title");

    @Test
    public void test() {
        final JFrame frame = new JFrame();
        frame.add(this.panel);
        frame.setSize(1000, 700);
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {

            @SuppressWarnings("nls")
            @Override
            public void run() {
                ConnectionPoolMonitorPanelTest.this.panel.addValues(new Timestamp(System.nanoTime()), "0", "1");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
