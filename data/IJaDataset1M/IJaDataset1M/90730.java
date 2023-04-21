package ru.spb.leti.g6351.kinpo.adressbook.main.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.text.JTextComponent;
import ru.spb.leti.g6351.kinpo.adressbook.data.DefaultDataSource;
import ru.spb.leti.g6351.kinpo.adressbook.data.IsDataSource;
import ru.spb.leti.g6351.kinpo.adressbook.gui.FieldsController;
import ru.spb.leti.g6351.kinpo.adressbook.gui.StringConstants;
import ru.spb.leti.g6351.kinpo.adressbook.handler.ButtonListener;
import ru.spb.leti.g6351.kinpo.adressbook.handler.CheckValuesHandler;
import ru.spb.leti.g6351.kinpo.adressbook.handler.InsertNewHandler;
import ru.spb.leti.g6351.kinpo.adressbook.handler.ShowTableHandler;
import ru.spb.leti.g6351.kinpo.adressbook.handler.UpdateHandler;
import ru.spb.leti.g6351.kinpo.adressbook.validators.DateValidator;
import ru.spb.leti.g6351.kinpo.adressbook.validators.DefaultValidator;
import ru.spb.leti.g6351.kinpo.adressbook.validators.DoubleIntervalValidator;
import ru.spb.leti.g6351.kinpo.adressbook.validators.IntegerIntervalValidator;
import ru.spb.leti.g6351.kinpo.adressbook.validators.NameValidator;
import ru.spb.leti.g6351.kinpo.adressbook.validators.SymbolValidator;
import ru.spb.leti.g6351.kinpo.adressbook.validators.UrlValidator;
import ru.spb.leti.g6351.kinpo.common.Interval;

/**
 * 
 * @author nikita
 */
public class MainFrame extends javax.swing.JFrame implements StringConstants {

    private static MainFrame s_instance;

