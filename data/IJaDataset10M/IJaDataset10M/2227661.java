package net.sf.eqemutils.eqemu;

import java.sql.*;
import java.util.*;
import net.sf.eqemutils.utils.*;

/** Connection to the EQEmu MySQL database.
 */
public class EqEmuDatabaseConnection {

    /** Constants for the various tables and columns used by name.
   */
    public static final String Table_Account = "account";

    public static final String Column_Account_Id = "id";

    public static final String Column_Account_Name = "name";

    public static final String Column_Account_Password = "password";

    public static final String Column_Account_CharName = "charname";

    public static final String Table_Character = "character_";

    public static final String Column_Character_Id = "id";

    public static final String Column_Character_Name = "name";

    public static final String Column_Character_AccountId = "account_id";

    public static final String Column_Character_Profile = "profile";

    public static final String Table_Inventory = "inventory";

    public static final String Column_Inventory_CharId = "charid";

    public static final String Column_Inventory_SlotId = "slotid";

    public static final String Column_Inventory_ItemId = "itemid";

    public static final String Column_Inventory_Charges = "charges";

    public static final String Table_Items = "items";

    public static final String Column_Items_Id = "id";

    public static final String Column_Items_Name = "Name";

    public static final String Column_Items_BagSize = "bagsize";

    public static final String Column_Items_BagSlots = "bagslots";

    public static final String Column_Items_LoreGroup = "loregroup";

    public static final String Column_Items_NoDrop = "nodrop";

    public static final String Column_Items_NoRent = "norent";

    public static final String Column_Items_Size = "size";

    public static final String Column_Items_StackSize = "stacksize";

    public static final String Table_NpcTypes = "npc_types";

    public static final String Column_NpcTypes_Id = "id";

    public static final String Column_NpcTypes_Name = "name";

    public static final String Table_FactionList = "faction_list";

    public static final String Column_FactionList_Id = "id";

    public static final String Column_FactionList_Name = "name";

    public static final String Table_SpawnEntry = "spawnentry";

    public static final String Column_SpawnEntry_NpcId = "npcID";

    public static final String Column_SpawnEntry_SpawnGroupId = "spawngroupID";

    public static final String Table_Spawn2 = "spawn2";

    public static final String Column_Spawn2_SpawnGroupId = "spawngroupID";

    public static final String Column_Spawn2_Zone = "zone";

    public static final String Table_Warehouse = "warehouse";

    public static final String Column_Warehouse_AccountId = "accountid";

    public static final String Column_Warehouse_ItemId = "itemid";

    public static final String Column_Warehouse_Amount = "amount";

    public static final String ItemId_Warehouse_Cash = "2000000000";

    public static final String ItemId_Warehouse_Lock = "2000000001";

    /** Initializes 'this' as a connection to the EQEmu database 'nDatabase' on host 'nHost', as user 'nUser'.
   *   The connection is initially closed.
   */
    public EqEmuDatabaseConnection(String nHost, String nDatabase, String nUser) {
        initWithConnectionInformation(nHost, nDatabase, nUser);
    }

    protected void initWithConnectionInformation(String nHost, String nDatabase, String nUser) {
        _Host = nHost;
        _Database = nDatabase;
        _User = nUser;
        _SqlConnection = null;
    }

    /** Tells whether 'this' is opened.
   */
    public boolean IsOpened() {
        boolean Result = (_SqlConnection != null);
        return Result;
    }

    /** Returns the host to which 'this' connects to.
   */
    public String Host() {
        String Result = _Host;
        return Result;
    }

    /** Returns the database to which 'this' connects to.
   */
    public String Database() {
        String Result = _Database;
        return Result;
    }

    /** Returns the user as who 'this' connects.
   */
    public String User() {
        String Result = _User;
        return Result;
    }

    /** Returns the SQL connection underlying 'this'.
   *  precondition
   *    IsOpened()
   */
    public Connection SqlConnection() {
        assert IsOpened();
        Connection Result = _SqlConnection;
        return Result;
    }

