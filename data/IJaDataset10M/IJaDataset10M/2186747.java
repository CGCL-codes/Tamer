package edu.indiana.extreme.xbaya.workflow.proxy;

import java.net.URISyntaxException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import edu.indiana.extreme.xbaya.wf.Workflow;
import xsul.lead.LeadContextHeader;

public interface WorkflowContext {

    public void prepare(WorkflowClient client, Workflow workflow) throws GSSException, URISyntaxException;

    public LeadContextHeader getHeader();

    public GSSCredential getCredentials();

    public String getTopic();
}
