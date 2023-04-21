package ants.p2p.query;

public class QueryStringItem extends QueryItem {

    String item;

    boolean content;

    public QueryStringItem(QueryNode father, String item, boolean content) {
        super(father);
        this.item = item;
        this.content = content;
    }
}
