package mipt.aaf.edit.swing.data;

import javax.swing.JComponent;
import javax.swing.JTable;
import mipt.aaf.edit.data.DataEditController;
import mipt.aaf.edit.data.link.LinkDataEditController;
import mipt.aaf.edit.form.mo.MultiObjectForm;
import mipt.aaf.edit.swing.form.MultiForms;
import mipt.aaf.edit.swing.form.TableForm;

/**
 * DataSwingEditor supporting links editing in MultiObjectForms. See MultiFormsSwingEditor as analog
 * By default adds MultiForms's views component after last field panel before button pane 
 * Note: correct this class and AbstractLinksDataSwingEditor simultaneously
 * (and don't place complex logic here: it should be placed in LinksForms delegate!)
 * @author Evdokimov
 */
public abstract class LinksDataSwingEditor extends DataSwingEditor implements LinksForms.Parent {

    protected LinksForms linksForms;

    /**
	 * @return forms
	 */
    public final LinksForms getLinksForms() {
        if (linksForms == null) linksForms = initLinksForms();
        return linksForms;
    }

    protected LinksForms initLinksForms() {
        return new LinksForms(this);
    }

    /**
	 * 
	 */
    public final LinkDataEditController getLinkController() {
        return (LinkDataEditController) getController();
    }

    /**
	 * @see mipt.aaf.edit.swing.data.DataSwingEditor#initController()
	 */
    protected DataEditController initController() {
        return new LinkDataEditController(this);
    }

    /**
	 * @see mipt.aaf.edit.data.link.LinksDelegate#getTypeOfDataSetField(java.lang.String)
	 */
    public String getTypeOfDataSetField(String dataSetField) {
        return null;
    }

    /**
	 * @see mipt.aaf.edit.data.link.LinksDelegate#updateLinksView()
	 */
    public void updateLinksView() {
        getLinksForms().updateLinksView();
    }

    /**
	 * @see mipt.aaf.edit.swing.form.MultiForms.Parent#getFormNames()
	 */
    public String[] getFormNames() {
        return getLinkTypes();
    }

    /**
	 * This method is only for naming compatibility with *MultiFormSwingEditor
	 */
    public final MultiForms getMultiForms() {
        return getLinksForms();
    }

    /**
	 * @see mipt.aaf.edit.UIEditDelegate#initView()
	 */
    public void initView() {
        super.initView();
        getMultiForms().initView();
    }

    /**
	 * @see mipt.aaf.edit.swing.AbstractSwingEditor#initActionListening(java.lang.String)
	 */
    public void initActionListening(String action) {
        getMultiForms().initActionListening(action);
    }

    public final MultiObjectForm getMultiForm(String formName) {
        return getMultiForms().getMultiForm(formName);
    }

    public abstract MultiObjectForm initMultiForm(String formName);

    /**
	 * Call only if you have initialized TableForm for this formName
	 */
    public JTable getTable(String formName) {
        return ((TableForm) getMultiForm(formName)).getTable();
    }

    /**
	 * @see mipt.aaf.edit.swing.form.MultiForms.Parent#addMultiViews(javax.swing.JComponent)
	 */
    public void addMultiViews(JComponent views) {
        getRootComponent().add(views, initPanelConstraints(getMultiViewsIndex(), null));
    }

    /**
	 * @see mipt.aaf.edit.swing.SwingEditor#getButtonPaneY()
	 */
    protected int getButtonPaneIndex() {
        return getFieldCount() + 1;
    }

    /**
	 * @see mipt.aaf.edit.swing.SwingEditor#getMaximumHeightComponentIndex()
	 */
    protected int getMaximumHeightComponentIndex() {
        return getMultiViewsIndex();
    }

    protected int getMultiViewsIndex() {
        return getFieldCount();
    }
}
