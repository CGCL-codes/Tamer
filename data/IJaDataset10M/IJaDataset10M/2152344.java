package com.cibertec.project.action;

import java.util.List;
import org.apache.log4j.Logger;
import com.cibertec.project.bean.TemaDTO;
import com.cibertec.project.service.ApplicationBusinessDelegate;
import com.cibertec.project.service.TemaService;
import com.opensymphony.xwork2.ActionSupport;

public class TemaAction extends ActionSupport {

    private static final Logger log = Logger.getLogger(PersonaAction.class);

    private static final long serialVersionUID = 1L;

    private TemaDTO tema;

    private List<TemaDTO> temaList;

    private static ApplicationBusinessDelegate abd = new ApplicationBusinessDelegate();

    private static TemaService temaService = abd.getTemaService();

    public String listado() throws Exception {
        temaList = temaService.obtenerTemas();
        return SUCCESS;
    }

    public String goToTema() throws Exception {
        if (tema != null) tema = temaService.obtenerTema(tema);
        return SUCCESS;
    }

    public String temaRegistraModifica() throws Exception {
        if (tema.getIntCodTema() == 0) {
            temaService.insertTema(tema);
        } else {
            temaService.updateTema(tema);
        }
        return SUCCESS;
    }

    public TemaDTO getTema() {
        return tema;
    }

    public void setTema(TemaDTO tema) {
        this.tema = tema;
    }

    public List<TemaDTO> getTemaList() {
        return temaList;
    }

    public void setTemaList(List<TemaDTO> temaList) {
        this.temaList = temaList;
    }

    public String temaElimina() throws Exception {
        log.info("antes de elimnar");
        temaService.deleteTema(tema);
        log.info("despues de elimnar");
        return SUCCESS;
    }
}
