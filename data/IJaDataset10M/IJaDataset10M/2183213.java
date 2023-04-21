package net.sf.thrift4eclipse.editor;

import java.util.HashMap;
import java.util.List;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;

public class ThriftEditor extends TextEditor {

    private DisplayManager displayManager;

    private ProjectionSupport projectionSupport;

    private ProjectionAnnotationModel annotationModel;

    private Annotation[] oldAnnotations;

    public ThriftEditor() {
        displayManager = new DisplayManager();
        setSourceViewerConfiguration(new ThriftConfiguration(this, displayManager));
        setDocumentProvider(new ThriftDocumentProvider());
    }

    public void dispose() {
        displayManager.dispose();
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

    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
        getSourceViewerDecorationSupport(viewer);
        return viewer;
    }

    public void updateFoldingStructure(List<Position> positions) {
        Annotation[] annotations = new Annotation[positions.size()];
        HashMap<Annotation, Position> newAnnotations = new HashMap<Annotation, Position>();
        int i = 0;
        for (Position pos : positions) {
            ProjectionAnnotation annotation = new ProjectionAnnotation();
            newAnnotations.put(annotation, pos);
            annotations[i++] = annotation;
        }
        annotationModel.modifyAnnotations(oldAnnotations, newAnnotations, null);
        oldAnnotations = annotations;
    }
}
