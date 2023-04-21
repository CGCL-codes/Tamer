package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegularItem extends Item {

    private String effect;

    private String statEffect;

    public RegularItem(Node n) {
        super(n);
        effect = n.contentOf("effect");
        statEffect = n.contentOf("stat");
    }

    public void use() {
    }
}
