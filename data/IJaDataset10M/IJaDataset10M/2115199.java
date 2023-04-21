package com.kenstevens.jei.model.index;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.kenstevens.jei.model.Item;
import com.kenstevens.jei.model.Item.ItemType;

@Component
public class ItemIndex implements TableIndex<Integer, Item> {

    private final Map<Integer, Item> index = new HashMap<Integer, Item>();

    private final Map<ItemType, Item> typeIndex = new HashMap<ItemType, Item>();

    private final Map<String, Item> mnemonicIndex = new HashMap<String, Item>();

    public void updateIndex(Item item) {
        index.put(item.getUid(), item);
        typeIndex.put(item.getType(), item);
        mnemonicIndex.put(item.getMnemonic(), item);
    }

    public Item get(Integer uid) {
        return index.get(uid);
    }

    public Item get(ItemType type) {
        return typeIndex.get(type);
    }

    public Item get(String mnemonic) {
        return mnemonicIndex.get(mnemonic);
    }

    public boolean containsMnenomic(String mnemonic) {
        return mnemonicIndex.containsKey(mnemonic);
    }
}
