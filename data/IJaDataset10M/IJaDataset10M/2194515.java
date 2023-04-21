package com.akcess.vo;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import java.io.Serializable;

/**
* 
* Instance of this class stores the number of
* records in log_usuario table
* 
*/
public class Log_usuarioRecordCount implements Serializable {

    private int count;

    public Log_usuarioRecordCount() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.akcess.vo.Log_usuarioRecordCount :");
        ret.append("count ='" + count + "'");
        return ret.toString();
    }
}
