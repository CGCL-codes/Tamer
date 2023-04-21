package com.metanology.mde.ui.pimExplorer;

import java.util.Enumeration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.TraverseEvent;
import com.metanology.mde.core.ui.plugin.MDEPlugin;
import org.eclipse.swt.graphics.Point;
import com.metanology.mde.core.metaModel.*;
import com.metanology.mde.core.ui.common.MetaClassCompare;
import com.metanology.mde.utils.Messages;
import com.metanology.mde.utils.QSortAlgorithm;
import java.util.Vector;

public class AssociationPropertiesForm extends MetaObjectForm {

    public AssociationPropertiesForm(Shell parentShell, int style) {
        super(parentShell);
        this.setShellStyle(style);
    }

    protected Group groupAssociation;

    public String[] populateClasses(MetaClass[] classesNotIncluded) {
        String[] classes;
        Vector v = new Vector();
        for (Enumeration e = MDEPlugin.getDefault().getRuntime().getModel().getMetaClasses().elements(); e.hasMoreElements(); ) {
            v.addElement(e.nextElement());
        }
        MetaClassCompare compare = new MetaClassCompare();
        new QSortAlgorithm(compare).sort(v);
        if (classesNotIncluded == null) {
            classes = new String[v.size()];
        } else {
            classes = new String[v.size() - classesNotIncluded.length];
        }
        for (int i = 0; i < v.size(); i++) {
            boolean includeFlag = true;
            MetaClass mc = (MetaClass) v.elementAt(i);
            if (classesNotIncluded != null) {
                for (int j = 0; j < classesNotIncluded.length; j++) {
                    if (mc.getObjId().equals(classesNotIncluded[j].getObjId())) {
                        includeFlag = false;
                        break;
                    }
                }
            }
            if (includeFlag) {
                classes[i] = mc.getName() + MDEPlugin.getResourceString("MetaClassDropdown.classSeparator") + mc.getVirtualPath(MDEPlugin.getResourceString("MetaClassDropdown.pathSeparator"));
            }
        }
        return classes;
    }

