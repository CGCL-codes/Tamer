package net.solosky.maplefetion.client.response;

import java.util.Iterator;
import java.util.List;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.SuccessEvent;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.XMLHelper;
import org.jdom.Element;

/**
 *
 *
 * @author solosky <solosky772@qq.com>
 */
public class GetGroupsInfoResponseHandler extends AbstractResponseHandler {

    /**
     * @param client
     * @param dialog
     * @param listener
     */
    public GetGroupsInfoResponseHandler(FetionContext client, Dialog dialog, ActionEventListener listener) {
        super(client, dialog, listener);
    }

    @Override
    protected ActionEvent doActionOK(SipcResponse response) throws FetionException {
        FetionStore store = this.context.getFetionStore();
        if (response.getBody() != null) {
            Element root = XMLHelper.build(response.getBody().toSendString());
            List groupList = XMLHelper.findAll(root, "/results/groups/*group");
            Iterator it = groupList.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                Group group = store.getGroup(e.getAttributeValue("uri"));
                if (group != null) {
                    BeanHelper.toBean(Group.class, group, e);
                    logger.debug("Got a group:" + group);
                }
            }
        }
        return super.doActionOK(response);
    }
}
