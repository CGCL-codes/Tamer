package spacetrader;

import jwinforms.enums.DialogResult;
import jwinforms.WinformPane;
import org.gts.bst.crew.CrewMemberId;
import org.gts.bst.ship.ShipType;
import org.gts.bst.ship.equip.Equipment;
import org.gts.bst.ship.equip.GadgetType;
import org.gts.bst.ship.equip.ShieldType;
import org.gts.bst.ship.equip.WeaponType;
import spacetrader.enums.AlertType;
import spacetrader.gui.FormAlert;
import spacetrader.util.Hashtable;

public class Commander extends CrewMember {

    private Ship _ship = new Ship(ShipType.Gnat);

    private boolean _insurance = false;

    private int _cash = 1000;

    private int _debt = 0;

    private int _killsPirate = 0;

    private int _killsPolice = 0;

    private int _killsTrader = 0;

    private int _policeRecordScore = 0;

    private int _reputationScore = 0;

    private int _days = 0;

    private int _noclaim = 0;

    private int[] _priceCargo = new int[10];

    public Commander(CrewMember cm) {
        super(cm);
        _ship.Crew()[0] = this;
        _ship.AddEquipment(Consts.WeapObjs[WeaponType.PulseLaser.id]);
    }

    public Commander(Hashtable hash) {
        super(hash);
        _cash = GetValueFromHash(hash, "_cash", _cash);
        _debt = GetValueFromHash(hash, "_debt", _debt);
        _killsPirate = GetValueFromHash(hash, "_killsPirate", _killsPirate);
        _killsPolice = GetValueFromHash(hash, "_killsPolice", _killsPolice);
        _killsTrader = GetValueFromHash(hash, "_killsTrader", _killsTrader);
        _policeRecordScore = GetValueFromHash(hash, "_policeRecordScore", _policeRecordScore);
        _reputationScore = GetValueFromHash(hash, "_reputationScore", _reputationScore);
        _days = GetValueFromHash(hash, "_days", _days);
        _insurance = GetValueFromHash(hash, "_insurance", _insurance);
        _noclaim = GetValueFromHash(hash, "_noclaim", _noclaim);
        _ship = new Ship(GetValueFromHash(hash, "_ship", Hashtable.class));
        _priceCargo = GetValueFromHash(hash, "_priceCargo", _priceCargo, int[].class);
        Game.CurrentGame().Mercenaries()[CrewMemberId.Commander.CastToInt()] = this;
        Strings.CrewMemberNames[CrewMemberId.Commander.CastToInt()] = GetValueFromHash(hash, "_name", Strings.CrewMemberNames[CrewMemberId.Commander.CastToInt()]);
    }

    public void PayInterest() {
        if (getDebt() > 0) {
            int interest = Math.max(1, (int) (getDebt() * Consts.IntRate));
            if (getCash() > interest) {
                setCash(getCash() - interest);
            } else {
                setDebt(getDebt() + (interest - getCash()));
                setCash(0);
            }
        }
    }

    @Override
    public Hashtable Serialize() {
        Hashtable hash = super.Serialize();
        hash.add("_cash", _cash);
        hash.add("_debt", _debt);
        hash.add("_killsPirate", _killsPirate);
        hash.add("_killsPolice", _killsPolice);
        hash.add("_killsTrader", _killsTrader);
        hash.add("_policeRecordScore", _policeRecordScore);
        hash.add("_reputationScore", _reputationScore);
        hash.add("_days", _days);
        hash.add("_insurance", _insurance);
        hash.add("_noclaim", _noclaim);
        hash.add("_ship", _ship.Serialize());
        hash.add("_priceCargo", _priceCargo);
        hash.add("_name", Name());
        return hash;
    }

    public boolean TradeShip(ShipSpec specToBuy, int netPrice, WinformPane owner) {
        return TradeShip(specToBuy, netPrice, specToBuy.Name(), owner);
    }

