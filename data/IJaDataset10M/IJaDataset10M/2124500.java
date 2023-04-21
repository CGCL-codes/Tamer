package org.avaje.ebean.server.deploy.xml;

import java.io.FileInputStream;
import java.util.List;
import org.avaje.lib.util.Dnode;
import org.avaje.lib.util.DnodeReader;

public class TestXmlReader {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        DnodeReader reader = new DnodeReader();
        FileInputStream fin = new FileInputStream("orm.xml");
        Dnode tree = reader.parseXml(fin);
        Dnode entityMappings = tree.find("entity-mappings");
        List<Dnode> entities = entityMappings.findAll("entity", 1);
        for (int i = 0; i < entities.size(); i++) {
            Dnode entity = entities.get(i);
            String entityClassName = (String) entity.getAttribute("class");
            List<Dnode> namedQueries = entity.findAll("named-query", 1);
            for (int j = 0; j < namedQueries.size(); j++) {
                Dnode namedQuery = namedQueries.get(i);
                String name = (String) namedQuery.getAttribute("name");
                Dnode query = namedQuery.find("query");
                String oql = query.getNodeContent();
                System.out.println(entityClassName + " " + name + " " + oql);
            }
        }
    }
}
