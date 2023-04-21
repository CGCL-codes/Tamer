package thinwire.apps.playground;

import java.util.List;
import thinwire.ui.Component;
import thinwire.ui.GridBox;
import thinwire.ui.Image;
import thinwire.ui.Panel;
import thinwire.ui.Tree;
import thinwire.ui.event.DropEvent;
import thinwire.ui.event.DropListener;
import thinwire.ui.layout.TableLayout;

/**
 * @author Joshua J. Gertzen
 */
public class ExampleDragAndDrop extends Example {

    @Override
    Component getContent() {
        Panel container = new Panel();
        container.setLayout(new TableLayout(new double[][] { { 0, 0, 0 }, { 0 } }, 10, 5));
        GridBox gb = (GridBox) new GridBox().setLimit("0, 0");
        Widget[] values = Widget.values();
        List<GridBox.Row> rows = gb.getRows();
        for (int i = 0; i < values.length; i++) {
            rows.add(new GridBox.Row(Main.getSimpleClassName(values[i].getType()) + ".png"));
        }
        container.getChildren().add(gb);
        Tree tree = (Tree) new Tree().setLimit("1, 0");
        final Tree.Item root = tree.getRootItem();
        root.setText("Images");
        root.setImage(Main.RES_PATH + "Folder.png");
        tree.setRootItemVisible(true);
        container.getChildren().add(tree);
        Panel panel = (Panel) new Panel().setLimit("2, 0");
        panel.setLayout(new TableLayout(new double[][] { { 0 }, { 0 } }, 10));
        container.getChildren().add(panel);
        gb.addDropListener(tree, new DropListener() {

            public void dropPerformed(DropEvent ev) {
                Tree.Item item = (Tree.Item) ev.getDragObject();
                GridBox.Row row = new GridBox.Row(item.getText());
                ((GridBox) ev.getSourceComponent()).getRows().add(row);
                root.getChildren().remove(item);
            }
        });
        tree.addDropListener(gb, new DropListener() {

            public void dropPerformed(DropEvent ev) {
                String imgName = (String) ((GridBox.Range) ev.getDragObject()).getCell();
                Tree.Item item = new Tree.Item(imgName, Main.RES_PATH + imgName);
                root.getChildren().add(item);
                ((GridBox) ev.getDragComponent()).getRows().remove(((GridBox.Range) ev.getDragObject()).getRow());
            }
        });
        panel.addDropListener(new Component[] { gb, tree }, new DropListener() {

            public void dropPerformed(DropEvent ev) {
                Image img = null;
                if (ev.getDragObject() instanceof Tree.Item) {
                    Tree.Item item = (Tree.Item) ev.getDragObject();
                    img = new Image(item.getImage());
                } else if (ev.getDragObject() instanceof GridBox.Range) {
                    GridBox.Range range = (GridBox.Range) ev.getDragObject();
                    img = new Image(Main.RES_PATH + range.getCell());
                }
                ((TableLayout) ((Panel) ev.getSourceComponent()).getLayout()).getRows().get(0).set(0, img);
            }
        });
        return container;
    }

    @Override
    String getName() {
        return "Drag and Drop";
    }

    @Override
    String getDescription() {
        return "<b>Drag the <font color='rgb(255,128,0)'>Images</font> between the GridBox, Tree, and Panel</b>.";
    }
}
