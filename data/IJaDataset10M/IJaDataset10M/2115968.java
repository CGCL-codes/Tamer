package org.jabusuite.transaction;

import java.util.ArrayList;
import java.util.Iterator;
import org.jabusuite.article.ArticleVAT;

/**
 *
 * @author hilwers
 */
public class VATSumList extends ArrayList<VATSum> {

    /**
     * Returns the vat-sum of the specified vat
     * @param vat The vat to look for
     * @param vatSums The list of the vat-sums
     * @return 
     */
    protected VATSum getVatSum(ArticleVAT vat) {
        VATSum result = null;
        Iterator<VATSum> it = this.iterator();
        while ((result == null) && (it.hasNext())) {
            VATSum vatSum = it.next();
            if (vatSum.getVat().getId() == vat.getId()) result = vatSum;
        }
        return result;
    }
}
