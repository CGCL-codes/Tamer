package po;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import po.statement.IStatement;
import po.statement.MultiLineStatement;
import po.statement.Statement;
import po.token.Token;

/**
 * 
 * 
 * @author jason
 * 
 */
public class Parser {

    private Lexer lexer;

    static Logger log = Logger.getLogger(Parser.class.getName());

    boolean eof = false;

    private List<List<Token>> statements = new LinkedList<List<Token>>();

    private IStatement statement;

    private Stack<IStatement> statementStack = new Stack<IStatement>();

    private Stack<Token> tokenStack = new Stack<Token>();

    /** Creates a new instance of LineFactory */
    public Parser() {
        lexer = new Lexer();
        statement = new Statement();
    }

    /**
	 * creates a Line from the tokenizer, a line is a series of tokens from
	 * initial to the next EOL or EOF
	 */
    public void process(BufferedReader br) {
        lexer.tokenize(br);
        recursive();
    }

    public void process(String string) {
        lexer.tokenize(string);
        recursive();
    }

    public List<Token> getStatement() {
        return statements.remove(0);
    }

    public boolean hasMore() {
        return !statements.isEmpty();
    }

    public boolean complete() {
        return statementStack.isEmpty();
    }

    private void recursive() {
        while (lexer.hasMore()) {
            retrieveLine();
        }
    }

    @SuppressWarnings("unchecked")
    private void retrieveLine() {
        while (lexer.hasMore()) {
            Token token = lexer.next();
            if (log.isLoggable(Level.FINE)) {
                log.fine("receiving token " + token);
            }
            switch(token.type) {
                case GRAMMAR:
                    switch(token.charValue) {
                        case '|':
                            if (!tokenStack.empty()) {
                                if (token.charValue == tokenStack.peek().charValue) {
                                    tokenStack.pop();
                                    Token listToken = Token.LIST((List<Token>) statement);
                                    statement = statementStack.pop();
                                    statement.add(listToken);
                                    break;
                                }
                            }
                        case '[':
                        case '(':
                            tokenStack.push(token);
                            statementStack.add(statement);
                            statement = new Statement();
                            break;
                        case '{':
                            tokenStack.push(token);
                            statementStack.push(statement);
                            statement = new MultiLineStatement();
                            break;
                        case ')':
                            if (tokenStack.pop().charValue == '(') {
                                Token listToken = Token.LIST((List<Token>) statement);
                                statement = statementStack.pop();
                                statement.add(listToken);
                                break;
                            } else {
                            }
                            break;
                        case ']':
                            if (tokenStack.pop().charValue == '[') {
                                Token listToken = Token.PRIORITY((List<Token>) statement);
                                statement = statementStack.pop();
                                statement.add(listToken);
                                break;
                            } else {
                            }
                            break;
                        case '}':
                            if (tokenStack.pop().charValue == '{') {
                                if (statement.notEmpty()) {
                                    statement.add(Token.EOL());
                                }
                                Token listToken = Token.CODEBLOCK((List<List<Token>>) statement);
                                statement = statementStack.pop();
                                statement.add(listToken);
                                break;
                            } else {
                            }
                            break;
                    }
                    break;
                case EOL:
                    if (statement.type() == IStatement.SINGLE_LINE) {
                        if (statement.notEmpty()) {
                            statements.add((List<Token>) statement);
                            statement = new Statement();
                        }
                    } else {
                        if (statement.notEmpty()) {
                            statement.add(token);
                        }
                    }
                    break;
                default:
                    statement.add(token);
                    break;
            }
        }
    }
}