    public static MainFrame getInstance() {
        return s_instance;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = -6576112152489961075L;

    /** Creates new form MainFrame */
    public MainFrame() {
        s_instance = this;
        setTitle(TITLE);
        initComponents();
        initFields();
        initHandlers();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSurnameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jStreetField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jCityField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jHomePhoneField = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jMobilePhoneField = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jPostalCodeField = new javax.swing.JFormattedTextField();
        jHouseField = new javax.swing.JFormattedTextField();
        jNameField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jEmailField = new javax.swing.JFormattedTextField();
        jSiteField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jBurthDay = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jOpenListButton = new javax.swing.JButton();
        jSaveButton = new javax.swing.JButton();
        jButtonInsert = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jHeightField = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        jLocationField = new javax.swing.JFormattedTextField();
        jCacheField = new javax.swing.JFormattedTextField();
        jVoditCatField = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Основные данные"));
        jLabel1.setText("Имя");
        jLabel2.setText("Фамилия");
        jLabel3.setText("Улица");
        jLabel4.setText("Дом/корп");
        jCityField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCityFieldActionPerformed(evt);
            }
        });
        jLabel5.setText("Город");
        try {
            jHomePhoneField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(***)***-**-**")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jLabel7.setText("Телефон(дом)");
        jLabel8.setText("Телефон(моб)");
        try {
            jMobilePhoneField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("+*(***)***-**-**")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jLabel9.setText("Индекс");
        try {
            jPostalCodeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("******")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel8).addComponent(jLabel7).addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel5).addComponent(jLabel3).addComponent(jLabel4).addComponent(jLabel9)).addGap(35, 35, 35).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jHouseField, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jPostalCodeField, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jStreetField, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCityField, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSurnameField, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)).addGap(140, 140, 140)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jMobilePhoneField, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jHomePhoneField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)).addContainerGap())))))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1)).addComponent(jNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5)).addGap(11, 11, 11).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jStreetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jHouseField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(7, 7, 7).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jHomePhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jMobilePhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9).addComponent(jPostalCodeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(18, Short.MAX_VALUE)));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Дополнительные данные"));
        jLabel6.setText("Email");
        jSiteField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSiteFieldActionPerformed(evt);
            }
        });
        jLabel10.setText("Сайт");
        jLabel11.setText("Дата рождения");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel10).addComponent(jLabel11)).addGap(47, 47, 47).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jSiteField, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jEmailField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)).addComponent(jBurthDay, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(260, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(jEmailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSiteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel10)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBurthDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11)).addContainerGap(59, Short.MAX_VALUE)));
        jOpenListButton.setText("Открыть список");
        jSaveButton.setText("Сохранить");
        jButtonInsert.setText("Добавить");
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Специальные сведения"));
        jLabel14.setText("Жилплощадь (кв.м)");
        jLabel15.setText("Водит. кат.");
        jLabel13.setText("Состояние счета");
        jLabel12.setText("Рост");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel12).addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jHeightField).addComponent(jLocationField, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel13).addComponent(jLabel15)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jVoditCatField).addComponent(jCacheField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap(25, Short.MAX_VALUE).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jHeightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCacheField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel13)).addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLocationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jVoditCatField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel15).addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 315, Short.MAX_VALUE).addComponent(jOpenListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(11, 11, 11).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jSaveButton).addComponent(jButtonInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jOpenListButton)).addContainerGap(50, Short.MAX_VALUE)));
        pack();
    }

    private void jSiteFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jCityFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    private javax.swing.JFormattedTextField jBurthDay;

    private javax.swing.JButton jButtonInsert;

    private javax.swing.JFormattedTextField jCacheField;

    private javax.swing.JTextField jCityField;

    private javax.swing.JFormattedTextField jEmailField;

    private javax.swing.JFormattedTextField jHeightField;

    private javax.swing.JFormattedTextField jHomePhoneField;

    private javax.swing.JFormattedTextField jHouseField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JFormattedTextField jLocationField;

    private javax.swing.JFormattedTextField jMobilePhoneField;

    private javax.swing.JFormattedTextField jNameField;

    private javax.swing.JButton jOpenListButton;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JFormattedTextField jPostalCodeField;

    private javax.swing.JButton jSaveButton;

    private javax.swing.JTextField jSiteField;

    private javax.swing.JTextField jStreetField;

    private javax.swing.JTextField jSurnameField;

    private javax.swing.JFormattedTextField jVoditCatField;

    private void initFields() {
        jEmailField.setName("Email");
        new DefaultValidator(jEmailField, REGEXP_EMAIL, "Нестандартный формат адреса email", true);
        jSiteField.setName("Сайт");
        new UrlValidator(jSiteField);
        new DefaultValidator(jHouseField, REGEXP_HOUSE, "Номер дома не соответствует формату.", true);
        new NameValidator(jNameField, false);
        new NameValidator(jSurnameField, false);
        new NameValidator(jStreetField, true);
        new NameValidator(jCityField, true);
        new DateValidator(jBurthDay);
        new DefaultValidator(jPostalCodeField, "(\\s*|\\d{6})", "Неправильный формат почтового индекса", true);
        new DefaultValidator(jHomePhoneField, REGEXP_HOME_PHONE, "Номер телефона не соответствует формату.", true);
        new DefaultValidator(jMobilePhoneField, REGEXP_CELL_PHONE, "Номер телефона не соответствует формату.", true);
        new IntegerIntervalValidator(jHeightField, new Interval<Integer>(30, true, 300, true));
        ArrayList<Interval<Double>> list = new ArrayList<Interval<Double>>();
        list.add(new Interval<Double>(-500000.0, false, 0.0, false));
        list.add(new Interval<Double>(10000.0, false, 100000.0, true));
        new DoubleIntervalValidator(jCacheField, list);
        new DoubleIntervalValidator(jLocationField, new Interval<Double>(18.0, true, Double.POSITIVE_INFINITY, false));
        new SymbolValidator(jVoditCatField, new Interval<Character>('A', true, 'E', true));
    }

    private void initHandlers() {
        FieldsController.initController();
        jOpenListButton.addActionListener(new ButtonListener(new ShowTableHandler()));
        jSaveButton.addActionListener(new ButtonListener(new CheckValuesHandler(s_fieldsMap.values()), new UpdateHandler()));
        jButtonInsert.addActionListener(new ButtonListener(new CheckValuesHandler(s_fieldsMap.values()), new InsertNewHandler()));
    }

    public static IsDataSource getDefaultDataSource() {
        return DefaultDataSource.getInstance();
    }

    public HashMap<String, JTextComponent> getFieldsMap() {
        if (s_fieldsMap.isEmpty()) initFieldsMap();
        return s_fieldsMap;
    }

    private static HashMap<String, JTextComponent> s_fieldsMap = new HashMap<String, JTextComponent>(20);

    private void initFieldsMap() {
        s_fieldsMap.put(FD_FNAME, jNameField);
        s_fieldsMap.put(FD_SNAME, jSurnameField);
        s_fieldsMap.put(FD_HOUSE, jHouseField);
        s_fieldsMap.put(FD_HPHONE, jHomePhoneField);
        s_fieldsMap.put(FD_CPHONE, jMobilePhoneField);
        s_fieldsMap.put(FD_WEBSITE, jSiteField);
        s_fieldsMap.put(FD_EMAIL, jEmailField);
        s_fieldsMap.put(FD_CITY, jCityField);
        s_fieldsMap.put(FD_STREET, jStreetField);
        s_fieldsMap.put(FD_CACHE, jCacheField);
        s_fieldsMap.put(FD_HEIGHT, jHeightField);
        s_fieldsMap.put(FD_VODIT, jVoditCatField);
        s_fieldsMap.put(FD_LOCATION, jLocationField);
        s_fieldsMap.put(FD_POSTCODE, jPostalCodeField);
        s_fieldsMap.put(FD_BURTHDAY, jBurthDay);
    }

    public void cleanFields() {
        for (Entry<String, JTextComponent> e : getFieldsMap().entrySet()) e.getValue().setText("");
    }
}
