package org.expasy.jpl.experimental.ms.lcmsms.writers;

import java.io.PrintStream;
import org.expasy.jpl.commons.ms.peak.JPLCharge;
import org.expasy.jpl.experimental.exceptions.JPLWriterException;
import org.expasy.jpl.experimental.ms.lcmsms.JPLRunLcmsms;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationSpectrum;

/**
 * @author alex
 * 
 */
public class JPLRunLcmsmsWriterMGF extends JPLRunLcmsmsWriter {

    @Override
    public void write(PrintStream out, JPLRunLcmsms run) throws JPLWriterException {
        if (run.getSource() != null) out.println("COM=" + run.getSource().getTitle());
        JPLFragmentationSpectrum spectrum;
        while ((spectrum = run.iterator().next()) != null) {
            out.print("\n\n");
            out.println("BEGIN ION");
            out.print("TITLE=" + spectrum.getTitle());
            if (spectrum.getPrecursor().getFragmentationMethod() != null) {
                out.println(", frag method: " + spectrum.getPrecursor().getFragmentationMethod());
            }
            out.println("PEPMASS=" + spectrum.getPrecursor().getMz() + " " + spectrum.getPrecursor().getIntensity());
            out.println("CHARGE=" + JPLCharge.chargestate2String(spectrum.getPrecursor().getChargeState()));
            if (spectrum.getRetentionTime() != null) {
                out.println("RETENTIONTIME=" + spectrum.getRetentionTime().getValue() + " " + spectrum.getRetentionTime().getUnit());
            }
            int n = spectrum.getNbPeak();
            for (int i = 0; i < n; i++) {
                out.println("" + spectrum.getMzAt(i) + "\t" + spectrum.getIntensityAt(i));
            }
            out.println("END IONS");
        }
    }
}
