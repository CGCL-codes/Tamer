package org.cmsuite2.util.comparator;

import java.util.Comparator;
import java.util.Date;
import org.cmsuite2.util.document.DocumentUtil;
import org.cmsuite2.util.document.IDocument;

public class IDocumentComparator implements Comparator<IDocument> {

    public int compare(IDocument id1, IDocument id2) {
        Date d1 = DocumentUtil.evaluate(id1).getExpireDate();
        Date d2 = DocumentUtil.evaluate(id2).getExpireDate();
        int deptComp = d2.compareTo(d1);
        return deptComp;
    }
}
