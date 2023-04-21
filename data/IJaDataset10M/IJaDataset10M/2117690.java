package commandlineinterface;

import exceptions.InvalidUserChoiceException;
import modeller.databasedesignmodel.*;
import modeller.databasedesignmodel.query.*;
import modeller.databasedesignmodel.relation.index.*;
import modeller.databasedesignmodel.transaction.*;
import java.util.*;

/**
 * Created by:  Jason Ye
 * Date:        28/02/2012
 * Time:        13:19
 */
public class UIEvaluator {

    private int currentTransactionID = 1;

    private Set<Relation> relations;

    private Set<Transaction> transactions;

    private DatabaseDesign design;

    private Scanner scanner;

    private int dbPageSize;

    private double dbFillFactor;

    private String[] yesNoArray = { "No", "Yes" };

    String[] sqlStatementTypesArray = { "Sequential Scan", "Scan with Equality Condition", "Scan with Range Condition", "Update/Delete Statement", "Insert Statement" };

    public UIEvaluator() {
        design = new DatabaseDesign();
        scanner = new Scanner(System.in);
        transactions = new HashSet<Transaction>();
        relations = new HashSet<Relation>();
    }

    public DatabaseDesign start() {
        dbPageSize = getIntFromUserInput("Please enter DB Page size in bytes: ", scanner);
        design.setDBPageSize(dbPageSize);
        System.out.println("DB page size now set as: " + dbPageSize);
        dbFillFactor = getDoubleFromUserInput("Please enter average DB fill factor as proportion of each page in bytes: ", scanner);
        design.setDBFillFactor(dbFillFactor);
        System.out.println("DB fillfactor now set as: " + dbFillFactor);
        double ioTime = getDoubleFromUserInput("Please enter average DB IO Time in milliseconds: ", scanner);
        design.setDAverageIOTime(ioTime);
        System.out.println("Now specifying RELATIONS ...");
        relations.add(initRelation());
        while (getYesNoAnswerToQuestion("Specify another relation?")) {
            relations.add(initRelation());
        }
        design.setRelations(relations);
        System.out.println("RELATIONs have now all been specified.");
        System.out.println("Now specifying TRANSACTIONs ...");
        transactions.add(initTransaction());
        while (getYesNoAnswerToQuestion("Would you like to add another Transaction?")) {
            transactions.add(initTransaction());
        }
        design.setTransactions(transactions);
        System.out.println("TRANSACTIONs have now all been specified.");
        scanner.close();
        System.out.println(design.toString());
        return design;
    }

    /**
     * processes user input and creates a Relation object
     * @return Relation as per user spec
     */
    public Relation initRelation() {
        Relation newRel = new Relation(2, 2, 22, 2);
        String relationName = getStringFromUserInput("Please enter a name for your new Relation:", scanner);
        newRel.setTableName(relationName);
        System.out.println("Creating the RELATION '" + relationName + "' ...");
        int expectedNumRows = getIntFromUserInput("Please enter the expected number of rows that this RELATION will contain:", scanner);
        newRel.setExpectedNumRows(expectedNumRows);
        System.out.println("We now specify the set of ATTRIBUTES for the '" + relationName + "' RELATION...");
        Set<Attribute> attributesForRelation = new HashSet<Attribute>();
        attributesForRelation.add(initAttribute(relationName));
        while (getYesNoAnswerToQuestion("Does the RELATION '" + relationName + "' have more than the " + attributesForRelation.size() + " attribute(s) already entered?")) {
            attributesForRelation.add(initAttribute(relationName));
        }
        newRel.setAttributes(attributesForRelation);
        Set<AbstractTableIndex> tableIndexesForRelation = new HashSet<AbstractTableIndex>();
        Attribute[] availableAttributes = new Attribute[attributesForRelation.size()];
        attributesForRelation.toArray(availableAttributes);
        String[] availableAttributeNames = new String[availableAttributes.length];
        for (int i = 0; i < availableAttributeNames.length; i++) {
            availableAttributeNames[i] = availableAttributes[i].getAttributeName();
        }
        System.out.println("Now that all ATTRIBUTES are known, we can define the INDEXs on RELATION '" + relationName + "' ...");
        tableIndexesForRelation.add(initTableIndex(availableAttributes, availableAttributeNames));
        while (getYesNoAnswerToQuestion("Is there another INDEX defined on the RELATION '" + relationName + "'?")) {
            tableIndexesForRelation.add(initTableIndex(availableAttributes, availableAttributeNames));
        }
        newRel.setTableIndexes(tableIndexesForRelation);
        System.out.println("The RELATION '" + relationName + "' has now been fully specified.");
        return newRel;
    }

