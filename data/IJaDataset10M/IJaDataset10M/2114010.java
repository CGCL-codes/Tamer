package org.openconcerto.modules.badge;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.openconcerto.erp.action.CreateFrameAbstractAction;
import org.openconcerto.erp.config.Gestion;
import org.openconcerto.erp.config.MainFrame;
import org.openconcerto.erp.core.common.element.ComptaSQLConfElement;
import org.openconcerto.erp.core.common.ui.ListeViewPanel;
import org.openconcerto.erp.modules.AbstractModule;
import org.openconcerto.erp.modules.ComponentsContext;
import org.openconcerto.erp.modules.DBContext;
import org.openconcerto.erp.modules.ModuleFactory;
import org.openconcerto.erp.modules.ModuleManager;
import org.openconcerto.erp.modules.ModulePackager;
import org.openconcerto.erp.modules.RuntimeModuleFactory;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.element.SQLComponent;
import org.openconcerto.sql.element.SQLElement;
import org.openconcerto.sql.element.SQLElementDirectory;
import org.openconcerto.sql.element.UISQLComponent;
import org.openconcerto.sql.model.SQLName;
import org.openconcerto.sql.model.SQLRowAccessor;
import org.openconcerto.sql.model.SQLSyntax;
import org.openconcerto.sql.model.Where;
import org.openconcerto.sql.utils.SQLCreateTable;
import org.openconcerto.sql.view.FileDropHandler;
import org.openconcerto.sql.view.IListFrame;
import org.openconcerto.sql.view.IListPanel;
import org.openconcerto.sql.view.ListeAddPanel;
import org.openconcerto.sql.view.list.IListe;
import org.openconcerto.sql.view.list.RowAction;
import org.openconcerto.sql.view.list.SQLTableModelSourceOnline;
import org.openconcerto.ui.PanelFrame;
import org.openconcerto.utils.CollectionMap;
import org.openconcerto.utils.FileUtils;
import org.openconcerto.utils.cc.IClosure;

public final class Module extends AbstractModule {

    public Module(ModuleFactory f) throws IOException {
        super(f);
    }

