package commands.linux;

import applications.LinuxTracerouteApplication;
import commands.AbstractCommandParser;
import commands.ApplicationNotifiable;
import commands.LongTermCommand;
import dataStructures.ipAddresses.BadIpException;
import dataStructures.ipAddresses.IpAddress;

/**
 * TODO dodelat traceroute
 * @author Tomas Pitrinec
 */
public class Traceroute extends LinuxCommand implements LongTermCommand, ApplicationNotifiable {

    protected IpAddress adr;

    protected int navrKod = 0;

    protected int maxTtl = 30;

    protected double interval = 0.1;

    /**
     * Stav vykonavani prikazu:
     * 0 - ceka se na pakety<br />
     * 1 - vratil se paket od cilovyho pocitace - skoncit<br />
     * 2 - byl timeout - vypisovat hvezdicky a skoncit<br />
     * 3 - vratilo se host unreachable nebo net unreachable<br />
     */
    protected int stavKonani = 0;

    private LinuxTracerouteApplication app;

    public Traceroute(AbstractCommandParser parser) {
        super(parser);
    }

    @Override
    public void run() {
        parsujPrikaz();
        if (navrKod == 0) {
            parser.setRunningCommand(this, false);
            vykonejPrikaz();
        }
    }

    @Override
    public void catchSignal(Signal signal) {
        if (signal == Signal.CTRL_C) {
            app.exit();
        }
    }

    @Override
    public void catchUserInput(String line) {
    }

    @Override
    public void applicationFinished() {
        parser.deleteRunningCommand();
    }

    private void vykonejPrikaz() {
        app = new LinuxTracerouteApplication(getDevice(), this);
        app.setTarget(adr);
        app.setQueriesPerTTL(3);
        app.setMaxTTL(30);
        app.start();
    }

    protected void parsujPrikaz() {
        try {
            adr = new IpAddress(dalsiSlovo());
        } catch (BadIpException ex) {
            navrKod = 1;
            parser.printService(": traceroute: Chyba v syntaxi prikazu," + " jedina povolena syntaxe je \"traceroute <adresa>\"");
        }
    }
}
