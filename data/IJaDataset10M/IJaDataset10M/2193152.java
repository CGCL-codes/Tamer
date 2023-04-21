package propres;

import java.util.*;
import java.io.Serializable;

public class estrucplato implements Serializable {

    private List<plato>[] tabla;

    public estrucplato() {
        tabla = new ArrayList[50];
        for (int i = 0; i < 50; i++) {
            tabla[i] = new ArrayList<plato>();
        }
    }

    public void añadir_plato(plato a) {
        int h = hash(a.Get_Nombre());
        tabla[h].add(a);
    }

    public void modificar_plato(plato a) {
        int h = hash(a.Get_Nombre());
        boolean r = false;
        Iterator it = tabla[h].iterator();
        int i = 0;
        while (i < tabla[h].size() && !r) {
            plato z = tabla[h].get(i);
            if (z.equals(a.Get_Nombre())) {
                r = true;
                it.remove();
            }
            i++;
        }
        if (r) {
            tabla[h].add(a);
        }
    }

    public plato consultar_plato(String Nombre) {
        int h = hash(Nombre);
        boolean z = false;
        plato b = new plato();
        Iterator it = tabla[h].iterator();
        while (it.hasNext() && !z) {
            b = (plato) it.next();
            z = b.Get_Nombre().equals(Nombre);
        }
        if (!z) {
            b = null;
        }
        return b;
    }

    public void eliminar_plato(String Nombre) {
        int h = hash(Nombre);
        Iterator it = tabla[h].iterator();
        while (it.hasNext()) {
            if (((plato) it.next()).Get_Nombre().equals(Nombre)) {
                it.remove();
            }
        }
    }

    public List<plato> listaplatos() {
        List<plato> a = new ArrayList();
        int alt = 0;
        int cont;
        while (alt < tabla.length) {
            Iterator i = tabla[alt].iterator();
            cont = 0;
            if (!tabla[alt].isEmpty()) {
                while (i.hasNext()) {
                    a.add(tabla[alt].get(cont));
                    i.next();
                    cont++;
                }
            }
            alt++;
        }
        return a;
    }

    private int hash(String a) {
        char b, c, d;
        if (a.length() > 2) {
            b = a.charAt(0);
            c = a.charAt(1);
            d = a.charAt(2);
        } else if (a.length() == 2) {
            b = a.charAt(0);
            c = a.charAt(1);
            d = a.charAt(1);
        } else {
            b = a.charAt(0);
            c = a.charAt(0);
            d = a.charAt(0);
        }
        return (((int) b + (int) c + (int) d) % 50);
    }
}
