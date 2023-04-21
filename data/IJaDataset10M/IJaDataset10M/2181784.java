package interfaces.spawnMenu.costumize.hunters;

import interfaces.GUISource;
import interfaces.ImageUpdater;
import interfaces.StaticContent;
import interfaces.spawnMenu.MoneyListener;
import interfaces.spawnMenu.costumize.CostumizeContainer;
import interfaces.spawnMenu.costumize.CostumizeWindow;
import logic.Team;
import logic.ships.hunter.Hunter;
import logic.ships.moveableShip.MovableShipProperties;
import main.InitGame;
import org.fenggui.Button;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import factories.HunterFactory;
import fileHandling.language.LanguageLoader;
import fileHandling.language.interfaces.SpawnMenuText;

public class HunterSelectContent extends CostumizeContainer<MovableShipProperties> implements MoneyListener {

    private CostumizeWindow costumWindow;

    private String selectedWraith, boughtWraith;

    private Button buyButton;

    private StaticContent wraithSelectContent;

    private ImageUpdater wraithPreview;

    private int previewSize;

    private HunterInfoContent infoContent;

    public HunterSelectContent(Team team, int width, int height, CostumizeWindow costumWindow) {
        super(team, width, height, "ships/maxWraithValues.txt");
        this.costumWindow = costumWindow;
        LanguageLoader.loadLanguageFile(LanguageLoader.SHIP_PROPERTIES_FILE);
        createBuyButton();
        makeWraithSelection();
    }

    private void createBuyButton() {
        buyButton = new Button(LanguageLoader.get(SpawnMenuText.Buy_Hunter));
        buyButton.getAppearance().setFont(GUISource.labelFont);
        buyButton.addButtonPressedListener(new IButtonPressedListener() {

            public void buttonPressed(ButtonPressedEvent e) {
                InitGame.getMatchState().getCurrentPlayer().becomePilot();
                boughtWraith = selectedWraith;
                hunter = HunterFactory.createNewHunter(boughtWraith, team);
                costumWindow.changeHunter(hunter);
                setUpgradeContent();
                hunter.setWraithProperties(infoContent.getProperties());
                buyButton.setEnabled(false);
            }
        });
        buyButton.setSize(width / 3, height / 15);
        buyButton.setXY(width - buyButton.getWidth(), 0);
    }

    private void makeWraithSelection() {
        wraithSelectContent = new StaticContent(width, height / 15, 0, height - height / 15);
        border = width / 30;
        previewSize = width / 3;
        infoX = border;
        infoY = buyButton.getHeight();
        infoWidth = width - infoX * 2;
        infoHeight = height - wraithSelectContent.getHeight() - previewSize - border * 4;
        int index = 0;
        int wraithButtonWidth = width / team.getAvailableHunters().size();
        for (String wraithName : team.getAvailableHunters()) {
            Button wraithButton = new Button(wraithName);
            wraithButton.getAppearance().setFont(GUISource.labelFont);
            wraithButton.addButtonPressedListener(new IButtonPressedListener() {

                public void buttonPressed(ButtonPressedEvent e) {
                    String wraithName = ((Button) e.getSource()).getText().toLowerCase();
                    String fraction = team.getFraction().toString().toLowerCase();
                    String wraithPath = "data/fractions/" + fraction + "/ships/" + wraithName + "/";
                    if (wraithName.equals(selectedWraith)) return;
                    changePreview(wraithPath);
                    if (!getContent().contains(buyButton)) addWidget(buyButton);
                    selectedWraith = wraithName;
                    setInfoContent(wraithName, false);
                    if (selectedWraith.equals(boughtWraith)) {
                        setUpgradeContent();
                        buyButton.setEnabled(false);
                    } else buyButton.setEnabled(true);
                    layout();
                }
            });
            wraithButton.setSize(wraithButtonWidth, wraithSelectContent.getHeight());
            wraithButton.setXY(index * wraithButtonWidth, 0);
            wraithSelectContent.addWidget(wraithButton);
            createWraithContents(wraithName);
            index++;
        }
        addWidget(wraithSelectContent);
        MovableShipProperties props = selectableSettings.get(team.getAvailableHunters().get(0));
        infoContent = new HunterInfoContent(infoWidth, infoHeight, infoX, infoY, props, maxValues);
    }

    private void createWraithContents(String wraithName) {
        String fraction = team.getFraction().toString().toLowerCase();
        String wraithPath = "data/fractions/" + fraction + "/ships/" + wraithName + "/";
        MovableShipProperties props = HunterFactory.getHunterProperties(wraithPath);
        if (props != null) selectableSettings.put(wraithName, props);
    }

    private void setInfoContent(String wraithName, boolean initUpgrades) {
        if (!getContent().contains(infoContent)) addWidget(infoContent);
        infoContent.setProperties(selectableSettings.get(wraithName));
        infoContent.initUpgrades(initUpgrades, false);
    }

    private void setUpgradeContent() {
        setInfoContent(boughtWraith, true);
    }

    private void changePreview(String wraithPath) {
        wraithPath += "wraithIcon.png";
        int prevX = border;
        int prevY = height - wraithSelectContent.getHeight() - previewSize - border;
        if (wraithPreview != null && getContent().contains(wraithPreview)) removeWidget(wraithPreview);
        wraithPreview = new ImageUpdater(wraithPath, prevX, prevY, previewSize, previewSize);
        if (wraithPreview != null) addWidget(wraithPreview);
    }

    @Override
    public void changeHunter(Hunter wraith) {
    }

    @Override
    public void updateMoney(int newMoney) {
        infoContent.updateMoney(newMoney);
    }
}
