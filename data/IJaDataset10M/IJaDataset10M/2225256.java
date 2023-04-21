package org.opensih.vaq.Actions;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.Identity;
import org.opensih.comunes.Controladores.IMantenimiento;
import org.opensih.descop.Controladores.ICCU_BusquedaDocClin;
import org.opensih.descop.Modelo.DocClin;
import org.opensih.descop.Utils.CriterioBusqueda;
import org.opensih.descop.Utils.TipoCriterio;
import org.opensih.vaq.Informes.IGenHojaEstPuntos;

@Name("estadisticaPuntos")
@Stateful
public class EstadisticaPuntosAction implements IEstadisticaPuntos {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager em;

    @EJB
    IGenHojaEstPuntos logicaEP;

    @EJB
    ICCU_BusquedaDocClin ccu_BusquedaDocClin;

    @EJB
    IMantenimiento logica;

    Date fecha_cirugia;

    Date fecha_cirugia2;

    List<DocClin> allDescr = new LinkedList<DocClin>();

    @In
    Identity identity;

    @Create
    @Begin(join = true)
    public void inicio() {
        if (allDescr.size() != 0) {
            allDescr.clear();
        }
        fecha_cirugia = fecha_cirugia2 = null;
    }

    public String crearInforme() {
        if (fecha_cirugia == null || fecha_cirugia2 == null) {
            FacesMessages.instance().add("Debe ingresar el periodo en el que se realizaron las cirugias.");
            return null;
        }
        if (allDescr.size() != 0) {
            allDescr.clear();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<CriterioBusqueda> criterios = new LinkedList<CriterioBusqueda>();
        criterios.add(new CriterioBusqueda("completed", "", TipoCriterio.Estado_DocClin));
        criterios.add(new CriterioBusqueda("NO", "", TipoCriterio.Cirugia_Suspendida));
        String f1 = sdf.format(fecha_cirugia);
        String f2 = sdf.format(fecha_cirugia2);
        criterios.add(new CriterioBusqueda(f1, f2, TipoCriterio.Fecha_Cirugia_Between));
        allDescr = ccu_BusquedaDocClin.realizarConsulta(criterios, em, 10000);
        Workbook wb = logicaEP.crearInforme(allDescr, f1 + "-" + f2);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String FileName = "Estadistica_VAQ.xls";
        BufferedOutputStream output = null;
        try {
            response.reset();
            response.setHeader("Content-Type", "application/xls");
            response.setHeader("Content-Disposition", "inline; filename=\"" + FileName + "\"");
            output = new BufferedOutputStream(response.getOutputStream());
            wb.write(output);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        facesContext.responseComplete();
        return "ok";
    }

    public String crearInformeAnestesia() {
        if (fecha_cirugia == null || fecha_cirugia2 == null) {
            FacesMessages.instance().add("Debe ingresar el periodo en el que se realizaron las cirugias.");
            return null;
        }
        if (allDescr.size() != 0) {
            allDescr.clear();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<CriterioBusqueda> criterios = new LinkedList<CriterioBusqueda>();
        criterios.add(new CriterioBusqueda("completed", "", TipoCriterio.Estado_DocClin));
        criterios.add(new CriterioBusqueda("NO", "", TipoCriterio.Cirugia_Suspendida));
        String f1 = sdf.format(fecha_cirugia);
        String f2 = sdf.format(fecha_cirugia2);
        criterios.add(new CriterioBusqueda(f1, f2, TipoCriterio.Fecha_Cirugia_Between));
        allDescr = ccu_BusquedaDocClin.realizarConsulta(criterios, em, 10000);
        Workbook wb = logicaEP.crearInformeAnestesia(allDescr, f1 + "-" + f2);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String FileName = "Estadistica_VAQ.xls";
        BufferedOutputStream output = null;
        try {
            response.reset();
            response.setHeader("Content-Type", "application/xls");
            response.setHeader("Content-Disposition", "inline; filename=\"" + FileName + "\"");
            output = new BufferedOutputStream(response.getOutputStream());
            wb.write(output);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        facesContext.responseComplete();
        return "ok";
    }

    public void seteo() {
    }

    @Destroy
    @Remove
    public void destroy() {
    }

    public Date getFecha_cirugia() {
        return fecha_cirugia;
    }

    public void setFecha_cirugia(Date fechaCirugia) {
        fecha_cirugia = fechaCirugia;
    }

    public Date getFecha_cirugia2() {
        return fecha_cirugia2;
    }

    public void setFecha_cirugia2(Date fechaCirugia2) {
        fecha_cirugia2 = fechaCirugia2;
    }
}
