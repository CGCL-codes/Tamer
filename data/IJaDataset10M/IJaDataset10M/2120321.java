package factory.sql;

import java.util.StringTokenizer;
import java.util.Vector;
import org.dlib.tools.TVector;
import org.dlib.tools.Util;
import druid.core.AttribList;
import druid.core.AttribSet;
import druid.core.DataTracker;
import druid.core.DruidException;
import druid.data.DatabaseNode;
import druid.data.FieldAttribs;
import druid.data.FieldNode;
import druid.data.TableNode;

public class SqlUtil {

    public static void syncIndexes(AttribList alData, AttribList alAttribs) {
        DataTracker.beginDisabledSection();
        Vector vIndexes = new Vector();
        for (int i = 0; i < alAttribs.size(); i++) {
            AttribSet as = alAttribs.get(i);
            String scope = as.getString("scope");
            if (scope.equals(FieldAttribs.SCOPE_INDEX) || scope.equals(FieldAttribs.SCOPE_UINDEX) || scope.equals(FieldAttribs.SCOPE_FTINDEX)) vIndexes.add(as);
        }
        for (int i = 0; i < alData.size(); i++) {
            int idxID = alData.get(i).getInt("id");
            boolean found = false;
            for (int j = 0; j < vIndexes.size(); j++) {
                AttribSet as = (AttribSet) vIndexes.get(j);
                if (as.getInt("id") == idxID) {
                    found = true;
                    break;
                }
            }
            if (!found) alData.remove(i--);
        }
        for (int i = 0; i < vIndexes.size(); i++) {
            AttribSet as = (AttribSet) vIndexes.get(i);
            if (alData.size() - 1 < i) {
                AttribSet newAS = alData.append();
                newAS.setInt("id", as.getInt("id"));
                newAS.setString("index", as.getString("name"));
            } else {
                AttribSet idxAS = alData.get(i);
                if (as.getInt("id") == idxAS.getInt("id")) {
                    idxAS.setString("index", as.getString("name"));
                } else {
                    AttribSet newAS = alData.insert(i);
                    newAS.setInt("id", as.getInt("id"));
                    newAS.setString("index", as.getString("name"));
                }
            }
        }
        DataTracker.endDisabledSection();
    }

    public static String generateIndex(TableNode node, AttribSet indexAS, TVector fields, boolean unique, int cnt, AttribList idxAL, boolean hasFullText) {
        int id = indexAS.getInt("id");
        String templ = indexAS.getString("sqlName");
        String table = node.attrSet.getString("name");
        AttribSet optAS = idxAL.find(id);
        if (optAS == null) throw new DruidException(DruidException.INC_STR, "Index not found in AttribList", "id:" + id + ", AL:" + idxAL);
        String name = optAS.getString("name");
        if (name.equals("")) name = templ;
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE ");
        sb.append(unique ? "UNIQUE " : "");
        if (hasFullText && indexAS.getString("scope").equals(FieldAttribs.SCOPE_FTINDEX)) {
            sb.append("FULLTEXT ");
        }
        sb.append("INDEX ");
        sb.append(expandTemplate(name, table, fields, cnt));
        sb.append(" ON " + table + "(");
        sb.append(fields.toString() + ")");
        return sb.toString();
    }

    public static String expandTemplate(String template, String table, TVector fields, int cnt) {
        String oldSep = fields.getSeparator();
        String s = template;
        s = Util.replaceStr(s, "{table}", table);
        fields.setSeparator("_");
        s = Util.replaceStr(s, "{fields}", fields.toString());
        s = Util.replaceStr(s, "{cnt}", Integer.toString(cnt));
        fields.setSeparator(oldSep);
        return s;
    }

    public static String getNameFromCode(String code) {
        StringTokenizer st = new StringTokenizer(code, " ");
        if (st.countTokens() < 3) return "???";
        String token1 = st.nextToken().toLowerCase();
        String token2 = st.nextToken().toLowerCase();
        String token3 = st.nextToken();
        if (token2.equals("or") && token3.toLowerCase().equals("replace")) {
            if (!st.hasMoreTokens()) return "???";
            st.nextToken();
            if (!st.hasMoreTokens()) return "???";
            token3 = st.nextToken();
        }
        if (token2.equals("materialized") && token3.toLowerCase().equals("view")) {
            if (!st.hasMoreTokens()) return "???";
            token3 = st.nextToken();
        }
        StringBuffer name = new StringBuffer();
        for (int i = 0; i < token3.length(); i++) {
            char c = token3.charAt(i);
            if (c == '(' || Character.isWhitespace(c)) break;
            name.append(c);
        }
        return name.toString();
    }

    public static String getTypeFromCode(String code) {
        StringTokenizer st = new StringTokenizer(code, " ");
        if (st.countTokens() < 3) return "???";
        String token1 = st.nextToken().toLowerCase();
        String token2 = st.nextToken().toLowerCase();
        String token3 = st.nextToken();
        String name = "";
        if (token2.equals("or") && token3.toLowerCase().equals("replace")) {
            if (!st.hasMoreTokens()) return "???";
            if (!st.hasMoreTokens()) return "???";
            token3 = st.nextToken();
            name = token3;
        } else if (token2.equals("materialized") && token3.toLowerCase().equals("view")) name = "MATERIALIZED VIEW"; else name = token2;
        return name;
    }

    public static Vector getForeignKeys(TableNode tableNode) {
        DatabaseNode dbNode = tableNode.getDatabase();
        Vector v = new Vector();
        for (int i = 0; i < tableNode.getChildCount(); i++) {
            FieldNode f = (FieldNode) tableNode.getChild(i);
            int type = f.attrSet.getInt("type");
            int refT = f.attrSet.getInt("refTable");
            if (type == 0 && refT != 0) {
                AttribSet as = f.attrSet;
                int refTable = as.getInt("refTable");
                int refField = as.getInt("refField");
                String fName = as.getString("name");
                String match = as.getString("matchType");
                String onUpd = as.getString("onUpdate");
                String onDel = as.getString("onDelete");
                TableNode fkeyTNode = dbNode.getTableByID(refTable);
                FieldNode fkeyFNode = fkeyTNode.getFieldByID(refField);
                String fkTable = fkeyTNode.attrSet.getString("name");
                String fkField = fkeyFNode.attrSet.getString("name");
                v.addElement(new FKeyEntry(fName, fkTable, fkField, match, onUpd, onDel));
            }
        }
        for (int i = 0; i < v.size() - 1; i++) {
            FKeyEntry fke1 = (FKeyEntry) v.elementAt(i);
            FKeyEntry fke2 = (FKeyEntry) v.elementAt(i + 1);
            if (fke1.merge(fke2)) {
                v.remove(i + 1);
                i--;
            }
        }
        return v;
    }

    public static Vector getForeignKeys(TableNode tableNode, String tableNameReferenced) {
        Vector v = getForeignKeys(tableNode);
        for (int i = 0; i < v.size(); i++) {
            FKeyEntry fke = (FKeyEntry) v.elementAt(i);
            if (!fke.fkTable.equals(tableNameReferenced)) {
                v.remove(i);
                i--;
            }
        }
        return v;
    }
}
