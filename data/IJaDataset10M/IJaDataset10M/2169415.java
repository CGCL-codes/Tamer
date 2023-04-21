package org.hmaciel.descop.ejb.controladores;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import org.hmaciel.descop.ejb.act.DocClinBean;

@Local
public interface IBusq {

    public abstract List<DocClinBean> realizarConsulta(String id, String exten, String ext_role_pac, String root_role_pac, String ci_autor, String ci_resp, Date fecha_Documento, Date fecha_Documento2, Date fecha_cirugia, Date fecha_cirugia2, int idserv, String cirugia, EntityManager em);

    public abstract List<DocClinBean> realizarConsulta(List<String> ids, EntityManager em);
}
