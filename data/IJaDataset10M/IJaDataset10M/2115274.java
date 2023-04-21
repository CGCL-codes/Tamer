package br.com.rmt.simplestgrid.agentes;

import br.com.rmt.ganttprogramming.Log;
import br.com.rmt.simplestgrid.agentes.observadores.ComputadoresObserver;
import br.com.rmt.simplestgrid.agentes.observadores.SistemaObserver;
import br.com.rmt.simplestgrid.modelo.dto.Computador;
import br.com.rmt.simplestgrid.ontologias.EstaCalculandoOntologia;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Agente de controle do Grid na máquina local
 * @since 17/08/2010 23:50
 * @author Ronneesley Moura Teles
 */
public class SimplestGridAgent extends Agent implements SistemaObserver {

    private List<Computador> computadores = new ArrayList<Computador>();

    public static final int PERIODO_NOTIFICACAO = 5000;

    private List<ComputadoresObserver> observadoresComputadores = new ArrayList<ComputadoresObserver>();

    public static String NOME_PLATAFORMA = null;

    private EscutadorMulticast escutadorMulticast = new EscutadorMulticast();

    private NotificadorMulticast notificadorMulticast = new NotificadorMulticast(this, PERIODO_NOTIFICACAO);

    private Codec codec = new SLCodec();

    private Ontology estaCalcundoOntologia = EstaCalculandoOntologia.getInstance();

    public static NetworkInterface interfaceRede;

    public static InetAddress enderecoRede;

    private Computador computadorLocal = null;

    @Override
    protected void setup() {
        this.log(String.format("distribuidor ativado com o AID: %s", this.getAID().getHap()));
        this.getContentManager().registerLanguage(codec);
        this.getContentManager().registerOntology(estaCalcundoOntologia);
        this.addBehaviour(this.escutadorMulticast);
        this.addBehaviour(this.notificadorMulticast);
    }

    public void adicionarComputador(Computador computador) {
        this.computadores.add(computador);
        for (ComputadoresObserver observador : this.observadoresComputadores) observador.novoComputador(computador);
    }

    public void adicionarComputador(String ip, String nomePlataforma, String nomeComputador) {
        Computador computador = new Computador(ip, nomePlataforma);
        computador.setNomeComputador(nomeComputador);
        this.computadores.add(computador);
        for (ComputadoresObserver observador : this.observadoresComputadores) observador.novoComputador(computador);
    }

    public void removerComputador(String ip) {
        Computador computador = null;
        Iterator<Computador> it = computadores.iterator();
        while (it.hasNext()) {
            Computador item = it.next();
            if (item.getIp().equals(ip)) {
                computador = item;
                break;
            }
        }
        if (computador != null) {
            this.computadores.remove(computador);
            for (ComputadoresObserver observador : this.observadoresComputadores) observador.computadorRemovido(computador);
        }
    }

    public boolean temComputador(String ip) {
        Iterator<Computador> it = computadores.iterator();
        while (it.hasNext()) {
            Computador item = it.next();
            if (item.getIp().equals(ip)) {
                return true;
            }
        }
        return false;
    }

    public void adicionarObservadorComputador(ComputadoresObserver observador) {
        this.observadoresComputadores.add(observador);
    }

    public void removerObservadorComputador(ComputadoresObserver observador) {
        this.observadoresComputadores.remove(observador);
    }

    public void sair() {
        this.escutadorMulticast.done();
        this.removeBehaviour(this.escutadorMulticast);
        this.notificadorMulticast.notificacaoSair();
        this.notificadorMulticast.done();
        this.removeBehaviour(this.notificadorMulticast);
        this.log("saindo do grid...");
        System.exit(0);
    }

    public Computador computadorLocal() {
        if (computadorLocal == null) computadorLocal = this.notificadorMulticast.computadorLocal();
        return computadorLocal;
    }

    public List<Computador> getComputadores() {
        return computadores;
    }

    public void setComputadores(List<Computador> computadores) {
        this.computadores = computadores;
    }

    public void log(String mensagem) {
        Computador c = this.computadorLocal();
        Log.log(String.format("Grid IP %s: %s", c.getIp(), mensagem));
    }
}

/**
 * Comportamento básico de Multicast
 * @since 18/08/2010 00:03
 * @author Ronneesley Moura Teles
 */
abstract class ComportamentoMulticast extends SimpleBehaviour {

    public static final String ADICIONAR_COMPUTADOR = "NovoComputador:";

    public static final String REMOVER_COMPUTADOR = "RemoverComputador:";

    public static final String MULTICAST_IP = "224.1.1.1";

    public static final int MULTICAST_PORTA = 10000;

    public static final int TAMANHO_MENSAGEM = 2048;

    public static final boolean log = true;
}

/**
 * Notificador que o computador faz parte do Grid
 * @since 18/08/2010 00:04
 * @author Ronneesley Moura Teles
 */
class NotificadorMulticast extends TickerBehaviour {

    private SimplestGridAgent getAgente() {
        return (SimplestGridAgent) this.myAgent;
    }

    public NotificadorMulticast(Agent a, long period) {
        super(a, period);
    }

