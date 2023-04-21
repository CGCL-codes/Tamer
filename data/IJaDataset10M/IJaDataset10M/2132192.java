package de.lema.client.view.event.overview;

import java.util.Date;
import java.util.List;
import de.lema.transfer.RequestContainer;
import de.lema.transfer.container.HistoryLadenRequest;
import de.lema.transfer.container.HistoryLadenRequest.LadeModus;
import de.lema.transfer.container.LiveEventsLadenRequest;
import de.lema.transfer.container.OptionFilterLadenRequest;
import de.lema.transfer.to.KeyValue;
import de.lema.transfer.to.LogEventRow;
import de.lema.ui.shared.DatumUniqueUtil;
import de.lema.ui.shared.OptionFilter;

public class EventSucheViewState {

    private boolean istLiveNichtSuche;

    private String letzteSucheNachKiterienMitLike;

    private OptionFilter letzteSucheNachKiterienMitOptionFiler;

    private final EventSucheViewModel model;

    private boolean systemAnwendungGeladen;

    private LogEventRow systemHistoryFirst;

    private boolean systemHistoryGeladen;

    private LogEventRow systemHistoryLast;

    private boolean systemIstWaitingForAnswer;

    private LogEventRow systemLastLiveEventReceived;

    public EventSucheViewState(EventSucheViewModel model) {
        this.model = model;
    }

    public HistoryLadenRequest createHistoryLadenRequest(LadeModus modus) {
        long createDatumUnique = 0;
        if (modus == LadeModus.mitte) {
            createDatumUnique = DatumUniqueUtil.createDatumUnique(1, model.getAktuelleZeit().getTime());
        } else {
            if (modus == LadeModus.vor && systemHistoryLast != null) {
                createDatumUnique = systemHistoryLast.getDatumUnique();
            } else if (modus == LadeModus.zurueck && systemHistoryFirst != null) {
                createDatumUnique = systemHistoryFirst.getDatumUnique();
            }
        }
        if (createDatumUnique > 0) {
            final KeyValue<Integer> kriteriumId = model.getKriteriumId();
            return new HistoryLadenRequest(model.getInstanz().getId(), createDatumUnique, model.getOptionFilter(), kriteriumId != null ? kriteriumId.getId() : null, modus, model.getTabellenGroesse());
        } else {
            return null;
        }
    }

    public HistoryLadenRequest createExportRequest(Date von, Integer limit) {
        final KeyValue<Integer> kriteriumId = model.getKriteriumId();
        return new HistoryLadenRequest(model.getInstanz().getId(), DatumUniqueUtil.createDatumUnique(1, von.getTime()), model.getOptionFilter(), kriteriumId != null ? kriteriumId.getId() : null, LadeModus.vor, limit);
    }

    public LiveEventsLadenRequest createLiveEventsLadenRequest() {
        if (istLiveNichtSuche() && istValidLive()) {
            return (getSystemLastLiveEventReceived() == null) ? new LiveEventsLadenRequest(model.getInstanz().getId(), model.getTabellenGroesse(), null) : new LiveEventsLadenRequest(model.getInstanz().getId(), model.getTabellenGroesse(), getSystemLastLiveEventReceived().getDatumUnique(), null);
        } else {
            return null;
        }
    }

    public RequestContainer createOptionFilterLadenRequest() {
        return new OptionFilterLadenRequest(model.getInstanz().getId(), model.getOptionFilter(), model.getKriteriumLike());
    }

    public String getLetzteSucheNachKiterienMitLike() {
        return letzteSucheNachKiterienMitLike;
    }

    public OptionFilter getLetzteSucheNachKiterienMitOptionFiler() {
        return letzteSucheNachKiterienMitOptionFiler;
    }

    public LogEventRow getSystemLastLiveEventReceived() {
        return systemLastLiveEventReceived;
    }

    public boolean isSystemAnwendungGeladen() {
        return systemAnwendungGeladen;
    }

    public boolean isSystemHistoryGeladen() {
        return systemHistoryGeladen;
    }

    public boolean isSystemIstWaitingForAnswer() {
        return systemIstWaitingForAnswer;
    }

    public boolean istInstanzGewaehlt() {
        return model.getInstanz() != null;
    }

    public boolean istLadeKriterien() {
        return !systemIstWaitingForAnswer && istInstanzGewaehlt() && istOptionFilterGewaehlt();
    }

    public boolean istLiveNichtSuche() {
        return istLiveNichtSuche;
    }

    public boolean istOptionFilterGewaehlt() {
        return model.getOptionFilter() != null;
    }

    public boolean istSystemReady() {
        return !istLiveNichtSuche() && !isSystemIstWaitingForAnswer();
    }

    public boolean istValidHistory() {
        return istInstanzGewaehlt() && istOptionFilterGewaehlt() && model.getTabellenGroesse() != null && (!model.getOptionFilter().hatTabelle() || (model.getOptionFilter().hatTabelle() && model.getKriteriumId() != null && model.getKriteriumId().getId() != null));
    }

    public boolean istValidLive() {
        return istLiveNichtSuche() && istInstanzGewaehlt() && model.getTabellenGroesse() != null;
    }

    public void setIstLiveNichtSuche(boolean systemLiveUpdateAktiv) {
        istLiveNichtSuche = systemLiveUpdateAktiv;
    }

    public void setLetzteSucheNachKiterienMitLike(String letzteSucheNachKiterienMitLike) {
        this.letzteSucheNachKiterienMitLike = letzteSucheNachKiterienMitLike;
    }

    public void setLetzteSucheNachKiterienMitOptionFiler(OptionFilter letzteSucheNachKiterienMitOptionFiler) {
        this.letzteSucheNachKiterienMitOptionFiler = letzteSucheNachKiterienMitOptionFiler;
    }

    public void setSystemAnwendungGeladen(boolean systemAnwendungGeladen) {
        this.systemAnwendungGeladen = systemAnwendungGeladen;
    }

    public void setSystemIstWaitingForAnswer(boolean systemIstWaitingForAnswer) {
        this.systemIstWaitingForAnswer = systemIstWaitingForAnswer;
    }

    public void setSystemLastLiveEventReceived(LogEventRow systemLastLiveEventReceived) {
        this.systemLastLiveEventReceived = systemLastLiveEventReceived;
    }

    public void updateResultHistorie(List<LogEventRow> result) {
        if (result != null && result.size() > 0) {
            systemHistoryFirst = result.get(0);
            systemHistoryLast = result.get(result.size() - 1);
            systemHistoryGeladen = true;
        } else {
            systemHistoryGeladen = false;
        }
    }
}
