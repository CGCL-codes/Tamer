package cruise.umple.compiler;

import java.io.*;
import cruise.umple.util.*;
import java.util.*;

/**
 * Parses an Umple file (.ump) based on grammar rules.
 * 
 * As parsing is going on "tokens" will be created and linked together in a hierarchy fashion based on what was parsed.
 */
public class Parser {

    private String filename;

    private String name;

    private List<String> grammarRules;

    private ParseResult parseResult;

    private Position _curParsePos;

    private Token rootToken;

    private List<Rule> rules;

    private List<Couple> couples;

    public Parser(String aName) {
        filename = null;
        name = aName;
        grammarRules = new ArrayList<String>();
        parseResult = new ParseResult(true);
        _curParsePos = null;
        rootToken = reset();
        rules = new ArrayList<Rule>();
        couples = new ArrayList<Couple>();
    }

    public boolean setFilename(String aFilename) {
        boolean wasSet = false;
        filename = aFilename;
        wasSet = true;
        return wasSet;
    }

    public boolean setName(String aName) {
        boolean wasSet = false;
        name = aName;
        wasSet = true;
        return wasSet;
    }

    public boolean addGrammarRule(String aGrammarRule) {
        boolean wasAdded = false;
        wasAdded = grammarRules.add(aGrammarRule);
        return wasAdded;
    }

    public boolean removeGrammarRule(String aGrammarRule) {
        boolean wasRemoved = false;
        wasRemoved = grammarRules.remove(aGrammarRule);
        return wasRemoved;
    }

    public boolean setParseResult(ParseResult aParseResult) {
        boolean wasSet = false;
        parseResult = aParseResult;
        wasSet = true;
        return wasSet;
    }

    public boolean setRootToken(Token aRootToken) {
        boolean wasSet = false;
        rootToken = aRootToken;
        wasSet = true;
        return wasSet;
    }

    /**
   * The Umple file (.ump) that will be parsed.
   */
    public String getFilename() {
        return filename;
    }

    public String getName() {
        return name;
    }

    public String getGrammarRule(int index) {
        String aGrammarRule = grammarRules.get(index);
        return aGrammarRule;
    }

    public String[] getGrammarRules() {
        String[] newGrammarRules = grammarRules.toArray(new String[grammarRules.size()]);
        return newGrammarRules;
    }

    public int numberOfGrammarRules() {
        int number = grammarRules.size();
        return number;
    }

    public boolean hasGrammarRules() {
        boolean has = grammarRules.size() > 0;
        return has;
    }

    public int indexOfGrammarRule(String aGrammarRule) {
        int index = grammarRules.indexOf(aGrammarRule);
        return index;
    }

    /**
   * The results of the parsing, and any errors/warning messages will be stored here.
   */
    public ParseResult getParseResult() {
        return parseResult;
    }

    /**
   * Every parser makes use of "tokens" that are built up based on what is parsed.  In the end it looks almost like a "tree".
   * This is the starting token for which everything will be built off of (sub-tokens).
   */
    public Token getRootToken() {
        return rootToken;
    }

    public Rule getRule(int index) {
        Rule aRule = rules.get(index);
        return aRule;
    }

    /**
   * A parser can have many "rules", likewise each rule can be part of many "parsers"; hence many-to-many.
   */
    public List<Rule> getRules() {
        List<Rule> newRules = Collections.unmodifiableList(rules);
        return newRules;
    }

    public int numberOfRules() {
        int number = rules.size();
        return number;
    }

    public boolean hasRules() {
        boolean has = rules.size() > 0;
        return has;
    }

    public int indexOfRule(Rule aRule) {
        int index = rules.indexOf(aRule);
        return index;
    }

    public Couple getCouple(int index) {
        Couple aCouple = couples.get(index);
        return aCouple;
    }

    public List<Couple> getCouples() {
        List<Couple> newCouples = Collections.unmodifiableList(couples);
        return newCouples;
    }

    public int numberOfCouples() {
        int number = couples.size();
        return number;
    }

