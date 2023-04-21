package org.helianto.document.filter;

import org.helianto.core.Entity;
import org.helianto.core.criteria.OrmCriteriaBuilder;
import org.helianto.document.PrivateDocument;

/**
 * Private document filter adapter.
 * 
 * @author mauriciofernandesdecastro
 */
public class PrivateDocumentFilterAdapter extends AbstractDocumentFilterAdapter<PrivateDocument> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 * 
	 * @param form
	 */
    public PrivateDocumentFilterAdapter(PrivateDocument form) {
        super(form);
    }

    /**
	 * Key constructor
	 * 
	 * @param entity
	 * @param docCode
	 */
    public PrivateDocumentFilterAdapter(Entity entity, String docCode) {
        super(new PrivateDocument(entity, docCode));
    }

    @Override
    public void doFilter(OrmCriteriaBuilder mainCriteriaBuilder) {
        super.doFilter(mainCriteriaBuilder);
        appendEqualFilter("contentType", getForm().getContentType(), mainCriteriaBuilder);
    }

    /**
	 * equals.
	 */
    @Override
    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof PrivateDocumentFilterAdapter)) return false;
        PrivateDocumentFilterAdapter castOther = (PrivateDocumentFilterAdapter) other;
        return this.getForm().equals(castOther.getForm());
    }
}
