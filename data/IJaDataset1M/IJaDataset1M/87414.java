package com.acervera.afw.utils;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import com.acervera.afw.exceptions.CodigoDeControlException;
import com.acervera.afw.exceptions.TipoDeSociedadException;

/**
 * @author Angel Cervera Claudio (angel@acervera.com)
 *
 */
public class CIF {

    private static final String TIPO_SOCIEDAD = "ABCDEFGHKLMNPQS";

    private static final String CARACTER_CONTROL = "JABCDEFGHI";

    /**
	 * Añade los ceros que le pudieran faltar al número del CIF.
	 * @param cif
	 * @return
	 */
    public static String normalizar(String cif) {
        String[] cifEnPartes = split(cif);
        return cifEnPartes[0].toUpperCase() + StringUtils.leftPad(cifEnPartes[1], 7, "0") + cifEnPartes[2].toUpperCase();
    }

    /**
	 * Nos devuelve el cif en tres partes, el tipo de sociedad, el número y el dígito de control.
	 * @param cif
	 * @return
	 */
    public static String[] split(String cif) {
        String[] retValue = new String[3];
        retValue[0] = cif.substring(0, 1);
        retValue[1] = cif.substring(1, cif.length() - 1);
        retValue[2] = cif.substring(cif.length() - 1);
        return retValue;
    }

    /**
	 * Valida que un cif es válido.
	 * @param cif
	 * @return
	 */
    public static boolean check(String cif) throws CodigoDeControlException, TipoDeSociedadException {
        cif = normalizar(cif);
        String[] cifEnPartes = split(cif);
        if (!StringUtils.contains(TIPO_SOCIEDAD, cifEnPartes[0])) {
            throw new TipoDeSociedadException();
        }
        if (!StringUtils.contains(codigoDeControl(cifEnPartes[1]), cifEnPartes[2])) {
            throw new CodigoDeControlException();
        }
        return true;
    }

    /**
	 * Nos devuelve una String con los dos caracteres de control posibles.
	 * El número debe estar normalizado.
	 * @param strNumero
	 * @return
	 */
    public static String codigoDeControl(String strNumero) {
        char[] charNumeros = strNumero.toCharArray();
        int total = 0;
        for (int idxPares = 1; idxPares < charNumeros.length; idxPares = idxPares + 2) {
            int valor = CharUtils.toIntValue(charNumeros[idxPares]);
            total = total + valor;
        }
        for (int idxImpares = 0; idxImpares < charNumeros.length; idxImpares = idxImpares + 2) {
            int valor = CharUtils.toIntValue(charNumeros[idxImpares]) * 2;
            if (valor > 9) {
                valor = 1 + (valor % 10);
            }
            total += valor;
        }
        int numControl = 10 - (total % 10);
        return String.valueOf(numControl) + CARACTER_CONTROL.charAt(numControl);
    }
}
