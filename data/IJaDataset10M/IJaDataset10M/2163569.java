package prefwork.datasource;

import java.util.List;
import prefwork.Attribute;
import prefwork.CommonUtils;

public class NetFlix extends CSVDataSource {

    IMDbMemory IMDb = new IMDbMemory();

    public List<Object> getRecord() {
        if (!hasNextRecord()) return null;
        List<Object> l = CommonUtils.getList(8);
        l.add(record.get(1));
        l.add(record.get(0));
        l.add(record.get(2));
        l.addAll(IMDb.getMovie(Integer.parseInt(record.get(0).toString())));
        l.add(IMDb.getPlot(Integer.parseInt(record.get(0).toString())));
        advanceRecords();
        return l;
    }

    /**
	 * 
	 */
    public Attribute[] getAttributes() {
        Attribute[] imdb = IMDb.getAttributes();
        Attribute[] temp = new Attribute[3 + imdb.length];
        temp[0] = attributes[1];
        temp[1] = attributes[0];
        temp[2] = attributes[2];
        for (int i = 0; i < imdb.length; i++) {
            imdb[i].setIndex(i + 3);
            temp[i + 3] = imdb[i];
        }
        return temp;
    }

    /**
	 * 
	 */
    public String[] getAttributesNames() {
        Attribute[] imdb = IMDb.getAttributes();
        String[] temp = new String[3 + imdb.length];
        temp[0] = attributes[1].getName();
        temp[1] = attributes[0].getName();
        temp[2] = attributes[2].getName();
        for (int i = 0; i < imdb.length; i++) {
            imdb[i].setIndex(i + 3);
            temp[i + 3] = imdb[i].getName();
        }
        return temp;
    }
}
