package repast.simphony.demo.layout;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;

/**
 * 
 * @author tatara
 *
 */
public class LayoutContextBuilder implements ContextBuilder<Node> {

    /**
	 * Builds and returns a context. Building a context consists of filling it with
	 * agents, adding projects and so forth. When this is called for the master context
	 * the system will pass in a created context based on information given in the
	 * model.score file. When called for subcontexts, each subcontext that was added
	 * when the master context was built will be passed in.
	 *
	 * @param context
	 * @return the built context.
	 */
    public Context<Node> build(Context<Node> context) {
        new NetworkBuilder<Node>("Network 1", context, true).buildNetwork();
        new NetworkBuilder<Node>("Network 2", context, false).buildNetwork();
        context.add(new Node());
        return context;
    }
}
