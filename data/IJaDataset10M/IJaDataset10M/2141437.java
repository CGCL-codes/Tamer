package sanguo.actions;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import java.util.Date;
import java.util.Vector;
import org.eclipse.swt.widgets.Display;
import sanguo.dataclass.NeedResoInfo;
import sanguo.dataclass.TranspTaskComp;
import sanguo.dataclass.VilResInfo;
import sanguo.sys.GXData;
import sanguo.sys.GXDataOper;
import sanguo.sys.OpenWPanels;
import sanguo.ui.BlankPanel;
import sanguo.intro.ApplicationActionBarAdvisor;
import sanguo.sys.IAppConstants;

public class AutoDeliverNeed {

    public static int step2;

    public static boolean finish, launch = false;

    public static BlankPanel Panel_trade;

    public static void start() {
        launch = !launch;
        balance();
        final Display display = Display.getDefault();
        final Runnable delayActions = new Runnable() {

            Vector<VilResInfo> resStatus = new Vector<VilResInfo>();

            Vector<TranspTaskComp> transpTasks = new Vector<TranspTaskComp>();

            Vector<NeedResoInfo> needresoinfo = new Vector<NeedResoInfo>();

            Vector<NeedResoInfo> currenttrades = new Vector<NeedResoInfo>();

            String fBody;

            int step = 0, time = 1000, count = 0, vilnum = 0;

            String vilId;

            int[] importreso;

            int targetX, targetY, interval = 1000 * 60 * 60, interval_t, interval_d;

            Date endtime = new Date();

            public void run() {
                if (launch) {
                    switch(step) {
                        case 0:
                            Panel_trade = OpenWPanels.open("trade", "��Դ����_������");
                            Panel_trade.mission = "needtask";
                            if (Panel_trade.getBrowser().getBody() == null) {
                                System.out.println("#" + Panel_trade.getBrowser().getBody() + "#");
                                Panel_trade.getBrowser().setUrl(GXData.SERVER);
                            }
                            Panel_trade.getBrowser().addDisposeListener(new DisposeListener() {

                                public void widgetDisposed(DisposeEvent e) {
                                    launch = false;
                                    balance();
                                }
                            });
                            sendMsg("����Դ����ҳ��׼��ִ�а���������");
                            step = 1;
                            break;
                        case 1:
                            if (Panel_trade.getBrowser().br_action.equals("completed")) {
                                Panel_trade.getBrowser().execute("MM_xmlLoad('vmanage.resource');");
                                sendMsg("��ȡ��������Դ��Ϣ...");
                                currenttrades.removeAllElements();
                                resStatus.removeAllElements();
                                transpTasks.removeAllElements();
                                needresoinfo.removeAllElements();
                                needresoinfo = GXDataOper.readNeedResoVil();
                                step = 2;
                            }
                            break;
                        case 2:
                            fBody = Panel_trade.getBrowser().getBody();
                            if (fBody.indexOf("ľ�Ĵ���") != -1) {
                                resStatus = Htmlfunc.parseRStatus(fBody);
                                step = 3;
                            }
                            break;
                        case 3:
                            if (vilnum >= resStatus.size()) {
                                vilnum = 0;
                                step = 5;
                            } else {
                                vilId = GetGDstable.getVilIdByName(resStatus.elementAt(vilnum).vilname);
                                System.out.println("��ȡ" + resStatus.elementAt(vilnum).vilname + "��������Ϣ");
                                sendMsg("��ȡ" + resStatus.elementAt(vilnum).vilname + "��������Ϣ");
                                Panel_trade.getBrowser().execute("vmanage(" + vilId + ",true,'build.act&do=trade18_0&btid=18');");
                                step = 4;
                            }
                            break;
                        case 4:
                            fBody = Panel_trade.getBrowser().getBody();
                            if (fBody.indexOf("changeVillage('" + vilId) == -1) {
                                if (fBody.indexOf("ÿ֧�̶ӿ�������") != -1) {
                                    resStatus.elementAt(vilnum).trade_single = RenewVilInfo.getTradeVol(fBody);
                                    if (fBody.indexOf("����ʱ��") != -1) {
                                        interval_t = Htmlfunc.getTradeInterval(fBody);
                                        if (interval_t == -1) {
                                            System.out.println("�ȴ���...");
                                            break;
                                        }
                                        System.out.println("��������ӷ������ٻ�ʣ" + Htmlfunc.getTimeFormat(interval_t));
                                        sendMsg("��������ӷ������ٻ�ʣ" + Htmlfunc.getTimeFormat(interval_t));
                                        if (interval_t < interval) interval = interval_t;
                                        importreso = Htmlfunc.getImportReso(fBody);
                                        if ((importreso[0] + importreso[1] + importreso[2] + importreso[3]) > 0) {
                                            addNeedVil(resStatus.elementAt(vilnum).vilname, importreso, currenttrades);
                                            System.out.println(resStatus.elementAt(vilnum).vilname + "���ڽ�����Դ:" + importreso[0] + "-" + importreso[1] + "-" + importreso[2] + "-" + importreso[3]);
                                            sendMsg("�������ڽ�����Դ:" + importreso[0] + "-" + importreso[1] + "-" + importreso[2] + "-" + importreso[3]);
                                        }
                                    } else {
                                        System.out.println("���������ȫ������");
                                        sendMsg("���������ȫ������");
                                    }
                                    vilnum++;
                                    step = 3;
                                    Panel_trade.getBrowser().execute("MM_closeLeft();");
                                } else if (fBody.indexOf("��δ����") != -1) {
                                    vilnum++;
                                    step = 3;
                                    Panel_trade.getBrowser().execute("MM_closeLeft();");
                                    resStatus.elementAt(vilnum).trade_single = 0;
                                    sendMsg("������δ����");
                                }
                            }
                            break;
                        case 5:
                            for (int i = 0; i < needresoinfo.size(); i++) {
                                System.out.println(needresoinfo.elementAt(i).vilname + ":" + needresoinfo.elementAt(i).lumb + "-" + needresoinfo.elementAt(i).clay + "-" + needresoinfo.elementAt(i).iron + "-" + needresoinfo.elementAt(i).crop);
                            }
                            transpTasks = getTranspTasks(resStatus, needresoinfo, currenttrades);
                            for (int i = 0; i < transpTasks.size(); i++) {
                                System.out.println(transpTasks.elementAt(i).sourceVilName + "--" + transpTasks.elementAt(i).targetVilName + ":" + transpTasks.elementAt(i).lumb + "-" + transpTasks.elementAt(i).clay + "-" + transpTasks.elementAt(i).iron + "-" + transpTasks.elementAt(i).crop);
                            }
                            step = 6;
                            break;
                        case 6:
                            if (vilnum < transpTasks.size()) {
                                vilId = GetGDstable.getVilIdByName(transpTasks.elementAt(vilnum).sourceVilName);
                                String tvilId = GetGDstable.getVilIdByName(transpTasks.elementAt(vilnum).targetVilName);
                                targetX = GetGDstable.getXById(tvilId);
                                targetY = GetGDstable.getYById(tvilId);
                                Panel_trade.getBrowser().execute("vmanage(" + vilId + ",true,'build.act&do=trade18_0&btid=18&x=" + targetX + "&y=" + targetY + "');");
                                sendMsg(transpTasks.elementAt(vilnum).sourceVilName + " TO " + transpTasks.elementAt(vilnum).targetVilName + ": " + transpTasks.elementAt(vilnum).lumb + "----" + transpTasks.elementAt(vilnum).clay + "----" + transpTasks.elementAt(vilnum).iron + "----" + transpTasks.elementAt(vilnum).crop);
                                step = 7;
                            } else {
                                step = 20;
                            }
                            break;
                        case 7:
                            fBody = Panel_trade.getBrowser().getBody();
                            if (fBody.indexOf("changeVillage('" + vilId) == -1) {
                                if (fBody.indexOf("ÿ֧�̶ӿ�������") != -1) {
                                    TranspTaskComp tmp = transpTasks.elementAt(vilnum);
                                    System.out.println("ִ��" + tmp.sourceVilName + "to" + tmp.targetVilName + ":" + tmp.lumb + "-" + tmp.clay + "-" + tmp.iron + "-" + tmp.crop);
                                    checkTrades(fBody, transpTasks, vilnum);
                                    tmp = transpTasks.elementAt(vilnum);
                                    System.out.println("ʵ��ִ��" + tmp.sourceVilName + "to" + tmp.targetVilName + ":" + tmp.lumb + "-" + tmp.clay + "-" + tmp.iron + "-" + tmp.crop);
                                    updateNeed(needresoinfo, transpTasks.elementAt(vilnum));
                                    if (sumTask(transpTasks.elementAt(vilnum)) > 0) {
                                        Panel_trade.getBrowser().setValue("Trader_Resource_Lumber", transpTasks.elementAt(vilnum).lumb);
                                        Panel_trade.getBrowser().setValue("Trader_Resource_Clay", transpTasks.elementAt(vilnum).clay);
                                        Panel_trade.getBrowser().setValue("Trader_Resource_Iron", transpTasks.elementAt(vilnum).iron);
                                        Panel_trade.getBrowser().setValue("Trader_Resource_Crop", transpTasks.elementAt(vilnum).crop);
                                        Panel_trade.getBrowser().setValue("Trader_Target_X", targetX);
                                        Panel_trade.getBrowser().setValue("Trader_Target_Y", targetY);
                                        Panel_trade.getBrowser().clickByClass("a", "redbutton_s", 0);
                                        step = 8;
                                    } else {
                                        vilnum++;
                                        step = 6;
                                    }
                                }
                            }
                            break;
                        case 8:
                            fBody = Panel_trade.getBrowser().getBody();
                            if (fBody.indexOf("Ŀ�����") != -1) {
                                Panel_trade.getBrowser().clickByClass("a", "redbutton_s", 0);
                                step = 9;
                            } else {
                                count++;
                                if (count > 8) {
                                    step = 7;
                                }
                            }
                            break;
                        case 9:
                            fBody = Panel_trade.getBrowser().getBody();
                            if (fBody.indexOf("ϵͳ��ʾ") != -1) {
                                Panel_trade.getBrowser().clickByClass("span", "dialogconfirm", 0);
                                step = 10;
                            }
                            break;
                        case 10:
                            fBody = Panel_trade.getBrowser().getBody();
                            if (!(fBody.indexOf("Я����Դ") == -1)) {
                                if (fBody.indexOf(transpTasks.elementAt(vilnum).targetVilName + "</A>����") != -1) {
                                    System.out.println("��ȡ����ʱ��");
                                    interval_t = Htmlfunc.getTradeInterval(fBody);
                                    if (interval_t == -1) {
                                        System.out.println("�ȴ���...");
                                        break;
                                    }
                                    System.out.println("interval_t=" + interval_t);
                                    interval_d = Htmlfunc.getReachInterval(fBody, transpTasks.elementAt(vilnum).targetVilName);
                                    System.out.println(transpTasks.elementAt(vilnum).targetVilName + "interval_d=" + interval_d);
                                    updateNeedInterval(transpTasks.elementAt(vilnum).targetVilName, interval_d, needresoinfo);
                                    System.out.println("��������ӷ��ػ�ʣ" + Htmlfunc.getTimeFormat(interval_t));
                                    sendMsg("��������ӷ��ػ�ʣ" + Htmlfunc.getTimeFormat(interval_t));
                                    Panel_trade.getBrowser().execute("MM_closeLeft();");
                                    if (interval_t < interval) interval = interval_t;
                                    vilnum++;
                                    System.out.println("���������" + vilnum + "/" + transpTasks.size());
                                    if (vilnum < transpTasks.size()) {
                                        step = 6;
                                    } else {
                                        step = 20;
                                    }
                                } else {
                                    step = 7;
                                }
                            } else if (fBody.indexOf("Ŀ�����") != -1) {
                                Panel_trade.getBrowser().execute("MM_xmlLoad('build.act&do=trade18_0&btid=18&');");
                            } else if (fBody.indexOf("ÿ֧�̶ӿ�������") != -1) {
                                step = 7;
                            }
                            break;
                        case 20:
                            if (transpTasks.size() == 0) {
                                System.out.println("Ŀǰû����Ҫ���������");
                                sendMsg("Ŀǰû����Ҫ���������");
                                interval = 1000 * 60 * 30;
                            } else {
                                System.out.println("ȫ����������ִ�����!" + Htmlfunc.getTimeFormat(interval));
                                sendMsg("ȫ����������ִ�����!");
                                for (int i = 0; i < needresoinfo.size(); i++) {
                                    if (sumNeed(needresoinfo.elementAt(i)) == 0) {
                                        endtime.setTime(Htmlfunc.getLocalDate().getTime() + needresoinfo.elementAt(i).interval);
                                        GXDataOper.updateBuildTask(needresoinfo.elementAt(i).vilname, Htmlfunc.Date2String(Htmlfunc.getLocalDate()));
                                        needresoinfo.removeElementAt(i);
                                    } else {
                                        endtime.setTime(Htmlfunc.getLocalDate().getTime() + needresoinfo.elementAt(i).interval + 1000 * 60 * 20);
                                        GXDataOper.updateBuildTask(needresoinfo.elementAt(i).vilname, Htmlfunc.Date2String(endtime));
                                    }
                                }
                                for (int i = 0; i < needresoinfo.size(); i++) {
                                    System.out.println(needresoinfo.elementAt(i).vilname + ":" + needresoinfo.elementAt(i).lumb + "-" + needresoinfo.elementAt(i).clay + "-" + needresoinfo.elementAt(i).iron + "-" + needresoinfo.elementAt(i).crop);
                                }
                                GXDataOper.writeNeedResoVil(needresoinfo);
                                Panel_trade.getBrowser().clickByClass("span", "dialogconfirm", 0);
                            }
                            Panel_trade.getBrowser().execute("MM_closeLeft();");
                            vilnum = 0;
                            step = 30;
                            break;
                        case 30:
                            if (interval > 0) {
                                interval = interval - 1000;
                                sendMsg("���´�ִ�з��������ʣ" + Htmlfunc.getTimeFormat(interval));
                            } else {
                                step = 1;
                            }
                            break;
                    }
                    display.timerExec(time, this);
                }
            }
        };
        display.asyncExec(delayActions);
    }

