package info.gryb.xacml.pap;

import java.lang.reflect.Array;
import java.util.Arrays;
import info.gryb.xacml.pdp.*;
import info.gryb.schemas.xacml.common.*;
import info.gryb.schemas.xacml.wsdl.PapServiceSkeletonInterface;
import info.gryb.schemas.xacml.wsdl.PdpServiceSkeletonInterface;
import info.gryb.xacml.pdp.PdpImpl;
import info.gryb.xacml.pdp.PolicyIterator;
import org.apache.log4j.Logger;
import os.schema.policy._0._2.xacml.tc.names.oasis.PolicySetType;
import os.schema.policy._0._2.xacml.tc.names.oasis.PolicySetDocument;
import os.schema.policy._0._2.xacml.tc.names.oasis.PolicyDocument;
import os.schema.policy._0._2.xacml.tc.names.oasis.PolicyType;

public class PapImpl {

    private static PapImpl pap = null;

    private static Logger log = null;

    public static synchronized PapImpl getInstance() {
        if (pap != null) return pap;
        pap = new PapImpl();
        return pap;
    }

    protected PapImpl() {
        log = Helper.getLogger();
    }

    public static OperationResultDocument createSuccessResponse(RepoType repo) {
        return createResponse("info:gryb:status:OK", "Repo was updated successfully", "Description: " + repo.getDescription());
    }

    public static OperationResultDocument createSuccessResponse(PolicySetType policySet) {
        return createResponse("info:gryb:status:OK", "Policy set was updated successfully", "Description: " + policySet.getDescription() + "; policy set ID: " + policySet.getPolicySetId());
    }

    public static OperationResultDocument createSuccessResponse(PolicyType policy, String message) {
        return createResponse("info:gryb:status:OK", message, "Description: " + policy.getDescription() + "; policy ID: " + policy.getPolicyId());
    }

    public static OperationResultDocument createSuccessResponse(String id, String message) {
        return createResponse("info:gryb:status:OK", message, "Policy ID: " + id);
    }

    public static OperationResultDocument createInvalidParamResponse(String message) {
        return createResponse("info:gryb:status:error:invalid-params", message, "Enable DEBUG mode and check log file for details");
    }

    public static OperationResultDocument createResponse(String code, String message, String details) {
        OperationResultDocument res = OperationResultDocument.Factory.newInstance(Constants.opts);
        StatusType st = StatusType.Factory.newInstance(Constants.opts);
        st.setStatusCode(code);
        st.setStatusMessage(message);
        st.setStatusDetail(details);
        res.setOperationResult(st);
        return res;
    }

    public OperationResultDocument setPolicyRoot(SetPolicyRootDocument policyRoot) {
        synchronized (this) {
            if (policyRoot != null && policyRoot.getSetPolicyRoot() != null && Helper.validate(policyRoot) && (policyRoot.getSetPolicyRoot().getPolicySet() != null || policyRoot.getSetPolicyRoot().getPolicy() != null)) {
                PolicySetType root = policyRoot.getSetPolicyRoot().getPolicySet();
                if (root == null) {
                    root = Helper.createPolicySetFromPolicy(policyRoot.getSetPolicyRoot().getPolicy());
                }
                PdpImpl.getConf().setPolicySet(root);
                return createSuccessResponse(root);
            } else {
                return createInvalidParamResponse("Could not read the policy set");
            }
        }
    }

    public info.gryb.schemas.xacml.common.OperationResultDocument setPolicies(PolicySetDocument policySet) {
        synchronized (this) {
            if (policySet != null && policySet.getPolicySet() != null && Helper.validate(policySet)) {
                PdpImpl.getConf().setPolicySet(policySet.getPolicySet());
                return createSuccessResponse(policySet.getPolicySet());
            } else {
                return createInvalidParamResponse("Could not read the policy set");
            }
        }
    }

    public info.gryb.schemas.xacml.common.OperationResultDocument setPolicy(PolicyDocument policy) {
        synchronized (this) {
            if (policy != null && policy.getPolicy() != null) {
                if (policy.getPolicy().getPolicyId() == null || policy.getPolicy().getPolicyId().length() == 0) {
                    return createInvalidParamResponse("Policy ID is not provided in input XML");
                }
                String polId = policy.getPolicy().getPolicyId();
                PolicySetType set = PdpImpl.getConf().getPolicySet();
                if (set == null) {
                    return createInvalidParamResponse("Could not update the policy becuase the policy set was not found");
                }
                PolicyIterator<PolicySetType> psIter = new PolicyIterator<PolicySetType>(set, true);
                while (psIter.hasNext()) {
                    PolicySetType ps = psIter.next();
                    PolicyType[] pols = ps.getPolicyArray();
                    if (pols == null) continue;
                    for (int i = 0; i < pols.length; i++) {
                        if (pols[i].getPolicyId().equals(polId)) {
                            pols[i] = policy.getPolicy();
                            ps.setPolicyArray(pols);
                            return createSuccessResponse(policy.getPolicy(), "Policy was updated successfully");
                        }
                    }
                }
                return createInvalidParamResponse("Could not update the policy becuase the policy ID was not found");
            } else {
                return createInvalidParamResponse("Could not read the policy");
            }
        }
    }

