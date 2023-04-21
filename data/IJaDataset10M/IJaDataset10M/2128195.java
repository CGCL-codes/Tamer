package de.jmulti.vecm;

import javolution37.javolution.xml.XmlElement;
import javolution37.javolution.xml.XmlFormat;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.SymbolTable;
import com.jstatcom.project.ProjectState;
import com.jstatcom.ts.Selection;

/**
 * Represents a VECM state. Each state instance holds a symbol table with all
 * global symbols of a VECM frame, together with the current selection of the
 * <code>TSSel</code> component.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig </a>
 */
public final class VECMProjectState implements ProjectState {

    private SymbolTable symbolTableGlobal = null;

    private SymbolTable symbolTableSVEC = null;

    private Selection selection = null;

    private JSCNArray detSelection = null;

    private JSCNArray ecSelection = null;

    private JSCNArray stratSelection = null;

    /**
     * XML format field for (de)serialization.
     */
    public static final XmlFormat<VECMProjectState> VECMProjectState_XML = new XmlFormat<VECMProjectState>(VECMProjectState.class) {

        public void format(VECMProjectState data, XmlElement xml) {
            xml.add(data.symbolTableGlobal);
            xml.add(data.selection);
            xml.add(data.detSelection);
            xml.add(data.symbolTableSVEC);
            xml.add(data.ecSelection);
            xml.add(data.stratSelection);
        }

        public VECMProjectState parse(XmlElement xml) {
            VECMProjectState state = new VECMProjectState();
            if (xml.hasNext()) {
                SymbolTable table = (SymbolTable) xml.getNext();
                state.setSymbolTableGlobal(table);
            }
            if (xml.hasNext()) {
                Selection sel = (Selection) xml.getNext();
                if (sel != null) state.setSelection(sel);
            }
            if (xml.hasNext()) {
                JSCNArray detSel = (JSCNArray) xml.getNext();
                if (detSel != null) state.setDetSelection(detSel);
            }
            if (xml.hasNext()) {
                SymbolTable SVECTable = (SymbolTable) xml.getNext();
                if (SVECTable != null) state.setSymbolTableSVEC(SVECTable);
            }
            if (xml.hasNext()) {
                JSCNArray ecSel = (JSCNArray) xml.getNext();
                if (ecSel != null) state.setEcSelection(ecSel);
            }
            if (xml.hasNext()) {
                JSCNArray procSel = (JSCNArray) xml.getNext();
                if (procSel != null) state.setStratSelection(procSel);
            }
            return state;
        }
    };

    public String getHandlerID() {
        return VECM.class.getName();
    }

    /**
     * Gets the global symbol table stored in this state.
     * 
     * @return symbol table with global symbols
     */
    public SymbolTable getSymbolTableGlobal() {
        return symbolTableGlobal;
    }

    /**
     * Sets the global symbol table to be stored in this state. The argument is
     * not copied but just the reference is used for performance reasons.
     * 
     * @param symbolTableGlobal
     *            reference to the current global table of the VAR module
     * @throws IllegalArgumentException
     *             <code>if (symbolTableGlobal == null)</code>
     */
    public void setSymbolTableGlobal(SymbolTable symbolTableGlobal) {
        if (symbolTableGlobal == null) throw new IllegalArgumentException("Argument was null.");
        this.symbolTableGlobal = symbolTableGlobal;
    }

    /**
     * Gets the selection instance that represents the selection state of the
     * underlying <code>TSSel</code> component.
     * 
     * @return the selection state for a <code>TSSel</code> object
     */
    public Selection getSelection() {
        return selection;
    }

    /**
     * Sets the selection instance to be stored in this state object.
     * 
     * @param selection
     *            <code>TSSel</code> selection state
     * @throws IllegalArgumentException
     *             <code>if (selection == null)</code>
     */
    public void setSelection(Selection selection) {
        if (selection == null) throw new IllegalArgumentException("Argument was null.");
        this.selection = selection;
    }

    /**
     * Gets the 3x1 array indicating the selection of: constant-trend-seasonal
     * dummies.
     * 
     * @return selection array
     */
    public JSCNArray getDetSelection() {
        return detSelection;
    }

    /**
     * Sets the 3x1 array indicating the selection of: constant-trend-seasonal
     * dummies.
     * 
     * @param detSelection
     *            selection array
     * @throws IllegalArgumentException
     *             <code>if (detSelection == null)</code>
     */
    public void setDetSelection(JSCNArray detSelection) {
        if (detSelection == null) throw new IllegalArgumentException("Argument was null.");
        this.detSelection = detSelection;
    }

    /**
     * Gets the SVEC symbol table stored in this state.
     * 
     * @return symbol table with SVEC symbols
     */
    public SymbolTable getSymbolTableSVEC() {
        return symbolTableSVEC;
    }

    /**
     * Sets the SVEC symbol table to be stored in this state. The argument is
     * not copied but just the reference is used for performance reasons.
     * 
     * @param symbolTableSVEC
     *            reference to the current global table of the VAR module
     * @throws IllegalArgumentException
     *             <code>if (symbolTableSVEC == null)</code>
     */
    public void setSymbolTableSVEC(SymbolTable symbolTableSVEC) {
        if (symbolTableSVEC == null) throw new IllegalArgumentException("Argument was null.");
        this.symbolTableSVEC = symbolTableSVEC;
    }

    /**
     * @return
     */
    public JSCNArray getEcSelection() {
        return ecSelection;
    }

    /**
     * @param ecSelection
     */
    public void setEcSelection(JSCNArray ecSelection) {
        if (ecSelection == null) throw new IllegalArgumentException("Argument was null.");
        this.ecSelection = ecSelection;
    }

    /**
     * @return
     */
    public JSCNArray getStratSelection() {
        return stratSelection;
    }

    /**
     * @param procSelection
     */
    public void setStratSelection(JSCNArray procSelection) {
        if (procSelection == null) throw new IllegalArgumentException("Argument was null.");
        this.stratSelection = procSelection;
    }
}
