package org.gamenet.application.mm8leveleditor.data.mm6;

import java.util.ArrayList;
import java.util.List;
import org.gamenet.application.mm8leveleditor.data.GameVersion;
import org.gamenet.swing.controls.ComparativeTableControl;
import org.gamenet.swing.controls.Vertex3DValueHolder;
import org.gamenet.util.ByteConversions;

public class Item implements Vertex3DValueHolder {

    private static final int ITEM_RECORD_LENGTH_MM6 = 100;

    private static final int ITEM_RECORD_LENGTH_MM7 = 112;

    private static final int RANGE_TYPE_TOUCH = 0;

    private static final int RANGE_TYPE_SHORT = 1;

    private static final int RANGE_TYPE_MEDIUM = 2;

    private static final int RANGE_TYPE_LONG = 3;

    private static final int ATTRIBUTE_TYPE_IDENTIFIED = 0x0001;

    private static final int ATTRIBUTE_TYPE_BROKEN = 0x0002;

    private static final int ATTRIBUTE_TYPE_CURSED = 0x0004;

    private static final int ATTRIBUTE_TYPE_TEMPORARY_POWER = 0x0008;

    private static final int ATTRIBUTE_TYPE_SPECIAL_EFFECT_1 = 0x0010;

    private static final int ATTRIBUTE_TYPE_SPECIAL_EFFECT_2 = 0x0020;

    private static final int ATTRIBUTE_TYPE_SPECIAL_EFFECT_3 = 0x0040;

    private static final int ATTRIBUTE_TYPE_SPECIAL_EFFECT_4 = 0x0080;

    private static final int ATTRIBUTE_TYPE_STOLEN = 0x0100;

    private static final int ATTRIBUTE_TYPE_HARDENED = 0x0200;

    private static final int TYPE_OFFSET = 0;

    private static final int ID_OFFSET = 2;

    private static final int X_OFFSET = 4;

    private static final int Y_OFFSET = 8;

    private static final int Z_OFFSET = 12;

    private static final int VELOCITY_X_OFFSET = 16;

    private static final int VELOCITY_Y_OFFSET = 18;

    private static final int VELOCITY_Z_OFFSET = 20;

    private static final int FACING_OFFSET = 22;

    private static final int SOUND_NUMBER_OFFSET = 24;

    private static final int ATTRIBUTES_OFFSET = 26;

    private static final int ROOM_OFFSET = 28;

    private static final int AGE_OFFSET = 30;

    private static final int MAX_AGE_OFFSET = 32;

    private static final int LIGHT_MULTIPLIER_OFFSET = 34;

    private static final int ITEM_NUMBER_OFFSET = 36;

    private static final int STANDARD_MAGIC_CLASS_OFFSET = 40;

    private static final int VALUE_MODIFIER_OFFSET = 44;

    private static final int SPECIAL_MAGIC_CLASS_OFFSET = 48;

    private static final int AMOUNT_OF_GOLD_OFFSET = 48;

    private static final int CHARGES_OFFSET = 52;

    private static final int ATTRIBUTE_OFFSET = 56;

    private static final int BODY_LOCATION_OFFSET = 60;

    private static final int MAXIMUM_CHARGES_OFFSET = 61;

    private static final int OWNER_OFFSET = 62;

    private static final int PADDING_OFFSET = 63;

    private static final int SPELL_TYPE_OFFSET_MM6 = 64;

    private static final int SPELL_SKILL_LEVEL_OFFSET_MM6 = 68;

    private static final int SPELL_LEVEL_OFFSET_MM6 = 72;

    private static final int SPELL_EFFECT_OFFSET_OFFSET_MM6 = 76;

    private static final int TARGET_OFFSET_MM6 = 80;

    private static final int RANGE_OFFSET_MM6 = 84;

    private static final int ATTACK_TYPE_OFFSET_MM6 = 85;

    private static final int PADDING_OFFSET_MM6 = 86;

    private static final int STARTING_X_OFFSET_MM6 = 88;

    private static final int STARTING_Y_OFFSET_MM6 = 92;

    private static final int STARTING_Z_OFFSET_MM6 = 96;

    private static final int TIME_OFFSET_MM7 = 64;

