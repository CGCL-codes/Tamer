package fitbook;

import java.util.Arrays;
import java.util.List;
import fit.Fixture;
import fitlibrary.ArrayFixture;
import fitlibrary.ParamRowFixture;
import fitlibrary.SetFixture;
import fitlibrary.SubsetFixture;

/**
 *
 */
public class StartListing extends fitlibrary.DoFixture {

    private int[] ints;

    public void listIs(int[] ints2) {
        this.ints = ints2;
    }

    public Fixture orderedList() {
        return new ArrayFixture(itemList());
    }

    public Fixture rowList() {
        return new ItemRowFixture();
    }

    public Fixture set() {
        return new SetFixture(itemList());
    }

    public Fixture subset() {
        return new SubsetFixture(itemList());
    }

    public Fixture paramRowList() {
        return new ParamRowFixture(itemArray(), Item.class);
    }

    private List<Object> itemList() {
        return Arrays.asList(itemArray());
    }

    protected Object[] itemArray() {
        Object[] result = new Object[ints.length];
        for (int i = 0; i < ints.length; i++) result[i] = new Item(ints[i]);
        return result;
    }

    public static class Item {

        public int item;

        public Item(int item) {
            this.item = item;
        }
    }

    public class ItemRowFixture extends fit.RowFixture {

        @Override
        public Object[] query() throws Exception {
            return itemArray();
        }

        @Override
        public Class<?> getTargetClass() {
            return Item.class;
        }
    }
}
