package com.flyox.game.militarychess.server.business;

import java.io.IOException;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.flyox.game.militarychess.CONSTS;
import com.flyox.game.militarychess.bean.ChessDesk;
import com.flyox.game.militarychess.bean.ChessMan;
import com.flyox.game.militarychess.bean.ChessSeat;
import com.flyox.game.militarychess.bean.Player;
import com.flyox.game.militarychess.bean.Position;
import com.flyox.game.militarychess.bean.ChessDesk.DESK_STATE;
import com.flyox.game.militarychess.bean.ChessMan.NAME;
import com.flyox.game.militarychess.bean.ChessMan.SHOWLEVEL;
import com.flyox.game.militarychess.bean.ChessSeat.SEAT_NAME;
import com.flyox.game.militarychess.bean.ChessSeat.SEAT_STATE;
import com.flyox.game.militarychess.message.ChessPackage;
import com.flyox.game.militarychess.message.Request;
import com.flyox.game.militarychess.message.Response;
import com.flyox.game.militarychess.server.services.DeskService;
import com.flyox.game.militarychess.server.services.LayoutService;
import com.flyox.game.militarychess.server.services.PlayerService;
import com.flyox.game.militarychess.service.ChessService;
import com.flyox.game.militarychess.service.CommonLayoutService;
import com.flyox.game.militarychess.util.Secret;
import com.flyox.game.militarychess.util.SerialUtil;

