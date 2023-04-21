package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;

/**
 * �C��q�������Ƃ��ł��邱�Ƃ�\���C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface Modifier {

    /**
     * �C��q��Set��Ԃ�
     * 
     * @return �C��q��Set
     */
    Set<ModifierInfo> getModifiers();
}
