package nhap;

import java.io.File;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class NhapParams {

    private String[] telnetLogonSequence;

    private String hostname;

    private int port;

    private String[] startSequence;

    private String playerName;

    private int time;

    private File representationDirectory;

    private File nethackDirectory;

    private String enduser;

    private int runLength;

    private int iterations = 1;

    private int startPoint;

    public NhapParams(String[] telnetLogonSequence, String hostname, int port, String[] startSequence, String playerName, int time, File representationDirectory, File nethackDirectory) {
        super();
        this.telnetLogonSequence = telnetLogonSequence;
        this.hostname = hostname;
        this.port = port;
        this.startSequence = startSequence;
        this.playerName = playerName;
        this.time = time;
        this.representationDirectory = representationDirectory;
        this.nethackDirectory = nethackDirectory;
    }

    public NhapParams(String[] telnetLogonSequence, String hostname, int port, String[] startSequence, String playerName, int time, File representationDirectory) {
        super();
        this.telnetLogonSequence = telnetLogonSequence;
        this.hostname = hostname;
        this.port = port;
        this.startSequence = startSequence;
        this.playerName = playerName;
        this.time = time;
        this.representationDirectory = representationDirectory;
    }

    public NhapParams(String[] telnetLogonSequence, String hostname, int port, String[] startSequence, String playerName, File representationDirectory) {
        super();
        this.telnetLogonSequence = telnetLogonSequence;
        this.hostname = hostname;
        this.port = port;
        this.startSequence = startSequence;
        this.playerName = playerName;
        this.representationDirectory = representationDirectory;
    }

    public String getHostname() {
        return hostname;
    }

    void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPlayerName() {
        return playerName;
    }

    void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPort() {
        return port;
    }

    void setPort(int port) {
        this.port = port;
    }

    public String[] getStartSequence() {
        return startSequence;
    }

    void setStartSequence(String[] startSequence) {
        this.startSequence = startSequence;
    }

    public String[] getTelnetLogonSequence() {
        return telnetLogonSequence;
    }

    void setTelnetLogonSequence(String[] telnetLogonSequence) {
        this.telnetLogonSequence = telnetLogonSequence;
    }

    int getTime() {
        return time;
    }

    void setTime(int time) {
        this.time = time;
    }

    File getRepresentationDirectory() {
        return representationDirectory;
    }

    void setRepresentationDirectory(File representationDirectory) {
        this.representationDirectory = representationDirectory;
    }

    File getNethackDirectory() {
        return nethackDirectory;
    }

    void setNethackDirectory(File nethackDirectory) {
        this.nethackDirectory = nethackDirectory;
    }

    public String getEnduser() {
        return enduser;
    }

    void setEnduser(String enduser) {
        this.enduser = enduser;
    }

    int getRunLength() {
        return runLength;
    }

    void setRunLength(int runLength) {
        this.runLength = runLength;
    }

    int getStartPoint() {
        return startPoint;
    }

    void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    int getIterations() {
        return iterations;
    }

    void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
