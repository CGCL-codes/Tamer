package playground.tnicolai.urbansim.jaxbTest;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.matsim.testcases.MatsimTestCase;
import playground.tnicolai.urbansim.UpdateXMLBindingClasses;
import playground.tnicolai.urbansim.utils.CommonUtilities;
import playground.tnicolai.urbansim.utils.io.TempDirectoryUtil;

/**
 * @author thomas
 *
 */
public class CreateXSDBindingClassTest extends MatsimTestCase {

    private static final Logger log = Logger.getLogger(CreateXSDBindingClassTest.class);

    @Ignore
    @Test
    public void testCreateBindingClass() {
        prepareTest();
    }

    /**
	 * preparing creation of binding classes test run
	 */
    private void prepareTest() {
        String jaxbLocation = findJAXBLibLocation();
        String destination = createDestinationDirectory();
        String packageName = "org.test.bindingClasses";
        String[] args = { "--jaxbLocation=" + jaxbLocation, "--destination=" + destination, "--package=" + packageName };
        log.info("Strating UpdateXMLBindingClasses with following arguments: ");
        log.info("JAXB location : " + jaxbLocation);
        log.info("Destination : " + destination);
        log.info("Package name : " + packageName);
        UpdateXMLBindingClasses.main(args);
        TempDirectoryUtil.deleteDirectory(destination);
    }

    /**
	 * gets the location of JAXB library
	 * @return path of JAXB library
	 */
    private String findJAXBLibLocation() {
        String path = CommonUtilities.getCurrentPath(CreateXSDBindingClassTest.class);
        String subPath = "lib/jaxb-2.1.7/lib/jaxb-xjc.jar";
        return CommonUtilities.replaceSubPath(1, path, subPath);
    }

    /**
	 * returns the path to the temp destination directory for generated 
	 * binding classes
	 * @return path to the temp destination directory for generated 
	 * binding classes
	 */
    private String createDestinationDirectory() {
        String path = CommonUtilities.getCurrentPath(CreateXSDBindingClassTest.class);
        path = CommonUtilities.replaceSubPath(1, path, "tmp/jaxbBindingClasses");
        if (TempDirectoryUtil.createDirectory(path)) return path;
        return null;
    }
}
