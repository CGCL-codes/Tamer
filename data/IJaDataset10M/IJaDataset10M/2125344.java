package com.hack23.cia.web.impl.ui.container.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.hack23.cia.model.api.common.ModelObject;
import com.hack23.cia.model.api.dto.application.UserSessionDto;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Table;

/**
 * The Class AbstractModelObjectContainer.
 */
public abstract class AbstractModelObjectContainer<ENTITY extends ModelObject> extends BeanItemContainer<ENTITY> implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The type. */
    private final Class<? extends ENTITY> type;

    /**
	 * Instantiates a new abstract model object container.
	 * 
	 * @param type the type
	 * 
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
    public AbstractModelObjectContainer(final Class<? extends ENTITY> type) throws InstantiationException, IllegalAccessException {
        super(type);
        this.type = type;
    }

    /**
	 * Gets the column generators.
	 * 
	 * @return the column generators
	 */
    public abstract Table.ColumnGenerator[] getColumnGenerators();

    /**
	 * Gets the column headers.
	 * 
	 * @param userSessionDto the user session dto
	 * 
	 * @return the column headers
	 */
    public final String[] getColumnHeaders(final UserSessionDto userSessionDto) {
        List<String> headers = new ArrayList<String>();
        for (Object column : getVisibleColumns()) {
            String columnAttribute = (String) column;
            headers.add(userSessionDto.getLanguageResource(type.getSimpleName().toLowerCase() + "." + columnAttribute));
        }
        for (Object column : getGeneratedColumns()) {
            String columnAttribute = (String) column;
            headers.add(userSessionDto.getLanguageResource(type.getSimpleName().toLowerCase() + "." + columnAttribute));
        }
        return headers.toArray(new String[headers.size()]);
    }

    /**
	 * Gets the generated columns.
	 * 
	 * @return the generated columns
	 */
    public abstract Object[] getGeneratedColumns();

    /**
	 * Gets the visible columns.
	 * 
	 * @return the visible columns
	 */
    public abstract Object[] getVisibleColumns();
}
