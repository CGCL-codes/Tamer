package br.gov.frameworkdemoiselle.template;

import java.util.List;

/**
 * Extends View interface adding behavior related to CRUD operations.
 * 
 * @author Marlon
 */
public interface CrudView<Bean> extends View {

    /**
	 * Get the bean which will receive CRUD operations.
	 * 
	 * @return Bean.
	 */
    Bean getBean();

    /**
	 * Set the bean which will receive CRUD operations.
	 * 
	 * @param bean Bean.
	 */
    void setBean(Bean bean);

    /**
	 * Clear the bean.
	 */
    void clear();

    /**
	 * Set the list of beans.
	 * 
	 * @param list List.
	 */
    void setList(List<Bean> list);
}
