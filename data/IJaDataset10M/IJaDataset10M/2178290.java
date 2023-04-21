package com.leaguefox.web.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.leaguefox.web.client.LeagueFoxServiceAsync;
import com.leaguefox.web.client.common.SelectionModel;
import com.leaguefox.web.client.event.AddPlayerEvent;
import com.leaguefox.web.client.event.EditPlayerEvent;
import com.leaguefox.web.client.view.PlayersView;
import com.leaguefox.web.shared.domain.dto.PlayerDto;
import java.util.ArrayList;
import java.util.List;

public class PlayersPresenter implements Presenter, PlayersView.Presenter<PlayerDto> {

    private List<PlayerDto> players;

    private final LeagueFoxServiceAsync rpcService;

    private final HandlerManager eventBus;

    private final PlayersView<PlayerDto> view;

    private final SelectionModel<PlayerDto> selectionModel;

    public PlayersPresenter(LeagueFoxServiceAsync rpcService, HandlerManager eventBus, PlayersView<PlayerDto> view) {
        this.rpcService = rpcService;
        this.eventBus = eventBus;
        this.view = view;
        this.selectionModel = new SelectionModel<PlayerDto>();
        this.view.setPresenter(this);
    }

    public void onAddButtonClicked() {
        eventBus.fireEvent(new AddPlayerEvent());
    }

    public void onDeleteButtonClicked() {
        deleteSelectedPlayers();
    }

    public void onItemClicked(PlayerDto player) {
        eventBus.fireEvent(new EditPlayerEvent(player.getId().toString()));
    }

    public void onItemSelected(PlayerDto PlayerDetails) {
        if (selectionModel.isSelected(PlayerDetails)) {
            selectionModel.removeSelection(PlayerDetails);
        } else {
            selectionModel.addSelection(PlayerDetails);
        }
    }

    public void go(final HasWidgets container) {
        container.clear();
        container.add(view.asWidget());
        fetchPlayerDetails();
    }

    public void sortPlayerDetails() {
        for (int i = 0; i < players.size(); ++i) {
            for (int j = 0; j < players.size() - 1; ++j) {
                if (players.get(j).getSurname().compareToIgnoreCase(players.get(j + 1).getSurname()) >= 0) {
                    PlayerDto tmp = players.get(j);
                    players.set(j, players.get(j + 1));
                    players.set(j + 1, tmp);
                }
            }
        }
    }

    public void setPlayerDetails(List<PlayerDto> PlayerDetails) {
        this.players = PlayerDetails;
    }

    public List<PlayerDto> getPlayerDetails() {
        return players;
    }

    public PlayerDto getPlayerDetail(int index) {
        return players.get(index);
    }

    private void fetchPlayerDetails() {
        rpcService.getAllPlayers(new AsyncCallback<ArrayList<PlayerDto>>() {

            public void onSuccess(ArrayList<PlayerDto> result) {
                players = result;
                view.setRowData(players);
            }

            public void onFailure(Throwable caught) {
                Window.alert("Error fetching Player details");
            }
        });
    }

    private void deleteSelectedPlayers() {
        List<PlayerDto> selectedPlayers = selectionModel.getSelectedItems();
        ArrayList<Long> ids = new ArrayList<Long>();
        for (int i = 0; i < selectedPlayers.size(); ++i) {
            ids.add(selectedPlayers.get(i).getId());
        }
        rpcService.deletePlayers(ids, new AsyncCallback<ArrayList<PlayerDto>>() {

            public void onSuccess(ArrayList<PlayerDto> result) {
                players = result;
                sortPlayerDetails();
                view.setRowData(players);
            }

            public void onFailure(Throwable caught) {
                System.out.println("Error deleting selected Players");
            }
        });
    }
}