    public boolean hasCouples() {
        boolean has = couples.size() > 0;
        return has;
    }

    public int indexOfCouple(Couple aCouple) {
        int index = couples.indexOf(aCouple);
        return index;
    }

    public static int minimumNumberOfRules() {
        return 0;
    }

    public boolean addRule(Rule aRule) {
        boolean wasAdded = false;
        if (rules.contains(aRule)) {
            return false;
        }
        rules.add(aRule);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeRule(Rule aRule) {
        boolean wasRemoved = false;
        if (rules.contains(aRule)) {
            rules.remove(aRule);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    public static int minimumNumberOfCouples() {
        return 0;
    }

    public boolean addCouple(Couple aCouple) {
        boolean wasAdded = false;
        if (couples.contains(aCouple)) {
            return false;
        }
        couples.add(aCouple);
        wasAdded = true;
        return wasAdded;
    }

    public boolean removeCouple(Couple aCouple) {
        boolean wasRemoved = false;
        if (couples.contains(aCouple)) {
            couples.remove(aCouple);
            wasRemoved = true;
        }
        return wasRemoved;
    }

    public void delete() {
        rules.clear();
        couples.clear();
    }

    public Parser(String aFilename, String aName) {
        filename = aFilename;
        name = aName;
        grammarRules = new ArrayList<String>();
        parseResult = new ParseResult(true);
        rootToken = reset();
        rules = new ArrayList<Rule>();
        couples = new ArrayList<Couple>();
    }

    public Token reset() {
        rootToken = new Token(getName(), "ROOT", new Position(filename, 1, 0, 0));
        return rootToken;
    }

    public Rule getRule(String ruleName) {
        for (Rule r : rules) {
            if (r.getName().equals(ruleName)) {
                return r;
            }
        }
        return null;
    }

    public String toString() {
        StringBuffer answer = new StringBuffer();
        toString(answer, rootToken);
        return answer.toString();
    }

    public String toGrammarNoStyle() {
        StringBuilder answer = new StringBuilder();
        for (String rule : grammarRules) {
            if (answer.length() > 0) {
                answer.append("<br />\n");
            }
            String cleanedUpRule = rule;
            cleanedUpRule = cleanedUpRule.replace("OPEN_ROUND_BRACKET", "-(");
            cleanedUpRule = cleanedUpRule.replace("CLOSE_ROUND_BRACKET", "-)");
            cleanedUpRule = cleanedUpRule.replace("DOUBLE_OR_BARS", "-||");
            answer.append(cleanedUpRule);
        }
        return answer.toString();
    }

    public String toGrammar() {
        return toGrammarParts("");
    }

    public String toGrammarParts(String rulesToOutput) {
        StringBuilder answer = new StringBuilder();
        StringBuilder queuedComments = new StringBuilder();
        for (String rule : grammarRules) {
            if (rule.length() == 0) {
                if (rulesToOutput.length() == 0) {
                    answer.append(queuedComments.toString());
                    answer.append("<br />\n");
                }
                queuedComments = new StringBuilder();
            } else if (rule.length() >= 2 && rule.charAt(0) == '/' && rule.charAt(1) == '/') {
                if (!rule.matches("\\Q//TODO\\E.*")) {
                    queuedComments.append("<br />\n<font color=\"brown\">");
                    String commentInterior = rule.replaceAll("\\Q[*http\\E(.*)\\Q*]\\E", "<a style=\"color: brown\" href=\"http$1\">http$1</a>");
                    commentInterior = commentInterior.replaceAll("\\Q[*\\E(.*)\\Q*]\\E", "<a style=\"color: brown\" href=\"$1.html\">$1</a>");
                    queuedComments.append(commentInterior);
                    queuedComments.append("</font>");
                }
            } else {
                String[] splitRule = rule.split(":", 2);
                if (splitRule[0].length() != 0) {
                    int ruleLength = splitRule[1].length();
                    int hasMinus = (splitRule[0]).charAt(splitRule[0].length() - 2) == '-' ? 1 : 0;
                    String ruleName = (splitRule[0]).substring(0, (splitRule[0]).length() - 1 - hasMinus);
                    if (rulesToOutput.length() == 0 || rulesToOutput.contains("[[" + ruleName + "]]")) {
                        String cleanedUpRule = splitRule[1];
                        cleanedUpRule = cleanedUpRule.replaceAll("\\Q[[\\E([a-zA-Z_]*)\\Q]]\\E", "[[<a href=\"UmpleGrammar.html#$1\">$1</a>]]");
                        cleanedUpRule = cleanedUpRule.replaceAll("(\\Q[\\E~?)([a-zA-Z_]+)\\Q]\\E", "$1<font color=\"green\">$2</font>]");
                        cleanedUpRule = cleanedUpRule.replaceAll("(\\Q[\\E)([a-zA-Z_]+),([a-zA-Z_]+)", "$1<font color=\"green\">$2</font>,<font color=\"green\">$3</font>");
                        cleanedUpRule = cleanedUpRule.replaceAll("([(]|\\s)([a-zA-Z_{}.,:;\\-\\\"<>]+|/|//|/\\*|\\*/|\\[|\\])(\\s|$|[)])", "$1<font color=\"red\">$2</font>$3");
                        cleanedUpRule = cleanedUpRule.replaceAll("(\\s)([{}]|OPEN_ROUND_BRACKET)(\\s)", "$1<font color=\"red\">$2</font>$3");
                        cleanedUpRule = cleanedUpRule.replaceAll("([\\[]=[a-zA-Z]*:|[|])([A-Za-z\\-+<>*]+)", "$1<font color=\"red\">$2</font>");
                        cleanedUpRule = cleanedUpRule.replaceAll("([\\[]=[a-zA-Z]*:)\\Q[]\\E", "$1<font color=\"red\">[]</font>");
                        cleanedUpRule = cleanedUpRule.replaceAll("[\\[]=([a-zA-Z_]*)[\\]]", "[=<font color=\"red\">$1</font>]");
                        cleanedUpRule = cleanedUpRule.replaceAll("[\\[][*][*]?([a-zA-Z_]*)[\\]]", "[**<font color=\"#AAAA22\">$1</font>]");
                        cleanedUpRule = cleanedUpRule.replace("OPEN_ROUND_BRACKET", "(");
                        cleanedUpRule = cleanedUpRule.replace("CLOSE_ROUND_BRACKET", ")");
                        cleanedUpRule = cleanedUpRule.replace("DOUBLE_OR_BARS", "||");
                        if (ruleLength > 80) {
                            cleanedUpRule = cleanedUpRule.replaceAll("\\s[|]\\s", "\n<br/>&nbsp;&nbsp;&nbsp;&nbsp| ");
                        }
                        answer.append(queuedComments.toString());
                        answer.append("<br />\n<a name=\"");
                        answer.append(ruleName);
                        answer.append("\" ></a>");
                        answer.append("<b><font color=\"#2E64FE\">");
                        answer.append(ruleName);
                        answer.append("</font></b>");
                        if (hasMinus == 1) answer.append("-");
                        answer.append(" :");
                        answer.append(cleanedUpRule);
                        answer.append("<br />\n");
                    }
                    queuedComments = new StringBuilder();
                }
            }
        }
        return answer.toString();
    }

    public StringBuffer toString(StringBuffer stringSoFar, Token currentToken) {
        return currentToken.toString(stringSoFar, rootToken);
    }

    public int addRulesInFile(String filenameOrResourcePath) {
        InputStream resourceStream = null;
        BufferedReader reader = null;
        int numberOfRulesProcessed = 0;
        try {
            if ((new File(filenameOrResourcePath)).exists()) {
                reader = new BufferedReader(new FileReader(filenameOrResourcePath));
            } else {
                resourceStream = getClass().getResourceAsStream(filenameOrResourcePath);
                reader = new BufferedReader(new InputStreamReader(resourceStream));
            }
            String nextRule = null;
            do {
                nextRule = reader.readLine();
                if (nextRule == null) {
                    continue;
                }
                nextRule = nextRule.trim();
                if (nextRule.startsWith("//") || nextRule.startsWith("#") || nextRule.equals("")) {
                    addGrammarRule(nextRule);
                } else {
                    addRule(nextRule);
                    numberOfRulesProcessed += 1;
                }
            } while (nextRule != null);
        } catch (Exception e) {
        } finally {
            SampleFileWriter.closeAsRequired(reader);
            SampleFileWriter.closeAsRequired(resourceStream);
        }
        return numberOfRulesProcessed;
    }

    public void addRule(String input) {
        input = input.replace("-(", "OPEN_ROUND_BRACKET");
        input = input.replace("-)", "CLOSE_ROUND_BRACKET");
        input = input.replace("-||", "DOUBLE_OR_BARS");
        grammarRules.add(input);
        TextParser ruleParser = new TextParser(input);
        String name = ruleParser.next();
        boolean shouldHide = false;
        if (name.endsWith("-")) {
            shouldHide = true;
            name = name.substring(0, name.length() - 1);
        }
        ruleParser.nextAt(":");
        int startIndex = ruleParser.previousIndex();
        int index = 0;
        Parser innerParser = new Parser("innerParser");
        while (ruleParser.lookFor("(", ")", true) != null) {
            String anonymousRuleName = cruise.umple.util.StringFormatter.format("anonymous::{0}::{1}", name, ++index);
            String anonymousRuleDefinition = ruleParser.name().substring(1, ruleParser.name().length() - 1).trim();
            innerParser.addRule(cruise.umple.util.StringFormatter.format("{0}- : {1}", anonymousRuleName, anonymousRuleDefinition));
            ruleParser.replace("[[" + anonymousRuleName + "]]");
        }
        ruleParser.reset(startIndex);
        ruleParser.nextAt(":");
        Rule newRule = new Rule(name);
        newRule.setShouldHide(shouldHide);
        while (ruleParser.nextUntil(false, "|") != null) {
            String definition = ruleParser.name();
            int innerStartIndex = ruleParser.previousIndex();
            while (isWithinVariable(definition)) {
                ruleParser.nextAfter("]");
                ruleParser.nextUntil(false, "|");
                definition = ruleParser.extractFrom(innerStartIndex);
            }
            definition = definition.replace("OPEN_ROUND_BRACKET", "(");
            definition = definition.replace("CLOSE_ROUND_BRACKET", ")");
            definition = definition.replace("DOUBLE_OR_BARS", "||");
            newRule.addDefinition(definition);
            ruleParser.nextAt("|");
        }
        rules.add(newRule);
        rules.addAll(innerParser.rules);
    }

    /**
   * Parses input based on a rule.
   * 
   * @param ruleName The rule to parse based on.
   * @param input The textual input to parse (such as that from the Umple file).
   * 
   * @return The result of the parsing.
   */
    public ParseResult parse(String ruleName, String input) {
        TextParser inputParser = new TextParser(filename, input);
        parseResult.setPosition(inputParser.currentPosition());
        _curParsePos = inputParser.currentPosition();
        boolean didParse = parse(ruleName, inputParser, rootToken, 0);
        parseResult.setWasSuccess(didParse);
        return parseResult;
    }

    /**
   * Parses input initialized into a textual parser and builds up a tree-like structure of tokens representing what was parsed.
   * 
   * It is VERY important to know this method, as its extremely vital to where a significant amount of parsing is done.
   * 
   * @param ruleName The rule to begin parsing off of.
   * @param inputParser The textual parser that is initialized to start parsing input from a pre-determined Umple file.
   * @param parentToken The current token, that will be built off of.
   * @param level The level The current level the parser is at (related to the token tree).
   * 
   * @return True if the input was successfully parsed, false otherwise.
   */
    private boolean parse(String ruleName, TextParser inputParser, Token parentToken, int level, String... stopAts) {
        for (Rule r : rules) {
            if (!r.getName().equals(ruleName)) {
                continue;
            }
            Token currentToken = null;
            if (r.getShouldHide()) {
                currentToken = parentToken;
            } else {
                currentToken = new Token(ruleName, "START_TOKEN", inputParser.currentPosition().copy());
            }
            for (String definition : r.getDefinitions()) {
                int currentTokenSize = currentToken.numberOfSubTokens();
                boolean isSucceeding = true;
                int savedIndex = inputParser.currentIndex();
                RuleInstance instance = new RuleInstance(this);
                instance.configureDefinition(definition, stopAts);
                while (instance.hasMoreRuleParts()) {
                    Position startTokenPosition = inputParser.currentPosition().copy();
                    RulePart part = instance.nextRulePart();
                    String currentRule = part.getName();
                    if (part.isStatic()) {
                        String inputValue = inputParser.nextAt(currentRule);
                        if (inputValue == null) {
                            isSucceeding = false;
                            break;
                        }
                        currentToken.addSubToken(new Token(inputValue, "STATIC", startTokenPosition));
                        _curParsePos = inputParser.currentPosition();
                        parseResult.setPosition(inputParser.currentPosition());
                    } else if (part.isVariable()) {
                        String value = null;
                        int startIndex = inputParser.currentIndex();
                        if (startIndex >= 3 && currentRule.equals("*inlineComment")) {
                            String lastThree = inputParser.getText().substring(startIndex - 3, startIndex);
                            if (lastThree.equals("//\n")) {
                                continue;
                            } else if (lastThree.charAt(2) == '\n') {
                                continue;
                            }
                        }
                        if (startIndex >= 2 && currentRule.equals("**multilineComment")) {
                            boolean emptyMultiLineComment = false;
                            String testString = inputParser.getText();
                            for (int i = startIndex; i < testString.length() - 2 && emptyMultiLineComment == false; i++) {
                                if (testString.substring(i, i + 2).equals("*/")) {
                                    emptyMultiLineComment = true;
                                } else if (!testString.substring(i, i + 1).equals("\n") && !testString.substring(i, i + 1).equals(" ")) {
                                    break;
                                }
                            }
                            if (emptyMultiLineComment) continue;
                        }
                        if (part.isToEndOfLine()) {
                            value = inputParser.nextLine();
                        } else if (part.getNextIdentifiers().length > 0) {
                            boolean stopAtSpace = !part.isMultiWord() && !part.hasInnerNames();
                            boolean isAlphaNumeric = part.isAlphanumeric();
                            value = inputParser.nextUntil(stopAtSpace, isAlphaNumeric, part.getNextIdentifiers());
                            while (part.isMultiWord() && !isBalanced(value)) {
                                int internalIndex = inputParser.currentIndex();
                                inputParser.nextAt(part.getNextIdentifiers());
                                String nextValue = inputParser.nextUntil(stopAtSpace, part.getNextIdentifiers());
                                if (inputParser.peekAt(part.getNextIdentifiers()) == null) {
                                    inputParser.reset(internalIndex);
                                    break;
                                }
                                value = inputParser.extractFrom(startIndex);
                                if (nextValue == null && inputParser.peekAt(part.getNextIdentifiers()) == null) {
                                    break;
                                }
                            }
                        } else if (part.isMultiWord()) {
                            value = inputParser.nextUntil(false, (String[]) null);
                        } else {
                            value = inputParser.next();
                        }
                        if (part.isEnum() && !part.isEnumValue(value)) {
                            value = null;
                        }
                        if (part.hasInnerNames() && !part.isValidInnerValues(value)) {
                            value = null;
                        }
                        if (value == null && part.isOne()) {
                            isSucceeding = instance.removeOptionalPart();
                            if (isSucceeding) {
                                instance.resetRulePart();
                                restorePrevious(inputParser, savedIndex, currentToken, currentTokenSize);
                                continue;
                            } else {
                                break;
                            }
                        } else if (value == null) {
                            instance.removeRulePart(part);
                            instance.resetRulePart();
                            restorePrevious(inputParser, savedIndex, currentToken, currentTokenSize);
                            continue;
                        }
                        if (part.hasInnerNames()) {
                            RulePartValue[] allValues = part.getInnerValues(value);
                            for (int innerI = 0; innerI < allValues.length; innerI++) {
                                String innerValue = allValues[innerI].getValue();
                                if (innerValue == null) {
                                    continue;
                                }
                                String innerName = allValues[innerI].getName();
                                Position innerPosition = allValues[innerI].getPosition();
                                currentToken.addSubToken(new Token(innerName, innerValue, startTokenPosition.add(innerPosition)));
                            }
                        } else {
                            currentToken.addSubToken(new Token(part.getDisplayName(), value, startTokenPosition));
                        }
                    } else if (part.isRule()) {
                        if (part.isOne()) {
                            isSucceeding = parse(part.getName(), inputParser, currentToken, level + 1, part.getNextIdentifiers());
                        } else if (part.isOptional()) {
                            isSucceeding = true;
                            boolean didParse = parse(part.getName(), inputParser, currentToken, level + 1, part.getNextIdentifiers());
                            if (!didParse) {
                                instance.removeRulePart(part);
                                instance.resetRulePart();
                                restorePrevious(inputParser, savedIndex, currentToken, currentTokenSize);
                                continue;
                            }
                        } else if (part.isMany()) {
                            int maxFound = part.getMaximumPartsFound();
                            if (maxFound == 0) {
                                instance.removeRulePart(part);
                                instance.resetRulePart();
                                restorePrevious(inputParser, savedIndex, currentToken, currentTokenSize);
                                continue;
                            }
                            isSucceeding = true;
                            int numberFoundSoFar = 0;
                            while (part.isWithinLimits(numberFoundSoFar) && parse(part.getName(), inputParser, currentToken, level + 1, part.getNextIdentifiers())) {
                                numberFoundSoFar += 1;
                            }
                            part.setMaximumPartsFound(numberFoundSoFar - 2);
                        }
                    }
                }
                if (!isSucceeding) {
                    restorePrevious(inputParser, savedIndex, currentToken, currentTokenSize);
                } else if (inputParser.peek() != null && level == 0) {
                    String badWord = inputParser.peek();
                    int messageNumber = 1500;
                    restorePrevious(inputParser, savedIndex, currentToken, currentTokenSize);
                    if ("generate".equals(badWord) || "use".equals(badWord) || "strictness".equals(badWord) || "traceType".equals(badWord)) {
                        messageNumber = 1501;
                    } else if ("class".equals(badWord) || "association".equals(badWord) || "interface".equals(badWord) || "external".equals(badWord) || "associationClass".equals(badWord) || "stateMachine".equals(badWord)) {
                        messageNumber = 1502;
                    }
                    parseResult.addErrorMessage(new ErrorMessage(messageNumber, _curParsePos, badWord));
                    return false;
                } else {
                    if (!r.getShouldHide()) {
                        parentToken.addSubToken(currentToken);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void restorePrevious(TextParser inputParser, int savedIndex, Token currentToken, int size) {
        inputParser.reset(savedIndex);
        while (size < currentToken.numberOfSubTokens()) {
            currentToken.remove(size);
        }
    }

    private boolean isWithinVariable(String definition) {
        int openBracket = definition.lastIndexOf("[");
        int closeBracket = definition.lastIndexOf("]");
        return openBracket > closeBracket;
    }

    private boolean isBalanced(String input) {
        for (Couple couple : couples) {
            if (!couple.isBalanced(input)) {
                return false;
            }
        }
        return true;
    }

    public Token getToken(int index) {
        return rootToken.getSubToken(index);
    }

    public List<Token> getTokens() {
        return rootToken.getSubTokens();
    }

    public int numberOfTokens() {
        return rootToken.numberOfSubTokens();
    }

    public boolean hasTokens() {
        return numberOfTokens() > 0;
    }

    public int indexOf(Token aToken) {
        return rootToken.indexOfSubToken(aToken);
    }
}
