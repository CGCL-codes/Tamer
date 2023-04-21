package monkey.loader.old;

import java.io.ObjectInputStream;
import java.util.Stack;
import monkey.generator.GeneratorFactory;
import monkey.generator.GeneratorException;
import monkey.generator.Generator;
import monkey.generator.UnknownLanguageException;
import monkey.parser.Grammar;
import monkey.parser.ParserData;
import monkey.parser.ParserDataException;
import monkey.parser.Production;
import monkey.scanner.ScannerData;
import monkey.scanner.TokenDefinition;
import monkey.scanner.alphabet.Alphabet;
import monkey.scanner.alphabet.ExcludedSymbolSet;
import monkey.scanner.alphabet.SymbolSet;
import monkey.scanner.alphabet.SymbolSetList;
import monkey.scanner.automaton.*;
import monkey.scanner.regexpr.*;
import monkey.util.Action;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

public class Parser {

    private Stack stack;

    private static final int ERROR = 0;

    private static final int SHIFT = 1;

    private static final int REDUCE = 2;

    private static final int ACCEPT = 3;

    private static final int[][][] actions;

    private static final int[][] gotos;

    private static final String[] terminalNames;

    private static final int[][] productions;

    static {
        try {
            ObjectInputStream in = new ObjectInputStream(Scanner.class.getResourceAsStream("parser.data"));
            actions = (int[][][]) in.readObject();
            gotos = (int[][]) in.readObject();
            terminalNames = (String[]) in.readObject();
            productions = (int[][]) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException("The file \"parser.data\" is either missing or corrupted.");
        }
    }

