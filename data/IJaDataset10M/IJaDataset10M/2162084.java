package de.tud.inf.st.rubadoc.gui.marker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ISourceReference;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;
import de.tud.inf.st.rubadoc.model.DocumentationEntry;
import de.tud.inf.st.rubadoc.model.DocumentationOccurrence;
import de.tud.inf.st.rubadoc.model.DocumentationRepository;
import de.tud.inf.st.rubadoc.model.VariableBinding;
import de.tud.inf.st.rubadoc.util.LabelAndIconProvider;

public class RuBaDocMarkerSupport {

    public static final String MARKER_TYPE = "de.tud.inf.st.rubadoc.rubadocmarker";

    private BoundJavaElementsCounter counter = new BoundJavaElementsCounter();

    private static RuBaDocMarkerSupport instance;

    private RuBaDocMarkerSupport() {
    }

    public static RuBaDocMarkerSupport instance() {
        if (instance == null) {
            instance = new RuBaDocMarkerSupport();
        }
        return instance;
    }

    /**
	 * Adds markers to the editor. Each Java element (class, method, ...)
	 * that is matched by one of the documentation entries gets a marker.
	 * 
	 * If an element is matched multiple times, only one marker is created.
	 */
    public void addMarkers(final AbstractTextEditor editor) {
        ICompilationUnit cuFromEditor = getCompilationUnit(editor);
        for (IJavaElement element : counter) {
            if (belongsToCompilationUnit(element, cuFromEditor)) {
                addMarkerForElement(element);
            }
        }
    }

    /**
	 * Returns the (parsed) CompilationUnit of the Java file displayed
	 * by the text editor. If the editor is null or no Java editor,
	 * null is returned.
	 */
    private ICompilationUnit getCompilationUnit(AbstractTextEditor editor) {
        if (editor != null) {
            IEditorInput input = editor.getEditorInput();
            if (input instanceof FileEditorInput) {
                return getCompilationUnit((FileEditorInput) input);
            }
        }
        return null;
    }

    /**
	 * Returns the (parsed) CompilationUnit of the Java file represented
	 * by input
	 */
    private ICompilationUnit getCompilationUnit(FileEditorInput input) {
        IFile file = input.getFile();
        IJavaElement element = (IJavaElement) file.getAdapter(IJavaElement.class);
        ICompilationUnit cu = (ICompilationUnit) element.getAncestor(IJavaElement.COMPILATION_UNIT);
        return cu;
    }

    /**
	 * Checks if the IJavaElement has ICompilationUnit cu as ancestor node, i.e.
	 * if the element is part (child) of cu.
	 */
    private boolean belongsToCompilationUnit(IJavaElement element, ICompilationUnit cu) {
        ICompilationUnit cuOfElement = (ICompilationUnit) element.getAncestor(IJavaElement.COMPILATION_UNIT);
        return cuOfElement.equals(cu);
    }

    private Map<String, Object> createMarkerAttributesWithMessageForSingleComment(IJavaElement element) {
        Map<String, Object> attributes = createMarkerAttributesWithoutMessage(element);
        attributes.put(IMarker.MESSAGE, "There is a RuBaDoc comment for " + LabelAndIconProvider.getShortName(element));
        return attributes;
    }

    private Map<String, Object> createMarkerAttributesWithMessageForMultipleComments(IJavaElement element) {
        Map<String, Object> attributes = createMarkerAttributesWithoutMessage(element);
        attributes.put(IMarker.MESSAGE, "There are RuBaDoc comments for " + LabelAndIconProvider.getShortName(element));
        return attributes;
    }

    private Map<String, Object> createMarkerAttributesAccordingToOccurrenceCount(IJavaElement element) {
        Map<String, Object> markerAttributes = null;
        int occurrenceCount = counter.getCount(element);
        if (occurrenceCount == 1) {
            markerAttributes = createMarkerAttributesWithMessageForSingleComment(element);
        } else if (occurrenceCount > 1) {
            markerAttributes = createMarkerAttributesWithMessageForMultipleComments(element);
        }
        return markerAttributes;
    }

    private Map<String, Object> createMarkerAttributesWithoutMessage(IJavaElement element) {
        try {
            ISourceRange range = null;
            if (element instanceof IMember) {
                IMember member = (IMember) element;
                range = member.getNameRange();
            } else if (element instanceof ISourceReference) {
                ISourceReference ref = (ISourceReference) element;
                range = ref.getSourceRange();
            }
            if (range != null) {
                ICompilationUnit cu = (ICompilationUnit) element.getAncestor(IJavaElement.COMPILATION_UNIT);
                String sourceCode = cu.getSource();
                ILineTracker tracker = new DefaultLineTracker();
                tracker.set(sourceCode);
                int line = tracker.getLineNumberOfOffset(range.getOffset());
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(IMarker.LOCATION, element.getElementName());
                map.put(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
                map.put(IMarker.LINE_NUMBER, line);
                map.put(IMarker.CHAR_START, range.getOffset());
                map.put(IMarker.CHAR_END, range.getOffset() + range.getLength());
                map.put("elementhandle", element.getHandleIdentifier());
                return map;
            }
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
        return null;
    }

    private void addMarkerForElement(IJavaElement element) {
        Map<String, Object> markerAttributes = createMarkerAttributesAccordingToOccurrenceCount(element);
        if (markerAttributes != null) {
            try {
                MarkerUtilities.createMarker(element.getResource(), markerAttributes, MARKER_TYPE);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void initialize() {
        DocumentationRepository rep = DocumentationRepository.getInstance();
        for (DocumentationEntry entry : rep.getAllDocumentations()) {
            for (DocumentationOccurrence occ : entry.getOccurrences()) {
                for (VariableBinding vb : occ.getVariableBindings().values()) {
                    try {
                        IJavaElement element = vb.getBoundVariableAsJavaElement();
                        if (element != null) {
                            Map<String, Object> markerAttribs = createMarkerAttributesWithMessageForSingleComment(element);
                            if (markerAttribs != null) {
                                IMarker marker = element.getResource().createMarker(MARKER_TYPE);
                                marker.setAttributes(markerAttribs);
                            }
                        }
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public synchronized void deleteAllMarkers() {
        List<IProject> openProjects = getOpenProjects();
        for (IProject project : openProjects) {
            try {
                project.deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    private List<IProject> getOpenProjects() {
        IWorkspace root = ResourcesPlugin.getWorkspace();
        IProject[] projects = root.getRoot().getProjects();
        List<IProject> lProjects = new LinkedList<IProject>();
        for (IProject project : projects) {
            if (project.isOpen()) {
                lProjects.add(project);
            }
        }
        return lProjects;
    }
}
