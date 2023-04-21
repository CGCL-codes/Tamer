package org.xith3d.test.platform;

import java.io.IOException;
import org.xith3d.test.Xith3DTest;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.platform.OSInfo;

@Xith3DTest.Description(fulltext = { "This test is used to verify that your local", "operating system is being detected correctly" }, authors = { "Kevin Finley (aka Horati)" })
public class OSInfoTest extends Xith3DTest {

    private void runTest() {
        OSInfo info = OSInfo.getInstance();
        System.out.println(info);
        if (info.getCategory() == OSInfo.Category.UNKNOWN || info.getCategoryDetail() == OSInfo.CategoryDetail.UNKNOWN) {
            System.out.println();
            System.out.println("Some or all information regarding your operating system could not be determined");
            System.out.println("Please post the following information on Xith 3D Support forum");
            System.out.println();
            try {
                System.getProperties().storeToXML(System.out, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        this.end();
    }

    public void begin(RunMode runMode, TimingMode timingMode) {
        runTest();
    }

    public OSInfoTest(BasicApplicationArguments arguments) {
        super((Float) null);
    }

    public static void main(String[] args) throws Throwable {
        OSInfoTest test = new OSInfoTest(parseCommandLine(args));
        test.begin();
    }
}
