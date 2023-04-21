package ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.orm.PersistentException;
import org.orm.PersistentTransaction;
import com.google.gson.Gson;
import SOAPVO.PublicacionSOAPVO;
import cl.oferta.DAOFactory;
import cl.oferta.FlexPersistentManager;
import cl.oferta.Tcp_persona;
import cl.oferta.Tcp_personaCriteria;
import cl.oferta.Tcp_publicacion;
import cl.oferta.dao.Tcp_publicacionDAO;

public class PublicacionSOA {

    private static final int ROW_COUNT = 100;

    /** 
	 * Metodo que sirve para agregar una nueva publicación. 
	 * @param pub_mon 
	 * @param pub_des 
	 * @param pers_rut 
	 * @return 
	 */
    public String add(String pub_mon, String pub_des, String pers_rut) {
        PersistentTransaction t;
        String resultado = "";
        if ((pub_mon == null) || (pub_mon.equals("")) || (pub_des == null) || (pub_des.equals("")) || (pers_rut == null) || (pers_rut.equals(""))) {
            resultado = "ERROR: Ingrese todos los datos (monto de publicación, descripción y rut de la persona)";
        } else {
            try {
                t = FlexPersistentManager.instance().getSession().beginTransaction();
                DAOFactory lDAOFactory = DAOFactory.getDAOFactory();
                Tcp_publicacionDAO lormTcp_publicacionDAO = lDAOFactory.getTcp_publicacionDAO();
                Tcp_publicacion lormTcp_publicacion = lormTcp_publicacionDAO.createTcp_publicacion();
                Tcp_persona[] ormPersonas;
                ormPersonas = lDAOFactory.getTcp_personaDAO().listTcp_personaByQuery("pers_rut='" + pers_rut + "'", null);
                int length2 = ormPersonas.length;
                if (length2 == 0) {
                    resultado = "ERROR: No se ha encontrado registros del rut ingresado";
                } else {
                    Tcp_personaCriteria tcp_personaCriteria = new Tcp_personaCriteria();
                    tcp_personaCriteria.pers_rut.eq(pers_rut);
                    Tcp_persona lormpersonPersona = new Tcp_persona();
                    lormpersonPersona = tcp_personaCriteria.uniqueTcp_persona();
                    int mon = Integer.parseInt(pub_mon);
                    lormTcp_publicacion.setPub_monto(mon);
                    lormTcp_publicacion.setPub_descripcion(pub_des);
                    lormTcp_publicacion.setTcp_personaPers(lormpersonPersona);
                    lormTcp_publicacionDAO.save(lormTcp_publicacion);
                    resultado = "Publicaci�n ingresada correctamente.";
                }
                t.commit();
            } catch (PersistentException e1) {
                resultado = "<FAIL>";
                e1.printStackTrace();
            }
        }
        return resultado;
    }

    /** 
	 * Este método sirve para borrar una publicación según su 
id 
	 * @param id de publicación 
	 * @return 
	 * @throws PersistentException 
	 */
    public String delete(String id) {
        String message = "";
        PersistentTransaction t;
        int resultado = 1;
        if ((id == null) || (id.equals(""))) {
            message = "debe ingresar la publicaci�n a borrar";
            resultado = 0;
        } else {
            try {
                t = FlexPersistentManager.instance().getSession().beginTransaction();
                DAOFactory lDAOFactory = DAOFactory.getDAOFactory();
                Tcp_publicacionDAO lormTcp_publicacionDAO = lDAOFactory.getTcp_publicacionDAO();
                Tcp_publicacion[] ormPublicacion;
                ormPublicacion = lDAOFactory.getTcp_publicacionDAO().listTcp_publicacionByQuery("pub_id='" + id + "'", null);
                int length2 = ormPublicacion.length;
                if (length2 == 0) {
                    message = "ERROR: No se ha encontrado registros";
                } else {
                    Tcp_publicacion lormTcp_publicacion = lormTcp_publicacionDAO.loadTcp_publicacionByQuery("pub_id=" + id, null);
                    lormTcp_publicacionDAO.delete(lormTcp_publicacion);
                    message = " ha sido eliminado(a) la publicacion";
                }
                t.commit();
            } catch (PersistentException e1) {
                message = " error";
                e1.printStackTrace();
            }
        }
        return message;
    }

