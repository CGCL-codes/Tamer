package org.xteam.cs.grm.model;

import java.util.ArrayList;
import java.util.List;

public class Grammar {

    private String name;

    private List<Symbol> symbols = new ArrayList<Symbol>();

    private List<Rule> rules = new ArrayList<Rule>();

    private NonTerminal start;

    public Grammar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public NonTerminal getStart() {
        return start;
    }

    public void setStart(NonTerminal nt) {
        this.start = nt;
    }
}
