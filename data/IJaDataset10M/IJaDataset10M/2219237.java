package purej.service.builder;

import org.dom4j.Node;
import purej.logging.Logger;
import purej.logging.LoggerFactory;
import purej.service.domain.LocalPOJO;

/**
 * ���� POJO ������ ����
 * 
 * @author Administrator
 * 
 */
public class LocalPOJOBuild extends XMLServiceBuilder {

    private static final Logger log = LoggerFactory.getLogger(LocalPOJOBuild.class, Logger.FRAMEWORK);

    private LocalPOJO localPOJO;

    public LocalPOJOBuild(Node node) throws Exception {
        initialize(node);
    }

    public void initialize(Node node) throws Exception {
        localPOJO = new LocalPOJO();
        try {
            localPOJO.setId(node.valueOf("@id"));
            localPOJO.setName(node.valueOf("@name"));
            localPOJO.setType(node.valueOf("@type"));
            localPOJO.setBusinessObjectClass(node.valueOf("@business-object-class"));
            localPOJO.setDescription(getDescription(node));
        } catch (Exception ex) {
            String msg = "LOCAL-POJO Service type domain build from XML error : " + ex.getMessage();
            log.error(msg, ex);
            throw new ServiceBuildException(msg, ex);
        }
    }

    /**
     * @return the localPOJO
     */
    public LocalPOJO getLocalPOJO() {
        return localPOJO;
    }

    /**
     * @param localPOJO
     *                the localPOJO to set
     */
    public void setLocalPOJO(LocalPOJO localPOJO) {
        this.localPOJO = localPOJO;
    }
}
