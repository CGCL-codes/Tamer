package computerlaboratorymanager;

public interface IRobot {

    public void mouseMove(int arg0, int arg1);

    public void mouseClick(int arg0);

    public void mousePress(int arg0);

    public void mouseRelease(int arg0);

    public void mouseWheel(int arg0);

    public void keyPress(int arg0);

    public void keyRelease(int arg0);

    public void keyType(int arg0);
}