    private Listener classListener = new Listener() {

        public void handleEvent(Event e) {
            String className = associationClass.getText();
            MetaClass[] mclasses = null;
            Association association = getAssoc();
            if (association.getRoles() != null) {
                mclasses = new MetaClass[association.getRoles().size()];
                for (int i = 0; i < association.getRoles().size(); i++) {
                    mclasses[i] = association.getRoles().elementAt(i).getMetaClass();
                }
            }
            switch(e.type) {
                case SWT.Selection:
                    if (associationClass.getSelectionIndex() > -1) {
                        String toolTip = associationClass.getItem(associationClass.getSelectionIndex());
                        associationClass.setToolTipText(toolTip);
                    } else {
                        associationClass.setToolTipText(null);
                    }
                    break;
                case SWT.MouseDown:
                    if (associationClass.getItemCount() == 0) {
                        associationClass.setItems(populateClasses(mclasses));
                    }
                    if (className != null && className.length() > 0) {
                        for (int i = 0; i < associationClass.getItems().length; i++) {
                            if (className.equals(associationClass.getItem(i))) {
                                associationClass.select(i);
                            }
                        }
                    }
                    break;
                case SWT.KeyDown:
                    if (associationClass.getItemCount() == 0) {
                        associationClass.setItems(populateClasses(mclasses));
                    }
                    if (e.character != SWT.BS) {
                        if (className != null && className.length() > 0) {
                            for (int i = 0; i < associationClass.getItemCount(); i++) {
                                if (associationClass.getItem(i).toLowerCase().startsWith(className.toLowerCase())) {
                                    associationClass.setText(associationClass.getItem(i));
                                    org.eclipse.swt.graphics.Point p = new org.eclipse.swt.graphics.Point(className.length(), Combo.LIMIT);
                                    associationClass.setSelection(p);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    };

    /**
	 * find the MetaClass by given fully qualified class name
	 */
    protected MetaClass findMetaClassByFullQualifiedName(String fqcn) {
        if (fqcn == null || fqcn.length() <= 0) return null;
        String pathName = fqcn.replace('.', java.io.File.separator.toCharArray()[0]);
        return MDEPlugin.getDefault().getRuntime().getModelCache().getMetaClassByPath(pathName);
    }

    /**
	 * detect potential Role collision due to change of assignment of associated class
	 * Since 'association' is mutual, this detection needs to check in both directions
	 */
    protected AssociationRole getCollisionRole(AssociationRole role) {
        return null;
    }

    private AssociationRole theRole = null;

    /**
	 * set the given association role as role1 -- the anchor role, the related as role2
	 * and the association as the if calling setAssoc().
	 * Use this to issure the order of roles immediately after construction and before 
	 * populateUIControl() take place.
	 * No effect if the form is already constructed and populated.
	 */
    public void setAssociationRole(AssociationRole role1) {
        this.theRole = role1;
        this.setAssoc(role1.getAssociation());
    }

    protected void validateInput() {
        this.save.setEnabled(true);
        StringBuffer title = new StringBuffer();
        title.append(MDEPlugin.getResourceString(MSG_TITLE));
        title.append(" ");
        if (this.theRole != null) {
            title.append(theRole.getName() + "[" + theRole.getMetaClass().getName() + "] - " + theRole.getRelatedRole().getName() + "[" + theRole.getRelatedRole().getMetaClass().getName() + "]");
        }
        if (isReadOnly()) {
            title.append("[");
            title.append(Messages.MSG_READONLY);
            title.append("]");
            this.save.setEnabled(false);
        }
        this.getShell().setText(title.toString());
    }

    private static final int FORM_H = 700;

    private static final int FORM_W = 800;

    /**
     * Creates a new AssociationPropertiesForm.
     */
    public AssociationPropertiesForm(Shell parentShell) {
        super(parentShell);
        this.setShellStyle(SWT.CLOSE | SWT.MIN | SWT.RESIZE);
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(MDEPlugin.getResourceString(MSG_TITLE));
        Rectangle rect = newShell.getBounds();
        rect.height = FORM_H;
        rect.width = FORM_W;
        Rectangle dis_rect = MDEPlugin.getShell().getBounds();
        rect.x = dis_rect.x + Math.max(0, dis_rect.width / 2 - rect.width / 2);
        rect.y = dis_rect.y + Math.max(0, dis_rect.height / 2 - rect.height / 2);
        newShell.setBounds(rect);
    }

    void handleSave() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

            public void run() {
                try {
                    doSave();
                } finally {
                    close();
                }
            }
        });
    }

    void doSave() {
        this.populateServerObjects();
        save(this.assoc, "Association");
        this.close();
    }

    void handleCancel() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

            public void run() {
                try {
                    doCancel();
                } finally {
                    close();
                }
            }
        });
    }

    void doCancel() {
        MDEPlugin.getDefault().getRuntime().getModelCache().refresh(assoc);
    }

    protected Control createContents(Composite parent) {
        Composite composite = (Composite) super.createContents(parent);
        this.init(composite);
        this.doLayout(composite);
        this.populateUIControls();
        parent.pack(true);
        Rectangle r = parent.getBounds();
        r.height += 10;
        parent.setBounds(r);
        return composite;
    }

