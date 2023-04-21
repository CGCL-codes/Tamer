package org.helios.util;

import java.math.*;
import java.util.*;
import org.helios.crivello.*;

public class Fattorizzatore {

    CrivelloNew c;

    public boolean isDivisibileSenzaResto(BigInteger numero, BigInteger divisore) {
        if (numero.remainder(divisore).compareTo(BigInteger.ZERO) == 0) return true; else return false;
    }

    public ArrayList<BigInteger> intToBigInteger(ArrayList<Integer> listaInt) {
        ArrayList<BigInteger> listaBigInt = new ArrayList<BigInteger>();
        for (int i = 0; i < listaInt.size(); i++) {
            BigInteger elemento = new BigInteger("" + listaInt.get(i));
            listaBigInt.add(elemento);
        }
        return listaBigInt;
    }
}
