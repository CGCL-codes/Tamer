package org.wsmostudio.grounding.sawsdl.ui.text;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.text.source.*;
import org.eclipse.jface.text.source.projection.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;

public class SAWSDLTextEditor extends TextEditor {

    private ProjectionSupport projectionSupport;

    private ColorManager colorManager;

    public SAWSDLTextEditor() {
        super();
        colorManager = new ColorManager();
        setSourceViewerConfiguration(new XMLConfiguration(colorManager, this));
        setDocumentProvider(new XMLDocumentProvider());
    }

    public void doUpdateView() {
        ((XMLConfiguration) getSourceViewerConfiguration()).updateColors();
        this.getSourceViewer().invalidateTextPresentation();
    }

    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }

    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
        projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
        projectionSupport.install();
        viewer.doOperation(ProjectionViewer.TOGGLE);
        annotationModel = viewer.getProjectionAnnotationModel();
    }

    private Annotation[] oldAnnotations;

    private ProjectionAnnotationModel annotationModel;

    @SuppressWarnings("unchecked")
    public void updateFoldingStructure(ArrayList positions) {
        Annotation[] annotations = new Annotation[positions.size()];
        HashMap newAnnotations = new HashMap();
        for (int i = 0; i < positions.size(); i++) {
            ProjectionAnnotation annotation = new ProjectionAnnotation();
            newAnnotations.put(annotation, positions.get(i));
            annotations[i] = annotation;
        }
        annotationModel.modifyAnnotations(oldAnnotations, newAnnotations, null);
        oldAnnotations = annotations;
    }

    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
        getSourceViewerDecorationSupport(viewer);
        return viewer;
    }
}