    private static final int SPELL_TYPE_OFFSET_MM7 = 72;

    private static final int SPELL_SKILL_LEVEL_OFFSET_MM7 = 76;

    private static final int SPELL_LEVEL_OFFSET_MM7 = 80;

    private static final int SPELL_EFFECT_OFFSET_OFFSET_MM7 = 84;

    private static final int OWNER_OFFSET_MM7 = 88;

    private static final int TARGET_OFFSET_MM7 = 92;

    private static final int RANGE_OFFSET_MM7 = 96;

    private static final int ATTACK_TYPE_OFFSET_MM7 = 97;

    private static final int PADDING_OFFSET_MM7 = 98;

    private static final int STARTING_X_OFFSET_MM7 = 100;

    private static final int STARTING_Y_OFFSET_MM7 = 104;

    private static final int STARTING_Z_OFFSET_MM7 = 108;

    private int gameVersion = 0;

    private byte itemData[] = null;

    private int goldAmount = 0;

    private int bodyLocation = 0;

    public Item(int gameVersion) {
        super();
        this.gameVersion = gameVersion;
        if (GameVersion.MM6 == gameVersion) this.itemData = new byte[ITEM_RECORD_LENGTH_MM6]; else this.itemData = new byte[ITEM_RECORD_LENGTH_MM7];
    }

    public Item(int gameVersion, int offset, int itemNumber, int stdMagicClass, int stdMagicBonus, int amountOfGold, int charges, int bodyLocation) {
        this(gameVersion);
        this.setItemNumber(itemNumber);
        this.setStandardMagicClass(stdMagicClass);
        this.setStandardMagicBonus(stdMagicBonus);
        this.setGoldAmount(amountOfGold);
        this.setSpecialMagicClass(amountOfGold);
        this.setCharges(charges);
        this.setBodyLocation(bodyLocation);
    }

    public Item(int gameVersion, int itemNumber, int pictureNumber, int x, int y, int z, int stdMagicClass, int stdMagicBonus, int amountOfGold, int charges) {
        this(gameVersion);
        this.setItemNumber(itemNumber);
        this.setPictureNumber(pictureNumber);
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setStandardMagicClass(stdMagicClass);
        this.setStandardMagicBonus(stdMagicBonus);
        this.setGoldAmount(amountOfGold);
        this.setSpecialMagicClass(amountOfGold);
        this.setCharges(charges);
    }

    public int initialize(byte dataSrc[], int offset) {
        System.arraycopy(dataSrc, offset, this.itemData, 0, this.itemData.length);
        offset += this.itemData.length;
        return offset;
    }

    public static int populateObjects(int gameVersion, byte[] data, int offset, List itemList) {
        int itemCount = ByteConversions.getIntegerInByteArrayAtPosition(data, offset);
        offset += 4;
        for (int itemIndex = 0; itemIndex < itemCount; ++itemIndex) {
            Item item = new Item(gameVersion);
            itemList.add(item);
            offset = item.initialize(data, offset);
        }
        return offset;
    }

    /**
     * @param newData
     * @param offset
     * @param itemArray
     * @return offset
     */
    public static int updateData(byte[] newData, int offset, List itemList) {
        ByteConversions.setIntegerInByteArrayAtPosition(itemList.size(), newData, offset);
        offset += 4;
        for (int itemIndex = 0; itemIndex < itemList.size(); ++itemIndex) {
            Item item = (Item) itemList.get(itemIndex);
            System.arraycopy(item.getItemData(), 0, newData, offset, item.getItemData().length);
            offset += item.getItemData().length;
        }
        return offset;
    }

    public byte[] getItemData() {
        return this.itemData;
    }

