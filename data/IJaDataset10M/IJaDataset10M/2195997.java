package monkey.main;

import monkey.generator.GeneratorFactory;
import monkey.generator.Generator;
import monkey.generator.java.JavaGenerator;
import monkey.parser.Grammar;
import monkey.parser.ParserData;
import monkey.scanner.ScannerData;
import monkey.scanner.alphabet.Alphabet;
import monkey.scanner.automaton.AutomatonConvertor;
import monkey.scanner.automaton.DynamicAutomaton;
import monkey.scanner.automaton.StaticAutomaton;
import monkey.scanner.automaton.ThompsonVisitor;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Monkey {

    public static void main(String[] args) throws Exception {
        GeneratorFactory generatorFactory = GeneratorFactory.getInstance();
        Generator generator = generatorFactory.createGenerator("java");
        File grammarFilePath = new File(args[0]);
        File path = grammarFilePath.getParentFile();
        Parser parser = new Parser();
        GrammarFile grammarFile = (GrammarFile) parser.parse(new Scanner(new FileReader(grammarFilePath)));
        DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
        System.out.println(dateformat.format(new Date()) + " --- Grammer file loaded!\n");
        Alphabet alphabet = grammarFile.getAlphabet();
        ScannerState[] scannerStates = grammarFile.getScannerData().getScannerStates();
        StaticAutomaton[] staticAutomatons = new StaticAutomaton[scannerStates.length];
        for (int i = 0; i < scannerStates.length; i++) {
            DynamicAutomaton dynamicAutomaton = ThompsonVisitor.createAutomaton(scannerStates[i].getTokenDefinitions(), alphabet);
            dynamicAutomaton.setStateName(scannerStates[i].getName());
            staticAutomatons[i] = new StaticAutomaton(AutomatonConvertor.convert(dynamicAutomaton));
            staticAutomatons[i].dump(System.out);
            System.out.println();
        }
        monkey.scanner.ScannerData scannerData = new ScannerData(staticAutomatons);
        scannerData.addUserCode(JavaGenerator.PACKAGE, grammarFile.getPackageName());
        scannerData.addUserCode(JavaGenerator.IMPORTS, grammarFile.getScannerData().getImports());
        scannerData.addUserCode(JavaGenerator.DECLARATIONS, grammarFile.getScannerData().getDeclarations());
        generator.generateScanner(scannerData, path);
        System.out.println(dateformat.format(new Date()) + " --- Scanner generated!\n");
        Grammar grammar = grammarFile.getParserData().getGrammar();
        grammar.assureIntegrity();
        grammar.dump(System.out);
        ParserData parserData = new ParserData(grammar);
        parserData.addUserCode(JavaGenerator.PACKAGE, grammarFile.getPackageName());
        parserData.addUserCode(JavaGenerator.IMPORTS, grammarFile.getParserData().getImports());
        parserData.addUserCode(JavaGenerator.DECLARATIONS, grammarFile.getParserData().getDeclarations());
        generator.generateParser(parserData, path);
        System.out.println();
        System.out.println(dateformat.format(new Date()) + " --- Parser generated!");
    }
}