    private void registerListener() {
        Listener visibilityKeyListener = new Listener() {

            String oldTxt = null;

            public void handleEvent(Event e) {
                String txt = null;
                if ((visibility.getStyle() & SWT.READ_ONLY) != 0) {
                    txt = String.valueOf(e.character);
                } else {
                    txt = visibility.getText();
                }
                if (e.type == SWT.KeyDown) {
                    if (e.character != SWT.BS && e.keyCode != SWT.SHIFT && e.keyCode != SWT.ARROW_LEFT && e.keyCode != SWT.ARROW_RIGHT && e.keyCode != SWT.ARROW_DOWN && e.keyCode != SWT.ARROW_UP && e.keyCode != SWT.HOME && e.keyCode != SWT.END) {
                        if (txt != null && txt.length() > 0) {
                            int selected = 0;
                            if (oldTxt != null) {
                                selected = visibility.indexOf(oldTxt);
                            }
                            boolean found = false;
                            for (int i = selected + 1; i < visibility.getItemCount() && !found; i++) {
                                found = matchSelection(i, txt, visibility);
                            }
                            for (int i = 0; i <= selected && !found; i++) {
                                found = matchSelection(i, txt, visibility);
                            }
                        }
                    }
                }
            }

            boolean matchSelection(int i, String txt, Combo c) {
                if (c.getItem(i).toLowerCase().startsWith(txt.toLowerCase())) {
                    c.select(i);
                    oldTxt = c.getItem(i);
                    org.eclipse.swt.graphics.Point p;
                    if ((c.getStyle() & SWT.READ_ONLY) != 0) {
                        p = new org.eclipse.swt.graphics.Point(0, org.eclipse.swt.widgets.Combo.LIMIT);
                    } else {
                        p = new org.eclipse.swt.graphics.Point(txt.length(), org.eclipse.swt.widgets.Combo.LIMIT);
                    }
                    c.setSelection(p);
                    return true;
                }
                return false;
            }
        };
        visibility.addListener(SWT.KeyDown, visibilityKeyListener);
        Listener associationClassKeyListener = new Listener() {

            String oldTxt = null;

            public void handleEvent(Event e) {
                String txt = null;
                if ((associationClass.getStyle() & SWT.READ_ONLY) != 0) {
                    txt = String.valueOf(e.character);
                } else {
                    txt = associationClass.getText();
                }
                if (e.type == SWT.KeyDown) {
                    if (e.character != SWT.BS && e.keyCode != SWT.SHIFT && e.keyCode != SWT.ARROW_LEFT && e.keyCode != SWT.ARROW_RIGHT && e.keyCode != SWT.ARROW_DOWN && e.keyCode != SWT.ARROW_UP && e.keyCode != SWT.HOME && e.keyCode != SWT.END) {
                        if (txt != null && txt.length() > 0) {
                            int selected = 0;
                            if (oldTxt != null) {
                                selected = associationClass.indexOf(oldTxt);
                            }
                            boolean found = false;
                            for (int i = selected + 1; i < associationClass.getItemCount() && !found; i++) {
                                found = matchSelection(i, txt, associationClass);
                            }
                            for (int i = 0; i <= selected && !found; i++) {
                                found = matchSelection(i, txt, associationClass);
                            }
                        }
                    }
                }
            }

            boolean matchSelection(int i, String txt, Combo c) {
                if (c.getItem(i).toLowerCase().startsWith(txt.toLowerCase())) {
                    c.select(i);
                    oldTxt = c.getItem(i);
                    org.eclipse.swt.graphics.Point p;
                    if ((c.getStyle() & SWT.READ_ONLY) != 0) {
                        p = new org.eclipse.swt.graphics.Point(0, org.eclipse.swt.widgets.Combo.LIMIT);
                    } else {
                        p = new org.eclipse.swt.graphics.Point(txt.length(), org.eclipse.swt.widgets.Combo.LIMIT);
                    }
                    c.setSelection(p);
                    return true;
                }
                return false;
            }
        };
        associationClass.addListener(SWT.KeyDown, associationClassKeyListener);
        this.save.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                handleSave();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                handleSave();
            }
        });
        this.getShell().setDefaultButton(this.save);
        this.cancel.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                handleCancel();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                handleCancel();
            }
        });
        this.getShell().addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                    e.doit = true;
                }
            }
        });
    }

    protected void init(Composite composite) {
        groupAssociation = new Group(composite, SWT.SIMPLE);
        groupAssociation.setText(MDEPlugin.getResourceString("AssociationPropertiesForm.groupLabel"));
        super.init(groupAssociation);
        labelVisibility = new Label(groupAssociation, SWT.RIGHT);
        labelVisibility.setText(MDEPlugin.getResourceString(AssociationPropertiesForm.MSG_VISIBILITY));
        visibility = new Combo(groupAssociation, SWT.DROP_DOWN | SWT.READ_ONLY);
        labelAssociationClass = new Label(groupAssociation, SWT.RIGHT);
        labelAssociationClass.setText(MDEPlugin.getResourceString(AssociationPropertiesForm.MSG_ASSOCIATIONCLASS));
        associationClass = new Combo(groupAssociation, SWT.DROP_DOWN);
        associationClass.addListener(SWT.Selection, classListener);
        associationClass.addListener(SWT.KeyDown, classListener);
        associationClass.addListener(SWT.MouseDown, classListener);
        tagEntrysPanel = new Composite(groupAssociation, SWT.NONE);
        tagEntrysPanel.setLayout(new GridLayout());
        tagEntrys = new TagEntryGrid(tagEntrysPanel);
        role1Panel = new Composite(composite, SWT.NONE);
        role1Panel.setLayout(new GridLayout());
        role1 = new AssociationRolePropertiesForm(role1Panel);
        role1.groupRole.setText(MDEPlugin.getResourceString(AssociationRolePropertiesForm.MSG_TITLE));
        role2Panel = new Composite(composite, SWT.NONE);
        role2Panel.setLayout(new GridLayout());
        role2 = new AssociationRolePropertiesForm(role2Panel);
        role2.groupRole.setText(MDEPlugin.getResourceString(AssociationRolePropertiesForm.MSG_TITLE2));
        save = new Button(composite, SWT.PUSH);
        save.setText(MDEPlugin.getResourceString(AssociationPropertiesForm.MSG_SAVE));
        cancel = new Button(composite, SWT.PUSH);
        cancel.setText(MDEPlugin.getResourceString(AssociationPropertiesForm.MSG_CANCEL));
        tagEntrys.setGridColumnWidth(TagEntryGrid.COL_NAME, 147);
        tagEntrys.setGridColumnWidth(TagEntryGrid.COL_VALUE, 147);
        this.registerListener();
    }

    protected void doLayout(Composite composite) {
        int labelWidth = 0;
        Point lp = labelAssociationClass.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        labelWidth = lp.x;
        lp = this.labelDescription.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        if (lp.x > labelWidth) labelWidth = lp.x;
        lp = this.labelName.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        if (lp.x > labelWidth) labelWidth = lp.x;
        lp = this.labelStereotype.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        if (lp.x > labelWidth) labelWidth = lp.x;
        lp = this.labelVisibility.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        if (lp.x > labelWidth) labelWidth = lp.x;
        FormLayout layout = new FormLayout();
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        FormLayout groupLayout = new FormLayout();
        groupAssociation.setLayout(groupLayout);
        FormData data = null;
        data = new FormData();
        data.top = new FormAttachment(0, 2);
        data.left = new FormAttachment(0, 2);
        data.width = labelWidth;
        labelName.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(0, 2);
        data.left = new FormAttachment(labelName, 10);
        data.right = new FormAttachment(60, -5);
        name.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(name, 3);
        data.left = new FormAttachment(0, 2);
        data.width = labelWidth;
        labelStereotype.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(name, 3);
        data.left = new FormAttachment(labelStereotype, 10);
        data.right = new FormAttachment(60, -5);
        stereotype.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(stereotype, 3);
        data.left = new FormAttachment(0, 2);
        data.width = labelWidth;
        labelDescription.setLayoutData(data);
        data = new FormData(10, 20);
        data.top = new FormAttachment(stereotype, 3);
        data.left = new FormAttachment(labelDescription, 10);
        data.right = new FormAttachment(60, -5);
        description.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(description, 3);
        data.left = new FormAttachment(0, 2);
        data.width = labelWidth;
        labelVisibility.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(description, 3);
        data.left = new FormAttachment(labelVisibility, 10);
        data.right = new FormAttachment(60, -5);
        visibility.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(visibility, 3);
        data.left = new FormAttachment(0, 2);
        data.width = labelWidth;
        labelAssociationClass.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(visibility, 3);
        data.left = new FormAttachment(labelAssociationClass, 10);
        data.right = new FormAttachment(60, -5);
        associationClass.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(0, 2);
        data.left = new FormAttachment(associationClass, 2);
        data.right = new FormAttachment(100, -2);
        data.bottom = new FormAttachment(100, -2);
        tagEntrysPanel.setLayoutData(data);
        groupAssociation.pack();
        Point p = groupAssociation.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        data = new FormData();
        data.top = new FormAttachment(0, 10);
        data.left = new FormAttachment(0, 10);
        data.right = new FormAttachment(100, -10);
        data.height = p.y;
        groupAssociation.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(groupAssociation, 0);
        data.left = new FormAttachment(0, 5);
        data.right = new FormAttachment(100, -5);
        role1Panel.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(role1Panel, 0);
        data.left = new FormAttachment(0, 5);
        data.right = new FormAttachment(100, -5);
        role2Panel.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(role2Panel, +10);
        data.right = new FormAttachment(100, -10);
        cancel.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(role2Panel, +10);
        data.right = new FormAttachment(cancel, -10);
        save.setLayoutData(data);
    }

    /** 
     * Populate all the ui controls
     * It will be invoked by createContents / createDialogArea
     */
    public void populateUIControls() {
        super.populateUIControls();
        if (this.assoc == null) return;
        visibility.setItems(new String[] { MDEPlugin.getResourceString("ScopeEnum.PUBLIC"), MDEPlugin.getResourceString("ScopeEnum.PROTECTED"), MDEPlugin.getResourceString("ScopeEnum.PRIVATE"), MDEPlugin.getResourceString("ScopeEnum.IMPLEMENTATION") });
        if (this.assoc.getScope() > 0) {
            visibility.select(ScopeEnum.indexOf(this.assoc.getScope()));
        }
        if (this.assoc.getLinkClass() != null) {
            associationClass.setText(this.assoc.getLinkClass().getName());
        }
        if (this.assoc != null) {
            tagEntrys.setMetaObject(this.assoc);
        }
        tagEntrys.populateUIControls();
        if (this.theRole == null) {
            this.theRole = (AssociationRole) assoc.getRoles().elementAt(0);
        }
        role1.setRole(this.theRole);
        role1.populateUIControls();
        if (this.theRole != null) {
            role2.setRole(this.theRole.getRelatedRole());
        } else {
            role2.setRole((AssociationRole) assoc.getRoles().elementAt(1));
        }
        role2.populateUIControls();
        this.name.setText(this.assoc.getName());
        this.name.setFocus();
        this.stereotype.setText(this.assoc.getStereotype());
        this.description.setText(this.assoc.getDescription());
        role1.className.setEnabled(false);
        role2.className.setEnabled(false);
        this.associationClass.setEnabled(false);
    }

    /** 
     * Populate all the objects providing data
     * for the UI controls
     * It should be invoked by user defined action 
     * implementation methods.
     */
    public void populateServerObjects() {
        super.populateServerObjects();
        if (visibility.getSelectionIndex() >= 0) {
            this.assoc.setScope(ScopeEnum.valueOf(visibility.getSelectionIndex()));
        }
        tagEntrys.populateServerObjects();
        this.assoc.getTags().removeAllElements();
        TagCollection tags = tagEntrys.getTags();
        if (tags != null) {
            for (Enumeration e = tags.elements(); e.hasMoreElements(); ) {
                this.assoc.getTags().addElement((Tag) e.nextElement());
            }
        }
        if (role1 != null) role1.populateServerObjects();
        if (role2 != null) role2.populateServerObjects();
        this.assoc.setName(this.name.getText());
        this.assoc.setStereotype(this.stereotype.getText());
        this.assoc.setDescription(this.description.getText());
    }

    public TagEntryGrid getTagEntrys() {
        return tagEntrys;
    }

    public AssociationRolePropertiesForm getRole1() {
        return role1;
    }

    public AssociationRolePropertiesForm getRole2() {
        return role2;
    }

    public com.metanology.mde.core.metaModel.Association getAssoc() {
        if (assoc == null) {
            assoc = new com.metanology.mde.core.metaModel.Association();
        }
        return assoc;
    }

    public void setAssoc(com.metanology.mde.core.metaModel.Association val) {
        assoc = val;
        this.setMobj(val);
    }

    protected Label labelVisibility;

    protected Combo visibility;

    protected Label labelAssociationClass;

    protected Combo associationClass;

    protected Button save;

    protected Button cancel;

    protected TagEntryGrid tagEntrys;

    protected Composite tagEntrysPanel;

    protected AssociationRolePropertiesForm role1;

    protected Composite role1Panel;

    protected AssociationRolePropertiesForm role2;

    protected Composite role2Panel;

    private com.metanology.mde.core.metaModel.Association assoc;

    public static final String MSG_TITLE = "AssociationPropertiesForm.title";

    public static final String MSG_VISIBILITY = "AssociationPropertiesForm.visibility";

    public static final String MSG_ASSOCIATIONCLASS = "AssociationPropertiesForm.associationClass";

    public static final String MSG_SAVE = "AssociationPropertiesForm.save";

    public static final String MSG_CANCEL = "AssociationPropertiesForm.cancel";
}
