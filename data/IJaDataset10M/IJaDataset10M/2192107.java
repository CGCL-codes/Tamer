package org.intellij.ibatis.dom.abator;

import com.intellij.javaee.model.xml.CommonDomModelRootElement;
import com.intellij.util.xml.SubTagList;
import org.intellij.ibatis.dom.configuration.Properties;
import java.util.List;

/**
 * abatorConfiguration root element
 */
public interface AbatorConfiguration extends CommonDomModelRootElement {

    public Properties getProperties();

    @SubTagList("abatorContext")
    public List<AbatorContext> getAbatorContextes();
}
