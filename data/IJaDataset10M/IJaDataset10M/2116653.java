package interfaces.hud.pilot.status;

import org.fenggui.StandardWidget;
import org.fenggui.util.Color;
import interfaces.PropertyContent;
import interfaces.hud.BasicHUD;
import interfaces.hud.spectate.SpectateChangeEvent;
import interfaces.hud.spectate.SpectateListener;
import interfaces.hud.spectate.SpectateWidget;
import interfaces.superWidgets.StaticContent;
import logic.common.player.Player;
import logic.common.player.playerListeners.NearStationChangedEvent;
import logic.common.player.playerListeners.PlayerStationListener;
import logic.common.player.playerListeners.SelectedSpawnChangedEvent;
import map.spawnStation.SpawnPoint;
import map.spawnStation.SpawnPosition;
import map.spawnStation.SpawnStation;
import map.spawnStation.stationListeners.SpawnStationPointsListener;
import map.spawnStation.stationListeners.StationPointsChangedEvent;

public class SpawnStationStatusHUD extends StaticContent implements SpectateWidget {

    private final Color ENEMY_COLOR = Color.RED;

    private final Color FRIENDLY_COLOR = new Color(0.15f, 0.9f, 0.15f);

    private PropertyContent captureContent;

    private Player player;

    private SpawnStation currentStation;

    private BasicHUD hud;

    private SpawnStationPointsListener stationListener;

    private PlayerStationListener playerStationListener;

    public SpawnStationStatusHUD(BasicHUD hud) {
        super(0, 0, 0, 0);
        this.hud = hud;
        StandardWidget topHUD = hud.getTopHUDElement();
        int border = hud.getHeight() / 30;
        int width = hud.getWidth() / 3;
        int height = border;
        setSize(width, height);
        setXY((hud.getWidth() / 2) - (getWidth() / 2), topHUD.getY() - border - getHeight());
        init();
        layout();
        initListeners();
        setPlayer(hud.getIngameState().getPlayer());
    }

    private void setPlayer(Player newPlayer) {
        if (player != newPlayer) {
            if (currentStation != null) currentStation.removeSpawnStationPointsListener(stationListener);
            if (player != null) player.removePlayerStationListener(playerStationListener);
            player = newPlayer;
            hud.addListener(player, playerStationListener);
            SpawnPosition nearPos = player.getNearSpawnPosition();
            if (nearPos != null && nearPos instanceof SpawnStation) {
                currentStation = (SpawnStation) nearPos;
                currentStation.addSpawnStationPointsListener(stationListener);
                hud.addWidget(this);
            } else hud.removeWidget(this);
        }
    }

    private void initListeners() {
        stationListener = new SpawnStationPointsListener() {

            @Override
            public void pointsChanged(StationPointsChangedEvent event) {
                if (currentStation != null) updateStation();
            }
        };
        playerStationListener = new PlayerStationListener() {

            @Override
            public void nearSpawnStationChanged(NearStationChangedEvent event) {
                SpawnPosition spawn = event.getSpawnPos();
                if (spawn != null && spawn instanceof SpawnStation) {
                    SpawnStation station = (SpawnStation) spawn;
                    hud.addWidget(SpawnStationStatusHUD.this);
                    hud.addListener(station, stationListener);
                    currentStation = station;
                } else {
                    hud.removeWidget(SpawnStationStatusHUD.this);
                    if (currentStation != null) currentStation.removeSpawnStationPointsListener(stationListener);
                    currentStation = null;
                }
            }

            @Override
            public void selectedSpawnPositionChanged(SelectedSpawnChangedEvent event) {
            }
        };
    }

    private void init() {
        int widgetWidth = getWidth();
        int widgetHeight = getHeight();
        Color bgColor = Color.DARK_BLUE;
        captureContent = new PropertyContent(widgetWidth, widgetHeight, 0, 0);
        captureContent.changeBackgroundColor(bgColor);
        addWidget(captureContent);
    }

    private void updateStation() {
        int maxValue = ((SpawnPoint) currentStation.getSpawnLocation()).getMaxCapturePoints();
        captureContent.setMaxValue(maxValue);
        captureContent.setValue(currentStation.getCapturePoints());
        boolean isFriendly = currentStation.getCurrentTeam() == player.getTeam();
        captureContent.changeValueColor(isFriendly ? FRIENDLY_COLOR : ENEMY_COLOR);
    }

    @Override
    public SpectateListener getSpectateListener() {
        return new SpectateListener() {

            @Override
            public void spectating(SpectateChangeEvent event) {
                setPlayer(event.getWatchedPlayer());
            }
        };
    }
}
