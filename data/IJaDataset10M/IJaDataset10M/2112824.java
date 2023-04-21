package ch.ethz.mxquery.cmdline;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import ch.ethz.mxquery.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ch.ethz.mxquery.query.XQCompiler;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.query.impl.CompilerImpl;
import ch.ethz.mxquery.query.impl.PreparedStatementImpl;
import ch.ethz.mxquery.query.parser.PlanLoader;
import ch.ethz.mxquery.query.parser.SchemaParser;
import ch.ethz.mxquery.cmdline.LocalSetting;
import ch.ethz.mxquery.cmdline.TimeInfos;
import ch.ethz.mxquery.contextConfig.CompilerOptions;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.QName;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.exceptions.StaticException;
import ch.ethz.mxquery.functions.FunctionGallery;
import ch.ethz.mxquery.model.VariableHolder;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.update.store.llImpl.LLStoreSet;
import ch.ethz.mxquery.util.FileReader;
import ch.ethz.mxquery.util.IOLib;
import ch.ethz.mxquery.util.KXmlSerializer;
import ch.ethz.mxquery.util.URIUtils;
import ch.ethz.mxquery.xdmio.XDMAtomicItemFactory;
import ch.ethz.mxquery.xdmio.XDMInputFactory;
import ch.ethz.mxquery.xdmio.XDMSerializer;
import ch.ethz.mxquery.xdmio.XDMSerializerSettings;

public class MXQuery {

    private static String VALIDATION_STRICT = "strict";

    private static String VALIDATION_LAX = "lax";

    private static String STATICALLY_KNOWN_NAMESPACES = "static-ns";

    private static String DEFAULT_ELEMENTTYPE_NAMESPACE = "default-elem-ns";

    private static String DEFAULT_FUNCTION_NAMESPACE = "default-func-ns";

    private static String CONSTRUCTION_MODE = "construction";

    private static String ORDERING_MODE = "ordering";

    private static String DEFAULT_ORDER_FOR_EMPTY_SEQUENCES = "default-es-order";

    private static String BOUNDARY_SPACE_POLICY = "boundary-space";

    private static String COPY_NAMESPACE_MODE = "copy-ns-mode";

    private static String BASE_URI = "base-uri";

    public static void main(String[] args) throws Exception {
        Options cliOptions = defineCliOptions();
        CommandLine cl = parseCliOptions(cliOptions, args);
        if (cl != null) {
            LocalSetting los = new LocalSetting();
            CompilerOptions cop = new CompilerOptions();
            Context ctx = new Context();
            XDMSerializerSettings serSet = new XDMSerializerSettings();
            TimeInfos timeInfos = new TimeInfos();
            interogateCliOptions(cl, los, cop, ctx);
            if (cl.hasOption("s")) {
                try {
                    serSet = processSerializerSettings(cl);
                } catch (MXQueryException e) {
                    System.err.print(e.getErrorCode() + " " + e.getMessage());
                    System.exit(-1);
                }
            }
            try {
                executeQuery(los.getInput(), ctx, timeInfos, los, serSet, cop, cl);
                if (los.isTiming()) {
                    PrintStream err = System.err;
                    err.print("\nMXQuery Engine from ETH Zurich\n");
                    ;
                    err.print("Compilation Time: " + (timeInfos.getCompileEnd() - timeInfos.getCompileStart() + " milliseconds\n"));
                    err.print("Execution Time: " + (timeInfos.getExecEnd() - timeInfos.getExecStart()) + " milliseconds\n");
                }
            } catch (Exception e) {
                if (los.isTiming()) {
                    PrintStream err = System.err;
                    err.print("MXQuery Engine from ETH Zurich -- ERROR!\n");
                    err.print("Execution Time: -1 milliseconds\n");
                    err.print(e.getMessage() + "\n");
                    for (int i = 0; i < e.getStackTrace().length; i++) {
                        StackTraceElement ste = e.getStackTrace()[i];
                        err.print("    at ");
                        err.print(ste.getClassName());
                        err.print(".");
                        err.print(ste.getMethodName());
                        err.print("(");
                        err.print(ste.getFileName());
                        err.print(":");
                        err.print(String.valueOf(ste.getLineNumber()));
                        err.print(")");
                        err.print("\n");
                    }
                }
                throw e;
            }
        }
        System.exit(0);
    }

