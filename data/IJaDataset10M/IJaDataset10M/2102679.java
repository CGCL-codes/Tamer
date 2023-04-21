package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * �C���i�[�N���X��\���N���X�D
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class TargetInnerClassInfo extends TargetClassInfo implements InnerClassInfo {

    public static ClassInfo getOutestClass(final InnerClassInfo innerClass) {
        if (null == innerClass) {
            throw new IllegalArgumentException();
        }
        ClassInfo outerClass = innerClass.getOuterClass();
        while (outerClass instanceof InnerClassInfo) {
            outerClass = ((InnerClassInfo) outerClass).getOuterClass();
        }
        return outerClass;
    }

    /**
     * �C���i�[�N���X�I�u�W�F�N�g��������
     * 
     * @param modifiers �C��q���� Set
     * @param namespace ���O���
     * @param className �N���X��
     * @param isInterface �C���^�t�F�[�X���ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace, final String className, final boolean isInterface, final boolean isEnum, final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, namespace, className, isInterface, isEnum, fileInfo, fromLine, fromColumn, toLine, toColumn);
        this.outerUnit = null;
    }

    /**
     * �C���i�[�N���X�I�u�W�F�N�g��������
     * 
     * @param modifiers �C��q���� Set
     * @param fullQualifiedName ���S���薼
     * @param isInterface �C���^�t�F�[�X���ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName, final boolean isInterface, final boolean isEnum, final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, fullQualifiedName, isInterface, isEnum, fileInfo, fromLine, fromColumn, toLine, toColumn);
        this.outerUnit = null;
    }

    /**
     * �O���̃��j�b�g��Ԃ�
     * 
     * @return �O���̃��j�b�g
     */
    @Override
    public UnitInfo getOuterUnit() {
        assert null != this.outerUnit : "outerUnit is null!";
        return this.outerUnit;
    }

    /**
     * �O���̃��j�b�g��ݒ肷��
     * 
     * @param �O���̃��j�b�g
     */
    @Override
    public void setOuterUnit(final UnitInfo outerUnit) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }
        this.outerUnit = outerUnit;
    }

    /**
     * �O���̃N���X��Ԃ�.
     * 
     * @return�@�O���̃N���X
     */
    @Override
    public final ClassInfo getOuterClass() {
        UnitInfo outer = this.getOuterUnit();
        while (true) {
            if (null == outer) {
                throw new IllegalStateException();
            }
            if (outer instanceof ClassInfo) {
                return (ClassInfo) outer;
            }
            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * �O���̃��\�b�h��Ԃ�.
     * 
     * @return�@�O���̃��\�b�h
     */
    @Override
    public final CallableUnitInfo getOuterCallableUnit() {
        UnitInfo outer = this.getOuterUnit();
        while (true) {
            if (null == outer) {
                return null;
            }
            if (outer instanceof CallableUnitInfo) {
                return (CallableUnitInfo) outer;
            }
            if (!(outer instanceof HavingOuterUnit)) {
                return null;
            }
            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        return this.getOuterClass();
    }

    /**
     * �O���̃��j�b�g��ۑ�����ϐ�
     */
    private UnitInfo outerUnit;
}
