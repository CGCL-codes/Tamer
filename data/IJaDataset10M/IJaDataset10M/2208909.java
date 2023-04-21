package net.sf.gateway.mef.businessrules;

import java.util.Iterator;
import net.sf.gateway.mef.businessrules.baserules.BinAttachRule;
import net.sf.gateway.mef.businessrules.baserules.FinTranRule;
import net.sf.gateway.mef.businessrules.baserules.Form111Rule;
import net.sf.gateway.mef.businessrules.baserules.HeaderRule;
import net.sf.gateway.mef.businessrules.baserules.R000Rule;
import net.sf.gateway.mef.configuration.ClientConfigurator;

/**
 * Class for building BusinessRule iterators.
 */
public class RuleFactory implements RuleFactoryType {

    private int taxYear;

    public RuleFactory(int taxYear) {
        this.taxYear = taxYear;
    }

    @SuppressWarnings("rawtypes")
    protected RuleFactoryType getRuleFactoryType2009() {
        String state = ClientConfigurator.getState().getStringValue();
        String pkg = this.getClass().getPackage().getName();
        String className = pkg + ".RuleFactory" + state.toUpperCase() + taxYear;
        try {
            Class cls = Class.forName(className);
            RuleFactoryType rf = (RuleFactoryType) cls.newInstance();
            return rf;
        } catch (Exception e) {
            return null;
        }
    }

    public Iterator<R000Rule> getR000Rules() {
        RuleFactoryType rf = getRuleFactoryType2009();
        if (rf == null) {
            return null;
        } else {
            return rf.getR000Rules();
        }
    }

    public Iterator<BinAttachRule> getBinAttachRules() {
        RuleFactoryType rf = getRuleFactoryType2009();
        if (rf == null) {
            return null;
        } else {
            return rf.getBinAttachRules();
        }
    }

    public Iterator<FinTranRule> getFinTranRules() {
        RuleFactoryType rf = getRuleFactoryType2009();
        if (rf == null) {
            return null;
        } else {
            return rf.getFinTranRules();
        }
    }

    public Iterator<HeaderRule> getHeaderRules() {
        RuleFactoryType rf = getRuleFactoryType2009();
        if (rf == null) {
            return null;
        } else {
            return rf.getHeaderRules();
        }
    }

    public Iterator<Form111Rule> getForm111Rules() {
        RuleFactoryType rf = getRuleFactoryType2009();
        if (rf == null) {
            return null;
        } else {
            return rf.getForm111Rules();
        }
    }
}
