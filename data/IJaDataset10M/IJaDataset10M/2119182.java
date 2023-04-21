package cn.csust.net2.manager.client.module;

import cn.csust.net2.manager.client.panel.NoticeManagePanel;
import cn.csust.net2.manager.shared.util.ClassForNameAble;

@ClassForNameAble
public class NoticeManager extends BaseModule {

    public NoticeManager() {
        this.setText("公告管理");
        this.setId("公告管理");
        this.setClosable(true);
        NoticeManagePanel panel = new NoticeManagePanel();
        this.add(panel);
        this.addStyleName("pad-text");
    }
}
