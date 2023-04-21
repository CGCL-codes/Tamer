package org.deft.repository.chaptertype.odftext;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.deft.RepositoryFacade;
import org.deft.operation.OperationChain;
import org.deft.repository.chaptertype.ChapterDocument;
import org.deft.repository.chaptertype.IntegrationRecorder;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactReference;
import org.deft.repository.datamodel.ArtifactRepresentation;
import org.deft.repository.datamodel.Chapter;
import org.deft.repository.integrator.ArtifactReferencePosition;
import org.deft.repository.integrator.Integrator;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.bion.officelayer.text.ITextCursor;
import ag.ion.bion.officelayer.text.ITextDocument;
import ag.ion.bion.officelayer.text.ITextRange;
import ag.ion.bion.officelayer.text.IViewCursor;
import ag.ion.noa.search.ISearchResult;
import ag.ion.noa.search.SearchDescriptor;

/**
 * Represents an ODFText document in an open OpenOffice editor. Modifications
 * performed on the document are realized via the OpenOffice API. 
 */
public class OdfTextChapterDocument implements ChapterDocument {

    private IDocument document;

    private Chapter chapter;

    private boolean isIntegrated;

    private static String WS = "\\s*?";

    private static String QUOTE = "\\\"";

    private static final String REF_ID_PATTERN_FOR_JAVA = "<" + WS + "artifact" + WS + "id" + WS + "=" + WS + QUOTE + "(.*?)" + QUOTE + WS + "/>";

    private static String OO_WS = "(\n| |\t|\r)*";

    private static String OO_QUOTE = "\"";

    private static final String TAG_PATTERN_FOR_OO = "<" + OO_WS + "artifact" + OO_WS + "id" + OO_WS + "=" + OO_WS + OO_QUOTE + "[^<>]*" + OO_QUOTE + OO_WS + "/>";

    private RepositoryFacade repository = RepositoryFacade.instance();

    private IntegrationRecorder recorder = new IntegrationRecorder();

    public OdfTextChapterDocument(Chapter chapter, IDocument document) {
        this.chapter = chapter;
        this.document = document;
    }