    private static int sumTask(TranspTaskComp taskComp) {
        return taskComp.lumb + taskComp.clay + taskComp.iron + taskComp.crop;
    }

    private static boolean updateNeedInterval(String vilname, int interval, Vector<NeedResoInfo> needresoinfo) {
        for (int i = 0; i < needresoinfo.size(); i++) {
            if (needresoinfo.elementAt(i).vilname.equals(vilname)) {
                if (needresoinfo.elementAt(i).interval < interval) needresoinfo.elementAt(i).interval = interval;
                return true;
            }
        }
        return false;
    }

    private static void checkTrades(String fBody, Vector<TranspTaskComp> transpTasks, int vilnum) {
        int needTrade, finalsum;
        needTrade = transpTasks.elementAt(vilnum).lumb + transpTasks.elementAt(vilnum).clay + transpTasks.elementAt(vilnum).iron + transpTasks.elementAt(vilnum).crop;
        int tradeVol = Htmlfunc.getTradeVol(fBody);
        int tradeQual = Htmlfunc.getTradeQuant(fBody);
        if (needTrade < tradeVol * tradeQual) {
            finalsum = tradeVol * (int) Math.ceil((float) needTrade / tradeVol);
            System.out.println("��ͬ�����̶ӻ��ɶ�����" + (finalsum - needTrade));
        } else {
            finalsum = tradeVol * tradeQual;
            System.out.println("Ŀǰ�̶���������������񻹲�" + (needTrade - finalsum));
        }
        transpTasks.elementAt(vilnum).lumb = (int) (finalsum * ((float) transpTasks.elementAt(vilnum).lumb / needTrade));
        transpTasks.elementAt(vilnum).clay = (int) (finalsum * ((float) transpTasks.elementAt(vilnum).clay / needTrade));
        transpTasks.elementAt(vilnum).iron = (int) (finalsum * ((float) transpTasks.elementAt(vilnum).iron / needTrade));
        transpTasks.elementAt(vilnum).crop = (int) (finalsum * ((float) transpTasks.elementAt(vilnum).crop / needTrade));
    }

