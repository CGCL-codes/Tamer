package samples;

import org.nargila.treexml.*;
import org.nargila.util.*;
import java.io.*;
import org.w3c.dom.*;

public class TreeDefault extends SampleApp {

    public TreeDefault(Reader xml) throws Exception {
        super("TreeDefault", xml);
    }

    protected TreeXml build(Document doc) throws Exception {
        TreeXml target = new TreeXml();
        ComponentBuilder builder = new ComponentBuilder();
        builder.setResource("target", target);
        builder.buildData(doc);
        target.setTree(builder.getData());
        return target;
    }

    public static void main(String args[]) throws Exception {
        if (args.length > 1) {
            usage();
        }
        Reader xml = 0 != args.length ? new FileReader(args[0]) : JarUtils.resourceReader(TreeDefault.class, "data/example.xml");
        new TreeDefault(xml);
    }
}
