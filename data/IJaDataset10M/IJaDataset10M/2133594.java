package atma.jconfiggen.sax;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import atma.jconfiggen.Config;
import atma.jconfiggen.Param;
import atma.jconfiggen.Expr;
import atma.jconfiggen.Seq;

class RootH extends ElemH {

    private static final AttrMatcher configAttr = new AttrMatcher("config", "package", "class");

    private ElemH notconfig() throws SAXException {
        return complain("Root element must be config");
    }

    RootH(SAXHandler s) {
        super(s);
    }

    ElemH elem(SAXHandler.Elem e, Attributes attr) throws SAXException {
        if (e != SAXHandler.Elem.CONFIG) notconfig();
        String[] atv = configAttr.match(saxh.loc, attr);
        if (atv[1] == null) missAttr("config", "class");
        ArrayList<String> imps = new ArrayList<String>(20);
        ArrayList<Param> params = new ArrayList<Param>(5);
        ArrayList<Expr> sub = new ArrayList<Expr>(10);
        saxh.config = new Config(atv[0], atv[1], imps, params, new Seq(sub));
        return new ConfigH(saxh, imps, params, sub);
    }

    ElemH elemstr(String tag, Attributes attr) throws SAXException {
        if (tag.equals("config")) complain("The config tag needs to be in the \"" + SAXHandler.nmsp + "\" namespace");
        return notconfig();
    }
}
