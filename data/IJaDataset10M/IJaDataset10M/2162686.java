package gate.creole.orthomatcher;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import gate.*;
import gate.creole.*;
import gate.util.*;

public class OrthoMatcher extends AbstractLanguageAnalyser {

    protected static final Logger log = Logger.getLogger(OrthoMatcher.class);

    public static final boolean DEBUG = false;

    public static final String OM_DOCUMENT_PARAMETER_NAME = "document";

    public static final String OM_ANN_SET_PARAMETER_NAME = "annotationSetName";

    public static final String OM_CASE_SENSITIVE_PARAMETER_NAME = "caseSensitive";

    public static final String OM_ANN_TYPES_PARAMETER_NAME = "annotationTypes";

    public static final String OM_ORG_TYPE_PARAMETER_NAME = "organizationType";

    public static final String OM_PERSON_TYPE_PARAMETER_NAME = "personType";

    public static final String OM_EXT_LISTS_PARAMETER_NAME = "extLists";

    protected static final String CDGLISTNAME = "cdg";

    protected static final String ALIASLISTNAME = "alias";

    protected static final String ARTLISTNAME = "def_art";

    protected static final String PREPLISTNAME = "prepos";

    protected static final String CONNECTORLISTNAME = "connector";

    protected static final String SPURLISTNAME = "spur_match";

    protected static final String PUNCTUATION_VALUE = "punctuation";

    protected static final String THE_VALUE = "The";

    /**the name of the annotation set*/
    protected String annotationSetName;

    /** the types of the annotation */
    protected List annotationTypes = new ArrayList(10);

    /** the organization type*/
    protected String organizationType = ORGANIZATION_ANNOTATION_TYPE;

    /** the person type*/
    protected String personType = PERSON_ANNOTATION_TYPE;

    protected String unknownType = "Unknown";

    /** internal or external list */
    protected boolean extLists = true;

    /** Use only high precision rules for Organizations */
    protected Boolean highPrecisionOrgs = false;

    /** matching unknowns or not*/
    protected boolean matchingUnknowns = true;

    /** This is an internal variable to indicate whether
   *  we matched using a rule that requires that
   *  the newly matched annotation matches all the others
   *  This is needed, because organizations can share
   *  first/last tokens like News and be different
   */
    protected boolean allMatchingNeeded = false;

    protected boolean caseSensitive = false;

    protected HashMap alias = new HashMap(100);

    protected HashSet cdg = new HashSet();

    protected HashMap spur_match = new HashMap(100);

    protected HashMap def_art = new HashMap(20);

    protected HashMap connector = new HashMap(20);

    protected HashMap prepos = new HashMap(30);

    protected AnnotationSet nameAllAnnots = null;

    protected HashMap processedAnnots = new HashMap(150);

    protected HashMap annots2Remove = new HashMap(75);

    protected List matchesDocFeature = new ArrayList();

    protected HashMap tokensMap = new HashMap(150);

    public HashMap getTokensMap() {
        return tokensMap;
    }

    protected HashMap normalizedTokensMap = new HashMap(150);

    protected Annotation shortAnnot;

    protected Annotation longAnnot;

    protected ArrayList<Annotation> tokensLongAnnot;

    protected ArrayList<Annotation> tokensShortAnnot;

    protected ArrayList<Annotation> normalizedTokensLongAnnot, normalizedTokensShortAnnot;

    /**
   * URL to the file containing the definition for this orthomatcher
   */
    private java.net.URL definitionFileURL;

    private Double minimumNicknameLikelihood;

    /** The encoding used for the definition file and associated lists.*/
    private String encoding;

    private Map<Integer, OrthoMatcherRule> rules = new HashMap<Integer, OrthoMatcherRule>();

    /** to be initialized in init() */
    private AnnotationOrthography orthoAnnotation;

    public OrthoMatcher() {
        annotationTypes.add(organizationType);
        annotationTypes.add(personType);
        annotationTypes.add("Location");
        annotationTypes.add("Date");
    }

    /** Initialise the rules. The orthomatcher loads its build-in rules. */
    private void initRules() {
        rules.put(0, new MatchRule0(this));
        rules.put(1, new MatchRule1(this));
        rules.put(2, new MatchRule2(this));
        rules.put(3, new MatchRule3(this));
        rules.put(4, new MatchRule4(this));
        rules.put(5, new MatchRule5(this));
        rules.put(6, new MatchRule6(this));
        rules.put(7, new MatchRule7(this));
        rules.put(8, new MatchRule8(this));
        rules.put(9, new MatchRule9(this));
        rules.put(10, new MatchRule10(this));
        rules.put(11, new MatchRule11(this));
        rules.put(12, new MatchRule12(this));
        rules.put(13, new MatchRule13(this));
        rules.put(14, new MatchRule14(this));
        rules.put(15, new MatchRule15(this));
        rules.put(16, new MatchRule16(this));
        rules.put(17, new MatchRule17(this));
    }

    /** Override this method to add, replace, remove rules */
    protected void modifyRules(Map<Integer, OrthoMatcherRule> rules) {
    }

