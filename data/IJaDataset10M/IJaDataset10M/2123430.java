package sicav.jpa.repositorios;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import sicav.jpa.modelo.Hotel;

;

public interface HotelRepository extends CrudRepository<Hotel, Long> {

    List<Hotel> todosLosHoteles();
}
