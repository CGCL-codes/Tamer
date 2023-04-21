package fr.jussieu.gla.wasa.monitor.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.lc.util.swing.DialogDisplay;
import com.lc.util.swing.IDialogValidator;
import fr.jussieu.gla.wasa.core.Engine;
import fr.jussieu.gla.wasa.monitor.event.NotifyingAlgorithm;
import fr.jussieu.gla.wasa.monitor.gui.application.ApplicationContext;
import fr.jussieu.gla.wasa.monitor.gui.event.RunNodeCreatedEvent;
import fr.jussieu.gla.wasa.monitor.model.EngineNode;
import fr.jussieu.gla.wasa.monitor.model.RunNode;

/**
 * Creates a RunNode.
 * @author Laurent Caillette
 * @version $Revision: 1.6 $ $Date: 2002/04/10 17:24:01 $
 */
public class CreateRunNodeAction extends AbstractAction {

    public CreateRunNodeAction(ApplicationContext appContext) {
        super(appContext);
    }

    public void actionPerformed(ActionEvent event) {
        final JTextField jTextFieldValue = new JTextField();
        jTextFieldValue.setText("1");
        jTextFieldValue.setHorizontalAlignment(SwingConstants.RIGHT);
        DialogDisplay display = new DialogDisplay(null, jTextFieldValue, "Run Node count");
        display.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        display.setCenteringOnScreen(true);
        final int[] nodeCountRef = new int[1];
        display.setValidator(new IDialogValidator() {

            public boolean validate(Object message, Object choice) {
                if (choice == IDialogValidator.FORCE_CLOSING_CHOICE) {
                    return false;
                }
                try {
                    int nodeCount = Integer.parseInt(jTextFieldValue.getText());
                    nodeCountRef[0] = nodeCount;
                    return true;
                } catch (NumberFormatException ex) {
                }
                return false;
            }
        });
        if (display.show()) {
            createRunNodes(nodeCountRef[0]);
        }
    }

    public String getName() {
        return "Create Run nodes...";
    }

    private void createRunNodes(int runNodeCount) {
        RunNode runNode = null;
        EngineNode parent = (EngineNode) getApplicationContext().getSelectionManager().getSelectedNode();
        for (int i = 0; i < runNodeCount; i++) {
            Engine engine = new Engine(getApplicationContext().getProblemNode().getProblem());
            engine.configure(parent.getEngineNodeParameters());
            NotifyingAlgorithm algorithm = new NotifyingAlgorithm(engine);
            engine.setRandom(getApplicationContext().getRandom());
            runNode = new RunNode(parent, engine);
            RunNodeCreatedEvent event = new RunNodeCreatedEvent(runNode);
            getApplicationContext().getEventBus().postEvent(event);
        }
        if (runNode != null) {
            getApplicationContext().getSelectionManager().selectNode(runNode);
        }
    }
}
