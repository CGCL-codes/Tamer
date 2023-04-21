package br.com.jops.cih.input;

import br.com.jops.cci.NoticiaPesquisaForm;
import br.com.jops.cih.form.NoticiaPesquisaFormTag;
import br.com.jops.cdp.Categoria;
import javax.servlet.jsp.JspException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Welton
 * Date: 21/08/2007
 * Time: 01:34:11
 * Renderiza combo de selecao de categoria
 * de pesquisa
 */
public class NoticiaCategoriaPesquisaSelectTag extends SelectTag {

    protected String obterNome() {
        return NoticiaPesquisaForm.CATEGORIA_REQ_PARM;
    }

    protected void renderizarItens() throws JspException {
        try {
            NoticiaPesquisaFormTag parent = (NoticiaPesquisaFormTag) getParent();
            if (parent == null) {
                throw new JspException("Tag <noticia-categoria-pesquisa-select> deve ter como pai uma tag do tipo <noticia-pesquisa-form>.");
            }
            String selecionado = parent.obterForm().getCategoria();
            Collection<Categoria> categorias = parent.obterForm().getCategorias();
            renderizarItem("Categoria", "", false);
            for (Categoria categoria : categorias) {
                renderizarItem(categoria.getNome(), categoria.getId(), (selecionado != null && categoria.getId().equals(selecionado)));
            }
        } catch (ClassCastException e) {
            throw new JspException("Tag <noticia-categoria-pesquisa-select> tem como pai uma tag invalida. Apenas a tag <noticia-pesquisa-form> � suportado.", e);
        } catch (Exception e) {
            throw new JspException(e);
        }
    }
}