    private static Vector<TranspTaskComp> getTranspTasks(Vector<VilResInfo> resStatus_, Vector<NeedResoInfo> needresoinfo_, Vector<NeedResoInfo> currenttrades_) {
        Vector<TranspTaskComp> transpTasks_ = new Vector<TranspTaskComp>();
        Vector<TranspTaskComp> tempTask = new Vector<TranspTaskComp>();
        sortNeeds(needresoinfo_);
        for (int i = 0; i < needresoinfo_.size(); i++) {
            tempTask.removeAllElements();
            tempTask = getTradeTasksByNeednum(needresoinfo_, i, resStatus_, currenttrades_);
            for (int j = 0; j < tempTask.size(); j++) {
                transpTasks_.addElement(tempTask.elementAt(j));
            }
        }
        return transpTasks_;
    }

    private static void sortSupportVils(Vector<VilResInfo> supportVils_, NeedResoInfo needresoinfo_) {
        String targetVil = needresoinfo_.vilname;
        VilResInfo temp;
        for (int i = 0; i < supportVils_.size(); i++) {
            int min = i;
            for (int j = i + 1; j < supportVils_.size(); j++) {
                if (Htmlfunc.getVilDistance(supportVils_.elementAt(j).vilname, targetVil) < Htmlfunc.getVilDistance(supportVils_.elementAt(min).vilname, targetVil)) {
                    min = j;
                }
            }
            temp = supportVils_.elementAt(i);
            supportVils_.setElementAt(supportVils_.elementAt(min), i);
            supportVils_.setElementAt(temp, min);
        }
    }

