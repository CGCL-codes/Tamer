package org.larz.dom3.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.larz.dom3.Activator;
import org.larz.dom3.dm.dm.AbstractElement;
import org.larz.dom3.dm.dm.DmFactory;
import org.larz.dom3.dm.dm.Dom3Mod;
import org.larz.dom3.dm.dm.GeneralInst1;
import org.larz.dom3.dm.ui.editor.DmXtextEditor;
import org.larz.dom3.dm.ui.help.HelpTextHelper;
import org.larz.dom3.image.ImageConverter;
import org.larz.dom3.image.ImageLoader;

public class MasterFormPage extends FormPage {

    public SummaryList block;

    private XtextEditor doc;

    enum General {

        MODNAME(Messages.getString("ScrolledPropertiesBlock.modname")), DESC(Messages.getString("ScrolledPropertiesBlock.description")), ICON(Messages.getString("ScrolledPropertiesBlock.icon")), VERSION(Messages.getString("ScrolledPropertiesBlock.version")), DOMVERSION(Messages.getString("ScrolledPropertiesBlock.domversion"));

        private String label;

        General(String label) {
            this.label = label;
        }
    }

    class GeneralFields {

        private Text value;
    }

    private EnumMap<General, GeneralFields> generalMap = new EnumMap<General, GeneralFields>(General.class);

    enum General2 {

        POPPERGOLD(Messages.getString("ScrolledPropertiesBlock.poppergold"), "100"), RESOURCEMULT(Messages.getString("ScrolledPropertiesBlock.resourcemult"), "100"), SUPPLYMULT(Messages.getString("ScrolledPropertiesBlock.supplymult"), "100"), UNRESTHALFINC(Messages.getString("ScrolledPropertiesBlock.unresthalfinc"), "50"), UNRESTHALFRES(Messages.getString("ScrolledPropertiesBlock.unresthalfres"), "100"), EVENTISRARE(Messages.getString("ScrolledPropertiesBlock.eventisrare"), "15"), TURMOILINCOME(Messages.getString("ScrolledPropertiesBlock.turmoilincome"), "7"), TURMOILEVENTS(Messages.getString("ScrolledPropertiesBlock.turmoilevents"), "5"), DEATHINCOME(Messages.getString("ScrolledPropertiesBlock.deathincome"), "2"), DEATHSUPPLY(Messages.getString("ScrolledPropertiesBlock.deathsupply"), "20"), DEATHDEATH(Messages.getString("ScrolledPropertiesBlock.deathdeath"), "2"), SLOTHINCOME(Messages.getString("ScrolledPropertiesBlock.slothincome"), "2"), SLOTHRESOURCES(Messages.getString("ScrolledPropertiesBlock.slothresources"), "15"), COLDINCOME(Messages.getString("ScrolledPropertiesBlock.coldincome"), "5"), COLDSUPPLY(Messages.getString("ScrolledPropertiesBlock.coldsupply"), "10"), MISFORTUNE(Messages.getString("ScrolledPropertiesBlock.misfortune"), "10"), LUCKEVENTS(Messages.getString("ScrolledPropertiesBlock.luckevents"), "5"), RESEARCHSCALE(Messages.getString("ScrolledPropertiesBlock.researchscale"), "2");

        private String label;

        private String defaultValue;

        General2(String label, String defaultValue) {
            this.label = label;
            this.defaultValue = defaultValue;
        }
    }

    class General2Fields {

        private Text value;

        private Button check;
    }

    private EnumMap<General2, General2Fields> general2Map = new EnumMap<General2, General2Fields>(General2.class);