    private final ProductionAction[] productionActions = { new ProductionAction() {

        public Object execute(Object[] objects) {
            ScannerData scannerData = (ScannerData) objects[1];
            ParserData parserData = (ParserData) objects[2];
            try {
                System.out.println();
                System.out.println("Generating scanner ...");
                generator.generateScanner(scannerData, destination);
                System.out.println();
                System.out.println("Generating parser ...");
                generator.generateParser(parserData, destination);
            } catch (GeneratorException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            ScannerData scannerData = (ScannerData) objects[1];
            try {
                System.out.println();
                System.out.println("Generating scanner ...");
                generator.generateScanner(scannerData, destination);
            } catch (GeneratorException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String language = ((Token) objects[1]).getImage();
            try {
                generator = GeneratorFactory.getInstance().createGenerator(language);
            } catch (UnknownLanguageException e) {
                System.out.println(e.getMessage());
            }
            return generator;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            ScannerData scannerData = (ScannerData) objects[2];
            System.out.println();
            System.out.println("... Parsed scanner section:");
            scannerData.dump(System.out);
            return scannerData;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getScannerData((List) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getScannerData((List) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getScannerData(new ArrayList());
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getScannerData(new ArrayList());
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getScannerData((List) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getScannerData((List) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getScannerData(new ArrayList());
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getScannerData(new ArrayList());
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List list = new ArrayList();
            list.add(objects[0]);
            return list;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List list = (List) objects[0];
            list.add(objects[1]);
            return list;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String code = ((Token) objects[2]).getImage();
            code = code.substring(2, code.length() - 2);
            return new String[] { ((Token) objects[1]).getImage(), code };
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String code = ((Token) objects[0]).getImage();
            code = code.substring(1, code.length() - 1);
            return code;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            createHelper((Token) objects[0], (RegularExpression) objects[2]);
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return objects[0];
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return new Alternation((RegularExpression) objects[0], (RegularExpression) objects[2]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return objects[0];
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return new Concatenation((RegularExpression) objects[0], (RegularExpression) objects[1]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return objects[0];
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return new PositiveClosure((RegularExpression) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return new KleeneClosure((RegularExpression) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return new OptionalClosure((RegularExpression) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return objects[0];
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return getHelper((Token) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return objects[1];
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String image = ((Token) objects[0]).getImage();
            image = image.substring(1, image.length() - 1);
            char[] chars = image.toCharArray();
            RegularExpression expression = null;
            int i = 0;
            if (chars[i] == '\\') {
                switch(chars[++i]) {
                    case 'n':
                        expression = alphabet.createSymbol('\n');
                        break;
                    case 't':
                        expression = alphabet.createSymbol('\t');
                        break;
                    case 'r':
                        expression = alphabet.createSymbol('\r');
                        break;
                    case '\\':
                        expression = alphabet.createSymbol('\\');
                        break;
                    case '\'':
                        expression = alphabet.createSymbol('\'');
                        break;
                    case '\"':
                        expression = alphabet.createSymbol('\"');
                        break;
                }
            } else {
                expression = alphabet.createSymbol(chars[i]);
            }
            i++;
            for (; i < chars.length; i++) {
                if (chars[i] == '\\') {
                    switch(chars[++i]) {
                        case 'n':
                            expression = new Concatenation(expression, alphabet.createSymbol('\n'));
                            break;
                        case 't':
                            expression = new Concatenation(expression, alphabet.createSymbol('\t'));
                            break;
                        case 'r':
                            expression = new Concatenation(expression, alphabet.createSymbol('\r'));
                            break;
                        case '\\':
                            expression = new Concatenation(expression, alphabet.createSymbol('\\'));
                            break;
                        case '\'':
                            expression = new Concatenation(expression, alphabet.createSymbol('\''));
                            break;
                        case '\"':
                            expression = new Concatenation(expression, alphabet.createSymbol('\"'));
                            break;
                    }
                } else {
                    expression = new Concatenation(expression, alphabet.createSymbol(chars[i]));
                }
            }
            return expression;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return alphabet.createSymbol(((Character) objects[0]).charValue());
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            Character character = (Character) objects[1];
            Character character1 = (Character) objects[3];
            return alphabet.createSymbolInterval(character.charValue(), character1.charValue());
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return objects[1];
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return new ExcludedSymbolSet((SymbolSet) objects[1]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            SymbolSetList symbolSetList = new SymbolSetList();
            symbolSetList.add((SymbolSet) objects[0]);
            return symbolSetList;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            SymbolSetList symbolSetList = (SymbolSetList) objects[2];
            symbolSetList.add(0, (SymbolSet) objects[0]);
            return symbolSetList;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String image = ((Token) objects[0]).getImage();
            if (image.charAt(1) == '\\') {
                switch(image.charAt(2)) {
                    case 'n':
                        return new Character('\n');
                    case 't':
                        return new Character('\t');
                    case 'r':
                        return new Character('\r');
                    case '\\':
                        return new Character('\\');
                    case '\'':
                        return new Character('\'');
                    case '"':
                        return new Character('"');
                }
            } else {
                return new Character(image.charAt(1));
            }
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            int number = Integer.valueOf(((Token) objects[0]).getImage()).intValue();
            return new Character((char) number);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String scannerState = ((Token) objects[0]).getImage();
            scannerState = scannerState.substring(1, scannerState.length() - 1);
            String token = ((Token) objects[0]).getImage();
            String code = (String) objects[3];
            createToken(token, scannerState, (RegularExpression) objects[2], code);
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String scannerState = "DEFAULT_STATE";
            String token = ((Token) objects[0]).getImage();
            String code = (String) objects[3];
            createToken(token, scannerState, (RegularExpression) objects[2], code);
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String code = ((Token) objects[0]).getImage();
            code = code.substring(2, code.length() - 2);
            return code;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            ignoreToken(((Token) objects[0]).getImage());
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            ignoreToken(((Token) objects[0]).getImage());
            return null;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            ParserData parserData = (ParserData) objects[2];
            System.out.println();
            System.out.println("... Parsed parser section:");
            parserData.dump(System.out);
            return parserData;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List productionList = (List) objects[1];
            return getParserData((Production[]) productionList.toArray(new Production[productionList.size()]), (List) objects[0]);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List productionList = (List) objects[0];
            return getParserData((Production[]) productionList.toArray(new Production[productionList.size()]), new ArrayList());
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            return objects[0];
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List productionList = (List) objects[0];
            List productionList1 = (List) objects[1];
            productionList.addAll(productionList1);
            return productionList;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List productionList = (List) objects[2];
            for (int i = 0; i < productionList.size(); i++) {
                Production production = (Production) productionList.get(i);
                production.setSymbol(getSymbol(((Token) objects[0]).getImage()));
            }
            return productionList;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List productionList = new ArrayList();
            productionList.add(objects[0]);
            return productionList;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List productionList = (List) objects[2];
            productionList.add(0, objects[0]);
            return productionList;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List symbolList = (List) objects[0];
            String javaCode = (String) objects[1];
            Action action;
            if (javaCode == null) {
                action = new Action(generator.getProductionDefaultAction());
            } else {
                action = new Action(replaceVariables(javaCode));
            }
            return new Production(null, (Grammar.Symbol[]) symbolList.toArray(new Grammar.Symbol[symbolList.size()]), action);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            String javaCode = (String) objects[0];
            Action action;
            if (javaCode == null) {
                action = new Action("{ return null; }");
            } else {
                action = new Action(replaceVariables(javaCode));
            }
            return new Production(null, new Grammar.Symbol[] {}, action);
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List symbolList = new ArrayList();
            symbolList.add(getSymbol(((Token) objects[0]).getImage()));
            return symbolList;
        }
    }, new ProductionAction() {

        public Object execute(Object[] objects) {
            List symbolList = (List) objects[0];
            symbolList.add(getSymbol(((Token) objects[1]).getImage()));
            return symbolList;
        }
    } };

    private Generator generator = null;

    private File destination = new File(".");

    private Hashtable helperHashtable = new Hashtable();

    private Hashtable scannerStateHashtable = new Hashtable();

    private Hashtable terminalHashtable = new Hashtable();

    private Hashtable nonterminalHashtable = new Hashtable();

    private Alphabet alphabet = new Alphabet();

    private int priority = 0;

    private ParserData getParserData(Production[] productions, List sectionsList) {
        Collection terminalCollection = terminalHashtable.values();
        Grammar.Symbol[] terminals = (Grammar.Symbol[]) terminalCollection.toArray(new Grammar.Symbol[terminalCollection.size()]);
        Collection nonterminalCollection = nonterminalHashtable.values();
        Grammar.Symbol[] nonterminals = (Grammar.Symbol[]) nonterminalCollection.toArray(new Grammar.Symbol[nonterminalCollection.size()]);
        try {
            ParserData parserData = new ParserData(new Grammar(terminals, nonterminals, productions[0].getSymbol(), productions));
            for (Iterator iterator = sectionsList.iterator(); iterator.hasNext(); ) {
                String[] section = (String[]) iterator.next();
                parserData.addUserCode(section[0], section[1]);
            }
            return parserData;
        } catch (ParserDataException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private ScannerData getScannerData(List sectionsList) {
        List automatonsList = new ArrayList();
        Enumeration statesEnumeration = scannerStateHashtable.keys();
        while (statesEnumeration.hasMoreElements()) {
            String state = (String) statesEnumeration.nextElement();
            List tokensList = (List) scannerStateHashtable.get(state);
            TokenDefinition[] tokenDefinitions = (TokenDefinition[]) tokensList.toArray(new TokenDefinition[tokensList.size()]);
            DynamicAutomaton dynamicAutomaton = ThompsonVisitor.createAutomaton(tokenDefinitions, alphabet);
            dynamicAutomaton.setStateName(state);
            try {
                automatonsList.add(new StaticAutomaton(AutomatonConvertor.convert(dynamicAutomaton)));
            } catch (NondeterministicException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        StaticAutomaton[] staticAutomatons = (StaticAutomaton[]) automatonsList.toArray(new StaticAutomaton[automatonsList.size()]);
        ScannerData scannerData = new ScannerData(staticAutomatons);
        for (Iterator iterator = sectionsList.iterator(); iterator.hasNext(); ) {
            String[] section = (String[]) iterator.next();
            scannerData.addUserCode(section[0], section[1]);
        }
        return scannerData;
    }

    private void createToken(String token, String scannerStateName, RegularExpression expression, String code) {
        String token_constant = createTerminal(token);
        if (code == null) {
            code = generator.getTokenMatchAction(token_constant);
        }
        List scannerStateTokenList = (List) scannerStateHashtable.get(scannerStateName);
        if (scannerStateTokenList == null) {
            scannerStateTokenList = new ArrayList();
            scannerStateHashtable.put(scannerStateName, scannerStateTokenList);
        }
        TokenDefinition tokenDefinition = new TokenDefinition(token_constant, expression);
        tokenDefinition.setAction(new Action(code, ++priority));
        scannerStateTokenList.add(tokenDefinition);
    }

    private void ignoreToken(String name) {
        Enumeration statesEnumeration = scannerStateHashtable.keys();
        while (statesEnumeration.hasMoreElements()) {
            String state = (String) statesEnumeration.nextElement();
            List tokensList = (List) scannerStateHashtable.get(state);
            for (Iterator iterator = tokensList.iterator(); iterator.hasNext(); ) {
                TokenDefinition tokenDefinition = (TokenDefinition) iterator.next();
                if (tokenDefinition.getConstantName().equals(name)) {
                    tokenDefinition.setAction(new Action(generator.getTokenIgnoredAction(), ++priority));
                    return;
                }
            }
        }
    }

    private String createTerminal(String name) {
        if (!terminalHashtable.containsKey(name)) {
            terminalHashtable.put(name, Grammar.createTerminal(name));
        }
        return name;
    }

    private Object getHelper(Token identifier) {
        String name = identifier.getImage();
        if (!helperHashtable.containsKey(name)) {
            System.out.println("error(" + identifier.getOffset() + "): Helper " + name + " redefined!");
            return Epsilon.EPSILON;
        } else {
            return helperHashtable.get(name);
        }
    }

    private void createHelper(Token identifier, RegularExpression regularExpression) {
        String name = identifier.getImage();
        if (helperHashtable.containsKey(name)) {
            System.out.println("warning(" + identifier.getOffset() + "): Helper " + name + " redefined!");
        } else {
            helperHashtable.put(name, regularExpression);
        }
    }

    private Grammar.Symbol getSymbol(String name) {
        if (terminalHashtable.containsKey(name)) {
            return (Grammar.Symbol) terminalHashtable.get(name);
        } else {
            if (nonterminalHashtable.containsKey(name)) {
                return (Grammar.Symbol) nonterminalHashtable.get(name);
            } else {
                Grammar.Symbol symbol = Grammar.createNonterminal(name);
                nonterminalHashtable.put(name, symbol);
                return symbol;
            }
        }
    }

    public void setDestination(File destination) {
        this.destination = destination;
    }

    private String replaceVariables(String javaCode) {
        for (int i = 0; i < 256; i++) {
            int index;
            String variable = "${" + i + "}";
            while ((index = javaCode.indexOf(variable)) >= 0) {
                javaCode = javaCode.substring(0, index) + "objects[" + i + "]" + javaCode.substring(index + variable.length());
            }
        }
        return javaCode;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Reader reader;
        File file = new File(args[0]);
        reader = new FileReader(file);
        Scanner scanner = new Scanner(reader);
        Parser parser = new Parser();
        System.out.println("Parsing file: " + args[0] + " ...");
        parser.setDestination(file.getParentFile());
        parser.parse(scanner);
        System.out.println();
        System.out.println("Done.");
    }

    private static interface ProductionAction {

        public Object execute(Object[] objects);
    }

    private static class StackElement {

        int state;

        Object object;

        public StackElement(int state, Object object) {
            this.state = state;
            this.object = object;
        }

        public String toString() {
            return "{" + state + ", " + object + "}";
        }
    }

    public Parser() {
        stack = new Stack();
        push(0, null);
    }

    private void push(int state, Object object) {
        stack.push(new StackElement(state, object));
    }

    private int currentState() {
        return ((StackElement) stack.peek()).state;
    }

    private Object[] pop(int length) {
        Object[] objects = new Object[length];
        for (int i = length - 1; i >= 0; i--) {
            objects[i] = ((StackElement) stack.pop()).object;
        }
        return objects;
    }

    public Object parse(Scanner scanner) {
        Token token = scanner.next();
        while (true) {
            int state = currentState();
            switch(actions[state][token.getCode()][0]) {
                case SHIFT:
                    push(actions[state][token.getCode()][1], token);
                    token = scanner.next();
                    break;
                case REDUCE:
                    int productionIndex = actions[state][token.getCode()][1];
                    int[] production = productions[productionIndex];
                    Object[] objects = pop(production[1]);
                    Object object = productionActions[productionIndex].execute(objects);
                    state = currentState();
                    push(gotos[state][production[0]], object);
                    break;
                case ERROR:
                    System.out.println("Unexpected token " + token + " at offset " + token.getOffset() + "!");
                    System.out.println("    Candidates are: ");
                    for (int i = 0; i < actions[state].length; i++) {
                        if (actions[state][i][0] != ERROR) {
                            System.out.println("        " + terminalNames[i]);
                        }
                    }
                    return null;
                case ACCEPT:
                    return pop(1)[0];
            }
        }
    }
}
