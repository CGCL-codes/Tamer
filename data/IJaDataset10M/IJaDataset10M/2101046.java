package net.sf.dalutils4j.cg.eclipse;

import net.sf.dalutils4j.cg.vo.CodeGeneratorProperties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.ide.ResourceUtil;

public class Editor2 extends FormEditor implements IEditor2 {

    private IProject project;

    protected boolean dirty = false;

    private DebugHelper debugHelper = new DebugHelper();

    private EditorPageDTO editorPageDTO;

    private EditorPageDAO editorPageDAO;

    private EditorPageConfiguration editorPageConfiguration;

    private EditorPageAbout editorPageAbout;

    public Editor2() {
    }

    @Override
    protected void addPages() {
        editorPageDTO = new EditorPageDTO(getContainer(), SWT.NONE);
        int index = addPage(editorPageDTO);
        setPageText(index, "DTO");
        editorPageDAO = new EditorPageDAO(getContainer(), SWT.NONE);
        index = addPage(editorPageDAO);
        setPageText(index, "DAO");
        editorPageConfiguration = new EditorPageConfiguration(getContainer(), SWT.NONE);
        index = addPage(editorPageConfiguration);
        setPageText(index, "Configuration");
        editorPageAbout = new EditorPageAbout(getContainer(), SWT.NONE);
        index = addPage(editorPageAbout);
        setPageText(index, "About");
        editorPageDTO.setDebugHelper(debugHelper);
        editorPageDTO.setProject(project);
        editorPageDAO.setDebugHelper(debugHelper);
        editorPageDAO.setProject(project);
        editorPageConfiguration.setProject(project);
        editorPageConfiguration.setEditor(this);
        boolean res = editorPageConfiguration.loadUI();
        if (res) {
            if (tryEnableCodeGeneration()) {
                res = true;
            }
        }
        if (res == false) {
            disableCodeGeneration();
        }
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        doSave();
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
        if (!(editorInput instanceof IFileEditorInput)) {
            throw new PartInitException("Invalid input: must be IFileEditorInput");
        }
        IFile inputFile = ResourceUtil.getFile(editorInput);
        IPath p = inputFile.getFullPath();
        if (p.segmentCount() != 2) {
            throw new PartInitException("Invalid file: must be [project]/" + CodeGeneratorProperties.FILENAME);
        }
        if (p.lastSegment().compareTo(CodeGeneratorProperties.FILENAME) != 0) {
            throw new PartInitException("Invalid file: must be [project]/" + CodeGeneratorProperties.FILENAME);
        }
        project = inputFile.getProject();
        setSite(site);
        setInput(editorInput);
        setPartName(project.getName());
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void update() {
        if (dirty) {
            disableCodeGeneration();
        } else {
            tryEnableCodeGeneration();
        }
    }

    @Override
    public void doSave() {
        boolean res = editorPageConfiguration.save();
        if (res) {
            tryEnableCodeGeneration();
            setDirty(false);
        }
    }

    @Override
    public void setDirty(boolean value) {
        dirty = value;
        update();
        firePropertyChange(PROP_DIRTY);
    }

    private boolean tryEnableCodeGeneration() {
        if (editorPageConfiguration.validateAll()) {
            editorPageDTO.reloadTable(false);
            editorPageDAO.reloadTable();
            setCodeGenerationEnabled(true);
            return true;
        } else {
            setCodeGenerationEnabled(false);
            return false;
        }
    }

    private void disableCodeGeneration() {
        setCodeGenerationEnabled(false);
    }

    private void setCodeGenerationEnabled(boolean enabled) {
        if (enabled == false) {
            activateConfigTab();
        }
    }

    private void activateConfigTab() {
        Composite container = getContainer();
        if (container instanceof CTabFolder) {
            CTabFolder tabs = ((CTabFolder) container);
            int oldSelection = tabs.getSelectionIndex();
            if (oldSelection != 2) {
                tabs.setSelection(2);
            }
        }
    }
}
