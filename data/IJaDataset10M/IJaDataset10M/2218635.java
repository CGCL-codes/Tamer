package networkModule.L3.nat;

import dataStructures.ipAddresses.IPwithNetmask;

/**
 * Trida reprezentujici jeden seznam-list.
 */
public class AccessList {

    public final IPwithNetmask ip;

    /**
	 * V podstate unikatni jmeno AccessListu.
	 */
    public final int number;

    public AccessList(IPwithNetmask ip, int cislo) {
        this.ip = ip;
        this.number = cislo;
    }
}
