package org.springframework.lucene.samples.quickstart.index;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("/org/springframework/lucene/samples/quickstart/index/applicationContext.xml");
            SampleIndexService service = (SampleIndexService) context.getBean("sampleIndexService");
            service.indexDocuments();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (context != null) {
                context.close();
            }
        }
    }
}
