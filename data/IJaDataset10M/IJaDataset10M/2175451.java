package test;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.scatter.algorithm.ObjectTCSS;
import com.scatter.model.Solution;
import com.scatter.model.State;
import com.scatter.model.StateMachine;
import com.scatter.model.dataGenerators.BookNamesGenerator;
import com.scatter.model.dataGenerators.CpfGenerator;
import com.scatter.model.dataGenerators.DateTimeGenerator;
import com.scatter.model.dataGenerators.PeopleNameGenerator;
import com.scatter.model.expression.BeanShellExpression;
import com.scatter.model.metamodel.Attribute;
import com.scatter.model.metamodel.BooleanAttribute;
import com.scatter.model.metamodel.Cardinality;
import com.scatter.model.metamodel.Classe;
import com.scatter.model.metamodel.DateAttribute;
import com.scatter.model.metamodel.IntegerAttribute;
import com.scatter.model.metamodel.StringAttribute;
import com.scatter.useCodeGenerator.UseCodeGenerator;

public class ObjectTCSSProcessoTest {

    private Classe pedido = null;

    @Before
    public void setUp() {
        pedido = new Classe("Pedido");
        pedido.addAttribute(new IntegerAttribute("numero"));
        pedido.addAttribute(new DateAttribute("dataEmissao", new DateTimeGenerator(5)));
        pedido.addAttribute(new IntegerAttribute("valorTotal", 900, 1100));
        Classe cliente = new Classe("Cliente");
        cliente.addAttribute(new StringAttribute("nome", new PeopleNameGenerator()));
        cliente.addAttribute(new StringAttribute("cpf", new CpfGenerator()));
        cliente.addAttribute(new DateAttribute("dataNascimento", new DateTimeGenerator(100)));
        cliente.addAttribute(new IntegerAttribute("idade", 5, 50));
        cliente.addAttribute(new BooleanAttribute("blacklisted", 0.5));
        Classe livro = new Classe("Livro");
        livro.addAttribute(new IntegerAttribute("numero"));
        livro.addAttribute(new StringAttribute("titulo", new BookNamesGenerator()));
        livro.addAttribute(new StringAttribute("autor", new PeopleNameGenerator()));
        livro.addAttribute(new IntegerAttribute("valor", 15, 200));
        pedido.addRelationship("consumidor", cliente, Cardinality.ONE);
        pedido.addRelationship("livrosSelecionados", livro, Cardinality.MANY);
    }

    @Test
    public void testObjectTCSS() {
        int refSetCapacity = 10;
        List<Attribute> variables = new ArrayList<Attribute>();
        StateMachine sm = new StateMachine();
        State startNode = sm.createRootState("start");
        State receberPedido = sm.createState("Receber Pedido");
        State verificarCredito = sm.createState("Verificar Cr�dito");
        State creditoOk = sm.createState("Cr�dito Ok");
        State creditoInvalido = sm.createState("Cr�dito inv�lido");
        State verificarPedido = sm.createState("Verificar Valor Pedido");
        State valorAcima = sm.createState("Valor Acima do V�lido");
        State valorAbaixo = sm.createState("Valor Abaixo do V�lido");
        State analisarPedido = sm.createState("Analisar Pedido");
        State PedidoAprovado = sm.createState("Pedido Aprovado pelo Financeiro");
        State PedidoNaoAprovado = sm.createState("Pedido N�o Aprovado pelo Financeiro");
        State selecionarFornecedor = sm.createState("Selecionar Fornecedor Com menor Pre�o");
        State criarPedido = sm.createState("Criar Pedido");
        State notificarCliente = sm.createState("Notificar Cliente");
        State endNode = sm.createState("end");
        sm.createTransition(startNode, receberPedido, null);
        sm.createTransition(receberPedido, verificarCredito, null);
        sm.createTransition(verificarCredito, creditoOk, new BeanShellExpression("Pedido.relsMap.get(\"Cliente\").attrMap.get(\"idade\").getValue() >= 18"));
        sm.createTransition(verificarCredito, creditoInvalido, new BeanShellExpression("Pedido.relsMap.get(\"Cliente\").attrMap.get(\"idade\").getValue() < 18"));
        sm.createTransition(creditoInvalido, endNode, null);
        sm.createTransition(creditoOk, verificarPedido, null);
        sm.createTransition(verificarPedido, valorAcima, new BeanShellExpression("Pedido.attrMap.get(\"valorTotal\").getValue() >= 1000"));
        sm.createTransition(verificarPedido, valorAbaixo, new BeanShellExpression("Pedido.attrMap.get(\"valorTotal\").getValue() < 1000"));
        sm.createTransition(valorAcima, analisarPedido, null);
        sm.createTransition(analisarPedido, PedidoAprovado, new BeanShellExpression("!(Pedido.relsMap.get(\"Cliente\").attrMap.get(\"blacklisted\").getValue())"));
        sm.createTransition(analisarPedido, PedidoNaoAprovado, new BeanShellExpression("Pedido.relsMap.get(\"Cliente\").attrMap.get(\"blacklisted\").getValue()"));
        sm.createTransition(PedidoNaoAprovado, endNode, null);
        sm.createTransition(PedidoAprovado, selecionarFornecedor, null);
        sm.createTransition(valorAbaixo, selecionarFornecedor, null);
        sm.createTransition(selecionarFornecedor, criarPedido, null);
        sm.createTransition(criarPedido, notificarCliente, null);
        sm.createTransition(notificarCliente, endNode, null);
        variables.add(pedido);
        ObjectTCSS objectTCSS = new ObjectTCSS(sm, variables, refSetCapacity, 5000, new Float(0.70), false);
        List<Solution> testCases = objectTCSS.start();
        UseCodeGenerator gen = new UseCodeGenerator();
        gen.generateUseObjects(testCases);
    }
}
