package br.biofoco.p2p.rpc.torrent;

public class TesteTorrentJTI {

    public static void main(String[] args) {
        try {
            TorrentManager tm = new TorrentManager(OptionTorrent.PUBLISH, args);
            Thread tmt = new Thread(tm);
            tmt.start();
            Thread.sleep(6000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erro " + e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }
}
