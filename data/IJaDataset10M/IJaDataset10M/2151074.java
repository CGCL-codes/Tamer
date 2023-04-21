package client;

public class ClientGameCommands {

    public static class LocalOperationTimeout extends Exception {

        public LocalOperationTimeout(String p) {
            super(p);
        }
    }

    ;

    public static interface Character {

        public abstract void CharMoveTo(Types.WorldCord w);

        public abstract void CharSpeech(String buf);
    }

    ;

    public static interface ObjectUsage {

        public abstract String UseItem(int itemserial);

        public abstract String UseItem(int itemserialtouse, int itemserialonuse);

        public abstract String UseItem(int itemserialtouse, Types.WorldCord w, ItemType it);

        public abstract int UseItemGump(int itemserialtouse, Types.UnsignedList itemserialtoselect, Types.ShortList itemslist);

        public abstract void MoveItemsG2G(int containerserialfrom, ItemType it, int containerserialto);

        public abstract void MoveItemsG2G(int containerserialfrom, ItemType it, int count, int containerserialto);

        public abstract int MoveItem(int itemwhat, int containerserialto, int count);

        public abstract int SelectItem(int containerserialwhere);

        public abstract int SelectItem(int containerserialwhere, ItemType it);

        public abstract int SelectItem(int containerserialfrom, ItemType it, int count);
    }

    ;

    public static interface Particle {

        public abstract int StatsGetHits();

        public abstract int StatsGetMana();

        public abstract int StatsGetStam();

        public abstract int StatsGetWeight();
    }

    ;

    public static interface Control extends Particle, ObjectUsage, Character {

        ;
    }

    ;
}
