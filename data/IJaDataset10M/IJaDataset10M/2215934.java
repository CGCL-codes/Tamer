package org.caisi.action.PMmodule;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.LazyValidatorForm;
import org.caisi.common.PMmodule.*;
import org.caisi.dao.PMmodule.*;
import org.caisi.entity.PMmodule.*;
import org.caisi.utility.PMmodule.*;

public class ReferralProgramAction extends Action {

    private boolean debugOn = PassUniversalVars.debugOn;

    HttpSession session = null;

    Database_Service databaseService = new Database_Service();

    DataSource dataSource = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String target = "select";
        String queryStr = "";
        try {
            session = request.getSession(false);
            if (session == null) {
                target = "timeout";
                return (mapping.findForward(target));
            }
            dataSource = getDataSource(request);
            AdmissionDao admissionDao = new AdmissionDao(dataSource);
            ClientDao clientDao = new ClientDao(dataSource);
            ProgramDao programDao = new ProgramDao(dataSource);
            List allBedPrograms = new ArrayList();
            Admission currBedProgram = null;
            String currBedProgramId = "";
            String currBedAdmissionId = "";
            PassClientVars passClientVars = new PassClientVars(request, response);
            String clientFirstName = passClientVars.getClientFirstName();
            String clientSurname = passClientVars.getClientSurname();
            String demographicNo = passClientVars.getDemographicNo();
            if (demographicNo.equals("")) {
                demographicNo = clientDao.getDemographicNo(databaseService, clientFirstName, clientSurname);
            }
            if (demographicNo != null && !demographicNo.equals("")) {
                allBedPrograms = programDao.getBedPrograms(databaseService);
                currBedProgram = admissionDao.getCurrentBedProgramAdmission(databaseService, demographicNo);
                if (currBedProgram != null) {
                    currBedProgramId = currBedProgram.getProgram_id();
                    currBedAdmissionId = currBedProgram.getAm_id();
                }
            }
            request.setAttribute("currBedProgramId", currBedProgramId);
            request.setAttribute("currBedAdmissionId", currBedAdmissionId);
            request.setAttribute("allBedPrograms", allBedPrograms);
            List allServicePrograms = new ArrayList();
            List currServicePrograms = new ArrayList();
            if (demographicNo != null && !demographicNo.equals("")) {
                allServicePrograms = programDao.getServicePrograms(databaseService);
                currServicePrograms = admissionDao.getCurrentServiceProgramAdmission(databaseService, demographicNo);
            }
            request.setAttribute("currServicePrograms", currServicePrograms);
            request.setAttribute("allServicePrograms", allServicePrograms);
        } catch (Exception ex) {
            target = "error";
            return (mapping.findForward(target));
        } catch (Throwable throwable) {
            target = "error";
            return (mapping.findForward(target));
        } finally {
            databaseService.closeConnection();
            databaseService.closeStatement();
        }
        return (mapping.findForward(target));
    }

    public List admissionsForClient_replaceIdsForNames(DataSource dataSource, ProgramDao programDao, List admissionsForClient) throws Throwable {
        if (admissionsForClient == null || admissionsForClient.size() <= 0) {
            return null;
        }
        ListIterator listIterator = admissionsForClient.listIterator();
        Admission admission = null;
        int columns = databaseService.colCount(dataSource, "admission");
        if (columns <= 0) {
            return null;
        }
        List admissionsForClientNew = new ArrayList();
        Program program = new Program();
        ReferrerDao referrerDao = new ReferrerDao(dataSource);
        String programId = "";
        String referrerName = "";
        while (listIterator.hasNext()) {
            try {
                admission = (Admission) listIterator.next();
                TempPassObj tempPassObj = new TempPassObj();
                tempPassObj.setPassVar1(admission.getAm_id());
                tempPassObj.setPassVar2(admission.getClient_id());
                tempPassObj.setPassVar6(admission.getAdmission_date());
                tempPassObj.setPassVar7(admission.getAdmission_notes());
                tempPassObj.setPassVar8(admission.getTemp_admission());
                tempPassObj.setPassVar9(admission.getDischarge_date());
                tempPassObj.setPassVar10(admission.getDischarge_notes());
                tempPassObj.setPassVar11(admission.getTemp_admit_discharge());
                tempPassObj.setPassVar16(admission.getAdmission_status());
                programId = Utility.convertToEmptyStrIfNull(admission.getProgram_id());
                program = programDao.getProgram(databaseService, programId);
                referrerName = referrerDao.getReferrerName(databaseService, Utility.convertToEmptyStrIfNull(admission.getReferrer_id()));
                tempPassObj.setPassVar3(program.getName());
                tempPassObj.setPassVar4(referrerName);
                tempPassObj.setPassVar12(program.getType());
                tempPassObj.setPassVar13(program.getMax_allowed());
                tempPassObj.setPassVar14(program.getNum_of_members());
                tempPassObj.setPassVar15(program.getProgram_id());
                admissionsForClientNew.add(tempPassObj);
            } catch (Exception ex) {
            }
        }
        return admissionsForClientNew;
    }
}