    private static void addNeedVil(String vilname, int[] importreso, Vector<NeedResoInfo> currenttrades) {
        currenttrades.addElement(new NeedResoInfo(vilname, importreso[0] + "", importreso[1] + "", importreso[2] + "", importreso[3] + "", "0", "0"));
    }

    private static void updateNeed(Vector<NeedResoInfo> needresoinfo, TranspTaskComp transpTaskComp) {
        for (int i = 0; i < needresoinfo.size(); i++) {
            if (needresoinfo.elementAt(i).vilname.equals(transpTaskComp.targetVilName)) {
                needresoinfo.elementAt(i).lumb = needresoinfo.elementAt(i).lumb - transpTaskComp.lumb;
                needresoinfo.elementAt(i).clay = needresoinfo.elementAt(i).clay - transpTaskComp.clay;
                needresoinfo.elementAt(i).iron = needresoinfo.elementAt(i).iron - transpTaskComp.iron;
                needresoinfo.elementAt(i).crop = needresoinfo.elementAt(i).crop - transpTaskComp.crop;
            }
            if (needresoinfo.elementAt(i).lumb < 0) needresoinfo.elementAt(i).lumb = 0;
            if (needresoinfo.elementAt(i).clay < 0) needresoinfo.elementAt(i).clay = 0;
            if (needresoinfo.elementAt(i).iron < 0) needresoinfo.elementAt(i).iron = 0;
            if (needresoinfo.elementAt(i).crop < 0) needresoinfo.elementAt(i).crop = 0;
        }
    }

