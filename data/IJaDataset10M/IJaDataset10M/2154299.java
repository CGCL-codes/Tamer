package com.schmeedy.relaxng.eclipse.ui.internal.commands;

import java.net.URI;
import java.util.Map;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.AbstractEnabledHandler;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.model.AbstractStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import com.schmeedy.relaxng.contentassist.IRngSchema;
import com.schmeedy.relaxng.contentassist.RngTools;
import com.schmeedy.relaxng.contentassist.IRngSchema.RngSchemaSyntax;
import com.schmeedy.relaxng.eclipse.core.internal.DefaultRngSchemaResolver;
import com.schmeedy.relaxng.eclipse.core.internal.RngConstants;
import com.schmeedy.relaxng.eclipse.core.internal.UriUtil;
import com.schmeedy.relaxng.eclipse.core.internal.binding.BindingUtils;

@SuppressWarnings("restriction")
public class ChangeSchemaAssociationCommandHandler extends AbstractEnabledHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        String changeType = event.getParameter("com.schmeedy.relaxng.eclipse.ui.changeSchemaAssoc.changeType");
        if (event.getApplicationContext() instanceof EvaluationContext) {
            EvaluationContext evaluationContext = (EvaluationContext) event.getApplicationContext();
            Object editor = evaluationContext.getVariable("activeEditor");
            Object editorInput = evaluationContext.getVariable("activeEditorInput");
            if (editor instanceof XMLMultiPageEditorPart && editorInput instanceof IFileEditorInput) {
                if ("new".equals(changeType) || "change".equals(changeType)) {
                    handleNewAssociation((XMLMultiPageEditorPart) editor, (IFileEditorInput) editorInput);
                } else if ("remove".equals(changeType)) {
                    handleRemoveAssociation((XMLMultiPageEditorPart) editor, (IFileEditorInput) editorInput);
                }
                return null;
            }
        }
        return null;
    }

    private void handleRemoveAssociation(XMLMultiPageEditorPart editor, IFileEditorInput currInput) {
        StructuredTextEditor textEditor = extractXmlTextEditor(editor);
        IDOMModel domModel = extractDomModel(textEditor, currInput);
        if (domModel == null) {
            return;
        }
        findAndDestroyObsoleteInstructions(domModel);
        if (DefaultRngSchemaResolver.INSTANCE.getSchema(domModel.getDocument()) == null) {
            ((AbstractStructuredModel) domModel).setContentTypeIdentifier(RngConstants.CONTENT_TYPE_XML);
            textEditor.update();
        }
    }

    private void handleNewAssociation(XMLMultiPageEditorPart editor, IFileEditorInput currInput) {
        StructuredTextEditor textEditor = extractXmlTextEditor(editor);
        IDOMModel domModel = extractDomModel(textEditor, currInput);
        if (domModel == null) {
            return;
        }
        String schemaUri = chooseSchemaFile();
        if (schemaUri == null) {
            return;
        }
        createProcessingInstruction(domModel, schemaUri);
        ((AbstractStructuredModel) domModel).setContentTypeIdentifier(RngConstants.CONTENT_TYPE_RELAX_DOCUMENT);
        textEditor.update();
    }

    private void findAndDestroyObsoleteInstructions(IDOMModel domModel) {
        try {
            domModel.aboutToChangeModel();
            IDOMDocument document = domModel.getDocument();
            removeObsoleteInstructions(document);
            RngTools.getInstance().getSchemaBinder().unBind(document);
        } finally {
            domModel.changedModel();
        }
    }

    private void createProcessingInstruction(IDOMModel domModel, String schemaUri) {
        try {
            domModel.aboutToChangeModel();
            IDOMDocument document = domModel.getDocument();
            removeObsoleteInstructions(document);
            insertProcessingInstruction(document, tryResolve(schemaUri));
            rebindSchema(document);
        } finally {
            domModel.changedModel();
        }
    }

    private void rebindSchema(Document document) {
        try {
            IRngSchema schema = DefaultRngSchemaResolver.INSTANCE.getSchema(document);
            if (schema != null) {
                RngTools.getInstance().getSchemaBinder().bind(document, schema);
            }
        } catch (Exception e) {
        }
    }

    private String tryResolve(String unresolvedUri) {
        URI resolved = UriUtil.resolveUri(unresolvedUri);
        if (resolved == null) {
            return unresolvedUri;
        }
        return resolved.toString();
    }

    private void insertProcessingInstruction(Document document, String schemaUri) {
        RngSchemaSyntax schemaSyntax = BindingUtils.guessSchemaSyntax(schemaUri);
        if (schemaSyntax == null) {
            return;
        }
        String instructionData = RngConstants.BINDING_INSTRUCTION_URI_PSEUDO_ATT_NAME + "=\"" + schemaUri + "\" " + RngConstants.BINDING_INSTRUCTION_TYPE_PSEUDO_ATT_NAME + "=\"" + BindingUtils.convertSchemaSyntaxToMimeType(schemaSyntax) + "\" " + RngConstants.BINDING_INSTRUCTION_MODE_PSEUDO_ATT_NAME + "=\"" + RngConstants.BINDING_INSTRUCTION_MODE_PSEUDO_ATT_VALUE + "\"";
        ProcessingInstruction pi = document.createProcessingInstruction(RngConstants.BINDING_INSTRUCTION_TARGET, instructionData);
        Node refChild = document.getFirstChild();
        if (refChild instanceof ProcessingInstruction && ((ProcessingInstruction) refChild).getTarget().equals("xml")) {
            refChild = refChild.getNextSibling();
            if (refChild instanceof Text) {
                refChild = refChild.getNextSibling();
            }
        }
        if (refChild == null) {
            document.appendChild(pi);
        } else {
            document.insertBefore(pi, refChild);
        }
        if (pi.getNextSibling() == null) {
            document.appendChild(document.createTextNode("\n"));
        } else if (!(pi.getNextSibling() instanceof Text)) {
            document.insertBefore(document.createTextNode("\n"), pi.getNextSibling());
        }
    }

    private void removeObsoleteInstructions(Document document) {
        NodeList docChildren = document.getChildNodes();
        for (int i = 0; i < docChildren.getLength(); i++) {
            Node item = docChildren.item(i);
            if (item instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) item;
                if (RngConstants.BINDING_INSTRUCTION_TARGET.equals(pi.getTarget())) {
                    Map<String, String> attributes = BindingUtils.parseBindingProcessingInstructionData(pi.getData());
                    if (attributes.containsKey(RngConstants.BINDING_INSTRUCTION_MODE_PSEUDO_ATT_NAME) && attributes.get(RngConstants.BINDING_INSTRUCTION_MODE_PSEUDO_ATT_NAME).equals(RngConstants.BINDING_INSTRUCTION_MODE_PSEUDO_ATT_VALUE)) {
                        document.removeChild(item);
                    }
                }
            } else if (item instanceof Element) {
                break;
            }
        }
    }

    private IDOMModel extractDomModel(StructuredTextEditor textEditor, IFileEditorInput currInput) {
        if (textEditor == null) {
            return null;
        }
        IDocument document = textEditor.getDocumentProvider().getDocument(currInput);
        if (document == null) {
            return null;
        }
        IStructuredModel model = StructuredModelManager.getModelManager().getExistingModelForEdit(document);
        if (model == null || !(model instanceof IDOMModel)) {
            return null;
        }
        return (IDOMModel) model;
    }

    private StructuredTextEditor extractXmlTextEditor(XMLMultiPageEditorPart editor) {
        IEditorPart[] editors = editor.findEditors(editor.getEditorInput());
        StructuredTextEditor textEditor = null;
        for (IEditorPart editorPart : editors) {
            if (editorPart instanceof StructuredTextEditor) {
                textEditor = (StructuredTextEditor) editorPart;
                break;
            }
        }
        return textEditor;
    }

    private String chooseSchemaFile() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        FileDialog dialog = new FileDialog(shell, SWT.SINGLE);
        String fileName = dialog.open();
        if (fileName == null) {
            return null;
        } else {
            return "file:/" + fileName;
        }
    }
}
