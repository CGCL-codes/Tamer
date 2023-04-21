package propres;

import java.util.*;

public class cargane4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        estrucalim alime = new estrucalim();
        g_disco disc = new g_discoa();
        Date o = new Date(2008, 11, 10);
        Date u = new Date(2009, 12, 12);
        Interval q = new Interval(o, u);
        dieta Y = new dieta();
        alimento b = new alimento("carne", 32, 10, 15, 36, "ABCD", q, 126735, 11101, "principal", 110);
        alimento c = new alimento("manzana", 230, 10, 15, 36, "ABCD", q, 540, 11001, "secundario", 001);
        alimento d = new alimento("pera", 212, 10, 15, 36, "ABCD", q, 2145, 11001, "secundario", 001);
        alimento e = new alimento("pescado", 564, 10, 15, 36, "ABCD", q, 24432, 1101, "principal", 011);
        alimento f = new alimento("berberechos", 20, 10, 15, 36, "D", q, 100, 1101, "secundario", 110);
        alimento g = new alimento("esparragos", 20, 10, 15, 36, "AD", q, 10000, 11101, "secundario", 001);
        alimento h = new alimento("cafe", 1243, 10, 15, 36, "A", q, 1234, 11001, "principal", 001);
        alimento m = new alimento("carajillo de magno", 1234, 10, 15, 36, "", q, 114, 1101, "principal", 011);
        alimento m1 = new alimento("azucar", 1234, 10, 15, 36, "D", q, 114, 1001, "secundario", 011);
        alimento m2 = new alimento("sacarina", 1234, 10, 15, 36, "AD", q, 114, 10101, "secundario", 011);
        alimento m3 = new alimento("leche", 1234, 10, 15, 36, "", q, 114, 1001, "principal", 011);
        alimento p = new alimento("pollo", 322, 15, 11, 12, "ABCD", q, 1235, 1000, "principal", 110);
        alimento y = new alimento("iogurt", 120, 12, 12, 43, "ABCD", q, 5240, 11101, "secundario", 001);
        alimento r = new alimento("naranja", 312, 22, 13, 26, "ABCD", q, 245, 10101, "secundario", 001);
        alimento s = new alimento("salmon", 142, 20, 31, 31, "ABCD", q, 24632, 10101, "principal", 011);
        alime.añadir_alimento(b);
        alime.añadir_alimento(c);
        alime.añadir_alimento(d);
        alime.añadir_alimento(e);
        alime.añadir_alimento(f);
        alime.añadir_alimento(g);
        alime.añadir_alimento(h);
        alime.añadir_alimento(m);
        alime.añadir_alimento(m1);
        alime.añadir_alimento(m2);
        alime.añadir_alimento(m3);
        alime.añadir_alimento(p);
        alime.añadir_alimento(y);
        alime.añadir_alimento(r);
        alime.añadir_alimento(s);
        disc.guardar_objecte(alime);
    }
}