    private Attribute initAttribute(String relationName) {
        System.out.println("Specifying new ATTRIBUTE on RELATION '" + relationName + "' ...");
        Attribute result = new Attribute("Blah", AttributeTypeEnum.INT, 20);
        result.setAttributeName(getStringFromUserInput("Please enter a name for this ATTRIBUTE:", scanner));
        result.setSelectivity(getDoubleFromUserInput("Please enter the SELECTIVITY of the '" + result.getAttributeName() + "' ATTRIBUTE:", scanner));
        result.setAttributeSize(getIntFromUserInput("Please enter the SIZE of this ATTRIBUTE", scanner));
        return result;
    }

    private AbstractTableIndex initTableIndex(Attribute[] availableIndexes, String[] availableIndexNames) {
        List<Attribute> attributesForIndex = new LinkedList<Attribute>();
        int lastSelected = -1;
        try {
            lastSelected = getOptionFromUserInput("Choose a ATTRIBUTE which forms part (or all) of this index ...", availableIndexNames, scanner);
            attributesForIndex.add(availableIndexes[lastSelected]);
            while (getYesNoAnswerToQuestion("Is this INDEX composed of more ATTRIBUTES?")) {
                lastSelected = getOptionFromUserInput("Choose another ATTRIBUTE which is part of the composite structure of this INDEX:", availableIndexNames, scanner);
                attributesForIndex.add(availableIndexes[lastSelected]);
            }
        } catch (InvalidUserChoiceException e) {
            e.printStackTrace();
        }
        boolean clustered = getYesNoAnswerToQuestion("Is this index clustered?");
        boolean isUnique = getYesNoAnswerToQuestion("Is this a UNIQUE index?");
        if (isUnique) {
            if (clustered) {
                return new UniqueClusteredTreeIndex(attributesForIndex);
            } else {
                return new UniqueUnclusteredTreeIndex(attributesForIndex);
            }
        } else {
        }
        AbstractTableIndex index = new HeapIndex();
        index.setAttributeList(attributesForIndex);
        return index;
    }

    private Transaction initTransaction() {
        Set<IProceduralStatement> proceduralStatements = new HashSet<IProceduralStatement>();
        System.out.println("Specifying a new TRANSACTION ...");
        int id = currentTransactionID;
        currentTransactionID++;
        double transactionPrevalence = -1;
        while (transactionPrevalence < 0 || transactionPrevalence > 1) {
            transactionPrevalence = getDoubleFromUserInput("Enter transaction prevalence between 0 and 1", scanner);
        }
        double arrivalRate = -1;
        while (arrivalRate < 0) {
            arrivalRate = getDoubleFromUserInput("Enter mean inter-arrival time (λ)", scanner);
        }
        System.out.println("TRANSACTIONs consist of one or more PROCEDURAL STATEMENTs - initialising first PROCEDURAL STATEMENT ...");
        proceduralStatements.add(initProceduralStatement());
        while (getYesNoAnswerToQuestion("Would you like to add another PROCEDURAL STATEMENT to this TRANSACTION?")) {
            proceduralStatements.add(initProceduralStatement());
        }
        Transaction result = new Transaction(id);
        result.setProceduralStatements(proceduralStatements);
        result.setTransactionPrevalence(transactionPrevalence);
        System.out.println("Transaction has been fully specified!");
        return result;
    }

