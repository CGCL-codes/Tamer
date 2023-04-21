package ormsamples;

import org.orm.*;

public class ListAnotacionesData {

    private static final int ROW_COUNT = 100;

    public void listTestData() throws PersistentException {
        orm.DAOFactory lDAOFactory = orm.DAOFactory.getDAOFactory();
        System.out.println("Listing Tda_alumno...");
        orm.Tda_alumno[] ormTda_alumnos = lDAOFactory.getTda_alumnoDAO().listTda_alumnoByQuery(null, null);
        int length = Math.min(ormTda_alumnos.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_alumnos[i]);
        }
        System.out.println(length + " record(s) retrieved.");
        System.out.println("Listing Tda_anotacion...");
        orm.Tda_anotacion[] ormTda_anotacions = lDAOFactory.getTda_anotacionDAO().listTda_anotacionByQuery(null, null);
        length = Math.min(ormTda_anotacions.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_anotacions[i]);
        }
        System.out.println(length + " record(s) retrieved.");
        System.out.println("Listing Tda_tipoanotacion...");
        orm.Tda_tipoanotacion[] ormTda_tipoanotacions = lDAOFactory.getTda_tipoanotacionDAO().listTda_tipoanotacionByQuery(null, null);
        length = Math.min(ormTda_tipoanotacions.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_tipoanotacions[i]);
        }
        System.out.println(length + " record(s) retrieved.");
        System.out.println("Listing Tda_curso...");
        orm.Tda_curso[] ormTda_cursos = lDAOFactory.getTda_cursoDAO().listTda_cursoByQuery(null, null);
        length = Math.min(ormTda_cursos.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_cursos[i]);
        }
        System.out.println(length + " record(s) retrieved.");
        System.out.println("Listing Tda_subsector...");
        orm.Tda_subsector[] ormTda_subsectors = lDAOFactory.getTda_subsectorDAO().listTda_subsectorByQuery(null, null);
        length = Math.min(ormTda_subsectors.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_subsectors[i]);
        }
        System.out.println(length + " record(s) retrieved.");
        System.out.println("Listing Tda_anotador...");
        orm.Tda_anotador[] ormTda_anotadors = lDAOFactory.getTda_anotadorDAO().listTda_anotadorByQuery(null, null);
        length = Math.min(ormTda_anotadors.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_anotadors[i]);
        }
        System.out.println(length + " record(s) retrieved.");
    }

    public void listByCriteria() throws PersistentException {
        System.out.println("Listing Tda_alumno by Criteria...");
        orm.Tda_alumnoCriteria tda_alumnoCriteria = new orm.Tda_alumnoCriteria();
        tda_alumnoCriteria.setMaxResults(ROW_COUNT);
        orm.Tda_alumno[] ormTda_alumnos = tda_alumnoCriteria.listTda_alumno();
        int length = ormTda_alumnos == null ? 0 : Math.min(ormTda_alumnos.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_alumnos[i]);
        }
        System.out.println(length + " Tda_alumno record(s) retrieved.");
        System.out.println("Listing Tda_anotacion by Criteria...");
        orm.Tda_anotacionCriteria tda_anotacionCriteria = new orm.Tda_anotacionCriteria();
        tda_anotacionCriteria.setMaxResults(ROW_COUNT);
        orm.Tda_anotacion[] ormTda_anotacions = tda_anotacionCriteria.listTda_anotacion();
        length = ormTda_anotacions == null ? 0 : Math.min(ormTda_anotacions.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_anotacions[i]);
        }
        System.out.println(length + " Tda_anotacion record(s) retrieved.");
        System.out.println("Listing Tda_tipoanotacion by Criteria...");
        orm.Tda_tipoanotacionCriteria tda_tipoanotacionCriteria = new orm.Tda_tipoanotacionCriteria();
        tda_tipoanotacionCriteria.setMaxResults(ROW_COUNT);
        orm.Tda_tipoanotacion[] ormTda_tipoanotacions = tda_tipoanotacionCriteria.listTda_tipoanotacion();
        length = ormTda_tipoanotacions == null ? 0 : Math.min(ormTda_tipoanotacions.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_tipoanotacions[i]);
        }
        System.out.println(length + " Tda_tipoanotacion record(s) retrieved.");
        System.out.println("Listing Tda_curso by Criteria...");
        orm.Tda_cursoCriteria tda_cursoCriteria = new orm.Tda_cursoCriteria();
        tda_cursoCriteria.setMaxResults(ROW_COUNT);
        orm.Tda_curso[] ormTda_cursos = tda_cursoCriteria.listTda_curso();
        length = ormTda_cursos == null ? 0 : Math.min(ormTda_cursos.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_cursos[i]);
        }
        System.out.println(length + " Tda_curso record(s) retrieved.");
        System.out.println("Listing Tda_subsector by Criteria...");
        orm.Tda_subsectorCriteria tda_subsectorCriteria = new orm.Tda_subsectorCriteria();
        tda_subsectorCriteria.setMaxResults(ROW_COUNT);
        orm.Tda_subsector[] ormTda_subsectors = tda_subsectorCriteria.listTda_subsector();
        length = ormTda_subsectors == null ? 0 : Math.min(ormTda_subsectors.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_subsectors[i]);
        }
        System.out.println(length + " Tda_subsector record(s) retrieved.");
        System.out.println("Listing Tda_anotador by Criteria...");
        orm.Tda_anotadorCriteria tda_anotadorCriteria = new orm.Tda_anotadorCriteria();
        tda_anotadorCriteria.setMaxResults(ROW_COUNT);
        orm.Tda_anotador[] ormTda_anotadors = tda_anotadorCriteria.listTda_anotador();
        length = ormTda_anotadors == null ? 0 : Math.min(ormTda_anotadors.length, ROW_COUNT);
        for (int i = 0; i < length; i++) {
            System.out.println(ormTda_anotadors[i]);
        }
        System.out.println(length + " Tda_anotador record(s) retrieved.");
    }

    public static void main(String[] args) {
        try {
            ListAnotacionesData listAnotacionesData = new ListAnotacionesData();
            try {
                listAnotacionesData.listTestData();
            } finally {
                orm.AnotacionesPersistentManager.instance().disposePersistentManager();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
