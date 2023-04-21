package tcbnet.treecast;

import java.io.FileNotFoundException;
import java.util.*;
import tcbnet.corba.*;

/**
 * Classe responsavel por manter Treecasts ativos e controlar criacao de novos.
 *
 * @author $Author: rcouto $
 * @version $Revision: 1.42 $
 */
public class TreecastControl {

    private Node localNode;

    private short timeToLive;

    private long defaultTimeout;

    private String savingDir;

    private Map treecasts;

    private StreamControl streamControl;

    private List downloads;

    /**
     * Construtor.
     *
     * @param local Node local.
     * @param ttl TimeToLive padrao dos Headers crisados.
     * @param to Timeout padrao do Treecast (em segundos).
     * @param sd Diretorio padrao onde serao salvos novos arquivos.
     # @param ms Numero maximo de Streams simultaneas.
     */
    public TreecastControl(Node local, short ttl, long to, String sd, int ms) {
        this.localNode = local;
        this.timeToLive = ttl;
        this.defaultTimeout = to;
        this.savingDir = sd;
        this.treecasts = new HashMap();
        this.streamControl = new StreamControl(ms);
        this.downloads = new ArrayList();
    }

    /**
     * Cria um novo Treecast, que enviarah um arquivo local.
     *
     * @param nl Lista dos Nodes conectados a rede P2PHN.
     * @param header Header do arquivo a ser enviado.
     * @param path Diretorio onde se encontra o arquivo.
     * @return o treecast criado
     */
    public Treecast addTreecast(P2PNodeList nl, Header header, String path) {
        Treecast t = new TreecastSender(this.streamControl, path, header);
        nl.sendHeaderRaw(this.localNode, header);
        this.putTreecast(header.checkSum, t);
        return t;
    }

    /**
     * Cria um novo Treecast, que retransmitira um arquivo.
     *
     * @param nl Lista dos Nodes conectados a rede P2PHN.
     * @param header Header do arquivo a ser retransmitido.
     * @return o treecast criado
     * @throws FileNotFoundException
     */
    public Treecast addTreecastReceiver(P2PNodeList nl, Header header) throws FileNotFoundException {
        System.out.println("addTreecastReceiver : TreecastControl");
        Header h = new Header();
        h.node = this.localNode;
        h.fileName = header.fileName;
        h.fileSize = header.fileSize;
        h.fileDesc = header.fileDesc;
        h.MIMEType = header.MIMEType;
        h.checkSum = header.checkSum;
        h.timeToLive = (short) (header.timeToLive + this.timeToLive);
        h.visited = new Node[header.visited.length + 1];
        int i;
        for (i = 0; i < header.visited.length; i++) h.visited[i] = header.visited[i];
        h.visited[i] = this.localNode;
        Treecast t = new TreecastReceiver(this.streamControl, this.savingDir, header, this.localNode);
        nl.sendHeaderRaw(this.localNode, h);
        this.putTreecast(h.checkSum, t);
        return t;
    }

    /**
     * Anuncio de desativacao de um Treecast.
     *
     * @param checkSum CheckSum do arquivo que foi transmitido no Treecast que
     *                 esta se desativando.
     */
    public void timeoutTreecast(String checkSum) {
        this.removeTreecast(checkSum);
    }

    /**
     * Adiciona um novo Treecast na lista de Treecasts ativos.
     *
     * @param checkSum CheckSum do arquivo que sera transmitido no Treecast a
     *                 ser adicionado.
     * @param treecast Treecast a ser adicionado.
     */
    private void putTreecast(String checkSum, Treecast treecast) {
        this.treecasts.put(checkSum, treecast);
        if (treecast instanceof TreecastReceiver) this.downloads.add(treecast);
    }

    /**
     * Remove um Treecast da lista de Treecasts ativos.
     *
     * @param checkSum CheckSum do arquivo que esta sendo transmitido pelo
     *                 Treecast a ser removido.
     */
    private void removeTreecast(String checkSum) {
        Treecast t = (Treecast) this.treecasts.remove(checkSum);
        if (t instanceof TreecastReceiver) this.downloads.remove(t);
    }

    /**
     * Retorna o Treecast cujo arquivo transmitido tem esse checksum.
     *
     * @param checkSum Checksum do arquivo transmitido pela Treecast.
     * @return Treecast cujo arquivo transmitido tem esse checksum.
     */
    private Treecast getTreecast(String checkSum) {
        return (Treecast) this.treecasts.get(checkSum);
    }

    /**
     * Retorna uma Stream do Treecast que esta transmitindo o arquivo com esse
     * checksum.
     *
     * @param n Node que esta pedindo a Stream.
     * @param checkSum Checksum do arquivo que sera transmitido
     * @return Stream que transmitira
     * @throws NoStreamAvailableException
     * @throws NotAvailableException
     */
    public synchronized Stream getStream(Node n, String checkSum) throws NoStreamAvailableException, NotAvailableException {
        System.out.println("getStream : TreecastControl");
        Treecast t = this.getTreecast(checkSum);
        if (t == null) System.out.println("FODEU 1");
        return t.getStream(n);
    }

    /**
     * Retorna informacoes sobre downloads.
     *
     * @param i i-esimo download.
     * @param j j-esima informacao de download.
     * @return j-esima dentre "Filename", "Transfered", "Filesize", "Speed",
     *         "Source", "Description", "Progress" do i-esimo download.
     */
    public Object getDownloadInfo(int i, int j) {
        TreecastReceiver tr = (TreecastReceiver) this.downloads.get(i);
        if (tr == null) return null;
        switch(j) {
            case 0:
                return tr.getHeader().fileName;
            case 1:
                return new Long(tr.getTransfered());
            case 2:
                return new Long(tr.getHeader().fileSize);
            case 3:
                return "na";
            case 4:
                return tr.getHeader().node;
            case 5:
                return tr.getHeader().fileDesc;
            case 6:
                return (((int) (tr.getTransfered() / ((double) tr.getHeader().fileSize)) * 100)) + "%";
            default:
                System.out.println("ERRO DE COLUNA");
                System.exit(1);
        }
        return null;
    }

    /**
     * @return o numero de downloads ativos
     */
    public int getDownloadCount() {
        return this.downloads.size();
    }
}
