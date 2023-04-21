package org.opensih.ViewActions;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
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
import org.opensih.ControladoresCU.ICCU_BusquedaDocClin;
import org.opensih.ControladoresCU.IMantenimiento;
import org.opensih.Informes.IGenHojaSinadi;
import org.opensih.Modelo.DocClin;
import org.opensih.Modelo.Servicio;
import org.opensih.Modelo.UnidadEjecutora;
import org.opensih.Seguridad.IUsuarios;
import org.opensih.Seguridad.IUtils;
import org.opensih.Utils.CriterioBusqueda;
import org.opensih.Utils.TipoCriterio;
import org.opensih.Utils.Converters.UEConverter;

@Name("informeSinadi")
@Stateful
public class InformeSinadiAction implements IInformeSinadi {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager em;

    @EJB
    IGenHojaSinadi logicaHC;

    @EJB
    IUsuarios ctaUser;

    @EJB
    IUtils utils;

    @EJB
    IMantenimiento logica;

    @EJB
    ICCU_BusquedaDocClin ccu_BusquedaDocClin;

    Date fecha_cirugia;

    Date fecha_cirugia2;

    Servicio serv;

    int idserv;

    String msg;

    String usuario;

    int cantidadDesc;

    @In
    org.jboss.seam.security.Credentials credentials;

    List<DocClin> allDescr = new LinkedList<DocClin>();

    UnidadEjecutora ue;

    List<UnidadEjecutora> ues;

    Map<String, UnidadEjecutora> uesMap;

    @Create
    @Begin(join = true)
    public void inicio() {
        cantidadDesc = 0;
        if (allDescr.size() != 0) {
            allDescr.clear();
        }
        fecha_cirugia = fecha_cirugia2 = null;
        serv = new Servicio();
        ue = utils.devolverUE();
        ues = logica.listarUnidades();
        if (ue == null) ue = ues.get(0);
        Map<String, UnidadEjecutora> results = new TreeMap<String, UnidadEjecutora>();
        for (UnidadEjecutora p : ues) {
            String nom = p.toString();
            results.put(nom, p);
        }
        uesMap = results;
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
        criterios.add(new CriterioBusqueda(ue.getCodigo(), "", TipoCriterio.Id_UnidadEjecutora));
        criterios.add(new CriterioBusqueda("NO", "", TipoCriterio.Cirugia_Suspendida));
        String f1 = sdf.format(fecha_cirugia);
        String f2 = sdf.format(fecha_cirugia2);
        criterios.add(new CriterioBusqueda(f1, f2, TipoCriterio.Fecha_Cirugia_Between));
        allDescr = ccu_BusquedaDocClin.realizarConsulta(criterios, em, 10000);
        Workbook wb = logicaHC.crearInforme(allDescr, fecha_cirugia, ue.getNombre());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String uEjec = ue.getNombre().replaceAll(" ", "_");
        String FileName = "Informe_" + uEjec + ".xls";
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

    public Converter getConverterUe() {
        return new UEConverter(ues);
    }

    public Map<String, UnidadEjecutora> getUes() {
        return uesMap;
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

    public Servicio getServ() {
        return serv;
    }

    public void setServ(Servicio serv) {
        this.serv = serv;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UnidadEjecutora getUe() {
        return ue;
    }

    public void setUe(UnidadEjecutora ue) {
        this.ue = ue;
    }
}
