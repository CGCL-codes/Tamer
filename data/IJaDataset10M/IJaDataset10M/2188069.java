package appointments.domain;

import java.util.Collection;

public class DoctorService {

    private DoctorDao doctorDao;

    public DoctorService(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    public Collection<Doctor> findAllDoctors() {
        return doctorDao.findAllDoctors();
    }
}
