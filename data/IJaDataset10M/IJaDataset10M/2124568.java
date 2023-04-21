package net.infordata.em.tn5250;

import java.io.IOException;
import java.io.InputStream;
import net.infordata.em.crt5250.XIEbcdicTranslator;

/**
 * 5250 Query command.
 *
 * @version  
 * @author   Valentino Proietti - Infordata S.p.A.
 */
public class XIQueryCmd extends XI5250Cmd {

    protected int[] ivPar = new int[5];

    @Override
    protected void readFrom5250Stream(InputStream inStream) throws IOException {
        for (int i = 0; i < 5; i++) ivPar[i] = inStream.read();
        if (ivPar[0] != 0x00 || ivPar[1] != 0x05 || ivPar[2] != 0xD9 || ivPar[3] != 0x70 || ivPar[4] != 0x00) ;
    }

    @Override
    protected void execute() {
        XIEbcdicTranslator trans = ivEmulator.getTranslator();
        byte[] buf = { (byte) 0x00, (byte) 0x00, (byte) 0x88, (byte) 0x00, (byte) 0x3A, (byte) 0xD9, (byte) 0x70, (byte) 0x80, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) trans.toEBCDIC('5'), (byte) trans.toEBCDIC('2'), (byte) trans.toEBCDIC('5'), (byte) trans.toEBCDIC('1'), (byte) trans.toEBCDIC('0'), (byte) trans.toEBCDIC('1'), (byte) trans.toEBCDIC('1'), (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) (0x03 | 0x40), (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
        ivEmulator.send5250Packet((byte) 0x00, (byte) 0x00, buf);
    }
}
