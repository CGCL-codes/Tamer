package com.mgensystems.jarindexer.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mgensystems.jarindexer.search.NonInteractiveSearch;
import com.mgensystems.jarindexer.search.format.BasicFormat;
import com.mgensystems.jarindexer.search.format.ResultFormat;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        NonInteractiveSearch search = new NonInteractiveSearch();
        ResultFormat format = new BasicFormat();
        Map fargs = new HashMap();
        fargs.put("repo", "c:\\mine\\m2\\.m2\\repository");
        format.setInputArgs(fargs);
        List<String> results = search.search("C:\\m2index", "all", format, "weblogic.utils.classloaders.GenericClassLoader", 1000);
        for (String item : results) {
            System.out.println(item);
        }
        System.out.println(results.size());
    }
}
