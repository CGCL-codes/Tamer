package org.capelin.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.capelin.core.models.CapelinRecord;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;

/**
 * <a href="http://code.google.com/p/capline-opac/">Capelin-opac</a>
 * License: GNU AGPL v3 | http://www.gnu.org/licenses/agpl.html
 * 
 * An template importer.
 * This class gives most basic structure of the importer.
 * There is another option to extend from ImpoterTemplate
 * 
 * @author Jing Xiao <jing.xiao.ca at gmail dot com>
 * @see ImpoterTemplate
 * 
 */
public abstract class AbstractMarcImporter {

    protected InputStream input;

    protected Map<String, Integer> tagMap = new HashMap<String, Integer>();

    /**
	 * Assume you are going to overwrite this method to setup the input stream
	 */
    protected abstract void setUp() throws IOException;

    /**
	 * Main function to build flag
	 * true to display all tags and code
	 * false to display the length.
	 * 
	 * @param flag  
	 * @throws Exception
	 */
    public void run(boolean flag) throws Exception {
        setUp();
        long start = System.currentTimeMillis();
        int total;
        if (flag) {
            total = importData();
        } else {
            total = displayTags();
        }
        input.close();
        System.out.println("Total Records: " + total);
        System.out.println("Time: " + (System.currentTimeMillis() - start));
        System.exit(0);
    }

    /**
	 * Main function that to import to data.
	 * 
	 * @param flag  
	 * @throws Exception
	 */
    public void run() throws Exception {
        setUp();
        long start = System.currentTimeMillis();
        int total = importData();
        input.close();
        System.out.println("Total Records: " + total);
        System.out.println("Time: " + (System.currentTimeMillis() - start));
        System.exit(0);
    }

    protected int importData() throws Exception {
        MarcReader reader = new MarcStreamReader(input);
        Record record = null;
        int index = 0;
        while (reader.hasNext()) {
            index++;
            record = reader.next();
            buildRecord(record);
        }
        return index;
    }

    /**
	 * This method is designed as a template method to build the CapelinRecord from MARC
	 * @param Record
	 * @return CapelinRecord
	 */
    protected abstract CapelinRecord buildRecord(Record r);

    protected int displayTags() {
        MarcReader reader = new MarcStreamReader(input);
        Record record = null;
        int index = 0;
        TagBuilder tagBuilder = new TagBuilder();
        while (reader.hasNext()) {
            index++;
            record = reader.next();
            tagBuilder.build(record);
        }
        tagBuilder.display(true);
        return index;
    }

    protected RecordUtil util = RecordUtil.getInstance();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        input.close();
    }
}
