package org.hmaciel.descop.Action;

import java.util.Map;
import javax.ejb.Local;
import javax.faces.convert.Converter;
import org.hmaciel.descop.ejb.entity.PersonBean;

@Local
public interface IDarMedicosRole {

    public abstract void loadData();

    public Map<String, PersonBean> getParts();

    public Converter getConverter();

    public abstract void destroy();

    public abstract String cargarAutor();

    public abstract PersonBean getMedicoAutor();

    public abstract void setMedicoAutor(PersonBean medicoAutor);

    public String getMedicoElegido();

    public void setMedicoElegido(String medicoElegido);
}
