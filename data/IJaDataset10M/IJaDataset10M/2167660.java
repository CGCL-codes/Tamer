package gleam.executive.service.impl;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.util.AnnotationDiffer;
import gate.creole.annic.Hit;
import gleam.docservice.proxy.DSProxyException;
import gleam.docservice.proxy.DocServiceProxy;
import gleam.docservice.proxy.DocServiceProxyFactory;
import gleam.docservice.proxy.IAAAlgorithm;
import gleam.docservice.proxy.IAAResult;
import gleam.docservice.proxy.LRInfo;
import gleam.executive.model.AnnicSearchResult;
import gleam.executive.model.AnnotationDifferResult;
import gleam.executive.model.AnnotationDifferScores;
import gleam.executive.model.Corpus;
import gleam.executive.model.Document;
import gleam.executive.service.DocServiceManager;
import gleam.executive.service.SafeManagerException;
import gleam.executive.service.SecurityTokenManager;
import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import gate.creole.annic.*;

public class DocServiceManagerImpl extends BaseManager implements DocServiceManager {

    private DocServiceProxyFactory docServiceProxyFactory;

    /**
   * A static doc service proxy, only used for testing.
   */
    private DocServiceProxy docServiceProxy;

    /**
   * Base doc service URL to which security tokens will be appended.
   */
    private String docServiceBaseURL;

    /**
   * The "private" base doc service URL to which security tokens
   * will be appended.
   */
    private String privateDocServiceBaseURL;

    /**
   * The current URL to which this manager will talk, including a security token.
   */
    private String docServiceCurrentURL;

    private String currentSecurityToken;

    private String annotatorGUIURL;

    private String poolModeAnnotatorGUIURL;

    private String annotationDiffGUIURL;

    private AnnotationDifferScores annDiffScores;

    private SecurityTokenManager securityTokenManager;

