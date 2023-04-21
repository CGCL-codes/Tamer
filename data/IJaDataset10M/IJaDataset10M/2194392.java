package gate.creole.coref;

import java.util.*;
import gate.*;
import gate.creole.*;
import gate.util.*;

public class NominalCoref extends AbstractCoreferencer implements ProcessingResource, ANNIEConstants {

    public static final String COREF_DOCUMENT_PARAMETER_NAME = "document";

    public static final String COREF_ANN_SET_PARAMETER_NAME = "annotationSetName";

    /** --- */
    private static final boolean DEBUG = false;

    private static final String PERSON_CATEGORY = "Person";

    private static final String JOBTITLE_CATEGORY = "JobTitle";

    private static final String ORGANIZATION_CATEGORY = "Organization";

    private static final String LOOKUP_CATEGORY = "Lookup";

    private static final String ORGANIZATION_NOUN_CATEGORY = "organization_noun";

    /** --- */
    private String annotationSetName;

    /** --- */
    private AnnotationSet defaultAnnotations;

    /** --- */
    private HashMap anaphor2antecedent;

    /** --- */
    public NominalCoref() {
        super("NOMINAL");
        this.anaphor2antecedent = new HashMap();
    }

    /** Initialise this resource, and return it. */
    public Resource init() throws ResourceInstantiationException {
        return super.init();
    }

    /**
   * Reinitialises the processing resource. After calling this method the
   * resource should be in the state it is after calling init.
   * If the resource depends on external resources (such as rules files) then
   * the resource will re-read those resources. If the data used to create
   * the resource has changed since the resource has been created then the
   * resource will change too after calling reInit().
  */
    public void reInit() throws ResourceInstantiationException {
        this.anaphor2antecedent = new HashMap();
        init();
    }

    /** Set the document to run on. */
    public void setDocument(Document newDocument) {
        super.setDocument(newDocument);
    }

    /** --- */
    public void setAnnotationSetName(String annotationSetName) {
        this.annotationSetName = annotationSetName;
    }

    /** --- */
    public String getAnnotationSetName() {
        return annotationSetName;
    }

