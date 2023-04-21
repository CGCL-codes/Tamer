package com.cafe.serve.view.usermanagement;

import javax.swing.JFrame;
import net.sourceforge.jaulp.layout.CloseWindow;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.cafe.serve.controller.usermanagement.UserManagementMainController;

public class UserManagementMainPanelTest {

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.addWindowListener(new CloseWindow());
        frame.setTitle("Manage users, roles and permissions");
        new ClassPathXmlApplicationContext("cashbox_dao_applicationContext.xml");
        DOMConfigurator.configure("conf/log4j/log4jconfig.xml");
        final UserManagementMainView userManagementMainView = new UserManagementMainView(new UserManagementMainController());
        frame.add(userManagementMainView.getComponent());
        frame.pack();
        frame.setVisible(true);
    }
}
