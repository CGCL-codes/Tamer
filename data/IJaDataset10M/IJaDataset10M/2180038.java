package org.spbu.pldoctoolkit.dialogs;

import java.util.ArrayList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.spbu.pldoctoolkit.parser.DRLLang.LangElem;
import org.spbu.pldoctoolkit.refactor.Couple;
import org.spbu.pldoctoolkit.refactor.CreateDirEntry;
import org.spbu.pldoctoolkit.refactor.CreateDirTemplate;
import org.spbu.pldoctoolkit.refactor.InsertIntoDirectory;
import org.spbu.pldoctoolkit.refactor.ProjectContent;
import org.spbu.pldoctoolkit.refactor.Util;
import org.spbu.pldoctoolkit.refactor.pattern.AnyThing;
import org.spbu.pldoctoolkit.refactor.pattern.NessesaryElement;
import org.spbu.pldoctoolkit.refactor.pattern.Pattern;
import org.spbu.pldoctoolkit.refactor.pattern.PatternElement;

public class InsertIntoDirectoryDialog extends Dialog {

    private List dirList;

    private List templateList;

    private List entryList;

    private ArrayList<LangElem> templates;

    private ArrayList<LangElem> entrys;

    private Pattern templatePattern;

    private Pattern templateAndEntryPattern;

    private Pattern currentPattern;

    private StyledText selectedText;

    private Button matchWithTemplateAndEntryButton;

    private Button matchWithTemplateButton;

    private Button createEntryButton;

    private Button addAttrButton;

    private ProjectContent projectContent;

    private InsertIntoDirectory refact;

    private LangElem entry;

    private LangElem template;

    public InsertIntoDirectoryDialog(Shell parentShell, ProjectContent projectContent, InsertIntoDirectory refact) {
        super(parentShell);
        this.projectContent = projectContent;
        this.refact = refact;
        setBlockOnOpen(true);
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        Point size = new Point(600, 500);
        newShell.setSize(size);
        Shell p = getParentShell();
        newShell.setLocation(p.getLocation().x + p.getSize().x / 2 - size.x / 2, p.getLocation().y + p.getSize().y / 2 - size.y / 2);
        newShell.setText("Replace with dirRef...");
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setSize(this.getShell().getClientArea().width, this.getShell().getClientArea().height);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.verticalSpacing = 9;
        composite.setLayout(layout);
        Group dirGroup = new Group(composite, SWT.NONE);
        dirGroup.setLayout(new GridLayout(1, true));
        dirGroup.setText("Select directory");
        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        dirGroup.setLayoutData(gd);
        dirList = new List(dirGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        setDirListContent();
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        dirList.setLayoutData(gd);
        dirList.addSelectionListener(new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                dirSelected();
            }

            public void widgetSelected(SelectionEvent e) {
                dirSelected();
            }
        });
        Group entryGroup = new Group(composite, SWT.NONE);
        entryGroup.setLayout(new GridLayout(1, true));
        entryGroup.setText("Select entry");
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        entryGroup.setLayoutData(gd);
        entryList = new List(entryGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.MULTI);
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        entryList.setLayoutData(gd);
        entryList.addSelectionListener(new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                entrySelected();
            }

