package net.sf.refactorit.refactorings.conflicts;

import net.sf.refactorit.classmodel.BinMember;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 *
 * @author Tonis Vaga
 */
public class ConflictRepositoryTest extends TestCase {

    Random rand = new Random(System.currentTimeMillis());

    int N = 100;

    int M = 5;

    BinMember members[] = new BinMember[N];

    BinMember targets[] = new BinMember[N];

    Conflict[] randConflicts = new Conflict[M];

    Conflict[] uniqueConflicts = new Conflict[M];

    public void setUp() {
    }

    public ConflictRepositoryTest() {
    }

    public void testAddConflictForMemberAndTarget() throws Exception {
        initConflicts();
        ConflictRepository rep = new ConflictRepository();
        for (int i = 0; i < members.length; i++) {
            members[i] = new MockBinMember();
            targets[i] = members[rand.nextInt(i + 1)];
            for (int k = 0; k < uniqueConflicts.length; k++) {
                rep.addConflict(uniqueConflicts[k], members[i]);
                rep.addConflict(uniqueConflicts[k], members[i], targets[i]);
            }
        }
        for (int i = 0; i < members.length; i++) {
            for (int k = 0; k < uniqueConflicts.length; ++k) {
                assertTrue(rep.getConflict(members[i], uniqueConflicts[k].getType()) == uniqueConflicts[k]);
                assertTrue(rep.getConflict(members[i], uniqueConflicts[k].getType(), targets[i]) == uniqueConflicts[k]);
            }
        }
        for (int i = 0; i < members.length; i++) {
            for (int k = 0; k < uniqueConflicts.length; ++k) {
                rep.removeConflict(members[i], uniqueConflicts[k].getType());
                assertTrue(rep.getConflict(members[i], uniqueConflicts[k].getType()) == null);
            }
        }
    }

    public void testAddConflictForMemberListAndTarget() throws Exception {
        initConflicts();
        ConflictRepository rep = new ConflictRepository();
        final int K = 10;
        List uniqueLists[] = new List[K];
        for (int i = 0; i < uniqueLists.length; i++) {
            uniqueLists[i] = new ArrayList(1);
            uniqueLists[i].add(new Integer(i));
        }
        for (int i = 0; i < members.length; i++) {
            members[i] = new MockBinMember();
            targets[i] = members[rand.nextInt(i + 1)];
            for (int j = 0; j < uniqueLists.length; ++j) {
                rep.addConflict(uniqueConflicts[0], members[i], uniqueLists[j], targets[i]);
            }
        }
        for (int i = 0; i < members.length; i++) {
            for (int j = 0; j < uniqueLists.length; ++j) {
                assertTrue(rep.getConflict(members[i], uniqueLists[j], targets[i]) == uniqueConflicts[0]);
                List list = new ArrayList(uniqueLists[j]);
                assertTrue("getConflict doesn't work with different list instance", rep.getConflict(members[i], list, targets[i]) == uniqueConflicts[0]);
                rep.removeConflict(members[i], list, targets[i]);
                assertTrue(rep.getConflict(members[i], uniqueLists[j], targets[i]) == null);
            }
        }
    }

    public static Test suite() {
        return new TestSuite(ConflictRepositoryTest.class);
    }

    Conflict generateRandomConflict() {
        return new MockConflict(new ConflictType(rand.nextInt(26) + 1));
    }

    private void initConflicts() {
        for (int k = 0; k < randConflicts.length; ++k) {
            randConflicts[k] = generateRandomConflict();
        }
        final int MAX_CONFLICTS = 27;
        assertTrue(uniqueConflicts.length < MAX_CONFLICTS);
        for (int i = 0; i < uniqueConflicts.length; i++) {
            uniqueConflicts[i] = new MockConflict(new ConflictType(i + 1));
        }
    }
}