    private double getDoubleFromUserInput(String userQuestion, Scanner scannerInUse) {
        System.out.println("\n" + userQuestion);
        double userSpecifiedDouble;
        try {
            userSpecifiedDouble = scannerInUse.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            return getDoubleFromUserInput(userQuestion, scannerInUse);
        }
        return userSpecifiedDouble;
    }

    private boolean getYesNoAnswerToQuestion(String question) {
        int wantAnother = 0;
        try {
            wantAnother = getOptionFromUserInput(question, yesNoArray, scanner);
            System.out.println("Your selection was: " + yesNoArray[wantAnother]);
        } catch (InvalidUserChoiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Type 1 for yes, 0 for no.");
            return getYesNoAnswerToQuestion(question);
        }
        return wantAnother == 1;
    }

    private int getOptionFromUserInput(String userQuestion, String[] options, Scanner scannerInUse) throws InvalidUserChoiceException {
        System.out.println("\n" + userQuestion);
        System.out.println("\nType the number in square brackets to choose the option associated with it\n");
        for (int i = 0; i < options.length; i++) {
            System.out.println("\t[" + i + "] " + options[i]);
        }
        int index = -1;
        index = scannerInUse.nextInt();
        if (index < 0 || index >= options.length) {
            throw new InvalidUserChoiceException(index);
        }
        return index;
    }

    private String getStringFromUserInput(String userQuestion, Scanner scannerInUse) {
        System.out.println("\n" + userQuestion);
        String userSpecifiedString = "";
        userSpecifiedString = scannerInUse.next();
        return userSpecifiedString;
    }

    private int getIntFromUserInput(String userQuestion, Scanner scannerInUse) {
        System.out.println("\n" + userQuestion);
        int userSpecifiedInt = -1;
        userSpecifiedInt = scannerInUse.nextInt();
        return userSpecifiedInt;
    }

    private IProceduralStatement initProceduralStatement() {
        System.out.println("Specifying a new PROCEDURAL STATEMENT");
        int selectedProcStmtType = getUserToSelectProceduralStatementType();
        switch(selectedProcStmtType) {
            case 0:
                return initLoopProcStmt();
            case 1:
                return initBranchProcStmt();
            case 2:
                return initSQLStatementSequenceProcStmt();
        }
        return null;
    }

    private Loop initLoopProcStmt() {
        System.out.println("Setting up this PROCEDURAL STATEMENT to be a LOOP ... ");
        int numIterations = getIntFromUserInput("How many iterations will this LOOP contain?", scanner);
        System.out.println("We need to specify the body of this " + numIterations + " iteration LOOP using another PROCEDURAL STATEMENT: ");
        IProceduralStatement loopBody = initProceduralStatement();
        System.out.println("LOOP statement created!");
        return new Loop(numIterations, loopBody);
    }

    private Branch initBranchProcStmt() {
        System.out.println("Setting up this PROCEDURAL STATEMENT to be a BRANCH ... ");
        double trueProb = getDoubleFromUserInput("Please specify the probability that the condition on this BRANCH will evaluate to TRUE", scanner);
        System.out.println("Now we specify the PROCEDURAL STATEMENT executed if the BRANCH condition evaluates to TRUE...");
        IProceduralStatement trueStat = initProceduralStatement();
        System.out.println("Now we specify the PROCEDURAL STATEMENT executed if the BRANCH condition evaluates to FALSE...");
        IProceduralStatement falseStat = initProceduralStatement();
        System.out.println("BRANCH statement created!");
        return new Branch(trueProb, trueStat, falseStat);
    }

