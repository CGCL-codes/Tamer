package blueprint4j.db.utils;

import java.sql.*;
import java.io.*;
import java.util.*;
import blueprint4j.db.*;
import blueprint4j.utils.*;

public interface Scheduable {

    /**
	 * Return the status of the run
	 * For example, executed fine, 500 line added
	 */
    public String runSchedule(DBConnection dbcon) throws Throwable;
}
