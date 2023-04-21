package j8086emu.model.hardware.formats;

import j8086emu.model.interfaces.Variable;
import j8086emu.model.progcode.types.VarTypes;
import j8086emu.model.utils.Data;

/**
 *
 * @author sone
 */
public class GeneralRegisterDataFormat implements Variable {

    private int data;

    private final int HEX = 16;

    private VarTypes type;

    public GeneralRegisterDataFormat(VarTypes type, int data) {
        this.data = data;
        this.type = type;
    }

    public void setData(int data) {
        this.data = data;
    }

    public VarTypes getType() {
        return type;
    }

    public int getData() {
        return data;
    }

    public String getHexData() {
        return Data.intToHex(data, 4);
    }

    public void setHexData(String hexData) {
        data = Integer.parseInt(hexData, HEX);
    }

    public int getLowBits() {
        String hexData = Data.intToHex(data, 4);
        hexData = hexData.substring(2);
        return Integer.parseInt(hexData, 16);
    }

    public int getHighBits() {
        String hexData = Data.intToHex(data, 4);
        hexData = hexData.substring(0, 2);
        return Integer.parseInt(hexData, 16);
    }

    public String getHexLowBits() {
        String hexData = Data.intToHex(data, 4);
        return hexData.substring(2);
    }

    public String getHexHighBits() {
        String hexData = Data.intToHex(data, 4);
        return hexData.substring(0, 2);
    }

    public void setLowBitsData(int data) {
        setHexData(getHexHighBits() + Data.intToHex(data, 2));
    }

    public void setHighBitsData(int data) {
        setHexData(Data.intToHex(data, 2) + getHexLowBits());
    }
}
