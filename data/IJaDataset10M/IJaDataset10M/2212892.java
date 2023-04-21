package client.campaign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;
import megamek.common.CriticalSlot;
import megamek.common.OffBoardDirection;
import client.MWClient;
import client.util.CArmyComparator;
import client.util.CUnitComparator;
import common.CampaignData;
import common.House;
import common.Player;
import common.SubFaction;
import common.Unit;
import common.util.TokenReader;
import common.util.UnitComponents;
import common.util.UnitUtils;

/**
 * Class for Player object used by Client
 */
public class CPlayer extends Player {

    public static final String PREFIX = "PL";

    public static final String DELIMITER = "#";

    private MWClient mwclient;

    private String Name;

    private String House;

    private String myLogo = "";

    private int Exp;

    private int Money;

    private int Bays;

    private int FreeBays;

    private int Influence;

    private int Techs;

    private int TechCost;

    private int RewardPoints;

    private double Rating;

    private int hangarPenalty;

    private int hangarPurchasePenalties[][] = new int[6][4];

    private Vector<CUnit> Hangar;

    private Vector<CArmy> Armies;

    private ArrayList<CUnit> AutoArmy;

    private ArrayList<String> adminExcludes;

    private ArrayList<String> playerExcludes;

    private CPersonalPilotQueues personalPilotQueue;

    private House myHouse = null;

    private House houseFightingFor = null;

    private ArrayList<Integer> totalTechs = new ArrayList<Integer>(4);

    private ArrayList<Integer> availableTechs = new ArrayList<Integer>(4);

    private int repairLocation = 0;

    private int repairTechType = 0;

    private int repairRetries = 0;

    private int conventionalMinesAllowed = 0;

    private int vibraMinesAllowed = 0;

    private UnitComponents partsCache = new UnitComponents();

    private String subFactionName = "";

    public CPlayer(MWClient client) {
        mwclient = client;
        Name = "";
        Exp = 0;
        Money = 0;
        Bays = 0;
        FreeBays = 0;
        Influence = 0;
        Rating = 0;
        House = "";
        Hangar = new Vector<CUnit>(1, 1);
        Armies = new Vector<CArmy>(1, 1);
        AutoArmy = new ArrayList<CUnit>();
        personalPilotQueue = new CPersonalPilotQueues();
        adminExcludes = new ArrayList<String>();
        playerExcludes = new ArrayList<String>();
        myHouse = new House();
        houseFightingFor = new House();
        for (int x = 0; x < 4; x++) {
            availableTechs.add(0);
            totalTechs.add(0);
        }
    }

    public boolean decodeCommand(String command) {
        StringTokenizer ST;
        String element;
        ST = new StringTokenizer(command, "|");
        element = TokenReader.readString(ST);
        if (!element.equals("PL")) {
            return (false);
        }
        element = TokenReader.readString(ST);
        command = command.substring(3);
        if (element.equals("DA")) {
            if (!setData(command)) {
                return (false);
            }
            return (true);
        }
        return (false);
    }

    /**
     * Called from PL after PL|SAD received. Adds a new army OR replaces an old
     * army's data with new dump.
     */
    public void setArmyData(String data) {
        CArmy newArmy = new CArmy();
        newArmy.fromString(data, this, "%", mwclient);
        CArmy oldArmy = getArmy(newArmy.getID());
        if (oldArmy != null) {
            newArmy.setLegalOperations(oldArmy.getLegalOperations());
        }
        removeArmy(newArmy.getID());
        if (Armies.size() < newArmy.getID()) {
            Armies.add(newArmy);
        } else {
            Armies.add(newArmy.getID(), newArmy);
        }
    }

