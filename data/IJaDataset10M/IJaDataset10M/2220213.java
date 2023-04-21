package SMS;

import java.util.Date;

public class Modem {

    private int delay;

    PortHandler handle;

    String modelName;

    public Modem(String commport, String baudrate) {
        handle = new PortHandler(commport, baudrate);
        handle.FindComPort();
        getModelname();
    }

    public Modem(PortHandler handle) {
        this.handle = handle;
    }

    public PortHandler getPortHandler() {
        return handle;
    }

    public boolean SendSms(String MobileNo, String Message, int NoOfRetry) {
        String command = "";
        String modelName = getModelname();
        for (int j = 0; j < NoOfRetry; j++) {
            String anssms = "";
            if (modelName.equals("HUAWEI")) {
                command = (new StringBuilder()).append("AT$HSMSSD=").append(MobileNo).append(",").append(Message.length()).append(",").append("0,1").toString();
                anssms = handle.sendAndRecv(command, 30);
            } else if (modelName.equals("TATA")) {
                command = (new StringBuilder()).append("AT*SKT*PRIMO=1,").append(MobileNo).append(",0,4098,1,").append(Message).append(" \r").toString();
                anssms = handle.sendAndRecv(command, 30);
            } else if (modelName.equals("Zteit")) anssms = handle.sendAndRecv((new StringBuilder()).append("AT+CMGS=").append(MobileNo).append("\r").append(Message).append("\032").toString(), 50);
            try {
                System.out.println((new StringBuilder()).append("SMS Delay==").append(delay).toString());
                Thread.sleep(delay);
            } catch (Exception e) {
                System.out.println((new StringBuilder()).append("ERROR IN THREAD==").append(e.getMessage()).toString());
            }
            if (anssms.indexOf(">") != -1 || anssms.indexOf("OK") != -1) {
                System.out.println((new StringBuilder()).append("This is Send Message").append(Message).toString());
                if (modelName.equals("HUAWEI")) anssms = handle.sendAndRecv(Message, 50);
            } else {
                anssms = "ERROR";
            }
            if (anssms.indexOf("OK") != -1) {
                System.out.println("Message Successfully Sent\nMobileno :: " + MobileNo + "\n Message :: " + Message);
                return true;
            }
            System.out.println("Message not sent sucessfully.");
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(" Sending failed ");
        return false;
    }

    public SMSDetails recieveSMS(int address) {
        SMSDetails smd = new SMSDetails();
        String message = null;
        if (modelName.equals("HUAWEI")) {
            message = handle.sendAndRecv("AT$HSMSRD=" + address + " \r", 50);
            handle.sendAndRecv("AT+CMGD=" + address + " \r", 50);
        } else if (modelName.equals("TATA")) {
            message = handle.sendAndRecv("AT*SKT*READMT=" + address + " \r", 50);
            handle.sendAndRecv("AT+CMGD=" + address + " \r", 50);
        } else if (modelName.equals("Zteit")) {
            message = handle.sendAndRecv("AT+CMGR==" + address + " \r", 50);
            handle.sendAndRecv("AT+CMGD=" + address + " \r", 50);
        }
        System.out.println("THIS IS READ TEXT===>" + message);
        smd.generate(message, modelName);
        return smd;
    }

    public String getModelname() {
        String command = "AT+GMI";
        String modelName = handle.sendAndRecv(command, 30);
        if (modelName.indexOf("ERROR") == -1 && modelName.indexOf("OK") >= 0) if (modelName.indexOf("QUALCOMM") >= 0) modelName = "TATA"; else if (modelName.indexOf("HUAWEI") >= 0) modelName = "HUAWEI"; else if (modelName.indexOf("ZTEiT") >= 0) modelName = "Zteit";
        this.modelName = modelName;
        return modelName;
    }

    public static void main(String[] args) {
        Modem send = new Modem("COM4", "115200");
        System.out.println(new Date());
        send.SendSms("9922930640", "Testing ...", 1);
        System.out.println(new Date());
    }
}
