package com.google.code.cana.web.usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.google.code.cana.pojo.pessoas.Usuario;

/**
 * A��o que recupera um registro de usu�rio e preenche o formul�rio correspondente
 * 
 * @author Taciano Pinheiro de Almeida Alc�ntara
 *
 */
public class PreencherFormularioAction extends RecuperarAction {

    private static Log log = LogFactory.getLog(PreencherFormularioAction.class);

    @Override
    protected void executarLogica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Instanciando dyna form");
        DynaActionForm dynaForm = (DynaActionForm) form;
        log.debug("Executando a��o para recuperar formul�rio");
        super.executarLogica(mapping, form, request, response);
        log.debug("Recuperando usu�rio");
        Usuario usuario = (Usuario) request.getAttribute("usuario");
        log.debug("Recuperando lista de pap�is associados aos usu�rios");
        dynaForm.set("papeis", usuario.getPapeisUsuario().toArray(new String[usuario.getPapeisUsuario().size()]));
        log.debug("Preenchendo formul�rio");
        BeanUtils.copyProperties(form, usuario);
    }
}
