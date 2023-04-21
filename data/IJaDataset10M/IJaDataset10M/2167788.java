package erreAga.dao;

import java.util.List;
import erreAga.eb.HoraExtra;

public interface HoraExtraDao {

    public HoraExtra saveHoraExtra(HoraExtra horaExtra);

    public HoraExtra getHoraExtra(Integer id);

    public void deleteHoraExtra(HoraExtra horaExtra);

    public List<HoraExtra> searchHoraExtra();

    public List<HoraExtra> searchHoraExtra(String matriculaFuncionario);
}
