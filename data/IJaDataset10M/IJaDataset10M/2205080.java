package net.sf.japaki.self;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import net.sf.japaki.basic.DuplicateKeyException;
import net.sf.japaki.beans.NoSuchPropertyException;
import net.sf.japaki.beans.SelfProperty;
import net.sf.japaki.kit.FixedFormatMold;
import net.sf.japaki.kit.ParserStore;
import net.sf.japaki.text.BeanStoreException;
import net.sf.japaki.text.EnumFormat;
import net.sf.japaki.text.FormatException;
import net.sf.japaki.text.Parser;
import net.sf.japaki.text.ParsingException;
import net.sf.japaki.text.ReaderWithPosition;
import net.sf.japaki.text.WriterWithPosition;

/**
 * This is the standard entry point to the japaki library.
 * The typical usage is as follows:<ol>
 * <li>{@link #ParserBench() Create} a new parser bench.</li>
 * <li>Configure the {@link #getParserStore() parser store}, if necessary.
 * <li>Add parser definitions, either by<ul>
 *    <li>{@link #loadSyntax(String) loading} them from a file or by</li>
 *    <li>{@link #add adding} a definition for each class.
 *    </ul></li>
 * <li>Create an instance of the topmost bean.</li>
 * <li>Start {@link #parse(String,String,Object) parsing}
 *  or {@link #write(String,String,Object) formatting}.</li>
 * <li>{@link #handleException(Exception) Handle} exceptions.</li>
 * </ol>
 * @version 1.0
 * @author Ralph Wagner
 */
public class ParserBench {

    private InspectorMill inspectorMill = new InspectorMill();

    private Parser<BeanParserBean> syntaxParser = SyntaxParser.getSyntaxParser();

    private static ParserBench phraseBench = null;

    private static final LeafParserBean delimiterBean = delimiterBean();

    private static LeafParserBean delimiterBean() {
        LeafParserBean reply = new LeafParserBean();
        reply.setParserName("anywhere");
        return reply;
    }

    /**
     * Specifies the inspector mill to be used.
     * @param inspectorMill new inspector mill
     */
    public void setInspectorMill(InspectorMill inspectorMill) {
        this.inspectorMill = inspectorMill;
    }

    /**
     * Returns the underlying parser store.
     * @return the underlying parser store
     */
    public ParserStore getParserStore() {
        return inspectorMill.getParserStore();
    }

    private static ParserBench getPhraseBench() throws IOException, ParseException {
        if (phraseBench == null) {
            phraseBench = new ParserBench();
            phraseBench.getParserStore().add("bean", phraseBench.syntaxParser, BeanParserBean.class);
            phraseBench.add("phrase", PhraseBean.class, "<phraseName,,\",\">,<baseClass> := <bean>\\n");
        }
        return phraseBench;
    }

    /**
     * Creates a mold from an enum type:<ol>
     * <li>Looks for a property file at the specified place.</li>
     * <li>Creates a new enum format from the property file.</li>
     * <li>Creates a mold from the format and stores it in the mold
     *  repository.</li>
     * <li>The created mold is also default for the specified type.</li>
     * </ol>
     * @param name name of the parser mold in the repository
     * @param bundleName location of the property file
     * @param type an enum type
     * @throws NullPointerException if any parameter is {@code null}
     * @throws DuplicateKeyException if there is already an entry for the name
     * @see ResourceBundle
     * @throws MissingResourceException if no resource bundle for the
     *  specified base name can be found
     */
    private <T extends Enum<T>> void addEnum(String name, String bundleName, Class<T> type) {
        getParserStore().addDefault(name, new FixedFormatMold<T>(type, EnumFormat.fromBundle(type, ResourceBundle.getBundle(bundleName))));
    }

    /**
     * Creates a mold from an enum type:<ol>
     * <li>Looks for a property file in the class path at the same place as
     *  the class file of the given type.</li>
     * <li>Creates a new enum format from the property file.</li>
     * <li>Creates a mold from the format and stores it in the mold
     *  repository under the {@link Class#getSimpleName simple name}
     *  of the type.</li>
     * <li>The created mold is also default for the specified type.</li>
     * </ol>
     * @param type an enum type
     * @throws NullPointerException if {@code type} is {@code null}
     * @throws DuplicateKeyException if there is already an entry for the name
     * @throws MissingResourceException if no resource bundle for the
     *  specified base name can be found
     * @see ResourceBundle
     */
    public <T extends Enum<T>> void addEnum(Class<T> type) {
        addEnum(type.getSimpleName(), type.getName(), type);
    }

