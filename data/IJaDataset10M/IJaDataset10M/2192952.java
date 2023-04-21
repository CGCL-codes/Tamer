package cycle;

import java.util.Collection;
import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.PlainTextConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.util.FileIO;
import cycle.retain.RetainConfig;
import cycle.retrieve.QueryConfig;
import cycle.retrieve.RetrieveResult;
import cycle.retrieve.SimilarityConfig;
import cycle.reuse.AutoAdaptationConfig;
import cycle.revise.RevisionConfig;

/**
 * <p>
 * Classe que implementa o ciclo de um RBC: Retrieve(recupera),Reuse(reusa),Revise(revisa), Retain(retem),
 * aplicado a um sistema de especula�ao imobiliaria de alugueis de apartamentos.
 * </p>
 * </br>
 * <p>
 * Descricao do caso: Bairro, Regiao, Area, Garagem, Quartos, Suites. 
 * </p>
 * <p>
 * Solucao do caso: Preco, Nome.
 * </p>
 * <p>
 * Como funciona?
 * </p>
 * <ul>
 * <li>O usu�rio preenche os dados referentes ao aluguel que ele deseja. Ele preenche descricao do caso! No caso, ele vai inserir:
 * o bairro, a regiao, a area do apartamento, a quantidade de vagas na garagem, o numero de quartos e de suites. Isso ser� feito
 * usando a classe QueryConfig.</li>
 * <li>J� iniciado o ciclo do RBC, o usu�rio vai configurar como quer que seja o peso para cada atributo da descri�ao para a recupera�ao
 * dos casos. Al�m disso, o usu�rio ter� que informa a quantidade de casos que deseja que seja recuperado (k).
 * Isso ser� feito usando a classe SimilarityConfig. </li>
 * <li>Feito isso, o usuario podera ver os casos recuperados atraves da classe RetrieveResult.</li>
 * <li>O proximo passo eh adaptar os casos recuperados podendo usar a propor�ao direta de 3 possiveis formas: �rea privativa e pre�o;
 * vagas na garagem e pre�o;quarto e pre�o;suites e pre�o. Podem ser usadas de nehuma a todas as op�oes. Isso � implementado usando
 * a classe AutoAdaptationConfig.</li>
 * <li>A revis�o � quando os casos j� adaptados (ou n�o) podem ser alterados juntamente com a solu��o. Todos os atributos (descricao e solucao) 
 * podem ser alterados para melhor corresponder o caso. Isso � feito na classe RevisionConfig</li>
 * <li>Finalmente, o aprendizado � poss�vel de ser feito. O usu�rio seleciona se quer ou n�o fazer o aprendizado dos casos selecionados. Se sim,
 * um novo id � dado ao caso e ele eh guardado.</li>
 * </ul>
 * @author Clerton
 *
 */
public class RentRecommender implements StandardCBRApplication {

    private static RentRecommender _instance = null;

    private Connector _connector;

    private CBRCaseBase _caseBase;

    private RentRecommender() {
    }

    /**
	 * Metodo que retorna a instancia unica da classe RentRecommender 
	 * @return Uma instancia unica da classe RentRecommender
	 */
    public static RentRecommender getInstance() {
        if (_instance == null) _instance = new RentRecommender();
        return _instance;
    }

    @Override
    public void configure() throws ExecutionException {
        try {
            _connector = new PlainTextConnector();
            _connector.initFromXMLfile(FileIO.findFile("src/database/plaintextconfig.xml"));
            _caseBase = new LinealCaseBase();
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public void cycle(CBRQuery query) throws ExecutionException {
        SimilarityConfig sim = new SimilarityConfig();
        NNConfig simConfig = sim.getSimilarityConfig();
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), query, simConfig);
        Collection<CBRCase> selectedcases = SelectCases.selectTopK(eval, sim.getK());
        RetrieveResult rr = new RetrieveResult(eval, selectedcases, sim.getK());
        rr.printRetrievalResult();
        AutoAdaptationConfig aac = new AutoAdaptationConfig(true, false, false, false, query, selectedcases);
        aac.printAdaptionResult();
        RevisionConfig rc = new RevisionConfig(selectedcases);
        rc.saveCase(0, "BoaViagem", "ShoppingCenterRecife", 100.0, 2, 3, 3, 1600.0, "MarioLinharesPordeus");
        rc.printRevisionCases();
        RetainConfig retain = new RetainConfig(selectedcases, _caseBase);
        retain.addCaseToRetain(0);
        retain.addCaseToRetain(1);
        retain.learn();
    }

    @Override
    public void postCycle() throws ExecutionException {
        _connector.close();
    }

    @Override
    public CBRCaseBase preCycle() throws ExecutionException {
        _caseBase.init(_connector);
        java.util.Collection<CBRCase> cases = _caseBase.getCases();
        for (CBRCase c : cases) System.out.println(c);
        return _caseBase;
    }

    public static void main(String[] args) {
        RentRecommender recommender = RentRecommender.getInstance();
        try {
            recommender.configure();
            recommender.preCycle();
            QueryConfig qf = new QueryConfig();
            qf.setQuery("BoaViagem", "ShoppingCenterRecife", 100.0, 2, 3, 3);
            System.out.println("Query: (BoaViagem;ShoppingCenterRecife;100.0;2;3;3)");
            boolean cont = true;
            while (cont) {
                CBRQuery query = qf.getQuery();
                recommender.cycle(query);
                cont = false;
            }
            recommender.postCycle();
        } catch (Exception e) {
            org.apache.commons.logging.LogFactory.getLog(RentRecommender.class).error(e);
        }
        System.exit(0);
    }
}