    /** Initialise this resource, and return it. */
    public Resource init() throws ResourceInstantiationException {
        if (definitionFileURL == null) {
            throw new ResourceInstantiationException("No URL provided for the definition file!");
        }
        String nicknameFile = null;
        try {
            BufferedReader reader = new BomStrippingInputStreamReader(definitionFileURL.openStream(), encoding);
            String lineRead = null;
            while ((lineRead = reader.readLine()) != null) {
                int index = lineRead.indexOf(":");
                if (index != -1) {
                    String nameFile = lineRead.substring(0, index);
                    String nameList = lineRead.substring(index + 1, lineRead.length());
                    if (nameList.equals("nickname")) {
                        if (minimumNicknameLikelihood == null) {
                            throw new ResourceInstantiationException("No value for the required parameter " + "minimumNicknameLikelihood!");
                        }
                        nicknameFile = nameFile;
                    } else {
                        createAnnotList(nameFile, nameList);
                    }
                }
            }
            reader.close();
            URL nicknameURL = null;
            if (nicknameFile != null) nicknameURL = new URL(definitionFileURL, nicknameFile);
            this.orthoAnnotation = new BasicAnnotationOrthography(personType, extLists, unknownType, nicknameURL, minimumNicknameLikelihood, encoding);
            initRules();
            modifyRules(rules);
        } catch (IOException ioe) {
            throw new ResourceInstantiationException(ioe);
        }
        return this;
    }

    /**  Run the resource. It doesn't make sense not to override
   *  this in subclasses so the default implementation signals an
   *  exception.
   */
    public void execute() throws ExecutionException {
        try {
            if (document == null) {
                throw new ExecutionException("No document for namematch!");
            }
            fireStatusChanged("OrthoMatcher processing: " + document.getName());
            if ((annotationSetName == null) || (annotationSetName.equals(""))) nameAllAnnots = document.getAnnotations(); else nameAllAnnots = document.getAnnotations(annotationSetName);
            if ((nameAllAnnots == null) || nameAllAnnots.isEmpty()) {
                Out.prln("OrthoMatcher Warning: No annotations found for processing");
                return;
            }
            docCleanup();
            Map matchesMap = (Map) document.getFeatures().get(DOCUMENT_COREF_FEATURE_NAME);
            if (!extLists) cdg = orthoAnnotation.buildTables(nameAllAnnots);
            matchNameAnnotations();
            if (!matchesDocFeature.isEmpty()) {
                if (matchesMap == null) {
                    matchesMap = new HashMap();
                }
                matchesMap.put(nameAllAnnots.getName(), matchesDocFeature);
                document.getFeatures().put(DOCUMENT_COREF_FEATURE_NAME, matchesMap);
                matchesDocFeature = new ArrayList();
                fireStatusChanged("OrthoMatcher completed");
            }
        } finally {
            nameAllAnnots = null;
            processedAnnots.clear();
            annots2Remove.clear();
            tokensMap.clear();
            normalizedTokensMap.clear();
            matchesDocFeature = new ArrayList();
            longAnnot = null;
            shortAnnot = null;
            tokensLongAnnot = null;
            tokensShortAnnot = null;
        }
    }

