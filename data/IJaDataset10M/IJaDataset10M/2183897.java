package eu.mpower.framework.sensor.fsa.adapter.rs232;

/**
 * This class is in charge of solve possible problems with the eibnet interface
 * @author ELR
 */
public class RS232reconnection extends Thread {

    private RS232Connection rs232Con;

    public RS232reconnection(RS232Connection con) {
        rs232Con = con;
    }

    @Override
    public void run() {
        System.out.println("Hilo comprobacion estado conexion rs232 ");
        while (!rs232Con.getStop()) {
            System.out.println("comprobando");
            try {
                if (!rs232Con.isConnected()) {
                    if (reconnect()) {
                        rs232Con.setConnected(true);
                    }
                }
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupt();
            }
        }
    }

    public boolean reconnect() {
        return true;
    }
}
