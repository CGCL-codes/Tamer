package org.plazmaforge.bsolution.partner.client.swt.forms;

import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.plazmaforge.bsolution.partner.common.beans.IPartnerTitle;
import org.plazmaforge.bsolution.partner.common.services.PartnerRepresentativeService;
import org.plazmaforge.framework.client.forms.IEditForm;
import org.plazmaforge.framework.client.swt.SWTToolkit;
import org.plazmaforge.framework.client.swt.forms.AbstractListForm;
import org.plazmaforge.framework.core.criteria.ICriteria;
import org.plazmaforge.framework.core.criteria.IOwnCriteriaImplementator;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * 
 * @author Oleh Hapon
 * $Id: PartnerRepresentativeListForm.java,v 1.5 2010/12/05 07:57:19 ohapon Exp $
 */
public class PartnerRepresentativeListForm extends AbstractListForm implements IPartnerableForm, IOwnCriteriaImplementator {

    private Table table;

    public PartnerRepresentativeListForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("PartnerRepresentativeListForm.title"));
        TableColumn tableColumn;
        TableColumn partnerColumn;
        TableColumn nameColumn;
        TableColumn codeColumn;
        TableColumn documentColumn;
        TableColumn addressColumn;
        TableColumn phoneColumn;
        setLayout(new FillLayout());
        table = new Table(this, SWTToolkit.TABLE_STYLE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setResizable(false);
        tableColumn.setWidth(20);
        partnerColumn = new TableColumn(table, SWT.NONE);
        partnerColumn.setWidth(200);
        partnerColumn.setText(Messages.getString("PartnerRepresentativeListForm.partnerColumn.text"));
        nameColumn = new TableColumn(table, SWT.NONE);
        nameColumn.setWidth(300);
        nameColumn.setText(Messages.getString("PartnerRepresentativeListForm.nameColumn.text"));
        codeColumn = new TableColumn(table, SWT.NONE);
        codeColumn.setWidth(80);
        codeColumn.setText(Messages.getString("PartnerRepresentativeListForm.codeColumn.text"));
        documentColumn = new TableColumn(table, SWT.NONE);
        documentColumn.setWidth(150);
        documentColumn.setText(Messages.getString("PartnerRepresentativeListForm.documentColumn.text"));
        addressColumn = new TableColumn(table, SWT.NONE);
        addressColumn.setWidth(250);
        addressColumn.setText(Messages.getString("PartnerRepresentativeListForm.addressColumn.text"));
        phoneColumn = new TableColumn(table, SWT.NONE);
        phoneColumn.setWidth(100);
        phoneColumn.setText(Messages.getString("PartnerRepresentativeListForm.phoneColumn.text"));
        this.setSize(new Point(700, 300));
    }

    public Table getTable() {
        return table;
    }

    protected void bindTable() {
        bindColumn(1, "partner");
        bindColumn(2, "name");
        bindColumn(3, "code");
        bindColumn(4, "document");
        bindColumn(5, "address");
        bindColumn(6, "phone");
    }

    protected List loadDataList() throws ApplicationException {
        return isSupportParentEntity() ? getPartnerRepresentativeService().findByPartner(getPartnerId()) : getPartnerRepresentativeService().findAll();
    }

    protected List loadDataList(ICriteria criteria) throws ApplicationException {
        return isSupportParentEntity() ? getPartnerRepresentativeService().findByPartner(getPartnerId(), criteria) : getPartnerRepresentativeService().findByCriteria(criteria);
    }

    protected PartnerRepresentativeService getPartnerRepresentativeService() {
        return (PartnerRepresentativeService) getEntityService();
    }

    protected void initEditForm(IEditForm form) throws ApplicationException {
        super.initEditForm(form);
        IPartnerTitle partner = getPartner();
        form.setChildFormMode(partner != null);
        form.setSupportParentEntity(partner != null);
    }

    public IPartnerTitle getPartner() {
        return (IPartnerTitle) getParentEntity();
    }

    public Integer getPartnerId() {
        return getPartner() == null ? null : getPartner().getId();
    }
}
