package sicav.jpa.persistencia;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class VueloServiceImpl implements VueloService {
}
