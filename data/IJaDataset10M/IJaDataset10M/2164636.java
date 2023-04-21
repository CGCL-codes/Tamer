package cn.csust.net2.manager.shared.xml;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface XmlResources extends ClientBundle {

    XmlResources INSTANCE = GWT.create(XmlResources.class);

    @Source("cn/csust/net2/manager/shared/po/Academy.hbm.xml")
    TextResource academy();

    @Source("cn/csust/net2/manager/shared/po/Activity.hbm.xml")
    TextResource activity();

    @Source("cn/csust/net2/manager/shared/po/ApplyParty.hbm.xml")
    TextResource applyParty();

    @Source("cn/csust/net2/manager/shared/po/Article.hbm.xml")
    TextResource article();

    @Source("cn/csust/net2/manager/shared/po/Authority.hbm.xml")
    TextResource authority();

    @Source("cn/csust/net2/manager/shared/po/Banji.hbm.xml")
    TextResource banji();

    @Source("cn/csust/net2/manager/shared/po/Comprehensive.hbm.xml")
    TextResource comprehensive();

    @Source("cn/csust/net2/manager/shared/po/Exam.hbm.xml")
    TextResource exam();

    @Source("cn/csust/net2/manager/shared/po/Family.hbm.xml")
    TextResource family();

    @Source("cn/csust/net2/manager/shared/po/Graduate.hbm.xml")
    TextResource graduate();

    @Source("cn/csust/net2/manager/shared/po/HeldPosition.hbm.xml")
    TextResource heldPosition();

    @Source("cn/csust/net2/manager/shared/po/Issue.hbm.xml")
    TextResource issue();

    @Source("cn/csust/net2/manager/shared/po/Major.hbm.xml")
    TextResource major();

    @Source("cn/csust/net2/manager/shared/po/Money.hbm.xml")
    TextResource money();

    @Source("cn/csust/net2/manager/shared/po/PartyTraining.hbm.xml")
    TextResource partyTraining();

    @Source("cn/csust/net2/manager/shared/po/PoorInfo.hbm.xml")
    TextResource poorInfo();

    @Source("cn/csust/net2/manager/shared/po/Punishment.hbm.xml")
    TextResource punishment();

    @Source("cn/csust/net2/manager/shared/po/RatifyJoinParty.hbm.xml")
    TextResource ratifyJoinParty();

    @Source("cn/csust/net2/manager/shared/po/Resume.hbm.xml")
    TextResource resume();

    @Source("cn/csust/net2/manager/shared/po/Reward.hbm.xml")
    TextResource reward();

    @Source("cn/csust/net2/manager/shared/po/TeacherReward.hbm.xml")
    TextResource teacherReward();

    @Source("cn/csust/net2/manager/shared/po/Tuiyourudang.hbm.xml")
    TextResource tuiyourudang();

    @Source("cn/csust/net2/manager/shared/po/Work.hbm.xml")
    TextResource work();

    @Source("cn/csust/net2/manager/shared/po/Vote.hbm.xml")
    TextResource vote();

    @Source("cn/csust/net2/manager/shared/po/Notice.hbm.xml")
    TextResource notice();

    @Source("cn/csust/net2/manager/shared/po/InnerMail.hbm.xml")
    TextResource innerMail();

    @Source("cn/csust/net2/manager/shared/po/User.hbm.xml")
    TextResource student();

    @Source("cn/csust/net2/manager/shared/po/User.hbm.xml")
    TextResource user();

    @Source("cn/csust/net2/manager/shared/po/Research.hbm.xml")
    TextResource research();

    @Source("cn/csust/net2/manager/shared/po/Table.hbm.xml")
    TextResource table();

    @Source("cn/csust/net2/manager/shared/po/Role.hbm.xml")
    TextResource role();

    @Source("cn/csust/net2/manager/shared/po/FileNotification.hbm.xml")
    TextResource fileNotification();

    @Source("cn/csust/net2/manager/shared/po/FileLoadNotification.hbm.xml")
    TextResource fileLoadNotification();

    @Source("cn/csust/net2/manager/shared/po/Parameter.hbm.xml")
    TextResource parameter();
}
