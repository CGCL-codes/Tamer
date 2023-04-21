package hudson.zipscript.parser.template.element.lang.variable;

import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.exception.ExecutionException;
import hudson.zipscript.parser.template.element.Element;
import hudson.zipscript.parser.template.element.lang.variable.adapter.RetrievalContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DynamicChild implements VariableChild {

    private Element evaluator;

    private Map pathChildren = new HashMap();

    private RetrievalContext retrievalContext;

    private String contextHint;

    public DynamicChild(Element evaluator) {
        this.evaluator = evaluator;
    }

    public Object execute(Object parent, ExtendedContext context) throws ExecutionException {
        if (null == parent) return null;
        String path = evaluator.objectValue(context).toString();
        List children = (List) pathChildren.get(path);
        if (null == children) {
            children = initialize(path);
            pathChildren.put(path, children);
        }
        for (Iterator i = children.iterator(); i.hasNext(); ) {
            if (null == parent) return null;
            parent = ((VariableChild) i.next()).execute(parent, context);
        }
        return parent;
    }

    protected List initialize(String path) {
        List children = new ArrayList();
        StringTokenizer st = new StringTokenizer(path, ".");
        while (st.hasMoreElements()) {
            children.add(new PropertyChild(st.nextToken()));
        }
        return children;
    }

    public boolean shouldReturnSomething() {
        return true;
    }

    public String getPropertyName() {
        return evaluator.toString();
    }

    public RetrievalContext getRetrievalContext() {
        return retrievalContext;
    }

    public void setRetrievalContext(RetrievalContext retrievalContext) {
        this.retrievalContext = retrievalContext;
    }

    public String getContextHint() {
        return contextHint;
    }

    public void setContextHint(String contextHint) {
        this.contextHint = contextHint;
    }
}
