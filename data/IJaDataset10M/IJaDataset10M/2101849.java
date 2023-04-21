package wb;

import static wb.Wbsys.*;
import static wb.Blink.*;
import static wb.Segs.*;
import static wb.Handle.*;
import static wb.Ents.*;
import static wb.Wbdefs.*;
import static wb.BooleanMethods.*;
import static wb.Han.*;

public class Db {

    public static boolean wb_Err_P(int x) {
        return 0 > x && x >= maxerr;
    }

    public static int flushEnts(int trynum, int flushnum) {
        return entsFlush(trynum, flushnum);
    }

    public static Han createBt(Seg seg, int typ, int wcb) {
        Han aHan = hanMakeHan();
        int ans = btCreate(seg, typ, aHan, wcb);
        if (err_P(ans)) return null;
        return aHan;
    }

    public static Han openBt(Seg seg, int blknum, int wcb) {
        Han aHan = hanMakeHan();
        int ans = btOpen(seg, blknum, aHan, wcb);
        if (err_P(ans)) return null;
        return aHan;
    }

    public static void closeBt(Han han) {
        btClose(han);
    }

    public static Han createDb(Seg seg, char typ, String nameStr) {
        return createDb(seg, Character.getNumericValue(typ), nameStr);
    }

    public static Han createDb(Seg seg, int typ, String nameStr) {
        byte[] tmpStr = new byte[0x100];
        Han aHan = createBt(seg, typ, 0);
        Han dHan = openBt(seg, 1, (wcbSap + wcbSar));
        if (!a2b(aHan) && !a2b(dHan)) return null; else {
            long2str(tmpStr, 1, han_Id(aHan));
            tmpStr[0] = 4;
            btPut(dHan, stringToBytes(nameStr), strlen(nameStr), tmpStr, 5);
            closeBt(dHan);
            return aHan;
        }
    }

    public static Han openDb(Seg seg, String nameStr) {
        byte[] tmpStr = new byte[0x100];
        Han dHan = openBt(seg, 1, (wcbSap + wcbSar));
        if (dHan == null) return null; else {
            int tlen = btGet(dHan, stringToBytes(nameStr), nameStr.length(), tmpStr);
            closeBt(dHan);
            if (err_P(tlen)) return null;
            if (tlen == 5) return openBt(seg, str2long(tmpStr, 1), 0);
            return null;
        }
    }

    public static boolean bt_Del(Han han, byte[] keyStr) {
        return success_P(btRem(han, keyStr, strlen(keyStr), null));
    }

    public static int bt_Delete(Han han, byte[] keyStr, byte[] key2Str) {
        byte[] tmpstr = new byte[0x100];
        substringMove(keyStr, 0, strlen(keyStr), tmpstr, 0);
        return btRemRange(han, tmpstr, !a2b(strlen(keyStr)) ? startOfChain : strlen(keyStr), key2Str, !a2b(strlen(key2Str)) ? endOfChain : strlen(key2Str));
    }

    public static byte[] bt_Rem(Han han, byte[] keyStr) {
        byte[] tmpStr = new byte[0x100];
        int tlen = btRem(han, keyStr, strlen(keyStr), tmpStr);
        if (err_P(tlen)) return null; else return subArray(tmpStr, tlen);
    }

    public static void bt_Put(Han han, byte[] keyStr, byte[] valStr) {
        btPut(han, keyStr, strlen(keyStr), valStr, strlen(valStr));
    }

    public static boolean bt_Insert(Han han, byte[] keyStr, byte[] valStr) {
        return success_P(btWrite(han, keyStr, strlen(keyStr), valStr, strlen(valStr)));
    }

    public static byte[] bt_Get(Han han, byte[] keyStr) {
        byte[] tmpStr = new byte[0x100];
        int tlen = btGet(han, keyStr, strlen(keyStr), tmpStr);
        if (err_P(tlen)) return null; else return subArray(tmpStr, tlen);
    }

    public static byte[] bt_Next(Han han, byte[] keyStr) {
        byte[] tmpStr = new byte[0x100];
        int tlen = (a2b(keyStr) && strlen(keyStr) > 0) ? btNext(han, keyStr, strlen(keyStr), tmpStr) : btNext(han, noByts, startOfChain, tmpStr);
        if (err_P(tlen)) return null; else return subArray(tmpStr, tlen);
    }

    public static byte[] bt_Prev(Han han, byte[] keyStr) {
        byte[] tmpStr = new byte[0x100];
        int tlen = (a2b(keyStr) && strlen(keyStr) > 0) ? btPrev(han, keyStr, strlen(keyStr), tmpStr) : btPrev(han, noByts, endOfChain, tmpStr);
        if (err_P(tlen)) return null; else return subArray(tmpStr, tlen);
    }

    public static boolean bt_Del(Han han, String keyStr) {
        return bt_Del(han, stringToBytes(keyStr));
    }

    public static void bt_Delete(Han han, String keyStr, String key2Str) {
        bt_Delete(han, stringToBytes(keyStr), stringToBytes(key2Str));
    }

    public static String bt_Get(Han han, String keyStr) {
        byte[] byts = bt_Get(han, stringToBytes(keyStr));
        return bytesToString(byts);
    }

    public static String bt_Next(Han han, String keyStr) {
        byte[] byts = bt_Next(han, stringToBytes(keyStr));
        return bytesToString(byts);
    }

    public static String bt_Prev(Han han, String keyStr) {
        byte[] byts = bt_Prev(han, stringToBytes(keyStr));
        return bytesToString(byts);
    }

    public static void bt_Put(Han han, String keyStr, String key2Str) {
        bt_Put(han, stringToBytes(keyStr), stringToBytes(key2Str));
    }

    public static String bt_Rem(Han han, String keyStr) {
        byte[] byts = bt_Rem(han, stringToBytes(keyStr));
        return bytesToString(byts);
    }
}
