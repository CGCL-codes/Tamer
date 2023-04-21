package org.activision.util.rs2cache;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.activision.util.CacheConstants;

public class Cache {

    private static RandomAccessFile ItemDefinitionFile2;

    public Cache() {
        try {
            CacheManager.load(CacheConstants.PATH);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        LoadItemsEquipId();
    }

    private static final void LoadItemsEquipId() {
        try {
            setItemDefinitionFile2(new RandomAccessFile(CacheConstants.ItemDefinitionsPart2, "rw"));
            if (ItemDefinitionFile2.length() == 0) ItemDefinitions.packItemPart2();
            int equipID = 0;
            for (int itemID = 0; itemID < getAmountOfItems(); itemID++) {
                ItemDefinitions item = ItemDefinitions.forID(itemID);
                if (item == null) continue;
                if (item.maleEquip1 >= 0 || item.maleEquip2 >= 0) item.equipId = equipID++;
            }
            getItemDefinitionFile2().close();
            setItemDefinitionFile2(null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static final void setItemDefinitionFile2(RandomAccessFile itemDefinitionFile2) {
        ItemDefinitionFile2 = itemDefinitionFile2;
    }

    public static final RandomAccessFile getItemDefinitionFile2() {
        return ItemDefinitionFile2;
    }

    public static final short getAmountOfItems() {
        return (short) CacheManager.cacheCFCount(CacheConstants.ITEMDEF_IDX_ID);
    }

    public static final int getAmountOfObjects() {
        return CacheManager.cacheCFCount(CacheConstants.OBJECTDEF_IDX_ID);
    }

    public static final short getAmountOfInterfaces() {
        return (short) CacheManager.containerCount(CacheConstants.INTERFACEDEF_IDX_ID);
    }

    public static final short getAmountOfNpcs() {
        return (short) CacheManager.cacheCFCount(CacheConstants.NPCDEF_IDX_ID);
    }
}
