package org.opennms.wicket.svclayer;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.opennms.netmgt.ConfigFileConstants;
import org.opennms.wicket.svclayer.model.userAdmin.xsd.users.Userinfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SuppressWarnings({ "UnusedAssignment" })
public class UserServiceImpl implements UserService {

    private String usersFile = "";

    public UserServiceImpl(String usersFile) {
        this.usersFile = usersFile;
    }

    @SuppressWarnings({ "AccessStaticViaInstance" })
    public Userinfo getUsers() {
        ConfigFileConstants m_config = new ConfigFileConstants();
        String onmsHome = m_config.getHome();
        Userinfo userinfo = new Userinfo();
        try {
            IBindingFactory bfact = BindingDirectory.getFactory(Userinfo.class);
            IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
            userinfo = (Userinfo) uctx.unmarshalDocument(new FileInputStream(onmsHome + "/etc/" + "users.xml"), null);
        } catch (JiBXException e) {
            System.out.println("JiBX Error: " + e);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!!!");
        }
        return (userinfo);
    }
}
