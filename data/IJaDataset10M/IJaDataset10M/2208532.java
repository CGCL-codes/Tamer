package matuya.db.sort;

import java.io.Serializable;
import java.util.Comparator;

public class SoIntComparator implements Comparator, Serializable {

    /** �t���ɂ���Ƃ���-1�B  */
    private int reverse_ = 1;

    public SoIntComparator() {
        this(false);
    }

    /**
	 * 
	 * @param _reverse �t���̏ꍇ��true���w�肷��B
	 */
    public SoIntComparator(boolean _reverse) {
        reverse_ = (_reverse ? -1 : 1);
    }

    public SoIntComparator(int _rev) {
        reverse_ = (_rev < 0 ? -1 : 1);
    }

    /**
	 * �t���Ȃ�true��Ԃ��B
	 * @return
	 */
    public boolean isReverse() {
        return (reverse_ == -1);
    }

    /**
	 * _arg0 <_arg1 : -1 (�t���Ȃ�1)
	 * _arg0==_arg1 :  0
	 * _arg0 >_arg1 :  1 (�t���Ȃ�-1)
	 */
    public int compare(Object _arg0, Object _arg1) {
        return compare((Integer) _arg0, (Integer) _arg1);
    }

    /**
	 * _arg0 <_arg1 : -1 (�t���Ȃ�1)
	 * _arg0==_arg1 :  0
	 * _arg0 >_arg1 :  1 (�t���Ȃ�-1)
	 */
    public int compare(Integer _arg0, Integer _arg1) {
        return compare(_arg0.intValue(), _arg1.intValue());
    }

    /**
	 * _arg0 <_arg1 : -1 (�t���Ȃ�1)
	 * _arg0==_arg1 :  0
	 * _arg0 >_arg1 :  1 (�t���Ȃ�-1)
	 * 
	 * @param _arg0
	 * @param _arg1
	 * @return
	 */
    public int compare(int _arg0, int _arg1) {
        if (_arg0 < _arg1) {
            return (reverse_) * (-1);
        } else if (_arg0 == _arg1) {
            return 0;
        } else {
            return (reverse_) * 1;
        }
    }
}
