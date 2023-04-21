package bigraphspace.tools;

import bigraphspace.api.ReactiveBigraph;
import bigraphspace.io.IOConstants;
import java.io.File;
import org.apache.log4j.Logger;

/** Read BTL bigraph and write XML bigraph
 * @author cmg
 *
 */
public class Btl2Xml {

    /** logger */
    static Logger logger = Logger.getLogger(Btl2Xml.class);

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length != 2 && args.length != 3) {
            System.err.println("Usage: " + Btl2Xml.class.getName() + " infile.btl outfile.xml [signature.xml]");
            System.exit(-1);
        }
        try {
            FileUtils.convertFile(args.length > 2 ? new File(args[2]) : null, new File(args[0]), IOConstants.FORMAT_BTL, new File(args[1]), IOConstants.FORMAT_XML);
            System.out.println("Converted BTL " + args[0] + " to XML " + args[1] + (args.length > 2 ? " with signature " + args[2] : ""));
        } catch (Exception e) {
            logger.error("Converting BTL " + args[0] + " to XML " + args[1] + (args.length > 2 ? " with signature " + args[2] : ""), e);
            System.exit(-1);
        }
    }
}
