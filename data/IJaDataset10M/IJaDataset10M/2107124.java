package gleam.docservice.proxy.iaa;

import gleam.docservice.proxy.IAAResult;

/**
 * Interface representing the results of an all-ways kappa
 * IAA calculation. 
 */
public interface AllWaysKappaIAAResult extends IAAResult {

    public float getKappaSC();

    public float getKappaDF();

    public float getObservedAgreement();
}
