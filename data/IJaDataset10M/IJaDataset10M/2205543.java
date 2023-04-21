package org.fudaa.ebli.animation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fudaa.ebli.commun.EbliLib;

/**
 * @author Fred Deniger
 * @version $Id: EbliAnimationComposite.java,v 1.6 2006-09-19 14:55:52 deniger Exp $
 */
public class EbliAnimationComposite implements EbliAnimationAdapterInterface {

    final List<EbliAnimationAdapterInterface> anims_;

    /**
   * @param _anims
   */
    public EbliAnimationComposite(final EbliAnimationAdapterInterface[] _anims) {
        super();
        anims_ = Collections.unmodifiableList(Arrays.asList(_anims));
    }

    /**
   * @param _anims
   */
    public EbliAnimationComposite(final List<EbliAnimationAdapterInterface> _anims) {
        super();
        anims_ = Collections.unmodifiableList(_anims);
    }

    /**
   * @return the anims liste non modifiable
   */
    public List<EbliAnimationAdapterInterface> getAnims() {
        return anims_;
    }

    public int getNbTimeStep() {
        return anims_.get(0).getNbTimeStep();
    }

    public double getTimeStepValueSec(int _idx) {
        return anims_.get(0).getTimeStepValueSec(_idx);
    }

    public String getTimeStep(final int _idx) {
        return anims_.get(0).getTimeStep(_idx);
    }

    public String getTitle() {
        return EbliLib.getS("Sources muliples");
    }

    public void setTimeStep(final int _idx) {
        for (EbliAnimationAdapterInterface it : anims_) {
            it.setTimeStep(_idx);
        }
    }
}
