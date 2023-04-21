package org.keyintegrity.webbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.components.Block;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.util.MultiKey;

public abstract class BeanEditorPool extends BasePage {

    public abstract int getRequestorIndex();

    public abstract void setRequestorIndex(int index);

    private final Map<MultiKey, Block> reservedBlocks = new HashMap<MultiKey, Block>();

    @SuppressWarnings("unchecked")
    private List<Block> getBeanEditorsBlocks() {
        List<Block> result = new ArrayList<Block>();
        for (Map.Entry<String, IComponent> entry : (Set<Map.Entry<String, IComponent>>) getComponents().entrySet()) {
            if (entry.getValue() instanceof Block) {
                result.add((Block) entry.getValue());
            }
        }
        return Collections.unmodifiableList(result);
    }

    private MultiKey getMultiKey(BeanEditor requestor, String key) {
        return new MultiKey(new Object[] { requestor.toString(), key }, false);
    }

    public Block acquireBeanEditorBlock(BeanEditor requestor, String key) {
        MultiKey multiKey = getMultiKey(requestor, key);
        if (!reservedBlocks.containsKey(multiKey)) {
            reservedBlocks.put(multiKey, findUnusedBlock(multiKey));
        }
        return reservedBlocks.get(multiKey);
    }

    private Block findUnusedBlock(MultiKey multiKey) {
        for (Block block : getBeanEditorsBlocks()) {
            if (!reservedBlocks.containsValue(block)) {
                return block;
            }
        }
        throw new ApplicationRuntimeException("BeanEditor Limits Exceeded");
    }
}
