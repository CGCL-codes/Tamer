package org.jiql.db;

import java.util.*;
import java.sql.*;
import java.io.*;
import org.jiql.util.JGNameValuePairs;
import tools.util.EZArrayList;
import tools.util.StringUtil;
import org.jiql.util.Criteria;
import org.jiql.util.SQLParser;
import org.jiql.util.Gateway;
import org.jiql.db.objs.jiqlCellValue;
import tools.util.NameValuePairs;
import org.jiql.util.JGException;
import org.jiql.util.JGUtil;

public class NextVal implements Serializable {

    Vector<String> nextval = new Vector<String>();

    public StringBuffer parse(String n, StringBuffer tok) {
        String tokstr = "nextval ";
        int i3 = tok.toString().toLowerCase().indexOf(tokstr);
        if (i3 > 0) {
            if (nextval.contains(n)) return tok;
            tok.replace(i3, i3 + tokstr.length(), " ");
            nextval.add(n);
            return tok;
        } else return tok;
    }
}
