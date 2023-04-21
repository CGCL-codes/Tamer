package arnaldoTestandoConeccao;

import org.hibernate.Session;

public class AdicionarProduto {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println("Adicionando Produto");
        Session session = new HibernateFactory().getSession();
        Produto p = new Produto();
        p.setNome("Martelo");
        p.setDescricao("Borracha");
        p.setPreco(100.0);
        System.out.println(p.getNome() + "#" + p.getDescricao());
        ProdutoDAO pdao = new ProdutoDAO(session);
        session.beginTransaction();
        pdao.salva(p);
        session.getTransaction().commit();
        System.out.println("ID do produto: " + p.getId());
        session.close();
        System.out.println("Adicionado Produto");
    }
}
