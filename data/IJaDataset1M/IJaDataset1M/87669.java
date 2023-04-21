package org.eclipse.xcde.backend;

import org.eclipse.jface.text.IDocument;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import java.io.Serializable;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Vector;
import ca.uwaterloo.fydp.xcde.*;
import ca.uwaterloo.fydp.ossp.*;
import ca.uwaterloo.fydp.ossp.std.*;
import ca.uwaterloo.fydp.xcde.annotations.*;

public class XCDEDocWrapper implements XCDEDocClientCallbacks, IDocumentListener, IAnnotationModelListener {

    private IDocument document;

    private IAnnotationModel annotModel;

    private IResource resource;

    private HashMap annotations = new HashMap();

    private XCDEDocClient docClient;

    private boolean downloadComplete = false;

    private boolean isConnected = false;

    private XCDERegister parentRegister;

    private int instanceID = this.hashCode();

    public XCDEDocWrapper(XCDEDocClient client, XCDERegister parent) {
        docClient = client;
        parentRegister = parent;
    }

    public void setIDocument(IDocument doc) {
        document = doc;
        docClient.setCallback(this);
        docClient.open();
        isConnected = true;
    }

    /**
	 * Called to connect the DocWrapper to the appropriate annotation model
	 * for the document being edited
	 * @param model The annotaiton model to connect to
	 * @param r The IResource which should manufacture any new IMarkers for us
	 */
    public void setAnnotModel(IAnnotationModel model, IResource r) {
        annotModel = model;
        annotModel.addAnnotationModelListener(this);
        resource = r;
        for (Iterator i = annotModel.getAnnotationIterator(); i.hasNext(); ) {
            Annotation current = (Annotation) i.next();
            if (!annotationSupported(current)) continue;
            if (current.getType().equals("org.eclipse.ui.workbench.texteditor.bookmark")) annotations.put(current, buildXCDEAnnotation(annotModel.getPosition(current).offset, annotModel.getPosition(current).length, buildBookmarkDetails((MarkerAnnotation) current))); else if (current.getType().equals("org.eclipse.ui.workbench.texteditor.task")) annotations.put(current, buildXCDEAnnotation(annotModel.getPosition(current).offset, annotModel.getPosition(current).length, buildTaskDetails((MarkerAnnotation) current)));
        }
    }

    /**
	 * Called just before a document change caused by the user is triggered.
	 * Required by the
	 * {@link org.eclipse.jface.text.IDocumentListener IDocumentListener}
	 * interface.
	 */
    public void documentAboutToBeChanged(DocumentEvent e) {
    }

    /**
	 * Iterates through the local user list and updates positions for a delete as specified
	 * no change messages are sent
	 * @param dc The XCDEDocChange object for the change that is occuring
	 */
    private void updateUserPositions(XCDEDocChange dc) {
        Vector users = parentRegister.getUserList().getUsers();
        for (int i = 0; i < users.size(); ++i) {
            UserData current = (UserData) users.get(i);
            if (current.getConcatPath().equals(getPath())) {
                current.setCursor(dc.updateSelection(current.getCursor()));
            }
        }
    }

