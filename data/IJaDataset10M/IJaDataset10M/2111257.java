package spacetrader.gui;

import java.awt.Color;
import java.awt.Point;
import jwinforms.Button;
import jwinforms.Container;
import jwinforms.EventArgs;
import jwinforms.EventHandler;
import jwinforms.Font;
import jwinforms.FormSize;
import jwinforms.GraphicsUnit;
import jwinforms.IContainer;
import jwinforms.ImageList;
import jwinforms.ImageListStreamer;
import jwinforms.Label;
import jwinforms.PaintEventArgs;
import jwinforms.PictureBox;
import jwinforms.ResourceManager;
import jwinforms.SystemColors;
import jwinforms.Timer;
import jwinforms.WinformForm;
import jwinforms.enums.BorderStyle;
import jwinforms.enums.FlatStyle;
import jwinforms.enums.FontStyle;
import jwinforms.enums.FormBorderStyle;
import jwinforms.enums.FormStartPosition;
import org.gts.bst.events.EncounterResult;
import spacetrader.Commander;
import spacetrader.Consts;
import spacetrader.Functions;
import spacetrader.Game;
import spacetrader.Ship;
import spacetrader.enums.AlertType;

public class FormEncounter extends WinformForm {

    private Button btnAttack;

    private Button btnFlee;

    private Button btnSubmit;

    private Button btnBribe;

    private Button btnSurrender;

    private Button btnIgnore;

    private Button btnTrade;

    private Button btnPlunder;

    private Button btnBoard;

    private Button btnMeet;

    private Button btnDrink;

    private Button btnInt;

    private Button btnYield;

    private Button[] buttons;

    private ImageList ilContinuous;

    private ImageList ilEncounterType;

    private ImageList ilTribbles;

    private Label lblEncounter;

    private Label lblOpponentLabel;

    private Label lblYouLabel;

    private Label lblOpponentShip;

    private Label lblYouShip;

    private Label lblYouHull;

    private Label lblYouShields;

    private Label lblOpponentShields;

    private Label lblOpponentHull;

    private Label lblAction;

    private PictureBox picShipYou;

    private PictureBox picShipOpponent;

    private PictureBox picContinuous;

    private PictureBox picEncounterType;

    private PictureBox picTrib00;

    private PictureBox picTrib50;

    private PictureBox picTrib10;

    private PictureBox picTrib40;

    private PictureBox picTrib20;

    private PictureBox picTrib30;

    private PictureBox picTrib04;

    private PictureBox picTrib03;

    private PictureBox picTrib02;

    private PictureBox picTrib01;

    private PictureBox picTrib05;

    private PictureBox picTrib11;

    private PictureBox picTrib12;

    private PictureBox picTrib13;

    private PictureBox picTrib14;

    private PictureBox picTrib15;

    private PictureBox picTrib21;

    private PictureBox picTrib22;

    private PictureBox picTrib23;

    private PictureBox picTrib24;

    private PictureBox picTrib25;

    private PictureBox picTrib31;

    private PictureBox picTrib32;

    private PictureBox picTrib33;

    private PictureBox picTrib34;

    private PictureBox picTrib35;

    private PictureBox picTrib41;

    private PictureBox picTrib51;

    private PictureBox picTrib42;

    private PictureBox picTrib52;

    private PictureBox picTrib43;

    private PictureBox picTrib53;

    private PictureBox picTrib44;

    private PictureBox picTrib45;

    private PictureBox picTrib54;

    private PictureBox picTrib55;

    private Timer tmrTick;

    private IContainer components;

    private final int ATTACK = 0;

    private final int BOARD = 1;

    private final int BRIBE = 2;

    private final int DRINK = 3;

    private final int FLEE = 4;

    private final int IGNORE = 5;

    private final int INT = 6;

    private final int MEET = 7;

    private final int PLUNDER = 8;

    private final int SUBMIT = 9;

    private final int SURRENDER = 10;

    private final int TRADE = 11;

    private final int YIELD = 12;

    private final Game game = Game.CurrentGame();

    private final Commander cmdr = game.Commander();

    private final Ship cmdrship = cmdr.getShip();

    private final Ship opponent = game.getOpponent();

    private int contImg = 1;

    private EncounterResult _result = EncounterResult.Continue;

    public FormEncounter() {
        InitializeComponent();
        game.EncounterBegin();
        if (game.getEasyEncounters()) {
            setControlBox(true);
        }
        buttons = new Button[] { btnAttack, btnBoard, btnBribe, btnDrink, btnFlee, btnIgnore, btnInt, btnMeet, btnPlunder, btnSubmit, btnSurrender, btnTrade, btnYield };
        UpdateShipInfo();
        UpdateTribbles();
        UpdateButtons();
        if (game.EncounterImageIndex() >= 0) {
            picEncounterType.setImage(ilEncounterType.getImages()[game.EncounterImageIndex()]);
        } else {
            picEncounterType.setVisible(false);
        }
        lblEncounter.setText(game.EncounterTextInitial());
        lblAction.setText(game.EncounterActionInitial());
    }

