package analyzers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import onepageeditor.editors.ColorManager;
import onepageeditor.editors.CustomContentAssistProcessor;
import onepageeditor.editors.CustomTagDescription;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.MarkerUtilities;
import weird.IAcceptableEngine;
import weird.ICollector;
import weird.XMLPartitionScanner;
import weird.XMLTagScanner;

public class TagAnalyzer implements IAcceptableEngine {

    private static final String NO_CLOSE_TAG = "No close tag for: ";

    private static final String NO_OPEN_TAG = "No open tag for: ";

    private static XMLPartitionScanner myScanner;

    private static IDocument myDocument;

    private TextEditor myEditor;

    private Set<CustomTagDescription> set = new HashSet<CustomTagDescription>();

    private final int[] myTypes;

    public TagAnalyzer(IDocument document, TextEditor editor, int[] types) {
        myDocument = document;
        myEditor = editor;
        myTypes = types;
    }

    public List<CustomProposal> analyzeTags(ITextViewer viewer, int documentOffset) {
        List<CustomTagDescription> myErrors = new ArrayList<CustomTagDescription>();
        List<CustomProposal> proposals = collectProposals(viewer.getDocument(), documentOffset, myErrors);
        return proposals;
    }

    private List<CustomProposal> collectProposals(IDocument document, int documentOffset, List<CustomTagDescription> myErrors) {
        int start = 0;
        int lines = myDocument.getNumberOfLines();
        List<CustomProposal> result = null;
        try {
            for (int i = 0; i < myTypes.length && i < lines && myDocument.getLineOffset(i) < documentOffset; ) {
                int first = i;
                for (; i < myTypes.length && myTypes[i] == myTypes[first] && i < lines && myDocument.getLineOffset(i) <= documentOffset; i++) ;
                int last = i - 1;
                int min = Math.min(documentOffset, document.getLineOffset(last) + document.getLineLength(last));
                result = collectProposals(document, start, min - start, myErrors);
                start = min;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<CustomProposal> collectProposals(IDocument document, int documentOffset, int length, List<CustomTagDescription> myErrors) {
        List<CustomProposal> proposals = new ArrayList<CustomProposal>();
        List<CustomTagDescription> tagStack = new ArrayList<CustomTagDescription>();
        set.clear();
        myScanner = new XMLPartitionScanner(myDocument.getDocumentPartitioner().getLegalContentTypes()[0]);
        myScanner.setRange(myDocument, documentOffset, length);
        XMLTagScanner tagScanner = new XMLTagScanner(new ColorManager(), null);
        while (myScanner.getTokenOffset() + myScanner.getTokenLength() < length + documentOffset) {
            IToken token = myScanner.nextToken();
            int tokenLength = myScanner.getTokenLength();
            int tokenOffset = myScanner.getTokenOffset();
            if (token.getData() == null || (false)) {
            } else {
                tagScanner.setRange(document, tokenOffset, tokenLength);
                String name = TagParser.getFirstWord(myDocument, tagScanner, tokenLength + tokenOffset);
                if (name.length() == 0) {
                    String cut = myDocument.get().substring(tokenLength + tokenOffset, tagScanner.getTokenOffset() + tagScanner.getTokenLength());
                } else {
                }
                CustomTagDescription description = new CustomTagDescription(name, false, tokenOffset, tokenLength, "");
                if (!description.isClose()) {
                    tagStack.add(description);
                } else {
                    if (tagStack.size() == 0) {
                        description.setErrorDescription(NO_OPEN_TAG);
                        myErrors.add(description);
                    } else {
                        CustomTagDescription tagUpper = tagStack.get(tagStack.size() - 1);
                        if (tagUpper.getName().equals(description.getName())) {
                            tagStack.remove(tagStack.size() - 1);
                        } else {
                            description.setErrorDescription(NO_OPEN_TAG);
                            myErrors.add(description);
                            if (set.contains(tagUpper) == false) {
                                proposals.add(new CustomProposal(tagUpper, myScanner.getTokenOffset(), CustomContentAssistProcessor.CLOSE_BEFORE_ERROR));
                            }
                            set.add(tagUpper);
                        }
                    }
                }
            }
        }
        int i = 0;
        while (tagStack.size() > i) {
            CustomTagDescription removedTag = tagStack.get(i);
            i++;
            removedTag.setErrorDescription(NO_CLOSE_TAG);
            proposals.add(new CustomProposal(removedTag, myScanner.getTokenOffset() + myScanner.getTokenLength(), CustomContentAssistProcessor.ADD_CLOSE));
            myErrors.add(removedTag);
        }
        return proposals;
    }

    public void check(IDocument document, IRegion[] regions, Object context, ICollector collector, IProgressMonitor monitor) {
        IResource resource = ((FileEditorInput) myEditor.getEditorInput()).getFile();
        try {
            resource.deleteMarkers(IMarker.PROBLEM, true, 0);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        if (resource == null) {
            return;
        }
        List<CustomTagDescription> myErrors = new ArrayList<CustomTagDescription>();
        collectProposals(document, document.getLength(), myErrors);
        for (CustomTagDescription description : myErrors) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(IMarker.CHAR_START, description.getOffset());
            map.put(IMarker.CHAR_END, description.getOffset() + description.getLength());
            String error = description.getErrorDescription();
            if (error != null && error.length() > 0) {
                map.put(IMarker.MESSAGE, error + description.getName());
            } else {
                map.put(IMarker.MESSAGE, NO_OPEN_TAG + description.getName());
            }
            map.put(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            try {
                MarkerUtilities.createMarker(resource, map, IMarker.PROBLEM);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    public IDocument getDocument() {
        return myDocument;
    }
}