    /**
     * Complete setData command. Called in response to a PS| sent by the server.
     * 
     * @param - data
     * @return - success
     */
    public boolean setData(String data) {
        StringTokenizer ST;
        String element;
        CUnit tmek;
        int i, Armiescount, Hangarcount;
        ST = new StringTokenizer(data, "~");
        element = TokenReader.readString(ST);
        if (!element.equals("CP")) {
            return false;
        }
        for (int x = 0; x < UnitUtils.TECH_ELITE; x++) {
            setTotalTechs(x, 0);
            setAvailableTechs(x, 0);
        }
        Armies.clear();
        Hangar.clear();
        Name = TokenReader.readString(ST);
        Money = TokenReader.readInt(ST);
        Exp = TokenReader.readInt(ST);
        Hangarcount = TokenReader.readInt(ST);
        for (i = 0; i < Hangarcount; i++) {
            tmek = new CUnit(mwclient);
            if (tmek.setData(TokenReader.readString(ST))) {
                Hangar.add(tmek);
            }
        }
        Armiescount = (TokenReader.readInt(ST));
        for (i = 0; i < Armiescount; i++) {
            CArmy army = new CArmy();
            army.fromString(TokenReader.readString(ST), this, "%", mwclient);
            Armies.add(army);
        }
        Bays = TokenReader.readInt(ST);
        FreeBays = TokenReader.readInt(ST);
        Rating = Double.parseDouble(TokenReader.readString(ST));
        Influence = TokenReader.readInt(ST);
        setTechnicians(TokenReader.readInt(ST));
        doPayTechniciansMath();
        RewardPoints = TokenReader.readInt(ST);
        House = TokenReader.readString(ST);
        setHouseFightingFor(TokenReader.readString(ST));
        setLogo(TokenReader.readString(ST));
        setInvisible(TokenReader.readBoolean(ST));
        if (Boolean.parseBoolean(mwclient.getserverConfigs("UsePartsRepair"))) {
            partsCache.fromString(TokenReader.readString(ST), "|");
        } else {
            TokenReader.readString(ST);
        }
        setAutoReorder(TokenReader.readBoolean(ST));
        CampaignData.mwlog.infoLog("My Player Flags: " + flags.export());
        sortHangar();
        return true;
    }

    /**
     * Called by PL|HD - adds a single unit to the hangar.
     */
    public void setHangarData(String data) {
        try {
            CUnit unit = new CUnit(mwclient);
            if (unit.setData(data)) {
                Hangar.add(unit);
                sortHangar();
            }
        } catch (Exception e) {
            CampaignData.mwlog.errLog(e);
            return;
        }
    }

    /**
     * Called by PL|UU - updates a unit's data.
     */
    public void updateUnitData(StringTokenizer st) {
        try {
            CUnit currUnit = getUnit(TokenReader.readInt(st));
            currUnit.setData(TokenReader.readString(st));
            sortHangar();
        } catch (Exception e) {
            CampaignData.mwlog.errLog(e);
            return;
        }
    }

    public void updateUnitMachineGuns(StringTokenizer st) {
        try {
            CUnit currUnit = getUnit(TokenReader.readInt(st));
            int location = TokenReader.readInt(st);
            int slot = TokenReader.readInt(st);
            boolean selection = TokenReader.readBoolean(st);
            CriticalSlot crit = currUnit.getEntity().getCritical(location, slot);
            currUnit.getEntity().getEquipment(crit.getIndex()).setRapidfire(selection);
            sortHangar();
        } catch (Exception e) {
            CampaignData.mwlog.errLog(e);
            return;
        }
    }

    /**
     * Remove an army from a player's set. This can be called directly from a
     * PL|RA command, or indirectly by PL|SAD via CPlayer.setArmyData(), which
     * removes all old instances of an army before adding the new data.
     */
    public boolean removeArmy(int lanceID) {
        for (Iterator<CArmy> i = Armies.iterator(); i.hasNext(); ) {
            if (i.next().getID() == lanceID) {
                i.remove();
                mwclient.getMainFrame().updateAttackMenu();
                return (true);
            }
        }
        return (false);
    }

    /**
     * Remove a unit from the player's hangar. Called from PL after receipt of a
     * PL|RU|ID (RemoveUnit#ID) command.
     * 
     * Note that there is NOT an analagous addUnit() method. Single additions
     * are sent to the clients using (obtusely enough) the PL|HD (hangar data)
     * command. See .setHangarData()'s comments, as well as those in
     * SUnit.addUnit(), for details/explanation.
     */
    public boolean removeUnit(int unitID) {
        for (Iterator<CUnit> i = Hangar.iterator(); i.hasNext(); ) {
            if (i.next().getId() == unitID) {
                i.remove();
                return (true);
            }
        }
        return (false);
    }

