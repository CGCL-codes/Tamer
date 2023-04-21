package hog;

import java.util.Iterator;

public class ModelHogFuel extends ModelHogComponent {

    private int[] constants;

    private char lastCommand;

    private int[] realTime;

    private HogMessenger theMessenger;

    private int[] changes;

    private int[] offsets;

    private int changeCount;

    public ModelHogFuel() throws java.io.IOException {
        id = "fuel";
        panelLabel = "Megasquirt Fuel Controller";
        panel = new UiHogFuelMaster(this);
        constants = new int[125];
        realTime = new int[22];
        changes = new int[125];
        offsets = new int[125];
        changeCount = 0;
        lastCommand = 0;
        theMessenger = HogMessenger.getMessenger();
    }

    public final void parseMessage(final HogMessenger sender, final HogMessage m) {
        Iterator<Object> stuff = m.getArgs();
        byte[] bytes = null;
        int numBytes = 0;
        int[] receiver = null;
        int temp = 0;
        if (stuff.hasNext()) {
            bytes = (byte[]) stuff.next();
        }
        if (stuff.hasNext()) {
            numBytes = ((Integer) stuff.next()).intValue();
        }
        if (lastCommand == 'A') {
            receiver = realTime;
        } else if (lastCommand == 'V') {
            receiver = constants;
        } else {
            return;
        }
        if (bytes != null) {
            for (int i = 0; i < numBytes; i++) {
                temp = (int) bytes[i];
                if (temp < 0) {
                    temp += 256;
                }
                receiver[i] = temp;
            }
            System.out.println("ModelHogFuel received " + numBytes + " bytes!");
        }
        ((UiHogFuelMaster) panel).update(lastCommand, receiver);
        return;
    }

    public final void getConstants() throws java.io.IOException {
        int[] args = { (int) 'V' };
        sendMessage(args);
        return;
    }

    public final void burn() throws java.io.IOException {
        int[] args = { (int) 'B' };
        sendMessage(args);
        return;
    }

    public final void getRealTime() throws java.io.IOException {
        int[] args = { (int) 'A' };
        sendMessage(args);
        return;
    }

    public final void addChange(final int offset, final int value) {
        for (int i = 0; i < changeCount; i++) {
            if (offsets[i] == offset) {
                changes[i] = value;
                return;
            }
        }
        offsets[changeCount] = offset;
        changes[changeCount] = value;
        changeCount++;
        return;
    }

    public final void writeChanges() throws java.io.IOException {
        if (changeCount == 0) {
            return;
        }
        int[] args = new int[changeCount * 3];
        int argsIndex = 0;
        for (int i = 0; i < changeCount; i++) {
            args[argsIndex++] = (int) 'W';
            args[argsIndex++] = offsets[i];
            args[argsIndex++] = changes[i];
        }
        sendMessage(args);
        changeCount = 0;
        return;
    }

    private void sendMessage(final int[] intArgs) throws java.io.IOException {
        HogMessage getC = new HogMessage("fuel");
        int mLength = intArgs.length;
        byte[] args = new byte[mLength];
        int temp = 0;
        for (int i = 0; i < mLength; ++i) {
            temp = intArgs[i];
            if (temp > 127) {
                temp -= 256;
            }
            args[i] = (byte) temp;
        }
        getC.addArg(args);
        getC.addArg(new Integer(mLength));
        theMessenger.sendMessage(getC);
        lastCommand = (char) intArgs[0];
        return;
    }
}
