package hidb2.gui.editor;

import static hidb2.kern.AttrType.T_String;
import hidb2.gui.Application;
import hidb2.gui.DataUpdatable;
import hidb2.gui.node.FolderDescrNode;
import hidb2.gui.ressources.RscMan;
import hidb2.gui.util.AttributTableViewer;
import hidb2.gui.util.FormEntry;
import hidb2.gui.util.FormEntryAdapter;
import hidb2.gui.util.FormLayoutFactory;
import hidb2.gui.util.IContextPart;
import hidb2.gui.util.Parts;
import hidb2.kern.Attribut;
import hidb2.kern.FolderDescription;
import hidb2.kern.HIDBConst;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * FolderDescription multi-page editor.
 * 
 * TODO : Convert to single page editor (nice to have)
 * 
 * <ul>
 * <li>page 0 : General parameters and statistics
 * <li>page 1 : Folder attributs table
 * <li>page 2 : Card attributs table
 * </ul>
 */
public class FolderDescrEditor extends MultiPageEditorPart implements HIDBConst, IContextPart {

    static final Logger log = Logger.getLogger("hidb2.gui.editor");

    /** Editor Identification */
    public static final String ID = "HIDB2.editors.FolderDescrEditor";

    protected boolean dirty = false;

    private FormEntry _fNameEntry;

    private FormEntry _fCmtEntry;

    private FormEntry _fMainTable, _fCardTable, _fIdxIconEntry, _fIdxLabelEntry, _fNbInstance;

    private AttributTableViewer _tabvFdAttr;

    private AttributTableViewer _tabvCdAttr;

    private void createNameEntry(Composite client, HIDB2Toolkit toolkit, IActionBars actionBars) {
        _fNameEntry = new FormEntry(client, toolkit, "Name", null, false);
        _fNameEntry.setFormEntryListener(new FormEntryAdapter(this, actionBars) {

            public void textValueChanged(FormEntry entry) {
                getFD().setName(entry.getValue());
                getFD().modify();
            }
        });
        _fNameEntry.setEditable(isEditable());
    }

    private void createCmtEntry(Composite client, HIDB2Toolkit toolkit, IActionBars actionBars) {
        _fCmtEntry = new FormEntry(client, toolkit, "Comment", null, false);
        _fCmtEntry.setFormEntryListener(new FormEntryAdapter(this, actionBars) {

            public void textValueChanged(FormEntry entry) {
                getFD().setComment(entry.getValue());
                getFD().modify();
            }
        });
        _fCmtEntry.setEditable(isEditable());
    }

    private void createLabelIdxEntry(Composite client, HIDB2Toolkit toolkit, IActionBars actionBars) {
        _fIdxLabelEntry = new FormEntry(client, toolkit, "Label Index", null, false);
        _fIdxLabelEntry.setFormEntryListener(new FormEntryAdapter(this, actionBars) {

            public void textValueChanged(FormEntry entry) {
                getFD().setLabelAttrIndex(Integer.parseInt(entry.getValue()));
                getFD().modify();
            }
        });
        _fIdxLabelEntry.setEditable(isEditable());
    }

    private void createIconIdxEntry(Composite client, HIDB2Toolkit toolkit, IActionBars actionBars) {
        _fIdxIconEntry = new FormEntry(client, toolkit, "Icon Index", null, false);
        _fIdxIconEntry.setFormEntryListener(new FormEntryAdapter(this, actionBars) {

            public void textValueChanged(FormEntry entry) {
                getFD().setIconAttrIndex(Integer.parseInt(entry.getValue()));
                getFD().modify();
            }
        });
        _fIdxIconEntry.setEditable(isEditable());
    }

