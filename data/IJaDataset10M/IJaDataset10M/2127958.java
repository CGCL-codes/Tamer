package com.eastidea.qaforum.action;

import java.util.List;
import javax.ejb.Local;
import com.eastidea.qaforum.action.base.BaseMaintAction;
import com.eastidea.qaforum.model.User;

@Local
public interface ActorQaMaintAction extends BaseMaintAction {

    public void editQa();

    public void saveQa();

    public void delQa();

    public void destroy();

    public List<User> getQaSelected();

    public void setQaSelected(List<User> qaSelected);
}
