package br.com.sse.actions;

import java.util.Date;
import org.mentawai.core.BaseAction;
import br.com.sse.beans.Es;
import br.com.sse.beans.Movimento;
import br.com.sse.beans.Paciente;
import br.com.sse.beans.Produto;
import br.com.sse.dao.GenericDao;
import br.com.sse.dao.HibernateGenericDao;
import br.com.sse.utils.HibernateUtility;

public class MovimentoAction extends BaseAction {

    private static final String ERROR_MSG = "Ocorreu um erro!";

    private static final String CONGRATULATION_MSG = "Dados enviados com sucesso!";

    private Movimento movimento;

    private Produto produto;

    private Paciente paciente;

    private Es es;

    private GenericDao dao;

    public String execute() throws Exception {
        try {
            dao = new HibernateGenericDao();
            HibernateUtility.beginTransaction();
            produto = (Produto) dao.getById(Produto.class, new Long(input.getStringValue("produto")));
            es = (Es) dao.getById(Es.class, new Integer(input.getStringValue("es")));
            movimento = new Movimento();
            movimento.setProduto(produto);
            movimento.setEs(es);
            movimento.setData(((Date) input.getValue("data")));
            movimento.setDocumento(input.getStringValue("documento"));
            movimento.setObservacao(input.getStringValue("observacao"));
            if (input.getIntValue("paciente") != 0) {
                paciente = (Paciente) dao.getById(Paciente.class, new Long(input.getStringValue("paciente")));
                movimento.setPaciente(paciente);
            }
            if (input.getValue("quantidade") != null) movimento.setQuantidade(new Integer(input.getStringValue("quantidade")));
            if (input.getValue("valor") != null) movimento.setValorTotal(new Double(input.getStringValue("valor")));
            dao.save(movimento);
            HibernateUtility.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            HibernateUtility.rollbackTransaction();
            addError(ERROR_MSG);
            return ERROR;
        } finally {
            HibernateUtility.closeSession();
        }
        output.setValue("mensagem_sucesso", CONGRATULATION_MSG);
        return SUCCESS;
    }
}
