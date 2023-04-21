package gov.sns.jclass;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import gov.sns.tools.swing.*;
import com.klg.jclass.chart.beans.SimpleChart;

public class PopupMenuHandler implements ActionListener {

    public PopupMenuHandler(SimpleChart chart) {
        this.chart = chart;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == "Zoom Restore") chart.reset(); else if (command == "Strip Delta") {
            _deltaPopup.setValue(((StripChartDataSource) chart.getDataView(0).getDataSource()).getStripDelta());
            _deltaPopup.setVisible(true);
        } else if (command == "Strip Skip") {
            _skipPopup.setValue(((StripChartDataSource) chart.getDataView(0).getDataSource()).getStripSkip());
            _skipPopup.setVisible(true);
        }
    }

    private SimpleChart chart;

    private EntryPopup _deltaPopup = new EntryPopup("Strip chart delta", "New Delta Value: ");

    private EntryPopup _skipPopup = new EntryPopup("Strip chart skip", "New Skip Value: ");

    /** entry popup class for setting strip skip and strip delta */
    protected class EntryPopup extends JDialog {

        EntryPopup(String title, String label) {
            enableEvents(AWTEvent.WINDOW_EVENT_MASK);
            initialize(title, label);
        }

        private void initialize(String title, String label) {
            _contentPane = (JPanel) getContentPane();
            titledBorder1 = new TitledBorder("Dialog Window");
            _contentPane.setLayout(borderLayout3);
            this.setSize(new Dimension(400, 111));
            this.setTitle(title);
            _title.setHorizontalAlignment(SwingConstants.CENTER);
            _title.setText(title);
            _buttonPanel.setLayout(borderLayout1);
            _okButton.setAlignmentX((float) 0.5);
            _okButton.setText("OK");
            _okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    valueChanged();
                    setVisible(false);
                }
            });
            _cancelButton.setText("Cancel");
            _cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
            _entryField.setValue(0.0);
            _label.setText("  " + label);
            _topPanel.setLayout(borderLayout2);
            borderLayout3.setHgap(5);
            borderLayout3.setVgap(10);
            borderLayout2.setVgap(10);
            _contentPane.add(_topPanel, BorderLayout.CENTER);
            _topPanel.add(_label, BorderLayout.WEST);
            _topPanel.add(_entryField, BorderLayout.CENTER);
            _topPanel.add(_title, BorderLayout.NORTH);
            _contentPane.add(_buttonPanel, BorderLayout.SOUTH);
            _buttonPanel.add(_okButton, BorderLayout.WEST);
            _buttonPanel.add(_cancelButton, BorderLayout.EAST);
        }

        public void valueChanged() {
            if (this == _deltaPopup) ((StripChartDataSource) chart.getDataView(0).getDataSource()).setStripDelta(_deltaPopup.getValue()); else if (this == _skipPopup) ((StripChartDataSource) chart.getDataView(0).getDataSource()).setStripSkip((int) _skipPopup.getValue());
        }

        public void setVisible(boolean isVisible) {
            if (isVisible == true) _oldValue = _entryField.getValue();
            super.setVisible(isVisible);
        }

        protected void processWindowEvent(WindowEvent e) {
            super.processWindowEvent(e);
        }

        public void setValue(double value) {
            _entryField.setValue(value);
        }

        public double getValue() {
            return _entryField.getValue();
        }

        private JPanel _contentPane = null;

        JLabel _title = new JLabel();

        JPanel _buttonPanel = new JPanel();

        JButton _okButton = new JButton();

        JButton _cancelButton = new JButton();

        JPanel _topPanel = new JPanel();

        DecimalField _entryField = new DecimalField();

        JLabel _label = new JLabel();

        BorderLayout borderLayout2 = new BorderLayout();

        BorderLayout borderLayout3 = new BorderLayout();

        TitledBorder titledBorder1;

        BorderLayout borderLayout1 = new BorderLayout();

        double _oldValue = 0.0;
    }
}
