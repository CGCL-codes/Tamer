package lang4j.generator;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import lang4j.parser.generated.*;
import org.antlr.Tool;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import java.io.*;
import java.util.*;

/**
 * User: felix
 * Date: 31.10.2005
 * Time: 09:33:06
 */
public class Generator {

    Lang4jGrammar grammar;

    Transformer transformer;

    private Template classTemplate;

    private Template enumTemplate;

    private Template interfaceTemplate;

    private Template visitorTemplate;

    private Template testTemplate;

    private Template rendererTemplate;

    private File targetFolder;

    public File actualTargetFolder;

    private Template grammarTemplate;

    private Template transformerTemplate;

    public Generator(final Lang4jGrammar grammar, final Transformer transformer, File targetFolder) {
        this.targetFolder = targetFolder;
        this.grammar = grammar;
        this.transformer = transformer;
        initTemplates();
        actualTargetFolder = new File(targetFolder, grammar.getPackageName().replace('.', '/'));
        try {
            FileUtils.forceMkdir(actualTargetFolder);
        } catch (IOException e) {
        }
        renderJavaClasses();
        renderGrammar();
        renderTestCase();
        renderRenderer();
    }

    private void renderRenderer() {
        VelocityContext context = new VelocityContext();
        populateContextWithGrammarInfo(context);
        context.put("renderMethods", new RenderMethodsGenerator(grammar, transformer).getText());
        context.put("productions", grammar.getProductions());
        try {
            Writer w = getWriter(StringUtils.capitalize(grammar.getName()) + "Renderer.java");
            rendererTemplate.merge(context, w);
            w.close();
        } catch (Exception e) {
            throw new RuntimeException("could not write renderer", e);
        }
    }

