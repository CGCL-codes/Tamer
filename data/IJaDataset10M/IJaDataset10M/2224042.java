package issrg.pba.rbac.policies;

import issrg.pba.PbaException;
import issrg.pba.rbac.xmlpolicy.XMLPolicyParser;
import issrg.pba.rbac.*;
import issrg.pba.*;
import iaik.asn1.*;
import iaik.asn1.structures.AlgorithmID;
import iaik.utils.Util;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.math.*;
import java.text.*;
import issrg.pba.rbac.RetainedADI;
import issrg.SAWS.*;

/**
 * This is the class representing the MSoD Policy. It contains a vector of MSoD rules. It determines 
 * whether a access request is allowed by the MSoD rules.
 *
 * @author W. Xu
 * @version 0.1
 */
public class MSoDPolicySet {

    protected java.util.List<MSoDRule> msodRules;

    protected RetainedADI retainedADI;

    private SAWSServer sawsServer;

    private PolicyParser pp;

    protected MSoDPolicySet() {
    }

    /**
   * This constructor creates the MSoDPolicySet object. 
   *
   * @params m is a List of MSoD rules. 
   * @params r is the retained ADI object for storing access request decisions in history. 
   * @params sawsServer is the SAWS server for recording log records for PERMIS. 
   * @params pp is the PolicyParser for PERMIS. 
   * 
   */
    public MSoDPolicySet(List<MSoDRule> m, RetainedADI r, SAWSServer sawsServer, PolicyParser pp) {
        msodRules = m;
        retainedADI = r;
        this.sawsServer = sawsServer;
        this.pp = pp;
        List<RecordBlock> v1 = null;
        while ((v1 = sawsServer.sawsReadOneLogFile()) != null) {
            addRecords(v1);
        }
    }

