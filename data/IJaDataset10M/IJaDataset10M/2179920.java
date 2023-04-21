package net.taylor.mail;

import com.dumbster.smtp.SimpleSmtpServer;

public interface DumbsterServiceMBean {

    void setPort(int port);

    int getPort();

    SimpleSmtpServer getServer();

    void create() throws Exception;

    void destroy();

    void start() throws Exception;

    void stop();
}
