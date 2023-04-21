package ru.dpelevin.gddc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.AbstractResource;
import org.xml.sax.SAXException;
import ru.dpelevin.command.ApplicationCommandReaction;
import ru.dpelevin.gddc.command.GetAvaibleTranslationDirectionsForLanguageCommand;
import ru.dpelevin.gddc.command.TranslateCommand;
import ru.dpelevin.gddc.command.TranslateCompleteCommand;
import ru.dpelevin.gddc.command.TranslateInProgressCommand;
import ru.dpelevin.gddc.model.Language;
import ru.dpelevin.gddc.model.response.google.Response;
import ru.dpelevin.gddc.service.TranslationManager;
import ru.dpelevin.gddc.service.util.LanguageIdConstants;
import ru.dpelevin.http.WebClient;
import ru.dpelevin.http.WebRequest;
import ru.dpelevin.http.WebResult;
import ru.dpelevin.http.util.HtmlHelper;
import ru.dpelevin.service.command.ApplicationCommandPublisher;
import ru.dpelevin.service.impl.AbstractApplicationListenerManager;
import ru.dpelevin.util.ValuePairHolder;
import ru.dpelevin.util.XmlHelper;
import ru.dpelevin.util.concurent.ArrayBlockingBuffer;

/**
 * Implementation of {@link ru.dpelevin.gddc.service.TranslationManager}.
 * 
 * @author Dmitry Pelevin
 */
public class TranslationManagerImpl extends AbstractApplicationListenerManager implements TranslationManager {

    /**
	 * Default thread sleep time.
	 */
    private static final int DEFAULT_THREAD_SLEEP_TIME = 2000;

    /** The log. */
    private static final Log log = LogFactory.getLog("rootLogger");

