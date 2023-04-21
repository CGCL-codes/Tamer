package org.spbu.pldoctoolkit.actions;

import java.io.StringReader;
import java.net.URL;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.spbu.pldoctoolkit.PLDocToolkitPlugin;
import org.spbu.pldoctoolkit.cache.SchemaCache;
import org.spbu.pldoctoolkit.dialogs.SelectIntoInfElemDialog;
import org.spbu.pldoctoolkit.editors.DrlTextEditor;
import org.spbu.pldoctoolkit.parser.DRLLang.DRLDocument;
import org.spbu.pldoctoolkit.parser.DRLLang.LangElem;
import org.spbu.pldoctoolkit.refactor.PositionInText;
import org.spbu.pldoctoolkit.refactor.ProjectContent;
import org.spbu.pldoctoolkit.refactor.SelectIntoInfElem;
import org.spbu.pldoctoolkit.registry.ProjectRegistryImpl;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.thaiopensource.validate.Validator;
import com.thaiopensource.xml.sax.Jaxp11XMLReaderCreator;

public class SelectIntoInfElementAction extends Action implements IValidateDRLSelection {

    IEditorPart editor;

    TextEditor te;

    IProject project;

    SelectIntoInfElem refact = new SelectIntoInfElem();

    ProjectContent projectContent;

    FileEditorInput editorInput;

    public static final String refactName = "Extract InfElement...";

    public SelectIntoInfElementAction(IEditorPart editor) throws Exception {
        super(refactName);
        this.editor = editor;
        te = (TextEditor) editor;
        validator = SCHEMA_CACHE.getValidator(SCHEMA_URL, errorHandler);
        xmlReader = new Jaxp11XMLReaderCreator().createXMLReader();
        xmlReader.setErrorHandler(errorHandler);
        editorInput = (FileEditorInput) editor.getEditorInput();
        project = editorInput.getFile().getProject();
        projectContent = (ProjectContent) ((ProjectRegistryImpl) PLDocToolkitPlugin.getRegistry(project.getName())).projectContent;
        ((DrlTextEditor) te).getMenuListener().addListener(this);
    }

    public void validateSelection(IEditorPart part) {
        ISelection sel = te.getSelectionProvider().getSelection();
        TextSelection ts = (TextSelection) sel;
        IDocument doc = te.getDocumentProvider().getDocument(editorInput);
        DRLDocument DRLdoc = projectContent.DRLDocs.get(editorInput.getFile());
        try {
            int line1 = ts.getStartLine();
            int column1 = ts.getOffset() - doc.getLineOffset(line1);
            int line2 = ts.getEndLine();
            int column2 = ts.getOffset() + ts.getLength() - doc.getLineOffset(line2);
            PositionInText pos1 = new PositionInText(line1 + 1, column1 + 1);
            PositionInText pos2 = new PositionInText(line2 + 1, column2 + 1);
            if (pos1.compare(pos2) == 0) {
                setEnabled(false);
                return;
            }
            refact.reset();
            refact.setValidationPararams(projectContent, DRLdoc, pos1, pos2);
            boolean res = refact.validate();
            setEnabled(res);
        } catch (Exception e) {
            setEnabled(false);
            e.printStackTrace();
        }
    }

    public void run() {
        SelectIntoInfElemDialog dialog = refact.createSelectIntoInfElemDialog(editor, projectContent);
        int res = dialog.open();
        if (res != Window.OK) return;
        IDocument doc = te.getDocumentProvider().getDocument(editorInput);
        ISelection sel = te.getSelectionProvider().getSelection();
        TextSelection ts = (TextSelection) sel;
        try {
            DRLDocument DRLdoc = projectContent.DRLDocs.get(editorInput.getFile());
            int line1 = ts.getStartLine();
            int column1 = ts.getOffset() - doc.getLineOffset(line1);
            int line2 = ts.getEndLine();
            int column2 = ts.getOffset() + ts.getLength() - doc.getLineOffset(line2);
            refact.setPararams(dialog.getInfElemId(), dialog.getInfElemName(), dialog.getInfElemRefId(), projectContent, DRLdoc, new PositionInText(line1 + 1, column1 + 1), new PositionInText(line2 + 1, column2 + 1));
            refact.perform();
            projectContent.saveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final SchemaCache SCHEMA_CACHE = new SchemaCache();

    private static final URL SCHEMA_URL = PLDocToolkitPlugin.getURL("schema/SelectInfElemCorrect.rng");

    private final MyErrorHandler errorHandler = new MyErrorHandler();

    private XMLReader xmlReader;

    private final Validator validator;

    public boolean validate(String what) {
        try {
            validator.reset();
            IFileEditorInput editorInput = (IFileEditorInput) editor.getEditorInput();
            final IFile editorFile = editorInput.getFile();
            editorFile.deleteMarkers(IMarker.MARKER, true, IResource.DEPTH_ZERO);
            xmlReader.setContentHandler(validator.getContentHandler());
            DTDHandler dtdHandler = validator.getDTDHandler();
            if (dtdHandler != null) xmlReader.setDTDHandler(dtdHandler);
            xmlReader.parse(new InputSource(new StringReader(what)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
