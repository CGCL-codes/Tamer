package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author ricardol
 * @
 */
public class Indicator implements Serializable {

    private int idIndicator;

    private List<Document> documents = new ArrayList<Document>();

    private List<Integer> documentIds = new ArrayList<Integer>();

    private int numDocuments = 0;

    private List<Segment> relevantSegments;

    private Date initialDate;

    private Date finalDate;

    private String medicalConditionString;

    private String locationString;

    private String timeFrame;

    private Date minUsedQueryDate;

    private Date maxUsedQueryDate;

    private SignalDefinitionFeatures signalDefinitionFeatures = new SignalDefinitionFeatures();

    private SortedMap<String, Float> favMap = new TreeMap<String, Float>();

    private Map<String, Float> favMapSinalDefinition = new TreeMap<String, Float>();

    private SortedMap<String, Integer> docFreq = new TreeMap<String, Integer>();

    private Boolean isProcessed;

    private Float score;

    private Date creationDate;

    public Indicator() {
    }

    /**
     * Method used for the web service
     * @param idIndicator
     */
    public Indicator(int idIndicator) {
        this.idIndicator = idIndicator;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Integer> getDocumentIds() {
        return this.documentIds;
    }

    /**
	 * It builds a string builder for all strings 
	 * @return
	 */
    public String getMedicalConditionString(Indicator indicator) {
        StringBuilder medicalConditionString = new StringBuilder();
        if (this.signalDefinitionFeatures.getMedicalConditions() != null) {
            for (MedicalCondition medicalCondition : indicator.signalDefinitionFeatures.getMedicalConditions()) {
                medicalConditionString.append(medicalCondition.getName());
                medicalConditionString.append(",");
            }
        }
        try {
            if (indicator.signalDefinitionFeatures.getMedicalConditions().size() > 0 && medicalConditionString.length() > 1 && (medicalConditionString.lastIndexOf(",") == medicalConditionString.length() - 1)) {
                medicalConditionString = new StringBuilder(medicalConditionString.substring(0, medicalConditionString.lastIndexOf(",")));
            }
        } catch (StringIndexOutOfBoundsException e) {
            medicalConditionString = new StringBuilder(medicalConditionString.substring(0, 5));
        }
        return medicalConditionString.toString();
    }

    /**
	 * It builds a string builder for all strings 
	 * @return
	 */
    public String getLocationString(Indicator indicator) {
        StringBuilder locationString = new StringBuilder();
        if (this.signalDefinitionFeatures.getLocations() != null) {
            for (Location location : indicator.signalDefinitionFeatures.getLocations()) {
                locationString.append(location.getName());
                locationString.append(",");
            }
        }
        try {
            if (indicator.signalDefinitionFeatures.getLocations().size() > 0 && locationString.length() > 1 && (locationString.lastIndexOf(",") == locationString.length() - 1)) {
                locationString = new StringBuilder(locationString.substring(0, locationString.lastIndexOf(",")));
            }
        } catch (StringIndexOutOfBoundsException e) {
            locationString = new StringBuilder(locationString.substring(0, 5));
        }
        return locationString.toString();
    }

    public Set<Location> getLocations() {
        return this.signalDefinitionFeatures.getLocations();
    }

    public Set<MedicalCondition> getMedicalConditions() {
        return this.signalDefinitionFeatures.getMedicalConditions();
    }

    /**
     * Adds a relevant document to the indicator
     * @param document Document matching this indicator
     * @param relevantSegments List of segments of the document being added that are relevant to the indicator
     */
    @Deprecated
    public void addDocument(Document document, List<Segment> relevantSegments) {
        this.documents.add(document);
        for (Segment s : relevantSegments) {
            if (!this.relevantSegments.contains(s) && s.getDocument().equals(document)) {
                this.relevantSegments.add(s);
            }
        }
        if (this.initialDate.compareTo(document.getDate()) <= 1) {
            this.initialDate = document.getDate();
        }
        if (this.finalDate.compareTo(document.getDate()) >= 1) {
            this.finalDate = document.getDate();
        }
    }

    public void addDocument(Document document) {
        if (!this.documents.contains(document)) {
            this.documents.add(document);
            updateTfdf(document.getTermFrequency());
            this.numDocuments++;
            if (!this.documentIds.contains(document.getDocumentId())) {
                this.documentIds.add(document.getDocumentId());
            }
        }
    }

    public void addDocumentId(Integer documentId) {
        if (!this.documentIds.contains(documentId)) {
            this.documentIds.add(documentId);
        }
    }

    public Map<String, Float> getTfdf() {
        return this.favMap;
    }

    /**
     * This should be more relevant than getTfdf
     * @return
     */
    public Map<String, Float> getTfdfSignalDefinition() {
        return this.favMapSinalDefinition;
    }

    /**
     * Do
     * @param termFrequency
     */
    public void calculateTfdfSignalDefinition(Map<String, Float> termFrequency) {
        this.favMapSinalDefinition = termFrequency;
    }

    public void updateTfdf(Map<String, Float> termFrequency) {
        for (Map.Entry<String, Float> entry : termFrequency.entrySet()) {
            if (entry.getKey().length() > 2) {
                Float value = new Float(0);
                int docFreqValue;
                if (favMap.containsKey(entry.getKey())) {
                    value = favMap.get(entry.getKey()) + (entry.getValue());
                    docFreqValue = docFreq.get(entry.getKey()) + 1;
                } else {
                    value = entry.getValue();
                    docFreqValue = 1;
                }
                favMap.put(entry.getKey(), value);
                docFreq.put(entry.getKey(), docFreqValue);
            }
        }
        for (Map.Entry<String, Float> entry : favMap.entrySet()) {
            entry.setValue(entry.getValue() / docFreq.get(entry.getKey()));
        }
    }

    public String getTimeFrame() {
        return this.timeFrame;
    }

    public Boolean IsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public void setId(int idIndicator) {
        this.idIndicator = idIndicator;
    }

    public int getId() {
        return this.idIndicator;
    }

    public int getIdIndicator() {
        return idIndicator;
    }

    public void setIdIndicator(int idIndicator) {
        this.idIndicator = idIndicator;
    }

    public SignalDefinitionFeatures getSignalDefinitionFeatures() {
        return this.signalDefinitionFeatures;
    }

    public List<Segment> getRelevantSegments() {
        return relevantSegments;
    }

    public void setRelevantSegments(List<Segment> relevantSegments) {
        this.relevantSegments = relevantSegments;
    }

    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
        this.numDocuments = documents.size();
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public String getMedicalConditionString() {
        return medicalConditionString;
    }

    public void setMedicalConditionString(String medicalConditionString) {
        this.medicalConditionString = medicalConditionString;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public void setNumberOfDocuments(int numDocuments) {
        this.numDocuments = numDocuments;
    }

    public int getNumberOfDocuments() {
        return this.numDocuments;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Float getScore() {
        return this.score;
    }

    public Date getMaxUsedQueryDate() {
        return maxUsedQueryDate;
    }

    public void setMaxUsedQueryDate(Date maxUsedQueryDate) {
        this.maxUsedQueryDate = maxUsedQueryDate;
    }

    public Date getMinUsedQueryDate() {
        return minUsedQueryDate;
    }

    public void setMinUsedQueryDate(Date minUsedQueryDate) {
        this.minUsedQueryDate = minUsedQueryDate;
    }

    public void setCreationDate(Date date) {
        this.creationDate = date;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }
}
