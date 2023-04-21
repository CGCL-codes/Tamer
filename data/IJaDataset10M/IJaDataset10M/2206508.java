package ormsamples;

import org.orm.*;

public class DeleteVOSOAPproyectodesarrolloData {

    public void deleteTestData() throws PersistentException {
        PersistentTransaction t = orm.VOSOAPproyectodesarrolloPersistentManager.instance().getSession().beginTransaction();
        try {
            orm.DAOFactory lDAOFactory = orm.DAOFactory.getDAOFactory();
            orm.dao.PersonaDAO lormPersonaDAO = lDAOFactory.getPersonaDAO();
            orm.Persona lormPersona = lormPersonaDAO.loadPersonaByQuery(null, null);
            lormPersonaDAO.delete(lormPersona);
            orm.dao.CiudadDAO lormCiudadDAO = lDAOFactory.getCiudadDAO();
            orm.Ciudad lormCiudad = lormCiudadDAO.loadCiudadByQuery(null, null);
            lormCiudadDAO.delete(lormCiudad);
            t.commit();
        } catch (Exception e) {
            t.rollback();
        }
    }

    public static void main(String[] args) {
        try {
            DeleteVOSOAPproyectodesarrolloData deleteVOSOAPproyectodesarrolloData = new DeleteVOSOAPproyectodesarrolloData();
            try {
                deleteVOSOAPproyectodesarrolloData.deleteTestData();
            } finally {
                orm.VOSOAPproyectodesarrolloPersistentManager.instance().disposePersistentManager();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
