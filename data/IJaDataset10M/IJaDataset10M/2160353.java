package org.openconcerto.modules.card;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openconcerto.erp.config.MainFrame;
import org.openconcerto.erp.core.common.element.ComptaSQLConfElement;
import org.openconcerto.erp.modules.AbstractModule;
import org.openconcerto.erp.modules.AlterTableRestricted;
import org.openconcerto.erp.modules.ComponentsContext;
import org.openconcerto.erp.modules.DBContext;
import org.openconcerto.erp.modules.ModuleFactory;
import org.openconcerto.erp.modules.ModulePreferencePanel;
import org.openconcerto.erp.modules.ModulePreferencePanelDesc;
import org.openconcerto.sql.element.SQLComponent;
import org.openconcerto.sql.element.SQLElement.ReferenceAction;
import org.openconcerto.sql.element.SQLElementDirectory;
import org.openconcerto.sql.element.UISQLComponent;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.utils.SQLCreateTable;
import org.openconcerto.utils.CollectionMap;
import org.openconcerto.utils.PrefType;

public final class ModuleCard extends AbstractModule {

    private static final String TABLE_NAME = "FIDELITY_CARD";

    public ModuleCard(ModuleFactory f) throws IOException {
        super(f);
    }

    @Override
    protected void install(DBContext ctxt) {
        super.install(ctxt);
        if (!ctxt.getTablesPreviouslyCreated().contains(TABLE_NAME)) {
            final SQLCreateTable createTable = ctxt.getCreateTable(TABLE_NAME);
            createTable.addVarCharColumn("SERIAL", 64);
            createTable.addColumn("POINTS", "int NOT NULL DEFAULT 0");
            createTable.addDateAndTimeColumn("EXPIRATION_DATE");
            final AlterTableRestricted alterTable = ctxt.getAlterTable("CLIENT");
            alterTable.addForeignColumn("ID_FIDELITY_CARD", createTable);
        }
    }

    @Override
    protected void setupElements(SQLElementDirectory dir) {
        super.setupElements(dir);
        final ComptaSQLConfElement fidCardElement = new ComptaSQLConfElement(TABLE_NAME, "une carte de fidélité", "cartes de fidélité") {

            @Override
            protected List<String> getListFields() {
                final List<String> l = new ArrayList<String>();
                l.add("SERIAL");
                l.add("POINTS");
                l.add("EXPIRATION_DATE");
                return l;
            }

            @Override
            protected List<String> getComboFields() {
                final List<String> l = new ArrayList<String>();
                l.add("SERIAL");
                l.add("POINTS");
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
                        this.addView("SERIAL");
                        this.addView("POINTS");
                        this.addView("EXPIRATION_DATE");
                    }
                };
            }
        };
        dir.addSQLElement(fidCardElement);
        final SQLTable clientT = fidCardElement.getTable().findReferentTable("CLIENT");
        dir.getElement(clientT).setAction("ID_FIDELITY_CARD", ReferenceAction.SET_EMPTY);
    }

    @Override
    protected void setupComponents(ComponentsContext ctxt) {
        ctxt.putAdditionalField("CLIENT", "ID_FIDELITY_CARD");
        ctxt.addMenuItem(ctxt.createListAction(TABLE_NAME), MainFrame.LIST_MENU);
    }

    @Override
    protected void start() {
    }

    @Override
    public List<ModulePreferencePanelDesc> getPrefDescriptors() {
        return Arrays.<ModulePreferencePanelDesc>asList(new ModulePreferencePanelDesc("Préf") {

            @Override
            protected ModulePreferencePanel createPanel() {
                return new ModulePreferencePanel("Mon super titre") {

                    @Override
                    protected void addViews() {
                        this.addView(new SQLPrefView<Boolean>(PrefType.BOOLEAN_TYPE, "Un booléen", "boolPref").setDefaultValue(Boolean.TRUE));
                        this.addView(new SQLPrefView<String>(PrefType.STRING_TYPE, 12, "Du texte", "textPref").setDefaultValue("court"));
                        this.addView(new SQLPrefView<Double>(PrefType.DOUBLE_TYPE, "Un double", "doublePref"));
                    }
                };
            }
        }, new ModulePreferencePanelDesc("Préf2") {

            @Override
            protected ModulePreferencePanel createPanel() {
                return new ModulePreferencePanel("Mon super titre") {

                    @Override
                    protected void addViews() {
                        this.addView(new SQLPrefView<String>(PrefType.STRING_TYPE, 512, "Du texte", "textPref").setDefaultValue("long texte"));
                        this.addView(new SQLPrefView<Long>(PrefType.LONG_TYPE, "Un long", "longPref"));
                    }
                };
            }
        }.setLocal(false).setKeywords("toto", "vélo"));
    }

    @Override
    protected void stop() {
    }
}