    public boolean TradeShip(ShipSpec specToBuy, int netPrice, String newShipName, WinformPane owner) {
        boolean traded = false;
        if (netPrice > 0 && getDebt() > 0) {
            FormAlert.Alert(AlertType.DebtNoBuy, owner);
        } else if (netPrice > CashToSpend()) {
            FormAlert.Alert(AlertType.ShipBuyIF, owner);
        } else if (specToBuy.getCrewQuarters() < getShip().SpecialCrew().length) {
            String passengers = getShip().SpecialCrew()[1].Name();
            if (getShip().SpecialCrew().length > 2) {
                passengers += " and " + getShip().SpecialCrew()[2].Name();
            }
            FormAlert.Alert(AlertType.ShipBuyPassengerQuarters, owner, passengers);
        } else if (specToBuy.getCrewQuarters() < getShip().CrewCount()) {
            FormAlert.Alert(AlertType.ShipBuyCrewQuarters, owner);
        } else if (getShip().ReactorOnBoard()) {
            FormAlert.Alert(AlertType.ShipBuyReactor, owner);
        } else {
            Equipment[] special = new Equipment[] { Consts.WeapObjs[WeaponType.MorgansLaser.id], Consts.WeapObjs[WeaponType.QuantumDistruptor.id], Consts.Shields[ShieldType.Lightning.id], Consts.Gadgets[GadgetType.FuelCompactor.asInteger()], Consts.Gadgets[GadgetType.HiddenCargoBays.asInteger()] };
            boolean[] add = new boolean[special.length];
            boolean addPod = false;
            int extraCost = 0;
            for (int i = 0; i < special.length; i++) {
                if (getShip().HasEquipment(special[i])) {
                    if (specToBuy.Slots(special[i].EquipmentType()) == 0) {
                        FormAlert.Alert(AlertType.ShipBuyNoSlots, owner, newShipName, special[i].Name(), Strings.EquipmentTypes[special[i].EquipmentType().CastToInt()]);
                    } else {
                        extraCost += special[i].TransferPrice();
                        add[i] = true;
                    }
                }
            }
            if (getShip().getEscapePod()) {
                addPod = true;
                extraCost += Consts.PodTransferCost;
            }
            if (netPrice + extraCost > CashToSpend()) {
                FormAlert.Alert(AlertType.ShipBuyIFTransfer, owner);
            }
            extraCost = 0;
            for (int i = 0; i < special.length; i++) {
                if (add[i]) {
                    if (netPrice + extraCost + special[i].TransferPrice() > CashToSpend()) {
                        FormAlert.Alert(AlertType.ShipBuyNoTransfer, owner, special[i].Name());
                    } else if (FormAlert.Alert(AlertType.ShipBuyTransfer, owner, special[i].Name(), special[i].Name().toLowerCase(), Functions.FormatNumber(special[i].TransferPrice())) == jwinforms.enums.DialogResult.Yes) {
                        extraCost += special[i].TransferPrice();
                    } else {
                        add[i] = false;
                    }
                }
            }
            if (addPod) {
                if (netPrice + extraCost + Consts.PodTransferCost > CashToSpend()) {
                    FormAlert.Alert(AlertType.ShipBuyNoTransfer, owner, Strings.ShipInfoEscapePod);
                } else if (FormAlert.Alert(AlertType.ShipBuyTransfer, owner, Strings.ShipInfoEscapePod, Strings.ShipInfoEscapePod.toLowerCase(), Functions.FormatNumber(Consts.PodTransferCost)) == DialogResult.Yes) {
                    extraCost += Consts.PodTransferCost;
                } else {
                    addPod = false;
                }
            }
            if (FormAlert.Alert(AlertType.ShipBuyConfirm, owner, getShip().Name(), newShipName, (add[0] || add[1] || add[2] || addPod ? Strings.ShipBuyTransfer : "")) == DialogResult.Yes) {
                CrewMember[] oldCrew = getShip().Crew();
                setShip(new Ship(specToBuy.Type()));
                setCash(getCash() - (netPrice + extraCost));
                System.arraycopy(oldCrew, 0, getShip().Crew(), 0, Math.min(oldCrew.length, getShip().Crew().length));
                for (int i = 0; i < special.length; i++) {
                    if (add[i]) {
                        getShip().AddEquipment(special[i]);
                    }
                }
                if (addPod) {
                    getShip().setEscapePod(true);
                } else if (getInsurance()) {
                    setInsurance(false);
                    NoClaim(0);
                }
                traded = true;
            }
        }
        return traded;
    }

    public int CashToSpend() {
        return _cash - (Game.CurrentGame().Options().getReserveMoney() ? Game.CurrentGame().CurrentCosts() : 0);
    }

    public int NoClaim() {
        return _noclaim;
    }

    public void NoClaim(int value) {
        _noclaim = Math.max(0, Math.min(Consts.MaxNoClaim, value));
    }

    public int[] PriceCargo() {
        return _priceCargo;
    }

    public int Worth() {
        return getShip().getPrice() + _cash - _debt + (Game.CurrentGame().getQuestStatusMoon() > 0 ? SpecialEvent.MoonCost : 0);
    }

    public void setShip(Ship ship) {
        _ship = ship;
    }

    public Ship getShip() {
        return _ship;
    }

    public void setReputationScore(int reputationScore) {
        _reputationScore = reputationScore;
    }

    public int getReputationScore() {
        return _reputationScore;
    }

    public void setPoliceRecordScore(int policeRecordScore) {
        _policeRecordScore = policeRecordScore;
    }

    public int getPoliceRecordScore() {
        return _policeRecordScore;
    }

    public void setKillsTrader(int killsTrader) {
        _killsTrader = killsTrader;
    }

    public int getKillsTrader() {
        return _killsTrader;
    }

    public void setKillsPolice(int killsPolice) {
        _killsPolice = killsPolice;
    }

    public int getKillsPolice() {
        return _killsPolice;
    }

    public void setKillsPirate(int killsPirate) {
        _killsPirate = killsPirate;
    }

    public int getKillsPirate() {
        return _killsPirate;
    }

    public void setInsurance(boolean insurance) {
        _insurance = insurance;
    }

    public boolean getInsurance() {
        return _insurance;
    }

    public void setDebt(int debt) {
        _debt = debt;
    }

    public int getDebt() {
        return _debt;
    }

    public void setDays(int days) {
        _days = days;
    }

    public int getDays() {
        return _days;
    }

    public void setCash(int cash) {
        _cash = cash;
    }

    public int getCash() {
        return _cash;
    }
}
