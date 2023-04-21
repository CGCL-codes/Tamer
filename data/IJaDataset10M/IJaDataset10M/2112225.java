package com.idna.batchid.startup;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.idna.batchid.processor.Processor;
import com.idna.batchid.processor.ProcessorImpl;

public class StartBatchIdTesting implements BeanFactoryAware {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(getConfigLocations());
        Processor processor = (Processor) context.getBean("processor");
        processor.setBeanFactory(context);
        while (true) {
            Thread.sleep(1000);
        }
    }

    protected static String[] getConfigLocations() {
        return new String[] { "classpath:applicationContext-entryPointAndFilesProcessor.xml", "classpath:applicationContext-batchManagement.xml", "classpath:applicationContext-recordManagement.xml", "classpath:applicationContext-scheduledJobs.xml", "classpath:applicationContext-test.xml" };
    }

    public void setBeanFactory(BeanFactory arg0) throws BeansException {
    }
}
