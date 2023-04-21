package unbbayes.util.extension.bn.inference;

import unbbayes.controller.INetworkMediator;
import unbbayes.prs.Graph;

/**
 * This is a interface which every inference algorithm should implement.
 * Plugins adding new inference algorithms should also implement this
 * interface.
 * @author Shou Matsumoto
 *
 */
public interface IInferenceAlgorithm {

    /**
	 * Sets the network (graph) treated by this algorithm.
	 * @param g : the network (graph) to set.
	 * @throws IllegalArgumentException : if the given graph is not appropriate
	 * for this algorithm.
	 */
    public void setNetwork(Graph g) throws IllegalArgumentException;

    /**
	 * @return Gets the network (graph) treated by this algorithm.
	 * 
	 */
    public Graph getNetwork();

    /**
	 * Runs the algorithm given the current state
	 * of the attributes.
	 * @throws IllegalStateException : this exception
	 * may also be used for general purpose.
	 */
    public void run() throws IllegalStateException;

    /**
	 * Obtains the name of this algorithm
	 * @return : name of this algorithm.
	 */
    public String getName();

    /**
	 * Obtains the description of this algorithm.
	 * This information is generally used as a tool tip text.
	 * @return : name of this algorithm.
	 */
    public String getDescription();

    /**
	 * Resets the algorithm and optionally the network state.
	 */
    public void reset();

    /**
	 * Propagate evidences.
	 */
    public void propagate();

    /**
	 * This method adds a listener which will be executed before/after
	 * {@link #run()}, {@link #propagate()} or {@link #reset()}.
	 * Use them if you want GUI method to be executed before/after
	 * inference algorithm methods.
	 * @param listener
	 */
    public void addInferencceAlgorithmListener(IInferenceAlgorithmListener listener);

    /**
	 * This method removes a listener which will be executed before/after
	 * {@link #run()}, {@link #propagate()} or {@link #reset()}.
	 * Use them if you want GUI method to be executed before/after
	 * inference algorithm methods.
	 * @param listener : if set to null, the implementation should clear the listeners.
	 */
    public void removeInferencceAlgorithmListener(IInferenceAlgorithmListener listener);

    /**
	 * This is a link to main controller.
	 * @param mediator
	 */
    public void setMediator(INetworkMediator mediator);

    /**
	 * This is a link to main controller.
	 * @return
	 */
    public INetworkMediator getMediator();
}
