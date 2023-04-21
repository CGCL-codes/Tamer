package br.biofoco.p2p.bootstrap;

import jBittorrentAPI.TorrentFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.LoggerFactory;
import com.jcraft.jsch.Logger;
import br.biofoco.p2p.bulk.FastaParser;
import br.biofoco.p2p.gui.Controller;
import br.biofoco.p2p.peer.PeerNode;
import br.biofoco.p2p.rpc.Protocol;
import br.biofoco.p2p.rpc.messaging.MessageException;
import br.biofoco.p2p.rpc.torrent.CreateTorrent;
import br.biofoco.p2p.rpc.torrent.PublisherTracker;
import br.biofoco.p2p.rpc.torrent.SharedTorrent;

public class BlastClient extends Controller implements PeerNodeListener {

    private volatile boolean ready = false;

    private PeerNode peerNode;

    private AppConfig appConfig = new AppConfig();

    private TorrentFile t_file = null;

    CreateTorrent ct = null;

    public List<File> parseFastaFile(File file) throws IOException {
        FastaParser parser = new FastaParser();
        return parser.parse(file.getAbsolutePath(), appConfig.getSpoolDirectory());
    }

    @Override
    public void submit(File file) {
        if (ready) {
            try {
                long init = System.currentTimeMillis();
                List<File> files = parseFastaFile(file);
                System.out.println(files);
                Collection<PeerNode> view = peerNode.getPeerView();
                for (PeerNode v : view) {
                    System.out.println("------------------------------------> SUBMITTING to " + v.getPeerID());
                    try {
                        if (files.size() == 0) break;
                        submitTask(file, files, v);
                        System.out.println("EXECUTION LIST = " + files.size());
                    } catch (MessageException e) {
                        e.printStackTrace();
                    }
                }
                while (files.size() > 0) {
                    Thread.sleep(10 * 1000);
                    for (PeerNode v : view) {
                        System.out.println("Verificando status");
                        String result = peerNode.getMessenger().sendMessage("BLAST-SERVICE", v, "STATUS");
                        System.out.println("status: " + result);
                        if (result.startsWith("300")) {
                            submitTask(file, files, v);
                            System.out.println("EXECUTION LIST = " + files.size());
                        }
                        if (files.size() == 0) break;
                    }
                }
                long end = System.currentTimeMillis();
                FileOutputStream fos = new FileOutputStream(new File(appConfig.getOutputDirectory(), "exec_time_" + file.getName()));
                String timing = "execution time (ms) = " + (end - init);
                fos.write(timing.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void submitTask(File file, List<File> files, PeerNode dstPeer) throws MessageException, UnknownHostException {
        if (files.size() > 0) {
            File seqFile = files.remove(0);
            System.out.println("--- sending " + file.getName() + " to " + dstPeer.getPeerID());
            String host = peerNode.getEndPoint(Protocol.TCP).getAddress();
            String res = peerNode.getMessenger().sendMessage("BLAST-SERVICE", dstPeer, "EXECUTE", seqFile.getParent(), seqFile.getName(), host);
            System.out.println("--- " + res + " to " + dstPeer.getPeerID());
        }
    }

    private void submitTorrent(String torrentFile, PeerNode dstPeer) throws MessageException {
        String host = peerNode.getEndPoint(Protocol.TCP).getAddress();
        peerNode.getMessenger().sendMessage("BLAST-SERVICE", dstPeer, "EXECUTE-TORRENT", appConfig.getTorrentFileDirectory(), torrentFile, host);
    }

    @Override
    public void onReady(PeerNode peerNode) {
        ready = true;
        this.peerNode = peerNode;
    }

    @Override
    public void submitTorrent(final File file) {
        final String[] data = { file.getAbsolutePath() };
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ArrayList<String> al = new ArrayList<String>();
                    for (String files : data) {
                        System.out.println("BlastClient.submitTorrent() - Create torrent file.");
                        al.add(files);
                    }
                    ct = new CreateTorrent(null);
                    t_file = ct.createTorrentFile(al);
                    System.out.println("BlastClient.submitTorrent() - File(s) " + t_file.name.get(0) + " created!!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.run();
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println("BlastClient.submitTorrent() - Publicar o arquivo no tracker.");
        PublisherTracker pt = new PublisherTracker(null);
        String[] data_torrent = { ct.getPathCompleteCreated() };
        pt.publish(data_torrent);
        System.out.println("BlastClient.submitTorrent()  - Torrent publicado no tracker.");
        System.out.println("BlastClient.submitTorrent() - Sharing file.....: " + data_torrent[0]);
        System.out.println("BlastClient.submitTorrent() - Path source file: " + ct.getPathFileToTorrent());
        SharedTorrent st = new SharedTorrent(data_torrent[0], ct.getPathFileToTorrent());
        Collection<PeerNode> view = peerNode.getPeerView();
        for (PeerNode v : view) {
            System.out.println("---------> SUBMITTING TORRENT " + v.getPeerID());
            try {
                submitTorrent("data.torrent", v);
            } catch (MessageException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void submitTaskSFTP(File arquivo) {
    }
}
