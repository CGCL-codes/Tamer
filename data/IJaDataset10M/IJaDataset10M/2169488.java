package com.bening.smsapp.dao;

import java.util.*;
import java.sql.*;
import com.bening.smsapp.bean.*;

public interface SmsAppDao {

    public void insertParsingResultAscii(List parseResult, String filename) throws SQLException;

    public void insertParsingResultAsn1(List beanList, String filename) throws SQLException;

    public List getFileList(int flagType);

    public void updateFileListFlag(int updateTo, List fileList);

    public void insertParsingAsciiRejected(String filename, String exception, String callReff, int lineNumber);
}
