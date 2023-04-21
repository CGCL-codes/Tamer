package blanco.commons.sql.format;

/**
 * blancoSqlFormatter�̃g�[�N���̃^�C�v�Ɋւ���萔�ł��B
 */
public class BlancoSqlTokenConstants {

    /**
     * ���ڔԍ�:1<br>
     * �󕶎�. TAB,CR�����P�̕�����Ƃ��Ċ܂ށB
     */
    public static final int SPACE = 0;

    /**
     * ���ڔԍ�:2<br>
     * �L��. " <="�̂悤�ȂQ�łP�̋L��������B
     */
    public static final int SYMBOL = 1;

    /**
     * ���ڔԍ�:3<br>
     * �L�[���[�h. "SELECT", "ORDER"�Ȃ�.
     */
    public static final int KEYWORD = 2;

    /**
     * ���ڔԍ�:4<br>
     * ���O. �e�[�u�����A�񖼂ȂǁB�_�u���N�H�[�e�[�V�������t���ꍇ������B
     */
    public static final int NAME = 3;

    /**
     * ���ڔԍ�:5<br>
     * �l. ���l�i�����A�����j�A������ȂǁB
     */
    public static final int VALUE = 4;

    /**
     * ���ڔԍ�:6<br>
     * �R�����g. �V���O�����C���R�����g�ƃ}���`���C���R�����g������B
     */
    public static final int COMMENT = 5;

    /**
     * ���ڔԍ�:7<br>
     * SQL���̏I���.
     */
    public static final int END = 6;

    /**
     * ���ڔԍ�:8<br>
     * ��͕s�\�ȃg�[�N��. �ʏ��SQL�ł͂��肦�Ȃ��B
     */
    public static final int UNKNOWN = 7;
}
