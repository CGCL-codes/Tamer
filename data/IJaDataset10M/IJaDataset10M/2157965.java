package practica4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Ens servir� tant per al pooled thread com per al multiThread.
 */
public class EchoService extends Thread {

    private Buffer buff;

    private BufferedReader in;

    private PrintWriter out;

    /**
	 * Si li passem un socket al constructor sabem que estem
	 * treballant per a un MultiServer.
	 * @param sc
	 * @throws IOException
	 */
    public EchoService(Socket sc) throws IOException {
        in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
        out = new PrintWriter(sc.getOutputStream(), true);
    }

    /**
	 * Si fem servir aquest constructor, sabem que treballem
	 * amb un pooledserver.
	 * @param buf
	 */
    public EchoService(Buffer buff) {
        this.buff = buff;
    }

    public void run() {
        if (buff == null) {
            runLogic();
        } else {
            while (true) {
                Socket sc = (Socket) buff.get();
                try {
                    in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                    out = new PrintWriter(sc.getOutputStream(), true);
                    runLogic();
                } catch (IOException e) {
                    System.err.println("Error IO en el socket: " + e.getMessage());
                }
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    System.err.println("Error en tancar la connexi�: " + e.getMessage());
                }
            }
        }
    }

    private void runLogic() {
        try {
            while (true) {
                String t = in.readLine();
                out.println(t);
                if (t.equalsIgnoreCase("END")) break;
            }
        } catch (IOException e) {
            System.err.println("Error IO en el socket: " + e.getMessage());
        }
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Error en tancar la connexi�: " + e.getMessage());
        }
    }
}