    private static void sortNeeds(Vector<NeedResoInfo> needresoinfo_) {
        NeedResoInfo temp;
        for (int i = 0; i < needresoinfo_.size(); i++) {
            int min = i;
            for (int j = i + 1; j < needresoinfo_.size(); j++) {
                if (sumNeed(needresoinfo_.elementAt(j)) < sumNeed(needresoinfo_.elementAt(min))) {
                    min = j;
                }
            }
            temp = needresoinfo_.elementAt(i);
            needresoinfo_.setElementAt(needresoinfo_.elementAt(min), i);
            needresoinfo_.setElementAt(temp, min);
        }
    }

    private static int sumNeed(NeedResoInfo singleneed) {
        return singleneed.lumb + singleneed.clay + singleneed.iron + singleneed.crop;
    }

    private static boolean checkNeedExit(String vilname, String type, Vector<NeedResoInfo> needresoinfo_) {
        for (int i = 0; i < needresoinfo_.size(); i++) {
            if (needresoinfo_.elementAt(i).vilname.equals(vilname)) {
                if (needresoinfo_.elementAt(i).lumb > 0) if (type.equals("lumb")) return true;
                if (needresoinfo_.elementAt(i).clay > 0) if (type.equals("clay")) return true;
                if (needresoinfo_.elementAt(i).iron > 0) if (type.equals("iron")) return true;
                if (needresoinfo_.elementAt(i).crop > 0) if (type.equals("crop")) return true;
            }
        }
        return false;
    }

    private static int getResoStatusNByName(String vilname, Vector<VilResInfo> resStatus_) {
        int i;
        for (i = 0; i < resStatus_.size(); i++) {
            if (resStatus_.elementAt(i).vilname.equals(vilname)) break;
        }
        return i;
    }

    private static boolean updateSuppTradeQuant(Vector<VilResInfo> resStatus_, String vilname, int trade_update) {
        for (int i = 0; i < resStatus_.size(); i++) {
            if (resStatus_.elementAt(i).vilname.equals(vilname)) {
                resStatus_.elementAt(i).trade_now = trade_update;
                return true;
            }
        }
        return false;
    }

