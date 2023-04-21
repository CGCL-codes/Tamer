package webservicesapi;

public interface UserInterface {

    void print(String msg);

    String scanShadowed();

    String scan();

    String[] scanCommand();

    void printLog(String log);
}
