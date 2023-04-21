package GUI;

import Model.Objects.ElementoLaminas;
import java.text.DecimalFormat;
import javax.swing.table.TableColumn;

/**
 *
 * @author PaO
 */
public class ConsolidacionLaminaDialog extends javax.swing.JDialog {

    DecimalFormat formato = new DecimalFormat("###,##0.00");

    /** Creates new form ConsolidacionLaminaDialog */
    public ConsolidacionLaminaDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        formatoTablaLaminaCons();
    }

    ConsolidacionLaminaDialog(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        formatoTablaLaminaCons();
    }

    private void formatoTablaLaminaCons() {
        TableColumn column = null;
        column = this.jTable2.getColumnModel().getColumn(0);
        column.setPreferredWidth(5);
        column = this.jTable2.getColumnModel().getColumn(1);
        column.setPreferredWidth(5);
        column = this.jTable2.getColumnModel().getColumn(2);
        column.setPreferredWidth(400);
        column = this.jTable2.getColumnModel().getColumn(3);
        column.setPreferredWidth(30);
        column = this.jTable2.getColumnModel().getColumn(4);
        column.setPreferredWidth(30);
        column = this.jTable2.getColumnModel().getColumn(5);
        column.setPreferredWidth(30);
    }

    public void resetConsolidaLam() {
        for (int i = 0; i < 10; i++) for (int j = 0; j < 6; j++) this.jTable2.setValueAt("", i, j);
        this.jFormattedTextField128.setValue(null);
        this.jFormattedTextField114.setValue(null);
        this.jFormattedTextField129.setValue(null);
        this.jFormattedTextField115.setValue(null);
        this.jFormattedTextField130.setValue(null);
        this.jFormattedTextField116.setValue(null);
        this.jFormattedTextField131.setValue(null);
        this.jFormattedTextField117.setValue(null);
        this.jFormattedTextField126.setValue(null);
        this.jFormattedTextField127.setValue(null);
        this.jFormattedTextField118.setValue(null);
        this.jFormattedTextField119.setValue(null);
        this.jFormattedTextField120.setValue(null);
        this.jFormattedTextField121.setValue(null);
        this.jFormattedTextField122.setValue(null);
        this.jFormattedTextField123.setValue(null);
        this.jFormattedTextField124.setValue(null);
        this.jFormattedTextField125.setValue(null);
        this.jFormattedTextField132.setValue(null);
        this.jFormattedTextField133.setValue(null);
        this.jFormattedTextField134.setValue(null);
        this.jFormattedTextField135.setValue(null);
        this.jFormattedTextField11.setValue(null);
        this.jFormattedTextField13.setValue(null);
        this.jFormattedTextField14.setValue(null);
        this.jFormattedTextField15.setValue(null);
        this.jFormattedTextField16.setValue(null);
    }

    public void alimentaComponentes() {
        if (RegasapView.elementos.size() > 1) {
            double importe_suma = 0;
            double iva_suma = 0;
            double total_suma = 0;
            double desc_suma = 0;
            double imp_cian = 0, imp_magenta = 0, imp_amarillo = 0, imp_negro = 0, imp_gris = 0, imp_pant1 = 0, imp_pant2 = 0, imp_pant3 = 0, imp_tiempo = 0;
            int total_elementos = 0;
            int cantidad_suma = 0;
            int cian = 0, magenta = 0, amarillo = 0, negro = 0, gris = 0, pant1 = 0, pant2 = 0, pant3 = 0, tiempo = 0;
            ElementoLaminas e;
            resetConsolidaLam();
            for (int i = 0; i < RegasapView.elementos.size(); i++) {
                e = new ElementoLaminas();
                e = (ElementoLaminas) RegasapView.elementos.get(i);
                this.jTable2.setValueAt(e.getId(), i, 0);
                this.jTable2.setValueAt(e.getCantidad(), i, 1);
                this.jTable2.setValueAt(e.impresionConsolidada(), i, 2);
                this.jTable2.setValueAt(formato.format(e.getImporte()), i, 3);
                this.jTable2.setValueAt(formato.format(e.getIva()), i, 4);
                this.jTable2.setValueAt(formato.format(e.getTotal()), i, 5);
                cantidad_suma += e.getCantidad();
                importe_suma += e.getImporte();
                iva_suma += e.getIva();
                System.out.println("vuelta " + i + String.valueOf(importe_suma));
                desc_suma += e.getDescuento();
                System.out.println("Precio de la lamina: " + String.valueOf(e.getPrecio()));
                cian += e.getCian();
                imp_cian += (e.getCian() * e.getPrecio());
                magenta += e.getMagenta();
                imp_magenta += (e.getMagenta() * e.getPrecio());
                amarillo += e.getAmarillo();
                imp_amarillo += (e.getAmarillo() * e.getPrecio());
                negro += e.getNegro();
                imp_negro += (e.getNegro() * e.getPrecio());
                gris += e.getGris();
                imp_gris += (e.getGris() * e.getPrecio());
                pant1 += e.getPantone1();
                imp_pant1 += (e.getPantone1() * e.getPrecio());
                pant2 += e.getPantone2();
                imp_pant2 += (e.getPantone2() * e.getPrecio());
                pant3 += e.getPantone3();
                imp_pant3 += (e.getPantone3() * e.getPrecio());
                tiempo += e.getTiempo_maquina();
                imp_tiempo += (e.getTiempo_maquina() * e.getCosto_tiempo());
            }
            total_elementos = RegasapView.elementos.size();
            total_suma = (importe_suma + iva_suma) - desc_suma;
            this.jFormattedTextField128.setValue(cian);
            this.jFormattedTextField114.setValue(imp_cian);
            this.jFormattedTextField129.setValue(magenta);
            this.jFormattedTextField115.setValue(imp_magenta);
            this.jFormattedTextField130.setValue(amarillo);
            this.jFormattedTextField116.setValue(imp_amarillo);
            this.jFormattedTextField131.setValue(negro);
            this.jFormattedTextField117.setValue(imp_negro);
            this.jFormattedTextField126.setValue(gris);
            this.jFormattedTextField127.setValue(imp_gris);
            this.jFormattedTextField118.setValue(pant1);
            this.jFormattedTextField119.setValue(imp_pant1);
            this.jFormattedTextField120.setValue(pant2);
            this.jFormattedTextField121.setValue(imp_pant2);
            this.jFormattedTextField122.setValue(pant3);
            this.jFormattedTextField123.setValue(imp_pant3);
            this.jFormattedTextField124.setValue(tiempo);
            this.jFormattedTextField125.setValue(imp_tiempo);
            this.jFormattedTextField132.setValue(importe_suma);
            this.jFormattedTextField133.setValue(iva_suma);
            this.jFormattedTextField134.setValue(desc_suma);
            this.jFormattedTextField135.setValue(total_suma);
            this.jFormattedTextField13.setValue(total_elementos);
            this.jFormattedTextField14.setValue(cantidad_suma);
            this.jFormattedTextField11.setValue(importe_suma);
            this.jFormattedTextField16.setValue(iva_suma);
            this.jFormattedTextField15.setValue(total_suma);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        consolidaLaminaPn = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jFormattedTextField11 = new javax.swing.JFormattedTextField();
        jFormattedTextField13 = new javax.swing.JFormattedTextField();
        jFormattedTextField14 = new javax.swing.JFormattedTextField();
        jFormattedTextField15 = new javax.swing.JFormattedTextField();
        jFormattedTextField16 = new javax.swing.JFormattedTextField();
        costosLaminaPn3 = new javax.swing.JPanel();
        jLabel255 = new javax.swing.JLabel();
        jLabel256 = new javax.swing.JLabel();
        jLabel257 = new javax.swing.JLabel();
        jLabel258 = new javax.swing.JLabel();
        jLabel259 = new javax.swing.JLabel();
        jLabel260 = new javax.swing.JLabel();
        jLabel261 = new javax.swing.JLabel();
        jLabel262 = new javax.swing.JLabel();
        jLabel263 = new javax.swing.JLabel();
        jFormattedTextField114 = new javax.swing.JFormattedTextField();
        jFormattedTextField115 = new javax.swing.JFormattedTextField();
        jFormattedTextField116 = new javax.swing.JFormattedTextField();
        jFormattedTextField117 = new javax.swing.JFormattedTextField();
        jFormattedTextField118 = new javax.swing.JFormattedTextField();
        jFormattedTextField119 = new javax.swing.JFormattedTextField();
        jFormattedTextField120 = new javax.swing.JFormattedTextField();
        jFormattedTextField121 = new javax.swing.JFormattedTextField();
        jFormattedTextField122 = new javax.swing.JFormattedTextField();
        jFormattedTextField123 = new javax.swing.JFormattedTextField();
        jFormattedTextField124 = new javax.swing.JFormattedTextField();
        jFormattedTextField125 = new javax.swing.JFormattedTextField();
        jFormattedTextField126 = new javax.swing.JFormattedTextField();
        jFormattedTextField127 = new javax.swing.JFormattedTextField();
        jFormattedTextField128 = new javax.swing.JFormattedTextField();
        jFormattedTextField129 = new javax.swing.JFormattedTextField();
        jFormattedTextField130 = new javax.swing.JFormattedTextField();
        jFormattedTextField131 = new javax.swing.JFormattedTextField();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel264 = new javax.swing.JLabel();
        jLabel265 = new javax.swing.JLabel();
        jLabel266 = new javax.swing.JLabel();
        jLabel267 = new javax.swing.JLabel();
        jLabel268 = new javax.swing.JLabel();
        jLabel269 = new javax.swing.JLabel();
        jLabel270 = new javax.swing.JLabel();
        jLabel271 = new javax.swing.JLabel();
        jLabel272 = new javax.swing.JLabel();
        jLabel273 = new javax.swing.JLabel();
        jLabel274 = new javax.swing.JLabel();
        jLabel275 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jFormattedTextField132 = new javax.swing.JFormattedTextField();
        jFormattedTextField133 = new javax.swing.JFormattedTextField();
        jFormattedTextField134 = new javax.swing.JFormattedTextField();
        jFormattedTextField135 = new javax.swing.JFormattedTextField();
        jLabel276 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(GUI.Regasap.class).getContext().getResourceMap(ConsolidacionLaminaDialog.class);
        consolidaLaminaPn.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), resourceMap.getString("consolidaLaminaPn.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("consolidaLaminaPn.border.titleFont")));
        consolidaLaminaPn.setName("consolidaLaminaPn");
        consolidaLaminaPn.setLayout(null);
        jScrollPane2.setName("jScrollPane2");
        jTable2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null }, { null, null, null, null, null, null } }, new String[] { "#", "Cant", "Descripcion", "Subtotal", "I.V.A.", "Total" }));
        jTable2.setName("jTable2");
        jScrollPane2.setViewportView(jTable2);
        consolidaLaminaPn.add(jScrollPane2);
        jScrollPane2.setBounds(10, 40, 730, 190);
        jFormattedTextField11.setEditable(false);
        jFormattedTextField11.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField11.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jFormattedTextField11.setName("jFormattedTextField11");
        consolidaLaminaPn.add(jFormattedTextField11);
        jFormattedTextField11.setBounds(540, 240, 60, 20);
        jFormattedTextField13.setEditable(false);
        jFormattedTextField13.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jFormattedTextField13.setName("jFormattedTextField13");
        consolidaLaminaPn.add(jFormattedTextField13);
        jFormattedTextField13.setBounds(10, 240, 40, 20);
        jFormattedTextField14.setEditable(false);
        jFormattedTextField14.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jFormattedTextField14.setName("jFormattedTextField14");
        consolidaLaminaPn.add(jFormattedTextField14);
        jFormattedTextField14.setBounds(60, 240, 40, 20);
        jFormattedTextField15.setEditable(false);
        jFormattedTextField15.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField15.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jFormattedTextField15.setName("jFormattedTextField15");
        jFormattedTextField15.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField15ActionPerformed(evt);
            }
        });
        consolidaLaminaPn.add(jFormattedTextField15);
        jFormattedTextField15.setBounds(680, 240, 60, 20);
        jFormattedTextField16.setEditable(false);
        jFormattedTextField16.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField16.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jFormattedTextField16.setName("jFormattedTextField16");
        consolidaLaminaPn.add(jFormattedTextField16);
        jFormattedTextField16.setBounds(610, 240, 60, 20);
        costosLaminaPn3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), resourceMap.getString("costosLaminaPn3.border.title"), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("costosLaminaPn3.border.titleFont")));
        costosLaminaPn3.setName("costosLaminaPn3");
        jLabel255.setFont(resourceMap.getFont("jLabel255.font"));
        jLabel255.setText(resourceMap.getString("jLabel255.text"));
        jLabel255.setName("jLabel255");
        jLabel256.setFont(resourceMap.getFont("jLabel256.font"));
        jLabel256.setText(resourceMap.getString("jLabel256.text"));
        jLabel256.setName("jLabel256");
        jLabel257.setFont(resourceMap.getFont("jLabel257.font"));
        jLabel257.setText(resourceMap.getString("jLabel257.text"));
        jLabel257.setName("jLabel257");
        jLabel258.setFont(resourceMap.getFont("jLabel258.font"));
        jLabel258.setText(resourceMap.getString("jLabel258.text"));
        jLabel258.setName("jLabel258");
        jLabel259.setFont(resourceMap.getFont("jLabel259.font"));
        jLabel259.setText(resourceMap.getString("jLabel259.text"));
        jLabel259.setName("jLabel259");
        jLabel260.setFont(resourceMap.getFont("jLabel260.font"));
        jLabel260.setText(resourceMap.getString("jLabel260.text"));
        jLabel260.setName("jLabel260");
        jLabel261.setFont(resourceMap.getFont("jLabel261.font"));
        jLabel261.setText(resourceMap.getString("jLabel261.text"));
        jLabel261.setName("jLabel261");
        jLabel262.setFont(resourceMap.getFont("jLabel262.font"));
        jLabel262.setText(resourceMap.getString("jLabel262.text"));
        jLabel262.setName("jLabel262");
        jLabel263.setFont(resourceMap.getFont("jLabel263.font"));
        jLabel263.setText(resourceMap.getString("jLabel263.text"));
        jLabel263.setName("jLabel263");
        jFormattedTextField114.setEditable(false);
        jFormattedTextField114.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField114.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField114.setName("jFormattedTextField114");
        jFormattedTextField114.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField114ActionPerformed(evt);
            }
        });
        jFormattedTextField115.setEditable(false);
        jFormattedTextField115.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField115.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField115.setName("jFormattedTextField115");
        jFormattedTextField116.setEditable(false);
        jFormattedTextField116.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField116.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField116.setName("jFormattedTextField116");
        jFormattedTextField117.setEditable(false);
        jFormattedTextField117.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField117.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField117.setName("jFormattedTextField117");
        jFormattedTextField118.setEditable(false);
        jFormattedTextField118.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField118.setName("jFormattedTextField118");
        jFormattedTextField119.setEditable(false);
        jFormattedTextField119.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField119.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField119.setName("jFormattedTextField119");
        jFormattedTextField119.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField119ActionPerformed(evt);
            }
        });
        jFormattedTextField120.setEditable(false);
        jFormattedTextField120.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField120.setName("jFormattedTextField120");
        jFormattedTextField121.setEditable(false);
        jFormattedTextField121.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField121.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField121.setName("jFormattedTextField121");
        jFormattedTextField121.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField121ActionPerformed(evt);
            }
        });
        jFormattedTextField122.setEditable(false);
        jFormattedTextField122.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField122.setName("jFormattedTextField122");
        jFormattedTextField123.setEditable(false);
        jFormattedTextField123.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField123.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField123.setName("jFormattedTextField123");
        jFormattedTextField123.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField123ActionPerformed(evt);
            }
        });
        jFormattedTextField124.setEditable(false);
        jFormattedTextField124.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField124.setName("jFormattedTextField124");
        jFormattedTextField125.setEditable(false);
        jFormattedTextField125.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField125.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField125.setName("jFormattedTextField125");
        jFormattedTextField125.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField125ActionPerformed(evt);
            }
        });
        jFormattedTextField126.setEditable(false);
        jFormattedTextField126.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField126.setName("jFormattedTextField126");
        jFormattedTextField127.setEditable(false);
        jFormattedTextField127.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField127.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField127.setName("jFormattedTextField127");
        jFormattedTextField128.setEditable(false);
        jFormattedTextField128.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField128.setName("jFormattedTextField128");
        jFormattedTextField128.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField128KeyTyped(evt);
            }
        });
        jFormattedTextField129.setEditable(false);
        jFormattedTextField129.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField129.setName("jFormattedTextField129");
        jFormattedTextField130.setEditable(false);
        jFormattedTextField130.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField130.setName("jFormattedTextField130");
        jFormattedTextField131.setEditable(false);
        jFormattedTextField131.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField131.setName("jFormattedTextField131");
        jSeparator12.setName("jSeparator12");
        jLabel264.setFont(resourceMap.getFont("jLabel264.font"));
        jLabel264.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel264.setText(resourceMap.getString("jLabel264.text"));
        jLabel264.setName("jLabel264");
        jLabel265.setFont(resourceMap.getFont("jLabel265.font"));
        jLabel265.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel265.setText(resourceMap.getString("jLabel265.text"));
        jLabel265.setName("jLabel265");
        jLabel266.setFont(resourceMap.getFont("jLabel266.font"));
        jLabel266.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel266.setText(resourceMap.getString("jLabel266.text"));
        jLabel266.setName("jLabel266");
        jLabel267.setFont(resourceMap.getFont("jLabel267.font"));
        jLabel267.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel267.setText(resourceMap.getString("jLabel267.text"));
        jLabel267.setName("jLabel267");
        jLabel268.setFont(resourceMap.getFont("jLabel268.font"));
        jLabel268.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel268.setText(resourceMap.getString("jLabel268.text"));
        jLabel268.setName("jLabel268");
        jLabel269.setFont(resourceMap.getFont("jLabel269.font"));
        jLabel269.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel269.setText(resourceMap.getString("jLabel269.text"));
        jLabel269.setName("jLabel269");
        jLabel270.setFont(resourceMap.getFont("jLabel270.font"));
        jLabel270.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel270.setText(resourceMap.getString("jLabel270.text"));
        jLabel270.setName("jLabel270");
        jLabel271.setFont(resourceMap.getFont("jLabel271.font"));
        jLabel271.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel271.setText(resourceMap.getString("jLabel271.text"));
        jLabel271.setName("jLabel271");
        jLabel272.setFont(resourceMap.getFont("jLabel272.font"));
        jLabel272.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel272.setText(resourceMap.getString("jLabel272.text"));
        jLabel272.setName("jLabel272");
        jLabel273.setFont(resourceMap.getFont("jLabel273.font"));
        jLabel273.setText(resourceMap.getString("jLabel273.text"));
        jLabel273.setName("jLabel273");
        jLabel274.setFont(resourceMap.getFont("jLabel274.font"));
        jLabel274.setText(resourceMap.getString("jLabel274.text"));
        jLabel274.setName("jLabel274");
        jLabel275.setFont(resourceMap.getFont("jLabel275.font"));
        jLabel275.setText(resourceMap.getString("jLabel275.text"));
        jLabel275.setName("jLabel275");
        jSeparator13.setName("jSeparator13");
        jFormattedTextField132.setEditable(false);
        jFormattedTextField132.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField132.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField132.setName("jFormattedTextField132");
        jFormattedTextField132.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField132ActionPerformed(evt);
            }
        });
        jFormattedTextField133.setEditable(false);
        jFormattedTextField133.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField133.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField133.setName("jFormattedTextField133");
        jFormattedTextField133.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField133ActionPerformed(evt);
            }
        });
        jFormattedTextField134.setEditable(false);
        jFormattedTextField134.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField134.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField134.setName("jFormattedTextField134");
        jFormattedTextField134.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField134ActionPerformed(evt);
            }
        });
        jFormattedTextField135.setEditable(false);
        jFormattedTextField135.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jFormattedTextField135.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField135.setName("jFormattedTextField135");
        jFormattedTextField135.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField135ActionPerformed(evt);
            }
        });
        jLabel276.setFont(resourceMap.getFont("jLabel276.font"));
        jLabel276.setText(resourceMap.getString("jLabel276.text"));
        jLabel276.setName("jLabel276");
        javax.swing.GroupLayout costosLaminaPn3Layout = new javax.swing.GroupLayout(costosLaminaPn3);
        costosLaminaPn3.setLayout(costosLaminaPn3Layout);
        costosLaminaPn3Layout.setHorizontalGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel273).addComponent(jLabel275)).addGroup(costosLaminaPn3Layout.createSequentialGroup().addContainerGap().addComponent(jLabel274))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jFormattedTextField132, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField134, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField133, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel256).addComponent(jLabel255).addComponent(jLabel257).addComponent(jLabel258).addComponent(jLabel262).addComponent(jLabel259).addComponent(jLabel260).addComponent(jLabel261).addGroup(costosLaminaPn3Layout.createSequentialGroup().addContainerGap().addComponent(jLabel263))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jFormattedTextField124, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addComponent(jFormattedTextField130, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addComponent(jFormattedTextField129, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addComponent(jFormattedTextField131, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addComponent(jFormattedTextField118, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addComponent(jFormattedTextField120, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addComponent(jFormattedTextField122, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addComponent(jFormattedTextField126, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE).addComponent(jFormattedTextField128, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, costosLaminaPn3Layout.createSequentialGroup().addComponent(jLabel272, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jFormattedTextField125, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel268, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel269, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel270, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel271, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jFormattedTextField123, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField127, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField119, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField121, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel265, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE).addComponent(jLabel266, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE).addComponent(jLabel267, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE).addComponent(jLabel264, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jFormattedTextField114, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField115, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField117, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField116, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))))).addGap(20, 20, 20)).addComponent(jSeparator12, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, costosLaminaPn3Layout.createSequentialGroup().addContainerGap(69, Short.MAX_VALUE).addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, costosLaminaPn3Layout.createSequentialGroup().addGap(36, 36, 36).addComponent(jLabel276).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE).addComponent(jFormattedTextField135, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(20, 20, 20)));
        costosLaminaPn3Layout.setVerticalGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel255).addComponent(jFormattedTextField114, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField128, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel264)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel256).addComponent(jFormattedTextField115, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField129, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel265)).addGap(12, 12, 12).addComponent(jLabel257).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGap(12, 12, 12).addComponent(jLabel258)).addGroup(costosLaminaPn3Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jFormattedTextField117, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jFormattedTextField130, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jFormattedTextField116, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel266)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel267).addComponent(jFormattedTextField131, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGap(27, 27, 27).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel269).addComponent(jFormattedTextField118, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel261).addComponent(jFormattedTextField120, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel271).addComponent(jFormattedTextField122, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel259).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jFormattedTextField127, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel268).addComponent(jFormattedTextField126, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel260).addComponent(jFormattedTextField119, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(6, 6, 6).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jFormattedTextField121, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel270)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel262).addComponent(jFormattedTextField123, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jFormattedTextField125, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel272).addComponent(jLabel263).addComponent(jFormattedTextField124, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(8, 8, 8).addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(27, 27, 27).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel273).addComponent(jFormattedTextField133, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel275).addComponent(jFormattedTextField134, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(5, 5, 5).addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(costosLaminaPn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jFormattedTextField135, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel276))).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGap(249, 249, 249).addComponent(jFormattedTextField132, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(costosLaminaPn3Layout.createSequentialGroup().addGap(249, 249, 249).addComponent(jLabel274)));
        consolidaLaminaPn.add(costosLaminaPn3);
        costosLaminaPn3.setBounds(750, 10, 200, 388);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(consolidaLaminaPn, javax.swing.GroupLayout.PREFERRED_SIZE, 955, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(consolidaLaminaPn, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE));
        pack();
    }

    private void jFormattedTextField15ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField114ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField119ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField121ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField123ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField125ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField128KeyTyped(java.awt.event.KeyEvent evt) {
    }

    private void jFormattedTextField132ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField133ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField134ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jFormattedTextField135ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JPanel consolidaLaminaPn;

    private javax.swing.JPanel costosLaminaPn3;

    private javax.swing.JFormattedTextField jFormattedTextField11;

    private javax.swing.JFormattedTextField jFormattedTextField114;

    private javax.swing.JFormattedTextField jFormattedTextField115;

    private javax.swing.JFormattedTextField jFormattedTextField116;

    private javax.swing.JFormattedTextField jFormattedTextField117;

    private javax.swing.JFormattedTextField jFormattedTextField118;

    private javax.swing.JFormattedTextField jFormattedTextField119;

    private javax.swing.JFormattedTextField jFormattedTextField120;

    private javax.swing.JFormattedTextField jFormattedTextField121;

    private javax.swing.JFormattedTextField jFormattedTextField122;

    private javax.swing.JFormattedTextField jFormattedTextField123;

    private javax.swing.JFormattedTextField jFormattedTextField124;

    private javax.swing.JFormattedTextField jFormattedTextField125;

    private javax.swing.JFormattedTextField jFormattedTextField126;

    private javax.swing.JFormattedTextField jFormattedTextField127;

    private javax.swing.JFormattedTextField jFormattedTextField128;

    private javax.swing.JFormattedTextField jFormattedTextField129;

    private javax.swing.JFormattedTextField jFormattedTextField13;

    private javax.swing.JFormattedTextField jFormattedTextField130;

    private javax.swing.JFormattedTextField jFormattedTextField131;

    private javax.swing.JFormattedTextField jFormattedTextField132;

    private javax.swing.JFormattedTextField jFormattedTextField133;

    private javax.swing.JFormattedTextField jFormattedTextField134;

    private javax.swing.JFormattedTextField jFormattedTextField135;

    private javax.swing.JFormattedTextField jFormattedTextField14;

    private javax.swing.JFormattedTextField jFormattedTextField15;

    private javax.swing.JFormattedTextField jFormattedTextField16;

    private javax.swing.JLabel jLabel255;

    private javax.swing.JLabel jLabel256;

    private javax.swing.JLabel jLabel257;

    private javax.swing.JLabel jLabel258;

    private javax.swing.JLabel jLabel259;

    private javax.swing.JLabel jLabel260;

    private javax.swing.JLabel jLabel261;

    private javax.swing.JLabel jLabel262;

    private javax.swing.JLabel jLabel263;

    private javax.swing.JLabel jLabel264;

    private javax.swing.JLabel jLabel265;

    private javax.swing.JLabel jLabel266;

    private javax.swing.JLabel jLabel267;

    private javax.swing.JLabel jLabel268;

    private javax.swing.JLabel jLabel269;

    private javax.swing.JLabel jLabel270;

    private javax.swing.JLabel jLabel271;

    private javax.swing.JLabel jLabel272;

    private javax.swing.JLabel jLabel273;

    private javax.swing.JLabel jLabel274;

    private javax.swing.JLabel jLabel275;

    private javax.swing.JLabel jLabel276;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator12;

    private javax.swing.JSeparator jSeparator13;

    private javax.swing.JTable jTable2;
}
