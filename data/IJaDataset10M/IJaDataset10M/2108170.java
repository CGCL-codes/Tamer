package laboratorio.servlets.crucigramas;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import laboratorio.db.dao.crucigramas.CrucigramaDAO;
import laboratorio.db.dao.crucigramas.TemaDAO;
import laboratorio.db.dao.definiciones.DefinicionDAO;
import laboratorio.db.dao.imagenes.ImagenDAO;
import laboratorio.model.BeanCrucigrama;
import laboratorio.model.BeanDefinicion;
import laboratorio.model.BeanImagen;
import laboratorio.model.BeanTema;
import laboratorio.servlets.ServletSuper;

/**
 * @author Martin
 * 
 */
public class ServletCrucigramas extends ServletSuper {

    public void doBuscarBeanDefinicion(HttpServletRequest req, javax.servlet.http.HttpServletResponse res, laboratorio.db.DBAccess pDba, Object[] pArg) {
        OutputStream xOut;
        ObjectOutputStream xObjOut;
        try {
            BeanDefinicion xBeanDef = (BeanDefinicion) pArg[0];
            DefinicionDAO xDefDao = new DefinicionDAO(pDba);
            Vector xVDefiniciones = xDefDao.searchObject(xBeanDef);
            xOut = res.getOutputStream();
            xObjOut = new ObjectOutputStream(xOut);
            xObjOut.writeObject(xVDefiniciones);
            xOut.flush();
            xOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doCreateBeanCrucigrama(HttpServletRequest req, javax.servlet.http.HttpServletResponse res, laboratorio.db.DBAccess pDba, Object[] pArg) {
        OutputStream xOut;
        ObjectOutputStream xObjOut;
        try {
            BeanCrucigrama xBeanCrucigrama = (BeanCrucigrama) pArg[0];
            CrucigramaDAO xCrucigramaDAO = new CrucigramaDAO(pDba);
            xCrucigramaDAO.insertObjAutonumeric(xBeanCrucigrama);
            xOut = res.getOutputStream();
            xObjOut = new ObjectOutputStream(xOut);
            xOut.flush();
            xOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doCreateBeanTema(HttpServletRequest req, javax.servlet.http.HttpServletResponse res, laboratorio.db.DBAccess pDba, Object[] pArg) {
        OutputStream xOut;
        ObjectOutputStream xObjOut;
        try {
            BeanTema xBeanTema = (BeanTema) pArg[0];
            TemaDAO xTemaDAO = new TemaDAO(pDba);
            Integer xIdCrucig = xTemaDAO.insertObjAutonumeric(xBeanTema);
            xOut = res.getOutputStream();
            xObjOut = new ObjectOutputStream(xOut);
            xObjOut.writeObject(xIdCrucig);
            xOut.flush();
            xOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doCreateBeanDefinicion(HttpServletRequest req, javax.servlet.http.HttpServletResponse res, laboratorio.db.DBAccess pDba, Object[] pArg) {
        OutputStream xOut;
        ObjectOutputStream xObjOut;
        try {
            BeanDefinicion xBeanDef = (BeanDefinicion) pArg[0];
            DefinicionDAO xDefDAO = new DefinicionDAO(pDba);
            Integer xIdCrucig = xDefDAO.insertObjAutonumeric(xBeanDef);
            xOut = res.getOutputStream();
            xObjOut = new ObjectOutputStream(xOut);
            xObjOut.writeObject(xIdCrucig);
            xOut.flush();
            xOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doCreateBeanImagen(HttpServletRequest req, javax.servlet.http.HttpServletResponse res, laboratorio.db.DBAccess pDba, Object[] pArg) {
        OutputStream xOut;
        ObjectOutputStream xObjOut;
        try {
            BeanImagen xBeanImagen = (BeanImagen) pArg[0];
            ImagenDAO xImagenDAO = new ImagenDAO(pDba);
            xImagenDAO.insertObject(xBeanImagen);
            xOut = res.getOutputStream();
            xObjOut = new ObjectOutputStream(xOut);
            xOut.flush();
            xOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doBuscarBeanCrucigrama(HttpServletRequest req, javax.servlet.http.HttpServletResponse res, laboratorio.db.DBAccess pDba, Object[] pArg) {
        OutputStream xOut;
        ObjectOutputStream xObjOut;
        try {
            BeanCrucigrama xCrucigrama = (BeanCrucigrama) pArg[0];
            CrucigramaDAO xCrucigramaDAO = new CrucigramaDAO(pDba);
            Vector xVResultado = xCrucigramaDAO.searchObject(xCrucigrama);
            xOut = res.getOutputStream();
            xObjOut = new ObjectOutputStream(xOut);
            xObjOut.writeObject(xVResultado);
            xOut.flush();
            xOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doBuscarBeanTema(HttpServletRequest req, javax.servlet.http.HttpServletResponse res, laboratorio.db.DBAccess pDba, Object[] pArg) {
        OutputStream xOut;
        ObjectOutputStream xObjOut;
        try {
            BeanTema xBeanTema = (BeanTema) pArg[0];
            TemaDAO xTemaDAO = new TemaDAO(pDba);
            Vector xVResultado = xTemaDAO.searchObject(xBeanTema);
            xOut = res.getOutputStream();
            xObjOut = new ObjectOutputStream(xOut);
            xObjOut.writeObject(xVResultado);
            xOut.flush();
            xOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doBuscarBeanImagen(HttpServletRequest req, javax.servlet.http.HttpServletResponse res, laboratorio.db.DBAccess pDba, Object[] pArg) {
        OutputStream xOut;
        ObjectOutputStream xObjOut;
        try {
            BeanImagen xBeanImagen = (BeanImagen) pArg[0];
            ImagenDAO xImagenDAO = new ImagenDAO(pDba);
            Vector xVResultado = xImagenDAO.searchObject(xBeanImagen);
            xOut = res.getOutputStream();
            xObjOut = new ObjectOutputStream(xOut);
            xObjOut.writeObject(xVResultado);
            xOut.flush();
            xOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