    public ITextDocument getDocument() {
        return (ITextDocument) document;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public boolean areArtifactsIntegrated() {
        return isIntegrated;
    }

    @Override
    public List<ArtifactReferencePosition> getArtifactReferencesPositions() {
        ITextDocument textDocument = (ITextDocument) document;
        ISearchResult searchResult = searchForReferences(textDocument);
        List<ArtifactReferencePosition> positions = convertResultsToArtifactPositionList(searchResult);
        return positions;
    }

    private ISearchResult searchForReferences(ITextDocument textDocument) {
        SearchDescriptor searchDescriptor = setupSearchDescriptor();
        ISearchResult result = textDocument.getSearchService().findAll(searchDescriptor);
        return result;
    }

    private SearchDescriptor setupSearchDescriptor() {
        SearchDescriptor searchDescriptor = new SearchDescriptor(TAG_PATTERN_FOR_OO);
        searchDescriptor.setIsCaseSensitive(false);
        searchDescriptor.setUseRegularExpression(true);
        return searchDescriptor;
    }

    private List<ArtifactReferencePosition> convertResultsToArtifactPositionList(ISearchResult result) {
        List<ArtifactReferencePosition> positions = new LinkedList<ArtifactReferencePosition>();
        for (ITextRange textRange : result.getTextRanges()) {
            OdfTextArtifactReferencePosition position = new OdfTextArtifactReferencePosition(this, textRange);
            positions.add(position);
        }
        return positions;
    }

    @Override
    public void insertReferenceTag(ArtifactReference reference) {
        String referenceTag = createArtifactReferenceContent(reference.getId());
        IViewCursor viewCursor = ((ITextDocument) document).getViewCursorService().getViewCursor();
        ITextCursor textCursor = viewCursor.getTextCursorFromEnd();
        try {
            if (referenceTag.length() > 0) {
                textCursor.setString(referenceTag);
                document.setModified(true);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void integrateArtifacts() {
        ITextRange cursorPosition = getCursorPosition();
        boolean isModified = document.isModified();
        List<ArtifactReferencePosition> positions = getArtifactReferencesPositions();
        for (ArtifactReferencePosition pos : positions) {
            ArtifactReference ref = getArtifactReference(pos);
            if (isValidReference(ref)) {
                OperationChain chain = repository.getOperationChain(ref);
                Integrator integrator = repository.getIntegrator(ref);
                if (chain != null && integrator != null) {
                    Artifact artifact = ref.getArtifact();
                    ArtifactRepresentation rep = chain.executeOperations(artifact);
                    String referenceId = ref.getId();
                    String integrationId = referenceId;
                    integrator.integrate(integrationId, rep, pos);
                    recorder.recordIntegration(integrationId, referenceId, integrator);
                    removeIntegrationStepsFromUndoHistory();
                }
            }
        }
        setCursorPosition(cursorPosition);
        try {
            document.setModified(isModified);
            isIntegrated = true;
        } catch (DocumentException de) {
            de.printStackTrace();
        }
    }

    public void removeDeadReferences() {
        List<String> referenceIdsInDocument = getReferenceIdsInDocument();
        List<ArtifactReference> referencesStoredForChapter = getChapter().getArtifactReferences();
        for (ArtifactReference ref : referencesStoredForChapter) {
            if (isReferenceNotInDocument(ref, referenceIdsInDocument)) {
                repository.remove(ref);
            }
        }
    }

    private List<String> getReferenceIdsInDocument() {
        List<ArtifactReferencePosition> positions = getArtifactReferencesPositions();
        List<String> refIds = new LinkedList<String>();
        for (ArtifactReferencePosition position : positions) {
            String tag = position.extractReferenceTag();
            String refId = extractRefIdFromTag(tag);
            refIds.add(refId);
        }
        return refIds;
    }

    private boolean isReferenceNotInDocument(ArtifactReference ref, List<String> referenceIdsInDoc) {
        boolean isInDoc = referenceIdsInDoc.contains(ref.getId());
        return !isInDoc;
    }

    /**
	 * Returns the current position of the cursor.
	 */
    private ITextRange getCursorPosition() {
        IViewCursor cursor = getDocument().getViewCursorService().getViewCursor();
        try {
            ITextRange cursorPosition = cursor.getStartTextRange();
            return cursorPosition;
        } catch (com.sun.star.uno.RuntimeException re) {
            ITextRange cursorPosition = tryAlternativeWayToGetCursorPosition();
            return cursorPosition;
        }
    }

    private ITextRange tryAlternativeWayToGetCursorPosition() {
        ITextCursor textCursor = getDocument().getTextService().getCursorService().getTextCursor();
        ITextRange cursorPosition = textCursor.getStart();
        return cursorPosition;
    }

    private void setCursorPosition(ITextRange cursorPosition) {
        try {
            if (cursorPosition != null) {
                IViewCursor cursor = getDocument().getViewCursorService().getViewCursor();
                cursor.goToRange(cursorPosition, false);
            }
        } catch (Exception e) {
            System.out.println("Setting cursor failed");
        }
    }

    private boolean isValidReference(ArtifactReference reference) {
        return reference != null;
    }

    private ArtifactReference getArtifactReference(ArtifactReferencePosition position) {
        String referenceTag = position.extractReferenceTag();
        String referenceId = extractRefIdFromTag(referenceTag);
        ArtifactReference ref = repository.getArtifactReference(referenceId);
        return ref;
    }

    private String extractRefIdFromTag(String referenceTag) {
        Pattern p = Pattern.compile(REF_ID_PATTERN_FOR_JAVA);
        Matcher m = p.matcher(referenceTag);
        if (m.find()) {
            String id = m.group(1);
            return id;
        }
        return "";
    }

    private void removeIntegrationStepsFromUndoHistory() {
    }

    @Override
    public int undoArtifactIntegration() {
        ITextRange cursorPosition = getCursorPosition();
        int undoSteps = 0;
        for (String integrationId : recorder.getIntegrationIds()) {
            String artifactRefId = recorder.getArtifactRefId(integrationId);
            Integrator integrator = recorder.getIntegrator(integrationId);
            undoSteps += integrator.undoIntegration(integrationId, this, artifactRefId);
        }
        setCursorPosition(cursorPosition);
        isIntegrated = false;
        return undoSteps;
    }

    @Override
    public String createArtifactReferenceContent(String artifactRefId) {
        return "<artifact id=\"" + artifactRefId + "\"/>";
    }

    @Override
    public void scrollToReferenceTagOrRepresentation(ArtifactReference reference) {
        OdfTextArtifactReferencePosition position = getPosition(reference.getId());
        if (position != null) {
            scrollToTextRange(position.getTextRange());
        }
    }

    private OdfTextArtifactReferencePosition getPosition(String referenceId) {
        if (areArtifactsIntegrated()) {
            OdfTextArtifactReferencePosition position = getRepresentationPosition(referenceId);
            return position;
        } else {
            OdfTextArtifactReferencePosition position = getReferencePosition(referenceId);
            return position;
        }
    }

    private OdfTextArtifactReferencePosition getReferencePosition(String referenceId) {
        SearchDescriptor searchDescriptor = new SearchDescriptor("<(\n| |\t|\r)*artifact id=\"" + referenceId + "\"/>");
        searchDescriptor.setIsCaseSensitive(false);
        searchDescriptor.setUseRegularExpression(true);
        ITextDocument textDocument = getDocument();
        ISearchResult result = textDocument.getSearchService().findFirst(searchDescriptor);
        if (!result.isEmpty()) {
            ITextRange textRange = result.getTextRanges()[0];
            OdfTextArtifactReferencePosition position = new OdfTextArtifactReferencePosition(this, textRange);
            return position;
        }
        return null;
    }

    private OdfTextArtifactReferencePosition getRepresentationPosition(String artifactRefId) {
        String integrationId = recorder.getIntegrationId(artifactRefId);
        Integrator integrator = recorder.getIntegrator(integrationId);
        if (integrator != null) {
            ArtifactReferencePosition position = integrator.getPositionForRepresentation(this, artifactRefId);
            return (OdfTextArtifactReferencePosition) position;
        }
        return null;
    }

    private void scrollToTextRange(ITextRange textRange) {
        if (textRange != null) {
            ITextDocument textDocument = getDocument();
            textDocument.getViewCursorService().getViewCursor().goToRange(textRange, false);
        }
    }
}
