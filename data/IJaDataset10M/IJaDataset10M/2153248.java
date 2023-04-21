package sanguo.actions;

import sanguo.dataclass.CityInfo;
import sanguo.dataclass.MapPoint;
import sanguo.intro.ApplicationActionBarAdvisor;
import sanguo.sys.GXData;
import sanguo.sys.GXDataOper;
import sanguo.sys.OpenWPanels;
import sanguo.ui.BlankPanel;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;

public class AutoWar_T {

    public static boolean launch = false;

    public static int step, step2 = 1, step3 = 1;

    public static Vector<CityInfo> cityList = new Vector<CityInfo>();

    public static String fBody = "", checkFbody = "";

    public static int radius, citynum, time = 2000, checkBlock;

    public static int[] force = new int[12];

    public static int[] maxforce = new int[12];

    public static Display display;

    public static int interval, launchtype;

    public static String nosheeps;

    public static BlankPanel Panel_aw;

    private static final int interdays = GXDataOper.getSheepInterval();

    public static void launchWar(int type) {
        launchtype = type;
        step = 0;
        launch = !launch;
        maxforce = GXDataOper.getMaxForce2Sheep();
        display = Display.getDefault();
        balance();
        final Runnable delayActions = new Runnable() {

            MapPoint startPoint = new MapPoint(-1, -1);

            String vilname;

            MapPoint mapCenter;

            int laststep = 0, laststep2 = 0, laststep3 = 0;

            int maxsheeppopu = GXDataOper.getMaxSheepPopu(), minsheeppopu = GXDataOper.getMinSheepPopu();

            public void run() {
                if (launch) {
                    switch(step) {
                        case 0:
                            Panel_aw = OpenWPanels.open("autowar", "�Զ�ɨ��");
                            if (Panel_aw.getBrowser().getBody() == null) {
                                System.out.println("#" + Panel_aw.getBrowser().getBody() + "#");
                                Panel_aw.getBrowser().setUrl(GXData.SERVER);
                            }
                            Panel_aw.getBrowser().addDisposeListener(new DisposeListener() {

                                public void widgetDisposed(DisposeEvent e) {
                                    launch = false;
                                    balance();
                                }
                            });
                            sendMsg("��ɨ��ҳ��׼��ִ������");
                            step = 1;
                            break;
                        case 1:
                            if (Panel_aw.getBrowser().br_action.equals("completed")) {
                                fBody = Panel_aw.getBrowser().getBody();
                                vilname = Htmlfunc.getCurrentVil(fBody);
                                mapCenter = GXDataOper.getMapCenter(vilname);
                                nosheeps = GXDataOper.getNoSheeps();
                                System.out.println(nosheeps);
                                if (fBody.indexOf("map.bookmark") == -1) {
                                    Panel_aw.getBrowser().execute("MM_xmlLoad('map.status');");
                                    sendMsg("�����ͼ����");
                                }
                                step = 2;
                                step2 = 1;
                                step3 = 1;
                            }
                            sendMsg("�ȴ�ҳ���������...");
                            break;
                        case 2:
                            System.out.println(step);
                            fBody = Panel_aw.getBrowser().getBody();
                            if (fBody.indexOf("map.bookmark") != -1) {
                                startPoint = getCurrentPoint();
                                citynum = GXDataOper.getCityNum(vilname);
                                if (mapCenter.x < 0 | launchtype == 0) {
                                    mapCenter.y = startPoint.y;
                                    mapCenter.x = startPoint.x;
                                    citynum = 0;
                                    launchtype = 1;
                                } else {
                                    sendMsg("��ȡǰ�μ�¼");
                                    Panel_aw.getBrowser().execute("MM_xmlLoad('map.status&uitx=" + mapCenter.x + "&uity=" + mapCenter.y + "&focus=1');");
                                }
                                step = 3;
                            }
                            break;
                        case 3:
                            fBody = Panel_aw.getBrowser().getBody();
                            if (fBody.indexOf("map.bookmark") != -1) {
                                if (mapCenter.x == Integer.parseInt(Panel_aw.getBrowser().getInputValue("map_move_x"))) {
                                    Panel_aw.getBrowser().execute("MM_closeRight();");
                                    sendMsg("��ȡ��ͼ��Ϣ...");
                                    getCityList(fBody);
                                    step = 4;
                                }
                            }
                            break;
                        case 4:
                            if (citynum >= cityList.size()) {
                                MapPoint next = getNextMap(mapCenter, startPoint);
                                GXDataOper.saveMapCenter(next, 0, vilname);
                                GXDataOper.setNoSheeps(nosheeps);
                                mapCenter.x = next.x;
                                mapCenter.y = next.y;
                                citynum = 0;
                                if (mapCenter.x < 10000) {
                                    System.out.println(mapCenter.x + ":" + mapCenter.y);
                                    sendMsg("�л���ͼ��:" + mapCenter.x + ":" + mapCenter.y + "    ��ǰ��ս�뾶" + radius);
                                    Panel_aw.getBrowser().execute("MM_xmlLoad('map.status&uitx=" + mapCenter.x + "&uity=" + mapCenter.y + "&focus=1');");
                                    step = 3;
                                } else {
                                    step = 100;
                                }
                            } else {
                                sendMsg("���ڷ�����ǰ��ͼ...");
                                for (int i = citynum; i <= cityList.size(); i++) {
                                    if (i == cityList.size()) {
                                        citynum = i;
                                        break;
                                    }
                                    if (nosheeps.indexOf(cityList.elementAt(i).king) == -1) {
                                        if (cityList.elementAt(i).popu <= maxsheeppopu & cityList.elementAt(i).popu >= minsheeppopu) {
                                            step = 5;
                                            citynum = i;
                                            sendMsg("��һ���鿴��" + cityList.elementAt(citynum).name + "�������˿ڣ����ж��Ƿ�Ϊ��");
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                        case 5:
                            if (cityIsSheep(maxsheeppopu, minsheeppopu) == 1) {
                                sendMsg("�ҵ���" + cityList.elementAt(citynum).name);
                                step = 6;
                            } else if (cityIsSheep(maxsheeppopu, minsheeppopu) == 0) {
                                citynum++;
                                step = 4;
                            }
                            break;
                        case 6:
                            int launResult = launchBattle();
                            if (launResult == 0) {
                                step = 9;
                            } else if (launResult == 1) {
                                step = 4;
                            }
                            System.out.println(launResult);
                            break;
                        case 9:
                            if (interval > 0) {
                                interval = interval - time;
                                sendMsg("�ȴ�" + Htmlfunc.getTimeFormat(interval) + "�����ɨ��");
                            } else {
                                Panel_aw.getBrowser().execute("MM_xmlLoad('task.main');");
                                step = 6;
                                step3 = 1;
                            }
                            break;
                        case 10:
                            launch = false;
                            display.sleep();
                            break;
                        case 20:
                            if (interval > 0) {
                                System.out.println("ϵͳ����ȴ���������" + interval / 1000 + "��");
                                sendMsg("ϵͳ����ȴ���������" + interval / 1000 + "��");
                                interval = interval - time;
                            } else {
                                step = 1;
                            }
                            break;
                    }
                    if (step != 9 & step != 20) {
                        if (laststep == step & laststep2 == step2 & laststep3 == step3) {
                            checkBlock++;
                            System.out.println("checkBlock=" + checkBlock);
                            if (checkBlock > 20) {
                                Panel_aw.getBrowser().br_action = "";
                                Panel_aw.getBrowser().setUrl(GXData.SITE);
                                step = 20;
                                checkBlock = 0;
                                interval = 300000;
                            }
                        } else {
                            checkBlock = 0;
                            laststep = step;
                            laststep2 = step2;
                            laststep3 = step3;
                        }
                    }
                    display.timerExec(time, this);
                } else {
                    sendMsg("�Զ�ɨ��ȡ��ɹ��������µ��������" + mapCenter.x + ":" + mapCenter.y);
                    balance();
                    GXDataOper.setNoSheeps(nosheeps);
                    GXDataOper.saveMapCenter(mapCenter, citynum, vilname);
                    System.out.println("save=" + mapCenter.x + ":" + mapCenter.y);
                    display.sleep();
                }
            }
        };
        display.asyncExec(delayActions);
    }

    public static MapPoint getCurrentPoint() {
        int x = Integer.parseInt(Panel_aw.getBrowser().getInputValue("map_move_x"));
        int y = Integer.parseInt(Panel_aw.getBrowser().getInputValue("map_move_y"));
        MapPoint currentPoint = new MapPoint(x, y);
        return currentPoint;
    }

    public static void getCityList(String fBody) {
        cityList.removeAllElements();
        Pattern pcity = Pattern.compile("id=city_div_(\\d+)([^=]+=){9}\"(\\d+)#(\\d+)#[^#]+#([^#]+)#([^#]+)#([^#]*)#(\\d+)#(\\d+)");
        Matcher mcity = pcity.matcher(fBody);
        while (mcity.find()) {
            cityList.addElement(new CityInfo(mcity.group(1), mcity.group(3), mcity.group(4), mcity.group(5), mcity.group(6), mcity.group(7), mcity.group(8), mcity.group(9), 0, "0"));
        }
        System.out.println("cityList.size()=" + cityList.size());
    }

    public static MapPoint getNextMap(MapPoint now, MapPoint startpoint) {
        int x = startpoint.x;
        int y = startpoint.y;
        int xnow = now.x;
        int ynow = now.y;
        MapPoint next = new MapPoint(x, y);
        radius = Math.abs(xnow - x) + Math.abs(ynow - y);
        if (xnow == x - radius) {
            radius = radius + 7;
            next.x = xnow;
            next.y = ynow - 7;
        } else if (xnow < x) {
            if (ynow >= y) {
                next.x = xnow - 7;
                next.y = ynow - 7;
            } else if (ynow < y) {
                next.x = xnow + 7;
                next.y = ynow - 7;
            }
        } else if (xnow == x) {
            if (ynow == y + radius) {
                next.x = xnow - 7;
                next.y = ynow - 7;
            } else if (ynow == y - radius) {
                next.x = xnow + 7;
                next.y = ynow + 7;
            }
        } else if (xnow > x) {
            if (ynow == y) {
                next.x = xnow - 7;
                next.y = ynow + 7;
            } else if (ynow < y) {
                next.x = xnow + 7;
                next.y = ynow + 7;
            } else if (ynow > y) {
                next.x = xnow - 7;
                next.y = ynow + 7;
            }
        }
        if (next.x < 7 | next.y < 7 | next.x > 600 | next.y > 600) {
            next.x = 10000;
            next.y = 10000;
        }
        System.out.println("radius=" + radius);
        return next;
    }

    public static int cityIsSheep(int maxsheeppopu, int minsheeppopu) {
        int x = cityList.elementAt(citynum).x;
        int y = cityList.elementAt(citynum).y;
        switch(step2) {
            case 1:
                System.out.println("�鿴" + cityList.elementAt(citynum).name);
                Panel_aw.getBrowser().execute("MM_xmlLoad('map.detail&uitx=" + x + "&uity=" + y + "');");
                Panel_aw.getBrowser().clickById("city_div_" + cityList.elementAt(citynum).pos);
                step2 = 2;
                break;
            case 2:
                Panel_aw.getBrowser().execute("MM_closeLeft();");
                fBody = Panel_aw.getBrowser().getBody();
                if (fBody.indexOf("cityresourse") != -1) {
                    Pattern paction = Pattern.compile("report.reportdetail[^>]+>(\\d+/\\d+/\\d+[^<]+)");
                    Matcher maction = paction.matcher(fBody);
                    if (fBody.indexOf("\"����\"") != -1) {
                        sendMsg((citynum + 1) + "/" + cityList.size() + "   " + "����" + cityList.elementAt(citynum).name + "������");
                        step2 = 10;
                    } else if (maction.find()) {
                        String tWardate = maction.group(1).replaceAll("/", "-");
                        if ((Htmlfunc.getLocalDate().getTime() - Htmlfunc.String2Date(tWardate).getTime()) > interdays * 24 * 60 * 60 * 1000) {
                            sendMsg((citynum + 1) + "/" + cityList.size() + "   " + "����" + cityList.elementAt(citynum).name + interdays + "����û��ս��");
                            System.out.println(citynum + "/" + cityList.size() + "   " + "����" + cityList.elementAt(citynum).name + interdays + "����û��ս��");
                            paction = Pattern.compile("user.main&amp;uid=(\\d+)&amp;keep=right");
                            maction = paction.matcher(fBody);
                            if (maction.find()) {
                                Panel_aw.getBrowser().execute("MM_xmlLoad('user.main&uid=" + maction.group(1) + "&keep=right');");
                                step2 = 3;
                            }
                        } else {
                            System.out.println((citynum + 1) + "/" + cityList.size() + "   " + "����" + cityList.elementAt(citynum).name + "��" + tWardate + "��ս����С���趨���" + interdays + "��");
                            sendMsg((citynum + 1) + "/" + cityList.size() + "   " + "����" + cityList.elementAt(citynum).name + "��" + tWardate + "��ս����С���趨���" + interdays + "��");
                            step2 = 10;
                        }
                    } else {
                        paction = Pattern.compile("user.main&amp;uid=(\\d+)&amp;keep=right");
                        maction = paction.matcher(fBody);
                        if (maction.find()) {
                            Panel_aw.getBrowser().execute("MM_xmlLoad('user.main&uid=" + maction.group(1) + "&keep=right');");
                            step2 = 3;
                        }
                    }
                }
                break;
            case 3:
                fBody = Panel_aw.getBrowser().getBody();
                if (fBody.indexOf("��������") != -1) {
                    Pattern pcity = Pattern.compile("�˿�([^>]+>){2}<SPAN class=ta_input>(\\d+)<");
                    Matcher mcity = pcity.matcher(fBody);
                    if (mcity.find()) {
                        if (Integer.parseInt(mcity.group(2)) <= maxsheeppopu & Integer.parseInt(mcity.group(2)) >= minsheeppopu) {
                            step2 = 1;
                            return 1;
                        } else {
                            System.out.println(cityList.elementAt(citynum).name + "������ܲ�����Ŷ" + mcity.group(2));
                            nosheeps = nosheeps + cityList.elementAt(citynum).king + "#";
                            sendMsg(citynum + "/" + cityList.size() + "   " + cityList.elementAt(citynum).king + "���˿ڣ�" + mcity.group(2) + "��������ܲ�����Ŷ");
                            step2 = 10;
                        }
                    }
                }
                break;
            case 10:
                step2 = 1;
                Panel_aw.getBrowser().execute("MM_closeLeft();");
                Panel_aw.getBrowser().execute("MM_closeRight();");
                return 0;
        }
        return -1;
    }

    public static int launchBattle() {
        int type = -1;
        int x = cityList.elementAt(citynum).x;
        int y = cityList.elementAt(citynum).y;
        String fnum;
        switch(step3) {
            case 1:
                System.out.println("�Ӷ�:" + cityList.elementAt(citynum).name);
                sendMsg(citynum + "/" + cityList.size() + "   " + "�Ӷ�:" + cityList.elementAt(citynum).name);
                Panel_aw.getBrowser().execute("MM_xmlLoad('build.act&do=war_list&btid=9&x=" + x + "&y=" + y + "&keep=right');");
                Panel_aw.getBrowser().execute("MM_closeRight();");
                step3 = 2;
                break;
            case 2:
                fBody = Panel_aw.getBrowser().getBody();
                if (fBody.indexOf("�Զ�����") != -1) {
                    getForce(force, fBody);
                    fnum = inputForceNum(force, fBody);
                    System.out.println("fnum=" + fnum);
                    Panel_aw.getBrowser().setValue("type", "1");
                    if (fnum.equals("no transm")) {
                        System.out.println("��������Ϊ0");
                        sendMsg("��������Ϊ0");
                        Panel_aw.getBrowser().execute("MM_closeLeft();");
                        Panel_aw.getBrowser().execute("MM_xmlLoad('task.main');");
                        step3 = 9;
                    } else if (fnum.equals("no force")) {
                        System.out.println("�������");
                        sendMsg("�������");
                        Panel_aw.getBrowser().execute("MM_closeLeft();");
                        Panel_aw.getBrowser().execute("MM_xmlLoad('task.main');");
                        step3 = 10;
                    } else {
                        Panel_aw.getBrowser().clickByClass("a", "automatism_button", 0);
                        step3 = 3;
                    }
                }
                time = 3000;
                break;
            case 3:
                fBody = Panel_aw.getBrowser().getBody();
                if (fBody.indexOf("����ſ�") != -1) {
                    if (fBody.indexOf(", true, 0, 0)") != -1) {
                        Panel_aw.getBrowser().clickByValue("A", "����", 0);
                        step3 = 4;
                        time = 5000;
                    }
                }
                break;
            case 4:
                fBody = Panel_aw.getBrowser().getBody();
                if (fBody.indexOf("���ǲ���") != -1) {
                    step3 = 5;
                    time = 2000;
                } else {
                    if (fBody.indexOf("DISPLAY: block") != -1) {
                        if (fBody.indexOf("�벻Ҫ�ظ��ύ") != -1) {
                            Panel_aw.getBrowser().clickByClass("SPAN", "dialogconfirm", 0);
                            System.out.println("����ظ��ύ");
                            time = 7000;
                        }
                    } else {
                        Panel_aw.getBrowser().clickByValue("A", "����", 0);
                        System.out.println("���2" + Panel_aw.getBrowser().getValueByClass("A", "redbutton_s", 1));
                        time = 7000;
                    }
                }
                break;
            case 5:
                Panel_aw.getBrowser().execute("MM_closeLeft();");
                Panel_aw.getBrowser().execute("MM_closeRight();");
                step3 = 1;
                citynum++;
                type = 1;
                break;
            case 9:
                Panel_aw.getBrowser().execute("MM_closeFloat();");
                step3 = 1;
                interval = getLatestTime("out");
                type = 0;
                break;
            case 10:
                Panel_aw.getBrowser().execute("MM_closeFloat();");
                type = 0;
                step3 = 1;
                interval = getLatestTime("back");
                break;
        }
        return type;
    }

    public static int getLatestTime(String timetype) {
        int msecond;
        long lmsecond = -1;
        Pattern paction;
        fBody = Panel_aw.getBrowser().getBody();
        if (timetype.equals("out")) paction = Pattern.compile("������([^>]+>){3}(\\d+):(\\d+):(\\d+)<"); else paction = Pattern.compile("���أ�([^>]+>){3}(\\d+):(\\d+):(\\d+)<");
        Matcher maction = paction.matcher(fBody);
        if (maction.find()) {
            lmsecond = Integer.parseInt(maction.group(2)) * 60 * 60 * 1000 + Integer.parseInt(maction.group(3)) * 60 * 1000 + Integer.parseInt(maction.group(4)) * 1000 + 10000;
            System.out.println(maction.group(2) + ":" + maction.group(3) + ":" + maction.group(4));
        }
        if (timetype.equals("back") & lmsecond == -1 & lmsecond == -1) {
            paction = Pattern.compile("������([^>]+>){3}(\\d+):(\\d+):(\\d+)<");
            maction = paction.matcher(fBody);
            if (maction.find()) {
                lmsecond = Integer.parseInt(maction.group(2)) * 60 * 60 * 1000 + Integer.parseInt(maction.group(3)) * 60 * 1000 + Integer.parseInt(maction.group(4)) * 1000 + 10000;
                lmsecond = lmsecond * 2;
                System.out.println("back=2*" + maction.group(2) + ":" + maction.group(3) + ":" + maction.group(4));
            }
        }
        if (lmsecond == -1) {
            lmsecond = 60 * 1000 * 5;
        }
        if (lmsecond > 2100000000) msecond = 2100000000; else msecond = (int) lmsecond;
        return msecond;
    }

    private static void sendMsg(String msg) {
        if (Panel_aw.getSite().getPage().getActivePart().equals(Panel_aw)) Htmlfunc.setStatusMsg(msg);
    }

    private static void getForce(int[] gforce, String fBody) {
        int i = 0;
        Pattern pfnum;
        pfnum = Pattern.compile("A id=soldier_link[^>]+>\\((\\d+)\\)<");
        Matcher mfnum = pfnum.matcher(fBody);
        while (mfnum.find()) {
            gforce[i] = Integer.parseInt(mfnum.group(1));
            i++;
        }
    }

    private static String inputForceNum(int[] gforce, String fBody) {
        Pattern pfnum = Pattern.compile("id=orderlyCnt>(\\d+)/(\\d+)</SPAN>");
        Matcher mfnum = pfnum.matcher(fBody);
        if (mfnum.find()) {
            sendMsg("����������ʣ" + mfnum.group(1));
            if (mfnum.group(1).equals("0")) {
                return "no transm";
            }
        }
        for (int i = 0; i < 12; i++) {
            if (maxforce[i] > 0) {
                if (gforce[i] >= maxforce[i]) {
                    sendMsg("����" + i + "���" + maxforce[i] + "��");
                    Panel_aw.getBrowser().setValue("soldier" + i, maxforce[i]);
                    return i + "��" + maxforce[i];
                } else if (gforce[i] >= maxforce[i] * 0.7) {
                    sendMsg("����" + i + "���" + gforce[i] + "��");
                    Panel_aw.getBrowser().setValue("soldier" + i, gforce[i]);
                    return i + "��" + gforce[i];
                }
            }
        }
        return "no force";
    }

    private static void balance() {
        if (launchtype == 0) {
            if (launch) {
                ApplicationActionBarAdvisor.autowar0.setText("ȡ���Զ�ɨ��");
                ApplicationActionBarAdvisor.autowar1.setEnabled(false);
            } else {
                ApplicationActionBarAdvisor.autowar0.setText("��ԭ���Զ�ɨ��");
                ApplicationActionBarAdvisor.autowar1.setEnabled(true);
            }
        } else {
            if (launch) {
                ApplicationActionBarAdvisor.autowar1.setText("ȡ���Զ�ɨ��");
                ApplicationActionBarAdvisor.autowar0.setEnabled(false);
            } else {
                ApplicationActionBarAdvisor.autowar1.setText("���ϴ��Զ�ɨ��");
                ApplicationActionBarAdvisor.autowar0.setEnabled(true);
            }
        }
    }
}