    /**
     * Adds a new parser to the underlying parser store.
     * @param name name of the new parser
     * @param type the class that the parse can handle
     * @param pattern pattern representation of the new parser,
     *   the syntax is described in {@link SyntaxParser}.
     * @throws NullPointerException if type is {@code null} or
     *  if a parameter in the pattern is missing
     * @throws DuplicateKeyException if there is already an entry for the name
     * @throws IllegalArgumentException if a parameter in the pattern
     *  can not be used to create a parser
     * @throws NoSuchPropertyException if the pattern contains an invalid
     *  property name
     * @throws ParseException when an exception occured while parsing the
     *  pattern
     * @throws IOException If an I/O error occurs
     */
    public <T> void add(String name, Class<T> type, String pattern) throws IOException, ParseException {
        BeanParserBean parserBean = new BeanParserBean();
        ReaderWithPosition reader = new ReaderWithPosition(new StringReader(pattern));
        syntaxParser.parse(reader, parserBean);
        getParserStore().add(name, parserBean.toParser(inspectorMill.getInspector(type)), type);
    }

    /**
     * Loads parsers definitions from a file and stores them in the
     * underlying parser store.
     * @param fileName name of the syntax definition file
     * @throws BeanStoreException if the parsed value could not be stored
     * @throws NullPointerException if a parameter in the input is missing
     * @throws IllegalArgumentException if a parameter in the input reader
     *  can not be used to create a parser
     * @throws NoSuchPropertyException if the input file contains an invalid
     *  property name
     * @throws IOException If an I/O error occurs
     * @throws ParseException when an exception occured while parsing the
     *  file
     * @see #loadSyntax(String)
     */
    @SuppressWarnings("unchecked")
    private void loadSyntax(ReaderWithPosition rwp) throws IOException, ParseException {
        ArrayList<PhraseBean> phrases = new ArrayList<PhraseBean>();
        getPhraseBench().parse("phrase", rwp, phrases);
        for (PhraseBean phrase : phrases) {
            phrase.getBean().setDelimiter(delimiterBean);
            getParserStore().add(phrase.getPhraseName(), phrase.getBean().toParser(inspectorMill.getInspector(phrase.getBaseClass())), phrase.getBaseClass());
        }
    }

    /**
     * Loads parsers definitions from a file and stores them in the
     * underlying parser store.</br>
     * The file must contain entries of the form
     * <blockquote><code>
     * name,class := pattern
     * </code></blockquote>
     * where<ul>
     * <li>name is the name under which it is store in the parser store, and
     * <li>class is the type of the object where the parsing result is
     *   stored. </li>
     * <li>pattern describes the parser,
     *   its syntax is described in {@link SyntaxParser}.</li>
     * </ul>
     * @param fileName name of the syntax definition file
     * @throws BeanStoreException if the parsed value could not be stored
     * @throws IOException If an I/O error occurs
     * @throws ParseException when an exception occured while parsing the
     *  file
     */
    public void loadSyntax(String fileName) throws IOException, ParseException {
        loadSyntax(new ReaderWithPosition(fileName));
    }

