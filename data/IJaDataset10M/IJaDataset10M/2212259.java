package br.com.insight.consultoria.negocio.bo.interfacebo;

import br.com.insight.consultoria.entity.Usuario;
import br.com.insight.consultoria.erro.exception.InsightException;

public interface UsuarioBO {

    public void inserir(Usuario usuario) throws InsightException;

    public void alterar(Usuario usuario) throws InsightException;

    public void excluir(Usuario usuario) throws InsightException;

    public Usuario getUsuario(Long id) throws InsightException;

    public boolean validateUsuario(Long id) throws InsightException;

    public Usuario loginUsuario(Long id, String senha) throws InsightException;
}