    /**
   * This method runs the coreferencer. It assumes that all the needed parameters
   * are set. If they are not, an exception will be fired.
   *
   * The process goes like this:
   * - Create a sorted list of Person and JobTitle annotations.
   * - Loop through the annotations
   *    If it is a Person, we add it to the top of a stack.
   *    If it is a job title, we subject it to a series of tests. If it 
   *      passes, we associate it with the Person annotation at the top
   *      of the stack
   */
    public void execute() throws ExecutionException {
        Annotation[] nominalArray;
        if (null == this.document) {
            throw new ExecutionException("[coreference] Document is not set!");
        }
        preprocess();
        Annotation[] tokens = defaultAnnotations.get(TOKEN_ANNOTATION_TYPE).toArray(new Annotation[0]);
        java.util.Arrays.sort(tokens, new OffsetComparator());
        int currentToken = 0;
        HashSet personConstraint = new HashSet();
        personConstraint.add(PERSON_CATEGORY);
        AnnotationSet people = this.defaultAnnotations.get(personConstraint);
        HashSet jobTitleConstraint = new HashSet();
        jobTitleConstraint.add(JOBTITLE_CATEGORY);
        AnnotationSet jobTitles = this.defaultAnnotations.get(jobTitleConstraint);
        FeatureMap orgNounConstraint = new SimpleFeatureMapImpl();
        orgNounConstraint.put(LOOKUP_MAJOR_TYPE_FEATURE_NAME, ORGANIZATION_NOUN_CATEGORY);
        AnnotationSet orgNouns = this.defaultAnnotations.get(LOOKUP_CATEGORY, orgNounConstraint);
        HashSet orgConstraint = new HashSet();
        orgConstraint.add(ORGANIZATION_CATEGORY);
        AnnotationSet organizations = this.defaultAnnotations.get(orgConstraint);
        Set<Annotation> nominals = new HashSet();
        if (people != null) {
            nominals.addAll(people);
        }
        if (jobTitles != null) {
            nominals.addAll(jobTitles);
        }
        if (orgNouns != null) {
            nominals.addAll(orgNouns);
        }
        if (organizations != null) {
            nominals.addAll(organizations);
        }
        nominalArray = nominals.toArray(new Annotation[0]);
        java.util.Arrays.sort(nominalArray, new OffsetComparator());
        ArrayList<Annotation> previousPeople = new ArrayList<Annotation>();
        ArrayList<Annotation> previousOrgs = new ArrayList<Annotation>();
        for (int i = 0; i < nominalArray.length; i++) {
            Annotation nominal = (Annotation) nominalArray[i];
            currentToken = advanceTokenPosition(nominal, currentToken, tokens);
            if (nominal.getType().equals(PERSON_CATEGORY)) {
                Object[] personTokens = getSortedTokens(nominal);
                if (personTokens.length == 1) {
                    Annotation personToken = (Annotation) personTokens[0];
                    String personCategory = (String) personToken.getFeatures().get(TOKEN_CATEGORY_FEATURE_NAME);
                    if (personCategory.equals("PP") || personCategory.equals("PRP") || personCategory.equals("PRP$") || personCategory.equals("PRPR$")) {
                        continue;
                    }
                }
                previousPeople.add(0, nominal);
            } else if (nominal.getType().equals(JOBTITLE_CATEGORY)) {
                Object[] jobTitleTokens = getSortedTokens(nominal);
                Annotation lastToken = (Annotation) jobTitleTokens[jobTitleTokens.length - 1];
                String tokenCategory = (String) lastToken.getFeatures().get(TOKEN_CATEGORY_FEATURE_NAME);
                if (!tokenCategory.equals("NN")) {
                    continue;
                }
                if (overlapsAnnotations(nominal, people)) {
                    continue;
                }
                Annotation previousToken;
                String previousValue;
                if (currentToken != 0) {
                    previousToken = (Annotation) tokens[currentToken - 1];
                    previousValue = (String) previousToken.getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                    if (previousValue.equalsIgnoreCase("a") || previousValue.equalsIgnoreCase("an") || previousValue.equalsIgnoreCase("other") || previousValue.equalsIgnoreCase("another")) {
                        continue;
                    }
                }
                if (i < nominalArray.length - 1) {
                    Annotation nextAnnotation = (Annotation) nominalArray[i + 1];
                    if (nextAnnotation.getType().equals(PERSON_CATEGORY)) {
                        previousToken = (Annotation) tokens[currentToken - 1];
                        previousValue = (String) previousToken.getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                        int interveningTokens = countInterveningTokens(nominal, nextAnnotation, currentToken, tokens);
                        if (interveningTokens == 0 && !previousValue.equalsIgnoreCase("the")) {
                            continue;
                        } else if (interveningTokens == 1) {
                            String tokenString = (String) getFollowingToken(nominal, currentToken, tokens).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                            if (!tokenString.equals(",") && !tokenString.equals("-")) {
                                continue;
                            }
                        }
                        anaphor2antecedent.put(nominal, nextAnnotation);
                        continue;
                    }
                }
                if (previousPeople.size() == 0) {
                    FeatureMap personFeatures = new SimpleFeatureMapImpl();
                    personFeatures.put("ENTITY_MENTION_TYPE", "NOMINAL");
                    this.defaultAnnotations.add(nominal.getStartNode(), nominal.getEndNode(), PERSON_CATEGORY, personFeatures);
                    continue;
                }
                int personIndex = 0;
                Annotation previousPerson = (Annotation) previousPeople.get(personIndex);
                String personGender = (String) previousPerson.getFeatures().get(PERSON_GENDER_FEATURE_NAME);
                String jobTitleGender = (String) nominal.getFeatures().get(PERSON_GENDER_FEATURE_NAME);
                if (personGender != null && jobTitleGender != null) {
                    if (!personGender.equals(jobTitleGender)) {
                        continue;
                    }
                }
                anaphor2antecedent.put(nominal, previousPerson);
            } else if (nominal.getType().equals(ORGANIZATION_CATEGORY)) {
                previousOrgs.add(0, nominal);
            } else if (nominal.getType().equals(LOOKUP_CATEGORY)) {
                if (previousOrgs.size() == 0) {
                    continue;
                }
                Annotation[] orgNounTokens = this.defaultAnnotations.get(TOKEN_ANNOTATION_TYPE, nominal.getStartNode().getOffset(), nominal.getEndNode().getOffset()).toArray(new Annotation[0]);
                java.util.Arrays.sort(orgNounTokens, new OffsetComparator());
                Annotation lastToken = (Annotation) orgNounTokens[orgNounTokens.length - 1];
                if (!lastToken.getFeatures().get(TOKEN_CATEGORY_FEATURE_NAME).equals("NN")) {
                    continue;
                }
                anaphor2antecedent.put(nominal, previousOrgs.get(0));
            }
        }
        generateCorefChains(anaphor2antecedent);
    }

