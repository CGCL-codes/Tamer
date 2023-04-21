package br.unb.gui.editPanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import br.unb.entities.vehicles.VehicleFactory;
import br.unb.entities.vehicles.reactionModels.gippsBased.GippsBasedReactionModel;
import br.unb.entities.vehicles.reactionModels.hutsimBased.HutsimBasedReactionModel;
import br.unb.entities.vehiclesGenerator.GeneralVehiclesParameters;
import br.unb.entities.vehiclesGenerator.VehicleTypeGeneralParameters;
import br.unb.main.ModelController;

/**
 * Window to change the general vehicles parameters
 * 
 * @author Marcelo Vale Asari
 */
public class GeneralVehiclesParametersEditPanel extends JFrame implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8991953821343140408L;

    private GeneralVehiclesParameters param;

    private VehicleTypeGeneralParameters typeParam;

    private JPanel contentPanel;

    private JLabel lbModel;

    private JLabel lbAccelAve;

    private JLabel lbAccelMax;

    private JLabel lbVehicleType;

    private JLabel lbDecelAve;

    private JLabel lbLength;

    private JLabel lbMaxSightDist;

    private JLabel lbReactionTime;

    private JLabel lbReactionTimeDev;

    private JLabel lbMinGap;

    private JComboBox cbModel;

    private JComboBox cbVehicleType;

    private JFormattedTextField tfAccelAve;

    private JFormattedTextField tfAccelMax;

    private JFormattedTextField tfDecelAve;

    private JFormattedTextField tfLength;

    private JFormattedTextField tfMaxSightDist;

    private JFormattedTextField tfReactionTime;

    private JFormattedTextField tfReactionTimeDev;

    private JFormattedTextField tfMinGap;

    private JButton btClose;

    private Locale locale;

    private ResourceBundle bundle;

    public GeneralVehiclesParametersEditPanel(GeneralVehiclesParameters param) {
        this.param = param;
        locale = new Locale("pt", "BR");
        bundle = ResourceBundle.getBundle("gui/generalVehiclesParamEditPanelBundle", locale);
        contentPanel = new JPanel();
        contentPanel.setLayout(new MigLayout());
        typeParam = param.getParam(VehicleFactory.carKey);
        addFields();
        btClose = new JButton(bundle.getString("btClose"));
        btClose.addActionListener(this);
        contentPanel.add(btClose, "span 2, align right");
        this.add(contentPanel);
        this.setTitle(bundle.getString("title"));
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void addFields() {
        lbModel = new JLabel(bundle.getString("lbModel"));
        lbModel.setToolTipText(bundle.getString("lbModelTip"));
        String[] modelTypes = ModelController.getModelTypes(locale);
        cbModel = new JComboBox(modelTypes);
        lbModel.setLabelFor(cbModel);
        if (param.reactionModel instanceof HutsimBasedReactionModel) {
            cbModel.setSelectedIndex(1);
        } else {
            cbModel.setSelectedIndex(0);
        }
        cbModel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ModelController.getTypeKey((String) cbModel.getSelectedItem(), locale).equals(ModelController.hutsimBasedModelKey)) {
                }
            }
        });
        contentPanel.add(lbModel);
        contentPanel.add(cbModel, "wrap");
        lbMinGap = new JLabel(bundle.getString("lbMinGap"));
        tfMinGap = new JFormattedTextField(DecimalFormat.getInstance(locale));
        addField(lbMinGap, bundle.getString("lbMinGapTip"), tfMinGap);
        tfMinGap.setValue(((GippsBasedReactionModel) param.reactionModel).getMinGap());
        tfMinGap.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                ((GippsBasedReactionModel) param.reactionModel).setMinGap(((Number) (tfMinGap.getValue())).doubleValue());
            }
        });
        tfMinGap.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfMinGap.transferFocus();
                }
            }
        });
        contentPanel.add(new JSeparator(JSeparator.HORIZONTAL), "span 2, grow x, wrap");
        lbVehicleType = new JLabel(bundle.getString("lbVehicleType"));
        lbVehicleType.setToolTipText(bundle.getString("lbVehicleTypeTip"));
        String[] types = VehicleFactory.getTypes(locale, true);
        cbVehicleType = new JComboBox(types);
        cbVehicleType.setEditable(false);
        lbVehicleType.setLabelFor(cbVehicleType);
        cbVehicleType.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                typeParam = param.getParam(VehicleFactory.getTypeKey((String) cbVehicleType.getSelectedItem(), locale));
                if (typeParam == null) {
                    typeParam = new VehicleTypeGeneralParameters();
                    param.setTypeParameters(VehicleFactory.getTypeKey((String) cbVehicleType.getSelectedItem(), locale), typeParam);
                }
                updateFields();
            }
        });
        contentPanel.add(lbVehicleType);
        contentPanel.add(cbVehicleType, "wrap");
        lbAccelAve = new JLabel(bundle.getString("lbAccelAve"));
        tfAccelAve = new JFormattedTextField(DecimalFormat.getInstance(locale));
        addField(lbAccelAve, bundle.getString("lbAccelAveTip"), tfAccelAve);
        tfAccelAve.setValue(typeParam.averageAcceleration);
        tfAccelAve.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                typeParam.averageAcceleration = ((Number) (tfAccelAve.getValue())).doubleValue();
            }
        });
        tfAccelAve.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfAccelAve.transferFocus();
                }
            }
        });
        lbAccelMax = new JLabel(bundle.getString("lbAccelMax"));
        tfAccelMax = new JFormattedTextField(DecimalFormat.getInstance(locale));
        addField(lbAccelMax, bundle.getString("lbAccelMaxTip"), tfAccelMax);
        tfAccelMax.setValue(typeParam.maxAcceleration);
        tfAccelMax.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                typeParam.maxAcceleration = ((Number) (tfAccelMax.getValue())).doubleValue();
            }
        });
        tfAccelMax.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfAccelMax.transferFocus();
                }
            }
        });
        lbDecelAve = new JLabel(bundle.getString("lbDecelAve"));
        tfDecelAve = new JFormattedTextField(DecimalFormat.getInstance(locale));
        addField(lbDecelAve, bundle.getString("lbDecelAveTip"), tfDecelAve);
        tfDecelAve.setValue(typeParam.averageDeceleration);
        tfDecelAve.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                typeParam.averageDeceleration = ((Number) (tfDecelAve.getValue())).doubleValue();
            }
        });
        tfDecelAve.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfDecelAve.transferFocus();
                }
            }
        });
        lbLength = new JLabel(bundle.getString("lbLength"));
        tfLength = new JFormattedTextField(DecimalFormat.getInstance(locale));
        addField(lbLength, bundle.getString("lbLengthTip"), tfLength);
        tfLength.setValue(typeParam.length);
        tfLength.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                typeParam.length = ((Number) (tfLength.getValue())).doubleValue();
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
        lbMaxSightDist = new JLabel(bundle.getString("lbMaxSightDist"));
        tfMaxSightDist = new JFormattedTextField(DecimalFormat.getInstance(locale));
        addField(lbMaxSightDist, bundle.getString("lbMaxSightDistTip"), tfMaxSightDist);
        tfMaxSightDist.setValue(typeParam.maxSight);
        tfMaxSightDist.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                typeParam.maxSight = ((Number) (tfMaxSightDist.getValue())).doubleValue();
            }
        });
        tfMaxSightDist.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfMaxSightDist.transferFocus();
                }
            }
        });
        lbReactionTime = new JLabel(bundle.getString("lbReactionTime"));
        tfReactionTime = new JFormattedTextField(DecimalFormat.getInstance(locale));
        addField(lbReactionTime, bundle.getString("lbReactionTimeTip"), tfReactionTime);
        tfReactionTime.setValue(typeParam.reactionTime);
        tfReactionTime.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                typeParam.reactionTime = ((Number) (tfReactionTime.getValue())).doubleValue();
            }
        });
        tfReactionTime.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfReactionTime.transferFocus();
                }
            }
        });
        lbReactionTimeDev = new JLabel(bundle.getString("lbReactionTimeDev"));
        tfReactionTimeDev = new JFormattedTextField(DecimalFormat.getInstance(locale));
        addField(lbReactionTimeDev, bundle.getString("lbReactionTimeDevTip"), tfReactionTimeDev);
        tfReactionTimeDev.setValue(typeParam.reactionTimeDeviation);
        tfReactionTimeDev.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                typeParam.reactionTimeDeviation = ((Number) (tfReactionTimeDev.getValue())).doubleValue();
            }
        });
        tfReactionTimeDev.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    tfReactionTimeDev.transferFocus();
                }
            }
        });
    }

    private void addField(JLabel label, String toolTip, JTextField textField) {
        label.setToolTipText(toolTip);
        label.setLabelFor(textField);
        contentPanel.add(label);
        contentPanel.add(textField, "wrap, w 100");
    }

    /**
	 * Update the fields when the user selects other type of vehicle
	 */
    private void updateFields() {
        tfAccelAve.setValue(typeParam.averageAcceleration);
        tfAccelMax.setValue(typeParam.maxAcceleration);
        tfDecelAve.setValue(typeParam.averageDeceleration);
        tfLength.setValue(typeParam.length);
        tfMaxSightDist.setValue(typeParam.maxSight);
        tfReactionTime.setValue(typeParam.reactionTime);
        tfReactionTimeDev.setValue(typeParam.reactionTimeDeviation);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btClose)) {
            this.dispose();
        }
    }
}
