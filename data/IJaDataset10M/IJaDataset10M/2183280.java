package net.sourceforge.entrainer.gui.animation;

import net.sourceforge.entrainer.mediator.EntrainerMediator;
import net.sourceforge.entrainer.mediator.ReceiverAdapter;
import net.sourceforge.entrainer.mediator.ReceiverChangeEvent;

/**
 * For future development....
 * @author burton
 *
 */
public abstract class NiaBrainwaveAnimation extends AbstractEntrainerAnimation {

    private double alpha1;

    private double alpha2;

    private double alpha3;

    private double beta1;

    private double beta2;

    private double beta3;

    public NiaBrainwaveAnimation() {
        super();
        setHideEntrainerFrame(true);
        initMediator();
    }

    private void initMediator() {
        EntrainerMediator.getInstance().addReceiver(new ReceiverAdapter(this) {

            @Override
            protected void processReceiverChangeEvent(ReceiverChangeEvent e) {
                if (e.isNiaAlpha1()) {
                    setAlpha1(e.getDoubleValue());
                } else if (e.isNiaAlpha2()) {
                    setAlpha2(e.getDoubleValue());
                } else if (e.isNiaAlpha3()) {
                    setAlpha3(e.getDoubleValue());
                } else if (e.isNiaBeta1()) {
                    setBeta1(e.getDoubleValue());
                } else if (e.isNiaBeta2()) {
                    setBeta2(e.getDoubleValue());
                } else if (e.isNiaBeta3()) {
                    setBeta3(e.getDoubleValue());
                }
            }
        });
    }

    protected double getAlpha1() {
        return alpha1;
    }

    private void setAlpha1(double alpha1) {
        this.alpha1 = alpha1;
    }

    protected double getAlpha2() {
        return alpha2;
    }

    private void setAlpha2(double alpha2) {
        this.alpha2 = alpha2;
    }

    protected double getAlpha3() {
        return alpha3;
    }

    private void setAlpha3(double alpha3) {
        this.alpha3 = alpha3;
    }

    protected double getBeta1() {
        return beta1;
    }

    private void setBeta1(double beta1) {
        this.beta1 = beta1;
    }

    protected double getBeta2() {
        return beta2;
    }

    private void setBeta2(double beta2) {
        this.beta2 = beta2;
    }

    protected double getBeta3() {
        return beta3;
    }

    private void setBeta3(double beta3) {
        this.beta3 = beta3;
    }
}