    public Computador computadorLocal() {
        InetAddress inet = SimplestGridAgent.enderecoRede;
        String ip = inet.getHostAddress();
        Computador c = new Computador();
        c.setIp(ip);
        c.setPlataformaId(SimplestGridAgent.NOME_PLATAFORMA);
        c.setNomeComputador(inet.getHostName());
        c.setSO(System.getProperty("os.name"));
        c.setVersaoSO(System.getProperty("os.version"));
        c.setQuantidadeProcessadores(Runtime.getRuntime().availableProcessors());
        c.setMaximoMemoria(Runtime.getRuntime().maxMemory());
        c.setTotalMemoria(Runtime.getRuntime().totalMemory());
        c.setVersaoJava(System.getProperty("java.version"));
        c.setVendedorJava(System.getProperty("java.vendor"));
        c.setArquitetura(System.getProperty("os.arch"));
        return c;
    }

    @Override
    protected void onTick() {
        try {
            InetAddress endereco = InetAddress.getByName(ComportamentoMulticast.MULTICAST_IP);
            MulticastSocket mSocket = new MulticastSocket();
            mSocket.joinGroup(endereco);
            Computador c = this.computadorLocal();
            byte dados[] = (ComportamentoMulticast.ADICIONAR_COMPUTADOR + c.serializar()).getBytes();
            DatagramPacket dtPk = new DatagramPacket(dados, dados.length, endereco, ComportamentoMulticast.MULTICAST_PORTA);
            mSocket.send(dtPk);
        } catch (IOException ex) {
            this.getAgente().log(String.format("ocorreu um erro: %s", ex.getMessage()));
        }
    }

    public void notificacaoSair() {
        try {
            getAgente().log("preparando mensagem de notificação de saída");
            InetAddress endereco = InetAddress.getByName(ComportamentoMulticast.MULTICAST_IP);
            MulticastSocket mSocket = new MulticastSocket();
            mSocket.joinGroup(endereco);
            String ip = SimplestGridAgent.enderecoRede.getHostAddress();
            byte dados[] = (ComportamentoMulticast.REMOVER_COMPUTADOR + ip).getBytes();
            DatagramPacket dtPk = new DatagramPacket(dados, dados.length, endereco, ComportamentoMulticast.MULTICAST_PORTA);
            mSocket.send(dtPk);
            getAgente().log("notificação de saída envidada");
        } catch (IOException ex) {
            this.getAgente().log(String.format("ocorreu um erro: %s", ex.getMessage()));
        }
    }
}

/**
 * Comportamento de descoberta de novos membros da Grid
 * @since 17/08/2010 23:00
 * @author Ronneesley Moura Teles
 */
class EscutadorMulticast extends ComportamentoMulticast {

    private SimplestGridAgent getAgente() {
        return (SimplestGridAgent) this.myAgent;
    }

    @Override
    public void action() {
        Escutar escutar = new Escutar(this.getAgente());
        escutar.start();
    }

    @Override
    public boolean done() {
        return true;
    }
}

class Escutar extends Thread {

    private SimplestGridAgent agente;

    public Escutar(SimplestGridAgent agente) {
        this.agente = agente;
    }

    @Override
    public void run() {
        try {
            InetAddress endereco = InetAddress.getByName(ComportamentoMulticast.MULTICAST_IP);
            this.agente.log(String.format("endereço multicast: %s, porta: %s", endereco.getHostAddress(), ComportamentoMulticast.MULTICAST_PORTA));
            MulticastSocket mSocket = new MulticastSocket(ComportamentoMulticast.MULTICAST_PORTA);
            mSocket.joinGroup(endereco);
            while (true) {
                byte dados[] = new byte[ComportamentoMulticast.TAMANHO_MENSAGEM];
                DatagramPacket dtPk = new DatagramPacket(dados, dados.length);
                mSocket.receive(dtPk);
                String mensagem = new String(dados);
                if (mensagem.startsWith(ComportamentoMulticast.ADICIONAR_COMPUTADOR)) {
                    String msg = mensagem.substring(ComportamentoMulticast.ADICIONAR_COMPUTADOR.length(), mensagem.length());
                    Computador c = Computador.lerDados(msg);
                    if (!agente.temComputador(c.getIp())) {
                        if (!SimplestGridAgent.enderecoRede.getHostAddress().equals(c.getIp())) {
                            this.agente.log(String.format("o computador de IP %s entrou no Grid", c.getIp()));
                            agente.adicionarComputador(c);
                        }
                    }
                } else if (mensagem.startsWith(ComportamentoMulticast.REMOVER_COMPUTADOR)) {
                    String ip = mensagem.substring(ComportamentoMulticast.REMOVER_COMPUTADOR.length(), mensagem.length());
                    ip = ip.trim();
                    if (agente.temComputador(ip)) {
                        this.agente.log(String.format("o computador de IP %s saiu do grid", ip));
                        agente.removerComputador(ip);
                    }
                }
            }
        } catch (IOException ex) {
            this.agente.log(String.format("ocorreu um erro: %s", ex.getMessage()));
        }
    }
}
