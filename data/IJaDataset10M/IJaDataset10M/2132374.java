package wow.play.cricket.ac;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import wow.play.cricket.common.IAppEventHandler;
import wow.play.cricket.common.LCConstants;
import wow.play.cricket.common.exception.LCException;
import wow.play.cricket.logic.PlayingNationsTeam;
import wow.play.cricket.logic.ProcessJobs;
import wow.play.cricket.logic.Tournament;
import wow.play.cricket.vo.LCTournamentVO;
import wow.play.cricket.vo.RefxVO;
import wow.play.cricket.vo.TournamentChangesVO;
import wow.play.cricket.vo.TournamentPlayerVO;
import wow.play.cricket.vo.TournamentVO;

/**
 *
 * @author NURUL SIDDIK
 */
public class TournamentAc extends LCCommonAc {

    public ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws LCException, Exception {
        ActionForward af = mapping.findForward("init");
        return af;
    }

    @IAppEventHandler(appEvent = "create")
    public ActionForward initializeCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentVO objVO = new TournamentVO();
        TournamentChangesVO objTCVO = new TournamentChangesVO();
        objVO.setCurrent_stage("0");
        objTCVO.setTournament_stage("0");
        objTCVO.setChanges_permitted("100");
        objTCVO.setStage_description("Initial Stage");
        List tcList = new ArrayList();
        tcList.add(objTCVO);
        objVO.setTournament_changes(tcList);
        objForm.set("objTournament", objVO);
        request.getSession().setAttribute("tournament_changes", tcList);
        request.setAttribute("SCREEN_MODE", "create");
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "createTCLine")
    public ActionForward createTournamentChangesLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("IN THE CREATE: ");
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentVO objFormVO = (TournamentVO) objForm.get("objTournament");
        TournamentChangesVO objFormLineVO = (TournamentChangesVO) objForm.get("objTournamentChanges");
        List tcList = (List) request.getSession().getAttribute("tournament_changes");
        if (tcList == null) {
            System.out.println("NULL MILA");
            tcList = new ArrayList();
        }
        tcList.add(objFormLineVO);
        objForm.set("objTournament", objFormVO);
        objForm.set("objTournamentChanges", new TournamentChangesVO());
        request.getSession().setAttribute("tournament_changes", tcList);
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "modifyTCLine")
    public ActionForward modifyTournamentChangesLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("IN THE MODIFY...");
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentChangesVO objFormLineVO = (TournamentChangesVO) objForm.get("objTournamentChanges");
        TournamentVO objFormVO = (TournamentVO) objForm.get("objTournament");
        String selRow = request.getParameter("selectedrow");
        List tcList = (List) request.getSession().getAttribute("tournament_changes");
        TournamentChangesVO objListVO = (TournamentChangesVO) tcList.get(Integer.parseInt(selRow));
        objListVO = objFormLineVO;
        tcList.set(Integer.parseInt(selRow), objListVO);
        System.out.println("MODIFIED :" + objListVO.getStage_description());
        objForm.set("objTournament", objFormVO);
        objForm.set("objTournamentChanges", new TournamentChangesVO());
        request.getSession().setAttribute("tournament_changes", tcList);
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "deleteTCLine")
    public ActionForward deleteTournamentChangesLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("IN THE DELETE");
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentVO objFormVO = (TournamentVO) objForm.get("objTournament");
        String selRow = request.getParameter("selectedrow");
        List tcList = (List) request.getSession().getAttribute("tournament_changes");
        tcList.remove(Integer.parseInt(selRow));
        objForm.set("objTournament", objFormVO);
        objForm.set("objTournamentChanges", new TournamentChangesVO());
        request.getSession().setAttribute("tournament_changes", tcList);
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "createNewTournament")
    public ActionForward createNewTournament(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("Create New Tournament");
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentVO objFormVO = (TournamentVO) objForm.get("objTournament");
        List tcList = (List) request.getSession().getAttribute("tournament_changes");
        objFormVO.setTournament_changes(tcList);
        Tournament logicTour = new Tournament();
        logicTour.insertRecord(objFormVO);
        request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "record.updated");
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "initTournPlayers")
    public ActionForward initializeTournamentPlayers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Tournament logicTour = new Tournament();
        request.getSession().setAttribute("opt_tournament_id", logicTour.fetchActiveTournament());
        request.getSession().setAttribute("tournament_player_list", new ArrayList());
        request.setAttribute("opt_country_id", new ArrayList());
        request.setAttribute("opt_player_id", new ArrayList());
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "fetchTournamentCountry")
    public ActionForward fetchTournamentCountry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentPlayerVO objFormVO = (TournamentPlayerVO) objForm.get("objTournamentPlayer");
        String tournament_id = objFormVO.getTournament_id();
        Tournament logicTour = new Tournament();
        List tournPlayerList = logicTour.fetchCountriesForTournament(tournament_id);
        List tournament_player_list = logicTour.fetchAssociatedPlayers(tournament_id);
        if (tournament_player_list == null) {
            tournament_player_list = new ArrayList();
        }
        HttpSession session = request.getSession();
        session.setAttribute("tournament_player_list", tournament_player_list);
        session.setAttribute("opt_country_id", tournPlayerList);
        request.setAttribute("opt_player_id", new ArrayList());
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "fetchTournamentCountryPlayer")
    public ActionForward fetchTournamentCountryPlayer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentPlayerVO objFormVO = (TournamentPlayerVO) objForm.get("objTournamentPlayer");
        String country_id = objFormVO.getCountry_id();
        PlayingNationsTeam logicPlayer = PlayingNationsTeam.getInstance();
        List tournPlayerList = logicPlayer.fetchNationPlayer(country_id);
        request.getSession().setAttribute("opt_player_id", tournPlayerList);
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "savePlayerLine")
    public ActionForward savePlayerLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentPlayerVO objFormVO = (TournamentPlayerVO) objForm.get("objTournamentPlayer");
        String player_name = request.getParameter("player_name");
        HttpSession session = request.getSession();
        List tourPlayerList = (ArrayList) session.getAttribute("tournament_player_list");
        if (tourPlayerList == null) {
            tourPlayerList = new ArrayList();
        }
        objFormVO.setPlayer_name(player_name);
        tourPlayerList.add(objFormVO);
        session.setAttribute("tournament_player_list", tourPlayerList);
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "removePlayerLine")
    public ActionForward removePlayerLine(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        List lstTournamentPlayer = (ArrayList) session.getAttribute("tournament_player_list");
        if (lstTournamentPlayer != null) {
            String selectedId = request.getParameter("selected_id");
            lstTournamentPlayer.remove(Integer.parseInt(selectedId));
        }
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "associatePlayers")
    public ActionForward associatePlayer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        TournamentPlayerVO objFormVO = (TournamentPlayerVO) objForm.get("objTournamentPlayer");
        String tournament_id = objFormVO.getTournament_id();
        List lstTournamentPlayer = (ArrayList) session.getAttribute("tournament_player_list");
        if (lstTournamentPlayer == null || lstTournamentPlayer.size() == 0) {
            request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "atleast.online");
        } else {
            Tournament logicTour = new Tournament();
            logicTour.associatePlayersToTournament(tournament_id, lstTournamentPlayer);
        }
        request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "record.updated");
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "initLockProcess")
    public ActionForward initLockProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        Tournament logicTour = new Tournament();
        List tournamentList = logicTour.fetchActiveTournament();
        if (tournamentList != null && tournamentList.size() > 0) {
            request.getSession().setAttribute("opt_tournament_id", tournamentList);
            String tournament_id = request.getParameter("tournament_id");
            if (tournament_id == null) {
                RefxVO objVO = (RefxVO) tournamentList.get(0);
                tournament_id = objVO.getRefx_value();
            }
            LCTournamentVO tourVO = logicTour.fetchFullDetailsofTournament(tournament_id);
            String next_eff_date = tourVO.getNext_effective_date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date effDate = sdf.parse(next_eff_date);
            sdf.applyPattern("dd-MM-yyyy");
            tourVO.setNext_effective_date(sdf.format(effDate));
            System.out.println("TOURNAMENT ID: " + tournament_id + "next eff" + next_eff_date);
            objForm.set("objLCTournament", tourVO);
        } else {
            request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "no_records");
        }
        request.getSession().setAttribute("opt_tournament_id", tournamentList);
        return mapping.findForward("locktournamentdate");
    }

    @IAppEventHandler(appEvent = "lockTournamentDay")
    public ActionForward lockTournamentDay(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tournament_id = request.getParameter("tournament_id");
        String next_eff_date = request.getParameter("next_effective_day");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dateNed = sdf.parse(next_eff_date.trim());
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        String formatted_next_eff_date = sdf.format(dateNed);
        ProcessJobs.lockTournamentChanges(tournament_id.trim(), formatted_next_eff_date);
        return mapping.findForward("success");
    }
}
