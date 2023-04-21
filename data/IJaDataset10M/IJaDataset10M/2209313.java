package gui;

import i18n.I18NHelper;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import javax.swing.AbstractAction;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.MaskFormatter;
import tools.CoordinateUtilities;
import tools.ImageProvider;
import control.ErrorHandler;
import data.coordinate.LatLon;

public class GotoCoordinateDialog {

    public static enum Status {

        OK, CANCEL
    }

    private Status selectedValue;

    private LatLon coordinate;

    private JDialog dialog;

    private JTextField decLatitudeTextField;

    private JTextField decLongitudeTextField;

    private JFormattedTextField degLatitudeTextField;

    private JFormattedTextField degLongitudeTextField;

    private JButton okButton;

    private JButton cancelButton;

    public GotoCoordinateDialog(JFrame owner) {
        initializeComponents(owner);
        initCloseListener();
    }

    private void initializeComponents(JFrame owner) {
        dialog = new JDialog();
        dialog.setTitle(I18NHelper.getInstance().getString("dialog.goto.title"));
        dialog.setModal(true);
        dialog.setSize(300, 160);
        dialog.setIconImage(ImageProvider.getImage("jagme"));
        dialog.setLocationRelativeTo(owner);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        JPanel tabOnePanel = new JPanel();
        tabOnePanel.setLayout(new GridLayout(3, 2));
        tabOnePanel.add(new JLabel(String.format("%s:", I18NHelper.getInstance().getString("dialog.goto.latitude"))));
        decLatitudeTextField = new JTextField();
        decLatitudeTextField.setInputVerifier(decimalCoordinateInputVerifier);
        decLatitudeTextField.addFocusListener(focusListener);
        tabOnePanel.add(decLatitudeTextField);
        tabOnePanel.add(new JLabel(String.format("%s:", I18NHelper.getInstance().getString("dialog.goto.longitude"))));
        decLongitudeTextField = new JTextField();
        decLongitudeTextField.setInputVerifier(decimalCoordinateInputVerifier);
        decLongitudeTextField.addFocusListener(focusListener);
        tabOnePanel.add(decLongitudeTextField);
        JPanel tabTwoPanel = new JPanel();
        tabTwoPanel.setLayout(new GridLayout(3, 2));
        MaskFormatter latDegMinSecFormatter = null;
        MaskFormatter lonDegMinSecFormatter = null;
        try {
            latDegMinSecFormatter = new MaskFormatter("##'°##''##'.####'''' U");
            latDegMinSecFormatter.setValidCharacters("0123456789NS");
            lonDegMinSecFormatter = new MaskFormatter("##'°##''##'.####'''' U");
            lonDegMinSecFormatter.setValidCharacters("0123456789EW");
        } catch (ParseException e) {
            ErrorHandler.handleFatalError(e);
        }
        tabTwoPanel.add(new JLabel(String.format("%s:", I18NHelper.getInstance().getString("dialog.goto.latitude"))));
        degLatitudeTextField = new JFormattedTextField(latDegMinSecFormatter);
        degLatitudeTextField.addFocusListener(focusListener);
        tabTwoPanel.add(degLatitudeTextField);
        tabTwoPanel.add(new JLabel(String.format("%s:", I18NHelper.getInstance().getString("dialog.goto.longitude"))));
        degLongitudeTextField = new JFormattedTextField(lonDegMinSecFormatter);
        degLongitudeTextField.addFocusListener(focusListener);
        tabTwoPanel.add(degLongitudeTextField);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(I18NHelper.getInstance().getString("dialog.goto.latlon"), tabOnePanel);
        tabbedPane.addTab(I18NHelper.getInstance().getString("dialog.goto.degminsec"), tabTwoPanel);
        JPanel buttonPanel = new JPanel();
        okButton = new JButton(I18NHelper.getInstance().getString("generic.ok"));
        okButton.addActionListener(buttonActionListener);
        buttonPanel.add(okButton);
        cancelButton = new JButton(I18NHelper.getInstance().getString("generic.cancel"));
        cancelButton.addActionListener(buttonActionListener);
        buttonPanel.add(cancelButton);
        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateUI() {
        decLatitudeTextField.setText(new Double(coordinate.getLatitude()).toString());
        decLongitudeTextField.setText(new Double(coordinate.getLongitude()).toString());
        degLatitudeTextField.setText(getFormattedCoordinateText(coordinate.getLatitude(), true));
        degLongitudeTextField.setText(getFormattedCoordinateText(coordinate.getLongitude(), false));
    }

    public void setCoordinate(LatLon coordinate) {
        this.coordinate = coordinate;
        updateUI();
    }

    public LatLon getCoordinate() {
        return coordinate;
    }

    public Status show() {
        dialog.setVisible(true);
        dialog.dispose();
        return selectedValue;
    }

    private void initCloseListener() {
        dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        dialog.getRootPane().getActionMap().put("close", new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
    }

    private String getFormattedCoordinateText(double coordinate, boolean isLatitudeCoordinate) {
        int degree = CoordinateUtilities.getDegreeOfDecimalCoordinate(Math.abs(coordinate));
        int minutes = CoordinateUtilities.getMinutesOfDecimalCoordinate(Math.abs(coordinate));
        double seconds = CoordinateUtilities.getSecondsOfDecimalCoordinate(Math.abs(coordinate));
        int intSeconds = (int) (seconds * 10000.0);
        String direction = "";
        if (isLatitudeCoordinate) {
            direction = CoordinateUtilities.getDirectionOfLatitudeCoordinate(coordinate);
        } else {
            direction = CoordinateUtilities.getDirectionOfLongitudeCoordinate(coordinate);
        }
        return String.format("%02d%02d%06d%s", degree, minutes, intSeconds, direction);
    }

    private InputVerifier decimalCoordinateInputVerifier = new InputVerifier() {

        @Override
        public boolean verify(JComponent input) {
            String text = null;
            if (input instanceof JTextField) {
                text = ((JTextField) input).getText();
            } else {
                ErrorHandler.handleFatalError(new Exception("Error in GotoCoordinateDialog. Given component is not of type JTextField."));
            }
            Double value;
            try {
                value = new Double(text);
            } catch (NumberFormatException nfe) {
                return false;
            }
            if (input == decLatitudeTextField) {
                if ((value < -90.0) || (value > 90.0)) {
                    return false;
                }
            } else if (input == decLongitudeTextField) {
                if ((value < -180.0) || (value > 180.0)) {
                    return false;
                }
            } else {
                ErrorHandler.handleFatalError(new Exception("Error in GotoCoordinateDialog: Unexpected text field."));
            }
            return true;
        }

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            if (!verify(input)) {
                if (input == decLatitudeTextField) {
                    decLatitudeTextField.setText(new Double(coordinate.getLatitude()).toString());
                } else if (input == decLongitudeTextField) {
                    decLongitudeTextField.setText(new Double(coordinate.getLongitude()).toString());
                }
            }
            return true;
        }
    };

    private FocusListener focusListener = new FocusListener() {

        @Override
        public void focusLost(FocusEvent e) {
            Object source = e.getSource();
            double latitude = coordinate.getLatitude();
            double longitude = coordinate.getLongitude();
            if (source == decLatitudeTextField) {
                latitude = new Double(decLatitudeTextField.getText());
            } else if (source == decLongitudeTextField) {
                longitude = new Double(decLongitudeTextField.getText());
            } else if (source == degLatitudeTextField) {
                latitude = CoordinateUtilities.getDecimalCoordinateOfDegMinSec(degLatitudeTextField.getText());
            } else if (source == degLongitudeTextField) {
                longitude = CoordinateUtilities.getDecimalCoordinateOfDegMinSec(degLongitudeTextField.getText());
            } else {
                ErrorHandler.handleFatalError(new Exception("Error in GotoCoordinateDialog: FocusListener applied on an unexpected component."));
            }
            coordinate.setLatitude(latitude);
            coordinate.setLongitude(longitude);
            updateUI();
        }

        @Override
        public void focusGained(FocusEvent e) {
        }
    };

    private ActionListener buttonActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == okButton) {
                selectedValue = Status.OK;
            } else if (source == cancelButton) {
                selectedValue = Status.CANCEL;
            } else {
                ErrorHandler.handleFatalError(new Exception("Error in GotoCoordinateDialog: ActionListener applied on an unexpected component."));
            }
            dialog.setVisible(false);
        }
    };
}
