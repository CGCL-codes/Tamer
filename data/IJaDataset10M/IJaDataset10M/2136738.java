package de.fmf.scp;

public class Tomcat {

    private static final String SUDO_ETC_INIT_D_TOMCAT_ = "sudo /etc/init.d/tomcat_";

    private Server server;

    private String tomcatName;

    public Tomcat(Server server, String tomcatName) {
        this.server = server;
        this.tomcatName = tomcatName;
    }

    public void startTomcat() {
        server.getConnection().executeCommand(SUDO_ETC_INIT_D_TOMCAT_ + this.tomcatName + " start");
    }

    public void stopTomcat() {
        server.getConnection().executeCommand(SUDO_ETC_INIT_D_TOMCAT_ + this.tomcatName + " stop");
    }

    public void restartTomcat() {
        server.getConnection().executeCommand(SUDO_ETC_INIT_D_TOMCAT_ + this.tomcatName + " restart");
    }
}
