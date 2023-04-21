package jmysql.util.run.imp;

import java.io.IOException;
import java.util.List;
import jmysql.util.run.RunImpl;

/**
 * @author zuora
 *
 */
public interface Run {

    public static final String RUN_SH = "/myapps/com/jmysql/util/run/imp/run.sh";

    public static final Run getRUN = new RunImpl();

    public List<String> runBash(String[] strs, String str);

    public List<String> returnReults(Process p) throws IOException;

    public String[] init(String str);

    /**
	 * display result.
	 * 
	 * @param l
	 */
    public void Display(List<String> l);

    public Run getInstance();
}
