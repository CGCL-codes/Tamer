package self.micromagic.eterna.view;

import java.util.Map;
import self.micromagic.eterna.digester.ConfigurationException;
import self.micromagic.eterna.share.EternaFactory;

public interface Replacement extends Component {

    public static final Object INIT_COMPONENT = new Object();

    public static final String PARENT_SCRIPT = "{$parentScript}";

    void initReplace(EternaFactory factory, Replacement parent) throws ConfigurationException;

    /**
    * ���ֱ��ƥ��ؼ�ӳ���.
    */
    Map getDirectMatchMap() throws ConfigurationException;

    void replaceComponent(EternaFactory factory, Component newReplace) throws ConfigurationException;

    void initBase(EternaFactory factory, Component base) throws ConfigurationException;

    Component getBaseComponent() throws ConfigurationException;
}
