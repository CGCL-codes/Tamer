package com.csft.client.task;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.csft.client.FileHelper;
import com.thoughtworks.selenium.Selenium;

public class T_Dodge extends Task {

    /**
	 * The x of the village to dodge to
	 */
    int x;

    /**
	 * The y of the village to dodge to
	 */
    int y;

    /**
	 * The constructor
	 */
    public T_Dodge(Selenium selenium, int x, int y) {
        super(selenium);
        this.x = x;
        this.y = y;
        execute();
    }

    @Override
    protected void execute() {
        if (new File("dodge.txt").exists()) {
            log.info("We want to cancel the troop movement according to the dodge.");
            String s = FileHelper.readFile("dodge.txt");
            log.info("The time to cancel is " + s);
            Date date = new Date(System.currentTimeMillis() + 10);
            try {
                date = dateTimeFormat.parse(s);
            } catch (Exception e) {
                log.info("Nothing we can do because we can not parse date time.");
                return;
            }
            if (date.before(new Date())) {
                selenium.open("/build.php?gid=16");
                if (selenium.isElementPresent("css=div.abort > button.icon")) {
                    selenium.click("css=div.abort > button.icon");
                    selenium.waitForPageToLoad("30000");
                } else {
                    log.info("The cancel button disappeared.");
                    log.info("Nothing we can do now.");
                }
                new File("dodge.txt").delete();
            } else {
                log.info("We are still waiting for the troop to pass.");
            }
        } else {
            log.info("Look for sign of incoming attack.");
            selenium.click("css=#n1 > a");
            selenium.waitForPageToLoad("30000");
            Pattern p1 = Pattern.compile("(?s)<img src=\"img/x.gif\" class=\"(att[123])\".*?" + "<div class=\"mov\"><span class=\".*?\">(\\d+).*?</span>.*?" + "<div class=\"dur_r\">.*?" + "<span id=\"timer\\d+\">([^<]*?)</span>");
            Matcher m1 = p1.matcher(selenium.getHtmlSource());
            while (m1.find()) {
                String move = m1.group(1);
                if (move.equals("att1")) {
                    waitTime = getTimeInMillis(m1.group(3)) - 30 * 1000;
                    if (waitTime < 0) {
                        log.info("Start to dodge.");
                        dodge();
                    } else {
                        hasWait = true;
                        log.info("We can wait to dodge for " + timeToString(waitTime));
                    }
                } else if (move.equals("att2")) {
                } else if (move.equals("att3")) {
                }
            }
            log.info("End looking for sign of incoming attack.");
        }
    }

    /**
	 * Execute the dodge action
	 */
    private void dodge() {
        selenium.open("/a2b.php");
        int[] existingTroops = new int[11];
        Arrays.fill(existingTroops, 0);
        String ex = "Current troop:";
        for (int i = 0; i < 11; i++) {
            Pattern p = Pattern.compile("document.snd.t" + (i + 1) + ".value=(\\d+); return false;");
            Matcher m = p.matcher(selenium.getHtmlSource());
            if (m.find()) {
                existingTroops[i] = new Integer(m.group(1));
            }
            ex += existingTroops[i] + " ";
        }
        log.info(ex);
        log.info("Send them to (" + x + "," + y + ")");
        for (int t = 0; t < 11; t++) {
            if (existingTroops[t] > 0) {
                selenium.type("name=t" + (t + 1), "" + existingTroops[t]);
            }
        }
        selenium.type("id=xCoordInput", "" + x);
        selenium.type("id=yCoordInput", "" + y);
        selenium.click("//input[@name='c' and @value='2']");
        selenium.click("id=btn_ok");
        selenium.waitForPageToLoad("30000");
        selenium.click("id=btn_ok");
        selenium.waitForPageToLoad("30000");
        executed = true;
        workTime = 45 * 1000;
        FileHelper.writeLineByLine("dodge.txt", new String[] { super.dateTimeFormat.format(new Date(System.currentTimeMillis() + workTime)) });
    }
}
