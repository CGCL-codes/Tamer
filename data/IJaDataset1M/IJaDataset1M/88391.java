package alice.respect.transducer;

import icommand.nxt.SensorPort;
import icommand.nxt.comm.NXTCommand;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import alice.respect.api.IManagementContext;
import alice.respect.api.RespectTC;
import alice.respect.api.TupleCentreId;
import alice.respect.core.RespectTCContainer;
import alice.respect.core.TransducerManager;
import alice.respect.level.Level0;

/**
 * This class create a transducer for each activate external resource and it also create
 * a transducer manaer for each transducer.
 *  
 * @author Duccio
 *
 */
public class TransducerMaker extends JFrame implements ActionListener, WindowListener, ItemListener {

    private static final long serialVersionUID = 1L;

    private boolean level0 = false;

    private boolean activ1 = false;

    private boolean activ2 = false;

    private boolean activ3 = false;

    private boolean activ4 = false;

    private boolean activA = false;

    private boolean activB = false;

    private boolean activC = false;

    private JComboBox cbPort1;

    private JComboBox cbPort2;

    private JComboBox cbPort3;

    private JComboBox cbPort4;

    private JComboBox cbPortA;

    private JComboBox cbPortB;

    private JComboBox cbPortC;

    private JRadioButton rbActivate1;

    private JRadioButton rbDeactivate1;

    private JRadioButton rbActivate2;

    private JRadioButton rbDeactivate2;

    private JRadioButton rbActivate3;

    private JRadioButton rbDeactivate3;

    private JRadioButton rbActivate4;

    private JRadioButton rbDeactivate4;

    private JRadioButton rbActivateA;

    private JRadioButton rbDeactivateA;

    private JRadioButton rbActivateB;

    private JRadioButton rbDeactivateB;

    private JRadioButton rbActivateC;

    private JRadioButton rbDeactivateC;

    private LightTransducer lightTransducer1;

    private LightTransducer lightTransducer2;

    private LightTransducer lightTransducer3;

    private LightTransducer lightTransducer4;

    private UltrasonicTransducer sonarTransducer1;

    private UltrasonicTransducer sonarTransducer2;

    private UltrasonicTransducer sonarTransducer3;

    private UltrasonicTransducer sonarTransducer4;

    private SoundTransducer soundTransducer1;

    private SoundTransducer soundTransducer2;

    private SoundTransducer soundTransducer3;

    private SoundTransducer soundTransducer4;

    private TouchTransducer touchTransducer1;

    private TouchTransducer touchTransducer2;

    private TouchTransducer touchTransducer3;

    private TouchTransducer touchTransducer4;

    private MotorTransducer motorTransducerA;

    private MotorTransducer motorTransducerB;

    private MotorTransducer motorTransducerC;

    private TransducerManager lightTm1;

    private TransducerManager lightTm2;

    private TransducerManager lightTm3;

    private TransducerManager lightTm4;

    private TransducerManager sonarTm1;

    private TransducerManager sonarTm2;

    private TransducerManager sonarTm3;

    private TransducerManager sonarTm4;

    private TransducerManager soundTm1;

    private TransducerManager soundTm2;

    private TransducerManager soundTm3;

    private TransducerManager soundTm4;

    private TransducerManager touchTm1;

    private TransducerManager touchTm2;

    private TransducerManager touchTm3;

    private TransducerManager touchTm4;

    private TransducerManager motorATm;

    private TransducerManager motorBTm;

    private TransducerManager motorCTm;

    private RespectTC tupleCenter1;

    private RespectTC tupleCenter2;

    private RespectTC tupleCenter;

    private RespectTC tupleCenterA;

    private RespectTC tupleCenterB;

    private RespectTC tupleCenterC;