    /**
	 * @param editor
	 * @param doc
	 */
    public MasterFormPage(DmEditor editor, XtextEditor doc) {
        super(editor, "MasterDetails", Messages.getString("MasterDetailsPage.details.label"));
        block = new SummaryList(this, editor, doc);
        this.doc = doc;
        generalMap.put(General.MODNAME, new GeneralFields());
        generalMap.put(General.DESC, new GeneralFields());
        generalMap.put(General.ICON, new GeneralFields());
        generalMap.put(General.VERSION, new GeneralFields());
        generalMap.put(General.DOMVERSION, new GeneralFields());
        general2Map.put(General2.POPPERGOLD, new General2Fields());
        general2Map.put(General2.RESOURCEMULT, new General2Fields());
        general2Map.put(General2.SUPPLYMULT, new General2Fields());
        general2Map.put(General2.UNRESTHALFINC, new General2Fields());
        general2Map.put(General2.UNRESTHALFRES, new General2Fields());
        general2Map.put(General2.EVENTISRARE, new General2Fields());
        general2Map.put(General2.TURMOILINCOME, new General2Fields());
        general2Map.put(General2.TURMOILEVENTS, new General2Fields());
        general2Map.put(General2.DEATHINCOME, new General2Fields());
        general2Map.put(General2.DEATHSUPPLY, new General2Fields());
        general2Map.put(General2.DEATHDEATH, new General2Fields());
        general2Map.put(General2.SLOTHINCOME, new General2Fields());
        general2Map.put(General2.SLOTHRESOURCES, new General2Fields());
        general2Map.put(General2.COLDINCOME, new General2Fields());
        general2Map.put(General2.COLDSUPPLY, new General2Fields());
        general2Map.put(General2.MISFORTUNE, new General2Fields());
        general2Map.put(General2.LUCKEVENTS, new General2Fields());
        general2Map.put(General2.RESEARCHSCALE, new General2Fields());
    }

