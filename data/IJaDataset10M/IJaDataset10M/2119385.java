package br.ufrgs.inf.biblioteca.business;

import java.util.Collection;
import br.gov.framework.demoiselle.core.layer.IBusinessController;
import br.ufrgs.inf.biblioteca.bean.Usuario;

public interface IUsuarioBC extends IBusinessController {

    public Usuario buscarPorId(Integer id);

    public Object incluir(Usuario usuario);

    public void excluir(Usuario usuario);

    public Collection<Usuario> listar();

    public boolean podeExcluir(Usuario usuario);
}
