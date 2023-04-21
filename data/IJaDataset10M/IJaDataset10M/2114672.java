package unbbayes.controller;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextArea;
import unbbayes.gui.NetworkWindow;
import unbbayes.io.BaseIO;
import unbbayes.prs.Edge;
import unbbayes.prs.Graph;
import unbbayes.prs.Network;
import unbbayes.prs.Node;
import unbbayes.prs.bn.ProbabilisticNode;
import unbbayes.prs.bn.SingleEntityNetwork;
import unbbayes.prs.hybridbn.ContinuousNode;
import unbbayes.util.extension.bn.inference.IInferenceAlgorithm;

/**
 * This is a common interface for NetworkController.
 * This is a Mediator design pattern since almost all communication is managed using
 * this.
 * @author Shou Matsumoto
 *
 */
public interface INetworkMediator {

    public abstract IInferenceAlgorithm getInferenceAlgorithm();

    public abstract void setInferenceAlgorithm(IInferenceAlgorithm inferenceAlgorithm);

    public abstract SENController getSENController();

    /**
	 *  Get the single entity network.
	 *
	 * @return The single entity network.
	 */
    public abstract SingleEntityNetwork getSingleEntityNetwork();

    /**
	 * Get the network being controlled.
	 * @return The network being controlled.
	 */
    public abstract Network getNetwork();

    /**
	 * Obtains the network in a Graph format
	 * @return
	 */
    public abstract Graph getGraph();

    /**
	 * Initialize the junction tree beliefs.
	 */
    public abstract void initialize();

    /**
	 * Reset the belief of the selected node.
	 */
    public abstract void removeEvidence(Node node);

    /**
	 * Creates and shows the panel to edit the node's table.
	 * @param node The table owner.
	 */
    public abstract void createTable(Node node);

    /**
	 * Creates and shows the panel where the user can edit the 
	 * continuous node normal distribution.
	 * @param node The continuous node to create the distribution pane for.
	 * @deprecated Continuous node is no longer supported in UnBBayes core. It has 
	 * now been replaced by the CPS plugin available at http://sourceforge.net/projects/prognos/.
	 */
    public abstract void createContinuousDistribution(ContinuousNode node);

    /**
	 * Creates and shows the panel where the user can edit the discrete 
	 * node table.
	 * @param node The discrete node to create the table pan for.
	 */
    public abstract void createDiscreteTable(Node node);

    /**
	 * Construct a potential table of the given node.
	 *
	 * @param node The node to get the data for the table.
	 */
    public abstract JTable makeTable(final Node node);

    /**
	 *  Propagate the evidences of the SingleEntityNetwork.
	 */
    public abstract void propagate();

    /**
	 *  Compile the SingleEntityNetwork.
	 *
	 * @return True if it compiles with no error and false otherwise.
	 */
    public abstract boolean compileNetwork();

    /**
	 * Change the GUI to allow PN evaluation.
	 */
    public abstract void evaluateNetwork();

    /**
	 * Insert a new continuous node in the SingleEntityNetwork with 
	 * the standard label and description.
	 *
	 * @param x The x position of the new node.
	 * @param y The y position of the new node.
	 */
    public abstract Node insertContinuousNode(double x, double y);

    /**
	 * Insert a new probabilistic node in the SingleEntityNetwork with 
	 * the standard label and description.
	 *
	 * @param x The x position of the new node.
	 * @param y The y position of the new node.
	 */
    public abstract Node insertProbabilisticNode(double x, double y);

    /**
	 * Insert a new decision node in the SingleEntityNetwork with
	 * the standard label and description.
	 *
	 * @param x The x position of the new node.
	 * @param y The y position of the new node.
	 */
    public abstract Node insertDecisionNode(double x, double y);

    /**
	 * Insert a new utility node in the SingleEntityNetwork with
	 * the standard label and description.
	 *
	 * @param x The x position of the new node.
	 * @param y The y position of the new node.
	 */
    public abstract Node insertUtilityNode(double x, double y);

    /**
	 * Show the explanation properties for the given node.
	 * @param node The node to show the explanation properties.
	 */
    public abstract void showExplanationProperties(ProbabilisticNode node);

    /**
	 * Insert a new edge in the network.
	 *
	 * @param edge The new edge to be inserted.
	 */
    public abstract boolean insertEdge(Edge edge) throws Exception;

