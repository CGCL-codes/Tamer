package it.univaq.di.chameleonserver.abstractanalyzer.aca;

import it.univaq.di.chameleonserver.abstractanalyzer.utility.LogServices;
import it.univaq.di.chameleonserver.abstractanalyzer.utility.XMLUtility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import soot.SootMethod;
import soot.coffi.CoffiMethodSource;
import soot.coffi.Instruction;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTControlFlowNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;

/**
 * La classe memorizza tutte le informazioni riguardanti l'ARAMethod
 * 
 * @author Adriano Felicione
 *
 */
public class ARAMethod {

    /**
	 * nome della classe
	 */
    private String nomeClass;

    /**
	 * nome del metodo
	 */
    private String nomeMetodo;

    /**
	 * signature del metodo
	 */
    private String Signature;

    /**
	 * radice dell'AST
	 */
    private ASTNode firstNode;

    /**
	 * lista istruzioni bytecode del metodo
	 */
    private TreeMap<Integer, ARAInstruction> instructionList;

    private boolean printedCode = false;

    private boolean alreadyAnalyzed = false;

    private boolean isAbstract = false;

    /**
	 * crea una nuova istanza del ARAMethod
	 * 
	 * @param classe a cui appartiene il metodo
	 * @param pos posizione del metodo all'interno della classe
	 */
    public ARAMethod(ARAClass classe, int pos) {
        Logger log = LogServices.getLogger(LogServices.logType.ACA);
        SootMethod sMet = (SootMethod) classe.getSootClass().getMethods().get(pos);
        nomeClass = classe.getName();
        nomeMetodo = sMet.getName();
        Signature = sMet.getSubSignature();
        log.debug("ARAMethod: " + nomeMetodo + " - Signature: " + Signature);
        isAbstract = sMet.isAbstract();
        firstNode = makeFirstNode(sMet);
        instructionList = makeInstructionList(sMet);
        if (firstNode != null) {
            findLostInstruction(firstNode);
            checkAllInstruction();
        }
    }

    /**
	 * crea il primo nodo dell'AST 
	 * 
	 * @param sMet il metodo di soot che contiene l'albero
	 * 
	 * @return un AST
	 */
    private ASTNode makeFirstNode(SootMethod sMet) {
        Logger logACA = LogServices.getLogger(LogServices.logType.ACA);
        if (isAbstract) return null;
        DavaBody dBody = null;
        try {
            JimpleBody jb = (JimpleBody) sMet.retrieveActiveBody();
            GrimpBody gb = Grimp.v().newBody(jb, "gb");
            dBody = Dava.v().newBody(gb);
        } catch (Exception e) {
            logACA.error("************************************");
            logACA.error("* ERRORE in SOOT !");
            logACA.error("* Class  " + nomeClass);
            logACA.error("* Method " + Signature);
            logACA.error(e.toString());
            logACA.error("************************************");
            System.exit(1);
        }
        return (ASTNode) dBody.getUnits().getFirst();
    }

    /**
	 * 
	 * @return la radice dell'AST
	 */
    public ASTNode getFirstNode() {
        return this.firstNode;
    }

    /**
	 * crea la lista delle istruzioni in bytecode
	 * 
	 * @param sMet metodo da cui estrarre la lista di istr
	 * @return la lista delle istruzioni in bytecode
	 */
    private TreeMap<Integer, ARAInstruction> makeInstructionList(SootMethod sMet) {
        TreeMap<Integer, ARAInstruction> iList = new TreeMap<Integer, ARAInstruction>();
        Instruction inst = ((CoffiMethodSource) sMet.getSource()).coffiMethod.instructions;
        while (inst != null) {
            ARAInstruction aInst = new ARAInstruction(inst, sMet);
            iList.put(new Integer(inst.label), aInst);
            if (aInst.getCombinedInstruction() != null) {
                for (int x = 0; x < aInst.getCombinedInstruction().length; x++) {
                    iList.get(aInst.getCombinedInstruction()[x]).setToEvalute(false);
                }
            }
            inst = inst.next;
        }
        return iList;
    }

