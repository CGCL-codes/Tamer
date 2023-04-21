package wow.play.cricket.ac;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import wow.play.cricket.common.IAppEventHandler;
import wow.play.cricket.common.LCConstants;
import wow.play.cricket.common.LCLogger;
import wow.play.cricket.common.exception.LCException;
import wow.play.cricket.logic.RegNewUserLogic;
import wow.play.cricket.logic.Tournament;
import wow.play.cricket.logic.UserTeam;
import wow.play.cricket.vo.LCTournamentVO;
import wow.play.cricket.vo.PlayingNationsTeamVO;
import wow.play.cricket.vo.RefxVO;
import wow.play.cricket.vo.UserTeamDetailVO;
import wow.play.cricket.vo.UserTeamDetail_1VO;
import wow.play.cricket.vo.UserTeamVO;

/**
 *
 * @author NURUL SIDDIK
 */
public class UserTeamAc extends LCCommonAc {

    /** Creates a new instance of UserTeamAc */
    public ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "initUserTeam")
    public ActionForward initUserTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Tournament logicTour = new Tournament();
        request.getSession().setAttribute("opt_tournament_id", logicTour.fetchActiveTournament());
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "initModifyUserTeam")
    public ActionForward initModifyUserTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        HttpSession session = request.getSession();
        String user_id = (String) session.getAttribute(LCConstants.USER_ID);
        UserTeam logic = UserTeam.getInstance();
        List lstUserTeams = logic.fetchUserTeam(user_id);
        session.setAttribute("opt_user_team", lstUserTeams);
        LCLogger.debug(lstUserTeams + " List User Team");
        if (lstUserTeams != null && lstUserTeams.size() > 0) {
            RefxVO objRefx = (RefxVO) lstUserTeams.get(0);
            String user_team_id = (String) request.getAttribute("new_user_team_id");
            if (user_team_id == null) {
                user_team_id = objRefx.getRefx_value();
            }
            LCLogger.debug("User Team: " + user_team_id);
            setAllTeamDetails(objForm, request, user_team_id);
        } else {
            request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "no_team_found");
            return mapping.findForward("noteamfound");
        }
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "createUserTeam")
    public ActionForward createUserTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        UserTeamVO objUserTeam = (UserTeamVO) objForm.get("objUserTeam");
        HttpSession session = request.getSession();
        String user_id = (String) session.getAttribute(LCConstants.USER_ID);
        objUserTeam.setUser_id(user_id);
        objUserTeam.setNumber_of_changes("0");
        objUserTeam.setUser_team_points("0");
        objUserTeam.setUser_team_id(objUserTeam.getUser_team_id().trim());
        UserTeam logic = UserTeam.getInstance();
        try {
            logic.createUserTeam(objUserTeam);
            RegNewUserLogic userLogic = new RegNewUserLogic();
            userLogic.updateUserDefaultTeam(user_id, objUserTeam.getUser_team_id());
        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getErrorCode() == 1062) {
                request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "team_id_exist");
                return mapping.findForward("error");
            }
        }
        List lstUserTeams = logic.fetchUserTeam(user_id);
        session.setAttribute("opt_user_team", lstUserTeams);
        request.setAttribute("new_user_team_id", objUserTeam.getUser_team_id());
        request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "record.updated");
        return mapping.findForward("teamcreated");
    }

    @IAppEventHandler(appEvent = "fetchUserTeamData")
    public ActionForward fetchUserTeamData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        UserTeamVO objUserTeam = (UserTeamVO) objForm.get("objUserTeam");
        UserTeam logic = UserTeam.getInstance();
        objUserTeam = logic.selectUserTeam(objUserTeam.getUser_team_id());
        objForm.set("objUserTeam", objUserTeam);
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "updateUserTeam")
    public ActionForward updateUserTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        UserTeamVO objUserTeam = (UserTeamVO) objForm.get("objUserTeam");
        objUserTeam.setUser_id((String) request.getSession().getAttribute(LCConstants.USER_ID));
        UserTeam logic = UserTeam.getInstance();
        logic.updateUserTeam(objUserTeam);
        request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "record.updated");
        return mapping.findForward("teamcreated");
    }

    private List defaultUserTeam() {
        List userTeam = new ArrayList();
        for (int i = 0; i < 4; i++) {
            PlayingNationsTeamVO objVO = new PlayingNationsTeamVO();
            objVO.setCountry_id(" ");
            objVO.setPlayer_type("Batsman");
            objVO.setPlayer_name("Select Batsman");
            objVO.setPlayer_value("0");
            userTeam.add(objVO);
        }
        PlayingNationsTeamVO objAlVO = new PlayingNationsTeamVO();
        objAlVO.setCountry_id(" ");
        objAlVO.setPlayer_type("All rounder");
        objAlVO.setPlayer_name("Select All rounder");
        objAlVO.setPlayer_value("0");
        userTeam.add(objAlVO);
        PlayingNationsTeamVO objAlVO1 = new PlayingNationsTeamVO();
        objAlVO1.setCountry_id(" ");
        objAlVO1.setPlayer_type("All rounder");
        objAlVO1.setPlayer_name("Select All rounder");
        objAlVO1.setPlayer_value("0");
        userTeam.add(objAlVO1);
        PlayingNationsTeamVO objWkVO = new PlayingNationsTeamVO();
        objWkVO.setCountry_id(" ");
        objWkVO.setPlayer_type("Wicket keeper");
        objWkVO.setPlayer_name("Select Wicket keeper");
        objWkVO.setPlayer_value("0");
        userTeam.add(objWkVO);
        for (int i = 0; i < 4; i++) {
            PlayingNationsTeamVO objBowVO = new PlayingNationsTeamVO();
            objBowVO.setCountry_id(" ");
            objBowVO.setPlayer_type("Bowler");
            objBowVO.setPlayer_name("Select Bowler");
            objBowVO.setPlayer_value("0");
            userTeam.add(objBowVO);
        }
        return userTeam;
    }

    private List fetchUserTeamPlayers(String user_team_id) throws Exception {
        UserTeam logic = UserTeam.getInstance();
        List allDaysTeam = logic.fetchAllTheUserTeams(user_team_id);
        Map userTeamMap = (Map) allDaysTeam.get(0);
        List playerList = logic.fetchPlayerDetailForTeam(userTeamMap);
        return playerList;
    }

    private List replaceDefaultByActual(List<PlayingNationsTeamVO> defaultList, List<PlayingNationsTeamVO> actualList, List<RefxVO> activeTournamentList) {
        int batsmanIndex = 0;
        int allrounderIndex = 4;
        int wkIndex = 6;
        int bowlersIndex = 7;
        Map<String, String> activeCountryMap = new HashMap<String, String>();
        for (RefxVO objActCountry : activeTournamentList) {
            String country_id = objActCountry.getRefx_value();
            activeCountryMap.put(country_id, country_id);
        }
        for (PlayingNationsTeamVO objPntVO : actualList) {
            if (!activeCountryMap.containsKey(objPntVO.getCountry_id())) {
                continue;
            }
            String player_type = objPntVO.getPlayer_type();
            if ("Batsman".equals(player_type) && batsmanIndex < 4) {
                defaultList.set(batsmanIndex, objPntVO);
                batsmanIndex++;
            } else if ("Bowler".equals(player_type) && bowlersIndex < 11) {
                defaultList.set(bowlersIndex, objPntVO);
                bowlersIndex++;
            } else if ("All rounder".equals(player_type) && allrounderIndex < 6) {
                defaultList.set(allrounderIndex, objPntVO);
                allrounderIndex++;
            } else if ("Wicket keeper".equals(player_type) && wkIndex < 7) {
                defaultList.set(wkIndex, objPntVO);
                wkIndex++;
            }
        }
        return defaultList;
    }

    public List fetchAllPlayers(String tournament_id) throws Exception {
        UserTeam logic = UserTeam.getInstance();
        return logic.fetchAllActiveCountriesPlayerDetails(tournament_id);
    }

    private boolean setAllTeamDetails(DynaValidatorForm objForm, HttpServletRequest request, String user_team_id) throws Exception {
        boolean retBool = true;
        HttpSession session = request.getSession();
        UserTeam logic = UserTeam.getInstance();
        UserTeamVO objUserTeam = logic.selectUserTeam(user_team_id);
        List<Map> allDaysTeam = logic.fetchAllTheUserTeams(user_team_id);
        String tournament_id = objUserTeam.getTournament_id();
        session.setAttribute("all_tournamentplayers_list", fetchAllPlayers(tournament_id));
        Tournament logicTourn = new Tournament();
        List<RefxVO> lstActiveCountries = logicTourn.fetchActiveCountriesForTournament(tournament_id);
        session.setAttribute("opt_nations", lstActiveCountries);
        LCTournamentVO objTournFullVO = logicTourn.fetchFullDetailsofTournament(tournament_id);
        String strNextEffTournDate = objTournFullVO.getNext_effective_date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nextEffTournDate = sdf.parse(strNextEffTournDate);
        String tournStatus = objTournFullVO.getTournament_status();
        String tournChangesAllowed = objTournFullVO.getChanges_permitted();
        request.setAttribute("TOURNAMENT_CHANGES", tournChangesAllowed);
        request.setAttribute("TOURNAMENT_VALUES", objTournFullVO.getTournament_value());
        if (tournStatus.equals("4")) {
            request.setAttribute("NOTE_CHANGES", "no");
        } else if (tournStatus.equals("1")) {
            request.setAttribute("NOTE_CHANGES", "yes");
        }
        Date latestTeamEffDate = new Date();
        sdf.applyPattern("dd-MM-yyyy");
        request.setAttribute("next_effective_date", sdf.format(nextEffTournDate));
        List<PlayingNationsTeamVO> playerList = new ArrayList();
        if (allDaysTeam == null || allDaysTeam.size() == 0) {
            playerList = replaceDefaultByActual(defaultUserTeam(), playerList, lstActiveCountries);
            request.setAttribute("curr_effective_date", sdf.format(latestTeamEffDate));
        } else if (allDaysTeam.size() >= 1) {
            Map latestTeamMap = allDaysTeam.get(0);
            latestTeamEffDate = (Date) latestTeamMap.get("effective_date");
            request.setAttribute("curr_effective_date", sdf.format(latestTeamEffDate));
            playerList = logic.fetchPlayerDetailForTeam(latestTeamMap);
            playerList = replaceDefaultByActual(defaultUserTeam(), playerList, lstActiveCountries);
        }
        int noOfChangesDone = logic.totalChangesForTeam(user_team_id);
        System.out.println("\n\n No of changes: " + noOfChangesDone);
        request.setAttribute("number_of_changes", noOfChangesDone + "");
        if (allDaysTeam != null) {
            Map compareTeam = new HashMap();
            if (allDaysTeam.size() >= 1) {
                System.out.println("TOURN DATE" + nextEffTournDate);
                System.out.println("LAT DATE" + latestTeamEffDate);
                if (nextEffTournDate.compareTo(latestTeamEffDate) <= 0) {
                    System.out.println("LATEST TEAM - 1");
                    if (allDaysTeam.size() == 1) {
                        compareTeam = allDaysTeam.get(0);
                    } else {
                        compareTeam = allDaysTeam.get(1);
                    }
                } else {
                    System.out.println("LATEST TEAM");
                    compareTeam = allDaysTeam.get(0);
                }
            }
            String strCompareTeam = convertToPlayerArray(compareTeam);
            request.setAttribute("compare_team", strCompareTeam);
        }
        Integer total_points = logic.fetchTeamPoints(user_team_id, objTournFullVO.getStage_effective_date());
        if (total_points != null) {
            session.setAttribute("team_points", total_points.toString());
        } else {
            session.setAttribute("team_points", "0");
        }
        session.setAttribute("userteam_players_list", playerList);
        objForm.set("objUserTeam", objUserTeam);
        return retBool;
    }

    @IAppEventHandler(appEvent = "insertUserTeamPlayers")
    public ActionForward insertUserTeamPlayers(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserTeamDetailVO objUserTeamDetail = new UserTeamDetailVO();
        objUserTeamDetail.setUser_team_id(request.getParameter("user_team_id"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date effective_date = sdf.parse(request.getParameter("effective_date"));
        sdf.applyPattern("yyyy-MM-dd");
        objUserTeamDetail.setEffective_date(sdf.format(effective_date));
        String player_ids = request.getParameter("player_ids");
        String[] aryPlayerIds = player_ids.split(",");
        objUserTeamDetail.setPlayer1(aryPlayerIds[0]);
        objUserTeamDetail.setPlayer2(aryPlayerIds[1]);
        objUserTeamDetail.setPlayer3(aryPlayerIds[2]);
        objUserTeamDetail.setPlayer4(aryPlayerIds[3]);
        objUserTeamDetail.setPlayer5(aryPlayerIds[4]);
        objUserTeamDetail.setPlayer6(aryPlayerIds[5]);
        objUserTeamDetail.setPlayer7(aryPlayerIds[6]);
        objUserTeamDetail.setPlayer8(aryPlayerIds[7]);
        objUserTeamDetail.setPlayer9(aryPlayerIds[8]);
        objUserTeamDetail.setPlayer10(aryPlayerIds[9]);
        objUserTeamDetail.setPlayer11(aryPlayerIds[10]);
        UserTeam logic = UserTeam.getInstance();
        logic.createUserTeamPlayers(objUserTeamDetail);
        System.out.println("FORWARDING TO TEAM..........");
        return mapping.findForward("teamplayersupdated");
    }

    private static String convertToPlayerArray(Map playerMap) {
        StringBuffer retString = new StringBuffer();
        if (playerMap.get("player1") != null) {
            retString.append(playerMap.get("player1") + ",");
        }
        if (playerMap.get("player2") != null) {
            retString.append(playerMap.get("player2") + ",");
        }
        if (playerMap.get("player3") != null) {
            retString.append(playerMap.get("player3") + ",");
        }
        if (playerMap.get("player4") != null) {
            retString.append(playerMap.get("player4") + ",");
        }
        if (playerMap.get("player5") != null) {
            retString.append(playerMap.get("player5") + ",");
        }
        if (playerMap.get("player6") != null) {
            retString.append(playerMap.get("player6") + ",");
        }
        if (playerMap.get("player7") != null) {
            retString.append(playerMap.get("player7") + ",");
        }
        if (playerMap.get("player8") != null) {
            retString.append(playerMap.get("player8") + ",");
        }
        if (playerMap.get("player9") != null) {
            retString.append(playerMap.get("player9") + ",");
        }
        if (playerMap.get("player10") != null) {
            retString.append(playerMap.get("player10") + ",");
        }
        if (playerMap.get("player11") != null) {
            retString.append(playerMap.get("player11") + ",");
        }
        String retStr = retString.toString();
        if (!"".equals(retStr) && retStr.indexOf(",") == retStr.length() - 1) {
            retStr = retStr.substring(0, retStr.length() - 2);
        }
        System.out.println("RETURNING STRING " + retStr);
        return retStr;
    }

    @IAppEventHandler(appEvent = "onChangeUserTeam")
    public ActionForward onChangeUserTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm objForm = (DynaValidatorForm) form;
        UserTeamVO objUserTeam = (UserTeamVO) objForm.get("objUserTeam");
        request.setAttribute("new_user_team_id", objUserTeam.getUser_team_id());
        return initModifyUserTeam(mapping, form, request, response);
    }

    @IAppEventHandler(appEvent = "initPredictCountries")
    public ActionForward initPredictCountries(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String user_id = (String) session.getAttribute(LCConstants.USER_ID);
        UserTeam logic = UserTeam.getInstance();
        List lstUserTeams = logic.fetchUserTeam(user_id);
        session.setAttribute("opt_user_team", lstUserTeams);
        RefxVO objRefx = (RefxVO) lstUserTeams.get(0);
        String user_team_id = request.getParameter("user_team_id");
        if (user_team_id == null) {
            user_team_id = objRefx.getRefx_value();
        }
        UserTeam userTeamLogic = UserTeam.getInstance();
        UserTeamVO objUserTeam = userTeamLogic.selectUserTeam(user_team_id);
        Tournament logicTourn = new Tournament();
        List<RefxVO> lstActiveCountries = logicTourn.fetchActiveCountriesForTournament(objUserTeam.getTournament_id());
        session.setAttribute("opt_nations", lstActiveCountries);
        List<UserTeamDetail_1VO> lstPredictTeam = logic.selectPredictedUserTeam(user_team_id);
        int semiFinalist = 1;
        int finalist = 1;
        for (UserTeamDetail_1VO objUTD : lstPredictTeam) {
            String status = objUTD.getCountry_status();
            if ("3".equals(objUTD.getCountry_status())) {
                request.setAttribute("sf_team_" + semiFinalist, objUTD.getCountry_id());
                semiFinalist++;
            } else if ("4".equals(objUTD.getCountry_status())) {
                request.setAttribute("f_team_" + finalist, objUTD.getCountry_id());
                finalist++;
            } else if ("5".equals(objUTD.getCountry_status())) {
                request.setAttribute("winner_team", objUTD.getCountry_id());
            }
        }
        request.setAttribute("user_team_id", user_team_id);
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "savePredictCountries")
    public ActionForward savePredictCountries(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List lst_predict_team = new ArrayList();
        String user_team_id = request.getParameter("user_team_id");
        String sf_team_1 = request.getParameter("sf_team_1");
        String sf_team_2 = request.getParameter("sf_team_2");
        String sf_team_3 = request.getParameter("sf_team_3");
        String sf_team_4 = request.getParameter("sf_team_4");
        String f_team_1 = request.getParameter("f_team_1");
        String f_team_2 = request.getParameter("f_team_2");
        String winner_team = request.getParameter("winner_team");
        UserTeamDetail_1VO objSf1VO = new UserTeamDetail_1VO();
        objSf1VO.setCountry_id(sf_team_1);
        objSf1VO.setCountry_status("3");
        objSf1VO.setTeam_points("0");
        objSf1VO.setUser_team_id(user_team_id);
        lst_predict_team.add(objSf1VO);
        UserTeamDetail_1VO objSf2VO = new UserTeamDetail_1VO();
        objSf2VO.setCountry_id(sf_team_2);
        objSf2VO.setCountry_status("3");
        objSf2VO.setTeam_points("0");
        objSf2VO.setUser_team_id(user_team_id);
        lst_predict_team.add(objSf2VO);
        UserTeamDetail_1VO objSf3VO = new UserTeamDetail_1VO();
        objSf3VO.setCountry_id(sf_team_3);
        objSf3VO.setCountry_status("3");
        objSf3VO.setTeam_points("0");
        objSf3VO.setUser_team_id(user_team_id);
        lst_predict_team.add(objSf3VO);
        UserTeamDetail_1VO objSf4VO = new UserTeamDetail_1VO();
        objSf4VO.setCountry_id(sf_team_4);
        objSf4VO.setCountry_status("3");
        objSf4VO.setTeam_points("0");
        objSf4VO.setUser_team_id(user_team_id);
        lst_predict_team.add(objSf4VO);
        UserTeamDetail_1VO objf1VO = new UserTeamDetail_1VO();
        objf1VO.setCountry_id(f_team_1);
        objf1VO.setCountry_status("4");
        objf1VO.setTeam_points("0");
        objf1VO.setUser_team_id(user_team_id);
        lst_predict_team.add(objf1VO);
        UserTeamDetail_1VO objf2VO = new UserTeamDetail_1VO();
        objf2VO.setCountry_id(f_team_2);
        objf2VO.setCountry_status("4");
        objf2VO.setTeam_points("0");
        objf2VO.setUser_team_id(user_team_id);
        lst_predict_team.add(objf2VO);
        UserTeamDetail_1VO winnerVO = new UserTeamDetail_1VO();
        winnerVO.setCountry_id(winner_team);
        winnerVO.setCountry_status("5");
        winnerVO.setTeam_points("0");
        winnerVO.setUser_team_id(user_team_id);
        lst_predict_team.add(winnerVO);
        UserTeam logic = UserTeam.getInstance();
        logic.insertPredictTeamForUser(user_team_id, lst_predict_team);
        request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "record.updated");
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "initPredictGameWinner")
    public ActionForward initPredictGameWinner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String user_team_id = request.getParameter("user_team_id");
        UserTeam logic = UserTeam.getInstance();
        UserTeamVO userTeam = logic.selectUserTeam(user_team_id);
        List lstUserTeams = logic.fetchAllUserTeamsForTournament(userTeam.getTournament_id());
        String predicted_team_id = logic.fetchPredictUserTeamWinner(user_team_id);
        if (predicted_team_id != null) {
            request.setAttribute("predicted_team_id", predicted_team_id);
        }
        session.setAttribute("lst_user_teams", lstUserTeams);
        return mapping.findForward("success");
    }

    @IAppEventHandler(appEvent = "savePredictedGameWinner")
    public ActionForward savePredictedGameWinner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String user_team_id = request.getParameter("user_team_id");
        String predicted_team_id = request.getParameter("predicted_team_id");
        UserTeam logic = UserTeam.getInstance();
        logic.insertPredictUserTeamWinner(user_team_id, predicted_team_id);
        request.setAttribute("predicted_team_id", predicted_team_id);
        request.setAttribute(LCConstants.STATUS_MESSAGE_CODE, "record.updated");
        return mapping.findForward("success");
    }
}
