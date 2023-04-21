package atmon;

public class AVRInstrTypes {

    public static final int MAX_WORD_CNT = 2;

    private static final char[] SREG_BITS = { 'C', 'Z', 'N', 'V', 'S', 'H', 'T', 'I' };

    private static final String[] IO_REGS = { "TWBR", "TWSR", "TWAR", "TWDR", "ADCL", "ADCH", "ADCSRA", "ADMUX", "ACSR", "UBRRL", "UCSRB", "UCSRA", "UDR", "SPCR", "SPSR", "SPDR", "PIND", "DDRD", "PORTD", "PINC", "DDRC", "PORTC", "PINB", "DDRB", "PORTB", "PINA", "DDRA", "PORTA", "EECR", "EEDR", "EEARL", "EEARH", "UBRRH", "WDTCR", "ASSR", "OCR2", "TCNT2", "TCCR2", "ICR1L", "ICR1H", "OCR1BL", "OCR1BH", "OCR1AL", "OCR1AH", "TCNT1L", "TCNT1H", "TCCR1B", "TCCR1A", "SFIOR", "OSCCAL", "TCNT0", "TCCR0", "MCUCSR", "MCUCR", "TWCR", "SPMCR", "TIFR", "TIMSK", "GIFR", "GICR", "OCR0", "SPL", "SPH", "SREG" };

    public static AVRInstrType RD_RR = new AIT_RD_RR();

    public static AVRInstrType RDLLD_KL = new AIT_RDLLD_KL();

    public static AVRInstrType RDL_K = new AIT_RDL_K();

    public static AVRInstrType RD = new AIT_RD();

    public static AVRInstrType SBIT = new AIT_SBIT();

    public static AVRInstrType RD_BIT = new AIT_RD_BIT();

    public static AVRInstrType NONE = new AIT_NONE();

    public static AVRInstrType K = new AIT_K();

    public static AVRInstrType LK = new AIT_LK();

    public static AVRInstrType IO_BIT = new AIT_IO_BIT();

    public static AVRInstrType RDL_RRL = new AIT_RDL_RRL();

    public static AVRInstrType RDLL_RRLL = new AIT_RDLL_RRLL();

    public static AVRInstrType RD_IO = new AIT_RD_IO();

    public static AVRInstrType IO_RR = new AIT_IO_RR();

    public static AVRInstrType RD_LK = new AIT_RD_LK();

    public static AVRInstrType LK_RR = new AIT_LK_RR();

    public static AVRInstrType RDL_RRL_D = new AIT_RDL_RRL_D();

    public static AVRInstrType K_2 = new AIT_K_2();

    public static AVRInstrType RD_Q = new AIT_RD_Q();

    public static AVRInstrType Q_RR = new AIT_Q_RR();

