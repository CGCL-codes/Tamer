package net.sf.brico.cmd.support.spring;

import net.sf.brico.cmd.base.form.DynamicFormFactory;
import org.springframework.beans.factory.BeanNameAware;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class SpringEnabledDynamicFormFactory extends DynamicFormFactory implements BeanNameAware {

    public void setBeanName(String name) {
        setName(name);
    }
}
