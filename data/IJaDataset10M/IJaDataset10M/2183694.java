package spacetrader.gui;

import java.awt.Point;
import jwinforms.Button;
import jwinforms.CheckBox;
import jwinforms.EventArgs;
import jwinforms.EventHandler;
import jwinforms.FormSize;
import jwinforms.ISupportInitialize;
import jwinforms.Label;
import jwinforms.NumericUpDown;
import jwinforms.WinformForm;
import jwinforms.enums.DialogResult;
import jwinforms.enums.FlatStyle;
import jwinforms.enums.FormBorderStyle;
import jwinforms.enums.FormStartPosition;
import spacetrader.Game;
import spacetrader.GameOptions;
import spacetrader.enums.AlertType;

public class FormOptions extends WinformForm {

    private final Game game = Game.CurrentGame();

    private Button btnOk;

    private Button btnCancel;

    private Button btnSave;

    private Button btnLoad;

    private CheckBox chkFuel;

    private CheckBox chkContinuousAttack;

    private CheckBox chkAttackFleeing;

    private CheckBox chkDisable;

    private CheckBox chkNewspaper;

    private CheckBox chkNewspaperShow;

    private CheckBox chkRange;

    private CheckBox chkStopTracking;

    private CheckBox chkLoan;

    private CheckBox chkIgnoreTradersDealing;

    private CheckBox chkReserveMoney;

    private CheckBox chkIgnoreTraders;

    private CheckBox chkIgnorePirates;

    private CheckBox chkIgnorePolice;

    private CheckBox chkRepair;

    private Label lblEmpty;

    private Label lblIgnore;

    private NumericUpDown numEmpty;

    private boolean initializing = true;

    private GameOptions opts = new GameOptions(false);

    public FormOptions() {
        InitializeComponent();
        if (game != null) {
            opts.CopyValues(game.Options());
        } else {
            opts.LoadFromDefaults(false, this);
            btnOk.setEnabled(false);
            FormAlert.Alert(AlertType.OptionsNoGame, this);
        }
        UpdateAll();
    }

