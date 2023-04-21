package pulsarhunter.jreaper.pmsurv;

import com.bbn.openmap.dataAccess.shape.input.LittleEndianInputStream;
import coordlib.Beam;
import coordlib.Coordinate;
import coordlib.Dec;
import coordlib.RA;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import pulsarhunter.jreaper.Cand;
import pulsarhunter.jreaper.CandidateFile;
import pulsarhunter.jreaper.Main;
import pulsarhunter.jreaper.gui.MainView;

/**
 *
 * @author mkeith
 */
public class PulsarFile_aph extends PulsarCandFile {

    /** Creates a new instance of PulsarFile_ph */
    public PulsarFile_aph(String phFile) {
        super(new File(phFile));
    }

    public PulsarFile_aph(File phFile) {
        super(phFile);
    }

    public void precache() {
        read();
    }

    public void read() {
        if (read) return;
        try {
            LittleEndianInputStream instream = getInputStream();
            instream.skipBytes(4);
            tsmp = instream.readLEDouble();
            nbin = instream.readLEInt();
            nbine = instream.readLEInt();
            nchan = instream.readLEInt();
            nband = instream.readLEInt();
            npsub = instream.readLEInt();
            dmc = instream.readLEFloat();
            pc = instream.readLEDouble();
            instream.skipBytes(8);
            plhd1 = instream.readString(120);
            plhd2 = instream.readString(120);
            plhd3 = instream.readString(120);
            plhd9 = instream.readString(120);
            instream.skipBytes(8);
            nsub = instream.readLEInt();
            rms = instream.readLEFloat();
            rmss = instream.readLEFloat();
            snrmax = instream.readLEFloat();
            kwmax = instream.readLEInt();
            ppmax = instream.readLEDouble();
            ppmaxe = instream.readLEFloat();
            dmmax = instream.readLEFloat();
            dmmaxe = instream.readLEFloat();
            pa = instream.readLEFloat();
            pb = instream.readLEFloat();
            dma = instream.readLEFloat();
            dmb = instream.readLEFloat();
            nprd = instream.readLEInt();
            nfdot = instream.readLEInt();
            plhd8 = instream.readString(120);
            nn = nfdot * nprd;
            instream.skipBytes(8);
            pdma = new float[nn];
            for (int n = 0; n < nn; n++) pdma[n] = instream.readLEFloat();
            instream.skipBytes(8);
            nn = nsub * nbin;
            wrk = new float[nn];
            for (int j = 0; j < nn; j++) wrk[j] = instream.readLEFloat();
            instream.skipBytes(8);
            nn = nband * nbin;
            frph = new float[nn];
            for (int j = 0; j < nn; j++) frph[j] = instream.readLEFloat();
            instream.skipBytes(8);
            ymin = instream.readLEFloat();
            ymax = instream.readLEFloat();
            plhd4 = instream.readString(60);
            plhd4a = instream.readString(60);
            plhd5 = instream.readString(60);
            plhd5a = instream.readString(60);
            plhd6 = instream.readString(60);
            plhd7 = instream.readString(60);
            instream.skipBytes(8);
            prmax = new float[nbin];
            for (int j = 0; j < nbin; j++) prmax[j] = instream.readLEFloat();
            instream.skipBytes(8);
            ndms = instream.readLEInt();
            instream.skipBytes(8);
            snlist = new float[ndms];
            for (int j = 0; j < ndms; j++) snlist[j] = instream.readLEFloat();
            instream.skipBytes(8);
            instream.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot Read file " + file.getName());
        }
        read = true;
    }

    /**
     *Override to get the p-pdot rather than p-dm for the aph files
     **/
    public int[][] getPDMprof(int scaleTo) {
        read();
        float max = 0.0f;
        float min = 0.0f;
        for (int i = 0; i < pdma.length; i++) {
            if (pdma[i] > max) max = pdma[i];
            if (pdma[i] < min) min = pdma[i];
        }
        for (int i = 0; i < pdma.length; i++) {
            pdma[i] = pdma[i] - min;
        }
        max = max - min;
        int posn;
        int[][] res = new int[nprd][nfdot];
        int prd;
        for (int i = 0; i < nfdot; i++) {
            for (int j = 0; j < nprd; j++) {
                prd = j;
                while (prd >= nprd) prd -= nprd;
                posn = nprd * i + prd;
                res[j][i] = (int) ((pdma[posn] / max) * scaleTo);
            }
        }
        return res;
    }

    public double getAccel() {
        read();
        String[] accString = plhd6.split(":");
        return Double.parseDouble(accString[1].split("Err")[0]);
    }

    public float getDM() {
        read();
        String[] dmstring = plhd2.split(":");
        return Float.parseFloat(dmstring[dmstring.length - 1].trim());
    }
}
