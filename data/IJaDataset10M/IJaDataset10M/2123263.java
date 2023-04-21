package bacoopc;

import bacoopc.excecoes.ExcecaoConexaoOPC;
import java.util.SortedMap;
import java.util.TreeMap;
import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.exception.SynchWriteException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.variant.Variant;

/**
 * Classe que encapsula a comunicação com um servidor OPC. Útil para ler as
 * variáveis provindas de um servidor e para a escrita de variáveis modificadas
 * por um controlador.
 *
 * @author Baco Sistemas
 */
public class ConexaoOPC implements Comparable<ConexaoOPC> {

    /** Nome do cadastro de um servidor OPC */
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private String nome;

    private JEasyOpc jopc;

    private OpcGroup grupoPadrão;

    private SortedMap<String, OpcItem> variáveis;

    static {
        JEasyOpc.coInitialize();
    }

    /**
     * Finaliza a biblioteca JEasyOPC.
     * Deve ser chamado no fim da execução do programa.
     */
    public static void finalizar() {
        JEasyOpc.coUninitialize();
    }

    public ConexaoOPC(String host, String nome) throws ExcecaoConexaoOPC {
        this.host = host;
        this.nome = nome;
        this.jopc = new JEasyOpc(host, nome, "JOPC" + Math.random());
        grupoPadrão = new OpcGroup("grupo", true, 100, 0f);
        variáveis = new TreeMap<String, OpcItem>();
    }

    @Override
    public void finalize() {
        if (jopc.isRunning()) jopc.terminate();
    }

    /**
     * Conecta ao servidor OPC e executa o cliente.
     * @param jopc
     */
    public void conectar() {
        try {
            if (jopc.getGroupsAsArray().length > 0) jopc.removeGroup(grupoPadrão);
            jopc.addGroup(grupoPadrão);
            jopc.connect();
            jopc.registerGroups();
        } catch (UnableAddItemException e) {
            e.printStackTrace();
        } catch (UnableAddGroupException e) {
            e.printStackTrace();
        } catch (ConnectivityException e) {
            e.printStackTrace();
        }
    }

    public void desconectar() {
        jopc.terminate();
    }

    public void adicionarVariável(String nome) {
        if (variáveis.containsKey(nome)) return;
        OpcItem var = new OpcItem(nome, true, "");
        grupoPadrão.addItem(var);
        variáveis.put(nome, var);
    }

    public double getValor(String nomeDaVariável) {
        OpcItem response = null;
        try {
            response = jopc.synchReadItem(grupoPadrão, variáveis.get(nomeDaVariável));
        } catch (SynchReadException e) {
            e.printStackTrace();
        }
        return Double.parseDouble(response.getValue().getString());
    }

    void setVariavel(String nome, double valor) {
        OpcItem item = variáveis.get(nome);
        item.setValue(new Variant(valor));
        try {
            jopc.synchWriteItem(grupoPadrão, item);
        } catch (SynchWriteException e) {
            e.printStackTrace();
        }
    }

    public int compareTo(ConexaoOPC that) {
        return this.getNome().compareTo(that.getNome());
    }

    public String getNome() {
        return nome;
    }
}
