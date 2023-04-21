package com.googlecode.ouvidoria.apresentacao.exibeContato;

import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see com.googlecode.ouvidoria.apresentacao.exibeContato.ExibeContatoCTL
 */
public class ExibeContatoCTLImpl extends ExibeContatoCTL {

    @Override
    public void populaTela(ActionMapping mapping, PopulaTelaForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        form.setEmail("email");
        form.setEndereco("endereco");
        form.setTelefone("telefone");
    }
}
