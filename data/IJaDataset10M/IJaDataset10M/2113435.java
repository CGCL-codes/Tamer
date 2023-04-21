package net.adrianromero.tpv.printer.escpos;

public class UnicodeTranslatorEur extends UnicodeTranslator {

    /** Creates a new instance of UnicodeTranslatorEur */
    public UnicodeTranslatorEur() {
    }

    public byte[] getCodeTable() {
        return ESCPOS.CODE_TABLE_13;
    }

    public byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch(sChar) {
                case 'á':
                    return -0x60;
                case 'é':
                    return -0x7E;
                case 'í':
                    return -0x5F;
                case 'ó':
                    return -0x5E;
                case 'ú':
                    return -0x5D;
                case 'ü':
                    return -0x7F;
                case 'ñ':
                    return -0x5C;
                case 'Ñ':
                    return -0x5B;
                case 'Á':
                    return 0x41;
                case 'É':
                    return 0x45;
                case 'Í':
                    return 0x49;
                case 'Ó':
                    return 0x4F;
                case 'Ú':
                    return 0x55;
                case 'Ü':
                    return -0x66;
                case '¿':
                    return -0x58;
                case '¡':
                    return -0x53;
                case '€':
                    return -0x12;
                default:
                    return 0x3F;
            }
        }
    }

    public byte transNumberChar(char sChar) {
        switch(sChar) {
            case '0':
                return 0x30;
            case '1':
                return 0x31;
            case '2':
                return 0x32;
            case '3':
                return 0x33;
            case '4':
                return 0x34;
            case '5':
                return 0x35;
            case '6':
                return 0x36;
            case '7':
                return 0x37;
            case '8':
                return 0x38;
            case '9':
                return 0x39;
            default:
                return 0x30;
        }
    }
}
