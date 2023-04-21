package openfarm.analysiscomponents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import openfarm.jni.template.openfarmtemplates.CppTemplate;
import openfarmtools.analysiscomponents.AnalysisComponentParameters;
import openfarmtools.interpreter.exceptions.AnalysisComponentNotFoundException;
import openfarmtools.interpreter.exceptions.InvalidParametersException;
import openfarmtools.interpreter.exceptions.TemplateException;
import openfarmtools.interpreter.exceptions.XmlException;
import openfarmtools.repository.filehandling.EOperatingSystemProperties;
import org.dom4j.DocumentException;
import org.junit.Test;

public class AnalysisComponentTest {

    AnalysisComponent t = null;

    String[] descriptors = new String[2];

    public AnalysisComponentTest() {
        descriptors[0] = "utresources/scenecutdetection.xml";
        descriptors[1] = "utresources/textextraction.xml";
    }

    /**
	 * Test method for {@link openfarm.analysiscomponents.AnalysisComponent#AnalysisComponent()}.
	 */
    @Test
    public void testAnalysisComponent() {
        t = new AnalysisComponent();
        assertTrue(t instanceof AnalysisComponent);
    }

    /**
	 * Test method for {@link openfarm.analysiscomponents.AnalysisComponent#AnalysisComponent(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Set, openfarm.jni.template.Template, java.util.Set)}.
	 */
    @Test
    public void testAnalysisComponentIntStringStringStringStringSetOfStringTemplateSetOfAnalysisComponentParameters() {
        t = new AnalysisComponent(0, "", "", "", "", "", null, null, null);
        assertTrue(t instanceof AnalysisComponent);
    }

    /**
	 * Test method for {@link openfarm.analysiscomponents.AnalysisComponent#validateParameters(openfarm.analysiscomponents.Parameterize)}.
	 */
    @Test
    public void testValidateParameters() {
        t = new AnalysisComponent();
        Set<AnalysisComponentParameters> parms = new HashSet<AnalysisComponentParameters>();
        parms.add(new AnalysisComponentParameters("nFrames", "int", "2000"));
        t.setParameterConfig(parms);
        Parameterize p = new Parameterize();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("nFrames", "500");
        p.setParams(map);
        try {
            t.validateParameters(p);
        } catch (InvalidParametersException e) {
            fail();
        }
        map.put("invalidparam", "9292929");
        boolean checked = false;
        try {
            t.validateParameters(p);
        } catch (InvalidParametersException e) {
            checked = true;
        }
        if (!checked) fail();
    }

    /**
	 * Test method for {@link openfarm.analysiscomponents.AnalysisComponent#getDefaultParameters()}.
	 */
    @Test
    public void testGetDefaultParameters() {
        t = new AnalysisComponent();
        Parameterize actualP = t.getDefaultParameters();
        assertNull(actualP);
        Set<AnalysisComponentParameters> parms = new HashSet<AnalysisComponentParameters>();
        parms.add(new AnalysisComponentParameters("nFrames", "int", "2000"));
        t.setParameterConfig(parms);
        actualP = t.getDefaultParameters();
        assertEquals("2000", actualP.getParams().get("nFrames"));
    }

    /**
	 * Test method for {@link openfarm.analysiscomponents.AnalysisComponent#getNewTemplateInstance()}.
	 */
    @Test
    public void testGetNewTemplateInstance() {
        t = new AnalysisComponent();
        t.setTemplate(new CppTemplate(null, null));
        assertTrue(t.getNewTemplateInstance("myRepository") instanceof CppTemplate);
    }

    @Test
    public void testEquals() throws AnalysisComponentNotFoundException, FileNotFoundException, XmlException, TemplateException, DocumentException {
        AnalysisComponent ac = new AnalysisComponent();
        ac.setId(1);
        ac.setName("Scene Cut Detection");
        ac.setLibName("libscencutdetection.dll");
        ac.setOs(EOperatingSystemProperties.WINDOWS);
        ac.setProgrammingLanguage("C++");
        Set<String> ext = new HashSet<String>();
        ext.add("avi");
        ac.setSupportedMaterialExtentions(ext);
        ac.setTemplate(new CppTemplate("libscencutdetection.dll", ""));
        Set<AnalysisComponentParameters> parms = new HashSet<AnalysisComponentParameters>();
        parms.add(new AnalysisComponentParameters("nFrames", "int", "2000"));
        parms.add(new AnalysisComponentParameters("threshold", "float", "0.6890"));
        parms.add(new AnalysisComponentParameters("mock", "String", ""));
        ac.setParameterConfig(parms);
        AnalysisComponent ac2 = new AnalysisComponent();
        ac2.setId(1);
        ac2.setName("Scene Cut Detection");
        ac2.setLibName("libscencutdetection.dll");
        ac2.setOs(EOperatingSystemProperties.WINDOWS);
        ac2.setProgrammingLanguage("C++");
        ac2.setSupportedMaterialExtentions(ext);
        ac2.setTemplate(new CppTemplate("libscencutdetection.dll", ""));
        parms.add(new AnalysisComponentParameters("nFrames", "int", "2000"));
        parms.add(new AnalysisComponentParameters("threshold", "float", "0.6890"));
        parms.add(new AnalysisComponentParameters("mock", "String", ""));
        ac2.setParameterConfig(parms);
        assertTrue(ac.equals(ac2));
    }
}
