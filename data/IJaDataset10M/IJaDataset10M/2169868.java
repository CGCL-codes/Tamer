package mindbright.ssh;

public interface SSHCommandShell {

    public void setStdIO(SSHStdIO stdio);

    public boolean doCommandShell();

    void launchCommandShell();

    boolean escapeSequenceTyped(char c);

    String escapeString();
}
