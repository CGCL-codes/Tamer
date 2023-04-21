package com.google.code.cana.web.materiaprima;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.google.code.cana.pojo.insumos.MateriaPrima;

/**
 * A��o que recupera um registro de mat�ria prima e preenche o formul�rio correspondente
 * 
 * @author Rodrigo Barbosa Lira
 *
 */
public class PreencherFormularioAction extends RecuperarAction {

    private static Log log = LogFactory.getLog(PreencherFormularioAction.class);

    @Override
    protected void executarLogica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executando a��o para recuperar formul�rio");
        super.executarLogica(mapping, form, request, response);
        log.debug("Recuperando mat�ria prima");
        MateriaPrima materiaPrima = (MateriaPrima) request.getAttribute("materiaPrima");
        log.debug("Preenchendo formul�rio");
        BeanUtils.copyProperties(form, materiaPrima);
    }
}
