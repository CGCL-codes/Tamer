package com.pragmaticminds.forrest.model;

import com.pragmaticminds.forrest.geometry.Position;
import com.pragmaticminds.forrest.geometry.Line;
import com.pragmaticminds.forrest.geometry.Function;
import com.pragmaticminds.forrest.game.Player;
import com.pragmaticminds.forrest.utils.Parameters;
import com.pragmaticminds.forrest.utils.Types.*;
import com.pragmaticminds.forrest.utils.CalculatePoint;
import futbol.tacticas.SituacionJugadores;
import java.awt.Point;

public class World {

    private Player[] teamMates = new Player[11];

    private Player[] opponents = new Player[11];

    private Position ballPosition;

    private static World instance;

    private static boolean optionDribble = true;

    private World() {
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World();
        }
        return instance;
    }

    public void setState(SituacionJugadores s) {
        Point[] team = s.getMisJugadores();
        Point[] opps = s.getContrario();
        for (int i = 0; i < s.getMisJugadores().length; i++) {
            teamMates[i].setActualPosition(new Position(team[i].getX(), team[i].getY()));
            opponents[i].setActualPosition(new Position(opps[i].getX(), opps[i].getY()));
        }
        ballPosition = new Position(s.getBola().getX(), s.getBola().getY());
    }

    public Player getTeamMate(int nroJugador) {
        return teamMates[nroJugador];
    }

    public Player getOpponent(int nroJugador) {
        return opponents[nroJugador];
    }

    public Player getOpponentNearness(Position pos) {
        double dMinMag = 1000.0;
        Player player;
        Player playerNearness = null;
        for (int i = 0; i < 11; i++) {
            player = opponents[i];
            if (pos.getDistanceTo(player.getActualPosition()) < dMinMag) {
                dMinMag = pos.getDistanceTo(player.getActualPosition());
                playerNearness = player;
            }
        }
        return playerNearness;
    }

    public Player getTeammateNearness(Position pos) {
        double dMinMag = 1000.0;
        Player player;
        Player playerNearness = null;
        for (int i = 0; i < 11; i++) {
            player = teamMates[i];
            if (pos.getDistanceTo(player.getActualPosition()) < dMinMag) {
                dMinMag = pos.getDistanceTo(player.getActualPosition());
                playerNearness = player;
            }
        }
        return playerNearness;
    }

    public int getCountOpponentInCone(double angCone, Position start, Position end) {
        int iNr = 0;
        Line line = Line.makeLineFromTwoPoints(start, end);
        Position posOnLine;
        Position posPlayer;
        for (int i = 0; i < 11; i++) {
            posPlayer = opponents[i].getActualPosition();
            posOnLine = line.getPointOnLineClosestTo(posPlayer);
            if (posOnLine.getDistanceTo(posPlayer) < angCone * posOnLine.getDistanceTo(start) && line.isInBetween(posOnLine, start, end) && start.getDistanceTo(posPlayer) < start.getDistanceTo(end)) {
                iNr++;
            }
        }
        return iNr;
    }

    public int getCountTeamMatesInCone(double angCone, Position start, Position end) {
        int iNr = 0;
        Line line = Line.makeLineFromTwoPoints(start, end);
        Position posOnLine;
        Position posPlayer;
        for (int i = 0; i < 11; i++) {
            posPlayer = teamMates[i].getActualPosition();
            posOnLine = line.getPointOnLineClosestTo(posPlayer);
            if (posOnLine.getDistanceTo(posPlayer) < angCone * posOnLine.getDistanceTo(start) && line.isInBetween(posOnLine, start, end) && start.getDistanceTo(posPlayer) < start.getDistanceTo(end)) {
                iNr++;
            }
        }
        return iNr;
    }

    public boolean isInField(Position pos, double dMargin) {
        return true;
    }

    public Position getCenterPosGoalOpponents() {
        return new Position(0, 0);
    }

    public void getPosGoalOpponents(Position paloI, Position paloD) {
        paloI = new Position(0, 0);
        paloD = new Position(0, 0);
    }

    public void setDribbleOption(boolean option) {
        optionDribble = option;
    }

    /****** VARIABLE DE ESTADO: esEspacioVacio ******/
    public boolean isEmptySpace(int nroJugador, DirectionT dir) {
        Player player = getTeamMate(nroJugador);
        Position posDestino = CalculatePoint.calculateNewPoint(player.getActualPosition(), dir, Parameters.getParameter(ParaT.PARA_DIST_DRIBBLE_VAREST));
        Line l = Line.makeLineFromTwoPoints(player.getActualPosition(), posDestino);
        if (!optionDribble) {
            return false;
        }
        if (!isInField(posDestino, 1)) {
            return false;
        }
        double angRad = Function.Deg2Rad(30);
        int cantOpp = getCountOpponentInCone(angRad, player.getActualPosition(), posDestino);
        if (cantOpp != 0) {
            return false;
        }
        Player opponent = getOpponentNearness(player.getActualPosition());
        if (player.getActualPosition().getDistanceTo(opponent.getActualPosition()) < 2.2) {
            return false;
        }
        return true;
    }

    /****** VARIABLE DE ESTADO: paseNoBloqueado ******/
    public boolean passNotBlocked(int nroPasador, int nroReceptor, DirectionT dirPase) {
        Player playerPasador = getTeamMate(nroPasador);
        Player playerReceptor = getTeamMate(nroReceptor);
        double distPase = getDistPass(playerPasador.getActualPosition(), playerReceptor.getActualPosition());
        Position posDestino = CalculatePoint.calculateNewPoint(playerReceptor.getActualPosition(), dirPase, distPase);
        Line l = Line.makeLineFromTwoPoints(playerPasador.getActualPosition(), posDestino);
        if (!isPassInRange(playerPasador.getActualPosition(), posDestino)) {
            return false;
        }
        if (!isInField(posDestino, 1)) {
            return false;
        }
        double angRad = Function.Deg2Rad(27);
        Position posGoal = getCenterPosGoalOpponents();
        double distArcoOpon = playerPasador.getActualPosition().getDistanceTo(posGoal);
        if (distArcoOpon < 20) angRad = Function.Deg2Rad(26); else angRad = Function.Deg2Rad(28);
        int cantOpp = getCountOpponentInCone(angRad, playerPasador.getActualPosition(), posDestino);
        if (cantOpp != 0) {
            return false;
        }
        Player opponent = getOpponentNearness(posDestino);
        if (playerPasador.getActualPosition().getDistanceTo(opponent.getActualPosition()) < 1.0) {
            return false;
        }
        return true;
    }

    /****** VARIABLE DE ESTADO: paseEnRango ******/
    public boolean isPassInRange(Position posPasador, Position posReceptor) {
        Position posGoal = getCenterPosGoalOpponents();
        double distArcoOpon = posReceptor.getDistanceTo(posGoal);
        double distAgentes = posPasador.getDistanceTo(posReceptor);
        double mtsAtras;
        if (distAgentes > Parameters.getParameter(ParaT.PARA_MAX_DIST_PASE)) return false;
        if (distArcoOpon < 25.0) mtsAtras = 7.5; else if (distArcoOpon < 60) mtsAtras = 7.5; else if (distArcoOpon < 65) mtsAtras = 2.5; else mtsAtras = 2.5;
        if (posReceptor.getPositionX() >= posPasador.getPositionX() - mtsAtras) return true; else return false;
    }

    /****** VARIABLE DE ESTADO: getMaxDistPaseAtras ********/
    public double getMaxDistPassBack() {
        return 7.5;
    }

    /****** VARIABLE DE ESTADO: tiroNoBloqueado ******/
    public boolean shootNotBlocked(Position posPateador, Position posDestino) {
        return true;
    }

    /****** VARIABLE DE ESTADO: esPosicionDeAtaque ******/
    public boolean isAttackPosition(int nroJugador) {
        Position posJugador = getTeamMate(nroJugador).getActualPosition();
        return isAttackPosition(posJugador);
    }

    public boolean isAttackPosition(Position posJugador) {
        Position posGoal = getCenterPosGoalOpponents();
        double distArcoOpon = posJugador.getDistanceTo(posGoal);
        if (distArcoOpon < Parameters.getParameter(ParaT.PARA_ATTACK_ZONE)) return true; else return false;
    }

    /****** VARIABLE DE ESTADO: esPosicionDribbleEastWest ******/
    public boolean isPositionDribbleEastWest(Position posJugador) {
        Position posGoal = getCenterPosGoalOpponents();
        double distArcoOpon = posJugador.getDistanceTo(posGoal);
        if (distArcoOpon < 35) return true; else return false;
    }

    public double getDistPass(Position posPasador, Position posReceptor) {
        double adyacente = posPasador.getDistanceTo(posReceptor);
        double distPase;
        double tangente = Function.tanDeg(14.0);
        double topeDistPase = 3.5;
        if (adyacente < 5.0) distPase = 0; else {
            distPase = tangente * adyacente;
            if (distPase > topeDistPase) distPase = topeDistPase;
        }
        return distPase;
    }

    public double getEvaluation(AccionT accion, Position posInicial, Position posFinal) {
        return getEvaluation(accion, posInicial, posFinal, null);
    }

    public double getEvaluation(AccionT accion, Position posInicial, Position posFinal, Double angPateo) {
        double utilidad, facPateo, angPateoTemp = 0;
        if (accion == AccionT.ACCION_SCORE) {
            utilidad = Utility.getUtility(posInicial, angPateo);
            if (utilidad > 90) utilidad = Utility.getMaxUtility();
        } else if (accion == AccionT.ACCION_CLEARBALL) {
            if (World.getInstance().isAttackPosition(posInicial)) {
                facPateo = Utility.getFScore(posInicial, angPateoTemp);
                if (facPateo != 1) utilidad = Utility.getUtility(posInicial, angPateoTemp) + 1; else utilidad = Utility.getMinUtility();
            } else utilidad = Utility.getMinUtility();
        } else utilidad = Utility.getUtility(posFinal, null);
        return utilidad;
    }

    public double getMaxEvaluation() {
        return Utility.getMaxUtility();
    }

    public double getMinEvaluation() {
        return Utility.getMinUtility();
    }
}
