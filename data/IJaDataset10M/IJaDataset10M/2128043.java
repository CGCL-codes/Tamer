package net.sf.echopm.sample.ui;

import net.sf.echopm.controller.AbstractController;
import net.sf.echopm.login.InitAppEvent;
import net.sf.echopm.navigation.WorkflowDisposition;
import net.sf.echopm.navigation.event.OpenPanelEvent;
import net.sf.echopm.panel.PanelActionType;
import net.sf.echopm.sample.Repository;
import net.sf.echopm.sample.login.User;
import nextapp.echo2.app.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author ron
 */
@Controller
@Scope("prototype")
public class NavigationInitializationController extends AbstractController {

    static final long serialVersionUID = 0;

    private final Repository repository;

    /**
	 * @param repository
	 */
    @Autowired
    public NavigationInitializationController(Repository repository) {
        super();
        this.repository = repository;
        addEventTypeToListenFor(InitAppEvent.class);
    }

    /**
	 * @see nextapp.echo2.app.event.ActionListener#actionPerformed(nextapp.echo2.app.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        if (e instanceof InitAppEvent) {
            InitAppEvent event = (InitAppEvent) e;
            User user = (User) event.getUser();
            fireEvent(new OpenPanelEvent(this, PanelActionType.Navigator, user, User.class, null, WorkflowDisposition.NewFlow));
        }
    }

    protected Repository getRepository() {
        return repository;
    }
}