    public TransducerMaker() {
        NXTCommand.open();
        this.setSize(400, 400);
        this.setResizable(false);
        this.setLayout(new GridLayout(2, 1));
        this.addWindowListener(this);
        JPanel jpActuators = new JPanel();
        JPanel jpSensors = new JPanel();
        jpActuators.setName("Actuators");
        jpActuators.setName("Sensors");
        cbPort1 = new JComboBox();
        cbPort2 = new JComboBox();
        cbPort3 = new JComboBox();
        cbPort4 = new JComboBox();
        cbPortA = new JComboBox();
        cbPortB = new JComboBox();
        cbPortC = new JComboBox();
        cbPortA.addItem("right motor");
        cbPortA.addItem("center motor");
        cbPortA.addItem("left motor");
        cbPortB.addItem("right motor");
        cbPortB.addItem("center motor");
        cbPortB.addItem("left motor");
        cbPortC.addItem("right motor");
        cbPortC.addItem("center motor");
        cbPortC.addItem("left motor");
        cbPort1.addItem("Light sensor");
        cbPort1.addItem("Sonar sensor");
        cbPort1.addItem("Sound sensor");
        cbPort1.addItem("Touch sensor");
        cbPort2.addItem("Light sensor");
        cbPort2.addItem("Sonar sensor");
        cbPort2.addItem("Sound sensor");
        cbPort2.addItem("Touch sensor");
        cbPort3.addItem("Light sensor");
        cbPort3.addItem("Sonar sensor");
        cbPort3.addItem("Sound sensor");
        cbPort3.addItem("Touch sensor");
        cbPort4.addItem("Light sensor");
        cbPort4.addItem("Sonar sensor");
        cbPort4.addItem("Sound sensor");
        cbPort4.addItem("Touch sensor");
        cbPort1.addActionListener(this);
        cbPort2.addActionListener(this);
        cbPort3.addActionListener(this);
        cbPort4.addActionListener(this);
        cbPortA.addActionListener(this);
        cbPortB.addActionListener(this);
        cbPortC.addActionListener(this);
        rbActivate1 = new JRadioButton("Activate");
        rbDeactivate1 = new JRadioButton("Deactivate");
        rbActivate2 = new JRadioButton("Activate");
        rbDeactivate2 = new JRadioButton("Deactivate");
        rbActivate3 = new JRadioButton("Activate");
        rbDeactivate3 = new JRadioButton("Deactivate");
        rbActivate4 = new JRadioButton("Activate");
        rbDeactivate4 = new JRadioButton("Deactivate");
        rbActivateA = new JRadioButton("Activate");
        rbDeactivateA = new JRadioButton("Deactivate");
        rbActivateB = new JRadioButton("Activate");
        rbDeactivateB = new JRadioButton("Deactivate");
        rbActivateC = new JRadioButton("Activate");
        rbDeactivateC = new JRadioButton("Deactivate");
        rbActivate1.addActionListener(this);
        rbDeactivate1.addActionListener(this);
        rbActivate2.addActionListener(this);
        rbDeactivate2.addActionListener(this);
        rbActivate3.addActionListener(this);
        rbDeactivate3.addActionListener(this);
        rbActivate4.addActionListener(this);
        rbDeactivate4.addActionListener(this);
        rbActivateA.addActionListener(this);
        rbDeactivateA.addActionListener(this);
        rbActivateB.addActionListener(this);
        rbDeactivateB.addActionListener(this);
        rbActivateC.addActionListener(this);
        rbDeactivateC.addActionListener(this);
        JLabel lblPort1 = new JLabel("PORT 1");
        JLabel lblPort2 = new JLabel("PORT 2");
        JLabel lblPort3 = new JLabel("PORT 3");
        JLabel lblPort4 = new JLabel("PORT 4");
        JLabel lblPortA = new JLabel("PORT A");
        JLabel lblPortB = new JLabel("PORT B");
        JLabel lblPortC = new JLabel("PORT C");
        jpActuators.add(lblPortA);
        jpActuators.add(cbPortA);
        jpActuators.add(rbActivateA);
        jpActuators.add(rbDeactivateA);
        jpActuators.add(lblPortB);
        jpActuators.add(cbPortB);
        jpActuators.add(rbActivateB);
        jpActuators.add(rbDeactivateB);
        jpActuators.add(lblPortC);
        jpActuators.add(cbPortC);
        jpActuators.add(rbActivateC);
        jpActuators.add(rbDeactivateC);
        jpSensors.add(lblPort1);
        jpSensors.add(cbPort1);
        jpSensors.add(rbActivate1);
        jpSensors.add(rbDeactivate1);
        jpSensors.add(lblPort2);
        jpSensors.add(cbPort2);
        jpSensors.add(rbActivate2);
        jpSensors.add(rbDeactivate2);
        jpSensors.add(lblPort3);
        jpSensors.add(cbPort3);
        jpSensors.add(rbActivate3);
        jpSensors.add(rbDeactivate3);
        jpSensors.add(lblPort4);
        jpSensors.add(cbPort4);
        jpSensors.add(rbActivate4);
        jpSensors.add(rbDeactivate4);
        rbDeactivate1.setSelected(true);
        rbDeactivate2.setSelected(true);
        rbDeactivate3.setSelected(true);
        rbDeactivate4.setSelected(true);
        rbDeactivateA.setSelected(true);
        rbDeactivateB.setSelected(true);
        rbDeactivateC.setSelected(true);
        this.add(jpActuators);
        this.add(jpSensors);
        this.setVisible(true);
        try {
            RespectTCContainer.createRespectTC(new TupleCentreId("tc_ma@localhost"), 100);
            IManagementContext tc_ma = RespectTCContainer.getManagementContext(new TupleCentreId("tc_ma@localhost"));
            motorATm = new TransducerManager();
            motorTransducerA = new MotorTransducer(motorATm, new TupleCentreId("tc_ma@localhost"), 0, "R");
            (new Thread(motorTransducerA)).start();
            RespectTCContainer.createRespectTC(new TupleCentreId("tc_mc@localhost"), 100);
            IManagementContext tc_mc = RespectTCContainer.getManagementContext(new TupleCentreId("tc_mc@localhost"));
            motorCTm = new TransducerManager();
            motorTransducerC = new MotorTransducer(motorCTm, new TupleCentreId("tc_ma@localhost"), 2, "L");
            (new Thread(motorTransducerC)).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "Activate") {
            if (e.getSource().equals(rbActivate1)) {
                rbActivate1.setSelected(true);
                rbDeactivate1.setSelected(false);
                switch(cbPort1.getSelectedIndex()) {
                    case 0:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s1@localhost"), 100);
                            IManagementContext tc_s1 = RespectTCContainer.getManagementContext(new TupleCentreId("s1@localhost"));
                            lightTm1 = new TransducerManager();
                            lightTransducer1 = new LightTransducer(lightTm1, SensorPort.S1, tupleCenter1);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        (new Thread(lightTransducer1)).start();
                        activ1 = true;
                        break;
                    case 1:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s1@localhost"), 100);
                            IManagementContext tc_s1 = RespectTCContainer.getManagementContext(new TupleCentreId("s1@localhost"));
                            sonarTm1 = new TransducerManager();
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        (new Thread(sonarTransducer1)).start();
                        activ1 = true;
                        break;
                    case 2:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s1@localhost"), 100);
                            IManagementContext tc_s1 = RespectTCContainer.getManagementContext(new TupleCentreId("s1@localhost"));
                            soundTm1 = new TransducerManager();
                            soundTransducer1 = new SoundTransducer(soundTm1, SensorPort.S1, tupleCenter1);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ1 = true;
                        break;
                    case 3:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s1@localhost"), 100);
                            IManagementContext tc_s1 = RespectTCContainer.getManagementContext(new TupleCentreId("s1@localhost"));
                            touchTm1 = new TransducerManager();
                            touchTransducer1 = new TouchTransducer(touchTm1, SensorPort.S1, tupleCenter1);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ1 = true;
                        break;
                }
            } else if (e.getSource().equals(rbActivate2)) {
                rbActivate2.setSelected(true);
                rbDeactivate2.setSelected(false);
                switch(cbPort2.getSelectedIndex()) {
                    case 0:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s2@localhost"), 100);
                            IManagementContext tc_s2 = RespectTCContainer.getManagementContext(new TupleCentreId("s2@localhost"));
                            lightTm2 = new TransducerManager();
                            lightTransducer2 = new LightTransducer(lightTm2, SensorPort.S2, tupleCenter2);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        (new Thread(lightTransducer2)).start();
                        activ2 = true;
                        break;
                    case 1:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s2@localhost"), 100);
                            IManagementContext tc_s2 = RespectTCContainer.getManagementContext(new TupleCentreId("s2@localhost"));
                            sonarTm2 = new TransducerManager();
                            sonarTransducer2 = new UltrasonicTransducer(sonarTm2, new TupleCentreId("s2@localhost"), SensorPort.S2);
                            (new Thread(sonarTransducer2)).start();
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ2 = true;
                        break;
                    case 2:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s2@localhost"), 100);
                            IManagementContext tc_s2 = RespectTCContainer.getManagementContext(new TupleCentreId("s2@localhost"));
                            soundTm2 = new TransducerManager();
                            soundTransducer2 = new SoundTransducer(soundTm2, SensorPort.S2, tupleCenter2);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ2 = true;
                        break;
                    case 3:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s2@localhost"), 100);
                            IManagementContext tc_s2 = RespectTCContainer.getManagementContext(new TupleCentreId("s2@localhost"));
                            touchTm2 = new TransducerManager();
                            touchTransducer2 = new TouchTransducer(touchTm2, SensorPort.S2, tupleCenter2);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ2 = true;
                        break;
                }
            } else if (e.getSource().equals(rbActivate3)) {
                rbActivate3.setSelected(true);
                rbDeactivate3.setSelected(false);
                switch(cbPort3.getSelectedIndex()) {
                    case 0:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s3@localhost"), 100);
                            IManagementContext tc_s3 = RespectTCContainer.getManagementContext(new TupleCentreId("s3@localhost"));
                            lightTm3 = new TransducerManager();
                            lightTransducer3 = new LightTransducer(lightTm3, SensorPort.S3, tupleCenter);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        (new Thread(lightTransducer3)).start();
                        activ3 = true;
                        break;
                    case 1:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s3@localhost"), 100);
                            IManagementContext tc_s3 = RespectTCContainer.getManagementContext(new TupleCentreId("s3@localhost"));
                            sonarTm3 = new TransducerManager();
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        (new Thread(sonarTransducer3)).start();
                        activ3 = true;
                        break;
                    case 2:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s3@localhost"), 100);
                            IManagementContext tc_s3 = RespectTCContainer.getManagementContext(new TupleCentreId("s3@localhost"));
                            soundTm3 = new TransducerManager();
                            soundTransducer3 = new SoundTransducer(soundTm3, SensorPort.S3, tupleCenter);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ3 = true;
                        break;
                    case 3:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s3@localhost"), 100);
                            IManagementContext tc_s3 = RespectTCContainer.getManagementContext(new TupleCentreId("s3@localhost"));
                            touchTm3 = new TransducerManager();
                            touchTransducer3 = new TouchTransducer(touchTm3, SensorPort.S3, tupleCenter);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ3 = true;
                        break;
                }
            } else if (e.getSource().equals(rbActivate4)) {
                rbActivate4.setSelected(true);
                rbDeactivate4.setSelected(false);
                switch(cbPort4.getSelectedIndex()) {
                    case 0:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s4@localhost"), 100);
                            IManagementContext tc_s4 = RespectTCContainer.getManagementContext(new TupleCentreId("s4@localhost"));
                            lightTm4 = new TransducerManager();
                            lightTransducer4 = new LightTransducer(lightTm4, SensorPort.S4, tupleCenter);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        (new Thread(lightTransducer4)).start();
                        activ4 = true;
                        break;
                    case 1:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s4@localhost"), 100);
                            IManagementContext tc_s4 = RespectTCContainer.getManagementContext(new TupleCentreId("s4@localhost"));
                            sonarTm4 = new TransducerManager();
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        (new Thread(sonarTransducer4)).start();
                        activ4 = true;
                        break;
                    case 2:
                        try {
                            RespectTCContainer.createRespectTC(new TupleCentreId("s4@localhost"), 100);
                            IManagementContext tc_s4 = RespectTCContainer.getManagementContext(new TupleCentreId("s4@localhost"));
                            soundTm4 = new TransducerManager();
                            soundTransducer4 = new SoundTransducer(soundTm4, SensorPort.S4, tupleCenter);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ4 = true;
                        break;
                    case 3:
                        try {
                            tupleCenter = new RespectTC(new TupleCentreId("s4@localhost"), 10);
                            RespectTCContainer.createRespectTC(new TupleCentreId("s4@localhost"), 100);
                            IManagementContext tc_s4 = RespectTCContainer.getManagementContext(new TupleCentreId("s4@localhost"));
                            touchTm4 = new TransducerManager();
                            touchTransducer4 = new TouchTransducer(touchTm4, SensorPort.S4, tupleCenter);
                            if (!level0) {
                                new Level0();
                                level0 = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        activ4 = true;
                        break;
                }
            }
        }
        if (e.getActionCommand() == "Deactivate") {
            if (e.getSource().equals(rbDeactivate1)) {
                rbActivate1.setSelected(false);
                rbDeactivate1.setSelected(true);
                if (activ1) {
                    switch(cbPort1.getSelectedIndex()) {
                        case 0:
                            if (lightTm1.removeResource(lightTransducer1)) {
                                activ1 = false;
                            } else {
                                System.out.println("Impossibile disabilitare il transducer");
                            }
                            break;
                        case 1:
                            sonarTm1.removeResource(sonarTransducer1);
                            activ1 = false;
                            break;
                        case 2:
                            soundTm1.removeResource(soundTransducer1);
                            activ1 = false;
                            break;
                        case 3:
                            touchTm1.removeResource(touchTransducer1);
                            activ1 = false;
                            break;
                    }
                }
            } else if (e.getSource().equals(rbDeactivate2)) {
                rbActivate2.setSelected(false);
                rbDeactivate2.setSelected(true);
                if (activ2) {
                    switch(cbPort2.getSelectedIndex()) {
                        case 0:
                            lightTm2.removeResource(lightTransducer2);
                            activ2 = false;
                            break;
                        case 1:
                            if (sonarTm2.removeResource(sonarTransducer2)) {
                                activ2 = false;
                            } else {
                                System.out.println("Impossibile disabilitare il transducer");
                            }
                            break;
                        case 2:
                            soundTm2.removeResource(soundTransducer2);
                            activ2 = false;
                            break;
                        case 3:
                            touchTm2.removeResource(touchTransducer2);
                            activ2 = false;
                            break;
                    }
                }
            } else if (e.getSource().equals(rbDeactivate3)) {
                rbActivate3.setSelected(false);
                rbDeactivate3.setSelected(true);
                if (activ3) {
                    switch(cbPort1.getSelectedIndex()) {
                        case 0:
                            lightTm3.removeResource(lightTransducer3);
                            activ3 = false;
                            break;
                        case 1:
                            sonarTm3.removeResource(sonarTransducer3);
                            activ3 = false;
                            break;
                        case 2:
                            soundTm3.removeResource(soundTransducer3);
                            activ3 = false;
                            break;
                        case 3:
                            touchTm3.removeResource(touchTransducer3);
                            activ3 = false;
                            break;
                    }
                }
            } else if (e.getSource().equals(rbDeactivate4)) {
                rbActivate4.setSelected(false);
                rbDeactivate4.setSelected(true);
                if (activ4) {
                    switch(cbPort1.getSelectedIndex()) {
                        case 0:
                            lightTm4.removeResource(lightTransducer4);
                            activ4 = false;
                            break;
                        case 1:
                            sonarTm4.removeResource(sonarTransducer4);
                            activ4 = false;
                            break;
                        case 2:
                            soundTm4.removeResource(soundTransducer4);
                            activ4 = false;
                            break;
                        case 3:
                            touchTm3.removeResource(touchTransducer4);
                            activ4 = false;
                            break;
                    }
                }
            }
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(EXIT_ON_CLOSE);
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void itemStateChanged(ItemEvent e) {
    }

    public static void main(String[] args) throws Exception {
        new TransducerMaker();
    }
}
