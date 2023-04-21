package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * �O���N���X�ɒ�`����Ă���R���X�g���N�^����ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalConstructorInfo extends ConstructorInfo {

    /**
     * �O���N���X�ɒ�`����Ă���R���X�g���N�^�I�u�W�F�N�g��������
     */
    public ExternalConstructorInfo(final Set<ModifierInfo> modifiers) {
        super(modifiers, getDummyPosition(), getDummyPosition(), getDummyPosition(), getDummyPosition());
    }

    /**
     * �O���N���X�ɒ�`����Ă���R���X�g���N�^�I�u�W�F�N�g��������
     */
    public ExternalConstructorInfo() {
        super(new HashSet<ModifierInfo>(), getDummyPosition(), getDummyPosition(), getDummyPosition(), getDummyPosition());
    }

    /**
     * ���SortedSet��Ԃ�
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
        return Collections.unmodifiableSortedSet(new TreeSet<StatementInfo>());
    }
}
