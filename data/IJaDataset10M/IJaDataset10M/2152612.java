package com.nccsjz.back.game.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import com.nccsjz.back.game.service.GameService;
import com.nccsjz.base.BaseAction;
import com.nccsjz.pojo.Game;
import com.nccsjz.utils.Pager;

@SuppressWarnings("serial")
public class GameAction extends BaseAction {

    private long id;

    private Pager pager;

    private int pageNo = 1;

    private int pageSize = 20;

    private String[] checkbox;

    private File[] myFile;

    private String[] myFilecontentType;

    private String[] myFileFileName;

    private String gameTitle;

    private String savePath;

    private Game game;

    private GameService gameService;

    private String gameDesc;

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameDesc() {
        return gameDesc;
    }

    public void setGameDesc(String gameDesc) {
        this.gameDesc = gameDesc;
    }

    public File[] getMyFile() {
        return myFile;
    }

    public void setMyFile(File[] myFile) {
        this.myFile = myFile;
    }

    public String[] getMyFilecontentType() {
        return myFilecontentType;
    }

    public void setMyFilecontentType(String[] myFilecontentType) {
        this.myFilecontentType = myFilecontentType;
    }

    public String[] getMyFileFileName() {
        return myFileFileName;
    }

    public void setMyFileFileName(String[] myFileFileName) {
        this.myFileFileName = myFileFileName;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    private String getSavePath() throws Exception {
        return ServletActionContext.getRequest().getRealPath(savePath);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String[] checkbox) {
        this.checkbox = checkbox;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String list_Game() throws Exception {
        gameService = new GameService();
        pager = gameService.getGame(pageSize, pageNo);
        return SUCCESS;
    }

    public String listF_Game() throws Exception {
        gameService = new GameService();
        pager = gameService.getGame(pageSize, pageNo);
        return "listf";
    }

    public String delete_Game() throws Exception {
        gameService = new GameService();
        gameService.deleteGameFile(id);
        boolean flag = gameService.deleteGame(id);
        if (flag) {
            return "list";
        } else {
            return ERROR;
        }
    }

    public String view_Game() throws Exception {
        gameService = new GameService();
        game = gameService.getGame(id);
        return "view";
    }

    public String list_GameByTitle() throws Exception {
        gameService = new GameService();
        pager = gameService.getGame(pageSize, pageNo, gameTitle);
        return SUCCESS;
    }

    public String deleteBatch_Game() throws SQLException {
        gameService = new GameService();
        List list = new ArrayList();
        for (int i = 0; i < checkbox.length; i++) {
            list.add(Long.parseLong(checkbox[i]));
        }
        gameService.deldetBatchGame(list);
        return "list";
    }

    public String add_Game() throws Exception {
        File[] files = getMyFile();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        utils.FileUtils.makeDir(getSavePath());
        String gamePath = getSavePath() + File.separator + getMyFileFileName()[0];
        String gamePic = getSavePath() + File.separator + getMyFileFileName()[1];
        for (int i = 0; i < files.length; i++) {
            fos = new FileOutputStream(getSavePath() + File.separator + getMyFileFileName()[i]);
            fis = new FileInputStream(files[i]);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
        fos.close();
        fis.close();
        Game game = new Game();
        game.setGametitle(gameTitle);
        game.setGamepath(gamePath);
        game.setUploaddate(new Date());
        game.setGamedesc(gameDesc);
        game.setGamepic(gamePic);
        gameService = new GameService();
        boolean flag = gameService.addGame(game);
        if (flag) {
            return "list";
        } else {
            return ERROR;
        }
    }
}
