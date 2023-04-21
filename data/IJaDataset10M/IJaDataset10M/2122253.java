package saadadb.admin.mapping;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import saadadb.admin.SaadaDBAdmin;
import saadadb.admin.dialogs.DialogConfName;
import saadadb.admin.dialogs.DialogFileChooser;
import saadadb.admin.tree.VoDataProductTree;
import saadadb.admin.widgets.AppendMappingTextField;
import saadadb.admin.widgets.ExtMappingTextField;
import saadadb.admin.widgets.ReplaceMappingTextField;
import saadadb.api.SaadaDB;
import saadadb.collection.Category;
import saadadb.command.ArgsParser;
import saadadb.database.Database;
import saadadb.exceptions.FatalException;
import saadadb.util.DefineType;
import saadadb.util.Messenger;
import saadadb.util.RegExp;

/**
 * @author michel
 * @version $Id: MappingKWPanel.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public class MappingKWPanel extends JPanel implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected JLabel conf_name;

    public final int category;

    private AppendMappingTextField name_compo;

    protected AppendMappingTextField e_name_compo;

    protected AppendMappingTextField ignored_compo;

    protected AppendMappingTextField e_ignored_compo;

    protected ReplaceMappingTextField[] extatt_fields;

    protected ReplaceMappingTextField[] e_extatt_fields;

    protected String[] ext_att = null;

    protected String[] e_ext_att = null;

    protected JRadioButton coosys_only_btn;

    protected JRadioButton coosys_first_btn;

    protected JRadioButton coosys_last_btn;

    protected JRadioButton coosys_no_btn;

    protected JComboBox coosys_combo;

    protected AppendMappingTextField coosys_field;

    protected JRadioButton coo_only_btn;

    protected JRadioButton coo_first_btn;

    protected JRadioButton coo_last_btn;

    protected JRadioButton coo_no_btn;

    protected AppendMappingTextField coo_field;

    protected JRadioButton errcoo_only_btn;

    protected JRadioButton errcoo_first_btn;

    protected JRadioButton errcoo_last_btn;

    protected JRadioButton errcoo_no_btn;

    protected AppendMappingTextField errcoo_field;

    protected JComboBox errcoo_unit;

    protected JRadioButton spec_only_btn;

    protected JRadioButton spec_first_btn;

    protected JRadioButton spec_last_btn;

    protected JRadioButton spec_no_btn;

    protected ReplaceMappingTextField spec_field;

    protected JComboBox specunit_combo;

    public ExtMappingTextField extension_field;

    protected JRadioButton classifier_btn;

    protected JRadioButton fusion_btn;

    protected JRadioButton noclass_btn;

    protected JTextField class_field;

    private JPanel category_panel;

    private JPanel extension_panel;

    private JPanel class_panel;

    private JPanel ignored_panel;

    private JPanel e_ignored_panel;

    private JPanel entry_panel;

    private JPanel name_panel;

    private JPanel e_name_panel;

    private JPanel extatt_panel;

    private JPanel e_extatt_panel;

    private JPanel coordsys_panel;

    private JPanel coord_panel;

    private JPanel coorderror_panel;

    private JPanel spccoord_panel;

    private GridBagConstraints c = new GridBagConstraints();

    private JDataLoaderConfPanel frame;

    private String last_saved = "";

    /**
	 * @param frame
	 */
    public MappingKWPanel(JDataLoaderConfPanel frame, int category) {
        super();
        this.category = category;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = c.weighty = 0.5;
        this.frame = frame;
        this.setLayout(new GridBagLayout());
        this.addCategoryPanel();
        this.addClassPanel();
        switch(this.category) {
            case Category.MISC:
                this.buildMiscPanel();
                break;
            case Category.IMAGE:
                this.buildImagePanel();
                break;
            case Category.SPECTRUM:
                this.buildSpectraPanel();
                break;
            case Category.TABLE:
                this.buildTablePanel();
                break;
            case Category.FLATFILE:
                this.buildFlatfilePanel();
                break;
        }
    }

    /**
	 * 
	 */
    private void addCategoryPanel() {
        if (category_panel == null) {
            category_panel = new JPanel(new GridBagLayout());
            category_panel.setBackground(SaadaDBAdmin.beige_color);
            category_panel.setBorder(BorderFactory.createTitledBorder("Product Category"));
            GridBagConstraints ccs = new GridBagConstraints();
            ccs.gridx = 0;
            ccs.gridy = 0;
            ccs.anchor = GridBagConstraints.EAST;
            ccs.gridheight = 2;
            ccs.weightx = 0.5;
            category_panel.add(SaadaDBAdmin.getPlainLabel("Configuration Name: "), ccs);
            ccs.gridx = 1;
            ccs.gridy = 0;
            ccs.anchor = GridBagConstraints.WEST;
            conf_name = new JLabel("null");
            category_panel.add(conf_name, ccs);
            ccs.gridx = 0;
            ccs.gridy = 2;
            ccs.anchor = GridBagConstraints.CENTER;
            ccs.gridwidth = 1;
            ccs.gridheight = 1;
            JLabel ds = SaadaDBAdmin.getPlainLabel("<HTML><A HREF=>Form Reset</A>");
            ds.addMouseListener(new MouseAdapter() {

                public void mouseReleased(MouseEvent e) {
                    reset(false);
                }
            });
            category_panel.add(ds, ccs);
            ccs.gridx = 1;
            ccs.gridy = 2;
            ccs.anchor = GridBagConstraints.WEST;
            ds = SaadaDBAdmin.getPlainLabel("<HTML><A HREF=>Loader Parameters</A>");
            ds.setToolTipText("Show dataloader parameters matching the current configuration.");
            ds.addMouseListener(new MouseAdapter() {

                public void mouseReleased(MouseEvent e) {
                    ArgsParser args_parser = getArgsParser();
                    if (args_parser != null) {
                        String[] args = args_parser.getArgs();
                        String summary = "";
                        for (int i = 0; i < args.length; i++) {
                            summary += args[i] + "\n";
                        }
                        SaadaDBAdmin.showCopiableInfo(frame, summary, "Loader Parameters");
                    }
                }
            });
            category_panel.add(ds, ccs);
            ccs.gridx = 2;
            ccs.gridy = 2;
            ccs.anchor = GridBagConstraints.LAST_LINE_START;
            ds = SaadaDBAdmin.getPlainLabel("<HTML><A HREF=>Data Sample</A> ");
            ds.setToolTipText("Show dataloader parameters matching the current configuration.");
            ds.addMouseListener(new MouseAdapter() {

                public void mouseReleased(MouseEvent e) {
                    DialogFileChooser fcd = new DialogFileChooser();
                    String filename = fcd.open(frame.frame, true);
                    if (filename.length() == 0) {
                        return;
                    }
                    frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    JFrame window = new JFrame(new File(filename).getName());
                    VoDataProductTree vot;
                    vot = new VoDataProductTree(window, "ext/keywords (drag & drop to the loader configuration panel)", filename);
                    frame.setCursor(Cursor.getDefaultCursor());
                    vot.buildTree(new Dimension(300, 500));
                    vot.setPreferredSize(new Dimension(300, 500));
                    window.add(vot);
                    window.pack();
                    window.setVisible(true);
                }
            });
            category_panel.add(ds, ccs);
            c.weightx = 0;
            this.add(category_panel, c);
        }
    }

    /**			GridBagConstraints ccs = new GridBagConstraints();

	 * 
	 */
    private void addExtensionPanel() {
        if (extension_panel == null) {
            extension_panel = new JPanel(new GridBagLayout());
            extension_panel.setBackground(SaadaDBAdmin.beige_color);
            GridBagConstraints ccs = new GridBagConstraints();
            extension_panel.setBorder(BorderFactory.createTitledBorder("Extension to Load"));
            ccs.gridx = 0;
            ccs.gridy = 0;
            extension_panel.add(SaadaDBAdmin.getPlainLabel("Extension name or number (prefixed with a #) "), ccs);
            extension_field = new ExtMappingTextField(this, 1, false, null);
            extension_field.setColumns(15);
            ccs.gridx = 1;
            ccs.gridy = 0;
            ccs.fill = GridBagConstraints.HORIZONTAL;
            extension_panel.add(extension_field, ccs);
            c.weightx = 0;
            this.add(extension_panel, c);
            extension_field.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MappingKWPanel.this.checkExtensionChange();
                }
            });
        }
    }

    /**
	 * 
	 */
    private void addClassPanel() {
        if (class_panel == null) {
            class_panel = new JPanel(new GridBagLayout());
            class_panel.setBackground(SaadaDBAdmin.beige_color);
            ;
            GridBagConstraints ccs = new GridBagConstraints();
            classifier_btn = new JRadioButton("Automatic Classifier");
            classifier_btn.setToolTipText("One class (with a name derived the given one) created for each group of identical product files");
            fusion_btn = new JRadioButton("Class Fusion");
            fusion_btn.setToolTipText("One class merging all product files will be created");
            noclass_btn = new JRadioButton("Default");
            fusion_btn.setToolTipText("One class (with a default name) created for each group of identical product files");
            class_field = new JTextField();
            class_panel.setBorder(BorderFactory.createTitledBorder("Class Mapping"));
            ccs.gridx = 0;
            ccs.gridy = 0;
            class_panel.add(SaadaDBAdmin.getPlainLabel("Mapping Mode "), ccs);
            ccs.gridx = 1;
            ccs.gridy = 0;
            new PrioritySelector(new JRadioButton[] { classifier_btn, fusion_btn, noclass_btn }, noclass_btn, new ButtonGroup(), new JComponent[] { class_field }, class_panel, ccs);
            ccs.gridx = 0;
            ccs.gridy = 1;
            ccs.anchor = GridBagConstraints.LINE_END;
            class_panel.add(SaadaDBAdmin.getPlainLabel("Class Name"), ccs);
            class_field.setColumns(15);
            class_field.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (MappingKWPanel.this.noclass_btn.isSelected()) {
                        MappingKWPanel.this.fusion_btn.setSelected(true);
                    }
                }
            });
            ccs.gridx = 1;
            ccs.gridy = 1;
            ccs.gridwidth = 2;
            ccs.fill = GridBagConstraints.HORIZONTAL;
            class_panel.add(class_field, ccs);
            c.weightx = 0;
            this.add(class_panel, c);
        }
    }

    /**
	 * 
	 */
    private void addNamePanel() {
        if (name_panel == null) {
            name_panel = new JPanel();
            name_panel.setBackground(SaadaDBAdmin.beige_color);
            name_compo = new AppendMappingTextField(this, 2, false, null);
            name_compo.setColumns(40);
            name_panel.add(name_compo);
            name_panel.setBorder(BorderFactory.createTitledBorder("Instance Name"));
            this.add(name_panel, c);
        }
    }

    /**
	 * 
	 */
    private void addIgnoredPanel() {
        if (ignored_panel == null) {
            ignored_panel = new JPanel();
            ignored_panel.setBackground(SaadaDBAdmin.beige_color);
            ignored_compo = new AppendMappingTextField(this, 2, false, null);
            ignored_compo.setColumns(40);
            ignored_panel.add(ignored_compo);
            ignored_panel.setBorder(BorderFactory.createTitledBorder("Ignored Keywords"));
            this.add(ignored_panel, c);
        }
    }

    /**
	 * 
	 */
    void addAttExtendPanel() {
        extatt_panel = new JPanel();
        extatt_panel.setBackground(SaadaDBAdmin.beige_color);
        extatt_panel.setBorder(BorderFactory.createTitledBorder("Extended Collection Attributes"));
        extatt_panel.setLayout(new GridBagLayout());
        switch(this.category) {
            case Category.MISC:
                ext_att = Database.getCachemeta().getAtt_extend_misc_names();
                break;
            case Category.IMAGE:
                ext_att = Database.getCachemeta().getAtt_extend_image_names();
                break;
            case Category.SPECTRUM:
                ext_att = Database.getCachemeta().getAtt_extend_spectrum_names();
                break;
            case Category.TABLE:
                ext_att = Database.getCachemeta().getAtt_extend_table_names();
                e_ext_att = Database.getCachemeta().getAtt_extend_entry_names();
                break;
            case Category.FLATFILE:
                ext_att = Database.getCachemeta().getAtt_extend_flatfile_names();
                break;
            default:
                ext_att = e_ext_att = null;
        }
        GridBagConstraints cae = new GridBagConstraints();
        cae.anchor = GridBagConstraints.EAST;
        int numLabels = ext_att.length;
        extatt_fields = new ReplaceMappingTextField[numLabels];
        for (int i = 0; i < numLabels; i++) {
            cae.gridwidth = GridBagConstraints.RELATIVE;
            cae.fill = GridBagConstraints.NONE;
            cae.weightx = 0.0;
            extatt_panel.add(SaadaDBAdmin.getPlainLabel(ext_att[i]), cae);
            extatt_fields[i] = new ReplaceMappingTextField(this, 2, false, null);
            extatt_fields[i].setColumns(10);
            cae.gridwidth = GridBagConstraints.REMAINDER;
            cae.fill = GridBagConstraints.HORIZONTAL;
            cae.weightx = 1.0;
            extatt_panel.add(extatt_fields[i], cae);
        }
        this.add(extatt_panel, c);
    }

    /**
	 * 
	 */
    private void addCoordSysPanel() {
        if (coordsys_panel == null) {
            coordsys_panel = new JPanel();
            coordsys_panel.setBackground(SaadaDBAdmin.beige_color);
            coordsys_panel.setLayout(new GridBagLayout());
            GridBagConstraints ccs = new GridBagConstraints();
            coordsys_panel.setBorder(BorderFactory.createTitledBorder("Coordinate System"));
            coosys_only_btn = new JRadioButton("only");
            coosys_first_btn = new JRadioButton("first");
            coosys_last_btn = new JRadioButton("last");
            coosys_no_btn = new JRadioButton("no mapping");
            ButtonGroup bg = new ButtonGroup();
            coosys_field = new AppendMappingTextField(this, 2, false, bg);
            coosys_combo = new JComboBox(new String[] { "ICRS", "FK5,J2000", "Galactic", "Ecliptic" });
            ccs.gridx = 0;
            ccs.gridy = 0;
            coordsys_panel.add(SaadaDBAdmin.getPlainLabel("Priority of the mapping Vs an automatic detection"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 0;
            new PrioritySelector(new JRadioButton[] { coosys_only_btn, coosys_first_btn, coosys_last_btn, coosys_no_btn }, coosys_no_btn, bg, new JComponent[] { coosys_field, coosys_combo }, coordsys_panel, ccs);
            ccs.gridx = 0;
            ccs.gridy = 1;
            ccs.anchor = GridBagConstraints.LINE_END;
            coordsys_panel.add(SaadaDBAdmin.getPlainLabel("Select a Supported Coordinate Systems"), ccs);
            coosys_combo.addActionListener(this);
            ccs.gridx = 1;
            ccs.gridy = 1;
            ccs.gridwidth = 3;
            ccs.anchor = GridBagConstraints.LINE_START;
            coordsys_panel.add(coosys_combo, ccs);
            ccs.gridx = 0;
            ccs.gridy = 2;
            ccs.gridwidth = 1;
            ccs.anchor = GridBagConstraints.LINE_END;
            coordsys_panel.add(SaadaDBAdmin.getPlainLabel("Give quoted constant vakues or keywords"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 2;
            ccs.gridwidth = 3;
            ccs.fill = GridBagConstraints.HORIZONTAL;
            coosys_field.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (MappingKWPanel.this.coosys_no_btn.isSelected()) {
                        MappingKWPanel.this.coosys_first_btn.setSelected(true);
                    }
                }
            });
            coordsys_panel.add(coosys_field, ccs);
            this.add(coordsys_panel, c);
        }
    }

    /**
	 * 
	 */
    private void addPositionPanel() {
        if (coord_panel == null) {
            coord_panel = new JPanel();
            coord_panel.setBackground(SaadaDBAdmin.beige_color);
            coord_panel.setLayout(new GridBagLayout());
            GridBagConstraints ccs = new GridBagConstraints();
            coord_panel.setBorder(BorderFactory.createTitledBorder("Position"));
            ButtonGroup bg = new ButtonGroup();
            coo_only_btn = new JRadioButton("only");
            coo_first_btn = new JRadioButton("first");
            coo_last_btn = new JRadioButton("last");
            coo_no_btn = new JRadioButton("no mapping");
            coo_field = new AppendMappingTextField(this, 2, false, bg);
            ccs.gridx = 0;
            ccs.gridy = 0;
            coord_panel.add(SaadaDBAdmin.getPlainLabel("Priority of the mapping Vs an automatic detection"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 0;
            new PrioritySelector(new JRadioButton[] { coo_only_btn, coo_first_btn, coo_last_btn, coo_no_btn }, coo_no_btn, bg, new JComponent[] { coo_field }, coord_panel, ccs);
            ccs.gridx = 0;
            ccs.gridy = 1;
            ccs.gridwidth = 4;
            ccs.anchor = GridBagConstraints.LINE_START;
            coord_panel.add(SaadaDBAdmin.getPlainLabel("Give an object name, a numerical position or keywords"), ccs);
            ccs.gridx = 0;
            ccs.gridy = 2;
            ccs.gridwidth = 1;
            ccs.anchor = GridBagConstraints.LINE_START;
            coord_panel.add(SaadaDBAdmin.getPlainLabel("with the following format RA[,DEC]"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 2;
            ccs.gridwidth = 3;
            ccs.fill = GridBagConstraints.HORIZONTAL;
            coo_field.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (MappingKWPanel.this.coo_no_btn.isSelected()) {
                        MappingKWPanel.this.coo_first_btn.setSelected(true);
                    }
                }
            });
            coord_panel.add(coo_field, ccs);
            this.add(coord_panel, c);
        }
    }

    /**
	 * 
	 */
    private void addErrorPositionPanel() {
        if (coorderror_panel == null) {
            coorderror_panel = new JPanel();
            coorderror_panel.setBackground(SaadaDBAdmin.beige_color);
            coorderror_panel.setLayout(new GridBagLayout());
            GridBagConstraints ccs = new GridBagConstraints();
            coorderror_panel.setBorder(BorderFactory.createTitledBorder("Position Errors"));
            ButtonGroup bg = new ButtonGroup();
            errcoo_only_btn = new JRadioButton("only");
            errcoo_first_btn = new JRadioButton("first");
            errcoo_last_btn = new JRadioButton("last");
            errcoo_no_btn = new JRadioButton("no mapping");
            errcoo_unit = new JComboBox(new String[] { "deg", "arcsec", "arcmin", "mas" });
            errcoo_field = new AppendMappingTextField(this, 2, false, bg);
            coorderror_panel.add(SaadaDBAdmin.getPlainLabel("Priority of the mapping Vs an automatic detection"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 0;
            new PrioritySelector(new JRadioButton[] { errcoo_only_btn, errcoo_first_btn, errcoo_last_btn, errcoo_no_btn }, errcoo_no_btn, bg, new JComponent[] { errcoo_unit, errcoo_field }, coorderror_panel, ccs);
            ccs.gridx = 0;
            ccs.gridy = 1;
            ccs.anchor = GridBagConstraints.LINE_END;
            coorderror_panel.add(SaadaDBAdmin.getPlainLabel("Select Error Unit (degree taken by default)"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 1;
            ccs.gridwidth = 3;
            ccs.anchor = GridBagConstraints.LINE_START;
            coorderror_panel.add(errcoo_unit, ccs);
            ccs.gridx = 0;
            ccs.gridy = 2;
            ccs.gridwidth = 1;
            ccs.anchor = GridBagConstraints.LINE_END;
            coorderror_panel.add(SaadaDBAdmin.getPlainLabel("<HTML>Give quoted constant value or Keywords<BR>with the following format ERA[,EDEC[,EANGLE]]"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 2;
            ccs.gridwidth = 3;
            ccs.fill = GridBagConstraints.HORIZONTAL;
            errcoo_field.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (MappingKWPanel.this.errcoo_no_btn.isSelected()) {
                        MappingKWPanel.this.errcoo_first_btn.setSelected(true);
                    }
                }
            });
            coorderror_panel.add(errcoo_field, ccs);
            this.add(coorderror_panel, c);
        }
    }

    /**
	 * 
	 */
    private void addSpectCoordPanel() {
        if (spccoord_panel == null) {
            spccoord_panel = new JPanel();
            spccoord_panel.setBackground(SaadaDBAdmin.beige_color);
            spccoord_panel.setLayout(new GridBagLayout());
            GridBagConstraints ccs = new GridBagConstraints();
            spccoord_panel.setBorder(BorderFactory.createTitledBorder("SpectralCoordinate"));
            ButtonGroup bg = new ButtonGroup();
            spec_only_btn = new JRadioButton("only");
            spec_first_btn = new JRadioButton("first");
            spec_last_btn = new JRadioButton("last");
            spec_no_btn = new JRadioButton("no mapping");
            specunit_combo = new JComboBox();
            specunit_combo.addItem("");
            specunit_combo.addItem("Angstrom");
            specunit_combo.addItem("nm");
            specunit_combo.addItem("um");
            specunit_combo.addItem("m");
            specunit_combo.addItem("mm");
            specunit_combo.addItem("cm");
            specunit_combo.addItem("km");
            specunit_combo.addItem("nm");
            specunit_combo.addItem("Hz");
            specunit_combo.addItem("kHz");
            specunit_combo.addItem("MHz");
            specunit_combo.addItem("GHz");
            specunit_combo.addItem("eV");
            specunit_combo.addItem("keV");
            specunit_combo.addItem("MeV");
            specunit_combo.addItem("GeV");
            specunit_combo.addItem("TeV");
            spec_field = new ReplaceMappingTextField(this, 2, false, bg);
            ccs.gridx = 0;
            ccs.gridy = 0;
            spccoord_panel.add(SaadaDBAdmin.getPlainLabel("Priority of the mapping Vs an automatic detection"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 0;
            new PrioritySelector(new JRadioButton[] { spec_only_btn, spec_first_btn, spec_last_btn, spec_no_btn }, spec_no_btn, bg, new JComponent[] { specunit_combo, spec_field }, spccoord_panel, ccs);
            ccs.gridx = 0;
            ccs.gridy = 1;
            ccs.anchor = GridBagConstraints.LINE_END;
            spccoord_panel.add(SaadaDBAdmin.getPlainLabel("Dispertion Unit"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 1;
            ccs.gridwidth = 3;
            ccs.fill = GridBagConstraints.HORIZONTAL;
            spccoord_panel.add(specunit_combo, ccs);
            ccs.gridx = 0;
            ccs.gridy = 2;
            ccs.gridwidth = 1;
            ccs.anchor = GridBagConstraints.LINE_END;
            spccoord_panel.add(SaadaDBAdmin.getPlainLabel("Name of the Dispersion Column or Field"), ccs);
            ccs.gridx = 1;
            ccs.gridy = 2;
            ccs.gridwidth = 3;
            ccs.fill = GridBagConstraints.HORIZONTAL;
            spec_field.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (MappingKWPanel.this.spec_no_btn.isSelected()) {
                        MappingKWPanel.this.spec_first_btn.setSelected(true);
                    }
                }
            });
            spccoord_panel.add(spec_field, ccs);
            this.add(spccoord_panel, c);
        }
    }

    /**
	 * 
	 */
    private void addSourcePanel() {
        if (entry_panel == null) {
            entry_panel = new JPanel();
            entry_panel.setBackground(SaadaDBAdmin.beige_color);
            entry_panel.setBorder(BorderFactory.createTitledBorder("Table Entry Mapping"));
            entry_panel.setLayout(new BorderLayout());
            entry_panel.add(SaadaDBAdmin.getPlainLabel("The following parameters are related to the table entries"), BorderLayout.NORTH);
            entry_panel.add(SaadaDBAdmin.getPlainLabel("Requested Keywords must be searched in the table columns"), BorderLayout.SOUTH);
            this.add(entry_panel, c);
        }
        if (e_name_panel == null) {
            e_name_panel = new JPanel();
            e_name_panel.setBackground(SaadaDBAdmin.beige_color);
            e_name_compo = new AppendMappingTextField(this, 2, true, null);
            e_name_compo.setColumns(40);
            e_name_panel.add(e_name_compo);
            e_name_panel.setBorder(BorderFactory.createTitledBorder("Entry Name"));
            this.add(e_name_panel, c);
        }
        if (e_ignored_panel == null) {
            e_ignored_panel = new JPanel();
            e_ignored_panel.setBackground(SaadaDBAdmin.beige_color);
            e_ignored_compo = new AppendMappingTextField(this, 2, true, null);
            e_ignored_compo.setColumns(40);
            e_ignored_panel.add(e_ignored_compo);
            e_ignored_panel.setBorder(BorderFactory.createTitledBorder("Ignored Keywords"));
            this.add(e_ignored_panel, c);
        }
        if (e_extatt_panel == null) {
            e_extatt_panel = new JPanel();
            e_extatt_panel.setBackground(SaadaDBAdmin.beige_color);
            e_extatt_panel.setBorder(BorderFactory.createTitledBorder("Extended Collection Attributes for Entries"));
            e_extatt_panel.setLayout(new GridBagLayout());
            this.addEAttExtendPanel();
            this.add(e_extatt_panel, c);
        }
        this.addCoordSysPanel();
        this.addPositionPanel();
        this.addErrorPositionPanel();
        coo_field.setForEntry();
        coosys_field.setForEntry();
        errcoo_field.setForEntry();
    }

    /**
	 * 
	 */
    public void addEAttExtendPanel() {
        e_ext_att = Database.getCachemeta().getAtt_extend_entry_names();
        GridBagConstraints cae = new GridBagConstraints();
        cae.anchor = GridBagConstraints.EAST;
        int numLabels = e_ext_att.length;
        e_extatt_fields = new ReplaceMappingTextField[numLabels];
        for (int i = 0; i < numLabels; i++) {
            cae.gridwidth = GridBagConstraints.RELATIVE;
            cae.fill = GridBagConstraints.NONE;
            cae.weightx = 0.0;
            e_extatt_panel.add(SaadaDBAdmin.getPlainLabel(e_ext_att[i]), cae);
            e_extatt_fields[i] = new ReplaceMappingTextField(this, 2, true, null);
            e_extatt_fields[i].setColumns(10);
            cae.gridwidth = GridBagConstraints.REMAINDER;
            cae.fill = GridBagConstraints.HORIZONTAL;
            cae.weightx = 1.0;
            e_extatt_panel.add(e_extatt_fields[i], cae);
        }
    }

    /**
	 * @return
	 */
    public ArgsParser getArgsParser() {
        ArrayList<String> params = new ArrayList<String>();
        if (extension_panel != null) {
            if (extension_field.getText().length() > 0) {
                params.add("-extension=" + extension_field.getText());
            }
        }
        if (class_panel != null) {
            if (class_field.getText().length() > 0) {
                if (classifier_btn.isSelected()) {
                    params.add("-classifier=" + class_field.getText());
                } else if (fusion_btn.isSelected()) {
                    params.add("-classfusion=" + class_field.getText());
                }
            } else if (classifier_btn.isSelected() || fusion_btn.isSelected()) {
                JOptionPane.showMessageDialog(this.frame, "A class mapping mode is selected but there is no class name given: ignored", "Mapping warning", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (ignored_panel != null) {
            if (ignored_compo.getText().length() > 0) {
                params.add("-ignore=" + ignored_compo.getText());
            }
        }
        if (e_ignored_panel != null) {
            if (e_ignored_compo.getText().length() > 0) {
                params.add("-eignore=" + e_ignored_compo.getText());
            }
        }
        switch(this.category) {
            case Category.MISC:
                params.add("-category=misc");
                break;
            case Category.IMAGE:
                params.add("-category=image");
                break;
            case Category.SPECTRUM:
                params.add("-category=spectrum");
                break;
            case Category.TABLE:
                params.add("-category=table");
                break;
            case Category.FLATFILE:
                params.add("-category=flatfile");
                break;
            default:
                break;
        }
        if (name_compo.getText().length() > 0) {
            params.add("-name=" + name_compo.getText());
        }
        for (int i = 0; i < ext_att.length; i++) {
            if (extatt_fields[i].getText().length() > 0) {
                params.add("-ukw");
                params.add(ext_att[i] + "=" + extatt_fields[i].getText());
            }
        }
        if (coordsys_panel != null && !coosys_no_btn.isSelected()) {
            if (coosys_first_btn.isSelected()) {
                params.add("-sysmapping=first");
            } else if (coosys_last_btn.isSelected()) {
                params.add("-sysmapping=last");
            } else if (coosys_only_btn.isSelected()) {
                params.add("-sysmapping=only");
            }
            if (coosys_field.getText().length() > 0) {
                params.add("-system=" + coosys_field.getText());
            }
        }
        if (coord_panel != null && !coo_no_btn.isSelected()) {
            if (coo_first_btn.isSelected()) {
                params.add("-posmapping=first");
            } else if (coo_last_btn.isSelected()) {
                params.add("-posmapping=last");
            } else if (coo_only_btn.isSelected()) {
                params.add("-posmapping=only");
            }
            if (coo_field.getText().length() > 0) {
                params.add("-position=" + coo_field.getText());
            }
        }
        if (coorderror_panel != null && !errcoo_no_btn.isSelected()) {
            if (errcoo_first_btn.isSelected()) {
                params.add("-poserrormapping=first");
            } else if (errcoo_last_btn.isSelected()) {
                params.add("-poserrormapping=last");
            } else if (errcoo_only_btn.isSelected()) {
                params.add("-poserrormapping=only");
            }
            if (errcoo_field.getText().length() > 0) {
                params.add("-poserror=" + errcoo_field.getText());
            }
            params.add("-poserrorunit=" + errcoo_unit.getSelectedItem().toString());
        }
        if (spccoord_panel != null && !spec_no_btn.isSelected()) {
            if (spec_first_btn.isSelected()) {
                params.add("-spcmapping=first");
            } else if (spec_last_btn.isSelected()) {
                params.add("-spcmapping=last");
            } else if (spec_only_btn.isSelected()) {
                params.add("-spcmapping=only");
            }
            if (spec_field.getText().length() > 0) {
                params.add("-spccolumn=" + spec_field.getText());
            }
            String param = specunit_combo.getSelectedItem().toString().trim();
            if (param.length() > 0) {
                params.add("-spcunit=" + param);
            }
        }
        if (e_name_panel != null) {
            if (e_name_compo.getText().length() > 0) {
                params.add("-ename=" + e_name_compo.getText());
            }
        }
        if (e_extatt_panel != null) {
            e_ext_att = Database.getCachemeta().getAtt_extend_entry_names();
            for (int i = 0; i < e_ext_att.length; i++) {
                if (e_extatt_fields[i].getText().trim().length() > 0) {
                    params.add("-eukw");
                    params.add(e_ext_att[i] + "=" + e_extatt_fields[i].getText());
                }
            }
        }
        ArgsParser retour;
        try {
            retour = new ArgsParser((String[]) (params.toArray(new String[0])));
            retour.setName(conf_name.getText());
            return retour;
        } catch (FatalException e) {
            Messenger.trapFatalException(e);
        }
        return null;
    }

    public void buildMiscPanel() {
        this.addNamePanel();
        this.addIgnoredPanel();
        this.updateUI();
    }

    public void buildFlatfilePanel() {
        this.addNamePanel();
        this.updateUI();
    }

    public void buildImagePanel() {
        this.addExtensionPanel();
        this.addNamePanel();
        this.addIgnoredPanel();
        this.addCoordSysPanel();
        this.addPositionPanel();
        this.addErrorPositionPanel();
        this.updateUI();
    }

    public void buildSpectraPanel() {
        this.addExtensionPanel();
        this.addNamePanel();
        this.addIgnoredPanel();
        this.addCoordSysPanel();
        this.addPositionPanel();
        this.addErrorPositionPanel();
        this.addSpectCoordPanel();
        this.updateUI();
    }

    public void buildTablePanel() {
        this.addExtensionPanel();
        this.addNamePanel();
        this.addIgnoredPanel();
        this.addSourcePanel();
        this.updateUI();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == coosys_combo) {
            this.coosys_field.setText("'" + coosys_combo.getSelectedItem() + "'");
            if (MappingKWPanel.this.coosys_no_btn.isSelected()) {
                MappingKWPanel.this.coosys_first_btn.setSelected(true);
            }
        }
    }

    /**
	 * @param parser
	 */
    public void loadConfig(ArgsParser parser) {
        try {
            if (parser != null) {
                this.last_saved = parser.toString();
                setConf_name(parser.getName());
                String param = null;
                if (class_panel != null) {
                    if (parser.getMappingType() == DefineType.TYPE_MAPPING_USER) {
                        this.fusion_btn.setSelected(true);
                        this.class_field.setText(parser.getClassName());
                        this.class_field.setEnabled(true);
                        this.class_field.setEditable(true);
                    } else if (parser.getMappingType() == DefineType.TYPE_MAPPING_CLASSIFIER) {
                        this.classifier_btn.setSelected(true);
                        this.class_field.setText(parser.getClassName());
                        this.class_field.setEnabled(true);
                        this.class_field.setEditable(true);
                    } else {
                        this.noclass_btn.setSelected(true);
                        this.class_field.setText("");
                        this.class_field.setEnabled(false);
                        this.class_field.setEditable(false);
                    }
                }
                if (extension_panel != null && (param = parser.getExtension()) != null) {
                    this.extension_field.setText(param);
                }
                if (ignored_panel != null) {
                    ignored_compo.setText(getMergedComponent(parser.getIgnoredAttributes()));
                }
                if (e_ignored_panel != null) {
                    e_ignored_compo.setText(getMergedComponent(parser.getEntryIgnoredAttributes()));
                }
                if (name_panel != null) {
                    name_compo.setText(getMergedComponent(parser.getNameComponents()));
                }
                if (e_name_panel != null) {
                    e_name_compo.setText(getMergedComponent(parser.getEntryNameComponents()));
                }
                if (extatt_panel != null) {
                    String[] ext_att = null;
                    switch(this.category) {
                        case Category.MISC:
                            ext_att = Database.getCachemeta().getAtt_extend_misc_names();
                            break;
                        case Category.IMAGE:
                            ext_att = Database.getCachemeta().getAtt_extend_image_names();
                            break;
                        case Category.SPECTRUM:
                            ext_att = Database.getCachemeta().getAtt_extend_spectrum_names();
                            break;
                        case Category.TABLE:
                            ext_att = Database.getCachemeta().getAtt_extend_table_names();
                            break;
                        case Category.FLATFILE:
                            ext_att = Database.getCachemeta().getAtt_extend_flatfile_names();
                            break;
                    }
                    if (ext_att != null) {
                        param = null;
                        for (int i = 0; i < ext_att.length; i++) {
                            param = parser.getUserKeyword(ext_att[i]);
                            if (param != null) extatt_fields[i].setText(param); else extatt_fields[i].setText("");
                        }
                    }
                }
                if (e_extatt_panel != null) {
                    String[] ext_att = null;
                    ext_att = Database.getCachemeta().getAtt_extend_entry_names();
                    if (ext_att != null) {
                        param = null;
                        for (int i = 0; i < ext_att.length; i++) {
                            param = parser.getEntryUserKeyword(ext_att[i]);
                            if (param != null) e_extatt_fields[i].setText(param); else e_extatt_fields[i].setText("");
                        }
                    }
                }
                if (coordsys_panel != null) {
                    coosys_field.setText("");
                    param = parser.getSysMappingPriority();
                    if (param == null) {
                        coosys_field.setEditable(false);
                        coosys_field.setEnabled(false);
                        coosys_no_btn.setSelected(true);
                    } else if (param.equalsIgnoreCase("first")) {
                        coosys_first_btn.setSelected(true);
                        coosys_field.setEditable(true);
                        coosys_field.setEnabled(true);
                    } else if (param.equalsIgnoreCase("last")) {
                        coosys_last_btn.setSelected(true);
                        coosys_field.setEditable(true);
                        coosys_field.setEnabled(true);
                    } else if (param.equalsIgnoreCase("only")) {
                        coosys_only_btn.setSelected(true);
                        coosys_field.setEditable(true);
                        coosys_field.setEnabled(true);
                    } else {
                        coosys_no_btn.setSelected(true);
                        coosys_field.setEditable(false);
                        coosys_field.setEnabled(false);
                    }
                    if (param != null) {
                        coosys_field.setText(getMergedComponent(parser.getCoordinateSystem()));
                    }
                    for (int i = 0; i < coosys_combo.getItemCount(); i++) {
                        if (coosys_combo.getItemAt(i).toString().equalsIgnoreCase(param)) {
                            coosys_combo.setSelectedIndex(i);
                        }
                    }
                }
                if (coord_panel != null) {
                    coo_field.setText("");
                    param = parser.getPositionMappingPriority();
                    if (param == null) {
                        coo_no_btn.setSelected(true);
                        coo_field.setEditable(false);
                        coo_field.setEnabled(false);
                    } else if (param.equalsIgnoreCase("first")) {
                        coo_first_btn.setSelected(true);
                        coo_field.setEditable(true);
                        coo_field.setEnabled(true);
                    } else if (param.equalsIgnoreCase("last")) {
                        coo_last_btn.setSelected(true);
                        coo_field.setEnabled(true);
                        coo_field.setEditable(true);
                    } else if (param.equalsIgnoreCase("only")) {
                        coo_only_btn.setSelected(true);
                        coo_field.setEditable(true);
                        coo_field.setEnabled(true);
                    } else {
                        coo_no_btn.setSelected(true);
                        coo_field.setEditable(false);
                        coo_field.setEnabled(false);
                    }
                    if (param != null) {
                        coo_field.setText(getMergedComponent(parser.getPositionMapping()));
                    }
                }
                if (coorderror_panel != null) {
                    errcoo_field.setText("");
                    param = parser.getPoserrorMappingPriority();
                    if (param == null) {
                        errcoo_no_btn.setSelected(true);
                        errcoo_field.setEditable(false);
                        errcoo_field.setEnabled(false);
                    } else if (param.equalsIgnoreCase("first")) {
                        errcoo_first_btn.setSelected(true);
                        errcoo_field.setEditable(true);
                        errcoo_field.setEnabled(true);
                    } else if (param.equalsIgnoreCase("last")) {
                        errcoo_last_btn.setSelected(true);
                        errcoo_field.setEditable(true);
                        errcoo_field.setEnabled(true);
                    } else if (param.equalsIgnoreCase("only")) {
                        errcoo_only_btn.setSelected(true);
                        errcoo_field.setEditable(true);
                        errcoo_field.setEnabled(true);
                    } else {
                        errcoo_no_btn.setSelected(true);
                        errcoo_field.setEditable(false);
                        errcoo_field.setEnabled(false);
                    }
                    if (param != null) {
                        errcoo_field.setText(getMergedComponent(parser.getPoserrorMapping()));
                    }
                    param = parser.getPoserrorUnit();
                    for (int i = 0; i < errcoo_unit.getItemCount(); i++) {
                        if (errcoo_unit.getItemAt(i).toString().equalsIgnoreCase(param)) {
                            errcoo_unit.setSelectedIndex(i);
                        }
                    }
                }
                if (spccoord_panel != null) {
                    spec_field.setText("");
                    param = parser.getSpectralMappingPriority();
                    if (param == null) {
                        spec_no_btn.setSelected(true);
                        spec_field.setEditable(false);
                        spec_field.setEnabled(false);
                    } else if (param.equalsIgnoreCase("first")) {
                        spec_first_btn.setSelected(true);
                        spec_field.setEditable(true);
                        spec_field.setEnabled(true);
                        specunit_combo.setEnabled(true);
                    } else if (param.equalsIgnoreCase("last")) {
                        spec_last_btn.setSelected(true);
                        spec_field.setEditable(true);
                        spec_field.setEnabled(true);
                        specunit_combo.setEnabled(true);
                    } else if (param.equalsIgnoreCase("only")) {
                        spec_only_btn.setSelected(true);
                        spec_field.setEditable(true);
                        spec_field.setEnabled(true);
                        specunit_combo.setEnabled(true);
                    } else {
                        spec_no_btn.setSelected(true);
                        spec_field.setEditable(false);
                        spec_field.setEnabled(false);
                    }
                    param = parser.getSpectralColumn();
                    if (param != null) {
                        spec_field.setText(param);
                    }
                    param = parser.getSpectralUnit();
                    for (int i = 0; i < specunit_combo.getItemCount(); i++) {
                        if (specunit_combo.getItemAt(i).toString().equalsIgnoreCase(param)) {
                            specunit_combo.setSelectedIndex(i);
                        }
                    }
                }
            } else {
                setConf_name(null);
            }
        } catch (Exception e) {
            Messenger.printStackTrace(e);
            JOptionPane.showMessageDialog(frame, e.toString(), "Error while loading configuration", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
	 * @param name
	 */
    protected void setConf_name(String name) {
        if (name == null) {
            conf_name.setText("null");
        } else {
            conf_name.setText(name);
        }
    }

    /**
	 * Clear all widgets
	 */
    public void reset(boolean keep_ext) {
        this.last_saved = "";
        this.conf_name.setText("null");
        if (name_compo != null) name_compo.setText("");
        if (e_name_compo != null) e_name_compo.setText("");
        if (ignored_compo != null) ignored_compo.setText("");
        if (e_ignored_compo != null) e_name_compo.setText("");
        if (extatt_fields != null) {
            for (int i = 0; i < extatt_fields.length; i++) extatt_fields[i].setText("");
        }
        if (e_extatt_fields != null) {
            for (int i = 0; i < extatt_fields.length; i++) e_extatt_fields[i].setText("");
        }
        if (coosys_no_btn != null) coosys_no_btn.setSelected(true);
        if (coosys_field != null) coosys_field.setText("");
        if (coo_no_btn != null) coo_no_btn.setSelected(true);
        if (coo_field != null) coo_field.setText("");
        if (errcoo_no_btn != null) errcoo_no_btn.setSelected(true);
        if (errcoo_field != null) errcoo_field.setText("");
        if (spec_no_btn != null) spec_no_btn.setSelected(true);
        if (spec_field != null) spec_field.setText("");
        if (!keep_ext && extension_field != null) extension_field.setText("");
        if (noclass_btn != null) noclass_btn.setSelected(true);
        if (class_field != null) class_field.setText("");
    }

    /**
	 * @param param_compo
	 * @return
	 */
    private static final String getMergedComponent(String[] param_compo) {
        String param = "";
        if (param_compo != null) {
            for (int i = 0; i < param_compo.length; i++) {
                if (i >= 1) param += ",";
                param += param_compo[i];
            }
        }
        return param;
    }

    protected void checkExtensionChange() {
        if (extension_field != null && extension_field.checkChange(extension_field.getText())) {
            extension_field.setPrevious_value(extension_field.getText());
        }
    }

    /**
	 * 
	 */
    protected void saveAs() {
        try {
            ArgsParser ap = this.getArgsParser();
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            String name = ap.getClassName();
            if (name == null || name.length() == 0 || name.equalsIgnoreCase("null")) {
                name = "NewConfig";
            }
            while (1 == 1) {
                DialogConfName dial = new DialogConfName(this.frame.frame, "Configuration Name", name);
                dial.pack();
                dial.setLocationRelativeTo(this.frame);
                dial.setVisible(true);
                String prefix;
                prefix = Category.explain(this.category);
                name = name = dial.getTyped_name();
                if (name == null) {
                    return;
                } else if (name.equalsIgnoreCase("null")) {
                    SaadaDBAdmin.showFatalError(this.frame, "Wrong config name.");
                } else {
                    String filename = SaadaDB.getRoot_dir() + Database.getSepar() + "config" + Database.getSepar() + prefix + "." + name + ".config";
                    if ((new File(filename)).exists() && SaadaDBAdmin.showConfirmDialog(this.frame, "Loader configuration <" + name + "> for \"" + prefix + "\" already exists.\nOverwrite it?") == false) {
                        ap.setName(name);
                    } else {
                        ap.setName(name);
                        this.setConf_name(name);
                        fos = new FileOutputStream(SaadaDB.getRoot_dir() + Database.getSepar() + "config" + Database.getSepar() + prefix + "." + name + ".config");
                        out = new ObjectOutputStream(fos);
                        out.writeObject(ap);
                        out.close();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            SaadaDBAdmin.showFatalError(this, ex.toString());
            return;
        }
    }

    /**
	 * 
	 */
    public void save() {
        String name = this.conf_name.getText();
        if (name.equals("") || name.equalsIgnoreCase("null")) {
            this.saveAs();
        } else if (!this.hasChanged()) {
            return;
        } else {
            ArgsParser ap = this.getArgsParser();
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            try {
                String prefix = Category.explain(this.category);
                fos = new FileOutputStream(SaadaDB.getRoot_dir() + Database.getSepar() + "config" + Database.getSepar() + prefix + "." + name + ".config");
                out = new ObjectOutputStream(fos);
                out.writeObject(ap);
                out.close();
                this.last_saved = ap.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                SaadaDBAdmin.showFatalError(this, ex.toString());
                return;
            }
        }
    }

    /**
	 * @return
	 */
    public boolean hasChanged() {
        return !last_saved.equals(this.getArgsParser().toString());
    }

    /**
	 * Does a basic param checking. Basically, parameter subject to a mapping must not be empty
	 * @return
	 */
    public boolean checkParams() {
        String msg = "";
        if (class_panel != null) {
            if ((classifier_btn.isSelected() || fusion_btn.isSelected())) {
                if (this.class_field.getText().length() == 0) {
                    msg += "<LI>Empty class name not allowed in this classification mode</LI>";
                } else if (!this.class_field.getText().matches(RegExp.CLASSNAME)) {
                    msg += "<LI>Bad class name</LI>";
                }
            }
        }
        if (coordsys_panel != null) {
            if ((coosys_first_btn.isSelected() || coosys_last_btn.isSelected() || coosys_only_btn.isSelected())) {
                if (this.coosys_field.getText().length() == 0) {
                    msg += "<LI>Empty coord. system  not allowed in this mapping mode</LI>";
                }
            }
        }
        if (coord_panel != null) {
            if ((coo_first_btn.isSelected() || coo_last_btn.isSelected() || coo_only_btn.isSelected())) {
                if (this.coo_field.getText().length() == 0) {
                    msg += "<LI>Empty coordinates not allowed in this mapping mode</LI>";
                }
            }
        }
        if (coorderror_panel != null) {
            if ((errcoo_first_btn.isSelected() || errcoo_last_btn.isSelected() || errcoo_only_btn.isSelected())) {
                if (this.errcoo_field.getText().length() == 0) {
                    msg += "<LI>Empty coordinate error not allowed in this mapping mode</LI>";
                }
            }
        }
        if (spccoord_panel != null) {
            if ((spec_first_btn.isSelected() || spec_last_btn.isSelected() || spec_only_btn.isSelected())) {
                if (this.spec_field.getText().length() == 0) {
                    msg += "<LI>Empty spectral coordinates not allowed in this mapping mode</LI>";
                }
            }
        }
        if (msg.length() > 0) {
            SaadaDBAdmin.showInputError(this.frame, "<HTML><UL>" + msg);
            return false;
        } else {
            return true;
        }
    }

    class PrioritySelector {

        private JRadioButton[] buttons;

        private JRadioButton nomapping;

        private JComponent[] components;

        private JPanel panel;

        private GridBagConstraints ccs;

        private ButtonGroup bg;

        /**
		 * @param buttons
		 * @param nomapping
		 * @param components
		 */
        public PrioritySelector(JRadioButton[] buttons, JRadioButton nomapping, ButtonGroup bg, JComponent[] components, JPanel panel, GridBagConstraints ccs) {
            this.buttons = buttons;
            this.nomapping = nomapping;
            this.components = components;
            this.bg = bg;
            this.panel = panel;
            this.ccs = ccs;
            for (JRadioButton b : buttons) {
                if (b.getText().equalsIgnoreCase("only")) {
                    b.setToolTipText("Only the rule given below will be applied");
                } else if (b.getText().equalsIgnoreCase("first")) {
                    b.setToolTipText("The rule given below will be applied first and then, in case of failure,  an automatic detection.");
                } else if (b.getText().equalsIgnoreCase("last")) {
                    b.setToolTipText("An automatic detection will be applied first and then, in case of failure, the rule given below.");
                } else if (b.getText().equalsIgnoreCase("no mapping")) {
                    b.setToolTipText("Only the automatic detection will be applied ");
                }
            }
            for (JRadioButton jrb : buttons) {
                jrb.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == PrioritySelector.this.nomapping) {
                            PrioritySelector.this.setEnable(false);
                        } else PrioritySelector.this.setEnable(true);
                    }
                });
                if (jrb == nomapping) {
                    jrb.setSelected(true);
                } else {
                    jrb.setSelected(false);
                }
                panel.add(jrb, ccs);
                bg.add(jrb);
                ccs.gridx++;
            }
            this.setEnable(false);
        }

        /**
		 * @param b
		 */
        protected void setEnable(boolean b) {
            for (JComponent jc : components) {
                if (jc.getClass().getName().matches(".*Text.*")) {
                    ((JTextComponent) jc).setEditable(b);
                }
                jc.setEnabled(b);
            }
        }
    }
}
