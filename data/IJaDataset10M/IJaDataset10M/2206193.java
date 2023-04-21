package info.goldenorb.mining;

import static org.junit.Assert.assertEquals;
import info.goldenorb.common.Text;
import info.goldenorb.lang.Entity;
import info.goldenorb.lang.Equation;
import info.goldenorb.lang.Term;
import info.goldenorb.lang.Vector;
import java.util.Map;
import org.junit.Test;

public class VectorCorrelationTest {

    private String expected1stE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<graph>\n" + "<nodes>\n" + "<node id=\"1\" entity=\"Alexandre Gonçalves\" class=\"RESEARCHER\"/>\n" + "<node id=\"2\" entity=\"Lucas Nazário\" class=\"RESEARCHER\"/>\n" + "<node id=\"3\" entity=\"Alfredo Zipperer\" class=\"RESEARCHER\"/>\n" + "<node id=\"4\" entity=\"Alessandro Bovo\" class=\"RESEARCHER\"/>\n" + "<node id=\"5\" entity=\"Fabiano Beppler\" class=\"RESEARCHER\"/>\n" + "</nodes>\n" + "<edges>\n" + "<edge source=\"1\" target=\"2\" weight=\"0.7749170345049221\"/>\n" + "<edge source=\"1\" target=\"3\" weight=\"0.1891961206550691\"/>\n" + "<edge source=\"1\" target=\"4\" weight=\"0.05284113698543604\"/>\n" + "<edge source=\"1\" target=\"5\" weight=\"0.3456059922314784\"/>\n" + "<edge source=\"2\" target=\"3\" weight=\"0.17145376648627703\"/>\n" + "<edge source=\"2\" target=\"4\" weight=\"0.04788582308242793\"/>\n" + "<edge source=\"2\" target=\"5\" weight=\"0.4242380813185811\"/>\n" + "<edge source=\"3\" target=\"4\" weight=\"0.4968815901856306\"/>\n" + "<edge source=\"3\" target=\"5\" weight=\"0.3823341496562339\"/>\n" + "<edge source=\"4\" target=\"5\" weight=\"0.10678322106312155\"/>\n" + "</edges>\n" + "</graph>";

    private String expected2ndE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<graph>\n" + "<nodes>\n" + "<node id=\"1\" entity=\"Alexandre Gonçalves\" class=\"RESEARCHER\"/>\n" + "<node id=\"2\" entity=\"Lucas Nazário\" class=\"RESEARCHER\"/>\n" + "<node id=\"3\" entity=\"Alfredo Zipperer\" class=\"RESEARCHER\"/>\n" + "<node id=\"4\" entity=\"Alessandro Bovo\" class=\"RESEARCHER\"/>\n" + "<node id=\"5\" entity=\"Fabiano Beppler\" class=\"RESEARCHER\"/>\n" + "</nodes>\n" + "<edges>\n" + "<edge source=\"1\" target=\"2\" weight=\"0.5163977794943222\"/>\n" + "<edge source=\"1\" target=\"3\" weight=\"0.2581988897471611\"/>\n" + "<edge source=\"1\" target=\"4\" weight=\"0.2581988897471611\"/>\n" + "<edge source=\"1\" target=\"5\" weight=\"0.22360679774997896\"/>\n" + "<edge source=\"2\" target=\"3\" weight=\"0.33333333333333337\"/>\n" + "<edge source=\"2\" target=\"4\" weight=\"0.33333333333333337\"/>\n" + "<edge source=\"2\" target=\"5\" weight=\"0.5773502691896258\"/>\n" + "<edge source=\"3\" target=\"4\" weight=\"0.6666666666666667\"/>\n" + "<edge source=\"3\" target=\"5\" weight=\"0.2886751345948129\"/>\n" + "<edge source=\"4\" target=\"5\" weight=\"0.2886751345948129\"/>\n" + "</edges>\n" + "</graph>";

    private String expected3rdE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<graph>\n" + "<nodes>\n" + "<node id=\"1\" entity=\"Entity 1\" class=\"RESEARCHER\"/>\n" + "<node id=\"2\" entity=\"Entity 2\" class=\"RESEARCHER\"/>\n" + "<node id=\"3\" entity=\"Entity 3\" class=\"RESEARCHER\"/>\n" + "<node id=\"4\" entity=\"Entity 4\" class=\"RESEARCHER\"/>\n" + "<node id=\"5\" entity=\"Entity 5\" class=\"RESEARCHER\"/>\n" + "<node id=\"6\" entity=\"Entity 6\" class=\"RESEARCHER\"/>\n" + "<node id=\"7\" entity=\"Entity 7\" class=\"RESEARCHER\"/>\n" + "<node id=\"8\" entity=\"Entity 8\" class=\"RESEARCHER\"/>\n" + "<node id=\"9\" entity=\"Entity 9\" class=\"RESEARCHER\"/>\n" + "<node id=\"10\" entity=\"Entity 10\" class=\"RESEARCHER\"/>\n" + "</nodes>\n" + "<edges>\n" + "<edge source=\"1\" target=\"2\" weight=\"0.0041600354754546725\"/>\n" + "<edge source=\"1\" target=\"3\" weight=\"0.024807961882950626\"/>\n" + "<edge source=\"1\" target=\"4\" weight=\"0.006543864309242861\"/>\n" + "<edge source=\"1\" target=\"6\" weight=\"0.06187909523523561\"/>\n" + "<edge source=\"1\" target=\"10\" weight=\"0.015145166398714391\"/>\n" + "<edge source=\"2\" target=\"3\" weight=\"0.7034585004770881\"/>\n" + "<edge source=\"2\" target=\"4\" weight=\"0.5753458771785144\"/>\n" + "<edge source=\"2\" target=\"5\" weight=\"0.07424536523499925\"/>\n" + "<edge source=\"2\" target=\"6\" weight=\"8.212899286490681E-4\"/>\n" + "<edge source=\"2\" target=\"7\" weight=\"0.05157412296106031\"/>\n" + "<edge source=\"2\" target=\"8\" weight=\"0.004512402936634486\"/>\n" + "<edge source=\"2\" target=\"9\" weight=\"0.09654298400066272\"/>\n" + "<edge source=\"2\" target=\"10\" weight=\"0.20297402000439285\"/>\n" + "<edge source=\"3\" target=\"4\" weight=\"0.4908894948256624\"/>\n" + "<edge source=\"3\" target=\"5\" weight=\"0.0796653495037214\"/>\n" + "<edge source=\"3\" target=\"6\" weight=\"0.007187964377158714\"/>\n" + "<edge source=\"3\" target=\"7\" weight=\"0.10962059415538603\"/>\n" + "<edge source=\"3\" target=\"8\" weight=\"1.1332207280015616E-4\"/>\n" + "<edge source=\"3\" target=\"9\" weight=\"0.12845871942196016\"/>\n" + "<edge source=\"3\" target=\"10\" weight=\"0.05493451254969644\"/>\n" + "<edge source=\"4\" target=\"5\" weight=\"0.04447907071484117\"/>\n" + "<edge source=\"4\" target=\"7\" weight=\"0.04841906492898683\"/>\n" + "<edge source=\"4\" target=\"9\" weight=\"0.009421076298785781\"/>\n" + "<edge source=\"4\" target=\"10\" weight=\"0.027544658724485242\"/>\n" + "<edge source=\"5\" target=\"6\" weight=\"9.539198791411113E-4\"/>\n" + "<edge source=\"5\" target=\"7\" weight=\"0.046083966882905746\"/>\n" + "<edge source=\"5\" target=\"8\" weight=\"0.004800799952500712\"/>\n" + "<edge source=\"5\" target=\"9\" weight=\"0.027286354853441566\"/>\n" + "<edge source=\"5\" target=\"10\" weight=\"0.001392207937202559\"/>\n" + "<edge source=\"7\" target=\"9\" weight=\"0.16160588722223\"/>\n" + "</edges>\n" + "</graph>";