    /**
   * This method adds a List of decision records to the retained ADI. The lastStep decision records should be 
   * removed along with decision records with the same contextinstance. 
   *
   * @param v is the List of decision records. 
   *
   * @return void 
   */
    private void addRecords(List<RecordBlock> v) {
        for (int i = 0; i < v.size(); ++i) {
            DecisionRecord dr = toDecisionRecord(((RecordBlock) v.get(i)).getRecord());
            ContextNamePrincipal instanceDN = null;
            if (dr == null) continue;
            try {
                instanceDN = new ContextNamePrincipal(dr.getContextInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            int size = msodRules.size();
            MSoDRule aMSoDRule = null;
            for (int j = 0; j < size; ++j) {
                aMSoDRule = (MSoDRule) msodRules.get(j);
                if (aMSoDRule.contains(instanceDN)) {
                    if (aMSoDRule.isLastStep(dr.getAction(), dr.getTarget())) {
                        retainedADI.removeContext(aMSoDRule.getPolicyContext(), instanceDN);
                        break;
                    } else retainedADI.add(dr);
                }
            }
        }
    }

    private issrg.pba.rbac.DecisionRecord toDecisionRecord(byte[] recordBlock) {
        DateFormat df = DateFormat.getDateInstance();
        issrg.pba.rbac.DecisionRecord dr = null;
        try {
            ASN1 asn1 = new ASN1(recordBlock);
            IA5String sType = (IA5String) asn1.getComponentAt(0);
            String type = (String) sType.getValue();
            if (type.compareTo("PermisMSoDType") != 0) return null;
            IA5String s0 = (IA5String) asn1.getComponentAt(1);
            String userID = (String) s0.getValue();
            s0 = (IA5String) asn1.getComponentAt(2);
            String action = (String) s0.getValue();
            s0 = (IA5String) asn1.getComponentAt(3);
            String Target = (String) s0.getValue();
            s0 = (IA5String) asn1.getComponentAt(4);
            String contextInstance = (String) s0.getValue();
            s0 = (IA5String) asn1.getComponentAt(5);
            Date dateTime = df.parse((String) s0.getValue());
            Vector<Credentials> CredsVec = new Vector<Credentials>();
            INTEGER i0 = (INTEGER) asn1.getComponentAt(6);
            int roleCount = ((BigInteger) i0.getValue()).intValue();
            for (int j = 0; j < roleCount; ++j) {
                s0 = (IA5String) asn1.getComponentAt(7 + j * 4);
                String roleValue = (String) s0.getValue();
                s0 = (IA5String) asn1.getComponentAt(7 + j * 4 + 1);
                String roleType = (String) s0.getValue();
                s0 = (IA5String) asn1.getComponentAt(7 + j * 4 + 2);
                String d1s = (String) s0.getValue();
                Date d1 = df.parse(d1s);
                s0 = (IA5String) asn1.getComponentAt(7 + j * 4 + 3);
                String d2s = (String) s0.getValue();
                Date d2 = df.parse(d2s);
                AbsoluteValidityPeriod avp = new AbsoluteValidityPeriod(d1, d2);
                Credentials pc = new PermisCredentials(((XMLPolicyParser) pp).getRole(roleType, roleValue));
                ExpirableCredentials ec = new ExpirableCredentials(pc, (ValidityPeriod) avp);
                CredsVec.add(ec);
            }
            Credentials creds = new SetOfSubsetsCredentials(CredsVec);
            dr = new DecisionRecord(userID, creds, action, Target, contextInstance, dateTime);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return dr;
    }

    /**
   * This method is to create a binary array for a permis request decision record. 
   * 
   * 
   * @param dr is the decision record class 
   * 
   * @return binary array of this PERMIS block
   */
    private byte[] toBytes(issrg.pba.rbac.DecisionRecord dr) {
        DateFormat df = DateFormat.getDateInstance();
        SEQUENCE ASN1Seq = new SEQUENCE();
        ASN1Seq.addComponent(new IA5String("PermisMSoDType"));
        ASN1Seq.addComponent(new IA5String(dr.getUserID()));
        ASN1Seq.addComponent(new IA5String(dr.getAction()));
        ASN1Seq.addComponent(new IA5String(dr.getTarget()));
        ASN1Seq.addComponent(new IA5String(dr.getContextInstance()));
        ASN1Seq.addComponent(new IA5String(df.format(dr.getDateTime())));
        Credentials creds = dr.getCreds();
        int credsNumber = 0;
        Vector<Credentials> credsV = ((SetOfSubsetsCredentials) creds).getValue();
        credsNumber = credsV.size();
        ASN1Seq.addComponent(new INTEGER(credsNumber));
        for (int i = 0; i < credsNumber; ++i) {
            ExpirableCredentials ec = (ExpirableCredentials) credsV.get(i);
            PermisCredentials pc = (PermisCredentials) ec.getExpirable();
            ValidityPeriod vp = (ValidityPeriod) ec.getValidityPeriod();
            String roleValue = (String) pc.getRoleValue();
            String roleType = (String) pc.getRoleType();
            ASN1Seq.addComponent(new IA5String(roleValue));
            ASN1Seq.addComponent(new IA5String(roleType));
            Date d1 = ((IntersectionValidityPeriod) vp).getNotBefore();
            Date d2 = ((IntersectionValidityPeriod) vp).getNotAfter();
            ASN1Seq.addComponent(new IA5String(df.format(d1)));
            ASN1Seq.addComponent(new IA5String(df.format(d2)));
        }
        byte[] arrayASN = DerCoder.encode(ASN1Seq);
        return arrayASN;
    }

    /**
   * This method is to determine if this MSoD policy applies to this user access request. 
   * If this MSoD policy applies, then it means the user access request has broken one of the MSoD rules in this MSoD policy
   * and it should be forbidden by this MSoD policy, and this method will return true; otherwise this method will return false. 
   *
   * @param creds is the user credential
   * @param a is the user action
   * @param t is the user requested target
   * @param environment is the environment of the decision by PERMIS 
   *
   * @return true if this MSoD policy applies to this user requested access; otherwise false. 
   */
    public boolean separationOfDutiesApplies(issrg.pba.Credentials creds, issrg.pba.Action a, issrg.pba.Target t, java.util.Map environment) {
        String contextInstance = (String) environment.get("ContextInstance");
        if (contextInstance == null) {
            return false;
        }
        ContextNamePrincipal instanceDN = null, maskDN = null, lastMaskDN = null;
        try {
            instanceDN = new ContextNamePrincipal(contextInstance);
        } catch (issrg.utils.RFC2253ParsingException e) {
        }
        issrg.pba.Subject subject = (issrg.pba.rbac.PermisSubject) environment.get("Subject");
        String actionName = a.getActionName();
        String userID = ((issrg.pba.rbac.PermisSubject) subject).getName();
        String targetName = ((PermisTarget) t).getName();
        int size = msodRules.size();
        MSoDRule aMSoDRule = null;
        boolean lastStepFlag = false;
        for (int i = 0; i < size; ++i) {
            aMSoDRule = msodRules.get(i);
            if (aMSoDRule.contains(instanceDN)) {
                boolean result = aMSoDRule.separationOfDutiesApplies(retainedADI, creds, subject, a, t, environment, instanceDN);
                if (!result) {
                    if (aMSoDRule.isLastStep(actionName, targetName)) {
                        lastStepFlag = true;
                        maskDN = aMSoDRule.getPolicyContext();
                        if (lastMaskDN == null) {
                            lastMaskDN = maskDN;
                        } else {
                            if (maskDN.contains(lastMaskDN)) {
                                lastMaskDN = maskDN;
                            }
                        }
                    }
                } else {
                    return true;
                }
            }
        }
        issrg.pba.rbac.DecisionRecord dr = new issrg.pba.rbac.DecisionRecord(userID, creds, actionName, targetName, contextInstance, CustomisePERMIS.getSystemClock().getTime());
        if (lastStepFlag) {
            retainedADI.removeContext(lastMaskDN, instanceDN);
        } else {
            retainedADI.add(dr);
        }
        sawsServer.sendLogRecord(toBytes(dr));
        return false;
    }
}
