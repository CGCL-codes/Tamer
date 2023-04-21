package org.gridtrust.ppm.impl.policy.matcher.jibx;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gridtrust.Constants;
import org.gridtrust.ppm.impl.policy.bind.jibx.ApplyType;
import org.gridtrust.ppm.impl.policy.bind.jibx.ConditionType;
import org.gridtrust.ppm.impl.policy.bind.jibx.PolicySetType;
import org.gridtrust.ppm.impl.policy.bind.jibx.PolicyType;
import org.gridtrust.ppm.impl.policy.bind.jibx.RuleType;
import org.gridtrust.ppm.impl.policy.bind.jibx.TargetType;
import org.gridtrust.ppm.impl.policy.matcher.AbstractPolicyCompatibleMatcher;
import org.gridtrust.ppm.impl.policy.matcher.PolicyUtil;
import org.gridtrust.ppm.impl.policy.normalizer.RuleAttributeList;
import org.gridtrust.ppm.impl.policy.normalizer.jibx.JIBXConditionWriter;
import org.gridtrust.ppm.impl.policy.normalizer.jibx.JIBXUtil;
import org.gridtrust.ppm.impl.policy.normalizer.jibx.JIBXRuleWriter;

public class JIBXPolicyCompatibleMatcher extends AbstractPolicyCompatibleMatcher {

    private static final Log log = LogFactory.getLog(JIBXPolicyCompatibleMatcher.class);

    private String firstPolicyContent;

    private int firstCompatability;

    private String firstPolicyDataName;

    private String secondPolicyDataName;

    private boolean isSaveDataStructure;

    public int getFirstCompatability() {
        return this.firstCompatability;
    }

    public String getFirstPolicyContent() {
        return this.firstPolicyContent;
    }

    public void setFirstCompatability(int firstCompatability) {
        this.firstCompatability = firstCompatability;
    }

    public void setFirstPolicyContent(String firstPolicyContent) {
        this.firstPolicyContent = firstPolicyContent;
    }

    public String getFirstPolicyDataName() {
        return firstPolicyDataName;
    }

    public void setFirstPolicyDataName(String firstPolicyDataName) {
        this.firstPolicyDataName = firstPolicyDataName;
    }

    public String getSecondPolicyDataName() {
        return secondPolicyDataName;
    }

    public void setSecondPolicyDataName(String servicePolicyDataName) {
        this.secondPolicyDataName = servicePolicyDataName;
    }

    public boolean isSaveDataStructure() {
        return isSaveDataStructure;
    }

    public boolean isCompatiblePolicies(String secondPolicyContent, int secondCompatability, int counter) {
        boolean isCompatible = false;
        long totalConversionTime = 0;
        long totalDSTime = 0;
        long totalCheckingTime = 0;
        long totalOverAllTime = 0;
        for (int i = 0; i < counter; i++) {
            long startTime = 0;
            long dataStructureTime = 0;
            long jaxbTime = 0;
            long compatibleTime = 0;
            if (firstCompatability == Constants.EXTENDS && secondCompatability == Constants.PERMIT) {
                isCompatible = true;
            } else {
                startTime = System.nanoTime();
                List<RuleType> firstRuleList = getRuleList(firstPolicyContent);
                List<RuleType> secondRuleList = getRuleList(secondPolicyContent);
                jaxbTime = System.nanoTime();
                List<RuleAttributeList> firstAttributeInfoList = getRuleAttributeList(firstRuleList);
                List<RuleAttributeList> secondAttributeInfoList = getRuleAttributeList(secondRuleList);
                if (isSaveDataStructure) {
                    savePolicyInformation(this.firstPolicyDataName, firstAttributeInfoList);
                    savePolicyInformation(this.secondPolicyDataName, secondAttributeInfoList);
                }
                dataStructureTime = System.nanoTime();
                if (firstCompatability == Constants.CONVERGE && secondCompatability == Constants.CONVERGE) {
                    isCompatible = isEqual(firstAttributeInfoList, secondAttributeInfoList);
                } else if (firstCompatability == Constants.RESTRICTS && secondCompatability == Constants.DENY) {
                    isCompatible = hasConjunction(firstAttributeInfoList, secondAttributeInfoList);
                } else {
                    int compType = getCompatibilityTypeCheck(firstCompatability, secondCompatability);
                    if (compType == 1) {
                        isCompatible = isSubset(firstAttributeInfoList, secondAttributeInfoList, 0);
                    } else if (compType == 2) {
                        isCompatible = isSubset(secondAttributeInfoList, firstAttributeInfoList, 0);
                    }
                }
                compatibleTime = System.nanoTime();
            }
            totalConversionTime += (jaxbTime - startTime);
            totalDSTime += (dataStructureTime - jaxbTime);
            totalCheckingTime += (compatibleTime - dataStructureTime);
            totalOverAllTime += ((compatibleTime - startTime));
        }
        log.info("XML Binding conversion time " + ((double) totalConversionTime / (1000 * 1000)));
        log.info("XML Binding to datastructure conversion time " + ((double) totalDSTime / (1000 * 1000)));
        log.info("Compatiblity checking time " + ((double) totalCheckingTime / (1000 * 1000)));
        log.info("Total time " + ((double) totalOverAllTime / (1000 * 1000)));
        return isCompatible;
    }

