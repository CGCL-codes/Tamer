package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * �t�B�[���h�C��C���[�J���ϐ��̋��ʂ̐e�N���X�D �ȉ��̏������D
 * <ul>
 * <li>�ϐ���</li>
 * <li>�^</li>
 * <li>�C��q</li>
 * <li>�ʒu���</li>
 * 
 * @author higo
 * @param <TUnit> ���̕ϐ���錾���Ă��郆�j�b�g 
 */
@SuppressWarnings("serial")
public abstract class VariableInfo<TUnit extends UnitInfo> extends UnitInfo implements Modifier {

    /**
     * �C��q�� Set ��Ԃ�
     * 
     * @return �C��q�� Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �ϐ�����Ԃ�
     * 
     * @return �ϐ���
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �ϐ��̌^��Ԃ�
     * 
     * @return �ϐ��̌^
     */
    public final TypeInfo getType() {
        assert null != this.type : "variable type is not set.";
        return this.type;
    }

    /**
     * �ϐ��̌^��ݒ肷��
     * 
     * @param type �ϐ��̌^
     */
    public final void setType(final TypeInfo type) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new IllegalArgumentException();
        }
        this.type = type;
    }

    /**
     * �ϐ���錾���Ă��郆�j�b�g��Ԃ�
     * 
     * @return �ϐ���錾���Ă��郆�j�b�g
     */
    public final TUnit getDefinitionUnit() {
        return this.definitionUnit;
    }

    /**
     * �ϐ��̃n�b�V���R�[�h��Ԃ�
     */
    @Override
    public final int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * �ϐ����p�̈ꗗ��Ԃ��D
     * �ǂ̕ϐ����p�����Ă��Ȃ��̂ŁC���set���Ԃ����
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * �ϐ���`��Set��Ԃ�
     * 
     * @return �ϐ���`��Set
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(this);
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public final Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * �ϐ��I�u�W�F�N�g��������
     * 
     * @param modifiers �C��q�� Set
     * @param name �ϐ���
     * @param type �ϐ��̌^
     * @param definitionUnit �錾���Ă��郆�j�b�g
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    VariableInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type, final TUnit definitionUnit, final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == modifiers) || (null == name) || (null == definitionUnit)) {
            throw new NullPointerException();
        }
        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
        this.definitionUnit = definitionUnit;
    }

    /**
     * �C��q��ۑ����邽�߂̕ϐ�
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * �ϐ�����\���ϐ�
     */
    private final String name;

    /**
     * �ϐ��̌^��\���ϐ�
     */
    private TypeInfo type;

    /**
     * ���̕ϐ���錾���Ă��郆�j�b�g��ۑ����邽�߂̕ϐ�
     */
    private final TUnit definitionUnit;

    /**
     * ��̕ϐ���Set��\��
     */
    public static final Set<VariableInfo<? extends UnitInfo>> EmptySet = Collections.unmodifiableSet(new HashSet<VariableInfo<? extends UnitInfo>>());
}
