package de.usd.nova.compiler;

import antlr.ASTFactory;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.TreeParser;
import antlr.collections.AST;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author JOetting
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Transform extends TreeParser implements Constants, NOVASemanticAnalyseTokenTypes, ByteCodeCommandos {

    ASTFactory fab;

    private ASTFactory dupFactory;

    private boolean modify = false;

    private boolean assemble = true;

    private static final String helperVariable_Start = "_helper";

    private static final String constVariable_Start = "_const ";

    private static int helperCounter = 0;

    private static final int EMPTYPREVIOUSMARKER = 9999;

    /**
	 * Die Transformation wird solange durchlaufen, bis keine Schachtelung mehr enthalten ist.
	 * Ueber diese Methode kann abgefragt werden, ob �nderungen stattgefunden haben
	 */
    public boolean isModified() {
        return modify;
    }

    public Transform() {
        {
            dupFactory = new ASTFactory();
            dupFactory.setASTNodeClass("de.usd.nova.compiler.NovaNode");
        }
    }

    /**
	 * erzeugt einer eindeutigen Benamung f�r eine tempor�re Variable
	 */
    public static String getTempVarName() {
        return helperVariable_Start + (helperCounter++);
    }

    /**
	 * erzeugt einer eindeutigen Benamung f�r eine Konstante
	 */
    public static String getConstVarName() {
        return constVariable_Start + (helperCounter++);
    }

    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

    int nrCommandos = 0;

    /**
	 * gibt die Anzahl der verarbeiteten Kommanodos zur�ck
	 */
    public int getNrCommnds() {
        return nrCommandos;
    }

    /**
	 * liefert das Script als ByteCode 
	 * Zuvor m�ssen alle Transformationsschritte durchlaufen sein
	 */
    public byte[] getCompiledScript() {
        return byteOut.toByteArray();
    }

    /**
	 * wandelt Zahlen in zwei Bytes um, die nach Little-Endian formatiert sind
	 */
    byte[] numberToLittleEndianByteArray(int number) {
        byte bytearray[] = new byte[2];
        bytearray[0] = (byte) (number % 256);
        bytearray[1] = (byte) (number / 256);
        return bytearray;
    }

    /**
	 * ermittelt, ob ein Knoten eine Variable ist oder ein Befehl	 * 
	 * @param ast
	 * @return true, wenn es sich um eine Konstante oder eine Variable handelt
	 */
    private boolean isLiteralOrVariable(NovaNode ast) {
        switch(ast.getType()) {
            case VARIABLE:
            case STRING:
            case NUMBER:
            case SEQUENCE:
            case TYPE_ARRAY_NUMBER:
            case TYPE_ARRAY_SEQUENCE:
            case TYPE_ARRAY_STRING:
                return true;
            default:
                return false;
        }
    }

    /**
	 * wandelt Konstanten in Variablen um und speichert ihre Werte in der Variablentabelle
	 * @pre �bergebener NovaNode muss vom Type ein Literal-Typ sein
	 */
    public void insertConstants(NovaNode node) {
        switch(node.getType()) {
            case STRING:
                int varStrNr = VariablenInfo.insertStringConstant(node.getText());
                node.setVariablenReferenz(varStrNr);
                break;
            case SEQUENCE:
                int varSeqNr = VariablenInfo.insertSequenceConstant(node.getText());
                node.setVariablenReferenz(varSeqNr);
                break;
            case NUMBER:
                int varNumNr = VariablenInfo.insertNumberConstant(Integer.parseInt(node.getText()));
                node.setVariablenReferenz(varNumNr);
                break;
            default:
                node.print();
                throw new IllegalStateException("method 'insertConstants' expects a literal");
        }
        node.setType(VARIABLE);
        node.setText(constVariable_Start + node.getText());
        node.setConstValue(node.getText());
    }

    /**
	 * Fehlerbehandlung bei normalen Fehlern
	 * d.h. es soll versucht werden, wieder aufzusetzen, um noch mehr Fehler festzustellen,
	 * aber er darf nicht durchkompilieren
	 * @param ex Exception mit angereicherten Informationen (wie Positionsangabe)
	 */
    public void reportError(RecognitionException ex) {
        Logger.reportError(ex, Logger.TRANSFORMER);
    }

    /**
	 * Fehlerbehandlung bei normalen Fehlern
	 * d.h. es soll versucht werden, wieder aufzusetzen, um noch mehr Fehler festzustellen,
	 * aber er darf nicht durchkompilieren
	 * @param s Meldungstext
	 */
    public void reportError(java.lang.String s) {
        Logger.reportError(s, Logger.TRANSFORMER);
    }

    /**
	 * Fehlerbehandlung bei Fehlern, die nicht sonderlich ernst sind.
	 * d.h. es soll versucht werden, wieder aufzusetzen,
	 * gibt es keine Error, kompiliert er durch
	 * @param s Meldungstext
	 */
    public void reportWarning(java.lang.String s) {
        Logger.reportWarning(s, Logger.TRANSFORMER);
    }

    public void setASTFactory(ASTFactory factory) {
        fab = factory;
    }

    /**
	 * Transformation eines Kontrollblocks
	 * Bei komplexen Operationen (z.B. IF-Anweisungen, muss er das Umbauen abgewartet werden, bevor rausgeschrieben wird.
	 * daf�r gibt es den Parameter  writeResultIntoStream. Ein Grund daf�r ist, dass man zum Debuggen den korrekten Baum hinter sehen k�nnen soll.  
	 * Statements unterhalb werden nie au�erhalb des Kontrollblockes verschoben
	 * @param command_block Block, der verarbeitet werden soll
	 * @param writeResultIntoStream wenn true, wird Ergebnis in Stream rausgeschrieben, wenn false, wird nur der Baum umgebaut. 
	 * 
	 */
    public void transformBlock(NovaNode command_block, boolean writeResultIntoStream) {
        if (command_block == null) return;
        NovaNode currentStatement = (NovaNode) command_block.getFirstChild();
        NovaNode previousStatement = new NovaNode();
        previousStatement.setType(EMPTYPREVIOUSMARKER);
        while (currentStatement != null) {
            if (currentStatement.getType() == IFELSE || currentStatement.getType() == IFTHEN || currentStatement.getType() == WHILE) {
                transformIFOrWhileBlock(currentStatement, previousStatement);
                currentStatement = (NovaNode) previousStatement.getNextSibling();
            } else {
                if (currentStatement.getType() == ASSIGNMENT) {
                    NovaNode result = (NovaNode) currentStatement.getFirstChild();
                    NovaNode operation = (NovaNode) result.getNextSibling();
                    if (!isLiteralOrVariable(operation)) {
                        result.setNextSibling(operation.getFirstChild());
                        currentStatement.initialize(operation);
                    }
                }
                NovaNode helperAssignments = new NovaNode();
                simplifyExpressionAndMoveInFront((NovaNode) currentStatement.getFirstChild(), helperAssignments);
                helperAssignments = (NovaNode) helperAssignments.getNextSibling();
                if (helperAssignments != null) {
                    if (previousStatement.getType() != EMPTYPREVIOUSMARKER) {
                        previousStatement.setNextSibling(helperAssignments.getNextSibling());
                    } else {
                        command_block.setFirstChild(helperAssignments.getNextSibling());
                    }
                    helperAssignments.addSiblingAtEnd(currentStatement);
                    currentStatement = helperAssignments;
                }
            }
            if (writeResultIntoStream) writeCommand(currentStatement);
            previousStatement = currentStatement;
            currentStatement = (NovaNode) currentStatement.getNextSibling();
        }
    }

    /**
	 * erstellt einen Sprungknoten
	 * @param conditional
	 * @param expression
	 * @param numberOfSteps
	 */
    private NovaNode createJumpNode(NovaNode expression, int numberOfSteps, boolean conditional) {
        NovaNode jumpNode = new NovaNode(new CommonToken(JUMP, "Jump"));
        jumpNode.setBytecodeNr(BC_JUMP);
        if (conditional) {
            jumpNode.setText("cond. not Jump");
            jumpNode.setType(CONDITIAL_JUMP);
            jumpNode.setBytecodeNr(BC_CONDJUMP);
        }
        NovaNode stepsToJump = new NovaNode(new CommonToken(VARIABLE, getConstVarName()));
        stepsToJump.setAprioriTyp(NUMBER);
        int ref = VariablenInfo.insertNumberConstant(numberOfSteps);
        stepsToJump.setVariablenReferenz(ref);
        stepsToJump.setText(stepsToJump.getText() + " " + numberOfSteps);
        jumpNode.addChild(stepsToJump);
        if (expression != null) jumpNode.addChild(dupFactory.dup(expression));
        return jumpNode;
    }

    /**
	 * Wandelt einen IF-Then Block in Jump Befehle um
	 * Eingef�gt wird als Kind die Anzahl der Elemente, die gesprungen wird
	 * Die Kommandos werden (Ohne COMMAND_Block) rechts eingef�gt.
	 * @param ifThen_block
	 */
    private void assembleIfThenToJump(NovaNode ifThen_block) {
        NovaNode nextStatementAfterIf = (NovaNode) ifThen_block.getNextSibling();
        NovaNode expression = (NovaNode) ifThen_block.getFirstChild();
        NovaNode command_block = (NovaNode) expression.getNextSibling();
        ifThen_block.deepInitialize(createJumpNode(expression, command_block.getNumberOfChildren() + 1, true));
        NovaNode firstCommandInThenBlock = (NovaNode) command_block.getFirstChild();
        ifThen_block.setNextSibling(firstCommandInThenBlock);
        firstCommandInThenBlock.addSiblingAtEnd(nextStatementAfterIf);
    }

    ;

    /**
	 * Wandelt einen IF-Then-Else Block in Jump Befehle um
     *	In If-Else Konstellation braucht es zwei Spr�nge, einen, um zu entscheiden, welcher Zweig gew�hlt wird
	 *	und einen unbedingten am Ende der ersten Teilschleife, um den zweiten ganz Zweig zu �berspringen
	 *	In den �brigen F�llen l�uft der Geradeausfluss (ohne Jump) den richtigen Weg
	 * @param ifThenElse_Block
	 */
    private void assembleIfThenElseToJump(NovaNode ifThenElse_Block) {
        NovaNode nextStatementAfterIf = (NovaNode) ifThenElse_Block.getNextSibling();
        NovaNode expression = (NovaNode) ifThenElse_Block.getFirstChild();
        NovaNode thencommand_block = (NovaNode) expression.getNextSibling();
        NovaNode elsecommand_block = (NovaNode) thencommand_block.getNextSibling();
        NovaNode firstJumpCommand = ifThenElse_Block;
        firstJumpCommand.deepInitialize(createJumpNode(expression, thencommand_block.getNumberOfChildren() + 2, true));
        NovaNode firstCommandInThenBlock = (NovaNode) thencommand_block.getFirstChild();
        firstJumpCommand.setNextSibling(firstCommandInThenBlock);
        NovaNode jumpAfterThenStatement = createJumpNode(null, elsecommand_block.getNumberOfChildren() + 1, false);
        firstCommandInThenBlock.addSiblingAtEnd(jumpAfterThenStatement);
        NovaNode firstCommandinElseblock = (NovaNode) elsecommand_block.getFirstChild();
        jumpAfterThenStatement.setNextSibling(firstCommandinElseblock);
        firstCommandinElseblock.addSiblingAtEnd(nextStatementAfterIf);
    }

    ;

    /**
	 * 
	 * @param while_loop
	 */
    private void assembleWhileLoop(NovaNode while_loop) {
        NovaNode nextStatementAfterWhile = (NovaNode) while_loop.getNextSibling();
        NovaNode expression = (NovaNode) while_loop.getFirstChild();
        NovaNode command_block = (NovaNode) expression.getNextSibling();
        NovaNode firstJumpCommand = while_loop;
        int nrOfCommand = command_block.getNumberOfChildren() + 2;
        firstJumpCommand.deepInitialize(createJumpNode(expression, nrOfCommand, true));
        NovaNode firstCommandInCommandBlock = (NovaNode) command_block.getFirstChild();
        firstJumpCommand.setNextSibling(firstCommandInCommandBlock);
        NovaNode secondJumpCommand = createJumpNode(null, -nrOfCommand, false);
        firstJumpCommand.addSiblingAtEnd(secondJumpCommand);
        secondJumpCommand.setNextSibling(nextStatementAfterWhile);
    }

    /**
	 * @param previousCommand an diesen Knoten werden Hilfszuweisungen angef�gt, die sich aus der Bedingung in der IF/While -Anweisung ergeben 
	 * @param ifOrWhile_block 
	 * 	@param previousCommand abgearbeitete Befehle
	 *     
	 * @pre (Am Beispiel IF-THEN): abgearbeitete Commands -->!!! # IF-BLOCK (expression, THEN_BLOCK) --> folgende Commands
	 * @post abgearbeitete Commands --> !!! expressions --> JMP THEN_BLOCK --> folgende Commands
	 * dabei steht !!! f�r Zeigerposition im Baum. Diese Methode ver�ndert nur den Baum, f�hrt aber keine Ausgaben durch. Daf�r wird der Baum in der aufrufenden Methode sp�ter erneut durchwandert
	 * 
	 */
    private void transformIFOrWhileBlock(NovaNode ifOrWhile_block, NovaNode previousCommand) {
        NovaNode expression = (NovaNode) ifOrWhile_block.getFirstChild();
        NovaNode thenAst = (NovaNode) expression.getNextSibling();
        NovaNode elseAst = (NovaNode) thenAst.getNextSibling();
        if (previousCommand == null) previousCommand = new NovaNode();
        previousCommand.setNextSibling(null);
        NovaNode followingCommand = (NovaNode) ifOrWhile_block.getNextSibling();
        if (thenAst.getType() != COMMAND_BLOCK) {
            NovaNode thenCommandBlock = new NovaNode(new CommonToken(COMMAND_BLOCK, "ThenComBlock"));
            thenCommandBlock.setFirstChild(dupFactory.dupTree(thenAst));
            thenAst = thenCommandBlock;
        }
        if (elseAst != null && elseAst.getType() != COMMAND_BLOCK) {
            NovaNode elseCommandBlock = new NovaNode(new CommonToken(COMMAND_BLOCK, "ElseComBlock"));
            elseCommandBlock.setFirstChild(dupFactory.dupTree(elseAst));
            elseAst = elseCommandBlock;
        }
        expression.setNextSibling(null);
        thenAst.setNextSibling(null);
        if (expression.getVariablenReferenz() == -1) {
            simplifyExpressionAndMoveInFront(expression, previousCommand);
        } else {
            insertConstIntoVariableList((NovaNode) expression);
        }
        transformBlock(thenAst, false);
        transformBlock(elseAst, false);
        previousCommand.addSiblingAtEnd(ifOrWhile_block);
        expression.setNextSibling(thenAst);
        thenAst.setNextSibling(elseAst);
        if (ifOrWhile_block.getType() == IFTHEN) {
            if (assemble) assembleIfThenToJump(ifOrWhile_block);
        } else if (ifOrWhile_block.getType() == IFELSE) {
            if (assemble) assembleIfThenElseToJump(ifOrWhile_block);
        } else if (ifOrWhile_block.getType() == WHILE) {
            if (assemble) assembleWhileLoop(ifOrWhile_block);
        } else {
            throw new RuntimeException("Unexpected type in assembling. Compiler error. Details : " + ifOrWhile_block.getText());
        }
    }

    public AST insertConstIntoVariableList(NovaNode node) {
        int type = node.getType();
        if ((type == STRING) || (type == NUMBER) || (type == SEQUENCE)) {
            insertConstants((NovaNode) node);
        }
        return node;
    }

    public AST insertVariableIntoVariableList(NovaNode node) {
        int type = node.getType();
        if (type == VARIABLE) {
            int ref = VariablenInfo.insertVariable(node.getAprioriTyp(), node.getText(), node.getLine(), node.getColumn());
            node.setVariablenReferenz(ref);
        }
        return node;
    }

    /**
	 * vereinfacht einen geschachtelten Ausdruck.
	 * Diese werden in primitive Ausdr�cke mit maximal zwei Parameter aufgel�st.
	 * Zwischenberechnungen werden in einer Queue gespeichert, die vor dem aktuellen Statement in den Baum eingebaut werden muss. 
	 * 
	 * @param currentExpression aktueller Ausdruck, der bearbeitet wird. Darf kein Statement sein
	 * @param helperAssignments Hieran werden Hilfsausdr�cke angeh�ngt 
	 */
    private void simplifyExpressionAndMoveInFront(NovaNode currentExpression, NovaNode helperAssignments) {
        if (currentExpression == null) return;
        insertConstIntoVariableList((NovaNode) currentExpression);
        simplifyExpressionAndMoveInFront((NovaNode) currentExpression.getFirstChild(), helperAssignments);
        if (currentExpression.getVariablenReferenz() == -1) {
            String varName = getTempVarName();
            NovaNode helperVariable = new NovaNode(new CommonToken(VARIABLE, varName));
            helperVariable.setAprioriTyp(currentExpression.getAprioriTyp());
            insertVariableIntoVariableList(helperVariable);
            NovaNode helperAssigment = new NovaNode();
            helperAssigment.addChild(helperVariable);
            helperAssigment.addChild(currentExpression.getFirstChild());
            helperAssigment.initialize(currentExpression);
            helperAssignments.addSiblingAtEnd(helperAssigment);
            currentExpression.setText(helperVariable.getText());
            currentExpression.setType(VARIABLE);
            currentExpression.setVariablenReferenz(helperVariable.getVariablenReferenz());
            currentExpression.setFirstChild(null);
        }
        simplifyExpressionAndMoveInFront((NovaNode) currentExpression.getNextSibling(), helperAssignments);
    }

    /**
	 * Transformation des gesamtes semantischen Baumes
	 * @param semanticTree NOVAScript-Knoten
	 * @return vereinfachter Baum
	 */
    public AST transform(AST semanticTree) {
        NovaNode copy = (NovaNode) dupFactory.dupList(semanticTree);
        transformBlock(copy, true);
        return copy;
    }

    /**
	 * Ausgabe eines Kommandos in die Console zum Debuggen. 
	 * @param command
	 */
    private void debugOutputCommand(NovaNode command) {
        System.out.print(nrCommandos + " : " + command.getText() + " ");
        System.out.print(Utils.byte2hex(Utils.convertSigned16BitIntToByteArray(command.getBytecodeNr())) + " ");
        System.out.print(Utils.byte2hex(Utils.convertSigned16BitIntToByteArray(command.getNumberOfChildren())) + " ");
        if (command.getFirstChild() != null) {
            NovaNode currentChild = (NovaNode) command.getFirstChild();
            do {
                System.out.print(" " + currentChild.getVariablenReferenz());
                currentChild = (NovaNode) currentChild.getNextSibling();
            } while (currentChild != null);
        }
        System.out.println();
    }

    ;

    private void writeCommand(NovaNode command) {
        if (debug) debugOutputCommand(command);
        try {
            byteOut.write(Utils.convertSigned16BitIntToByteArray(command.getBytecodeNr()));
            byteOut.write(Utils.convertSigned16BitIntToByteArray(command.getNumberOfChildren()));
            nrCommandos++;
            if (command.getFirstChild() != null) {
                NovaNode currentChild = (NovaNode) command.getFirstChild();
                do {
                    int varNr = currentChild.getVariablenReferenz();
                    byte[] varAsByteArray = Utils.convertSigned16BitIntToByteArray(varNr);
                    byteOut.write(varAsByteArray);
                    currentChild = (NovaNode) currentChild.getNextSibling();
                } while (currentChild != null);
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("Internal Compiler Error. IO Exception during compiling : " + e.getMessage());
        }
    }

    ;
}
