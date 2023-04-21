package objectwiz;

import com.javafx.preview.control.CustomMenuItem;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class GroupedItems extends CustomMenuItem implements EventHandler<MouseEvent> {

    Image img_plus = new Image("zoom_+_.png");

    ImageView view_plus = new ImageView();

    Image img_moins = new Image("zoom_-_.png");

    ImageView view_moins = new ImageView();

    Image img_click = new Image("clic.png");

    ImageView view_click = new ImageView();

    Image img_move = new Image("move.png");

    ImageView view_move = new ImageView();

    Image img_1 = new Image("Appli_1.png");

    ImageView view_1 = new ImageView();

    Image img_2 = new Image("Appli_2.png");

    ImageView view_2 = new ImageView();

    Image img_3 = new Image("Appli_3.png");

    ImageView view_3 = new ImageView();

    Image img_4 = new Image("Appli_4.png");

    ImageView view_4 = new ImageView();

    Image img_5 = new Image("Appli_5.png");

    ImageView view_5 = new ImageView();

    final int zoom = Main.map.getZoom();

    int n = zoom;

    public GroupedItems() {
    }

    public Group createItems() {
        Group gr = new Group();
        view_plus.setX(5);
        view_plus.setY(30);
        view_plus.setImage(img_plus);
        view_plus.setOnMouseClicked(this);
        view_moins.setX(55);
        view_moins.setY(30);
        view_moins.setImage(img_moins);
        view_moins.setOnMouseClicked(this);
        view_click.setX(105);
        view_click.setY(30);
        view_click.setImage(img_click);
        view_move.setX(155);
        view_move.setY(30);
        view_move.setImage(img_move);
        view_1.setX(5);
        view_1.setY(400);
        view_1.setImage(img_1);
        view_2.setX(55);
        view_2.setY(400);
        view_2.setImage(img_2);
        view_3.setX(105);
        view_3.setY(400);
        view_3.setImage(img_3);
        view_4.setX(155);
        view_4.setY(400);
        view_4.setImage(img_4);
        view_5.setX(5);
        view_5.setY(460);
        view_5.setImage(img_5);
        gr.getChildren().addAll(view_plus, view_moins, view_click, view_move, view_1, view_2, view_3, view_4, view_5);
        return gr;
    }

    public void handle(MouseEvent t) {
        if (t.getTarget() == view_plus) {
            n = --n;
            Main.map.setZoom(n);
        }
        if (t.getTarget() == view_moins) {
            n = ++n;
            Main.map.setZoom(n);
        }
    }
}
