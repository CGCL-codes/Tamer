package ws.prova.api2;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;
import ws.prova.agent2.ProvaReagent;
import ws.prova.kernel2.ProvaList;
import ws.prova.kernel2.ProvaSolution;

public interface ProvaCommunicator {

    public List<ProvaSolution[]> getInitializationSolutions();

    public void setPrintWriter(PrintWriter printWriter);

    public List<ProvaSolution[]> consultSync(String src, String key, Object[] objects) throws Exception;

    public List<ProvaSolution[]> consultSync(BufferedReader in, String key, Object[] objects) throws Exception;

    public void consultAsync(String src, String key, Object[] objects) throws Exception;

    public void consultAsync(BufferedReader in, String key, Object[] objects) throws Exception;

    public void shutdown();

    public ProvaReagent getReagent();

    public void addMsg(ProvaList terms);

    public void unconsultSync(String src);

    public void stop();
}
