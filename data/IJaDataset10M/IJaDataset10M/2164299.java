package jlib.log;

import java.util.ArrayList;
import jlib.xml.Tag;

/**
 * @author PJD
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CallStackExclusion {

    CallStackExclusion() {
    }

    void fillExcluded(Tag tagSettings) {
        m_arrExclude = new ArrayList<String>();
        Tag tagCallLocation = tagSettings.getEnumChild("CallLocation");
        while (tagCallLocation != null) {
            String csExcludeName = tagCallLocation.getVal("Exclude");
            m_arrExclude.add(csExcludeName);
            tagCallLocation = tagSettings.getEnumChild("CallLocation");
        }
    }

    boolean doNotContains(String csClassName) {
        if (m_arrExclude != null) {
            int nNbExclusion = m_arrExclude.size();
            for (int n = 0; n < nNbExclusion; n++) {
                String csExclude = m_arrExclude.get(n);
                if (csClassName.startsWith(csExclude)) return false;
            }
        }
        return true;
    }

    private ArrayList<String> m_arrExclude = null;
}
