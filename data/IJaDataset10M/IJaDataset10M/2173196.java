package modelo_objetos.cliente;

import java.util.ArrayList;
import java.util.List;
import mensaje_objetos.almacen.PrecioProvincia;
import mensaje_objetos.cliente.Cliente;
import mensaje_objetos.cliente.Direccion;
import mensaje_objetos.cliente.GrupoCliente;
import mensaje_objetos.cliente.Ciudad;
import mensaje_objetos.comunes.Secuencia;
import modelo_objetos.acceso_datos.Conexion;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utilidades.SubjectHelper;

/**
 *
 * @author carlos
 */
public class ModeloCiudad {

    private static ModeloCiudad modelo;

    private static SubjectHelper subjectAdd;

    private static SubjectHelper subjectRemove;

    private static SubjectHelper subjectUpdate;

    private static SubjectHelper subjectSaveOrUpdate;

    private static SubjectHelper subjectMensaje;

    private Conexion conn;

    /** Creates a new instance of ModeloCiudad */
    public ModeloCiudad() {
        conn = Conexion.getConexion();
        subjectAdd = new SubjectHelper();
        subjectRemove = new SubjectHelper();
        subjectUpdate = new SubjectHelper();
        subjectSaveOrUpdate = new SubjectHelper();
        subjectMensaje = new SubjectHelper();
    }

    public static SubjectHelper getSubjectAdd() {
        return subjectAdd;
    }

    public static SubjectHelper getSubjectRemove() {
        return subjectRemove;
    }

    public static SubjectHelper getSubjectUpdate() {
        return subjectUpdate;
    }

    public static SubjectHelper getSubjectSaveOrUpdate() {
        return subjectSaveOrUpdate;
    }

    /**
     *Intancia de la clase.
     */
    public static ModeloCiudad getInstance() {
        if (modelo == null) {
            modelo = new ModeloCiudad();
        }
        return modelo;
    }

    public static SubjectHelper getSubjectMensaje() {
        return subjectMensaje;
    }

    /**Metodo para insertar un Grupo*/
    public void addCiudad(Ciudad ciudad) {
        Session sl = conn.getSession();
        Transaction tx = sl.beginTransaction();
        if (!existCiudadSession(ciudad, sl)) {
            sl.save(ciudad);
            tx.commit();
            getSubjectAdd().notify(ModeloCiudad.class, ciudad, "addCiudad");
            sl.close();
        } else {
            getSubjectMensaje().notify(ModeloCiudad.class, "Existe un grupo con este codigo", "mensaje");
        }
    }

    /**Metodo para borrar un grupo*/
    public void removeCiudad(Ciudad ciudad) {
        Session sl = conn.getSession();
        Transaction tx = sl.beginTransaction();
        sl.delete(ciudad);
        tx.commit();
        sl.close();
        getSubjectRemove().notify(ModeloCiudad.class, ciudad, "removeCiudad");
    }

