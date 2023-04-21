package br.com.sse.actions;

import org.mentawai.core.BaseAction;
import br.com.sse.beans.Usuario;
import br.com.sse.dao.GenericDao;
import br.com.sse.dao.HibernateGenericDao;
import br.com.sse.utils.HibernateUtility;
import br.com.sse.utils.SHA1Utility;

public class UsuarioAction extends BaseAction {

    private static final String ERROR_MSG = "Ocorreu um erro!";

    private static final String CONGRATULATION_MSG = "Dados enviados com sucesso!";

    private Usuario user;

    private GenericDao dao;

    public String execute() throws Exception {
        try {
            dao = new HibernateGenericDao();
            HibernateUtility.beginTransaction();
            user = new Usuario();
            user.setNome(input.getStringValue("nome"));
            user.setEmail(input.getStringValue("email"));
            if (input.getStringValue("regra").equals("1")) {
                user.setRegra("administrador");
            }
            if (input.getStringValue("regra").equals("2")) {
                user.setRegra("usuario");
            }
            user.setLogin(input.getStringValue("login"));
            user.setFone(input.getStringValue("fone"));
            user.setSenha(SHA1Utility.crypt(input.getStringValue("senha")));
            dao.save(user);
            HibernateUtility.commitTransaction();
            output.setValue("mensagem_sucesso", CONGRATULATION_MSG);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            HibernateUtility.rollbackTransaction();
            addError(ERROR_MSG);
            return ERROR;
        } finally {
            HibernateUtility.closeSession();
        }
    }
}