    public PolicySetDocument getPolicies() {
        PolicySetDocument doc = PolicySetDocument.Factory.newInstance(Constants.opts);
        doc.setPolicySet(PdpImpl.getConf().getPolicySet());
        return doc;
    }

    public PolicyDocument getPolicy(String id) {
        PolicyDocument doc = PolicyDocument.Factory.newInstance(Constants.opts);
        if (id == null || id.length() == 0) return null;
        PolicySetType set = PdpImpl.getConf().getPolicySet();
        if (set == null || set.getPolicyArray() == null) return null;
        PolicyIterator<PolicyType> iter = new PolicyIterator<PolicyType>(set, false);
        while (iter.hasNext()) {
            PolicyType pol = iter.next();
            if (pol.getPolicyId().equals(id)) {
                doc.setPolicy(pol);
                return doc;
            }
        }
        return null;
    }

    public static synchronized info.gryb.schemas.xacml.common.OperationResultDocument addPolicy(PolicyType policy, String psId) {
        if (policy != null) {
            if (policy.getPolicyId() == null || policy.getPolicyId().length() == 0) {
                return createInvalidParamResponse("Policy ID is not provided in input XML");
            }
            if (psId == null || psId.length() == 0) {
                return createInvalidParamResponse("Policy set ID is not provided in input XML");
            }
            String polId = policy.getPolicyId();
            PolicySetType set = PdpImpl.getConf().getPolicySet();
            if (set == null) {
                return createInvalidParamResponse("Could not add the policy becuase the policy set: " + psId + " was not found");
            }
            PolicyIterator<PolicySetType> psIter = new PolicyIterator<PolicySetType>(set, true);
            PolicySetType parent = null;
            while (psIter.hasNext()) {
                PolicySetType ps = psIter.next();
                if (ps.getPolicySetId().equals(psId)) {
                    parent = ps;
                }
                PolicyType[] pols = ps.getPolicyArray();
                if (pols == null) continue;
                for (int i = 0; i < pols.length; i++) {
                    if (pols[i].getPolicyId().equals(polId)) {
                        return createInvalidParamResponse("Policy with ID: " + polId + " can't be added becuase it already exists in policy set: " + ps.getPolicySetId());
                    }
                }
            }
            if (parent == null) {
                return createInvalidParamResponse("Could not add the policy becuase the policy set: " + psId + " was not found");
            }
            Helper.addPolicy(parent, policy);
            return createSuccessResponse(policy, "Added successfully");
        } else {
            return createInvalidParamResponse("Could not read the policy");
        }
    }

    public info.gryb.schemas.xacml.common.OperationResultDocument deletePolicy(String id) {
        synchronized (this) {
            if (id == null) {
                return createInvalidParamResponse("Could not read the policy ID from input XML");
            }
            PolicySetType set = PdpImpl.getConf().getPolicySet();
            if (set == null || set.getPolicyArray() == null) {
                return createInvalidParamResponse("Could not delete the policy becuase the policy set was not found or was empty");
            }
            PolicyIterator<PolicyType> iter = new PolicyIterator<PolicyType>(set, false);
            while (iter.hasNext()) {
                PolicyType pol = iter.next();
                if (pol.getPolicyId().equals(id)) {
                    iter.remove();
                    return createSuccessResponse(id, "Deleted Successfully");
                }
            }
            return createInvalidParamResponse("Policy was not found");
        }
    }

    public RepoDocument getRepo() {
        RepoDocument doc = RepoDocument.Factory.newInstance(Constants.opts);
        doc.setRepo(PdpImpl.getConf().getRepo());
        return doc;
    }

    public info.gryb.schemas.xacml.common.OperationResultDocument setRepo(RepoDocument repo) {
        synchronized (this) {
            if (repo != null && repo.getRepo() != null && Helper.validate(repo)) {
                PdpImpl.getConf().setRepo(repo.getRepo());
                return createSuccessResponse(repo.getRepo());
            } else {
                return createInvalidParamResponse("Could not update repo");
            }
        }
    }
}
