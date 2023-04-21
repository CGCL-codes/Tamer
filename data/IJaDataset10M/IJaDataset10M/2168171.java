package Control;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import DAO.MakeDAO;
import Model.ChegadaRecurso;

/**
 * Classe que invoca m�todos para gerenciar 
 * a entidade ChegadaRecurso dentro do banco de dados
 * @author Arnaldo
 * @author Pablo Diego
 * @author Yuri Cariry 
 * 
 */
public class ChegadaRecursoCRUD {

    private static AnnotationConfiguration cfg;

    private static MakeDAO MakeDAO;

    private static SessionFactory factory;

    public static ChegadaRecursoCRUD instance;

    /**
	 * Padrao de Projeto Singleton
	 */
    public static ChegadaRecursoCRUD getInstance() {
        if (instance == null) {
            instance = new ChegadaRecursoCRUD();
        }
        return instance;
    }

    /**
	 * cria o ambiente de configura��o de conex�o 
	 * com o banco de dados automaticamente
	 */
    static {
        cfg = new AnnotationConfiguration();
        cfg.addAnnotatedClass(ChegadaRecurso.class);
        factory = cfg.buildSessionFactory();
        MakeDAO = new MakeDAO(factory.openSession());
    }

    /**
	 * gera as tabelas baseando-se 
	 * no objeto cfg que foi criado. 
	 */
    public void geraTabelas() {
        SchemaExport se = new SchemaExport(cfg);
        se.create(true, true);
    }

    /**
	 * M�todo que salva no banco usando o 
	 * padr�o DAO encapsulando a persist�ncia.
	 * @param chegadaRecurso Um ChegadaRecurso para armzenar 
	 * no banco de dados
	 */
    public void salvaChegadaRecurso(ChegadaRecurso chegadaRecurso) {
        MakeDAO.salvar(chegadaRecurso);
    }

    /**
	 * M�todo que remove um chegadaRecurso usando o 
	 * padr�o DAO encapsulando a persist�ncia.
	 * @param chegadaRecurso Um chegadaRecurso para armzen�-lo 
	 * no banco de dados
	 */
    public void removeChegadaRecurso(ChegadaRecurso chegadaRecurso) {
        MakeDAO.remove(chegadaRecurso);
    }

    /**
	 * Metodo que atualiza um chegadaRecurso
	 * @param chegadaRecurso O chegadaRecurso a ser atualizado.
	 */
    public void atualizarChegadaRecurso(ChegadaRecurso chegadaRecurso) {
        MakeDAO.atualiza(chegadaRecurso);
    }

    /**
	 * Metodo que retorna uma lista contendo ChegadaRecurso  
	 * @return Uma lista de ChegadaRecurso
	 */
    public List<ChegadaRecurso> getListaDeChegadaRecurso() {
        return MakeDAO.getlistaDeChegadaRecurso();
    }

    /**
	 * Metodo que retorna um ChegadaRecurso
	 * @param cod O cod do ChegadaRecurso a ser obtido.
	 * @return o ChegadaRecurso procurado.
	 */
    public ChegadaRecurso getChegadaRecurso(int cod) {
        return MakeDAO.getChegadaRecurso(cod);
    }
}
