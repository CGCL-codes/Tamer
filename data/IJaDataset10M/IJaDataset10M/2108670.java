package net.sourceforge.glsof.common.io;

import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.io.read.BeanCreationList;
import org.apache.commons.betwixt.io.read.ChainedBeanCreator;
import org.xml.sax.SAXException;
import java.beans.IntrospectionException;
import java.io.*;

public class XMLRepository {

    protected static final String FILE_EXTENSION = ".xml";

    protected File _baseDir;

    protected XMLRepository(final File baseDir) {
        if (baseDir != null && !baseDir.exists()) baseDir.mkdirs();
        _baseDir = baseDir;
    }

    protected Object read(final Class beanClass, final String beanName, final String fileName, final ChainedBeanCreator chainedBeanCreator) {
        Object bean = null;
        try {
            final BeanReader beanReader = new BeanReader();
            beanReader.getXMLIntrospector().getConfiguration().setSimpleTypeMapper(new AttributesMapper());
            beanReader.getBindingConfiguration().setMapIDs(false);
            beanReader.getReadConfiguration().setBeanCreationChain(createChain(chainedBeanCreator));
            beanReader.registerBeanClass(beanName, beanClass);
            bean = beanReader.parse(new FileReader(new File(_baseDir, getFileName(fileName))));
        } catch (IOException ioE) {
            ioE.printStackTrace();
        } catch (SAXException saxE) {
            saxE.printStackTrace();
        } catch (IntrospectionException iE) {
            iE.printStackTrace();
        }
        return bean;
    }

    private BeanCreationList createChain(final ChainedBeanCreator chainedBeanCreator) {
        final BeanCreationList chain = BeanCreationList.createStandardChain();
        final ChainedBeanCreator beanCreator = chainedBeanCreator;
        chain.insertBeanCreator(1, beanCreator);
        return chain;
    }

    protected void write(final Object bean, final String beanName, final String name) {
        final String fileName = _baseDir.getPath() + File.separator + getFileName(name);
        FileWriter fileWriter = null;
        try {
            final StringBuffer xmlBuffer = toXMLString(bean, beanName);
            xmlBuffer.insert(0, "<?xml version='1.0' ?>\n");
            final File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            fileWriter.write(xmlBuffer.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (Exception e) {
            }
        }
    }

    private StringBuffer toXMLString(final Object bean, final String beanName) {
        final StringWriter outWriter = new StringWriter();
        final BeanWriter beanWriter = new BeanWriter(outWriter);
        final XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setSimpleTypeMapper(new AttributesMapper());
        beanWriter.setXMLIntrospector(introspector);
        final BindingConfiguration binding = new BindingConfiguration();
        binding.setMapIDs(false);
        beanWriter.setBindingConfiguration(binding);
        beanWriter.enablePrettyPrint();
        try {
            beanWriter.write(beanName, bean);
        } catch (IOException ioE) {
            ioE.printStackTrace();
        } catch (SAXException saxE) {
            saxE.printStackTrace();
        } catch (IntrospectionException iE) {
            iE.printStackTrace();
        }
        return outWriter.getBuffer();
    }

    public boolean fileExists(final String filename) {
        return new File(_baseDir, getFileName(filename)).exists();
    }

    protected String getFileName(final String fileName) {
        return fileName + FILE_EXTENSION;
    }
}
