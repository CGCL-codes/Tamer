package com.kirolak.jsf.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import com.kirolak.KirolakObject;
import com.kirolak.LocalizedListItem;
import com.kirolak.ScoreMode;
import com.kirolak.StageType;
import com.kirolak.dao.MatchStatusDAO;
import com.kirolak.dao.ScoreModeDAO;
import com.kirolak.dao.StageTypeDAO;

public class Kirolak implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KirolakObject> scoreModes;

    private List<SelectItem> scoreModesSelectItems;

    private List<KirolakObject> stageTypes;

    private List<SelectItem> stageTypesSelectItems;

    private List<KirolakObject> matchStatus;

    private List<SelectItem> matchStatusSelectItems;

    public List<KirolakObject> getScoreModes() {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        this.scoreModes = ScoreModeDAO.list(locale);
        return this.scoreModes;
    }

    public List<SelectItem> getScoreModesSelectItems() {
        getScoreModes();
        this.scoreModesSelectItems = new ArrayList<SelectItem>();
        Iterator<KirolakObject> scoreModesIterator = this.scoreModes.iterator();
        while (scoreModesIterator.hasNext()) {
            KirolakObject scoreMode = scoreModesIterator.next();
            this.scoreModesSelectItems.add(new SelectItem(((ScoreMode) scoreMode).getId(), scoreMode.getName()));
        }
        return this.scoreModesSelectItems;
    }

    public List<KirolakObject> getStageTypes() {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        this.stageTypes = StageTypeDAO.list(locale);
        return this.stageTypes;
    }

    public List<SelectItem> getStageTypesSelectItems() {
        getStageTypes();
        if (this.stageTypesSelectItems == null) {
            this.stageTypesSelectItems = new ArrayList<SelectItem>();
            Iterator<KirolakObject> stageTypesIterator = stageTypes.iterator();
            while (stageTypesIterator.hasNext()) {
                KirolakObject stageType = stageTypesIterator.next();
                this.stageTypesSelectItems.add(new SelectItem(((StageType) stageType).getId(), stageType.getName()));
            }
        }
        return this.stageTypesSelectItems;
    }

    public List<KirolakObject> getMatchStatus() {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        this.matchStatus = MatchStatusDAO.list(locale);
        return this.matchStatus;
    }

    public List<SelectItem> getMatchStatusSelectItems() {
        getMatchStatus();
        if (this.matchStatusSelectItems == null) {
            this.matchStatusSelectItems = new ArrayList<SelectItem>();
            Iterator<KirolakObject> iterator = matchStatus.iterator();
            while (iterator.hasNext()) {
                KirolakObject matchStatus = iterator.next();
                this.matchStatusSelectItems.add(new SelectItem(((LocalizedListItem) matchStatus).getId(), matchStatus.getName()));
            }
        }
        return this.matchStatusSelectItems;
    }

    public Iterator<Locale> getSupportedLocales() {
        return FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
    }
}
