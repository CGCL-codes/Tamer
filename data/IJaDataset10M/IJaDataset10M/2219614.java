package edu.clemson.cs.r2jt.location;

import edu.clemson.cs.r2jt.collections.Iterator;
import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.collections.Stack;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.ModuleID;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.data.Symbol;
import edu.clemson.cs.r2jt.entry.*;
import edu.clemson.cs.r2jt.errors.ErrorHandler;
import edu.clemson.cs.r2jt.init.Environment;
import edu.clemson.cs.r2jt.scope.*;
import edu.clemson.cs.r2jt.type.*;

public class ProofLocator {

    private ErrorHandler err;

    private SymbolTable table;

    private TypeMatcher tm;

    public ProofLocator(SymbolTable table, TypeMatcher tm, ErrorHandler err) {
        this.table = table;
        this.tm = tm;
        this.err = err;
    }

    public ProofEntry locateProof(PosSymbol name) throws SymbolSearchException {
        List<ProofEntry> proofs = locateProofsInStack(name);
        if (proofs.size() == 0) {
            proofs = locateProofsInImports(name);
        }
        if (proofs.size() > 1) {
            List<Location> locs = getLocationList(proofs);
            String msg = ambigProofRefMessage(name.toString(), locs.toString());
            err.error(name.getLocation(), msg);
            throw new SymbolSearchException();
        } else if (proofs.size() == 0) {
            String msg = cantFindProofMessage(name.toString());
            err.error(name.getLocation(), msg);
            throw new SymbolSearchException();
        } else {
            return proofs.get(0);
        }
    }

    public ProofEntry locateProof(PosSymbol qual, PosSymbol name) throws SymbolSearchException {
        if (qual == null) {
            return locateProof(name);
        }
        QualifierLocator qlocator = new QualifierLocator(table, err);
        ModuleScope scope;
        scope = qlocator.locateMathModule(qual);
        if (scope.containsProof(name.getSymbol())) {
            ProofEntry p = scope.getProof(name.getSymbol());
            return p;
        } else {
            String msg = cantFindProofInModMessage(name.toString(), qual.toString());
            err.error(qual.getLocation(), msg);
            throw new SymbolSearchException();
        }
    }

    private List<ProofEntry> locateProofsInStack(PosSymbol name) throws SymbolSearchException {
        List<ProofEntry> proofs = new List<ProofEntry>();
        Stack<Scope> stack = table.getStack();
        Stack<Scope> hold = new Stack<Scope>();
        try {
            while (!stack.isEmpty()) {
                Scope scope = stack.pop();
                hold.push(scope);
                if (scope instanceof ModuleScope) {
                    ModuleScope mscope = (ModuleScope) scope;
                    if (mscope.containsProof(name.getSymbol())) {
                        proofs.add(mscope.getProof(name.getSymbol()));
                    }
                }
            }
            return proofs;
        } finally {
            while (!hold.isEmpty()) {
                stack.push(hold.pop());
            }
        }
    }

    private List<ProofEntry> locateProofsInImports(PosSymbol name) throws SymbolSearchException {
        List<ProofEntry> proofs = new List<ProofEntry>();
        Iterator<ModuleScope> i = table.getModuleScope().getMathVisibleModules();
        while (i.hasNext()) {
            ModuleScope iscope = i.next();
            if (iscope.containsProof(name.getSymbol())) {
                proofs.add(iscope.getProof(name.getSymbol()));
            }
        }
        return proofs;
    }

    private List<Location> getLocationList(List<ProofEntry> entries) {
        List<Location> locs = new List<Location>();
        Iterator<ProofEntry> i = entries.iterator();
        while (i.hasNext()) {
            ProofEntry entry = i.next();
            locs.add(entry.getLocation());
        }
        return locs;
    }

    private String cantFindProofInModMessage(String name, String module) {
        return "Cannot find a proof named " + name + " in module " + module + ".";
    }

    private String cantFindProofMessage(String name) {
        return "Cannot find a proof named " + name + ".";
    }

    private String ambigProofRefMessage(String name, String mods) {
        return "The proof named " + name + " is found in more than one " + "module visible from this scope: " + mods + ".";
    }
}
