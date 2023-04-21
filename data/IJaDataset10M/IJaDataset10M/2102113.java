package visual3d.test;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 24.07.2004
 * Time: 22:18:09
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String args[]) {
        JarFileLoader.loadJarFiles(TestFrameOption.getLibraryDirectory());
        new TestFrame();
    }
}