    /**
	 *  Insert a new state for the given node.
	 *
	 * @param node The selected node to insert the new state.
	 */
    public abstract void insertState(Node node);

    /**
	 *  Remove the last state from the given node.
	 *
	 * @param node The selected node to remove the last state.
	 */
    public abstract void removeState(Node node);

    public abstract void deleteSelected(Object selected);

    /**
	 * It does nothing when a key is typed.
	 *
	 * @param e The <code>KeyEvent</code> that is passed from the <code>KeyListener</code>
	 * @see KeyEvent
	 * @see KeyListener
	 */
    public abstract void keyTyped(KeyEvent e);

    /**
	 *  Delete all selected objects of the network when the key (KeyEvent.VK_DELETE) is
	 *  pressed.
	 *
	 * @param e The <code>KeyEvent</code> that is passed from the <code>KeyListener</code>
	 * @see KeyEvent
	 * @see KeyListener
	 */
    public abstract void keyPressed(KeyEvent e);

    /**
	 * It does nothing when a key is released.
	 *
	 * @param e The <code>KeyEvent</code> that is passed from the <code>KeyListener</code>
	 * @see KeyEvent
	 * @see KeyListener
	 */
    public abstract void keyReleased(KeyEvent e);

    /**
	 *  Get the network window.
	 *
	 * @return    The network window.
	 */
    public abstract NetworkWindow getScreen();

    /**
	 * Save the network image to a file.
	 */
    public abstract void saveNetImage();

    /**
	 * Save the table image to a file.
	 */
    public abstract void saveTableImage();

    /**
	 * This is just a delegator to {@link #getLogContent()},
	 * which is a delegator to {@link SingleEntityNetwork#getLog()}.
	 * The visibility of {@link #getLogContent()} was not altered, for
	 * backward compatibility.
	 * @return log content
	 */
    public abstract String getLog();

    /**
	 *  Show every single step taken during the compilation of the 
	 *  SingleEntityNetwork.
	 *  @deprecated avoid GUI methods in controllers. Use {@link unbbayes.gui.util.TextAreaDialog} instead
	 */
    public abstract JDialog showLog();

    /**
	 * Open Warning dialog.
	 * Currently, this is only a stub.
	 */
    public abstract void openWarningDialog();

    /**
	 * Close current warning dialog.
	 * This is a stub yet.
	 */
    public abstract void closeWarningDialog();

    /**
	 * Preview the log printing.
	 * @deprecated moved to {@link unbbayes.gui.util.TextAreaDialog}
	 */
    public abstract void previewPrintLog(final JTextArea texto, final JDialog dialog);

    /**
	 * Preview the table printing.
	 */
    public abstract void previewPrintTable();

    /**
	 * Preview the net printing.
	 */
    public abstract void previewPrintNet(final JComponent rede, final Rectangle retangulo);

    /**
	 * Print the given area of the given network.
	 *
	 * @param network A component representing the graphical 
	 * representation of the network to be printed.
	 * @param rectangle The area to be printed.
	 */
    public abstract void printNet(final JComponent network, final Rectangle rectangle);

    /**
	 * Print the table.
	 */
    public abstract void printTable();

    /**
	 * Method responsible for calculating the network border. If there are
	 * selected objects, the resulting rectangle consider only these objects.
	 * Otherwise, all objects from the network are considered.
	 */
    public abstract Rectangle calculateNetRectangle();

    /**
	 * Returns the selected node.
	 * @return the selected node.
	 */
    public abstract Node getSelectedNode();

    /**
	 * Selects a node
	 * @param node
	 */
    public abstract void selectNode(Node node);

    /**
	 * Unselects all graphical elements
	 */
    public abstract void unselectAll();

    /**
	 * This is the class responsible for storing the network controlled by this controller.
	 * {@link #setBaseIO(BaseIO)} must be set to a correct controller depending to what type of
	 * network this controller is dealing.
	 * @return the baseIO
	 */
    public abstract BaseIO getBaseIO();

    /**
	 * This is the class responsible for storing the network controlled by this controller.
	 * {@link #setBaseIO(BaseIO)} must be set to a correct controller depending to what type of
	 * network this controller is dealing.
	 * @param baseIO the baseIO to set
	 */
    public abstract void setBaseIO(BaseIO baseIO);

    /**
	 * @param screen the screen to set
	 */
    public abstract void setScreen(NetworkWindow screen);
}
