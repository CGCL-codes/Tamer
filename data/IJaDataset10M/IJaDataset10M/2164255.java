package uk.ac.lkl.migen.system.cdst.model.net;

import uk.ac.lkl.common.util.collections.event.ListAdapter;
import uk.ac.lkl.common.util.collections.event.ListEvent;
import uk.ac.lkl.migen.system.cdst.model.TeacherModel;
import uk.ac.lkl.migen.system.server.MiGenServerCommunicator;
import uk.ac.lkl.migen.system.server.UserSet;

/**
 * Listens to a UserSetEntityList for new users and adds expresser model listeners to them 
 */
public class UserSetListListener extends ListAdapter<UserSet> {

    private MiGenServerCommunicator serverCommunicator;

    private TeacherModel teacherModel;

    public UserSetListListener(MiGenServerCommunicator serverCommunicator, TeacherModel teacherModel) {
        this.serverCommunicator = serverCommunicator;
        this.teacherModel = teacherModel;
    }

    public void elementAdded(ListEvent<UserSet> e) {
        UserSet userSet = e.getElement();
        teacherModel.addUserSet(userSet);
        ExpresserModelEntityList modelList = new ExpresserModelEntityList(serverCommunicator, userSet);
        modelList.addListListener(new ExpresserModelListListener(serverCommunicator, teacherModel, userSet));
        modelList.setPollingEnabled(true);
    }
}
