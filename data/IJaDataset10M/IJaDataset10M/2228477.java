package ge.telasi.tasks.fixture;

import ge.telasi.tasks.PersistenceUtils;
import ge.telasi.tasks.controller.StructureController;
import ge.telasi.tasks.model.Structure;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 * @author dimitri
 */
public class StructureFixture extends Fixture<Structure> {

    public static final String GEN_DIR = "gen_dir";

    public static final String HR_DEP = "gen_dir_hr_dep";

    public static final String IT_DEP = "gen_dir_it_dep";

    public static final String PROG_DIV = "gen_dir_it_dep_div";

    @Override
    protected Map<String, Structure> execute(EntityManager em) {
        Structure genDir = createStructure(em, null, "გენერალური დირექტორის აპარატი", "Аппарат Генерального Директора", Structure.DIRECTION);
        Structure hrDep = createStructure(em, genDir, "პერსონალის მართვის დეპარტამენტი", "Департамент управления персоналом", Structure.DEPARTMENT);
        Structure itDep = createStructure(em, genDir, "საინფორმაციო ტექნოლოგიების დეპარტამენტი", "Департамент информационных технологий", Structure.DEPARTMENT);
        Structure progGroup = createStructure(em, itDep, "პროგრამული დამუშავების ჯგუფი", "Группа програмного обеспечения", Structure.GROUP);
        Map<String, Structure> map = new HashMap<String, Structure>();
        map.put(GEN_DIR, genDir);
        map.put(HR_DEP, hrDep);
        map.put(IT_DEP, itDep);
        map.put(PROG_DIV, progGroup);
        return map;
    }

    protected Structure createStructure(EntityManager em, Structure parent, String name, String name2, int type) {
        Structure str = new Structure();
        str.setName(name);
        str.setName2(name2);
        str.setParent(parent);
        str.setType(type);
        PersistenceUtils.beginTransaction(em);
        new StructureController().createStructure(em, null, str);
        PersistenceUtils.endTransaction(em);
        return str;
    }
}
