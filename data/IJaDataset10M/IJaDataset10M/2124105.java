package org.omwg.ui.editors.text;

import java.util.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.swt.*;
import org.omwg.ui.models.*;
import org.omwg.versioning.VersionIdentifier;
import org.omwg.versioning.Versioning;
import org.omwg.ontology.Ontology;
import org.wsmo.common.TopEntity;
import org.wsmo.wsml.ParserException;

/**
 * @author Jan Henke, jan.henke@deri.org
 */
public class WsmlXmlEditor extends org.omwg.ui.editors.text.DomeTextEditor {

    public WsmlXmlEditor(TreeModel m) {
        super(m);
    }

    public void createPartControl(Composite parent) {
        viewer = new TextViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL);
        ((TextViewer) viewer).addTextListener(new ITextListener() {

            public void textChanged(TextEvent event) {
                if (!treeModel.dirty) {
                    treeModel.dirty = true;
                    setInput(getEditorInput());
                }
            }
        });
        viewer.setInput(this);
    }

    public void write() throws Exception {
        StringBuffer buffer = new StringBuffer();
        Vector temp = treeModel.dynamicOntologies;
        boolean dirty = treeModel.dirty;
        if (temp.isEmpty()) {
            ((TextViewer) viewer).setDocument(new Document(""));
        } else {
            treeModel.xmlSerializer.serialize(treeModel.dynamicOntologies.toArray(new TopEntity[treeModel.dynamicOntologies.size()]), buffer);
            ((TextViewer) viewer).setDocument(new Document(buffer.toString()));
        }
        treeModel.dirty = dirty;
        setInput(getEditorInput());
    }

    public void read() throws Exception {
        String doc = ((TextViewer) viewer).getDocument().get();
        StringBuffer buffer = new StringBuffer(doc);
        boolean committed;
        if (treeModel.dynamicOntologies.isEmpty()) {
            committed = false;
        } else {
            committed = Versioning.isCommitted((Ontology) treeModel.dynamicOntologies.get(0));
        }
        treeModel.dynamicOntologies = new Vector<Ontology>();
        treeModel.addOntology((Ontology) treeModel.xmlParser.parse(buffer)[0]);
        if (!committed && !treeModel.dynamicOntologies.isEmpty()) {
            Ontology ontology = ((Ontology) treeModel.dynamicOntologies.get(0));
            VersionIdentifier vi = Versioning.getVersionIdentifier(ontology);
            Versioning.setChanged(ontology, vi);
        }
    }

    public void doSave(IProgressMonitor monitor) {
        if (reallySave()) {
            try {
                read();
                write();
                treeModel.dirty = false;
                setInput(getEditorInput());
            } catch (ParserException e) {
                createHighlighting(e);
                String message = e.getLocalizedMessage();
                if (message == null) {
                    message = e.toString();
                }
                MessageDialog.openError(this.viewer.getControl().getShell(), e.getClass().getName(), message);
            } catch (com.ontotext.wsmo4j.parser.WrappedInvalidModelException e) {
                String message = e.getLocalizedMessage();
                if (message == null) {
                    message = e.toString();
                }
                e.printStackTrace();
                MessageDialog.openError(this.getSite().getShell(), e.getClass().getName(), message);
            } catch (IllegalArgumentException e) {
                String message = e.getLocalizedMessage();
                if (message == null) {
                    message = e.toString();
                }
                e.printStackTrace();
                MessageDialog.openError(this.getSite().getShell(), e.getClass().getName(), message);
            } catch (Exception e) {
                MessageDialog.openError(viewer.getControl().getShell(), e.getClass().getName(), e.getLocalizedMessage());
            }
        }
    }

    public void doSaveAs() {
        if (reallySave()) {
            SaveAsDialog dialog = new SaveAsDialog(this.getSite().getShell());
            dialog.setOriginalName(getEditorInput().getName());
            dialog.open();
            serialize(dialog.getResult().toString(), "");
        }
    }

    private void serialize(String path, String name) {
        try {
            treeModel.serialize(path.replaceAll(name, ""), name);
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = ex.getLocalizedMessage();
            if (message == null) {
                StackTraceElement[] elements = ex.getStackTrace();
                for (int i = 0; i < elements.length; i++) {
                    message += elements[i];
                }
            }
        }
        treeModel.dirty = false;
        setInput(getEditorInput());
    }

    private boolean reallySave() {
        boolean value = true;
        if (isDirty()) {
            value = MessageDialog.openQuestion(getSite().getShell(), "Saving uncommitted ontology", "Ontology \"" + "\" is not committed. Do you want to save without committing it?");
        }
        return value;
    }
}