    private static Vector<TranspTaskComp> getTradeTasksByNeednum(Vector<NeedResoInfo> needresoinfo_, int neednum, Vector<VilResInfo> resStatus_, Vector<NeedResoInfo> currenttrades_) {
        Vector<TranspTaskComp> tempTasks = new Vector<TranspTaskComp>();
        Vector<VilResInfo> supportVils = new Vector<VilResInfo>();
        NeedResoInfo needSingle4Reso = needresoinfo_.elementAt(neednum);
        tempTasks.removeAllElements();
        int sourceNeed = 0, trademax = 0, tquantity;
        if (needSingle4Reso.lumb > 0) {
            sourceNeed = needSingle4Reso.lumb;
            supportVils.removeAllElements();
            for (int i = 0; i < resStatus_.size(); i++) {
                if (resStatus_.elementAt(i).changk_max > IAppConstants.CHANGK_SUPPLY) {
                    if (!(resStatus_.elementAt(i).vilname.equals(needSingle4Reso.vilname) | GetGDstable.systemVils.indexOf(resStatus_.elementAt(i).vilname) != -1)) {
                        if (resStatus_.elementAt(i).trade_now > 0) {
                            if (resStatus_.elementAt(i).lumber_r > IAppConstants.SUPP_SOURCER | resStatus_.elementAt(i).lumber_now > 100000) {
                                if (!checkNeedExit(resStatus_.elementAt(i).vilname, "lumb", needresoinfo_)) if (!checkNeedExit(resStatus_.elementAt(i).vilname, "lumb", currenttrades_)) supportVils.addElement(resStatus_.elementAt(i));
                            }
                        }
                    }
                }
            }
            if (supportVils.size() > 1) sortSupportVils(supportVils, needSingle4Reso); else if (supportVils.size() == 0) {
                if (!GetGDstable.mainvil.equals(needSingle4Reso.vilname)) if (resStatus_.elementAt(getResoStatusNByName(GetGDstable.mainvil, resStatus_)).trade_now > 0) supportVils.addElement(resStatus_.elementAt(getResoStatusNByName(GetGDstable.mainvil, resStatus_)));
            }
            for (int i = 0; i < supportVils.size(); i++) {
                if (supportVils.elementAt(i).trade_now > 0) {
                    trademax = supportVils.elementAt(i).trade_now * supportVils.elementAt(i).trade_single;
                    if (sourceNeed < trademax) {
                        tempTasks.addElement(new TranspTaskComp(supportVils.elementAt(i).vilname, needSingle4Reso.vilname, sourceNeed, 0, 0, 0));
                        tquantity = supportVils.elementAt(i).trade_now - (int) Math.ceil((float) sourceNeed / supportVils.elementAt(i).trade_single);
                        System.out.println(sourceNeed + "/" + supportVils.elementAt(i).trade_single + "=" + (int) Math.ceil((float) sourceNeed / supportVils.elementAt(i).trade_single));
                        updateSuppTradeQuant(resStatus_, supportVils.elementAt(i).vilname, tquantity);
                        break;
                    } else {
                        tempTasks.addElement(new TranspTaskComp(supportVils.elementAt(i).vilname, needSingle4Reso.vilname, trademax, 0, 0, 0));
                        updateSuppTradeQuant(resStatus_, supportVils.elementAt(i).vilname, 0);
                        sourceNeed = sourceNeed - trademax;
                    }
                }
            }
        }
        if (needSingle4Reso.clay > 0) {
            sourceNeed = needSingle4Reso.clay;
            supportVils.removeAllElements();
            for (int i = 0; i < resStatus_.size(); i++) {
                if (resStatus_.elementAt(i).changk_max > IAppConstants.CHANGK_SUPPLY) {
                    if (!(resStatus_.elementAt(i).vilname.equals(needSingle4Reso.vilname) | GetGDstable.systemVils.indexOf(resStatus_.elementAt(i).vilname) != -1)) {
                        if (resStatus_.elementAt(i).trade_now > 0) {
                            if (resStatus_.elementAt(i).clay_r > IAppConstants.SUPP_SOURCER | resStatus_.elementAt(i).clay_now > 100000) {
                                if (!checkNeedExit(resStatus_.elementAt(i).vilname, "clay", needresoinfo_)) if (!checkNeedExit(resStatus_.elementAt(i).vilname, "clay", currenttrades_)) supportVils.addElement(resStatus_.elementAt(i));
                            }
                        }
                    }
                }
            }
            if (supportVils.size() > 1) sortSupportVils(supportVils, needSingle4Reso); else if (supportVils.size() == 0) if (!GetGDstable.mainvil.equals(needSingle4Reso.vilname)) if (resStatus_.elementAt(getResoStatusNByName(GetGDstable.mainvil, resStatus_)).trade_now > 0) supportVils.addElement(resStatus_.elementAt(getResoStatusNByName(GetGDstable.mainvil, resStatus_)));
            for (int i = 0; i < supportVils.size(); i++) {
                if (supportVils.elementAt(i).trade_now > 0) {
                    trademax = supportVils.elementAt(i).trade_now * supportVils.elementAt(i).trade_single;
                    if (sourceNeed < trademax) {
                        tempTasks.addElement(new TranspTaskComp(supportVils.elementAt(i).vilname, needSingle4Reso.vilname, 0, sourceNeed, 0, 0));
                        tquantity = supportVils.elementAt(i).trade_now - (int) Math.ceil((float) sourceNeed / supportVils.elementAt(i).trade_single);
                        updateSuppTradeQuant(resStatus_, supportVils.elementAt(i).vilname, tquantity);
                        break;
                    } else {
                        tempTasks.addElement(new TranspTaskComp(supportVils.elementAt(i).vilname, needSingle4Reso.vilname, 0, trademax, 0, 0));
                        updateSuppTradeQuant(resStatus_, supportVils.elementAt(i).vilname, 0);
                        sourceNeed = sourceNeed - trademax;
                        needresoinfo_.elementAt(neednum).clay = sourceNeed;
                    }
                }
            }
        }
        if (needSingle4Reso.iron > 0) {
            sourceNeed = needSingle4Reso.iron;
            supportVils.removeAllElements();
            for (int i = 0; i < resStatus_.size(); i++) {
                if (resStatus_.elementAt(i).changk_max > IAppConstants.CHANGK_SUPPLY) {
                    if (!(resStatus_.elementAt(i).vilname.equals(needSingle4Reso.vilname) | GetGDstable.systemVils.indexOf(resStatus_.elementAt(i).vilname) != -1)) {
                        if (resStatus_.elementAt(i).trade_now > 0) {
                            if (resStatus_.elementAt(i).iron_r > IAppConstants.SUPP_SOURCER | resStatus_.elementAt(i).iron_now > 100000) {
                                if (!checkNeedExit(resStatus_.elementAt(i).vilname, "iron", needresoinfo_)) if (!checkNeedExit(resStatus_.elementAt(i).vilname, "iron", currenttrades_)) supportVils.addElement(resStatus_.elementAt(i));
                            }
                        }
                    }
                }
            }
            if (supportVils.size() > 1) sortSupportVils(supportVils, needSingle4Reso); else if (supportVils.size() == 0) if (!GetGDstable.mainvil.equals(needSingle4Reso.vilname)) if (resStatus_.elementAt(getResoStatusNByName(GetGDstable.mainvil, resStatus_)).trade_now > 0) supportVils.addElement(resStatus_.elementAt(getResoStatusNByName(GetGDstable.mainvil, resStatus_)));
            for (int i = 0; i < supportVils.size(); i++) {
                if (supportVils.elementAt(i).trade_now > 0) {
                    trademax = supportVils.elementAt(i).trade_now * supportVils.elementAt(i).trade_single;
                    if (sourceNeed < trademax) {
                        tempTasks.addElement(new TranspTaskComp(supportVils.elementAt(i).vilname, needSingle4Reso.vilname, 0, 0, sourceNeed, 0));
                        tquantity = supportVils.elementAt(i).trade_now - (int) Math.ceil((float) sourceNeed / supportVils.elementAt(i).trade_single);
                        updateSuppTradeQuant(resStatus_, supportVils.elementAt(i).vilname, tquantity);
                        break;
                    } else {
                        tempTasks.addElement(new TranspTaskComp(supportVils.elementAt(i).vilname, needSingle4Reso.vilname, 0, 0, trademax, 0));
                        updateSuppTradeQuant(resStatus_, supportVils.elementAt(i).vilname, 0);
                        sourceNeed = sourceNeed - trademax;
                    }
                }
            }
        }
        if (needSingle4Reso.crop > 0) {
            sourceNeed = needSingle4Reso.crop;
            supportVils.removeAllElements();
            for (int i = 0; i < resStatus_.size(); i++) {
                if (resStatus_.elementAt(i).changk_max > IAppConstants.CHANGK_SUPPLY) {
                    if (!(resStatus_.elementAt(i).vilname.equals(needSingle4Reso.vilname) | GetGDstable.systemVils.indexOf(resStatus_.elementAt(i).vilname) != -1) | resStatus_.elementAt(i).crop_increase < 0) {
                        if (resStatus_.elementAt(i).trade_now > 0) {
                            if (resStatus_.elementAt(i).crop_r > IAppConstants.SUPP_SOURCER | resStatus_.elementAt(i).crop_now > 100000) {
                                if (!checkNeedExit(resStatus_.elementAt(i).vilname, "crop", needresoinfo_)) if (!checkNeedExit(resStatus_.elementAt(i).vilname, "crop", currenttrades_)) supportVils.addElement(resStatus_.elementAt(i));
                            }
                        }
                    }
                }
            }
            if (supportVils.size() > 1) sortSupportVils(supportVils, needSingle4Reso); else if (supportVils.size() == 0) {
                if (!GetGDstable.mainvil.equals(needSingle4Reso.vilname)) if (resStatus_.elementAt(getResoStatusNByName(GetGDstable.mainvil, resStatus_)).trade_now > 0) supportVils.addElement(resStatus_.elementAt(getResoStatusNByName(GetGDstable.mainvil, resStatus_)));
            }
            for (int i = 0; i < supportVils.size(); i++) {
                if (supportVils.elementAt(i).trade_now > 0) {
                    trademax = supportVils.elementAt(i).trade_now * supportVils.elementAt(i).trade_single;
                    if (sourceNeed < trademax) {
                        tempTasks.addElement(new TranspTaskComp(supportVils.elementAt(i).vilname, needSingle4Reso.vilname, 0, 0, 0, sourceNeed));
                        tquantity = supportVils.elementAt(i).trade_now - (int) Math.ceil((float) sourceNeed / supportVils.elementAt(i).trade_single);
                        updateSuppTradeQuant(resStatus_, supportVils.elementAt(i).vilname, tquantity);
                        break;
                    } else {
                        tempTasks.addElement(new TranspTaskComp(supportVils.elementAt(i).vilname, needSingle4Reso.vilname, 0, 0, 0, trademax));
                        updateSuppTradeQuant(resStatus_, supportVils.elementAt(i).vilname, 0);
                        sourceNeed = sourceNeed - trademax;
                    }
                }
            }
        }
        combineTradeTasks(tempTasks);
        return tempTasks;
    }

