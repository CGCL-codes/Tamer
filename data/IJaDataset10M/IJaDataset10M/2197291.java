package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * ���\�b�h�����Ǘ�����N���X�D methodInfo ��v�f�Ƃ��Ď��D
 * 
 * @author higo
 * 
 */
public final class MethodInfoManager {

    /**
     * 
     * @param methodInfo �ǉ����郁�\�b�h���
     */
    public void add(final TargetMethodInfo methodInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }
        this.targetMethodInfos.add(methodInfo);
    }

    /**
     * 
     * @param methodInfo �ǉ����郁�\�b�h���
     */
    public void add(final ExternalMethodInfo methodInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }
        this.externalMethodInfos.add(methodInfo);
    }

    /**
     * 
     * @param constructorInfo �ǉ�����R���X�g���N�^���
     */
    public void add(final TargetConstructorInfo constructorInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == constructorInfo) {
            throw new NullPointerException();
        }
        this.targetConstructorInfos.add(constructorInfo);
    }

    /**
     * 
     * @param constructorInfo �ǉ�����R���X�g���N�^���
     */
    public void add(final ExternalConstructorInfo constructorInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == constructorInfo) {
            throw new NullPointerException();
        }
        this.externalConstructorInfos.add(constructorInfo);
    }

    /**
     * �Ώۃ��\�b�h����SortedSet��Ԃ��D
     * 
     * @return �Ώۃ��\�b�h����SortedSet
     */
    public SortedSet<TargetMethodInfo> getTargetMethodInfos() {
        return Collections.unmodifiableSortedSet(this.targetMethodInfos);
    }

    /**
     * �O�����\�b�h����SortedSet��Ԃ��D
     * 
     * @return �O�����\�b�h����SortedSet
     */
    public SortedSet<ExternalMethodInfo> getExternalMethodInfos() {
        return Collections.unmodifiableSortedSet(this.externalMethodInfos);
    }

    /**
     * �����Ă���Ώۃ��\�b�h���̌���Ԃ�.
     * 
     * @return �Ώۃ��\�b�h�̌�
     */
    public int getTargetMethodCount() {
        return this.targetMethodInfos.size();
    }

    /**
     * �����Ă���O�����\�b�h���̌���Ԃ�.
     * 
     * @return �O�����\�b�h�̌�
     */
    public int getExternalMethodCount() {
        return this.externalMethodInfos.size();
    }

    /**
     * �ΏۃR���X�g���N�^����SortedSet��Ԃ��D
     * 
     * @return �ΏۃR���X�g���N�^����SortedSet
     */
    public SortedSet<TargetConstructorInfo> getTargetConstructorInfos() {
        return Collections.unmodifiableSortedSet(this.targetConstructorInfos);
    }

    /**
     * �O���R���X�g���N�^����SortedSet��Ԃ��D
     * 
     * @return �O���R���X�g���N�^����SortedSet
     */
    public SortedSet<ExternalConstructorInfo> getExternalConstructorInfos() {
        return Collections.unmodifiableSortedSet(this.externalConstructorInfos);
    }

    /**
     * �ΏۃR���X�g���N�^���̌���Ԃ�.
     * 
     * @return �ΏۃR���X�g���N�^�̌�
     */
    public int getTargetConstructorCount() {
        return this.targetConstructorInfos.size();
    }

    /**
     * �O���R���X�g���N�^���̌���Ԃ�.
     * 
     * @return �O���R���X�g���N�^�̌�
     */
    public int getExternalConstructorCount() {
        return this.externalConstructorInfos.size();
    }

    /**
     * ���\�b�h�����N���A
     */
    public void clear() {
        this.targetMethodInfos.clear();
        this.targetConstructorInfos.clear();
        this.externalMethodInfos.clear();
        this.externalConstructorInfos.clear();
    }

    /**
     * 
     * �R���X�g���N�^�D 
     */
    public MethodInfoManager() {
        this.targetMethodInfos = new TreeSet<TargetMethodInfo>();
        this.externalMethodInfos = new TreeSet<ExternalMethodInfo>();
        this.targetConstructorInfos = new TreeSet<TargetConstructorInfo>();
        this.externalConstructorInfos = new TreeSet<ExternalConstructorInfo>();
    }

    /**
     * 
     * �Ώۃ��\�b�h�����i�[����ϐ��D
     */
    private final SortedSet<TargetMethodInfo> targetMethodInfos;

    /**
     * 
     * �O�����\�b�h�����i�[����ϐ��D
     */
    private final SortedSet<ExternalMethodInfo> externalMethodInfos;

    /**
     * �ΏۃR���X�g���N�^�����i�[����ϐ�
     */
    private final SortedSet<TargetConstructorInfo> targetConstructorInfos;

    /**
     * �O���R���X�g���N�^�����i�[����ϐ�
     */
    private final SortedSet<ExternalConstructorInfo> externalConstructorInfos;
}