    protected void matchNameAnnotations() throws ExecutionException {
        Iterator iterAnnotationTypes = annotationTypes.iterator();
        while (iterAnnotationTypes.hasNext()) {
            String annotationType = (String) iterAnnotationTypes.next();
            AnnotationSet nameAnnots = nameAllAnnots.get(annotationType);
            if (nameAnnots.isEmpty()) continue;
            AnnotationSet tokensNameAS = nameAllAnnots.get(TOKEN_ANNOTATION_TYPE);
            if (tokensNameAS.isEmpty()) continue;
            ArrayList<Annotation> sortedNameAnnots = new ArrayList<Annotation>(nameAnnots);
            Collections.<Annotation>sort(sortedNameAnnots, new OffsetComparator());
            for (int snaIndex = 0; snaIndex < sortedNameAnnots.size(); snaIndex++) {
                Annotation tempAnnot = sortedNameAnnots.get(snaIndex);
                Annotation nameAnnot = nameAllAnnots.get(tempAnnot.getId());
                Integer id = nameAnnot.getId();
                String annotString = orthoAnnotation.getStringForAnnotation(nameAnnot, document);
                if (!caseSensitive) annotString = annotString.toLowerCase();
                if (DEBUG) {
                    if (log.isDebugEnabled()) {
                        log.debug("Now processing the annotation:  " + orthoAnnotation.getStringForAnnotation(nameAnnot, document) + " Id: " + nameAnnot.getId() + " Type: " + nameAnnot.getType() + " Offset: " + nameAnnot.getStartNode().getOffset());
                    }
                }
                List tokens = new ArrayList(tokensNameAS.getContained(nameAnnot.getStartNode().getOffset(), nameAnnot.getEndNode().getOffset()));
                if (tokens.isEmpty()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Didn't find any tokens for the following annotation.  We will be unable to perform coref on this annotation.  \n String:  " + orthoAnnotation.getStringForAnnotation(nameAnnot, document) + " Id: " + nameAnnot.getId() + " Type: " + nameAnnot.getType());
                    }
                    continue;
                }
                Collections.sort(tokens, new gate.util.OffsetComparator());
                tokensMap.put(nameAnnot.getId(), tokens);
                normalizedTokensMap.put(nameAnnot.getId(), new ArrayList<Annotation>(tokens));
                if (processedAnnots.containsValue(annotString) && (!(nameAnnot.getType().equals(personType) && (tokens.size() == 1)))) {
                    Annotation returnAnnot = orthoAnnotation.updateMatches(nameAnnot, annotString, processedAnnots, nameAllAnnots, matchesDocFeature);
                    if (returnAnnot != null) {
                        if (DEBUG) {
                            if (log.isDebugEnabled()) {
                                log.debug("Exact match criteria matched " + annotString + " from (id: " + nameAnnot.getId() + ", offset: " + nameAnnot.getStartNode().getOffset() + ") to " + "(id: " + returnAnnot.getId() + ", offset: " + returnAnnot.getStartNode().getOffset() + ")");
                            }
                        }
                        processedAnnots.put(nameAnnot.getId(), annotString);
                        continue;
                    }
                } else if (processedAnnots.isEmpty()) {
                    processedAnnots.put(nameAnnot.getId(), annotString);
                    continue;
                }
                if (nameAnnot.getType().equals(personType)) {
                    annotString = orthoAnnotation.stripPersonTitle(annotString, nameAnnot, document, tokensMap, normalizedTokensMap, nameAllAnnots);
                    normalizePersonName(nameAnnot);
                } else if (nameAnnot.getType().equals(organizationType)) annotString = normalizeOrganizationName(annotString, nameAnnot);
                if (null == annotString || "".equals(annotString) || tokens.isEmpty()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Annotation ID " + nameAnnot.getId() + " of type" + nameAnnot.getType() + " refers to a null or empty string or one with no tokens after normalization.  Unable to process further.");
                    }
                    continue;
                }
                matchWithPrevious(nameAnnot, annotString, sortedNameAnnots, snaIndex);
                processedAnnots.put(nameAnnot.getId(), annotString);
            }
            if (matchingUnknowns) {
                matchUnknown(sortedNameAnnots);
            }
        }
    }

    protected void matchUnknown(ArrayList<Annotation> sortedAnnotationsForAType) throws ExecutionException {
        AnnotationSet unknownAnnots = nameAllAnnots.get(unknownType);
        annots2Remove.clear();
        if (unknownAnnots.isEmpty()) return;
        AnnotationSet nameAllTokens = nameAllAnnots.get(TOKEN_ANNOTATION_TYPE);
        if (nameAllTokens.isEmpty()) return;
        Iterator<Annotation> iter = unknownAnnots.iterator();
        while (iter.hasNext()) {
            Annotation unknown = iter.next();
            String unknownString = orthoAnnotation.getStringForAnnotation(unknown, document);
            if (!caseSensitive) unknownString = unknownString.toLowerCase();
            List tokens = new ArrayList((Set) nameAllTokens.getContained(unknown.getStartNode().getOffset(), unknown.getEndNode().getOffset()));
            if (tokens.isEmpty()) continue;
            Collections.sort(tokens, new gate.util.OffsetComparator());
            tokensMap.put(unknown.getId(), tokens);
            normalizedTokensMap.put(unknown.getId(), tokens);
            if (processedAnnots.containsValue(unknownString)) {
                Annotation matchedAnnot = orthoAnnotation.updateMatches(unknown, unknownString, processedAnnots, nameAllAnnots, matchesDocFeature);
                if (matchedAnnot == null) {
                    log.info("Orthomatcher: Unable to find the annotation: " + orthoAnnotation.getStringForAnnotation(unknown, document) + " in matchUnknown");
                } else {
                    if (matchedAnnot.getType().equals(unknownType)) {
                        annots2Remove.put(unknown.getId(), annots2Remove.get(matchedAnnot.getId()));
                    } else annots2Remove.put(unknown.getId(), matchedAnnot.getType());
                    processedAnnots.put(unknown.getId(), unknownString);
                    unknown.getFeatures().put("NMRule", unknownType);
                    continue;
                }
            }
            if (tokens.size() == 1 && "hyphen".equals(unknown.getFeatures().get(TOKEN_KIND_FEATURE_NAME))) {
                if (matchHyphenatedUnknowns(unknown, unknownString, iter)) continue;
            }
            matchWithPrevious(unknown, unknownString, sortedAnnotationsForAType, sortedAnnotationsForAType.size() - 1);
        }
        if (!annots2Remove.isEmpty()) {
            Iterator unknownIter = annots2Remove.keySet().iterator();
            while (unknownIter.hasNext()) {
                Integer unknId = (Integer) unknownIter.next();
                Annotation unknown = nameAllAnnots.get(unknId);
                Integer newID = nameAllAnnots.add(unknown.getStartNode(), unknown.getEndNode(), (String) annots2Remove.get(unknId), unknown.getFeatures());
                nameAllAnnots.remove(unknown);
                List mList = (List) unknown.getFeatures().get(ANNOTATION_COREF_FEATURE_NAME);
                mList.remove(unknId);
                mList.add(newID);
            }
        }
    }

    private boolean matchHyphenatedUnknowns(Annotation unknown, String unknownString, Iterator iter) {
        boolean matched = false;
        int stringEnd = unknownString.indexOf("-");
        unknownString = unknownString.substring(0, stringEnd);
        if (processedAnnots.containsValue(unknownString)) {
            matched = true;
            Annotation matchedAnnot = orthoAnnotation.updateMatches(unknown, unknownString, processedAnnots, nameAllAnnots, matchesDocFeature);
            iter.remove();
            String newType;
            if (matchedAnnot.getType().equals(unknownType)) newType = (String) annots2Remove.get(matchedAnnot.getId()); else newType = matchedAnnot.getType();
            Integer newID = new Integer(-1);
            try {
                newID = nameAllAnnots.add(unknown.getStartNode().getOffset(), new Long(unknown.getStartNode().getOffset().longValue() + stringEnd), newType, unknown.getFeatures());
            } catch (InvalidOffsetException ex) {
                throw new GateRuntimeException(ex.getMessage());
            }
            nameAllAnnots.remove(unknown);
            List mList = (List) unknown.getFeatures().get(ANNOTATION_COREF_FEATURE_NAME);
            mList.remove(unknown.getId());
            mList.add(newID);
        }
        return matched;
    }

    /**
   * Attempt to match nameAnnot against all previous annotations of the same type, which are passed down
   * in listOfThisType.  Matches are tested in order from most recent to oldest.
   * @param nameAnnot    Annotation we are trying to match
   * @param annotString  Normalized string representation of annotation
   * @param listOfThisType  ArrayList of Annotations of the same type as nameAnnot
   * @param startIndex   Index in listOfThisType that we will start from in matching the current annotation
   */
    protected void matchWithPrevious(Annotation nameAnnot, String annotString, ArrayList<Annotation> listOfThisType, int startIndex) {
        boolean matchedUnknown = false;
        for (int curIndex = startIndex - 1; curIndex >= 0; curIndex--) {
            Integer prevId = listOfThisType.get(curIndex).getId();
            Annotation prevAnnot = nameAllAnnots.get(prevId);
            if (prevAnnot == null || (!prevAnnot.getType().equals(nameAnnot.getType()) && !nameAnnot.getType().equals(unknownType))) continue;
            if (nameAnnot.getType().equals(unknownType) && prevAnnot.getType().equals(unknownType)) continue;
            if (orthoAnnotation.matchedAlready(nameAnnot, prevAnnot, matchesDocFeature, nameAllAnnots)) continue;
            if (prevAnnot.getType().equals(personType)) {
                String prevGender = (String) prevAnnot.getFeatures().get(PERSON_GENDER_FEATURE_NAME);
                String nameGender = (String) nameAnnot.getFeatures().get(PERSON_GENDER_FEATURE_NAME);
                if (prevGender != null && nameGender != null && ((nameGender.equalsIgnoreCase("female") && prevGender.equalsIgnoreCase("male")) || (prevGender.equalsIgnoreCase("female") && nameGender.equalsIgnoreCase("male")))) continue;
            }
            boolean prevAnnotUsedToMatchWithLonger = prevAnnot.getFeatures().containsKey("matchedWithLonger");
            if (matchAnnotations(nameAnnot, annotString, prevAnnot)) {
                orthoAnnotation.updateMatches(nameAnnot, prevAnnot, matchesDocFeature, nameAllAnnots);
                if (DEBUG) {
                    log.debug("Just matched nameAnnot " + nameAnnot.getId() + " with prevAnnot " + prevAnnot.getId());
                }
                if (!prevAnnotUsedToMatchWithLonger && prevAnnot.getFeatures().containsKey("matchedWithLonger")) {
                    propagatePropertyToExactMatchingMatches(prevAnnot, "matchedWithLonger", true);
                }
                if (nameAnnot.getType().equals(unknownType)) {
                    matchedUnknown = true;
                    if (prevAnnot.getType().equals(unknownType)) annots2Remove.put(nameAnnot.getId(), annots2Remove.get(prevAnnot.getId())); else annots2Remove.put(nameAnnot.getId(), prevAnnot.getType());
                    nameAnnot.getFeatures().put("NMRule", unknownType);
                }
                break;
            }
        }
        if (matchedUnknown) processedAnnots.put(nameAnnot.getId(), annotString);
    }

    protected void propagatePropertyToExactMatchingMatches(Annotation updateAnnot, String featureName, Object value) {
        try {
            List<Integer> matchesList = (List<Integer>) updateAnnot.getFeatures().get(ANNOTATION_COREF_FEATURE_NAME);
            if ((matchesList == null) || matchesList.isEmpty()) {
                return;
            } else {
                String updateAnnotString = orthoAnnotation.getStringForAnnotation(updateAnnot, document).toLowerCase();
                for (Integer nextId : matchesList) {
                    Annotation a = nameAllAnnots.get(nextId);
                    if (orthoAnnotation.fuzzyMatch(orthoAnnotation.getStringForAnnotation(a, document), updateAnnotString)) {
                        if (DEBUG) {
                            log.debug("propogateProperty: " + featureName + " " + value + " from: " + updateAnnot.getId() + " to: " + a.getId());
                        }
                        a.getFeatures().put(featureName, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error in propogatePropertyToExactMatchingMatches", e);
        }
    }

    protected boolean matchAnnotations(Annotation newAnnot, String annotString, Annotation prevAnnot) {
        if (newAnnot.overlaps(prevAnnot)) return false;
        String prevAnnotString = (String) processedAnnots.get(prevAnnot.getId());
        if (prevAnnotString == null) {
            return false;
        }
        String longName = prevAnnotString;
        String shortName = annotString;
        longAnnot = prevAnnot;
        shortAnnot = newAnnot;
        boolean longerPrevious = true;
        if (shortName.length() > longName.length()) {
            String temp = longName;
            longName = shortName;
            shortName = temp;
            Annotation tempAnn = longAnnot;
            longAnnot = shortAnnot;
            shortAnnot = tempAnn;
            longerPrevious = false;
        }
        tokensLongAnnot = (ArrayList) tokensMap.get(longAnnot.getId());
        normalizedTokensLongAnnot = (ArrayList) normalizedTokensMap.get(longAnnot.getId());
        tokensShortAnnot = (ArrayList) tokensMap.get(shortAnnot.getId());
        normalizedTokensShortAnnot = (ArrayList) normalizedTokensMap.get(shortAnnot.getId());
        List matchesList = (List) prevAnnot.getFeatures().get(ANNOTATION_COREF_FEATURE_NAME);
        if (matchesList == null || matchesList.isEmpty()) return apply_rules_namematch(prevAnnot.getType(), shortName, longName, prevAnnot, newAnnot, longerPrevious);
        if (apply_rules_namematch(prevAnnot.getType(), shortName, longName, prevAnnot, newAnnot, longerPrevious)) {
            if (allMatchingNeeded) {
                allMatchingNeeded = false;
                List toMatchList = new ArrayList(matchesList);
                toMatchList.remove(prevAnnot.getId());
                return matchOtherAnnots(toMatchList, newAnnot, annotString);
            } else return true;
        }
        return false;
    }

    /** This method checkes whether the new annotation matches
   *  all annotations given in the toMatchList (it contains ids)
   *  The idea is that the new annotation needs to match all those,
   *  because assuming transitivity does not always work, when
   *  two different entities share a common token: e.g., BT Cellnet
   *  and BT and British Telecom.
   */
    protected boolean matchOtherAnnots(List toMatchList, Annotation newAnnot, String annotString) {
        if (toMatchList.isEmpty()) return true;
        boolean matchedAll = true;
        int i = 0;
        while (matchedAll && i < toMatchList.size()) {
            Annotation prevAnnot = nameAllAnnots.get((Integer) toMatchList.get(i));
            String prevAnnotString = (String) processedAnnots.get(prevAnnot.getId());
            if (prevAnnotString == null) try {
                prevAnnotString = document.getContent().getContent(prevAnnot.getStartNode().getOffset(), prevAnnot.getEndNode().getOffset()).toString();
            } catch (InvalidOffsetException ioe) {
                return false;
            }
            String longName = prevAnnotString;
            String shortName = annotString;
            longAnnot = prevAnnot;
            shortAnnot = newAnnot;
            boolean longerPrevious = true;
            if (shortName.length() >= longName.length()) {
                String temp = longName;
                longName = shortName;
                shortName = temp;
                Annotation tempAnn = longAnnot;
                longAnnot = shortAnnot;
                shortAnnot = tempAnn;
                longerPrevious = false;
            }
            tokensLongAnnot = (ArrayList) tokensMap.get(longAnnot.getId());
            normalizedTokensLongAnnot = (ArrayList) normalizedTokensMap.get(longAnnot.getId());
            tokensShortAnnot = (ArrayList) tokensMap.get(shortAnnot.getId());
            normalizedTokensShortAnnot = (ArrayList) normalizedTokensMap.get(shortAnnot.getId());
            matchedAll = apply_rules_namematch(prevAnnot.getType(), shortName, longName, prevAnnot, newAnnot, longerPrevious);
            i++;
        }
        return matchedAll;
    }

    protected void docCleanup() {
        Object matchesValue = document.getFeatures().get(DOCUMENT_COREF_FEATURE_NAME);
        if (matchesValue != null && (matchesValue instanceof Map)) ((Map) matchesValue).remove(nameAllAnnots.getName()); else if (matchesValue != null) {
            document.getFeatures().put(DOCUMENT_COREF_FEATURE_NAME, new HashMap());
        }
        HashSet fNames = new HashSet();
        fNames.add(ANNOTATION_COREF_FEATURE_NAME);
        AnnotationSet annots = nameAllAnnots.get(null, fNames);
        if (annots == null || annots.isEmpty()) return;
        Iterator<Annotation> iter = annots.iterator();
        while (iter.hasNext()) {
            while (iter.hasNext()) iter.next().getFeatures().remove(ANNOTATION_COREF_FEATURE_NAME);
        }
    }

    static Pattern periodPat = Pattern.compile("[\\.]+");

    protected void normalizePersonName(Annotation annot) throws ExecutionException {
        ArrayList<Annotation> tokens = (ArrayList) normalizedTokensMap.get(annot.getId());
        for (int i = tokens.size() - 1; i >= 0; i--) {
            String tokenString = ((String) tokens.get(i).getFeatures().get(TOKEN_STRING_FEATURE_NAME));
            String kind = (String) tokens.get(i).getFeatures().get(TOKEN_KIND_FEATURE_NAME);
            String category = (String) tokens.get(i).getFeatures().get(TOKEN_CATEGORY_FEATURE_NAME);
            if (!caseSensitive) {
                tokenString = tokenString.toLowerCase();
            }
            if (kind.equals(PUNCTUATION_VALUE)) {
                tokens.get(i).getFeatures().put("ortho_stop", true);
            }
        }
        ArrayList<Annotation> normalizedTokens = new ArrayList<Annotation>(tokens);
        for (int j = normalizedTokens.size() - 1; j >= 0; j--) {
            if (normalizedTokens.get(j).getFeatures().containsKey("ortho_stop")) {
                normalizedTokens.remove(j);
            }
        }
        normalizedTokensMap.put(annot.getId(), normalizedTokens);
    }

    /** return an organization  without a designator and starting The*/
    protected String normalizeOrganizationName(String annotString, Annotation annot) {
        ArrayList<Annotation> tokens = (ArrayList) tokensMap.get(annot.getId());
        if (((String) ((Annotation) tokens.get(0)).getFeatures().get(TOKEN_STRING_FEATURE_NAME)).equalsIgnoreCase(THE_VALUE)) tokens.remove(0);
        if (tokens.size() > 0) {
            for (int i = tokens.size() - 1; i >= 0; i--) {
                String tokenString = ((String) tokens.get(i).getFeatures().get(TOKEN_STRING_FEATURE_NAME));
                String kind = (String) tokens.get(i).getFeatures().get(TOKEN_KIND_FEATURE_NAME);
                String category = (String) tokens.get(i).getFeatures().get(TOKEN_CATEGORY_FEATURE_NAME);
                if (!caseSensitive) {
                    tokenString = tokenString.toLowerCase();
                }
                if (kind.equals(PUNCTUATION_VALUE) || ((category != null) && (category.equals("DT") || category.equals("IN"))) || cdg.contains(tokenString)) {
                    tokens.get(i).getFeatures().put("ortho_stop", true);
                }
            }
            String compareString = (String) tokens.get(tokens.size() - 1).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
            if (!caseSensitive) {
                compareString = compareString.toLowerCase();
            }
            if (cdg.contains(compareString)) {
                tokens.remove(tokens.size() - 1);
            }
        }
        ArrayList<Annotation> normalizedTokens = new ArrayList<Annotation>(tokens);
        for (int j = normalizedTokens.size() - 1; j >= 0; j--) {
            if (normalizedTokens.get(j).getFeatures().containsKey("ortho_stop")) {
                normalizedTokens.remove(j);
            }
        }
        normalizedTokensMap.put(annot.getId(), normalizedTokens);
        StringBuffer newString = new StringBuffer(50);
        for (int i = 0; i < tokens.size(); i++) {
            newString.append((String) ((Annotation) tokens.get(i)).getFeatures().get(TOKEN_STRING_FEATURE_NAME));
            if (i != tokens.size() - 1) newString.append(" ");
        }
        if (caseSensitive) return newString.toString();
        return newString.toString().toLowerCase();
    }

    /** creates the lookup tables */
    protected void createAnnotList(String nameFile, String nameList) throws IOException {
        URL fileURL = new URL(definitionFileURL, nameFile);
        BufferedReader bufferedReader = new BomStrippingInputStreamReader(fileURL.openStream(), encoding);
        String lineRead = null;
        while ((lineRead = bufferedReader.readLine()) != null) {
            if (nameList.compareTo(CDGLISTNAME) == 0) {
                Matcher matcher = punctPat.matcher(lineRead.toLowerCase().trim());
                lineRead = matcher.replaceAll(" ").trim();
                if (caseSensitive) cdg.add(lineRead); else cdg.add(lineRead.toLowerCase());
            } else {
                int index = lineRead.indexOf("£");
                if (index != -1) {
                    String expr = lineRead.substring(0, index);
                    if (!caseSensitive) expr = expr.toLowerCase();
                    String code = lineRead.substring(index + 1, lineRead.length());
                    if (nameList.equals(ALIASLISTNAME)) {
                        alias.put(expr, code);
                    } else if (nameList.equals(ARTLISTNAME)) {
                        def_art.put(expr, code);
                    } else if (nameList.equals(PREPLISTNAME)) {
                        prepos.put(expr, code);
                    } else if (nameList.equals(CONNECTORLISTNAME)) {
                        connector.put(expr, code);
                    } else if (nameList.equals(SPURLISTNAME)) {
                        spur_match.put(expr, code);
                    }
                }
            }
        }
    }

    /**
   * This is the skeleton of a function which should be available in OrthoMatcher to allow a pairwise comparison of two name strings
   * It should eventually be made public.  It is private here (and thus non-functional) because OrthoMatcher is currently reliant
   * on the tokenization of the names, which are held in the global variables tokensShortAnnot and tokensLongAnnot
   *
   * @param name1
   * @param name2
   * @return  true if the two names indicate the same person
   */
    private boolean pairwise_person_name_match(String name1, String name2) {
        String shortName, longName;
        if (name1.length() > name2.length()) {
            longName = name1;
            shortName = name2;
        } else {
            longName = name2;
            shortName = name1;
        }
        if (rules.get(0).value(longName, shortName)) {
            return false;
        } else {
            if (longName.equals(shortName) || rules.get(2).value(longName, shortName) || rules.get(3).value(longName, shortName)) {
                return true;
            } else {
                return (rules.get(0).value(longName, shortName));
            }
        }
    }

    /**
   * basic_person_match_criteria
   * Note that this function relies on various global variables in some other match rules.
   * @param shortName
   * @param longName
   * @param mr
   * @return
   */
    private boolean basic_person_match_criteria(String shortName, String longName, boolean mr[]) {
        if (OrthoMatcherHelper.executeDisjunction(rules, new int[] { 1, 5, 6, 13, 15, 16 }, longName, shortName, mr)) {
            return true;
        }
        return false;
    }

    /** apply_rules_namematch: apply rules similarly to lasie1.5's namematch */
    private boolean apply_rules_namematch(String annotationType, String shortName, String longName, Annotation prevAnnot, Annotation followAnnot, boolean longerPrevious) {
        boolean mr[] = new boolean[rules.size()];
        if (DEBUG) {
            log.debug("Now matching " + longName + "(id: " + longAnnot.getId() + ") to " + shortName + "(id: " + shortAnnot.getId() + ")");
        }
        if (rules.get(0).value(longName, shortName)) return false;
        if ((OrthoMatcherHelper.executeDisjunction(rules, new int[] { 2, 3 }, longName, shortName, mr)) || ((annotationType.equals(organizationType) || annotationType.equals("Facility")) && ((!highPrecisionOrgs && OrthoMatcherHelper.executeDisjunction(rules, new int[] { 4, 6, 7, 8, 9, 10, 11, 12, 14 }, longName, shortName, mr)) || (highPrecisionOrgs && OrthoMatcherHelper.executeDisjunction(rules, new int[] { 7, 8, 10, 11, 17 }, longName, shortName, mr))))) {
            return true;
        }
        if ((annotationType.equals(personType))) {
            if (noMatchRule1(longName, shortName, prevAnnot, longerPrevious) || noMatchRule2(longName, shortName)) {
                return false;
            } else {
                if (basic_person_match_criteria(shortName, longName, mr)) {
                    if ((longName.length() != shortName.length()) && (mr[4] || mr[5] || mr[14] || mr[15])) {
                        if (longerPrevious) {
                            followAnnot.getFeatures().put("matchedWithLonger", true);
                        } else {
                            prevAnnot.getFeatures().put("matchedWithLonger", true);
                        }
                    } else if ((longName.length() == shortName.length()) && (mr[1])) {
                        if (prevAnnot.getFeatures().containsKey("matchedWithLonger")) {
                            followAnnot.getFeatures().put("matchedWithLonger", true);
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /** set the extLists flag */
    public void setExtLists(Boolean newExtLists) {
        extLists = newExtLists.booleanValue();
    }

    /** set the caseSensitive flag */
    public void setCaseSensitive(Boolean newCase) {
        caseSensitive = newCase.booleanValue();
    }

    /** set the annotation set name*/
    public void setAnnotationSetName(String newAnnotationSetName) {
        annotationSetName = newAnnotationSetName;
    }

    /** set the types of the annotations*/
    public void setAnnotationTypes(List newType) {
        annotationTypes = newType;
    }

    /** set whether to process the Unknown annotations*/
    public void setProcessUnknown(Boolean processOrNot) {
        this.matchingUnknowns = processOrNot.booleanValue();
    }

    public void setOrganizationType(String newOrganizationType) {
        organizationType = newOrganizationType;
    }

    public void setPersonType(String newPersonType) {
        personType = newPersonType;
    }

    /**get the name of the annotation set*/
    public String getAnnotationSetName() {
        return annotationSetName;
    }

    /** get the types of the annotation*/
    public List getAnnotationTypes() {
        return annotationTypes;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public String getPersonType() {
        return personType;
    }

    public Boolean getExtLists() {
        return new Boolean(extLists);
    }

    /** Are we running in a case-sensitive mode?*/
    public Boolean getCaseSensitive() {
        return new Boolean(caseSensitive);
    }

    /** Return whether or not we're processing the Unknown annots*/
    public Boolean getProcessUnknown() {
        return new Boolean(matchingUnknowns);
    }

    /**
  No Match Rule 1:
  Avoids the problem of matching
  David Jones ...
  David ...
  David Smith
  Since "David" was matched with David Jones, we don't match David with David Smith.
   */
    public boolean noMatchRule1(String s1, String s2, Annotation previousAnnot, boolean longerPrevious) {
        if (longerPrevious || !previousAnnot.getFeatures().containsKey("matchedWithLonger")) {
            return false;
        } else {
            return true;
        }
    }

    /***
   * returns true if it detects a middle name which indicates that the name string contains a nickname or a
   * compound last name
   */
    private boolean detectBadMiddleTokens(ArrayList<Annotation> tokArray) {
        for (int j = 1; j < tokArray.size() - 1; j++) {
            String currentToken = (String) tokArray.get(j).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
            Matcher matcher = badMiddleTokens.matcher(currentToken.toLowerCase().trim());
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    /**
   * NoMatch Rule #2: Do we have a mismatch of middle initial?
   * Condition(s):  Only applies to person names with more than two tokens in the name
   *
   * Want George W. Bush != George H. W. Bush and George Walker Bush != George Herbert Walker Bush
   * and
   * John T. Smith != John Q. Smith
   * however
   * John T. Smith == John Thomas Smith
   * be careful about
   * Hillary Rodham Clinton == Hillary Rodham-Clinton
   * be careful about
   * Carlos Bueno de Lopez == Bueno de Lopez
   * and
   * Cynthia Morgan de Rothschild == Cynthia de Rothschild
   */
    public boolean noMatchRule2(String s1, String s2) {
        if (normalizedTokensLongAnnot.size() > 2 && normalizedTokensShortAnnot.size() > 2) {
            boolean retval = false;
            if (normalizedTokensLongAnnot.size() != normalizedTokensShortAnnot.size()) {
                String firstNameLong = (String) normalizedTokensLongAnnot.get(0).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                String firstNameShort = (String) normalizedTokensShortAnnot.get(0).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                String lastNameLong = (String) normalizedTokensLongAnnot.get(normalizedTokensLongAnnot.size() - 1).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                String lastNameShort = (String) normalizedTokensShortAnnot.get(normalizedTokensShortAnnot.size() - 1).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                if (rules.get(1).value(firstNameLong, firstNameShort) && (rules.get(1).value(lastNameLong, lastNameShort))) {
                    if (detectBadMiddleTokens(tokensLongAnnot) || detectBadMiddleTokens(tokensShortAnnot)) {
                        if (DEBUG && log.isDebugEnabled()) {
                            log.debug("noMatchRule2Name did not non-match because of bad middle tokens " + s1 + "(id: " + longAnnot.getId() + ") to " + s2 + "(id: " + shortAnnot.getId() + ")");
                        }
                        return false;
                    } else {
                        retval = true;
                    }
                }
            } else {
                for (int i = 1; i < normalizedTokensLongAnnot.size() - 1; i++) {
                    String s1_middle = (String) ((Annotation) normalizedTokensLongAnnot.get(i)).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                    String s2_middle = (String) ((Annotation) normalizedTokensShortAnnot.get(i)).getFeatures().get(TOKEN_STRING_FEATURE_NAME);
                    if (!caseSensitive) {
                        s1_middle = s1_middle.toLowerCase();
                        s2_middle = s2_middle.toLowerCase();
                    }
                    if (!(rules.get(1).value(s1_middle, s2_middle) || OrthoMatcherHelper.initialMatch(s1_middle, s2_middle))) {
                        retval = true;
                        break;
                    }
                }
            }
            if (retval && log.isDebugEnabled() && DEBUG) {
                log.debug("noMatchRule2Name non-matched  " + s1 + "(id: " + longAnnot.getId() + ") to " + s2 + "(id: " + shortAnnot.getId() + ")");
            }
            return retval;
        }
        return false;
    }

    public void setDefinitionFileURL(java.net.URL definitionFileURL) {
        this.definitionFileURL = definitionFileURL;
    }

    public java.net.URL getDefinitionFileURL() {
        return definitionFileURL;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    public Double getMinimumNicknameLikelihood() {
        return minimumNicknameLikelihood;
    }

    public void setMinimumNicknameLikelihood(Double minimumNicknameLikelihood) {
        this.minimumNicknameLikelihood = minimumNicknameLikelihood;
    }

    /**
   * @return the highPrecisionOrgs
   */
    public Boolean getHighPrecisionOrgs() {
        return highPrecisionOrgs;
    }

    /**
   * @param highPrecisionOrgs the highPrecisionOrgs to set
   */
    public void setHighPrecisionOrgs(Boolean highPrecisionOrgs) {
        this.highPrecisionOrgs = highPrecisionOrgs;
    }

    public void setOrthography(AnnotationOrthography orthography) {
        this.orthoAnnotation = orthography;
    }

    public AnnotationOrthography getOrthography() {
        return orthoAnnotation;
    }

    static Pattern punctPat = Pattern.compile("[\\p{Punct}]+");

    static Pattern badMiddleTokens = Pattern.compile("[“”‘’\'\\(\\)\"]+|^de$|^von$");
}
