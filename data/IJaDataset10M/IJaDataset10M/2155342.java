package com.appspot.battlerafts.servlets.lobby;

import com.appspot.battlerafts.classes.Game;
import com.appspot.battlerafts.enums.GameState;
import com.appspot.battlerafts.jsons.GameStatJSON;
import com.appspot.battlerafts.utils.PMF;
import com.google.gson.Gson;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tonis
 * Date: 01.04.12
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
public class GetGamesStat extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query q = pm.newQuery("select gameID from " + Game.class.getName());
        q.setFilter("gameState == stateParam");
        q.declareParameters("String stateParam");
        List<String> waitingGameList = (List<String>) q.execute(GameState.WAITING);
        int nrOfWaitingGames = waitingGameList.size();
        List<String> runningGameList = (List<String>) q.execute(GameState.RUNNING);
        int nrOfRunningGames = runningGameList.size();
        Gson gson = new Gson();
        GameStatJSON gameStatJSON = new GameStatJSON();
        gameStatJSON.nrOfRunningGames = nrOfRunningGames;
        gameStatJSON.nrOfWaitingGames = nrOfWaitingGames;
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(gson.toJson(gameStatJSON));
    }
}
