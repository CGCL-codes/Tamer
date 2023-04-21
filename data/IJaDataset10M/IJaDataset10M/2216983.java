package net.sourceforge.jwakeup;

import org.apache.log4j.Logger;
import javax.servlet.jsp.PageContext;
import java.util.Map;

/**
 * Controller class for the edit Machine view.
 *
 * @author: Matthias
 * Date: 10.01.2008
 */
public class EditMachineController extends MachineController {

    @SuppressWarnings({ "UnusedDeclaration" })
    private static final Logger LOGGER = Logger.getLogger(EditMachineController.class);

    public static final String ACTION_EDIT_MACHINE = "update";

    public static final String CURRENT_MACHINE = "currentMachine";

    public ControllerResult handleRequest(PageContext pc) {
        ControllerResult result = super.handleRequest(pc);
        Machine m = DatabaseAdapter.getMachineById(InetTools.asInt(getParams().get(ID)));
        if (m != null) {
            result.setAttribute(CURRENT_MACHINE, m);
        } else {
            LOGGER.warn("Couldn't determine Machine with id: " + getParams().get(ID) + ".");
        }
        return result;
    }

    public ControllerResult update(Map<String, String> params) {
        ControllerResult result = new ControllerResult(true);
        result.addErrors(vaildate(params));
        if (!result.hasErrors()) {
            Machine m = DatabaseAdapter.getMachineById(InetTools.asInt(params.get(ID)));
            if (m != null) {
                m.setUsername(params.get(MACHINE_NAME));
                m.setIpAdress(params.get(IP_ADDRESS));
                m.setEthernetAdress(params.get(MAC_ADDRESS));
                DatabaseAdapter.update(m);
            } else {
                result.addError(ID, "The given Machine is invalid.");
            }
        }
        return result;
    }
}
