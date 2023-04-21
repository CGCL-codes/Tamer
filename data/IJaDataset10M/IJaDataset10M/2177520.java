package vavi.awt.event;

import java.util.EventListener;

/**
 * Selection ���X�i�[�C���^�[�t�F�[�X�ł��D
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020603 nsano initial version <br>
 */
public interface SelectionListener extends EventListener {

    /**
     * ������ύX���܂��D
     */
    void valueChanged(SelectionEvent ev);
}
