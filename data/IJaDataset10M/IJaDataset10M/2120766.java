package com.guanghua.brick.db.extend.hibernate;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.hibernate.MappingException;
import org.hibernate.cfg.AnnotationConfiguration;

public class ExtendHBAnnotationConfiguration extends AnnotationConfiguration {

    private static Log logger = LogFactory.getLog(ExtendHBAnnotationConfiguration.class);

    @Override
    protected void parseMappingElement(Element arg0, String arg1) {
        try {
            super.parseMappingElement(arg0, arg1);
        } catch (MappingException ex) {
            Attribute pkg = arg0.attribute("pkg");
            logger.debug("search annotation class in hibernate package: [" + pkg + "]");
            if (pkg != null) {
                try {
                    List<Class> list = ExtendSpringAnnotationSessionFactoryBean.getPackageMapping(pkg.getValue());
                    Iterator<Class> i = list.iterator();
                    while (i.hasNext()) {
                        super.addAnnotatedClass(i.next());
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("can not find package or package-info.java:" + pkg, e);
                }
            } else throw ex;
        }
    }
}
