package com.jedox.etl.components.load;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;
import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Element;
import com.jedox.etl.components.config.load.CubeShrinkConfigurator;
import com.jedox.etl.components.config.load.CubeShrinkConfigurator.DimensionDescription;
import com.jedox.etl.core.component.InitializationException;
import com.jedox.etl.core.component.Locator;
import com.jedox.etl.core.component.RuntimeException;
import com.jedox.etl.core.connection.IRelationalConnection;
import com.jedox.etl.core.node.Column;
import com.jedox.etl.core.node.IColumn;
import com.jedox.etl.core.node.Row;
import com.jedox.etl.core.persistence.PersistorDefinition;
import com.jedox.etl.core.source.processor.IProcessor;
import com.jedox.etl.core.util.NamingUtil;
import com.jedox.etl.core.persistence.Datastore;
import com.jedox.etl.core.persistence.DatastoreManager;
import com.jedox.etl.core.persistence.IStore;
import com.jedox.etl.core.persistence.hibernate.HibernatePersistor;

public class CubeShrink extends CubeLoad {

    private static final Log log = LogFactory.getLog(CubeShrink.class);

    private long limit;

    private List<DimensionDescription> dimensions = new ArrayList<DimensionDescription>();

    public CubeShrink() {
        setConfigurator(new CubeShrinkConfigurator());
    }

    public CubeShrinkConfigurator getConfigurator() {
        return (CubeShrinkConfigurator) super.getConfigurator();
    }

    protected IProcessor getProcessor() throws RuntimeException {
        Locator loc = new Locator().add(getDatabaseName()).add(getCubeName());
        loc.setPersistentSchema(getDatabaseName());
        PersistorDefinition definition = new PersistorDefinition();
        definition.setConnection((IRelationalConnection) getDrillthroughDescription().connection);
        definition.setLocator(loc);
        Datastore store = DatastoreManager.getInstance().provide(definition);
        String[] ordernames = new String[getDimensions().size()];
        String[] orderops = new String[getDimensions().size()];
        for (int i = 0; i < getDimensions().size(); i++) {
            DimensionDescription d = getDimensions().get(i);
            ordernames[i] = d.name;
            orderops[i] = d.order;
        }
        return store.getOrderedProcessor(ordernames, orderops, 0);
    }

    public Modes getMode() {
        return Modes.DELETE;
    }

    private long getLimit() {
        return limit;
    }

    private void setLimit(long limit) {
        this.limit = limit;
    }

    private List<DimensionDescription> getDimensions() {
        return dimensions;
    }

    private void setDimensions(List<DimensionDescription> dimensions) {
        this.dimensions = dimensions;
    }

    public void execute() {
        if (isExecutable()) {
            try {
                Connection connection = getConnection().open();
                Database d = connection.getDatabaseByName(getDatabaseName());
                if (d == null) {
                    throw new InitializationException("Database " + getDatabaseName() + " does not exist.");
                }
                Cube c = d.getCubeByName(getCubeName());
                setCellsFilled(c.getFilledCellCount().longValue());
                if (getCellsFilled() > getLimit()) {
                    log.info("Starting shrinking " + getName() + " of Cube " + getCubeName());
                    IProcessor rows = getProcessor();
                    setCoordinateMap(buildCoordinateMap(c, c.getDimensions()));
                    Row idRow = new Row();
                    IColumn key = new Column("id");
                    idRow.addColumn(key);
                    Row dataRow = new Row();
                    dataRow.addColumns(rows.current());
                    dataRow.removeColumn(NamingUtil.internalKeyName());
                    Locator temploc = getLocator().clone();
                    String name = temploc.getName();
                    temploc.reduce().add(NamingUtil.internal("load_" + name));
                    PersistorDefinition definition = new PersistorDefinition(temploc, getInternalConnection());
                    definition.setInput(idRow);
                    Datastore idStore = DatastoreManager.getInstance().provide(definition);
                    idStore.setWriteable();
                    while ((rows.next() != null) && (isExecutable() && c.getFilledCellCount().longValue() > getLimit())) {
                        Row row = rows.current();
                        idRow.getColumn(0).setValue(row.getColumn(NamingUtil.internalKeyName()).getValue());
                        idStore.write();
                        Element[] coordinates = getCoordinateElements(dataRow, c);
                        boolean numeric = isNumericCell(coordinates);
                        String value = getValue(dataRow, getMode(), numeric);
                        c.setData(coordinates, value);
                    }
                    rows.close();
                    idStore.commit();
                    log.info("Cube cell deletion finished.");
                    rows = idStore.getProcessor("select * from " + NamingUtil.internalDatastoreName(), 0);
                    rows.current().getColumn(0).setName(NamingUtil.internalKeyName());
                    Locator storeloc = new Locator().add(getDatabaseName()).add(getCubeName());
                    definition = new PersistorDefinition();
                    definition.setId(storeloc.toString());
                    definition.setMode(getMode());
                    rows.addPersistor(definition);
                    rows.run();
                    idStore.close();
                    log.info("Finished load " + getName() + " of Cube " + getCubeName() + ". Filled cells changed from " + getCellsFilled() + " to " + c.getFilledCellCount() + ".");
                }
            } catch (Exception e) {
                log.error("Cannot shrink Cube " + getCubeName() + ": " + e.getMessage());
                log.debug("", e);
            }
        }
    }

    public void init() throws InitializationException {
        try {
            super.init();
            setDimensions(getConfigurator().getDimensions());
            setLimit(getConfigurator().getLimit());
            if (getDrillthroughDescription() == null) setDrillthroughDescription(getConfigurator().getDefaultDrillthroughDescription());
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }
}
