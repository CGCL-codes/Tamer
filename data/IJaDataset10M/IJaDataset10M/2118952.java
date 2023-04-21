package com.docum.util.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.docum.domain.po.common.Cargo;
import com.docum.domain.po.common.Container;
import com.docum.test.data.AllDataPreparator;

public class StatsHelperTest {

    private AllDataPreparator preparator;

    @Before
    public void setUp() {
        preparator = new AllDataPreparator();
        preparator.prepareAllData();
    }

    @Test
    public void testGetCargoGroups() {
        Collection<StatsCargoGroup> groups = StatsHelper.getCargoGroups(preparator.getContainers());
        Assert.assertFalse(groups.isEmpty());
        List<Cargo> groupCargoes = new ArrayList<Cargo>();
        List<Cargo> actualCargoes = new ArrayList<Cargo>();
        Set<StatsCargoGroupInfo> keys = new HashSet<StatsCargoGroupInfo>();
        for (Container container : preparator.getContainers()) {
            if (container.getInspection() != null) {
                for (Cargo cargo : container.getActualCondition().getCargoes()) {
                    actualCargoes.add(cargo);
                    keys.add(new StatsCargoGroupInfo(cargo));
                }
            }
        }
        Assert.assertEquals(groups.size(), keys.size());
        for (StatsCargoGroup g : groups) {
            Assert.assertTrue(keys.contains(g.getInfo()));
            groupCargoes.addAll(g.getCargoes());
        }
        Assert.assertEquals(actualCargoes.size(), groupCargoes.size());
        for (Cargo c : actualCargoes) {
            Assert.assertTrue(groupCargoes.contains(c));
        }
    }
}
