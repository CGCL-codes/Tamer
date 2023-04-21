package otservices.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import otservices.mapper.translationrepository.Ontology;
import otservices.translator.interactionmodule.InteractionModuleInterface;
import otservices.translator.language.LanguageFactory;
import otservices.translator.language.LanguageException;
import otservices.translator.language.LanguageInterface;
import otservices.translator.language.ObjectInterface;
import otservices.translator.mapperserverclient.ws.MapperClientWebServiceInterface;
import otservices.translator.reputationreasoner.ReputationReasonerInterface;
import otservices.translator.strategy.TranslationStrategyInterface;
import otservices.translator.valuetransformation.ValueTransformationInterface;
import otservices.util.configuration.ConfigurationParser;
import otservices.util.log.Log;
import otservices.util.ObjectCopy;
import org.apache.log4j.Logger;

public class TranslatorController implements TranslatorConstants {

    private ConfigurationParser confTranslator;

    String id;

    private TranslationStrategyInterface inputTranslationStrategy;

    private InteractionModuleInterface interactionModule;

    private LanguageFactory internalLanguage;

    private String internalLanguageName;

    private Boolean isConnected = new Boolean(false);

    private static Logger logger = Logger.getLogger(TranslatorController.class.getName());

    private Map<String, String[]> mapInterchangeNative;

    private Map<String, String[]> mapNativeInterchange;

    private MapperClientWebServiceInterface mapperClient;

    private String ontInterchangeName;

    private Integer ontInterchangeVersion;

    private String ontNativeName;

    private Integer ontNativeVersion;

    private TranslationStrategyInterface outputTranslationStrategy;

    private Map<String, ObjectInterface> stack;

    private ReputationReasonerInterface reputationReasoner;

    private String urlMapperServer;

    private String valueTransformationConcept;

    private ValueTransformationInterface valueTransformationClass;

