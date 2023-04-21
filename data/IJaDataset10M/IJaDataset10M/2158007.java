package com.csft.client.task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.thoughtworks.selenium.Selenium;

public class T_BuildResource extends Task {

    /**
	 * The resource name
	 */
    private String resource;

    /**
	 * The target level
	 */
    private int targetLevel;

    /**
	 * The constructor
	 */
    public T_BuildResource(Selenium selenium, String resource, int targetLevel) {
        super(selenium);
        this.resource = resource;
        this.targetLevel = targetLevel;
        execute();
    }

    @Override
    protected void execute() {
        log.info("Start to build " + resource + " field.");
        selenium.click("css=#n1 > a");
        selenium.waitForPageToLoad("30000");
        Pattern p1 = Pattern.compile("(?s)resources.production.*?: (\\d+),.*?: (\\d+),.*?: (\\d+),.*?: (\\d+)");
        Matcher m1 = p1.matcher(selenium.getHtmlSource());
        int hWood = 0;
        int hClay = 0;
        int hIron = 0;
        int hCrop = 0;
        boolean parsed = false;
        if (m1.find()) {
            parsed = true;
            hWood = new Integer(m1.group(1));
            hClay = new Integer(m1.group(2));
            hIron = new Integer(m1.group(3));
            hCrop = new Integer(m1.group(4));
        } else {
            log.info("There is no hourly production data. There must be something wrong with the parsing.");
        }
        Pattern p2 = Pattern.compile("(?s)<area href=\"build\\.php\\?id=\\d+\".*?alt=\"([^\"]*?)\"");
        Matcher m2 = p2.matcher(selenium.getHtmlSource());
        boolean foundResourceField = false;
        int minLevel = Integer.MAX_VALUE;
        String minName = "";
        while (m2.find()) {
            foundResourceField = true;
            if (m2.group(1).contains(resource) == false) {
                continue;
            }
            String[] ss = m2.group(1).split(" ");
            int level = new Integer(ss[ss.length - 1]);
            if (minLevel > level) {
                minLevel = level;
                minName = m2.group(1);
            }
        }
        if (minLevel >= targetLevel) {
            log.info("Target level " + targetLevel + " has reached.");
            executed = false;
        } else {
            if (selenium.isElementPresent("css=area[alt=\"" + minName + "\"]")) {
                log.info("Visit " + minName);
                selenium.click("css=area[alt=\"" + minName + "\"]");
                selenium.waitForPageToLoad("30000");
                Pattern p3 = Pattern.compile("(?s)for upgrading to level " + (minLevel + 1) + ".*?<img class=\"clock\"[^>]*?>(\\d?\\d:\\d?\\d:\\d?\\d)</span>");
                Matcher m3 = p3.matcher(selenium.getHtmlSource());
                if (m3.find()) {
                    workTime = getTimeInMillis(m3.group(1));
                    if (selenium.isElementPresent("css=button.build")) {
                        selenium.click("css=button.build");
                        selenium.waitForPageToLoad("30000");
                        executed = true;
                        log.info("Building " + minName + " will take " + timeToString(workTime));
                    } else {
                        log.info("There is no build button.");
                    }
                } else {
                    log.info("There is no work time for the field " + minName + " to upgrade.");
                }
            } else {
                log.info("There is no resource field found with the lowest level. There must be some parsing issue.");
            }
            if (foundResourceField == false) {
                log.info("There is no resource field. There must be some parsing issue.");
            }
            hasWait = true;
            String pp = "(?s)<ul id=\"res\">.*?<li class=\"r";
            String pn = "\".*?alt=\"([^\"]*)\".*?<span.*?>(\\d*?)/(\\d*?)</span>";
            int wood = 0;
            int clay = 0;
            int iron = 0;
            int crop = 0;
            boolean parseResource = true;
            for (int i = 1; i < 5; i++) {
                Pattern p4 = Pattern.compile(pp + i + pn);
                Matcher m4 = p4.matcher(selenium.getHtmlSource());
                if (m4.find()) {
                    int n = Integer.parseInt(m4.group(2));
                    switch(i) {
                        case 1:
                            wood = n;
                            break;
                        case 2:
                            clay = n;
                            break;
                        case 3:
                            iron = n;
                            break;
                        case 4:
                            crop = n;
                            break;
                        default:
                    }
                } else {
                    parseResource = false;
                }
            }
            int needWood = 0;
            int needClay = 0;
            int needIron = 0;
            int needCrop = 0;
            boolean parseNeed = true;
            for (int i = 1; i < 5; i++) {
                String pattern = "(?s)<div id=\"contract\".*?<img class=\"r" + i + "\" src=\"img/x.gif\" alt=\"[^\"]*?\".*?>(\\d+)</span>";
                Pattern p5 = Pattern.compile(pattern);
                Matcher m5 = p5.matcher(selenium.getHtmlSource());
                if (m5.find()) {
                    int resource = parseInt(m5.group(1));
                    switch(i) {
                        case 1:
                            needWood = resource;
                            break;
                        case 2:
                            needClay = resource;
                            break;
                        case 3:
                            needIron = resource;
                            break;
                        case 4:
                            needCrop = resource;
                            break;
                    }
                    m5.end();
                } else {
                    parseNeed = false;
                }
            }
            if (parsed == false || parseResource == false || parseNeed == false) {
                hasWait = false;
            } else {
                waitTime = 0;
                if (wood < needWood) {
                    int t = (needWood - wood) * 60 * 60 / hWood;
                    if (t > waitTime) {
                        waitTime = t;
                    }
                }
                if (clay < needClay) {
                    int t = (needClay - clay) * 60 * 60 / hClay;
                    if (t > waitTime) waitTime = t;
                }
                if (iron < needIron) {
                    int t = (needIron - iron) * 60 * 60 / hIron;
                    if (t > waitTime) waitTime = t;
                }
                if (crop < needCrop) {
                    if (hCrop > 0) {
                        int t = (needCrop - crop) * 60 * 60 / hCrop;
                        if (t > waitTime) waitTime = t;
                    } else {
                        hasWait = false;
                    }
                }
                waitTime = waitTime * 1000;
                if (waitTime == 0) {
                    hasWait = false;
                }
                log.info("The wait time is " + timeToString(waitTime));
            }
        }
        log.info("End building " + resource + " field.");
    }
}
