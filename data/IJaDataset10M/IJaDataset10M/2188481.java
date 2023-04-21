package aimclient;

public interface CLCommand {

    void handle(String line, String cmd, String[] args);
}
