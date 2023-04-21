package org.avaje.ebean.server.persist.dmlbind;

import java.sql.SQLException;
import org.avaje.ebean.server.deploy.BeanPropertyAssocOne;
import org.avaje.ebean.server.deploy.id.ImportedId;
import org.avaje.ebean.server.persist.dml.GenerateDmlRequest;

/**
 * Bindable for an ManyToOne or OneToOne associated bean.
 */
public class BindableAssocOne implements Bindable {

    private final BeanPropertyAssocOne assocOne;

    private final ImportedId importedId;

    public BindableAssocOne(BeanPropertyAssocOne assocOne) {
        this.assocOne = assocOne;
        this.importedId = assocOne.getImportedId();
    }

    public String toString() {
        return "BindableAssocOne " + assocOne;
    }

    public void dmlAppend(GenerateDmlRequest request, boolean checkIncludes) {
        if (checkIncludes && !request.isIncluded(assocOne)) {
            return;
        }
        importedId.dmlAppend(request);
    }

    /**
	 * Used for dynamic where clause generation.
	 */
    public void dmlWhere(GenerateDmlRequest request, boolean checkIncludes, Object bean) {
        if (checkIncludes && !request.isIncluded(assocOne)) {
            return;
        }
        Object assocBean = assocOne.getValue(bean);
        importedId.dmlWhere(request, assocBean);
    }

    public void dmlBind(BindableRequest request, boolean checkIncludes, Object bean, boolean bindNull) throws SQLException {
        if (checkIncludes && !request.isIncluded(assocOne)) {
            return;
        }
        Object assocBean = assocOne.getValue(bean);
        importedId.bind(request, assocBean, bindNull);
    }
}