    private void fillBodyPageGen(ScrolledForm managedForm, HIDB2Toolkit toolkit) {
        Composite body = managedForm.getForm().getBody();
        body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));
        Composite left = toolkit.createComposite(body);
        left.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
        left.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        Section section = Parts.createStaticSection(toolkit, left, "General Attributes");
        Composite container = Parts.createStaticSectionClient(toolkit, section);
        IActionBars actionBars = getEditorSite().getActionBars();
        createNameEntry(container, toolkit, actionBars);
        createCmtEntry(container, toolkit, null);
        createIconIdxEntry(container, toolkit, null);
        createLabelIdxEntry(container, toolkit, null);
        section.setClient(container);
        Composite right = toolkit.createComposite(body);
        right.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
        right.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        Section sectionR = Parts.createStaticSection(toolkit, right, "Informations");
        Composite cnt2 = Parts.createStaticSectionClient(toolkit, sectionR);
        _fMainTable = new FormEntry(cnt2, toolkit, "Main Table", SWT.SINGLE);
        _fMainTable.setEditable(false);
        _fCardTable = new FormEntry(cnt2, toolkit, "Card Table", SWT.SINGLE);
        _fCardTable.setEditable(false);
        _fNbInstance = new FormEntry(cnt2, toolkit, "Instance Count", SWT.SINGLE);
        _fNbInstance.setEditable(false);
        sectionR.setClient(cnt2);
    }

    /**
   * Creates page 0 of the multi-page editor, which contains the general fields.
   */
    private void createGeneralPage(HIDB2Toolkit toolkit) {
        ScrolledForm form = toolkit.createScrolledForm(getContainer());
        fillBodyPageGen(form, toolkit);
        int index = addPage(form);
        setPageText(index, "General");
    }

    /**
   * Create the attribut edition window
   * 
   * @param managedForm
   * @param toolkit
   * @param AddCb
   * @param DelCb
   * @return
   */
    private AttributTableViewer createTabAttrSection(ScrolledForm managedForm, HIDB2Toolkit toolkit, String sectionTitle, SelectionListener AddAfterCb, SelectionListener AddBeforeCb, SelectionListener DelCb, SelectionListener UpCb, SelectionListener DownCb) {
        final AttributTableViewer tabv;
        Composite body = managedForm.getForm().getBody();
        body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 1));
        Composite left = toolkit.createComposite(body);
        left.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
        left.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
        Section section = Parts.createStaticSection(toolkit, left, sectionTitle);
        Composite container = toolkit.createComposite(section, SWT.NONE);
        container.setLayout(FormLayoutFactory.createSectionClientTableWrapLayout(false, 2));
        TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
        container.setLayoutData(data);
        tabv = new AttributTableViewer(container);
        tabv.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });
        Composite containerBtn = toolkit.createComposite(container, SWT.NONE);
        containerBtn.setLayout(FormLayoutFactory.createClearGridLayout(true, 1));
        toolkit.createGridButton(containerBtn, "Add After", AddAfterCb);
        toolkit.createGridButton(containerBtn, "Add Before", AddBeforeCb);
        toolkit.createGridButton(containerBtn, "Delete", DelCb);
        toolkit.createGridButton(containerBtn, "Up", UpCb);
        toolkit.createGridButton(containerBtn, "Down", DownCb);
        tabv.createMMI(new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                Attribut attr = (Attribut) element;
                if (columnIndex == 0) {
                    if (attr.getDisplayOrder() == getFD().getIconAttrIndex()) {
                        return RscMan.getImage(RscMan.IN_BOSS);
                    } else {
                        if (attr.getDisplayOrder() == getFD().getLabelAttrIndex()) {
                            return RscMan.getImage(RscMan.IN_GREENBOOKMARK);
                        }
                    }
                }
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {
                return tabv.getLstAttr().get(columnIndex).format(element);
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object element, String property) {
                return true;
            }

            public void removeListener(ILabelProviderListener listener) {
            }
        });
        section.setClient(container);
        return tabv;
    }

    /**
   * Create the page 1 with the attributes of the folderdescription
   */
    private void createFolderAttrPage(HIDB2Toolkit toolkit) {
        ScrolledForm form = toolkit.createScrolledForm(getContainer());
        _tabvFdAttr = createTabAttrSection(form, toolkit, "Folder Attributes", new SelectionAdapter() {

            /**
         * Add After Callback
         */
            @Override
            public void widgetSelected(SelectionEvent e) {
                ISelection selection = _tabvFdAttr.getSelection();
                int addidx = 0;
                if (selection.isEmpty()) {
                    addidx = getFD().getAttributList().size();
                } else {
                    Object obj = ((IStructuredSelection) selection).getFirstElement();
                    Attribut a = (Attribut) obj;
                    addidx = a.getDisplayOrder() + 1;
                }
                Attribut at0 = new Attribut(getFD().getID(), T_String);
                getFD().addAttribut(at0, addidx);
                _tabvFdAttr.refresh();
            }
        }, new SelectionAdapter() {

            /**
        * Add Before Callback
        */
            @Override
            public void widgetSelected(SelectionEvent e) {
                ISelection selection = _tabvFdAttr.getSelection();
                int addidx = 0;
                if (selection.isEmpty()) {
                    Object obj = ((IStructuredSelection) selection).getFirstElement();
                    Attribut a = (Attribut) obj;
                    addidx = Math.max(0, a.getDisplayOrder() - 1);
                }
                Attribut at0 = new Attribut(getFD().getID(), T_String);
                getFD().addAttribut(at0, addidx);
                _tabvFdAttr.refresh();
            }
        }, new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ISelection selection = _tabvFdAttr.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                Attribut attr = (Attribut) obj;
                boolean rb = MessageDialog.openConfirm(_tabvFdAttr.getControl().getShell(), "Delete Attribute", "Do you confirm the deletion of Attribut:" + attr.getName() + "\nType:" + attr.getType().name);
                if (rb) {
                    getFD().removeAttribut(attr);
                    _tabvFdAttr.refresh();
                }
            }
        }, new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ISelection selection = _tabvFdAttr.getSelection();
                if (!selection.isEmpty()) {
                    Object obj = ((IStructuredSelection) selection).getFirstElement();
                    Attribut a = (Attribut) obj;
                    int r = getFD().changeAttributPosition(a, false);
                    if (r == C_OK) {
                        setDirty(true);
                    }
                    _tabvFdAttr.refresh();
                }
            }
        }, new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ISelection selection = _tabvFdAttr.getSelection();
                if (!selection.isEmpty()) {
                    Object obj = ((IStructuredSelection) selection).getFirstElement();
                    Attribut a = (Attribut) obj;
                    int r = getFD().changeAttributPosition(a, true);
                    if (r == C_OK) {
                        setDirty(true);
                    }
                    _tabvFdAttr.refresh();
                }
            }
        });
        int index = addPage(form);
        setPageText(index, "Attributes");
    }

    /**
   * Create the page 2 with the attributes of the carddescription
   */
    private void createCardAttrPage(HIDB2Toolkit toolkit) {
        ScrolledForm form = toolkit.createScrolledForm(getContainer());
        _tabvCdAttr = createTabAttrSection(form, toolkit, "Card Attributes", new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Attribut at0 = new Attribut(getFD().getCardDescription().getID(), T_String);
                getFD().getCardDescription().addAttribut(at0, 0);
                _tabvCdAttr.refresh();
            }
        }, new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Attribut at0 = new Attribut(getFD().getCardDescription().getID(), T_String);
                getFD().getCardDescription().addAttribut(at0, 0);
                _tabvCdAttr.refresh();
            }
        }, new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ISelection selection = _tabvCdAttr.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                Attribut attr = (Attribut) obj;
                boolean rb = MessageDialog.openConfirm(_tabvFdAttr.getControl().getShell(), "Delete Attribute", "Do you confirm the deletion of Attribut:" + attr.getName() + "\nType:" + attr.getType().name);
                if (rb) {
                    getFD().getCardDescription().removeAttribut(attr);
                    _tabvCdAttr.refresh();
                }
            }
        }, new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ISelection selection = _tabvCdAttr.getSelection();
                if (!selection.isEmpty()) {
                    Object obj = ((IStructuredSelection) selection).getFirstElement();
                    Attribut a = (Attribut) obj;
                    int r = getFD().getCardDescription().changeAttributPosition(a, false);
                    if (r == C_OK) {
                        setDirty(true);
                    }
                    _tabvCdAttr.refresh();
                }
            }
        }, new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ISelection selection = _tabvCdAttr.getSelection();
                if (!selection.isEmpty()) {
                    Object obj = ((IStructuredSelection) selection).getFirstElement();
                    Attribut a = (Attribut) obj;
                    int r = getFD().getCardDescription().changeAttributPosition(a, true);
                    if (r == C_OK) {
                        setDirty(true);
                    }
                    _tabvCdAttr.refresh();
                }
            }
        });
        int index = addPage(form);
        setPageText(index, "Card Attr");
    }

    @Override
    protected void createPages() {
        HIDB2Toolkit toolkit = new HIDB2Toolkit(getContainer().getDisplay());
        createGeneralPage(toolkit);
        createFolderAttrPage(toolkit);
        createCardAttrPage(toolkit);
    }

    protected void setDirty(boolean value) {
        dirty = value;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        _fCmtEntry.commit();
        _fNameEntry.commit();
        _fIdxIconEntry.commit();
        _fIdxLabelEntry.commit();
        int minImgOrder = getFD().getMinImageOrder();
        boolean r = true;
        if ((minImgOrder < Integer.MAX_VALUE) && (minImgOrder != 0)) {
            r = MessageDialog.openConfirm(getContainer().getShell(), "", "The Image is not first in the display order.\nConfirm save?");
        }
        if (r) {
            getFD().write(Application.getDataStore());
            setDirty(false);
            Application.signalRefresh(DataUpdatable.FOLDERDESCRLIST, getFD());
        } else {
            monitor.setCanceled(true);
        }
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
   * Calculates the contents of page 2 when the it is activated.
   */
    protected void pageChange(int newPageIndex) {
        super.pageChange(newPageIndex);
        FolderDescription fd = getFD();
        switch(newPageIndex) {
            case 0:
                setPartName(fd.getName());
                _fNameEntry.setValue(fd.getName(), true);
                _fCmtEntry.setValue(fd.getComment(), true);
                _fIdxIconEntry.setValue(Integer.toString(fd.getIconAttrIndex()), true);
                _fIdxLabelEntry.setValue(Integer.toString(fd.getLabelAttrIndex()), true);
                _fMainTable.setValue(fd.getTName(), true);
                _fCardTable.setValue(fd.getCardDescription().getTName(), true);
                fd.updateStats(Application.getDataStore());
                _fNbInstance.setValue(Long.toString(fd.getNbInstance()), true);
                break;
            case 1:
                _tabvFdAttr.setDescr(fd);
                break;
            case 2:
                _tabvCdAttr.setDescr(fd.getCardDescription());
                break;
        }
    }

    public void cancelEdit() {
    }

    @Override
    public void fireSaveNeeded() {
        setDirty(true);
    }

    @Override
    public String getContextId() {
        return null;
    }

    @Override
    public FormPage getPage() {
        return null;
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    public FolderDescription getFD() {
        return (FolderDescription) ((FolderDescrNode) getEditorInput()).getDescr();
    }

    @Override
    public void dispose() {
        super.dispose();
        Application.release(getFD());
    }
}
