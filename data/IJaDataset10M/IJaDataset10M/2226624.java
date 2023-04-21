package org.crypthing.things.validator.pkibr;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.ResourceBundle;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.crypthing.things.cert.CertificatePolicies;
import org.crypthing.things.config.Bundle;
import org.crypthing.things.validator.ActionOutput;
import org.crypthing.things.validator.OutputFactory;
import org.crypthing.things.validator.Rule;
import org.crypthing.things.validator.RuleOutput;
import org.crypthing.things.validator.ValidationInput;

/**
 * Certificate Policies extension validator
 * @author yorickflannagan
 * @version 1.0
 *
 */
public class CertificatePoliciesValidator implements Rule {

    private static final long serialVersionUID = 2113782685253591596L;

    private static String PKIBR_RULE_NAME_STR;

    private static String PKIBR_RULE_ACTION_STR;

    private static String PKIBR_RULE_MSG_STR;

    static {
        ResourceBundle resources = Bundle.getInstance().getBundle(new CertificatePoliciesValidator());
        PKIBR_RULE_NAME_STR = resources.getString("PKIBR_RULE_NAME_CP_STR");
        PKIBR_RULE_ACTION_STR = resources.getString("PKIBR_RULE_ACTION_CP_STR");
        PKIBR_RULE_MSG_STR = resources.getString("PKIBR_RULE_MSG_STR");
    }

    @Override
    public RuleOutput execute(ValidationInput input, OutputFactory outputEngine) {
        X509Certificate[] chain = (X509Certificate[]) input.getRuleInput("CertificateChain");
        if (chain == null) throw new InvalidInputParamException(Bundle.getInstance().getResourceString(this, "PKIBR_CHAIN_MISSING_ERROR"));
        RuleOutput output = outputEngine.getRuleOutput();
        output.setRuleName(PKIBR_RULE_NAME_STR);
        for (int i = 0; i < chain.length; i++) {
            ActionOutput out = outputEngine.getActionOutput();
            out.setRuleAction(PKIBR_RULE_ACTION_STR.replace("[DN]", chain[i].getSubjectX500Principal().getName()));
            if ((checkExtension(chain[i], out)) && (checkPolicy(chain[i], out)) && (checkURL(chain[i], out))) {
                out.setSuccess(true);
                out.setLevel(PKIBRWarningLevel.WARNING_LEVEL_SUCCESS);
                out.setMessage(PKIBR_RULE_MSG_STR);
            }
            output.addActionOutput(out);
        }
        return output;
    }

    private boolean checkExtension(X509Certificate cert, ActionOutput output) {
        boolean retVal = true;
        if (cert.getExtensionValue(X509Extensions.CertificatePolicies.getId()) == null) {
            output.setSuccess(false);
            output.setLevel(PKIBRWarningLevel.WARNING_LEVEL_NON_COMPLIANCE);
            output.setMessage(Bundle.getInstance().getResourceString(this, "PKIBR_RULE_MISSING_CP_WARN"));
            retVal = false;
        }
        return retVal;
    }

    private boolean checkPolicy(X509Certificate cert, ActionOutput output) {
        boolean retVal = true;
        try {
            CertificatePolicies policies = new CertificatePolicies(cert.getExtensionValue(X509Extensions.CertificatePolicies.getId()));
            Iterator<String> iterator = policies.iterator();
            if (!iterator.hasNext()) {
                output.setSuccess(false);
                output.setLevel(PKIBRWarningLevel.WARNING_LEVEL_NON_COMPLIANCE);
                output.setMessage(Bundle.getInstance().getResourceString(this, "PKIBR_RULE_MISSING_CP_WARN"));
            }
        } catch (Exception e) {
            output.setSuccess(false);
            output.setLevel(PKIBRWarningLevel.WARNING_LEVEL_ERROR);
            output.setMessage(Bundle.getInstance().getResourceString(this, "PKIBR_RULE_INVALID_CP_WARN"));
        }
        return retVal;
    }

    private boolean checkURL(X509Certificate cert, ActionOutput output) {
        boolean hasURL = false;
        CertificatePolicies policies = new CertificatePolicies(cert.getExtensionValue(X509Extensions.CertificatePolicies.getId()));
        Iterator<String> iterator = policies.iterator();
        while ((iterator.hasNext()) && (!hasURL)) {
            String url = policies.getValue(iterator.next());
            hasURL = ((url != null) && (!url.contentEquals("")));
        }
        if (!hasURL) {
            output.setSuccess(false);
            output.setLevel(PKIBRWarningLevel.WARNING_LEVEL_NON_COMPLIANCE);
            output.setMessage(Bundle.getInstance().getResourceString(this, "PKIBR_RULE_CP_WITHOUTURL_WARN"));
        }
        return hasURL;
    }
}