    /**
   * This method specifies whether a given annotation overlaps any of a 
   * set of annotations. For instance, JobTitles occasionally are
   * part of Person annotations.
   * 
   */
    private boolean overlapsAnnotations(Annotation a, AnnotationSet annotations) {
        Iterator<Annotation> iter = annotations.iterator();
        while (iter.hasNext()) {
            Annotation current = iter.next();
            if (a.overlaps(current)) {
                return true;
            }
        }
        return false;
    }

    /** Use this method to keep the current token pointer at the right point
   * in the token list */
    private int advanceTokenPosition(Annotation target, int currentPosition, Object[] tokens) {
        long targetOffset = target.getStartNode().getOffset().longValue();
        long currentOffset = ((Annotation) tokens[currentPosition]).getStartNode().getOffset().longValue();
        if (targetOffset > currentOffset) {
            while (targetOffset > currentOffset) {
                currentPosition++;
                currentOffset = ((Annotation) tokens[currentPosition]).getStartNode().getOffset().longValue();
            }
        } else if (targetOffset < currentOffset) {
            while (targetOffset < currentOffset) {
                currentPosition--;
                currentOffset = ((Annotation) tokens[currentPosition]).getStartNode().getOffset().longValue();
            }
        }
        return currentPosition;
    }

    /** Return the number of tokens between the end of annotation 1 and the
   * beginning of annotation 2. Will return 0 if they are not in order */
    private int countInterveningTokens(Annotation first, Annotation second, int currentPosition, Object[] tokens) {
        int interveningTokens = 0;
        long startOffset = first.getEndNode().getOffset().longValue();
        long endOffset = second.getStartNode().getOffset().longValue();
        long currentOffset = ((Annotation) tokens[currentPosition]).getStartNode().getOffset().longValue();
        while (currentOffset < endOffset) {
            if (currentOffset >= startOffset) {
                interveningTokens++;
            }
            currentPosition++;
            currentOffset = ((Annotation) tokens[currentPosition]).getStartNode().getOffset().longValue();
        }
        return interveningTokens;
    }

    /** Get the next token after an annotation */
    private Annotation getFollowingToken(Annotation current, int currentPosition, Object[] tokens) {
        long endOffset = current.getEndNode().getOffset().longValue();
        long currentOffset = ((Annotation) tokens[currentPosition]).getStartNode().getOffset().longValue();
        while (currentOffset < endOffset) {
            currentPosition++;
            currentOffset = ((Annotation) tokens[currentPosition]).getStartNode().getOffset().longValue();
        }
        return (Annotation) tokens[currentPosition];
    }

    /** Get the text of an annotation */
    private String stringValue(Annotation ann) {
        Object[] tokens = getSortedTokens(ann);
        StringBuffer output = new StringBuffer();
        for (int i = 0; i < tokens.length; i++) {
            Annotation token = (Annotation) tokens[i];
            output.append(token.getFeatures().get(TOKEN_STRING_FEATURE_NAME));
            if (i < tokens.length - 1) {
                output.append(" ");
            }
        }
        return output.toString();
    }

    /** Get a sorted array of the tokens that make up a given annotation. */
    private Annotation[] getSortedTokens(Annotation a) {
        Annotation[] annotationTokens = this.defaultAnnotations.get(TOKEN_ANNOTATION_TYPE, a.getStartNode().getOffset(), a.getEndNode().getOffset()).toArray(new Annotation[0]);
        java.util.Arrays.sort(annotationTokens, new OffsetComparator());
        return annotationTokens;
    }

    /** --- */
    public HashMap getResolvedAnaphora() {
        return this.anaphor2antecedent;
    }

    /** --- */
    private void preprocess() throws ExecutionException {
        this.anaphor2antecedent.clear();
        if (this.annotationSetName == null || this.annotationSetName.equals("")) {
            this.defaultAnnotations = this.document.getAnnotations();
        } else {
            this.defaultAnnotations = this.document.getAnnotations(annotationSetName);
        }
        if (this.defaultAnnotations == null || this.defaultAnnotations.isEmpty()) {
            Err.prln("Coref Warning: No annotations found for processing!");
            return;
        }
    }
}
