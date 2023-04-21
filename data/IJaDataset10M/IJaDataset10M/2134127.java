package net.solosky.maplefetion.client.notify;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.client.dialog.ChatDialog;
import net.solosky.maplefetion.client.dialog.DialogState;
import net.solosky.maplefetion.sipc.SipcNotify;

/**
 * 
 * 好友离开对话的通知
 *
 * @author solosky <solosky772@qq.com>
 */
public class ByeNotifyHandler extends AbstractNotifyHandler {

    @Override
    public void handle(SipcNotify notify) throws FetionException {
        Buddy buddy = this.context.getFetionStore().getBuddyByUri(notify.getFrom());
        if (buddy != null) {
            ChatDialog dialog = this.context.getDialogFactory().findChatDialog(buddy);
            if (dialog != null && dialog.getState() != DialogState.CLOSED) {
                dialog.closeDialog();
            }
        }
    }
}
