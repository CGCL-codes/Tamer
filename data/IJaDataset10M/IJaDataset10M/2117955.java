package test;

import com.javector.soaj.deploy.invocation.SoajEJB21Method;
import java.util.ArrayList;

public class IntegrationTest_SoajEJB21Method {

    public String runTest() throws Exception {
        ArrayList<String> paramClass = new ArrayList<String>();
        paramClass.add("java.lang.String");
        paramClass.add("java.lang.String");
        SoajEJB21Method method = new SoajEJB21Method("samples.EJB21Tester", "test", paramClass, "samples.EJB21TesterHome", "samples.EJB21TesterHome", true);
        ArrayList<Object> paramObject = new ArrayList<Object>();
        paramObject.add("Hello");
        paramObject.add("World");
        return (String) method.invoke(paramObject);
    }
}
