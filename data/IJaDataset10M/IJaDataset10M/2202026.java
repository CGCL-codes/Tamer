package com.celiosilva.simbanc.controller.util;

import com.celiosilva.simbanc.beans.Cliente;
import java.util.Comparator;

/**
 *
 * @author celio@celiosilva.com
 */
public class OrdenarClienteCodigo implements Comparator<Cliente> {

    @Override
    public int compare(Cliente o1, Cliente o2) {
        return o1.getCodigo().compareTo(o2.getCodigo());
    }
}
