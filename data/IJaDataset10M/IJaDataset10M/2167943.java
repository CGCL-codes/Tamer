package org.universa.tcc.gemda.web;

import java.util.Date;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.universa.tcc.gemda.entidade.Sprint;
import org.universa.tcc.gemda.web.config.ActionConfig;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Namespace("/sprint")
@Result(name = ActionSupport.SUCCESS, location = "/WEB-INF/content/sprint/gerenciar.jsp")
@ActionConfig(entity = "sprint")
public class SprintAction extends GeMDAAction<Sprint> {

    private Sprint sprint;

    @Action("quadro-tarefa")
    public String quadroTarefa() {
        try {
            sprint = getFacade().recuperar(Sprint.class, sprint.getId());
        } catch (Exception e) {
            doError("Erro ao invocar o m�todo #quadroTarefa()", e);
        }
        return INPUT;
    }

    @Override
    protected void doNovo() throws Exception {
        sprint = new Sprint(null, getProjetoCorrente(), new Date(), null);
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }
}
