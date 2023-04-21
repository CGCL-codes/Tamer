package com.chronomus.workflow.execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.chronomus.workflow.definition.Workflow;
import com.chronomus.workflow.execution.expressions.Expression;
import com.chronomus.workflow.execution.expressions.primitives.Primitive;

public class MethodCall implements Expression, Task {

    private String name;

    private List<Expression> parameters;

    private final Workflow workflow;

    public MethodCall(String name, Workflow workflow) {
        this.name = name;
        this.workflow = workflow;
    }

    public MethodCall(MethodCall methodCall, String serviceName) {
        this.name = serviceName;
        this.workflow = methodCall.workflow;
        this.parameters = methodCall.parameters;
    }

    public void setParameters(List<Expression> parseParameters) {
        this.parameters = parseParameters;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getParameters() {
        return parameters != null ? Collections.unmodifiableList(parameters) : Collections.<Expression>emptyList();
    }

    @Override
    public void run(ServiceContext context) throws ExecutionException {
        evaluate(context.getVariables());
    }

    @Override
    public Primitive evaluate(VariableStore context) throws ExecutionException {
        return workflow.getService(name).run(evaluateParameters(context), context);
    }

    private List<Primitive> evaluateParameters(VariableStore context) throws ExecutionException {
        List<Primitive> values = new ArrayList<Primitive>();
        for (Expression expr : this.parameters) {
            values.add(expr.evaluate(context));
        }
        return values;
    }

    public boolean isJobService() {
        return workflow.getService(this.name) instanceof JobService;
    }

    public Expression getOutput() {
        List<Expression> outputs = workflow.getJobs().get(this.name).getOutputs();
        if (outputs == null) {
            return null;
        }
        return outputs.iterator().next();
    }

    public Workflow getWorkflow() {
        return workflow;
    }
}
