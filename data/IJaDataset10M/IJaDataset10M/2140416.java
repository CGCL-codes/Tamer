package net.sf.doolin.app.sc.console.task;

import net.sf.doolin.app.sc.console.bean.ConnectBean;
import net.sf.doolin.app.sc.console.bean.ServerBean;
import net.sf.doolin.app.sc.engine.Engine;
import net.sf.doolin.app.sc.engine.remote.RemoteClientFactory;
import net.sf.doolin.app.sc.game.SCClientResponse;
import net.sf.doolin.app.sc.game.SCClientState;
import net.sf.doolin.app.sc.game.SCGameGenerator;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.action.task.AbstractGUITask;
import net.sf.doolin.gui.window.opener.ViewOpener;
import org.springframework.beans.factory.annotation.Required;

public class ConnectTask extends AbstractGUITask<ConnectBean, ServerBean> {

    private ViewOpener<ServerBean> serverView;

    private RemoteClientFactory<Engine<SCGameGenerator, SCClientState, SCClientResponse>> clientFactory;

    @Override
    public ServerBean doInBackground(ActionContext actionContext, ConnectBean connectBean) throws Exception {
        Engine<SCGameGenerator, SCClientState, SCClientResponse> client = this.clientFactory.createRemoteClient(Engine.class, connectBean.getUrl(), connectBean.getUser(), connectBean.getPassword());
        ServerBean server = new ServerBean(connectBean.getUrl(), client);
        return server;
    }

    @Override
    public void doOnResult(ActionContext actionContext, ConnectBean bean, ServerBean result) {
        this.serverView.openView(actionContext, result);
    }

    @Required
    public void setClientFactory(RemoteClientFactory<Engine<SCGameGenerator, SCClientState, SCClientResponse>> clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Required
    public void setServerView(ViewOpener<ServerBean> serverView) {
        this.serverView = serverView;
    }
}
