package obliquelaunch;

import obliquelaunch.*;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JOptionPane;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Color;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

/**
 *
 * @author  Marcello Junior, Augusto Cesar, Julian Herrera
 * @version 1.0
 */
public class JMain extends JFrame implements ActionListener {

    private JPanel basePanel, topPanel, bottomPanel, animationPanel, informationPanel, speedPanel, anglePanel, gravityPanel, reachPanel, mHeightPanel, spaceBoxPanel, situationPanel, confResPanel, configPanel, resultPanel;

    private JButton moreLaunches;

    private JTextField speedField, gravityField, reachField, mHeightField;

    private JSliderText angleSlider;

    private JTextArea resultArea, situationDescriptionArea;

    private JScrollPane situationDescriptionScroll, resultScroll;

    private JComboBox spaceComboBox, situationsComboBox;

    private Container container;

    private JFrame mainFrame;

    private Environment world;

    private Thrower cannon;

    private Projectile ball;

    private MathGraph graphPanel;

    private int situation = 0;

    private boolean errorOccorred = false;

    /** Initialize all Jobjects in JMain. */
    public void initComponets() {
        graphPanel = new MathGraph();
        graphPanel.setToolTipText("Gr�fico do lan�amento obl�quo.");
        animationPanel = new JPanel();
        topPanel = new JPanel();
        speedField = new JTextField(5);
        speedField.setToolTipText("Velocidade do lan�amento medida em m/s.");
        speedPanel = new JPanel();
        angleSlider = new JSliderText("45", 1, 89, 45);
        angleSlider.setToolTipText("Ajuste a medida do �ngulo (1� - 89�).");
        anglePanel = new JPanel();
        gravityField = new JTextField(5);
        gravityField.setToolTipText("Gravidade do espa�o de lan�amento medido em m/s^2.");
        gravityPanel = new JPanel();
        String[] spaceOption = { " Terra", " Lua", " Sol" };
        spaceComboBox = new JComboBox(spaceOption);
        reachField = new JTextField(5);
        reachField.setToolTipText("Valor do Alcance");
        reachField.disable();
        reachPanel = new JPanel();
        mHeightField = new JTextField(5);
        mHeightField.setToolTipText("Valor da Altura M�xima");
        mHeightField.disable();
        mHeightPanel = new JPanel();
        spaceBoxPanel = new JPanel();
        informationPanel = new JPanel();
        situationPanel = new JPanel();
        String[] situationOptions = { " �ngulo e Velocidade Inicial", " Velocidade Inicial e Alcance", " Angulo e Alcance", " Alcance e Altura M�xima" };
        situationsComboBox = new JComboBox(situationOptions);
        situationDescriptionArea = new JTextArea(3, 30);
        situationDescriptionScroll = new JScrollPane(situationDescriptionArea);
        confResPanel = new JPanel();
        resultArea = new JTextArea(5, 45);
        resultArea.setToolTipText("Resultados do gr�fico do lan�amento");
        resultScroll = new JScrollPane(resultArea);
        resultPanel = new JPanel();
        bottomPanel = new JPanel();
        basePanel = new JPanel();
        mainFrame = new JFrame("FIS308 - Lan�amento Obl�quo - Augusto Cesar : Julian Herrera : Marcello Junior - UFAL - Julho de 2002");
        mainFrame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /** Creates new form JMain. */
    public JMain() {
        this.initComponets();
        this.initializeObliqueLaunch();
        animationPanel.setBorder(BorderFactory.createTitledBorder("Gr�fico do Lan�amento"));
        animationPanel.add(this.graphPanel, BorderLayout.CENTER);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(animationPanel, BorderLayout.CENTER);
        speedField.addActionListener(this);
        speedPanel.setBorder(BorderFactory.createTitledBorder("Velocidade Inicial - m/s"));
        speedPanel.add(speedField);
        angleSlider.text.addActionListener(this);
        anglePanel.setBorder(BorderFactory.createTitledBorder("�ngulo - �"));
        anglePanel.add(angleSlider);
        gravityField.addActionListener(this);
        gravityPanel.setBorder(BorderFactory.createTitledBorder("Gravidade - m/s^2"));
        gravityPanel.add(gravityField);
        spaceComboBox.addActionListener(this);
        spaceComboBox.setSelectedIndex(0);
        spaceBoxPanel.setBorder(BorderFactory.createTitledBorder("Acelera��o Gravitacional"));
        spaceBoxPanel.add(spaceComboBox);
        reachField.addActionListener(this);
        reachPanel.setBorder(BorderFactory.createTitledBorder("Alcance - m"));
        reachPanel.add(reachField);
        mHeightField.addActionListener(this);
        mHeightPanel.setBorder(BorderFactory.createTitledBorder("Altura M�xima - m"));
        mHeightPanel.add(mHeightField);
        informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.X_AXIS));
        informationPanel.setBorder(BorderFactory.createTitledBorder("Informa��es de Entrada"));
        informationPanel.add(speedPanel);
        informationPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        informationPanel.add(anglePanel);
        informationPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        informationPanel.add(gravityPanel);
        informationPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        informationPanel.add(spaceBoxPanel);
        informationPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        informationPanel.add(reachPanel);
        informationPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        informationPanel.add(mHeightPanel);
        resultArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        situationDescriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        situationDescriptionArea.setLineWrap(true);
        situationDescriptionArea.setWrapStyleWord(true);
        situationDescriptionArea.setEditable(false);
        situationPanel.setBorder(BorderFactory.createTitledBorder("Situa��es Poss�veis"));
        situationPanel.setLayout(new BoxLayout(situationPanel, BoxLayout.Y_AXIS));
        situationsComboBox.addActionListener(this);
        situationsComboBox.setSelectedIndex(0);
        situationPanel.add(situationsComboBox);
        situationPanel.add(situationDescriptionScroll);
        resultPanel.setBorder(BorderFactory.createTitledBorder("Resultados"));
        resultPanel.add(resultScroll);
        confResPanel.setLayout(new BoxLayout(confResPanel, BoxLayout.X_AXIS));
        confResPanel.add(situationPanel);
        confResPanel.add(resultPanel);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(informationPanel);
        bottomPanel.add(confResPanel);
        basePanel.setLayout(new BorderLayout());
        basePanel.setBorder(BorderFactory.createEtchedBorder());
        basePanel.add(topPanel, BorderLayout.CENTER);
        basePanel.add(bottomPanel, BorderLayout.SOUTH);
        container = mainFrame.getContentPane();
        container.add(basePanel, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.show();
    }

