package aau;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import meco.Document;
import meco.Indicator;
import meco.MecoEvaluation;
import meco.MecoUser;
import meco.RecommendedDocument;
import meco.RecommendedIndicator;
import meco.SignalDefinition;
import meco.SimilarityModel;
import meco.Source;
import mecoDB.DocumentDB;
import mecoDB.IndicatorDB;
import mecoGeneric.Location;
import mecoGeneric.MedicalCondition;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import tree.BaseBean;
import tree.MessageBundleLoader;
import com.icesoft.faces.context.effects.JavascriptContext;

/**
 * @author freddurao
 */
@Name("signalDefinitionAction")
@Scope(ScopeType.SESSION)
public class SignalDefinitionAction extends BaseBean implements ValueChangeListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private boolean modalRendered = false;

    private boolean modalEvaluationRendered = false;

    private IndicatorDB indicatorDB = null;

    private DocumentDB documentDB = null;

    private String name;

    private String round;

    private String description;

    private String location;

    private String childLocation;

    private String medicalCondition;

    private String indicatorId;

    private String recommendedIndicatorId;

    private String source;

    private boolean isAdmin = false;

    private String tagString;

    private List<Location> locations;

    private List<Location> childLocations;

    private List<MedicalCondition> medicalConditions;

    private String recommenderIndicatorSelected;

    private List<RecommendedIndicator> recommendedIndicators;

    private List<RecommendedDocument> recommendedDocuments;

    private String representativeness;

    private String newness;

    private String correctness;

    private String timeliness;

    private String userName;

    private List<Source> sources;

    private List<SignalDefinition> signalDefinitions;

    /**
     * @return
     */
    private IndicatorDB getIndicatorDB() {
        if (this.indicatorDB == null) {
            this.indicatorDB = new IndicatorDB();
        }
        return this.indicatorDB;
    }

    private DocumentDB getDocumentDB() {
        if (this.documentDB == null) {
            this.documentDB = new DocumentDB();
        }
        return this.documentDB;
    }

    @In(create = true, value = "currentRequest")
    private HttpServletRequest request;

    private List<Resource> results;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public boolean getModalRendered() {
        return modalRendered;
    }

    public void setModalRendered(boolean modalRendered) {
        this.modalRendered = modalRendered;
    }

    public void toggleModal(ActionEvent event) {
        modalRendered = !modalRendered;
    }

    public void toggleEvaluationModal(ActionEvent event) {
        modalEvaluationRendered = !modalEvaluationRendered;
    }

    /**
	 * @param query
	 * @return
	 */
    public List<SignalDefinition> getSignalDefinitions() {
        return signalDefinitions;
    }

    /**
	 * @param query
	 * @return
	 */
    public List<RecommendedIndicator> getRecommendedIndicators2() {
        this.recommendedIndicators = new ArrayList<RecommendedIndicator>();
        MecoUser mecoUser = new MecoUser();
        Indicator indicator = new Indicator(1);
        indicator.setTimeFrame("2008-2009");
        indicator.setLocationString("Mexico");
        indicator.setMedicalConditionString("Avian Flu");
        Indicator indicator2 = new Indicator(2);
        indicator2.setTimeFrame("2009-2010");
        indicator2.setLocationString("Canada");
        indicator2.setMedicalConditionString("Measles");
        Indicator indicator3 = new Indicator(3);
        indicator3.setTimeFrame("2010-2011");
        indicator3.setLocationString("USA");
        indicator3.setMedicalConditionString("Dengue Fever");
        RecommendedIndicator recommendedIndicator = new RecommendedIndicator(mecoUser, indicator, new Date(), 3f);
        RecommendedIndicator recommendedIndicator2 = new RecommendedIndicator(mecoUser, indicator2, new Date(), 5f);
        RecommendedIndicator recommendedIndicator3 = new RecommendedIndicator(mecoUser, indicator3, new Date(), 6f);
        recommendedIndicators.add(recommendedIndicator);
        recommendedIndicators.add(recommendedIndicator2);
        recommendedIndicators.add(recommendedIndicator3);
        return recommendedIndicators;
    }

    /**
	 * @param query
	 * @retur
	 */
    public List<RecommendedIndicator> getRecommendedIndicators() {
        List<RecommendedIndicator> recList = new ArrayList<RecommendedIndicator>();
        MecoUser mecoUser = this.getMecoUser();
        if (mecoUser != null) {
            this.recommendedIndicators = new ArrayList<RecommendedIndicator>();
            IndicatorDB indicatorDB = getIndicatorDB();
            recList = indicatorDB.getRecommendedIndicators(mecoUser.getId());
        }
        return recList;
    }

    public void openDocumentLink(String documentLink) {
        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "window.open('" + documentLink + "', 'Document Window');");
    }

    public void setDatabaseSchema() {
    }

    /**
	 * @return
	 */
    public MecoUser getMecoUser() {
        MecoUser mecoUser = null;
        if (this.getUserName() != null && !this.getUserName().isEmpty()) {
            mecoUser = this.getIndicatorDB().getUserByName(this.getUserName());
        }
        return mecoUser;
    }

    /**
	 * @return
	 */
    public boolean getIsAdmin() {
        boolean isAdmin = false;
        MecoUser user = this.getMecoUser();
        if (user != null) {
            isAdmin = user.isAdmin();
        }
        return isAdmin;
    }

    /**
	 * 
	 */
    public void createSignalDefinition() {
        IndicatorDB indicatorDB = this.getIndicatorDB();
        if (this.getUserName() != null && !this.getUserName().trim().isEmpty()) {
            MecoUser mecoUser = indicatorDB.getUserByName(this.getUserName());
            SignalDefinition signalDefinition = new SignalDefinition();
            signalDefinition.setUser(mecoUser);
            signalDefinition.setDescription(getDescription());
            signalDefinition.setTitle(this.getName());
            List<Location> locations = new ArrayList<Location>();
            for (int i = 0; i < this.getSelectedCars().length; i++) {
                String locationName = getSelectedCars()[i];
                Location location = indicatorDB.getLocationDB().getLocationByName(locationName);
                locations.add(location);
            }
            List<MedicalCondition> medicalConditions = new ArrayList<MedicalCondition>();
            for (int i = 0; i < this.getSelectedMedicalConditions().length; i++) {
                String medicalConditionName = getSelectedMedicalConditions()[i];
                MedicalCondition medicalCondition = indicatorDB.getMedicalConditionByName(medicalConditionName);
                medicalConditions.add(medicalCondition);
            }
            if (signalDefinition.getUser() != null) {
                indicatorDB.createSignalDefition(signalDefinition, locations, medicalConditions);
                FacesMessage facesMessage = new FacesMessage("Signal Definition Created");
                FacesContext.getCurrentInstance().addMessage("dragForm", facesMessage);
            }
        } else {
            FacesMessage facesMessage = new FacesMessage("User must be logged !");
            FacesContext.getCurrentInstance().addMessage("dragForm", facesMessage);
        }
    }

    /**
	 * Field values that come from the url query
	 */
    public void setImplicitFields() {
    }

    @SuppressWarnings("unused")
    private List<SelectItem> selectItems = null;

    /**
     * @return
     */
    public List<SelectItem> loadLocations() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        return result;
    }

    public void setResults(List<Resource> results) {
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Location> getLocations() {
        this.locations = new ArrayList<Location>();
        IndicatorDB indicatorDB = this.getIndicatorDB();
        this.locations = indicatorDB.getLocations();
        return locations;
    }

    public void signUpUser() {
        IndicatorDB indicatorDB = new IndicatorDB();
        MecoUser user = indicatorDB.getUserByName(this.getUserName());
        if (user == null) {
            indicatorDB.createUser(this.getUserName(), this.isAdmin);
            FacesMessage facesMessage = new FacesMessage("User created and logged!");
            FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
        } else {
            FacesMessage facesMessage = new FacesMessage("User logged!");
            FacesContext.getCurrentInstance().addMessage("userForm", facesMessage);
        }
    }

    /**
	 * @param idParentLocation
	 * @return
	 */
    public List<Location> getChildLocations() {
        this.childLocations = new ArrayList<Location>();
        if (this.location != null && !this.location.equals("")) {
            IndicatorDB indicatorDB = this.getIndicatorDB();
            this.childLocations = indicatorDB.getChildLocations(new Integer(this.location));
        }
        return childLocations;
    }

    /**
	 * @return
	 */
    public Converter getConverter() {
        return new Converter() {

            public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
                for (SelectItem si : getSelectLocation()) if (si.getLabel().equals(arg2)) return si.getValue();
                throw new IllegalArgumentException("Item not found: " + arg2);
            }

            public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
                for (SelectItem si : getSelectLocation()) if (si.getValue().equals(arg2)) return si.getLabel();
                throw new IllegalArgumentException("Item not found: " + arg2);
            }
        };
    }

    public List<SelectItem> getSelectChildLocation() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (Location l : this.getChildLocations()) result.add(new SelectItem(l, l.getName()));
        return result;
    }

    /**
     * @return
     */
    public List<SelectItem> getSelectLocation() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (Location l : this.getLocations()) result.add(new SelectItem(l, l.getName()));
        return result;
    }

    private String[] selectedCars;

    public SelectItem[] getCarListItems() {
        SelectItem[] carItems = new SelectItem[getLocations().size()];
        for (int i = 0; i < carItems.length; i++) {
            carItems[i] = new SelectItem(getLocations().get(i).getName());
        }
        return carItems;
    }

    private String[] selectedMedicalConditions;

    public SelectItem[] getMedicalConditionItems() {
        SelectItem[] mcs = new SelectItem[getMedicalConditions().size()];
        for (int i = 0; i < mcs.length; i++) {
            mcs[i] = new SelectItem(getMedicalConditions().get(i).getName());
        }
        return mcs;
    }

    /**
     * @return
     */
    public List<SelectItem> getSelectMedicalCondition() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (MedicalCondition l : this.getMedicalConditions()) result.add(new SelectItem(l, l.getName()));
        return result;
    }

    /**
     * @return
     */
    public List<SelectItem> getSelectSource() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (Source s : this.getSources()) result.add(new SelectItem(s, s.getName()));
        return result;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    /**
	 * @return
	 */
    public List<MedicalCondition> getMedicalConditions() {
        this.medicalConditions = new ArrayList<MedicalCondition>();
        IndicatorDB indicatorDB = this.getIndicatorDB();
        this.medicalConditions = indicatorDB.getMedicalConditions();
        return medicalConditions;
    }

    public void setMedicalConditions(List<MedicalCondition> medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    /**
	 * @param indicatorId
	 */
    public void likeIndicator(String indicatorId) {
        MecoUser mecoUser = this.getMecoUser();
        if (mecoUser != null) {
            IndicatorDB inIndicatorDB = this.getIndicatorDB();
            RecommendedIndicator recommendedIndicator = inIndicatorDB.getRecommendedIndicatorById(new Integer(indicatorId));
            inIndicatorDB.addUserIndicatorRating(recommendedIndicator.getRecommendedIndicatorId(), mecoUser, 1);
            FacesMessage facesMessage = new FacesMessage("Vote computed. You like this indicator !");
            FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
        } else {
            FacesMessage facesMessage = new FacesMessage("Please sign in man!");
            FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
        }
    }

    /**
	 * @param indicatorId
	 */
    public void dislikeIndicator(String indicatorId) {
        MecoUser mecoUser = this.getMecoUser();
        if (mecoUser != null) {
            IndicatorDB inIndicatorDB = this.getIndicatorDB();
            RecommendedIndicator recommendedIndicator = inIndicatorDB.getRecommendedIndicatorById(new Integer(indicatorId));
            inIndicatorDB.addUserIndicatorRating(recommendedIndicator.getRecommendedIndicatorId(), mecoUser, 0);
            FacesMessage facesMessage = new FacesMessage("Vote computed. You dislike this indicator !");
            FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
        } else {
            FacesMessage facesMessage = new FacesMessage("Please sign in man!");
            FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
        }
    }

    /**
	 * @param indicatorId
	 */
    public void openPopUp(String recommendedIndicatorId) {
        System.out.println(recommendedIndicatorId);
        this.setRecommendedIndicatorId(recommendedIndicatorId);
        this.setTagString(this.getIndicatorDB().getTagStringRecommendedIndicatorId(new Integer(recommendedIndicatorId)));
        setModalRendered(true);
    }

    /**
	 * @param indicatorId
	 */
    public void openEvaluationPopUp(String recommendedIndicatorId) {
        if (this.getRound() != null && !this.getRound().isEmpty()) {
            IndicatorDB indicatorDB = this.getIndicatorDB();
            this.setRecommendedIndicatorId(recommendedIndicatorId);
            this.setMedicalCondition(indicatorDB.getRecommendedIndicatorById(new Integer(recommendedIndicatorId)).getIndicator().getMedicalConditionString());
            setModalEvaluationRendered(true);
        } else {
            FacesMessage facesMessage = new FacesMessage("Please ask the system admin to set the evaluation round!");
            FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
        }
    }

    /**
	 * @param tagString
	 */
    public void addTagging(String tagString) {
        IndicatorDB indicatorDB = this.getIndicatorDB();
        indicatorDB.addTagToRecommendedIndicator(new Integer(recommendedIndicatorId), tagString);
        FacesMessage facesMessage = new FacesMessage("Tags assigned to the selected indicator!");
        FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
        this.toggleModal(null);
    }

    public static void main(String[] args) {
        SignalDefinitionAction signalDefinitionAction = new SignalDefinitionAction();
        signalDefinitionAction.calculateIndicators();
    }

    /**
	 * @param tagString
	 */
    public void calculateIndicators() {
        IndicatorDB indicatorDB = this.getIndicatorDB();
        DocumentDB documentDB = this.getDocumentDB();
        Map<Integer, Document> documents = documentDB.getDocumentsOfIndicators();
        indicatorDB.setDocuments(documents);
        SimilarityModel similarityModel = new SimilarityModel();
        List<MecoUser> users = indicatorDB.getMecoUserDB().getMecoUsers();
        int i = 1;
        for (MecoUser user : users) {
            System.out.println("Computing recommendations of user " + user.getName() + "(" + i++ + " out of " + users.size() + ")...");
            List<Indicator> indicators = indicatorDB.getNonRecommendedIndicators(user);
            for (Indicator indicator : indicators) {
                double similarityScore = 0d;
                similarityScore = similarityModel.calculateSimilarityScore(user.getTfdf(), indicator.getTfdf());
                if (similarityScore > 0f) {
                    System.out.println(new Float(similarityScore));
                    indicatorDB.saveSimilarityUserIndicator(user.getId(), indicator.getId(), new Float(similarityScore));
                }
            }
        }
        calculateRecommendedDocuments();
        FacesMessage facesMessage = new FacesMessage("Recommended Indicators and Documents Calculated !");
        FacesContext.getCurrentInstance().addMessage("admForm", facesMessage);
    }

    /**
	 * 
	 */
    public void calculateRecommendedDocuments() {
        IndicatorDB indicatorDB = this.getIndicatorDB();
        SimilarityModel similarityModel = new SimilarityModel();
        List<MecoUser> users = indicatorDB.getMecoUserDB().getMecoUsers();
        for (MecoUser user : users) {
            List<RecommendedIndicator> recommendedIndicators = indicatorDB.getRecommendedIndicators(user.getId());
            for (RecommendedIndicator recommendedIndicator : recommendedIndicators) {
                Indicator indicator = recommendedIndicator.getIndicator();
                double similarityScore = 0d;
                System.out.println("Calculating Document Similarity of recommended indicators");
                List<Document> documents = indicatorDB.getDocumentsByIndicatorId(indicator.getId());
                for (Document document : documents) {
                    similarityScore = similarityModel.calculateSimilarityScore(user.getTfdf(), document.getTermFrequency());
                    if (similarityScore > 0f) {
                        System.out.println(" Saving Document Similarity Score: " + similarityScore);
                        indicatorDB.saveSimilarityUserDocument(user.getId(), recommendedIndicator.getRecommendedIndicatorId(), document.getDocumentId(), new Float(similarityScore));
                    }
                }
            }
        }
    }

    /**
	 * @param indicatorId
	 * @return
	 */
    public List<RecommendedDocument> loadRecommendations2(String indicatorId) {
        recommendedDocuments = new ArrayList<RecommendedDocument>();
        Document document = new Document("2006", "Varize comendo souto", "http://www.cnn.com", "x");
        Document document2 = new Document("2006", "Surto de cloera", "http://www.cnn.com", "x");
        Document document3 = new Document("2006", "Fudeu geral agora", "http://www.cnn.com", "x");
        Document document4 = new Document("2006", "acou o sono", "http://www.cnn.com", "x");
        RecommendedDocument recommendedDocument = new RecommendedDocument();
        recommendedDocument.setDateRecommendationString("2006");
        recommendedDocument.setDocument(document);
        RecommendedDocument recommendedDocument2 = new RecommendedDocument();
        recommendedDocument2.setDateRecommendationString("2006");
        recommendedDocument2.setDocument(document2);
        RecommendedDocument recommendedDocument3 = new RecommendedDocument();
        recommendedDocument3.setDateRecommendationString("2006");
        recommendedDocument3.setDocument(document3);
        RecommendedDocument recommendedDocument4 = new RecommendedDocument();
        recommendedDocument4.setDateRecommendationString("2006");
        recommendedDocument4.setDocument(document4);
        recommendedDocuments.add(recommendedDocument);
        recommendedDocuments.add(recommendedDocument2);
        recommendedDocuments.add(recommendedDocument3);
        recommendedDocuments.add(recommendedDocument4);
        return recommendedDocuments;
    }

    /**
	 * @param indicatorId
	 * @return
	 */
    public List<RecommendedDocument> loadRecommendations(String indicatorId) {
        int indId = new Integer(indicatorId).intValue();
        recommendedDocuments = new ArrayList<RecommendedDocument>();
        IndicatorDB indicatorDB = this.getIndicatorDB();
        recommendedDocuments = indicatorDB.getRecommendedDocumentsByRecommendedIndicatorId(indId);
        return recommendedDocuments;
    }

    public List<Source> getSources() {
        this.sources = new ArrayList<Source>();
        IndicatorDB indicatorDB = this.getIndicatorDB();
        this.sources = indicatorDB.getSources();
        return sources;
    }

    public void processValueChange(ValueChangeEvent valueChangeEvent) {
        this.location = valueChangeEvent.getNewValue().toString();
        System.out.println(this.location);
        this.getSelectChildLocation();
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public void setSignalDefinitions(List<meco.SignalDefinition> signalDefinitions) {
        this.signalDefinitions = signalDefinitions;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Variables to store the button clicked and the input submitted.
     */
    private String clicked;

    private String inputText;

    /**
     * Gets the name of the button clicked.
     *
     * @return name of the button clicked.
     */
    public String getClicked() {
        return MessageBundleLoader.getMessage(clicked);
    }

    /**
     * Gets the value of the input submitted.
     *
     * @return the value of the input submitted.
     */
    public String getInputText() {
        return inputText;
    }

    /**
     * Sets the input text value.
     *
     * @param newValue input text value.
     */
    public void setInputText(String newValue) {
        inputText = newValue;
    }

    /**
     * Listener for the submit button click action.
     *
     * @param e click action event.
     */
    public void submitButtonListener(ActionEvent e) {
        clicked = "bean.buttonsAndLinks.submitButton";
        valueChangeEffect.setFired(false);
    }

    /**
     * Listener for the image button click action.
     *
     * @param e click action event.
     */
    public void imageButtonListener(ActionEvent e) {
        clicked = "bean.buttonsAndLinks.imageButton";
        valueChangeEffect.setFired(false);
    }

    /**
     * Listener for the command link click action.
     *
     * @param e click action event.
     */
    public void commandLinkListener(ActionEvent e) {
        clicked = "bean.buttonsAndLinks.commandLink";
        valueChangeEffect.setFired(false);
    }

    public void setRecommendedIndicators(List<RecommendedIndicator> recommendedIndicators) {
        this.recommendedIndicators = recommendedIndicators;
    }

    public String getRecommenderIndicatorSelected() {
        return recommenderIndicatorSelected;
    }

    public void setRecommenderIndicatorSelected(String recommenderIndicatorSelected) {
        this.recommenderIndicatorSelected = recommenderIndicatorSelected;
    }

    public List<RecommendedDocument> getRecommendedDocuments() {
        return recommendedDocuments;
    }

    public void setRecommendedDocuments(List<RecommendedDocument> recommendedDocuments) {
        this.recommendedDocuments = recommendedDocuments;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public String getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(String indicatorId) {
        this.indicatorId = indicatorId;
    }

    public boolean isModalEvaluationRendered() {
        return modalEvaluationRendered;
    }

    public void setModalEvaluationRendered(boolean modalEvaluationRendered) {
        this.modalEvaluationRendered = modalEvaluationRendered;
    }

    public String getRecommendedIndicatorId() {
        return recommendedIndicatorId;
    }

    public void setRecommendedIndicatorId(String recommendedIndicatorId) {
        this.recommendedIndicatorId = recommendedIndicatorId;
    }

    /**
	 * 
	 */
    public void saveEvaluation(String recommendedIndicatorId) {
        if (this.getRound() != null && !this.getRound().isEmpty()) {
            MecoUser mecoUser = this.getMecoUser();
            if (mecoUser != null) {
                IndicatorDB indicatorDB = this.getIndicatorDB();
                RecommendedIndicator recommendedIndicator = indicatorDB.getRecommendedIndicatorById(new Integer(recommendedIndicatorId));
                MecoEvaluation mecoEvaluation = new MecoEvaluation();
                mecoEvaluation.setRound(this.getRound());
                mecoEvaluation.setCorrectness(this.getCorrectness());
                mecoEvaluation.setNewness(this.getNewness());
                mecoEvaluation.setTimeliness(this.getTimeliness());
                mecoEvaluation.setRepresentativeness(this.getRepresentativeness());
                mecoEvaluation.setMecoUser(mecoUser);
                mecoEvaluation.setRecommendedIndicator(recommendedIndicator);
                indicatorDB.saveMecoEvaluation(mecoEvaluation);
                FacesMessage facesMessage = new FacesMessage("Evaluation finalized !");
                FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
                this.toggleEvaluationModal(null);
            } else {
                FacesMessage facesMessage = new FacesMessage("Please sign in man!");
                FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
            }
        } else {
            FacesMessage facesMessage = new FacesMessage("Please ask the system admin to set the evaluation round!");
            FacesContext.getCurrentInstance().addMessage("evaluationForm", facesMessage);
        }
    }

    /**
	 * 
	 */
    public void confirmRound(String round) {
        this.setRound(round);
        FacesMessage facesMessage = new FacesMessage("Round set !");
        FacesContext.getCurrentInstance().addMessage("admForm", facesMessage);
    }

    public String getRepresentativeness() {
        return representativeness;
    }

    public void setRepresentativeness(String representativeness) {
        this.representativeness = representativeness;
    }

    public String getNewness() {
        return newness;
    }

    public void setNewness(String newness) {
        this.newness = newness;
    }

    public String getCorrectness() {
        return correctness;
    }

    public void setCorrectness(String correctness) {
        this.correctness = correctness;
    }

    public String getTimeliness() {
        return timeliness;
    }

    public void setTimeliness(String timeliness) {
        this.timeliness = timeliness;
    }

    public void setChildLocations(List<Location> childLocations) {
        this.childLocations = childLocations;
    }

    public String getChildLocation() {
        return childLocation;
    }

    public void setChildLocation(String childLocation) {
        this.childLocation = childLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getSelectedCars() {
        return selectedCars;
    }

    public void setSelectedCars(String[] selectedCars) {
        this.selectedCars = selectedCars;
    }

    public void carChanged(ValueChangeEvent event) {
        valueChangeEffect.setFired(false);
    }

    public void medicalConditionChanged(ValueChangeEvent event) {
        valueChangeEffect.setFired(false);
    }

    public String[] getSelectedMedicalConditions() {
        return selectedMedicalConditions;
    }

    public void setSelectedMedicalConditions(String[] selectedMedicalConditions) {
        this.selectedMedicalConditions = selectedMedicalConditions;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
