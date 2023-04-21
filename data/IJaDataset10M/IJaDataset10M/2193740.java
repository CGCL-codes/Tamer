package org.tnt.ikaixin.core;

import static watij.finders.SymbolFactory.id;
import static watij.finders.SymbolFactory.name;
import static watij.finders.SymbolFactory.value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tnt.ikaixin.exception.FatalException;
import org.tnt.ikaixin.exception.InitException;
import org.tnt.ikaixin.util.DomHelper;
import org.tnt.ikaixin.util.ElementConstant;
import org.tnt.ikaixin.util.Status;
import org.tnt.ikaixin.util.StringUtil;
import org.tnt.ikaixin.util.URLConstant;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import watij.elements.Button;
import watij.elements.Frame;
import watij.elements.Link;
import watij.elements.Links;
import watij.elements.Radio;
import watij.elements.Radios;
import watij.runtime.ie.IE;

/**
 * 
 * @author TNT
 * 
 */
public class BitePeopleManager extends BasicManager {

    private static Log log = LogFactory.getLog(BitePeopleManager.class);

    private String statusStr = "";

    BitePeopleManager(IE ie) throws InitException {
        super(ie);
    }

    private String getStatusStr() throws FatalException {
        if (!StringUtil.isEmpty(statusStr)) return statusStr;
        openUrl(URLConstant.BITE_PEOPLE_URL);
        log.debug("Getting status...");
        try {
            DomHelper.getInstance().parse(getIe().html());
        } catch (Exception e) {
            log.error("Get Status failed! ", e);
            return statusStr;
        }
        statusStr = DomHelper.getInstance().getDataByXpath("/HTML/BODY/DIV[3]/DIV[2]/DIV/DIV[2]/DIV/DIV/DIV[3]/DIV/UL/LI[5]/I");
        log.info("Got status: " + statusStr);
        return statusStr;
    }

    public Status sendSMSWhenSleep() throws FatalException {
        openUrl(URLConstant.BITE_PEOPLE_URL);
        log.info("Begin send SMS When Sleeping too long...");
        try {
            DomHelper.getInstance().parse(getIe().html());
        } catch (Exception e) {
            log.error("Get Sleeping Status failed! Assume it is sleeping.", e);
            return Status.fail;
        }
        String sleepStatusXpath = "/HTML/BODY/DIV[3]/DIV[2]/DIV/DIV[2]/DIV/DIV/DIV[3]/DIV/UL/LI[5]/I";
        String sleepFriendXpath = "/HTML/BODY/DIV[3]/DIV[2]/DIV/DIV[2]/DIV/DIV/DIV[3]/DIV/UL/LI[5]/I/A";
        String sleepStr = DomHelper.getInstance().getDataByXpath(sleepStatusXpath);
        if (sleepStr.indexOf(ElementConstant.BITE_TEXT_SLEEPING) < 0) {
            log.info("Not slept. terminate the process.");
            return Status.finished;
        }
        HTMLAnchorElement sleepFlink = (HTMLAnchorElement) DomHelper.getInstance().getNodesByXpath(sleepFriendXpath).item(0);
        String friendUid = StringUtil.getUid(sleepFlink.getHref());
        int sleepHours = 0;
        if (sleepStr.indexOf("小时") >= 0) sleepHours = Integer.parseInt(sleepStr.substring(sleepStr.indexOf("(") + 1, sleepStr.indexOf("小")));
        log.info("Got sleeping Hours: " + sleepHours + ", Friend Uid: " + friendUid);
        if (sleepHours > 2 && !StringUtil.isEmpty(friendUid)) {
            Map<String, String> uids = new HashMap<String, String>();
            uids.put(friendUid, sleepFlink.getTextContent());
            String[] smsContents = { "开门让休息一下,呵呵", "咬累了，借你宝地休息一下:)", "没血了想在你这休息。拜托啦:)", "请求同意休息:)" };
            Random rd = new Random();
            return sendSMS(uids, smsContents[rd.nextInt(smsContents.length)]);
        }
        log.debug("No need Send. Terminate sending SMS When Sleeping too long.");
        return Status.finished;
    }

