package com.unmsm.fisi.clinica.ws.presentacion.ws.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import com.unmsm.fisi.clinica.ws.infraestructura.listener.ContextLoaderListener;
import com.unmsm.fisi.clinica.ws.infraestructura.persistencia.hibernate.repositorio.PatientRepository;
import com.unmsm.fisi.clinica.ws.infraestructura.ws.parser.Parser;
import com.unmsm.fisi.clinica.ws.presentacion.compartido.dto.DoctorDTO;
import com.unmsm.fisi.clinica.ws.presentacion.ws.buscarpaciente.facade.PatientSearchFacade;
import com.unmsm.fisi.clinica.ws.presentacion.ws.buscarpaciente.facade.impl.PatientSearchFacadeImpl;
import com.unmsm.fisi.clinica.ws.presentacion.ws.servlet.RequestHandler;

public class GetPatientsOfDoctorRH extends RequestHandler {

    public GetPatientsOfDoctorRH(Parser parser) {
        super(parser);
    }

    @Override
    public String handleRequest(HttpServletRequest request) {
        String doctorString = request.getParameter("doctorDTO");
        DoctorDTO doctorDTO = (DoctorDTO) parser.toObject(doctorString, DoctorDTO.class);
        PatientRepository patientRepository = (PatientRepository) ContextLoaderListener.getBean(PatientRepository.class);
        PatientSearchFacade patientSearch = new PatientSearchFacadeImpl(patientRepository);
        return parser.toString(patientSearch.getPatientsOf(doctorDTO));
    }
}