    private String expected4thE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<graph>\n" + "<nodes>\n" + "<node id=\"1\" entity=\"José Geraldo Pimentel Neto\" class=\"RESEARCHER\"/>\n" + "<node id=\"2\" entity=\"Renato de Castro Garcia\" class=\"RESEARCHER\"/>\n" + "<node id=\"3\" entity=\"Flávia Maia Jesini\" class=\"RESEARCHER\"/>\n" + "<node id=\"4\" entity=\"Valeria Maria Martins Judice\" class=\"RESEARCHER\"/>\n" + "</nodes>\n" + "<edges>\n" + "<edge source=\"1\" target=\"2\" weight=\"0.3881949591724805\"/>\n" + "<edge source=\"1\" target=\"3\" weight=\"0.6208252015243095\"/>\n" + "<edge source=\"1\" target=\"4\" weight=\"0.43837422385613356\"/>\n" + "<edge source=\"2\" target=\"3\" weight=\"0.44410310157310734\"/>\n" + "<edge source=\"2\" target=\"4\" weight=\"0.3129836929812566\"/>\n" + "<edge source=\"3\" target=\"4\" weight=\"0.5199671273918011\"/>\n" + "</edges>\n" + "</graph>";

    private String expected5thE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<graph>\n" + "<nodes>\n" + "<node id=\"1\" entity=\"Lucimara Stolz\" class=\"RESEARCHER\"/>\n" + "<node id=\"2\" entity=\"Paulo Luiz de Andrade Coutinho\" class=\"RESEARCHER\"/>\n" + "<node id=\"3\" entity=\"Ivanir Luiz de Oliveira\" class=\"RESEARCHER\"/>\n" + "<node id=\"4\" entity=\"Vera Maria Cartana Fernandes\" class=\"RESEARCHER\"/>\n" + "<node id=\"5\" entity=\"Wayne Brod Beskow\" class=\"RESEARCHER\"/>\n" + "<node id=\"6\" entity=\"Juliana Debei Herling\" class=\"RESEARCHER\"/>\n" + "</nodes>\n" + "<edges>\n" + "<edge source=\"2\" target=\"6\" weight=\"0.21404718739793865\"/>\n" + "<edge source=\"4\" target=\"6\" weight=\"0.0024648068134965825\"/>\n" + "</edges>\n" + "</graph>";

    /**
	 * First example. 
	 * @param corr
	 * @throws Exception
	 */
    private void composeVectors1stE(final VectorCorrelation corr) throws Exception {
        String keywords = "text mining|20|data mining|15|information retrieval|12|correlation|8|knowledge discovery|3|";
        Map<String, Term> terms = Text.generateVector(keywords);
        Vector vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        Entity entity = new Entity("1", "Alexandre Gonçalves", "researcher", vector);
        corr.add(entity);
        keywords = "text mining|12|information retrieval|5|ontology|3|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("2", "Lucas Nazário", "researcher", vector);
        corr.add(entity);
        keywords = "java programming|5|information retrieval|3|social network|3|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("3", "Alfredo Zipperer", "researcher", vector);
        corr.add(entity);
        keywords = "social network|15|markov chain|4|information retrieval|2|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("4", "Alessandro Bovo", "researcher", vector);
        corr.add(entity);
        keywords = "information retrieval|22|ontology|13|hermeneutics|6|distributed programming|2|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("5", "Fabiano Beppler", "researcher", vector);
        corr.add(entity);
    }

    /**
	 * Second example. 
	 * @param corr
	 * @throws Exception
	 */
    private void composeVectors2ndE(final VectorCorrelation corr) throws Exception {
        String keywords = "text mining|data mining|knowledge discovery|information retrieval|correlation";
        Map<String, Term> terms = Text.generateVector(keywords);
        Vector vector = new Vector(terms);
        Entity entity = new Entity("1", "Alexandre Gonçalves", "researcher", vector);
        corr.add(entity);
        keywords = "text mining|information retrieval|ontology";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("2", "Lucas Nazário", "researcher", vector);
        corr.add(entity);
        keywords = "java programming|information retrieval|social network";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("3", "Alfredo Zipperer", "researcher", vector);
        corr.add(entity);
        keywords = "information retrieval|social network|markov chain";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("4", "Alessandro Bovo", "researcher", vector);
        corr.add(entity);
        keywords = "information retrieval|distributed programming|ontology|hermeneutics";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("5", "Fabiano Beppler", "researcher", vector);
        corr.add(entity);
    }

