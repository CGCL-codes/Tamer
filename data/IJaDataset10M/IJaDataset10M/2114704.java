package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Accumulator;
import controller.RuntimeController;

public class AccumulatorView extends JComponent implements Observer {

    private static final long serialVersionUID = 4141822522543772501L;

    private final JLabel accumulator = new JLabel("0", null, JLabel.CENTER);

    public RuntimeController runtimeController;

    public AccumulatorView() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(makeContent());
        Accumulator.getInstance().addObserver(this);
    }

    private JComponent makeContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Accumulator"));
        accumulator.setFont(new Font("Helvetica", Font.BOLD, 15));
        panel.add(accumulator, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void update(Observable o, Object arg) {
        accumulator.setText(String.valueOf(arg));
    }
}
