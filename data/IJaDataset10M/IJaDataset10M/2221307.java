package uk.icat3;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import uk.icat3.entity.EntityBaseBean;
import uk.icat3.exceptions.IcatInternalException;
import uk.icat3.manager.EntityInfoHandler;
import uk.icat3.manager.EntityInfoHandler.KeyType;
import uk.icat3.manager.EntityInfoHandler.Relationship;

public class DocGenerator {

    private static EntityInfoHandler eiHandler = EntityInfoHandler.getInstance();

    public static void main(String[] args) throws IcatInternalException {
        try {
            File dir = new File("src/main/java/uk/icat3/entity");
            PrintWriter out = new PrintWriter(new File("icat.html"));
            out.print("<!DOCTYPE HTML><html><head><style type=\"text/css\">h1,h2,h3 {color:sienna;} table { border-collapse:collapse; } td, th { border:1px solid sienna; padding:4px; font-weight:normal; text-align:left} th { color:sienna; }</style><title>ICAT Schema</title><link rel=\"icon\" href=\"http://www.icatproject.org/favicon.ico\"/></head><body><h1>ICAT Schema</h1>");
            List<String> cnames = new ArrayList<String>();
            for (File f : dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    String name = pathname.getName();
                    return name.endsWith(".java") && !Arrays.asList("Comment", "EntityBaseBean", "Parameter").contains(name.replace(".java", ""));
                }
            })) {
                String cname = f.getName().replace(".java", "");
                cnames.add(cname);
            }
            Collections.sort(cnames);
            out.print("<p style=\"max-width:50em;\">");
            boolean first = true;
            for (String cname : cnames) {
                if (first) {
                    first = false;
                } else {
                    out.print(", ");
                }
                out.print("<a href = \"#" + cname + "\">" + cname + "</a>");
            }
            out.print("</p>");
            for (String cname : cnames) {
                out.print("<hr/><h2 id=\"" + cname + "\">" + cname + "</h2>");
                Class<?> klass = Class.forName("uk.icat3.entity." + cname);
                @SuppressWarnings("unchecked") Class<? extends EntityBaseBean> eklass = (Class<? extends EntityBaseBean>) klass;
                String classComment = eiHandler.getClassComment(eklass);
                if (classComment == null) {
                    System.out.println(cname + " has no comment");
                } else {
                    out.println("<p>" + classComment + "</p>");
                }
                Set<Field> fields = eiHandler.getGetters(eklass).keySet();
                Map<Field, String> fieldComments = eiHandler.getFieldComments(eklass);
                Set<Field> notnullables = new HashSet<Field>(eiHandler.getNotNullableFields(eklass));
                Map<Field, Integer> stringFields = eiHandler.getStringFields(eklass);
                Field key = eiHandler.getKeyFor(eklass);
                KeyType keyType = eiHandler.getKeytype(eklass);
                String comments = fieldComments.get(key);
                String keyTypeString = "";
                if (keyType == KeyType.GENERATED) {
                    keyTypeString = " auto generated";
                }
                out.print("<p><b>Key</b> " + key.getName() + keyTypeString + ((comments == null) ? "" : (": " + comments)) + "</p>");
                fields.remove(key);
                for (List<Field> constraint : eiHandler.getConstraintFields(eklass)) {
                    out.print("<p><b>Constraint</b> ");
                    first = true;
                    for (Field f : constraint) {
                        if (first) {
                            first = false;
                        } else {
                            out.print(", ");
                        }
                        out.print(f.getName());
                    }
                    out.println("</p>");
                }
                out.println("<h3>Relationships</h3><table><tr><th>Card</th><th>Class</th><th>Field</th><th>Cascaded</th><th>Description</th></tr>");
                for (Relationship r : eiHandler.getRelatedEntities(eklass)) {
                    Field f = r.getField();
                    boolean notnullable = notnullables.contains(f);
                    boolean many = r.isCollection();
                    String beanName = r.getBean().getSimpleName();
                    String card = (notnullable ? "1" : "0") + "," + (many ? "*" : "1");
                    String cascaded = (r.isCascaded() ? "Yes" : "");
                    out.print("<tr><td> " + card + "</td>");
                    out.print("<td><a href = \"#" + beanName + "\">" + beanName + "</a></td><td>" + f.getName() + "</td><td>" + cascaded + "</td>");
                    comments = fieldComments.get(f);
                    out.println("<td>" + ((comments == null) ? "" : comments) + "</td></tr>");
                    fields.remove(f);
                }
                out.println("</table>");
                if (!fields.isEmpty()) {
                    out.println("<h3>Other fields</h3>");
                    out.println("<table><tr><th>Field</th><th>Type</th><th>Description</th></tr>");
                    for (Field f : fields) {
                        String type = f.getType().getSimpleName();
                        Integer length = stringFields.get(f);
                        if (length != null) {
                            type = type + " [" + length + "]";
                        }
                        if (notnullables.contains(f)) {
                            type = type + " NOT NULL";
                        }
                        comments = fieldComments.get(f);
                        out.print("<tr><td>" + f.getName() + "</td><td>" + type + "</td>");
                        out.println("<td>" + ((comments == null) ? "" : comments) + "</td></tr>");
                    }
                    out.println("</table>");
                }
            }
            out.print("</body></html>");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
