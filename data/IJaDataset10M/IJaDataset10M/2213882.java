package ac.hiu.j314.elmve;

import java.io.*;
import java.util.*;

public class Transporter extends RealElm {

    public Transporter() {
        elm2DUIClass = "ac.hiu.j314.elmve.ui.EHidden2DUI";
    }

    public Serializable get2DUIData() {
        return null;
    }

    public Serializable get2DUIRepaintData() {
        return null;
    }

    public void transportAgent(Order o) {
        ElmAgent agent = (ElmAgent) o.getArgAt(0);
        MyRequest r = makeMyRequest(agent, "prepareForTranslation", null);
        prepareForReply(r, "transportAgent2", o.getArgs());
        sendMessage(r);
    }

    public void transportAgent2(ReplySet rs) {
        ElmAgent agent = (ElmAgent) rs.getArgAt(0);
        agent.engine.suspend();
        Request r = makeRequest(agent.parent, "delElm", agent);
        prepareForReply(r, "transportAgent3", rs.getArgs());
        sendMessage(r);
    }

    public void transportAgent3(ReplySet rs) {
        ElmAgent agent = (ElmAgent) rs.getArgAt(0);
        String location = rs.getStringArgAt(1);
        String elmVELocation = W.getElmVEPath(location);
        String localPath = W.getLocalPath(location);
        Elm t = getElm(elmVELocation + "/transporter");
        agent.prepareForTranslation();
        sendMessage(makeOrder(t, "catchAgent", W.pp(agent, localPath)), 1000);
    }

    public void catchAgent(Order o) {
        ElmAgent agent = (ElmAgent) o.getArgAt(0);
        if (agent instanceof ElmAvatar) {
            ((ElmAvatar) agent).lastUpdateTime = -1;
            ((ElmAvatar) agent).lastElms = new ArrayList();
        }
        String path = o.getStringArgAt(1);
        LocalElm elm = (LocalElm) getElm(path);
        elm.addElm(agent);
        agent.initOwnEngine("ElmAgent", 2);
        if (agent instanceof ElmAvatar) {
            sendMessage(makeOrder(((ElmAvatar) agent).client, "bindAvatar", new ElmStub(elmVE, agent)));
        }
    }
}
