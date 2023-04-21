package org.mb.micromvc.controller.base;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Screen;
import javax.microedition.midlet.MIDlet;
import org.mb.micromvc.controller.AbstractController;
import org.mb.micromvc.model.AbstractModel;
import org.mb.session.Conversation;

/**
 * this class extends all other controllers
 *
 * @author gubriansky
 *
 */
public abstract class BaseController extends AbstractController {

    /**
	 * form key to conversation
	 */
    protected static final String FORM_KEY = "_FORM";

    /**
	 * model key to conversation
	 */
    protected static final String MODEL_KEY = "_MODEL";

    /**
	 * message key to conversation
	 */
    protected static final String MESSAGE_KEY = "MESSAGE";

    /**
	 * navigation key to conversation
	 */
    protected static final String NAVIGATION_KEY = "NAVIGATION";

    /**
	 * controller midlet
	 */
    protected static MIDlet midp;

    /**
	 * renderView is been called from MicroMIDlet on refresh
	 *
	 * @param midp
	 * @return
	 */
    public Display renderView(MIDlet midp) {
        Screen form = getForm();
        Display d = null;
        if (form != null) {
            this.midp = midp;
            d = Display.getDisplay(midp);
            d.setCurrent(form);
        }
        return d;
    }

    /**
	 * if there is form in conversation returns that object else calls
	 * constructForm and make new form object
	 *
	 * @return form
	 */
    protected final Screen getForm() {
        Screen form = null;
        if (c.getAttribute(FORM_KEY) != null) {
            form = (Screen) c.getAttribute(FORM_KEY);
            c.removeAttribute(FORM_KEY);
        }
        if (form == null) {
            form = constructForm();
            decorateForm(form);
            c.setAttribute(FORM_KEY, form);
        }
        return form;
    }

    /**
	 * basicaly this class is caled from getForm and can be used to add special
	 * functions to form after its construct
	 *
	 * @param form
	 */
    protected void decorateForm(Screen form) {
    }

    /**
	 * @return
	 */
    protected abstract AbstractModel getModel();

    /**
	 * @return
	 */
    protected abstract String[] getComponentList();

    /**
	 * prepare conversation to forward od backward next controller
	 *
	 * if param is false it add new conversation, sets midlet and cancel
	 * controller will be this
	 *
	 * if param id true than last conversation is removed
	 *
	 * @param back
	 */
    protected void prepareConversation(boolean back) {
        if (!back) {
            getConversation().addNewMap();
            getConversation().setAttribute(Conversation.MIDLET, midp);
            getConversation().setAttribute(Conversation.CANCEL_CONTROLLER, this.getClass());
        } else {
            getConversation().removeLastMap();
        }
    }

    /**
	 * returns instance of controller in param
	 *
	 * @param newContr
	 * @return
	 */
    protected Object getNewController(Class newContr) {
        try {
            Object obj = newContr.newInstance();
            ((BaseController) obj).setConversation(getConversation());
            return obj;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * basic action to go backward from controller
	 */
    protected void actionBack() {
        BaseController obj = (BaseController) getBackController();
        prepareConversation(true);
        getConversation().setAttribute(Conversation.CONTROLLER, obj);
        obj.setConversation(c);
        getConversation().getMIDlet().refreshDisplay();
    }

    /**
	 * binds screen to model
	 *
	 * @param form
	 */
    protected abstract void bind(Screen form);
}