    protected static Options defineCliOptions() {
        Options cliOptions = new Options();
        OptionGroup inputData = new OptionGroup();
        inputData.setRequired(true);
        inputData.addOption(OptionBuilder.hasArg().withArgName("queryFile").withDescription("specify the file of the query or plan to run").withLongOpt("queryFile").create("f"));
        inputData.addOption(OptionBuilder.hasArg().withArgName("inputQuery").withDescription("specify the query in the command line").withLongOpt("inlineQuery").create("i"));
        inputData.addOption(OptionBuilder.hasArg().withArgName("queryPlan").withDescription("specify the query plan to run").withLongOpt("queryPlan").create("p"));
        OptionGroup outputData = new OptionGroup();
        outputData.setRequired(false);
        outputData.addOption(OptionBuilder.withDescription("compile only, do not run query").withLongOpt("compileOnly").create("k"));
        outputData.addOption(OptionBuilder.withDescription("discard the generated result").withLongOpt("discardResult").create("d"));
        outputData.addOption(OptionBuilder.hasArg().withArgName("=fileName|-").withDescription("specify where the result should be written to. either a file (=) o the standard output (-)").withLongOpt("outputFile").create("o"));
        cliOptions.addOption(OptionBuilder.hasArgs().withArgName("property=value").withValueSeparator(' ').withDescription("specify a number of serializer parameters").withLongOpt("serializer").create("s"));
        cliOptions.addOptionGroup(inputData);
        cliOptions.addOptionGroup(outputData);
        cliOptions.addOption("ex", "explain", false, "print the query plan in XML format");
        cliOptions.addOption(OptionBuilder.hasArg().withArgName("queryPlan").withDescription("print the execution plan in XML format into the specified file").withLongOpt("serializePlan").create("sp"));
        cliOptions.addOption("l", "printErrorsAsXML", false, "Print the errors as XML suitable for XQDT integration");
        cliOptions.addOption("ps", "printStores", false, "print the state of all variables at the end of execution");
        cliOptions.addOption("t", "timing", false, "print timing information in standard error stream");
        cliOptions.addOption("v", "verbose", false, "print settings, query and additional information");
        cliOptions.addOption("sa", "schemaAwareness", false, "enable XML Schema support and hence the use of the validate keyword");
        cliOptions.addOption("fm", "fulltext", false, "enable XQuery Fulltext support");
        cliOptions.addOption("um", "updateMode", false, "enable the use of updatable variables");
        cliOptions.addOption("sm", "scriptingMode", false, "enable the use of scripting facilities");
        cliOptions.addOption("11m", "xquery11Mode", false, "enable the use of XQuery 1.1 features");
        cliOptions.addOption("cm", "continuousMode", false, "enable the use of Continous/Streaming XQuery");
        cliOptions.addOption("x", "updateFiles", false, "make updates on files persistent");
        cliOptions.addOption("b", "backupUpdates", false, "backup updated files by storing the old version with a .bak extension");
        OptionGroup validation = new OptionGroup();
        validation.addOption(OptionBuilder.withDescription("disable all validation DTD/Schema support/validation when fn:doc is called").withLongOpt("ignoreDTD").create("nodtd"));
        validation.addOption(OptionBuilder.withDescription("disable all validation DTD/Schema support/validation when fn:doc is called (default)").withLongOpt("noValidation").create("noval"));
        validation.addOption(OptionBuilder.withDescription("enable DTD support/validation when fn:doc is called").withLongOpt("dtdAwareness").create("dtd"));
        validation.addOption(OptionBuilder.hasArg().withArgName("validationMode").withDescription("enable input (schema) validation and optionally set the validation mode [strict|lax] when fn:doc is called").withLongOpt("validation").create("val"));
        cliOptions.addOptionGroup(validation);
        cliOptions.addOption("str", "xmlStream", false, "enable input stream validation");
        OptionGroup schemaLocs = new OptionGroup();
        schemaLocs.addOption(OptionBuilder.hasArgs().withArgName("schema=file").withDescription("specify a number of schema files for loading at engine instantiation time").withLongOpt("schemaFiles").create("xsd"));
        schemaLocs.addOption(OptionBuilder.hasArgs().withArgName("catalogs").withDescription("specify a number of catalog files for resolving DTD/schema files").withLongOpt("catalogs").create("cat"));
        cliOptions.addOptionGroup(schemaLocs);
        cliOptions.addOption(OptionBuilder.hasArgs().withArgName("externalFunc=file").withDescription("specify a number of external function catalogs for loading at engine instantiation time").withLongOpt("funcCatalogs").create("func"));
        cliOptions.addOption(OptionBuilder.hasArgs().withArgName("component=value").withValueSeparator(' ').withDescription("specify a number of components of the static context").withLongOpt("context").create("c"));
        cliOptions.addOption(OptionBuilder.hasArg().withArgName("var:=literal|var=file|var-").withValueSeparator(' ').withDescription("specify external variable of context item as literal (:=), file (=) or standard input(-). Use '.' as variable name to specify the context item").withLongOpt("externalVariable").create("e"));
        return cliOptions;
    }