    public Status agreeRest() throws FatalException {
        openUrl(URLConstant.BITE_PEOPLE_URL);
        log.info("Checking if someone need rest...");
        try {
            DomHelper.getInstance().parse(getIe().html());
            String agreeXpath = "/HTML/BODY/DIV[3]/DIV[2]/DIV/DIV[2]/DIV/DIV/DIV[3]/DIV[3]/UL/LI[2]/A";
            NodeList nodes = DomHelper.getInstance().getNodesByXpath(agreeXpath);
            HTMLAnchorElement agreeLink = (HTMLAnchorElement) nodes.item(0);
            if (!"0".equals(StringUtil.getUid(agreeLink.getHref()))) {
                log.info("Someone need rest, agree script excuting...");
                getIe().executeScript(agreeLink.getHref());
                waitAjax();
                log.info("Rest request agreed.");
            } else {
                log.info("No one need rest.");
            }
        } catch (Exception e) {
            handleFatalException(e);
            log.error("Failed to agree rest request.", e);
            return Status.fail;
        }
        return Status.success;
    }

    public Boolean isWaitingWithNoHP() throws FatalException {
        openUrl(URLConstant.BITE_PEOPLE_URL);
        try {
            log.debug("Getting HP status...");
            Link recoverLink = getIe().link(ElementConstant.BITE_LINK_TEXT_RECOVER);
            if (recoverLink.exists()) {
                log.info("Need recover HP.");
            } else {
                log.info("No need to recover HP.");
                return false;
            }
        } catch (Exception e) {
            handleFatalException(e);
            log.error("Failed to get HP Status", e);
            return false;
        }
        return true;
    }

    public Status recoverHP() throws FatalException {
        try {
            String sleepStatusStr = getStatusStr();
            int sleptHours = 0;
            if (sleepStatusStr.indexOf("小时") >= 0) {
                sleptHours = Integer.parseInt(sleepStatusStr.substring(sleepStatusStr.indexOf("(") + 1, sleepStatusStr.indexOf("小时")));
            }
            if (isWaitingWithNoHP()) {
                log.debug("Begin recover HP...");
                for (int i = 0; i < getCfg().getMaxLoopCount(); i++) {
                    openUrl(URLConstant.BITE_PEOPLE_URL);
                    getIe().executeScript(ElementConstant.BITE_LINK_SCRIPT_RESTABLE);
                    waitUntilReady();
                    Frame frame = getIe().frame(id, ElementConstant.IFRAME_ID_POPUP);
                    Link personToRecoverLink = frame.link(0);
                    if (personToRecoverLink.element() == null) {
                        log.error("Can not locate first person link.");
                        return Status.fail;
                    }
                    String href = personToRecoverLink.href();
                    personToRecoverLink.click();
                    waitUntilReady();
                    getIe().executeScript(StringUtil.getScript(href, ElementConstant.BITE_LINK_JSTEMPLATE_REST));
                    waitAjax();
                    Frame resultFrame = getIe().frame(id, ElementConstant.IFRAME_ID_PUPUP_0);
                    String resultStrXpath = "/HTML/BODY/DIV/DIV/DIV[2]/STRONG";
                    DomHelper.getInstance().parse(resultFrame.html());
                    String resultStr = DomHelper.getInstance().getDataByXpath(resultStrXpath);
                    log.debug("Recover HP result: " + resultStr);
                    if (resultStr.indexOf("开始恢复体力") > 0) return Status.success;
                }
            } else {
                log.warn("Finish recover HP. You have no need to do recover HP process.");
                return Status.finished;
            }
        } catch (Exception e) {
            handleFatalException(e);
            log.error("Recover HP failed!", e);
            return Status.fail;
        }
        return Status.fail;
    }

