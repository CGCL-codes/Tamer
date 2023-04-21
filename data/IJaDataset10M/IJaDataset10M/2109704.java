package testings;

import com.ideals.weavec.cpr.cprbuilding.CPRTreeBuilder;
import com.ideals.weavec.cpr.CPRBuilder;
import com.ideals.weavec.cpr.CPRBuilder;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: ByelasH
 * Date: 18-mei-2005
 * Time: 11:07:51
 * To change this template use File | Settings | File Templates.
 */
public class TestAssembling {

    public static void main(String[] args) {
        CPRBuilder builder = new CPRBuilder("tests\\deepstructure\\deepstructure.xml");
        String curDir = System.getProperty("user.dir");
        File dir = new File(curDir + "\\tests\\input\\testmap");
        String str = dir.getAbsolutePath();
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            File child = new File(dir, children[i]);
            String path = child.getAbsolutePath();
            System.out.println(i + " -- " + path);
            try {
                builder.createComposedModel(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