    protected void createFormContent(final IManagedForm managedForm) {
        final ScrolledForm form = managedForm.getForm();
        form.setText(Messages.getString("MasterDetailsPage.title"));
        form.setBackgroundImage(Activator.getImageDescriptor("icons/form_banner.gif").createImage());
        FormToolkit toolkit = managedForm.getToolkit();
        Section expandable = toolkit.createSection(form.getBody(), Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED);
        expandable.setText(Messages.getString("MasterDetailsPage.general.title"));
        expandable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        expandable.addExpansionListener(new ExpansionAdapter() {

            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }
        });
        final Composite header1 = toolkit.createComposite(expandable);
        header1.setLayout(new GridLayout(2, true));
        final Composite header = toolkit.createComposite(header1);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        header.setLayout(layout);
        expandable.setClient(header1);
        expandable.setExpanded(false);
        for (final Map.Entry<General, GeneralFields> fields : generalMap.entrySet()) {
            final General general = fields.getKey();
            Label label = toolkit.createLabel(header, general.label);
            label.setToolTipText(HelpTextHelper.getText(HelpTextHelper.GENERAL_CATEGORY, general.label));
            final Text modname = toolkit.createText(header, getGeneral(general, doc), (general.equals(General.DESC) ? SWT.MULTI | SWT.WRAP : SWT.SINGLE) | SWT.BORDER);
            modname.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(FocusEvent e) {
                    setGeneral(general, doc, modname.getText());
                }
            });
            modname.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.character == '\r') {
                        setGeneral(general, doc, modname.getText());
                    }
                }
            });
            GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
            data.widthHint = 380;
            modname.setLayoutData(data);
            if (general.equals(General.DESC)) {
                modname.addListener(SWT.Modify, new Listener() {

                    @Override
                    public void handleEvent(Event event) {
                        int currentHeight = modname.getSize().y;
                        int preferredHeight = modname.computeSize(380, SWT.DEFAULT).y;
                        if (currentHeight < preferredHeight || currentHeight > 2 * preferredHeight) {
                            GridData data = (GridData) modname.getLayoutData();
                            data.heightHint = preferredHeight;
                            form.getBody().layout(true, true);
                        }
                    }
                });
            }
            fields.getValue().value = modname;
        }
        final String iconPath = getGeneral(General.ICON, doc);
        if (iconPath != null && iconPath.length() > 0) {
            ImageLoader loader = new ImageLoader() {

                @Override
                public InputStream getStream() throws IOException {
                    String path = ((DmXtextEditor) doc).getPath();
                    path = path.substring(0, path.lastIndexOf('/') + 1);
                    if (iconPath != null && iconPath.startsWith("./")) {
                        path += iconPath.substring(2);
                    } else if (iconPath != null) {
                        path += iconPath;
                    }
                    return new FileInputStream(new File(path));
                }
            };
            try {
                Image image = new Image(null, ImageConverter.convertToSWT(loader.loadImage()));
                Label label = toolkit.createLabel(header1, "");
                label.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Composite general2Comp = toolkit.createComposite(header1);
        layout = new GridLayout(18, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        general2Comp.setLayout(layout);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        general2Comp.setLayoutData(gd);
        for (final Map.Entry<General2, General2Fields> fields : general2Map.entrySet()) {
            final General2 general2 = fields.getKey();
            final Button check = toolkit.createButton(general2Comp, general2.label, SWT.CHECK);
            check.setToolTipText(HelpTextHelper.getText(HelpTextHelper.GENERAL_CATEGORY, general2.label));
            GridData data = new GridData(SWT.BEGINNING, SWT.DEFAULT, false, false);
            check.setLayoutData(data);
            final Text modname = toolkit.createText(general2Comp, getGeneral2(general2, doc) != null ? getGeneral2(general2, doc).toString() : null, SWT.SINGLE | SWT.BORDER);
            modname.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(FocusEvent e) {
                    setGeneral2(general2, doc, modname.getText());
                }
            });
            modname.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.character == '\r') {
                        setGeneral2(general2, doc, modname.getText());
                    }
                }
            });
            data = new GridData(SWT.BEGINNING, SWT.DEFAULT, false, false);
            data.widthHint = 30;
            modname.setLayoutData(data);
            if (getGeneral2(general2, doc) == null) {
                modname.setEnabled(false);
            }
            check.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (check.getSelection()) {
                        addGeneral2(general2, doc, general2.defaultValue);
                        modname.setEnabled(true);
                        modname.setText(general2.defaultValue);
                    } else {
                        removeInst2(general2, doc);
                        modname.setEnabled(false);
                        modname.setText("");
                    }
                }
            });
            check.setSelection(getGeneral2(general2, doc) != null);
            Label space = toolkit.createLabel(general2Comp, "");
            gd = new GridData(SWT.DEFAULT, SWT.DEFAULT, false, false);
            gd.widthHint = 10;
            space.setLayoutData(gd);
            fields.getValue().value = modname;
            fields.getValue().check = check;
        }
        block.createContent(managedForm);
    }

    private String getGeneral(final General general, XtextEditor editor) {
        final IXtextDocument myDocument = editor.getDocument();
        return myDocument.readOnly(new IUnitOfWork<String, XtextResource>() {

            @Override
            public String exec(XtextResource state) throws Exception {
                Dom3Mod dom3Mod = (Dom3Mod) state.getContents().get(0);
                switch(general) {
                    case MODNAME:
                        return dom3Mod.getModname();
                    case DESC:
                        return dom3Mod.getDesc();
                    case ICON:
                        return dom3Mod.getIcon();
                    case VERSION:
                        return dom3Mod.getVersion();
                    case DOMVERSION:
                        return dom3Mod.getDomversion();
                }
                return null;
            }
        });
    }

    private Integer getGeneral2(final General2 general, XtextEditor editor) {
        final IXtextDocument myDocument = editor.getDocument();
        return myDocument.readOnly(new IUnitOfWork<Integer, XtextResource>() {

            @Override
            public Integer exec(XtextResource state) throws Exception {
                Dom3Mod dom3Mod = (Dom3Mod) state.getContents().get(0);
                for (AbstractElement abElement : dom3Mod.getElements()) {
                    if (abElement instanceof GeneralInst1) {
                        GeneralInst1 element = (GeneralInst1) abElement;
                        switch(general) {
                            case POPPERGOLD:
                                if (element.isPoppergold()) {
                                    return element.getValue();
                                }
                                break;
                            case RESOURCEMULT:
                                if (element.isResourcemult()) {
                                    return element.getValue();
                                }
                                break;
                            case SUPPLYMULT:
                                if (element.isSupplymult()) {
                                    return element.getValue();
                                }
                                break;
                            case UNRESTHALFINC:
                                if (element.isUnresthalfinc()) {
                                    return element.getValue();
                                }
                                break;
                            case UNRESTHALFRES:
                                if (element.isUnresthalfres()) {
                                    return element.getValue();
                                }
                                break;
                            case EVENTISRARE:
                                if (element.isEventisrare()) {
                                    return element.getValue();
                                }
                                break;
                            case TURMOILINCOME:
                                if (element.isTurmoilincome()) {
                                    return element.getValue();
                                }
                                break;
                            case TURMOILEVENTS:
                                if (element.isTurmoilevents()) {
                                    return element.getValue();
                                }
                                break;
                            case DEATHINCOME:
                                if (element.isDeathincome()) {
                                    return element.getValue();
                                }
                                break;
                            case DEATHSUPPLY:
                                if (element.isDeathsupply()) {
                                    return element.getValue();
                                }
                                break;
                            case DEATHDEATH:
                                if (element.isDeathdeath()) {
                                    return element.getValue();
                                }
                                break;
                            case SLOTHINCOME:
                                if (element.isSlothincome()) {
                                    return element.getValue();
                                }
                                break;
                            case SLOTHRESOURCES:
                                if (element.isSlothresources()) {
                                    return element.getValue();
                                }
                                break;
                            case COLDINCOME:
                                if (element.isColdincome()) {
                                    return element.getValue();
                                }
                                break;
                            case COLDSUPPLY:
                                if (element.isColdsupply()) {
                                    return element.getValue();
                                }
                                break;
                            case MISFORTUNE:
                                if (element.isMisfortune()) {
                                    return element.getValue();
                                }
                                break;
                            case LUCKEVENTS:
                                if (element.isLuckevents()) {
                                    return element.getValue();
                                }
                                break;
                            case RESEARCHSCALE:
                                if (element.isResearchscale()) {
                                    return element.getValue();
                                }
                                break;
                        }
                    }
                }
                return null;
            }
        });
    }

    private void setGeneral(final General general, final XtextEditor editor, final String newName) {
        final IXtextDocument myDocument = editor.getDocument();
        myDocument.modify(new IUnitOfWork.Void<XtextResource>() {

            @Override
            public void process(XtextResource resource) throws Exception {
                Dom3Mod dom3Mod = (Dom3Mod) resource.getContents().get(0);
                switch(general) {
                    case MODNAME:
                        dom3Mod.setModname(newName);
                        break;
                    case DESC:
                        dom3Mod.setDesc(newName);
                        break;
                    case ICON:
                        dom3Mod.setIcon(newName);
                        break;
                    case VERSION:
                        dom3Mod.setVersion(newName);
                        break;
                    case DOMVERSION:
                        dom3Mod.setDomversion(newName);
                        break;
                }
            }
        });
    }

    private void addGeneral2(final General2 general2, final XtextEditor editor, final String newName) {
        BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

            @Override
            public void run() {
                final IXtextDocument myDocument = editor.getDocument();
                myDocument.modify(new IUnitOfWork.Void<XtextResource>() {

                    @Override
                    public void process(XtextResource state) throws Exception {
                        Dom3Mod dom3Mod = (Dom3Mod) state.getContents().get(0);
                        EList<AbstractElement> mods = dom3Mod.getElements();
                        GeneralInst1 element = DmFactory.eINSTANCE.createGeneralInst1();
                        switch(general2) {
                            case POPPERGOLD:
                                element.setPoppergold(true);
                                break;
                            case RESOURCEMULT:
                                element.setResourcemult(true);
                                break;
                            case SUPPLYMULT:
                                element.setSupplymult(true);
                                break;
                            case UNRESTHALFINC:
                                element.setUnresthalfinc(true);
                                break;
                            case UNRESTHALFRES:
                                element.setUnresthalfres(true);
                                break;
                            case EVENTISRARE:
                                element.setEventisrare(true);
                                break;
                            case TURMOILINCOME:
                                element.setTurmoilincome(true);
                                break;
                            case TURMOILEVENTS:
                                element.setTurmoilevents(true);
                                break;
                            case DEATHINCOME:
                                element.setDeathincome(true);
                                break;
                            case DEATHSUPPLY:
                                element.setDeathsupply(true);
                                break;
                            case DEATHDEATH:
                                element.setDeathdeath(true);
                                break;
                            case SLOTHINCOME:
                                element.setSlothincome(true);
                                break;
                            case SLOTHRESOURCES:
                                element.setSlothresources(true);
                                break;
                            case COLDINCOME:
                                element.setColdincome(true);
                                break;
                            case COLDSUPPLY:
                                element.setColdsupply(true);
                                break;
                            case MISFORTUNE:
                                element.setMisfortune(true);
                                break;
                            case LUCKEVENTS:
                                element.setLuckevents(true);
                                break;
                            case RESEARCHSCALE:
                                element.setResearchscale(true);
                                break;
                        }
                        element.setValue(Integer.valueOf(newName));
                        mods.add(0, element);
                    }
                });
            }
        });
    }

    private void setGeneral2(final General2 general2, final XtextEditor editor, final String newName) {
        try {
            Integer.parseInt(newName);
        } catch (NumberFormatException e) {
            return;
        }
        final IXtextDocument myDocument = editor.getDocument();
        myDocument.modify(new IUnitOfWork.Void<XtextResource>() {

            @Override
            public void process(XtextResource resource) throws Exception {
                Dom3Mod dom3Mod = (Dom3Mod) resource.getContents().get(0);
                for (AbstractElement abElement : dom3Mod.getElements()) {
                    if (abElement instanceof GeneralInst1) {
                        GeneralInst1 element = (GeneralInst1) abElement;
                        switch(general2) {
                            case POPPERGOLD:
                                if (element.isPoppergold()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case RESOURCEMULT:
                                if (element.isResourcemult()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case SUPPLYMULT:
                                if (element.isSupplymult()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case UNRESTHALFINC:
                                if (element.isUnresthalfinc()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case UNRESTHALFRES:
                                if (element.isUnresthalfres()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case EVENTISRARE:
                                if (element.isEventisrare()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case TURMOILINCOME:
                                if (element.isTurmoilincome()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case TURMOILEVENTS:
                                if (element.isTurmoilevents()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case DEATHINCOME:
                                if (element.isDeathincome()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case DEATHSUPPLY:
                                if (element.isDeathsupply()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case DEATHDEATH:
                                if (element.isDeathdeath()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case SLOTHINCOME:
                                if (element.isSlothincome()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case SLOTHRESOURCES:
                                if (element.isSlothresources()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case COLDINCOME:
                                if (element.isColdincome()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case COLDSUPPLY:
                                if (element.isColdsupply()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case MISFORTUNE:
                                if (element.isMisfortune()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case LUCKEVENTS:
                                if (element.isLuckevents()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                            case RESEARCHSCALE:
                                if (element.isResearchscale()) {
                                    element.setValue(Integer.parseInt(newName));
                                }
                                break;
                        }
                    }
                }
            }
        });
    }

    private void removeInst2(final General2 inst2, final XtextEditor editor) {
        final IXtextDocument myDocument = editor.getDocument();
        myDocument.modify(new IUnitOfWork.Void<XtextResource>() {

            @Override
            public void process(XtextResource resource) throws Exception {
                Dom3Mod dom3Mod = (Dom3Mod) resource.getContents().get(0);
                AbstractElement modToRemove = null;
                EList<AbstractElement> mods = dom3Mod.getElements();
                for (AbstractElement mod : mods) {
                    if (mod instanceof GeneralInst1) {
                        switch(inst2) {
                            case POPPERGOLD:
                                if (((GeneralInst1) mod).isPoppergold()) {
                                    modToRemove = mod;
                                }
                                break;
                            case RESOURCEMULT:
                                if (((GeneralInst1) mod).isResourcemult()) {
                                    modToRemove = mod;
                                }
                                break;
                            case SUPPLYMULT:
                                if (((GeneralInst1) mod).isSupplymult()) {
                                    modToRemove = mod;
                                }
                                break;
                            case UNRESTHALFINC:
                                if (((GeneralInst1) mod).isUnresthalfinc()) {
                                    modToRemove = mod;
                                }
                                break;
                            case UNRESTHALFRES:
                                if (((GeneralInst1) mod).isUnresthalfres()) {
                                    modToRemove = mod;
                                }
                                break;
                            case EVENTISRARE:
                                if (((GeneralInst1) mod).isEventisrare()) {
                                    modToRemove = mod;
                                }
                                break;
                            case TURMOILINCOME:
                                if (((GeneralInst1) mod).isTurmoilincome()) {
                                    modToRemove = mod;
                                }
                                break;
                            case TURMOILEVENTS:
                                if (((GeneralInst1) mod).isTurmoilevents()) {
                                    modToRemove = mod;
                                }
                                break;
                            case DEATHINCOME:
                                if (((GeneralInst1) mod).isDeathincome()) {
                                    modToRemove = mod;
                                }
                                break;
                            case DEATHSUPPLY:
                                if (((GeneralInst1) mod).isDeathsupply()) {
                                    modToRemove = mod;
                                }
                                break;
                            case DEATHDEATH:
                                if (((GeneralInst1) mod).isDeathdeath()) {
                                    modToRemove = mod;
                                }
                                break;
                            case SLOTHINCOME:
                                if (((GeneralInst1) mod).isSlothincome()) {
                                    modToRemove = mod;
                                }
                                break;
                            case SLOTHRESOURCES:
                                if (((GeneralInst1) mod).isSlothresources()) {
                                    modToRemove = mod;
                                }
                                break;
                            case COLDINCOME:
                                if (((GeneralInst1) mod).isColdincome()) {
                                    modToRemove = mod;
                                }
                                break;
                            case COLDSUPPLY:
                                if (((GeneralInst1) mod).isColdsupply()) {
                                    modToRemove = mod;
                                }
                                break;
                            case MISFORTUNE:
                                if (((GeneralInst1) mod).isMisfortune()) {
                                    modToRemove = mod;
                                }
                                break;
                            case LUCKEVENTS:
                                if (((GeneralInst1) mod).isLuckevents()) {
                                    modToRemove = mod;
                                }
                                break;
                            case RESEARCHSCALE:
                                if (((GeneralInst1) mod).isResearchscale()) {
                                    modToRemove = mod;
                                }
                                break;
                        }
                    }
                }
                if (modToRemove != null) {
                    mods.remove(modToRemove);
                }
            }
        });
    }

    public void update() {
        for (Map.Entry<General, GeneralFields> fields : generalMap.entrySet()) {
            String val = getGeneral(fields.getKey(), doc);
            if (val != null) {
                fields.getValue().value.setText(val);
            } else {
                fields.getValue().value.setText("");
            }
        }
        for (Map.Entry<General2, General2Fields> fields : general2Map.entrySet()) {
            Integer val = getGeneral2(fields.getKey(), doc);
            if (val != null) {
                fields.getValue().value.setText(val.toString());
                fields.getValue().value.setEnabled(true);
                fields.getValue().check.setSelection(true);
            } else {
                fields.getValue().value.setText("");
                fields.getValue().value.setEnabled(false);
                fields.getValue().check.setSelection(false);
            }
        }
    }
}
