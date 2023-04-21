package nickyb.sqleonardo.environment.ctrl.content;

import java.awt.Color;

public class ContentFlag {

    Color bgcolor;

    int block;

    int row;

    int col;

    ContentFlag() {
        this(Color.yellow);
    }

    ContentFlag(Color bgcolor) {
        this.bgcolor = bgcolor;
    }
}