            public void widgetSelected(SelectionEvent e) {
                entrySelected();
            }
        });
        Group templateGroup = new Group(composite, SWT.NONE);
        templateGroup.setLayout(new GridLayout(1, true));
        templateGroup.setText("Select template");
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalSpan = 2;
        templateGroup.setLayoutData(gd);
        templateList = new List(templateGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        templateList.setLayoutData(gd);
        templateList.addSelectionListener(new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {
                templateSelected();
            }

            public void widgetSelected(SelectionEvent e) {
                templateSelected();
            }
        });
        Button newTemplateButton = new Button(templateGroup, SWT.PUSH | SWT.MULTI);
        newTemplateButton.setText("Create template");
        gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        gd.verticalAlignment = SWT.TOP;
        newTemplateButton.setLayoutData(gd);
        newTemplateButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                newTemplateButtonPressed();
            }
        });
        Composite textComposite = new Composite(composite, SWT.NONE);
        textComposite.setLayout(new GridLayout(2, false));
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalSpan = 2;
        textComposite.setLayoutData(gd);
        Label textLabel = new Label(textComposite, SWT.NONE);
        textLabel.setText("Text to be replaced:");
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalSpan = 2;
        textLabel.setLayoutData(gd);
        selectedText = new StyledText(textComposite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        selectedText.setText(refact.getText());
        selectedText.setEditable(false);
        selectedText.setBackground(new Color(Display.getCurrent(), 250, 250, 250));
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        selectedText.setLayoutData(gd);
        Composite buttonsComposite = new Composite(textComposite, SWT.NONE);
        buttonsComposite.setLayout(new GridLayout(1, false));
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        buttonsComposite.setLayoutData(gd);
        matchWithTemplateButton = new Button(buttonsComposite, SWT.PUSH | SWT.MULTI);
        matchWithTemplateButton.setText("Next match with\ntemplate");
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.TOP;
        matchWithTemplateButton.setLayoutData(gd);
        matchWithTemplateButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                matchWithTemplateButtonPressed();
            }
        });
        matchWithTemplateAndEntryButton = new Button(buttonsComposite, SWT.PUSH | SWT.MULTI);
        matchWithTemplateAndEntryButton.setText("Next match with\ntemplate and entry");
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.TOP;
        matchWithTemplateAndEntryButton.setLayoutData(gd);
        matchWithTemplateAndEntryButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                matchWithTemplateAndentryButtonPressed();
            }
        });
        createEntryButton = new Button(buttonsComposite, SWT.PUSH | SWT.MULTI);
        createEntryButton.setText("Create entry");
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.TOP;
        createEntryButton.setLayoutData(gd);
        createEntryButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                createEntryButtonPressed();
            }
        });
        addAttrButton = new Button(buttonsComposite, SWT.PUSH | SWT.MULTI);
        addAttrButton.setText("Add attrs to entry");
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.TOP;
        addAttrButton.setLayoutData(gd);
        addAttrButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addAttrButtonPressed();
            }
        });
        return composite;
    }

    protected void okPressed() {
        entry = entrys.get(entryList.getSelectionIndex());
        template = templates.get(templateList.getSelectionIndex());
        super.okPressed();
    }

    public LangElem getEntry() {
        return entry;
    }

    public LangElem getTemplate() {
        return template;
    }

    private void newTemplateButtonPressed() {
        CreateDirTemplate createTemplRefact = new CreateDirTemplate(refact.getFromText(), refact.getToText(), refact.getDoc(), refact.getTextDoc());
        CreateNewDirTamplateDialog dialog = new CreateNewDirTamplateDialog(getShell(), projectContent, createTemplRefact);
        if (dialog.open() == Window.OK) {
            createTemplRefact.perform(dialog.getText(), dialog.getTemplateId(), dialog.getDirectoryId());
            projectContent.saveAll();
            updateContent();
            refact.setParams(refact.getFromText(), refact.getToText(), projectContent.DRLDocs.get(refact.getDoc().file), refact.getTextDoc());
        }
    }

    private void addAttrButtonPressed() {
        LangElem entry = entrys.get(entryList.getSelectionIndex());
        CreateDirEntry.addAttrs(entry, getNewAttrs());
        projectContent.saveAll();
        updateContent();
        refact.setParams(refact.getFromText(), refact.getToText(), projectContent.DRLDocs.get(refact.getDoc().file), refact.getTextDoc());
    }

    private void createEntryButtonPressed() {
        final LangElem dir = projectContent.directories.get(dirList.getSelectionIndex());
        InputDialog dialog = new InputDialog(this.getShell(), "Create entry...", "Create entry.\nEnter id of new Entry", CreateDirEntry.getEntryId(dir), new IInputValidator() {

            public String isValid(String newText) {
                if (CreateDirEntry.isValidId(dir, newText)) return null; else return "Such id exists";
            }
        });
        if (dialog.open() == Window.OK) {
            CreateDirEntry.createEntry(dir, dialog.getValue(), getNewAttrs());
            projectContent.saveAll();
            updateContent();
            refact.setParams(refact.getFromText(), refact.getToText(), projectContent.DRLDocs.get(refact.getDoc().file), refact.getTextDoc());
        }
    }

    private ArrayList<Couple<String, String>> getNewAttrs() {
        ArrayList<Couple<String, String>> res = new ArrayList<Couple<String, String>>();
        for (PatternElement pe : currentPattern.elements) {
            if (pe instanceof AnyThing) {
                String id = pe.getLangElem().attrs.getValue(LangElem.ATTRID);
                res.add(new Couple<String, String>(id, pe.getText()));
            }
        }
        return res;
    }

    private void matchWithTemplateButtonPressed() {
        if (templatePattern.nextMatch()) {
            StyleRange ranges[] = new StyleRange[templatePattern.elements.size()];
            int i = 0;
            int count = templatePattern.elements.size();
            for (PatternElement pe : templatePattern.elements) {
                ranges[i] = new StyleRange();
                ranges[i].start = pe.getOffset();
                ranges[i].length = pe.getLength();
                if (pe instanceof NessesaryElement) {
                    ranges[i].background = new Color(Display.getCurrent(), 0, 127 + 127 * i / count, 127 + 127 * i / count);
                }
                i++;
            }
            selectedText.setStyleRanges(ranges);
            currentPattern = templatePattern;
        } else {
        }
    }

    private void matchWithTemplateAndentryButtonPressed() {
        if (templateAndEntryPattern.nextMatch()) {
            StyleRange ranges[] = new StyleRange[templateAndEntryPattern.elements.size()];
            int i = 0;
            int count = templatePattern.elements.size();
            for (PatternElement pe : templateAndEntryPattern.elements) {
                ranges[i] = new StyleRange();
                ranges[i].start = pe.getOffset();
                ranges[i].length = pe.getLength();
                if (pe instanceof NessesaryElement) {
                    ranges[i].background = ranges[i].background = new Color(Display.getCurrent(), 127 + 127 * i / count, 0, 0);
                }
                i++;
            }
            selectedText.setStyleRanges(ranges);
            currentPattern = templateAndEntryPattern;
        } else {
        }
    }

    private void templateSelected() {
        LangElem template = templates.get(templateList.getSelectionIndex());
        templatePattern = refact.createPatternFromTemplate(template);
        if (entryList.getSelectionIndex() != -1) changeTemplateAndEntryPattern();
    }

    private void entrySelected() {
        if (templateList.getSelectionIndex() != -1) changeTemplateAndEntryPattern();
    }

    private void changeTemplateAndEntryPattern() {
        LangElem template = templates.get(templateList.getSelectionIndex());
        LangElem entry = entrys.get(entryList.getSelectionIndex());
        templateAndEntryPattern = refact.createPatternFromTemplateAndEntry(template, entry);
    }

    private void setDirListContent() {
        dirList.removeAll();
        for (LangElem dir : projectContent.directories) {
            dirList.add(dir.attrs.getValue(LangElem.NAME) + " (id :" + dir.attrs.getValue(LangElem.ID) + ")");
        }
    }

    private void dirSelected() {
        int idx = dirList.getSelectionIndex();
        setDirectory(projectContent.directories.get(idx));
    }

    private void updateContent() {
        setDirListContent();
        templateList.removeAll();
        entryList.removeAll();
    }

    private void setDirectory(LangElem dir) {
        templates = Util.getTemplates(projectContent, dir);
        entrys = Util.getEntrys(dir);
        int idx = projectContent.directories.indexOf(dir);
        setTemplateListContent();
        setEntrysListContent();
    }

    private void setTemplateListContent() {
        templateList.removeAll();
        for (LangElem template : templates) {
            templateList.add(template.getTextRepresentation());
        }
    }

    private void setEntrysListContent() {
        entryList.removeAll();
        for (LangElem entry : entrys) {
            entryList.add(entry.getTextRepresentation());
        }
    }
}