    /**
	 * Second example. 
	 * @param corr
	 * @throws Exception
	 */
    private void composeVectors3rdE(final VectorCorrelation corr) throws Exception {
        String keywords = "Lattes|9|Framework|7|Ensino|6|Libras|5|Língua Portuguesa|4|Surdo|4|Diretório|3|Informática|3|Pesquisa|3|Educação|2|Grupos|2|Java|2|Ajax|1|Business Intelligence|1|Escrita|1|Indicador|1|Ontologia|1|Tomada de Decisão|1|";
        Map<String, Term> terms = Text.generateVector(keywords);
        Vector vector = new Vector(terms);
        Entity entity = new Entity("1", "Entity 1", "researcher", vector);
        corr.add(entity);
        keywords = "CNPq|114|Plataforma Lattes|67|Pós-graduação|49|Diretório dos grupos de pesquisa|38|Governo Eletrônico|37|Avaliação|36|Internet|33|Diretório dos Grupos de Pesquisa no Brasil|32|CAPES|30|Portal Inovação|27|Engenharia Química|18|Universidade|17|Currículo|16|Engenharia do Conhecimento|16|E-Gov|15|Componentes|13|Teses e Dissertações|12|Avaliação em Ciência e Tecnologia|11|Sistemas de Informação|11|XML|11|Banco de Dados|10|Busca Textual|10|Lei de Inovaçào|10|Padronização|10|Plataforma Stela|10|Colaboração Empresas - Especialistas|9|Sistema Stela|9|Avaliação Institucional|7|Data Warehouse|7|Ensino Superior|7|Gestão de Ciência e Tecnologia|7|INEP|7|Plataforma Genos|7|Rede SCienTI|7|Avaliação Curricular|6|Desenvolvimento de Sistemas|5|Transmissão de Arquivos|5|Administração Pública|4|Arquitetura de sistemas de informação|4|Ciências e tecnologias|4|Plano Tabular|4|Sistemas de Apoio à Decisão|4|Administração Universitária|3|Currículo Eletrônico|3|Engenharia de Sistemas|3|Fundos Setoriais|3|Plataforma Coleta-DataCAPES|3|Sql|3|Administração de Capital de Giro|2|Análise de Projetos|2|Avaliação de Ciência & Tecnologia|2|Delphi|2|Desenvolvimento Web|2|Financial Systems|2|Java|2|Linguagem Natural|2|Portal Web|2|Teoria da Decisão|2|Treatment of Uncertainty|2|Ajax|1|Cooperação internacional|1|Currículum|1|Extrator|1|Framework|1|Fuzzy Logic|1|Gestão de Ciência & Tecnologia|1|Inteligência Artificial|1|Nutrição animal|1|Plataforma CvLAC|1|Programação Linear|1|Programação Orientada a Objetos|1|Project Management|1|Prolog|1|Redes de colaboração|1|Sistemas Especialistas|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("2", "Entity 2", "researcher", vector);
        corr.add(entity);
        keywords = "CNPq|152|Plataforma Lattes|145|Tecnologia|143|Pos-Graduação|74|Sistemas de Informação|65|Governo|64|Ciência e Tecnologia|63|Internet|60|Governo Eletrônico|58|Diretório dos Grupos de Pesquisa no Brasil|42|Organizações|42|Universidade|42|Web|39|CAPES|38|Data Warehouse|38|Trabalho|36|Rede ScienTI|31|Arquitetura de sistemas de informação|30|Gestão do Conhecimento|25|Information Systems|24|Innovation|24|Tecnologia da informação|24|Banco de Dados|23|Padronização|23|Teses e Dissertações|23|Fuzzy Logic|22|Data Mining|21|Engenharia Química|21|Grupo Stela|20|Gestão de Ciência e Tecnologia|18|Plataforma Stela|18|Portal Inovação|18|Framework|17|Planejamento|17|Avaliação em Ciência e Tecnologia|16|Qualidade|16|Setor Público|16|Interoperability|15|Administração Pública|14|Conteúdo|14|Sistemas Operacionais|14|Peer review|13|Plataforma Lattes Institucional|12|Theses and dissertations|12|Conjuntos Difusos|11|Plataforma Genos|11|Rock|11|Virtual Organizations|11|XML|11|Administração Universitária|10|Busca Textual|10|Case-based Reasoning|10|Descoberta de Conhecimento|10|Inteligência Artificial|10|Java|10|Lattes Platform|10|Mercosul|10|Web Services|10|Desenvolvimento de Sistemas|9|Digital libraries|9|E-government|9|Engenharia do conhecimento|9|Financial Systems|9|Fuzzy Sets|9|Gestão da informação|9|Hybrid Systems|9|Información|9|Integração de sistemas|9|Intelligent Systems|9|Avaliação Institucional|8|Bibliotecas digitais|8|Cooperation|8|Energia|8|Graduate Program|8|ISO 14000|8|Institutos de pesquisa|8|Knowledge management|8|Portais corporativos|8|Sistemas de Apoio à Decisão|8|Avaliação Curricular|7|Critical thinking|7|Currículo Lattes|7|Diversidade Cultural|7|Fundos Setoriais|7|Fuzzy Systems|7|Inovação tecnológica|7|Knowledge discovery in database|7|Raciocínio Baseado em Casos|7|SCienTI Network|7|Sistema Nacional de Inovação|7|Sistemas Especialistas|7|Written expression|7|Agentes Inteligentes|6|Capital de giro|6|CommonKADS|6|Consultoria|6|Engenharia de Sistemas|6|Engenharia de Software|6|Fuzzy Set Theory|6|História do Rock|6|Lei de Inovação|6|Product Pricing|6|Reconhecimento de Padrões|6|Rede Clara|6|Redes Neurais Artificiais|6|Subjectivity|6|Unidades de informação|6|Virtual Consulting|6|Colaboração científica|5|Fuzzy Control|5|Information System Architecture|5|Knowledge engineering|5|Knowledge extraction|5|Legal Cases|5|Ontologias|5|Organizações do conhecimento|5|Portales corporativos|5|Recuperação de informação|5|Redes de colaboração|5|Remuneração|5|Semantic Web|5|Teletrabalho|5|Transmissão de Arquivos|5|Videoconferência|5|Virtual university|5|Black Sabbath|4|Ciência da Informação|4|Commuting Languages|4|Cooperação internacional|4|Data Webhouse|4|Diretório de instituições|4|EEG|4|Genetic Algorithms|4|Gerência por processos|4|Gestão Ambiental|4|Indicadores em CT&I|4|Industrial Virtual Enterprises|4|Information Infraestructure|4|Information integration|4|Janis Joplin|4|Learning paradigms|4|Mudança organizacional|4|Neural networks|4|Orientação a objetos|4|Planejamento de Tecnologia da Informação|4|Plano Tabular|4|Plataforma Coleta-DataCAPES|4|Redes de pesquisa|4|Sistema de gestão do conhecimento|4|Sistemas Inteligentes|4|Sofware livre|4|Textual templates|4|Virtual stores|4|Web Semântica|4|Agent technology|3|Algorítimos Genéticos|3|Biblioteconomia|3|Bureaucracy|3|Clustering|3|Complexidade|3|Computação invisível|3|Comércio eletrônico|3|Consciousness|3|Cooperação universidade-empresa|3|Currículo Eletrônico|3|E-gov|3|Elis Regina|3|Expert systems|3|Extração de conhecimento|3|Gestão universitária|3|Information extraction|3|Instituto Stela|3|Limitação sensorial|3|Lynyrd Skynyrd|3|Machine learning|3|Organizações Virtuais|3|PPA|3|Pricing Evaluation|3|Processo Térmico|3|Regressão Linear|3|Research information systems|3|Route Planning|3|SNI (Sistema Nacional de Inovação)|3|Social Network Analysis|3|Text mining|3|Workflow|3|Análise de Projetos|2|Baldwin's effect|2|Clara Network|2|Collaboration networks|2|Competency management|2|Comunidade Virtual|2|Controlador PID|2|Correlation methods|2|Desenvolvimento Web|2|Distance edutacion|2|Distance learning|2|Educação médica|2|Electronic commerce|2|Elkans Theorem|2|Explicit Knowledge|2|Fuzzy System Learning|2|Gestão da informação em Ciência e Tecnologia|2|Incerteza|2|Information quality|2|Information retrieval|2|Informetria|2|Integração de informações|2|Interoperabilidade semântica|2|LRD (Latent Relation Discovery)|2|Link Analysis|2|Metadados|2|Organizational Theory|2|Portal Web|2|Previsão de demanda|2|Prisoners Dilemma|2|Processo Decisório|2|Professional responsability|2|Programação Orientada a Objetos|2|Projeto Genos|2|Psychoanalysis|2|Qualidade de serviço|2|Redução de Dimensionalidade|2|Regras de associação|2|Science and technology information|2|Sistemas Operacionais Baseados em Conhecimento|2|Sistemas Operacionais Baseados em Contexto|2|Surdez|2|Tecnologias da Informação e da comunicação|2|Teoria da Decisão|2|UML|2|Agent-based modelling|1|Análise de redes sociais|1|Aprendizado de Máquina|1|Aproximate Reasoning|1|Aproximação universidade-empresa|1|Arquivos abertos|1|Banco de dados orientado a objetos|1|Bibliometria|1|CERIF (Common European Research Information Format|1|Ciência em Rede|1|Clickstream|1|Cluster analisys|1|Cognição|1|E-LOTOS|1|Educação especial|1|Electronic Theses and Dissertations|1|Eletronic Government|1|Equivalence Classes|1|Espaços inteligentes|1|Expertise location|1|Extrator|1|Fatores de certeza|1|Formal Logic|1|Framework para aplicações|1|Fuzzy Data Bases|1|Gerenciamento da informação|1|Gestão de Tecnologia da Informação|1|Gestão de conteúdos|1|Gestão por competências|1|Indexação Recursiva|1|Information systems modeling|1|Intelligent Agents|1|Intelligent Teaching Environment|1|Intercâmbio e compartilhamento de informações|1|Jurisprudência|1|Knowledge Manegment|1|Linguagens de marcação|1|Membership Function Generation|1|Meta-modelo de dados|1|Modelo de Sistemas de Informações|1|Monitoramento Ambiental|1|NIS (National Innovation System)|1|NIT (Núcleo de Inovação Tecnológica)|1|Object-Oriented Programming|1|Online information services|1|Ontology-based context vector|1|Organizational Learning|1|Organizações de conhecimento|1|Planos Governamentais|1|Portal corporativo|1|Privatisation|1|Probability Theory|1|Processamento de Sinal|1|Processos gerenciais|1|Psicologia Organizacional|1|Raciocínio Aproximado|1|Redes acadêmicas de pesquisa|1|Reengenharia de Software|1|Relação universidade-empresa|1|Rule discovering|1|Science and technology evaluation|1|Self-organizing maps (SOM)|1|Semantic similarity|1|Sistemas Difusos|1|Sistemas Educacionais|1|Sistemas Multiagentes|1|Start-up|1|Statistical Control|1|Tacit Knowledge|1|Telecommunication|1|Text Retrieval|1|Theorem Proving|1|Tratamento de incerteza|1|Universal Networking Language|1|Virtual reality|1|Linguagem C++|0|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("3", "Entity 3", "researcher", vector);
        corr.add(entity);
        keywords = "Ciência & Tecnologia|41|CNPq|40|Diretório dos Grupos de Pesquisa no Brasil|34|Banco de Dados|22|Plataforma Lattes|22|Internet|21|Avaliação de Ciência & Tecnologia|20|Data Warehouse|14|Gestão em Ciência & Tecnologia|12|Sistemas de Informação|9|Avaliação Curricular|7|Avaliação Institucional|7|Busca Textual|6|Sistemas de Apoio à Decisão|6|Administração Pública|4|Arquitetura de sistemas de informações|4|Conjuntos Difusos|4|Data Mining|4|Descoberta de Conhecimento|4|Educação|4|Sistema Stela|4|Sistemas Especialistas|4|Algorítimos Genéticos|3|Balanciamento de Dieta|3|Modelagem e Sistemas|3|Processo Térmico|3|Redes Neurais|3|Sistemas para WEB|3|Análise de Projetos|2|Controlador PID|2|Engenharia de Sistemas|2|Programação Linear|2|Route Planning|2|Sistemas tutorais inteligêntes|2|Analise de clusters|1|Aquisição do conhecimento|1|Business Intelligence|1|Controle difuso|1|Machine learning|1|Modelos Adaptativos|1|OLAP|1|Previsão de carga de energia|1|Raciocínio Baseado em Casos|1|Reconhecimento de Padrões|1|Rede neural com função de base radial|1|Redes neurais artificiais|1|Self-organizing maps (SOM)|1|Sistemas Híbridos|1|Teoria da Decisão|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("4", "Entity 4", "researcher", vector);
        corr.add(entity);
        keywords = "a|664|Bancos de dados|79|Plataforma Lattes|45|Revisão pelos pares|45|IDEF1X|33|Portal Inovação|30|Modelagem da informação|28|Aprendizagem|27|Gestão do Conhecimento|26|Sistemas de informação|25|CNPq|24|Projeto de banco de dados|23|Ciência da Computação|21|Gerência de bancos de dados|19|Grupos de pesquisa|19|Peer review|18|Rede ScienTI|18|c|18|Iniciação científica|17|Linux|17|Metadados|17|Governo eletrônico|15|Integração da informação|15|Currículo Lattes|13|Educação a distância|13|Instituto Stela|13|Internet|13|SQL|13|Bibliotecas digitais|12|Engenharia de produção|12|Divulgação científica|11|Engenharia do Conhecimento|11|Ontologias|11|Educação em Ciência da Computação|10|Information modeling|10|Integração de sistemas|10|Interoperability|10|Planejamento|10|Provão|10|Pós-graduação|10|Inovação tecnológica|9|Written expression|9|XML (eXtended Markup Language)|9|b|9|Algoritmos genéticos|8|Alocação|8|Engenharia de requisitos|8|Information integration|8|STEP (Std. for The Exchange of Product model data)|8|Theses and dissertations|8|Treinamento|8|BTD (Banco de Teses e Dissertações)|7|Gestão de C&T|7|Grupo Stela|7|IDEFobject|7|Informática na educação|7|Inteligência artificial|7|Interoperabilidade|7|Lei de Inovação|7|Programação da produção|7|e-gov|7|Aprendizaje|6|Bancos de dados relacionais|6|Caminhos duais|6|Colaboração|6|Crítica|6|Digital libraries|6|Intercâmbio e compartilhamento de informações|6|Knowledge management|6|MIC (Núcleo de Mídia Científica)|6|Modelagem de dados|6|SGBD (Sistemas de Gerência de Bancos de Dados)|6|SNI (Sistema Nacional de Inovação)|6|Tecnologia da informação|6|Web semântica|6|Abstração de dados|5|Arquitetura de sistemas de informação|5|Atitudes|5|Ativos de conhecimento|5|Bibliometry|5|Concurrency control|5|Control de concurrencia|5|Critical thinking|5|Data warehousing|5|ENC (Exame Nacional de Cursos)|5|EXPRESS|5|Industrial virtual enterprises|5|Informetry|5|Institutos de pesquisa|5|Intercâmbio de dados de produtos|5|Modelo Entidade-Relacionamento|5|Modelo interno de dados|5|Professional responsibility|5|Qualidade da informação|5|Responsabilidade profissional|5|TCP/IP|5|Teletrabalho|5|Alocação de recursos|4|Bibliotecas virtuais|4|CAD/CAM (Computer-Aided Design and Manufacturing)|4|Collaborative Learning|4|Computers in education|4|Conjuntos difusos|4|Database management|4|Diagnóstico|4|E-mail|4|Educação em Engenharia|4|Engineering databases|4|Integração de dados|4|Interoperabilidade semântica|4|Jornalismo científico|4|Learning paradigms|4|Metodologia científica|4|Modelo relacional|4|Organizações do conhecimento|4|Postgres|4|Programação linear|4|Propagação de restrições|4|Proyecto de banco de datos|4|Textual templates|4|World Wide Web|4|Avaliação da aprendizagem|3|Bancos de dados orientados a objeto|3|Bancos de dados para Engenharia|3|C&T (Ciência e Tecnologia)|3|CSCL (Computer Supported Collaborative Learning)|3|Capacidade cognitiva|3|Clustering|3|Colaboração científica|3|CommonKADS|3|Compiladores|3|Cooperação internacional|3|Cooperação tecnológica|3|Distance learning|3|Empresas virtuais|3|Engenharia de software|3|Geração automática de texto|3|Gestão governamental|3|Gestão por competências|3|Information extraction|3|Information retrieval|3|Intercâmbio de informações|3|Knowledge extraction|3|Linguagens formais|3|Link analysis|3|Machine learning|3|Marcação retórica|3|Modelagem de problemas|3|Modelo Entidad-Relacionamiento|3|Modelo físico de dados|3|Object-oriented databases|3|Pensamento crítico|3|Plataformas de governo eletrônico|3|Propagación de restricciones|3|Redes de pesquisa|3|Research information systems|3|SDAI (Standard Data Access Interface)|3|Software livre|3|Teoria geral dos sistemas|3|Text mining|3|Administração universitária|2|Aproximação universidade-empresa|2|Barreira da língua|2|Bens intangíveis|2|CGEE (Centro de Gestão e Estudos Estratégicos)|2|Capital intelectual|2|Cluster analysis|2|Collaboration networks|2|Controle de concorrência|2|Correlation methods|2|Curricula|2|Data mining|2|Database design|2|Diretório dos Grupos de Pesquisa no Brasil|2|Educação médica|2|Engineering education|2|Gestão da informação|2|Gestão de ativos de conhecimento|2|Grafos|2|IFIP (Internatl Fed for Information Processing)|2|Information quality|2|Information systems architecture|2|Information technology|2|Informetria|2|Inovações educacionais|2|Jogos educativos|2|LRD (Latent Relation Discovery)|2|Lenguaje natural|2|Metamodelo relacional|2|NIT (Núcleo de Inovação Tecnológica)|2|Neural networks|2|Normalização|2|Ontology-based context vectors|2|Perda semântica|2|Reengenharia de sistemas|2|Regras de negócios|2|Relação universidade-empresa|2|ScienTI Network|2|Social Network Analysis|2|TIC (Tecnologia da Informação e da Comunicação)|2|UML (Unified Modeling Language)|2|UNL (Universal Networking Language)|2|ABIPTI (Assoc Bras Instituições Pesq Tecnológica)|1|AI (Artificial Intelligence)|1|AP (Application Protocol)|1|Análise de redes sociais|1|Aprendizagem apoiada por computador|1|Arquivos abertos|1|Autômatos finitos|1|Bibliometria|1|CAD (Computer-Aided Design)|1|CERIF (Common European Research Information Format|1|CORBA (Common Object Request Broker Architecture)|1|CSCW (Computer-Supported Cooperative Work)|1|Compreensão pública da ciência|1|Computação evolutiva|1|Comunicação científica|1|Constructivismo|1|Cooperative Learning|1|DFM (Design for Manufacturing)|1|Dependência funcional|1|Desenvolvimento de software|1|Digital divide|1|Diseño gráfico|1|E-learning|1|EGC (PPG Engenharia e Gestão do Conhecimento/UFSC)|1|Educational applications|1|Educational games|1|Entity-Relationship modeling|1|Estratégias de ensino|1|FAPESC (Fund. Apoio Pesq. C&T Estado de SC)|1|Formas normais|1|Fuzzy logic|1|Gerencia de bancos de datos|1|Gestão de teletrabalho|1|IR (Integrated Resources)|1|Identidade de objeto|1|Indicadores de C&T|1|Indicadores de CT&I|1|Infoexclusão|1|Information systems modeling|1|Juegos educativos|1|Knowledge discovery in databases|1|Knowledge engineering|1|Lattes Platform|1|Learning environments|1|Linguagem gráfica de modelagem da informação|1|Linguagens de marcação|1|MUD (Modelo Universal de Dados)|1|Modelagem para flexibilidade|1|Multidisciplinaridade|1|Máquinas de estados|1|Mídia e Conhecimento|1|NIS (National Innovation System)|1|Online information services|1|Orientação a objeto|1|Otimização multicritérios|1|PDE (Product data exchange)|1|PDES|1|Paradigmas de aprendizagem|1|Portal Sinaes|1|Problemas intensivos em conhecimento|1|Procesamiento de transacciones|1|Projeto pedagógico|1|Redes conexionistas|1|Refereeing|1|Relational databases|1|Resistência a mudança|1|Restrições de caminho|1|SOM (Self-organizing maps)|1|Science and tecnology evaluation|1|Scientific methodology|1|Scientific writing|1|Semantic similarity|1|Sistemas distribuídos|1|Sociedade da informação|1|Start-up|1|TCC (Trabalho de Conclusão de Curso)|1|Tolerancing|1|Trabalhadores do conhecimento|1|Trabalho colaborativo|1|Trabalho cooperativo|1|Transaction processing|1|Webometria|1|Ética e responsabilidade profissional|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("5", "Entity 5", "researcher", vector);
        corr.add(entity);
        keywords = "Inovação|8|Gestão Acadêmica|7|Arquitetura de Sistemas Web|5|Sistema de Ensino Inteligente|3|marcação retórica|3|CMMI|2|Desenvolvimento de Requisitos|2|Interação Universidade-Empresa|2|Rich Internet Applications|2|Scrum|2|framework|2|Arquitetura de Software|1|Desenvolvimento Baseado em Componentes|1|Ensino a Distância|1|Extração de Conteúdo|1|Extreme Programming|1|Indexação|1|Integração de Software|1|Método Ágil|1|Padrões de Projeto|1|Processo de Software|1|Sistema Curricular|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("6", "Entity 6", "researcher", vector);
        corr.add(entity);
        keywords = "Gestão do conhecimento|13|Grupos de pesquisa|10|Portais corporativos|7|Portales corporativos|5|Contenido|4|Framework Struts|4|Gestão de Conteúdo|4|Plataforma SIGA|4|Currículo Lattes|3|Desenvolvimento Web|3|Framework de aplicações|3|Framework para aplicações|3|Governo Eletrônico|3|Interação|3|Sistema de Publicação|3|Capacitação tecnológica|2|Competências e conhecimento organizacional|2|Internet|2|Mapeamento de processos|2|Padrão Model-View-Controller|2|Profissionais do conhecimento|2|Reconhecimento de padrões|2|Remuneração|2|Sistemas Web|2|Administração|1|Ciência & Tecnologia|1|Ciência da Computação|1|Colaboração|1|Comunicação institucional|1|Disseminação da Informação|1|E-Commerce|1|Empenho e recompensa|1|Engenharia de Software|1|Engenharia do conhecimento|1|Extensão universitária|1|Gerenciamento da informação|1|Gestão de Projetos|1|Gestão de competências|1|Gestão de conhecimento|1|Inclusão digital|1|Información|1|Integração da informação|1|Learning Vector Quantization|1|Matriz Esforço Recompensa|1|Modelo de gestão administrativa|1|Procedimentos e normas|1|Qualidade na prestação dos serviços|1|Relacionamento com o cliente|1|Relação Universidade-Empresa|1|Sistemas de Informação|1|Tecnologia da Informação|1|Portal Web, Inovação|0|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("7", "Entity 7", "researcher", vector);
        corr.add(entity);
        keywords = "bases de datos|37|scienti|14|metadata|13|sql|13|bases de datos relacionales|11|sql server|10|Sistema de Información|7|neptunia|7|seguimiento de clientes|7|navegador|6|informática médica|5|vicuoptimizer|5|MigraT|4|WABS|4|activos fijos|4|herramientas de productividad|4|modelado|4|ABAS|3|broker|3|depreciación|3|guiché|3|hospital peruano|3|metamodelos|3|multisql|3|navegador de objetos|3|oracle|3|pucp|3|reportes gerenciales|3|sistema de remuneración variable|3|vicudb|3|ERP Naviero|2|ISS|2|SCLD|2|Virtu@l Sport|2|WTS|2|federación de bases de datos|2|generación de horarios|2|gestión de la tecnología en salud|2|informatización de la gestión de salud|2|ingeniería clínica|2|metadatos|2|modelo de clases|2|objetos de bases de datos relacionales|2|optimizador de bases de datos|2|sclt|2|sistema de almacenes|2|sistema de compras|2|sistema de información hotelero|2|sistema de mensajería|2|trabajos en curso|2|CIE-10|1|Reserva de Tickets por Internet|1|Sistema de Información Web|1|algoritmos heurísticos|1|carga temporal|1|control de pedidos por Internet|1|costeo promedio|1|gestión automatizada de inventarios|1|gestión de la información hospitalaria|1|gráficos por computadora|1|hospitales peruanos|1|lenguaje C|1|metodología de pagos|1|migración de procedimientos almacenados|1|modelo de datos para la salud|1|modelos genéricos|1|multibases de datos|1|optimización de bases de datos|1|optimizadores de bases de datos|1|portal Web|1|rotación de solidos en 3D|1|sistema de seguimiento de clientes|1|sistema de seguimiento de obras|1|site de curricula|1|site de grupos|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("8", "Entity 8", "researcher", vector);
        corr.add(entity);
        keywords = "Inteligência Artificial|48|Tecnologia da Informação|43|Sistemas Baseados em Conhecimento|29|Engenharia do Conhecimento|28|Governo Eletrônico|23|Raciocínio Baseado em Casos|21|Direito|20|Gestão do Conhecimento|15|Inteligência Artificial e Direito|14|XML|12|UNL|10|Administração Pública|9|Protocolo de Comunicação|9|Agentes Inteligentes|4|DTD|4|Engenharia de Sistemas|3|Pesquisa Contextual Estruturada|3|Metadados|2|Reengenharia de Software|2|Representação do Conhecimento|2|Sistemas Especialistas|2|Comércio Eletrônico|1|Conselho de Segurança da ONU|1|Jurisprudência|1|Rede de Relacionamentos|1|Teoria da Argumentação Jurídica|1|Text minig|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("9", "Entity 9", "researcher", vector);
        corr.add(entity);
        keywords = "Avaliação|72|Universidade|37|Avaliação Institucional|32|EDUCACAO SUPERIOR|31|Graduação|16|JOHN UPDIKE|12|LITERATURA NORTEAMERICANA CONTEMPORANEA|9|Provão|9|Pesquisa|8|Brazil|6|EXPRESSAO ESCRITA|5|ANOS TRINTA|3|Gratuidade|3|SAUL BELLOW|3|BEING THERE|2|Ensino Pago|2|Ensino Superior|2|FORD MADOX FORD|2|GOOD SOLDIER|2|GROUND ZERO|2|HOLLOW MEN|2|IDENTIDADE AMERICANA|2|INVISIBLE GENERATION|2|JERZY KOSINSKI|2|JOHN BENNET|2|JOHN PLYMELL|2|LAST OF THE MOCASINS|2|LEE WALLEK|2|LITERATURA INGLESA|2|MICHAEL GOLD|2|REDACAO|2|RETORICA|2|THOMAS JOHNSON|2|ULYSSES|2|URBAN EXPERIENCE|2|WHITMAN|2|literatura Norte Americana Contemporânea|2|Administração Universitária|1|Alice Walker|1|Capacitação Docente|1|ELLEN QUANDAHL|1|EMERSON|1|Ensino de Graduação|1|GRANDE DEPRESSAO|1|INGLES INSTRUMENTAL|1|JAMES JOYCE|1|JEWISH-AMERICAN LITERATURE|1|JOSEPH CONRAD|1|LITERATURA AMERICANA DOS ANOS TRINTA|1|LITERATURA INGLESA DO SECULO XX|1|LITERATURA IRLANDESA|1|LITERATURA NORTEAMERICANA DOS ANOS TRINTA|1|OFICINAS DE EXPRESSAO ESCRITA|1|PAIUB|1|PATRICIA DONAHUE|1|PEDAGOGIA E RETORICA|1|Philip Roth|1|RABBIT TETRALOGY|1|Relatório Boyer|1|SELECAO DE TEXTOS|1|THE COUP|1|colonialismo cultural|1|identidade nacional|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms);
        entity = new Entity("10", "Entity 10", "researcher", vector);
        corr.add(entity);
    }

