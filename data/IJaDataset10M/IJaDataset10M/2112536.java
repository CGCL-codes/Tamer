package net.ajaxforms.compiler.tokens;

import java.util.HashSet;
import java.util.Set;

public class Choices extends Expr {

    private Set<Item> items = new HashSet<Item>();

    private String str;

    public Choices(Modifier modifier, String value) {
        super(modifier);
        this.str = value;
        StringBuffer sb = new StringBuffer("[");
        boolean first = true;
        for (String item : value.split(" | ")) {
            this.items.add(new Item(item));
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(item);
        }
        sb.append("]");
    }

    public boolean validate(String namespace, String name) {
        for (Item item : this.items) {
            if (item.validate(namespace, name)) {
                increment();
                return true;
            }
        }
        return false;
    }

    public Expr clone() {
        return new Choices(this.modifier, this.str);
    }

    public String toString() {
        return this.str;
    }
}
