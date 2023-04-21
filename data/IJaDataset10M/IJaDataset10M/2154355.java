package prjfbtypes;

import org.jdom.Element;
import prjfbtypes.CommonInformation;
import prjfbtypes.FBNetwork;

public class Application extends CommonInformation {

    private FBNetwork fbNetwork;

    public Application(String Name, String Comment, FBNetwork fbNetwork) {
        super(Name, Comment);
        this.fbNetwork = fbNetwork;
    }

    public String toString() {
        return "Application";
    }

    public Element toXML() {
        Element applicationElement = new Element("Application");
        applicationElement.setAttribute("Name", Name);
        if (Comment != null) {
            applicationElement.setAttribute("Comment", Comment);
        }
        applicationElement.addContent(fbNetwork.toXML());
        return applicationElement;
    }

    public void print() {
        super.print();
        fbNetwork.print();
    }

    public FBNetwork getFbNetwork() {
        return fbNetwork;
    }

    public void setFbNetwork(FBNetwork fbNetwork) {
        this.fbNetwork = fbNetwork;
    }
}
