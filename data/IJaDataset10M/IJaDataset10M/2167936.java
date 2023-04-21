package org.avaje.ebean.server.deploy.parse;

/**
 * Read the deployment annotations for the bean.
 */
public class ReadAnnotations {

    /**
     * Read and process all the annotations.
     */
    public void process(DeployBeanInfo info) {
        try {
            AnnotationClass clsAnnotations = new AnnotationClass(info);
            clsAnnotations.parse();
            info.setDefaultTableName();
            new AnnotationFields(info).parse();
            new AnnotationAssocOnes(info).parse();
            new AnnotationAssocManys(info).parse();
            clsAnnotations.readSqlAnnotations();
        } catch (RuntimeException e) {
            String msg = "Error reading annotations for " + info;
            throw new RuntimeException(msg, e);
        }
    }
}
