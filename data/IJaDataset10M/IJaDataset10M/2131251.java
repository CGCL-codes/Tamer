package net.blogbotplatform.blogbot;

import java.util.ArrayList;

public class BlogBotService {

    BlogBotPanel parent;

    boolean serviceWake;

    boolean firstStart = false;

    TwitterBrigde tb;

    XMLBuffer myXMLBuffer;

    String filename;

    String filePath;

    String fileList = "";

    String arcPath;

    static final int SMALL_ARRAY_INDEX = 100;

    static final int MAX_ARRAY_INDEX = 3600;

    int[] sampleBuffer = new int[SMALL_ARRAY_INDEX];

    int sampleBufferIndex = 0;

    int[] dataValues = new int[MAX_ARRAY_INDEX];

    int dataValuesIndex = 0;

    int currentValue;

    int bufferStartTime;

    long pulseTimer;

    int PULSE_INTERVAL = 50;

    long sampleTimer;

    int SAMPLE_INTERVAL = 2000;

    long writeTimer;

    int WRITE_INTERVAL = 3600000;

    long liveTimer;

    int LIVE_TIME_OUT = 18000;

    long currentTime;

    BlogBotSerialBridge bbsb;

    BlogBotSimulator bbsim;

    LinePrinterBridge lp;

    BlogBotArchive arc;

    int mode, previousMode;

    boolean liveMode = false;

    boolean timerInterupted = false;

    long liveStart;

    int randomNumber, lastRandom;

    boolean simulated = false;

    public BlogBotService(BlogBotPanel _parent) {
        parent = _parent;
        serviceWake = false;
        tb = new TwitterBrigde();
        myXMLBuffer = new XMLBuffer();
        new BlogBotShop();
        new BlogBotLoader();
        System.out.println(fileList);
        if (bbsb == null) {
            System.out.println("prepping serial port.");
            bbsb = new BlogBotSerialBridge();
        }
        if (lp == null) {
            System.out.println("setting up dot matrix printer.");
            lp = new LinePrinterBridge();
        }
        filePath = "logs/";
        myXMLBuffer.startXMLBuffer();
        arcPath = "arc/";
        arc = new BlogBotArchive();
        pulseTimer = System.currentTimeMillis() + PULSE_INTERVAL;
        sampleTimer = System.currentTimeMillis() + SAMPLE_INTERVAL;
        writeTimer = System.currentTimeMillis() + WRITE_INTERVAL;
    }

    void runBlogBotService() {
        if (BlogBotShop.timedSleep == true) checkAlarmClock();
        if (simulated) {
            bbsim.runSimulator();
        }
        if (BlogBotShop.lastByte == 255) {
            currentTime = System.currentTimeMillis();
            if (firstStart == true) {
                firstStart = false;
                mode = checkMode(BlogBotShop.dataBuffer[0]);
                previousMode = mode;
            }
            if (currentTime > pulseTimer) {
                System.out.print(".");
                sampleBuffer[sampleBufferIndex] = BlogBotShop.dataBuffer[0];
                sampleBufferIndex++;
                if (sampleBufferIndex >= sampleBuffer.length) sampleBufferIndex = 0;
                pulseTimer = currentTime + PULSE_INTERVAL;
            }
            if (serviceWake == true) {
                if (currentTime > sampleTimer) {
                    int sumOfSamples = 0;
                    for (int i = 0; i < sampleBufferIndex; i++) sumOfSamples += sampleBuffer[i];
                    currentValue = sumOfSamples / (sampleBufferIndex);
                    sampleBufferIndex = 0;
                    dataValues[dataValuesIndex] = currentValue;
                    dataValuesIndex++;
                    if (dataValuesIndex >= dataValues.length) dataValuesIndex = 0;
                    myXMLBuffer.fillXML();
                    System.out.print("current value " + currentValue + " ");
                    previousMode = mode;
                    mode = checkMode(currentValue);
                    System.out.println(mode);
                    if (mode != previousMode) {
                        liveMode = true;
                        liveTimer = System.currentTimeMillis();
                        if (timerInterupted == false) {
                            System.out.print("I");
                            blogDataValues();
                            timerInterupted = true;
                            liveStart = currentTime;
                        }
                        blogCurrent();
                    } else {
                        liveMode = false;
                        if ((timerInterupted == true) && (System.currentTimeMillis() > liveTimer + LIVE_TIME_OUT)) {
                            writeTimer = currentTime + WRITE_INTERVAL;
                            timerInterupted = false;
                            System.out.println(writeTimer);
                            lp.spitOut();
                            arc.fillArc("\n");
                            parent.outputTA.append("\n");
                        }
                    }
                    sampleTimer = currentTime + SAMPLE_INTERVAL;
                }
                if ((currentTime > writeTimer) && (timerInterupted == false)) {
                    System.out.print("X");
                    blogDataValues();
                    lp.spitOut();
                    arc.fillArc("\n");
                    parent.outputTA.append("\n");
                    writeTimer = currentTime + WRITE_INTERVAL;
                }
            }
        }
    }