    /**Metodo para saber si un grupo esta registrado*/
    private boolean existCiudadSession(Ciudad ciudad, Session ss) {
        String hql = "From Ciudad P WHERE P.codigo=?";
        Query q = ss.createQuery(hql);
        q.setString(0, ciudad.getCodigo());
        List<Ciudad> lista = q.list();
        if (lista.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**Metodo para saber si un grupo esta registrado*/
    private boolean existCiudad(Ciudad ciudad) {
        Session ss = conn.getSession();
        String hql = "From Ciudad P WHERE P.codigo=?";
        Query q = ss.createQuery(hql);
        q.setString(0, ciudad.getCodigo());
        List<Ciudad> lista = q.list();
        ss.close();
        if (lista.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**Metodo para actualizar un grupo*/
    public void updateCiudad(Ciudad ciudad) {
        Session ss = conn.getSession();
        Transaction tx = ss.beginTransaction();
        ss.merge(ciudad);
        tx.commit();
        ss.close();
        getSubjectUpdate().notify(ModeloCiudad.class, ciudad, "updateCiudad");
    }

    /**Metodo para actualizar o insertar*/
    public void saveOrUpdate(Ciudad ciudad) {
        Session ss = conn.getSession();
        Transaction tx = ss.beginTransaction();
        ss.saveOrUpdate(ciudad);
        tx.commit();
        ss.close();
        getSubjectUpdate().notify(ModeloCiudad.class, ciudad, "saveOrUpdateCiudad");
    }

    /**Metodo para consultar todos los clientes*/
    public List<Ciudad> getAllCiudad() {
        List<Ciudad> ciudads = new ArrayList();
        String hql = "FROM Ciudad P";
        Session ss = conn.getSession();
        Query query = ss.createQuery(hql);
        if (query != null) {
            ciudads = query.list();
        }
        ss.close();
        return ciudads;
    }

    /**Metodo para consultar cliente por codigo*/
    public List<Ciudad> getCiudadPorCodigo(List<Ciudad> lista, String patronCodigo) {
        List<Ciudad> ciudads = new ArrayList();
        for (Ciudad k : lista) {
            String codigo = k.getCodigo() + "";
            if (!codigo.equals("") && patronCodigo.length() <= codigo.length()) {
                String codigoGrupo = codigo.substring(0, patronCodigo.length());
                if (codigoGrupo.equalsIgnoreCase(patronCodigo)) {
                    ciudads.add(k);
                }
            }
        }
        return ciudads;
    }

    public List<Ciudad> getCiudadPorNombre(List<Ciudad> lista, String patronNombre) {
        List<Ciudad> ciudads = new ArrayList();
        for (Ciudad k : lista) {
            String nombre = k.getNombre() + "";
            if (!nombre.equals("") && patronNombre.length() <= nombre.length()) {
                String nombreGrupo = nombre.substring(0, patronNombre.length());
                if (nombreGrupo.equalsIgnoreCase(patronNombre)) {
                    ciudads.add(k);
                }
            }
        }
        return ciudads;
    }

    public List<Ciudad> getCiudadPorProvincia(List<Ciudad> lista, String patronProv) {
        List<Ciudad> ciudads = new ArrayList();
        for (Ciudad k : lista) {
            if (k.getProvincia() != null) {
                String prov = k.getProvincia().getNombre() + "";
                if (!prov.equals("") && patronProv.length() <= prov.length()) {
                    String nombreProv = prov.substring(0, patronProv.length());
                    if (nombreProv.equalsIgnoreCase(patronProv)) {
                        ciudads.add(k);
                    }
                }
            }
        }
        return ciudads;
    }

    public List<Ciudad> getCiudadPorRegion(List<Ciudad> lista, String patronRegion) {
        List<Ciudad> ciudads = new ArrayList();
        for (Ciudad k : lista) {
            if (k.getProvincia().getRegion() != null) {
                String region = k.getProvincia().getRegion().getDescripcion();
                if (!region.equals("") && patronRegion.length() <= region.length()) {
                    String nombreRegion = region.substring(0, patronRegion.length());
                    if (nombreRegion.equalsIgnoreCase(patronRegion)) {
                        ciudads.add(k);
                    }
                }
            }
        }
        return ciudads;
    }

    public Secuencia getSecuencia() {
        Session ss = conn.getSession();
        Transaction tx = ss.beginTransaction();
        String hql = "From Secuencia S where S.tabla=?";
        Query que = ss.createQuery(hql);
        que.setString(0, "Ciudad");
        List<Secuencia> secs = que.list();
        Secuencia sec0 = secs.get(0);
        ss.close();
        return sec0;
    }

    public void updateSecuencia(int valor) {
        Session ss = conn.getSession();
        Transaction tx = ss.beginTransaction();
        Secuencia sec0 = new Secuencia();
        sec0.setSecuencia(valor);
        sec0.setTabla("Ciudad");
        ss.update(sec0);
        tx.commit();
        ss.close();
    }
}
