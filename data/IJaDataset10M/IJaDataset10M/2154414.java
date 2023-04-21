package cn.edu.pku.dr.requirement.elicitation.action;

import javax.servlet.http.HttpServletRequest;
import easyJ.common.EasyJException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import cn.edu.pku.dr.requirement.elicitation.data.Ambiguity;
import cn.edu.pku.dr.requirement.elicitation.data.Problemreason;
import cn.edu.pku.dr.requirement.elicitation.data.ProblemreasonEvaluation;

public interface ProblemReasonInterface {

    public StringBuffer getProblemreason(Problemreason problemreason, HttpServletRequest request, boolean vote) throws EasyJException;

    public StringBuffer problemreasonUpdate(ProblemreasonEvaluation pre, HttpServletRequest request, HttpServletResponse response) throws EasyJException, IOException;

    public StringBuffer viewAllProblemreason(Problemreason problemreason, HttpServletRequest request) throws EasyJException;

    public String creatingProblemReason(Problemreason pr, HttpServletRequest request, HttpServletResponse response) throws EasyJException, IOException;
}
