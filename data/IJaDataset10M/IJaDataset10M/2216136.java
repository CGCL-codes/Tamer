package com.unmsm.fisi.clinica.ws.presentacion.ws.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import com.unmsm.fisi.clinica.ws.infraestructura.listener.ContextLoaderListener;
import com.unmsm.fisi.clinica.ws.infraestructura.persistencia.hibernate.repositorio.AllergyRepository;
import com.unmsm.fisi.clinica.ws.infraestructura.persistencia.hibernate.repositorio.PathologicalAntecedentRepository;
import com.unmsm.fisi.clinica.ws.infraestructura.ws.parser.Parser;
import com.unmsm.fisi.clinica.ws.presentacion.compartido.dto.AllergyDTO;
import com.unmsm.fisi.clinica.ws.presentacion.compartido.dto.PatientDTO;
import com.unmsm.fisi.clinica.ws.presentacion.ws.alergias.facade.AllergyManagementFacade;
import com.unmsm.fisi.clinica.ws.presentacion.ws.alergias.facade.impl.AllergyManagementFacadeImpl;
import com.unmsm.fisi.clinica.ws.presentacion.ws.servlet.RequestHandler;

public class AddAllergyToPatientRH extends RequestHandler {

    public AddAllergyToPatientRH(Parser parser) {
        super(parser);
    }

    @Override
    public String handleRequest(HttpServletRequest request) {
        String newAllergyString = request.getParameter("nuevaAlergiaDTO");
        String patientString = request.getParameter("pacienteDTO");
        AllergyDTO newAllergyDTO = (AllergyDTO) parser.toObject(newAllergyString, AllergyDTO.class);
        PatientDTO patientDTO = (PatientDTO) parser.toObject(patientString, PatientDTO.class);
        AllergyRepository allergyRepository = (AllergyRepository) ContextLoaderListener.getBean(AllergyRepository.class);
        PathologicalAntecedentRepository pathologicalAntecedentRepository = (PathologicalAntecedentRepository) ContextLoaderListener.getBean(PathologicalAntecedentRepository.class);
        AllergyManagementFacade allergyManagement = new AllergyManagementFacadeImpl(allergyRepository, pathologicalAntecedentRepository);
        return parser.toString(allergyManagement.addAlergicPathologicalAntecedent(newAllergyDTO, patientDTO));
    }
}
