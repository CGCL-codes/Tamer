package com.wonebiz.crm.client.login;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ProgressInfo extends Window {

    private Label info;

    private static final String BEGIN = "开始验证...";

    private static final String LOCAL = "检查输入状态...";

    private static final String SERVER = "读取远程数据...";

    private static final String END = "验证结束...";

    private static final String OK = "验证成功,登入中...";

    private static final String INI = "初始化欢迎页面...";

    public ProgressInfo() {
        setShowHeader(false);
        setWidth(110);
        setHeight(40);
        setAutoCenter(true);
        setCanDrag(false);
        setCanDragResize(false);
        setCanDragReposition(false);
        setOpacity(70);
        VLayout vlay = new VLayout();
        vlay.setWidth100();
        vlay.setHeight100();
        vlay.setOpacity(70);
        vlay.setBackgroundColor("white");
        HLayout hlay1 = new HLayout();
        hlay1.setWidth100();
        hlay1.setHeight(33);
        hlay1.setAlign(Alignment.CENTER);
        Img img = new Img("login/logo.JPG");
        img.setWidth(35);
        img.setHeight(33);
        Label l = new Label("<b>CRM</b>");
        l.setAlign(Alignment.CENTER);
        l.setWidth(60);
        HLayout hlay2 = new HLayout();
        hlay2.setWidth100();
        hlay2.setAlign(Alignment.CENTER);
        info = new Label(BEGIN);
        hlay2.addMember(info);
        HLayout hlay3 = new HLayout();
        hlay3.setWidth100();
        vlay.addMember(hlay2);
        addMember(vlay);
        show();
    }

    public void setInfoBegin() {
        info.setContents(BEGIN);
    }

    public void setInfoLocal() {
        info.setContents(LOCAL);
    }

    public void setInfoServer() {
        info.setContents(SERVER);
    }

    public void setInfoEnd() {
        info.setContents(END);
    }

    public void setInfoOk() {
        info.setContents(OK);
    }

    public void setInfoIni() {
        info.setContents(INI);
    }

    public void setInfo(String s) {
        info.setContents(s);
    }
}
