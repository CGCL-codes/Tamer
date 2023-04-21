package org.nex.ts.agent.merge;

import org.nex.ts.TopicSpacesException;
import org.nex.ts.smp.api.ISubjectProxy;
import org.nex.ts.smp.SubjectMapProvider;
import org.nex.ts.smp.api.ISubjectProxyMergeAgent;
import org.nex.ts.smp.api.ISubjectProxyMergeRule;

/**
 * <p>Title: TopicSpaces SubjectMap Engine</p>
 * <p>Description: Implementation of the TMRM</p>
 * <p>Copyright: Copyright (c) 2006, Jack Park</p>
 * <p>Company: NexistGroup</p>
 * @version 1.0
 * @author Jack Park
 */
public abstract class AbstractMergeRule implements ISubjectProxyMergeRule {

    protected String name = "unnamed rule";

    public AbstractMergeRule() {
    }

    protected SubjectMapProvider smp;

    protected ISubjectProxyMergeAgent host;

    public void setMergeAgent(ISubjectProxyMergeAgent agent) {
        host = agent;
    }

    public void setSubjectMapProvider(SubjectMapProvider s) {
        smp = s;
    }

    /**
	 * <p>
	 * Apply this rule's tests. In this case, the test is for comparison of
	 * <code>subjectName</code> fields. If they compare, the subjects are
	 * then deemed to be mergable, return <code>true</code>
	 * </p>
	 * @param coreProxy ISubjectProxy
	 * @param otherProxy ISubjectProxy
	 * @return double
	 * @throws TopicSpacesException
	 */
    public abstract double testConditions(ISubjectProxy coreProxy, ISubjectProxy otherProxy) throws TopicSpacesException;

    /**
	 * Return 
	 */
    public abstract String getMergeReason();

    public abstract void initialize() throws TopicSpacesException;

    public String getName() {
        return name;
    }
}
