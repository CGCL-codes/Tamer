package com.ibm.awb.misc;

/**
 * The <tt>PortPattern</tt> class represents a range of ports permitted to
 * access.
 * 
 * @version 1.00 $Date: 2009/07/28 07:04:53 $
 * @author ONO Kouichi
 */
public class PortPattern {

    private static final int DEFAULT_PORT_NO = -1;

    private static final String DEFAULT_PORT = String.valueOf(DEFAULT_PORT_NO);

    private static final String GREATER_THAN_OR_EQUAL = ">=";

    private static final String LESS_THAN_OR_EQUAL = "<=";

    private static final String GREATER_THAN = ">";

    private static final String LESS_THAN = "<";

    private static final String BETWEEN_PORTS = "-";

    private static final String ANYPORT = "*";

    private static final int TYPE_NOTYPE = 0;

    private static final int TYPE_DEFAULT_PORT = 1;

    private static final int TYPE_A_PORT = 2;

    private static final int TYPE_ANYPORT = 3;

    private static final int TYPE_GREATER_THAN_OR_EQUAL = 4;

    private static final int TYPE_GREATER_THAN = 5;

    private static final int TYPE_LESS_THAN_OR_EQUAL = 6;

    private static final int TYPE_LESS_THAN = 7;

    private static final int TYPE_BETWEEN = 8;

    private static final int NO_PORT = -1;

    private String _pattern = null;

    private int _type = TYPE_NOTYPE;

    private int _portFrom = NO_PORT;

    private int _portTo = NO_PORT;

    public PortPattern() {
        this(null);
    }

    public PortPattern(int port) {
        this._pattern = String.valueOf(port);
        if (port == DEFAULT_PORT_NO) {
            this._type = TYPE_DEFAULT_PORT;
        } else {
            this._type = TYPE_A_PORT;
            this._portFrom = port;
            if (!isValid(this._portFrom)) {
                this._type = TYPE_NOTYPE;
                this._portFrom = NO_PORT;
                throw getException(this._pattern + ". Port number should be positive.");
            }
        }
    }

    public PortPattern(String pattern) {
        if (pattern == null) {
            this._type = TYPE_DEFAULT_PORT;
            return;
        }
        this._pattern = pattern.trim();
        final int ind = this._pattern.indexOf(BETWEEN_PORTS);
        String ptFrom = null;
        String ptTo = null;
        if (ind > 0) {
            ptFrom = this._pattern.substring(0, ind);
            ptTo = this._pattern.substring(ind + 1);
        }
        if (this._pattern.equals(DEFAULT_PORT)) {
            this._type = TYPE_DEFAULT_PORT;
        } else if (this._pattern.equals(ANYPORT)) {
            this._type = TYPE_ANYPORT;
        } else if (this._pattern.startsWith(GREATER_THAN_OR_EQUAL)) {
            this._type = TYPE_GREATER_THAN_OR_EQUAL;
        } else if (this._pattern.startsWith(LESS_THAN_OR_EQUAL)) {
            this._type = TYPE_LESS_THAN_OR_EQUAL;
        } else if (this._pattern.startsWith(GREATER_THAN)) {
            this._type = TYPE_GREATER_THAN;
        } else if (this._pattern.startsWith(LESS_THAN)) {
            this._type = TYPE_LESS_THAN;
        } else {
            if (ind > 0) {
                this._type = TYPE_BETWEEN;
            } else {
                this._type = TYPE_A_PORT;
            }
        }
        String pt = null;
        switch(this._type) {
            case TYPE_NOTYPE:
                break;
            case TYPE_DEFAULT_PORT:
                break;
            case TYPE_A_PORT:
                try {
                    this._portFrom = Integer.parseInt(this._pattern);
                    this._portTo = NO_PORT;
                } catch (NumberFormatException excpt) {
                    this._type = TYPE_NOTYPE;
                    this._portFrom = NO_PORT;
                    throw getException(this._pattern);
                }
                if (!isValid(this._portFrom)) {
                    this._type = TYPE_NOTYPE;
                    this._portFrom = NO_PORT;
                    throw getException(this._pattern + ". Port number should be positive.");
                }
                break;
            case TYPE_ANYPORT:
                break;
            case TYPE_GREATER_THAN_OR_EQUAL:
            case TYPE_GREATER_THAN:
            case TYPE_LESS_THAN_OR_EQUAL:
            case TYPE_LESS_THAN:
                switch(this._type) {
                    case TYPE_GREATER_THAN_OR_EQUAL:
                    case TYPE_LESS_THAN_OR_EQUAL:
                        pt = this._pattern.substring(2).trim();
                        break;
                    case TYPE_GREATER_THAN:
                    case TYPE_LESS_THAN:
                        pt = this._pattern.substring(1).trim();
                        break;
                }
                try {
                    this._portFrom = Integer.parseInt(pt);
                    this._portTo = NO_PORT;
                } catch (NumberFormatException excpt) {
                    this._type = TYPE_NOTYPE;
                    this._portFrom = NO_PORT;
                    this._portTo = NO_PORT;
                }
                if (!isValid(this._portFrom)) {
                    this._type = TYPE_NOTYPE;
                    this._portFrom = NO_PORT;
                    throw getException(pt + ". Port number should be positive.");
                }
                break;
            case TYPE_BETWEEN:
                if ((ptFrom == null) || ptFrom.equals("") || (ptTo == null) || ptTo.equals("")) {
                    throw getException(this._pattern);
                }
                try {
                    this._portFrom = Integer.parseInt(ptFrom);
                } catch (NumberFormatException excpt) {
                    this._type = TYPE_NOTYPE;
                    this._portFrom = NO_PORT;
                    throw getException(ptFrom);
                }
                try {
                    this._portTo = Integer.parseInt(ptTo);
                } catch (NumberFormatException excpt) {
                    this._type = TYPE_NOTYPE;
                    this._portTo = NO_PORT;
                    throw getException(ptTo);
                }
                if (!isValid(this._portFrom)) {
                    this._type = TYPE_NOTYPE;
                    this._portFrom = NO_PORT;
                    throw getException(ptFrom + ". Port number should be positive.");
                }
                if (!isValid(this._portTo)) {
                    this._type = TYPE_NOTYPE;
                    this._portTo = NO_PORT;
                    throw getException(ptTo + ". Port number should be positive.");
                }
                break;
        }
    }

