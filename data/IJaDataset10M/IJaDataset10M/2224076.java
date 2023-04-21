package jm.lib.dao;

/**
 * @author JimingLiu
 *
 */
public interface DaoConstants {

    int UNKNOWN_USER_ID = -1;

    byte STATUS_ENABLED = 0;

    byte STATUS_DISABLED = 91;

    byte STATUS_DELETED = 101;

    byte OP_INSERT = 1;

    byte OP_UPDATE = 2;

    byte OP_DELETE = 3;

    byte OP_DELETE_SET_FLAG = 4;

    byte OP_SELECT = 101;
}
