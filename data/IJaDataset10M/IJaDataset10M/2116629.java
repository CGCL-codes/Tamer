package sun.io;

/**
 * A table to convert Cp869 to Unicode
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.6
 */
public class ByteToCharCp869 extends ByteToCharSingleByte {

    public String getCharacterEncoding() {
        return "Cp869";
    }

    public ByteToCharCp869() {
        super.byteToCharTable = byteToCharTable;
    }

    private static final String byteToCharTable = "������Ά�" + "·¬¦‘’Έ―Ή" + "ΊΪΌ��ΎΫ©" + "Ώ²³ά£έήί" + "ϊΐόύΑΒΓΔ" + "ΕΖΗ½ΘΙ«»" + "░▒▓│┤ΚΛΜ" + "Ν╣║╗╝ΞΟ┐" + "└┴┬├─┼ΠΡ" + "╚╔╩╦╠═╬Σ" + "ΤΥΦΧΨΩαβ" + "γ┘┌█▄δε▀" + "ζηθικλμν" + "ξοπρσςτ΄" + "­±υφχ§ψ΅" + "°¨ωϋΰώ■ " + " " + "\b\t\n\f\r" + "" + "" + " !\"#$%&\'" + "()*+,-./" + "01234567" + "89:;<=>?" + "@ABCDEFG" + "HIJKLMNO" + "PQRSTUVW" + "XYZ[\\]^_" + "`abcdefg" + "hijklmno" + "pqrstuvw" + "xyz{|}~";
}
