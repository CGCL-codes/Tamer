package eu.planets_project.pp.plato.validators;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.swing.tree.TreeModel;
import org.apache.commons.logging.Log;
import org.jboss.seam.faces.FacesMessages;
import eu.planets_project.pp.plato.model.tree.TreeNode;
import eu.planets_project.pp.plato.util.PlatoLogger;

/**
 * Performs validation for the tree.
 *
 * @author Florian Motlik
 */
public class TreeValidator implements ITreeValidator {

    private static final Log log = PlatoLogger.getLogger(TreeValidator.class);

    private INodeValidator validator;

    /**
     * Traverses through the CoreTreeTable and validates each TreeNode in the CoreTreeTable.
     *
     * Overrides {@link eu.planets_project.pp.plato.validators.ITreeValidator#validate(TreeNode, INodeValidator, INodeValidator, List, boolean)}
     *
     * @see eu.planets_project.pp.plato.validators.ITreeValidator#validate(TreeModel, CoreTreeTable, INodeValidator, List, boolean)
     */
    public boolean validate(TreeNode node, INodeValidator validator, List<TreeNode> nodes, boolean showValidationErrors) {
        List<String> messages = new ArrayList<String>();
        this.validator = validator;
        boolean validates = true;
        validates = validateRow(node, messages, nodes);
        if (showValidationErrors) {
            for (String errorMessage : messages) {
                log.debug("Error at: " + errorMessage.toString());
                FacesMessages.instance().add(FacesMessage.SEVERITY_ERROR, errorMessage);
            }
        }
        log.debug(validates);
        return validates;
    }

    /**
     * This Method is for recursively traversing through the tree and
     * validating every single TreeNode in the tree.
     *
     * @param nodeList
     *            ErrorMessages to be displayed
     * @param nodes
     *            Nodes and Leaves can add themselves to this list if they fail
     * @return True if the Tree Validates, false if not.
     */
    private boolean validateRow(TreeNode node, List<String> nodeList, List<TreeNode> nodes) {
        boolean validates = true;
        boolean valid = this.validator.validateNode(node, nodeList, nodes);
        log.debug("Validator is: " + valid + " " + node);
        if (!valid) {
            log.debug("Not Completely Specified");
            validates = false;
        }
        if (!node.isLeaf()) {
            for (TreeNode n : node.getChildren()) {
                if (!validateRow(n, nodeList, nodes)) {
                    validates = false;
                }
            }
        }
        return validates;
    }
}
