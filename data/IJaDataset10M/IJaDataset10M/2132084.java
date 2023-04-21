package userguide.mex.services.example3;

public class Version {

    public String getVersion() throws Exception {
        return "Hello I am Axis2 version service ," + " My version is " + org.apache.axis2.Version.getVersionText();
    }
}
