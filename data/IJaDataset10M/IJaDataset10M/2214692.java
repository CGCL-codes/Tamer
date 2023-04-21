package clump.language.boot;

import clump.boot.Version;
import clump.boot.options.Option;
import clump.boot.options.Options;
import clump.kernel.engine.ModelLoader;
import clump.language.analysis.impl.PackageDefinition;
import clump.language.ast.exception.AbstractSyntaxTreeError;
import clump.language.ast.specification.expression.impl.EntityInstance;
import clump.language.compilation.backend.impl.PackageGenerator;
import clump.language.compilation.coverage.impl.PackageCoverage;
import clump.language.compilation.generator.Generators;
import clump.language.compilation.generator.IDiagnosticListener;
import clump.language.compilation.generator.IGenerator;
import clump.language.compilation.generator.common.PackageCompiler;
import clump.language.compilation.generator.exception.GeneratorNotFoundException;
import clump.language.compilation.generator.java.JavaGenerator;
import clump.language.compilation.normalize.impl.PackageNormalizer;
import clump.language.compilation.solver.impl.PackageSolver;
import clump.language.compilation.typechecker.impl.ClassFinder;
import clump.language.compilation.typechecker.impl.PackageTypeChecker;
import clump.language.parsing.ClumpSupport;
import clump.language.parsing.entity.EntitiesUnit;
import clump.message.MessageProvider;
import opala.lexing.IGenericLexer;
import opala.lexing.ILexeme;
import opala.lexing.ILexemeTokenizer;
import opala.lexing.exception.LexemeNotFoundException;
import opala.lexing.exception.UnexpectedLexemeException;
import opala.lexing.impl.GenLex;
import opala.lexing.impl.LexerTokenizer;
import opala.parsing.ILanguageSupport;
import opala.parsing.exception.ParsingUnitNotFound;
import opala.parsing.impl.AbstractSupport;
import opala.scanner.ILexemeFilter;
import opala.scanner.IScanner;
import opala.scanner.exception.ScannerException;
import opala.scanner.impl.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compile implements IDiagnosticListener {

    private static PrintStream outStream = System.err;

    private static int nbErrors = 0;

    private static boolean debug;

    private static boolean verbose;

    private static void printStackTraceOnDemand(Throwable e) {
        if (debug) {
            e.printStackTrace();
        }
        if (verbose) {
            System.exit(1);
        }
    }

    private static void printVerboseInformation(String information) {
        if (verbose) {
            System.err.println(information);
        }
    }

    public static void parse(ILanguageSupport support, ILexemeFilter filter, File node, EntitiesUnit entitiesUnit) throws FileNotFoundException, ScannerException, MalformedURLException {
        if (node.isDirectory()) {
            File[] files = node.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (nbErrors < 100) {
                        parse(support, filter, file, entitiesUnit);
                    }
                }
            }
        } else if (node.isFile() && node.getName().endsWith(".vor")) {
            final URL fileURL = node.getAbsoluteFile().toURI().toURL();
            final IGenericLexer genericLexer = GenLex.getGenericLexer(fileURL, new FileInputStream(node));
            final ILexemeTokenizer tokenizer = new LexerTokenizer(genericLexer);
            try {
                printVerboseInformation("{Parse} " + fileURL);
                IScanner scanner = new Scanner(filter, tokenizer);
                entitiesUnit.compile(support, scanner, null);
            } catch (ScannerException e) {
                nbErrors++;
                outStream.println("# Parse error " + e.getCause().getMessage());
                if (e.getLocation() != null) {
                    outStream.println("# in " + e.getLocation());
                }
                outStream.println();
                printStackTraceOnDemand(e);
            } catch (ParsingUnitNotFound e) {
                printStackTraceOnDemand(e);
                nbErrors++;
                outStream.println("# Parse error " + e.getMessage());
                if (e.getLocation() != null) {
                    outStream.println("# in " + e.getLocation());
                }
                outStream.println();
            } catch (UnexpectedLexemeException e) {
                nbErrors++;
                outStream.print("[");
                String[] strings = e.getContext();
                for (String string : strings) {
                    outStream.print(string + "::");
                }
                outStream.println("]");
                outStream.println("# Parse error " + e.getMessage());
                outStream.println("# in " + e.getLocation());
                outStream.println();
                printStackTraceOnDemand(e);
            } catch (LexemeNotFoundException e) {
                nbErrors++;
                outStream.println("# Parse error " + e.getMessage());
                outStream.println("# in " + e.getLocation());
                outStream.println();
                printStackTraceOnDemand(e);
            } catch (AbstractSyntaxTreeError e) {
                outStream.println("# Parse error " + e.getMessage());
                outStream.println("# in " + e.getLocation());
                outStream.println();
                printStackTraceOnDemand(e);
                nbErrors++;
            } finally {
                tokenizer.finish();
            }
        } else if (node.getName().endsWith(".vor")) {
            outStream.println("# Parse Error Cannot access file " + node.getPath());
            outStream.println();
            nbErrors++;
        }
    }

    public static void main(String[] args) {
        try {
            final AbstractSupport support = new ClumpSupport();
            final ILexemeFilter filter = new ILexemeFilter.LexemeSetOfFilters(new ILexemeFilter[] { ILexemeFilter.SPACE_AND_COMMENT });
            final EntitiesUnit entities = new EntitiesUnit();
            final Options options = new Options(MessageProvider.getMessage("compiler.usage"));
            final Option versionOption = options.registerSwitchOption("--version", MessageProvider.getMessage("compiler.usage.version"));
            final Option verboseOption = options.registerSwitchOption("-v", MessageProvider.getMessage("compiler.usage.verbose"));
            final Option debugOption = options.registerSwitchOption("-d", MessageProvider.getMessage("compiler.usage.debug"));
            final Option libraryOption = options.registerURLOption("-l", MessageProvider.getMessage("compiler.usage.library"));
            final Option objectsOption = options.registerDirectoryOption("-o", MessageProvider.getMessage("compiler.usage.output"));
            final Option targetsOption = options.registerStringOption("-t", MessageProvider.getMessage("compiler.usage.targets"));
            final Option selectedStage = options.registerStringOption("-s", new String[] { "syntax", "symbol", "type", "cover" }, MessageProvider.getMessage("compiler.usage.stages"));
            final Option keepSources = options.registerSwitchOption("-k", MessageProvider.getMessage("compiler.usage.sources"));
            final Option showMain = options.registerSwitchOption("-m", MessageProvider.getMessage("compiler.usage.main"));
            options.parse(args);
            if (versionOption.hasBeenSpecified()) {
                System.err.println(Version.getVersionAsString(MessageProvider.getMessage("compiler.name")));
                System.exit(1);
            } else if (options.getArguments().length == 0 || options.helpNeeded()) {
                options.displayUsage();
                System.exit(1);
            }
            final boolean checkSymbol;
            final boolean checkType;
            final boolean checkCover;
            final boolean doCompilation;
            if (selectedStage.hasBeenSpecified()) {
                final String value = selectedStage.getValue();
                if (value.equals("syntax")) {
                    checkSymbol = false;
                    checkType = false;
                    checkCover = false;
                    doCompilation = false;
                } else if (value.equals("symbol")) {
                    checkSymbol = true;
                    checkType = false;
                    checkCover = false;
                    doCompilation = false;
                } else if (value.equals("type")) {
                    checkSymbol = true;
                    checkType = true;
                    checkCover = false;
                    doCompilation = false;
                } else if (value.equals("cover")) {
                    checkSymbol = true;
                    checkType = true;
                    checkCover = true;
                    doCompilation = false;
                } else {
                    checkSymbol = true;
                    checkType = true;
                    checkCover = true;
                    doCompilation = true;
                }
            } else {
                checkSymbol = true;
                checkType = true;
                checkCover = true;
                doCompilation = true;
            }
            verbose = verboseOption.hasBeenSpecified();
            debug = debugOption.hasBeenSpecified();
            if (libraryOption.hasBeenSpecified()) {
                final List<String> specified = libraryOption.getValues();
                final List<URL> urls = new ArrayList<URL>();
                for (String entry : specified) {
                    urls.add(new URL(entry));
                }
                ModelLoader.setLoader(new URLClassLoader(urls.toArray(new URL[urls.size()])));
            }
            long t0 = System.currentTimeMillis();
            for (String arg : options.getArguments()) {
                parse(support, filter, new File(arg), entities);
            }
            if (checkSymbol == false) return;
            nbErrors += new PackageSolver(outStream, verbose, debug).solve(PackageDefinition.getRoot());
            if (checkType == false) return;
            nbErrors += new PackageTypeChecker(outStream, verbose, debug).typeChecker(PackageDefinition.getRoot());
            nbErrors += new PackageNormalizer(outStream, verbose, debug).normalize(PackageDefinition.getRoot());
            if (checkCover == false) return;
            nbErrors += new PackageCoverage(outStream, verbose, debug).coverage(PackageDefinition.getRoot());
            if (doCompilation == false) return;
            final PackageGenerator belangGenerator = new PackageGenerator(outStream, verbose, debug);
            nbErrors += belangGenerator.generate(PackageDefinition.getRoot());
            if (nbErrors == 0) {
                final File classDirectory;
                if (objectsOption.hasBeenSpecified() == false) {
                    System.err.println(MessageProvider.getMessage("compilation.no.output"));
                } else {
                    classDirectory = new File(objectsOption.getValue());
                    final String[] targets;
                    if (targetsOption.hasBeenSpecified()) {
                        targets = targetsOption.getValue().split(",");
                    } else {
                        System.err.println(MessageProvider.getMessage("compilation.no.target"));
                        targets = new String[] { JavaGenerator.getExternalIdentifier() };
                    }
                    for (String name : targets) {
                        try {
                            final String target = name.trim();
                            final IGenerator generator = Generators.getInstance().getGeneratorByIdentifier(target);
                            if (verbose) {
                                outStream.println(MessageProvider.getMessage("compilation.target.information", new String[] { target }));
                            }
                            generator.generate(keepSources.hasBeenSpecified(), new Compile(), classDirectory, belangGenerator.getEntities());
                        } catch (GeneratorNotFoundException e) {
                            outStream.println(MessageProvider.getMessage("target.not.found", new String[] { e.getTarget() }));
                        }
                    }
                    new PackageCompiler(classDirectory, outStream, verbose, debug).compile(PackageDefinition.getRoot());
                    if (showMain.hasBeenSpecified()) {
                        final ArrayList<String> path = new ArrayList<String>();
                        path.add("clump");
                        path.add("lang");
                        final EntityInstance main = new EntityInstance(path, "Main");
                        ClassFinder.searchForImplementation(outStream, PackageDefinition.getRoot(), main);
                    }
                }
            }
            long compilation = System.currentTimeMillis() - t0;
            if (verbose) {
                final String measure;
                if (compilation < 1000) {
                    measure = compilation + "ms";
                } else {
                    measure = (compilation / 1000) + "s";
                }
                System.err.println(MessageProvider.getMessage("compilation.duration", new Object[] { measure, System.currentTimeMillis() }));
            }
        } catch (Throwable e) {
            outStream.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (nbErrors > 0) {
                outStream.println(MessageProvider.getMessage("compilation.errors", new Object[] { nbErrors }));
            }
        }
    }

    @Override
    public void notifyBackEndCompilation(ILexeme lexeme) {
        if (lexeme.getValue() != null) {
            System.err.print(lexeme.getValue());
        }
    }

    @Override
    public void notifyCompilation(String label, clump.common.EntityKind kind, String entity) {
        printVerboseInformation("{" + label + "} " + kind + " " + entity);
    }

    @Override
    public void notifyError(String reason) {
        System.err.println(reason);
        nbErrors += 1;
    }

    @Override
    public void notifyLocatedError(String file, int line, String reason) {
        System.err.println(file + ":" + line + ":" + reason);
        nbErrors += 1;
    }
}