    /**
     * @return Returns the armies.
     */
    public Vector<CArmy> getArmies() {
        return Armies;
    }

    public void setExp(int texp) {
        Exp = texp;
    }

    public void setMoney(int tmoney) {
        Money = tmoney;
    }

    public void setRewardPoints(int rewards) {
        RewardPoints = rewards;
    }

    public void setBays(int tbays) {
        Bays = tbays;
    }

    public void setFreeBays(int tfreebays) {
        FreeBays = tfreebays;
    }

    public void setInfluence(int tinfluence) {
        Influence = tinfluence;
    }

    public void setRating(double trating) {
        Rating = trating;
    }

    public void setHouse(String faction) {
        myHouse = mwclient.getData().getHouseByName(faction);
        House = faction;
        mwclient.sendChat(MWClient.CAMPAIGN_PREFIX + "c getfactionconfigs#0" + mwclient.getserverConfigs("TIMESTAMP"));
        if (mwclient.getMainFrame().getMainPanel().getBMPanel() != null) {
            mwclient.getMainFrame().getMainPanel().getBMPanel().checkFactionAccess();
        }
        if (mwclient.getMainFrame().getMainPanel().getHQPanel() != null) {
            mwclient.getMainFrame().getMainPanel().getHQPanel().reinitialize();
        }
    }

    public String getHouse() {
        return House;
    }

    public void setHouseFightingFor(String faction) {
        houseFightingFor = mwclient.getData().getHouseByName(faction);
    }

    public House getHouseFightingFor() {
        return houseFightingFor;
    }

    public void setLogo(String logo) {
        myLogo = logo;
    }

    public String getLogo() {
        return "<img height=\"140\" width=\"130\" src =\"" + myLogo + "\">";
    }

    public String getMyLogo() {
        return myLogo;
    }

    public String getName() {
        return Name;
    }

    public int getExp() {
        return Exp;
    }

    public double getRating() {
        return Rating;
    }

    public int getRewardPoints() {
        return RewardPoints;
    }

    public int getMoney() {
        return Money;
    }

    public int getBays() {
        return Bays;
    }

    public int getFreeBays() {
        return FreeBays;
    }

    public int getInfluence() {
        return Influence;
    }

    public int getTechs() {
        return Techs;
    }

    @Override
    public void setTechnicians(int tech) {
        Techs = tech;
        doPayTechniciansMath();
    }

    public int getTechCost() {
        if (TechCost < 0) {
            return 0;
        }
        return TechCost + getHangarPenalty();
    }

    public Vector<CUnit> getHangar() {
        return Hangar;
    }

    /**
     * Calculate the the ID that would be assined to a newly created army. This
     * is used by the army builder to construct /c exm# commands for an as-yet
     * non-existant army.
     */
    public int getNextNewArmyID() {
        int newID = -1;
        int possibleNewID = 0;
        while (newID == -1) {
            for (int i = 0; i < Armies.size(); i++) {
                if ((Armies.get(i)).getID() == possibleNewID) {
                    newID = i;
                }
            }
            if (newID == -1) {
                newID = possibleNewID;
            } else {
                possibleNewID++;
                newID = -1;
            }
        }
        return newID;
    }

    /**
     * Method which greates an autoarmy. takes in a string with weight classes,
     * and uses server configs (path, filenames) to construct units of those
     * weights.
     * 
     * Units are added to servers when a player joins a game, same as units from
     * locked armies.
     */
    public void setAutoArmy(StringTokenizer st) {
        AutoArmy = new ArrayList<CUnit>();
        if (st == null) {
            return;
        }
        while (st.hasMoreTokens()) {
            String filename = TokenReader.readString(st);
            if (filename.equals("CLEAR")) {
                return;
            }
            int distInBoards = Integer.parseInt(mwclient.getserverConfigs("DistanceFromMap"));
            int distInHexes = distInBoards * 17;
            CUnit currUnit = new CUnit(mwclient);
            OffBoardDirection direction = OffBoardDirection.NORTH;
            switch(mwclient.getPlayerStartingEdge()) {
                case 0:
                    break;
                case 1:
                case 2:
                case 3:
                    direction = OffBoardDirection.NORTH;
                    break;
                case 4:
                    direction = OffBoardDirection.EAST;
                    break;
                case 5:
                case 6:
                case 7:
                    direction = OffBoardDirection.SOUTH;
                    break;
                case 8:
                    direction = OffBoardDirection.WEST;
                    break;
            }
            currUnit.setAutoUnitData(filename, distInHexes, direction);
            AutoArmy.add(currUnit);
        }
    }

