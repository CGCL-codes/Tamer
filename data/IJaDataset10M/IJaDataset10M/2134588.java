package game;

import java.io.Serializable;

public class treeImage implements Serializable {

    int type;

    int x;

    int y;

    double offsetX;

    double offsetY;

    treeImage(int X, int Y, int T, double oX, double oY) {
        x = X;
        y = Y;
        type = T;
        offsetX = oX;
        offsetY = oY;
    }
}
