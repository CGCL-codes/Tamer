package org.bpaul.rtalk.thread;

import java.util.List;
import javax.swing.SwingWorker;
import org.bpaul.rtalk.protocol.Avatar;
import org.bpaul.rtalk.ui.PostLogin;

public class AvatarTask extends SwingWorker<Avatar, String> {

    private Avatar avatar;

    public AvatarTask(Avatar avatar) {
        this.avatar = avatar;
    }

    public Avatar doInBackground() {
        updateStatus("1");
        try {
            avatar.fetchKey();
        } catch (Exception e) {
            e.printStackTrace();
            updateStatus("4");
            return null;
        }
        updateStatus("2");
        try {
            avatar.fetchImg();
        } catch (Exception e) {
            e.printStackTrace();
            updateStatus("4");
            return null;
        }
        updateStatus("3");
        return avatar;
    }

    private void updateStatus(String state) {
        publish(state);
    }

    public void process(List<String> msgs) {
        for (String msg : msgs) {
            PostLogin.getInstance().getUiUpdater().updateAvatarFetch(Integer.parseInt(msg));
        }
    }

    public void done() {
        try {
            Avatar avatar = get();
            PostLogin.getInstance().getUiUpdater().updateAvatarFetch(avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
