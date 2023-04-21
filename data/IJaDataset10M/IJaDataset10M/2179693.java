package View;

import Model.Enums;
import Presenter.MainWindowPresenter;
import java.awt.Color;
import java.awt.Cursor;
import java.util.Locale;
import javax.swing.JFrame;

/**
 *
 */
public class MainWindowView extends JFrame {

    private MainWindowPresenter Presenter = null;

    /** Creates new form MainWindow */
    public MainWindowView() {
        initComponents();
        InitGUI();
        Presenter = new MainWindowPresenter(this);
    }

    private void InitGUI() {
        this.setTitle("Greenhouse Gases Transmittance Simulation");
        String msg;
        msg = String.format(Locale.ENGLISH, "%s%n", "Simulation results: ...");
        this.jTextArea1.setText(msg);
    }

    public String CarbonDioxideLevel;

    public String MethaneLevel;

    public String NitrousOxideLevel;

    public String WaterVaporLevel;

    public String MinimalTemperature;

    public String MaximalTemperature;

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panel_concentration = new javax.swing.JPanel();
        txt_carbon = new javax.swing.JTextField();
        txt_methane = new javax.swing.JTextField();
        txt_nitrous = new javax.swing.JTextField();
        txt_water = new javax.swing.JTextField();
        lbl_carbon = new javax.swing.JLabel();
        lbl_nitrous = new javax.swing.JLabel();
        lbl_methane = new javax.swing.JLabel();
        lbl_water = new javax.swing.JLabel();
        header_concentration = new javax.swing.JLabel();
        lbl_carbon_percent = new javax.swing.JLabel();
        lbl_methane_percent = new javax.swing.JLabel();
        lbl_nitrous_percent = new javax.swing.JLabel();
        lbl_water_percent = new javax.swing.JLabel();
        panel_temperature = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        button_start = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        panel_concentration.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbl_carbon.setFont(new java.awt.Font("Arial", 0, 12));
        lbl_carbon.setText("Carbon Dioxide");
        lbl_nitrous.setFont(new java.awt.Font("Arial", 0, 12));
        lbl_nitrous.setText("Nitrous Oxide");
        lbl_methane.setFont(new java.awt.Font("Arial", 0, 12));
        lbl_methane.setText("Methane");
        lbl_water.setFont(new java.awt.Font("Arial", 0, 12));
        lbl_water.setText("Water Vapor");
        header_concentration.setFont(new java.awt.Font("Tahoma", 1, 11));
        header_concentration.setText("Level of concentration");
        lbl_carbon_percent.setFont(new java.awt.Font("Arial", 0, 12));
        lbl_carbon_percent.setText("%");
        lbl_methane_percent.setFont(new java.awt.Font("Arial", 0, 12));
        lbl_methane_percent.setText("%");
        lbl_nitrous_percent.setFont(new java.awt.Font("Arial", 0, 12));
        lbl_nitrous_percent.setText("%");
        lbl_water_percent.setFont(new java.awt.Font("Arial", 0, 12));
        lbl_water_percent.setText("%");
        javax.swing.GroupLayout panel_concentrationLayout = new javax.swing.GroupLayout(panel_concentration);
        panel_concentration.setLayout(panel_concentrationLayout);
        panel_concentrationLayout.setHorizontalGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panel_concentrationLayout.createSequentialGroup().addContainerGap().addGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(header_concentration).addGroup(panel_concentrationLayout.createSequentialGroup().addGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lbl_carbon).addComponent(lbl_methane).addComponent(lbl_nitrous).addComponent(lbl_water)).addGap(18, 18, 18).addGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panel_concentrationLayout.createSequentialGroup().addComponent(txt_methane, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lbl_methane_percent)).addGroup(panel_concentrationLayout.createSequentialGroup().addComponent(txt_carbon, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lbl_carbon_percent)).addGroup(panel_concentrationLayout.createSequentialGroup().addComponent(txt_nitrous, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lbl_nitrous_percent)).addGroup(panel_concentrationLayout.createSequentialGroup().addComponent(txt_water, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lbl_water_percent))))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        panel_concentrationLayout.setVerticalGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panel_concentrationLayout.createSequentialGroup().addContainerGap().addComponent(header_concentration).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lbl_carbon).addComponent(txt_carbon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lbl_carbon_percent)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lbl_methane).addComponent(txt_methane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lbl_methane_percent)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lbl_nitrous).addComponent(txt_nitrous, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lbl_nitrous_percent)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panel_concentrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lbl_water).addComponent(txt_water, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lbl_water_percent)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        panel_temperature.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        javax.swing.GroupLayout panel_temperatureLayout = new javax.swing.GroupLayout(panel_temperature);
        panel_temperature.setLayout(panel_temperatureLayout);
        panel_temperatureLayout.setHorizontalGroup(panel_temperatureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE));
        panel_temperatureLayout.setVerticalGroup(panel_temperatureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE));
        button_start.setText("START");
        button_start.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_startMouseClicked(evt);
            }
        });
        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setRows(5);
        jTextArea2.setText("  Greenhouse Gases Transmittance \n\tSimulator\n\n   To start simulation:\n     - fill in level of concentration\n        for each greenhouse gas\n     - click START\n\n   Sum of greenhouse gasses levels\n   must be from interval <100,0>.\n");
        jScrollPane2.setViewportView(jTextArea2);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addGap(80, 80, 80).addComponent(button_start, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(panel_concentration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addGap(18, 18, 18).addComponent(panel_temperature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(panel_temperature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(jScrollPane2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(panel_concentration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(button_start))).addContainerGap()));
        pack();
    }

    private void button_startMouseClicked(java.awt.event.MouseEvent evt) {
        CarbonDioxideLevel = txt_carbon.getText();
        MethaneLevel = txt_methane.getText();
        NitrousOxideLevel = txt_nitrous.getText();
        WaterVaporLevel = txt_water.getText();
        lbl_carbon.setForeground(Color.black);
        lbl_carbon_percent.setForeground(Color.black);
        lbl_methane.setForeground(Color.black);
        lbl_methane_percent.setForeground(Color.black);
        lbl_nitrous.setForeground(Color.black);
        lbl_nitrous_percent.setForeground(Color.black);
        lbl_water.setForeground(Color.black);
        lbl_water_percent.setForeground(Color.black);
        Enums.MainWindowInput error = Presenter.ValidateInput();
        if (error == null) {
            if (this.Presenter.ValidateLevels()) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Presenter.Start();
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            } else {
                lbl_carbon.setForeground(Color.red);
                lbl_carbon_percent.setForeground(Color.red);
                lbl_methane.setForeground(Color.red);
                lbl_methane_percent.setForeground(Color.red);
                lbl_nitrous.setForeground(Color.red);
                lbl_nitrous_percent.setForeground(Color.red);
                lbl_water.setForeground(Color.red);
                lbl_water_percent.setForeground(Color.red);
            }
        } else {
            if (error == Enums.MainWindowInput.CarbonDioxideLevel) {
                lbl_carbon.setForeground(Color.red);
                lbl_carbon_percent.setForeground(Color.red);
            } else if (error == Enums.MainWindowInput.MethaneLevel) {
                lbl_methane.setForeground(Color.red);
                lbl_methane_percent.setForeground(Color.red);
            } else if (error == Enums.MainWindowInput.NitrousOxideLevel) {
                lbl_nitrous.setForeground(Color.red);
                lbl_nitrous_percent.setForeground(Color.red);
            } else if (error == Enums.MainWindowInput.WaterVaporLevel) {
                lbl_water.setForeground(Color.red);
                lbl_water_percent.setForeground(Color.red);
            }
        }
    }

    public javax.swing.JButton GetStartButton() {
        return this.button_start;
    }

    public javax.swing.JTextArea GetTextArea() {
        return this.jTextArea1;
    }

    private javax.swing.JButton button_start;

    private javax.swing.JLabel header_concentration;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextArea jTextArea2;

    private javax.swing.JLabel lbl_carbon;

    private javax.swing.JLabel lbl_carbon_percent;

    private javax.swing.JLabel lbl_methane;

    private javax.swing.JLabel lbl_methane_percent;

    private javax.swing.JLabel lbl_nitrous;

    private javax.swing.JLabel lbl_nitrous_percent;

    private javax.swing.JLabel lbl_water;

    private javax.swing.JLabel lbl_water_percent;

    private javax.swing.JPanel panel_concentration;

    private javax.swing.JPanel panel_temperature;

    private javax.swing.JTextField txt_carbon;

    private javax.swing.JTextField txt_methane;

    private javax.swing.JTextField txt_nitrous;

    private javax.swing.JTextField txt_water;
}
