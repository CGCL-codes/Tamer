package fuku.eb4j.hook;

import net.cloudhunter.compat.java.lang.StringBuilder;
import net.cloudhunter.compat.org.apache.commons.lang.StringUtils;
import fuku.eb4j.SubAppendix;
import fuku.eb4j.SubBook;
import fuku.eb4j.EBException;
import fuku.eb4j.util.ByteUtil;
import fuku.eb4j.util.HexUtil;

/**
 * デフォルトエスケープシーケンス加工クラス。
 *
 * @author Hisaya FUKUMOTO
 */
public class DefaultHook extends HookAdapter {

    /** 最大入力行数 */
    private int _maxLine = 500;

    /** 半角表示が開始されているかどうかを示すフラグ */
    private boolean _narrow = false;

    /** 行数 */
    private int _line = 0;

    /** 文字列バッファ */
    private StringBuilder _buf = new StringBuilder(2048);

    /** 付録パケージ */
    private SubAppendix _appendix = null;

    /**
     * コンストラクタ。
     *
     * @param sub 副本
     */
    public DefaultHook(SubBook sub) {
        this(sub, 500);
    }

    /**
     * コンストラクタ。
     *
     * @param sub 副本
     * @param maxLine 最大読み込み行数
     */
    public DefaultHook(SubBook sub, int maxLine) {
        super();
        _appendix = sub.getSubAppendix();
        _maxLine = maxLine;
    }

    public void clear() {
        _buf.delete(0, _buf.length());
        _narrow = false;
        _line = 0;
    }

    public Object getObject() {
        return _buf.toString();
    }

    public boolean isMoreInput() {
        if (_line >= _maxLine) {
            return false;
        }
        return true;
    }

    public void append(String str) {
        if (_narrow) {
            str = ByteUtil.wideToNarrow(str);
        }
        _buf.append(str);
    }

    public void append(int code) {
        String str = null;
        if (_narrow) {
            if (_appendix != null) {
                try {
                    str = _appendix.getNarrowFontAlt(code);
                } catch (EBException e) {
                }
            }
            if (StringUtils.isBlank(str)) {
                str = "[GAIJI=n" + HexUtil.toHexString(code) + "]";
            }
        } else {
            if (_appendix != null) {
                try {
                    str = _appendix.getWideFontAlt(code);
                } catch (EBException e) {
                }
            }
            if (StringUtils.isBlank(str)) {
                str = "[GAIJI=w" + HexUtil.toHexString(code) + "]";
            }
        }
        _buf.append(str);
    }

    public void beginNarrow() {
        _narrow = true;
    }

    public void endNarrow() {
        _narrow = false;
    }

    public void newLine() {
        _buf.append('\n');
        _line++;
    }
}
