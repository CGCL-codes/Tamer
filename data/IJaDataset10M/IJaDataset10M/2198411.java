package taseanalyzer.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import taseanalyzer.model.EntityManager;

/**
 *
 * All TASE files have the same header and footer.
 * The header and footer provide the meta-data of the file: its date and type (ID).
 * Parsing of the header is done to validate that the file type parsed is correct.
 * This class provides generic functions to parse the header and footer of a
 * TASE file.
 * 
 * RECORD TYPE 01: HEADER
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |FIELD|         FIELD NAME        | LENGTH | PICTURE |                    REMARKS                   |
 * | NO. |                           |        |         |                                              |
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |  1  |RECORD TYPE                |    2   |9(2)     |VALUE = 01                                    |0-2
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |  2  |FILLER                     |    4   |9(4)     |ZEROES                                        |2-6
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |  3  |T.A.S.E. FILE ID           |    2   |9(2)     |VALUE = 20                                    |6-8
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |  4  |DATE                       |    6   |9(6)     |YYMMDD                                        |8-14
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |  5  |VERSION                    |    2   |9(2)     |                                              |14-16
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |  6  |FILLER                     |   52   |X(52)    |ZEROES                                        |16-68
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |  7  |T.A.S.E. FILE ID (4 CHRS)  |    4   |9(4)     |VALUE = 0020                                  |68-72
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 * |  8  |FILLER                     |    8   |X(8)     |ZEROES                                        |72-80
 * +-----+---------------------------+--------+---------+----------------------------------------------+
 *  
 * @author vainolo
 */
public abstract class TASEFileParser {

    private static TASE0020Parser tase0020Parser = new TASE0020Parser();

    private static TASE0023Parser tase0023Parser = new TASE0023Parser();

    private static TASE0024Parser tase0024Parser = new TASE0024Parser();

    private static TASE0028Parser tase0028Parser = new TASE0028Parser();

    private static TASE0030Parser tase0030Parser = new TASE0030Parser();

    private static TASE0035Parser tase0035Parser = new TASE0035Parser();

    private static TASE0036Parser tase0036Parser = new TASE0036Parser();

    private static TASE0068Parser tase0068Parser = new TASE0068Parser();

    private static TASE0803Parser tase0803Parser = new TASE0803Parser();

    private static Calendar calendar;

    protected static EntityManager em = EntityManager.INSTANCE;

    private String fileId;

    private String fileId2;

    private Date date;

    protected BufferedReader reader;

    protected List createdEntities = new ArrayList();

    private static Date calculateDate(int year, int month, int day) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    private void parseHeader() throws IOException {
        String line = reader.readLine();
        String recordType = line.substring(0, 2);
        if (!recordType.equals("01")) {
            throw new IllegalStateException("Expecting header line. Read\n" + line);
        }
        fileId = line.substring(6, 8);
        int year = Integer.parseInt(line.substring(8, 10));
        if (year > 50) year += 1900; else year += 2000;
        int month = Integer.parseInt(line.substring(10, 12)) - 1;
        int day = Integer.parseInt(line.substring(12, 14));
        date = calculateDate(year, month, day);
        fileId2 = line.substring(68, 72);
    }

    private void parseTrailer() {
    }

    protected abstract void parseSpecific() throws IOException;

    public void reset() {
        if (calendar != null) calendar.clear();
        if (createdEntities != null) createdEntities.clear();
    }

    public void setReader(BufferedReader reader) {
        date = null;
        this.reader = reader;
    }

    public void parse() throws IOException {
        parseHeader();
        parseSpecific();
        parseTrailer();
        reader.close();
    }

    public Date getTASEFileDate() {
        return date;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileId2() {
        return fileId2;
    }

    public List getCreatedEntities() {
        return createdEntities;
    }

    public static TASEFileParser getParser(String filename) {
        if (filename.startsWith("0020")) {
            return tase0020Parser;
        } else if (filename.startsWith("0023")) {
            return tase0023Parser;
        } else if (filename.startsWith("0030")) {
            return tase0030Parser;
        } else if (filename.startsWith("0035")) {
            return tase0035Parser;
        } else if (filename.startsWith("0803")) {
            return tase0803Parser;
        }
        return null;
    }
}
