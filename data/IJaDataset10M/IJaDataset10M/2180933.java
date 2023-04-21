package net.sourceforge.freejava.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.freejava.io.term.AbstractTerminal;
import net.sourceforge.freejava.io.term.ITerminal;
import net.sourceforge.freejava.sio.IPrintOut;
import net.sourceforge.freejava.util.exception.NotImplementedException;

public class MultiplexedTerminal extends AbstractTerminal {

    private List<ITerminal> terminals;

    public MultiplexedTerminal(ITerminal... terminals) {
        this.terminals = new ArrayList<ITerminal>(terminals.length);
        for (ITerminal t : terminals) this.terminals.add(t);
    }

    public boolean add(ITerminal terminal) {
        return terminals.add(terminal);
    }

    public boolean remove(ITerminal terminal) {
        return terminals.remove(terminal);
    }

    @Override
    public void f(String format, Object... args) {
        for (ITerminal t : terminals) t.f(format, args);
    }

    public void n(String s) {
        for (ITerminal t : terminals) t.p_(s);
    }

    public void p() {
        for (ITerminal t : terminals) t.p();
    }

    @Override
    public void p(String s) {
        for (ITerminal t : terminals) t.p(s);
    }

    @Override
    public void p_(String s) {
        for (ITerminal t : terminals) t.p_(s);
    }

    @Override
    public void flush() throws IOException {
        for (ITerminal t : terminals) t.flush();
    }

    @Override
    public IPrintOut getCharOut() {
        throw new NotImplementedException();
    }

    @Override
    public PrintStream getPrintStream() {
        throw new NotImplementedException();
    }

    @Override
    public Writer getWriter() {
        throw new NotImplementedException();
    }
}
