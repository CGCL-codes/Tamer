package com.justin.drools.research;

import org.drools.runtime.rule.Activation;
import org.drools.runtime.rule.AgendaFilter;

public class MyAgendaFilter implements AgendaFilter {

    private String suffix;

    public MyAgendaFilter(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean accept(Activation activation) {
        return activation.getRule().getName().endsWith(suffix);
    }
}
