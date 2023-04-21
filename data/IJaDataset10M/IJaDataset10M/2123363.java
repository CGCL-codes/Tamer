package com.odontosis.view.decorator;

import java.text.SimpleDateFormat;
import org.displaytag.decorator.TableDecorator;
import com.odontosis.entidade.Paciente;
import com.odontosis.entidade.Servico;
import com.odontosis.util.StringUtilsOdontosis;

/**
 * 
 * @author pablo
 *
 */
public class ListaServicoDecorator extends TableDecorator {

    public String getPaciente() {
        Servico servico = (Servico) getCurrentRowObject();
        return servico.getPacienteServico().getNome();
    }

    public String getTipo() {
        Servico servico = (Servico) getCurrentRowObject();
        return servico.getTipoServico().getDescricao();
    }

    public String getId() {
        Servico servico = (Servico) getCurrentRowObject();
        return servico.getId().toString();
    }

    public String getData() {
        Servico servico = (Servico) getCurrentRowObject();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(servico.getDataCadastro());
    }

    public String getPasta() {
        Servico servico = (Servico) getCurrentRowObject();
        Paciente paciente = servico.getPacienteServico();
        return StringUtilsOdontosis.isVazia(paciente.getNumeroPasta()) ? "Não possui" : StringUtilsOdontosis.colocaZeros(paciente.getNumeroPasta(), 10);
    }

    public String getFuncoes() {
        StringBuffer lEditar = new StringBuffer();
        lEditar.append("<a  href=\"/odontosis/cadastroServico.do?alteracao=" + getId() + " \" class=\"edit\" title=\"Editar Serviço\">");
        lEditar.append("</a>");
        lEditar.append("<a  href=\"javascript:deletar(" + getId() + ")\"+" + "\" class=\"del\" title=\"Excluir Serviço\"></a>");
        return lEditar.toString();
    }
}