    /**
     * Loads parsers definitions from a file in the classpath and stores them
     * in the underlying parser store.
     * @param fileName name of the syntax definition file
     * @throws BeanStoreException if the parsed value could not be stored
     * @throws IOException If an I/O error occurs
     * @throws ParseException when an exception occured while parsing the
     *  file
     * @see #loadSyntax(String)
     */
    public <T> void loadSyntaxFromResource(String fileName) throws IOException, ParseException {
        loadSyntax(new ReaderWithPosition(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName))));
    }

    /**
     * @throws NullPointerException if {@code object} is {@code null}
     * @throws IllegalArgumentException if a parser of the given name is not
     *  found in the store
     */
    @SuppressWarnings("unchecked")
    private <T> Parser<T> findParser(String name, T object) {
        String subParserName = null;
        if ((object instanceof Collection) || (object instanceof Map)) {
            subParserName = name;
            name = null;
        }
        return getParserStore().findParser(name, SelfProperty.newInstance((Class<T>) object.getClass()), null, subParserName, null);
    }

    private <T> void parse(String parserName, ReaderWithPosition rwp, T target) throws IOException, ParseException {
        try {
            findParser(parserName, target).parse(rwp, target);
            if (!rwp.eof()) {
                throw rwp.getExceptionLog().last();
            }
        } catch (ParseException e) {
            throw rwp.getExceptionLog().last();
        }
    }

    /**
     * Performs the following steps:<ol>
     * <li>The parser is looked up in the parser store.</li>
     * <li>The content of the specified reader is parsed.</li>
     * <li>The parsing result is stored in the target object.</li>
     * </ol>
     * @param parserName name of the parser to be used
     * @param reader reader with the text to be parsed
     * @param target object where the parsing result is stored
     * @throws NullPointerException if {@code reader} or {@code target}
     *  or one of its (sub-) properties is {@code null}
     *  and the parser cannot handle it
     * @throws IllegalArgumentException if a parser of the given name is not
     *  found in the store
     * @throws BeanStoreException if the parsed value could not be stored
     * @throws IOException If an I/O error occurs
     * @throws ParseException if the reader content could not be parsed
     */
    public <T> void parse(String parserName, Reader reader, T target) throws IOException, ParseException {
        parse(parserName, new ReaderWithPosition(reader), target);
    }

    /**
     * Performs the following steps:<ol>
     * <li>The parser is looked up in the parser store.</li>
     * <li>The content of the specified file is parsed.</li>
     * <li>The parsing result is stored in the target object.</li>
     * </ol>
     * @param parserName name of the parser to be used
     * @param fileName name of the file with the text to be parsed
     * @param target object where the parsing result is stored
     * @throws NullPointerException if {@code target}
     *  or one of its (sub-) properties is {@code null}
     *  and the parser cannot handle it
     * @throws IllegalArgumentException if a parser of the given name is not
     *  found in the store
     * @throws BeanStoreException if the parsed value could not be stored
     * @throws IOException If an I/O error occurs
     * @throws ParseException if the file content could not be parsed
     */
    public <T> void parse(String parserName, String fileName, T target) throws IOException, ParseException {
        parse(parserName, new ReaderWithPosition(fileName), target);
    }

    private <T> void write(String parserName, WriterWithPosition wps, T source) throws IOException, FormatException {
        try {
            findParser(parserName, source).write(wps, source);
        } finally {
            wps.flush();
            wps.close();
        }
    }

    /**
     * Performs the following steps:<ol>
     * <li>The parser is looked up in the parser store.</li>
     * <li>The source object is formatted with the found parser.</li>
     * <li>The result is written to the specified writer.</li>
     * </ol>
     * @param parserName name of the parser to be used
     * @param writer writer where the formatted source object is written to
     * @param source object to be formatted
     * @throws NullPointerException if {@code writer} or {@code source}
     *  or one of its (sub-) properties is {@code null}
     *  and the parser cannot handle it
     * @throws IllegalArgumentException if a parser of the given name is not
     *  found in the store or if the parser cannot handle the source object
     * @throws IOException If an I/O error occurs
     * @throws FormatException if the source object can not be formatted with
     *  this parser.
     */
    public <T> void write(String parserName, Writer writer, T source) throws IOException, FormatException {
        write(parserName, new WriterWithPosition(writer), source);
    }

    /**
     * Performs the following steps:<ol>
     * <li>The parser is looked up in the parser store.</li>
     * <li>The source object is formatted with the found parser.</li>
     * <li>The result is written to the specified file.</li>
     * </ol>
     * @param parserName name of the parser to be used
     * @param fileName name of the file where the formatted source object is
     *  written to
     * @param source object to be formatted
     * @throws NullPointerException if {@code source}
     *  or one of its (sub-) properties is {@code null}
     *  and the parser cannot handle it
     * @throws IllegalArgumentException if a parser of the given name is not
     *  found in the store or if the parser cannot handle the source object
     * @throws IOException If an I/O error occurs
     * @throws FormatException if the source object can not be formatted with
     *  this parser.
     */
    public <T> void write(String parserName, String fileName, T source) throws IOException, FormatException {
        write(parserName, new WriterWithPosition(fileName), source);
    }

    /**
     * Performs the default way to handle exceptions that occur during
     *  parsing or formatting.
     * @param e an exception that was thrown
     * @throws RuntimeException {@code RuntimeExceptions} are not handled
     *  by this method, but passed on.
     */
    public static void handleException(Exception e) throws RuntimeException {
        if (e instanceof ParsingException) {
            ((ParsingException) e).printDetails();
        } else if (e instanceof FormatException) {
            e.printStackTrace();
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the logging mechanism.
     * Messages are handled by a {@link ConsoleHandler}.
     */
    public static void useLoggingDefaults() {
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
        ch.setFormatter(new SimpleFormatter());
        Logger.getLogger("").addHandler(ch);
    }
}
