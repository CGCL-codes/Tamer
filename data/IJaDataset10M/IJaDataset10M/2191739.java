package org.python.compiler.advanced;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class ScopeInformation {

    private static class Undecided {

        private boolean global = true;

        private final String name;

        private Collection<ScopeInformation> subscribers = new LinkedList<ScopeInformation>();

        Undecided(String name, ScopeInformation info) {
            this.name = name;
            addListener(info);
        }

        void addListener(ScopeInformation info) {
            if (subscribers != null) {
                subscribers.add(info);
            } else if (global) {
                info.global.add(name);
            } else {
            }
        }

        void global() {
            for (ScopeInformation info : subscribers) {
                info.global.add(name);
            }
            subscribers = null;
        }

        void closure() {
            for (ScopeInformation info : subscribers) {
                info.free.add(name);
            }
            subscribers = null;
        }
    }

    private static Iterable<Undecided> getAllUndecidedIn(Iterable<ScopeInformation> infos) {
        List<Undecided> undecided = new LinkedList<Undecided>();
        for (ScopeInformation info : infos) {
            undecided.addAll(info.undecided());
        }
        return undecided;
    }

    private Collection<Undecided> undecided;

    private final Set<String> global;

    private final Set<String> local;

    private final Set<String> free;

    private final Set<String> cell;

    private ScopeInformation(Set<String> global, Set<String> local, Set<String> free, Set<String> cell) {
        this.global = global;
        this.local = local;
        this.free = free;
        this.cell = cell;
    }

    private Collection<Undecided> undecided() {
        if (undecided != null) {
            return undecided;
        } else {
            return Collections.emptyList();
        }
    }

    public static ScopeInformation makeGlobalScope(String[] locals, String[] cells, boolean hasStarImport, List<ScopeInformation> children) {
        for (Undecided undecided : getAllUndecidedIn(children)) {
            undecided.global();
        }
        Set<String> local = new HashSet<String>(Arrays.asList(locals));
        Set<String> cell = new HashSet<String>(Arrays.asList(cells));
        local.removeAll(cell);
        return new GlobalScope(local, cell);
    }

    public static ScopeInformation makeClassScope(String name, String[] locals, String[] explicitlyGlobal, String[] explicitlyClosure, String[] free, String[] scopeRequired, boolean hasStarImport, List<ScopeInformation> children) {
        return new ClassScope(name, locals, explicitlyGlobal, explicitlyClosure, free, scopeRequired, hasStarImport, children);
    }

    public static ScopeInformation makeFunctionScope(String name, String[] parameters, String[] locals, String[] explicitlyGlobal, String[] explicitlyClosure, String[] free, String[] scopeRequired, boolean isGenerator, boolean hasStarImport, List<ScopeInformation> children) {
        return new FunctionScope(name, parameters, locals, explicitlyGlobal, explicitlyClosure, free, scopeRequired, isGenerator, hasStarImport, children);
    }

    protected String string;

    private static class GlobalScope extends ScopeInformation {

        GlobalScope(String[] locals, String[] cell, boolean hasStarImport, List<ScopeInformation> children) {
            super(null, null, null, null);
            this.string = "Module[locals=" + Arrays.toString(locals) + ", cell=" + Arrays.toString(cell) + ", starImport=" + hasStarImport + "]\n";
        }

        public GlobalScope(Set<String> local, Set<String> cell) {
            super(Collections.<String>emptySet(), local, Collections.<String>emptySet(), cell);
        }
    }

    private static class ClassScope extends ScopeInformation {

        ClassScope(String name, String[] locals, String[] explicitlyGlobal, String[] explicitlyClosure, String[] free, String[] scopeRequired, boolean hasStarImport, List<ScopeInformation> children) {
            super(null, null, null, null);
            this.string = "Class[name=" + name + ", locals=" + Arrays.toString(locals) + ", globals=" + Arrays.toString(explicitlyGlobal) + ", nonlocals=" + Arrays.toString(explicitlyClosure) + ", free=" + Arrays.toString(free) + ", required=" + Arrays.toString(scopeRequired) + ", starImport=" + hasStarImport + "]\n";
        }
    }

    private static class FunctionScope extends ScopeInformation {

        FunctionScope(String name, String[] parameters, String[] locals, String[] explicitlyGlobal, String[] explicitlyClosure, String[] free, String[] scopeRequired, boolean isGenerator, boolean hasStarImport, List<ScopeInformation> children) {
            super(null, null, null, null);
            this.string = "Function[name=" + name + ", parameters=" + Arrays.toString(parameters) + ", locals=" + Arrays.toString(locals) + ", globals=" + Arrays.toString(explicitlyGlobal) + ", nonlocals=" + Arrays.toString(explicitlyClosure) + ", free=" + Arrays.toString(free) + ", required=" + Arrays.toString(scopeRequired) + ", generator=" + isGenerator + ", starImport=" + hasStarImport + "]\n";
        }
    }

    @Override
    public String toString() {
        return string;
    }

    public Iterable<String> freeVariables() {
        return Collections.unmodifiableSet(free);
    }

    public boolean isGenerator() {
        return false;
    }

    public boolean isModule() {
        return false;
    }
}
