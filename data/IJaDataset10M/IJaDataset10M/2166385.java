package sun.io;

/**
 * A table to convert Cp866 to Unicode
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.6
 */
public class ByteToCharCp866 extends ByteToCharSingleByte {

    public String getCharacterEncoding() {
        return "Cp866";
    }

    public ByteToCharCp866() {
        super.byteToCharTable = byteToCharTable;
    }

    private static final String byteToCharTable = "АБВГДЕЖЗ" + "ИЙКЛМНОП" + "РСТУФХЦЧ" + "ШЩЪЫЬЭЮЯ" + "абвгдежз" + "ийклмноп" + "░▒▓│┤╡╢╖" + "╕╣║╗╝╜╛┐" + "└┴┬├─┼╞╟" + "╚╔╩╦╠═╬╧" + "╨╤╥╙╘╒╓╫" + "╪┘┌█▄▌▐▀" + "рстуфхцч" + "шщъыьэюя" + "ЁёЄєЇїЎў" + "°∙·√№¤■ " + " " + "\b\t\n\f\r" + "" + "" + " !\"#$%&\'" + "()*+,-./" + "01234567" + "89:;<=>?" + "@ABCDEFG" + "HIJKLMNO" + "PQRSTUVW" + "XYZ[\\]^_" + "`abcdefg" + "hijklmno" + "pqrstuvw" + "xyz{|}~";
}
