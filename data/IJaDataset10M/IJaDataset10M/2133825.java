package gov.sns.tools.beam.ens;

import gov.sns.tools.beam.*;

/**
 *
 * @author  CKAllen
 */
public final class EnsembleGenerator {

    public static final Ensemble generate(EnsembleDescriptor descr) throws EnsembleException {
        Twiss twissX = new Twiss();
        Twiss twissY = new Twiss();
        Twiss twissZ = new Twiss();
        twissX.setTwiss(descr.ax, descr.bx, descr.ex);
        twissY.setTwiss(descr.ay, descr.by, descr.ey);
        twissZ.setTwiss(descr.az, descr.bz, descr.ez);
        switch(descr.enmProfile) {
            case EnsembleDescriptor.DIST_NONE:
                throw new EnsembleException("EnsembleGenerator::generate() - statistical profile not specified");
            case EnsembleDescriptor.DIST_KV:
                return generateKV(twissX, twissY, twissZ);
            default:
                throw new EnsembleException("EnsembleGenerator::generate() - statistical profile not supported");
        }
    }

    private static Ensemble generateKV(Twiss csX, Twiss csY, Twiss csZ) throws EnsembleException {
        Ensemble ens = new Ensemble();
        return ens;
    }

    private static Ensemble generateWaterbag(Twiss csX, Twiss csY, Twiss csZ) throws EnsembleException {
        Ensemble ens = new Ensemble();
        return ens;
    }

    private static Ensemble generateSemiGauss(int nStd, Twiss csX, Twiss csY, Twiss csZ) throws EnsembleException {
        Ensemble ens = new Ensemble();
        return ens;
    }

    private static Ensemble generateGaussian(int nStd, Twiss csX, Twiss csY, Twiss csZ) throws EnsembleException {
        Ensemble ens = new Ensemble();
        return ens;
    }
}
