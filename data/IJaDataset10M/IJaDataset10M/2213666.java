package br.unb.gui.editPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import br.unb.entities.RoadSegment;
import br.unb.main.ModelController;
import br.unb.main.ModelController.EntityTypes;

public class RoadSegmentEditPanel extends JFrame implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2300191682160047666L;

    private RoadSegment roadSegment;

    private ResourceBundle bundle;

    private Locale locale;

    private JPanel contentPanel;

    private JButton btClose;

    private JLabel lbId;

    private JLabel lbLength;

    private JLabel lbWidth;

    private JLabel lbSpeedLimit;

    private JFormattedTextField tfId;

    private JFormattedTextField tfLength;

    private JFormattedTextField tfWidth;

    private JFormattedTextField tfSpeedLimit;

    private ModelController modelController;

    private JCheckBox ckIsATurnBeginning;

    public RoadSegmentEditPanel(RoadSegment roadSegment, ModelController modelController) {
        this.modelController = modelController;
        this.roadSegment = roadSegment;
        locale = new Locale("pt", "BR");
        bundle = ResourceBundle.getBundle("gui/roadSegmentEditPanelBundle", locale);
        contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout());
        addFields();
        btClose = new JButton(bundle.getString("btClose"));
        btClose.addActionListener(this);
        contentPanel.add(btClose, "span 2, align right, wrap");
        this.add(contentPanel);
        this.setTitle(bundle.getString("title"));
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void addFields() {
        lbId = new JLabel(bundle.getString("lbId"));
        tfId = new JFormattedTextField(NumberFormat.getIntegerInstance(locale));
        tfId.setValue(roadSegment.getId());
        addField(lbId, bundle.getString("lbIdTip"), tfId);
        tfId.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Integer id = ((Number) tfId.getValue()).intValue();
                if (!modelController.IdExists(id, EntityTypes.ROADSEGMENT)) {
                    roadSegment.setId(id);
                    modelController.addId(id, EntityTypes.ROADSEGMENT);
                } else {
                    tfId.setValue(roadSegment.getId());
                }
            }
        });
        tfId.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfId.transferFocus();
                }
            }
        });
        lbLength = new JLabel(bundle.getString("lbLength"));
        tfLength = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfLength.setValue(roadSegment.getLength());
        addField(lbLength, bundle.getString("lbLengthTip"), tfLength);
        tfLength.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                try {
                    tfLength.commitEdit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                roadSegment.setLength(((Number) tfLength.getValue()).doubleValue());
            }
        });
        tfLength.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfLength.transferFocus();
                }
            }
        });
        lbWidth = new JLabel(bundle.getString("lbWidth"));
        tfWidth = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfWidth.setValue(roadSegment.getWidth());
        addField(lbWidth, bundle.getString("lbWidthTip"), tfWidth);
        tfWidth.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                roadSegment.setWidth(((Number) tfWidth.getValue()).doubleValue());
            }
        });
        tfWidth.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfWidth.transferFocus();
                }
            }
        });
        lbSpeedLimit = new JLabel(bundle.getString("lbSpeedLimit"));
        tfSpeedLimit = new JFormattedTextField(DecimalFormat.getInstance(locale));
        tfSpeedLimit.setValue(roadSegment.getSpeedLimit() * 3.6);
        addField(lbSpeedLimit, bundle.getString("lbSpeedLimitTip"), tfSpeedLimit);
        tfSpeedLimit.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                roadSegment.setSpeedLimit(((Number) tfSpeedLimit.getValue()).doubleValue() / 3.6);
            }
        });
        tfSpeedLimit.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfSpeedLimit.transferFocus();
                }
            }
        });
        ckIsATurnBeginning = new JCheckBox(bundle.getString("lbTurnBeginning"));
        ckIsATurnBeginning.setToolTipText(bundle.getString("lbTurnBeginningTip"));
        ckIsATurnBeginning.setSelected(roadSegment.isATurn());
        ckIsATurnBeginning.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                roadSegment.setIsATurn(ckIsATurnBeginning.isSelected());
            }
        });
        contentPanel.add(ckIsATurnBeginning, "wrap");
    }

    private void addField(JLabel label, String toolTip, JFormattedTextField textField) {
        label.setToolTipText(toolTip);
        label.setLabelFor(textField);
        contentPanel.add(label);
        contentPanel.add(textField, "wrap, w 100");
    }

    public void setResourceBundle(String path, Locale locale) {
        bundle = ResourceBundle.getBundle(path, locale);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btClose)) {
            this.dispose();
        }
    }

    public void notifyChanges() {
        tfId.setValue(roadSegment.getId());
        tfLength.setText(DecimalFormat.getInstance(locale).format(roadSegment.getLength()));
        tfWidth.setValue(roadSegment.getWidth());
        tfSpeedLimit.setValue(roadSegment.getSpeedLimit() * 3.6);
    }
}
