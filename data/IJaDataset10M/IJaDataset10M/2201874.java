package org.opensih.ViewActions.Mantenimiento;

import java.util.Map;
import javax.faces.convert.Converter;
import org.opensih.Modelo.Servicio;
import org.opensih.Modelo.UnidadEjecutora;

public interface IServicio {

    public void nuevo();

    public void destroy();

    public String alta();

    public String modificar();

    public void seteo();

    public void seteo2();

    public String getNombre();

    public void setNombre(String nombre);

    public String getNombreNuevo();

    public void setNombreNuevo(String nombreNuevo);

    public Map<String, Servicio> getOrgs();

    public Map<String, UnidadEjecutora> getUes();

    public Converter getConverterServ();

    public Converter getConverterUe();

    public Servicio getOrg();

    public void setOrg(Servicio org);

    public UnidadEjecutora getUe();

    public void setUe(UnidadEjecutora ue);

    public String getHabilitado();

    public void setHabilitado(String habilitado);

    public String getPagoVAQCir();

    public void setPagoVAQCir(String pagoVAQCir);

    public String getPagoVAQAnest();

    public void setPagoVAQAnest(String pagoVAQAnest);

    public String getVAQCir();

    public void setVAQCir(String vAQCir);

    public String getVAQAnest();

    public void setVAQAnest(String vAQAnest);
}
