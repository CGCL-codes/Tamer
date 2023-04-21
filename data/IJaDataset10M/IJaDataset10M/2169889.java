package org.jogre.webapp.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.jogre.server.data.SnapShot;
import org.jogre.server.data.db.IDatabase;
import org.jogre.webapp.IJogreWeb;
import org.jogre.webapp.data.OnlineGame;
import org.jogre.webapp.forms.GameListForm;

/**
 * Game list action.
 * 
 * @author  Bob Marks
 * @version Beta 0.3
 */
public class GameListAction extends AbstractJogreAction {

    private static final String DEFAULT_COL = "game";

    private static final int DEFAULT_DIR = 1;

    /**
	 * Execute logging in.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.init(request);
        if (errors.size() > 0) return mapping.findForward(IJogreWeb.FORWARD_ERROR);
        populateForm(mapping, request, form);
        return mapping.findForward(IJogreWeb.FORWARD_GAME_LIST);
    }

    /**
	 * Populate form.
	 * 
	 * @param mapping
	 * @param request
	 * @param form
	 */
    public void populateForm(ActionMapping mapping, HttpServletRequest request, ActionForm form) {
        GameListForm gameListForm = (GameListForm) form;
        List games = new ArrayList();
        ActionMessages errors = new ActionMessages();
        String reqGenre = request.getParameter("genre");
        try {
            List snapshots = ibatis.getList(IDatabase.ST_SELECT_ALL_SNAP_SHOTS);
            for (int i = 0; i < snapshots.size(); i++) {
                SnapShot snapshot = (SnapShot) snapshots.get(i);
                String gameKey = snapshot.getGameKey();
                if (props.isSupportedGame(gameKey) && isGameGenre(gameKey, reqGenre)) {
                    OnlineGame game = new OnlineGame(snapshot, resources);
                    games.add(game);
                }
            }
            sortGameList(request, gameListForm, games);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            errors.add("sql.exception", new ActionMessage("error.exception.sql", sqlEx.getMessage()));
        } catch (Exception genEx) {
            genEx.printStackTrace();
            errors.add("gen.exception", new ActionMessage("error.exception.general", genEx.getMessage()));
        }
        if (errors.size() > 0) saveErrors(request, errors);
        gameListForm.setGames(games);
        if (reqGenre != null) gameListForm.setGenre(reqGenre);
    }

    /**
	 * Sort game list.
	 * 
	 * Note: slightly hacky at minute (i.e. not SQL sort) - will improve if required.
	 * 
	 * @param request
	 * @param gameListForm
	 * @param games
	 */
    private void sortGameList(HttpServletRequest request, GameListForm gameListForm, List games) {
        final String col;
        final int dir;
        if (null == gameListForm.getCol()) {
            col = DEFAULT_COL;
            dir = DEFAULT_DIR;
        } else {
            col = gameListForm.getCol();
            if (request.getParameter("n") != null) dir = gameListForm.getDir() * -1; else dir = DEFAULT_DIR;
        }
        gameListForm.setCol(col);
        gameListForm.setDir(dir);
        Collections.sort(games, new Comparator() {

            public int compare(Object obj1, Object obj2) {
                OnlineGame game1 = (OnlineGame) obj1;
                OnlineGame game2 = (OnlineGame) obj2;
                if (col.equals("game")) return game1.getGameName().compareTo(game2.getGameName()) * dir; else if (col.equals("genre")) return game1.getGameGenre().compareTo(game2.getGameGenre()) * dir; else if (col.equals("players")) return new Integer(game1.getNumOfUsers()).compareTo(new Integer(game2.getNumOfUsers())) * dir; else return game1.getGameSynopsis().compareTo(game2.getGameName()) * dir;
            }
        });
    }

    /**
	 * Does this game belong to a requested genre?
	 * 
	 * @param gameKey    Game key
	 * @param reqGenre   Requested genre to filter on.
	 * @return         
	 */
    private boolean isGameGenre(String gameKey, String reqGenre) {
        String gameGenre = resources.getMessage(gameKey + ".genre");
        if (IJogreWeb.GENRE_ALL_GAMES.equals(reqGenre)) return true; else return gameGenre.equals(reqGenre);
    }
}
