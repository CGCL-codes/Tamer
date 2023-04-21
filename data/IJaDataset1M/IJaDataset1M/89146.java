package tcbnet.control;

import tcbnet.ui.UIConfiguration;
import java.io.IOException;

/**
 * Interface de parametros de configuracao de Nodes.
 * @author $Author: dseki $
 * @version $Revision: 1.8 $
 */
public interface ConfigurationUI {

    /**
     * Salva este objeto de configuracao no arquivo de configuracao padrao.
     *
     * @throws IOException quando nao for possivel salvar o objeto de config
     */
    public void save() throws IOException;

    /**
     * Le do objeto de configuracoes o numero maximo de conexoes da
     * Peer-To-Peer Headers Network.
     *
     * @return numero maximo de conexoes da P2PHN
     */
    public int getP2PMaxConnections();

    /**
     * Salva no objeto de configuracoes o numero maximo de conexoes da
     * Peer-To-Peer Headers Netword.
     *
     * @param p2pMaxConnections numero maximo de conexoes da P2PHN
     * @throws IllegalArgumentException quando o numero for menor do que 1
     */
    public void setP2PMaxConnections(int p2pMaxConnections) throws IllegalArgumentException;

    /**
     * Le do objeto de configuracoes o numero maximo de nodes a serem
     * armazenados visando novas conexoes na rede P2PHN.
     *
     * @return numero maximo de nodes conhecidos para guardar
     */
    public int getP2PMaxSeenNodes();

    /**
     * Salva no objeto de configuracoes o numero maximo de nodes a serem
     * armazenados visando novas conexoes na rede P2PHN.
     *
     * @param p2pMaxSeenNodes numero maximo de nodes a serem armazenados
     * @throws IllegalArgumentException quando o numero for menor do que 0
     */
    public void setP2PMaxSeenNodes(int p2pMaxSeenNodes) throws IllegalArgumentException;

    /**
     * Le do objeto de configuracoes o Time-To-Live ("Tempo de Vida" ou nivel
     * de recursao) de um cabecalho (Header) na P2PHN.
     *
     * @return tempo de vida de um Header
     */
    public short getP2PTimeToLive();

    /**
     * Salva no objeto de configuracoes o Time-To-Live de um cabecalho na
     * P2PHN.
     *
     * @param p2pTimeToLive tempo de vida do cabecalho na P2PHN
     * @throws IllegalArgumentException quando o tempo for menor do que 1
     */
    public void setP2PTimeToLive(short p2pTimeToLive) throws IllegalArgumentException;

    /**
     * Le do objeto de configuracao o diretorio padrao onde serao armazenados
     * os novos arquivos capturados pelo programa.
     *
     * @return string com o diretorio padrao para salvar os arquivos
     */
    public String getTreecastDefaultDirectory();

    /**
     * Salva no objeto de configuracao o diretorio padrao onde deverao ser
     * armazenados os novos arquivos capturados pelo programa.
     *
     * @param treecastDefaultDirectory local padrao para salvar arquivos
     */
    public void setTreecastDefaultDirectory(String treecastDefaultDirectory);

    /**
     * Le do objeto de configuraco o numero maximo de conexoes da Treecast
     * Network (TN).
     *
     * @return numero maximo de conexoes da TN
     */
    public int getTreecastMaxConnections();

    /**
     * Salva no objeto de configuracao o numero maximo de conexoes da TN.
     *
     * @param treecastMaxConnections numero maximo de conexoes da TN
     */
    public void setTreecastMaxConnections(int treecastMaxConnections) throws IllegalArgumentException;

    /**
     * Le do objeto de configuracao o tempo em segundos de espera antes de
     * parar um Push, ou seja, evitar que novas transmissoes sejam criadas para
     * ele.
     *
     * @return tempo em segundos de espera antes de parar push
     */
    public long getTreecastTimeout();

    /**
     * Salva no objeto de configuracao o tempo em segundos de espera antes de
     * parar um Push, ou seja, evitar que novas transmissoes sejam criadas para
     * ele. Exige que o tempo seja pelo menos 60 segundos.
     *
     * @param treecastTimeout tempo em segundos de espera antes de parar push
     * @throws IllegalArgumentException quando o tempo eh menor do que 60
     */
    public void setTreecastTimeout(long treecastTimeout) throws IllegalArgumentException;

    /**
     * Le a arvore de filtros do arquivo de configuracoes.
     *
     * @return arvore de filtro
     */
    public FilterRoot getFilterTree();

    /**
     * Salva nas configuracoes uma arvore de filtros.
     *
     * @param filterTree arvore a ser salva
     */
    public void setFilterTree(FilterRoot filterTree);

    /**
     * Le deste objeto de configuracao um objeto de configuracao de interface
     * com o usuario (UI).
     *
     * @param uiConfiguration objeto de configuracao de interface
     */
    public UIConfiguration getUIConfiguration();

    /**
     * Salva neste objeto de configuracao um objeto de configuracao de
     * interface com o usuario (UI).
     *
     * @param uiConfiguration objeto de configuracao de interface
     */
    public void setUIConfiguration(UIConfiguration uiConfiguration);
}
