package org.activebpel.rt.bpel.def.faults;

import java.util.Iterator;
import org.activebpel.rt.bpel.IAeFaultTypeInfo;
import org.activebpel.rt.util.AeUtil;
import org.activebpel.rt.wsdl.IAeContextWSDLProvider;

/**
 * Base impl of the fault matching strategy. This interface provides the common framework for matching thrown
 * faults to catch or catchAll fault handlers.
 */
public abstract class AeBaseFaultMatchingStrategy implements IAeFaultMatchingStrategy {

    /**
    * Base class for matching rules. Provides the best match flag.
    */
    protected abstract static class AeFaultMatchingRule implements IAeFaultMatchingRule {

        /** 
       * Priority order of this rule. 
       */
        private int mPriority;

        /**
       * Creates the rule w/ the best match flag
       */
        protected AeFaultMatchingRule() {
        }

        /**
       * @param aPriority
       */
        public void setPriority(int aPriority) {
            mPriority = aPriority;
        }

        /**
       * Get the priority of this rule.
       */
        public int getPriority() {
            return mPriority;
        }
    }

    /**
    * Matches a fault w/o data to a catch with a matching fault name
    */
    protected static class AeFaultNameOnly extends AeFaultMatchingRule {

        /**
       * @see org.activebpel.rt.bpel.def.faults.IAeFaultMatchingRule#getMatch( org.activebpel.rt.wsdl.IAeContextWSDLProvider, 
       * org.activebpel.rt.bpel.def.faults.IAeCatch, org.activebpel.rt.bpel.IAeFaultTypeInfo)
       */
        public IAeMatch getMatch(IAeContextWSDLProvider aProvider, IAeCatch aCatch, IAeFaultTypeInfo aFault) {
            boolean matched = !aFault.hasData() && !aCatch.hasFaultVariable() && AeUtil.compareObjects(aFault.getFaultName(), aCatch.getFaultName());
            IAeMatch match = null;
            if (matched) {
                match = new AeMatch(true, getPriority());
            }
            return match;
        }
    }

    /**
    * Matches a fault w/ data to a catch with a matching fault name and data
    */
    protected static class AeFaultNameAndData extends AeFaultMatchingRule {

        /**
       * @see org.activebpel.rt.bpel.def.faults.IAeFaultMatchingRule#getMatch( org.activebpel.rt.wsdl.IAeContextWSDLProvider, 
       * org.activebpel.rt.bpel.def.faults.IAeCatch, org.activebpel.rt.bpel.IAeFaultTypeInfo)
       */
        public IAeMatch getMatch(IAeContextWSDLProvider aProvider, IAeCatch aCatch, IAeFaultTypeInfo aFault) {
            boolean matched = false;
            if (aFault.hasMessageData() && aCatch.hasFaultVariable()) {
                matched = AeUtil.compareObjects(aCatch.getFaultMessageType(), aFault.getMessageType()) && AeUtil.compareObjects(aCatch.getFaultName(), aFault.getFaultName());
            }
            IAeMatch match = null;
            if (matched) {
                match = new AeMatch(true, getPriority());
            }
            return match;
        }
    }

    /**
    * Matches a fault with data to a catch w/o a name but with matching data
    */
    protected static class AeVariableOnly extends AeFaultMatchingRule {

        /**
       * @see org.activebpel.rt.bpel.def.faults.IAeFaultMatchingRule#getMatch( org.activebpel.rt.wsdl.IAeContextWSDLProvider, 
       * org.activebpel.rt.bpel.def.faults.IAeCatch, org.activebpel.rt.bpel.IAeFaultTypeInfo)
       */
        public IAeMatch getMatch(IAeContextWSDLProvider aProvider, IAeCatch aCatch, IAeFaultTypeInfo aFault) {
            boolean matched = false;
            if (aFault.hasMessageData() && aCatch.hasFaultVariable() && aCatch.getFaultName() == null) {
                matched = AeUtil.compareObjects(aFault.getMessageType(), aCatch.getFaultMessageType());
            }
            IAeMatch match = null;
            if (matched) {
                match = new AeMatch(false, getPriority());
            }
            return match;
        }
    }

    /**
    * Gets all of the rules for the fault matching strategy.
    */
    protected abstract IAeFaultMatchingRule[] getRules();

    /**
    * @see org.activebpel.rt.bpel.def.faults.IAeFaultMatchingStrategy#selectMatchingCatch(org.activebpel.rt.wsdl.IAeContextWSDLProvider, 
    *   java.util.Iterator, org.activebpel.rt.bpel.IAeFaultTypeInfo)
    */
    public IAeCatch selectMatchingCatch(IAeContextWSDLProvider aProvider, Iterator aIterOfCatches, IAeFaultTypeInfo aFault) {
        IAeCatch matched = null;
        IAeMatch previousMatch = null;
        while (aIterOfCatches.hasNext()) {
            IAeCatch katch = (IAeCatch) aIterOfCatches.next();
            IAeFaultMatchingRule[] rules = getRules();
            for (int i = 0; i < rules.length; i++) {
                IAeFaultMatchingRule rule = rules[i];
                IAeMatch match = rule.getMatch(aProvider, katch, aFault);
                if (match != null) {
                    if (match.isBestMatch()) {
                        return katch;
                    }
                    if (previousMatch == null) {
                        previousMatch = match;
                        matched = katch;
                    } else if (match.isBetterMatchThan(previousMatch)) {
                        previousMatch = match;
                        matched = katch;
                    }
                }
            }
        }
        return matched;
    }
}
