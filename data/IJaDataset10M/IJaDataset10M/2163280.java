package dormatory;

import java.util.List;

public interface DormitoryService {

    List<Dormitory> getDormList();

    Dormitory getDorm(String name);

    void save(Dormitory dorm);
}
