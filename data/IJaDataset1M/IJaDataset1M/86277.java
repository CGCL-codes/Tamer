package net.xmlmiddleware.aaf.plugin;

import net.xmlmiddleware.io.GetFileURL;
import net.xmlmiddleware.io.StringStore;
import net.xmlmiddleware.props.AAFProps;
import net.xmlmiddleware.props.XMLDBMSProps;
import net.xmlmiddleware.props.XSLTProps;
import net.xmlmiddleware.transform.xslt.ProcessXslt;
import net.xmlmiddleware.transform.xslt.XSLTLoader;

public class ProcessXSLT implements net.xmlmiddleware.aaf.AafPlugin {

    String sheet, xmlfile, outputfile;

    private ProcessXslt processxslt;

    /**
	 * ProcessXSLT constructor comment.
	 */
    public ProcessXSLT() {
        super();
    }

    /**
	 * Insert the method's description here.
	 * Creation date: (31/07/2002 13:31:48)
	 * @return java.lang.String
	 * @param param java.util.Properties
	 */
    public String init(java.util.Properties props) {
        GetFileURL gfu = new GetFileURL();
        XSLTLoader a = new XSLTLoader();
        String Error = "OK";
        try {
            processxslt = a.init(props);
            sheet = props.getProperty(XSLTProps.XSLTSCRIPT);
            xmlfile = props.getProperty(XMLDBMSProps.XMLFILE);
            outputfile = props.getProperty(XSLTProps.XSLTOUTPUT);
            sheet = gfu.fullqual(sheet);
            if (xmlfile != null) {
                xmlfile = gfu.fullqual(xmlfile);
            }
        } catch (Exception E) {
            Error = "ProcessXSLT.init() Error = " + E;
        }
        return "Error";
    }

    /**
	 * Insert the method's description here.
	 * Creation date: (31/07/2002 13:31:48)
	 * @return java.lang.String[]
	 */
    public java.lang.String[] req_props() {
        String[] Arg = new String[2];
        Arg[0] = XSLTProps.XSLTCLASS;
        Arg[1] = XSLTProps.XSLTSCRIPT;
        return Arg;
    }

    public java.lang.String[] opt_props() {
        String[] Arg = new String[0];
        return Arg;
    }

    /**
	 * Insert the method's description here.
	 * Creation date: (31/07/2002 13:31:48)
	 * @return java.lang.String
	 * @param param java.util.Hashtable
	 */
    public java.util.Hashtable run(java.util.Hashtable var) {
        String output = new String();
        String Data = (String) var.get(AAFProps.DATA);
        try {
            if (xmlfile != null) {
                output = processxslt.transformFiles(xmlfile, sheet);
                System.out.println(output);
            } else {
                output = processxslt.transformString(Data, sheet);
                System.out.println(output);
            }
            var.put(AAFProps.DATA, output);
        } catch (Exception e) {
            System.out.println("Problem running demo: " + e.toString());
        }
        return var;
    }
}
