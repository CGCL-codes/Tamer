package org.eclipse.dltk.freemarker.internal.ui.editor.template;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Collections;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.freemarker.core.FreemarkerLanguageToolkit;
import org.eclipse.dltk.freemarker.core.manager.FreemarkerTemplateManager;
import org.eclipse.dltk.freemarker.core.settings.provider.InstanceProviderException;
import org.eclipse.dltk.freemarker.internal.ui.FreemarkerUIPluginMessages;
import org.eclipse.dltk.freemarker.internal.ui.outline.FreemarkerOutlinePage;
import org.eclipse.dltk.freemarker.internal.ui.text.IFreemarkerPartitions;
import org.eclipse.dltk.freemarker.ui.FreemarkerUIPlugin;
import org.eclipse.dltk.freemarker.ui.IFreemarkerUIPluginConstants;
import org.eclipse.dltk.freemarker.ui.actions.FreemarkerGenerateActionGroup;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.internal.ui.editor.ScriptOutlinePage;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import freemarker.provisionnal.template.ConfigurationProvider;
import freemarker.provisionnal.template.ModelProvider;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * FTL template editor based on DTLK {@link ScriptEditor} where user can type
 * FTL template.
 * 
 */
public class TemplateEditor extends ScriptEditor implements IFreemarkerUIPluginConstants, IFormPage {

    private static final String PREVIEW_FTL = "preview.ftl";

    public static final String EDITOR_CONTEXT = "#FreemarkerEditorContext";

    public static final String RULER_CONTEXT = "#FreemarkerRulerContext";

    private static final String ID = "template";

    private FormEditor editor;

    private int index;

    private String id;

    private Control control;

    public TemplateEditor(FormEditor editor) {
        super();
        this.editor = editor;
        this.id = ID;
    }

    public String getEditorId() {
        return FREEMARKER_EDITOR_ID;
    }

    public IDLTKLanguageToolkit getLanguageToolkit() {
        return FreemarkerLanguageToolkit.getDefault();
    }

    protected IPreferenceStore getScriptPreferenceStore() {
        return FreemarkerUIPlugin.getDefault().getPreferenceStore();
    }

    @Override
    public ScriptTextTools getTextTools() {
        return FreemarkerUIPlugin.getDefault().getTextTools();
    }

    protected void initializeEditor() {
        super.initializeEditor();
        setEditorContextMenuId(EDITOR_CONTEXT);
        setRulerContextMenuId(RULER_CONTEXT);
        setPartName(FreemarkerUIPluginMessages.FreemarkerEditor_TemplatePage_title);
    }

    protected ScriptOutlinePage doCreateOutlinePage() {
        return new FreemarkerOutlinePage(this, FreemarkerUIPlugin.getDefault().getPreferenceStore());
    }

    protected void connectPartitioningToElement(IEditorInput input, IDocument document) {
        if (document instanceof IDocumentExtension3) {
            IDocumentExtension3 extension = (IDocumentExtension3) document;
            if (extension.getDocumentPartitioner(IFreemarkerPartitions.FREEMARKER_PARTITIONING) == null) {
                FreemarkerDocumentSetupParticipant participant = new FreemarkerDocumentSetupParticipant();
                participant.setup(document);
            }
        }
    }

    /**
	 * Returns {@link IDocument} content of this editor.
	 * 
	 * @return
	 */
    private IDocument getDocument() {
        ISourceViewer viewer = getSourceViewer();
        if (viewer != null) {
            return viewer.getDocument();
        }
        return null;
    }

    @Override
    protected void initializeKeyBindingScopes() {
        setKeyBindingScopes(new String[] { "org.eclipse.dltk.ui.freemarkerEditorScope" });
    }

    @Override
    protected void createActions() {
        super.createActions();
        ActionGroup generateActions = new FreemarkerGenerateActionGroup(this, ITextEditorActionConstants.GROUP_EDIT);
        fActionGroups.addGroup(generateActions);
        fContextMenuGroup.addGroup(generateActions);
    }

    /**
	 * Merge the FM template (coming from the editor page) with Model and
	 * display result to the {@link Writer}.
	 * 
	 * <p>
	 * The writer doesn't contains the error (use
	 * {@link TemplateExceptionHandler#RETHROW_HANDLER} is used). When error
	 * occurs this method returns the exception.
	 * </p>
	 * 
	 * @param writer
	 * @throws IOException
	 */
    public Throwable preview(Writer writer) throws IOException {
        writer.write("");
        IDocument document = getDocument();
        if (document == null) return null;
        String templateName = getTemplateName();
        String pageContents = document.get();
        try {
            Reader reader = new StringReader(pageContents);
            Object model = getModel();
            FreemarkerTemplateManager.getManager().process(templateName, reader, getConfiguration(), model, writer, TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (Throwable e) {
            return e;
        } finally {
            writer.flush();
            writer.close();
        }
        return null;
    }

    private String getTemplateName() {
        IFile templateFile = (IFile) editor.getAdapter(IFile.class);
        if (templateFile != null) {
            return templateFile.getName();
        }
        return PREVIEW_FTL;
    }

    private void printFMError(Writer writer, Throwable e) {
        PrintWriter print = new PrintWriter(writer);
        e.printStackTrace(print);
    }

    private Configuration getConfiguration() throws InstanceProviderException {
        return ((ConfigurationProvider) editor.getAdapter(ConfigurationProvider.class)).getConfiguration();
    }

    private Object getModel() throws InstanceProviderException {
        Object model = getModelFromSettings();
        if (model != null) {
            return model;
        }
        return Collections.EMPTY_MAP;
    }

    private Object getModelFromSettings() throws InstanceProviderException {
        return ((ModelProvider) editor.getAdapter(ModelProvider.class)).getModel();
    }

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        Control[] children = parent.getChildren();
        control = children[children.length - 1];
    }

    /**
	 * By default, editor will be allowed to flip the page.
	 * 
	 * @return <code>true</code>
	 */
    public boolean canLeaveThePage() {
        return true;
    }

    /**
	 * Returns the parent editor.
	 * 
	 * @return parent editor instance
	 */
    public FormEditor getEditor() {
        return editor;
    }

    /**
	 * Returns the unique identifier that can be used to reference this page.
	 * 
	 * @return the unique page identifier
	 */
    public String getId() {
        return id;
    }

    /**
	 * Returns the managed form owned by this page.
	 * 
	 * @return the managed form
	 */
    public IManagedForm getManagedForm() {
        return null;
    }

    public Control getPartControl() {
        return control;
    }

    public void initialize(FormEditor editor) {
        this.editor = editor;
    }

    public boolean isActive() {
        return this.equals(editor.getActivePageInstance());
    }

    public boolean isEditor() {
        return true;
    }

    public boolean selectReveal(Object object) {
        if (object instanceof IMarker) {
            IDE.gotoMarker(this, (IMarker) object);
            return true;
        }
        return false;
    }

    public void setActive(boolean active) {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
