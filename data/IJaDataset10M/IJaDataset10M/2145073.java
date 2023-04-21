package com.aufa.elSecreto.modelo.dao.impl;

import com.aufa.elSecreto.modelo.dao.ProductoDao;
import com.aufa.elSecreto.modelo.dominio.Producto;
import com.aufa.elSecreto.modelo.excepcion.ExcepcionProducto;
import com.aufa.elSecreto.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Implementacion de la interface ProductoDao
 * @author Caligares Miguel Augusto
 * @email mcaligares@gmail.com
 * @version 1.3
 */
public class ProductoDaoImpl implements ProductoDao {

    /**
     * Obtiene todos los productos de la tabla PRODUCTOS.
     * @return List<Producto> Lista de productos
     * @throws ExcepcionProducto excepciones causadas por conexion a la DB relacionada con Producto
     */
    public List<Producto> getProductos() throws ExcepcionProducto {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Producto> productos = null;
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Producto.class);
            productos = criteria.list();
        } catch (RuntimeException exception) {
            throw new ExcepcionProducto(exception);
        } finally {
            session.close();
        }
        return productos;
    }

    /**
     * Obtiene un producto de la tabla PRODUCTOS sabiendo su ID.
     * @param idProducto Integer ID del producto a obtener
     * @return Producto | null
     * @throws ExcepcionProducto excepciones causadas por conexion a la DB relacionada con Producto
     */
    public Producto getProducto(Integer idProducto) throws ExcepcionProducto {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Producto producto = null;
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Producto.class);
            criteria.add(Restrictions.eq("idProducto", idProducto));
            producto = (Producto) criteria.list().get(0);
        } catch (RuntimeException exception) {
            throw new ExcepcionProducto(exception);
        } finally {
            session.close();
        }
        return producto;
    }

    /**
     * Inserta un producto en la tabla PRODUCTOS.
     * @param producto Prducto a insertar
     * @throws ExcepcionProducto excepciones causadas por conexion a la DB relacionada con Producto
     */
    public void insertProducto(Producto producto) throws ExcepcionProducto {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(producto);
            session.getTransaction().commit();
        } catch (RuntimeException exception) {
            throw new ExcepcionProducto(exception);
        } finally {
            session.close();
        }
    }

    /**
     * Modifica un producto en la tabla PRODUCTOS
     * @param producto Producto a modificar
     * @throws ExcepcionProducto excepciones causadas por conexion a la DB relacionada con Producto
     */
    public void updateProducto(Producto producto) throws ExcepcionProducto {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(producto);
            session.getTransaction().commit();
        } catch (RuntimeException exception) {
            throw new ExcepcionProducto(exception);
        } finally {
            session.close();
        }
    }

    /**
     * Elimina un producto de la tabla PRODUCTO.
     * @param producto Producto a eliminar
     * @throws ExcepcionProducto excepciones causadas por conexion a la DB relacionada con Producto
     */
    public void deleteProducto(Producto producto) throws ExcepcionProducto {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.delete(producto);
            session.getTransaction().commit();
        } catch (RuntimeException exception) {
            throw new ExcepcionProducto(exception);
        } finally {
            session.close();
        }
    }

    /**
     * Obtiene un producto con esa descripcion
     * @param descripcion String descripcion del producto
     * @return Producto | null
     * @throws ExcepcionProducto excepciones causadas por conexion a la DB relacionada con Producto
     */
    public Producto getProductoByDescripcion(String descripcion) throws ExcepcionProducto {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Producto pedido = null;
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Producto.class);
            criteria.add(Restrictions.eq("descripcion", descripcion));
            pedido = (Producto) criteria.list().get(0);
        } catch (RuntimeException exception) {
            throw new ExcepcionProducto(exception);
        } finally {
            session.close();
        }
        return pedido;
    }

    /**
     * Obtiene una lista de productos con esa descripcion
     * @param descripcion String descripcion a obtener
     * @return List<Producto>
     * @throws ExcepcionProducto excepciones causadas por conexion a la DB relacionada con Producto
     */
    public List<Producto> getProductosByStartDescripcion(String descripcion) throws ExcepcionProducto {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Producto> productos = null;
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Producto.class);
            criteria.add(Restrictions.like("descripcion", descripcion, MatchMode.START));
            productos = criteria.list();
        } catch (RuntimeException exception) {
            throw new ExcepcionProducto(exception);
        } finally {
            session.close();
        }
        return productos;
    }
}
