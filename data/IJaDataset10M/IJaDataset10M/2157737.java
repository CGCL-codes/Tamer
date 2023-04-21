package com.agentfactory.astr.debugger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.agentfactory.astr.interpreter.ActionTraceLogEntry;
import com.agentfactory.astr.interpreter.PartialPlan;
import com.agentfactory.astr.interpreter.PlanRule;
import com.agentfactory.visualiser.impl.AbstractLanguageAgentSnapshot;

/**
 *
 * @author remcollier
 */
public class ASTRSnapshot extends AbstractLanguageAgentSnapshot {

    public List<String> events = new LinkedList<String>();

    public List<String> inferences = new LinkedList<String>();

    public List<String> beliefs = new LinkedList<String>();

    public List<IntentionDescription> commitments = new LinkedList<IntentionDescription>();

    public List<PlanRule> rules;

    public List<PartialPlan> plans;

    public String event;

    public List<String> options = new LinkedList<String>();

    public String option;

    public List<String> functions;

    public List<ActionTraceLogEntry> actionTrace;

    public Map<String, List<String>> programStack;

    public List<String> activePrograms;
}
