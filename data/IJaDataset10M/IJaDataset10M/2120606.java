package vf1.rn;

import vf1.iu.VF1_IU_IEspera;

public interface VF1_RN_IEspera {

    public void notifyObserver();

    public void registerObserver(VF1_IU_IEspera espera);

    public void unregisterObserver(VF1_IU_IEspera espera);
}
