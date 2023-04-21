package edu.uiuc.ncsa.soap;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sun.xacml.ConfigurationStore;
import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.ParsingException;
import com.sun.xacml.PDP;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.combine.PermitOverridesPolicyAlg;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.sun.xacml.ctx.Subject;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.AttributeFinderModule;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.impl.CurrentEnvModule;
import com.sun.xacml.finder.impl.SelectorModule;
import com.sun.xacml.finder.PolicyFinderModule;
import com.sun.xacml.support.finder.StaticPolicyFinderModule;
import com.sun.xacml.support.finder.StaticRefPolicyFinderModule;
import com.sun.xacml.support.finder.URLPolicyFinderModule;

/**
 * SecurityAuthorizer is a simple implementation of an XACML PDP (Policy
 * Decision Point).  You should call the constructor which accepts a
 * directory name containing one or more XACML policy files in XML (text)
 * format.  These policy files will be read in and combined to create a
 * policy engine.  Then call the "authorizeMessage" method with three string
 * parameters corresponding to the "From", "To", and "Action" fields of a
 * SOAPMessage.  The method will run the policy engine and return a
 * true/false response for if the message is authorized or not.
 *
 * The vast majority of this code is taken from the Sun XACML 2.0 example
 * code.  The code can be found on SourceForge in the SVN repository: 
 * http://sourceforge.net/projects/sunxacml/
 *
 * <p>SVN: $Id: SecurityAuthorizer.java 159 2009-12-01 20:43:11Z tfleury $</p>
 * @author Terry Fleury (tfleury@ncsa.uiuc.edu)
 * @version $Revision: 159 $
 */
public class SecurityAuthorizer {

    private static final transient Log LOG = LogFactory.getLog(SecurityAuthorizer.class);

    private PDP pdp = null;

    /**
     * Default constructor, should not be used.
     */
    public SecurityAuthorizer() throws Exception {
        ConfigurationStore store = new ConfigurationStore();
        store.useDefaultFactories();
        pdp = new PDP(store.getDefaultPDPConfig());
    }

    /**
     * This contructor creates a PDP (policy decision point) by reading in
     * policy XML files found in the directory specified in the policyDir
     * parameter.  The directory name should be specified by a full path,
     * and should NOT end in a '/'.  Any files found in that directory
     * should be XACML-style text files containing policy statements in XML
     * format.  This constructor reads in those XML files and creates a PDP
     * to be used later by the "authorizeMessage()" method.
     * @param policyDir A full path specification to a directory containing
     *        XACML files in XML format.
     * @throws Exception If there is a problem reading in the files from the
     *         policyDir, or if the PDP cannot be created.
     */
    public SecurityAuthorizer(String policyDir) throws Exception {
        String[] policyFiles = null;
        File policyFileHandle = new File(policyDir);
        if (policyFileHandle.isDirectory()) {
            String[] filelist = policyFileHandle.list();
            policyFiles = new String[filelist.length];
            for (int i = 0; i < filelist.length; i++) {
                policyFiles[i] = policyDir + "/" + filelist[i];
            }
        } else {
            policyFiles = new String[1];
            policyFiles[0] = policyDir;
        }
        List policyList = Arrays.asList(policyFiles);
        StaticPolicyFinderModule staticModule = new StaticPolicyFinderModule(PermitOverridesPolicyAlg.algId, policyList);
        StaticRefPolicyFinderModule staticRefModule = new StaticRefPolicyFinderModule(policyList);
        URLPolicyFinderModule urlModule = new URLPolicyFinderModule();
        PolicyFinder policyFinder = new PolicyFinder();
        HashSet<PolicyFinderModule> policyModules = new HashSet<PolicyFinderModule>();
        policyModules.add(staticModule);
        policyModules.add(staticRefModule);
        policyModules.add(urlModule);
        policyFinder.setModules(policyModules);
        CurrentEnvModule envAttributeModule = new CurrentEnvModule();
        SelectorModule selectorAttributeModule = new SelectorModule();
        AttributeFinder attributeFinder = new AttributeFinder();
        ArrayList<AttributeFinderModule> attributeModules = new ArrayList<AttributeFinderModule>();
        attributeModules.add(envAttributeModule);
        attributeModules.add(selectorAttributeModule);
        attributeFinder.setModules(attributeModules);
        pdp = new PDP(new PDPConfig(attributeFinder, policyFinder, null));
    }