    private void renderTestCase() {
        List prodInfo = new LinkedList();
        for (Iterator iterator = grammar.getProductions().iterator(); iterator.hasNext(); ) {
            Production prod = (Production) iterator.next();
            prodInfo.add(new ProductionTestInfo(prod));
        }
        VelocityContext context = new VelocityContext();
        populateContextWithGrammarInfo(context);
        context.put("productions", prodInfo);
        try {
            Writer w = getWriter(StringUtils.capitalize(grammar.getName()) + "ParserTest.java");
            testTemplate.merge(context, w);
            w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseErrorException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void populateContextWithGrammarInfo(final VelocityContext context) {
        context.put("grammarName", grammar.getName());
        context.put("packageName", grammar.getPackageName());
    }

    private void renderGrammar() {
        VelocityContext context = new VelocityContext();
        populateContextWithGrammarInfo(context);
        context.put("grammar", grammar);
        context.put("helper", new Helper());
        context.put("productionBodies", new GrammarGenerator(grammar, transformer).getProductions());
        context.put("terminalMap", transformer.getTokens());
        context.put("keywordMap", transformer.getKeywords());
        try {
            Writer writer = getWriter(grammar.getName() + ".g");
            grammarTemplate.merge(context, writer);
            writer.close();
            writer = getWriter(grammar.getName() + ".dot");
            Lang4jToDot dotExport = new Lang4jToDot(writer, transformer);
            dotExport.renderGrammar(grammar);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void renderJavaClasses() {
        for (Iterator iterator = grammar.getProductions().iterator(); iterator.hasNext(); ) {
            Production production = (Production) iterator.next();
            production.accept(new ProductionVisitor() {

                public void visitEnumProduction(EnumProduction enumProduction) {
                    renderEnum(enumProduction);
                }

                public void visitTypeProduction(TypeProduction typeProduction) {
                    typeProduction.accept(new TypeProductionVisitor() {

                        public void visitInterfaceProduction(InterfaceProduction interfaceProduction) {
                            renderInterface(interfaceProduction);
                        }

                        public void visitConstructorProduction(ConstructorProduction constructorProduction) {
                            renderConcreteClass(constructorProduction);
                        }
                    });
                }

                public void visitListProduction(ListProduction listProduction) {
                }
            });
        }
    }

    private void renderInterface(final InterfaceProduction interfaceProduction) {
        try {
            final String className = StringUtils.capitalize(interfaceProduction.getName());
            Writer output = getWriter(className + ".java");
            VelocityContext context = new VelocityContext();
            context.put("className", StringUtils.capitalize(interfaceProduction.getName()));
            context.put("attributes", transformer.getAttributes(interfaceProduction));
            context.put("implements", supertypeDeclaration("extends", interfaceProduction, Collections.EMPTY_LIST));
            context.put("helper", new Helper());
            context.put("packageName", grammar.getPackageName());
            context.put("subTypes", interfaceProduction.getSubTypes());
            interfaceTemplate.merge(context, output);
            output.close();
            output = getWriter(className + "Visitor.java");
            visitorTemplate.merge(context, output);
            output.close();
            output = getWriter(className + "Transformer.java");
            transformerTemplate.merge(context, output);
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseErrorException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String supertypeDeclaration(final String keyword, final TypeProduction typeProduction, List additionalTypes) {
        Collection supertypes = new ArrayList(transformer.getDirectSuperTypes(typeProduction));
        supertypes.addAll(additionalTypes);
        if (supertypes.size() == 0) return "";
        StringBuffer ret = new StringBuffer(keyword);
        ret.append(' ');
        for (Iterator iterator = supertypes.iterator(); iterator.hasNext(); ) {
            Object element = iterator.next();
            if (element instanceof Production) {
                Production production = (Production) element;
                ret.append(StringUtils.capitalize(production.getName()));
            }
            if (element instanceof String) {
                ret.append((String) element);
            }
            if (iterator.hasNext()) ret.append(',');
        }
        return ret.toString();
    }

    private void renderConcreteClass(final ConstructorProduction constructorProduction) {
        try {
            final String className = StringUtils.capitalize(constructorProduction.getName());
            Writer output = getWriter(className + ".java");
            VelocityContext context = new VelocityContext();
            context.put("className", StringUtils.capitalize(constructorProduction.getName()));
            context.put("attributes", transformer.getAttributes(constructorProduction));
            context.put("implements", supertypeDeclaration("implements", constructorProduction, Arrays.asList(new Object[] { "ParsedEntity" })));
            context.put("helper", new Helper());
            context.put("packageName", grammar.getPackageName());
            context.put("acceptors", transformer.getAcceptInfos(constructorProduction));
            classTemplate.merge(context, output);
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseErrorException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void renderEnum(final EnumProduction enumProduction) {
        try {
            final String className = StringUtils.capitalize(enumProduction.getName());
            Writer output = null;
            output = getWriter(className + ".java");
            VelocityContext context = new VelocityContext();
            context.put("className", StringUtils.capitalize(enumProduction.getName()));
            context.put("valueSet", enumProduction.getValues());
            context.put("helper", new Helper());
            context.put("packageName", grammar.getPackageName());
            enumTemplate.merge(context, output);
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseErrorException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Writer getWriter(String name) throws IOException {
        return new FileWriter(getFile(name));
    }

    private File getFile(final String name) {
        return new File(actualTargetFolder, name);
    }

    public void initTemplates() {
        try {
            VelocityEngine ve = new VelocityEngine();
            ve.setProperty("resource.loader", "class");
            ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            ve.init();
            classTemplate = ve.getTemplate("templates/javaclass.vm");
            enumTemplate = ve.getTemplate("templates/enum.vm");
            interfaceTemplate = ve.getTemplate("templates/interface.vm");
            visitorTemplate = ve.getTemplate("templates/visitor.vm");
            transformerTemplate = ve.getTemplate("templates/transformer.vm");
            testTemplate = ve.getTemplate("templates/parsertest.vm");
            grammarTemplate = ve.getTemplate("templates/grammar.vm");
            rendererTemplate = ve.getTemplate("templates/renderer.vm");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, TokenStreamException, RecognitionException {
        Options options = new Options();
        try {
            Option outdir = OptionBuilder.hasArg().withDescription("generation output base-folder").create('o');
            options.addOption(outdir);
            CommandLineParser clparser = new PosixParser();
            CommandLine commandLine = clparser.parse(options, args);
            File targetFolder;
            if (commandLine.hasOption('o')) {
                targetFolder = new File(commandLine.getOptionValue('o'));
                System.out.println("using target folder " + targetFolder);
            } else {
                targetFolder = new File("gen-src");
            }
            if (commandLine.getArgs().length != 1) {
                System.out.println("No input file specified" + commandLine.getArgs().length);
                throw new ParseException("Please give me an input file");
            }
            parseAndGenerate(commandLine, targetFolder);
            System.out.println("Finished sucessfully!");
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("generator [options] file", options);
        } catch (org.antlr.runtime.RecognitionException e) {
            throw new RuntimeException("", e);
        }
    }

    private static void parseAndGenerate(final CommandLine commandLine, final File targetFolder) throws RecognitionException, TokenStreamException, IOException, org.antlr.runtime.RecognitionException {
        InputStream is = new FileInputStream(commandLine.getArgs()[0]);
        Lang4jParser parser = new Lang4jParser(new CommonTokenStream(new Lang4jLexer(new ANTLRInputStream(is))));
        Lang4jGrammar grammar = parser.lang4jGrammar();
        if (!parser.identifierMap.isResolved()) {
            Collection un = parser.identifierMap.getUnresolved();
            for (Object o : un) {
                System.out.println("Unresolved reference: " + o);
            }
            throw new RuntimeException("Unresolved References");
        }
        is.close();
        Transformer transformer = new Transformer(grammar);
        Generator gen = new Generator(grammar, transformer, targetFolder);
        final String absolutePath = targetFolder.getAbsolutePath();
        System.out.println("Output: " + absolutePath);
        Tool.main(new String[] { gen.getFile(grammar.getName() + ".g").getPath() });
    }

    public static class Helper {

        public String cap(String s) {
            return StringUtils.capitalize(s);
        }
    }
}
