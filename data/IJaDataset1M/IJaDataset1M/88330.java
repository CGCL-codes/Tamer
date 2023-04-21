package com.nhncorp.cubridqa.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.DefaultUndoManager;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import com.nhncorp.cubridqa.Activator;
import com.nhncorp.cubridqa.model.Case;
import com.nhncorp.cubridqa.swtdesigner.ResourceManager;

/**
 * 
 * @ClassName: Constants
 * @deprecated
 * @date 2009-9-7
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class EditorView extends ViewPart {

    private Action executeAction;

    private SourceViewer sourceViewer;

    private StyledText styledText;

    private PersistDocument persistDoc;

    private boolean editable;

    private Case ca;

    private Configuration configration;

    private DefaultUndoManager undoManager;

    public static final String ID = "com.nhncorp.cubridqa.editor.EditorView";

    /**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout());
        persistDoc = new PersistDocument();
        sourceViewer = new SourceViewer(container, new VerticalRuler(10), SWT.V_SCROLL | SWT.H_SCROLL);
        sourceViewer.configure(configration);
        sourceViewer.setDocument(persistDoc);
        undoManager = new DefaultUndoManager(100);
        undoManager.connect(sourceViewer);
        StyledText styledText = sourceViewer.getTextWidget();
        styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        createActions();
        initializeToolBar();
        initializeMenu();
    }

    /**
	 * Create the actions
	 */
    private void createActions() {
        executeAction = new Action("Execute") {

            public void run() {
            }
        };
        executeAction.setHoverImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/run_tool.gif"));
    }

    /**
	 * Initialize the toolbar
	 */
    private void initializeToolBar() {
        IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
        toolbarManager.add(executeAction);
    }

    /**
	 * Initialize the menu
	 */
    private void initializeMenu() {
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
        menuManager.add(executeAction);
    }

    @Override
    public void setFocus() {
    }

    /**
	 * 
	 * @param ca
	 * @param readonly
	 */
    public void load(Case ca, boolean readonly) {
        this.ca = ca;
        setEditable(!readonly);
        persistDoc.open(ca.getName());
    }

    private void setEditable(boolean editable) {
        this.editable = editable;
        sourceViewer.setEditable(editable);
        if (ca != null) {
            String name = ca.getName();
            if (!editable) {
                this.setPartName("View - " + name.substring(name.lastIndexOf("/")));
            } else {
                this.setPartName("Edit - " + name.substring(name.lastIndexOf("/")));
            }
        }
    }
}