    private static class AIT_RD_RR extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            int rr = ((buf[off + 1] & 0x02) << 3) | (buf[off] & 0x0F);
            return " r" + rd + ",r" + rr;
        }
    }

    private static class AIT_RDLLD_KL extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = (buf[off] & 0xF0) >>> 4;
            int k = ((buf[off] & 0xC0) >>> 2) | (buf[off] & 0x0F);
            rd = (rd * 2) + 24;
            return " r" + (rd + 1) + ":r" + rd + ", " + k;
        }
    }

    private static class AIT_RDL_K extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = (buf[off] & 0xF0) >>> 4;
            int k = ((buf[off + 1] & 0x0F) << 4) | (buf[off] & 0x0F);
            rd |= 0x10;
            return " r" + rd + ", " + k;
        }
    }

    private static class AIT_RD extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            if (instr.getAttributesSuffix() == null) {
                return " r" + rd;
            } else {
                return " r" + rd + ", " + instr.getAttributesSuffix();
            }
        }
    }

    private static class AIT_SBIT extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int sbit = (buf[off] & 0x70) >>> 4;
            return " " + SREG_BITS[sbit];
        }
    }

    private static class AIT_RD_BIT extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            int bit = buf[off] & 0x07;
            return " r" + rd + ", " + bit;
        }
    }

    private static class AIT_NONE extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            return "";
        }
    }

    private static class AIT_K extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int k = ((buf[off + 1] & 0x03) << 5) | ((buf[off] & 0xF8) >>> 3);
            if (k >= 64) {
                k = (k & 63) - 64;
            }
            k = k + addr + 1;
            return " " + mf.convFlashAddrToString(k);
        }
    }

    private static class AIT_LK extends AVRInstrType {

        public int getWordCnt() {
            return 2;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int k = (buf[off + 1] & 0x01) << 5;
            k |= (buf[off] & 0xF0) >>> 3;
            k |= buf[off] & 0x01;
            k = (k << 16) | (buf[off + 3] << 8) | buf[off + 2];
            return " " + mf.convFlashAddrToString(k);
        }
    }

    private static class AIT_IO_BIT extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int io = (buf[off] & 0xF8) >>> 3;
            int bit = buf[off] & 0x07;
            return " " + IO_REGS[io] + ", " + bit;
        }
    }

    private static class AIT_RDL_RRL extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = (buf[off] & 0xF0) >>> 4;
            int rr = buf[off] & 0x0F;
            rd |= 0x10;
            rr |= 0x10;
            return " r" + rd + ", r" + rr;
        }
    }

    private static class AIT_RDLL_RRLL extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = (buf[off] & 0x70) >>> 4;
            int rr = buf[off] & 0x07;
            rd |= 0x10;
            rr |= 0x10;
            return " r" + rd + ", r" + rr;
        }
    }

    private static class AIT_RD_IO extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            int io = ((buf[off + 1] & 0x06) << 3) | (buf[off] & 0x0F);
            return " r" + rd + ", " + IO_REGS[io];
        }
    }

    private static class AIT_IO_RR extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            int io = ((buf[off + 1] & 0x06) << 3) | (buf[off] & 0x0F);
            return " " + IO_REGS[io] + ", r" + rd;
        }
    }

    private static class AIT_RD_LK extends AVRInstrType {

        public int getWordCnt() {
            return 2;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            int k = (buf[off + 3] << 8) | buf[off + 2];
            return " r" + rd + ", 0x" + Utils.wordToHex(k);
        }
    }

    private static class AIT_LK_RR extends AVRInstrType {

        public int getWordCnt() {
            return 2;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rr = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            int k = (buf[off + 3] << 8) | buf[off + 2];
            return " 0x" + Utils.wordToHex(k) + ", r" + rr;
        }
    }

    private static class AIT_RDL_RRL_D extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = (buf[off] & 0xF0) >>> 4;
            int rr = buf[off] & 0x0F;
            rd *= 2;
            rr *= 2;
            return " r" + (rd + 1) + ":r" + rd + ", r" + (rr + 1) + ":r" + rr;
        }
    }

    private static class AIT_K_2 extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int k = ((buf[off + 1] & 0x0F) << 8) | buf[off];
            if (k >= 2048) {
                k = (k & 2047) - 2048;
            }
            k = k + addr + 1;
            return " " + mf.convFlashAddrToString(k);
        }
    }

    private static class AIT_RD_Q extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            int q = (buf[off + 1] & 0x20) | ((buf[off + 1] & 0x0C) << 1) | (buf[off] & 0x07);
            return " r" + rd + ", " + instr.getAttributesSuffix() + q;
        }
    }

    private static class AIT_Q_RR extends AVRInstrType {

        public int getWordCnt() {
            return 1;
        }

        public String getAttribs(MainFrame mf, AVRInstr instr, int addr, byte[] buf, int off) {
            int rd = ((buf[off + 1] & 0x01) << 4) | ((buf[off] & 0xF0) >>> 4);
            int q = (buf[off + 1] & 0x20) | ((buf[off + 1] & 0x0C) << 1) | (buf[off] & 0x07);
            return "" + q + ", r" + rd;
        }
    }
}