    @Override
    protected void install(DBContext ctxt) {
        super.install(ctxt);
        if (!ctxt.getTablesPreviouslyCreated().contains("ENTREE")) {
            final SQLCreateTable createTableEntree = ctxt.getCreateTable("ENTREE");
            createTableEntree.addDateAndTimeColumn("DATE");
            createTableEntree.addVarCharColumn("NUMERO_CARTE", 256);
            createTableEntree.addVarCharColumn("ADHERENT", 512);
            createTableEntree.addVarCharColumn("MOTIF", 2048);
            createTableEntree.addColumn("ACCEPTE", "boolean");
        }
        if (!ctxt.getTablesPreviouslyCreated().contains("PLAGE_HORAIRE")) {
            final SQLCreateTable createTablePlage = ctxt.getCreateTable("PLAGE_HORAIRE");
            createTablePlage.addVarCharColumn("NOM", 256);
            createTablePlage.addColumn("DEBUT_1_LUNDI", "time");
            createTablePlage.addColumn("DEBUT_1_MARDI", "time");
            createTablePlage.addColumn("DEBUT_1_MERCREDI", "time");
            createTablePlage.addColumn("DEBUT_1_JEUDI", "time");
            createTablePlage.addColumn("DEBUT_1_VENDREDI", "time");
            createTablePlage.addColumn("DEBUT_1_SAMEDI", "time");
            createTablePlage.addColumn("DEBUT_1_DIMANCHE", "time");
            createTablePlage.addColumn("DEBUT_2_LUNDI", "time");
            createTablePlage.addColumn("DEBUT_2_MARDI", "time");
            createTablePlage.addColumn("DEBUT_2_MERCREDI", "time");
            createTablePlage.addColumn("DEBUT_2_JEUDI", "time");
            createTablePlage.addColumn("DEBUT_2_VENDREDI", "time");
            createTablePlage.addColumn("DEBUT_2_SAMEDI", "time");
            createTablePlage.addColumn("DEBUT_2_DIMANCHE", "time");
            createTablePlage.addColumn("DEBUT_3_LUNDI", "time");
            createTablePlage.addColumn("DEBUT_3_MARDI", "time");
            createTablePlage.addColumn("DEBUT_3_MERCREDI", "time");
            createTablePlage.addColumn("DEBUT_3_JEUDI", "time");
            createTablePlage.addColumn("DEBUT_3_VENDREDI", "time");
            createTablePlage.addColumn("DEBUT_3_SAMEDI", "time");
            createTablePlage.addColumn("DEBUT_3_DIMANCHE", "time");
            createTablePlage.addColumn("FIN_1_LUNDI", "time");
            createTablePlage.addColumn("FIN_1_MARDI", "time");
            createTablePlage.addColumn("FIN_1_MERCREDI", "time");
            createTablePlage.addColumn("FIN_1_JEUDI", "time");
            createTablePlage.addColumn("FIN_1_VENDREDI", "time");
            createTablePlage.addColumn("FIN_1_SAMEDI", "time");
            createTablePlage.addColumn("FIN_1_DIMANCHE", "time");
            createTablePlage.addColumn("FIN_2_LUNDI", "time");
            createTablePlage.addColumn("FIN_2_MARDI", "time");
            createTablePlage.addColumn("FIN_2_MERCREDI", "time");
            createTablePlage.addColumn("FIN_2_JEUDI", "time");
            createTablePlage.addColumn("FIN_2_VENDREDI", "time");
            createTablePlage.addColumn("FIN_2_SAMEDI", "time");
            createTablePlage.addColumn("FIN_2_DIMANCHE", "time");
            createTablePlage.addColumn("FIN_3_LUNDI", "time");
            createTablePlage.addColumn("FIN_3_MARDI", "time");
            createTablePlage.addColumn("FIN_3_MERCREDI", "time");
            createTablePlage.addColumn("FIN_3_JEUDI", "time");
            createTablePlage.addColumn("FIN_3_VENDREDI", "time");
            createTablePlage.addColumn("FIN_3_SAMEDI", "time");
            createTablePlage.addColumn("FIN_3_DIMANCHE", "time");
        }
        if (!ctxt.getTablesPreviouslyCreated().contains("ADHERENT")) {
            final SQLCreateTable createTable = ctxt.getCreateTable("ADHERENT");
            createTable.addVarCharColumn("NUMERO_CARTE", 256);
            createTable.addVarCharColumn("NOM", 256);
            createTable.addVarCharColumn("TEL", 256);
            createTable.addColumn("ACTIF", "boolean default false");
            createTable.addColumn("ADMIN", "boolean default false");
            createTable.addVarCharColumn("MAIL", 256);
            createTable.addVarCharColumn("PRENOM", 256);
            createTable.addVarCharColumn("INFOS", 2048);
            createTable.addColumn("DATE_VALIDITE_INSCRIPTION", "date");
            createTable.addColumn("DATE_NAISSANCE", "date");
            createTable.addForeignColumn("ID_ADRESSE", Configuration.getInstance().getRoot().findTable("ADRESSE"));
            createTable.addForeignColumn("ID_PLAGE_HORAIRE", new SQLName("PLAGE_HORAIRE"), SQLSyntax.ID_NAME, null);
        }
    }

