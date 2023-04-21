package edu.uchicago.lib;

import edu.uchicago.lib.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;

public class SearchKey {

    public static final int INT = 0, STRING = 1;

    protected static class KeyInfo {

        String field;

        String column;

        int colType;

        String join;

        public KeyInfo(String field, String column, int colType) {
            this.field = field;
            this.column = column;
            this.colType = colType;
        }

        public KeyInfo(String field, String column, int colType, String join) {
            this(field, column, colType);
            this.join = join;
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("KeyInfo: field=").append(this.field);
            buf.append(", column=").append(this.column);
            buf.append(", colType=").append(this.colType);
            return buf.toString();
        }
    }

    Hashtable keyHash;

    KeyInfo keyInfo;

    String value = null;

    int val = -1;

    public SearchKey(String field, String value, Hashtable argKeyHash) {
        keyHash = argKeyHash;
        if (!validKeys().contains(field)) {
            throw new IllegalArgumentException("Unknown search key: " + field);
        }
        this.keyInfo = (KeyInfo) keyHash.get(field);
        if (this.keyInfo.colType == INT) {
            this.value = value;
            try {
                this.val = Integer.parseInt(this.value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(field + " requires an integer value");
            }
        } else if (this.keyInfo.colType == STRING) {
            this.value = value;
        } else {
            throw new RuntimeException("Impossible colType: " + this.keyInfo.colType);
        }
    }

    public String join() {
        return this.keyInfo.join;
    }

    public String field() {
        return this.keyInfo.field;
    }

    public String column() {
        return this.keyInfo.column;
    }

    public String value() {
        return value;
    }

    public int val() {
        return val;
    }

    public int colType() {
        return this.keyInfo.colType;
    }

    public Collection validKeys() {
        return keyHash.keySet();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("SearchKey: field=").append(this.keyInfo.field);
        buf.append(", column=").append(this.keyInfo.column);
        buf.append(", colType=").append(this.keyInfo.colType);
        buf.append(", value=").append(this.value);
        buf.append(", int val=").append(this.val);
        return buf.toString();
    }
}