    private List<RuleAttributeList> getRuleAttributeList(List<RuleType> ruleList) {
        List<RuleAttributeList> attributeInfoList = new ArrayList<RuleAttributeList>();
        JIBXConditionWriter conditionWriter = new JIBXConditionWriter();
        for (RuleType rule : ruleList) {
            RuleAttributeList ruleAttributeList = new RuleAttributeList();
            ruleAttributeList.setRuleId(rule.getRuleId());
            String decesion = rule.getEffect();
            ConditionType condition = rule.getCondition();
            if (condition != null) {
                try {
                    ApplyType applyType = JIBXUtil.getApplyType(condition);
                    ruleAttributeList = conditionWriter.getRuleAttributeList(applyType, ruleAttributeList);
                    ruleAttributeList.setDecesion(decesion);
                    attributeInfoList.add(ruleAttributeList);
                } catch (Exception e) {
                    log.error("Unable to extract attribute information, does not support this policy", e);
                }
            } else {
                ruleAttributeList.setDecesion(decesion);
            }
        }
        return attributeInfoList;
    }

    private List<RuleType> getRuleList(String policyContent) {
        List<RuleType> ruleList = new ArrayList<RuleType>();
        try {
            String rootElement = PolicyUtil.getRootElement(policyContent);
            if (rootElement.equalsIgnoreCase("Policy")) {
                PolicyType policyType = JIBXUtil.getPolicyType(policyContent);
                ruleList = getRuleList(policyType, ruleList);
            } else if (rootElement.equalsIgnoreCase("PolicySet")) {
                PolicySetType policySetType = JIBXUtil.getPolicySetType(policyContent);
                ruleList = getRuleList(policySetType, ruleList);
            }
        } catch (Exception e) {
            log.error("Wrong type policy provided", e);
        }
        return ruleList;
    }

    private List<RuleType> getRuleList(PolicyType policyType, List<RuleType> ruleList) {
        for (RuleType rule : policyType.getRuleList()) {
            if (!JIBXUtil.isEmptyTarget(rule.getTarget())) {
                rule = reWriteRule(rule, rule.getTarget());
            }
            ruleList.add(rule);
        }
        return ruleList;
    }

    private List<RuleType> getRuleList(PolicySetType policySetType, List<RuleType> ruleList) {
        List<PolicySetType> policySetList = policySetType.getPolicySetList();
        if (policySetList == null || policySetList.size() == 0) {
            List<PolicyType> policyList = policySetType.getPolicyList();
            for (PolicyType policy : policyList) {
                ruleList = getRuleList(policy, ruleList);
            }
        } else {
            for (PolicySetType policySet : policySetList) {
                ruleList = getRuleList(policySet, ruleList);
            }
        }
        return ruleList;
    }

    private RuleType reWriteRule(RuleType rule, TargetType target) {
        JIBXRuleWriter ruleWriter = new JIBXRuleWriter();
        return ruleWriter.addNonEmptyTargetToRule(rule, target);
    }

    public void savePolicyInformation(String name, List<RuleAttributeList> ruleAttributeList) {
        try {
            FileOutputStream fOut = new FileOutputStream(name);
            ObjectOutputStream objOut = new ObjectOutputStream(fOut);
            objOut.writeObject(ruleAttributeList);
        } catch (Exception e) {
            log.error("Object write error ", e);
        }
    }
}