    public Status biteOthersByList() throws FatalException {
        openUrl(URLConstant.BITE_PEOPLE_URL);
        log.info("Begin bite people by list...");
        try {
            if (isWaitingWithNoHP()) {
                log.info("Not enough HP to bite. Need recover HP.");
                return Status.finished;
            }
            if (getStatusStr().indexOf(ElementConstant.BITE_TEXT_SLEEPING) >= 0) {
                log.debug("It is sleeping. finish the whole bite people by list process.");
                return Status.finished;
            }
            getIe().executeScript(ElementConstant.BITE_LINK_SCRIPT_BITABLE);
            waitUntilReady();
            Frame popupFrame = getIe().frame(id, ElementConstant.IFRAME_ID_POPUP);
            Links links = popupFrame.links();
            List<String> allLinks = new ArrayList<String>();
            List<String> onlyListLinks = new ArrayList<String>();
            String biteListStr = getCfg().getBiteList();
            for (Link link : links) {
                if (!ElementConstant.BITE_LINK_CLASS_BITTEN.equals(link.element().getAttribute("class"))) {
                    allLinks.add(link.href());
                    if (!StringUtil.isEmpty(biteListStr) && biteListStr.indexOf(link.text()) >= 0) {
                        onlyListLinks.add(link.href());
                    }
                }
            }
            List<String> exactLinks = onlyListLinks.isEmpty() ? allLinks : onlyListLinks;
            int count = exactLinks.size();
            int exactSize = exactLinks.size();
            while (count > 0) {
                String href = null;
                Random rd = new Random();
                int index = rd.nextInt(exactLinks.size());
                href = exactLinks.get(index);
                Status status = bite(href);
                if (status == Status.finished) {
                    log.info("Finish bite people by list.");
                    if (count < exactSize) return Status.success; else return Status.finished;
                } else if (status == Status.unfinished || status == Status.success) {
                    exactLinks.remove(index);
                    count--;
                }
            }
        } catch (Exception e) {
            handleFatalException(e);
            log.warn("Bite people failed!", e);
            return Status.fail;
        }
        log.info("Finish bite people by list. But it sames you have more HP.");
        return Status.unfinished;
    }

    private Status bite(String strWithUid) throws FatalException {
        String gotoUserUrl = StringUtil.getUrl(strWithUid, URLConstant.BITE_PEOPLE_USER_URL);
        String biteJS = StringUtil.getScript(strWithUid, ElementConstant.BITE_LINK_JSTEMPLATE_BITE);
        openUrl(gotoUserUrl);
        waitUntilReady();
        log.debug("Begin bite one people, run script: " + biteJS);
        try {
            getIe().executeScript(biteJS);
            waitUntilReady();
            Frame frameConfirmBite = getIe().frame(id, ElementConstant.IFRAME_ID_POPUP);
            try {
                frameConfirmBite.element();
            } catch (Exception UnknownObjectException) {
                Frame frameConfirmInfo = getIe().frame(id, ElementConstant.IFRAME_ID_PUPUP_0);
                if (frameConfirmInfo.html().indexOf(ElementConstant.BITE_TEXT_VALUE_BITTEN) > 0) {
                    log.warn("It sames you have bitten him/her too much.");
                    getIe().executeScript(ElementConstant.POPUPIFRAME_SCRIPT_CLOSE);
                    waitUntilReady();
                    return Status.unfinished;
                } else if (frameConfirmInfo.html().indexOf(ElementConstant.BITE_TEXT_VALUE_NOHP) > 0) {
                    log.warn("It sames you have no more HP." + " Terminate the whole bite process.");
                    getIe().executeScript(ElementConstant.POPUPIFRAME_SCRIPT_CLOSE);
                    return Status.finished;
                } else {
                    log.warn("Can not find any useful info. Page Structure changed??");
                }
            }
            Random rd = new Random();
            Radios biteStyles = frameConfirmBite.radios(name, "style");
            Radio biteStyle = biteStyles.get(rd.nextInt(biteStyles.length()));
            biteStyle.set();
            Radios bitePositions = frameConfirmBite.radios(name, "position");
            Radio bitePosition = bitePositions.get(rd.nextInt(bitePositions.length()));
            bitePosition.set();
            Button btn = frameConfirmBite.button(value, ElementConstant.BITE_BUTTON_VALUE_CONFIRM_FEMALE);
            if (btn.element() == null) btn = frameConfirmBite.button(value, ElementConstant.BITE_BUTTON_VALUE_CONFIRM_MALE);
            btn.click();
            waitUntilReady();
            getIe().executeScript(ElementConstant.POPUPIFRAME_SCRIPT_CLOSE);
            waitUntilReady();
        } catch (Exception e) {
            handleFatalException(e);
            log.error("Bite people" + StringUtil.getUid(strWithUid) + " failed!", e);
            return Status.fail;
        }
        log.debug("Finish bite one people.");
        return Status.success;
    }
}
