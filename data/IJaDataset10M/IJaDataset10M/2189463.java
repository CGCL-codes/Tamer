package propres;

import java.util.*;
import javax.swing.DefaultListModel;
import java.lang.Character;

/**
 *
 * @author Administrador
 */
public class CtrlVistaPreferencias {

    private CtrlDominioPreferencias driver;

    public CtrlVistaPreferencias(CtrlVistaCliente C) {
        try {
            driver = new CtrlDominioPreferencias(C.Get_Ctrl());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String[] gestion_evento(String[] D, DefaultListModel l1, DefaultListModel l2, DefaultListModel l3, DefaultListModel l4) {
        if (D[7].equals("Alta preferencias")) {
            driver.Alta_pref(D, l1, l2, l3, l4);
        } else if (D[7].equals("Modificar")) {
            driver.Modificar_pref(D, l1, l2, l3, l4);
        }
        return D;
    }

    public int[] gestion_consulta_ints(String DNI) {
        return driver.Consultar_pref_cal_prec(DNI);
    }

    public List<String> gestion_consulta_nec(String DNI) {
        return driver.Consultar_pref_nec(DNI);
    }

    public List<String> gestion_consulta_res(String DNI) {
        return driver.Consultar_pref_res(DNI);
    }

    public List<Integer> gestion_consulta_nec_prio(String DNI) {
        return driver.Consultar_pref_nec_prio(DNI);
    }

    public List<Integer> gestion_consulta_res_prio(String DNI) {
        return driver.Consultar_pref_res_prio(DNI);
    }

    public String validez_datos(String[] D, DefaultListModel modelo1, DefaultListModel modelo2, DefaultListModel modelo3, DefaultListModel modelo4) {
        String error = "";
        int i = 0;
        int j = 0;
        char c;
        if (true) error = "El cliente no existe"; else if (!D[0].equals("") && !isNumeric(D[0])) error = "DNI no valido"; else if (!D[1].equals("") && !isNumeric(D[0])) error = "Calorias totales no validas"; else if (!D[2].equals("") && !isNumeric(D[0])) error = "Calorias diaris no validas"; else if (!D[3].equals("") && !isNumeric(D[0])) error = "Calorias por comida no validas"; else if (!D[4].equals("") && !isNumeric(D[0])) error = "Precio total no valido"; else if (!D[5].equals("") && !isNumeric(D[0])) error = "Precio diario no valido"; else if (!D[6].equals("") && !isNumeric(D[0])) error = "Precio por comida no valido";
        return error;
    }

    public boolean isNumeric(String s) {
        boolean b = true;
        int i = 0;
        while (i < s.length() && b) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                b = false;
            }
            i++;
        }
        return b;
    }

    public boolean String_valido(String s) {
        boolean b = true;
        Character c = null;
        int valor = 0;
        int i = 0;
        if (s.equals("")) return false; else {
            while (i < s.length() && b) {
                valor = c.getNumericValue(s.charAt(i));
                if ((valor < 10 || valor > 35) && !c.isSpaceChar(s.charAt(i))) b = false;
                i++;
            }
            return b;
        }
    }
}
