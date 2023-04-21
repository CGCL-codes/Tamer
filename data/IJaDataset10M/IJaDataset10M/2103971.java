package org.fernwood.jbasic.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import org.fernwood.jbasic.JBasic;
import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.Expression;
import org.fernwood.jbasic.compiler.Optimizer;
import org.fernwood.jbasic.compiler.Tokenizer;
import org.fernwood.jbasic.opcodes.OpCLEAR;
import org.fernwood.jbasic.opcodes.OpCATALOG;
import org.fernwood.jbasic.value.RecordStreamValue;
import org.fernwood.jbasic.value.Value;

/**
 * Super-simplified SQL processor. Accepts really simplistic SQL expressions
 * for column selection, row filtering, and sorting and translates them into
 * lower-level table semantics.
 * 
 * @author cole
 * @version version 1.0 Apr 21, 2011
 *
 */
public class MockSQLStatement {

    /**
	 * An unknown statement type, or one that was not successfully prepared
	 */
    public static final int STMT_UNKNOWN = 0;

    /**
	 * a SELECT statement, which creates a result set
	 */
    public static final int STMT_SELECT = 1;

    /**
	 * An INSERT INTO <table> VALUES(...) statement which adds a single row
	 * of data to a database
	 */
    public final int STMT_INSERT_VALUES = 2;

    /**
	 * An INSERT INTO <table> SELECT... statement which adds rows from one
	 * result set into a new table.
	 */
    public final int STMT_INSERT_SELECT = 3;

    /**
	 * DROP TABLE statement which deletes a table.
	 */
    public final int STMT_DROP = 4;

    /**
	 * This private value indicates the start of the range of CREATE
	 * statements. This is used internally during execute() processing
	 */
    private static final int STMT_CREATE = 20;

    /**
	 * a CREATE TABLE .. AS SELECT ... which creates a new table from
	 * a SELECT statement.
	 */
    public static final int STMT_CREATE_AS = 21;

    /**
	 * a CREATE TABLE .. LIKE SELECT ... which creates an empty table
	 * whose column format is the same as the results of the SELECT
	 * statement.
	 */
    public static final int STMT_CREATE_LIKE = 22;

    /**
	 * CREATE TABLE with list of types
	 */
    private static final int STMT_CREATE_TABLE = 23;

    /**
	 * CREATE CATALOG
	 */
    private static final int STMT_CREATE_CATALOG = 24;

    /**
	 * This describes what kind of statement we are
	 */
    private int statementType;

    /**
	 * The list of variables that are to be read/written
	 */
    ArrayList<String> variableList;

    /**
	 * Flag indicating if generated code has been optimized.
	 * This is affeced by the SYS$SQLOPT flag and only has
	 * effect for statements that are explicitly run by the
	 * execute() method, such as the SQL() function.
	 */
    private boolean fOptimized;

    /**
	 * Flag indicating if this is a SELECT * operation.
	 */
    boolean fSelectAll;

    /**
	 * The name of the table to be manipulated
	 */
    private String tableName;

    /**
	 * The source table to be manipulated.
	 */
    RecordStreamValue table;

    /**
	 * Bytecode that contains the generated code for the statement.
	 */
    public ByteCode generatedCode;

    /**
	 * This is a VALUE object that is used to declare the result set for a SELECT, etc.
	 */
    private RecordStreamValue resultSet;

    /**
	 * This is usually the same as resultSet except in the case where the result of
	 * the SQL statement is a scalar value, such as SELECT COUNT(*)...
	 */
    private Value resultValue;

    /**
	 * The name of the output table, if any.
	 */
    private String resultName;

    /**
	 * This is the position in the source table we last read.
	 */
    int cursor;

    /**
	 * The default set of any given FETCH buffer.  If there are more rows in 
	 * the table than this count, then only the next resultSetSize rows are
	 * returned. The default is 100.
	 */
    int resultSetSize;

