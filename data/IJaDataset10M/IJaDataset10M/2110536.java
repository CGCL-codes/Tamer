package org.stateengine.action;

import java.io.Serializable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.stateengine.entity.IStateEngineUser;
import org.stateengine.state.IStateContainer;
import org.stateengine.state.State;
import org.stateengine.storage.IDBConnection;

public interface IAjaxActionOnState extends Serializable {

    public String getLabel();

    public void perform(IDBConnection connection, IStateEngineUser loggedUser, HttpSession sess, HttpServletRequest request, HttpServletResponse response, IStateContainer parent, State state, ServletContext servletContext);
}
