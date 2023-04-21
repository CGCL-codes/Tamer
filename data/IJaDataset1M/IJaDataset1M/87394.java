package org.vramework.mvc.web.compiletimetags;

import org.vramework.commons.config.VConf;
import org.vramework.commons.datatypes.VStrBuilder;
import org.vramework.mvc.IMarkupTag;
import org.vramework.mvc.web.ICompileTimeTagRenderer;

/**
 * Returns the Java code contained in the tag. The code will be added as is to the view renderers code.
 * 
 * @author thomas.mahringer
 */
public class CurrentUser implements ICompileTimeTagRenderer {

    protected static final String lf = VConf.getLineSep();

    @Override
    public String render(IMarkupTag tag) {
        VStrBuilder code = new VStrBuilder(255);
        String remoteUserVar = "remoteUser" + tag.getBeginPos();
        code.append("  String ", remoteUserVar, " = req.getRemoteUser();", lf);
        code.append("  if (", remoteUserVar, " == null) {", lf);
        code.append("    ", remoteUserVar, " = \"No User logged in\";", lf);
        code.append("     out.write(", remoteUserVar, ");", lf);
        code.append("  } else { ", lf);
        code.append("     out.write(", remoteUserVar, ");", lf);
        code.append("     out.write(\"&nbsp;&nbsp;\");", lf);
        code.append("     out.write(\"<a href=\\\"\");", lf);
        code.append("     out.write(appContext);", lf);
        code.append("     out.write(\"/logout\");", lf);
        code.append("     out.write(\"\\\">Logout</a>\");", lf);
        code.append("  }", lf);
        return code.toString();
    }
}
