package rubbish.db.scaffold.template.page;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.SortedSet;
import rubbish.db.gen.template.AbstractJavaTemplate;
import rubbish.db.util.StringUtils;

/**
 * NewPage�̃e���v���[�g
 *
 * @author $Author: winebarrel $
 * @version $Revision: 1.9 $
 */
public class NewPageTemplate extends AbstractJavaTemplate {

    public NewPageTemplate(String pkg, String destdir) {
        super(pkg, destdir);
    }

    public boolean isWritable(String clsname, LinkedHashMap props, LinkedHashMap sizes, LinkedHashMap nullables, LinkedHashSet pks, String[] parentClasses, String[] childClasses) {
        return (pks.size() > 0);
    }

    public String getFilename(String clsname) {
        return StringUtils.capitalizeFully(clsname) + "New.java";
    }

    protected void processImports(SortedSet imports, String clsname) {
        imports.add("net.sf.click.Page");
        imports.add("net.sf.click.control.ActionLink");
        imports.add("net.sf.click.control.FieldSet");
        imports.add("net.sf.click.control.Form");
        imports.add("net.sf.click.control.Submit");
        imports.add("rubbish.db.manager.DatabaseManager");
    }

    protected void body(PrintWriter writer, String clsname, LinkedHashMap props, LinkedHashMap sizes, LinkedHashMap nullables, LinkedHashSet pks, SortedSet imports, String[] parentClasses, String[] childClasses) {
        String lclsname = clsname.toLowerCase();
        String pclsname = StringUtils.capitalizeFully(clsname);
        writer.println("public class " + pclsname + "New extends Page {");
        writer.println();
        writer.println("    public Form form = new Form();");
        writer.println();
        writer.println("    public ActionLink back = new ActionLink(this, \"onBackClick\");");
        writer.println();
        writer.println("    public " + pclsname + "New() {");
        writer.println("        FieldSet fieldSet = new FieldSet(\"" + clsname + "\");");
        writer.println("        form.add(fieldSet);");
        for (Iterator ite = props.keySet().iterator(); ite.hasNext(); ) {
            String colname = ((String) ite.next()).toUpperCase();
            writer.println("        fieldSet.add(FieldUtils.getField(" + clsname + "." + colname + "));");
        }
        writer.println("        form.add(new Submit(\"create\", this, \"onCreateClick\"));");
        writer.println("    }");
        writer.println();
        writer.println("    public void onDestroy() {");
        writer.println("        DatabaseManager.disconnect();");
        writer.println("    }");
        writer.println();
        writer.println("    /////////////////////////////////////////////////////////////////");
        writer.println();
        writer.println("    public boolean onCreateClick() {");
        writer.println("        " + clsname + " " + lclsname + " = new " + clsname + "();");
        writer.println("        form.copyTo(" + lclsname + ");");
        writer.println("        " + lclsname + ".save();");
        writer.println();
        writer.println("        " + pclsname + "Show " + lclsname + "Show = (" + pclsname + "Show) getContext().createPage(" + pclsname + "Show.class);");
        writer.println("        " + lclsname + "Show.set" + clsname + "(" + lclsname + ");");
        writer.println("        setForward(" + lclsname + "Show);");
        writer.println("        return false;");
        writer.println("    }");
        writer.println();
        writer.println("    public boolean onBackClick() {");
        writer.println("        setForward(" + pclsname + "List.class);");
        writer.println("        return false;");
        writer.println("    }");
        writer.println();
        writer.println("}");
    }
}
