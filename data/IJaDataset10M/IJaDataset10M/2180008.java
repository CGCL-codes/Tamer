package net.sf.tcpswitch.parser;

import net.sf.tcpswitch.ConnInfo;
import net.sf.tcpswitch.Logger;
import net.sf.tcpswitch.TCPSWException;

public class PForward extends PStatement {

    private String fwdHost = null;

    private int fwdPort = -1;

    private String varHost = null;

    private String varPort = null;

    private String fwdOptions = null;

    public PForward(String params) throws TCPSWException {
        String[] hostport_opts = params.split("[,]");
        String[] host_port = hostport_opts[0].split("[:]");
        if (host_port[0].startsWith("$")) {
            varHost = host_port[0].substring(1);
            checkHostVar(varHost);
        } else fwdHost = host_port[0];
        if (host_port.length >= 2) {
            if (host_port[1].startsWith("$")) {
                varPort = host_port[1].substring(1);
                checkNumVar(varPort);
            } else {
                try {
                    fwdPort = Integer.parseInt(host_port[1]);
                } catch (NumberFormatException e) {
                    throw new TCPSWException("invlaid port in forward: " + params);
                }
            }
            if (hostport_opts.length >= 2) {
                fwdOptions = hostport_opts[1];
                checkOptions(fwdOptions);
            }
        } else fwdPort = 0;
    }

    private void checkOptions(String options) throws TCPSWException {
        if (options.startsWith("HTTPSPROXYconnect")) options = "HTTPSPROXYconnect";
        if ("#HTTPrewritehost#HTTPSPROXYestablish#HTTPSPROXYconnect#".indexOf(options) == -1) {
            throw new TCPSWException("invalid forward option " + options);
        }
    }

    private void checkNumVar(String var) throws TCPSWException {
        if ("#proxyport#port#".indexOf(var) == -1) throw new TCPSWException("invalid port variable $" + var);
    }

    private void checkHostVar(String var) throws TCPSWException {
        if ("#proxyhost#host#".indexOf(var) == -1) throw new TCPSWException("invalid host variable $" + var);
    }

    public int forward(ConnInfo conn) {
        int result;
        try {
            if (varHost != null) fwdHost = evaluateHostVar(conn, varHost);
            if (varPort != null) fwdPort = evaluatePortVar(conn, varPort);
            conn.setForward(fwdHost, fwdPort, fwdOptions);
            result = EX_FINISHED;
        } catch (TCPSWException e) {
            Logger.err("forward failed, reason: " + e.getMessage());
            result = EX_QUIT;
        }
        return result;
    }

    private int evaluatePortVar(ConnInfo conn, String varPort) throws TCPSWException {
        int result = -1;
        if (varPort.equals("proxyport")) result = conn.getProxyPort(); else if (varPort.equals("port")) result = conn.getPort();
        if (result <= 0) throw new TCPSWException("ivalid Port variable " + varPort);
        return result;
    }

    private String evaluateHostVar(ConnInfo conn, String varHost) throws TCPSWException {
        String result = null;
        if (varHost.equals("proxyhost")) result = conn.getProxyHost(); else if (varHost.equals("ip")) result = conn.getHost();
        if (result == null) throw new TCPSWException("ivalid host variable " + varHost);
        return result;
    }

    public String show(String indent) {
        String result = indent + "forward " + fwdHost + ":" + Integer.toString(fwdPort);
        if (fwdOptions != null) result += "," + fwdOptions;
        return result + "\n";
    }
}