    private SQLQuerySequence initSQLStatementSequenceProcStmt() {
        SQLQuerySequence sequence = new SQLQuerySequence();
        System.out.println("Setting up this PROCEDURAL STATEMENT to be a SEQUENCE OF SQL STATEMENTS ... ");
        int numStatements = 0;
        System.out.println("Instantiate first SQL STATEMENT ...");
        Relation[] availableRelations = new Relation[relations.size()];
        relations.toArray(availableRelations);
        String[] relationNames = new String[availableRelations.length];
        for (int i = 0; i < relationNames.length; i++) {
            relationNames[i] = availableRelations[i].getTableName();
        }
        sequence.addSQLStatement(specifySQLQuery(availableRelations, relationNames));
        numStatements++;
        while (getYesNoAnswerToQuestion("Add another SQL STATEMENT to this SEQUENCE OF SQL STATEMENTS?")) {
            sequence.addSQLStatement(specifySQLQuery(availableRelations, relationNames));
            numStatements++;
        }
        System.out.println("SQL Statements instantiated, SEQUENCE OF " + numStatements + " SQL STATEMENTS created!...\n");
        return sequence;
    }

    private SQLQuery specifySQLQuery(Relation[] availableRelations, String[] availableRelationsNames) {
        List<Relation> relationsVisited = new LinkedList<Relation>();
        int lastSelected = -1;
        try {
            lastSelected = getOptionFromUserInput("Choose a RELATION which this SQL STATEMENT visits first ...", availableRelationsNames, scanner);
            relationsVisited.add(availableRelations[lastSelected]);
            while (getYesNoAnswerToQuestion("Does this SQL STATEMENT access another RELATION after " + availableRelationsNames[lastSelected] + "?")) {
                lastSelected = getOptionFromUserInput("Choose a RELATION which this SQL STATEMENT visits next ...", availableRelationsNames, scanner);
                relationsVisited.add(availableRelations[lastSelected]);
            }
        } catch (InvalidUserChoiceException e) {
            e.printStackTrace();
        }
        SQLQuery query = initSQLQuery(relationsVisited);
        return query;
    }

    private SQLQuery initSQLQuery(List<Relation> relationsVisited) {
        int queryType = -1;
        try {
            queryType = getOptionFromUserInput("What type of SQL STATEMENT is this?", sqlStatementTypesArray, scanner);
        } catch (InvalidUserChoiceException e) {
            e.printStackTrace();
            return initSQLQuery(relationsVisited);
        }
        switch(queryType) {
            case 0:
                return new SequentialScanQuery(relationsVisited);
            case 1:
                int numRows = getUserToSpecifyNumMatchingRows();
                EqualitySearchQuery ret = new EqualitySearchQuery(relationsVisited);
                ret.setNumMatchingRows(numRows);
                return ret;
            case 2:
                int numRows2 = getUserToSpecifyNumMatchingRows();
                EqualityWithRangeSearchQuery ret2 = new EqualityWithRangeSearchQuery(relationsVisited);
                ret2.setNumMatchingRows(numRows2);
                return ret2;
            case 3:
                int numMatchingRows = getUserToSpecifyNumMatchingRows();
                return new UpdateDeleteQuery(numMatchingRows);
            default:
                return new InsertQuery(relationsVisited);
        }
    }

    private int getUserToSpecifyNumMatchingRows() {
        return getIntFromUserInput("Please specify the number of matching rows for this QUERY:", scanner);
    }

    /**
     * creates user prompt and handles user input to enter a procedural statement type
     * @return int representing the intended type of procedural statement
     */
    private int getUserToSelectProceduralStatementType() {
        int selectedProcStmtType = -1;
        String[] proceduralStatementTypes = { "Loop", "Branch", "Sequence of one or more SQL statements" };
        boolean valid = false;
        while (valid == false) {
            try {
                selectedProcStmtType = getOptionFromUserInput("Please select the type of this PROCEDURAL STATEMENT:", proceduralStatementTypes, scanner);
                valid = true;
            } catch (InvalidUserChoiceException e) {
                System.out.println(e.getMessage());
            }
        }
        return selectedProcStmtType;
    }

    public static void main(String[] args) {
        UIEvaluator thisEval = new UIEvaluator();
        thisEval.start();
    }

    public DatabaseDesign getDesign() {
        return design;
    }

    public void setDesign(DatabaseDesign design) {
        this.design = design;
    }
}