    @Override
    protected void setupElements(SQLElementDirectory dir) {
        super.setupElements(dir);
        final ComptaSQLConfElement entreeElement = new ComptaSQLConfElement("ENTREE", "une entrée", "entrées") {

            @Override
            protected List<String> getListFields() {
                final List<String> l = new ArrayList<String>();
                l.add("DATE");
                l.add("NUMERO_CARTE");
                l.add("ADHERENT");
                l.add("MOTIF");
                l.add("ACCEPTE");
                return l;
            }

            @Override
            public Set<String> getReadOnlyFields() {
                Set<String> s = new HashSet<String>(super.getReadOnlyFields());
                s.add("NUMERO_CARTE");
                return s;
            }

            @Override
            protected List<String> getComboFields() {
                final List<String> l = new ArrayList<String>();
                l.add("NUMERO_CARTE");
                l.add("DATE");
                return l;
            }

            @Override
            public CollectionMap<String, String> getShowAs() {
                return CollectionMap.singleton(null, getComboFields());
            }

            @Override
            public SQLComponent createComponent() {
                return new UISQLComponent(this) {

                    @Override
                    protected void addViews() {
                        this.addView("NUMERO_CARTE");
                        this.addView("DATE");
                    }
                };
            }
        };
        dir.addSQLElement(entreeElement);
        dir.addSQLElement(new PlageHoraireSQLElement());
        dir.addSQLElement(new AdherentSQLElement());
    }

    private IListPanel getPanelEntree(boolean filtered) {
        final SQLElement element = Configuration.getInstance().getDirectory().getElement("ENTREE");
        final SQLTableModelSourceOnline tableModelEntree = element.getTableSource(true);
        if (filtered) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -15);
            Where wAttente = new Where(element.getTable().getField("DATE"), ">=", cal.getTime());
            tableModelEntree.getReq().setWhere(wAttente);
        }
        tableModelEntree.getColumn(element.getTable().getField("DATE")).setRenderer(new DefaultTableCellRenderer() {

            DateFormat format = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final String format2 = format.format((Date) value);
                return super.getTableCellRendererComponent(table, format2, isSelected, hasFocus, row, column);
            }
        });
        final IListPanel panel = new ListeViewPanel(element, new IListe(tableModelEntree));
        final Action createAction = RowAction.createAction("Assigner à", null, new IClosure<List<SQLRowAccessor>>() {

            @Override
            public void executeChecked(List<SQLRowAccessor> input) {
                SQLRowAccessor row = input.get(0);
                PanelFrame frame = new PanelFrame(new AssignationPanel(row.getString("NUMERO_CARTE")), "Assignation d'une carte");
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        panel.getListe().addRowAction(createAction);
        return panel;
    }

    @Override
    protected void setupComponents(ComponentsContext ctxt) {
        ctxt.addFileDropHandler("ADHERENT", new FileDropHandler() {

            @Override
            public boolean handle(File f, Component source) {
                AdherentImporter importer;
                try {
                    importer = new AdherentImporter(f);
                    importer.importAdherent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean canHandle(File f) {
                String n = f.getName().toLowerCase();
                return n.endsWith(".xls") || n.endsWith(".ods");
            }
        });
        ctxt.addMenuItem(new CreateFrameAbstractAction("Liste des adhérents") {

            @Override
            public JFrame createFrame() {
                IListFrame frameAdh = new IListFrame(new ListeAddPanel(Configuration.getInstance().getDirectory().getElement("ADHERENT")));
                return frameAdh;
            }
        }, MainFrame.LIST_MENU);
        ctxt.addMenuItem(new CreateFrameAbstractAction("Liste des entrées") {

            @Override
            public JFrame createFrame() {
                return new IListFrame(getPanelEntree(false));
            }
        }, MainFrame.LIST_MENU);
    }

    @Override
    protected void start() {
        MainFrame.getInstance().getTabbedPane().addTab("Liste des entrées", getPanelEntree(true));
    }

    @Override
    protected void stop() {
    }

    public static void main(String[] args) throws IOException {
        final File propsFile = new File("gestionModule.properties");
        final ModuleFactory factory = new RuntimeModuleFactory(propsFile);
        final File distDir = new File("dist");
        FileUtils.mkdir_p(distDir);
        new ModulePackager(propsFile, new File("bin/")).writeToDir(distDir);
        new ModulePackager(propsFile, new File("bin/")).writeToDir(new File("../OpenConcerto/Modules"));
        ModuleManager.getInstance().addFactoryAndStart(factory, false);
        Gestion.main(args);
    }
}
