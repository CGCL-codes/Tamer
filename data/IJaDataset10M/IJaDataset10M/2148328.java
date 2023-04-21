package de.srcml.dom;

import java.util.Iterator;
import org.dom4j.*;

/**
 * Base class for SrcML Switches.
 *
 * @author Frank Raiser
 */
public class Switch extends Statement {

    private static java.util.logging.Logger logger = de.srcml.Logger.getLogger();

    protected Switch() {
        super(Switch.getSrcMLTagName());
    }

    protected Switch(QName f_name) {
        super(f_name);
    }

    protected Switch(QName f_name, int f_attrCount) {
        super(f_name, f_attrCount);
    }

    protected Switch(String f_name) {
        super(f_name);
    }

    protected Switch(String f_name, Namespace f_ns) {
        super(f_name, f_ns);
    }

    protected Switch(Element f_el) {
        super(f_el);
    }

    protected void createCFG(CFG f_cfg, CFGNode f_start, CFGNode f_end) {
        f_start.addDOMNode(this);
        Argument arg = getArgumentNode();
        CFGNode argend = new CFGNode(f_cfg);
        arg.createCFG(f_cfg, f_start, argend);
        Cases cases = getCasesNode();
        cases.createCFG(f_cfg, argend, f_end);
        Iterator it = selectNodes(".//" + Break.getSrcMLTagName()).iterator();
        while (it.hasNext()) {
            Break brk = (Break) it.next();
            if (brk.getBreakStructure() == this) {
                CFGNode node = f_cfg.getCFGNode(brk);
                if (node == null) {
                    System.err.println("Missing CFGNode for break: " + brk);
                    continue;
                }
                node.addSuccessor(f_end);
            }
        }
    }

    /**
     * Returns the Argument node for this switch.
     *
     * @return the Argument node for this switch.
     */
    public Argument getArgumentNode() {
        Node n = selectSingleNode(Argument.getSrcMLTagName());
        if (n instanceof Argument) {
            return (Argument) n;
        }
        logger.warning("Illegal Switch node: Missing Argument node!");
        return null;
    }

    /**
     * Returns the Cases node of this switch.
     *
     * @return the Cases node of this switch.
     */
    public Cases getCasesNode() {
        Node n = selectSingleNode(Cases.getSrcMLTagName());
        if (n instanceof Cases) {
            return (Cases) n;
        }
        logger.warning("Illegal Switch node: Missing Cases node!");
        return null;
    }

    public static String getSrcMLTagName() {
        return "switch";
    }

    public boolean isBranching() {
        return true;
    }
}