    public String update(String id, int campo, String new_valor) {
        PersistentTransaction t;
        String message = "";
        int length2 = 0;
        if ((id == null) || (id.equals("")) || (new_valor.equals("")) || (new_valor == null)) {
            message = "ERROR: Existen campos vac�os";
        } else {
            try {
                t = FlexPersistentManager.instance().getSession().beginTransaction();
                DAOFactory lDAOFactory = DAOFactory.getDAOFactory();
                Tcp_publicacionDAO lormTcp_publicacionDAO = lDAOFactory.getTcp_publicacionDAO();
                Tcp_publicacion[] ormTcp_publicacion = lDAOFactory.getTcp_publicacionDAO().listTcp_publicacionByQuery("pub_id='" + id + "'", null);
                int length = Math.min(ormTcp_publicacion.length, ROW_COUNT);
                if (length >= 1) {
                    Tcp_publicacion lormTcp_publicacion = lormTcp_publicacionDAO.loadTcp_publicacionByQuery("pub_id='" + id + "'", null);
                    if (campo == 0) {
                        int valor = Integer.parseInt(new_valor);
                        lormTcp_publicacion.setPub_monto(valor);
                        lormTcp_publicacionDAO.save(lormTcp_publicacion);
                        message = "El monto de publicaci�n ha sido actualizado";
                    }
                    if (campo == 1) {
                        lormTcp_publicacion.setPub_descripcion(new_valor);
                        lormTcp_publicacionDAO.save(lormTcp_publicacion);
                        message = "La descripci�n de la publicaci�n ha sido actualizada";
                    }
                } else {
                    message = "No se encontraron registros";
                }
                t.commit();
            } catch (PersistentException e1) {
                message = "error";
                e1.printStackTrace();
            }
        }
        return message;
    }

    /** 
	 * Este método sirve para buscar una publicación segpun su id 
	 * @param id 
	 * @return 
	 * @throws PersistentException 
	 */
    public String find(String buscar) {
        String message;
        if ((buscar == null) || (buscar.equals(""))) {
            message = "debe ingresar id de la publicación a buscar";
        }
        DAOFactory lDAOFactory = DAOFactory.getDAOFactory();
        Tcp_publicacionDAO lormTcp_publicacionDAO = lDAOFactory.getTcp_publicacionDAO();
        Tcp_publicacion lormTcp_publicacion;
        try {
            lormTcp_publicacion = lormTcp_publicacionDAO.loadTcp_publicacionByQuery("pub_id='" + buscar + "'", null);
            if (lormTcp_publicacion != null) {
                String descrip = lormTcp_publicacion.getPub_descripcion();
                int monto = lormTcp_publicacion.getPub_monto();
                message = "Publicacion encontrada, monto \n" + monto + "\n y su descripcción es: \n" + descrip;
            } else {
                message = "No se encontron publicaciones";
            }
        } catch (PersistentException e) {
            message = "Error";
            e.printStackTrace();
        }
        return message;
    }

    /** 
	 * Método que realiza una busqueda de todas las 
publicaciones existentes. 
	 * @return json almacena todos los registros encontrados 
	 * @throws PersistentException 
	 */
    public static String findall(String buscar) throws PersistentException {
        String json = null;
        int length2 = 0;
        int length3 = 0;
        DAOFactory lDAOFactory = DAOFactory.getDAOFactory();
        Arrays.asList(lDAOFactory.getTcp_personaDAO().listTcp_personaByQuery(null, null));
        Collection<PublicacionSOAPVO> colecionPublicacionSOAPVO = new ArrayList<PublicacionSOAPVO>();
        Tcp_publicacion[] ormPublicacion;
        try {
            if ((buscar == null) || (buscar.equals(""))) {
                ormPublicacion = lDAOFactory.getTcp_publicacionDAO().listTcp_publicacionByQuery(null, null);
            } else {
                ormPublicacion = lDAOFactory.getTcp_publicacionDAO().listTcp_publicacionByQuery("pub_monto='" + buscar + "'", null);
                length2 = ormPublicacion.length;
                if (length2 == 0) {
                    ormPublicacion = lDAOFactory.getTcp_publicacionDAO().listTcp_publicacionByQuery("pub_descripcion='" + buscar + "'", null);
                    length3 = ormPublicacion.length;
                }
                if (length2 == 0 && length3 == 0) {
                    ormPublicacion = lDAOFactory.getTcp_publicacionDAO().listTcp_publicacionByQuery("pub_id='" + buscar + "'", null);
                }
            }
            int length = ormPublicacion.length;
            if (length == 0) {
                json = "No se ha encontrado ningun registro";
            } else {
                for (int i = 0; i < length; i++) {
                    System.out.println(ormPublicacion[i].getPub_monto());
                    PublicacionSOAPVO objeto = PublicacionSOAPVO.crearPublicacionSOAPVO(ormPublicacion[i]);
                    colecionPublicacionSOAPVO.add(objeto);
                }
                Gson gson = new Gson();
                json = gson.toJson(colecionPublicacionSOAPVO);
            }
        } catch (PersistentException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static void main(String args[]) {
        PublicacionSOA pbsoa = new PublicacionSOA();
        System.out.println(pbsoa.find(""));
    }
}
