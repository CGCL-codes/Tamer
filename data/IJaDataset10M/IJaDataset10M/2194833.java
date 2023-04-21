package br.com.infomais.cadastro.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import br.com.infomais.bean.Produto;

@Stateless
public class ProdutoSessionBean implements ProdutoSession {

    @PersistenceContext
    private EntityManager em;

    public Produto create(Produto produto) {
        em.persist(produto);
        return produto;
    }

    public Produto update(Produto produto) {
        em.merge(produto);
        return produto;
    }

    public void remove(Produto produto) {
        em.remove(produto);
    }

    public List<Produto> getProdutos(Integer idCategoria) {
        List<Produto> produtos = (List<Produto>) (em.createQuery(" FROM Produto p WHERE p.categoria.id =:id").setParameter("id", idCategoria).getResultList());
        return produtos;
    }

    public List<Produto> getProdutos() {
        List<Produto> produtos = (List<Produto>) (em.createQuery(" FROM Produto p").getResultList());
        return produtos;
    }

    public List<Produto> getProdutosPromocao() {
        List<Produto> produtos = (List<Produto>) (em.createQuery(" FROM Produto p WHERE p.promocao = true").getResultList());
        return produtos;
    }

    public Produto get(Integer id) {
        return em.find(Produto.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Produto> getProdutosPorNomeOuCodigoBarras(String filtro) {
        return em.createQuery("FROM Produto p" + " WHERE (p.codigoBarras like :filtro OR UPPER(p.nome) like UPPER(:filtro))").setParameter("filtro", "%" + filtro + "%").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Produto> getProdutosPorTodosCampos(String filtro) {
        return em.createQuery("FROM Produto p" + " WHERE (p.codigoBarras like :filtro " + "OR UPPER(p.nome) like UPPER(:filtro) " + "OR UPPER(p.descricao) like UPPER(:filtro) " + "OR UPPER(p.link) like UPPER(:filtro) " + "OR UPPER(p.fabricante) like UPPER(:filtro) " + " ) ").setParameter("filtro", "%" + filtro + "%").getResultList();
    }
}
