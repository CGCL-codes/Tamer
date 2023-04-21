package com.css.notifier.personnel.type;

import com.css.notifier.notifyType.NotifyTypeDaiBanXinXi;
import com.css.notifier.notifyType.NotifyTypeGongZuoTongZhi;

/**
 * Created by IntelliJ IDEA.
 * User: liuzhy
 * Date: 2010-2-24
 * Time: 14:25:36
 * Des:不同的用户会展示不同的信息，具体展示什么信息，可以通过信息类型的装饰者模式进行控制
 */
public class PersonnelBaoSongRenYuan extends PersonnelType {

    public String notifyByPersonnelType() {
        notifyType = new NotifyTypeDaiBanXinXi();
        notifyType = new NotifyTypeGongZuoTongZhi(notifyType);
        notifyType.notifyMessage();
        return null;
    }
}
