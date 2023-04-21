package edu.univalle.lingweb.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.Common;
import edu.univalle.lingweb.persistence.CoMaterial;
import edu.univalle.lingweb.persistence.CoMaterialDAO;
import edu.univalle.lingweb.persistence.CoMenu;
import edu.univalle.lingweb.persistence.CoMenuDAO;
import edu.univalle.lingweb.persistence.CoMenuTypeDAO;
import edu.univalle.lingweb.persistence.EntityManagerHelper;
import edu.univalle.lingweb.rest.RestServiceResult;

/**
 * Clase que contiene los m�todos CRUD (Create Read Update Delete) entre otros
 * para la tabla 'co_material'( Material )
 * 
 * @author Jose Aricapa
 */
public class DataManagerMaterial extends DataManager {

    /**
	 * Manejo de mensajes Log's
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger log = Logger.getLogger(DataManagerMaterial.class);

    /**
	 * Contructor de la clase
	 */
    public DataManagerMaterial() {
        super();
        DOMConfigurator.configure(DataManagerMaterial.class.getResource("/log4j.xml"));
    }

    /**
	 * Crea una nuevo material
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param coSequence
	 *            Secuencia a guardar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult create(RestServiceResult serviceResult, CoMaterial coMaterial) {
        CoMaterialDAO coMaterialDAO = new CoMaterialDAO();
        try {
            coMaterial.setMaterialId(getSequence("sq_co_material"));
            if (coMaterial.getCoMaterial() == null) {
                coMaterial.setCoMaterial(coMaterial);
            }
            EntityManagerHelper.beginTransaction();
            coMaterialDAO.save(coMaterial);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coMaterial);
            log.info("Material" + coMaterial.getMaterialId() + " creado con �xito...");
            Object[] arrayParam = { coMaterial.getMaterialId() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.create.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar el material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.create.error"), e.getMessage()));
        }
        return serviceResult;
    }

    /**
	 * Actualiza una una nueva secuencia en la base de datos
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param coSequence
	 *            Secuencia a actualizar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult update(RestServiceResult serviceResult, CoMaterial coMaterial) {
        CoMaterialDAO coMaterialDAO = new CoMaterialDAO();
        try {
            log.info("Actualizando el material: " + coMaterial.getTitle());
            EntityManagerHelper.beginTransaction();
            coMaterialDAO.update(coMaterial);
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(coMaterial);
            Object[] args = { coMaterial.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.update.success"), args));
            log.info("Se actualizo la material con �xito: " + coMaterial.getTitle());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar la material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.update.error"), e.getMessage()));
        }
        return serviceResult;
    }

    /**
	 * Elimina una secuencia de la base de datos
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param coMaterial
	 *            Secuencia a eliminar
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult delete(RestServiceResult serviceResult, CoMaterial coMaterial) {
        try {
            log.info("Eliminando la menu: " + coMaterial.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_MATERIAL);
            query.setParameter(1, coMaterial.getMaterialId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coMaterial.getTitle() };
            log.info("Secuencia eliminada con �xito: " + coMaterial.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el material: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coMaterial.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }

    /**
	 * Realiza la busqueda de un material
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @param nMaterialId
	 *            C�digo del material
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult search(RestServiceResult serviceResult, Long nMaterialId) {
        CoMaterial coMaterial = new CoMaterialDAO().findById(nMaterialId);
        if (coMaterial == null) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("material.search.notFound"));
        } else {
            List<CoMaterial> list = new ArrayList<CoMaterial>();
            EntityManagerHelper.refresh(coMaterial);
            list.add(coMaterial);
            Object[] arrayParam = { list.size() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.search.success"), arrayParam));
            serviceResult.setObjResult(list);
        }
        return serviceResult;
    }

    /**
	 * Realiza el proceso de relacionar una opci�n de men� a un material
	 * 
	 * @param result
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @param coMaterial
	 *            men� a guardar
	 * 
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.s
	 */
    public RestServiceResult createMenuMaterial(RestServiceResult serviceResult, CoMaterial coMaterial, CoMenu coMenu) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.INSERT_CO_MENU_MATERIAL);
            query.setParameter(1, coMenu.getMenuId());
            query.setParameter(2, coMaterial.getMaterialId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coMaterial.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("menu.createMenuUser.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Menu - Material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("menu.createMenuUser.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }

    /**
	 * Obtiene la lista de materiales
	 * 
	 * @param result
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult list(RestServiceResult result) {
        return list(result, 0, 0);
    }

    /**
	 * Obtiene la lista de sequencias
	 * 
	 * @param serviceResult
	 *            El {@link RestServiceResult} que contendr�n los mensajes
	 *            localizados y estado SQL .
	 * @return El {@link RestServiceResult} que contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult list(RestServiceResult serviceResult, int nRowStart, int nMaxResults) {
        CoMaterialDAO coMaterialDAO = new CoMaterialDAO();
        List<CoMaterial> list = coMaterialDAO.findAll(nRowStart, nMaxResults);
        if (list.size() == 0) {
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("material.list.notFound"));
        } else {
            Object[] array = { list.size() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.list.success"), array));
            serviceResult.setObjResult(list);
            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(coMaterialDAO.findAll().size()); else serviceResult.setNumResult(list.size());
        }
        return serviceResult;
    }

    /**
	 * Obtiene la lista tipos de secuencia
	 * 
	 * @param result
	 *            El {@link RestServiceResult} que contendr�n el resultado de la
	 *            operaci�n.
	 * @return El {@link RestServiceResult} contiene el resultado de la
	 *         operaci�n.
	 */
    public RestServiceResult listMaterialForMenu(RestServiceResult serviceResult, Long nMenuId) {
        ArrayList<CoMaterial> list = new ArrayList<CoMaterial>();
        CoMenu coMenu = new CoMenuDAO().findById(nMenuId);
        EntityManagerHelper.refresh(coMenu);
        if (coMenu != null) {
            Set<CoMaterial> setMaterials = coMenu.getCoMaterials();
            for (Iterator<CoMaterial> iterator = setMaterials.iterator(); iterator.hasNext(); ) {
                CoMaterial coMaterial = (CoMaterial) iterator.next();
                list.add(coMaterial);
            }
        }
        Object[] array = { list.size() };
        serviceResult.setMessage(MessageFormat.format(bundle.getString("material.list.success"), array));
        serviceResult.setObjResult(list);
        serviceResult.setNumResult(list.size());
        return serviceResult;
    }

    /**
	 * Calcula el porcentaje de materiales por usuario
	 * @param serviceResult El {@link RestServiceResult} que contendr�n el resultado de la operaci�n.
	 * @param nUserId C�digo del usuario
	 * @return El {@link RestServiceResult} contiene el resultado de la operaci�n.
	 */
    public RestServiceResult percentMaterialForUser(RestServiceResult serviceResult, Long nUserId) {
        int nPercentMaterial = 0;
        try {
            log.info("Obteniendo el total de materiales - Usuario: " + nUserId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.SELECT_PERCENT_MATERIAL);
            query.setParameter(1, nUserId);
            Vector vecResult = (Vector) query.getSingleResult();
            log.info("objResult => " + vecResult);
            if (vecResult.get(0) == null) {
                nPercentMaterial = 0;
            } else {
                nPercentMaterial = Integer.parseInt(vecResult.get(0).toString());
            }
            EntityManagerHelper.commit();
            Object[] arrayParam = { nPercentMaterial };
            log.info("Total Porcentaje:" + nPercentMaterial + " %");
            serviceResult.setObjResult(nPercentMaterial);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.delete.success"), arrayParam));
        } catch (Exception e) {
            e.printStackTrace();
            EntityManagerHelper.rollback();
            log.error("Error al calcular el poncentaje de material: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { nPercentMaterial };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("material.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }

    /**
	 * Crea una copia de una material en el repositorio general
	 * @param serviceResult El {@link RestServiceResult} que contendr�n el resultado de la operaci�n. 
	 */
    public CoMenu copyGeneralRepository(RestServiceResult serviceResult, CoMenu coMenuMaterial, CoMaterial coMaterial) {
        long nMenuId = 0;
        int nCount = 0;
        LinkedList<CoMenu> list = getListParent(coMenuMaterial, new LinkedList<CoMenu>());
        System.out.println("N�mero de opciones MENU encontradas: " + list.size());
        DataManagerMenu dataManagerMenu = new DataManagerMenu();
        dataManagerMenu.setBundle(bundle);
        LinkedList<CoMenu> listClone = new LinkedList<CoMenu>();
        CoMenu coMenuClone = null;
        CoMenu coMenuParentRepo = null;
        for (int i = 0; i < list.size(); i++) {
            CoMenu coMenuCourse = list.get(i);
            System.out.println("MENU[" + i + "] = " + coMenuCourse.getMenuId());
            coMenuParentRepo = getMenuNameRepo(coMenuCourse);
            if (coMenuParentRepo == null) {
                System.out.println(" No se encuentra en repositorio. Se crea uno nuevo");
                coMenuClone = cloneMenu(coMenuCourse);
            } else {
                System.out.println(" Se encuentra en repositorio. Se almacena para el siguiente: " + coMenuParentRepo.getMenuId());
                listClone.add(coMenuParentRepo);
                nCount++;
                if ((i + 1) == list.size()) {
                    System.out.println(" Ultimo asociando menuId : " + coMenuParentRepo.getMenuId() + " con padre: " + coMenuParentRepo.getCoMenu().getMenuId());
                    createMenuMaterial(serviceResult, coMaterial, coMenuParentRepo);
                    return coMenuParentRepo;
                } else {
                    System.out.println("Corta ciclo \n\n");
                    continue;
                }
            }
            nMenuId = dataManagerMenu.getSequence("sq_co_menu");
            if (coMenuCourse.getMenuId().compareTo(coMenuCourse.getCoMenu().getMenuId()) == 0) {
                coMenuClone.setMenuId(nMenuId);
                coMenuClone.setCoMenu(coMenuClone);
                listClone.add(coMenuClone);
            } else {
                System.out.println("Creando Hijo: " + coMenuClone.getMenuId());
                coMenuClone.setCoMenu(listClone.get(nCount - 1));
                coMenuClone.setMenuId(nMenuId);
                listClone.add(coMenuClone);
            }
            nCount++;
            System.out.println("Nuevo Creando menuId : " + coMenuClone.getMenuId() + " con padre: " + coMenuClone.getCoMenu().getMenuId());
            dataManagerMenu.create(serviceResult, coMenuClone);
        }
        CoMenu coMenu3 = listClone.get(listClone.size() - 1);
        createMenuMaterial(serviceResult, coMaterial, coMenu3);
        return coMenu3;
    }

    /**
	 * Obtiene la opci�n de men� a partir del nombre
	 * @param sMenuName Nombre del men�
	 * @return CoMenu con la opci�n de men�. <<null>> si no encontr� opci�n de men� 
	 */
    public CoMenu getMenuNameRepo(CoMenu coMenu) {
        List<CoMenu> list = new CoMenuDAO().findByMenuName(coMenu.getMenuName());
        System.out.println("Menu en REPO encontrados => " + list.size());
        for (CoMenu coMenuResult : list) {
            if (coMenuResult.getCoMenuType().getMenuTypeId().compareTo(Common.REPOSITORY_GENERAL) == 0) {
                System.out.println("Repositorio..." + coMenuResult.getMenuName() + " -- " + coMenuResult.getPath());
                System.out.println("Curso..." + coMenu.getMenuName() + " -- " + coMenu.getPath());
                if (coMenuResult.getMenuName().equals(coMenu.getMenuName()) && coMenuResult.getPath().equals(coMenu.getPath())) {
                    System.out.println("SON iguales...");
                    return coMenuResult;
                } else {
                    System.out.println("No son iguales...");
                }
            }
        }
        return null;
    }

    /**
	 * Clona un men�.
	 * @param coMenu menu a clonar
	 * @return CoMenu clonado
	 */
    public CoMenu cloneMenu(CoMenu coMenu) {
        CoMenu coMenuClone = new CoMenu();
        coMenuClone.setMenuName(coMenu.getMenuName());
        coMenuClone.setCoMenuType(new CoMenuTypeDAO().findById(new Long(Common.REPOSITORY_GENERAL)));
        coMenuClone.setPath(coMenu.getPath());
        return coMenuClone;
    }

    /**
	 * Obtiene la lista de men�s padre
	 * 
	 * @param coMenu
	 *            menu a obtener todos sus menu-padre
	 * @param list2
	 *            arreglo a cargar
	 * @return List<Long> Lista de c�digos de los menus padres
	 */
    public LinkedList<CoMenu> getListParent(CoMenu coMenu, LinkedList<CoMenu> list2) {
        CoMenu coMenuParent = coMenu.getCoMenu();
        if (coMenuParent.getMenuId().equals(coMenuParent.getCoMenu().getMenuId())) {
            list2.addFirst(coMenu);
            if (!list2.contains(coMenuParent)) list2.addFirst(coMenuParent);
            return list2;
        } else {
            list2.addFirst(coMenu);
            list2.addFirst(coMenuParent);
            return getListParent(coMenuParent.getCoMenu(), list2);
        }
    }

    public static void main(String[] args) {
        DataManagerMaterial dataManagerMaterial = new DataManagerMaterial();
        dataManagerMaterial.setBundle(ResourceBundle.getBundle("edu.univalle.lingweb.LzTrackMessages", new Locale("es")));
        CoMenu coMenu = new CoMenuDAO().findById(new Long("25"));
        dataManagerMaterial.copyGeneralRepository(new RestServiceResult(), coMenu, new CoMaterialDAO().findById(new Long("1")));
    }
}