    /** Opens 'this'.
   */
    public void Open(String Password) throws SQLException {
        assert !IsOpened();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            System.err.println("fatal error : cannot load the JDBC driver com.mysql.jdbc.Driver");
            throw (new Error(e));
        }
        _SqlConnection = DriverManager.getConnection("jdbc:mysql://" + _Host + "/" + _Database, _User, Password);
    }

    /** Closes 'this' if it has been opened.
   */
    public void Close() throws SQLException {
        if (_SqlConnection != null) {
            _SqlConnection.close();
            _SqlConnection = null;
        }
    }

    /** Returns in 'Result' the IDs of 'NpcEncodedNames', at the same index (the vectors are synchronized).
   *  If any name is not a valid NPC then sets the corresponding entry to 'null'.
   *  If several items match a given name then sets the corresponding entry to '-1'.
   */
    public Vector<Long> QueryNpcIdsBySortedEncodedNames(Vector<String> NpcEncodedNames) throws Exception {
        assert IsOpened();
        Vector<Long> Result;
        Statement SqlRequest;
        ResultSet SqlResult;
        long NpcId, IdCount;
        String PreviousNpcEncodedName, NpcEncodedName;
        int j, jEnd;
        Result = new Vector<Long>(NpcEncodedNames.size());
        SqlRequest = _SqlConnection.createStatement();
        SqlResult = SqlRequest.executeQuery("SELECT npc." + Column_NpcTypes_Id + ", npc." + Column_NpcTypes_Name + ", npccnt.Count" + " FROM " + Table_NpcTypes + " npc" + ", ( SELECT " + Column_NpcTypes_Name + " Name" + ", COUNT(" + Column_NpcTypes_Id + ") Count" + " FROM " + Table_NpcTypes + " WHERE " + SqlUtils.WhereOrColumnEqualsOneOfStrings("Name", NpcEncodedNames) + " GROUP BY " + Column_NpcTypes_Name + ") npccnt" + " WHERE (" + SqlUtils.WhereOrColumnEqualsOneOfStrings("npc." + Column_NpcTypes_Name, NpcEncodedNames) + ") AND npc." + Column_NpcTypes_Name + "=npccnt.Name" + " ORDER BY npc." + Column_NpcTypes_Name);
        PreviousNpcEncodedName = null;
        for (j = 1, jEnd = NpcEncodedNames.size(); SqlResult.next(); ) {
            NpcId = SqlResult.getLong(1);
            NpcEncodedName = SqlResult.getString(2);
            IdCount = SqlResult.getLong(3);
            if (PreviousNpcEncodedName != null && PreviousNpcEncodedName.equals(NpcEncodedName)) ; else {
                PreviousNpcEncodedName = NpcEncodedName;
                for (; j <= jEnd && ((NpcEncodedName != null && NpcEncodedNames.elementAt(j - 1) == null) || !NpcEncodedName.equals(NpcEncodedNames.elementAt(j - 1))); j++) Result.addElement(null);
                if (IdCount == 1) for (; j <= jEnd && ((NpcEncodedName == null && NpcEncodedNames.elementAt(j - 1) == null) || NpcEncodedName.equals(NpcEncodedNames.elementAt(j - 1))); j++) Result.addElement(NpcId); else for (; j <= jEnd && ((NpcEncodedName == null && NpcEncodedNames.elementAt(j - 1) == null) || NpcEncodedName.equals(NpcEncodedNames.elementAt(j - 1))); j++) Result.addElement(new Long(-1));
            }
        }
        for (; j <= jEnd; j++) Result.addElement(null);
        SqlRequest.close();
        return Result;
    }

    /** Returns in 'Result' the IDs of 'ItemNames', at the same index (the vectors are synchronized).
   *  If any name is not a valid item then sets the corresponding entry to 'null'.
   *  If several items match a given name then sets the corresponding entry to '-1'.
   */
    public Vector<Long> QueryItemIdsBySortedNames(Vector<String> ItemNames) throws Exception {
        assert IsOpened();
        Vector<Long> Result;
        Statement SqlRequest;
        ResultSet SqlResult;
        long ItemId, IdCount;
        String ItemName, PreviousItemName;
        int j, jEnd;
        Result = new Vector<Long>(ItemNames.size());
        SqlRequest = _SqlConnection.createStatement();
        SqlResult = SqlRequest.executeQuery("SELECT itm." + Column_Items_Id + ", itm." + Column_Items_Name + ", itmcnt.Count" + " FROM " + Table_Items + " itm" + ", ( SELECT " + Column_Items_Name + " Name" + ", COUNT(" + Column_Items_Id + ") Count" + " FROM " + Table_Items + " WHERE " + SqlUtils.WhereOrColumnEqualsOneOfStrings("Name", ItemNames) + " GROUP BY " + Column_Items_Name + ") itmcnt" + " WHERE (" + SqlUtils.WhereOrColumnEqualsOneOfStrings("itm." + Column_Items_Name, ItemNames) + ") AND itm." + Column_Items_Name + "=itmcnt.Name" + " ORDER BY itm." + Column_Items_Name);
        PreviousItemName = null;
        for (j = 1, jEnd = ItemNames.size(); SqlResult.next(); ) {
            ItemId = SqlResult.getLong(1);
            ItemName = SqlResult.getString(2);
            IdCount = SqlResult.getLong(3);
            if (PreviousItemName != null && PreviousItemName.equals(ItemName)) ; else {
                PreviousItemName = ItemName;
                for (; j <= jEnd && ((ItemName != null && ItemNames.elementAt(j - 1) == null) || !ItemName.equals(ItemNames.elementAt(j - 1))); j++) Result.addElement(null);
                if (IdCount == 1) for (; j <= jEnd && ((ItemName == null && ItemNames.elementAt(j - 1) == null) || ItemName.equals(ItemNames.elementAt(j - 1))); j++) Result.addElement(ItemId); else for (; j <= jEnd && ((ItemName == null && ItemNames.elementAt(j - 1) == null) || ItemName.equals(ItemNames.elementAt(j - 1))); j++) Result.addElement(new Long(-1));
            }
        }
        for (; j <= jEnd; j++) Result.addElement(null);
        SqlRequest.close();
        return Result;
    }

    /** Returns in 'Result' the IDs of 'FactionNames', at the same index (the vectors are synchronized).
   *  If any name is not a valid faction then sets the corresponding entry to 'null'.
   */
    public Vector<Long> QueryFactionIdsBySortedNames(Vector<String> FactionNames) throws Exception {
        assert IsOpened();
        Vector<Long> Result;
        Statement SqlRequest;
        ResultSet SqlResult;
        long FactionId;
        String FactionName;
        int j, jEnd;
        Result = new Vector<Long>(FactionNames.size());
        SqlRequest = _SqlConnection.createStatement();
        SqlResult = SqlRequest.executeQuery("SELECT " + Column_FactionList_Id + ", " + Column_FactionList_Name + " FROM " + Table_FactionList + " WHERE " + SqlUtils.WhereOrColumnEqualsOneOfStrings(Column_FactionList_Name, FactionNames) + " ORDER BY " + Column_FactionList_Name);
        for (j = 1, jEnd = FactionNames.size(); SqlResult.next(); ) {
            FactionId = SqlResult.getLong(1);
            FactionName = SqlResult.getString(2);
            for (; j <= jEnd && ((FactionName != null && FactionNames.elementAt(j - 1) == null) || !FactionName.equals(FactionNames.elementAt(j - 1))); j++) Result.addElement(null);
            for (; j <= jEnd && ((FactionName == null && FactionNames.elementAt(j - 1) == null) || FactionName.equals(FactionNames.elementAt(j - 1))); j++) Result.addElement(FactionId);
        }
        for (; j <= jEnd; j++) Result.addElement(null);
        SqlRequest.close();
        return Result;
    }

    /** Returns in 'Result' the zone names where the NPCs with IDs 'NpcIds' spawn, at the same index (the vectors are synchronized).
   *  If any NPC does not spawn then sets the corresponding entry to 'null'.
   */
    public Vector<Vector<String>> QueryNpcZoneSpawnsBySortedIds(Vector<Long> NpcIds) throws Exception {
        assert IsOpened();
        Vector<Vector<String>> Result;
        Statement SqlRequest;
        ResultSet SqlResult;
        long NpcId;
        String ZoneName;
        Vector<String> ZoneNames;
        boolean LastZonesFound;
        int j, jEnd;
        Result = new Vector<Vector<String>>(NpcIds.size());
        SqlRequest = _SqlConnection.createStatement();
        SqlResult = SqlRequest.executeQuery("SELECT spwe." + Column_SpawnEntry_NpcId + ", spwn." + Column_Spawn2_Zone + " FROM " + Table_SpawnEntry + " spwe" + ", " + Table_Spawn2 + " spwn" + " WHERE (" + SqlUtils.WhereOrColumnEqualsOneOfLongs("spwe." + Column_SpawnEntry_NpcId, NpcIds) + ") AND spwn." + Column_Spawn2_SpawnGroupId + "=spwe." + Column_SpawnEntry_SpawnGroupId + " ORDER BY spwe." + Column_SpawnEntry_NpcId + ", spwn." + Column_Spawn2_Zone);
        if (SqlResult.next()) {
            LastZonesFound = false;
            for (j = 1, jEnd = NpcIds.size(); ; ) {
                ZoneNames = new Vector<String>();
                NpcId = SqlResult.getLong(1);
                for (; SqlResult.getLong(1) == NpcId; ) {
                    if (ZoneNames.isEmpty() || !SqlResult.getString(2).equals(ZoneNames.elementAt(ZoneNames.size() - 1))) ZoneNames.addElement(SqlResult.getString(2));
                    if (!SqlResult.next()) {
                        LastZonesFound = true;
                        break;
                    }
                }
                for (; j <= jEnd && NpcIds.elementAt(j - 1) != NpcId; j++) Result.addElement(null);
                for (; j <= jEnd && NpcIds.elementAt(j - 1) == NpcId; j++) Result.addElement(ZoneNames);
                if (LastZonesFound) break;
            }
            for (; j <= jEnd; j++) Result.addElement(null);
        }
        SqlRequest.close();
        return Result;
    }

    public long QueryAccountIdByLogin(String AccountName, String Password) throws Exception {
        long Result;
        Result = SqlUtils.RequestReturning_Long(_SqlConnection, "SELECT " + Column_Account_Id + " FROM " + Table_Account + " WHERE " + Column_Account_Name + "='" + SqlUtils.ToSqlString(AccountName) + "'" + " AND " + Column_Account_Password + "='" + SqlUtils.ToSqlString(Password) + "'");
        return Result;
    }

    public Vector<Map<String, String>> QueryCharactersByAccountId(long AccountId) throws Exception {
        Vector<Map<String, String>> Result;
        Result = SqlUtils.RequestReturning_Rows(_SqlConnection, "SELECT " + Column_Character_Id + ", " + Column_Character_Name + " FROM " + Table_Character + " WHERE " + Column_Character_AccountId + "='" + SqlUtils.ToSqlString(AccountId) + "'" + " ORDER BY " + Column_Character_Name);
        return Result;
    }

    public Map<String, String> QueryCurrentCharacterByAccountId(long AccountId) throws Exception {
        Map<String, String> Result;
        Result = SqlUtils.RequestReturning_Row(_SqlConnection, "SELECT acc." + Column_Account_CharName + ", chr." + Column_Character_Id + " FROM " + Table_Account + " acc, " + Table_Character + " chr" + " WHERE acc." + Column_Account_Id + "='" + SqlUtils.ToSqlString(AccountId) + "'" + " AND chr." + Column_Character_Name + "=acc." + Column_Account_CharName);
        return Result;
    }

    public ProfileBlob QueryCharacterProfileById(long CharacterId) throws Exception {
        ProfileBlob Result;
        Result = new ProfileBlob(SqlUtils.RequestReturning_Blob(_SqlConnection, "SELECT " + Column_Character_Profile + " FROM " + Table_Character + " WHERE " + Column_Character_Id + "='" + CharacterId + "'"));
        return Result;
    }

    public Vector<Map<String, String>> QueryBankInventoryByCharacterId(long CharacterId) throws Exception {
        Vector<Map<String, String>> Result;
        Result = SqlUtils.RequestReturning_Rows(_SqlConnection, "SELECT inv." + Column_Inventory_SlotId + ", inv." + Column_Inventory_ItemId + ", inv." + Column_Inventory_Charges + ", itm." + Column_Items_Name + ", itm." + Column_Items_BagSize + ", itm." + Column_Items_BagSlots + ", itm." + Column_Items_LoreGroup + ", itm." + Column_Items_NoDrop + ", itm." + Column_Items_NoRent + ", itm." + Column_Items_Size + ", itm." + Column_Items_StackSize + " FROM " + Table_Inventory + " inv" + ", " + Table_Items + " itm" + " WHERE inv." + Column_Inventory_CharId + "='" + CharacterId + "'" + " AND inv." + Column_Inventory_ItemId + "=itm." + Column_Items_Id + " AND inv." + Column_Inventory_SlotId + " >= 2000" + " AND inv." + Column_Inventory_SlotId + " <= 2220" + " ORDER BY inv." + Column_Inventory_SlotId);
        return Result;
    }

    public Vector<Map<String, String>> QueryWarehouseInventoryByCharacterId(long AccountId) throws Exception {
        Vector<Map<String, String>> Result;
        Result = SqlUtils.RequestReturning_Rows(_SqlConnection, "SELECT wrh." + Column_Warehouse_ItemId + ", wrh." + Column_Warehouse_Amount + ", itm." + Column_Items_Name + ", itm." + Column_Items_BagSize + ", itm." + Column_Items_BagSlots + ", itm." + Column_Items_LoreGroup + ", itm." + Column_Items_NoDrop + ", itm." + Column_Items_NoRent + ", itm." + Column_Items_Size + ", itm." + Column_Items_StackSize + " FROM " + Table_Warehouse + " wrh" + ", " + Table_Items + " itm" + " WHERE wrh." + Column_Warehouse_AccountId + "='" + AccountId + "'" + " AND wrh." + Column_Warehouse_ItemId + "=itm." + Column_Items_Id + " AND wrh." + Column_Warehouse_ItemId + " <> " + ItemId_Warehouse_Cash + " ORDER BY itm." + Column_Items_Name);
        return Result;
    }

    public Map<String, String> QueryWarehouseCashByCharacterId(long AccountId) throws Exception {
        Map<String, String> Result;
        Result = SqlUtils.RequestReturning_Row(_SqlConnection, "SELECT " + Column_Warehouse_Amount + " FROM " + Table_Warehouse + " WHERE " + Column_Warehouse_AccountId + "='" + AccountId + "'" + " AND " + Column_Warehouse_ItemId + "=" + ItemId_Warehouse_Cash);
        return Result;
    }

    protected String _Host;

    protected String _Database;

    protected String _User;

    protected Connection _SqlConnection;

    protected EqEmuDatabaseConnection() {
    }
}
