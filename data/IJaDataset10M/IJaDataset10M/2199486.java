package StreamRipStar;

public class Control_RunExternProgram implements Runnable {

    private String path;

    public Control_RunExternProgram(String path) {
        this.path = path;
    }

    public void run() {
        try {
            Runtime.getRuntime().exec(path);
        } catch (Exception e) {
            System.err.println("Fehler beim Ausführen von" + path + e);
        }
    }
}
