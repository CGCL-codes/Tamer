package net.sf.spooler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

public class MonitorRemoto extends AbstractMonitor {

    public MonitorRemoto(List<Diretorio> diretorios, Service servico) {
        super(diretorios, servico);
    }

    @Override
    public void processaArquivo(Arquivo arquivo) throws SpoolerException {
        log.info("Processando arquivo " + arquivo.getNome());
        Properties p = Util.lerPropriedades(arquivo.getNome());
        String alias = p.getProperty("servidor").trim();
        String copiasStr = p.getProperty("copias").trim();
        int copias = Integer.parseInt(copiasStr);
        String fila = p.getProperty("fila").trim();
        String reportFile = p.getProperty("arquivo").trim();
        reportFile = fixedReportName(reportFile, arquivo.getNome());
        if (!alias.equalsIgnoreCase(servico.getAlias())) {
            throw new SpoolerException("Alias: " + alias + " <> Servico:" + servico.getAlias());
        }
        File file = new File(reportFile);
        if (!file.exists()) {
            Util.delete(arquivo.getNome());
            throw new SpoolerException("Arquivo " + reportFile + " não encontrado.");
        }
        JobRemoto job = new JobRemoto();
        job.setNome(reportFile);
        job.setFila(fila);
        job.setCopias(copias);
        Socket socket = getConnection();
        try {
            ObjectOutput out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(job);
            transferFile(file, socket);
            log.info("Arquivo " + reportFile + " foi transmitido.");
        } catch (IOException ioe) {
            throw new SpoolerException(ioe.getMessage());
        }
        Util.pause(1000);
        Util.delete(arquivo.getNome());
    }

    private Socket getConnection() {
        Socket socket = null;
        int tentativas = 0;
        boolean isConnected = false;
        while (!isConnected) {
            try {
                String ip = servico.getIp();
                int porta = Integer.parseInt(servico.getPorta());
                tentativas++;
                log.info("Conectando a " + ip + ":" + porta + " (" + tentativas + ")");
                socket = new Socket(ip, porta);
                isConnected = socket.isConnected();
            } catch (ConnectException ce) {
                log.warn(ce.getMessage() + " " + ce.getCause());
            } catch (UnknownHostException uhe) {
                log.warn(uhe.getMessage());
            } catch (IOException ioe) {
                log.warn(ioe.getMessage());
            }
        }
        return socket;
    }

    private void transferFile(File file, Socket socket) throws IOException {
        FileInputStream in = new FileInputStream(file);
        OutputStream out = new GZIPOutputStream(socket.getOutputStream());
        Util.copy(in, out);
        out.flush();
        out.close();
        in.close();
    }
}
