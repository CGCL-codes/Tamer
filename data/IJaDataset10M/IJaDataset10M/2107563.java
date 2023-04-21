package aau;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.faces.event.ValueChangeEvent;
import meco.Document;
import mecoDB.DocumentDB;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import edu.stanford.nlp.util.StringUtils;
import tagcloud.TagCloudGeneration;
import tagcloud.datastructure.GenericTag;
import tagcloud.datastructure.MecoTag;
import tagcloud.util.DbUtil;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;

/**
 * @author Martin Leginus
 */
@Name("tagCloudAction")
@Scope(ScopeType.SESSION)
public class TagCloudAction<Tag extends GenericTag> {

    private TagCloudGeneration tagCloudService = new TagCloudGeneration(null);

    private List<Document> documents;

    private List<Tag> selectedTags = new ArrayList<Tag>();

    private AbstractStringMetric cosineSimilarity = new CosineSimilarity();

    public void generateTagCloud() {
        System.out.println("Calling generateTagCloud");
        tagCloudService = new TagCloudGeneration(null);
        tagCloudService.generateTagCloud();
    }

    public void OnValChanged(ValueChangeEvent ev) {
        System.out.println("Obtained tag cloud click value--------------------------------------------------------------------------------");
        System.out.println("The following tag cloud was selected" + ev.toString());
        System.out.println((String) ev.getNewValue());
        Tag selectedTag = (Tag) tagCloudService.selectTag((String) ev.getNewValue());
        insertSelectedTag(selectedTag);
        refreshSelectedDocuments();
    }

    public void removeTag(Tag selectedTag) {
        System.out.println("Tag was removed" + selectedTag.getTagLabel());
        selectedTags.remove(selectedTag);
        refreshSelectedDocuments();
    }

    public void addTag(Tag selectedTag) {
        selectedTag = (Tag) tagCloudService.selectTag(selectedTag.getTagLabel());
        List<Tag> tag = new ArrayList<Tag>();
        tag.add(selectedTag);
        tagCloudService.getTaggedDocuments(tag);
        insertSelectedTag(selectedTag);
        refreshSelectedDocuments();
    }

    private void refreshSelectedDocuments() {
        documents = new LinkedList<Document>();
        DocumentDB documentDb = new DocumentDB(DbUtil.dbAccess);
        String selectedTagsConvertedToString = convertTags();
        if (selectedTags.size() > 1) {
            Set<Integer> docsOfSelectedTags = null;
            for (Tag tag : selectedTags) {
                if (docsOfSelectedTags == null) {
                    docsOfSelectedTags = new HashSet<Integer>();
                    docsOfSelectedTags.addAll(tag.getTaggedDocuments().keySet());
                } else {
                    docsOfSelectedTags.retainAll(tag.getTaggedDocuments().keySet());
                    System.out.println("Intersetcion of docs is" + docsOfSelectedTags.size());
                }
            }
            for (Integer docId : docsOfSelectedTags) {
                System.out.println("Intersection" + docId);
                Document doc = documentDb.getDocument(docId);
                doc.setDocumentSelectedTagsCosineScore(computeCosineSimilarityDocTags(doc, selectedTagsConvertedToString));
                documents.add(doc);
            }
            Collections.sort(documents, Document.RankingScoreComparator);
            Set<Integer> docsIdOfNotAllSelectedTags = new HashSet<Integer>();
            for (Tag tag : selectedTags) {
                for (Integer docId : tag.getTaggedDocuments().keySet()) {
                    docsIdOfNotAllSelectedTags.add(docId);
                }
            }
            docsIdOfNotAllSelectedTags.removeAll(docsOfSelectedTags);
            List<Document> docsOfNotAllSelectedTags = new ArrayList<Document>();
            for (Integer docId : docsIdOfNotAllSelectedTags) {
                Document doc = documentDb.getDocument(docId);
                doc.setDocumentSelectedTagsCosineScore(computeCosineSimilarityDocTags(doc, selectedTagsConvertedToString));
                docsOfNotAllSelectedTags.add(doc);
            }
            Collections.sort(docsOfNotAllSelectedTags, Document.RankingScoreComparator);
            documents.addAll(docsOfNotAllSelectedTags);
        } else if (selectedTags.size() == 1) {
            Set<Integer> docsIdOfSelectedTag = new HashSet<Integer>();
            for (Tag tag : selectedTags) {
                for (Integer docId : tag.getTaggedDocuments().keySet()) {
                    docsIdOfSelectedTag.add(docId);
                }
            }
            List<Document> docsOfSelectedTag = new ArrayList<Document>();
            for (Integer docId : docsIdOfSelectedTag) {
                Document doc = documentDb.getDocument(docId);
                doc.setDocumentSelectedTagsCosineScore(computeCosineSimilarityDocTags(doc, selectedTagsConvertedToString));
                docsOfSelectedTag.add(doc);
            }
            Collections.sort(docsOfSelectedTag, Document.RankingScoreComparator);
            documents.addAll(docsOfSelectedTag);
        }
    }

    private float computeCosineSimilarityDocTags(Document doc, String tags) {
        float cosineSimilarityScore = 0;
        if (doc.getName() != null) {
            if (doc.getDescription() != null) {
                String mergedDescriptionTitle = doc.getName() + " " + doc.getDescription();
                if (!StringUtils.isAlphanumeric(mergedDescriptionTitle)) {
                    return cosineSimilarityScore;
                }
                cosineSimilarityScore = cosineSimilarity.getSimilarity(tags, mergedDescriptionTitle.substring(0, 20));
            } else {
                String mergedTitle = doc.getName();
                if (!StringUtils.isAlphanumeric(mergedTitle)) {
                    return cosineSimilarityScore;
                }
                cosineSimilarityScore = cosineSimilarity.getSimilarity(tags, mergedTitle.substring(0, 20));
            }
        }
        if (Float.isNaN(cosineSimilarityScore)) cosineSimilarityScore = 0;
        if (cosineSimilarityScore > 0) {
            System.out.println("Similarity score is" + cosineSimilarityScore);
        }
        return cosineSimilarityScore;
    }

    private String convertTags() {
        StringBuffer tags = new StringBuffer();
        for (Tag tag : selectedTags) {
            tags.append(" " + tag.getTagLabel());
        }
        System.out.println("Selected tags" + tags.toString());
        return tags.toString();
    }

    private void insertSelectedTag(Tag selectedTag) {
        boolean tagAlreadySelected = false;
        for (Tag tag : selectedTags) {
            if (tag.getTagLabel().equals(selectedTag.getTagLabel())) {
                tagAlreadySelected = true;
            }
        }
        if (!tagAlreadySelected) {
            selectedTags.add(selectedTag);
        }
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Tag> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<Tag> selectedTags) {
        this.selectedTags = selectedTags;
    }
}