    /**
	 * First example. 
	 * @param corr
	 * @throws Exception
	 */
    private void composeVectors4thE(final VectorCorrelation corr) throws Exception {
        String keywords = "Inovação|65|Desenvolvimento|28|Geografia|24|Recife|20|Tecnologia|14|sistema de inovação|13|Espaço|12|conhecimento|12|Optoeletrônica|11|Pernambuco|11|Região|10|cluster|10|Desenvolvimento regional|9|Nordeste|8|Globalização|7|Inovação Tecnológica|7|territorio|7|Geografia da inovação|6|Espaço Geográfico|5|Lugar|5|Rede Urbana|5|Água|5|Cana-de-açúcar|4|Ciência Geográfica|4|Geomorfologia|4|Geoprocessamento|4|Organização|4|Pólo Médico do Recife|4|Pós-modernidade|4|aprendizagem|4|Análise ritímica|3|Aquicultura|3|Desenvolvimento local|3|Planejamento Territorial|3|RMR|3|ação antrópica|3|escala geografica|3|mapeamento|3|monitoria|3|sistema territorial de inovacao|3|áreas degradadas|3|ASACE|2|Captação/manejo de água|2|Centros Rurais|2|Cidades médias|2|Climatologia Dinâmica|2|Desenvolvimento sustentável|2|Economia do conhecimento|2|Espaço Agrário|2|Funcionalidade|2|GPS (Sistema de Posicionamento Global)|2|Geografia Humana|2|Geografia dos serviços|2|Gestão de bacia|2|Mapa de risco|2|Oceonografia geral|2|Organização espacial|2|Ouricuri-PE|2|Pequenas Cidades|2|População ribeirinha|2|Região periférica|2|Teoria geografica|2|cidadania|2|difusão|2|indicadores de ciência|2|indicadores de inovacao|2|monitoramento|2|sistema Regional de Inovação|2|tecnologia social|2|Autogestão|1|Cooperativa|1|Desenvolvimento Territorial|1|Desenvolvimento econômico|1|Desenvolvimento rural|1|Desparidades regionais|1|Dinâmica espacial|1|Disparidade econômica|1|Distribuição territorial|1|Domínios do Cerrado|1|Engenho|1|Extensão|1|Geografia do turismo|1|Gestão de bacia hidrográfica|1|Gestão territorial|1|Grau de saturação|1|Hierarquia Urbana|1|Impactos ambientais|1|Leitura da paisagem cultural|1|Mercado de trabalho|1|Relação universidade - empresa|1|SIG|1|Sensoriamento Remoto|1|Setor de serviços de saúde|1|Territorialidade|1|Usina|1|análise local-regional|1|fluxo de conhecimento|1|informação|1|redes de cooperação|1|redes socais|1|redes sociais|1|transferência|1|";
        Map<String, Term> terms = Text.generateVector(keywords);
        Vector vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        Entity entity = new Entity("1", "José Geraldo Pimentel Neto", "researcher", vector);
        corr.add(entity);
        keywords = "Aglomerações de empresas|117|Inovação|108|São Paulo|60|Indústria de Calçados|52|Política Industrial|48|Indústria Têxtil|36|Competitividade industrial|29|Metodologia de análise regional|27|Franca|25|Estratégias empresariais|24|Pequenas e Médias Empresas|24|Cadeias produtivas globais|23|Indústria Petroquímica|22|Organização Industrial|19|Economia Industrial|17|Reestruturação Industrial|17|Indústria Cerâmica|15|Indústria de Cosméticos|15|Indústria de Móveis|14|Interação Universidade-Empresa|14|Economia brasileira|13|Cadeia Produtiva|12|Design|12|Complexo eletrônico|10|Desenvolvimento Regional|10|Internacionalização|10|Parques Tecnológicos|10|Coordenação e governança|9|Economia Regional|9|Instituições de apoio|9|Região Metropolitana|9|Aprendizado|8|Campinas|8|Indústria do Vestuário|8|Geografia da Inovação|7|Gestão da Tecnologia|7|Indústrias de Alta Tecnologia|7|Santa Gertrudes|7|Conhecimento e inovação|6|Grande ABC|6|Redes de empresas|6|Americana|5|Bento Gonçalves|5|Complexo têxtil-vestuário|5|Cooperação Interfirma|5|Globalização|5|Lei de Informática|5|Abertura Comercial|4|Impactos sócio-econômicos|4|Nova Serrana|4|Sistemas de inovação|4|Administração de empresas|3|Agronegócio|3|Ciência e Tecnologia|3|Crisciuma|3|Experiências industriais|3|Incubadoras de empresas|3|Indústria de Couro|3|Indústria de Semicondutores|3|Paulínia|3|Prospecção Tecnológica|3|Reestruturação Patrimonial|3|Relações interfirmas|3|Varejo|3|Automação comercial|2|Conjuntura Econômica|2|Economia da tecnologia|2|Economia do Trabalho|2|Economia paulista|2|Emiglia-Romagna|2|Empreendedorismo|2|Finanças das empresas|2|Gini locacional|2|Indústria do Plástico|2|Iniciação Científica|2|Instituições públicas e privadas|2|Integração comercial|2|Orçamento Familiar|2|Planejamento Estratégico|2|Setor de Serviços|2|Substituição das Importações|2|Vale do Sílicio|2|Análise da viabilidade de empreendimentos|1|Combate à fome|1|Comércio Externo|1|Desenvolvimento de Produto|1|Economia Internacional|1|Economia Política|1|Economia Solidária|1|Economias e deseconomias de escala|1|Espírito Santo|1|Financiamento da atividade produtiva|1|Indicadores de C,T&I|1|Industria automobilistica|1|Indústria de Fundição|1|Indústrias de bens de consumo|1|Plano de Negócios|1|Pólos Tecnológicos|1|Sistemas de informação|1|São Bento do Sul|1|Transbordamentos tecnológicos|1|Vale do Sinos|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("2", "Renato de Castro Garcia", "researcher", vector);
        corr.add(entity);
        keywords = "Inovação|45|Ciência, Tecnologia & Inovação|15|Diretrizes|8|Fundos Setoriais|8|Governo|8|Arranjos Produtivos Locais|7|Comunidade Científica|6|CT&I|5|Cadeia do Conhecimento|5|Empresas|5|Interação|5|Interação Universidade-Empresa|4|Minas Gerais|4|CEITEC|3|Desenvolvimento Nacional|3|História|3|Impérios|3|Linhas de ação|3|Microeletrônica|3|Objetivos de Desenvolvimento do Milênio|3|Pesquisa e Desenvolvimento|3|Política Nacional de C&T|3|Resultados|3|Belle Époque|2|Conquistas|2|Coréia|2|Don Quijote de la Mancha|2|Herói|2|Idade Média|2|Invenções|2|Miguel de Cervantes|2|Novas Tecnologias de Informação e Comunicação|2|Universidades|2|Átila|2|Costumes sociais|1|Diretrizes, Estratégias e Planejamento|1|Diretrizes, Planejamentos, Propostas e Estratégias|1|Empresas / Setor Produtivo|1|Ensino a Distância|1|Estratégias / Planejamento|1|Estratégias em C&T|1|Estratégias para a Inovação|1|Fundos Setorias|1|Gestão do Conhecimento|1|Hunos|1|Incentivos|1|Instituições de Pesquisa e Desenvolvimento|1|Investimentos|1|Municípios|1|Nanotecnologia|1|Obras literárias|1|Paradigma do Conhecimento|1|Parcerias|1|Planejamento, Desafios e Estratégias|1|Planejamento, diretrizes e estratégias|1|Região Nordeste|1|Região Norte|1|Sociologia da Ciência e do Conhecimento|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("3", "Flávia Maia Jesini", "researcher", vector);
        corr.add(entity);
        keywords = "biotecnologia|87|inovação|78|Empreendedorismo|42|redes|23|Administração|18|Arranjos produtivos locais|12|Clusters|10|Capital de risco|8|Biotecnologia e desenvolvimento|7|indústrias criativas|6|transferencia de tecnologia|6|Gestão de inovação|5|clusters, arranjos produtivos locais|5|estratégia e competitividade|5|setor de Diagnósticos|5|Inserção global|4|novas arquiteturas organizacionais|4|Diretório nacional de empresas de Biotecnologia|3|Estudo de caso de ensino|3|Gestão de pessoas|3|Gestão de tecnologia e inovação|3|Gestão do conhecimento|3|Gestão e Tecnologia|3|MPEs em Biotecnologia|3|incubação de empresas|3|modelos de gestão|3|mudanças e inovações|3|Colaboração|2|Desenvolvimento econômico|2|Estratégia competitiva|2|Florestas sociais|2|Função gerencial|2|Impactos da seca|2|Incubadoras de empresas de Biotecnologia|2|Políticas de Ciência e Tecnologia|2|Políticas internacionais|2|Tecnologias de Informação e Comunicação|2|marketing|2|universidade-empresa|2|Biotecnologia saude humana|1|Cultura bioempresarial brasileira|1|Cultura e comportamento empreendedor|1|Diagnósticos em Saúde Humana, Animal, Vegetal|1|Empreendedor-cientista|1|Estabelecimento de prioridades em biotecnologia|1|Financiamento a incubadoras e empresas incubadas|1|Intervenção governamental no semi-árido nordeste|1|Investimentos venture em Biotecnologia|1|Mercado madeireiro - demanda e oferta|1|Micro e Pequenas Empresas (MPEs)|1|Parques tecnológicos e incubadoras de empresas|1|Recursos humanos em biotecnologia|1|Rede de incubadoras MG|1|Relações de poder e de trabalho|1|financimento empresas de biotecnologia|1|parcerias estratégicas em biotecnologia|1|qualidade vida, estresse,  saúde mental  trabalho|1|sociologia da ciência|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("4", "Valeria Maria Martins Judice", "researcher", vector);
        corr.add(entity);
    }

