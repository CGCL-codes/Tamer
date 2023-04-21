package ttu.iti0011.pold.praktikum01;

public class DiceFive extends Dice implements DiceInterface {

    public final int getFaceNumber() {
        return 5;
    }

    public char[][] toChar() {
        char[][] _char = getEmptyChar();
        _char[1][1] = getFiller();
        _char[3][1] = getFiller();
        _char[2][2] = getFiller();
        _char[1][3] = getFiller();
        _char[3][3] = getFiller();
        return _char;
    }
}
