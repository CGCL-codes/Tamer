package net.sf.brico.cmd.support.spring;

import net.sf.brico.cmd.Command;
import net.sf.brico.cmd.CommandFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class SpringEnabledCommandFactory implements CommandFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public Command createCommand(String name) {
        return (Command) applicationContext.getBean(name);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