    /**
	 * Fifth example. 
	 * @param corr
	 * @throws Exception
	 */
    private void composeVectors5thE(final VectorCorrelation corr) throws Exception {
        String keywords = "Inspeção Veicular|9|Controle de Acesso|7|Desenvolvimento de Hardware|7|Desenvolvimento de Firmware|6|Instrumentação Eletrônica|6|Telefônia Digital|6|Computador de Bordo|5|Sistemas Microcontrolados|4|Aplicação Espacial|3|Arquitetura de Computadores|3|Processamento Paralelo|3|Sistemas Embarcados|3|Alarmes e Bloqueadores de Veículos|2|Automação Industrial|2|Automação Residencial|2|Comunicação sem Fio|2|Hardware Tolerante a Falha|2|Processamento Digital de Sinais|2|Sistemas Distribuído|2|Space Applications|2|Tecnologia GPRS|2|Tolerância a Falhas|2|Automação Hospitalar|1|Automação Hoteleira|1|Bioamplificadores|1|Dispositivos Wireless|1|Fault Tolerance|1|Instrumentação Biomédica|1|Máquinas e Equipamentos|1|Mídia Eletrônica|1|Painéis Eletrônicos a LEDS|1|Reliability|1|Sensores Infravermelhos|1|Sistema Tolerante a Falhas|1|Sistemas Operacionais|1|";
        Map<String, Term> terms = Text.generateVector(keywords);
        Vector vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        Entity entity = new Entity("1", "Lucimara Stolz", "researcher", vector);
        corr.add(entity);
        keywords = "Propriedade Intelectual|42|Biodiversidade|2|monitoramento tecnológico|2|patentes nacionais e internacionais|2|bancos de dados internacionais - DialogSTN|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("2", "Paulo Luiz de Andrade Coutinho", "researcher", vector);
        corr.add(entity);
        keywords = "fungos micorrízicos arbusculares|19|Eucalyptus grandis|18|Diferentes Substratos|10|Micorrizas|7|Fossa Séptica|6|recuperação de áreas degradadas|5|Arenização|4|Doses de P|3|recursos naturais|3|fauna edáfica|2|Acácia negra|1|Bactérias Diazotróficas|1|Fixação biológica de N|1|Produção Agropecuária|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("3", "Ivanir Luiz de Oliveira", "researcher", vector);
        corr.add(entity);
        keywords = "Tecnologia da Informação|52|Gestão do Conhecimento|48|INTERFACE|25|Gestão da Tecnologia da Informação|23|Governança de Tencologia da Informação|16|Web|13|ZOOM CONTÍNUO|12|Governaça Corporativa|11|Micro e Pequenas Empresas|10|EAD|9|INTERFACES|9|Hypertext|8|Usabilidade|8|Capital Intelectual|7|Business Intelligence|6|DESORIENTAÇÃO|6|Governança Corporativa|6|Interface Humano-computador|6|Balanced Scorecard|5|Disorientation|5|HIPERTEXTO|5|CMMI|4|Scalable Vector Graphics|4|DESORIENTATION|3|Font Ratio|3|ZOOMING INTERFACE|3|Comunicação Organizacional|2|Cultura Organizacional|2|ZOOM INTERFACE|2|Aparelho Celular|1|Computação móvel|1|DESENVOLVIMENTO ORGANIZACIONAL|1|Educação a Distancia|1|Engenharia de Software|1|Inteligência Competitiva|1|Interface Móvel|1|Modelo Mentais|1|Navegação semantica|1|Qualidade de Software|1|Sisstemas de Informações Gerenciais|1|Tomada de Decisões Inteligentes|1|Web Service|1|Web semantica|1|Website|1|Zoomable interface|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("4", "Vera Maria Cartana Fernandes", "researcher", vector);
        corr.add(entity);
        keywords = "desafio de pesquisadores e empresas|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("5", "Wayne Brod Beskow", "researcher", vector);
        corr.add(entity);
        keywords = "CNS/ATM|19|Comunicação de Dados|13|Controle de Tráfego Aéreo|9|Engenharia de Software|6|Propriedade Intelectual|6|Inovação Tecnológica|5|Lei de Inovação|4|Projeto Soldado Cidadão|4|OMT/UML|3|Power Point|3|Windows|3|Especificação de Requisitos|2|Programação de Computadores|2|Comunicações de Dados|1|Gestão de Inovação|1|Patentes|1|Telecomunicações Aeronáuticas|1|Visual Basic|1|";
        terms = Text.generateVector(keywords);
        vector = new Vector(terms, Equation.NORMALIZING_TYPE.MAX_FREQUENCY);
        entity = new Entity("6", "Juliana Debei Herling", "researcher", vector);
        corr.add(entity);
    }