    /** 1 situation -  Sets the oblique launch values into the thrower and environment object. */
    private void setObliqueLaunchValues(double initialSpeed, int angle, double gravity) {
        this.world.setGravity(gravity);
        this.cannon.setGravity(this.world.getGravity(), this.ball);
        this.cannon.setAngle(angle, this.ball);
        this.cannon.setInitialSpeed(initialSpeed, this.ball);
    }

    /** 2 situation - Sets the oblique launch values into the thrower and environment object. */
    private void setObliqueLaunchValues(double initialSpeed, double gravity, double reach) {
        this.world.setGravity(gravity);
        this.cannon.setGravity(this.world.getGravity(), this.ball);
        this.cannon.setInitialSpeed(initialSpeed, this.ball);
        this.world.setReach(reach);
    }

    /** Sets the initial values of the current environment. */
    public void setInitialValues() {
        double gravity = this.world.gravity;
        double initialSpeed = 65;
        int angle = 75;
        speedField.setText(String.valueOf(initialSpeed));
        this.angleSlider.text.setText(String.valueOf(angle));
        gravityField.setText(String.valueOf(gravity));
    }

    /** Initialize the Oblique Launch Objects and theirs values. */
    public void initializeObliqueLaunch() {
        world = new Environment();
        cannon = new Thrower();
        ball = new Projectile();
        this.setInitialValues();
    }