    private void InitializeComponent() {
        components = new Container();
        ResourceManager resources = new ResourceManager(FormEncounter.class);
        lblEncounter = new Label();
        picShipYou = new PictureBox();
        picShipOpponent = new PictureBox();
        lblAction = new Label();
        lblOpponentLabel = new Label();
        lblYouLabel = new Label();
        lblOpponentShip = new Label();
        lblYouShip = new Label();
        lblYouHull = new Label();
        lblYouShields = new Label();
        lblOpponentShields = new Label();
        lblOpponentHull = new Label();
        btnAttack = new Button();
        btnFlee = new Button();
        btnSubmit = new Button();
        btnBribe = new Button();
        btnSurrender = new Button();
        btnIgnore = new Button();
        btnTrade = new Button();
        btnPlunder = new Button();
        btnBoard = new Button();
        btnMeet = new Button();
        btnDrink = new Button();
        btnInt = new Button();
        btnYield = new Button();
        picContinuous = new PictureBox();
        ilContinuous = new ImageList(components);
        picEncounterType = new PictureBox();
        ilEncounterType = new ImageList(components);
        picTrib00 = new PictureBox();
        ilTribbles = new ImageList(components);
        picTrib50 = new PictureBox();
        picTrib10 = new PictureBox();
        picTrib40 = new PictureBox();
        picTrib20 = new PictureBox();
        picTrib30 = new PictureBox();
        picTrib04 = new PictureBox();
        picTrib03 = new PictureBox();
        picTrib02 = new PictureBox();
        picTrib01 = new PictureBox();
        picTrib05 = new PictureBox();
        picTrib11 = new PictureBox();
        picTrib12 = new PictureBox();
        picTrib13 = new PictureBox();
        picTrib14 = new PictureBox();
        picTrib15 = new PictureBox();
        picTrib21 = new PictureBox();
        picTrib22 = new PictureBox();
        picTrib23 = new PictureBox();
        picTrib24 = new PictureBox();
        picTrib25 = new PictureBox();
        picTrib31 = new PictureBox();
        picTrib32 = new PictureBox();
        picTrib33 = new PictureBox();
        picTrib34 = new PictureBox();
        picTrib35 = new PictureBox();
        picTrib41 = new PictureBox();
        picTrib51 = new PictureBox();
        picTrib42 = new PictureBox();
        picTrib52 = new PictureBox();
        picTrib43 = new PictureBox();
        picTrib53 = new PictureBox();
        picTrib44 = new PictureBox();
        picTrib45 = new PictureBox();
        picTrib54 = new PictureBox();
        picTrib55 = new PictureBox();
        tmrTick = new Timer(components);
        SuspendLayout();
        lblEncounter.setLocation(new Point(8, 152));
        lblEncounter.setName("lblEncounter");
        lblEncounter.setSize(new FormSize(232, 26));
        lblEncounter.setTabIndex(0);
        lblEncounter.setText("At 20 clicks from Tarchannen, you encounter the famous Captain Ahab.");
        picShipYou.setBackColor(Color.white);
        picShipYou.setBorderStyle(BorderStyle.FixedSingle);
        picShipYou.setLocation(new Point(26, 24));
        picShipYou.setName("picShipYou");
        picShipYou.setSize(new FormSize(70, 58));
        picShipYou.setTabIndex(13);
        picShipYou.setTabStop(false);
        picShipYou.setPaint(new EventHandler<Object, PaintEventArgs>() {

            @Override
            public void handle(Object sender, PaintEventArgs e) {
                picShipYou_Paint(sender, e);
            }
        });
        picShipOpponent.setBackColor(Color.white);
        picShipOpponent.setBorderStyle(BorderStyle.FixedSingle);
        picShipOpponent.setLocation(new Point(138, 24));
        picShipOpponent.setName("picShipOpponent");
        picShipOpponent.setSize(new FormSize(70, 58));
        picShipOpponent.setTabIndex(14);
        picShipOpponent.setTabStop(false);
        picShipOpponent.setPaint(new EventHandler<Object, PaintEventArgs>() {

            @Override
            public void handle(Object sender, PaintEventArgs e) {
                picShipOpponent_Paint(sender, e);
            }
        });
        lblAction.setLocation(new Point(8, 192));
        lblAction.setName("lblAction");
        lblAction.setSize(new FormSize(232, 39));
        lblAction.setTabIndex(15);
        lblAction.setText("\"We know you removed illegal goods from the Marie Celeste. You must give them up at once!\"");
        lblOpponentLabel.setAutoSize(true);
        lblOpponentLabel.setFont(new Font("Microsoft Sans Serif", 8.25F, FontStyle.Bold, GraphicsUnit.Point, ((byte) (0))));
        lblOpponentLabel.setLocation(new Point(141, 8));
        lblOpponentLabel.setName("lblOpponentLabel");
        lblOpponentLabel.setSize(new FormSize(59, 16));
        lblOpponentLabel.setTabIndex(16);
        lblOpponentLabel.setText("Opponent:");
        lblYouLabel.setAutoSize(true);
        lblYouLabel.setFont(new Font("Microsoft Sans Serif", 8.25F, FontStyle.Bold, GraphicsUnit.Point, ((byte) (0))));
        lblYouLabel.setLocation(new Point(45, 8));
        lblYouLabel.setName("lblYouLabel");
        lblYouLabel.setSize(new FormSize(28, 16));
        lblYouLabel.setTabIndex(17);
        lblYouLabel.setText("You:");
        lblOpponentShip.setLocation(new Point(138, 88));
        lblOpponentShip.setName("lblOpponentShip");
        lblOpponentShip.setSize(new FormSize(80, 13));
        lblOpponentShip.setTabIndex(18);
        lblOpponentShip.setText("Space Monster");
        lblYouShip.setLocation(new Point(26, 88));
        lblYouShip.setName("lblYouShip");
        lblYouShip.setSize(new FormSize(100, 13));
        lblYouShip.setTabIndex(19);
        lblYouShip.setText("Grasshopper");
        lblYouHull.setLocation(new Point(26, 104));
        lblYouHull.setName("lblYouHull");
        lblYouHull.setSize(new FormSize(68, 13));
        lblYouHull.setTabIndex(20);
        lblYouHull.setText("Hull at 100%");
        lblYouShields.setLocation(new Point(26, 120));
        lblYouShields.setName("lblYouShields");
        lblYouShields.setSize(new FormSize(86, 13));
        lblYouShields.setTabIndex(21);
        lblYouShields.setText("Shields at 100%");
        lblOpponentShields.setLocation(new Point(138, 120));
        lblOpponentShields.setName("lblOpponentShields");
        lblOpponentShields.setSize(new FormSize(86, 13));
        lblOpponentShields.setTabIndex(23);
        lblOpponentShields.setText("Shields at 100%");
        lblOpponentHull.setLocation(new Point(138, 104));
        lblOpponentHull.setName("lblOpponentHull");
        lblOpponentHull.setSize(new FormSize(68, 13));
        lblOpponentHull.setTabIndex(22);
        lblOpponentHull.setText("Hull at 100%");
        btnAttack.setFlatStyle(FlatStyle.Flat);
        btnAttack.setLocation(new Point(8, 240));
        btnAttack.setName("btnAttack");
        btnAttack.setSize(new FormSize(46, 22));
        btnAttack.setTabIndex(24);
        btnAttack.setText("Attack");
        btnAttack.setVisible(false);
        btnAttack.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnAttack_Click(sender, e);
            }
        });
        btnFlee.setFlatStyle(FlatStyle.Flat);
        btnFlee.setLocation(new Point(62, 240));
        btnFlee.setName("btnFlee");
        btnFlee.setSize(new FormSize(36, 22));
        btnFlee.setTabIndex(25);
        btnFlee.setText("Flee");
        btnFlee.setVisible(false);
        btnFlee.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnFlee_Click(sender, e);
            }
        });
        btnSubmit.setFlatStyle(FlatStyle.Flat);
        btnSubmit.setLocation(new Point(106, 240));
        btnSubmit.setName("btnSubmit");
        btnSubmit.setSize(new FormSize(49, 22));
        btnSubmit.setTabIndex(26);
        btnSubmit.setText("Submit");
        btnSubmit.setVisible(false);
        btnSubmit.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnSubmit_Click(sender, e);
            }
        });
        btnBribe.setFlatStyle(FlatStyle.Flat);
        btnBribe.setLocation(new Point(163, 240));
        btnBribe.setName("btnBribe");
        btnBribe.setSize(new FormSize(41, 22));
        btnBribe.setTabIndex(27);
        btnBribe.setText("Bribe");
        btnBribe.setVisible(false);
        btnBribe.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnBribe_Click(sender, e);
            }
        });
        btnSurrender.setFlatStyle(FlatStyle.Flat);
        btnSurrender.setLocation(new Point(106, 240));
        btnSurrender.setName("btnSurrender");
        btnSurrender.setSize(new FormSize(65, 22));
        btnSurrender.setTabIndex(28);
        btnSurrender.setText("Surrender");
        btnSurrender.setVisible(false);
        btnSurrender.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnSurrender_Click(sender, e);
            }
        });
        btnIgnore.setFlatStyle(FlatStyle.Flat);
        btnIgnore.setLocation(new Point(62, 240));
        btnIgnore.setName("btnIgnore");
        btnIgnore.setSize(new FormSize(46, 22));
        btnIgnore.setTabIndex(29);
        btnIgnore.setText("Ignore");
        btnIgnore.setVisible(false);
        btnIgnore.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnIgnore_Click(sender, e);
            }
        });
        btnTrade.setFlatStyle(FlatStyle.Flat);
        btnTrade.setLocation(new Point(116, 240));
        btnTrade.setName("btnTrade");
        btnTrade.setSize(new FormSize(44, 22));
        btnTrade.setTabIndex(30);
        btnTrade.setText("Trade");
        btnTrade.setVisible(false);
        btnTrade.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnTrade_Click(sender, e);
            }
        });
        btnPlunder.setFlatStyle(FlatStyle.Flat);
        btnPlunder.setLocation(new Point(62, 240));
        btnPlunder.setName("btnPlunder");
        btnPlunder.setSize(new FormSize(53, 22));
        btnPlunder.setTabIndex(31);
        btnPlunder.setText("Plunder");
        btnPlunder.setVisible(false);
        btnPlunder.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnPlunder_Click(sender, e);
            }
        });
        btnBoard.setFlatStyle(FlatStyle.Flat);
        btnBoard.setLocation(new Point(8, 240));
        btnBoard.setName("btnBoard");
        btnBoard.setSize(new FormSize(44, 22));
        btnBoard.setTabIndex(32);
        btnBoard.setText("Board");
        btnBoard.setVisible(false);
        btnBoard.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnBoard_Click(sender, e);
            }
        });
        btnMeet.setFlatStyle(FlatStyle.Flat);
        btnMeet.setLocation(new Point(116, 240));
        btnMeet.setName("btnMeet");
        btnMeet.setSize(new FormSize(39, 22));
        btnMeet.setTabIndex(34);
        btnMeet.setText("Meet");
        btnMeet.setVisible(false);
        btnMeet.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnMeet_Click(sender, e);
            }
        });
        btnDrink.setFlatStyle(FlatStyle.Flat);
        btnDrink.setLocation(new Point(8, 240));
        btnDrink.setName("btnDrink");
        btnDrink.setSize(new FormSize(41, 22));
        btnDrink.setTabIndex(35);
        btnDrink.setText("Drink");
        btnDrink.setVisible(false);
        btnDrink.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnDrink_Click(sender, e);
            }
        });
        btnInt.setFlatStyle(FlatStyle.Flat);
        btnInt.setLocation(new Point(179, 240));
        btnInt.setName("btnInt");
        btnInt.setSize(new FormSize(30, 22));
        btnInt.setTabIndex(36);
        btnInt.setText("Int.");
        btnInt.setVisible(false);
        btnInt.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnInt_Click(sender, e);
            }
        });
        btnYield.setFlatStyle(FlatStyle.Flat);
        btnYield.setLocation(new Point(106, 240));
        btnYield.setName("btnYield");
        btnYield.setSize(new FormSize(39, 22));
        btnYield.setTabIndex(37);
        btnYield.setText("Yield");
        btnYield.setVisible(false);
        btnYield.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnYield_Click(sender, e);
            }
        });
        picContinuous.setLocation(new Point(214, 247));
        picContinuous.setName("picContinuous");
        picContinuous.setSize(new FormSize(9, 9));
        picContinuous.setTabIndex(38);
        picContinuous.setTabStop(false);
        picContinuous.setVisible(false);
        ilContinuous.setImageSize(new FormSize(9, 9));
        ilContinuous.setImageStream(((ImageListStreamer) (resources.GetObject("ilContinuous.ImageStream"))));
        ilContinuous.setTransparentColor(Color.white);
        picEncounterType.setLocation(new Point(220, 2));
        picEncounterType.setName("picEncounterType");
        picEncounterType.setSize(new FormSize(12, 12));
        picEncounterType.setTabIndex(39);
        picEncounterType.setTabStop(false);
        ilEncounterType.setImageSize(new FormSize(12, 12));
        ilEncounterType.setImageStream(((ImageListStreamer) (resources.GetObject("ilEncounterType.ImageStream"))));
        ilEncounterType.setTransparentColor(Color.white);
        picTrib00.setBackColor(SystemColors.Control);
        picTrib00.setLocation(new Point(16, 16));
        picTrib00.setName("picTrib00");
        picTrib00.setSize(new FormSize(12, 12));
        picTrib00.setTabIndex(41);
        picTrib00.setTabStop(false);
        picTrib00.setVisible(false);
        picTrib00.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        ilTribbles.setImageSize(new FormSize(12, 12));
        ilTribbles.setImageStream(((ImageListStreamer) (resources.GetObject("ilTribbles.ImageStream"))));
        ilTribbles.setTransparentColor(Color.white);
        picTrib50.setBackColor(SystemColors.Control);
        picTrib50.setLocation(new Point(16, 224));
        picTrib50.setName("picTrib50");
        picTrib50.setSize(new FormSize(12, 12));
        picTrib50.setTabIndex(42);
        picTrib50.setTabStop(false);
        picTrib50.setVisible(false);
        picTrib50.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib10.setBackColor(SystemColors.Control);
        picTrib10.setLocation(new Point(8, 56));
        picTrib10.setName("picTrib10");
        picTrib10.setSize(new FormSize(12, 12));
        picTrib10.setTabIndex(43);
        picTrib10.setTabStop(false);
        picTrib10.setVisible(false);
        picTrib10.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib40.setBackColor(SystemColors.Control);
        picTrib40.setLocation(new Point(8, 184));
        picTrib40.setName("picTrib40");
        picTrib40.setSize(new FormSize(12, 12));
        picTrib40.setTabIndex(44);
        picTrib40.setTabStop(false);
        picTrib40.setVisible(false);
        picTrib40.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib20.setBackColor(SystemColors.Control);
        picTrib20.setLocation(new Point(8, 96));
        picTrib20.setName("picTrib20");
        picTrib20.setSize(new FormSize(12, 12));
        picTrib20.setTabIndex(45);
        picTrib20.setTabStop(false);
        picTrib20.setVisible(false);
        picTrib20.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib30.setBackColor(SystemColors.Control);
        picTrib30.setLocation(new Point(16, 136));
        picTrib30.setName("picTrib30");
        picTrib30.setSize(new FormSize(12, 12));
        picTrib30.setTabIndex(46);
        picTrib30.setTabStop(false);
        picTrib30.setVisible(false);
        picTrib30.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib04.setBackColor(SystemColors.Control);
        picTrib04.setLocation(new Point(176, 8));
        picTrib04.setName("picTrib04");
        picTrib04.setSize(new FormSize(12, 12));
        picTrib04.setTabIndex(47);
        picTrib04.setTabStop(false);
        picTrib04.setVisible(false);
        picTrib04.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib03.setBackColor(SystemColors.Control);
        picTrib03.setLocation(new Point(128, 8));
        picTrib03.setName("picTrib03");
        picTrib03.setSize(new FormSize(12, 12));
        picTrib03.setTabIndex(48);
        picTrib03.setTabStop(false);
        picTrib03.setVisible(false);
        picTrib03.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib02.setBackColor(SystemColors.Control);
        picTrib02.setLocation(new Point(96, 16));
        picTrib02.setName("picTrib02");
        picTrib02.setSize(new FormSize(12, 12));
        picTrib02.setTabIndex(49);
        picTrib02.setTabStop(false);
        picTrib02.setVisible(false);
        picTrib02.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib01.setBackColor(SystemColors.Control);
        picTrib01.setLocation(new Point(56, 8));
        picTrib01.setName("picTrib01");
        picTrib01.setSize(new FormSize(12, 12));
        picTrib01.setTabIndex(50);
        picTrib01.setTabStop(false);
        picTrib01.setVisible(false);
        picTrib01.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib05.setBackColor(SystemColors.Control);
        picTrib05.setLocation(new Point(208, 16));
        picTrib05.setName("picTrib05");
        picTrib05.setSize(new FormSize(12, 12));
        picTrib05.setTabIndex(51);
        picTrib05.setTabStop(false);
        picTrib05.setVisible(false);
        picTrib05.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib11.setBackColor(SystemColors.Control);
        picTrib11.setLocation(new Point(32, 80));
        picTrib11.setName("picTrib11");
        picTrib11.setSize(new FormSize(12, 12));
        picTrib11.setTabIndex(52);
        picTrib11.setTabStop(false);
        picTrib11.setVisible(false);
        picTrib11.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib12.setBackColor(SystemColors.Control);
        picTrib12.setLocation(new Point(88, 56));
        picTrib12.setName("picTrib12");
        picTrib12.setSize(new FormSize(12, 12));
        picTrib12.setTabIndex(53);
        picTrib12.setTabStop(false);
        picTrib12.setVisible(false);
        picTrib12.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib13.setBackColor(SystemColors.Control);
        picTrib13.setLocation(new Point(128, 40));
        picTrib13.setName("picTrib13");
        picTrib13.setSize(new FormSize(12, 12));
        picTrib13.setTabIndex(54);
        picTrib13.setTabStop(false);
        picTrib13.setVisible(false);
        picTrib13.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib14.setBackColor(SystemColors.Control);
        picTrib14.setLocation(new Point(192, 72));
        picTrib14.setName("picTrib14");
        picTrib14.setSize(new FormSize(12, 12));
        picTrib14.setTabIndex(55);
        picTrib14.setTabStop(false);
        picTrib14.setVisible(false);
        picTrib14.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib15.setBackColor(SystemColors.Control);
        picTrib15.setLocation(new Point(216, 48));
        picTrib15.setName("picTrib15");
        picTrib15.setSize(new FormSize(12, 12));
        picTrib15.setTabIndex(56);
        picTrib15.setTabStop(false);
        picTrib15.setVisible(false);
        picTrib15.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib21.setBackColor(SystemColors.Control);
        picTrib21.setLocation(new Point(56, 96));
        picTrib21.setName("picTrib21");
        picTrib21.setSize(new FormSize(12, 12));
        picTrib21.setTabIndex(57);
        picTrib21.setTabStop(false);
        picTrib21.setVisible(false);
        picTrib21.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib22.setBackColor(SystemColors.Control);
        picTrib22.setLocation(new Point(96, 80));
        picTrib22.setName("picTrib22");
        picTrib22.setSize(new FormSize(12, 12));
        picTrib22.setTabIndex(58);
        picTrib22.setTabStop(false);
        picTrib22.setVisible(false);
        picTrib22.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib23.setBackColor(SystemColors.Control);
        picTrib23.setLocation(new Point(136, 88));
        picTrib23.setName("picTrib23");
        picTrib23.setSize(new FormSize(12, 12));
        picTrib23.setTabIndex(59);
        picTrib23.setTabStop(false);
        picTrib23.setVisible(false);
        picTrib23.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib24.setBackColor(SystemColors.Control);
        picTrib24.setLocation(new Point(176, 104));
        picTrib24.setName("picTrib24");
        picTrib24.setSize(new FormSize(12, 12));
        picTrib24.setTabIndex(60);
        picTrib24.setTabStop(false);
        picTrib24.setVisible(false);
        picTrib24.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib25.setBackColor(SystemColors.Control);
        picTrib25.setLocation(new Point(216, 96));
        picTrib25.setName("picTrib25");
        picTrib25.setSize(new FormSize(12, 12));
        picTrib25.setTabIndex(61);
        picTrib25.setTabStop(false);
        picTrib25.setVisible(false);
        picTrib25.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib31.setBackColor(SystemColors.Control);
        picTrib31.setLocation(new Point(56, 128));
        picTrib31.setName("picTrib31");
        picTrib31.setSize(new FormSize(12, 12));
        picTrib31.setTabIndex(62);
        picTrib31.setTabStop(false);
        picTrib31.setVisible(false);
        picTrib31.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib32.setBackColor(SystemColors.Control);
        picTrib32.setLocation(new Point(96, 120));
        picTrib32.setName("picTrib32");
        picTrib32.setSize(new FormSize(12, 12));
        picTrib32.setTabIndex(63);
        picTrib32.setTabStop(false);
        picTrib32.setVisible(false);
        picTrib32.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib33.setBackColor(SystemColors.Control);
        picTrib33.setLocation(new Point(128, 128));
        picTrib33.setName("picTrib33");
        picTrib33.setSize(new FormSize(12, 12));
        picTrib33.setTabIndex(64);
        picTrib33.setTabStop(false);
        picTrib33.setVisible(false);
        picTrib33.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib34.setBackColor(SystemColors.Control);
        picTrib34.setLocation(new Point(168, 144));
        picTrib34.setName("picTrib34");
        picTrib34.setSize(new FormSize(12, 12));
        picTrib34.setTabIndex(65);
        picTrib34.setTabStop(false);
        picTrib34.setVisible(false);
        picTrib34.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib35.setBackColor(SystemColors.Control);
        picTrib35.setLocation(new Point(208, 128));
        picTrib35.setName("picTrib35");
        picTrib35.setSize(new FormSize(12, 12));
        picTrib35.setTabIndex(66);
        picTrib35.setTabStop(false);
        picTrib35.setVisible(false);
        picTrib35.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib41.setBackColor(SystemColors.Control);
        picTrib41.setLocation(new Point(48, 176));
        picTrib41.setName("picTrib41");
        picTrib41.setSize(new FormSize(12, 12));
        picTrib41.setTabIndex(67);
        picTrib41.setTabStop(false);
        picTrib41.setVisible(false);
        picTrib41.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib51.setBackColor(SystemColors.Control);
        picTrib51.setLocation(new Point(64, 216));
        picTrib51.setName("picTrib51");
        picTrib51.setSize(new FormSize(12, 12));
        picTrib51.setTabIndex(68);
        picTrib51.setTabStop(false);
        picTrib51.setVisible(false);
        picTrib51.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib42.setBackColor(SystemColors.Control);
        picTrib42.setLocation(new Point(88, 168));
        picTrib42.setName("picTrib42");
        picTrib42.setSize(new FormSize(12, 12));
        picTrib42.setTabIndex(69);
        picTrib42.setTabStop(false);
        picTrib42.setVisible(false);
        picTrib42.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib52.setBackColor(SystemColors.Control);
        picTrib52.setLocation(new Point(96, 224));
        picTrib52.setName("picTrib52");
        picTrib52.setSize(new FormSize(12, 12));
        picTrib52.setTabIndex(70);
        picTrib52.setTabStop(false);
        picTrib52.setVisible(false);
        picTrib52.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib43.setBackColor(SystemColors.Control);
        picTrib43.setLocation(new Point(136, 176));
        picTrib43.setName("picTrib43");
        picTrib43.setSize(new FormSize(12, 12));
        picTrib43.setTabIndex(71);
        picTrib43.setTabStop(false);
        picTrib43.setVisible(false);
        picTrib43.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib53.setBackColor(SystemColors.Control);
        picTrib53.setLocation(new Point(144, 216));
        picTrib53.setName("picTrib53");
        picTrib53.setSize(new FormSize(12, 12));
        picTrib53.setTabIndex(72);
        picTrib53.setTabStop(false);
        picTrib53.setVisible(false);
        picTrib53.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib44.setBackColor(SystemColors.Control);
        picTrib44.setLocation(new Point(184, 184));
        picTrib44.setName("picTrib44");
        picTrib44.setSize(new FormSize(12, 12));
        picTrib44.setTabIndex(73);
        picTrib44.setTabStop(false);
        picTrib44.setVisible(false);
        picTrib44.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib45.setBackColor(SystemColors.Control);
        picTrib45.setLocation(new Point(216, 176));
        picTrib45.setName("picTrib45");
        picTrib45.setSize(new FormSize(12, 12));
        picTrib45.setTabIndex(74);
        picTrib45.setTabStop(false);
        picTrib45.setVisible(false);
        picTrib45.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib54.setBackColor(SystemColors.Control);
        picTrib54.setLocation(new Point(176, 224));
        picTrib54.setName("picTrib54");
        picTrib54.setSize(new FormSize(12, 12));
        picTrib54.setTabIndex(75);
        picTrib54.setTabStop(false);
        picTrib54.setVisible(false);
        picTrib54.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        picTrib55.setBackColor(SystemColors.Control);
        picTrib55.setLocation(new Point(208, 216));
        picTrib55.setName("picTrib55");
        picTrib55.setSize(new FormSize(12, 12));
        picTrib55.setTabIndex(76);
        picTrib55.setTabStop(false);
        picTrib55.setVisible(false);
        picTrib55.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                picTrib_Click(sender, e);
            }
        });
        tmrTick.setInterval(1000);
        tmrTick.Tick = new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                tmrTick_Tick(sender, e);
            }
        };
        setAutoScaleBaseSize(new FormSize(5, 13));
        setClientSize(new FormSize(234, 271));
        setControlBox(false);
        Controls.add(picTrib55);
        Controls.add(picTrib54);
        Controls.add(picTrib45);
        Controls.add(picTrib44);
        Controls.add(picTrib53);
        Controls.add(picTrib43);
        Controls.add(picTrib52);
        Controls.add(picTrib42);
        Controls.add(picTrib51);
        Controls.add(picTrib41);
        Controls.add(picTrib35);
        Controls.add(picTrib34);
        Controls.add(picTrib33);
        Controls.add(picTrib32);
        Controls.add(picTrib31);
        Controls.add(picTrib25);
        Controls.add(picTrib24);
        Controls.add(picTrib23);
        Controls.add(picTrib22);
        Controls.add(picTrib21);
        Controls.add(picTrib15);
        Controls.add(picTrib14);
        Controls.add(picTrib13);
        Controls.add(picTrib12);
        Controls.add(picTrib11);
        Controls.add(picTrib05);
        Controls.add(picTrib01);
        Controls.add(picTrib02);
        Controls.add(picTrib03);
        Controls.add(picTrib04);
        Controls.add(picTrib30);
        Controls.add(picTrib20);
        Controls.add(picTrib40);
        Controls.add(picTrib10);
        Controls.add(picTrib50);
        Controls.add(picTrib00);
        Controls.add(picEncounterType);
        Controls.add(picContinuous);
        Controls.add(btnYield);
        Controls.add(btnInt);
        Controls.add(btnMeet);
        Controls.add(btnPlunder);
        Controls.add(btnTrade);
        Controls.add(btnIgnore);
        Controls.add(btnSurrender);
        Controls.add(btnBribe);
        Controls.add(btnSubmit);
        Controls.add(btnFlee);
        Controls.add(lblOpponentShields);
        Controls.add(lblOpponentHull);
        Controls.add(lblYouShields);
        Controls.add(lblYouHull);
        Controls.add(lblYouShip);
        Controls.add(lblOpponentShip);
        Controls.add(lblYouLabel);
        Controls.add(lblOpponentLabel);
        Controls.add(lblAction);
        Controls.add(picShipOpponent);
        Controls.add(picShipYou);
        Controls.add(lblEncounter);
        Controls.add(btnDrink);
        Controls.add(btnBoard);
        Controls.add(btnAttack);
        setFormBorderStyle(FormBorderStyle.FixedDialog);
        setMaximizeBox(false);
        setMinimizeBox(false);
        setName("FormEncounter");
        setShowInTaskbar(false);
        setStartPosition(FormStartPosition.CenterParent);
        setText("Encounter");
        ResumeLayout(false);
    }

    private void DisableAuto() {
        tmrTick.Stop();
        game.setEncounterContinueFleeing(false);
        game.setEncounterContinueAttacking(false);
        btnInt.setVisible(false);
        picContinuous.setVisible(false);
    }

    private void ExecuteAction() {
        _result = game.EncounterExecuteAction(this);
        if (_result == EncounterResult.Continue) {
            UpdateButtons();
            UpdateShipStats();
            lblEncounter.setText(game.EncounterText());
            lblAction.setText(game.EncounterAction());
            if (game.getEncounterContinueFleeing() || game.getEncounterContinueAttacking()) {
                tmrTick.Start();
            }
        } else {
            Close();
        }
    }

    private void Exit(EncounterResult result) {
        _result = result;
        Close();
    }

    private void UpdateButtons() {
        boolean[] visible = new boolean[buttons.length];
        switch(game.getEncounterType()) {
            case BottleGood:
            case BottleOld:
                visible[DRINK] = true;
                visible[IGNORE] = true;
                btnIgnore.setLeft(btnDrink.getLeft() + btnDrink.getWidth() + 8);
                break;
            case CaptainAhab:
            case CaptainConrad:
            case CaptainHuie:
                visible[ATTACK] = true;
                visible[IGNORE] = true;
                visible[MEET] = true;
                break;
            case DragonflyAttack:
            case FamousCaptainAttack:
            case ScorpionAttack:
            case SpaceMonsterAttack:
            case TraderAttack:
                visible[ATTACK] = true;
                visible[FLEE] = true;
                btnInt.setLeft(btnFlee.getLeft() + btnFlee.getWidth() + 8);
                break;
            case DragonflyIgnore:
            case FamousCaptDisabled:
            case PoliceDisabled:
            case PoliceFlee:
            case PoliceIgnore:
            case PirateFlee:
            case PirateIgnore:
            case ScarabIgnore:
            case ScorpionIgnore:
            case SpaceMonsterIgnore:
            case TraderFlee:
            case TraderIgnore:
                visible[ATTACK] = true;
                visible[IGNORE] = true;
                break;
            case MarieCeleste:
                visible[BOARD] = true;
                visible[IGNORE] = true;
                btnIgnore.setLeft(btnBoard.getLeft() + btnBoard.getWidth() + 8);
                break;
            case MarieCelestePolice:
                visible[ATTACK] = true;
                visible[FLEE] = true;
                visible[YIELD] = true;
                visible[BRIBE] = true;
                btnBribe.setLeft(btnYield.getLeft() + btnYield.getWidth() + 8);
                break;
            case PirateAttack:
            case PoliceAttack:
            case PoliceSurrender:
            case ScarabAttack:
                visible[ATTACK] = true;
                visible[FLEE] = true;
                visible[SURRENDER] = true;
                btnInt.setLeft(btnSurrender.getLeft() + btnSurrender.getWidth() + 8);
                break;
            case PirateDisabled:
            case PirateSurrender:
            case TraderDisabled:
            case TraderSurrender:
                visible[ATTACK] = true;
                visible[PLUNDER] = true;
                break;
            case PoliceInspect:
                visible[ATTACK] = true;
                visible[FLEE] = true;
                visible[SUBMIT] = true;
                visible[BRIBE] = true;
                break;
            case TraderBuy:
            case TraderSell:
                visible[ATTACK] = true;
                visible[IGNORE] = true;
                visible[TRADE] = true;
                break;
        }
        if (game.getEncounterContinueAttacking() || game.getEncounterContinueFleeing()) {
            visible[INT] = true;
        }
        for (int i = 0; i < visible.length; i++) {
            if (visible[i] != buttons[i].getVisible()) {
                buttons[i].setVisible(visible[i]);
                if (i == INT) {
                    picContinuous.setVisible(visible[i]);
                }
            }
        }
        if (picContinuous.getVisible()) {
            picContinuous.setImage(ilContinuous.getImages()[contImg = (contImg + 1) % 2]);
        }
    }

    private void UpdateShipInfo() {
        lblYouShip.setText(cmdrship.Name());
        lblOpponentShip.setText(opponent.Name());
        UpdateShipStats();
    }

    private void UpdateShipStats() {
        lblYouHull.setText(cmdrship.HullText());
        lblYouShields.setText(cmdrship.ShieldText());
        lblOpponentHull.setText(opponent.HullText());
        lblOpponentShields.setText(opponent.ShieldText());
        picShipYou.Refresh();
        picShipOpponent.Refresh();
    }

    private void UpdateTribbles() {
        PictureBox[] tribbles = new PictureBox[] { picTrib00, picTrib01, picTrib02, picTrib03, picTrib04, picTrib05, picTrib10, picTrib11, picTrib12, picTrib13, picTrib14, picTrib15, picTrib20, picTrib21, picTrib22, picTrib23, picTrib24, picTrib25, picTrib30, picTrib31, picTrib32, picTrib33, picTrib34, picTrib35, picTrib40, picTrib41, picTrib42, picTrib43, picTrib44, picTrib45, picTrib50, picTrib51, picTrib52, picTrib53, picTrib54, picTrib55 };
        int toShow = Math.min(tribbles.length, (int) Math.sqrt(cmdrship.getTribbles() / Math.ceil(Consts.MaxTribbles / Math.pow(tribbles.length + 1, 2))));
        for (int i = 0; i < toShow; i++) {
            int index = Functions.GetRandom(tribbles.length);
            while (tribbles[index].getVisible()) {
                index = (index + 1) % tribbles.length;
            }
            tribbles[index].setImage(ilTribbles.getImages()[Functions.GetRandom(ilTribbles.getImages().length)]);
            tribbles[index].setVisible(true);
        }
    }

    private void btnAttack_Click(Object sender, EventArgs e) {
        DisableAuto();
        if (game.EncounterVerifyAttack(this)) {
            ExecuteAction();
        }
    }

    private void btnBoard_Click(Object sender, EventArgs e) {
        if (game.EncounterVerifyBoard(this)) {
            Exit(EncounterResult.Normal);
        }
    }

    private void btnBribe_Click(Object sender, EventArgs e) {
        if (game.EncounterVerifyBribe(this)) {
            Exit(EncounterResult.Normal);
        }
    }

    private void btnDrink_Click(Object sender, EventArgs e) {
        game.EncounterDrink(this);
        Exit(EncounterResult.Normal);
    }

    private void btnFlee_Click(Object sender, EventArgs e) {
        DisableAuto();
        if (game.EncounterVerifyFlee(this)) {
            ExecuteAction();
        }
    }

    private void btnIgnore_Click(Object sender, EventArgs e) {
        DisableAuto();
        Exit(EncounterResult.Normal);
    }

    private void btnInt_Click(Object sender, EventArgs e) {
        DisableAuto();
    }

    private void btnMeet_Click(Object sender, EventArgs e) {
        game.EncounterMeet(this);
        Exit(EncounterResult.Normal);
    }

    private void btnPlunder_Click(Object sender, EventArgs e) {
        DisableAuto();
        game.EncounterPlunder(this);
        Exit(EncounterResult.Normal);
    }

    private void btnSubmit_Click(Object sender, EventArgs e) {
        if (game.EncounterVerifySubmit(this)) {
            Exit(cmdrship.IllegalSpecialCargo() ? EncounterResult.Arrested : EncounterResult.Normal);
        }
    }

    private void btnSurrender_Click(Object sender, EventArgs e) {
        DisableAuto();
        _result = game.EncounterVerifySurrender(this);
        if (_result != EncounterResult.Continue) {
            Close();
        }
    }

    private void btnTrade_Click(Object sender, EventArgs e) {
        game.EncounterTrade(this);
        Exit(EncounterResult.Normal);
    }

    private void btnYield_Click(Object sender, EventArgs e) {
        _result = game.EncounterVerifyYield(this);
        if (_result != EncounterResult.Continue) {
            Close();
        }
    }

    private void picShipOpponent_Paint(Object sender, PaintEventArgs e) {
        Functions.PaintShipImage(opponent, e.Graphics, picShipOpponent.getBackColor());
    }

    private void picShipYou_Paint(Object sender, PaintEventArgs e) {
        Functions.PaintShipImage(cmdrship, e.Graphics, picShipYou.getBackColor());
    }

    private void picTrib_Click(Object sender, EventArgs e) {
        FormAlert.Alert(AlertType.TribblesSqueek, this);
    }

    private void tmrTick_Tick(Object sender, EventArgs e) {
        DisableAuto();
        ExecuteAction();
    }

    public EncounterResult Result() {
        return _result;
    }
}
