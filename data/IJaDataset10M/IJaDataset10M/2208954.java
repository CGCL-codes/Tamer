package modulos.projeto.mbeans;

import java.util.List;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import moduloRemoto.bo.enumerador.LookUpEnum;
import moduloRemoto.bo.interfaces.ColaboradorRemote;
import moduloRemoto.bo.interfaces.ProjetoRemote;
import moduloRemoto.pojo.Colaborador;
import moduloRemoto.pojo.Projeto;
import modulos.util.ServiceLocator;
import modulos.util.Util;

/**
 * Bean Respons�vel pela tela de Associ��o de Colaboradores ao Projeto
 * @author Eduardo Ordine
 */
public class ProjetoAssociarColaboradoresMB {

    private ColaboradorRemote colaboradorBO;

    private ProjetoRemote projetoBO;

    public ProjetoAssociarColaboradoresMB() {
        try {
            colaboradorBO = (ColaboradorRemote) ServiceLocator.getInstance().get(LookUpEnum.COLABORADOR);
            projetoBO = (ProjetoRemote) ServiceLocator.getInstance().get(LookUpEnum.PROJETO);
        } catch (Exception ex) {
            System.out.println("Erro ao acessar o servidor remoto " + ex.getMessage());
        }
    }

    /**
	 * Recupera uma Listagem dos Colaboradores Associados ao Projeto
	 * @return Lista de Colaboradores Associados
	 */
    public List<Colaborador> getListaColaboradoresProjeto() {
        return projetoBO.carregarObjeto((Integer) Util.getSessionProperty("idPro")).getColaboradores();
    }

    /**
	 * Recupera uma Listagem de Todos os Colaboradores
	 * @return Lista de Colaboradores
	 */
    public List<Colaborador> getListaColaboradores() {
        return colaboradorBO.carregarListaColaboradores();
    }

    /**
	 * Action do gridview para associar um Colaborador ao Projeto
	 * @param event
	 */
    public String associarColaborador(ActionEvent event) {
        UIParameter component = (UIParameter) event.getComponent().findComponent("IdColAdd");
        Projeto objProjeto = projetoBO.carregarObjeto((Integer) Util.getSessionProperty("idPro"));
        Colaborador objColaborador = colaboradorBO.carregarObjeto((Integer) component.getValue());
        objColaborador.getListaProjetos().add(objProjeto);
        colaboradorBO.persistir(objColaborador);
        return "colaboradorAssociado";
    }

    /**
	 * Action do gridview para associar um Colaborador ao Projeto
	 * @param event
	 */
    public String removerColaborador(ActionEvent event) {
        UIParameter component = (UIParameter) event.getComponent().findComponent("IdColRem");
        Projeto objProjeto = projetoBO.carregarObjeto((Integer) Util.getSessionProperty("idPro"));
        Colaborador objColaborador = colaboradorBO.carregarObjeto((Integer) component.getValue());
        for (int i = 0; i < objColaborador.getListaProjetos().size(); i++) {
            Projeto objeto = objColaborador.getListaProjetos().get(i);
            if (objeto.getIdPro() == objProjeto.getIdPro()) objColaborador.getListaProjetos().remove(i);
        }
        colaboradorBO.persistir(objColaborador);
        return "colaboradorAssociado";
    }

    /**
	 * Action para redirecionar
	 * @return String de A��o
	 */
    public String redirect() {
        return "colaboradorAssociado";
    }
}
