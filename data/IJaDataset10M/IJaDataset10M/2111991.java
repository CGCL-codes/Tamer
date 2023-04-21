package nox.xml;

import java.io.Serializable;
import javax.swing.ImageIcon;
import nox.ui.common.ItemStatus;

@SuppressWarnings("serial")
public class NoxPeerStatusUnit implements Serializable {

    private String nickname;

    private String sign;

    private ItemStatus stat;

    private ImageIcon portrait;

    public NoxPeerStatusUnit(String name, String sign, ItemStatus stat, ImageIcon portr) {
        this.nickname = name;
        this.sign = sign;
        this.stat = stat;
        this.portrait = portr;
    }

    public String getNickName() {
        return nickname;
    }

    public String getSign() {
        return sign;
    }

    public ItemStatus getOnlineStatus() {
        return stat;
    }

    public ImageIcon getPortrait() {
        return portrait;
    }
}