    @Test
    public void correlate1stE() throws Exception {
        VectorCorrelation corr = new VectorCorrelation();
        this.composeVectors1stE(corr);
        corr.correlate();
        String output = corr.getXML();
        assertEquals(output, expected1stE);
    }

    @Test
    public void correlate2ndE() throws Exception {
        VectorCorrelation corr = new VectorCorrelation();
        this.composeVectors2ndE(corr);
        corr.correlate();
        String output = corr.getXML();
        assertEquals(output, expected2ndE);
    }

    @Test
    public void correlate3rdE() throws Exception {
        VectorCorrelation corr = new VectorCorrelation();
        this.composeVectors3rdE(corr);
        corr.correlate();
        String output = corr.getXML();
        assertEquals(output, expected3rdE);
    }

    @Test
    public void correlate4thE() throws Exception {
        VectorCorrelation corr = new VectorCorrelation();
        this.composeVectors4thE(corr);
        corr.correlate();
        String output = corr.getXML();
        assertEquals(output, expected4thE);
    }

    @Test
    public void correlate5thE() throws Exception {
        VectorCorrelation corr = new VectorCorrelation();
        this.composeVectors5thE(corr);
        corr.correlate();
        String output = corr.getXML();
        assertEquals(output, expected5thE);
    }
}