    public DocServiceManagerImpl(String dsURL, String privateURL, String annotatorGUIURL, String poolModeAnnotatorGUIURL, String annotationDiffGUIURL, DocServiceProxyFactory docServiceProxyFactory, SecurityTokenManager stm) {
        this.docServiceBaseURL = dsURL;
        this.privateDocServiceBaseURL = privateURL;
        this.annotatorGUIURL = annotatorGUIURL;
        this.poolModeAnnotatorGUIURL = poolModeAnnotatorGUIURL;
        this.annotationDiffGUIURL = annotationDiffGUIURL;
        log.debug("docserviceUrl: " + dsURL);
        this.docServiceProxyFactory = docServiceProxyFactory;
        this.securityTokenManager = stm;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#getGateDocument(java.lang.String)
   */
    public gate.Document getGateDocument(String documentId) throws SafeManagerException {
        gate.Document document = null;
        try {
            document = currentDocServiceProxy().getDocumentContentOnly(documentId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return document;
    }

    private synchronized DocServiceProxy currentDocServiceProxy() throws DSProxyException {
        if (docServiceProxy != null) {
            return docServiceProxy;
        }
        if (currentSecurityToken == null || !securityTokenManager.isValid(currentSecurityToken)) {
            docServiceCurrentURL = getPrivateDocServiceURL();
            currentSecurityToken = docServiceCurrentURL.substring(privateDocServiceBaseURL.length() + 1);
        }
        return docServiceProxyFactory.getDocServiceProxy(URI.create(docServiceCurrentURL));
    }

    /**
   * @see gleam.executive.service.DocServiceManager#getSearcherID(java.lang.String,java.lang.String)
   */
    public String getSearcherID(String query, String corpusID, String annotationSetID, int contextWindow) throws SafeManagerException {
        String searcherID = null;
        try {
            searcherID = currentDocServiceProxy().startSearch(query, corpusID, annotationSetID, contextWindow);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return searcherID;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#getAnnicSearchResults(java.lang.String)
   */
    public List<AnnicSearchResult> getAnnicSearchResults(String searchID, int numberOfResults) throws SafeManagerException {
        List<AnnicSearchResult> results = new ArrayList<AnnicSearchResult>();
        try {
            Hit[] hits = currentDocServiceProxy().getNextResults(searchID, numberOfResults);
            for (int i = 0; i < hits.length; i++) {
                gate.creole.annic.Pattern ap = (gate.creole.annic.Pattern) hits[i];
                Document doc = this.getDocument(ap.getDocumentID());
                AnnicSearchResult result = new AnnicSearchResult();
                result.setDocumentID(ap.getDocumentID());
                result.setDocumentName(doc.getDocumentName());
                result.setLeftContext(ap.getPatternText(ap.getLeftContextStartOffset(), ap.getStartOffset()));
                result.setPattern(ap.getPatternText(ap.getStartOffset(), ap.getEndOffset()));
                result.setRightContext(ap.getPatternText(ap.getEndOffset(), ap.getRightContextEndOffset()));
                result.setDetailsID(new Integer(i).toString());
                result.setDetailsTable(HTMLGenerator.generateHTMLTable(ap));
                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return results;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#listCorpora()
   */
    public List<Corpus> listCorpora() throws SafeManagerException {
        List<Corpus> corpora = new ArrayList<Corpus>();
        try {
            LRInfo[] corporaArray = currentDocServiceProxy().listCorpora();
            if (corporaArray != null) {
                for (int i = 0; i < corporaArray.length; i++) {
                    Corpus corpus = new Corpus();
                    corpus.setCorpusID(corporaArray[i].getID());
                    corpus.setCorpusName(corporaArray[i].getName());
                    corpus.setNumberOfDocuments(corporaArray[i].getSize());
                    corpus.setUploader(getCorpusFeature(corporaArray[i].getID(), gleam.executive.Constants.UPLOADER));
                    corpora.add(corpus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return corpora;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#getCorpusInfo(java.lang.String)
   */
    public String getCorpusName(String corpusID) throws SafeManagerException {
        String corpus = null;
        try {
            corpus = currentDocServiceProxy().getCorpusName(corpusID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return corpus;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#setCorpusName(java.lang.String,java.lang.String)
   */
    public void setCorpusName(String corpusID, String name) throws SafeManagerException {
        try {
            currentDocServiceProxy().setCorpusName(corpusID, name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
    }

    /**
   * @see gleam.executive.service.DocServiceManager#getCorpusFeature(String, String)
   */
    public String getCorpusFeature(String corpusID, String featureName) throws SafeManagerException {
        try {
            return currentDocServiceProxy().getCorpusFeature(corpusID, featureName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
    }

    /**
   * @see gleam.executive.service.DocServiceManager#setCorpusFeature(String, String, String)
   */
    public void setCorpusFeature(String corpusID, String featureName, String featureValue) throws SafeManagerException {
        try {
            currentDocServiceProxy().setCorpusFeature(corpusID, featureName, featureValue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
    }

    /**
   * @see gleam.executive.service.DocServiceManager#listDocuments()
   */
    public List<Document> listDocuments() throws SafeManagerException {
        return listDocuments(null);
    }

    /**
   * @see gleam.executive.service.DocServiceManager#listDocuments(java.lang.String)
   */
    public List<Document> listDocuments(String corpusID) throws SafeManagerException {
        List<Document> documents = new ArrayList<Document>();
        try {
            LRInfo[] documentArray = currentDocServiceProxy().listDocuments(corpusID);
            if (documentArray != null) {
                for (int i = 0; i < documentArray.length; i++) {
                    Document document = new Document();
                    document.setDocumentID(documentArray[i].getID());
                    document.setDocumentName(documentArray[i].getName());
                    documents.add(document);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return documents;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#getDocument(java.lang.String)
   */
    public Document getDocument(String documentID) throws SafeManagerException {
        Document document = new Document();
        try {
            String documentName = currentDocServiceProxy().getDocumentName(documentID);
            document.setDocumentID(documentID);
            document.setDocumentName(documentName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return document;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#getDocumentFeature(String, String)
   */
    public String getDocumentFeature(String documentID, String featureName) throws SafeManagerException {
        try {
            return currentDocServiceProxy().getDocumentFeature(documentID, featureName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
    }

    /**
   * @see gleam.executive.service.DocServiceManager#setDocumentFeature(String, String, String)
   */
    public void setDocumentFeature(String documentID, String featureName, String featureValue) throws SafeManagerException {
        try {
            currentDocServiceProxy().setDocumentFeature(documentID, featureName, featureValue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
    }

    /**
   * @see gleam.executive.service.DocServiceManager#createAnnotationSet(java.lang.String docID, java.lang.String annotationSetName)
   */
    public boolean createAnnotationSet(String docID, String annotationSetName) throws SafeManagerException {
        boolean created = false;
        try {
            created = currentDocServiceProxy().createAnnotationSet(docID, annotationSetName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return created;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#createCorpus(java.lang.String)
   */
    public String createCorpus(String corpusName) throws SafeManagerException {
        String corpusID = null;
        try {
            corpusID = currentDocServiceProxy().createCorpus(corpusName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return corpusID;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#deleteCorpus(java.lang.String)
   */
    public void deleteCorpus(String corpusID) throws SafeManagerException {
        try {
            currentDocServiceProxy().deleteCorpus(corpusID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
    }

    /**
   * @see gleam.executive.service.DocServiceManager#addDocumentToCorpus(java.lang.String,java.lang.String)
   */
    public boolean addDocumentToCorpus(String corpusID, String documentID) throws SafeManagerException {
        boolean addSuccessful = false;
        try {
            addSuccessful = currentDocServiceProxy().addDocumentToCorpus(corpusID, documentID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return addSuccessful;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#removeDocumentFromCorpus(java.lang.String,java.lang.String)
   */
    public boolean removeDocumentFromCorpus(String corpusID, String documentID) throws SafeManagerException {
        boolean removeSuccessful = false;
        try {
            removeSuccessful = currentDocServiceProxy().removeDocumentFromCorpus(corpusID, documentID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return removeSuccessful;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#deleteDocument(java.lang.String)
   */
    public boolean deleteDocument(String docID) throws SafeManagerException {
        boolean deleteSuccessful = false;
        try {
            deleteSuccessful = currentDocServiceProxy().deleteDocument(docID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return deleteSuccessful;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#createDocIntoCorpus(java.lang.String,java.lang.String)
   */
    public String createDocIntoCorpus(String docName, String corpusID, byte[] docXmlContent, String encoding) throws SafeManagerException {
        String docID = null;
        try {
            docID = currentDocServiceProxy().createDocument(docName, corpusID, docXmlContent, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return docID;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#listAnnotationSetNames(java.lang.String)
   */
    public List listAnnotationSetNames(String docID) throws SafeManagerException {
        List<String> annosets = new ArrayList<String>();
        try {
            String[] names = currentDocServiceProxy().getAnnotationSetNames(docID);
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                annosets.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return annosets;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#listAnnSetNames(java.lang.String)
   */
    public List<AnnotationDifferResult> listAnnSetNames(String docID) throws SafeManagerException {
        List<AnnotationDifferResult> annosets = new ArrayList<AnnotationDifferResult>();
        try {
            String[] names = currentDocServiceProxy().getAnnotationSetNames(docID);
            for (int i = 0; i < names.length; i++) {
                AnnotationDifferResult adr = new AnnotationDifferResult();
                adr.setKeyAnnoSetName(names[i]);
                adr.setResAnnoSetName(names[i]);
                annosets.add(adr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return annosets;
    }

    public List<AnnotationDifferResult> listSharedAnnoTypes(String docID, String keyAnnoSetName, String resAnnoSetName) throws SafeManagerException {
        List<AnnotationDifferResult> types = new ArrayList<AnnotationDifferResult>();
        gate.Document doc = null;
        try {
            doc = this.currentDocServiceProxy().getDocumentContentOnly(docID);
            this.currentDocServiceProxy().getAnnotationSet(doc, keyAnnoSetName, "Key", true);
            this.currentDocServiceProxy().getAnnotationSet(doc, resAnnoSetName, "Res", true);
            AnnotationSet keys = doc.getAnnotations("Key");
            AnnotationSet response = doc.getAnnotations("Res");
            Set keyTypes = keys.getAllTypes();
            Set resTypes = response.getAllTypes();
            Iterator itKey = keyTypes.iterator();
            while (itKey.hasNext()) {
                String keyType = (String) itKey.next();
                if (resTypes.contains(keyType) && (!types.contains(keyType))) {
                    AnnotationDifferResult adr = new AnnotationDifferResult();
                    adr.setKeyAnnoSetName(keyAnnoSetName);
                    adr.setResAnnoSetName(resAnnoSetName);
                    adr.setAnnoType(keyType);
                    types.add(adr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        } finally {
            if (doc != null) {
                Factory.deleteResource(doc);
            }
        }
        return types;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#listSharedAnnotationTypes
   *      (java.lang.String,java.lang.String,java.lang.String)
   */
    public List<String> listSharedAnnotationTypes(String docID, String... asNames) throws SafeManagerException {
        List<String> types = new ArrayList<String>();
        gate.Document doc = null;
        try {
            doc = this.currentDocServiceProxy().getDocumentContentOnly(docID);
            if (asNames.length == 0) {
                return Collections.emptyList();
            }
            if (asNames.length > 0) {
                AnnotationSet[] annoSets = new AnnotationSet[asNames.length];
                for (int i = 0; i < asNames.length; i++) {
                    this.currentDocServiceProxy().getAnnotationSet(doc, asNames[i], "as" + i, true);
                    annoSets[i] = doc.getAnnotations("as" + i);
                }
                types.addAll(annoSets[0].getAllTypes());
                for (int j = 1; j < annoSets.length; j++) {
                    types.retainAll(annoSets[j].getAllTypes());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        } finally {
            if (doc != null) {
                Factory.deleteResource(doc);
            }
        }
        return types;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#listAnnotationTypesForSingleAnnotationSet
   *      (java.lang.String,java.lang.String,java.lang.String)
   */
    public List<AnnotationDifferResult> listAnnotationTypesForSingleAnnotationSet(String docID, String annoSetName) throws SafeManagerException {
        List<AnnotationDifferResult> types = new ArrayList<AnnotationDifferResult>();
        gate.Document doc = null;
        try {
            doc = this.currentDocServiceProxy().getDocumentContentOnly(docID);
            this.currentDocServiceProxy().getAnnotationSet(doc, annoSetName, "Key", true);
            this.currentDocServiceProxy().getAnnotationSet(doc, annoSetName, "Res", true);
            AnnotationSet keys = doc.getAnnotations("Key");
            AnnotationSet response = doc.getAnnotations("Res");
            Set keyTypes = keys.getAllTypes();
            Set resTypes = response.getAllTypes();
            Iterator itKey = keyTypes.iterator();
            while (itKey.hasNext()) {
                String keyType = (String) itKey.next();
                AnnotationDifferResult adr = new AnnotationDifferResult();
                adr.setAnnoType(keyType);
                if (resTypes.contains(keyType) && (!types.contains(keyType))) {
                    types.add(adr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        } finally {
            if (doc != null) {
                Factory.deleteResource(doc);
            }
        }
        return types;
    }

    /**
   * @see gleam.executive.service.DocServiceManager#getAnnoDifferResult
   *      (java.lang.String,java.lang.String,java.lang.String,java.lang.String)
   */
    public List<AnnotationDifferResult> getAnnoDifferResult(String docID, String keyAnnoSetName, String resAnnoSetName, String annoType) throws SafeManagerException {
        List<AnnotationDifferResult> result = new ArrayList<AnnotationDifferResult>();
        gate.Document doc = null;
        try {
            doc = this.currentDocServiceProxy().getDocumentContentOnly(docID);
            this.currentDocServiceProxy().getAnnotationSet(doc, keyAnnoSetName, "Key", true);
            this.currentDocServiceProxy().getAnnotationSet(doc, resAnnoSetName, "Res", true);
            AnnotationSet keys = doc.getAnnotations("Key").get(annoType);
            AnnotationSet responses = doc.getAnnotations("Res").get(annoType);
            AnnotationDiffer annoDiffer = new AnnotationDiffer();
            annoDiffer.setSignificantFeaturesSet(new HashSet());
            List pairings = annoDiffer.calculateDiff(keys, responses);
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(4);
            AnnotationDifferScores scores = new AnnotationDifferScores();
            scores.setCorrect(format.format(annoDiffer.getCorrectMatches()));
            scores.setPartCorrect(format.format(annoDiffer.getPartiallyCorrectMatches()));
            scores.setMissing(format.format(annoDiffer.getMissing()));
            scores.setSpurious(format.format(annoDiffer.getSpurious()));
            scores.setFMeasure(format.format(annoDiffer.getFMeasureStrict(1)));
            scores.setPrecision(format.format(annoDiffer.getPrecisionStrict()));
            scores.setRecall(format.format(annoDiffer.getRecallStrict()));
            this.setAnnDiffScores(scores);
            AnnotationDifferResult adResult = null;
            for (int i = 0; i < pairings.size(); i++) {
                AnnotationDiffer.Pairing pairing = (AnnotationDiffer.Pairing) pairings.get(i);
                int type = pairing.getType();
                if (type == AnnotationDiffer.CORRECT_TYPE) {
                    Annotation keyAnnotation = pairing.getKey();
                    Annotation resAnnotation = pairing.getResponse();
                    Long startKey = keyAnnotation.getStartNode().getOffset();
                    Long endKey = keyAnnotation.getEndNode().getOffset();
                    Long startRes = resAnnotation.getStartNode().getOffset();
                    Long endRes = resAnnotation.getEndNode().getOffset();
                    String annotationKeyString = doc.getContent().getContent(startKey, endKey).toString();
                    String annotationResString = doc.getContent().getContent(startRes, endRes).toString();
                    FeatureMap keyFeatures = keyAnnotation.getFeatures();
                    FeatureMap resFeatures = resAnnotation.getFeatures();
                    adResult = new AnnotationDifferResult();
                    adResult.setStartKey(startKey);
                    adResult.setEndKey(endKey);
                    adResult.setKeyString(annotationKeyString);
                    adResult.setKeyFeature(keyFeatures.toString());
                    adResult.setMarkString("=");
                    adResult.setStartRes(startRes);
                    adResult.setEndRes(endRes);
                    adResult.setResString(annotationResString);
                    adResult.setResFeature(resFeatures.toString());
                    result.add(adResult);
                } else if (type == AnnotationDiffer.PARTIALLY_CORRECT_TYPE) {
                    Annotation keyAnnotation = pairing.getKey();
                    Annotation resAnnotation = pairing.getResponse();
                    Long startKey = keyAnnotation.getStartNode().getOffset();
                    Long endKey = keyAnnotation.getEndNode().getOffset();
                    Long startRes = resAnnotation.getStartNode().getOffset();
                    Long endRes = resAnnotation.getEndNode().getOffset();
                    String annotationKeyString = doc.getContent().getContent(startKey, endKey).toString();
                    String annotationResString = doc.getContent().getContent(startRes, endRes).toString();
                    FeatureMap keyFeatures = keyAnnotation.getFeatures();
                    FeatureMap resFeatures = resAnnotation.getFeatures();
                    adResult = new AnnotationDifferResult();
                    adResult.setStartKey(startKey);
                    adResult.setEndKey(endKey);
                    adResult.setKeyString(annotationKeyString);
                    adResult.setKeyFeature(keyFeatures.toString());
                    adResult.setMarkString("~");
                    adResult.setStartRes(startRes);
                    adResult.setEndRes(endRes);
                    adResult.setResString(annotationResString);
                    adResult.setResFeature(resFeatures.toString());
                    result.add(adResult);
                } else if (type == AnnotationDiffer.MISSING_TYPE) {
                    Annotation keyAnnotation = pairing.getKey();
                    Long startKey = keyAnnotation.getStartNode().getOffset();
                    Long endKey = keyAnnotation.getEndNode().getOffset();
                    String annotationKeyString = doc.getContent().getContent(startKey, endKey).toString();
                    FeatureMap keyFeatures = keyAnnotation.getFeatures();
                    adResult = new AnnotationDifferResult();
                    adResult.setStartKey(startKey);
                    adResult.setEndKey(endKey);
                    adResult.setKeyString(annotationKeyString);
                    adResult.setKeyFeature(keyFeatures.toString());
                    adResult.setMarkString("!=");
                    adResult.setStartRes(new Long("-1"));
                    adResult.setEndRes(new Long("-1"));
                    adResult.setResString(" ");
                    adResult.setResFeature(" ");
                    result.add(adResult);
                } else if (type == AnnotationDiffer.SPURIOUS_TYPE) {
                    Annotation resAnnotation = pairing.getResponse();
                    Long startRes = resAnnotation.getStartNode().getOffset();
                    Long endRes = resAnnotation.getEndNode().getOffset();
                    String annotationResString = doc.getContent().getContent(startRes, endRes).toString();
                    FeatureMap resFeatures = resAnnotation.getFeatures();
                    adResult = new AnnotationDifferResult();
                    adResult.setStartKey(new Long("-1"));
                    adResult.setEndKey(new Long("-1"));
                    adResult.setKeyString(" ");
                    adResult.setKeyFeature(" ");
                    adResult.setMarkString("!=");
                    adResult.setStartRes(startRes);
                    adResult.setEndRes(endRes);
                    adResult.setResString(annotationResString);
                    adResult.setResFeature(resFeatures.toString());
                    result.add(adResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        } finally {
            if (doc != null) {
                Factory.deleteResource(doc);
            }
        }
        return result;
    }

    public boolean annotationSetNameExists(String docID, String annSetName) throws SafeManagerException {
        boolean flag = false;
        try {
            flag = this.currentDocServiceProxy().annotationSetNameExists(docID, annSetName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return flag;
    }

    public boolean deleteAnnotationSet(String docID, String asName) throws SafeManagerException {
        boolean flag = false;
        try {
            flag = this.currentDocServiceProxy().deleteAnnotationSet(docID, asName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return flag;
    }

    public boolean deleteAnnotationSet(gate.Document doc, String docServiceASName) throws SafeManagerException {
        boolean flag = false;
        try {
            flag = this.currentDocServiceProxy().deleteAnnotationSet(doc, docServiceASName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return flag;
    }

    public boolean releaseLock(String taskID) throws SafeManagerException {
        boolean flag = false;
        try {
            flag = this.currentDocServiceProxy().releaseLock(taskID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return flag;
    }

    /**
   * @see DocServiceManager#calculateIAA
   */
    public IAAResult calculateIAA(String[] docIDs, String[] asNames, String annotationType, String featureName, IAAAlgorithm algorithm) throws SafeManagerException {
        IAAResult result = null;
        try {
            result = this.currentDocServiceProxy().calculateIAA(docIDs, asNames, annotationType, featureName, algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
        return result;
    }

    public byte[] getDocXML(String docID) throws SafeManagerException {
        try {
            return currentDocServiceProxy().getDocXML(docID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SafeManagerException(e.getMessage());
        }
    }

    /**
   * @param docServiceProxy The docServiceProxy to set.
   */
    public void setDocServiceProxy(DocServiceProxy docServiceProxy) {
        this.docServiceProxy = docServiceProxy;
    }

    /**
   * @return Returns the docServiceURL.
   */
    public String getDocServiceURL() {
        return docServiceBaseURL + "/" + securityTokenManager.newToken();
    }

    /**
   * @return Returns the "private" docServiceURL.
   */
    public String getPrivateDocServiceURL() {
        return privateDocServiceBaseURL + "/" + securityTokenManager.newToken();
    }

    /**
   * @param docServiceURL The docServiceURL to set.
   */
    public void setDocServiceURL(String docServiceURL) {
        this.docServiceBaseURL = docServiceURL;
    }

    public String getAnnotatorGUIURL() {
        return annotatorGUIURL;
    }

    public void setAnnotatorGUIURL(String annotatorGUIURL) {
        this.annotatorGUIURL = annotatorGUIURL;
    }

    public String getPoolModeAnnotatorGUIURL() {
        return poolModeAnnotatorGUIURL;
    }

    public void setPoolModeAnnotatorGUIURL(String poolModeAnnotatorGUIURL) {
        this.poolModeAnnotatorGUIURL = poolModeAnnotatorGUIURL;
    }

    public String getAnnotationDiffGUIURL() {
        return annotationDiffGUIURL;
    }

    public void setAnnotationDiffGUIURL(String annotationDiffGUIURL) {
        this.annotationDiffGUIURL = annotationDiffGUIURL;
    }

    /**
   * @return Returns the annDiffScores.
   */
    public AnnotationDifferScores getAnnDiffScores() {
        return annDiffScores;
    }

    /**
   * @param annDiffScores The annDiffScores to set.
   */
    public void setAnnDiffScores(AnnotationDifferScores annDiffScores) {
        this.annDiffScores = annDiffScores;
    }
}
