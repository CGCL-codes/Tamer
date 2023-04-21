package gc3d.test;

public class TestPovProcess {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        TestRender r1 = new TestRender("D:\\GC3D\\testpp\\pp1", "povrayfile.pov1.ini");
        TestRender r2 = new TestRender("D:\\GC3D\\testpp\\pp2", "povrayfile.pov3.ini");
        r1.start();
        r2.start();
        System.out.println("finish");
    }
}
