package interfaces.spawnMenu.costumize;

import interfaces.GUISource;
import interfaces.spawnMenu.SpawnMenu;
import interfaces.spawnMenu.SpawnMenuWindow;
import interfaces.spawnMenu.costumize.hunters.HunterSelectContent;
import interfaces.spawnMenu.costumize.weapons.WeaponSelectContent;
import java.util.LinkedList;
import java.util.List;
import logic.Team;
import logic.nodes.nodeSettings.UpgradableSettings;
import logic.ships.hunter.Hunter;
import main.InitGame;
import fileHandling.language.LanguageLoader;
import fileHandling.language.interfaces.SpawnMenuText;
import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.StaticLayout;

public class CostumizeWindow extends SpawnMenuWindow {

    private WeaponSelectContent weaponSelectContentTeam, weaponSelectContentEnemyTeam, currentWeaponSelectContent;

    private HunterSelectContent hunterSelectContentTeam, hunterSelectContentEnemyTeam, currentHunterSelectContent;

    private CostumizeContainer<? extends UpgradableSettings> currentContent;

    private List<CostumizeContainer<? extends UpgradableSettings>> teamContainer;

    private Container northContent;

    public CostumizeWindow(Display display, Team team, Team enemyTeam, SpawnMenu spawnMenu) {
        super(display, LanguageLoader.get(SpawnMenuText.Costumize), team, enemyTeam, spawnMenu);
        content.setLayoutManager(new StaticLayout());
        makeNorthPanel();
    }

    public void init() {
        teamContainer = new LinkedList<CostumizeContainer<? extends UpgradableSettings>>();
        currentWeaponSelectContent = weaponSelectContentTeam;
        currentHunterSelectContent = hunterSelectContentTeam;
        teamContainer.add(currentWeaponSelectContent);
        teamContainer.add(currentHunterSelectContent);
        currentContent = currentHunterSelectContent;
        content.addWidget(currentContent);
    }

    public void initHunterSelectTeam1() {
        hunterSelectContentTeam = initHunterSelect(team);
    }

    public void initWraithSelectTeam2() {
        hunterSelectContentEnemyTeam = initHunterSelect(enemyTeam);
    }

    private HunterSelectContent initHunterSelect(Team team) {
        int contentWidth = content.getWidth();
        int contentHeight = content.getHeight() - northContent.getHeight();
        return new HunterSelectContent(team, contentWidth, contentHeight, this);
    }

    public void initEnergyWeaponsTeam1() {
        weaponSelectContentTeam = initWeaponSelect(team);
        weaponSelectContentTeam.initEnergyWeapons();
    }

    public void initEnergyWeaponsTeam2() {
        weaponSelectContentEnemyTeam = initWeaponSelect(enemyTeam);
        weaponSelectContentEnemyTeam.initEnergyWeapons();
    }

    private WeaponSelectContent initWeaponSelect(Team team) {
        int contentWidth = content.getWidth();
        int contentHeight = content.getHeight() - northContent.getHeight();
        return new WeaponSelectContent(team, contentWidth, contentHeight);
    }

    public void initProjectileWeaponsTeam1() {
        weaponSelectContentTeam.initProjectileWeapons();
    }

    public void initProjectileWeaponsTeam2() {
        weaponSelectContentEnemyTeam.initProjectileWeapons();
    }

    private void makeNorthPanel() {
        northContent = new Container(new StaticLayout());
        northContent.setSize(content.getWidth(), content.getHeight() / 20);
        northContent.setXY(0, content.getHeight() - northContent.getHeight());
        content.addWidget(northContent);
        int buttonWidth = northContent.getWidth() / 2;
        int buttonHeight = northContent.getHeight();
        Button weaponButton = new Button(LanguageLoader.get(SpawnMenuText.Select_your_Weapons));
        weaponButton.getAppearance().setFont(GUISource.labelFont);
        weaponButton.setSize(buttonWidth, buttonHeight);
        weaponButton.setXY(0, 0);
        weaponButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                setCurrentContent(currentWeaponSelectContent);
            }
        });
        northContent.addWidget(weaponButton);
        Button wraithButton = new Button(LanguageLoader.get(SpawnMenuText.Select_your_Hunter));
        wraithButton.getAppearance().setFont(GUISource.labelFont);
        wraithButton.setSize(buttonWidth, buttonHeight);
        wraithButton.setXY(buttonWidth, 0);
        wraithButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                setCurrentContent(currentHunterSelectContent);
            }
        });
        northContent.addWidget(wraithButton);
    }

    private void setCurrentContent(CostumizeContainer<? extends UpgradableSettings> newContent) {
        content.removeWidget(currentContent);
        currentContent = newContent;
        content.addWidget(currentContent);
    }

    @Override
    public void changeTeam(Team newTeam) {
        enemyTeam = team;
        team = newTeam;
        if (currentWeaponSelectContent == weaponSelectContentTeam) currentWeaponSelectContent = weaponSelectContentEnemyTeam; else currentWeaponSelectContent = weaponSelectContentTeam;
        if (currentHunterSelectContent == hunterSelectContentTeam) currentHunterSelectContent = hunterSelectContentEnemyTeam; else currentHunterSelectContent = hunterSelectContentTeam;
        teamContainer = new LinkedList<CostumizeContainer<? extends UpgradableSettings>>();
        teamContainer.add(currentWeaponSelectContent);
        teamContainer.add(currentHunterSelectContent);
        setCurrentContent(currentHunterSelectContent);
    }

    @Override
    public void changeHunter(Hunter hunter) {
        this.hunter = hunter;
        hunter.setPilot(InitGame.getMatchState().getCurrentPlayer());
        for (CostumizeContainer<? extends UpgradableSettings> teamCont : teamContainer) {
            if (teamCont != null) teamCont.changeHunter(hunter);
        }
        spawnMenu.changeSelectedHunter(hunter);
    }

    public Hunter getSelectedHunter() {
        return hunterSelectContentTeam.getHunter();
    }
}
