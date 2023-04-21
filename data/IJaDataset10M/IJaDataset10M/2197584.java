package org.santeplanning.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.santeplanning.action.AddEnchainementCycleAction;
import org.santeplanning.action.DelEnchainementCycleAction;
import org.santeplanning.action.EditCycleAction;
import org.santeplanning.action.EditEnchainementCycleAction;
import org.santeplanning.model.EnchainementCycleEnumeration;
import org.santeplanning.model.Utilisateur;
import org.santeplanning.model.UtilisateurAdapter;
import org.stateengine.action.GoToPreviousState;
import org.stateengine.action.IHavePreviousState;
import org.stateengine.entity.IStateEngineUser;
import org.stateengine.state.IState;
import org.stateengine.state.State;
import org.stateengine.storage.IDBConnection;
import org.stateengine.util.CollectionUtil;

public class ViewEnchainementCycleState extends State implements IHavePreviousState {

    private static final long serialVersionUID = 1L;

    private IState previousState;

    private List listEnchainement = new ArrayList();

    ;

    public ViewEnchainementCycleState(IState previousState) {
        this.previousState = previousState;
    }

    public void processRequestParameter(IDBConnection connection, IStateEngineUser loggedUser, HttpSession sess, HttpServletRequest request) {
        super.processRequestParameter(connection, loggedUser, sess, request);
    }

    protected Map initActionsMap() {
        Map actions = super.initActionsMap();
        addActionToMap(actions, new EditCycleAction("editCycle"));
        addActionToMap(actions, new DelEnchainementCycleAction("delEnchainementCycle"));
        addActionToMap(actions, new AddEnchainementCycleAction("addEnchainementCycle"));
        addActionToMap(actions, new EditEnchainementCycleAction("editEnchainementCycle"));
        addActionToMap(actions, new GoToPreviousState("back"));
        return actions;
    }

    public void updateModel(IDBConnection connection, Utilisateur utilisateur) {
        listEnchainement.clear();
        CollectionUtil.apppend(listEnchainement, EnchainementCycleEnumeration.getAll(connection));
    }

    public String forwardResponse(IDBConnection connection, IStateEngineUser loggedUser, HttpSession sess, HttpServletRequest request) {
        updateModel(connection, ((UtilisateurAdapter) loggedUser).getUtilisateur());
        return "/viewEnchainementCycle.jsp";
    }

    public List getListEnchainement() {
        return listEnchainement;
    }

    public IState getPreviousState() {
        return previousState;
    }

    public void setPreviousState(IState previousState) {
        this.previousState = previousState;
    }
}
