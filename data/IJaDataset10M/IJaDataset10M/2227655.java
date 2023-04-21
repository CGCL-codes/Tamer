package com.googlecode.climb.menu;

import javax.microedition.rms.*;
import java.io.*;
import javax.microedition.lcdui.*;

public class OptionsScreen extends Form implements CommandListener {

    private MainMenu menu;

    private Command toMain;

    private Command resetScore;

    private Command save;

    ChoiceGroup keyJump;

    ChoiceGroup keyLeft;

    ChoiceGroup keyRight;

    public OptionsScreen(MainMenu menu) {
        super("Settings");
        this.menu = menu;
        this.setCommandListener(this);
        this.toMain = new Command("Back", Command.SCREEN, 3);
        this.addCommand(toMain);
        this.save = new Command("Save Changes", Command.SCREEN, 1);
        this.addCommand(save);
        keyJump = new ChoiceGroup("Jump", ChoiceGroup.POPUP);
        keyJump.append("0", null);
        keyJump.append("1", null);
        keyJump.append("2", null);
        keyJump.append("3", null);
        keyJump.append("4", null);
        keyJump.append("5", null);
        keyJump.append("6", null);
        keyJump.append("7", null);
        keyJump.append("8", null);
        keyJump.append("9", null);
        keyJump.append("*", null);
        keyJump.append("#", null);
        keyJump.append("UP_KEY", null);
        keyJump.append("DOWN_KEY", null);
        keyJump.append("LEFT_KEY", null);
        keyJump.append("RIGHT_KEY", null);
        keyJump.append("ACTION_KEY", null);
        keyLeft = new ChoiceGroup("Left", ChoiceGroup.POPUP);
        keyLeft.append("0", null);
        keyLeft.append("1", null);
        keyLeft.append("2", null);
        keyLeft.append("3", null);
        keyLeft.append("4", null);
        keyLeft.append("5", null);
        keyLeft.append("6", null);
        keyLeft.append("7", null);
        keyLeft.append("8", null);
        keyLeft.append("9", null);
        keyLeft.append("*", null);
        keyLeft.append("#", null);
        keyLeft.append("UP_KEY", null);
        keyLeft.append("DOWN_KEY", null);
        keyLeft.append("LEFT_KEY", null);
        keyLeft.append("RIGHT_KEY", null);
        keyLeft.append("ACTION_KEY", null);
        keyRight = new ChoiceGroup("Right", ChoiceGroup.POPUP);
        keyRight.append("0", null);
        keyRight.append("1", null);
        keyRight.append("2", null);
        keyRight.append("3", null);
        keyRight.append("4", null);
        keyRight.append("5", null);
        keyRight.append("6", null);
        keyRight.append("7", null);
        keyRight.append("8", null);
        keyRight.append("9", null);
        keyRight.append("*", null);
        keyRight.append("#", null);
        keyRight.append("UP_KEY", null);
        keyRight.append("DOWN_KEY", null);
        keyRight.append("LEFT_KEY", null);
        keyRight.append("RIGHT_KEY", null);
        keyRight.append("ACTION_KEY", null);
        try2OpenRS();
        applySavedRecs();
        this.append("Key settings");
        this.append(keyJump);
        this.append(keyLeft);
        this.append(keyRight);
    }

    private void applySavedRecs() {
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore("sett", true);
            byte[] byteA = new byte[100];
            byteA = rs.getRecord(1);
            ByteArrayInputStream bytes = new ByteArrayInputStream(byteA);
            DataInputStream dataIn = new DataInputStream(bytes);
            keyJump.setSelectedIndex(dataIn.readInt(), true);
            keyLeft.setSelectedIndex(dataIn.readInt(), true);
            keyRight.setSelectedIndex(dataIn.readInt(), true);
            rs.closeRecordStore();
        } catch (Exception exp) {
            System.out.println("fehler: " + exp.toString());
            exp.printStackTrace();
            keyJump.setSelectedIndex(1, true);
            keyLeft.setSelectedIndex(0, true);
            keyRight.setSelectedIndex(11, true);
        }
    }

    private void try2OpenRS() {
        try {
            RecordStore rs = RecordStore.openRecordStore("sett", false);
            rs.closeRecordStore();
        } catch (RecordStoreNotFoundException exp) {
            initFirstTimeRS();
        } catch (RecordStoreException exo) {
            System.out.println("feilure" + exo.toString());
            exo.printStackTrace();
        }
    }

    private void initFirstTimeRS() {
        try {
            RecordStore rs = null;
            rs = RecordStore.openRecordStore("sett", true);
            ByteArrayOutputStream bytes;
            DataOutputStream dataOut = new DataOutputStream(bytes = new ByteArrayOutputStream());
            dataOut.writeInt(1);
            dataOut.writeInt(0);
            dataOut.writeInt(11);
            byte[] byteA = bytes.toByteArray();
            rs.addRecord(byteA, 0, byteA.length);
            rs.closeRecordStore();
        } catch (Exception exp) {
            System.out.println(exp.toString());
            exp.printStackTrace();
        }
    }

    public void commandAction(Command comm, Displayable disp) {
        if (comm == toMain) menu.show(menu); else if (comm == save) {
            save2Store();
        } else if (comm == resetScore) {
            menu.scoreScreen.resetScore();
        }
    }

    public void save2Store() {
        try {
            RecordStore rs = null;
            RecordStore.deleteRecordStore("sett");
            rs = RecordStore.openRecordStore("sett", true);
            ByteArrayOutputStream bytes;
            DataOutputStream dataOut = new DataOutputStream(bytes = new ByteArrayOutputStream());
            dataOut.writeInt(keyJump.getSelectedIndex());
            dataOut.writeInt(keyLeft.getSelectedIndex());
            dataOut.writeInt(keyRight.getSelectedIndex());
            byte[] byteA = bytes.toByteArray();
            rs.addRecord(byteA, 0, byteA.length);
            rs.closeRecordStore();
        } catch (Exception exp) {
            System.out.println(exp.toString());
        }
    }
}
