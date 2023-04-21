package com.agentfactory.afapl.interpreter;

import com.agentfactory.clf.lang.IFormula;
import com.agentfactory.clf.util.DefaultLogicFactory;

public class AFAPLLogicFactory extends DefaultLogicFactory {

    @Override
    protected IFormula createCustomFormula(String fos) {
        if (fos.startsWith("!")) {
            return new Goal(Goal.ACHIEVE, createFormula(fos.substring(1)));
        } else if (fos.startsWith("$")) {
            return new Goal(Goal.MAINTAIN, createFormula(fos.substring(1)));
        }
        return null;
    }
}