    /** The russian abc. */
    private Set<Character> russianAbc = new HashSet<Character>(Arrays.asList(new Character[] { 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я' }));

    /** The english abc. */
    @SuppressWarnings("unused")
    private Set<Character> englishAbc = new HashSet<Character>(Arrays.asList(new Character[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' }));

    /** The languages by id. */
    private Map<String, Language> languagesById = new LinkedHashMap<String, Language>();

    /** The dictionaries avaible for language. */
    private Map<String, Set<Language>> dictionariesAvaibleForLanguage = new HashMap<String, Set<Language>>();

    /** The web client. */
    private WebClient webClient;

    /** The marshaller. */
    private Marshaller marshaller;

    /** The unmarshaller. */
    private Unmarshaller unmarshaller;

    /** The context. */
    private JAXBContext context;

    /** The schema resource. */
    private AbstractResource schemaResource;

    /** The queue. */
    private ArrayBlockingBuffer<Runnable> queue = new ArrayBlockingBuffer<Runnable>(1, true);

    /** The translation executor. */
    private ExecutorService translationExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, queue);

    /** The application command publisher. */
    private ApplicationCommandPublisher applicationCommandPublisher;

    /**
	 * English.
	 */
    private Language enLang;

    /**
	 * French.
	 */
    private Language frLang;

    /**
	 * German.
	 */
    private Language deLang;

    /**
	 * Italian.
	 */
    private Language itLang;

    /**
	 * Korean.
	 */
    private Language koLang;

    /**
	 * Spanish.
	 */
    private Language esLang;

    /**
	 * Russian.
	 */
    private Language ruLang;

    /**
	 * Chinese (traditional).
	 */
    private Language zhTwLang;

    /**
	 * Chinese (simplified).
	 */
    private Language zhCnLang;

    /**
	 * Portuguese.
	 */
    private Language ptLang;

    /**
	 * Hindi.
	 */
    private Language hiLang;

    /**
	 * Arabic.
	 */
    private Language arLang;

    /**
	 * Czech.
	 */
    private Language csLang;

    /**
	 * Thai.
	 */
    private Language thLang;

    /**
	 * Bulgarian.
	 */
    private Language bgLang;

    /**
	 * Croatian.
	 */
    private Language hrLang;

    /**
	 * Finnish.
	 */
    private Language fiLang;

    /**
	 * Hebrew.
	 */
    private Language iwLang;

    /**
	 * Greek.
	 */
    private Language elLang;

    /**
	 * Serbian.
	 */
    private Language srLang;

    /**
	 * Instantiates a new translation manager impl.
	 */
    public TranslationManagerImpl() {
        initializeLanguages();
        initializeTranslationDirections();
    }

    /**
	 * Initialize languages.
	 */
    private void initializeLanguages() {
        enLang = new Language(LanguageIdConstants.ENGLISH, "english");
        languagesById.put(LanguageIdConstants.ENGLISH, enLang);
        frLang = new Language(LanguageIdConstants.FRENCH, "french");
        languagesById.put(LanguageIdConstants.FRENCH, frLang);
        deLang = new Language(LanguageIdConstants.GERMAN, "german");
        languagesById.put(LanguageIdConstants.GERMAN, deLang);
        itLang = new Language(LanguageIdConstants.ITALIAN, "italian");
        languagesById.put(LanguageIdConstants.ITALIAN, itLang);
        koLang = new Language(LanguageIdConstants.KOREAN, "korean");
        languagesById.put(LanguageIdConstants.KOREAN, koLang);
        esLang = new Language(LanguageIdConstants.SPANISH, "spanish");
        languagesById.put(LanguageIdConstants.SPANISH, esLang);
        ruLang = new Language(LanguageIdConstants.RUSSIAN, "russian");
        languagesById.put(LanguageIdConstants.RUSSIAN, ruLang);
        zhTwLang = new Language(LanguageIdConstants.CHINESE_TW, "chinese (traditional)");
        languagesById.put(LanguageIdConstants.CHINESE_TW, zhTwLang);
        zhCnLang = new Language(LanguageIdConstants.CHINESE_CN, "chinese (simplified)");
        languagesById.put(LanguageIdConstants.CHINESE_CN, zhCnLang);
        ptLang = new Language(LanguageIdConstants.PORTUGUESE, "portuguese");
        languagesById.put(LanguageIdConstants.PORTUGUESE, ptLang);
        hiLang = new Language(LanguageIdConstants.HINDI, "hindi");
        languagesById.put(LanguageIdConstants.HINDI, hiLang);
        arLang = new Language(LanguageIdConstants.ARABIC, "arabic");
        languagesById.put(LanguageIdConstants.ARABIC, arLang);
        csLang = new Language(LanguageIdConstants.CZECH, "czech");
        languagesById.put(LanguageIdConstants.CZECH, csLang);
        thLang = new Language(LanguageIdConstants.THAI, "thai");
        languagesById.put(LanguageIdConstants.THAI, thLang);
        bgLang = new Language(LanguageIdConstants.BULGARIAN, "bulgarian");
        languagesById.put(LanguageIdConstants.BULGARIAN, bgLang);
        hrLang = new Language(LanguageIdConstants.CROATIAN, "croatian");
        languagesById.put(LanguageIdConstants.CROATIAN, hrLang);
        fiLang = new Language(LanguageIdConstants.FINNISH, "finnish");
        languagesById.put(LanguageIdConstants.FINNISH, fiLang);
        iwLang = new Language(LanguageIdConstants.HEBREW, "hebrew");
        languagesById.put(LanguageIdConstants.HEBREW, iwLang);
        elLang = new Language(LanguageIdConstants.GREEK, "greek");
        languagesById.put(LanguageIdConstants.GREEK, elLang);
        srLang = new Language(LanguageIdConstants.SERBIAN, "serbian");
        languagesById.put(LanguageIdConstants.SERBIAN, srLang);
    }

    /**
	 * Initialize translation directions.
	 */
    private void initializeTranslationDirections() {
        Set<Language> enSet;
        enSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(enLang.getId(), enSet);
        Set<Language> frSet;
        frSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(frLang.getId(), frSet);
        Set<Language> deSet;
        deSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(deLang.getId(), deSet);
        Set<Language> itSet;
        itSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(itLang.getId(), itSet);
        Set<Language> koSet;
        koSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(koLang.getId(), koSet);
        Set<Language> esSet;
        esSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(esLang.getId(), esSet);
        Set<Language> ruSet;
        ruSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(ruLang.getId(), ruSet);
        Set<Language> zhTwSet;
        zhTwSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(zhTwLang.getId(), zhTwSet);
        Set<Language> zhCnSet;
        zhCnSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(zhCnLang.getId(), zhCnSet);
        Set<Language> ptSet;
        ptSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(ptLang.getId(), ptSet);
        Set<Language> hiSet;
        hiSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(hiLang.getId(), hiSet);
        Set<Language> arSet;
        arSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(arLang.getId(), arSet);
        Set<Language> csSet;
        csSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(csLang.getId(), csSet);
        Set<Language> thSet;
        thSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(thLang.getId(), thSet);
        Set<Language> bgSet;
        bgSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(bgLang.getId(), bgSet);
        Set<Language> hrSet;
        hrSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(hrLang.getId(), hrSet);
        Set<Language> fiSet;
        fiSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(fiLang.getId(), fiSet);
        Set<Language> iwSet;
        iwSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(iwLang.getId(), iwSet);
        Set<Language> elSet;
        elSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(elLang.getId(), elSet);
        Set<Language> srSet;
        srSet = new LinkedHashSet<Language>();
        dictionariesAvaibleForLanguage.put(srLang.getId(), srSet);
        enSet.add(frLang);
        enSet.add(deLang);
        enSet.add(koLang);
        enSet.add(itLang);
        enSet.add(ruLang);
        enSet.add(esLang);
        enSet.add(zhTwLang);
        enSet.add(zhCnLang);
        enSet.add(ptLang);
        enSet.add(hiLang);
        enSet.add(csLang);
        enSet.add(arLang);
        enSet.add(thLang);
        enSet.add(hrLang);
        enSet.add(fiLang);
        enSet.add(bgLang);
        enSet.add(iwLang);
        enSet.add(elLang);
        enSet.add(srLang);
        frSet.add(enLang);
        deSet.add(enLang);
        itSet.add(enLang);
        koSet.add(enLang);
        esSet.add(enLang);
        ruSet.add(enLang);
        zhTwSet.add(enLang);
        zhCnSet.add(enLang);
        ptSet.add(enLang);
        hiSet.add(enLang);
        arSet.add(enLang);
        csSet.add(enLang);
        thSet.add(enLang);
        bgSet.add(enLang);
        hrSet.add(enLang);
        fiSet.add(enLang);
        iwSet.add(enLang);
        elSet.add(enLang);
        srSet.add(enLang);
    }

    /**
	 * Initialization method for Spring.
	 */
    public final void init() {
        try {
            context = JAXBContext.newInstance("ru.dpelevin.gddc.model.response.google");
            SchemaFactory schemaFactory;
            schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema;
            schema = schemaFactory.newSchema(schemaResource.getURL());
            marshaller = context.createMarshaller();
            marshaller.setSchema(schema);
            marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public final List<Language> getAvaibleDictionaries() {
        return new ArrayList<Language>(languagesById.values());
    }

    /**
	 * {@inheritDoc}
	 */
    public final List<Language> getAvaibleTranslationDirectionsForLanguage(final String languageId) {
        Set<Language> resultSet;
        resultSet = dictionariesAvaibleForLanguage.get(languageId);
        List<Language> result;
        if (resultSet == null) {
            result = new ArrayList<Language>();
        } else {
            result = new ArrayList<Language>(resultSet);
        }
        return result;
    }

    /**
	 * Sets the schema resource.
	 * 
	 * @param schemaResource
	 *            the new schema resource
	 */
    public final void setSchemaResource(final AbstractResource schemaResource) {
        this.schemaResource = schemaResource;
    }

    /**
	 * Sets the web client.
	 * 
	 * @param webClient
	 *            the new web client
	 */
    public final void setWebClient(final WebClient webClient) {
        this.webClient = webClient;
    }

    /**
	 * Sets the application command publisher.
	 * 
	 * @param applicationCommandPublisher
	 *            the new application command publisher
	 */
    public final void setApplicationCommandPublisher(final ApplicationCommandPublisher applicationCommandPublisher) {
        this.applicationCommandPublisher = applicationCommandPublisher;
    }

    /**
	 * Register command reactions.
	 */
    @Override
    protected final void registerCommandReactions() {
        registerReaction(TranslateCommand.class, new TranslateCommandReaction());
        registerReaction(GetAvaibleTranslationDirectionsForLanguageCommand.class, new GetAvaibleTranslationDirectionsForLanguageCommandReaction());
    }

    /**
	 * The Class GetAvaibleTranslationDirectionsForLanguageCommandReaction.
	 */
    class GetAvaibleTranslationDirectionsForLanguageCommandReaction extends ApplicationCommandReaction<GetAvaibleTranslationDirectionsForLanguageCommand> {

        /**
		 * Execute.
		 * 
		 * @param command
		 *            the command
		 */
        @Override
        public void execute(final GetAvaibleTranslationDirectionsForLanguageCommand command) {
            command.returnResult(getAvaibleTranslationDirectionsForLanguage(command.getLanguageId()));
        }
    }

    /**
	 * The Class TranslateCommandReaction.
	 */
    class TranslateCommandReaction extends ApplicationCommandReaction<TranslateCommand> {

        /**
		 * Execute.
		 * 
		 * @param command
		 *            the command
		 */
        @Override
        public void execute(final TranslateCommand command) {
            translationExecutor.execute(new Runnable() {

                public void run() {
                    Response response;
                    response = getTranslationResponse(command.getQuery(), command.getSourceLanguageId(), command.getDestinationLanguageId());
                    command.returnResult(new ValuePairHolder<String, Response>(command.getQuery(), response));
                }
            });
        }
    }

    /**
	 * Shutdown.
	 */
    public final void shutdown() {
        translationExecutor.shutdown();
    }

    /**
	 * Gets the translation response.
	 * 
	 * @param word
	 *            the word
	 * @param sourceLanguageId
	 *            the source language id
	 * @param destinationLanguageId
	 *            the destination language id
	 * 
	 * @return the translation response
	 */
    public final Response getTranslationResponse(final String word, final String sourceLanguageId, final String destinationLanguageId) {
        return getTranslationResponseWithoutCaching(word, sourceLanguageId, destinationLanguageId);
    }

    /**
	 * Gets the translation response.
	 * 
	 * @param word
	 *            the word
	 * @param sourceLanguageId
	 *            the source language id
	 * @param destinationLanguageId
	 *            the destination language id
	 * 
	 * @return the translation response
	 */
    public final Response getTranslationResponseWithoutCaching(final String word, final String sourceLanguageId, final String destinationLanguageId) {
        try {
            applicationCommandPublisher.publishCommand(new TranslateInProgressCommand(this));
            if (word == null) {
                return null;
            }
            if (sourceLanguageId == null && destinationLanguageId == null) {
                boolean russianWord;
                russianWord = false;
                for (int i = 0; i < word.length(); i++) {
                    if (russianAbc.contains(word.charAt(i))) {
                        russianWord = true;
                        break;
                    }
                }
                String url;
                try {
                    if (russianWord) {
                        url = "http://www.google.com/dictionary?aq=f&langpair=ru|en&q=" + word + "&hl=en";
                    } else {
                        url = "http://www.google.com/dictionary?aq=f&langpair=en|ru&q=" + word + "&hl=en";
                    }
                    Response response = getResponseObjectFromURL(url);
                    return response;
                } catch (UnsupportedEncodingException e) {
                    log.error("", e);
                } catch (MalformedURLException e) {
                    log.error("", e);
                } catch (IOException e) {
                    log.error("", e);
                } catch (TransformerFactoryConfigurationError e) {
                    log.error("", e);
                }
            } else {
                String url;
                try {
                    url = "http://www.google.com/dictionary?aq=f&langpair=" + sourceLanguageId + "|" + destinationLanguageId + "&q=" + word + "&hl=" + destinationLanguageId;
                    Response response = getResponseObjectFromURL(url);
                    return response;
                } catch (UnsupportedEncodingException e) {
                    log.error("", e);
                } catch (URIException e) {
                    log.error("", e);
                } catch (HttpException e) {
                    log.error("", e);
                } catch (IOException e) {
                    log.error("", e);
                }
            }
            return null;
        } finally {
            applicationCommandPublisher.publishCommand(new TranslateCompleteCommand(this));
        }
    }

    /**
	 * Gets the response object from url.
	 * 
	 * @param url
	 *            the url
	 * 
	 * @return the response object from url
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public final Response getResponseObjectFromURL(final String url) throws IOException {
        WebRequest request;
        request = new WebRequest();
        request.setURL(new HttpURL(url));
        request.setPost(false);
        WebResult webResult;
        try {
            webResult = webClient.executeRequest(request);
        } catch (ProtocolException e) {
            try {
                Thread.sleep(DEFAULT_THREAD_SLEEP_TIME);
            } catch (InterruptedException e1) {
                log.warn("It's ok.", e1);
            }
            webResult = webClient.executeRequest(request);
        } catch (HttpException e) {
            try {
                Thread.sleep(DEFAULT_THREAD_SLEEP_TIME);
            } catch (InterruptedException e1) {
                log.warn("It's ok.", e1);
            }
            webResult = webClient.executeRequest(request);
        } catch (IOException e) {
            try {
                Thread.sleep(DEFAULT_THREAD_SLEEP_TIME);
            } catch (InterruptedException e1) {
                log.warn("It's ok.", e1);
            }
            webResult = webClient.executeRequest(request);
        }
        return getTranslationResponseObjectFromInputStream(webResult.getInputStream(), "UTF-8");
    }

    /**
	 * Gets the translation response object from input stream.
	 * 
	 * @param inputStream
	 *            the input stream
	 * @param encoding
	 *            the encoding
	 * 
	 * @return the translation response object from input stream
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public final Response getTranslationResponseObjectFromInputStream(final InputStream inputStream, final String encoding) throws IOException {
        org.w3c.dom.Document responseXml;
        responseXml = HtmlHelper.convertHtmlToXml(inputStream, "googleDictionaryResponce.xsl", encoding);
        Response response;
        response = transformXmlToObject(IOUtils.toInputStream(XmlHelper.documentToString(responseXml), encoding));
        return response;
    }

    /**
	 * Transform xml to object.
	 * 
	 * @param inputStream
	 *            the input stream
	 * 
	 * @return the response
	 */
    private Response transformXmlToObject(final InputStream inputStream) {
        try {
            XMLStreamReader xsr;
            xsr = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
            JAXBElement<Response> o;
            o = unmarshaller.unmarshal(xsr, Response.class);
            return o.getValue();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Gets the language by id.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the language by id
	 */
    public final Language getLanguageById(final String id) {
        return languagesById.get(id);
    }
}
