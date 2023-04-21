package org.primordion.user.app.database.school.clazz.client;

import org.primordion.user.app.database.IXhDatabaseController;
import org.primordion.user.app.database.school.XhSchool;
import org.primordion.xholon.base.IMessage;
import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.io.ISwingEntity;

/**
 * Class Controller
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8 (Created on July 2, 2009)
*/
public class ClassController extends XhSchool implements IXhDatabaseController {

    private IXholon model = null;

    private IXholon view = null;

    private IXholon worker = null;

    protected static final int STATE_INITIALIZING = 0;

    protected static final int STATE_READY = 1;

    /**
	 * Current state of the controller.
	 */
    private int controllerState = STATE_INITIALIZING;

    public void postConfigure() {
        controllerState = STATE_READY;
        super.postConfigure();
        worker.sendMessage(SIG_RETRIEVE_ALL, null, this);
    }

    public void processReceivedMessage(IMessage msg) {
        doStateMachine(controllerState, msg.getSignal(), msg);
    }

    /**
	 * Do controller state machine.
	 * @param state The current state.
	 * @param event An event.
	 * @param msg A received message.
	 */
    protected void doStateMachine(int state, int event, IMessage msg) {
        switch(state) {
            case STATE_READY:
                switch(event) {
                    case ISwingEntity.SWIG_ACTION_EVENT:
                        String command = (String) msg.getData();
                        IMessage responseMsg = null;
                        if ("add".equals(command)) {
                            responseMsg = view.sendSyncMessage(SIG_ADD, null, this);
                        } else if ("edit".equals(command)) {
                            responseMsg = view.sendSyncMessage(SIG_EDIT, null, this);
                        } else if ("delete".equals(command)) {
                            responseMsg = view.sendSyncMessage(SIG_DELETE, null, this);
                            if (responseMsg.getSignal() == SIG_YES) {
                                worker.sendMessage(SIG_DELETE, responseMsg.getData(), this);
                            }
                        } else if ("save".equals(command)) {
                            responseMsg = view.sendSyncMessage(SIG_SAVE, null, this);
                            worker.sendMessage(SIG_SAVE, responseMsg.getData(), this);
                        } else if ("cancel".equals(command)) {
                            responseMsg = view.sendSyncMessage(SIG_CANCEL, null, this);
                        } else if ("close".equals(command)) {
                            responseMsg = view.sendSyncMessage(SIG_CLOSE, null, this);
                        } else if ("refresh".equals(command)) {
                            worker.sendMessage(SIG_RETRIEVE_ALL, null, this);
                        } else {
                            System.out.println("controller: unknown command [" + command + "]");
                        }
                        break;
                    case SIG_SAVED:
                    case SIG_DELETED:
                        worker.sendMessage(SIG_RETRIEVE_ALL, null, this);
                        break;
                    case SIG_RETRIEVED:
                        break;
                    case ISwingEntity.SWIG_TABLEMODEL_EVENT:
                        break;
                    case ISwingEntity.SWIG_LISTSELECTION_EVENT:
                        view.sendMessage(SIG_REFRESH_DETAILS, msg.getData(), this);
                        break;
                    case -1056:
                        break;
                    default:
                        logger.warn("Controller received an unexpected message:" + msg);
                        break;
                }
                break;
            default:
                break;
        }
    }

    public IXholon getModel() {
        return model;
    }

    public void setModel(IXholon model) {
        this.model = model;
    }

    public IXholon getWorker() {
        return worker;
    }

    public void setWorker(IXholon worker) {
        this.worker = worker;
    }

    public IXholon getView() {
        return view;
    }

    public void setView(IXholon view) {
        this.view = view;
    }

    public int getControllerState() {
        return controllerState;
    }

    public void setControllerState(int controllerState) {
        this.controllerState = controllerState;
    }
}
