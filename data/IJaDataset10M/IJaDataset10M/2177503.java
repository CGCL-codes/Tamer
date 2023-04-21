package com.leibict.ussd;

import dao.SlotDAO;

/**
 *
 * @author Acer
 */
public class MenuProvider {

    static String[] REGINT = new String[] { "Enter Destination", "Saved Destinations", "Recent Destinations", "Unregister", "Exit" };

    static String[] UINT = new String[] { "Enter Destination", "Recent Destinations", "Register", "Exit" };

    static String[] REG = new String[] { "Register", "Back", "Exit" };

    static String[] UREG = new String[] { "Unregister", "Back", "Exit" };

    static String[] REGOK = new String[] { "Main Menu", "Exit" };

    static String[] UREGOK = new String[] { "Main Menu", "Exit" };

    static String[] NOREC = new String[] { "Back", "Exit" };

    static String[] NOSUG = new String[] { "Back", "Exit" };

    static String[] CONUREG = new String[] { "Confirm", "Back", "Exit" };

    static String[] CONREG = new String[] { "Confirm", "Schedule", "Back", "Exit" };

    static Menu getRegInt() {
        Menu m = new Menu();
        m.setTitle("Welcome to Thena Hari");
        m.setType("REG-INT");
        m.setChoices(REGINT);
        return m;
    }

    static Menu getUInt() {
        Menu m = new Menu();
        m.setTitle("Welcome to Thena Hari");
        m.setType("U-INT");
        m.setChoices(UINT);
        return m;
    }

    static Menu getREG() {
        Menu m = new Menu();
        m.setTitle("Register in Thena Hari\nYou will be charged 30Rs per Month");
        m.setType("REG");
        m.setChoices(REG);
        return m;
    }

    static Menu getUREG() {
        Menu m = new Menu();
        m.setTitle("Unregister in Thena Hari\nDo You Really want to unregister ?");
        m.setType("UREG");
        m.setChoices(UREG);
        return m;
    }

    static Menu getRegOK() {
        Menu m = new Menu();
        m.setTitle("Thank You for Registering in Thena Hari.\nNever Get Lost");
        m.setType("REG-OK");
        m.setChoices(REGOK);
        return m;
    }

    static Menu getURegOK() {
        Menu m = new Menu();
        m.setTitle("Thank You for using Thena Hari.\n");
        m.setType("UREG-OK");
        m.setChoices(UREGOK);
        return m;
    }

    static Menu getRecentReg(String[] str) {
        String[] med = new String[str.length + 1];
        for (int h = 0; h < str.length; h++) {
            med[h] = str[h];
        }
        med[str.length] = "Back";
        Menu m = new Menu();
        m.setTitle("Your Recent Destinations");
        m.setType("REC-REG");
        m.setChoices(med);
        return m;
    }

    static Menu getSaved(String[] str) {
        String[] med = new String[str.length + 1];
        for (int h = 0; h < str.length; h++) {
            med[h] = str[h];
        }
        med[str.length] = "Back";
        Menu m = new Menu();
        m.setTitle("Your Saved Destinations");
        m.setType("SAV");
        m.setChoices(med);
        return m;
    }

    static Menu getRecentUReg(String[] str) {
        String[] med = new String[str.length + 1];
        for (int h = 0; h < str.length; h++) {
            med[h] = str[h];
        }
        med[str.length] = "Back";
        Menu m = new Menu();
        m.setTitle("Your Recent Destinations");
        m.setType("REC-UREG");
        m.setChoices(med);
        return m;
    }

    static Menu getSugLoc(String[] str) {
        String[] med = new String[str.length + 1];
        for (int h = 0; h < str.length; h++) {
            med[h] = str[h];
        }
        med[str.length] = "Back";
        Menu m = new Menu();
        m.setTitle("No destinations found.\nDid You mean one of the following");
        m.setType("SUG-LOC");
        m.setChoices(med);
        return m;
    }

    static Menu getNoSug() {
        Menu m = new Menu();
        m.setTitle("No Suggestions");
        m.setType("NO-REC");
        m.setChoices(NOSUG);
        return m;
    }

    static Menu getNoRec() {
        Menu m = new Menu();
        m.setTitle("No Recent Destinations");
        m.setType("NO-REC");
        m.setChoices(NOREC);
        return m;
    }

    static Menu getNoSav() {
        Menu m = new Menu();
        m.setTitle("No Saved Destinations");
        m.setType("NO-REC");
        m.setChoices(NOREC);
        return m;
    }

    static Menu getConUReg(String dest) {
        Menu m = new Menu();
        m.setTitle("You Selected " + dest + ".\nYou will receive an alarm at this place");
        m.setType("CON-UREG");
        m.setChoices(CONUREG);
        return m;
    }

    static Menu getConReg(String dest) {
        Menu m = new Menu();
        m.setTitle("You Selected " + dest + ".\nYou will receive an alarm at this place");
        m.setType("CON-REG");
        m.setChoices(CONREG);
        return m;
    }

    static Menu getDes() {
        Menu m = new Menu();
        m.setTitle("Thena Hari.\nEnter The Destination");
        m.setType("ENT");
        return m;
    }

    static Menu getSchedule() {
        Menu m = new Menu();
        m.setTitle("Scehdule The Alarm\nYou will get daily alarms");
        m.setType("SCH");
        SlotDAO slt = new SlotDAO();
        String[] ki = slt.getTimes();
        String[] med = new String[ki.length + 1];
        for (int h = 0; h < ki.length; h++) {
            med[h] = ki[h];
        }
        med[ki.length] = " Back";
        m.setChoices(med);
        return m;
    }
}