    /** 
     * Given an XACML attribute and a URI string, return a new HashSet
     * containing a corresponding XACML Attribute.  In essence, this method
     * converts a "From", "To", or "Action" into an XACML "Subject",
     * "Resource" or "Action".  This is a helper method called by
     * getSubject, getResource, and getAction.
     * @param attr An XACML AttributeId, usually one of the following strings:
     *        "urn:oasis:names:tc:xacml:1.0:subject:subject-id",
     *        "urn:oasis:names:tc:xacml:1.0:resource:resource-id", or
     *        "urn:oasis:names:tc:xacml:1.0:action:action-id".
     * @param uri A string corresponding to the Id in the policy file.
     * @return A HashSet containing an XACML Attribute for use with the PDP.
     * @throws URISyntaxException If the passed-in uri is formatted incorrectly.
     */
    private HashSet getAnyURIClause(String attr, String uri) throws URISyntaxException {
        HashSet<Attribute> rethash = new HashSet<Attribute>();
        URI attrId = new URI(attr);
        AnyURIAttribute value = new AnyURIAttribute(new URI(uri));
        rethash.add(new Attribute(attrId, null, null, value));
        return rethash;
    }

    /**
     * Given a "From" string (a "Subject" in XACML terminology), return a
     * Set containing an XACML Subject object to be utilized in a PDP.
     * @param addr A "From" string to be converted into an XACML Subject.
     * @throws URISyntaxException If the given addr cannot be parsed as a
     *         valid URI.
     */
    private Set getSubject(String addr) throws URISyntaxException {
        HashSet<Subject> rethash = new HashSet<Subject>();
        HashSet subj = getAnyURIClause("urn:oasis:names:tc:xacml:1.0:subject:subject-id", addr);
        rethash.add(new Subject(subj));
        return rethash;
    }

    /**
     * Given a "To" string (a "Resource" in XACML terminology), return a
     * Set containing an XACML Resource object to be utilized in a PDP.
     * @param addr A "To" string to be converted into an XACML Resource.
     * @throws URISyntaxException If the given addr cannot be parsed as a
     *         valid URI.
     */
    private Set getResource(String addr) throws URISyntaxException {
        return getAnyURIClause(EvaluationCtx.RESOURCE_ID, addr);
    }

    /**
     * Given an "Action" string, return a Set containing an XACML Action
     * object to be utilized in a PDP.
     * @param addr An "Action" string to be converted into an XACML Action.
     * @throws URISyntaxException If the given action cannot be parsed as a
     *         valid URI.
     */
    private Set getAction(String action) throws URISyntaxException {
        return getAnyURIClause("urn:oasis:names:tc:xacml:1.0:action:action-id", action);
    }

    /**
     * Determine if the given "From", "To", and "Action" parameters are
     * authorized given the PDP (Policy Decision Point) created when the
     * constructor read in a set of XACML policy files.  This is the main
     * method for this class.  The three parameters represent "Is a message
     * from an entity at fromAddr allowed to perform the specified action on
     * an entity at toAddr?"  If so, then return true. 
     * @param fromAddr A URI representing a "Subject" in an XACML policy
     *        file.
     * @param toAddr A URI representing a "Resource" in an XACML policy file.
     * @param action A URI representing an "Action" in an XACML policy file.
     * @return True if fromAddr is authorized to perform action on toAddr.
     *         False otherwise.  Note that in Sun's XACML there are four
     *         possible Results when running a query against a PDP: Permit,
     *         Deny, Indeterminate, and Not Applicable.  In this method,
     *         only Deny returns false.  The other three Results return
     *         true.
     */
    public boolean authorizeMessage(String fromAddr, String toAddr, String action) {
        boolean retval = false;
        RequestCtx requestCtx = null;
        try {
            requestCtx = new RequestCtx(getSubject(fromAddr), getResource(toAddr), getAction(action), new HashSet());
        } catch (Exception e) {
            LOG.error("Unable to create requestCtx: " + e);
        }
        ResponseCtx responseCtx = pdp.evaluate(requestCtx);
        Iterator it = responseCtx.getResults().iterator();
        while (it.hasNext()) {
            Result result = (Result) (it.next());
            int decision = result.getDecision();
            if (decision == Result.DECISION_PERMIT) {
                retval = true;
            } else if (decision == Result.DECISION_DENY) {
                retval = false;
                break;
            }
        }
        return retval;
    }
}
