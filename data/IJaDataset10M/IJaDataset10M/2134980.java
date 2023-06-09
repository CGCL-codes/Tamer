package org.makumba.providers.datadefinition.makumba;

import java.util.Vector;
import org.makumba.DataDefinition;
import org.makumba.DataDefinitionParseError;

/**
 * This is the field definition tokenizer. The lookup metods return false or null if the respective token has not been
 * encountered. The expect methods throw a DataDefinitionParseError instead. Some lookup methods simply do lookupLetters
 * for now, they can be refined later (e.g. letter followed by letters or digits, etc).
 */
public class FieldCursor {

    String toParse;

    int index = 0;

    RecordParser rp;

    public FieldCursor(RecordParser rp, String definition) {
        this.rp = rp;
        toParse = definition;
        index = definition.indexOf('=') + 1;
    }

    public DataDefinitionParseError fail(String reason) {
        return new DataDefinitionParseError(rp.dd.getName(), reason, toParse, index);
    }

    void skipBlank() {
        try {
            while (Character.isWhitespace(toParse.charAt(index))) index++;
        } catch (StringIndexOutOfBoundsException e) {
        }
    }

    /** check if the respective string follows, maybe after some spaces */
    public boolean lookup(String s) {
        skipBlank();
        int end = s.length() + index;
        try {
            if (toParse.substring(index, end).equals(s)) {
                index = end;
                return true;
            }
        } catch (StringIndexOutOfBoundsException e) {
        }
        return false;
    }

    /** expect the string to follow */
    public void expect(String s) throws DataDefinitionParseError {
        if (!lookup(s)) throw fail(s + " expected");
    }

    /** expect the whitespace */
    public void expectWhitespace() throws DataDefinitionParseError {
        int indexBefore = index;
        skipBlank();
        if (indexBefore == index) throw fail("some whitespace expected");
    }

    /** a type name */
    public String lookupTypeLiteral() {
        return lookupIdentifier();
    }

    public String expectTypeLiteral() throws DataDefinitionParseError {
        String ret = lookupTypeLiteral();
        if (ret == null) throw fail("type expected");
        return ret;
    }

    /** an enumerator name */
    public String lookupEnumName() throws DataDefinitionParseError {
        skipBlank();
        if (!lookup("\"")) throw fail("\"expected");
        return upToExpect("\"");
    }

    public String expectEnumName() throws DataDefinitionParseError {
        String ret = lookupEnumName();
        if (ret == null) throw fail("enumerator name expected");
        return ret;
    }

    /** a table name */
    public String lookupTableName() throws DataDefinitionParseError {
        skipBlank();
        int end = index;
        char c;
        try {
            while (Character.isLetter(c = toParse.charAt(end)) || Character.isDigit(c) || c == '_' || c == '-' || c == '.' || c == '/' || c == '>') end++;
        } catch (StringIndexOutOfBoundsException siob) {
        }
        if (end == index) return null;
        return upTo(end);
    }

    /** a group of letters */
    public String lookupLetters() {
        skipBlank();
        int end = index;
        try {
            while (Character.isLetter(toParse.charAt(end))) end++;
        } catch (StringIndexOutOfBoundsException siob) {
        }
        if (end == index) return null;
        return upTo(end);
    }

    public Integer lookupInteger() {
        skipBlank();
        int end = index;
        try {
            if (toParse.charAt(end) == '-') end++;
            while (Character.isDigit(toParse.charAt(end))) end++;
        } catch (StringIndexOutOfBoundsException siob) {
        }
        if (end == index) return null;
        try {
            return new Integer(upTo(end));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer expectInteger() throws DataDefinitionParseError {
        Integer i = lookupInteger();
        if (i == null) throw fail("Integer expected");
        return i;
    }

    String lookupIdentifier() {
        skipBlank();
        int end = index;
        try {
            if (!Character.isLetter(toParse.charAt(end))) return null;
            end++;
            char c;
            while (Character.isDigit(c = toParse.charAt(end)) || Character.isLetter(c) || c == '_' || c == '-') end++;
        } catch (StringIndexOutOfBoundsException siob) {
        }
        if (end == index) return null;
        return upTo(end);
    }

    String upToExpect(String s) throws DataDefinitionParseError {
        int beg = index;
        int end = s.length() + index;
        try {
            while (!toParse.substring(index, end).equals(s)) {
                index++;
                end++;
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw fail(s + " expected");
        }
        s = toParse.substring(beg, index);
        index = end;
        return s;
    }

    /** looks for a table specifier in the form tablename */
    DataDefinition lookupTableSpecifier() throws DataDefinitionParseError {
        int beg = index;
        String path = lookupTableName();
        if (path == null) return null;
        DataDefinition ri = null;
        try {
            ri = RecordInfo.getRecordInfo(path);
        } catch (org.makumba.DataDefinitionNotFoundError e) {
        }
        if (ri == null) {
            index = beg;
            return null;
        }
        return ri;
    }

    void expectCharEnum(FieldInfo fi) throws DataDefinitionParseError {
        Vector valueset = new Vector();
        fi.extra1 = valueset;
        int len = 0;
        String s = lookupEnumName();
        int ln;
        if (s != null) while (true) {
            ln = s.length();
            if (valueset.contains(s)) throw fail("repeated name: " + s);
            valueset.addElement(s);
            if (ln > len) len = ln;
            if (lookup(",")) {
                s = expectEnumName();
                continue;
            }
            break;
        }
        if (lookup("}")) {
            fi.extra2 = new Integer(len);
            return;
        }
        throw fail(" , or } epxected");
    }

    void expectIntEnum(FieldInfo fi) throws DataDefinitionParseError {
        Vector valueset = new Vector();
        Vector nameset = new Vector();
        Vector deprset = new Vector();
        fi.extra1 = valueset;
        fi.extra2 = nameset;
        fi.extra3 = deprset;
        String s = lookupEnumName();
        if (s != null) while (true) {
            if (nameset.contains(s)) throw fail("repeated name: " + s);
            nameset.addElement(s);
            expect("=");
            Integer val = expectInteger();
            valueset.addElement(val);
            if (lookup("deprecated")) deprset.addElement(val);
            if (lookup(",")) {
                s = expectEnumName();
                continue;
            }
            break;
        }
        if (!lookup("}")) throw fail("deprecated or , or } epxected");
    }

    public String lookupDescription() throws DataDefinitionParseError {
        if (lookup(";")) {
            try {
                return toParse.substring(index);
            } catch (StringIndexOutOfBoundsException siobe) {
                return null;
            }
        }
        try {
            if (toParse.substring(index).trim().length() != 0) throw fail("end of definition expected");
        } catch (StringIndexOutOfBoundsException siobe) {
        }
        return null;
    }

    String upTo(int end) {
        String ret = toParse.substring(index, end);
        index = end;
        return ret;
    }

    public void substitute(int l, String s) {
        index -= l;
        toParse = toParse.substring(0, index) + s + toParse.substring(index + l);
    }
}