    private static void combineTradeTasks(Vector<TranspTaskComp> ttasks) {
        for (int i = 0; i < ttasks.size(); i++) {
            for (int j = i + 1; j < ttasks.size(); j++) {
                if (ttasks.elementAt(i).sourceVilName.equals(ttasks.elementAt(j).sourceVilName)) if (ttasks.elementAt(i).targetVilName.equals(ttasks.elementAt(j).targetVilName)) {
                    if (ttasks.elementAt(j).lumb > 0) {
                        ttasks.elementAt(i).lumb = ttasks.elementAt(j).lumb;
                    } else if (ttasks.elementAt(j).clay > 0) {
                        ttasks.elementAt(i).clay = ttasks.elementAt(j).clay;
                    } else if (ttasks.elementAt(j).iron > 0) {
                        ttasks.elementAt(i).iron = ttasks.elementAt(j).iron;
                    } else if (ttasks.elementAt(j).crop > 0) {
                        ttasks.elementAt(i).crop = ttasks.elementAt(j).crop;
                    }
                    ttasks.removeElementAt(j);
                    j--;
                }
            }
        }
    }

    private static void sendMsg(String msg) {
        if (Panel_trade.getSite().getPage().getActivePart().equals(Panel_trade)) Htmlfunc.setStatusMsg(msg);
    }

    private static void balance() {
        if (launch) {
            ApplicationActionBarAdvisor.autodeliverneed.setText("ȡ���������");
            ApplicationActionBarAdvisor.autodeliverinsu.setEnabled(false);
        } else {
            ApplicationActionBarAdvisor.autodeliverneed.setText("���������");
            ApplicationActionBarAdvisor.autodeliverinsu.setEnabled(true);
        }
    }
}