    /**
	 * Constructor method
	 * 
	 * @param XMLFilename
	 *            Configuration XML filename
	 * @param XSDFilename
	 *            Configuration schema specification filename
	 * @param logFilename
	 *            Log filename
	 * @return none
	 * @throws Exception
	 */
    public TranslatorController(String XMLFilename, String XSDFilename, String logFilename) throws Exception {
        try {
            new Log(logFilename);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.debug("Logging initialized");
        logger.debug("Loaded log4j configuration file = " + logFilename);
        this.confTranslator = new ConfigurationParser(XMLFilename, XSDFilename);
        this.id = (String) confTranslator.get("id");
        this.mapInterchangeNative = new HashMap<String, String[]>();
        this.mapNativeInterchange = new HashMap<String, String[]>();
        this.urlMapperServer = (String) confTranslator.get("urlMapperServer");
        this.mapperClient = (MapperClientWebServiceInterface) getClass().getClassLoader().loadClass((String) confTranslator.get("mapperClientClass")).newInstance();
        this.mapperClient.setURL(this.urlMapperServer);
        this.isConnected = this.mapperClient.connect();
        this.ontNativeName = (String) confTranslator.get("ontNativeName");
        this.ontNativeVersion = (Integer) confTranslator.get("ontNativeVersion");
        this.ontInterchangeName = (String) confTranslator.get("ontInterchangeName");
        this.ontInterchangeVersion = (Integer) confTranslator.get("ontInterchangeVersion");
        this.internalLanguageName = (String) confTranslator.get("internalLanguageName");
        this.internalLanguage = (LanguageFactory) getClass().getClassLoader().loadClass((String) confTranslator.get("internalLanguageClass")).newInstance();
        this.inputTranslationStrategy = (TranslationStrategyInterface) getClass().getClassLoader().loadClass((String) confTranslator.get("inputTranslationStrategyClass")).newInstance();
        this.inputTranslationStrategy.setWebServices(ontInterchangeName, ontInterchangeVersion, ontNativeName, ontNativeVersion, mapperClient);
        this.outputTranslationStrategy = (TranslationStrategyInterface) getClass().getClassLoader().loadClass((String) confTranslator.get("outputTranslationStrategyClass")).newInstance();
        this.outputTranslationStrategy.setWebServices(ontInterchangeName, ontInterchangeVersion, ontNativeName, ontNativeVersion, mapperClient);
        this.reputationReasoner = (ReputationReasonerInterface) getClass().getClassLoader().loadClass((String) confTranslator.get("reputationReasonerClass")).newInstance();
        this.reputationReasoner.setID(this.id);
        this.valueTransformationConcept = ((String) confTranslator.get("valueTransformationConcept")).toLowerCase();
        this.valueTransformationClass = (ValueTransformationInterface) getClass().getClassLoader().loadClass((String) confTranslator.get("valueTransformationClass")).newInstance();
        this.stack = new Hashtable<String, ObjectInterface>();
        this.confTranslator = null;
    }

    /**
	 * 
	 */
    public void clearStack() {
        this.stack.clear();
    }

    /**
	 * Return the Translator Controller id
	 * 
	 * @param none
	 * @return Translator Controller id
	 */
    public String getID() {
        return this.id;
    }

    /**
	 * Return the Input Translation Strategy interface
	 * 
	 * @param none
	 * @return Input Translation Strategy interface
	 */
    public TranslationStrategyInterface getInputTranslationStrategy() {
        return this.inputTranslationStrategy;
    }

    /**
	 * Return the Interaction Module interface
	 * 
	 * @param none
	 * @return Interaction Module interface
	 */
    public InteractionModuleInterface getInteractionModule() {
        return this.interactionModule;
    }

    /**
	 * Return the internal Language Parser interface
	 * 
	 * @param none
	 * @return Internal Language Parser interface
	 */
    public LanguageFactory getInternalLanguage() {
        return this.internalLanguage;
    }

    /**
	 * Return the Internal Language Name
	 * 
	 * @param none
	 * @return Internal Query Language Name
	 */
    public String getInternalLanguageName() {
        return this.internalLanguageName;
    }

    /**
	 * Return the Mapper Client interface
	 * 
	 * @param none
	 * @return Mapper Client interface
	 */
    public MapperClientWebServiceInterface getMapperClient() {
        return this.mapperClient;
    }

    /**
	 * Return the Ontology Interchange name
	 * 
	 * @param none
	 * @return Ontology Interchange name
	 */
    public String getOntInterchangeName() {
        return this.ontInterchangeName;
    }

    /**
	 * Return the Ontology Interchange version
	 * 
	 * @param none
	 * @return Ontology Interchange version
	 */
    public Integer getOntInterchangeVersion() {
        return this.ontInterchangeVersion;
    }

    /**
	 * Return the Ontology Native name
	 * 
	 * @param none
	 * @return Ontology Native name
	 */
    public String getOntNativeName() {
        return this.ontNativeName;
    }

    /**
	 * Return the Ontology Native version
	 * 
	 * @param none
	 * @return Ontology Native version
	 */
    public Integer getOntNativeVersion() {
        return this.ontNativeVersion;
    }

    /**
	 * Return the Output Translation Strategy interface
	 * 
	 * @param none
	 * @return Output Translation Strategy interface
	 */
    public TranslationStrategyInterface getOutputTranslationStrategy() {
        return this.outputTranslationStrategy;
    }

    /**
	 * Return the Reputation Reasoner interface
	 * 
	 * @param none
	 * @return Reputation Reasoner interface
	 */
    public ReputationReasonerInterface getReputationReasoner() {
        return this.reputationReasoner;
    }

    /**
	 * Return the Mapper Server URL
	 * 
	 * @param none
	 * @return Mapper Server URL
	 */
    public String getURLMapperServer() {
        return this.urlMapperServer;
    }

    /**
	 * Return the concept associated to the Value Transformation
	 * 
	 * @param none
	 * @return Concept associated to the Value Transformation
	 */
    public String getValueTransformationConcept() {
        return this.valueTransformationConcept;
    }

    /**
	 * Return the Value Transformation interface
	 * 
	 * @param none
	 * @return Value Transformation interface
	 */
    public ValueTransformationInterface getValueTransformation() {
        return this.valueTransformationClass;
    }

    /**
	 * Return if the Translator Controller is connected to the Mapper Server
	 * 
	 * @param none
	 * @return true - if it is connected / false - otherwise
	 */
    public Boolean isConnected() {
        return this.isConnected;
    }

    /**
	 * Process a incoming message and reply it if required
	 * 
	 * @param sender
	 *            Sender's address
	 * @param language
	 *            Internal language
	 * @param ontology
	 *            Interchange ontology
	 * @param version
	 *            Interchange ontology version
	 * @param msgType
	 *            Type of the message
	 * @param message
	 *            Message content
	 * @param reply
	 *            Message reply identification
	 * @return Translated received object message
	 */
    public synchronized ObjectInterface receiveReputationMessage(String sender, String language, String ontology, Integer version, String message, String reply) throws LanguageException {
        ObjectInterface result = null;
        ObjectInterface originalParsed = null;
        ObjectInterface parsed = null;
        if ((language.equals(this.internalLanguageName)) && (ontology.equals(this.ontInterchangeName)) && (version.intValue() == this.ontInterchangeVersion.intValue())) {
            LanguageInterface parser = this.internalLanguage.createParser(message);
            parser.run();
            Object parsedObject = parser.getParsedObject();
            parsed = (ObjectInterface) parsedObject;
            originalParsed = (ObjectInterface) ObjectCopy.copy(parsed);
            if (parsed.getMessageType().intValue() == TranslatorConstants.REQUEST.intValue()) {
                parsed.setSender(sender);
                List<String> conceptsList = parsed.getConcepts();
                Map<String, String[]> concepts = null;
                if (conceptsList != null) {
                    concepts = this.translateConcepts(conceptsList, INPUT);
                    parsed.updateConcepts(concepts);
                }
                parsed.updateValue(this.getValueTransformationConcept(), this.getValueTransformation(), INPUT);
                result = this.reputationReasoner.processInMessage(parsedObject);
                result.updateValue(this.getValueTransformationConcept(), this.getValueTransformation(), OUTPUT);
                originalParsed.setMapTable(result.getMapTable());
                originalParsed.update(result);
                String[] receivers = new String[1];
                receivers[0] = sender;
                this.interactionModule.outMessage(receivers, language, ontology, version, TranslatorConstants.RESULT, originalParsed.getMessage(), reply);
                result = originalParsed;
            } else if (parsed.getMessageType().intValue() == TranslatorConstants.INFORM.intValue()) {
                parsed.setSender(sender);
                List<String> conceptsList = parsed.getConcepts();
                Map<String, String[]> concepts = null;
                if (conceptsList != null) {
                    concepts = this.translateConcepts(conceptsList, INPUT);
                    parsed.updateConcepts(concepts);
                }
                parsed.updateValue(this.getValueTransformationConcept(), this.getValueTransformation(), INPUT);
                this.reputationReasoner.processInMessage(parsedObject);
                result = parsed;
            } else if (parsed.getMessageType().intValue() == TranslatorConstants.RESULT.intValue()) {
                if (this.stack.containsKey(reply)) {
                    originalParsed = this.stack.get(reply);
                    this.stack.remove(reply);
                    originalParsed.update(parsed);
                    originalParsed.updateValue(this.getValueTransformationConcept(), this.getValueTransformation(), INPUT);
                    originalParsed.setSender(sender);
                    this.reputationReasoner.processInMessage(originalParsed);
                    result = originalParsed;
                }
            } else if (parsed.getMessageType().intValue() == TranslatorConstants.FAULT.intValue()) {
                if (this.stack.containsKey(reply)) {
                    this.stack.remove(reply);
                }
            }
        }
        if (result == null) {
            throw new LanguageException();
        }
        originalParsed = null;
        parsed = null;
        return result;
    }

    /**
	 * Process a outgoing message and send it out
	 * 
	 * @param receivers
	 *            Message target agent
	 * @param language
	 *            Internal language
	 * @param ontology
	 *            Native ontology
	 * @param version
	 *            Native ontology version
	 * @param msgType
	 *            Type of the message
	 * @param message
	 *            Message content
	 * @return Translated sent object message
	 * @throws LanguageException
	 */
    public synchronized ObjectInterface sendReputationMessage(String[] receivers, String language, String ontology, Integer version, String message, String replyWith) throws LanguageException {
        ObjectInterface parsed = null;
        if ((language.equals(this.internalLanguageName)) && (ontology.equals(this.ontNativeName)) && (version.intValue() == this.ontNativeVersion.intValue())) {
            LanguageInterface parser = this.internalLanguage.createParser(message);
            parser.run();
            Object parsedObject = parser.getParsedObject();
            parsed = (ObjectInterface) parsedObject;
            ObjectInterface originalParsed = (ObjectInterface) ObjectCopy.copy(parsed);
            List<String> conceptsList = parsed.getConcepts();
            Map<String, String[]> concepts = null;
            if (conceptsList != null) {
                concepts = this.translateConcepts(conceptsList, OUTPUT);
            }
            parsed.updateValue(this.getValueTransformationConcept(), this.getValueTransformation(), OUTPUT);
            if (conceptsList != null) {
                parsed.updateConcepts(concepts);
            }
            if (parsed.getMessageType().intValue() == TranslatorConstants.INFORM.intValue()) {
                this.interactionModule.outMessage(receivers, this.internalLanguageName, this.ontInterchangeName, this.ontInterchangeVersion, parsed.getMessageType(), parsed.getMessage(), null);
            } else if (parsed.getMessageType().intValue() == TranslatorConstants.REQUEST.intValue()) {
                if (replyWith == null) {
                    replyWith = parsed.getSender() + (new Double(Math.random() * 100)).toString() + new Long(System.currentTimeMillis()).toString();
                }
                originalParsed.setMapTable(parsed.getMapTable());
                this.stack.put(replyWith, originalParsed);
                this.interactionModule.outMessage(receivers, this.internalLanguageName, this.ontInterchangeName, this.ontInterchangeVersion, parsed.getMessageType(), parsed.getMessage(), replyWith);
            }
        }
        if (parsed == null) {
            throw new LanguageException();
        }
        return parsed;
    }

    /**
	 * Set the Interaction Module interface
	 * 
	 * @param interactionModule
	 *            Interaction Module interface
	 * @return none
	 */
    public void setInteractionModule(InteractionModuleInterface interactionModule) {
        this.interactionModule = interactionModule;
    }

    /**
	 * Translate the concepts between ontologies considering the Translation
	 * Strategies configured
	 * 
	 * @param conceptsList
	 *            List of concepts to translate
	 * @param inOut
	 *            true if incoming message / false - if outgoing message
	 * @return Translation Map of the concepts
	 */
    private synchronized Map<String, String[]> translateConcepts(List<String> conceptsList, Boolean inOut) {
        Map<String, String[]> result = new HashMap<String, String[]>();
        Ontology ontFrom = new Ontology();
        Ontology ontTo = new Ontology();
        if (inOut) {
            ontFrom.setType(Ontology.TYPE_INTERCHANGE);
            ontFrom.setUri(this.ontInterchangeName);
            ontFrom.setVersion(this.ontInterchangeVersion);
            ontTo.setType(Ontology.TYPE_NATIVE);
            ontTo.setUri(this.ontNativeName);
            ontTo.setVersion(this.ontNativeVersion);
        } else {
            ontFrom.setType(Ontology.TYPE_NATIVE);
            ontFrom.setUri(this.ontNativeName);
            ontFrom.setVersion(this.ontNativeVersion);
            ontTo.setType(Ontology.TYPE_INTERCHANGE);
            ontTo.setUri(this.ontInterchangeName);
            ontTo.setVersion(this.ontInterchangeVersion);
        }
        if (!conceptsList.isEmpty()) {
            List<String> t = new ArrayList<String>();
            Iterator<String> i = conceptsList.iterator();
            String fromConcept;
            String[] toConcepts;
            while (i.hasNext()) {
                fromConcept = i.next();
                if (!t.contains(fromConcept)) {
                    toConcepts = null;
                    if (inOut) {
                        if (this.mapInterchangeNative.containsKey(fromConcept)) {
                            toConcepts = this.mapInterchangeNative.get(fromConcept);
                        } else {
                            toConcepts = mapperClient.translateConcept(fromConcept, ontFrom, ontTo);
                            this.mapInterchangeNative.put(fromConcept, toConcepts);
                        }
                    } else {
                        if (this.mapNativeInterchange.containsKey(fromConcept)) {
                            toConcepts = this.mapNativeInterchange.get(fromConcept);
                        } else {
                            toConcepts = mapperClient.translateConcept(fromConcept, ontFrom, ontTo);
                            this.mapNativeInterchange.put(fromConcept, toConcepts);
                        }
                    }
                    String[] concepts;
                    if (inOut) {
                        concepts = this.inputTranslationStrategy.run(fromConcept, toConcepts);
                    } else {
                        concepts = this.outputTranslationStrategy.run(fromConcept, toConcepts);
                    }
                    result.put(fromConcept, concepts);
                    t.add(fromConcept);
                }
            }
        }
        return result;
    }
}
