package org.rascalli.mbe.mmg;

import java.util.LinkedList;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.rascalli.mbe.RdfData;

/**
 * Creates a java representation of information represented in the rdf data
 * serving as input to multi-modal generation.
 * 
 * @author gregor
 */
public class MMGInputRepresentation implements MMGInput {

    private RdfData data;

    private final Log log = LogFactory.getLog(getClass());

    private String communicationChannel;

    private ContentType contentType;

    private String textValue;

    private String dialogueAct;

    private String templateName;

    private LinkedList<String> linkUrlList;

    private String emotionType;

    private String passData;

    private LinkedList<Keyword> keywordList;

    /**
	 * Constructor
	 * 
	 * @param data
	 *            the RDF data to be represented
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 * @throws RepositoryException
	 */
    public MMGInputRepresentation(RdfData data) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
        this.data = data;
        communicationChannel = "ECA";
        linkUrlList = new LinkedList<String>();
        analyze();
    }

    /**
	 * Analyze
	 * 
	 * NOTE: extraction of subgraph for graph input mode should be extracted
	 * using a different query (construct?) after we notice that there is no
	 * String content.
	 * 
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
    private void analyze() throws RepositoryException, MalformedQueryException, QueryEvaluationException {
        Repository r = data.getRepository();
        RepositoryConnection con = r.getConnection();
        contentType = ContentType.EMPTY;
        String query = "SELECT DISTINCT textValue, link, dialogueAct, " + "communicationChannel, utteranceRepresentation, templateName, emotionType, passData, keywords FROM " + "[{textBase} <" + LOCAL + "#literalStringValue> {}" + "<" + LOCAL + "#textValue> {textValue}, " + "{textBase} rdf:type{<" + LOCAL + "#AnswerCandidate>} ],\n" + "[{textBase} <" + LOCAL + "#literalStringValue> {}" + "<" + LOCAL + "#textValue> {textValue}, " + "{textBase} rdf:type{<" + LOCAL + "#Utterance>} ],\n" + "[{linkBase} rdf:type {<" + LOCAL + "#URL>},\n" + "{linkBase} <" + LOCAL + "#link> {link} ]\n," + "[{dialogueActBase} <" + LOCAL + "#hasDialogueAct> {dialogueActInstance} rdf:type {dialogueAct} " + "],\n" + "[{commBase} <" + LOCAL + "#hasCommunicationChannel> {communicationChannel}],\n" + "[{templateBase} <" + LOCAL + "#useTemplate> {templateName}],\n" + "[{passBase} <" + LOCAL + "#hasInput> {BMLData},\n" + "{BMLData} rdf:type {<" + LOCAL + "#BMLData>},\n" + "{BMLData}" + " <" + LOCAL + "#literalStringValue> {}" + "<" + LOCAL + "#textValue> {passData}],\n" + "[ {KWBase} rdf:type {<" + LOCAL + "#Keywords>},\n" + " {KWBase} <" + LOCAL + "#literalStringValue> {} <" + LOCAL + "#textValue> {keywords}],\n" + "[{emotionBase} <" + LOCAL + "#hasEmotion> {emotionInstance} rdf:type {emotionType}],\n" + "[{uttBase} <" + LOCAL + "#hasUtteranceRepresentation> {utteranceRepresentation}]";
        TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, query);
        TupleQueryResult result = tupleQuery.evaluate();
        while (result.hasNext()) {
            BindingSet bs = result.next();
            if (bs.hasBinding("textValue")) {
                textValue = bs.getValue("textValue").stringValue();
                contentType = ContentType.STRING;
            }
            if (bs.hasBinding("utteranceRepresentation")) {
                contentType = ContentType.CONCEPTS;
            }
            if (bs.hasBinding("dialogueAct")) {
                dialogueAct = bs.getValue("dialogueAct").stringValue();
            }
            if (bs.hasBinding("link")) {
                linkUrlList.add(bs.getValue("link").stringValue());
            }
            if (bs.hasBinding("templateName")) {
                templateName = bs.getValue("templateName").stringValue();
            }
            if (bs.hasBinding("communicationChannel")) {
                communicationChannel = bs.getValue("communicationChannel").stringValue();
            }
            if (bs.hasBinding("passData")) {
                passData = StringEscapeUtils.unescapeXml(bs.getValue("passData").stringValue());
            }
            if (bs.hasBinding("keywords")) {
                String keywords = bs.getValue("keywords").stringValue();
                keywordList = new LinkedList<Keyword>();
                for (String k : keywords.split(",")) {
                    String[] kw = k.split("\\:");
                    keywordList.add(new Keyword(kw[0], kw[1]));
                }
            }
            if (bs.hasBinding("emotionType")) {
                emotionType = bs.getValue("emotionType").stringValue();
                if (emotionType.equalsIgnoreCase(LOCAL + "#normal")) {
                    emotionType = "Sit";
                } else if (emotionType.equalsIgnoreCase(LOCAL + "#happy")) {
                    emotionType = "happy";
                } else if (emotionType.equalsIgnoreCase(LOCAL + "#sad")) {
                    emotionType = "sad";
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("invalid emotion type specified: " + emotionType);
                    }
                    emotionType = null;
                }
            }
        }
        if (dialogueAct == null) {
            if (log.isWarnEnabled()) {
                log.warn("DialogueAct missing - assuming default.");
            }
            dialogueAct = DEFAULT_DIALOGUE_ACT;
        }
        result.close();
        con.close();
    }

    public LinkedList<String> getLinkUrlList() {
        return linkUrlList;
    }

    public String getTextValue() {
        return textValue;
    }

    public String getTemplateName() {
        return templateName;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getCommunicationChannel() {
        return communicationChannel;
    }

    public enum ContentType {

        STRING, CONCEPTS, EMPTY
    }

    public String getDialogueAct() {
        return dialogueAct;
    }

    public RdfData getRdfData() {
        return data;
    }

    public String getEmotionType() {
        return emotionType;
    }

    public LinkedList<Keyword> getKeywords() {
        return keywordList;
    }

    public boolean hasPassData() {
        if (passData != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getPassData() {
        return passData;
    }
}
