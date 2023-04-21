package net.sf.openforge.backend.timedc;

import java.util.*;
import java.io.*;
import net.sf.openforge.app.*;
import net.sf.openforge.app.project.*;
import net.sf.openforge.lim.*;
import net.sf.openforge.lim.io.*;
import net.sf.openforge.lim.memory.*;
import net.sf.openforge.lim.op.*;
import net.sf.openforge.util.naming.*;

/**
 * This class is responsible for finding all the nodes in the LIM
 * which maintain any sort of state.  Effectively this is all
 * nodes whose latency is greater than 0.  A unique OpHandle
 * subclass is created for each of these nodes which knows how to
 * handle declaration and updating of state.  
 */
class StateComponentFinder extends DefaultVisitor {

    private CNameCache nameCache;

    private Map referenceableMap;

    /**
     * A Map of component to StateVar.
     */
    private Map seqElements = new LinkedHashMap();

    StateComponentFinder(CNameCache cache, Map refMap) {
        super();
        setTraverseComposable(true);
        this.nameCache = cache;
        this.referenceableMap = refMap;
    }

    Map getSeqElements() {
        return Collections.unmodifiableMap(this.seqElements);
    }

    public void visit(Design design) {
        for (Iterator iter = design.getResetPins().iterator(); iter.hasNext(); ) {
            GlobalReset grst = (GlobalReset) iter.next();
            final File[] inputFiles = EngineThread.getGenericJob().getTargetFiles();
            int delay = 5;
            if (ForgeFileTyper.isXLIMSource(inputFiles[0].getName())) delay = 10;
            this.seqElements.put(grst, new ResetVar(grst, this.nameCache, delay));
        }
        for (Iterator iter = design.getDesignModule().getComponents().iterator(); iter.hasNext(); ) {
            try {
                ((Visitable) iter.next()).accept(this);
            } catch (UnexpectedVisitationException uve) {
            }
        }
    }

    public void visit(Reg comp) {
        super.visit(comp);
        this.seqElements.put(comp, new RegVar(comp, this.nameCache));
    }

    public void visit(MemoryRead comp) {
        super.visit(comp);
        MemoryVar memVar = (MemoryVar) this.referenceableMap.get(comp.getMemoryPort().getLogicalMemory());
        this.seqElements.put(comp, new MemAccessVar(comp, memVar, this.nameCache));
    }

    public void visit(MemoryWrite comp) {
        super.visit(comp);
        MemoryVar memVar = (MemoryVar) this.referenceableMap.get(comp.getMemoryPort().getLogicalMemory());
        this.seqElements.put(comp, new MemAccessVar(comp, memVar, this.nameCache));
    }

    public void visit(RegisterWrite comp) {
        Register target = (Register) comp.getReferenceable();
        assert target != null : "null target of " + comp;
        RegisterVar registerVar = (RegisterVar) this.referenceableMap.get(target);
        if (registerVar == null) {
            registerVar = new RegisterVar(target);
            this.referenceableMap.put(target, registerVar);
        }
        RegWriteVar writeVar = new RegWriteVar(comp, registerVar, this.nameCache);
        this.seqElements.put(comp, writeVar);
    }

    public void visit(SRL16 comp) {
        super.visit(comp);
        this.seqElements.put(comp, new SRL16Var(comp, this.nameCache));
    }
}
