package negocio;

import java.io.Serializable;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.UserTransaction;

/**
 *
 * @author user
 */
@Entity
@Table(name = "archivo")
@NamedQueries({ @NamedQuery(name = "Archivo.findAll", query = "SELECT a FROM Archivo a"), @NamedQuery(name = "Archivo.findByIdArchivo", query = "SELECT a FROM Archivo a WHERE a.idArchivo = :idArchivo"), @NamedQuery(name = "Archivo.findByRuta", query = "SELECT a FROM Archivo a WHERE a.ruta = :ruta") })
public class Archivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_archivo")
    private Integer idArchivo;

    @Basic(optional = false)
    @Column(name = "ruta")
    private String ruta;

    @JoinColumn(name = "id_tarea", referencedColumnName = "id_tarea")
    @ManyToOne(optional = false)
    private Tarea idTarea;

    public Archivo() {
    }

    public Archivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Archivo(Integer idArchivo, String ruta) {
        this.idArchivo = idArchivo;
        this.ruta = ruta;
    }

    public Archivo(Integer idArchivo, String ruta, Tarea idTarea) {
        this.idArchivo = idArchivo;
        this.ruta = ruta;
        this.idTarea = idTarea;
    }

    public Integer getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Tarea getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Tarea idTarea) {
        this.idTarea = idTarea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArchivo != null ? idArchivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Archivo)) {
            return false;
        }
        Archivo other = (Archivo) object;
        if ((this.idArchivo == null && other.idArchivo != null) || (this.idArchivo != null && !this.idArchivo.equals(other.idArchivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "negocio.Archivo[idArchivo=" + idArchivo + "]";
    }

    public void persist(Object object) {
        try {
            Context ctx = new InitialContext();
            UserTransaction utx = (UserTransaction) ctx.lookup("java:comp/env/UserTransaction");
            utx.begin();
            EntityManager em = (EntityManager) ctx.lookup("java:comp/env/persistence/LogicalName");
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }

    /**********************insertar********************/
    public void insertar() {
        int id_tarea = this.getIdTarea().getIdTarea();
        String queryString = "insert into archivo (id_archivo,id_Tarea, ruta) values(" + this.getIdArchivo() + "," + id_tarea + ",'" + this.getRuta() + "')";
        try {
            System.out.println("query :" + queryString);
            Context ctx = new InitialContext();
            UserTransaction utx = (UserTransaction) ctx.lookup("java:comp/env/UserTransaction");
            utx.begin();
            EntityManager em = (EntityManager) ctx.lookup("java:comp/env/persistence/LogicalName");
            Query q = (Query) em.createNativeQuery(queryString);
            q.executeUpdate();
            utx.commit();
        } catch (Exception e) {
            System.out.println("error al insertar" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*******METODO PARA OBTENER EL ID **********/
    public static int obtener_new_id() {
        String queryString = "SELECT id_archivo FROM `archivo`";
        Integer mayor = 0, tmp = 0;
        try {
            Context ctx = new InitialContext();
            EntityManager em = (EntityManager) ctx.lookup("java:comp/env/persistence/LogicalName");
            Query q = (Query) em.createNativeQuery(queryString, Archivo.class);
            List<Archivo> resultado = (List<Archivo>) q.getResultList();
            for (int i = 0; i < resultado.size(); i++) {
                Archivo ev = (Archivo) resultado.get(i);
                tmp = ev.getIdArchivo();
                if (tmp > mayor) mayor = tmp;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Integer.valueOf(mayor) + 1;
    }
}
