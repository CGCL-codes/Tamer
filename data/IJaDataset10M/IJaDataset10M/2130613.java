package jtokyotyrant.protocol;

import java.util.ArrayList;
import java.util.List;
import jtokyotyrant.RDB;

public class Misc extends Command<Object> {

    private String funcName;

    private int opts;

    ;

    private byte[][] argsByteArray;

    private String[] argsString;

    enum ArgumentType {

        TYPE1, TYPE2, TYPE3, TYPE4
    }

    ;

    private ArgumentType type;

    public Misc(RDB rdb, String funcName, byte[][] args) {
        super(rdb);
        this.funcName = funcName;
        this.argsByteArray = args;
        this.type = ArgumentType.TYPE1;
    }

    public Misc(RDB rdb, String funcName, int opts, byte[][] args) {
        super(rdb);
        this.funcName = funcName;
        this.argsByteArray = args;
        this.opts = opts;
        this.type = ArgumentType.TYPE2;
    }

    public Misc(RDB rdb, String funcName, String[] args) {
        super(rdb);
        this.funcName = funcName;
        this.argsString = args;
        this.type = ArgumentType.TYPE3;
    }

    public Misc(RDB rdb, String funcName, int opts, String[] args) {
        super(rdb);
        this.funcName = funcName;
        this.argsString = args;
        this.opts = opts;
        this.type = ArgumentType.TYPE4;
    }

    public Object call() throws Exception {
        switch(type) {
            case TYPE1:
                return rdb.misc(funcName, argsByteArray);
            case TYPE2:
                return rdb.misc(funcName, opts, argsByteArray);
            case TYPE3:
                return rdb.misc(funcName, argsString);
            case TYPE4:
                return rdb.misc(funcName, opts, argsString);
        }
        return null;
    }
}
