package pulsarhunter.jreaper.pmsurv;

import coordlib.Beam;
import java.io.File;
import pulsarhunter.jreaper.Cand;
import pulsarhunter.jreaper.CandList;
import pulsarhunter.jreaper.CandScorer;
import pulsarhunter.jreaper.CandidateReader;
import pulsarhunter.jreaper.Main;
import pulsarhunter.jreaper.Score;

/**
 *
 * @author mkeith
 */
public class aphfile_CandidateReader implements CandidateReader {

    private CandList testCandList = new CandList(null, new Cand[0][0], null);

    /** Creates a new instance of phfile_CandidateReader */
    public aphfile_CandidateReader() {
        testCandList.setFch1(1374.0f);
        testCandList.setBand(288.0);
        testCandList.setTobs(2100);
    }

    public Cand getFromFile(File file, CandScorer scorer) {
        try {
            PulsarFile_aph phfile = new PulsarFile_aph(file);
            phfile.read();
            double period = phfile.getPeriod();
            float SNR = phfile.getSNR();
            float DM = phfile.getDM();
            double accel = phfile.getAccel();
            Beam beam = phfile.getBeam();
            double mjd = phfile.getMJD();
            double specsnr = phfile.getSpecSnr();
            double reconsnr = phfile.getReconSnr();
            Score score = null;
            testCandList.setBeam(phfile.getBeam());
            Cand pCand = new Cand(phfile, period, SNR, DM, 0, 0, null, mjd);
            pCand.setCandList(this.testCandList);
            int np = pCand.getNPulses();
            if (scorer != null) score = scorer.score(pCand);
            phfile = null;
            pCand = null;
            Cand result = new Cand(new PulsarFile_aph(file), period, SNR, DM, accel, 0, score, mjd);
            result.setSpecSNR((float) specsnr);
            result.setReconSNR((float) reconsnr);
            result.setCandList(this.testCandList);
            result.setNPulses(np);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public int getSearchType() {
        return 1;
    }
}