    public boolean equals(PortPattern ppat) {
        if (ppat == null) {
            return false;
        }
        if (ppat._type != this._type) {
            return false;
        }
        boolean eq = false;
        switch(this._type) {
            case TYPE_NOTYPE:
                eq = false;
                break;
            case TYPE_DEFAULT_PORT:
                eq = true;
                break;
            case TYPE_A_PORT:
                eq = ppat._portFrom == this._portFrom;
                break;
            case TYPE_ANYPORT:
                eq = true;
                break;
            case TYPE_GREATER_THAN_OR_EQUAL:
                eq = ppat._portFrom == this._portFrom;
                break;
            case TYPE_GREATER_THAN:
                eq = ppat._portFrom == this._portFrom;
                break;
            case TYPE_LESS_THAN_OR_EQUAL:
                eq = ppat._portFrom == this._portFrom;
                break;
            case TYPE_LESS_THAN:
                eq = ppat._portFrom == this._portFrom;
                break;
            case TYPE_BETWEEN:
                eq = (ppat._portFrom == this._portFrom) && (ppat._portTo == this._portTo);
                break;
        }
        return eq;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PortPattern) {
            PortPattern ppat = (PortPattern) obj;
            return this.equals(ppat);
        }
        return false;
    }

    private static IllegalArgumentException getException(String msg) {
        return new IllegalArgumentException("Illegal port number : " + msg);
    }

    public int getFromPort() {
        return this._portFrom;
    }

    public int getToPort() {
        return this._portTo;
    }

    public boolean isMatch(int port) {
        boolean matched = false;
        switch(this._type) {
            case TYPE_NOTYPE:
                matched = false;
                break;
            case TYPE_DEFAULT_PORT:
                matched = port == DEFAULT_PORT_NO;
                break;
            case TYPE_A_PORT:
                matched = this._portFrom == port;
                break;
            case TYPE_ANYPORT:
                matched = true;
                break;
            case TYPE_GREATER_THAN_OR_EQUAL:
                matched = port >= this._portFrom;
                break;
            case TYPE_GREATER_THAN:
                matched = port > this._portFrom;
                break;
            case TYPE_LESS_THAN_OR_EQUAL:
                matched = port <= this._portFrom;
                break;
            case TYPE_LESS_THAN:
                matched = port < this._portFrom;
                break;
            case TYPE_BETWEEN:
                matched = (this._portFrom <= port) && (port <= this._portTo);
                break;
        }
        return matched;
    }

    public boolean isSinglePort() {
        if ((this._type == TYPE_DEFAULT_PORT) || (this._type == TYPE_A_PORT)) {
            return true;
        }
        return false;
    }

    private static boolean isValid(int port) {
        return port >= 0;
    }

    public static void main(String arg[]) {
        PortPattern ppat = null;
        if (arg.length == 0) {
            ppat = new PortPattern();
        } else {
            ppat = new PortPattern(arg[0]);
        }
        if (ppat != null) {
            System.out.print(ppat.toString());
        }
    }

    @Override
    public String toString() {
        String str = null;
        switch(this._type) {
            case TYPE_NOTYPE:
                break;
            case TYPE_DEFAULT_PORT:
                str = DEFAULT_PORT;
                break;
            case TYPE_A_PORT:
                str = String.valueOf(this._portFrom);
                break;
            case TYPE_ANYPORT:
                str = ANYPORT;
                break;
            case TYPE_GREATER_THAN_OR_EQUAL:
                str = GREATER_THAN_OR_EQUAL + String.valueOf(this._portFrom);
                break;
            case TYPE_GREATER_THAN:
                str = GREATER_THAN + String.valueOf(this._portFrom);
                break;
            case TYPE_LESS_THAN_OR_EQUAL:
                str = LESS_THAN_OR_EQUAL + String.valueOf(this._portFrom);
                break;
            case TYPE_LESS_THAN:
                str = LESS_THAN_OR_EQUAL + String.valueOf(this._portFrom);
                break;
            case TYPE_BETWEEN:
                str = String.valueOf(this._portFrom) + BETWEEN_PORTS + String.valueOf(this._portTo);
                break;
        }
        return str;
    }
}
