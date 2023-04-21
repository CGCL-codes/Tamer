package view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class SummaryPanel extends JPanel {

    TotalTextArea totalTextArea;

    ViewTextArea viewCountsTextArea;

    OptTextArea viewThroughputTextArea;

    public SummaryPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        totalTextArea = new TotalTextArea();
        totalTextArea.setBorder(BorderFactory.createTitledBorder("Totals"));
        viewCountsTextArea = new ViewTextArea();
        viewCountsTextArea.setBorder(BorderFactory.createTitledBorder("View counts"));
        viewThroughputTextArea = new OptTextArea();
        viewThroughputTextArea.setBorder(BorderFactory.createTitledBorder("View throughput [Mbit/sec]"));
        add(viewThroughputTextArea);
        add(viewCountsTextArea);
        add(totalTextArea);
    }

    public class TotalTextArea extends JTextArea {

        public TotalTextArea() {
            setTabSize(10);
            setEditable(false);
            setCaretPosition(0);
        }
    }

    public class ViewTextArea extends JTextArea {

        public ViewTextArea() {
            setTabSize(12);
            setEditable(false);
            setCaretPosition(0);
        }
    }

    public class OptTextArea extends JTextArea {

        public OptTextArea() {
            setTabSize(12);
            setEditable(false);
            setCaretPosition(0);
        }
    }

    public void clear() {
        totalTextArea.setText("");
        viewCountsTextArea.setText("");
        viewThroughputTextArea.setText("");
    }
}