    public int getItemNumber() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), ITEM_NUMBER_OFFSET);
    }

    public void setItemNumber(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), ITEM_NUMBER_OFFSET);
    }

    public int getPictureNumber() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), ID_OFFSET);
    }

    public void setPictureNumber(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), ID_OFFSET);
    }

    public int getStandardMagicClass() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), STANDARD_MAGIC_CLASS_OFFSET);
    }

    public void setStandardMagicClass(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), STANDARD_MAGIC_CLASS_OFFSET);
    }

    public int getStandardMagicBonus() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), VALUE_MODIFIER_OFFSET);
    }

    public void setStandardMagicBonus(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), VALUE_MODIFIER_OFFSET);
    }

    public int getSpecialMagicClass() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), SPECIAL_MAGIC_CLASS_OFFSET);
    }

    public void setSpecialMagicClass(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), SPECIAL_MAGIC_CLASS_OFFSET);
    }

    public int getCharges() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), CHARGES_OFFSET);
    }

    public void setCharges(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), CHARGES_OFFSET);
    }

    public int getX() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), X_OFFSET);
    }

    public void setX(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), X_OFFSET);
    }

    public int getY() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), Y_OFFSET);
    }

    public void setY(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), Y_OFFSET);
    }

    public int getZ() {
        return ByteConversions.getIntegerInByteArrayAtPosition(getItemData(), Z_OFFSET);
    }

    public void setZ(int value) {
        ByteConversions.setIntegerInByteArrayAtPosition(value, getItemData(), Z_OFFSET);
    }

    public int getGoldAmount() {
        return this.goldAmount;
    }

    public void setGoldAmount(int goldAmount) {
        this.goldAmount = goldAmount;
    }

    public int getBodyLocation() {
        return this.bodyLocation;
    }

    public void setBodyLocation(int bodyLocation) {
        this.bodyLocation = bodyLocation;
    }

    public static List getOffsetList(int gameVersion) {
        List offsetList = new ArrayList();
        offsetList.add(new ComparativeTableControl.OffsetData(TYPE_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "type"));
        offsetList.add(new ComparativeTableControl.OffsetData(ID_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "ID"));
        offsetList.add(new ComparativeTableControl.OffsetData(X_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "x"));
        offsetList.add(new ComparativeTableControl.OffsetData(Y_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "y"));
        offsetList.add(new ComparativeTableControl.OffsetData(Z_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "z"));
        offsetList.add(new ComparativeTableControl.OffsetData(VELOCITY_X_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "velocity x"));
        offsetList.add(new ComparativeTableControl.OffsetData(VELOCITY_Y_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "velocity y"));
        offsetList.add(new ComparativeTableControl.OffsetData(VELOCITY_Z_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "velocity z"));
        offsetList.add(new ComparativeTableControl.OffsetData(FACING_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "facing"));
        offsetList.add(new ComparativeTableControl.OffsetData(SOUND_NUMBER_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "sound #"));
        offsetList.add(new ComparativeTableControl.OffsetData(ATTRIBUTES_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "attributes"));
        offsetList.add(new ComparativeTableControl.OffsetData(ROOM_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "room"));
        offsetList.add(new ComparativeTableControl.OffsetData(AGE_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "age"));
        offsetList.add(new ComparativeTableControl.OffsetData(MAX_AGE_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "max age"));
        offsetList.add(new ComparativeTableControl.OffsetData(LIGHT_MULTIPLIER_OFFSET, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "light multiplier"));
        offsetList.add(new ComparativeTableControl.OffsetData(ITEM_NUMBER_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "item #"));
        offsetList.add(new ComparativeTableControl.OffsetData(STANDARD_MAGIC_CLASS_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "std magic class"));
        offsetList.add(new ComparativeTableControl.OffsetData(VALUE_MODIFIER_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "value modifier"));
        offsetList.add(new ComparativeTableControl.OffsetData(SPECIAL_MAGIC_CLASS_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "spc magic class/gold"));
        offsetList.add(new ComparativeTableControl.OffsetData(CHARGES_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "charges"));
        offsetList.add(new ComparativeTableControl.OffsetData(ATTRIBUTE_OFFSET, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "attributes"));
        offsetList.add(new ComparativeTableControl.OffsetData(BODY_LOCATION_OFFSET, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "body location"));
        offsetList.add(new ComparativeTableControl.OffsetData(MAXIMUM_CHARGES_OFFSET, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "max charges"));
        offsetList.add(new ComparativeTableControl.OffsetData(OWNER_OFFSET, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "owner"));
        offsetList.add(new ComparativeTableControl.OffsetData(PADDING_OFFSET, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "padding"));
        if (GameVersion.MM6 == gameVersion) {
            offsetList.add(new ComparativeTableControl.OffsetData(SPELL_TYPE_OFFSET_MM6, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "spell type"));
            offsetList.add(new ComparativeTableControl.OffsetData(SPELL_SKILL_LEVEL_OFFSET_MM6, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "spell skill"));
            offsetList.add(new ComparativeTableControl.OffsetData(SPELL_LEVEL_OFFSET_MM6, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "spell level"));
            offsetList.add(new ComparativeTableControl.OffsetData(STARTING_X_OFFSET_MM6, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "starting x"));
            offsetList.add(new ComparativeTableControl.OffsetData(STARTING_Y_OFFSET_MM6, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "starting y"));
            offsetList.add(new ComparativeTableControl.OffsetData(STARTING_Z_OFFSET_MM6, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "starting z"));
            offsetList.add(new ComparativeTableControl.OffsetData(TARGET_OFFSET_MM6, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "target"));
            offsetList.add(new ComparativeTableControl.OffsetData(RANGE_OFFSET_MM6, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "range"));
            offsetList.add(new ComparativeTableControl.OffsetData(ATTACK_TYPE_OFFSET_MM6, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "attack type"));
            offsetList.add(new ComparativeTableControl.OffsetData(PADDING_OFFSET_MM6, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "padding"));
            offsetList.add(new ComparativeTableControl.OffsetData(SPELL_EFFECT_OFFSET_OFFSET_MM6, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "offset"));
        } else {
            offsetList.add(new ComparativeTableControl.OffsetData(TIME_OFFSET_MM7, 8, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "time"));
            offsetList.add(new ComparativeTableControl.OffsetData(SPELL_TYPE_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "spell type"));
            offsetList.add(new ComparativeTableControl.OffsetData(SPELL_SKILL_LEVEL_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "spell skill"));
            offsetList.add(new ComparativeTableControl.OffsetData(SPELL_LEVEL_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "spell level"));
            offsetList.add(new ComparativeTableControl.OffsetData(OWNER_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "owner"));
            offsetList.add(new ComparativeTableControl.OffsetData(TARGET_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "target"));
            offsetList.add(new ComparativeTableControl.OffsetData(RANGE_OFFSET_MM7, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "range"));
            offsetList.add(new ComparativeTableControl.OffsetData(ATTACK_TYPE_OFFSET_MM7, 1, ComparativeTableControl.REPRESENTATION_BYTE_DEC, "attack type"));
            offsetList.add(new ComparativeTableControl.OffsetData(STARTING_X_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "starting x"));
            offsetList.add(new ComparativeTableControl.OffsetData(STARTING_Y_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "starting y"));
            offsetList.add(new ComparativeTableControl.OffsetData(STARTING_Z_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "starting z"));
            offsetList.add(new ComparativeTableControl.OffsetData(PADDING_OFFSET_MM7, 2, ComparativeTableControl.REPRESENTATION_SHORT_DEC, "padding"));
            offsetList.add(new ComparativeTableControl.OffsetData(SPELL_EFFECT_OFFSET_OFFSET_MM7, 4, ComparativeTableControl.REPRESENTATION_INT_DEC, "offset"));
        }
        return offsetList;
    }

    public static ComparativeTableControl.DataSource getComparativeDataSource(final List itemList) {
        return new ComparativeTableControl.DataSource() {

            public int getRowCount() {
                return itemList.size();
            }

            public byte[] getData(int dataRow) {
                Item item = (Item) itemList.get(dataRow);
                return item.getItemData();
            }

            public int getAdjustedDataRowIndex(int dataRow) {
                return dataRow;
            }

            public String getIdentifier(int dataRow) {
                Item item = (Item) itemList.get(dataRow);
                return String.valueOf(dataRow) + " - id #" + item.getItemNumber();
            }

            public int getOffset(int dataRow) {
                return 0;
            }
        };
    }

    public static int getRecordSize(int gameVersion) {
        if (GameVersion.MM6 == gameVersion) return ITEM_RECORD_LENGTH_MM6; else return ITEM_RECORD_LENGTH_MM7;
    }

    public int getGameVersion() {
        return this.gameVersion;
    }
}
