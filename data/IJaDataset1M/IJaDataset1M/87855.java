package com.mobfee.web.model;

import java.io.Serializable;
import com.mobfee.domain.GameArticle;
import com.mobfee.domain.GameInfo;
import com.mobfee.web.FileUploadBean;

public class GameArticleForm implements Serializable {

    private GameArticle gameArticle;

    public GameArticleForm(GameArticle gameArticle) {
        this.gameArticle = gameArticle;
    }

    public GameArticleForm() {
        this.gameArticle = new GameArticle();
    }

    public GameArticle getGameArticle() {
        return gameArticle;
    }

    public void setGameArticle(GameArticle gameArticle) {
        this.gameArticle = gameArticle;
    }
}