    void blogDataValues() {
        Utils.setTimeStamp();
        int averageData = 0;
        int totalData = 0;
        String timeStamp = Utils.returnTimeStampAsText();
        System.out.println();
        System.out.println("TimeStamp: " + timeStamp);
        String blogText;
        String pBlogText;
        String interpText = "";
        for (int dataCount = 0; dataCount < dataValuesIndex; dataCount++) {
            totalData = totalData + dataValues[dataCount];
        }
        averageData = totalData / dataValuesIndex;
        int averageMode = checkMode(averageData);
        if ((averageMode > -1) && (averageMode < BlogBotShop.NUM_OF_MODES)) {
            lastRandom = randomNumber;
            if (BlogBotShop.PAST_COMMENTS[averageMode].size() > 1) {
                while (lastRandom == randomNumber) {
                    randomNumber = (int) Math.floor(Math.random() * BlogBotShop.PAST_COMMENTS[averageMode].size());
                }
            } else {
                randomNumber = 0;
            }
            System.out.println("Mode " + mode + " RandomNumber " + randomNumber);
            interpText = (String) BlogBotShop.PAST_COMMENTS[averageMode].get(randomNumber);
        } else {
            interpText = "I Don't Remember!";
        }
        blogText = timeStamp + " >> " + " Samples : " + (dataValuesIndex + 1) + " Reading : " + totalData + " Average : " + averageData + " >> " + interpText;
        pBlogText = timeStamp + " >> " + " Samples : " + (dataValuesIndex + 1) + " Reading : " + totalData + " Average : " + averageData + "\n         >> " + interpText;
        parent.outputTA.append(pBlogText + "\n");
        arc.fillArc(pBlogText);
        System.out.println(blogText);
        lp.sendPrintln(pBlogText);
        if (BlogBotShop.useTwitter == true) {
            if (tb.checkAlive() == true) tb.update(blogText);
        }
        myXMLBuffer.writeXML(blogText, filePath);
        dataValuesIndex = 0;
    }

    void blogCurrent() {
        String liveText;
        String liveBlogLine;
        String pLiveBlogLine;
        int elapsedTime;
        String elapsedAsString;
        elapsedTime = (int) (currentTime - liveStart) / 1000;
        if ((mode > -1) && (mode < BlogBotShop.NUM_OF_MODES)) {
            lastRandom = randomNumber;
            if (BlogBotShop.LIVE_COMMENTS[mode].size() > 1) {
                while (lastRandom == randomNumber) {
                    randomNumber = (int) Math.floor(Math.random() * BlogBotShop.LIVE_COMMENTS[mode].size());
                }
            } else {
                randomNumber = 0;
            }
            System.out.println("Mode " + mode + " RandomNumber " + randomNumber);
            liveText = (String) BlogBotShop.LIVE_COMMENTS[mode].get(randomNumber);
        } else {
            liveText = "I'm feeling Dizzy!";
        }
        elapsedAsString = "+";
        if (elapsedTime < 100) elapsedAsString += "0";
        if (elapsedTime < 10) elapsedAsString += "0";
        elapsedAsString += elapsedTime;
        liveBlogLine = elapsedAsString + " >> " + currentValue + " >> " + liveText;
        pLiveBlogLine = elapsedAsString + "     >> " + liveText;
        parent.outputTA.append(pLiveBlogLine + "\n");
        arc.fillArc(liveBlogLine);
        System.out.println(liveBlogLine);
        lp.sendPrintln(pLiveBlogLine);
        if (BlogBotShop.useTwitter == true) {
            if (tb.checkAlive() == true) tb.update(liveBlogLine);
        }
    }