    /**
     * Method which greates an autoarmy gun emplacements. takes in a string with
     * weight classes, and uses server configs (path, filenames) to construct
     * units of those weights.
     * 
     * Units are added to servers when a player joins a game, same as units from
     * locked armies.
     */
    public void setAutoGunEmplacements(StringTokenizer st) {
        if (st == null) {
            return;
        }
        while (st.hasMoreTokens()) {
            String filename = TokenReader.readString(st);
            if (filename.equals("CLEAR")) {
                return;
            }
            CUnit currUnit = new CUnit(mwclient);
            currUnit.setAutoUnitData(filename, 0, OffBoardDirection.NORTH);
            AutoArmy.add(currUnit);
        }
    }

    public void setMULCreatedArmy(StringTokenizer st) {
        while (st.hasMoreElements()) {
            String data = TokenReader.readString(st);
            if (data.equalsIgnoreCase("CLEAR")) {
                return;
            }
            CUnit cm = new CUnit();
            cm.setData(data);
            AutoArmy.add(cm);
        }
    }

    /**
     * Method which returns the autoArmy arraylist.
     */
    public ArrayList<CUnit> getAutoArmy() {
        return AutoArmy;
    }

    public CUnit getUnit(int unitID) {
        for (CUnit currU : Hangar) {
            if (currU.getId() == unitID) {
                return currU;
            }
        }
        return null;
    }

    public CArmy getArmy(int id) {
        for (CArmy currA : Armies) {
            if (currA.getID() == id) {
                return currA;
            }
        }
        return null;
    }

    public int getAmountOfTimesUnitExistsInArmies(int unitID) {
        int result = 0;
        for (CArmy currA : Armies) {
            if (currA.getUnit(unitID) != null) {
                result++;
            }
        }
        return result;
    }

    public String getArmiesUnitIsIn(int unitID) {
        StringBuilder result = new StringBuilder();
        for (CArmy currA : Armies) {
            if (currA.getUnit(unitID) != null) {
                result.append(currA.getID() + " ");
            }
        }
        return result.toString();
    }

    public synchronized ArrayList<Unit> getLockedUnits() {
        ArrayList<Unit> result = new ArrayList<Unit>();
        for (CArmy currA : Armies) {
            if (currA.isLocked()) {
                result.addAll(currA.getUnits());
            }
        }
        return result;
    }

    public synchronized CArmy getLockedArmy() {
        for (CArmy currA : Armies) {
            if (currA.isLocked()) {
                return currA;
            }
        }
        return null;
    }

    public void doPayTechniciansMath() {
        if (Techs <= 0) {
            TechCost = 0;
            return;
        }
        float amountToPay = 0;
        float additive = Float.parseFloat(mwclient.getserverConfigs("AdditivePerTech"));
        float ceiling = Float.parseFloat(mwclient.getserverConfigs("AdditiveCostCeiling"));
        int techCeiling = (int) (ceiling / additive);
        if (Techs > techCeiling) {
            int techsPastCeiling = Techs - techCeiling;
            amountToPay += ceiling * techsPastCeiling;
        }
        int techsUsingAdditive = 0;
        if (Techs > techCeiling) {
            techsUsingAdditive = techCeiling;
        } else {
            techsUsingAdditive = Techs;
        }
        int totalAdditions = 0;
        for (int i = 1; i <= techsUsingAdditive; i++) {
            totalAdditions += i;
        }
        amountToPay += totalAdditions * additive;
        amountToPay += hangarPenalty;
        TechCost = Math.round(amountToPay);
    }