    /**
	 * 
	 * @return la signature del metodo
	 */
    public String getSignature() {
        String signature;
        if (Signature.startsWith("void <init>(")) {
            signature = "void [init](" + Signature.substring(12);
        } else {
            signature = Signature;
        }
        return signature;
    }

    /**
	 * 
	 * @return il nome del metodo
	 */
    public String getNomeMetodo() {
        return XMLUtility.formatForXML(nomeMetodo);
    }

    /**
	 * 
	 * @return il nome completo del metodo
	 */
    public String getNomeMetodoCompleto() {
        return getNomeClasseAssociata() + "." + getSignature();
    }

    /**
	 * 
	 * @return la lista delle istruzioni bytecode per la sua stampa
	 */
    public String getCodice() {
        if (printedCode) {
            return "";
        }
        printedCode = true;
        if (this.firstNode == null) {
            return "";
        }
        String lineSep = System.getProperty("line.separator");
        String codice = "***********************************" + lineSep + "* " + getNomeMetodoCompleto() + lineSep + "***********************************" + lineSep + lineSep;
        Iterator<Integer> it = this.instructionList.keySet().iterator();
        while (it.hasNext()) {
            Integer key = it.next();
            codice = codice + this.instructionList.get(key).toStringComplete() + lineSep;
        }
        codice = codice + lineSep + lineSep;
        return codice;
    }

    /**
	 * 
	 * @return il nome della classe associata
	 */
    public String getNomeClasseAssociata() {
        return this.nomeClass;
    }

    /**
	 * restituisce una lista con le istruzioni richieste
	 * @param first prima istruzione della lista
	 * @param last ultima istruzione della lista
	 * @return la lista della istruzioni
	 */
    public ArrayList<ARAInstruction> getSubsetInstruction(Integer first, Integer last, boolean bound) {
        ArrayList<ARAInstruction> instructions = new ArrayList<ARAInstruction>();
        Iterator<Integer> it = instructionList.subMap(first, bound, last, bound).keySet().iterator();
        while (it.hasNext()) {
            instructions.add(instructionList.get((Integer) it.next()));
        }
        return instructions;
    }

    /**
	 * restituisce la posizione di una particolare istruzione
	 * @param tag lista di tag in cui cercare 
	 * @param val stringa da cercare 
	 * @return la posizione
	 */
    public int getPosOf(List<?> tag, String val) {
        int valRet = 0;
        for (int x = 0; x < tag.size(); x++) {
            Integer pos = new Integer(tag.get(x).toString());
            if (instructionList.get(pos).getNome().startsWith(val)) {
                valRet = instructionList.get(pos).getPosizione();
            }
        }
        return valRet;
    }

    /**
	 * restituisce i bound del ciclo lopp
	 * @param tag lista dei tag
	 * @return una stringa del bound del tag
	 */
    public String getLoopBound(List<?> tag) {
        String valRet = "";
        for (int x = 0; x < tag.size(); x++) {
            Integer pos = new Integer(tag.get(x).toString());
            if (instructionList.get(pos).getNome().startsWith("if")) {
                int b1 = instructionList.get(pos).getPosizione();
                int b2 = Integer.parseInt(instructionList.get(pos).getParam().substring(3));
                if (b1 > b2) {
                    valRet = "#loop_" + b2 + "_" + b1 + "#";
                } else {
                    valRet = "#loop_" + b1 + "_" + b2 + "#";
                }
            }
        }
        return valRet;
    }

    /**
	 * restituisce l'istruzione identifica dell'etichetta
	 * 
	 * @param label da cerca
	 * @return ARAIstruction
	 */
    public ARAInstruction getInstruction(Integer label) {
        return instructionList.get(label);
    }