    int checkMode(int sentValue) {
        int returnMode;
        if (sentValue < 30) {
            returnMode = 0;
        } else if ((sentValue >= 40) && (sentValue < 50)) {
            returnMode = 1;
        } else if ((sentValue >= 60) && (sentValue < 90)) {
            returnMode = 2;
        } else if ((sentValue >= 100) && (sentValue < 140)) {
            returnMode = 3;
        } else if ((sentValue >= 150) && (sentValue < 170)) {
            returnMode = 4;
        } else if ((sentValue >= 180) && (sentValue < 240)) {
            returnMode = 5;
        } else if (((sentValue >= 30) && (sentValue < 40)) || ((sentValue >= 50) && (sentValue < 60)) || ((sentValue >= 90) && (sentValue < 100)) || ((sentValue >= 140) && (sentValue < 150)) || ((sentValue >= 170) && (sentValue < 180))) {
            returnMode = mode;
        } else {
            returnMode = -1;
            ;
        }
        return returnMode;
    }

    void wakeService() {
        serviceWake = true;
        BlogBotLoader.loadConfig();
        if (BlogBotShop.useTwitter == true) tb.setTwitterAccount();
        Utils.setTimeStamp();
        String timeStamp = Utils.returnTimeStampAsText();
        parent.outputTA.setText("");
        parent.outputTA.append(timeStamp + " >> Blog Bot Awake \n\n");
        arc.fillArc(timeStamp + " >> Blog Bot Awake\n\n");
        lp.sendPrint(timeStamp);
        lp.sendPrintln(" >> Blog Bot Awake");
        lp.spitOut();
        if (BlogBotShop.useTwitter == true) {
            if (tb.checkAlive() == true) tb.update(timeStamp + " >> Blog Bot Awake");
        }
    }

    void sleepService() {
        if (firstStart == false) {
            serviceWake = false;
            Utils.setTimeStamp();
            String timeStamp = Utils.returnTimeStampAsText();
            parent.outputTA.append(timeStamp + " >> Blog Bot Asleep \n\n");
            arc.fillArc("\n" + timeStamp + " >> Blog Bot Asleep");
            arc.writeArc(arcPath);
            lp.sendPrintln(timeStamp + " >> Blog Bot Asleep");
            lp.spitOut();
            if (BlogBotShop.useTwitter) {
                if (tb.checkAlive() == true) tb.update(timeStamp + " >> Blog Bot Asleep");
            }
        }
    }

    void checkAlarmClock() {
        Utils.setTimeStamp();
        int currentHour = Utils.hour;
        if ((currentHour >= BlogBotShop.wakeHour) && (currentHour < BlogBotShop.sleepHour) && (serviceWake == false)) wakeService();
        if (((currentHour >= BlogBotShop.sleepHour) || (currentHour < BlogBotShop.wakeHour)) && (serviceWake == true)) sleepService();
    }

    void changeSerialPort(String portName) {
        simulated = false;
        int baudRate = 9600;
        bbsb.closeSerialPort();
        if (portName.equals("Off")) return;
        if (portName.equals("Simulator")) {
            System.out.println("Starting the Simulator");
            if (bbsim == null) bbsim = new BlogBotSimulator();
            simulated = true;
        } else {
            bbsb.openSerialPort(portName, baudRate);
        }
    }

    void changeParallelPort(String portName) {
        lp.closeLinePrinterPort();
        lp.openLinePrinterPort(portName);
    }

    void stopService() {
        sleepService();
        bbsb.closeSerialPort();
        lp.closeLinePrinterPort();
    }
}
