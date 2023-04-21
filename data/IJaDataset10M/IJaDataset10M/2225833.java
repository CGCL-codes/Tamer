package za.co.OO7J.queries;

import za.co.OO7J.AtomicPart;
import za.co.OO7J.utils.Persistence;
import za.co.OO7J.utils.RandomUtil;
import za.co.OO7J.utils.SettingsUtil;

/**
 * @author pvz 18-Apr-2006
 * 
 *         Comments are taken from the Paper: The OO7 Benchmark by M .J. Carey,
 *         D. J. DeWitt and J. F. Naughton
 * 
 *         Query #1 - Generate 10 random atomic part id's. For each part id
 *         generated lookup the atomic part with that id. Return the number of
 *         atomic parts processed
 * 
 *         OZONE implements only this method. They call it: matchQuery()
 * 
 */
public class Query1 implements QueryInterface {

    public int query() {
        int partId;
        for (int i = 0; i < SettingsUtil.Query1RepeatCnt; i++) {
            AtomicPart atomicPart = null;
            partId = (RandomUtil.nextInt() % SettingsUtil.TotalAtomicParts) + 1;
            atomicPart = (AtomicPart) Persistence.findAtomicPart(partId);
            if (atomicPart == null) {
                System.out.println("ERROR: Unable to find atomic part with id:" + partId);
            }
            if (SettingsUtil.debugMode) {
                System.out.println("In Query1, partId = " + partId);
            }
        }
        return SettingsUtil.Query1RepeatCnt;
    }
}
