package edu.mit.lcs.haystack.adenine.interpreter;

import edu.mit.lcs.haystack.adenine.query.DefaultQueryEngine;
import edu.mit.lcs.haystack.adenine.query.IQueryEngine;
import edu.mit.lcs.haystack.proxy.IServiceAccessor;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.security.Identity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public class DynamicEnvironment extends Environment {

    public DynamicEnvironment() {
        setOutput(new PrintWriter(System.out, true));
        setInput(new BufferedReader(new InputStreamReader(System.in)));
        setQueryEngine(new DefaultQueryEngine());
        setIdentity(null);
    }

    public DynamicEnvironment(IRDFContainer rdfc) {
        this();
        setSource(rdfc);
        setTarget(rdfc);
    }

    public DynamicEnvironment(IRDFContainer rdfc, IServiceAccessor sa) {
        this(rdfc);
        setServiceAccessor(sa);
    }

    /**
	 * @see Object#clone()
	 */
    public Object clone() {
        DynamicEnvironment e = new DynamicEnvironment();
        e.m_bindings = (HashMap) m_bindings.clone();
        return e;
    }

    public Message getMessageIfAny() {
        if (isBound("__message__")) {
            return (Message) getValue("__message__");
        } else {
            return null;
        }
    }

    public IQueryEngine getQueryEngine() {
        return (IQueryEngine) getValue("__queryengine__");
    }

    public IRDFContainer getSource() {
        return (IRDFContainer) getValue("__source__");
    }

    public IRDFContainer getTarget() {
        return (IRDFContainer) getValue("__target__");
    }

    public IRDFContainer getInstructionSource() {
        return (IRDFContainer) getValue("__instructionsource__");
    }

    public IServiceAccessor getServiceAccessor() {
        return (IServiceAccessor) getValue("__serviceaccessor__");
    }

    public PrintWriter getOutput() {
        return (PrintWriter) getValue("__output__");
    }

    public BufferedReader getInput() {
        return (BufferedReader) getValue("__input__");
    }

    public Identity getIdentity() {
        return (Identity) getValue("__identity__");
    }

    public void setQueryEngine(IQueryEngine qe) {
        setCell("__queryengine__", new Cell(qe));
    }

    public void setTarget(IRDFContainer rdfc) {
        setCell("__target__", new Cell(rdfc));
    }

    public void setSource(IRDFContainer rdfc) {
        setCell("__source__", new Cell(rdfc));
    }

    public void setInstructionSource(IRDFContainer rdfc) {
        setCell("__instructionsource__", new Cell(rdfc));
    }

    public void setInput(BufferedReader input) {
        setCell("__input__", new Cell(input));
    }

    public void setOutput(PrintWriter output) {
        setCell("__output__", new Cell(output));
    }

    public void setServiceAccessor(IServiceAccessor sa) {
        setCell("__serviceaccessor__", new Cell(sa));
    }

    public void setIdentity(Identity id) {
        setCell("__identity__", new Cell(id));
    }

    public void setMessage(Message msg) {
        setCell("__message__", new Cell(msg));
    }

    public void setMessageIfAny(Message msg) {
        if (msg != null) {
            setMessage(msg);
        }
    }
}