public class RequestService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    ChessService chessService = new ChessService();

    PlayerService playerService = new PlayerService();

    DeskService chessDeskService = new DeskService();

    LayoutService layoutService = new LayoutService();

    public void processSec(IoSession session, String playerID, byte[] content) {
        Response response = new Response();
        Player player = playerService.getPlayer(playerID);
        if (player == null) {
            logger.trace("server CMD_SEC ERROR!");
            response.setRes(CONSTS.RESPONSE_ERROR);
        } else {
            byte[] dcontent = Secret.decipherMsg(content, player.getDesKey());
            Serializable s = null;
            try {
                s = SerialUtil.deSerial(dcontent);
            } catch (Exception e) {
                e.printStackTrace();
                s = null;
            }
            Request cr = (Request) s;
            if (s == null || cr == null) {
                logger.trace("server CMD_SEC ERROR! SER=NULL");
                response.setRes(CONSTS.RESPONSE_ERROR);
            } else {
                BroadService broadService = new BroadService();
                switch(cr.getCommand()) {
                    case CONSTS.CMD_SEC_LOGIN:
                        response = processLogin(cr, player, session);
                        break;
                    case CONSTS.CMD_SEC_ONSEAT:
                        response = processOnSeat(cr, player);
                        if (CONSTS.RESPONSE_OK == response.getRes()) {
                            broadService.onSeatBroad(player);
                        }
                        break;
                    case CONSTS.CMD_SEC_READY:
                        response = processReady(cr, player);
                        if (CONSTS.RESPONSE_OK == response.getRes()) {
                            broadService.onReady(player);
                        }
                        break;
                    case CONSTS.CMD_SEC_EAT:
                        response = processEat(cr, player);
                        if (CONSTS.RESPONSE_OK == response.getRes()) {
                            broadService.onEat(player, response, cr.getFrom(), cr.getTo());
                        }
                        break;
                    case CONSTS.CMD_SEC_MOVE:
                        response = processMove(cr, player);
                        if (CONSTS.RESPONSE_OK == response.getRes()) {
                            broadService.onMove(player, cr.getFrom(), cr.getTo());
                        }
                        break;
                    case CONSTS.CMD_SEC_SURRENDER:
                        response = processSurrender(player);
                        if (CONSTS.RESPONSE_OK == response.getRes()) {
                            broadService.onSurrender(player, response);
                        }
                        break;
                    case CONSTS.CMD_SEC_OVERTIME:
                        response = processOvertime(player);
                        if (CONSTS.RESPONSE_OK == response.getRes()) {
                            broadService.onOverTime(player, response);
                        }
                        break;
                    case CONSTS.CMD_SEC_LOGOUT:
                        response.setResponseType(CONSTS.CMD_SEC_LOGOUT);
                        response.setRes(CONSTS.RESPONSE_OK);
                        broadService.onLogout(player);
                        break;
                    case CONSTS.CMD_SEC_MESSAGE:
                        broadService.onMessage(player, cr.getMessage());
                        break;
                    default:
                        response.setRes(CONSTS.RESPONSE_ERROR);
                        break;
                }
            }
        }
        try {
            ChessPackage cp = new ChessPackage(CONSTS.CMD_SEC);
            byte[] resp = SerialUtil.enSerial(response);
            resp = Secret.encryptMsg(resp, player.getDesKey());
            cp.setContent(resp);
            session.write(cp);
            if (response.getResponseType() == CONSTS.CMD_SEC_LOGOUT) {
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processManager(IoSession session, byte[] content) {
        Serializable s = null;
        try {
            s = SerialUtil.deSerial(content);
        } catch (Exception e) {
            e.printStackTrace();
            s = null;
        }
        Request cr = (Request) s;
        if (s == null || cr == null) {
            logger.trace("server CMD_SEC ERROR! SER=NULL");
        } else {
            switch(cr.getCommand()) {
                case CONSTS.CMD_MANAGER_LAYOUT:
                    CommonLayoutService cls = new CommonLayoutService();
                    ChessMan[][] layout = layoutService.getDeskLayout(cr.getDeskNum());
                    cls.showLayout(layout);
                    break;
                case CONSTS.CMD_MANAGER_USERS:
                    Hashtable<String, Player> players = playerService.getPlayers();
                    System.out.println("player size=" + players.size());
                    Enumeration<Player> pe = players.elements();
                    while (pe.hasMoreElements()) {
                        Player cs = pe.nextElement();
                        System.out.println("name=" + cs.getName() + " seatName=" + cs.getSeatName());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Response processLogin(Request cr, Player player, IoSession session) {
        Response response = new Response();
        response.setResponseType(CONSTS.CMD_SEC_LOGIN);
        if (userLogin(cr.getPlayerName())) {
            logger.trace("server CMD_SEC_LOGIN error! playerName=" + cr.getPlayerName() + " all ready login");
            response.setMessage(cr.getPlayerName() + " 已登录！");
            response.setRes(CONSTS.RESPONSE_ERROR);
        } else if (player.getStates() == CONSTS.PLAYER_STATS_HAND2) {
            player.setName(cr.getPlayerName());
            player.setPass(cr.getPlayerPass());
            player.setStates(CONSTS.PLAYER_STATS_LOGIN);
            player.setSession(session);
            response.setRes(CONSTS.RESPONSE_OK);
            response.setDesks(chessDeskService.getDesks());
            logger.trace("server CMD_SEC_LOGIN OK! playerName=" + cr.getPlayerName() + " playPass=" + cr.getPlayerPass() + " loged in");
        } else {
            logger.trace("server CMD_SEC_LOGIN error! playerName=" + cr.getPlayerName() + " playPass=" + cr.getPlayerPass());
            response.setRes(CONSTS.RESPONSE_ERROR);
        }
        return response;
    }

    private Response processOnSeat(Request cr, Player player) {
        Response response = new Response();
        response.setResponseType(CONSTS.CMD_SEC_ONSEAT);
        logger.trace("server before CMD_SEC_ONSEAT deskNum=" + cr.getDeskNum() + " seatName=" + cr.getSeatName());
        ChessDesk cd = chessDeskService.getChessDesk(cr.getDeskNum());
        ChessSeat cs = cd.getSeats().get(cr.getSeatName());
        if (CONSTS.PLAYER_STATS_LOGIN != player.getStates()) {
            logger.trace("server CMD_SEC ERROR! state error");
            response.setRes(CONSTS.RESPONSE_ERROR);
        } else if (cd.getDeskStates() == DESK_STATE.PLAYING || cs.getSeatState() != SEAT_STATE.EMPTY) {
            response.setRes(CONSTS.RESPONSE_ERROR);
            response.setMessage("不能坐下");
            logger.trace("server CMD_SEC_ONSEAT error! deskStates=" + cd.getDeskStates() + " deskNum=" + cr.getDeskNum() + " seatName=" + cr.getSeatName() + " seatState=" + cs.getSeatState());
        } else {
            player.setDeskNum(cr.getDeskNum());
            player.setSeatName(cr.getSeatName());
            player.setStates(CONSTS.PLAYER_STATS_ONSEAT);
            cd.setDeskStates(DESK_STATE.PREPARE);
            cs.setSeatState(SEAT_STATE.ONSEAT);
            cs.setPlayerID(player.getId());
            if (cd.getDeskStates() == DESK_STATE.EMPTY) {
                layoutService.initChessLayout(cr.getDeskNum());
            }
            response.setLayout(layoutService.getDeskLayout(player));
            response.setChessDesk(chessDeskService.getChessDesk(cr.getDeskNum()));
            response.setRes(CONSTS.RESPONSE_OK);
            logger.trace("server CMD_SEC_ONSEAT OK! deskStates=" + cd.getDeskStates() + " deskNum=" + cr.getDeskNum() + " seatName=" + cr.getSeatName() + " seatState=" + cs.getSeatState());
        }
        return response;
    }

    private Response processReady(Request cr, Player player) {
        Response response = new Response();
        response.setResponseType(CONSTS.CMD_SEC_READY);
        ChessDesk cd = chessDeskService.getChessDesk(player.getDeskNum());
        new CommonLayoutService().copyPlayerLayout(cr.getLayout(), layoutService.getDeskLayout(player.getDeskNum()), player.getSeatName());
        player.setStates(CONSTS.PLAYER_STATS_READY);
        cd.setDeskStates(DESK_STATE.PREPARE);
        if (new ChessService().isAllReady(cd.getSeats())) {
            player.setStates(CONSTS.PLAYER_STATS_PLAYING);
            Hashtable<SEAT_NAME, ChessSeat> seats = cd.getSeats();
            Enumeration<ChessSeat> eSeat = seats.elements();
            while (eSeat.hasMoreElements()) {
                ChessSeat seat = eSeat.nextElement();
                if (seat.getPlayerID() != null) {
                    playerService.getPlayer(seat.getPlayerID()).setStates(CONSTS.PLAYER_STATS_PLAYING);
                }
            }
            cd.setDeskStates(DESK_STATE.PLAYING);
            cd.setOnTurn(SEAT_NAME.S1);
            logger.trace("server CMD_SEC_READY isAllReady=true PLAYING!");
        }
        response.setLayout(layoutService.getDeskLayout(player));
        response.setChessDesk(cd);
        response.setRes(CONSTS.RESPONSE_OK);
        logger.trace("server CMD_SEC_READY OK! deskNum=" + player.getDeskNum() + " seatName=" + player.getSeatName() + " name=" + player.getName());
        return response;
    }

    private Response processEat(Request cr, Player player) {
        Response response = new Response();
        response.setResponseType(CONSTS.CMD_SEC_EAT);
        response.setRes(CONSTS.RESPONSE_OK);
        Position from = cr.getFrom();
        Position to = cr.getTo();
        ChessMan[][] layout = layoutService.getDeskLayout(player.getDeskNum());
        SEAT_NAME fromTeam = layout[from.row][from.col].getTeam();
        SEAT_NAME toTeam = layout[to.row][to.col].getTeam();
        Hashtable<SEAT_NAME, ChessSeat> seats = chessDeskService.getChessDesk(player.getDeskNum()).getSeats();
        Player toPlayer = playerService.getPlayer(seats.get(toTeam).getPlayerID());
        if (!fromTeam.equals(player.getSeatName())) {
            response.setRes(CONSTS.RESPONSE_ERROR);
            response.setMessage("不可以移动他人棋子");
        } else if (chessService.sameTeamChessMan(fromTeam, toTeam)) {
            response.setRes(CONSTS.RESPONSE_ERROR);
            response.setMessage("不可以吃本队的棋子");
        } else if (!new ChessService().canMoveHere(layout, layout[from.row][from.col], to)) {
            response.setMessage("你的吃棋子操作不符合规则");
            response.setRes(CONSTS.RESPONSE_ERROR);
        } else {
            NAME fName = layout[from.row][from.col].getName();
            NAME tName = layout[to.row][to.col].getName();
            if (fName.equals(NAME.GONGBING)) {
                if (tName.equals(NAME.JUNQI) || tName.equals(NAME.DILEI)) {
                    response.setChessManStates(CONSTS.RESPONSE_EAT_WIN);
                } else if (tName.equals(NAME.ZHANGDAN) || tName.equals(NAME.GONGBING)) {
                    response.setChessManStates(CONSTS.RESPONSE_EAT_DRAW);
                } else {
                    response.setChessManStates(CONSTS.RESPONSE_EAT_FAIL);
                }
            } else if (fName.equals(NAME.ZHANGDAN)) {
                response.setChessManStates(CONSTS.RESPONSE_EAT_DRAW);
            } else if (tName.equals(NAME.ZHANGDAN)) {
                response.setChessManStates(CONSTS.RESPONSE_EAT_DRAW);
            } else if (fName.compareTo(tName) < 0) {
                response.setChessManStates(CONSTS.RESPONSE_EAT_FAIL);
            } else if (fName.compareTo(tName) == 0) {
                response.setChessManStates(CONSTS.RESPONSE_EAT_DRAW);
            } else if (fName.compareTo(tName) > 0) {
                response.setChessManStates(CONSTS.RESPONSE_EAT_WIN);
            }
            if (CONSTS.RESPONSE_EAT_WIN == response.getChessManStates()) {
                try {
                    layout[to.row][to.col] = layout[from.row][from.col].deepClone();
                } catch (OptionalDataException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                layout[to.row][to.col].getPosition().row = to.row;
                layout[to.row][to.col].getPosition().col = to.col;
                layout[from.row][from.col] = null;
            } else if (CONSTS.RESPONSE_EAT_FAIL == response.getChessManStates()) {
                layout[from.row][from.col] = null;
            } else if (CONSTS.RESPONSE_EAT_DRAW == response.getChessManStates()) {
                layout[to.row][to.col] = null;
                layout[from.row][from.col] = null;
            }
            if (NAME.JUNQI.equals(tName)) {
                response.setSeatStates(CONSTS.RESPONSE_EAT_WIN);
                toPlayer.setStates(CONSTS.PLAYER_STATS_FAIL);
            } else if (!chessService.haveAnyChessCanMove(layout, toTeam)) {
                response.setSeatStates(CONSTS.RESPONSE_EAT_WIN);
                toPlayer.setStates(CONSTS.PLAYER_STATS_FAIL);
            } else if (!chessService.haveAnyChessCanMove(layout, fromTeam)) {
                response.setSeatStates(CONSTS.RESPONSE_EAT_FAIL);
                player.setStates(CONSTS.PLAYER_STATS_FAIL);
            } else {
                response.setSeatStates(CONSTS.RESPONSE_EAT_CONTINUE);
            }
            int teamRes = chessService.getTeamRes(player);
            response.setTeamStates(teamRes);
            if (teamRes != CONSTS.RESPONSE_EAT_CONTINUE) {
            }
            if (fName.equals(NAME.SILING) && tName.equals(NAME.SILING)) {
                showBanner(player.getDeskNum(), fromTeam);
                showBanner(player.getDeskNum(), toTeam);
            } else if (fName.equals(NAME.SILING) && response.getChessManStates() != CONSTS.RESPONSE_EAT_WIN) {
                showBanner(player.getDeskNum(), fromTeam);
            } else if (tName.equals(NAME.SILING) && response.getChessManStates() != CONSTS.RESPONSE_EAT_FAIL) {
                showBanner(player.getDeskNum(), toTeam);
            }
            if (CONSTS.RESPONSE_EAT_WIN == response.getSeatStates()) {
                for (int i = 0; i < 13; i++) {
                    for (int j = 0; j < 20; j++) {
                        if (layout[i][j] != null && toTeam.equals(layout[i][j].getTeam())) {
                            layout[i][j] = null;
                        }
                    }
                }
            } else if (CONSTS.RESPONSE_EAT_FAIL == response.getSeatStates()) {
                for (int i = 0; i < 13; i++) {
                    for (int j = 0; j < 20; j++) {
                        if (layout[i][j] != null && fromTeam.equals(layout[i][j].getTeam())) {
                            layout[i][j] = null;
                        }
                    }
                }
            } else {
            }
            updateOnTurnPlayer(chessDeskService.getChessDesk(player.getDeskNum()));
        }
        response.setChessDesk(chessDeskService.getChessDesk(player.getDeskNum()));
        response.setLayout(layoutService.getDeskLayout(player));
        return response;
    }

    private Response processMove(Request cr, Player player) {
        Response response = new Response();
        response.setResponseType(CONSTS.CMD_SEC_MOVE);
        response.setRes(CONSTS.RESPONSE_OK);
        Position from = cr.getFrom();
        Position to = cr.getTo();
        logger.trace("CMD_SEC_MOVE server move from " + from.toString() + " to=" + to.toString());
        ChessMan[][] layout = layoutService.getDeskLayout(player.getDeskNum());
        SEAT_NAME fromTeam = layout[from.row][from.col].getTeam();
        if (!fromTeam.equals(player.getSeatName())) {
            response.setMessage("不可以移动他人棋子");
            response.setRes(CONSTS.RESPONSE_ERROR);
        } else if (!new ChessService().canMoveHere(layout, layout[from.row][from.col], to)) {
            response.setMessage("你的棋子移动不符合规则");
            response.setRes(CONSTS.RESPONSE_ERROR);
        } else {
            try {
                layout[to.row][to.col] = layout[from.row][from.col].deepClone();
            } catch (OptionalDataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            layout[to.row][to.col].getPosition().row = to.row;
            layout[to.row][to.col].getPosition().col = to.col;
            layout[from.row][from.col] = null;
            updateOnTurnPlayer(chessDeskService.getChessDesk(player.getDeskNum()));
        }
        response.setChessDesk(chessDeskService.getChessDesk(player.getDeskNum()));
        response.setLayout(layoutService.getDeskLayout(player));
        return response;
    }

    /**
	 * 投降
	 * 
	 * @param player
	 * @return
	 */
    private Response processSurrender(Player player) {
        Response response = new Response();
        response.setResponseType(CONSTS.CMD_SEC_SURRENDER);
        response.setRes(CONSTS.RESPONSE_OK);
        if (CONSTS.PLAYER_STATS_PLAYING == player.getStates()) {
            player.setStates(CONSTS.PLAYER_STATS_FAIL);
            ChessMan[][] layout = layoutService.getDeskLayout(player.getDeskNum());
            for (int i = 0; i < 13; i++) {
                for (int j = 0; j < 20; j++) {
                    if (layout[i][j] != null && player.getSeatName().equals(layout[i][j].getTeam())) {
                        layout[i][j] = null;
                    }
                }
            }
            int teamRes = chessService.getTeamRes(player);
            response.setTeamStates(teamRes);
            if (teamRes != CONSTS.RESPONSE_EAT_CONTINUE) {
            }
            updateOnTurnPlayer(chessDeskService.getChessDesk(player.getDeskNum()));
            response.setChessDesk(chessDeskService.getChessDesk(player.getDeskNum()));
            response.setLayout(layoutService.getDeskLayout(player));
        } else {
            response.setRes(CONSTS.RESPONSE_ERROR);
            response.setMessage("状态错误！");
        }
        return response;
    }

    /**
	 * 超时处理
	 * 
	 * @param player
	 * @return
	 */
    private Response processOvertime(Player player) {
        Response response = new Response();
        response.setResponseType(CONSTS.CMD_SEC_OVERTIME);
        response.setRes(CONSTS.RESPONSE_OK);
        if (chessDeskService.getChessDesk(player.getDeskNum()).getOnTurn().equals(player.getSeatName()) && CONSTS.PLAYER_STATS_PLAYING == player.getStates()) {
            player.setOvertimeTimes(player.getOvertimeTimes() + 1);
            if (player.getOvertimeTimes() >= 3) {
                player.setStates(CONSTS.PLAYER_STATS_FAIL);
                ChessMan[][] layout = layoutService.getDeskLayout(player.getDeskNum());
                for (int i = 0; i < 13; i++) {
                    for (int j = 0; j < 20; j++) {
                        if (layout[i][j] != null && player.getSeatName().equals(layout[i][j].getTeam())) {
                            layout[i][j] = null;
                        }
                    }
                }
                int teamRes = chessService.getTeamRes(player);
                response.setTeamStates(teamRes);
                if (teamRes != CONSTS.RESPONSE_EAT_CONTINUE) {
                }
            }
            System.out.println("cur seat=" + chessDeskService.getChessDesk(player.getDeskNum()).getOnTurn().name());
            SEAT_NAME next = updateOnTurnPlayer(chessDeskService.getChessDesk(player.getDeskNum()));
            System.out.println("next seat=" + next.name());
            response.setChessDesk(chessDeskService.getChessDesk(player.getDeskNum()));
            response.setLayout(layoutService.getDeskLayout(player));
        } else {
            response.setRes(CONSTS.RESPONSE_ERROR);
            response.setMessage("状态错误！");
        }
        return response;
    }

    /**
	 * 亮旗
	 * 
	 * @param deskNum
	 * @param sn
	 */
    private void showBanner(int deskNum, SEAT_NAME sn) {
        ChessMan[][] layout = layoutService.getDeskLayout(deskNum);
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 20; j++) {
                if (layout[i][j] != null && layout[i][j].getTeam().equals(sn) && layout[i][j].getName().equals(NAME.JUNQI)) {
                    layout[i][j].setShowLevel(SHOWLEVEL.DESK);
                }
            }
        }
    }

    /**
	 * 是否已登录
	 * 
	 * @param name
	 * @return
	 */
    public boolean userLogin(String name) {
        Hashtable<String, Player> p = playerService.getPlayers();
        Enumeration<Player> e = p.elements();
        while (e.hasMoreElements()) {
            if (e.nextElement().getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public SEAT_NAME updateOnTurnPlayer(ChessDesk cd) {
        SEAT_NAME cur = cd.getOnTurn();
        SEAT_NAME next = getNextSeat(cur);
        int i = 0;
        while (i++ < 8) {
            if (cd.getSeats().get(next).getSeatState().equals(SEAT_STATE.ONSEAT)) {
                String playerID = cd.getSeats().get(next).getPlayerID();
                if (CONSTS.PLAYER_STATS_PLAYING == playerService.getPlayer(playerID).getStates()) {
                    cd.setOnTurn(next);
                    return next;
                }
            } else {
                System.out.println("server not on seat=" + next.name());
            }
            next = getNextSeat(next);
        }
        return null;
    }

    private SEAT_NAME getNextSeat(SEAT_NAME s) {
        if (s.equals(SEAT_NAME.S1)) {
            return SEAT_NAME.S5;
        } else if (s.equals(SEAT_NAME.S5)) {
            return SEAT_NAME.S2;
        } else if (s.equals(SEAT_NAME.S2)) {
            return SEAT_NAME.S6;
        } else if (s.equals(SEAT_NAME.S6)) {
            return SEAT_NAME.S3;
        } else if (s.equals(SEAT_NAME.S3)) {
            return SEAT_NAME.S7;
        } else if (s.equals(SEAT_NAME.S7)) {
            return SEAT_NAME.S4;
        } else if (s.equals(SEAT_NAME.S4)) {
            return SEAT_NAME.S8;
        } else if (s.equals(SEAT_NAME.S8)) {
            return SEAT_NAME.S1;
        } else {
            return SEAT_NAME.S1;
        }
    }
}
