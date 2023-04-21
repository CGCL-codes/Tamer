package com.hack23.cia.web.impl.ui.container.admin.parliament;

import java.io.Serializable;
import com.hack23.cia.model.impl.sweden.Committee;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * The Class CommitteeContainer.
 */
public class CommitteeContainer extends AbstractParliamentDataContainer<Committee> implements Serializable {

    /** The Constant COL_GENERATORS. */
    private static final Table.ColumnGenerator[] COL_GENERATORS = new Table.ColumnGenerator[] {};

    /** The Constant GENERATED_COL_ORDER. */
    private static final Object[] GENERATED_COL_ORDER = new Object[] {};

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "resourceType", "name", "shortCode", "importStatus", "importedDate" };

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new committee container.
	 * 
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
    public CommitteeContainer() throws InstantiationException, IllegalAccessException {
        super(Committee.class);
    }

    @Override
    public final ColumnGenerator[] getColumnGenerators() {
        return COL_GENERATORS;
    }

    @Override
    public final Object[] getGeneratedColumns() {
        return GENERATED_COL_ORDER;
    }

    @Override
    public final Object[] getVisibleColumns() {
        return NATURAL_COL_ORDER;
    }
}
