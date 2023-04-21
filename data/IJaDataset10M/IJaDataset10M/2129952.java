package org.dengues.designer.ui.query;

import org.dengues.commons.IDenguesSharedImage;
import org.dengues.commons.utils.ImageUtil;
import org.dengues.designer.ui.i18n.Messages;
import org.dengues.designer.ui.query.sections.FiltersSection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-1-21 qiang.zhang $
 * 
 */
public class FiltersFormPage extends AbstractFormPage {

    /**
     * Qiang.Zhang.Adolf@gmail.com FiltersFormPage constructor comment.
     * 
     * @param editor
     */
    public FiltersFormPage(FormEditor editor) {
        super(editor, ID_FILTERS, Messages.getString("FiltersFormPage.name"));
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);
        ScrolledForm form = managedForm.getForm();
        form.setImage(ImageUtil.getImage(IDenguesSharedImage.QUERSTION));
        form.setText(Messages.getString("FiltersFormPage.name"));
        Composite body = form.getBody();
        body.setLayout(createFormGridLayout(true, 1));
        managedForm.addPart(new FiltersSection(this, body));
    }
}
