package net.sf.parser4j.gen4gen.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import net.sf.parser4j.gen4gen.entity.grammar.BaseInputGrammar4GenFromSpring;
import net.sf.parser4j.gen4gen.entity.grammar.InputGrammar4GenReader;
import net.sf.parser4j.gen4gen.entity.parserdata.IntermediateParserDataIO;
import net.sf.parser4j.generator.service.ParserDataGenerator;
import net.sf.parser4j.generator.service.ParserDataToHtml;
import net.sf.parser4j.generator.service.match.GenMatchPackage;
import net.sf.parser4j.kernelgenerator.entity.grammarnode.GrammarTreeFactoryData;
import net.sf.parser4j.kernelgenerator.entity.grammarnode.IGrammarNode;
import net.sf.parser4j.kernelgenerator.service.GeneratorException;
import net.sf.parser4j.kernelgenerator.service.KernelParserDataGenerator;
import net.sf.parser4j.kernelgenerator.service.grammarnode.GrammarNodeVisitException;
import net.sf.parser4j.parser.entity.ParseResult;
import net.sf.parser4j.parser.entity.data.NonTerminalMap;
import net.sf.parser4j.parser.entity.data.Pair;
import net.sf.parser4j.parser.entity.data.ParserData;
import net.sf.parser4j.parser.service.IParserListener;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.ParserFileReader;
import org.apache.log4j.Logger;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class Gen4GenSupport implements IParserListener {

    private static final Logger LOGGER = Logger.getLogger(Gen4GenSupport.class);

    private static final KernelParserDataGenerator KERNEL_PARSER_DATA_GENERATOR = KernelParserDataGenerator.getInstance();

    private static final ParserDataGenerator PARSER_DATA_GENERATOR = ParserDataGenerator.getInstance();

    private static final InputGrammar4GenReader INPUT_GRAMMAR4GEN_READER = InputGrammar4GenReader.getInstance();

    private static final ParserDataToHtml PARSER_DATA_TO_HTML = ParserDataToHtml.getInstance();

    private boolean logLine;

    /**
	 * all step generation of definitive parser data for parser generator
	 * 
	 * @throws ParserException
	 * @throws UnsupportedEncodingException
	 * @throws GeneratorException
	 * @throws IOException
	 * @throws GrammarNodeVisitException
	 */
    public void generateDefinitiveParserData() throws ParserException, UnsupportedEncodingException, GeneratorException, IOException, GrammarNodeVisitException {
        final ParserData baseXmlParserData = generateBaseParserDataFromXml();
        generateBaseParserDataFromTextAndNext(baseXmlParserData);
    }

    private void generateBaseParserDataFromTextAndNext(final ParserData baseXmlParserData) throws GeneratorException, ParserException, IOException {
        final ParserData baseTxtParserData = generateBaseParserDataFromText(baseXmlParserData);
        if (baseTxtParserData != null) {
            generateDefinitiveParserData(baseTxtParserData);
        }
    }

    public ParserData generateBaseParserDataFromXml() throws GrammarNodeVisitException, IOException, ParserException, GeneratorException {
        LOGGER.info("get base grammar tree" + " from spring xml definition");
        final GrammarTreeFactoryData grammarData = BaseInputGrammar4GenFromSpring.getInstance().getGrammar();
        final NonTerminalMap nonTerminalMap = grammarData.getNonTerminalMap();
        final IGrammarNode rootGrammarNode = grammarData.getRootGrammarNode();
        final String grammarFileUrl = grammarData.getGrammarFileUrl();
        LOGGER.info("generate base parser data from xml grammar");
        final String matchMgrPackageName = GenMatchPackage.getPackageName();
        final ParserData baseXmlParserData = KERNEL_PARSER_DATA_GENERATOR.generate(grammarFileUrl, rootGrammarNode, nonTerminalMap, matchMgrPackageName);
        IntermediateParserDataIO.getInstance().writeBaseXmlParserData(baseXmlParserData);
        final File htmlFile = new File("runtime/base_xml_parser_data.html");
        PARSER_DATA_TO_HTML.generateHtml(baseXmlParserData, htmlFile);
        return baseXmlParserData;
    }

    public void generateBaseParserDataFromText() throws UnsupportedEncodingException, MalformedURLException, GeneratorException, ParserException, IOException {
        generateBaseParserDataFromText(IntermediateParserDataIO.getInstance().readBaseXmlParserData());
    }

    private ParserData generateBaseParserDataFromText(final ParserData baseXmlParserData) throws UnsupportedEncodingException, MalformedURLException, GeneratorException, ParserException, IOException {
        LOGGER.info("generate base parser data  from base_input_grammar_4gen.txt file");
        final ParserFileReader reader = INPUT_GRAMMAR4GEN_READER.createBaseGrammarInput4GenReader();
        final String grammarFileUrl = new File(reader.getFileName()).toURI().toURL().toString();
        final File grammarDefTreeTextFile = new File("runtime/base_txt_grammardef.txt");
        final File grammarTreeTextFile = new File("runtime/base_txt_grammar.txt");
        final String matchMgrPackageName = GenMatchPackage.getPackageName();
        final Pair<ParseResult, ParserData> pair = PARSER_DATA_GENERATOR.generate(baseXmlParserData, grammarFileUrl, reader, matchMgrPackageName, grammarDefTreeTextFile, grammarTreeTextFile, "ws", this);
        final ParseResult baseTxtParseResult = pair.getMemberA();
        ParserData baseTxtParserData = pair.getMemberB();
        if (baseTxtParseResult.isInError()) {
            LOGGER.error(baseTxtParseResult.toString());
            baseTxtParserData = null;
        } else {
            IntermediateParserDataIO.getInstance().writeBaseTxtParserData(baseTxtParserData);
            final File htmlFile = new File("runtime/base_txt_parser_data.html");
            PARSER_DATA_TO_HTML.generateHtml(baseTxtParserData, htmlFile);
        }
        return baseTxtParserData;
    }

    public void generateDefinitiveParserDataFromBaseTxtParserData() throws GeneratorException, ParserException, IOException {
        generateDefinitiveParserData(IntermediateParserDataIO.getInstance().readBaseTxtParserData());
    }

    private void generateDefinitiveParserData(final ParserData baseTxtParserData) throws GeneratorException, ParserException, IOException {
        LOGGER.info("generate definitive parser data from definitive_input_grammar_4gen.txt file");
        final ParserFileReader reader = INPUT_GRAMMAR4GEN_READER.createDefinitiveGrammarInput4GenReader();
        final String grammarFileUrl = new File(reader.getFileName()).toURI().toURL().toString();
        final File grammarDefTreeTextFile = new File("runtime/definitive_grammardef.txt");
        final File grammarTreeTextFile = new File("runtime/definitive_grammar.txt");
        final String matchMgrPackageName = GenMatchPackage.getPackageName();
        final Pair<ParseResult, ParserData> pair = PARSER_DATA_GENERATOR.generate(baseTxtParserData, grammarFileUrl, reader, matchMgrPackageName, grammarDefTreeTextFile, grammarTreeTextFile, null, this);
        final ParseResult definitiveTxtParseResult = pair.getMemberA();
        final ParserData definitiveTxtParserData = pair.getMemberB();
        if (definitiveTxtParseResult.isInError()) {
            LOGGER.error(definitiveTxtParseResult.toString());
        } else {
            final ParserDataOutputForGenerator parserDataOutputForGenerator = ParserDataOutputForGenerator.getInstance();
            parserDataOutputForGenerator.writeParserData(definitiveTxtParserData);
            final File htmlFile = new File("runtime/definitive_parser_data.html");
            PARSER_DATA_TO_HTML.generateHtml(definitiveTxtParserData, htmlFile);
        }
    }

    @Override
    public void newLine(final int line) {
        if (logLine) {
            System.out.println(line);
        }
    }
}
