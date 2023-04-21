package org.uithin.nodes.ui.widgets;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.uithin.nodes.ui.UINode;
import org.uithin.nodes.unit.Unit;
import org.uithin.toolkits.UIBuilder;
import zz.utils.SimpleAction;
import static org.uithin.nodes.TypeDescriptor.*;

public class ULabel extends UINode {

    public final Property<String> pText = prop("text", "Label's text", STRING, "label");
}
