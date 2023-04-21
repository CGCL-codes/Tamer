package de.berndsteindorff.junittca.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;
import org.junit.runner.notification.Failure;
import de.berndsteindorff.junittca.TcaMain;
import de.berndsteindorff.junittca.model.IProjectModel;
import de.berndsteindorff.junittca.model.Project;
import de.berndsteindorff.junittca.model.Run;
import de.berndsteindorff.junittca.view.observer.TcaEvent;
import de.berndsteindorff.junittca.view.observer.TcaEventCommands;
import de.berndsteindorff.junittca.view.observer.TcaEventListener;

/**
 * View-class for a center-panel with a Run.
 * 
 * @author Bernd Steindorff
 */
public class CenterPanelRun extends JPanel implements TcaEventListener {

    private final Logger logger = Logger.getLogger(this.getClass());

    private final IProjectModel model;

    private JLabel lResultShort = null;

    private JLabel lResult = null;

    private JTextArea tResult = null;

    private JScrollPane scrollpane = null;

    private Run run = null;

    /**
	 * Creates the center-panel with a Run.
	 * 
	 * @param model
	 */
    public CenterPanelRun(IProjectModel model) {
        this.model = model;
        logger.setLevel(TcaMain.LOGLEVEL);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY), "central-panel - result of a test-run"));
        lResultShort = new JLabel("should not be seen");
        add(lResultShort, new GBC(0, 0));
        lResult = new JLabel("failure");
        lResult.setForeground(Color.RED);
        add(lResult, new GBC(0, 1).setInsets(20, 0, 0, 0));
        tResult = new JTextArea(25, 50);
        tResult.setLineWrap(true);
        tResult.setText("should not be seen");
        tResult.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        scrollpane = new JScrollPane(tResult, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.setPreferredSize(new Dimension(300, 550));
        add(scrollpane, new GBC(0, 2, 0, 10).setFill(GBC.HORIZONTAL).setWeight(100, 0));
    }

    public void setRun(Run run) {
        this.run = run;
        setRun();
        repaint();
    }

    @Override
    public void sendEvent(TcaEvent e) {
        String ename = e.getEventName();
        if (ename.equals(TcaEventCommands.UPDATERUN)) {
            logger.info(ename);
            Project project = model.getProject();
            run = project.getTestRuns().get(0);
            setRun();
            repaint();
        }
    }

    private void setRun() {
        logger.info("setRun()");
        StringBuffer sb = new StringBuffer();
        sb.append("result run: ");
        Calendar cal = new GregorianCalendar();
        cal.setTime(run.getRunDate());
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int m = cal.get(Calendar.MONTH) + 1;
        int y = cal.get(Calendar.YEAR);
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        sb.append(d > 9 ? d : "0" + d);
        sb.append(".");
        sb.append(m > 9 ? m : "0" + m);
        sb.append(".");
        sb.append("0" + (y - 2000) + " ");
        sb.append(h > 9 ? h : "0" + h);
        sb.append(":");
        sb.append(min > 9 ? min : "0" + min);
        sb.append(":");
        sb.append(s > 9 ? s : "0" + s);
        sb.append("                 class: ");
        sb.append(run.getClassMethods().size());
        sb.append(",  tests: " + run.getResult().getRunCount());
        sb.append(",  ignored: " + run.getResult().getIgnoreCount());
        sb.append(",  ok: " + (run.getResult().getRunCount() - run.getResult().getFailureCount()));
        sb.append(",  failure: " + (run.getResult().getFailureCount()));
        lResultShort.setText(sb.toString());
        if (run.getResult().getFailureCount() == 0) {
            lResult.setForeground(Color.BLACK);
            tResult.setText("no failure");
        } else {
            sb = new StringBuffer();
            for (Failure f : run.getResult().getFailures()) {
                lResult.setForeground(Color.RED);
                int index = f.getTestHeader().indexOf('(');
                String method = f.getTestHeader().substring(0, index);
                String clazz = f.getTestHeader().substring(index);
                sb.append(method + "\t" + clazz + ":\n");
                sb.append(f.getMessage() + "\n");
                sb.append("------------------------------------------------------------------------------------\n");
            }
            tResult.setText(sb.toString());
        }
    }
}