    public void addArmyUnit(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        if (ST.hasMoreTokens()) {
            int army = TokenReader.readInt(ST);
            int unitid = TokenReader.readInt(ST);
            int bv = TokenReader.readInt(ST);
            int position = TokenReader.readInt(ST);
            if (position >= 0) {
                getArmy(army).addUnit(getUnit(unitid), position);
            } else {
                getArmy(army).addUnit(getUnit(unitid));
            }
            getArmy(army).setBV(bv);
            sortArmies();
        }
    }

    public void removeArmyUnit(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        if (ST.hasMoreTokens()) {
            int army = TokenReader.readInt(ST);
            int unitid = TokenReader.readInt(ST);
            int bv = TokenReader.readInt(ST);
            Iterator<Unit> i = getArmy(army).getUnits().iterator();
            while (i.hasNext()) {
                if (i.next().getId() == unitid) {
                    i.remove();
                    break;
                }
            }
            getArmy(army).setBV(bv);
            getArmy(army).getC3Network().remove(unitid);
        }
        mwclient.refreshGUI(MWClient.REFRESH_HQPANEL);
    }

    /**
     * Method called from PL| which updates a CArmy's legalOperations tree.
     */
    public void updateOperations(String data) {
        if (data.equals("CLEAR")) {
            for (CArmy army : getArmies()) {
                army.getLegalOperations().clear();
            }
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(data, "*");
        int armyID = TokenReader.readInt(tokenizer);
        CArmy army = getArmy(armyID);
        if (army == null) {
            return;
        }
        while (tokenizer.hasMoreTokens()) {
            String mode = "";
            String name = "";
            try {
                mode = TokenReader.readString(tokenizer);
                name = TokenReader.readString(tokenizer);
            } catch (NoSuchElementException e) {
                return;
            }
            if (mode.equals("a")) {
                army.getLegalOperations().add(name);
            } else if (mode.equals("r")) {
                army.getLegalOperations().remove(name);
            }
        }
        mwclient.getMainFrame().updateAttackMenu();
    }

    public void repositionArmyUnit(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        int army = TokenReader.readInt(ST);
        int unitid = TokenReader.readInt(ST);
        int position = TokenReader.readInt(ST);
        CArmy a = getArmy(army);
        Iterator<Unit> i = a.getUnits().iterator();
        while (i.hasNext()) {
            if (i.next().getId() == unitid) {
                i.remove();
                break;
            }
        }
        getArmy(army).addUnit(getUnit(unitid), position);
    }

    public void setUnitStatus(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        if (ST.hasMoreTokens()) {
            int unitid = TokenReader.readInt(ST);
            int status = TokenReader.readInt(ST);
            CUnit unit = getUnit(unitid);
            if (unit == null) {
                return;
            }
            if (mwclient.isUsingAdvanceRepairs() && (status == Unit.STATUS_UNMAINTAINED)) {
                unit.setStatus(Unit.STATUS_OK);
            } else {
                unit.setStatus(status);
            }
        }
    }

    public void setArmyName(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        if (ST.hasMoreTokens()) {
            int army = TokenReader.readInt(ST);
            String name = TokenReader.readString(ST);
            if (getArmy(army) != null) {
                getArmy(army).setName(name);
            }
        }
    }

    public void playerLockArmy(int aid) {
        if (getArmy(aid) != null) {
            getArmy(aid).playerLockArmy();
        }
    }

    public void playerUnlockArmy(int aid) {
        if (getArmy(aid) != null) {
            getArmy(aid).playerUnlockArmy();
        }
    }

    public void toggleArmyDisabled(int aid) {
        if (getArmy(aid) != null) {
            getArmy(aid).toggleArmyDisabled();
        }
    }

    public void setArmyBV(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        if (ST.hasMoreTokens()) {
            int army = TokenReader.readInt(ST);
            if (getArmy(army) != null) {
                getArmy(army).setBV(TokenReader.readInt(ST));
            } else {
                CampaignData.mwlog.errLog("Bad Army id: " + army);
            }
        }
    }

    public void setArmyLimit(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        if (ST.hasMoreTokens()) {
            int army = TokenReader.readInt(ST);
            int lowerLimit = TokenReader.readInt(ST);
            int upperLimit = TokenReader.readInt(ST);
            getArmy(army).setLowerLimiter(lowerLimit);
            getArmy(army).setUpperLimiter(upperLimit);
        }
    }

    public void setArmyOpForceSize(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        if (ST.hasMoreTokens()) {
            int army = TokenReader.readInt(ST);
            float opForceSize = TokenReader.readFloat(ST);
            getArmy(army).setOpForceSize(opForceSize);
        }
    }

    public void setArmyLock(String data) {
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        if (ST.hasMoreTokens()) {
            int army = TokenReader.readInt(ST);
            boolean lock = TokenReader.readBoolean(ST);
            getArmy(army).setLocked(lock);
        }
    }

    public void setPlayerPersonalPilotQueue(CPersonalPilotQueues queue) {
        personalPilotQueue = queue;
    }

    public CPersonalPilotQueues getPersonalPilotQueue() {
        return personalPilotQueue;
    }

    /**
     * Exclude method, called after receipt of PL|AEU| (Admin Exclude Update).
     * Because NP lists are expected to be small (2-5 players), the entire list
     * is sent every time.
     * 
     * @urgru 4.3.05
     */
    public void setAdminExcludes(String buffer, String token) {
        adminExcludes.clear();
        StringTokenizer ST = new StringTokenizer(buffer, token);
        while (ST.hasMoreElements()) {
            String curr = TokenReader.readString(ST);
            if (!curr.equals("0")) {
                adminExcludes.add(curr);
            }
        }
        mwclient.getMainFrame().getMainPanel().getUserListPanel().repaint();
    }

    /**
     * Exclude method, called after receipt of PL|PEU| (Player Exclude Update).
     * Because NP lists are expected to be small (2-5 players), the entire list
     * is sent every time.
     */
    public void setPlayerExcludes(String buffer, String token) {
        playerExcludes.clear();
        StringTokenizer ST = new StringTokenizer(buffer, token);
        while (ST.hasMoreElements()) {
            String curr = TokenReader.readString(ST);
            if (!curr.equals("0")) {
                playerExcludes.add(curr);
            }
        }
        mwclient.getMainFrame().getMainPanel().getUserListPanel().repaint();
    }

    public ArrayList<String> getAdminExcludes() {
        return adminExcludes;
    }

    public ArrayList<String> getPlayerExcludes() {
        return playerExcludes;
    }

    /**
     * Method which resorts every unit. Inefficient, but we hate clients.
     * Because we're evil. So there.
     * 
     * @urgru 4.4.05
     */
    public void sortHangar() {
        String primeSortOrder = mwclient.getConfigParam("PRIMARYHQSORTORDER");
        String secondarySortOrder = mwclient.getConfigParam("SECONDARYHQSORTORDER");
        String tertiarySortOrder = mwclient.getConfigParam("TERTIARYHQSORTORDER");
        String[] choices = { "Name", "Battle Value", "Gunnery Skill", "ID Number", "MP (Jumping)", "MP (Walking)", "Pilot Kills", "Unit Type", "Weight (Class)", "Weight (Tons)", "No Sort" };
        int primarySort = CUnitComparator.HQSORT_NONE;
        for (int i = 0; i < choices.length; i++) {
            if (primeSortOrder.equals(choices[i])) {
                primarySort = i;
            }
        }
        int secondarySort = CUnitComparator.HQSORT_NONE;
        for (int i = 0; i < choices.length; i++) {
            if (secondarySortOrder.equals(choices[i])) {
                secondarySort = i;
            }
        }
        int tertiarySort = CUnitComparator.HQSORT_NONE;
        for (int i = 0; i < choices.length; i++) {
            if (tertiarySortOrder.equals(choices[i])) {
                tertiarySort = i;
            }
        }
        Object[] unitsArray = Hangar.toArray();
        if ((tertiarySort != primarySort) && (tertiarySort != secondarySort) && (tertiarySort != CUnitComparator.HQSORT_NONE)) {
            Arrays.sort(unitsArray, new CUnitComparator(tertiarySort));
        }
        if ((primarySort != secondarySort) && (secondarySort != CUnitComparator.HQSORT_NONE)) {
            Arrays.sort(unitsArray, new CUnitComparator(secondarySort));
        }
        if (primarySort != CUnitComparator.HQSORT_NONE) {
            Arrays.sort(unitsArray, new CUnitComparator(primarySort));
        }
        Vector<CUnit> Hangar2 = new Vector<CUnit>(1, 1);
        for (Object element : unitsArray) {
            Hangar2.add((CUnit) element);
        }
        Hangar = Hangar2;
        unitsArray = null;
    }

    /**
     * Method which resorts every unit. Inefficient, but we hate clients.
     * Because we're evil. So there.
     * 
     * @urgru 4.4.05
     */
    public void sortArmies() {
        String primeSortOrder = mwclient.getConfigParam("PRIMARYARMYSORTORDER");
        String secondarySortOrder = mwclient.getConfigParam("SECONDARYARMYSORTORDER");
        String tertiarySortOrder = mwclient.getConfigParam("TERTIARYARMYSORTORDER");
        String[] choices = { "Name", "Battle Value", "ID Number", "Max Tonnage", "Avg Walk MP", "Avg Jump MP", "Number Of Units", "No Sort" };
        int primarySort = CArmyComparator.ARMYSORT_NONE;
        for (int i = 0; i < choices.length; i++) {
            if (primeSortOrder.equals(choices[i])) {
                primarySort = i;
            }
        }
        int secondarySort = CArmyComparator.ARMYSORT_NONE;
        for (int i = 0; i < choices.length; i++) {
            if (secondarySortOrder.equals(choices[i])) {
                secondarySort = i;
            }
        }
        int tertiarySort = CArmyComparator.ARMYSORT_NONE;
        for (int i = 0; i < choices.length; i++) {
            if (tertiarySortOrder.equals(choices[i])) {
                tertiarySort = i;
            }
        }
        Object[] armiesArray = Armies.toArray();
        if ((tertiarySort != primarySort) && (tertiarySort != secondarySort) && (tertiarySort != CArmyComparator.ARMYSORT_NONE)) {
            Arrays.sort(armiesArray, new CArmyComparator(tertiarySort));
        }
        if ((primarySort != secondarySort) && (secondarySort != CArmyComparator.ARMYSORT_NONE)) {
            Arrays.sort(armiesArray, new CArmyComparator(secondarySort));
        }
        if (primarySort != CArmyComparator.ARMYSORT_NONE) {
            Arrays.sort(armiesArray, new CArmyComparator(primarySort));
        }
        Vector<CArmy> Army2 = new Vector<CArmy>(1, 1);
        for (Object element : armiesArray) {
            Army2.add((CArmy) element);
        }
        Armies = Army2;
        armiesArray = null;
    }

    public int getHangarSpaceRequired(int typeid, int weightclass, int baymod, String model) {
        if (typeid == Unit.PROTOMEK) {
            return 0;
        }
        if ((typeid == Unit.INFANTRY) && Boolean.parseBoolean(mwclient.getserverConfigs("FootInfTakeNoBays"))) {
            boolean isFoot = model.startsWith("Foot");
            boolean isAMFoot = model.startsWith("Anti-Mech Foot");
            if (isFoot || isAMFoot) {
                return 0;
            }
        }
        int result = 1;
        String techAmount = "TechsFor" + Unit.getWeightClassDesc(weightclass) + Unit.getTypeClassDesc(typeid);
        result = Integer.parseInt(mwclient.getserverConfigs(techAmount));
        if (!mwclient.isUsingAdvanceRepairs()) {
            result += baymod;
        }
        if (result < 0) {
            result = 0;
        }
        return result;
    }

    public House getMyHouse() {
        return myHouse;
    }

    public void applyUnitRepairs(StringTokenizer data) {
        CUnit unit = getUnit(TokenReader.readInt(data));
        unit.applyRepairs(TokenReader.readString(data));
    }

    public void updateTotalTechs(String data) {
        StringTokenizer techs = new StringTokenizer(data, "%");
        int slot = 0;
        while (techs.hasMoreTokens()) {
            setTotalTechs(slot, TokenReader.readInt(techs));
            slot++;
        }
    }

    public void setTotalTechs(int slot, int techs) {
        totalTechs.set(slot, techs);
    }

    public ArrayList<Integer> getTotalTechs() {
        return totalTechs;
    }

    public void updateAvailableTechs(String data) {
        StringTokenizer techs = new StringTokenizer(data, "%");
        int slot = 0;
        while (techs.hasMoreTokens()) {
            setAvailableTechs(slot, TokenReader.readInt(techs));
            slot++;
        }
    }

    public void setAvailableTechs(int slot, int techs) {
        availableTechs.set(slot, techs);
    }

    public ArrayList<Integer> getAvailableTechs() {
        return availableTechs;
    }

    public void setRepairLocation(int loc) {
        repairLocation = loc;
    }

    public int getRepairLocation() {
        return repairLocation;
    }

    public void setRepairTechType(int type) {
        repairTechType = type;
    }

    public int getRepairTechType() {
        return repairTechType;
    }

    public void setRepairRetries(int retries) {
        repairRetries = retries;
    }

    public int getRepairRetries() {
        return repairRetries;
    }

    public void resetRepairs() {
        repairLocation = 0;
        repairTechType = 0;
        repairRetries = 0;
    }

    public void setConventionalMinesAllowed(int mines) {
        conventionalMinesAllowed = mines;
    }

    public int getConventionalMinesAllowed() {
        return conventionalMinesAllowed;
    }

    public void setVibraMinesAllowed(int mines) {
        vibraMinesAllowed = mines;
    }

    public int getVibraMinesAllowed() {
        return vibraMinesAllowed;
    }

    public void setMines(StringTokenizer st) {
        setConventionalMinesAllowed(TokenReader.readInt(st));
        setVibraMinesAllowed(TokenReader.readInt(st));
    }

    public void setFactionConfigs(String data) {
        if (data.startsWith("DONE#DONE")) {
            mwclient.setWaiting(false);
            return;
        }
        StringTokenizer ST = new StringTokenizer(data, DELIMITER);
        while (ST.hasMoreTokens()) {
            String key = TokenReader.readString(ST);
            String value = TokenReader.readString(ST);
            mwclient.getserverConfigs().setProperty(key, value);
        }
        mwclient.setWaiting(false);
    }

    public UnitComponents getPartsCache() {
        return partsCache;
    }

    public void setSubFaction(String name) {
        subFactionName = name;
    }

    public SubFaction getSubFaction() {
        SubFaction mySubFaction = myHouse.getSubFactionList().get(subFactionName);
        if (mySubFaction == null) {
            return new SubFaction();
        }
        return mySubFaction;
    }

    public int getSubFactionAccess() {
        SubFaction mySubFaction = myHouse.getSubFactionList().get(subFactionName);
        if (mySubFaction == null) {
            return 0;
        }
        return Integer.parseInt(mySubFaction.getConfig("AccessLevel"));
    }

    public String getSubFactionName() {
        return subFactionName;
    }

    public int getHangarPenalty() {
        return hangarPenalty;
    }

    public int getHangarPurchasePenalty(int type, int weight) {
        return hangarPurchasePenalties[type][weight];
    }

    public void setHangarPenalty(int p) {
        hangarPenalty = p;
    }

    public void setHangarPurchasePenalty(int type, int weight, int p) {
        hangarPurchasePenalties[type][weight] = p;
    }

    public void parseHangarPenaltyString(String readString) {
        StringTokenizer st = new StringTokenizer(readString, "*");
        setHangarPenalty(Integer.parseInt(st.nextToken()));
        for (int type = Unit.MEK; type < Unit.MAXBUILD; type++) {
            for (int weight = Unit.LIGHT; weight <= Unit.ASSAULT; weight++) {
                setHangarPurchasePenalty(type, weight, Integer.parseInt(st.nextToken()));
            }
        }
        mwclient.getMainFrame().getMainPanel().getHSPanel().updateDisplay();
    }
}
