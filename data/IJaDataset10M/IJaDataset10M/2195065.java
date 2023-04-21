package br.com.infomais.web.principal;

import java.util.List;
import java.util.Random;
import br.com.infomais.bean.Noticia;
import br.com.infomais.bean.Produto;
import br.com.infomais.cadastro.ejb.ProdutoSession;
import br.com.infomais.cadastro.ejb.ProdutoSessionBean;
import br.com.infomais.util.factory.SessionBeanManager;
import br.com.infomais.util.factory.SessionBeanManager.TipoEJB;
import br.com.infomais.web.noticia.NoticiaFactory;
import br.com.infomais.web.noticia.NoticiaReader;

public class TelaInicial {

    private Produto produtoPromocao1;

    private Produto produtoPromocao2;

    private Produto produto1;

    private Produto produto2;

    private Produto produto3;

    private Noticia noticia1;

    private Noticia noticia2;

    public TelaInicial() {
        setRandomProdutosPromocao();
        setRandomProdutos();
        setNoticias();
    }

    private void setNoticias() {
        NoticiaReader reader = NoticiaFactory.getInstance().getReader();
        noticia1 = reader.getNextRandom();
        noticia2 = reader.getNextRandom();
    }

    private void setRandomProdutos() {
        ProdutoSession produtoSession = SessionBeanManager.getInstance().buildBean(ProdutoSessionBean.class, TipoEJB.local);
        List<Produto> listaProdutos = produtoSession.getProdutos();
        int indiceMaximo = listaProdutos.size();
        Random random = new Random();
        if (indiceMaximo > 0) {
            int primeiroIndice = random.nextInt(indiceMaximo);
            produto1 = listaProdutos.get(primeiroIndice);
            int segundoIndice = random.nextInt(indiceMaximo);
            produto2 = listaProdutos.get(segundoIndice);
            if (indiceMaximo > 2) {
                int terceiroIndice = random.nextInt(indiceMaximo);
                produto3 = listaProdutos.get(terceiroIndice);
            }
        }
    }

    private void setRandomProdutosPromocao() {
        ProdutoSession produtoSession = SessionBeanManager.getInstance().buildBean(ProdutoSessionBean.class, TipoEJB.local);
        List<Produto> listaProdutosPromocao = produtoSession.getProdutosPromocao();
        int indiceMaximo = listaProdutosPromocao.size();
        Random random = new Random();
        if (indiceMaximo > 0) {
            int primeiroIndice = random.nextInt(indiceMaximo);
            produtoPromocao1 = listaProdutosPromocao.get(primeiroIndice);
            if (indiceMaximo > 1) {
                int segundoIndice = random.nextInt(indiceMaximo);
                produtoPromocao2 = listaProdutosPromocao.get(segundoIndice);
            }
        }
    }

    public Produto getProduto1() {
        return produto1;
    }

    public void setProduto1(Produto produto1) {
        this.produto1 = produto1;
    }

    public Produto getProduto2() {
        return produto2;
    }

    public void setProduto2(Produto produto2) {
        this.produto2 = produto2;
    }

    public Produto getProduto3() {
        return produto3;
    }

    public void setProduto3(Produto produto3) {
        this.produto3 = produto3;
    }

    public Produto getProdutoPromocao1() {
        return produtoPromocao1;
    }

    public void setProdutoPromocao1(Produto produtoPromocao1) {
        this.produtoPromocao1 = produtoPromocao1;
    }

    public Produto getProdutoPromocao2() {
        return produtoPromocao2;
    }

    public void setProdutoPromocao2(Produto produtoPromocao2) {
        this.produtoPromocao2 = produtoPromocao2;
    }

    public Noticia getNoticia1() {
        return noticia1;
    }

    public void setNoticia1(Noticia noticia1) {
        this.noticia1 = noticia1;
    }

    public Noticia getNoticia2() {
        return noticia2;
    }

    public void setNoticia2(Noticia noticia2) {
        this.noticia2 = noticia2;
    }
}
