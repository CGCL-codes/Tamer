package cn.jsprun.struts.foreg.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import cn.jsprun.api.Tenpayapi;
import cn.jsprun.domain.Members;
import cn.jsprun.service.system.DataBaseService;
import cn.jsprun.utils.BeanFactory;
import cn.jsprun.utils.Common;
import cn.jsprun.utils.DataParse;
import cn.jsprun.utils.FormDataCheck;
import cn.jsprun.utils.Md5Token;

public class TradeManageAction extends DispatchAction {

    private DataBaseService dataBaseService = (DataBaseService) BeanFactory.getBean("dataBaseService");

    private DataParse dataParse = (DataParse) BeanFactory.getBean("dataParse");

    @SuppressWarnings("unchecked")
    public ActionForward trade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String orderid = request.getParameter("orderid");
        HttpSession session = request.getSession();
        int jsprun_uid = (Integer) session.getAttribute("jsprun_uid");
        if (orderid == null) {
            int page = Common.toDigit(request.getParameter("page"), 2147483647l, 1l).intValue();
            int tid = Common.toDigit(request.getParameter("tid"));
            int pid = Common.toDigit(request.getParameter("pid"));
            if (pid == 0) {
                List<Map<String, String>> posts = dataBaseService.executeQuery("SELECT pid FROM jrun_posts WHERE tid='" + tid + "' AND first='1' LIMIT 1");
                if (posts != null && posts.size() > 0) {
                    Common.toDigit(posts.get(0).get("pid"));
                }
            }
            List<Map<String, String>> threads = dataBaseService.executeQuery("SELECT closed FROM jrun_threads WHERE tid='" + tid + "'");
            if (threads == null || threads.size() == 0) {
                request.setAttribute("errorInfo", "δ����Ĳ�����");
                return mapping.findForward("showMessage");
            }
            int closed = Common.toDigit(threads.get(0).get("closed"));
            if (closed > 0) {
                request.setAttribute("successInfo", "�������ѹر�");
                request.setAttribute("requestPath", "viewthread.jsp?tid=" + tid + "&page=" + page);
                return mapping.findForward("showMessage");
            }
            List<Map<String, String>> trades = dataBaseService.executeQuery("SELECT * FROM jrun_trades WHERE tid='" + tid + "' AND pid='" + pid + "'");
            if (trades == null || trades.size() == 0) {
                request.setAttribute("errorInfo", "δ����Ĳ�����");
                return mapping.findForward("showMessage");
            }
            Map<String, String> trade = trades.get(0);
            closed = Common.toDigit(trade.get("closed"));
            if (closed > 0) {
                request.setAttribute("successInfo", "�������ѹر�");
                request.setAttribute("requestPath", "viewthread.jsp?tid=" + tid + "&page=" + page);
                return mapping.findForward("showMessage");
            }
            float price = Float.valueOf(trade.get("price"));
            if (price < 0) {
                request.setAttribute("successInfo", "��Ч�Ľ�������");
                request.setAttribute("requestPath", "viewthread.jsp?tid=" + tid + "&page=" + page);
                return mapping.findForward("showMessage");
            }
            String action = request.getParameter("trade");
            String tradesubmit = request.getParameter("tradesubmit");
            if (!"trade".equals(action) && tradesubmit == null) {
                List<Map<String, String>> tradelogs = dataBaseService.executeQuery("SELECT buyername,buyercontact,buyerzip,buyerphone,buyermobile FROM jrun_tradelog WHERE buyerid='" + jsprun_uid + "' AND status!=0 AND buyername!='' ORDER BY lastupdate DESC LIMIT 1");
                if (tradelogs != null && tradelogs.size() > 0) {
                    request.setAttribute("lastbuyerinfo", tradelogs.get(0));
                }
                request.setAttribute("trade", trade);
                return mapping.findForward("totrade");
            } else {
                int number = Common.toDigit(request.getParameter("number"));
                if (Integer.valueOf(trade.get("sellerid")) == jsprun_uid) {
                    request.setAttribute("errorInfo", "���޷������Լ�����Ʒ���뷵���޸ġ�");
                    return mapping.findForward("showMessage");
                } else if (number <= 0) {
                    request.setAttribute("errorInfo", "��������ȷ����Ʒ���뷵���޸ġ�");
                    return mapping.findForward("showMessage");
                } else if (number > Integer.valueOf(trade.get("amount"))) {
                    request.setAttribute("errorInfo", "��Ʒʣ���������㣬�뷵���޸ġ�");
                    return mapping.findForward("showMessage");
                }
                Map<String, Object> pay = new HashMap<String, Object>();
                pay.put("number", number);
                pay.put("price", trade.get("price"));
                price = price * number;
                int buyercredits = 0;
                pay.put("commision", 0);
                int timeoffset = (int) ((Float) session.getAttribute("timeoffset") * 3600);
                int timestamp = (Integer) (request.getAttribute("timestamp"));
                orderid = Common.gmdate("yyyyMMddHHmmss", timestamp + timeoffset) + Common.getRandStr(18, false);
                int transportfee = 0;
                int transport = Common.toDigit(request.getParameter("transport"));
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("fee", Common.toDigit(request.getParameter("fee")));
                data.put("trade", trade);
                data.put("transport", transport);
                Tenpayapi.trade_setprice(data, price, pay, transportfee);
                String jsprun_userss = (String) session.getAttribute("jsprun_userss");
                String buyermsg = Common.dhtmlspecialchars(request.getParameter("buyermsg"));
                String buyerzip = Common.dhtmlspecialchars(request.getParameter("buyerzip"));
                String buyerphone = Common.dhtmlspecialchars(request.getParameter("buyerphone"));
                String buyermobile = Common.dhtmlspecialchars(request.getParameter("buyermobile"));
                String buyername = Common.dhtmlspecialchars(request.getParameter("buyername"));
                String buyercontact = Common.dhtmlspecialchars(request.getParameter("buyercontact"));
                int offline = Common.toDigit(request.getParameter("offline"));
                float tax = 0;
                dataBaseService.runQuery("INSERT INTO jrun_tradelog (tid, pid, orderid,tradeno, subject, price, quality, itemtype, number, tax, locus, sellerid, seller, selleraccount, buyerid, buyer, buyercontact, buyercredits, buyermsg, lastupdate, offline, buyerzip, buyerphone, buyermobile, buyername, transport, transportfee, baseprice, discount,message) VALUES('" + trade.get("tid") + "', '" + trade.get("pid") + "', '" + orderid + "','', '" + trade.get("subject") + "', '" + pay.get("price") + "', '" + trade.get("quality") + "', '" + trade.get("itemtype") + "', '" + number + "', '" + tax + "', '" + trade.get("locus") + "', '" + trade.get("sellerid") + "', '" + trade.get("seller") + "', '" + trade.get("account") + "', '" + jsprun_uid + "', '" + (jsprun_userss == null ? "�ο�" : jsprun_userss) + "', '" + buyercontact + "', 0, '" + buyermsg + "', '" + timestamp + "', '" + offline + "', '" + buyerzip + "', '" + buyerphone + "', '" + buyermobile + "', '" + buyername + "', '" + transport + "', '" + pay.get("transportfee") + "', '" + trade.get("price") + "', 0,'')");
                dataBaseService.runQuery("UPDATE jrun_trades SET amount=amount-'" + number + "' WHERE tid='" + trade.get("tid") + "' AND pid='" + trade.get("pid") + "'");
                request.setAttribute("successInfo", "���׵��Ѵ��������ڽ�ת�뽻�׵�ҳ�档");
                request.setAttribute("requestPath", "trade.jsp?orderid=" + orderid);
                return mapping.findForward("showMessage");
            }
        } else {
            List<Map<String, String>> tradelogs = dataBaseService.executeQuery("SELECT * FROM jrun_tradelog WHERE orderid='" + orderid + "'");
            if (tradelogs == null || tradelogs.size() == 0) {
                request.setAttribute("errorInfo", "δ����Ĳ�����");
                return mapping.findForward("showMessage");
            }
            Map<String, String> tradelog = tradelogs.get(0);
            int sellerid = Common.toDigit(tradelog.get("sellerid"));
            int buyerid = Common.toDigit(tradelog.get("buyerid"));
            if (jsprun_uid != sellerid && jsprun_uid != buyerid) {
                request.setAttribute("errorInfo", "δ����Ĳ�����");
                return mapping.findForward("showMessage");
            }
            Members member = (Members) session.getAttribute("user");
            float price = Float.valueOf(tradelog.get("baseprice")) * Integer.valueOf(tradelog.get("number"));
            Map<String, String> settings = (Map<String, String>) request.getAttribute("settings");
            int creditstransid = Common.toDigit(settings.get("creditstrans"));
            int currentcredit = creditstransid > 0 ? (Integer) Common.getValues(member, "extcredits" + creditstransid) : 0;
            String payStr = request.getParameter("pay");
            int offline = Integer.valueOf(tradelog.get("offline"));
            int status = Integer.valueOf(tradelog.get("status"));
            if (payStr != null && offline == 0 && status == 0 && buyerid == jsprun_uid) {
                List<Map<String, String>> trades = dataBaseService.executeQuery("SELECT * FROM jrun_trades WHERE tid='" + tradelog.get("tid") + "' AND pid='" + tradelog.get("pid") + "'");
                Map<String, String> trade = trades.get(0);
                Map extcredits = dataParse.characterParse(settings.get("extcredits"), true);
                Map creditstrans = (Map) extcredits.get(creditstransid);
                int discountcredit = 0;
                if (jsprun_uid > 0 && currentcredit < discountcredit && Integer.valueOf(tradelog.get("discount")) > 0) {
                    request.setAttribute("errorInfo", "��� " + creditstrans.get("title") + " ���㣬�޷��� " + creditstrans.get("title") + " ��ȡ��Ʒ���Żݣ��뷵���޸ġ�");
                    return mapping.findForward("showMessage");
                }
                Map<String, Object> pay = new HashMap<String, Object>();
                pay.put("commision", 0);
                int transport = Integer.valueOf(tradelog.get("transport"));
                int transportfee = 0;
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("fee", Common.toDigit(request.getParameter("fee")));
                data.put("trade", trade);
                data.put("transport", transport);
                Tenpayapi.trade_setprice(data, price, pay, transportfee);
                String boardurl = (String) session.getAttribute("boardurl");
                String chnid = settings.get("ec_account");
                String key = settings.get("ec_key");
                String payurl = Tenpayapi.trade_payurl(boardurl, chnid, key, pay, trade, tradelog);
                request.setAttribute("successInfo", "�����ύ���Ϊ " + orderid + " �Ķ�����");
                request.setAttribute("requestPath", payurl);
                return mapping.findForward("showMessage");
            }
            String offlinesubmit = request.getParameter("offlinesubmit");
            List<Integer> offlines = (List<Integer>) Tenpayapi.trade_offline(jsprun_uid, tradelog, false);
            int offlinestatus = Common.toDigit(request.getParameter("offlinestatus"));
            if (offlinesubmit != null && offlines.contains(offlinestatus)) {
                String password = Md5Token.getInstance().getLongToken(request.getParameter("password"));
                if (!password.equals((String) session.getAttribute("jsprun_pw"))) {
                    request.setAttribute("successInfo", "����������벻��ȷ�������޸Ķ���״̬���뷵���޸ġ�");
                    request.setAttribute("requestPath", "trade.jsp?orderid=" + orderid + "");
                    return mapping.findForward("showMessage");
                }
                int timestamp = (Integer) (request.getAttribute("timestamp"));
                String boardurl = (String) session.getAttribute("boardurl");
                if (offlinestatus == Tenpayapi.STATUS_SELLER_SEND) {
                    List<Map<String, String>> trades = dataBaseService.executeQuery("SELECT amount FROM jrun_trades WHERE tid='" + tradelog.get("tid") + "' AND pid='" + tradelog.get("pid") + "'");
                    Map<String, String> trade = trades.get(0);
                    if (Integer.valueOf(tradelog.get("number")) > Integer.valueOf(trade.get("amount"))) {
                        request.setAttribute("errorInfo", "��Ʒʣ���������㣬�뷵���޸ġ�");
                        return mapping.findForward("showMessage");
                    }
                    String message = "��������̳ϵͳ�Զ����͵�֪ͨ����Ϣ��\n\n��� " + tradelog.get("buyer") + " ���������Ʒ " + tradelog.get("subject") + "\n\n����Ѹ���ȴ��������[url=" + boardurl + "trade.jsp?orderid=" + orderid + "]�������[/url]�鿴���顣";
                    Common.sendpm(tradelog.get("sellerid"), "[ϵͳ��Ϣ] ����ҹ��������Ʒ", message, "0", "System Message", timestamp);
                } else if (offlinestatus == Tenpayapi.STATUS_WAIT_BUYER) {
                    String message = "��������̳ϵͳ�Զ����͵�֪ͨ����Ϣ��\n\n�������Ʒ " + tradelog.get("subject") + "\n\n���� " + tradelog.get("seller") + " �ѷ������ȴ����ȷ�ϣ���[url=" + boardurl + "trade.jsp?orderid=" + orderid + "]�������[/url]�鿴���顣";
                    Common.sendpm(tradelog.get("buyerid"), "[ϵͳ��Ϣ] �������Ʒ�Ѿ�����", message, "0", "System Message", timestamp);
                } else if (offlinestatus == Tenpayapi.STATUS_TRADE_SUCCESS) {
                    dataBaseService.runQuery("UPDATE jrun_trades SET lastbuyer='" + tradelog.get("buyer") + "', lastupdate='" + timestamp + "', totalitems=totalitems+'" + tradelog.get("number") + "', tradesum=tradesum+'" + tradelog.get("price") + "' WHERE tid='" + tradelog.get("tid") + "' AND pid='" + tradelog.get("pid") + "'");
                    String message = "��������̳ϵͳ�Զ����͵�֪ͨ����Ϣ��\n\n��Ʒ " + tradelog.get("subject") + " �ѽ��׳ɹ�����[url=" + boardurl + "trade.jsp?orderid=" + orderid + "]�������[/url]��Է����֡�";
                    Common.sendpm(tradelog.get("sellerid"), "[ϵͳ��Ϣ] ��Ʒ�����ѳɹ����", message, "0", "System Message", timestamp);
                    Common.sendpm(tradelog.get("buyerid"), "[ϵͳ��Ϣ] ��Ʒ�����ѳɹ����", message, "0", "System Message", timestamp);
                } else if (offlinestatus == Tenpayapi.STATUS_REFUND_CLOSE) {
                    dataBaseService.runQuery("UPDATE jrun_trades SET amount=amount+'" + tradelog.get("number") + "' WHERE tid='" + tradelog.get("tid") + "' AND pid='" + tradelog.get("pid") + "'");
                    String message = "��������̳ϵͳ�Զ����͵�֪ͨ����Ϣ��\n\n��Ʒ " + tradelog.get("subject") + " ���˿�ɹ�����[url=" + boardurl + "trade.jsp?orderid=" + orderid + "]�������[/url]��Է����֡�";
                    Common.sendpm(tradelog.get("sellerid"), "[ϵͳ��Ϣ] �������Ʒ�ѳɹ��˿�", message, "0", "System Message", timestamp);
                    Common.sendpm(tradelog.get("buyerid"), "[ϵͳ��Ϣ] �������Ʒ�ѳɹ��˿�", message, "0", "System Message", timestamp);
                }
                String message = request.getParameter("message").trim();
                if (message.length() > 0) {
                    message = tradelog.get("message") + "\t\t\t" + jsprun_uid + "\t" + session.getAttribute("jsprun_userss") + "\t" + timestamp + "\t" + Common.strip_tags(Common.cutstr(message, 200));
                } else {
                    message = tradelog.get("message");
                }
                dataBaseService.runQuery("UPDATE jrun_tradelog SET status='" + offlinestatus + "', lastupdate='" + timestamp + "', message='" + message + "' WHERE orderid='" + orderid + "'");
                request.setAttribute("successInfo", "���׵�״̬�Ѹ��¡�");
                request.setAttribute("requestPath", "trade.jsp?orderid=" + orderid + "");
                return mapping.findForward("showMessage");
            }
            String tradesubmit = request.getParameter("tradesubmit");
            if (tradesubmit != null) {
                if (status == 0) {
                    StringBuffer update = new StringBuffer();
                    if (sellerid == jsprun_uid) {
                        String baseprice = FormDataCheck.turnToDoubleString(request.getParameter("newprice"));
                        int transportfee = Common.toDigit(request.getParameter("newfee"));
                        tradelog.put("baseprice", baseprice);
                        tradelog.put("transportfee", transportfee + "");
                        update.append("baseprice='" + baseprice + "',");
                        update.append("transportfee='" + transportfee + "',");
                    } else if (buyerid == jsprun_uid) {
                        int number = Common.toDigit(request.getParameter("newnumber"));
                        tradelog.put("number", number + "");
                        List<Map<String, String>> trades = dataBaseService.executeQuery("SELECT amount FROM jrun_trades WHERE tid='" + tradelog.get("tid") + "' AND pid='" + tradelog.get("pid") + "'");
                        Map<String, String> trade = trades.get(0);
                        if (number > Integer.valueOf(trade.get("amount"))) {
                            request.setAttribute("errorInfo", "��Ʒʣ���������㣬�뷵���޸ġ�");
                            return mapping.findForward("showMessage");
                        }
                        update.append("number='" + Common.toDigit(request.getParameter("newnumber")) + "',");
                        update.append("discount=0,");
                        update.append("buyername='" + Common.dhtmlspecialchars(request.getParameter("newbuyername")) + "',");
                        update.append("buyercontact='" + Common.dhtmlspecialchars(request.getParameter("newbuyercontact")) + "',");
                        update.append("buyerzip='" + Common.dhtmlspecialchars(request.getParameter("newbuyerzip")) + "',");
                        update.append("buyerphone='" + Common.dhtmlspecialchars(request.getParameter("newbuyerphone")) + "',");
                        update.append("buyermobile='" + Common.dhtmlspecialchars(request.getParameter("newbuyermobile")) + "',");
                    }
                    float tax = 0;
                    if (update.length() > 0) {
                        float baseprice = Float.valueOf(tradelog.get("baseprice"));
                        if (Integer.valueOf(tradelog.get("discount")) > 0) {
                            baseprice = baseprice - tax;
                        }
                        price = baseprice * Integer.valueOf(tradelog.get("number"));
                        update.append("price='" + (price + (Integer.valueOf(tradelog.get("transport")) == 2 ? Integer.valueOf(tradelog.get("transportfee")) : 0)) + "'");
                        dataBaseService.runQuery("UPDATE jrun_tradelog SET " + update + " WHERE orderid='" + orderid + "'");
                        tradelogs = dataBaseService.executeQuery("SELECT * FROM jrun_tradelog WHERE orderid='" + orderid + "'");
                        tradelog = tradelogs.get(0);
                    }
                }
            }
            tradelog.put("statusview", (String) Tenpayapi.trade_getstatus(status));
            if (offline > 0) {
                Map<Integer, String> offlinenexts = (Map<Integer, String>) Tenpayapi.trade_offline(jsprun_uid, tradelog, true);
                String trade_message = offlinenexts.get(-1);
                offlinenexts.remove(-1);
                request.setAttribute("offlinenexts", offlinenexts);
                request.setAttribute("trade_message", trade_message);
                List<String[]> messages = new ArrayList<String[]>();
                String[] message = tradelog.get("message").split("\t\t\t");
                for (String row : message) {
                    String[] rows = row.split("\t");
                    if (rows.length == 4) {
                        messages.add(rows);
                    }
                }
                request.setAttribute("messages", messages.size() > 0 ? messages : null);
            } else {
                request.setAttribute("loginurl", "https://www.tenpay.com/med/tradeDetail.shtml?b=1&trans_id=" + tradelog.get("tradeno"));
            }
            boolean isratestatus = (Boolean) Tenpayapi.trade_typestatus("successtrades", status) || (Boolean) Tenpayapi.trade_typestatus("refundsuccess", status);
            request.setAttribute("isratestatus", isratestatus);
            request.setAttribute("tradelog", tradelog);
            return mapping.findForward("totrade_view");
        }
    }
}