    /**
	 * JBasic session for this statement.
	 */
    private JBasic session;

    /**
	 * Create a new instance.  This does any required initialization of
	 * the class.
	 * @param session the JBasic session hosting this statement, which is
	 * used to locate execution context during execute() and fetch()
	 * operations.
	 */
    public MockSQLStatement(JBasic session) {
        statementType = STMT_UNKNOWN;
        variableList = new ArrayList<String>();
        resultSetSize = 100;
        fSelectAll = false;
        resultSet = null;
        tableName = null;
        table = null;
        generatedCode = null;
        this.session = session;
        fOptimized = false;
    }

    /**
	 * Prepare a statement for execution using a text string.
	 * @param stmt the text of the statement.
	 * @return a Status object indicating if it was successful
	 */
    public Status prepare(String stmt) {
        Tokenizer tokens = new Tokenizer(stmt);
        return prepare(tokens);
    }

    /**
	 * Prepare a statement for execution using a token buffer
	 * @param tokens the statement tokens to process
	 * @return a Status object indicating if it was successful
	 */
    public Status prepare(Tokenizer tokens) {
        tableName = null;
        table = null;
        variableList = null;
        fOptimized = false;
        String verb = tokens.nextToken();
        if (tokens.getType() != Tokenizer.IDENTIFIER) return new Status(Status.SQL, new Status(Status.VERB, verb));
        if (verb.equals("SELECT")) return prepareSelect(tokens);
        if (verb.equals("CREATE")) {
            return prepareCreate(tokens);
        }
        if (verb.equals("INSERT")) return prepareInsert(tokens);
        if (verb.equals("DROP")) return prepareDrop(tokens);
        return new Status(Status.SQL, new Status(Status.VERB, verb));
    }