    protected static CommandLine parseCliOptions(Options cliOptions, String[] s) {
        CommandLine cl = null;
        if (s.length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar mxquery.jar ", cliOptions);
        }
        CommandLineParser parser = new GnuParser();
        try {
            cl = parser.parse(cliOptions, s);
        } catch (ParseException exp) {
            if (s.length != 0) System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
        return cl;
    }

    protected static void interogateCliOptions(CommandLine cl, LocalSetting los, CompilerOptions cop, Context ctx) throws IOException, MXQueryException {
        if (cl.hasOption("f")) {
            String fileName = cl.getOptionValue("f");
            File file = new File(fileName);
            if (file.exists()) {
                los.setInput(ch.ethz.mxquery.util.FileReader.getFileContent(file.toURI().toString(), true));
                los.setInputInformation(file.getName());
            } else {
                throw new FileNotFoundException("ERROR: The specified file does not exist: " + fileName + "\n");
            }
        } else if (cl.hasOption("i")) {
            los.setInput(cl.getOptionValue("i"));
        } else {
            los.setInput(cl.getOptionValue("p"));
            los.setIsFromPlan(true);
        }
        if (cl.hasOption("k")) {
            los.setCompileOnly(true);
        } else if (cl.hasOption("d")) {
            los.setIsDiscardResult(true);
        } else if (cl.hasOption("o")) {
            if (cl.getOptionValue("o").trim().equalsIgnoreCase("-")) {
                los.setIsToFile(false);
            } else {
                los.setIsToFile(true);
                los.setOutput(cl.getOptionValue("o"));
            }
        }
        if (cl.hasOption("ex")) {
            los.setIsExplain(true);
        }
        if (cl.hasOption("sp")) {
            los.setIsSerializePlan(true);
            los.setSerializationPlan(cl.getOptionValue("sp"));
        }
        if (cl.hasOption("ps")) {
            los.setIsPrintStores(true);
        }
        if (cl.hasOption("t")) {
            los.setIsTiming(true);
        }
        if (cl.hasOption("printErrorsAsXML")) {
            los.setXmlErrors(true);
        }
        if (cl.hasOption("x")) {
            los.setUpdateFiles(true);
            ctx.getStores().setSerializeStores(true);
        }
        if (cl.hasOption("b")) {
            los.setBackupBeforeUpdate(true);
        }
        parseCompilerOptions(cl, ctx, cop);
        if (cl.hasOption("nodtd")) {
            ctx.setInputValidationMode(Context.IGNORE_DTD);
        }
        if (cl.hasOption("noval")) {
            ctx.setInputValidationMode(Context.NO_VALIDATION);
        }
        if (cl.hasOption("dtd")) {
            ctx.setInputValidationMode(Context.DTD_VALIDATION);
        }
        if (cl.hasOption("val")) {
            String validationMode = cl.getOptionValue("val");
            if (validationMode.equalsIgnoreCase(VALIDATION_STRICT)) {
                ctx.setInputValidationMode(Context.SCHEMA_VALIDATION_STRICT);
            } else if (validationMode.equalsIgnoreCase(VALIDATION_LAX)) {
                ctx.setInputValidationMode(Context.SCHEMA_VALIDATION_LAX);
            } else {
                System.err.println("Unrecognized validation mode value: " + validationMode);
            }
        }
        if (cl.hasOption("str")) {
        }
        if (cl.hasOption("xsd")) {
            String[] schemaFiles = cl.getOptionValue("xsd").split(";");
            for (int j = 0; j < schemaFiles.length; j++) {
                String schemaFile = schemaFiles[j].trim();
                try {
                    SchemaParser.preLoadSchema(Context.getGlobalContext(), schemaFile);
                } catch (MXQueryException e) {
                    e.printStackTrace();
                }
            }
        }
        if (cl.hasOption("func")) {
            String[] funcFiles = cl.getOptionValue("func").split(";");
            for (int j = 0; j < funcFiles.length; j++) {
                String funcFile = funcFiles[j].trim();
                setUpExtension(funcFile);
            }
        }
        if (cl.hasOption("c")) {
            try {
                processStaticContextOptions(cl, ctx);
            } catch (MXQueryException e) {
                e.printStackTrace();
            }
        }
        if (cl.hasOption("v")) {
            los.printDebug();
        }
    }

    protected static void setUpExtension(String funcFile) throws FileNotFoundException {
        try {
            Hashtable builtInExtensions = FunctionGallery.getModulesFile(funcFile);
            Enumeration mods = builtInExtensions.keys();
            while (mods.hasMoreElements()) {
                String ns = (String) mods.nextElement();
                Context modCtx = (Context) builtInExtensions.get(ns);
                Context.getGlobalContext().addModule(modCtx, ns);
            }
        } catch (MXQueryException e) {
            e.printStackTrace();
        }
    }

    private static void parseCompilerOptions(CommandLine cl, Context ctx, CompilerOptions cop) {
        if (cl.hasOption("sa")) {
            cop.setSchemaAwareness(true);
        }
        if (cl.hasOption("fm")) {
            cop.setFulltext(true);
        }
        if (cl.hasOption("um")) {
            cop.setUpdate(true);
        }
        if (cl.hasOption("sm")) {
            cop.setScripting(true);
        }
        if (cl.hasOption("11m")) {
            cop.setXquery11(true);
        }
        if (cl.hasOption("cm")) {
            cop.setContinuousXQ(true);
        }
    }

    protected static XDMSerializerSettings processSerializerSettings(CommandLine cl) throws MXQueryException {
        String[] propString = cl.getOptionValues("s");
        return new XDMSerializerSettings(propString);
    }

    private static void processStaticContextOptions(CommandLine cl, Context ctx) throws MXQueryException {
        String[] compString = cl.getOptionValues("c");
        Hashtable contextMap = getArguments(compString);
        if (contextMap.containsKey(STATICALLY_KNOWN_NAMESPACES)) {
            String argString = (String) contextMap.get(STATICALLY_KNOWN_NAMESPACES);
            String[] argArray = argString.split(";");
            for (int j = 0; j < argArray.length; j++) {
                String argPair = argArray[j];
                if (argPair.matches("\\(.*,.*\\)")) {
                    String argPairContent = argPair.substring(1, argPair.length() - 1);
                    String prefix = argPairContent.substring(0, argPairContent.indexOf(","));
                    String uri = argPairContent.substring(argPairContent.indexOf(",") + 1);
                    ctx.addNamespace(prefix, uri);
                } else {
                    System.err.println("Unrecognized context statically known namespaces value: " + argPair);
                }
            }
            contextMap.remove(STATICALLY_KNOWN_NAMESPACES);
        }
        if (contextMap.containsKey(DEFAULT_ELEMENTTYPE_NAMESPACE)) {
            ctx.setDefaultElementNamespace((String) contextMap.get(DEFAULT_ELEMENTTYPE_NAMESPACE));
        }
        if (contextMap.containsKey(DEFAULT_FUNCTION_NAMESPACE)) {
            ctx.setDefaultFunctionNamespace((String) contextMap.get(DEFAULT_FUNCTION_NAMESPACE));
        }
        if (contextMap.containsKey(CONSTRUCTION_MODE)) {
            String consMode = (String) contextMap.get(CONSTRUCTION_MODE);
            if (consMode.equalsIgnoreCase(Context.PRESERVE)) {
                ctx.setConstructionMode(consMode);
            } else if (consMode.equalsIgnoreCase(Context.STRIP)) {
                ctx.setConstructionMode(consMode);
            } else {
                System.err.println("Unrecognized context construction mode value: " + consMode);
            }
        }
        if (contextMap.containsKey(ORDERING_MODE)) {
            String ordMode = (String) contextMap.get(ORDERING_MODE);
            if (ordMode.equalsIgnoreCase(Context.ORDERED)) {
                ctx.setOrderingMode(ordMode);
            } else if (ordMode.equalsIgnoreCase(Context.UNORDERED)) {
                ctx.setOrderingMode(ordMode);
            } else {
                System.err.println("Unrecognized context cordering mode value: " + ordMode);
            }
        }
        if (contextMap.containsKey(DEFAULT_ORDER_FOR_EMPTY_SEQUENCES)) {
            String defOrder = (String) contextMap.get(DEFAULT_ORDER_FOR_EMPTY_SEQUENCES);
            if (defOrder.equalsIgnoreCase(Context.ORDER_GREATEST)) {
                ctx.setDefaultOrderEmptySequence(defOrder);
            } else if (defOrder.equalsIgnoreCase(Context.ORDER_LEAST)) {
                ctx.setDefaultOrderEmptySequence(defOrder);
            } else {
                System.err.println("Unrecognized context default order for empty sequences value: " + defOrder);
            }
        }
        if (contextMap.containsKey(BOUNDARY_SPACE_POLICY)) {
            String bSpace = (String) contextMap.get(BOUNDARY_SPACE_POLICY);
            if (bSpace.equalsIgnoreCase(Context.PRESERVE)) {
                ctx.setBoundarySpaceHandling(true);
            } else if (bSpace.equalsIgnoreCase(Context.STRIP)) {
                ctx.setBoundarySpaceHandling(false);
            } else {
                System.err.println("Unrecognized context boundary-space value: " + bSpace);
            }
        }
        if (contextMap.containsKey(COPY_NAMESPACE_MODE)) {
            String copyNsMode = (String) contextMap.get(COPY_NAMESPACE_MODE);
            String[] copyNsModes = copyNsMode.split(",");
            if (copyNsModes.length == 2) {
                if ((copyNsModes[0].equalsIgnoreCase(Context.COPY_MODE_PRESERVE) || copyNsModes[1].equalsIgnoreCase(Context.COPY_MODE_PRESERVE)) && (copyNsModes[0].equalsIgnoreCase(Context.COPY_MODE_INHERIT) || copyNsModes[1].equalsIgnoreCase(Context.COPY_MODE_INHERIT))) {
                    ctx.setCopyNamespacesMode(true, true);
                } else if ((copyNsModes[0].equalsIgnoreCase(Context.COPY_MODE_PRESERVE) || copyNsModes[1].equalsIgnoreCase(Context.COPY_MODE_PRESERVE)) && (copyNsModes[0].equalsIgnoreCase(Context.COPY_MODE_NO_INHERIT) || copyNsModes[1].equalsIgnoreCase(Context.COPY_MODE_NO_INHERIT))) {
                    ctx.setCopyNamespacesMode(true, false);
                } else if ((copyNsModes[0].equalsIgnoreCase(Context.COPY_MODE_NO_PRESERVE) || copyNsModes[1].equalsIgnoreCase(Context.COPY_MODE_NO_PRESERVE)) && (copyNsModes[0].equalsIgnoreCase(Context.COPY_MODE_INHERIT) || copyNsModes[1].equalsIgnoreCase(Context.COPY_MODE_INHERIT))) {
                    ctx.setCopyNamespacesMode(false, true);
                } else if ((copyNsModes[0].equalsIgnoreCase(Context.COPY_MODE_NO_PRESERVE) || copyNsModes[1].equalsIgnoreCase(Context.COPY_MODE_NO_PRESERVE)) && (copyNsModes[0].equalsIgnoreCase(Context.COPY_MODE_NO_INHERIT) || copyNsModes[1].equalsIgnoreCase(Context.COPY_MODE_NO_INHERIT))) {
                    ctx.setCopyNamespacesMode(false, false);
                }
            } else {
                System.err.println("Unrecognized context copy namespace mode value: " + copyNsMode);
            }
            contextMap.remove(COPY_NAMESPACE_MODE);
        }
        if (contextMap.containsKey(BASE_URI)) {
            ctx.setBaseURI((String) contextMap.get(BASE_URI));
            contextMap.remove(BASE_URI);
        }
    }

    private static void processExternalValues(CommandLine cl, Context ctx, Map extValues) throws Exception, MXQueryException {
        String errorMsg = "Incorrect format to set external variable: ";
        String[] compString = cl.getOptionValues("e");
        for (int j = 0; j < compString.length; j++) {
            if (compString[j].indexOf(":=") >= 0) {
                String[] argValPair = compString[j].split(":=");
                if (argValPair.length != 2) throw new Exception(errorMsg + compString[j]);
                QName varName = getVar(ctx, argValPair);
                VariableHolder vh = ctx.getVariable(varName);
                XDMIterator xmlIt;
                if (vh != null && vh.getType() != null && Type.isAtomicType(vh.getType().getType(), Context.getDictionary())) {
                    xmlIt = XDMAtomicItemFactory.createUntypedAtomic(argValPair[1]);
                } else xmlIt = XDMInputFactory.createXMLInput(ctx, new java.io.StringReader(argValPair[1]), true, ctx.getInputValidationMode(), null);
                extValues.put(varName, xmlIt);
            } else if (compString[j].indexOf('=') >= 0) {
                String[] argValPair = compString[j].split("=");
                if (argValPair.length != 2) throw new Exception(errorMsg + compString[j]);
                QName varName = getVar(ctx, argValPair);
                String uri = URIUtils.resolveURI(ctx.getBaseURI(), argValPair[1], QueryLocation.OUTSIDE_QUERY_LOC);
                Reader rd = IOLib.getInput(uri, false, null, QueryLocation.OUTSIDE_QUERY_LOC);
                VariableHolder vh = ctx.getVariable(varName);
                XDMIterator xmlIt;
                if (vh != null && vh.getType() != null && Type.isAtomicType(vh.getType().getType(), Context.getDictionary())) {
                    xmlIt = XDMAtomicItemFactory.createUntypedAtomic(FileReader.getContents(rd));
                } else xmlIt = XDMInputFactory.createXMLInput(ctx, rd, true, ctx.getInputValidationMode(), null);
                extValues.put(varName, xmlIt);
            } else if (compString[j].trim().endsWith("-")) {
                String[] argValPair = compString[j].split("-");
                if (argValPair.length != 1) throw new Exception(errorMsg + compString[j]);
                QName varName = getVar(ctx, argValPair);
                VariableHolder vh = ctx.getVariable(varName);
                XDMIterator xmlIt;
                if (vh != null && vh.getType() != null && Type.isAtomicType(vh.getType().getType(), Context.getDictionary())) {
                    xmlIt = XDMAtomicItemFactory.createUntypedAtomic(FileReader.getContents(new InputStreamReader(System.in)));
                } else xmlIt = XDMInputFactory.createXMLInput(ctx, new InputStreamReader(System.in), true, ctx.getInputValidationMode(), null);
                extValues.put(varName, xmlIt);
            } else {
                throw new Exception(errorMsg + compString[j]);
            }
        }
    }

    private static QName getVar(Context ctx, String[] argValPair) throws MXQueryException, StaticException {
        QName varName;
        if (argValPair[0].equalsIgnoreCase(".")) varName = Context.CONTEXT_ITEM; else {
            varName = new QName(argValPair[0].trim());
            varName = (QName) varName.resolveQNameNamespace(ctx, true);
        }
        return varName;
    }

    private static Hashtable getArguments(String[] arguments) {
        Hashtable args = new Hashtable();
        for (int j = 0; j < arguments.length; j++) {
            String[] argValPair = arguments[j].split("=", 2);
            if (argValPair.length == 2) args.put(argValPair[0], argValPair[1]); else args.put(argValPair[0], "");
        }
        return args;
    }

    protected static void executeQuery(String query, Context ctx, TimeInfos timeInfos, LocalSetting los, XDMSerializerSettings xss, CompilerOptions cop, CommandLine cl) throws Exception {
        XQCompiler compiler;
        PreparedStatement statement = null;
        Logger lg = Logger.getLogger("ch.ethz.mxquery.model.Iterator");
        lg.log(Level.WARNING, "Logger started");
        lg.setLevel(Level.FINEST);
        if (!los.isFromPlan()) {
            compiler = new CompilerImpl();
            try {
                if (timeInfos != null) timeInfos.setCompileStart(System.currentTimeMillis());
                statement = compiler.compile(ctx, query, cop, null, null);
            } catch (StaticException err) {
                if (!los.isXmlErrors()) {
                    StaticException.printErrorPosition(query, err.getLocation());
                    System.err.println("Error:");
                    throw err;
                } else {
                    System.err.println(err.getErrorAsXML(query, los.getInputInformation()));
                    System.exit(-1);
                }
            } finally {
                if (timeInfos != null) timeInfos.setCompileEnd(System.currentTimeMillis());
            }
        } else {
            PlanLoader PL = new PlanLoader();
            File file = new File(query);
            if (file.exists()) {
                FileInputStream fs = null;
                XDMIterator result;
                try {
                    fs = new FileInputStream(file);
                    result = PL.processPlan(fs);
                } finally {
                    if (fs != null) fs.close();
                }
                statement = new PreparedStatementImpl(result.getContext(), result, new CompilerOptions(), null);
            } else {
                System.err.println("Sorry! File no found!!");
                System.exit(-1);
            }
        }
        if (timeInfos != null) timeInfos.setExecStart(System.currentTimeMillis());
        doQuery(query, statement, ctx, los, xss, cl);
        if (timeInfos != null) timeInfos.setExecEnd(System.currentTimeMillis());
    }

    protected static void doQuery(String query, PreparedStatement statement, Context ctx, LocalSetting los, XDMSerializerSettings xss, CommandLine cl) throws Exception {
        System.setProperty("http.agent", "MXQuery");
        XDMIterator result = statement.evaluate();
        if (los.isExplain()) {
            System.err.println("############################ query plan ############################");
            KXmlSerializer serializer = new KXmlSerializer();
            serializer.setOutput(System.err, null);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            result.traverseIteratorTree(serializer);
            serializer.flush();
            System.err.println();
            System.err.println("############################ query plan ############################");
            System.err.println();
        }
        if (los.isSerializePlan()) {
            try {
                PrintStream out = new PrintStream(new FileOutputStream(los.getSerializationPlan()));
                out.println("<StaticContext>");
                Enumeration keys = result.getContext().getAllVariables().keys();
                if (keys.hasMoreElements()) {
                    out.println(" <Variables>");
                    while (keys.hasMoreElements()) {
                        out.println(" <Variable>");
                        out.println(" <Key>" + keys.nextElement() + "</Key>");
                        out.println(" </Variable>");
                    }
                    out.println(" </Variables>");
                } else {
                    out.println(" <Variables/>");
                }
                out.println(" <UsedVariables/>");
                out.println("</StaticContext>");
                out.println("<ParseTree>");
                KXmlSerializer serializer = new KXmlSerializer();
                serializer.setOutput(out, null);
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                result.traverseIteratorTree(serializer);
                serializer.flush();
                out.println();
                out.println("</ParseTree>");
            } catch (MXQueryException e) {
                if (!los.isXmlErrors()) {
                    System.err.println("Error occured during evaluation:\nError code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\n");
                    MXQueryException.printErrorPosition(query, e.getLocation());
                    System.err.println("Error:");
                    e.printStackTrace();
                } else {
                    System.err.println(e.getErrorAsXML(query, los.getInputInformation()));
                }
            } catch (IOException e) {
            }
        }
        if (los.isCompileOnly()) return;
        Map extValues = new HashMap();
        if (cl.hasOption("e")) {
            processExternalValues(cl, ctx, extValues);
        }
        java.util.Set vals = extValues.entrySet();
        java.util.Iterator valIt = vals.iterator();
        while (valIt.hasNext()) {
            Map.Entry mp = (Map.Entry) valIt.next();
            QName qn = (QName) mp.getKey();
            XDMIterator it = (XDMIterator) mp.getValue();
            if (qn.equals(Context.CONTEXT_ITEM)) statement.setContextItem(it); else statement.addExternalResource(qn, it);
        }
        if (!los.isDiscardResult()) {
            try {
                OutputStream out = System.out;
                if (los.isToFile()) {
                    out = new BufferedOutputStream(new FileOutputStream(los.getOutput()));
                }
                XDMSerializer ip = new XDMSerializer(xss);
                ip.eventsToXML(out, result);
                if (los.isToFile()) {
                    out.flush();
                    out.close();
                }
                statement.applyPUL();
                if (los.isUpdateFiles()) statement.serializeStores(los.isBackupBeforeUpdate());
            } catch (MXQueryException e) {
                if (!los.isXmlErrors()) {
                    System.err.println("Error occured during evaluation:\nError code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\n");
                    MXQueryException.printErrorPosition(query, e.getLocation());
                    System.err.println("Error:");
                    e.printStackTrace();
                } else {
                    System.err.println(e.getErrorAsXML(query, los.getInputInformation()));
                }
            } catch (IOException e) {
            }
        }
        result.close(false);
        if (los.isPrintStores()) {
            System.err.println("############################ store ############################");
            System.err.println(((LLStoreSet) statement.getStores()).toString(true));
            System.err.println("############################ store ############################");
            System.err.println();
        }
    }
}
