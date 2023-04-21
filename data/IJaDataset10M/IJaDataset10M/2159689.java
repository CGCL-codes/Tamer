package test.navigator.client.ui.filtering;

import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import com.navigator.client.ui.filtering.FilterModel;
import com.navigator.shared.data.Unit;

public class FilterModelTest extends TestCase {

    private FilterModel model;

    private MockDataSource source = new MockDataSource();

    private MockFilterTarget target = new MockFilterTarget();

    private Unit chineseGeneral, kv1;

    protected void setUp() throws Exception {
        chineseGeneral = new Unit();
        chineseGeneral.setCost(3);
        chineseGeneral.setName("Chinese general");
        chineseGeneral.setNationality("China");
        chineseGeneral.setType("Soldier-Commander");
        chineseGeneral.setSetNumber(2);
        chineseGeneral.setUnitNumber(4);
        source.addUnit(chineseGeneral);
        kv1 = new Unit();
        kv1.setCost(32);
        kv1.setType("Vehicle-Tank");
        kv1.setNationality("Russia");
        kv1.setName("KV1 Tank");
        kv1.setSetNumber(1);
        kv1.setUnitNumber(13);
        source.addUnit(kv1);
        model = new FilterModel(source, target);
    }

    public void testFilterChoices() {
        List types = model.getValuesForAttribute("type");
        assertEquals(2, types.size());
        assertTrue(types.contains("Vehicle-Tank"));
        assertTrue(types.contains("Soldier-Commander"));
        Unit nataku = new Unit();
        nataku.setType("Ninja");
        source.addUnit(nataku);
        types = model.getValuesForAttribute("type");
        assertEquals(3, types.size());
        assertTrue(types.contains("Ninja"));
        nataku.setType("Soldier-Commander");
        types = model.getValuesForAttribute("type");
        assertEquals(2, types.size());
        assertFalse(types.contains("Ninja"));
    }

    public void testFilterTarget() {
        model.setNationality("Russia");
        model.filter();
        List results = target.getResults();
        assertTrue(results.size() == 1);
        chineseGeneral.setNationality("Russia");
        model.filter();
        results = target.getResults();
        assertTrue(results.size() == 2);
        model.setNationality("English");
        model.filter();
        assertTrue(target.getResults().isEmpty());
    }
}
