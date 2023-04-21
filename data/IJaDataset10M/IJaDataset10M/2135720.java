package versusSNP.gui.widgets;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import versusSNP.Size;
import versusSNP.util.swing.SwingUtils;

public class ProgressBar extends JFrame {

    private static final long serialVersionUID = 552585463219460845L;

    private JProgressBar progressBar;

    private JLabel label;

    private int pos = 0;

    public ProgressBar(String title) {
        super(title);
        this.label = new JLabel();
        this.progressBar = new JProgressBar(0, 100);
        setLayout(new GridLayout(2, 1));
        getContentPane().add(this.label);
        getContentPane().add(progressBar);
        setSize(Size.progress_bar);
        SwingUtils.centerScreen(this);
    }

    public ProgressBar(String title, boolean isIndeterminate) {
        this(title);
        progressBar.setIndeterminate(true);
    }

    public ProgressBar(String title, String label) {
        this(title);
        setLabel(label);
    }

    public ProgressBar(String title, String label, boolean isIndeterminate) {
        this(title);
        setLabel(label);
        progressBar.setIndeterminate(true);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public void setLabelAntTitle(String label, String title) {
        setLabel(label);
        setTitle(title);
    }

    public void stepIt() {
        if (pos < 100) progressBar.setValue(++pos);
    }

    public void stepBackToBegin() {
        pos = 0;
        progressBar.setValue(0);
    }
}
