package ch.comtools.jsch;

public class ChannelSubsystem extends ChannelSession {

    boolean xforwading = false;

    boolean pty = false;

    boolean want_reply = true;

    String subsystem = "";

    public void setXForwarding(boolean foo) {
        xforwading = true;
    }

    public void setPty(boolean foo) {
        pty = foo;
    }

    public void setWantReply(boolean foo) {
        want_reply = foo;
    }

    public void setSubsystem(String foo) {
        subsystem = foo;
    }

    public void start() throws JSchException {
        try {
            Request request;
            if (xforwading) {
                request = new RequestX11();
                request.request(session, this);
            }
            if (pty) {
                request = new RequestPtyReq();
                request.request(session, this);
            }
            request = new RequestSubsystem();
            ((RequestSubsystem) request).request(session, this, subsystem, want_reply);
        } catch (Exception e) {
            if (e instanceof JSchException) {
                throw (JSchException) e;
            }
            if (e instanceof Throwable) throw new JSchException("ChannelSubsystem", (Throwable) e);
            throw new JSchException("ChannelSubsystem");
        }
        if (io.in != null) {
            thread = new Thread(this);
            thread.setName("Subsystem for " + session.host);
            if (session.daemon_thread) {
                thread.setDaemon(session.daemon_thread);
            }
            thread.start();
        }
    }

    public void init() {
        io.setInputStream(session.in);
        io.setOutputStream(session.out);
    }

    public void setErrStream(java.io.OutputStream out) {
        setExtOutputStream(out);
    }

    public java.io.InputStream getErrStream() throws java.io.IOException {
        return getExtInputStream();
    }
}
