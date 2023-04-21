package cz.fi.muni.xkremser.editor.shared.event;

import com.gwtplatform.dispatch.annotation.GenEvent;
import com.gwtplatform.dispatch.annotation.Order;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * The Class LoadStructure.
 */
@GenEvent
@SuppressWarnings("unused")
public class LoadStructure {

    @Order(1)
    private TreeNode[] tree;

    @Order(2)
    private Record[] pages;
}