    /**
	 * cerca tutte le istruzioni che sono agganciate a nodi AST
	 * 
	 * @param node radice
	 * 
	 */
    private void findLostInstruction(ASTNode node) {
        if (node instanceof ASTStatementSequenceNode) {
            List<?> stat = ((ASTStatementSequenceNode) node).getStatements();
            for (int j = 0; j < stat.size(); j++) {
                Stmt s = ((AugmentedStmt) stat.get(j)).get_Stmt();
                findLostInstruction(s.getTags(), s);
            }
        } else if (node instanceof ASTControlFlowNode) {
            findLostInCondition(((ASTControlFlowNode) node).get_Condition());
            findLostInSubBody(node.get_SubBodies());
        } else if (node instanceof ASTSwitchNode) {
            findLostInstruction(node.getTags(), node);
            findLostInSubBody(node.get_SubBodies());
        } else if (node instanceof ASTTryNode) {
            findLostInSubNode(((ASTTryNode) node).get_TryBody());
            List<?> cl = ((ASTTryNode) node).get_CatchList();
            for (int x = 0; x < cl.size(); x++) {
                findLostInSubNode((List<?>) ((ASTTryNode.container) cl.get(x)).o);
            }
        } else {
            findLostInSubBody(node.get_SubBodies());
        }
    }

    /**
	 * cerca tutte le istruzione aggangiate al subNode AST
	 * 
	 * @param nodeList lista di nodi
	 */
    private void findLostInSubBody(List<?> nodeList) {
        for (int x = 0; x < nodeList.size(); x++) {
            findLostInSubNode((List<?>) nodeList.get(x));
        }
    }

    /**
	 * cerca tutte le istruzioni agganciate alla lista di nodi
	 *
	 * @param nodeList lista di nodi
	 */
    private void findLostInSubNode(List<?> nodeList) {
        for (int x = 0; x < nodeList.size(); x++) {
            findLostInstruction((ASTNode) nodeList.get(x));
        }
    }

    /**
	 * cerca tutte le istruzioni agganciate alla nodo Condition
	 * 
	 * @param node nodo AST condition
	 */
    private void findLostInCondition(ASTCondition node) {
        if (node instanceof ASTAggregatedCondition) {
            findLostInCondition(((ASTAggregatedCondition) node).getLeftOp());
            findLostInCondition(((ASTAggregatedCondition) node).getRightOp());
        } else {
            findLostInstruction(node.getTags(), node);
        }
    }

    /**
	 * cerca tutte le istruzioni agganciate al nodo Statement
	 * 
	 * @param listaTag lista Tag
	 * @param node nodo
	 */
    private void findLostInstruction(List<?> listaTag, Object node) {
        for (int k = 0; k < listaTag.size(); k++) {
            Tag t = (Tag) listaTag.get(k);
            ARAInstruction instr = getInstruction(Integer.parseInt(t.toString()));
            instr.setASTNode(node);
        }
    }

    /**
	 * verifica se ogni istruzione � agganciata ad un nodo del AST
	 * 
	 */
    private void checkAllInstruction() {
        Iterator<Integer> it = instructionList.keySet().iterator();
        while (it.hasNext()) {
            ARAInstruction instruc = instructionList.get((Integer) it.next());
            if (instruc.getASTNode() == null) {
                Iterator<Integer> iterInstr = instructionList.subMap(0, true, instruc.getPosizione(), false).descendingKeySet().iterator();
                while (iterInstr.hasNext()) {
                    ARAInstruction prevInstr = instructionList.get(iterInstr.next());
                    if (prevInstr.getASTNode() != null) {
                        Object node = prevInstr.getASTNode();
                        instruc.setASTNode(node);
                        BytecodeOffsetTag tag = new BytecodeOffsetTag(instruc.getPosizione());
                        if (node instanceof ASTCondition) {
                            ((ASTCondition) node).addTag(tag);
                        } else if (node instanceof Stmt) {
                            ((Stmt) node).addTag(tag);
                        }
                        break;
                    }
                }
            }
        }
    }

    public boolean getAlreadyAnalyzed() {
        return alreadyAnalyzed;
    }

    public void setAlreadyAnalyzed(boolean newState) {
        alreadyAnalyzed = newState;
    }

    /**
	 * 
	 * @return true if the method is abstract
	 */
    public boolean isAbstract() {
        return isAbstract;
    }
}
