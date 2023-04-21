package org.javasign.util;

import java.util.Arrays;
import org.javasign.util.ApduData;
import com.jaccal.Atr;
import com.jaccal.CardResponse;
import com.jaccal.Session;
import com.jaccal.SessionFactory;
import com.jaccal.command.ApduCmd;

public class FilePerms {

    public static void main(String[] args) {
        SessionFactory factory;
        Session session;
        Atr atr = null;
        ;
        ApduCmd cmd = null;
        CardResponse response = null;
        byte[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        String MF = "3F00";
        byte[] OK = { (byte) 0x90, (byte) 0x00 };
        try {
            factory = SessionFactory.getInstance();
            session = factory.createSession("default");
            System.out.println("Power on");
            atr = session.open();
            System.out.println(atr.toString());
            String DF = "3F00";
            cmd = new ApduCmd("00A4080002" + DF);
            System.out.println(cmd.toString());
            response = session.execute(cmd);
            System.out.println(response.toString());
            ApduData.printResponse(response, false);
            String pwd = "abcdefgh";
            String PIN = ApduData.toHexString(pwd.getBytes());
            String BS_ID = "32";
            String BS = "830200" + BS_ID + "8508020F870FFFFF0008" + "860700000000000000" + "8B10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF" + "8F" + ApduData.hexLength(PIN) + PIN;
            cmd = new ApduCmd("00DA016E" + ApduData.hexLength(BS) + BS);
            System.out.println(cmd.toString());
            response = session.execute(cmd);
            System.out.println(response.toString());
            ApduData.printResponse(response, false);
            if (!Arrays.equals(response.getStatusWord().getBytes(), OK)) return;
            String F_ID = "1221";
            String FV = "81020110" + "820301FF00" + "8302" + F_ID + "8503010000" + "8609" + BS_ID + "0000000000000000" + "8B18FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
            String FF = "6F" + ApduData.hexLength(FV) + FV;
            cmd = new ApduCmd("00E00000" + ApduData.hexLength(FF) + FF);
            System.out.println(cmd.toString());
            response = session.execute(cmd);
            System.out.println(response.toString());
            ApduData.printResponse(response, false);
            if (!Arrays.equals(response.getStatusWord().getBytes(), OK)) return;
            cmd = new ApduCmd("002000" + BS_ID + ApduData.hexLength(PIN) + PIN);
            System.out.println(cmd.toString());
            response = session.execute(cmd);
            System.out.println(response.toString());
            ApduData.printResponse(response, false);
            if (!Arrays.equals(response.getStatusWord().getBytes(), OK)) return;
            System.out.println("Power off");
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
