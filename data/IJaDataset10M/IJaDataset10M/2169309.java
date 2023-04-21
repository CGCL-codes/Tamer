package org.epoline.impexp.jsf.businesslogic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.epoline.service.support.PropertyException;

public class BatchPrinter {

    private String name;

    private String pageRange;

    private String destCountryGroup;

    private SendMethod sendType;

    private int startPage;

    private int endPage;

    private Hashtable countries = new Hashtable();

    private static ArrayList instances = new ArrayList();

    /**
	 * 
	 */
    public static BatchPrinter getBatchPrinter(int nofPages, String destCountry, int aSendType) {
        Iterator it = instances.iterator();
        while (it.hasNext()) {
            BatchPrinter cur = (BatchPrinter) it.next();
            if (cur.isValidFor(nofPages, destCountry, aSendType)) return cur;
        }
        return null;
    }

    public static List getInstances() {
        return instances;
    }

    public BatchPrinter(String number, String aPageRange, String aDestCountryGroup, String aSendType) throws PropertyException {
        super();
        name = number;
        pageRange = aPageRange;
        destCountryGroup = aDestCountryGroup;
        sendType = SendMethod.getByName(aSendType);
        if (sendType == null) throw new PropertyException("Invalid sendType: " + aSendType);
        initPageRange();
        instances.add(this);
    }

    public void addDestCountry(String aCountry) {
        countries.put(aCountry, aCountry);
    }

    public void setDestCountries(Hashtable aCountries) {
        countries = aCountries;
    }

    private void initPageRange() throws PropertyException {
        try {
            int index = pageRange.indexOf('-');
            if (index > 0) {
                startPage = Integer.parseInt(pageRange.substring(0, index));
                endPage = Integer.parseInt(pageRange.substring(index + 1));
            } else {
                index = pageRange.indexOf('>');
                if (index >= 0) {
                    startPage = Integer.parseInt(pageRange.substring(index + 1)) + 1;
                    endPage = Integer.MAX_VALUE;
                } else {
                    startPage = Integer.parseInt(pageRange);
                    endPage = Integer.parseInt(pageRange);
                }
            }
        } catch (NumberFormatException e) {
            throw new PropertyException(e.toString());
        }
    }

    public boolean isValidFor(int aPage, String aCountry, int aSendType) {
        if (aPage >= startPage) if (aPage <= endPage) if (countries.containsKey(aCountry)) {
            SendMethod s = SendMethod.getByOrdinal(aSendType);
            if (s != null) if (sendType == s) return true;
        }
        return false;
    }

    /**
	 * @return
	 */
    public String getName() {
        return name;
    }
}