    /**
	 * Handle parsing and preparation for DROP TABLE statement
	 * @param tokens
	 * @return
	 */
    private Status prepareDrop(Tokenizer tokens) {
        generatedCode = new ByteCode(session);
        if (tokens.assumeNextToken("CATALOG")) {
            if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.SQL, new Status(Status.INVCATALOG));
            String catalogName = tokens.nextToken();
            generatedCode.add(ByteCode._CATALOG, 0, catalogName);
            generatedCode.add(ByteCode._CLEAR, OpCLEAR.CLEAR_SYMBOL, catalogName);
            return new Status();
        }
        if (!tokens.assumeNextToken("TABLE")) return new Status(Status.SQL, new Status(Status.SQLWORD, "TABLE"));
        if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.SQL, new Status(Status.SQLTABLE));
        String tableName = tokens.nextToken();
        if (tokens.assumeNextSpecial(".")) {
            if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.SQL, new Status(Status.SQLTABLE));
            String recordName = tableName;
            tableName = tokens.nextToken();
            generatedCode.add(ByteCode._CATALOG, 0, recordName);
            generatedCode.add(ByteCode._LOADREF, recordName);
            generatedCode.add(ByteCode._CLEAR, OpCLEAR.CLEAR_MEMBER, tableName);
            statementType = STMT_DROP;
            return new Status();
        }
        generateTypeCheck(tableName);
        generatedCode.add(ByteCode._CLEAR, OpCLEAR.CLEAR_SYMBOL, tableName);
        statementType = STMT_DROP;
        return new Status();
    }

    /**
	 * Generate code in the code stream to verify that the
	 * given name a valid TABLE data type.
	 * If it's not, signal an error.
	 * 
	 * @param tableName The name of the table to be used 
	 * in generating an error message.
	 */
    private void generateTypeCheck(String tableName) {
        generatedCode.add(ByteCode._LOADREF, tableName);
        generatedCode.add(ByteCode._ASSERTTYPE, Value.TABLE, Status.INVTABLE);
    }

    /**
	 * Handle the parsing and preparation for a SELECT statement
	 * @param tokens the tokenizer buffer following the SELECT verb.
	 * @return Status indicating if the statement was valid.
	 */
    private Status prepareSelect(Tokenizer tokens) {
        fSelectAll = false;
        boolean fJoin = false;
        boolean fCount = false;
        boolean fDistinct = false;
        if (tokens.assumeNextToken("COUNT")) fCount = true;
        if (fCount && !tokens.assumeNextSpecial("(")) return new Status(Status.SQL, new Status(Status.PAREN));
        if (tokens.assumeNextToken("DISTINCT")) fDistinct = true;
        if (tokens.assumeNextSpecial("*")) fSelectAll = true; else {
            variableList = new ArrayList<String>();
            while (true) {
                if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) break;
                variableList.add(tokens.nextToken());
                if (!tokens.assumeNextSpecial(",")) break;
            }
        }
        if (fCount && !tokens.assumeNextSpecial(")")) return new Status(Status.SQL, new Status(Status.PAREN));
        if (!tokens.assumeNextToken("FROM")) return new Status(Status.SQL, new Status(Status.SQLWORD, "FROM"));
        if (generatedCode == null) generatedCode = new ByteCode(session);
        tableName = tokens.nextToken();
        boolean fSubSelect = false;
        if (tableName.equals("(")) {
            fSubSelect = true;
            tableName = tokens.nextToken();
        }
        if (tableName.equals("SELECT")) {
            ArrayList<String> tempVariableList = variableList;
            String tempTableName = tableName;
            boolean tempSelectAll = fSelectAll;
            boolean tempJoin = fJoin;
            variableList = null;
            Status subStatus = prepareSelect(tokens);
            if (subStatus.failed()) return subStatus;
            variableList = tempVariableList;
            tableName = tempTableName;
            fSelectAll = tempSelectAll;
            fJoin = tempJoin;
            if (fSubSelect && tokens.assumeNextSpecial(")")) {
                fSubSelect = false;
            }
        } else {
            Expression source = new Expression(session, generatedCode);
            source.setSQL(true);
            generatedCode.add(ByteCode._STORBOOL, 1, "__SQL_SELECT_ACTIVE");
            Status sts = source.compileIdentifier(tokens, false);
            if (sts.failed()) return sts;
            if (source.isSingleton && source.isVariable) tableName = source.variableName; else tableName = null;
            if (tableName != null) {
                generateTypeCheck(tableName);
            }
            if (tokens.assumeNextToken("WHERE")) {
                int start = tokens.getPosition();
                Expression exp = new Expression(session);
                int whereLoc = generatedCode.add(ByteCode._WHERE, 0);
                sts = exp.compile(generatedCode, tokens);
                if (sts.failed()) return new Status(Status.SQL, sts);
                StringBuffer whereClause = new StringBuffer();
                int resume = tokens.getPosition();
                tokens.setPosition(start);
                while (tokens.getPosition() < resume) {
                    String t = tokens.nextToken();
                    if (tokens.getType() == Tokenizer.STRING) {
                        whereClause.append('"');
                        whereClause.append(t);
                        whereClause.append('"');
                    } else whereClause.append(t);
                    whereClause.append(' ');
                }
                Instruction i = generatedCode.getInstruction(whereLoc);
                i.integerOperand = generatedCode.size() - whereLoc - 1;
                i.stringValid = true;
                i.stringOperand = whereClause.toString();
            }
            generatedCode.add(ByteCode._STORBOOL, 0, "__SQL_SELECT_ACTIVE");
            if (fSubSelect) {
                fSubSelect = false;
                if (!tokens.assumeNextSpecial(")")) return new Status(Status.PAREN);
            }
            if (tokens.assumeNextToken("JOIN")) {
                fJoin = true;
                String joinTable = null;
                generatedCode.add(ByteCode._STORBOOL, 1, "__SQL_SELECT_ACTIVE");
                tokens.nextToken();
                if (tokens.getSpelling().equals("(")) {
                    fSubSelect = true;
                    tokens.nextToken();
                }
                if (tokens.getSpelling().equals("SELECT")) {
                    ArrayList<String> tempVariableList = variableList;
                    boolean tempSelectAll = fSelectAll;
                    boolean tempJoin = fJoin;
                    variableList = null;
                    Status subStatus = prepareSelect(tokens);
                    if (subStatus.failed()) return subStatus;
                    variableList = tempVariableList;
                    tableName = null;
                    fSelectAll = tempSelectAll;
                    fJoin = tempJoin;
                    if (fSubSelect && tokens.assumeNextToken(")")) fSubSelect = false;
                } else {
                    sts = source.compileIdentifier(tokens, false);
                    if (sts.failed()) return sts;
                    if (source.isSingleton && source.isVariable) joinTable = source.variableName;
                }
                if (tableName != null) {
                    generateTypeCheck(tableName);
                }
                if (joinTable != null) generateTypeCheck(joinTable);
                if (tokens.assumeNextToken("WHERE")) {
                    Expression exp = new Expression(session);
                    generatedCode.add(ByteCode._WHERE, 0);
                    int whereLoc = generatedCode.size() - 1;
                    sts = exp.compile(generatedCode, tokens);
                    if (sts.failed()) return new Status(Status.SQL, sts);
                    Instruction i = generatedCode.getInstruction(whereLoc);
                    i.integerOperand = generatedCode.size() - whereLoc - 1;
                }
                generatedCode.add(ByteCode._STORBOOL, 0, "__SQL_SELECT_ACTIVE");
                if (!tokens.assumeNextToken("ON")) return new Status(Status.SQL, new Status(Status.SQLWORD, "ON"));
                Expression exp = new Expression(session);
                generatedCode.add(ByteCode._JOIN, 0);
                int joinLoc = generatedCode.size() - 1;
                sts = exp.compile(generatedCode, tokens);
                if (sts.failed()) return new Status(Status.SQL, sts);
                Instruction i = generatedCode.getInstruction(joinLoc);
                i.integerOperand = generatedCode.size() - joinLoc - 1;
                if (fSubSelect && tokens.assumeNextToken(")")) fSubSelect = false;
            }
        }
        if (!fJoin && tokens.assumeNextToken("WHERE")) {
            Expression exp = new Expression(session);
            generatedCode.add(ByteCode._WHERE, 0);
            int whereLoc = generatedCode.size() - 1;
            Status sts = exp.compile(generatedCode, tokens);
            if (sts.failed()) return new Status(Status.SQL, sts);
            Instruction i = generatedCode.getInstruction(whereLoc);
            i.integerOperand = generatedCode.size() - whereLoc - 1;
        }
        if (tokens.peek(0).equals("ORDER") && tokens.peek(1).equals("BY")) {
            tokens.assumeNextToken("ORDER");
            tokens.assumeNextToken("BY");
            int keyCount = 0;
            while (true) {
                if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) break;
                generatedCode.add(ByteCode._STRING, tokens.nextToken());
                keyCount++;
                if (!tokens.assumeNextSpecial(",")) break;
            }
            generatedCode.add(ByteCode._CALLF, keyCount + 1, "SORT");
        }
        if (fCount && !fDistinct) {
            generatedCode.add(ByteCode._LENGTH);
        } else {
            if (fSelectAll) {
                if (fDistinct) generatedCode.add(ByteCode._CALLF, 1, "DISTINCT");
                if (fCount) generatedCode.add(ByteCode._LENGTH);
            } else {
                for (int ix = 0; ix < variableList.size(); ix++) generatedCode.add(ByteCode._STRING, namePart(variableList.get(ix)));
                generatedCode.add(ByteCode._CALLF, variableList.size() + 1, "SELECT");
                if (fDistinct) generatedCode.add(ByteCode._CALLF, 1, "DISTINCT");
                if (fCount) generatedCode.add(ByteCode._LENGTH);
            }
        }
        statementType = STMT_SELECT;
        return new Status();
    }

    /**
	 * Prepare an INSERT statement from the token stream
	 * @param tokens
	 * @param symbols
	 * @return a Status indicating success or failure of the prepare operation.
	 */
    private Status prepareInsert(Tokenizer tokens) {
        if (!tokens.assumeNextToken("INTO")) return new Status(Status.SQL, new Status(Status.SQLWORD, "INTO"));
        if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.SQL, new Status(Status.SQLTABLE, ""));
        tableName = tokens.nextToken();
        generatedCode = new ByteCode(this.session);
        if (tokens.assumeNextSpecial(".")) {
            if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.SQL, new Status(Status.SQLTABLE, ""));
            String catalogName = tableName;
            tableName = tokens.nextToken();
            generatedCode.add(ByteCode._CATALOG, 0, catalogName);
            generatedCode.add(ByteCode._STRING, tableName);
            generatedCode.add(ByteCode._LOADR, catalogName);
        } else {
            generatedCode.add(ByteCode._LOADREF, tableName);
            generateTypeCheck(tableName);
        }
        if (tokens.assumeNextToken("SELECT")) {
            Status sts = prepareSelect(tokens);
            if (sts.failed()) return sts;
            generatedCode.add(ByteCode._INSERT);
            statementType = STMT_INSERT_SELECT;
            return new Status();
        } else if (tokens.assumeNextToken("VALUES")) {
            if (!tokens.assumeNextSpecial("(")) return new Status(Status.SQL, new Status(Status.PAREN));
            Expression dv = new Expression(this.session);
            int vCount = 0;
            while (true) {
                Status sts = dv.compile(generatedCode, tokens);
                if (sts.failed()) return sts;
                vCount++;
                if (tokens.assumeNextSpecial(")")) break;
                if (!tokens.assumeNextSpecial(",")) return new Status(Status.SQL, new Status(Status.COMMA));
            }
            generatedCode.add(ByteCode._ARRAY, vCount);
            generatedCode.add(ByteCode._INSERT);
            statementType = STMT_INSERT_VALUES;
            return new Status();
        } else if (tokens.assumeNextToken("VALUE")) {
            if (!tokens.assumeNextSpecial("(")) return new Status(Status.SQL, new Status(Status.PAREN));
            Expression dv = new Expression(this.session);
            Status sts = dv.compile(generatedCode, tokens);
            if (sts.failed()) return sts;
            if (!tokens.assumeNextSpecial(")")) return new Status(Status.SQL, new Status(Status.PAREN));
            generatedCode.add(ByteCode._INSERT);
            statementType = STMT_INSERT_VALUES;
            return new Status();
        }
        return new Status(Status.SQL);
    }

    private Status prepareCreate(Tokenizer tokens) {
        if (tokens.assumeNextToken("CATALOG")) {
            String catalog;
            if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.INVCATALOG, tokens.nextToken());
            catalog = tokens.nextToken();
            generatedCode = new ByteCode(session, null);
            generatedCode.add(ByteCode._RECORD, 0);
            generatedCode.add(ByteCode._STOR, catalog);
            generatedCode.add(ByteCode._CATALOG, OpCATALOG.SET_VALID, catalog);
            if (tokens.assumeNextToken(new String[] { "SAVE", "AUTOSAVE" })) {
                generatedCode.add(ByteCode._STRING, catalog.toUpperCase() + "_catalog.xml");
                generatedCode.add(ByteCode._CATALOG, OpCATALOG.SET_NAME, catalog);
                generatedCode.add(ByteCode._BOOL, 1);
                generatedCode.add(ByteCode._CATALOG, OpCATALOG.SET_AUTO_FLAG, catalog);
            } else if (tokens.assumeNextToken("AS")) {
                Expression e = new Expression(session);
                Status s = e.compile(generatedCode, tokens);
                if (s.failed()) return s;
                generatedCode.add(ByteCode._CATALOG, OpCATALOG.SET_NAME, catalog);
                generatedCode.add(ByteCode._BOOL, 1);
                generatedCode.add(ByteCode._CATALOG, OpCATALOG.SET_AUTO_FLAG, catalog);
            }
            statementType = STMT_CREATE_CATALOG;
            return new Status();
        }
        if (!tokens.assumeNextToken("TABLE")) return new Status();
        if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.SQL, new Status(Status.SQLTABLE, ""));
        String catalogName = null;
        resultName = tokens.nextToken();
        generatedCode = new ByteCode(session, null);
        if (tokens.assumeNextToken(".")) {
            catalogName = resultName;
            generatedCode.add(ByteCode._CATALOG, 0, catalogName);
            if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.SQL, new Status(Status.SQLTABLE, ""));
            resultName = tokens.nextToken();
        }
        int createType = STMT_UNKNOWN;
        if (tokens.assumeNextSpecial("(")) {
            variableList = new ArrayList<String>();
            HashMap<String, String> namesInUse = new HashMap<String, String>();
            int count = 0;
            while (tokens.testNextToken(Tokenizer.IDENTIFIER)) {
                String varName = tokens.nextToken();
                if (namesInUse.get(varName) != null) return new Status(Status.SQL, new Status(Status.SQLDUPFIELD, varName));
                StringBuffer var = new StringBuffer(varName);
                namesInUse.put(varName, varName);
                if (!tokens.testNextToken(Tokenizer.IDENTIFIER)) return new Status(Status.SQL, new Status(Status.SQLWORD, tokens.nextToken()));
                String type = tokens.nextToken();
                if (type.equals("INTEGER")) type = "@INTEGER"; else if (type.equals("DOUBLE")) type = "@DOUBLE"; else if (type.equals("BOOLEAN")) type = "@BOOLEAN"; else if (type.equals("CHAR") || type.equals("CHARACTER")) {
                    tokens.assumeNextToken("VARYING");
                    if (tokens.assumeNextToken("(")) {
                        if (!tokens.testNextToken(Tokenizer.INTEGER)) return new Status(Status.SQL, new Status(Status.SQLWORD, tokens.nextToken()));
                        tokens.nextToken();
                        if (!tokens.testNextToken(")")) return new Status(Status.SQL, new Status(Status.PAREN));
                    }
                    type = "@STRING";
                } else return new Status(Status.SQL, new Status(Status.SQLWORD, type));
                var.append(type);
                count++;
                variableList.add(var.toString());
                if (tokens.assumeNextSpecial(",")) continue;
                if (tokens.assumeNextSpecial(")")) break;
                return new Status(Status.SQL, new Status(Status.SQLWORD, tokens.nextToken()));
            }
            for (int ix = 0; ix < variableList.size(); ix++) generatedCode.add(ByteCode._STRING, variableList.get(variableList.size() - ix - 1));
            generatedCode.add(ByteCode._TABLE, count);
            if (catalogName == null) generatedCode.add(ByteCode._STOR, resultName); else {
                generatedCode.add(ByteCode._STRING, resultName);
                generatedCode.add(ByteCode._STORR, catalogName);
            }
            statementType = STMT_CREATE_TABLE;
            return new Status();
        }
        if (tokens.peek(0).equals("AS")) createType = STMT_CREATE_AS; else if (tokens.peek(0).equals("LIKE")) createType = STMT_CREATE_LIKE;
        if (createType == STMT_UNKNOWN) return new Status(Status.SQL, new Status(Status.SQLWORD, "LIKE or AS"));
        tokens.nextToken();
        if (!tokens.assumeNextToken("SELECT")) return new Status(Status.SQL, new Status(Status.SQLWORD, "SELECT"));
        Status sts = prepareSelect(tokens);
        if (sts.success()) {
            if (createType == STMT_CREATE_LIKE) {
                generatedCode.add(ByteCode._WHERE, 0);
            }
            generatedCode.add(ByteCode._STOR, resultName);
            statementType = createType;
        } else statementType = STMT_UNKNOWN;
        return sts;
    }

    private String namePart(String varName) {
        int n = varName.indexOf('@');
        if (n < 0) return varName;
        return varName.substring(0, n);
    }

    /**
	 * Execute a previously prepared statement
	 * @param symbolTable the active symbol table to use to resolve table
	 * and variable names.
	 * @return a Status object indicating if the execution was successfull.
	 */
    public Status execute(SymbolTable symbolTable) {
        if (statementType == STMT_UNKNOWN) return new Status(Status.SQL, new Status(Status.SQLPREP));
        if (generatedCode != null) {
            if (symbolTable.getBoolean("SYS$SQL_OPT") && !fOptimized) {
                Optimizer opt = new Optimizer();
                opt.optimize(generatedCode);
                fOptimized = true;
            }
            if (symbolTable.getBoolean("SYS$SQL_DISASM")) generatedCode.disassemble();
            Status sts = generatedCode.run(symbolTable, 0);
            if (sts.failed()) return sts;
            resultValue = generatedCode.getResult();
            if (resultValue == null) {
                table = null;
                resultSet = null;
            } else if (resultValue.getType() == Value.TABLE) {
                table = (RecordStreamValue) resultValue;
                resultSet = (RecordStreamValue) table.copy();
                resultSet.empty();
            }
        }
        if (statementType == STMT_INSERT_VALUES) {
            table.addElement(resultSet.getElement(1));
            resultSet = null;
            return new Status();
        }
        return new Status();
    }

    /**
	 * Fetch a result set, specifying the maximum result set size requested
	 * for this particular operation.  The default result set size is not
	 * affected after this call completes.
	 * @param maxResultSetSize The maximum number of rows that can be returned
	 * in a result set from the fetch. Values less than 1 result in the default. 
	 * You can specify the special value of -1 which means deliver all of the
	 * result set in the first call to fetch().
	 * @return a Value containing the portion of the table represented by the
	 * next result set, or null if there was an error or no more data.
	 */
    public Value fetch(int maxResultSetSize) {
        int save = resultSetSize;
        if (maxResultSetSize == -1) resultSetSize = (table == null ? 0 : table.size()); else if (maxResultSetSize > 0) resultSetSize = maxResultSetSize;
        Value r = fetch();
        resultSetSize = save;
        return r;
    }

    /**
	 * Fetch the next result set from the SELECT table.  The number of rows
	 * is the lesser of the number of remaining rows and the resultSetSize 
	 * setting for the statement.  
	 * 
	 * @return a Value containing the portion of the table represented by the
	 * next result set, or null if there was an error.
	 */
    public Value fetch() {
        if (statementType != STMT_SELECT && statementType < STMT_CREATE && statementType != STMT_CREATE_TABLE) return null;
        if (resultValue != null && resultValue.getType() != Value.TABLE) {
            Value temp = resultValue;
            resultValue = null;
            resultSet = null;
            return temp;
        }
        if (resultSet == null) return null;
        if (cursor >= table.size()) return null;
        resultSet.empty();
        for (int ix = 0; ix < resultSetSize; ix++) {
            if (cursor > table.size() - 1) break;
            Value row = table.getElement(++cursor);
            Value newRow = new Value(Value.RECORD, null);
            if (fSelectAll) {
                newRow = row.copy();
            } else {
                for (int cx = 0; cx < variableList.size(); cx++) {
                    String name = namePart(variableList.get(cx));
                    newRow.setElement(row.getElement(name), name);
                }
            }
            resultSet.addElement(newRow);
        }
        return resultSet;
    }

    /**
	 * Return the type of the statement that was last processed with the prepare()
	 * method.  The result will be one of STMT_SELECT, STMT_CREATE_AS, etc. or it
	 * will be STMT_UNKNOWN if there was an error preparing the statement.
	 * @return integer value indicating statement type.
	 */
    public int getStatementType() {
        return statementType;
    }
}