    /**
	 * Called when a document is changed by the local user. Required by the
	 * {@link org.eclipse.jface.text.IDocumentListener IDocumentListener}
	 * interface.
	 */
    public void documentChanged(DocumentEvent e) {
        try {
            XCDEDocChange delete = null, add = null;
            if (e.getLength() != 0) {
                delete = new XCDEDocChangeDeletion(parentRegister.getUsername(), e.getOffset(), e.getLength());
                updateUserPositions(delete);
            }
            if (e.getText() != null && e.getText().length() != 0) {
                add = new XCDEDocChangeInsertion(parentRegister.getUsername(), e.getOffset(), e.getText());
                updateUserPositions(add);
            }
            if (delete != null) updateAnnotations(delete);
            if (add != null) updateAnnotations(add);
            if (delete != null && add != null) {
                makeChange(new OSSPCompoundStimulus(delete, add));
            } else if (delete != null) {
                makeChange(delete);
            } else if (add != null) {
                makeChange(add);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void _notifyOfChange(OSSPStimulus ch) {
        if (ch instanceof XCDEDocChangeInsertion) {
            try {
                document.replace(((XCDEDocChange) ch).pos, 0, ((XCDEDocChangeInsertion) ch).text);
                updateUserPositions(((XCDEDocChange) ch));
            } catch (BadLocationException e) {
                throw new RuntimeException("Bad location exection from XCDE insert!", e);
            }
        } else if (ch instanceof XCDEDocChangeDeletion) {
            try {
                document.replace(((XCDEDocChange) ch).pos, ((XCDEDocChangeDeletion) ch).num, "");
                updateUserPositions((XCDEDocChange) ch);
            } catch (BadLocationException e) {
                throw new RuntimeException("Bad location exception from XCDE delete", e);
            }
        } else if (ch instanceof OSSPCompoundStimulus) {
            _notifyOfChange(((OSSPCompoundStimulus) ch).getFirst());
            _notifyOfChange(((OSSPCompoundStimulus) ch).getSecond());
        } else if (ch instanceof XCDEDocumentAnnotationStimulusAdd) {
            XCDEDocumentAnnotationStimulusAdd addStimulus = (XCDEDocumentAnnotationStimulusAdd) ch;
            addAnnotationToModel(addStimulus.annotation);
        } else if (ch instanceof XCDEDocumentAnnotationStimulusChange) {
            XCDEDocumentAnnotationStimulusChange change = (XCDEDocumentAnnotationStimulusChange) ch;
            removeAnnotationFromModel(change.annotation);
            addAnnotationToModel(change.newNote);
        } else if (ch instanceof XCDEDocumentAnnotationStimulusDelete) {
            XCDEDocumentAnnotationStimulusDelete deletion = (XCDEDocumentAnnotationStimulusDelete) ch;
            removeAnnotationFromModel(deletion.annotation);
        } else {
            throw new RuntimeException("Unknown XCDEChange received!");
        }
    }

    /**
	 * Return the type string for a change.
	 * @param s Object to check
	 * @return String representing the type of annotaiton
	 */
    private String getAnnotationType(Serializable s) {
        if (s instanceof XCDEBookmarkDetails) return "org.eclipse.ui.workbench.texteditor.bookmark"; else if (s instanceof XCDETaskDetails) return "org.eclipse.ui.workbench.texteditor.task"; else return "";
    }

    /**
	 * Searches the current annotation model for an annotation matching the parameters
	 * passed in.
	 * @param type The type of annotation to find, of the form returned by getAnnotationType
	 * @param offset The offset from the start of the file to the start of the annotated region
	 * @param length Length of the annotated region
	 * @param text The text of the annotaiton
	 * @return The first mataching annotation found
	 */
    private Annotation findAnnotation(String type, int offset, int length, String text) {
        for (Iterator i = annotModel.getAnnotationIterator(); i.hasNext(); ) {
            Annotation current = (Annotation) i.next();
            if (!annotationSupported(current)) continue;
            try {
                if (current.getType().equals(type) && annotModel.getPosition(current).offset == offset && annotModel.getPosition(current).length == length && ((MarkerAnnotation) current).getMarker().getAttribute(IMarker.MESSAGE).equals(text)) {
                    return current;
                }
            } catch (CoreException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
	 * Called by the document's XCDE client when a change message is received
	 * from the XCDE server. Required by the
	 * {@link org.eclipse.xcde.xcde.eclipse.XCDEEclipseCallBack XCDE Client Callback}
	 * interface.
	 */
    public void notifyOfChange(XCDEDocClient client, OSSPStimulus ch) {
        if (downloadComplete) {
            document.removeDocumentListener(this);
            annotModel.removeAnnotationModelListener(this);
            _notifyOfChange(ch);
            annotModel.addAnnotationModelListener(this);
            document.addDocumentListener(this);
        }
    }

    /**
	 * Called by the document's XCDE client if the client is disconnected from
	 * the server Required by the
	 * {@link org.eclipse.xcde.xcde.eclipse.XCDEEclipseCallBack XCDE Client Callback}
	 * interface.
	 */
    public void notifyOfDisconnect(XCDEDocClient client) {
        notifyOfChange(client, new XCDEDocChangeInsertion(0, "DISCONNECTED!\n"));
        if (downloadComplete) {
            document.removeDocumentListener(this);
            annotModel.removeAnnotationModelListener(this);
        }
        isConnected = false;
    }

    /**
	 * Called by the document if the document needs to disconnect from the XCDE
	 * server.
	 */
    public void disconnect(String username) {
        docClient.disconnect();
        if (downloadComplete) {
            document.removeDocumentListener(this);
            annotModel.removeAnnotationModelListener(this);
            for (Iterator i = annotations.values().iterator(); i.hasNext(); ) {
                removeAnnotationFromModel((XCDEDocumentAnnotation) i.next());
            }
        }
        isConnected = false;
    }

    /**
	 * Returns whether or not this docClient is connected to the server and has
	 * a document listener attached to it.
	 */
    public boolean isConnected() {
        return isConnected;
    }

    public void receiveState(XCDEDocClient client, XCDEDocument d) {
        if (!downloadComplete) {
            if (!document.get().equals(d.content)) {
                document.set(d.content);
            }
            annotModel.removeAnnotationModelListener(this);
            for (Iterator i = d.annotations.iterator(); i.hasNext(); ) {
                XCDEDocumentAnnotation note = (XCDEDocumentAnnotation) i.next();
                removeAnnotationFromModel(note);
                addAnnotationToModel(note);
            }
            downloadComplete = true;
            document.addDocumentListener(this);
            annotModel.addAnnotationModelListener(this);
        }
    }

    private void makeChange(final OSSPStimulus stim) {
        docClient.makeChange(stim);
    }

    /**
	 * This model is called whenever the user causes a change in the annotaiton model.  In here we process
	 * the entire state and generate the change messages for the server.
	 */
    public void modelChanged(IAnnotationModel model) {
        HashSet found = new HashSet();
        for (Iterator i = annotations.keySet().iterator(); i.hasNext(); ) found.add(i.next());
        for (Iterator i = annotModel.getAnnotationIterator(); i.hasNext(); ) {
            Annotation current = (Annotation) i.next();
            if (!annotationSupported(current)) continue;
            if (!annotations.containsKey(current)) {
                MarkerAnnotation toAdd = (MarkerAnnotation) current;
                if (toAdd.getType().equals("org.eclipse.ui.workbench.texteditor.bookmark")) {
                    XCDEBookmarkDetails details = buildBookmarkDetails(toAdd);
                    XCDEDocumentAnnotation a = buildXCDEAnnotation(annotModel.getPosition(current).offset, annotModel.getPosition(current).length, details);
                    annotations.put(current, a);
                    makeChange(new XCDEDocumentAnnotationStimulusAdd(parentRegister.getUsername(), a));
                } else if (toAdd.getType().equals("org.eclipse.ui.workbench.texteditor.task")) {
                    XCDETaskDetails details = buildTaskDetails(toAdd);
                    XCDEDocumentAnnotation a = buildXCDEAnnotation(annotModel.getPosition(current).offset, annotModel.getPosition(current).length, details);
                    annotations.put(current, a);
                    makeChange(new XCDEDocumentAnnotationStimulusAdd(parentRegister.getUsername(), a));
                }
            } else {
                MarkerAnnotation toChange = (MarkerAnnotation) current;
                if (toChange.getType().equals("org.eclipse.ui.workbench.texteditor.bookmark")) {
                    XCDEBookmarkDetails details = buildBookmarkDetails(toChange);
                    XCDEDocumentAnnotation a = buildXCDEAnnotation(annotModel.getPosition(current).offset, annotModel.getPosition(current).length, details);
                    if (!((XCDEDocumentAnnotation) annotations.get(current)).details.equals(details)) {
                        makeChange(new XCDEDocumentAnnotationStimulusChange(parentRegister.getUsername(), (XCDEDocumentAnnotation) annotations.get(current), a));
                    }
                    annotations.put(current, a);
                } else if (toChange.getType().equals("org.eclipse.ui.workbench.texteditor.task")) {
                    XCDETaskDetails details = buildTaskDetails(toChange);
                    XCDEDocumentAnnotation a = buildXCDEAnnotation(annotModel.getPosition(current).offset, annotModel.getPosition(current).length, details);
                    if (!((XCDEDocumentAnnotation) annotations.get(current)).details.equals(details)) {
                        makeChange(new XCDEDocumentAnnotationStimulusChange(parentRegister.getUsername(), (XCDEDocumentAnnotation) annotations.get(current), a));
                    }
                    annotations.put(current, a);
                }
                found.remove(current);
            }
        }
        for (Iterator i = found.iterator(); i.hasNext(); ) {
            Annotation current = (Annotation) i.next();
            MarkerAnnotation toDelete = (MarkerAnnotation) current;
            if (toDelete.getType().equals("org.eclipse.ui.workbench.texteditor.bookmark")) {
                makeChange(new XCDEDocumentAnnotationStimulusDelete(parentRegister.getUsername(), (XCDEDocumentAnnotation) annotations.get(current)));
            } else if (toDelete.getType().equals("org.eclipse.ui.workbench.texteditor.task")) {
                makeChange(new XCDEDocumentAnnotationStimulusDelete(parentRegister.getUsername(), (XCDEDocumentAnnotation) annotations.get(current)));
            }
            annotations.remove(current);
        }
    }

    /**
	 * This is a helper method that wraps the constructor for XCDETaskDetails and generates the details object from a
	 * MarkerAnnotation.
	 * @param m The MarkerAnnotation that will provide the information for the new XCDETaskDetails
	 * @return The XCDETaskDetails object constructed from the provided information
	 */
    private static XCDETaskDetails buildTaskDetails(MarkerAnnotation m) {
        XCDETaskDetails details = null;
        try {
            details = new XCDETaskDetails(((Boolean) m.getMarker().getAttribute(IMarker.DONE)).booleanValue(), ((Integer) m.getMarker().getAttribute(IMarker.PRIORITY)).intValue(), (String) m.getMarker().getAttribute(IMarker.MESSAGE), (String) m.getMarker().getAttribute(IMarker.LOCATION), m.getText());
        } catch (CoreException e) {
            throw new RuntimeException(e);
        }
        return details;
    }

    /**
	 * This is a helper method that wraps the constructor for XCDEBookmarkDetails and generates the details object from a
	 * MarkerAnnotation.
	 * @param m The MarkerAnnotation that will provide the information for the new XCDEBookmarkDetails
	 * @return The XCDETaskDetails object constructed from the provided information
	 */
    private static XCDEBookmarkDetails buildBookmarkDetails(MarkerAnnotation m) {
        XCDEBookmarkDetails details = null;
        try {
            details = new XCDEBookmarkDetails((String) m.getMarker().getAttribute(IMarker.MESSAGE), (String) m.getMarker().getAttribute(IMarker.LOCATION), m.getText());
        } catch (CoreException e) {
            throw new RuntimeException(e);
        }
        return details;
    }

    private static XCDEDocumentAnnotation buildXCDEAnnotation(int offset, int length, Serializable s) {
        return new XCDEDocumentAnnotation(new XCDEDocumentSelection(offset, length), s);
    }

    /**
	 * Adds an XCDEDocumentAnnotation to the current AnnotationModel
	 * @param a The XCDEDocumentAnnotation to add
	 */
    private void addAnnotationToModel(XCDEDocumentAnnotation a) {
        IMarker marker = null;
        try {
            if (a.details instanceof XCDEBookmarkDetails) {
                XCDEBookmarkDetails details = (XCDEBookmarkDetails) a.details;
                marker = resource.createMarker(IMarker.BOOKMARK);
                marker.setAttribute(IMarker.MESSAGE, details.message);
                marker.setAttribute(IMarker.LOCATION, details.location);
            } else if (a.details instanceof XCDETaskDetails) {
                XCDETaskDetails details = (XCDETaskDetails) a.details;
                marker = resource.createMarker(IMarker.TASK);
                marker.setAttribute(IMarker.DONE, details.complete);
                marker.setAttribute(IMarker.PRIORITY, details.priority);
                marker.setAttribute(IMarker.MESSAGE, details.message);
                marker.setAttribute(IMarker.LOCATION, details.location);
            }
            marker.setAttribute(IMarker.CHAR_START, a.selection.pos);
            marker.setAttribute(IMarker.CHAR_END, a.selection.pos + a.selection.length);
            try {
                marker.setAttribute(IMarker.LINE_NUMBER, document.getLineOfOffset(a.selection.pos) + 1);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        } catch (CoreException e) {
            throw new RuntimeException("CoreException creating IMarker", e);
        }
        MarkerAnnotation annotation = new MarkerAnnotation(marker);
        annotation.setType(getAnnotationType(a.details));
        Position position = new Position(a.selection.pos, a.selection.length);
        annotModel.addAnnotation(annotation, position);
        annotations.put(annotation, a);
    }

    /**
	 * Removes an XCDEDocumentAnnotation to the current AnnotationModel
	 * @param a The XCDEDocumentAnnotation to remove
	 */
    private void removeAnnotationFromModel(XCDEDocumentAnnotation a) {
        int length = a.selection.length;
        int offset = a.selection.pos;
        String type = getAnnotationType(a.details);
        String text = "";
        if (a.details instanceof XCDEBookmarkDetails) text = ((XCDEBookmarkDetails) a.details).text; else if (a.details instanceof XCDETaskDetails) text = ((XCDETaskDetails) a.details).text;
        Annotation toRemove = findAnnotation(type, offset, length, text);
        if (toRemove != null) {
            try {
                ((MarkerAnnotation) toRemove).getMarker().getResource().getWorkspace().deleteMarkers(new IMarker[] { ((MarkerAnnotation) toRemove).getMarker() });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            annotations.remove(toRemove);
            annotModel.removeAnnotation(toRemove);
        }
    }

    private static final boolean annotationSupported(Annotation a) {
        if (!(a instanceof MarkerAnnotation)) return false;
        MarkerAnnotation m = (MarkerAnnotation) a;
        try {
            return ((m.getType().equals("org.eclipse.ui.workbench.texteditor.bookmark") && m.getMarker().getAttribute(IMarker.MESSAGE) != null) || (m.getType().equals("org.eclipse.ui.workbench.texteditor.task") && m.getMarker().getAttribute(IMarker.DONE) != null && m.getMarker().getAttribute(IMarker.PRIORITY) != null && m.getMarker().getAttribute(IMarker.MESSAGE) != null));
        } catch (CoreException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAnnotations(XCDEDocChange ch) {
        annotModel.removeAnnotationModelListener(this);
        for (Iterator i = annotModel.getAnnotationIterator(); i.hasNext(); ) {
            Annotation a = (Annotation) i.next();
            if (!annotationSupported(a)) continue;
            annotations.remove(a);
            XCDEDocumentSelection newSel = new XCDEDocumentSelection(annotModel.getPosition(a).offset, annotModel.getPosition(a).length);
            IMarker marker = ((MarkerAnnotation) a).getMarker();
            try {
                marker.setAttribute(IMarker.CHAR_START, newSel.pos);
                marker.setAttribute(IMarker.CHAR_END, newSel.pos + newSel.length);
                marker.setAttribute(IMarker.LINE_NUMBER, document.getLineOfOffset(newSel.pos) + 1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (a.getType().equals("org.eclipse.ui.workbench.texteditor.bookmark")) annotations.put(a, buildXCDEAnnotation(newSel.pos, newSel.length, buildBookmarkDetails((MarkerAnnotation) a))); else annotations.put(a, buildXCDEAnnotation(newSel.pos, newSel.length, buildTaskDetails((MarkerAnnotation) a)));
        }
        annotModel.addAnnotationModelListener(this);
    }

    /**
     * Returns the full path of the document
	 * @return The path as a string
     */
    public String getPath() {
        return docClient.getPath();
    }
}