    /** Sets the values of the launch. */
    private void setResultText() {
        double gravity = 0, initialSpeed = 0, maxHeight = 0, maxHeightTime = 0, reach = 0;
        int angle = 0;
        switch(this.situation) {
            case 0:
                {
                    if (speedField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor da velocidade inicial deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else if (angleSlider.text.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor do �ngulo deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else if (gravityField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor da gravidade deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        gravity = Double.parseDouble(gravityField.getText());
                        initialSpeed = Double.parseDouble(speedField.getText());
                        angle = Integer.parseInt(angleSlider.text.getText());
                        this.setObliqueLaunchValues(initialSpeed, angle, gravity);
                        maxHeight = this.world.getMaxHeight(this.cannon, this.ball);
                        maxHeightTime = this.world.getMaxHeightTime(this.ball);
                        reach = this.world.getReach(this.cannon, this.ball);
                    }
                }
                break;
            case 1:
                {
                    if (speedField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor da velocidade inicial deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else if (gravityField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor da gravidade deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else if (reachField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor do alcance deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        gravity = Double.parseDouble(gravityField.getText());
                        initialSpeed = Double.parseDouble(speedField.getText());
                        reach = Double.parseDouble(reachField.getText());
                        this.setObliqueLaunchValues(initialSpeed, gravity, reach);
                        angle = this.ball.getAngle(reach);
                        this.angleSlider.text.setText(String.valueOf(angle));
                        maxHeight = this.world.getMaxHeight(this.ball);
                        maxHeightTime = this.world.getMaxHeightTime(this.ball);
                    }
                }
                break;
            case 2:
                {
                    if (angleSlider.text.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor do �ngulo deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else if (gravityField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor da gravidade deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else if (reachField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor do alcance deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        gravity = Double.parseDouble(gravityField.getText());
                        reach = Double.parseDouble(reachField.getText());
                        angle = Integer.parseInt(angleSlider.text.getText());
                        initialSpeed = this.ball.getInitialSpeed(angle, reach, gravity);
                        this.setObliqueLaunchValues(initialSpeed, angle, gravity);
                        maxHeight = this.world.getMaxHeight(this.cannon, this.ball);
                        maxHeightTime = this.world.getMaxHeightTime(this.ball);
                    }
                }
                break;
            case 3:
                {
                    if (gravityField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor da gravidade deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else if (reachField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor do alcance deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else if (mHeightField.getText().equals("")) {
                        this.errorOccorred = true;
                        JOptionPane.showMessageDialog(this, "O valor da altura m�xima deve ser informado!", "Valor de entrada faltando!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        gravity = Double.parseDouble(gravityField.getText());
                        maxHeight = Double.parseDouble(mHeightField.getText());
                        reach = Double.parseDouble(reachField.getText());
                        angle = this.ball.getAngle(reach, maxHeight);
                        initialSpeed = this.ball.getInitialSpeed(angle, reach, gravity);
                        this.setObliqueLaunchValues(initialSpeed, angle, gravity);
                        maxHeightTime = this.world.getMaxHeightTime(this.ball);
                    }
                }
                break;
        }
        if (!this.errorOccorred) {
            this.completeSimulation(gravity, initialSpeed, angle, maxHeight, maxHeightTime, reach);
        } else {
            this.errorOccorred = false;
        }
    }

    /**
   * Method completeSimulation. Completes the drawing of the thrower of the particle
   * and puts the final result in the result TextArea.
   * @param    gravity             a  double
   * @param    initialSpeed        a  double
   * @param    angle               an int
   * @param    maxHeight           a  double
   * @param    maxHeightTime       a  double
   * @param    reach               a  double
   *
   */
    private void completeSimulation(double gravity, double initialSpeed, int angle, double maxHeight, double maxHeightTime, double reach) {
        this.resultArea.setText("");
        if (angle == 0) {
            JOptionPane.showMessageDialog(this, "Os par�metros de velocidade inicial e alcance fornecidos s�o incompat�veis.", "Erro de par�metros", JOptionPane.ERROR_MESSAGE);
        } else {
            this.resultArea.append("Dadas as informa��es iniciais para esta situa��o, o lan�amento " + "do proj�til alcan�ou uma altura m�xima de " + String.valueOf((float) maxHeight) + " metros em " + String.valueOf((float) maxHeightTime) + " segundos, alcan�ando " + String.valueOf((float) reach) + " metros em " + 2 * maxHeightTime + " segundos.");
        }
        this.reachField.setText(String.valueOf((double) reach));
        this.mHeightField.setText(String.valueOf((double) maxHeight));
        this.gravityField.setText(String.valueOf((double) gravity));
        this.speedField.setText(String.valueOf((double) initialSpeed));
        this.angleSlider.text.setText(String.valueOf((int) angle));
        Point point = new Point();
        this.graphPanel.reach = (int) reach;
        this.graphPanel.maxHeight = (int) maxHeight;
        this.graphPanel.points.clear();
        if (angle != 90) {
            int lastX = 0;
            for (int x = 0; x <= reach; x++) {
                point = this.ball.getParablePoint(x);
                this.graphPanel.points.add(point);
                lastX = (int) point.getX();
            }
            this.graphPanel.points.add(new Point(lastX, 0));
        } else {
            for (int y = 0; y <= 150; y++) {
                point.setLocation(0, y);
                this.graphPanel.points.add(point);
            }
        }
        if (angle != 0) this.graphPanel.startAnimation();
    }

    public void setEnvironmentValuesFromSpaceComboBox(JComboBox spaceComboBox) {
        int environmentChosen = this.spaceComboBox.getSelectedIndex();
        String value = "";
        switch(environmentChosen) {
            case 0:
                value = "9.81";
                break;
            case 1:
                value = "1.67";
                break;
            case 2:
                value = "274";
                break;
        }
        this.gravityField.setText(value);
    }

    private void enableAllFields() {
        this.enableObject(this.speedField);
        this.enableObject(this.angleSlider.text);
        this.angleSlider.slider.enable();
        this.enableObject(this.gravityField);
        this.enableObject(this.reachField);
        this.enableObject(this.mHeightField);
    }

    private void enableObject(JTextField textField) {
        textField.enable();
        textField.setBackground(Color.WHITE);
    }

    private void disableObject(JTextField textField) {
        textField.disable();
        textField.setBackground(Color.DARK_GRAY);
    }

    public void setSituationValuesFromSpaceComboBox(JComboBox situationDescComboBox) {
        int environmentChosen = this.situationsComboBox.getSelectedIndex();
        String value = "";
        switch(environmentChosen) {
            case 0:
                {
                    value = "Os valores de entrada s�o Velocidade Inicial, �ngulo e Gravidade, para ser" + " encontrado o Alcance e Altura M�xima, al�m do tempo para ambos.";
                    this.enableAllFields();
                    this.disableObject(this.reachField);
                    this.disableObject(this.mHeightField);
                    this.situation = 0;
                }
                break;
            case 1:
                {
                    value = "Os valores de entrada s�o Velocidade Inicial, Gravidade e Alcance, para ser" + " encontrado o Angulo e Altura M�xima, al�m do tempo para ambos.";
                    this.enableAllFields();
                    this.disableObject(this.angleSlider.text);
                    this.angleSlider.slider.disable();
                    this.disableObject(this.mHeightField);
                    this.situation = 1;
                }
                break;
            case 2:
                {
                    value = "Os valores de entrada s�o Gravidade, �ngulo e Alcance, para ser" + " encontrado a Velocidade Inicial e Altura M�xima al�m do tempo.";
                    this.enableAllFields();
                    this.disableObject(this.speedField);
                    this.disableObject(this.mHeightField);
                    this.situation = 2;
                }
                break;
            case 3:
                {
                    value = "Os valores de entrada s�o Gravidade, Alcance e Altura M�xima, para ser" + " encontrado o �ngulo e Velocidade Inicial, al�m do tempo.";
                    this.enableAllFields();
                    this.disableObject(this.speedField);
                    this.disableObject(this.angleSlider.text);
                    this.angleSlider.slider.disable();
                    this.situation = 3;
                }
                break;
        }
        this.situationDescriptionArea.setText("");
        this.situationDescriptionArea.append(value);
    }

    public void actionPerformed(ActionEvent action) {
        Object source = action.getSource();
        if (source == this.speedField || source == this.angleSlider.text || source == gravityField || source == reachField || source == mHeightField) {
            this.setResultText();
        } else if (source == this.spaceComboBox) {
            JComboBox spaceComboAction = (JComboBox) action.getSource();
            this.setEnvironmentValuesFromSpaceComboBox(spaceComboAction);
        } else if (source == this.situationsComboBox) {
            JComboBox situationDescComboBox = (JComboBox) action.getSource();
            this.setSituationValuesFromSpaceComboBox(situationDescComboBox);
        }
    }

    public static void main(String[] args) {
        System.out.println("Universidade Federal de Alagoas");
        System.out.println("CCEN - FIS308");
        System.out.println("Lan�amento Obl�quo - Julho de 2002");
        System.out.println("* Augusto C�sar Melo de Oliveira - 1999G55D007V-1");
        System.out.println("* Julian Diego Herrera Braga - 1999G55D029V-5");
        System.out.println("* Marcello Alves de Sales Junior - 1998G55D001T-9");
        System.out.println("");
        JMain world = new JMain();
        world.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
