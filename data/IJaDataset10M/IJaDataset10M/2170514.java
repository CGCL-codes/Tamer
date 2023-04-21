package com.unmsm.fisi.clinica.ws.presentacion.web.doctor.home.facade;

import java.util.Collection;
import com.unmsm.fisi.clinica.ws.presentacion.compartido.dto.DoctorDTO;
import com.unmsm.fisi.clinica.ws.presentacion.compartido.dto.PatientDTO;
import com.unmsm.fisi.clinica.ws.presentacion.compartido.dto.UserDTO;

public interface HomeDoctorFacade {

    public DoctorDTO getDoctorOfUser(UserDTO userDTO);

    public Collection<PatientDTO> getPatientsOfDoctor(DoctorDTO doctorDTO);
}
