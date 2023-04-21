package entidades;

import java.util.List;

/**
 *
 * @author 0213101
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Hibernate h = new Hibernate();
        Pessoa p;
        Recinto r = new Recinto();
        Recurso rec = new Recurso();
        rec.setRecinto(r);
        h.beginTransaction();
        p = new Pessoa("Vinicius", "sarado_2008@zipmail.com", "5197287877", "mestre", "master", "chief", true, false);
        h.saveOnly(p);
        h.endTransaction();
    }
}