    private void InitializeComponent() {
        btnOk = new Button();
        btnCancel = new Button();
        lblEmpty = new Label();
        chkFuel = new CheckBox();
        chkContinuousAttack = new CheckBox();
        chkAttackFleeing = new CheckBox();
        chkNewspaper = new CheckBox();
        chkRange = new CheckBox();
        chkStopTracking = new CheckBox();
        chkLoan = new CheckBox();
        chkIgnoreTradersDealing = new CheckBox();
        chkReserveMoney = new CheckBox();
        chkIgnoreTraders = new CheckBox();
        chkIgnorePirates = new CheckBox();
        chkIgnorePolice = new CheckBox();
        chkRepair = new CheckBox();
        lblIgnore = new Label();
        numEmpty = new NumericUpDown();
        btnSave = new Button();
        btnLoad = new Button();
        chkNewspaperShow = new CheckBox();
        chkDisable = new CheckBox();
        ((ISupportInitialize) (numEmpty)).BeginInit();
        SuspendLayout();
        btnOk.setDialogResult(DialogResult.OK);
        btnOk.setFlatStyle(FlatStyle.Flat);
        btnOk.setLocation(new Point(14, 240));
        btnOk.setName("btnOk");
        btnOk.setSize(new FormSize(40, 22));
        btnOk.setTabIndex(15);
        btnOk.setText("Ok");
        btnCancel.setDialogResult(DialogResult.Cancel);
        btnCancel.setFlatStyle(FlatStyle.Flat);
        btnCancel.setLocation(new Point(62, 240));
        btnCancel.setName("btnCancel");
        btnCancel.setSize(new FormSize(49, 22));
        btnCancel.setTabIndex(16);
        btnCancel.setText("Cancel");
        lblEmpty.setAutoSize(true);
        lblEmpty.setLocation(new Point(52, 90));
        lblEmpty.setName("lblEmpty");
        lblEmpty.setSize(new FormSize(292, 16));
        lblEmpty.setTabIndex(38);
        lblEmpty.setText("Cargo Bays to leave empty when buying goods in-system");
        chkFuel.setLocation(new Point(8, 8));
        chkFuel.setName("chkFuel");
        chkFuel.setSize(new FormSize(160, 16));
        chkFuel.setTabIndex(1);
        chkFuel.setText("Get full fuel tanks on arrival");
        chkFuel.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkContinuousAttack.setLocation(new Point(8, 176));
        chkContinuousAttack.setName("chkContinuousAttack");
        chkContinuousAttack.setSize(new FormSize(163, 16));
        chkContinuousAttack.setTabIndex(13);
        chkContinuousAttack.setText("Continuous attack and flight");
        chkContinuousAttack.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkAttackFleeing.setLocation(new Point(24, 192));
        chkAttackFleeing.setName("chkAttackFleeing");
        chkAttackFleeing.setSize(new FormSize(177, 16));
        chkAttackFleeing.setTabIndex(14);
        chkAttackFleeing.setText("Continue attacking fleeing ship");
        chkAttackFleeing.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkNewspaper.setLocation(new Point(8, 40));
        chkNewspaper.setName("chkNewspaper");
        chkNewspaper.setSize(new FormSize(155, 16));
        chkNewspaper.setTabIndex(3);
        chkNewspaper.setText("Always pay for newspaper");
        chkNewspaper.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkRange.setLocation(new Point(184, 8));
        chkRange.setName("chkRange");
        chkRange.setSize(new FormSize(175, 16));
        chkRange.setTabIndex(5);
        chkRange.setText("Show range to tracked system");
        chkRange.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkStopTracking.setLocation(new Point(184, 24));
        chkStopTracking.setName("chkStopTracking");
        chkStopTracking.setSize(new FormSize(139, 16));
        chkStopTracking.setTabIndex(6);
        chkStopTracking.setText("Stop tracking on arrival");
        chkStopTracking.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkLoan.setLocation(new Point(184, 56));
        chkLoan.setName("chkLoan");
        chkLoan.setSize(new FormSize(124, 16));
        chkLoan.setTabIndex(4);
        chkLoan.setText("Remind about loans");
        chkLoan.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkIgnoreTradersDealing.setLocation(new Point(152, 152));
        chkIgnoreTradersDealing.setName("chkIgnoreTradersDealing");
        chkIgnoreTradersDealing.setSize(new FormSize(133, 16));
        chkIgnoreTradersDealing.setTabIndex(12);
        chkIgnoreTradersDealing.setText("Ignore dealing traders");
        chkIgnoreTradersDealing.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkReserveMoney.setLocation(new Point(184, 40));
        chkReserveMoney.setName("chkReserveMoney");
        chkReserveMoney.setSize(new FormSize(176, 16));
        chkReserveMoney.setTabIndex(7);
        chkReserveMoney.setText("Reserve money for warp costs");
        chkReserveMoney.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkIgnoreTraders.setLocation(new Point(136, 136));
        chkIgnoreTraders.setName("chkIgnoreTraders");
        chkIgnoreTraders.setSize(new FormSize(62, 16));
        chkIgnoreTraders.setTabIndex(11);
        chkIgnoreTraders.setText("Traders");
        chkIgnoreTraders.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkIgnorePirates.setLocation(new Point(8, 136));
        chkIgnorePirates.setName("chkIgnorePirates");
        chkIgnorePirates.setSize(new FormSize(58, 16));
        chkIgnorePirates.setTabIndex(9);
        chkIgnorePirates.setText("Pirates");
        chkIgnorePirates.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkIgnorePolice.setLocation(new Point(74, 136));
        chkIgnorePolice.setName("chkIgnorePolice");
        chkIgnorePolice.setSize(new FormSize(54, 16));
        chkIgnorePolice.setTabIndex(10);
        chkIgnorePolice.setText("Police");
        chkIgnorePolice.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkRepair.setLocation(new Point(8, 24));
        chkRepair.setName("chkRepair");
        chkRepair.setSize(new FormSize(167, 16));
        chkRepair.setTabIndex(2);
        chkRepair.setText("Get full hull repairs on arrival");
        chkRepair.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        lblIgnore.setAutoSize(true);
        lblIgnore.setLocation(new Point(8, 120));
        lblIgnore.setName("lblIgnore");
        lblIgnore.setSize(new FormSize(152, 16));
        lblIgnore.setTabIndex(52);
        lblIgnore.setText("Always ignore when it is safe:");
        numEmpty.setLocation(new Point(8, 88));
        numEmpty.setMaximum(99);
        numEmpty.setName("numEmpty");
        numEmpty.setSize(new FormSize(40, 20));
        numEmpty.setTabIndex(8);
        numEmpty.setValue(88);
        numEmpty.setValueChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        btnSave.setFlatStyle(FlatStyle.Flat);
        btnSave.setLocation(new Point(119, 240));
        btnSave.setName("btnSave");
        btnSave.setSize(new FormSize(107, 22));
        btnSave.setTabIndex(17);
        btnSave.setText("Save As Defaults");
        btnSave.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnSave_Click();
            }
        });
        btnLoad.setFlatStyle(FlatStyle.Flat);
        btnLoad.setLocation(new Point(234, 240));
        btnLoad.setName("btnLoad");
        btnLoad.setSize(new FormSize(114, 22));
        btnLoad.setTabIndex(18);
        btnLoad.setText("Load from Defaults");
        btnLoad.setClick(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                btnLoad_Click();
            }
        });
        chkNewspaperShow.setLocation(new Point(24, 56));
        chkNewspaperShow.setName("chkNewspaperShow");
        chkNewspaperShow.setSize(new FormSize(160, 16));
        chkNewspaperShow.setTabIndex(53);
        chkNewspaperShow.setText("Show newspaper on arrival");
        chkNewspaperShow.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        chkDisable.setLocation(new Point(8, 208));
        chkDisable.setName("chkDisable");
        chkDisable.setSize(new FormSize(244, 16));
        chkDisable.setTabIndex(54);
        chkDisable.setText("Attempt to disable opponents when possible");
        chkDisable.setCheckedChanged(new EventHandler<Object, EventArgs>() {

            @Override
            public void handle(Object sender, EventArgs e) {
                controlChanged();
            }
        });
        setAcceptButton(btnOk);
        setAutoScaleBaseSize(new FormSize(5, 13));
        setCancelButton(btnCancel);
        setClientSize(new FormSize(362, 271));
        Controls.add(chkDisable);
        Controls.add(chkLoan);
        Controls.add(chkNewspaperShow);
        Controls.add(btnLoad);
        Controls.add(btnSave);
        Controls.add(numEmpty);
        Controls.add(lblIgnore);
        Controls.add(lblEmpty);
        Controls.add(chkRepair);
        Controls.add(chkIgnorePolice);
        Controls.add(chkIgnorePirates);
        Controls.add(chkIgnoreTraders);
        Controls.add(chkReserveMoney);
        Controls.add(chkIgnoreTradersDealing);
        Controls.add(chkStopTracking);
        Controls.add(chkRange);
        Controls.add(chkNewspaper);
        Controls.add(chkAttackFleeing);
        Controls.add(chkContinuousAttack);
        Controls.add(chkFuel);
        Controls.add(btnCancel);
        Controls.add(btnOk);
        setFormBorderStyle(FormBorderStyle.FixedDialog);
        setMaximizeBox(false);
        setMinimizeBox(false);
        setName("FormOptions");
        setShowInTaskbar(false);
        setStartPosition(FormStartPosition.CenterParent);
        setText("Options");
        ((ISupportInitialize) (numEmpty)).EndInit();
        ResumeLayout(false);
    }

    private void UpdateAll() {
        initializing = true;
        chkFuel.setChecked(opts.getAutoFuel());
        chkRepair.setChecked(opts.getAutoRepair());
        chkNewspaper.setChecked(opts.getNewsAutoPay());
        chkNewspaperShow.setChecked(opts.getNewsAutoShow());
        chkLoan.setChecked(opts.getRemindLoans());
        chkRange.setChecked(opts.getShowTrackedRange());
        chkStopTracking.setChecked(opts.getTrackAutoOff());
        chkReserveMoney.setChecked(opts.getReserveMoney());
        numEmpty.setValue(opts.getLeaveEmpty());
        chkIgnorePirates.setChecked(opts.getAlwaysIgnorePirates());
        chkIgnorePolice.setChecked(opts.getAlwaysIgnorePolice());
        chkIgnoreTraders.setChecked(opts.getAlwaysIgnoreTraders());
        chkIgnoreTradersDealing.setChecked(opts.getAlwaysIgnoreTradeInOrbit());
        chkContinuousAttack.setChecked(opts.getContinuousAttack());
        chkAttackFleeing.setChecked(opts.getContinuousAttackFleeing());
        chkDisable.setChecked(opts.getDisableOpponents());
        UpdateContinueAttackFleeing();
        UpdateIgnoreTradersDealing();
        UpdateNewsAutoShow();
        initializing = false;
    }

    private void UpdateContinueAttackFleeing() {
        if (!chkContinuousAttack.isChecked()) {
            chkAttackFleeing.setChecked(false);
        }
        chkAttackFleeing.setEnabled(chkContinuousAttack.isChecked());
    }

    private void UpdateIgnoreTradersDealing() {
        if (!chkIgnoreTraders.isChecked()) {
            chkIgnoreTradersDealing.setChecked(false);
        }
        chkIgnoreTradersDealing.setEnabled(chkIgnoreTraders.isChecked());
    }

    private void UpdateNewsAutoShow() {
        if (!chkNewspaper.isChecked()) {
            chkNewspaperShow.setChecked(false);
        }
        chkNewspaperShow.setEnabled(chkNewspaper.isChecked());
    }

    private void btnLoad_Click() {
        opts.LoadFromDefaults(true, this);
        UpdateAll();
    }

    private void btnSave_Click() {
        opts.SaveAsDefaults(this);
    }

    private void controlChanged() {
        if (!initializing) {
            initializing = true;
            UpdateContinueAttackFleeing();
            UpdateIgnoreTradersDealing();
            UpdateNewsAutoShow();
            initializing = false;
            opts.setAutoFuel(chkFuel.isChecked());
            opts.setAutoRepair(chkRepair.isChecked());
            opts.setNewsAutoPay(chkNewspaper.isChecked());
            opts.setNewsAutoShow(chkNewspaperShow.isChecked());
            opts.setRemindLoans(chkLoan.isChecked());
            opts.setShowTrackedRange(chkRange.isChecked());
            opts.setTrackAutoOff(chkStopTracking.isChecked());
            opts.setReserveMoney(chkReserveMoney.isChecked());
            opts.setLeaveEmpty(numEmpty.getValue());
            opts.setAlwaysIgnorePirates(chkIgnorePirates.isChecked());
            opts.setAlwaysIgnorePolice(chkIgnorePolice.isChecked());
            opts.setAlwaysIgnoreTraders(chkIgnoreTraders.isChecked());
            opts.setAlwaysIgnoreTradeInOrbit(chkIgnoreTradersDealing.isChecked());
            opts.setContinuousAttack(chkContinuousAttack.isChecked());
            opts.setContinuousAttackFleeing(chkAttackFleeing.isChecked());
            opts.setDisableOpponents(chkDisable.isChecked());
        }
    }

    public GameOptions Options() {
        return opts;
    }
}
