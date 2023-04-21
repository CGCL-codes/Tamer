package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * �������z��^��\�����߂̃N���X�D�ȉ��̏������D
 * <ul>
 * <li>�������^ (UnresolvedTypeInfo)</li>
 * <li>���� (int)</li>
 * </ul>
 * 
 * @author higo
 * @see UnresolvedTypeInfo
 */
public class UnresolvedArrayTypeInfo implements UnresolvedReferenceTypeInfo<ArrayTypeInfo> {

    /**
     * ���̖������z��g�p�������ς݂��ǂ����Ԃ�
     * 
     * @return �����ς݂̏ꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ςݔz��^��Ԃ�
     * 
     * @return �����ςݔz��^
     * @throws NotResolvedException �������̏ꍇ�ɃX���[�����
     */
    @Override
    public final ArrayTypeInfo getResolved() {
        return this.resolvedInfo;
    }

    /**
     * �������z��^���������C�����ςݔz��^��Ԃ��D
     * 
     * @param usingClass �������z��^�����݂���N���X
     * @param usingMethod �������z��^�����݂��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ςݔz��^
     */
    @Override
    public ArrayTypeInfo resolve(final TargetClassInfo usingClass, final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }
        if (this.alreadyResolved()) {
            return this.getResolved();
        }
        final UnresolvedTypeInfo<?> unresolvedElementType = this.getElementType();
        final int dimension = this.getDimension();
        final TypeInfo elementType = unresolvedElementType.resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert elementType != null : "resolveEntityUsage returned null!";
        if (elementType instanceof UnknownTypeInfo) {
            this.resolvedInfo = ArrayTypeInfo.getType(UnknownTypeInfo.getInstance(), dimension);
            return this.resolvedInfo;
        } else {
            this.resolvedInfo = ArrayTypeInfo.getType(elementType, dimension);
            return this.resolvedInfo;
        }
    }

    /**
     * �z��̗v�f�̖������^��Ԃ�
     * 
     * @return �z��̗v�f�̖������^
     */
    public final UnresolvedTypeInfo<? extends TypeInfo> getElementType() {
        return this.type;
    }

    /**
     * �z��̎�����Ԃ�
     * 
     * @return �z��̎���
     */
    public final int getDimension() {
        return this.dimension;
    }

    public final String getTypeName() {
        final StringBuilder text = new StringBuilder();
        text.append(this.getElementType().getTypeName());
        for (int i = 0; i < this.getDimension(); i++) {
            text.append("[]");
        }
        return text.toString();
    }

    /**
     * ���̃C���X�^���X���\���z��̎�����1�傫�������z���\���C���X�^���X��Ԃ��D
     * 
     * @return ���̃C���X�^���X���\���z��̎�����1�傫�������z��
     */
    public final UnresolvedArrayTypeInfo getDimensionInclementedArrayType() {
        return getType(getElementType(), getDimension() + 1);
    }

    /**
     * UnresolvedArrayTypeInfo �̃C���X�^���X��Ԃ����߂̃t�@�N�g�����\�b�h�D
     * 
     * @param type �������^��\���ϐ�
     * @param dimension ������\���ϐ�
     * @return �������� UnresolvedArrayTypeInfo �I�u�W�F�N�g
     */
    public static UnresolvedArrayTypeInfo getType(final UnresolvedTypeInfo<? extends TypeInfo> type, final int dimension) {
        if (null == type) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }
        final Key key = new Key(type, dimension);
        UnresolvedArrayTypeInfo arrayUsage = ARRAY_TYPE_MAP.get(key);
        if (arrayUsage == null) {
            arrayUsage = new UnresolvedArrayTypeInfo(type, dimension);
            ARRAY_TYPE_MAP.put(key, arrayUsage);
        }
        return arrayUsage;
    }

    /**
     * �������z��^�I�u�W�F�N�g�̏�����s���D�z��̗v�f�̖������^�Ɣz��̎������^�����Ȃ���΂Ȃ�Ȃ�
     * 
     * @param type �z��̗v�f�̖������^
     * @param dimension �z��̎���
     */
    UnresolvedArrayTypeInfo(final UnresolvedTypeInfo<?> type, final int dimension) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }
        if (1 > dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }
        this.type = type;
        this.dimension = dimension;
        this.resolvedInfo = null;
    }

    /**
     * �z��̗v�f�̌^��ۑ�����ϐ�
     */
    private final UnresolvedTypeInfo<?> type;

    /**
     * �z��̎�����ۑ�����ϐ�
     */
    private final int dimension;

    /**
     * �����ςݔz��g�p��ۑ����邽�߂̕ϐ�
     */
    private ArrayTypeInfo resolvedInfo;

    /**
     * UnresolvedArrayTypeInfo �I�u�W�F�N�g���ꌳ�Ǘ����邽�߂� Map�D�I�u�W�F�N�g�̓t�@�N�g�����\�b�h�Ő��������D
     */
    private static final ConcurrentMap<Key, UnresolvedArrayTypeInfo> ARRAY_TYPE_MAP = new ConcurrentHashMap<Key, UnresolvedArrayTypeInfo>();

    /**
     * �ϐ��̌^�Ǝ�����p���ăL�[�ƂȂ�N���X�D
     * 
     * @author higo
     */
    static class Key {

        /**
         * ���L�[
         */
        private final UnresolvedTypeInfo<?> type;

        /**
         * ���L�[
         */
        private final int dimension;

        /**
         * ���C���L�[����C�L�[�I�u�W�F�N�g�𐶐�����
         * 
         * @param type ���L�[
         * @param dimension ���L�[
         */
        Key(final UnresolvedTypeInfo<?> type, final int dimension) {
            if (null == type) {
                throw new NullPointerException();
            }
            if (1 > dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }
            this.type = type;
            this.dimension = dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�̃n�b�V���R�[�h��Ԃ��D
         */
        @Override
        public int hashCode() {
            return this.type.hashCode() + this.dimension;
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public UnresolvedTypeInfo<?> getFirstKey() {
            return this.type;
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public int getSecondKey() {
            return this.dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�ƈ�Ŏw�肳�ꂽ�I�u�W�F�N�g������������Ԃ��D
         */
        @Override
        public boolean equals(Object o) {
            if (null == o) {
                throw new NullPointerException();
            }
            if (!(o instanceof Key)) {
                return false;
            }
            final UnresolvedTypeInfo<?> firstKey = this.getFirstKey();
            final UnresolvedTypeInfo<?> correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            }
            final int secondKey = this.getSecondKey();
            final int correspondSecondKey = ((Key) o).getSecondKey();
            return secondKey == correspondSecondKey;
        }
    }
}
